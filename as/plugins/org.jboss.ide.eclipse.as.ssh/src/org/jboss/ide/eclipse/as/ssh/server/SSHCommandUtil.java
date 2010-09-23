package org.jboss.ide.eclipse.as.ssh.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.extensions.events.IEventCodes;
import org.jboss.ide.eclipse.as.ssh.SSHDeploymentPlugin;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHCommandUtil {
	public static void launchRemoveCommand(Session session, String remoteLocation, IProgressMonitor monitor) throws CoreException {
		String command = "rm " + remoteLocation;
		launchCommand(session, command, monitor);
	}
	
	public static void launchCommand(Session session, String command, IProgressMonitor monitor) throws CoreException {
		launchThreadedCommand(session, command, monitor);
	}

	public static void launchThreadedCommand(final Session session, final String command, final IProgressMonitor monitor) throws CoreException {
		// thread and watch the monitor for cancelations and interrupt the thread
		LaunchRunnable r = new LaunchRunnable() { public void run() throws CoreException { 
			launchCommandNoThread(session, command, monitor);
		} };
		launchThreadedCommand(r, monitor);
	}
	
	public static void launchCommandNoThread(Session session, String command, IProgressMonitor monitor) throws CoreException {
		Channel channel = null;
		try {
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
	
			channel.connect();
			while(!channel.isClosed()) {
				try {Thread.sleep(300);} catch(InterruptedException ie) {}
			}
		} catch( JSchException jsche ) {
			throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, IEventCodes.SSH_PUBLISHING_ROOT_CODE, "Error executing command: " + command, null));
		} finally {
			channel.disconnect();
		}
	}
	
	public static class LaunchRunnable {
		public void run() throws CoreException {
		}
	}
	
	public static void launchCopyCommand(final Session session, final String localFile, 
						final String remoteFile, final IProgressMonitor monitor) throws CoreException {
		// thread and watch the monitor for cancelations and interrupt the thread
		LaunchRunnable r = new LaunchRunnable() { public void run() throws CoreException { 
			launchCopyCommandImpl(session, localFile, remoteFile, monitor);
		} };
		launchThreadedCommand(r, monitor);
	}
	
	public static void launchThreadedCommand(final LaunchRunnable runnable, IProgressMonitor monitor) throws CoreException {
		final Exception[] e = new Exception[1];
		e[0] = null;
		final Object waitObject = new Object();
		final Boolean[] subtaskComplete = new Boolean[1];
		subtaskComplete[0] = new Boolean(false);
		Thread t = new Thread() {
			public void run() {
				Exception exception = null;
				try {
					runnable.run();
				} catch( Exception ex ) {
					exception = ex;
				}
				synchronized(waitObject) {
					e[0] = exception;
					subtaskComplete[0] = new Boolean(true);
					waitObject.notifyAll();
				}
			}
		};
		t.start();
		while(t.isAlive() && !monitor.isCanceled() ) {
			synchronized(waitObject) {
				if( subtaskComplete[0].booleanValue() )
					break;
				try {
					waitObject.wait(500);
				} catch(InterruptedException ie) {}
			}
		}
		synchronized(waitObject) {
			if( !subtaskComplete[0].booleanValue()) {
				t.interrupt();
				IStatus status = new Status(IStatus.WARNING, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.ISTATUS_CODE_ERROR, "SSH command canceled", e[0]);
				CoreException ce = new CoreException(status);
				throw ce;
			}
			if( e[0] != null ) {
				IStatus status = new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.ISTATUS_CODE_ERROR, "Error running remote command", e[0]);
				CoreException ce = new CoreException(status);
				throw ce;
			}
				
		}
	}
	
	public static void launchCopyCommandImpl(Session session, String localFile, String remoteFile, IProgressMonitor monitor) throws CoreException {
		Channel channel = null;
		OutputStream out = null;
		try {
			// exec 'scp -t rfile' remotely
			String command = "scp -p -t " + remoteFile;
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
	
			// get I/O streams for remote scp
			out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			channel.connect();
			if (checkAck(in) != 0) {
				throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, IEventCodes.SSH_PUBLISHING_ROOT_CODE, "Error transfering file: " + localFile, null));
			}
			
			// send "C0644 filesize filename", where filename should not include
			// '/'
			long filesize = (new File(localFile)).length();
			command = "C0644 " + filesize + " ";
			if (localFile.lastIndexOf('/') > 0) {
				command += localFile.substring(localFile.lastIndexOf('/') + 1);
			} else {
				command += localFile;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, IEventCodes.SSH_PUBLISHING_ROOT_CODE, "Error transfering file: " + localFile, null));
			}

			// send a content of lfile
			FileInputStream fis = new FileInputStream(localFile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, IEventCodes.SSH_PUBLISHING_ROOT_CODE, "Error transfering file: " + localFile, null));
			}

		} catch( JSchException jsche ) {
			throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, IEventCodes.SSH_PUBLISHING_ROOT_CODE, "Error transfering file: " + localFile, jsche));
		} catch( IOException ioe) {
			throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, IEventCodes.SSH_PUBLISHING_ROOT_CODE, "Error transfering file: " + localFile, ioe));
		} finally {
			if( channel != null )
				channel.disconnect();
			if( out != null ) {
				try {
					out.close();
				} catch(IOException ioe) {}
			}
		}
	}

	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
//			if (b == 1) { // error
//				System.out.print(sb.toString());
//			}
//			if (b == 2) { // fatal error
//				System.out.print(sb.toString());
//			}
		}
		return b;
	}

}

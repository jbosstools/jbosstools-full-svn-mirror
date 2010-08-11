/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.ssh.server;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.Server;
import org.jboss.ide.eclipse.as.core.ExtensionManager;
import org.jboss.ide.eclipse.as.core.publishers.LocalPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublishMethodType;
import org.jboss.ide.eclipse.as.core.server.internal.DeployableServerBehavior;
import org.jboss.ide.eclipse.as.core.server.xpl.PublishCopyUtil.IPublishCopyCallbackHandler;
import org.jboss.ide.eclipse.as.ssh.SSHDeploymentPlugin;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSHServerBehaviourDelegate extends DeployableServerBehavior {

	public SSHServerBehaviourDelegate() {
		super();
	}

	@Override
	public void stop(boolean force) {
		setServerState(IServer.STATE_STOPPED);
	}
	
	@Override
	public IJBossServerPublishMethod createPublishMethod() {
		return new SSHPublishMethod(); // TODO FIX THIS in superclass
	}
	
	public class SSHPublishMethod extends LocalPublishMethod {
		public static final String SSH_PUBLISH_METHOD = "ssh";  //$NON-NLS-1$
		private Session session;
		public Session getSession() {
			return session;
		}
		@Override
		public IJBossServerPublishMethodType getPublishMethodType() {
			return ExtensionManager.getDefault().getPublishMethod(SSH_PUBLISH_METHOD);
		}
		@Override
		public void publishStart(DeployableServerBehavior behaviour,
				IProgressMonitor monitor) throws CoreException {
			
			try {
				ServerUserInfo info = new ServerUserInfo(getServer());
				JSch jsch = new JSch();
				session = jsch.getSession(info.getUser(), behaviour.getServer().getHost(), 22);
				jsch.setKnownHosts(info.getHostsFile());
				session.setUserInfo(info);
				session.connect();
			} catch( JSchException jsche) {
				throw new CoreException(new Status(IStatus.ERROR, SSHDeploymentPlugin.PLUGIN_ID, "Remote Authentication Error", jsche));
			}
		}
		
		@Override
		public int publishFinish(DeployableServerBehavior behaviour,
				IProgressMonitor monitor) throws CoreException {
			int ret = super.publishFinish(behaviour, monitor);
			if( session != null )
				session.disconnect();
			session = null;
			return ret;
		}
		
		public String getPublishDefaultRootFolder(IServer server) {
			return ((Server)server).getAttribute(ISSHDeploymentConstants.DEPLOY_DIRECTORY, (String)null);
		}
	}
	
	
	public static class ServerUserInfo implements UserInfo {
		private String user;
		private String password;
		private String hostsFile;
		public ServerUserInfo(IServer server) {
			IServer tmp = ServerCore.findServer(server.getId());
			String tmp_pass = SSHPublishUtil.getPass(tmp);
			user = SSHPublishUtil.getUser(server);
			password = SSHPublishUtil.getPass(server);
			hostsFile = SSHPublishUtil.getHostsFile(server);
		}
		public String getPassword() {
			return password;
		}

		public String getUser() {
			return user;
		}
		
		public String getHostsFile() {
			return hostsFile;
		}
		
		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
			// TODO eh?
		}

		public boolean promptYesNo(String message) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public IPublishCopyCallbackHandler getCallbackHandler(IPath path,
			IServer server, IJBossServerPublishMethod method) {
		return new SSHCopyCallback(path, (SSHPublishMethod)method);
	}

	public String getPublishDefaultRootFolder(IServer server) {
		return getPublishDefaultRootFolder(server);
	}


	
}

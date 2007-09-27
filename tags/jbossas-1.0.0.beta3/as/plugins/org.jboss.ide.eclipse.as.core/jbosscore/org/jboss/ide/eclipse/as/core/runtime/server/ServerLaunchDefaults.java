/**
 * JBoss, a Division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
* This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.as.core.runtime.server;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.runtime.IJBossServerLaunchDefaults;
import org.jboss.ide.eclipse.as.core.runtime.IJBossServerRuntime;
import org.jboss.ide.eclipse.as.core.server.JBossServer;
import org.jboss.ide.eclipse.as.core.server.JBossServerLaunchConfiguration;

/**
 * A class of launch defaults including how to start, stop, and twiddle 
 * a server.
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 *
 */
public class ServerLaunchDefaults implements IJBossServerLaunchDefaults {

	/*
	 * Can be overwritten by subclasses 
	 */
	protected String startMainType = "org.jboss.Main";
	protected String stopMainType = "org.jboss.Shutdown";
	protected String twiddleMainType = "org.jboss.console.twiddle.Twiddle";
	
	protected String defaultShutdownArgs = "-S";
	protected String defaultVMArgs = "";
	
	protected String runJar = "bin" + File.separator + "run.jar";
	protected String shutdownJar = "bin" + File.separator + "shutdown.jar";
	protected String twiddleJar = "bin" + File.separator + "twiddle.jar";


	
	protected IServer server;
	protected IJBossServerRuntime runtime;
	protected JBossServer jbServer;
	
	public ServerLaunchDefaults(IServer server) {
		this.server = server;
	}
	
	protected JBossServer getJBServer() {
		if( jbServer == null ) {
			try {
				jbServer = (JBossServer)server.loadAdapter(JBossServer.class, new NullProgressMonitor());
			} catch( Exception e) {
			}
		}
		return jbServer;
	}
	protected IJBossServerRuntime getRuntime() {
		if( runtime == null ) {
			try {
				runtime = (IJBossServerRuntime) server.getRuntime()
						.loadAdapter(IJBossServerRuntime.class, new NullProgressMonitor());
			} catch( Exception e ) {
			}
		}
		return runtime;
	}
	
	public void fillDefaults(ILaunchConfigurationWorkingCopy workingCopy) throws CoreException {
		IJBossServerRuntime runtime = getRuntime();
		String argsKey = IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS;
		String vmArgsKey = IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS;
		
		String START_SUFFIX = JBossServerLaunchConfiguration.PRGM_ARGS_START_SUFFIX;
		String STOP_SUFFIX = JBossServerLaunchConfiguration.PRGM_ARGS_STOP_SUFFIX;
		String TWIDDLE_SUFFIX = JBossServerLaunchConfiguration.PRGM_ARGS_TWIDDLE_SUFFIX;
		
		
		
		// START items
		workingCopy.setAttribute(argsKey + START_SUFFIX, getStartArgs());
		workingCopy.setAttribute(vmArgsKey + START_SUFFIX,  getVMArgs());

		// STOP items
		workingCopy.setAttribute(argsKey + STOP_SUFFIX, getStopArgs());
		workingCopy.setAttribute(vmArgsKey + STOP_SUFFIX,  getVMArgs());


		int jndiPort = getJBServer().getJNDIPort();
		String host = server.getHost();
		String twiddleArgs = "-s " + host + ":" + jndiPort +  " -a jmx/rmi/RMIAdaptor ";

		workingCopy.setAttribute(argsKey + TWIDDLE_SUFFIX, twiddleArgs);
		workingCopy.setAttribute(vmArgsKey + TWIDDLE_SUFFIX,  "");
		
		
		
		/* Now add in the main types */
		String mainKey = IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME;
		workingCopy.setAttribute(mainKey + START_SUFFIX, getStartMainType());
		workingCopy.setAttribute(mainKey + STOP_SUFFIX, getStopMainType());
		workingCopy.setAttribute(mainKey + TWIDDLE_SUFFIX, twiddleMainType);
		
		String wdKey =  IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY;
		String wdVal = getServerHome() + Path.SEPARATOR + "bin";
		workingCopy.setAttribute( wdKey + START_SUFFIX, wdVal);
		workingCopy.setAttribute( wdKey + STOP_SUFFIX, wdVal);
		workingCopy.setAttribute( wdKey + TWIDDLE_SUFFIX, wdVal);
        
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
		String cpKey = IJavaLaunchConfigurationConstants.ATTR_CLASSPATH;
		workingCopy.setAttribute(cpKey + START_SUFFIX, getRuntimeClasspath(JBossServerLaunchConfiguration.START));
		workingCopy.setAttribute(cpKey + STOP_SUFFIX, getRuntimeClasspath(JBossServerLaunchConfiguration.STOP));
		workingCopy.setAttribute(cpKey + TWIDDLE_SUFFIX, getRuntimeClasspath(JBossServerLaunchConfiguration.TWIDDLE));
	}


	public String getStartArgs() {
		IJBossServerRuntime rt = getRuntime();
		if( rt != null ) {
			return "--configuration=" + rt.getJBossConfiguration();
		}
		return "";
	}

	
	public String getServerHome() {
		return server.getRuntime().getLocation().toOSString();
	}
	
	public String getConfigurationPath() {
		try {	
			return getServerHome() + Path.SEPARATOR + "server" + Path.SEPARATOR + getRuntime().getJBossConfiguration();
		} catch( Exception e ) {
		}
		return "";
	}
	
	public String getStartJar() {
		return getServerHome() + Path.SEPARATOR + runJar;
	}

	public String getShutdownJar() {
		return getServerHome() + Path.SEPARATOR + shutdownJar;
	}

	public String getTwiddleJar() {
		return getServerHome() + Path.SEPARATOR + twiddleJar;
	}

	public String getStartMainType() {
		return startMainType;
	}

	public String getStopMainType() {
		return stopMainType;
	}
	
	public String getTwiddleMainType() {
		return twiddleMainType;
	}

	public String getStopArgs() {
		return defaultShutdownArgs;
	}

	public String getVMArgs() {
		return defaultVMArgs;
	}


	public List getRuntimeClasspath(String action) {
		String serverHome = getServerHome();
		ArrayList classpath = new ArrayList();
		
		if( action.equals(JBossServerLaunchConfiguration.START)) {
			classpath.add(JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(getStartJar())));
		} else if( action.equals(JBossServerLaunchConfiguration.STOP))  {
			classpath.add(JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(getShutdownJar())));
		} else if( action.equals(JBossServerLaunchConfiguration.TWIDDLE)) {
			
			// Twiddle requires more classes and I'm too lazy to actually figure OUT which ones it needs.
			classpath.add(JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(getTwiddleJar())));
			addDirectory (serverHome, classpath, "lib");
			addDirectory (serverHome, classpath, "lib" + File.separator + "endorsed");
			addDirectory (serverHome, classpath, "client");
			addDirectory (getConfigurationPath(), classpath, "lib"); 
		}
				
		ArrayList runtimeClassPaths = convertClasspath(classpath, runtime.getVM());
		return runtimeClassPaths;
	}
	
	private ArrayList convertClasspath(ArrayList cp, IVMInstall vmInstall) {
		if (vmInstall != null) {
			try {
				cp.add(JavaRuntime.newRuntimeContainerClasspathEntry(
					new Path(JavaRuntime.JRE_CONTAINER)
						.append("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType")
						.append(vmInstall.getName()), IRuntimeClasspathEntry.BOOTSTRAP_CLASSES));
			} catch (Exception e) {
				// ignore
			}			
			
			IPath jrePath = new Path(vmInstall.getInstallLocation().getAbsolutePath());
			if (jrePath != null) {
				IPath toolsPath = jrePath.append("lib").append("tools.jar");
				if (toolsPath.toFile().exists()) {
					cp.add(JavaRuntime.newArchiveRuntimeClasspathEntry(toolsPath));
				}
			}
		}
		
		Iterator cpi = cp.iterator();
		ArrayList list = new ArrayList();
		while (cpi.hasNext()) {
			IRuntimeClasspathEntry entry = (IRuntimeClasspathEntry) cpi.next();
			try {
				list.add(entry.getMemento());
			} catch (Exception e) {
				//Trace.trace(Trace.SEVERE, "Could not resolve classpath entry: " + entry, e);
			}
		}

		return list;
	}
	
	private void addDirectory (String serverHome, ArrayList classpath, String dirName) {
		String libPath = serverHome + File.separator + dirName;
		File libDir = new File(libPath);
		File libs[] = libDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name)
			{
				return (name != null && name.endsWith("jar"));
			}
		});
		
		if( libs == null ) return;
		
		for (int i = 0; i < libs.length; i++)
		{
			classpath.add(JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(libPath + File.separator + libs[i].getName())));
		}
	} // end method



}

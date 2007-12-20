/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.server;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.componentcore.internal.util.ComponentUtilities;
import org.eclipse.wst.server.core.*;
import org.eclipse.wst.server.core.internal.ServerType;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class RegistrationHelper {
	
	public static boolean isRegistered(String appname, IServer server) {
		IProject project = null;
		if(server != null) {
			IModule[] ms = server.getModules();
			for (int i = 0; i < ms.length && project == null; i++) {
				if(ms[i].getName().equals(appname)) {
					IProject p = ms[i].getProject();
					if(p != null && p.exists() && p.isOpen()) {
						project = p;
					}
				}
			}
		}
		return isRegistered(project, server);
	}

	public static boolean isRegistered(IProject project) {
		return isRegistered(project, ServerManager.getInstance().getSelectedServer());
	}
	
	public static boolean isRegistered(IProject project, IServer server) {
		if(server == null || project == null) return false;
		IModule[] ms = server.getModules();
		IModule m = findModule(project);
		return (contains(ms, m));
	}


	public static boolean canRegister(IProject project) {
		return (getSelectedServer() != null) && !isRegistered(project);
	}

	public static boolean canRegister(IProject project, IServer server) {
		return (server != null) && !isRegistered(project, server);
	}

	public static String getRegistrationError(IProject project, String appname, IServer server) {
		if(server == null) return WebUIMessages.SERVER_ISNOT_SELECTED;
		String contextRootError = checkContextRoot(appname);
		if(contextRootError != null) return contextRootError;
		IModule m = findModule(project);
		return getRegistrationError(m, appname, server);
	}
	
	static String FORBIDDEN = ";/?:@&=+,$,\\";
	public static String checkContextRoot(String appname) {
		if(appname == null) return null;		
		for (int i = 0; i < appname.length(); i++) {
			char c = appname.charAt(i);
			if(FORBIDDEN.indexOf(c) >= 0) return NLS.bind(WebUIMessages.CONTEXT_ROOT_CANNOT_CONTAIN_CHARACTER, "" + c);
		}
		return null;
	}

	public static String getRegistrationError(IModule m, String appname, IServer server) {
		if(server == null) return WebUIMessages.SERVER_ISNOT_SELECTED;
		if(m == null) return WebUIMessages.CANNOT_FIND_MODULE_FOR_THE_PROJECT;
		if(contains(server.getModules(), m)) {
			return NLS.bind(WebUIMessages.APPLICATION_IS_ALREADY_REGISTERED, appname, server.getName());
		}
		IModule[] add = new IModule[]{m};
		IModule[] remove = new IModule[0];
		try {
			try {
				server.getRootModules(m, null);
			} catch (CoreException ce) {
				return ce.getStatus().getMessage();
			}

			IProgressMonitor monitor = new NullProgressMonitor();
			IServerWorkingCopy copy = server.createWorkingCopy();
			IStatus status = copy.canModifyModules(add, remove, monitor);
			if(status != null && !status.isOK()) return status.getMessage();
			return null;
		} catch (Exception e) {
			//ignore
			return WebUIMessages.CANNOT_REGISTER_IN_THIS_SERVER;
		}
	}
	
	public static void register(IProject project) {
		register(project, ServerManager.getInstance().getSelectedServer());
	}

	public static void register(IProject project, IServer server) {
		if(server == null) return;
		IModule m = findModule(project);
		if(m == null) return;
		if(contains(server.getModules(), m)) return;
		IModule[] add = new IModule[]{m};
		IModule[] remove = new IModule[0];
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			IServerWorkingCopy copy = server.createWorkingCopy();
			IStatus status = copy.canModifyModules(add, remove, monitor);
			if(status != null && !status.isOK()) return;
			ServerUtil.modifyModules(copy, add, remove, monitor);
			copy.save(true, monitor);
			if(canPublish(server)) {
				server.publish(IServer.PUBLISH_INCREMENTAL, monitor);
			}
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}
	
	public static boolean canPublish(IServer server) {
		if(server == null || server.getRuntime() == null) return false;
		if (((ServerType)server.getServerType()).startBeforePublish() && 
			(server.getServerState() != IServer.STATE_STARTED)) {
			return false;
		}
		return true;
	}
	
	public static boolean canUnregister(IProject project) {
		return (getSelectedServer() != null) && isRegistered(project);
	}
	
	public static boolean unregister(IProject project) {
		return unregister(project, getSelectedServer());
	}
	
	public static boolean unregister(IProject project, IServer server) {
		if(server == null) return false;
		IModule m = findModule(project);
		if(!contains(server.getModules(), m)) return false;
		IModule[] add = new IModule[0];
		IModule[] remove = new IModule[]{m};
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			IServerWorkingCopy copy = server.createWorkingCopy();
			ServerUtil.modifyModules(copy, add, remove, monitor);
			copy.save(true, monitor);
			if(canPublish(server)) {
				server.publish(IServer.PUBLISH_INCREMENTAL, monitor);
			}
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
		return true;
	}
	
	public static IServer getSelectedServer() {
		return ServerManager.getInstance().getSelectedServer();
	}
	
	private static boolean contains(IModule[] modules, IModule module) {
		if(modules == null || module == null) return false;
		for (int i = 0; i < modules.length; i++) {
			if(modules[i].getName() != null && modules[i].getName().equals(module.getName())) return true;
		}
		return false;
	}
	
	static String MODULE_NAME_PREFIX = "org.eclipse.jst.j2ee.server.web:"; //$NON-NLS-1$
	
	public static IModule findModule(IProject project) {
		return ServerUtil.getModule(project);
		
	}

	public static void runRegisterInServerJob(IProject p, IServer server) {
		runRegisterInServerJob(p, new IServer[]{server}, null);
	}

	public static void runRegisterInServerJob(final IProject p, final IServer[] servers, final String contextRoot) {
		Job startRegistration = new Job("Starting deployment to server") {
			/* (non-Javadoc)
			 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				RegisterServerJob job = new RegisterServerJob(p, servers, contextRoot);
				job.schedule(100);
				return Status.OK_STATUS;
			}
		};
		startRegistration.setUser(true);
		startRegistration.setRule(p);
		startRegistration.setPriority(Job.DECORATE);
		startRegistration.schedule();
	}
	
	// registerInJob
	
	private static class RegisterServerJob extends Job {
		long counter = 100;
		IProject p;
		IServer[] servers;
		String contextRoot;
		public RegisterServerJob(IProject p, IServer[] servers, String contextRoot) {
			super("Register in Server");
			this.p = p;
			this.servers = servers;
			this.contextRoot = contextRoot;
		}

		protected IStatus run(IProgressMonitor monitor) {
			try {
				if(RegistrationHelper.findModule(p) == null) {
					counter *= 2;
					if(counter > 10000) {
						String mesage = "Timeout expired for registering project " + p + " in server. Please check the project and try context menu action.";
						return new Status(IStatus.ERROR, "org.jboss.tools.jst.web", 0, mesage, null);
					} else {
						schedule(counter);
					}
				} else {
					ModelPlugin.getWorkspace().run(new WR(), monitor);
				}
			} catch (Exception e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
			return Status.OK_STATUS;
		}

		class WR implements IWorkspaceRunnable {
			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					register(p, servers, contextRoot, monitor);
				} catch (Exception e) {
					WebModelPlugin.getPluginLog().logError(e);
				}
			}
			
		}
	}

	private static void register(IProject p, IServer[] servers, String contextRoot, IProgressMonitor monitor) throws Exception {
		if(monitor != null) monitor.beginTask("", 100);
		if(monitor != null) monitor.worked(5);

		int step = 70;
		if(monitor != null) {
			monitor.worked(5);
			step = step / (2 * servers.length + 1);
		}
		if(contextRoot != null && !contextRoot.equals(ComponentUtilities.getServerContextRoot(p))) {
			ComponentUtilities.setServerContextRoot(p, contextRoot);
		}
		for (int i = 0; i < servers.length; i++) {
			if(monitor != null) {
				monitor.worked(step);
				monitor.subTask(servers[i].getName());
			}
			RegistrationHelper.register(p, servers[i]);
			if(monitor != null) monitor.worked(step);
		}
		
		if(contextRoot != null) {
			IModelNature n = EclipseResourceUtil.getModelNature(p);
			XModel model = n == null ? null : n.getModel();
			if(model != null) {
				XModelObject object = FileSystemsHelper.getFileSystems(model);
				if(object != null) model.changeObjectAttribute(object, "application name", contextRoot);
			}
		}
		if(monitor != null) monitor.worked(20);
	}

}

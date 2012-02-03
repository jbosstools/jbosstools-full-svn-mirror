/*******************************************************************************
 * Copyright (c) 2011 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.openshift.express.internal.core.behaviour;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.egit.core.op.PushOperationResult;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.internal.ui.wizards.ConfigureProjectWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IPublishCopyCallbackHandler;
import org.jboss.ide.eclipse.as.core.server.internal.DeployableServerBehavior;
import org.jboss.tools.openshift.egit.core.EGitUtils;
import org.jboss.tools.openshift.express.internal.ui.console.ConsoleUtils;

public class ExpressPublishMethod implements IJBossServerPublishMethod {

	private ArrayList<IProject> projectsLackingGitRepo = null;

	public ExpressPublishMethod() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void publishStart(DeployableServerBehavior behaviour,
			IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public int publishFinish(DeployableServerBehavior behaviour,
			IProgressMonitor monitor) throws CoreException {
		if( projectsLackingGitRepo != null ) {
			IProject[] projects = (IProject[]) projectsLackingGitRepo.toArray(new IProject[projectsLackingGitRepo.size()]);
			shareProjects(projects);
			projectsLackingGitRepo = null;
		}
        return areAllPublished(behaviour) ? IServer.PUBLISH_STATE_NONE : IServer.PUBLISH_STATE_INCREMENTAL;	
    }
	
	protected boolean areAllPublished(DeployableServerBehavior behaviour) {
        IModule[] modules = behaviour.getServer().getModules();
        boolean allpublished= true;
        for (int i = 0; i < modules.length; i++) {
        	if(behaviour.getServer().getModulePublishState(new IModule[]{modules[i]})!=IServer.PUBLISH_STATE_NONE)
                allpublished=false;
        }
        return allpublished;
	}

	@Override
	public int publishModule(DeployableServerBehavior behaviour, int kind,
			int deltaKind, IModule[] module, IProgressMonitor monitor)
			throws CoreException {
		
		// If this action is not user-initiated, bail!
		IAdaptable a = ((ExpressBehaviour)behaviour).getPublishAdaptableInfo();
		if( a == null )
			return -1;
		String s = (String)a.getAdapter(String.class);
		if( s == null || !s.equals("user"))
			return -1;
		
		
		int state = behaviour.getServer().getModulePublishState(module);
		IProject p = module[module.length-1].getProject();
		
		if( deltaKind == ServerBehaviourDelegate.REMOVED)
			return IServer.PUBLISH_STATE_NONE;  // go ahead and remove it
		
		Repository repository = EGitUtils.getRepository(p);
		if (repository==null) {
			if( projectsLackingGitRepo == null )
				projectsLackingGitRepo = new ArrayList<IProject>();
			projectsLackingGitRepo.add(p);
			return IServer.PUBLISH_STATE_UNKNOWN;
		}
		
		commitAndPushProject(p, behaviour, monitor);
		
		return IServer.PUBLISH_STATE_NONE;
	}

	protected PushOperationResult commitAndPushProject(IProject p,
			DeployableServerBehavior behaviour, IProgressMonitor monitor) throws CoreException {
		Repository repository = EGitUtils.getRepository(p);

		int changed = EGitUtils.countCommitableChanges(p, behaviour.getServer(), new NullProgressMonitor() );
		String remoteName = behaviour.getServer().getAttribute(ExpressServerUtils.ATTRIBUTE_REMOTE_NAME, 
				ExpressServerUtils.ATTRIBUTE_REMOTE_NAME_DEFAULT);
		PushOperationResult result = null;
		boolean committed = false;
		try {
			if( changed != 0 && requestCommitAndPushApproval(p.getName(), changed)) {
				monitor.beginTask("Publishing " + p.getName(), 300);
				EGitUtils.commit(p, new SubProgressMonitor(monitor, 100));
				committed = true;
			} 
			
			if( committed || (changed == 0 && requestPushApproval(p.getName()))) {
				if( !committed )
					monitor.beginTask("Publishing " + p.getName(), 200);
				result = EGitUtils.push(remoteName, EGitUtils.getRepository(p), new SubProgressMonitor(monitor, 100));
				monitor.done();
			}
		} catch(CoreException ce) {
			// Comes if either commit or push has failed
			try {
				result = EGitUtils.pushForce(remoteName, repository, new SubProgressMonitor(monitor, 100));
				monitor.done();
			} catch(CoreException ce2) {
				// even the push force failed, and we don't have a valid result to check :( 
				// can only throw it i guess
				throw ce2;  
			}
		}
		
		if( result != null ) {
			ConsoleUtils.appendGitPushToConsole(behaviour.getServer(), result);
		}
		return result;
	}
	
	
	private void shareProjects(final IProject[] projects) {
		Display.getDefault().asyncExec(new Runnable() { 
			public void run() {
				String msg = ExpressMessages.bind(ExpressMessages.shareProjectMessage, projects.length);
				String title = ExpressMessages.shareProjectTitle;
				boolean approved = requestApproval(msg, title);
				if( approved ) {
					ConfigureProjectWizard.shareProjects(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
							projects);
				}
			}
		});
	}
	
	protected String getModuleProjectName(IModule[] module) {
		return module[module.length-1].getProject().getName();
	}
	
	protected boolean requestCommitAndPushApproval(String projName, int changed) {
		String msg = NLS.bind(ExpressMessages.requestCommitAndPushMsg, changed, projName);
		String title = NLS.bind(ExpressMessages.requestCommitAndPushTitle, projName);
		return requestApproval(msg, title);
	}

	protected boolean requestPushApproval(String projName) {
		String msg = NLS.bind(ExpressMessages.requestPushMsg, projName);
		String title = NLS.bind(ExpressMessages.requestPushTitle, projName);
		return requestApproval(msg, title);
	}

	protected boolean requestApproval(final String message, final String title) {
		final boolean[] b = new boolean[1];
		Display.getDefault().syncExec(new Runnable() { 
			public void run() {
		        b[0] = MessageDialog.openQuestion(getActiveShell(), title, message);
			}
		});
		return b[0];
	}
	
	protected static Shell getActiveShell() {
		Display display = Display.getDefault();
		final Shell[] ret = new Shell[1];
		display.syncExec(new Runnable() {
			public void run() {
				ret[0] = Display.getCurrent().getActiveShell();
			}
		});
		return ret[0];
	}
	
	@Override
	public IPublishCopyCallbackHandler getCallbackHandler(IPath path,
			IServer server) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPublishDefaultRootFolder(IServer server) {
		// TODO Auto-generated method stub
		return null;
	}

}

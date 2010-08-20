package org.jboss.tools.bpel.runtimes.ui.view.server;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.view.servers.ModuleServer;
import org.jboss.tools.bpel.runtimes.module.JBTBPELPublisher;
import org.jboss.tools.bpel.runtimes.ui.view.server.BPELModuleContentProvider.BPELVersionDeployment;

public class BPELModuleActionProvider extends CommonActionProvider {

	private ICommonActionExtensionSite actionSite;
	private Action undeployVersionAction;
	private IStructuredSelection lastSelection;
	public BPELModuleActionProvider() {
		super();
	}

    public void dispose() {
    	super.dispose();
    }

	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		this.actionSite = aSite;
		createActions(aSite);
	}

	protected void createActions(ICommonActionExtensionSite aSite) {
		ICommonViewerSite site = aSite.getViewSite();
		if( site instanceof ICommonViewerWorkbenchSite ) {
			StructuredViewer v = aSite.getStructuredViewer();
			if( v instanceof CommonViewer ) {
				CommonViewer cv = (CommonViewer)v;
				ICommonViewerWorkbenchSite wsSite = (ICommonViewerWorkbenchSite)site;
				undeployVersionAction = new Action() {
					public void run() {
						runUndeployVersion();
						refreshViewer(getLastServer());
					}
				};
				undeployVersionAction.setText("Undeploy Version");
				undeployVersionAction.setDescription("Undeploy this version of the module");
				//undeployVersionAction.setImageDescriptor(JBossServerUISharedImages.getImageDescriptor(JBossServerUISharedImages.PUBLISH_IMAGE));
			}
		}
	}

	protected void runUndeployVersion() {
		Object firstSel = lastSelection.getFirstElement();
		if( firstSel instanceof BPELVersionDeployment ) {
			BPELVersionDeployment deployment = (BPELVersionDeployment)firstSel;
			JBTBPELPublisher.removeVersion(deployment.getModuleServer().server, 
					deployment.getProject(), deployment.getPath());
		}
	}
	
	protected IServer getLastServer() {
		Object firstSel = lastSelection.getFirstElement();
		if( firstSel instanceof IServer ) 
			return (IServer)firstSel;
		if( firstSel instanceof ModuleServer )
			return ((ModuleServer)firstSel).getServer();
		if( firstSel instanceof BPELVersionDeployment ) 
			return ((BPELVersionDeployment)firstSel).getModuleServer().getServer();
		return null;
	}
	
	protected void refreshViewer(Object o) {
		actionSite.getStructuredViewer().refresh(o);
	}

	
	public void fillContextMenu(IMenuManager menu) {
		lastSelection = getSelection();
		if( lastSelection.size() == 1 ) {
			Object sel = lastSelection.getFirstElement();
			if( sel instanceof BPELVersionDeployment )
				menu.add(undeployVersionAction);
		}
	}
	
	public IStructuredSelection getSelection() {
		ICommonViewerSite site = actionSite.getViewSite();
		IStructuredSelection selection = null;
		if (site instanceof ICommonViewerWorkbenchSite) {
			ICommonViewerWorkbenchSite wsSite = (ICommonViewerWorkbenchSite) site;
			selection = (IStructuredSelection) wsSite.getSelectionProvider()
					.getSelection();
			return selection;
		}
		return new StructuredSelection();
	}
	
}

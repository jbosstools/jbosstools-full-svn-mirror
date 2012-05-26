package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;
import org.jboss.tools.internal.deltacloud.ui.wizards.EditCloudConnectionWizard;
import org.jboss.tools.internal.deltacloud.ui.wizards.ImageFilterWizard;
import org.jboss.tools.internal.deltacloud.ui.wizards.InstanceFilterWizard;
import org.jboss.tools.internal.deltacloud.ui.wizards.NewCloudConnectionWizard;

public class CloudViewActionProvider extends CommonActionProvider {

	private ICommonActionExtensionSite actionSite;
	private CommonViewer cv;

	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		this.actionSite = aSite;
		ICommonViewerSite site = aSite.getViewSite();
		if( site instanceof ICommonViewerWorkbenchSite ) {
			StructuredViewer v = aSite.getStructuredViewer();
			if( v instanceof CommonViewer ) {
				cv = (CommonViewer)v;
				ICommonViewerWorkbenchSite wsSite = (ICommonViewerWorkbenchSite)site;
				createActions(cv, wsSite.getSelectionProvider());
				addDoubleClickHandler(cv);
			}
		}
	}
	
	public void dispose() {
		super.dispose();
		removeDoubleClickHandler();
	}
	
	private IDoubleClickListener doubleClickListener;
	protected void addDoubleClickHandler(CommonViewer cv) {
		doubleClickListener = new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				Object o = getSelection();
			}
		};
		cv.addDoubleClickListener(doubleClickListener);
	}
	
	protected void removeDoubleClickHandler() {
		cv.removeDoubleClickListener(doubleClickListener);
	}

	private Action newConnectionAction, editConnectionAction,
			deleteCloudAction, refreshCloudAction, launchInstanceAction, 
			startAction, stopAction, rebootAction, destroyAction, 
			filterImageAction, filterInstanceAction;
	
	protected IStructuredSelection getSelection() {
		ICommonViewerSite site = actionSite.getViewSite();
		if (site instanceof ICommonViewerWorkbenchSite) {
			ICommonViewerWorkbenchSite wsSite = (ICommonViewerWorkbenchSite) site;
			return (IStructuredSelection)wsSite.getSelectionProvider().getSelection();
		}
		return null;
	}
	
	protected Object getSelectedItem() {
		IStructuredSelection selection = getSelection();
		if( selection != null ) 
			return selection.getFirstElement();
		return null;
	}
	
	private void addGM(String id, boolean visible, IMenuManager manager) {
		GroupMarker gm = new GroupMarker(id);
		manager.add(gm);
		Separator s = new Separator();
		s.setVisible(true);
		manager.add(s);
	}
	
	public void fillContextMenu(IMenuManager menu) {
		createActions(cv, null);
		Object first = getSelectedItem();
		addGM("BEGIN_CLOUD", false, menu);
		menu.insertAfter("BEGIN_CLOUD", newConnectionAction);
		if (first == null)
			return;
		
		addGM("END_CLOUD", true, menu);
		menu.insertBefore("END_CLOUD", editConnectionAction);
		menu.insertBefore("END_CLOUD", deleteCloudAction);
		menu.insertBefore("END_CLOUD", refreshCloudAction);
		addGM("INSTANCE_ACTIONS_BEGIN", false, menu);
		addGM("INSTANCE_ACTIONS_END", true, menu);
		menu.insertBefore("INSTANCE_ACTIONS_END", launchInstanceAction);
		if( (first instanceof DeltaCloudInstance)) {
			DeltaCloudInstance i = (DeltaCloudInstance)first;
			menu.insertBefore("INSTANCE_ACTIONS_END", startAction);
			menu.insertBefore("INSTANCE_ACTIONS_END", stopAction);
			menu.insertBefore("INSTANCE_ACTIONS_END", rebootAction);
			menu.insertBefore("INSTANCE_ACTIONS_END", destroyAction);
			startAction.setEnabled(i.canStart());
			stopAction.setEnabled(i.canStop());
			rebootAction.setEnabled(i.canReboot());
			destroyAction.setEnabled(i.canDestroy());
		}
		addGM("FILTERS_BEGIN", false, menu);
		addGM("FILTERS_END", true, menu);
		menu.insertBefore("FILTERS_END", filterImageAction);
		menu.insertBefore("FILTERS_END", filterInstanceAction);
	}
	
	public void createActions(CommonViewer tableViewer, ISelectionProvider provider) {
		final Shell shell = tableViewer.getTree().getShell();
		newConnectionAction = new Action() {
			public void run() {
				NewCloudConnectionWizard wizard = new NewCloudConnectionWizard();
				wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				dialog.open();
			}
		};
		newConnectionAction.setText("New Connection");
		newConnectionAction.setImageDescriptor(SWTImagesFactory.DESC_NEW_DELTA);
		
		editConnectionAction = new Action() {
			public void run() {
				DeltaCloud cloud = WorkbenchUtils.getFirstAdaptedElement(getSelection(), DeltaCloud.class);
				if (cloud != null) {
					IWizard wizard = new EditCloudConnectionWizard(cloud);
					WizardDialog dialog = new WizardDialog(shell, wizard);
					dialog.create();
					dialog.open();
				}
			}
		};
		editConnectionAction.setText("Edit Connection");

		deleteCloudAction = new Action() {
			public void run() {
				CloudViewActionUtil.deleteDeltaClouds(shell, getSelection());
			}
		};
		deleteCloudAction.setText("Delete Cloud");
		deleteCloudAction.setImageDescriptor(SWTImagesFactory.DESC_DESTROY);
		deleteCloudAction.setDisabledImageDescriptor(SWTImagesFactory.DESC_DESTROYD);
		
		refreshCloudAction = new Action() {
			public void run() {
				CloudViewActionUtil.refreshDeltaClouds(shell, getSelection());
			}
		};
		refreshCloudAction.setText("Refresh Cloud");
	
		launchInstanceAction = new Action() {
			public void run() {
				CloudViewActionUtil.showCreateInstanceWizard(shell, getSelection());
			}
		};
		launchInstanceAction.setText("Launch Instance");
		launchInstanceAction.setImageDescriptor(SWTImagesFactory.DESC_INSTANCE);
		
		startAction = new Action() {
			public void run() {
				CloudViewActionUtil.startInstances(getSelection());
			}
		};
		startAction.setText("Start");
		startAction.setImageDescriptor(SWTImagesFactory.DESC_START);
		startAction.setDisabledImageDescriptor(SWTImagesFactory.DESC_STARTD);

		stopAction = new Action() {
			public void run() {
				CloudViewActionUtil.stopInstances(getSelection());
			}
		};
		stopAction.setText("Stop");
		stopAction.setImageDescriptor(SWTImagesFactory.DESC_STOP);
		stopAction.setDisabledImageDescriptor(SWTImagesFactory.DESC_STOPD);

		rebootAction = new Action() {
			public void run() {
				CloudViewActionUtil.rebootInstances(getSelection());
			}
		};
		rebootAction.setText("Reboot");
		rebootAction.setImageDescriptor(SWTImagesFactory.DESC_REBOOT);
		rebootAction.setDisabledImageDescriptor(SWTImagesFactory.DESC_REBOOTD);

		destroyAction = new Action() {
			public void run() {
				CloudViewActionUtil.destroyInstances(getSelection());
			}
		};
		destroyAction.setText("Destroy");
		destroyAction.setImageDescriptor(SWTImagesFactory.DESC_DESTROY);
		destroyAction.setDisabledImageDescriptor(SWTImagesFactory.DESC_DESTROYD);
		
		filterImageAction = new Action() {
			public void run() {
				DeltaCloud cloud = WorkbenchUtils.getFirstAdaptedElement(getSelection(), DeltaCloud.class);
				if( cloud != null ) {
					IWizard wizard = new ImageFilterWizard(cloud);
					WizardDialog dialog = new WizardDialog(shell, wizard);
					dialog.create();
					dialog.open();
				}
			}
		};
		filterImageAction.setText("Filter Images");

		
		filterInstanceAction = new Action() {
			public void run() {
				DeltaCloud cloud = WorkbenchUtils.getFirstAdaptedElement(getSelection(), DeltaCloud.class);
				if( cloud != null ) {
					IWizard wizard = new InstanceFilterWizard(cloud);
					WizardDialog dialog = new WizardDialog(shell, wizard);
					dialog.create();
					dialog.open();
				}
			}
		};
		filterInstanceAction.setText("Filter Instances");
	}
}

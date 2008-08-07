package org.jboss.ide.eclipse.archives.ui.providers;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.jboss.ide.eclipse.archives.core.build.ArchiveBuildDelegate;
import org.jboss.ide.eclipse.archives.core.build.SaveArchivesJob;
import org.jboss.ide.eclipse.archives.core.model.ArchiveNodeFactory;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.ExtensionManager;
import org.jboss.ide.eclipse.archives.ui.NodeContribution;
import org.jboss.ide.eclipse.archives.ui.actions.NewArchiveAction;
import org.jboss.ide.eclipse.archives.ui.wizards.FilesetWizard;
import org.jboss.ide.eclipse.archives.ui.wizards.NewJARWizard;

public class ArchivesActionProvider extends CommonActionProvider {
	public static final String NEW_PACKAGE_MENU_ID = "org.jboss.ide.eclipse.archives.ui.newPackageMenu";
	public static final String NODE_CONTEXT_MENU_ID = "org.jboss.ide.eclipse.archives.ui.nodeContextMenu";
	public static final String NEW_PACKAGE_ADDITIONS = "newPackageAdditions";

	private MenuManager newPackageManager;
	private NodeContribution[]  nodePopupMenuContributions;
	private NewArchiveAction[] newPackageActions;
	private Action editAction, deleteAction, newFolderAction, newFilesetAction;
	private Action buildAction;
	private ICommonViewerSite site;

	public ArchivesActionProvider() {
	}
	
	public void init(ICommonActionExtensionSite aSite) {
		newPackageActions = ExtensionManager.findNewArchiveActions();
		nodePopupMenuContributions = ExtensionManager.findNodePopupMenuContributions();
		Arrays.sort(nodePopupMenuContributions);
		site = aSite.getViewSite();
		createActions();
		newPackageManager = new MenuManager(ArchivesUIMessages.ProjectPackagesView_newPackageMenu_label, NEW_PACKAGE_MENU_ID);
		newPackageManager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	public void fillContextMenu(IMenuManager manager) {
		menuAboutToShow2(manager);
	}
	
	public void menuAboutToShow2(IMenuManager manager) {
		addNewPackageActions(newPackageManager);

		IStructuredSelection selection = getSelection();
		if (selection != null && !selection.isEmpty()) {
			Object element = selection.getFirstElement();
			
			if (element instanceof IProject) {
				manager.add(newPackageManager);
				manager.add(buildAction);
				buildAction.setText(ArchivesUIMessages.ProjectPackagesView_buildProjectAction_label);
			} else if( element instanceof IArchiveNode ){
				IArchiveNode node = (IArchiveNode)element;
				
				if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE
						|| node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER) {
					manager.add(newPackageManager);
					manager.add(newFolderAction);
					manager.add(newFilesetAction);
					manager.add(new Separator());
				}
				
				if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
					editAction.setText(ArchivesUIMessages.ProjectPackagesView_editPackageAction_label);
					deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deletePackageAction_label);
					editAction.setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_PACKAGE_EDIT));
					buildAction.setText(ArchivesUIMessages.ProjectPackagesView_buildArchiveAction_label);
					manager.add(buildAction);
				} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER) {
					editAction.setText(ArchivesUIMessages.ProjectPackagesView_editFolderAction_label); 
					deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deleteFolderAction_label);
					editAction.setImageDescriptor(platformDescriptor(ISharedImages.IMG_OBJ_FOLDER));
				} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET) {
					editAction.setText(ArchivesUIMessages.ProjectPackagesView_editFilesetAction_label); 
					deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deleteFilesetAction_label);
					editAction.setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_MULTIPLE_FILES));
				}
				manager.add(editAction);
				manager.add(deleteAction);
				addContextMenuContributions(node, manager);
			}
		} else {
			manager.add(newPackageManager);
		}
	}
	
	protected void createActions() {
		newFolderAction = new Action(ArchivesUIMessages.ProjectPackagesView_newFolderAction_label, platformDescriptor(ISharedImages.IMG_OBJ_FOLDER)) { //$NON-NLS-1$
			public void run () {
				createFolder();
			}
		};
		
		newFilesetAction = new Action(ArchivesUIMessages.ProjectPackagesView_newFilesetAction_label, ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_MULTIPLE_FILES)) { //$NON-NLS-1$
			public void run () {
				createFileset();
			}
		};
		
		deleteAction = new Action (ArchivesUIMessages.ProjectPackagesView_deletePackageAction_label, platformDescriptor(ISharedImages.IMG_TOOL_DELETE)) { //$NON-NLS-1$
			public void run () {
				deleteSelectedNode();
			}	
		};
		
		editAction = new Action (ArchivesUIMessages.ProjectPackagesView_editPackageAction_label, ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_PACKAGE_EDIT)) { //$NON-NLS-1$
			public void run () {
				editSelectedNode();
			}
		};
		
		buildAction = new Action("", ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_BUILD_PACKAGES)) {
			public void run() {
				buildSelectedNode(getSelectedObject());
			}
		};
	}
	
	


	private void addContextMenuContributions (final IArchiveNode context, IMenuManager mgr) {

		for( int i = 0; i < nodePopupMenuContributions.length; i++ ) {
			final NodeContribution contribution = nodePopupMenuContributions[i];
			if ( contribution.getActionDelegate().isEnabledFor(context)) {
				Action action = new Action () {
					public String getId() {
						return contribution.getId();
					}
		
					public ImageDescriptor getImageDescriptor() {
						return contribution.getIcon();
					}
		
					public String getText() {
						return contribution.getLabel();
					}
		
					public void run() {
						contribution.getActionDelegate().run(context);
					}
				};
				mgr.add(action);
			}
		}
	}
	
	
	/**
	 * Adds the new package type actions (which come from an extension point)
	 * to the menu.
	 * @param manager
	 */
	private void addNewPackageActions (IMenuManager manager) {
		for( int i = 0; i < newPackageActions.length; i++ ) {
			NewArchiveAction action = newPackageActions[i];
			ActionWrapper wrapped = new ActionWrapper(action);
			wrapped.selectionChanged(getSelection());
			manager.add(wrapped);
		}
	}

	public class ActionWrapper extends Action {
		private NewArchiveAction action;
		public ActionWrapper(NewArchiveAction act) {
			this.action = act;
		}
		public String getId() {
			return action.getId();
		}
		
		public ImageDescriptor getImageDescriptor() {
			return action.getIconDescriptor();
		}
		
		public String getText() {
			return action.getLabel();
		}
		
		public void run() {
			action.getAction().run(null);
		}
		
		public void selectionChanged(IStructuredSelection sel) {
			action.getAction().selectionChanged(this, sel);
		}
	}
	
	
	/*
	 * Methods below are called from the standard actions, 
	 * the implementations of the action, where the action does its work etc
	 */
	
	private void createFolder () {
		IInputValidator validator = new IInputValidator () {
			public String isValid(String newText) {
				IArchiveNode selected = getSelectedNode();
				
				boolean folderExists = false;
				IArchiveNode[] folders = selected.getChildren(IArchiveNode.TYPE_ARCHIVE_FOLDER);
				for (int i = 0; i < folders.length; i++) {
					IArchiveFolder folder = (IArchiveFolder) folders[i];
					if (folder.getName().equals(newText)) {
						folderExists = true; break;
					}
				}
				
				if (folderExists) {
					return ArchivesUIMessages.bind(
						ArchivesUIMessages.ProjectPackagesView_createFolderDialog_warnFolderExists, newText);
					
				}
				return null;
			}
		};
		
		InputDialog dialog = new InputDialog(getShell(),
			ArchivesUIMessages.ProjectPackagesView_createFolderDialog_title,
			ArchivesUIMessages.ProjectPackagesView_createFolderDialog_message, "", validator);
		
		int response = dialog.open();
		if (response == Dialog.OK) {
			String[] folderPaths = dialog.getValue().split("[\\\\/]");
			IArchiveNode selected = getSelectedNode();
			IArchiveFolder current = null;
			IArchiveFolder temp = null;
			
			for(int i = folderPaths.length-1; i >= 0 ; i-- ) {
				temp = ArchiveNodeFactory.createFolder();
				temp.setName(folderPaths[i]);
				if( current == null ) 
					current = temp;
				else {
					temp.addChild(current);
					current = temp;
				}
			}
			
			selected.addChild(current);
			new SaveArchivesJob(selected.getProjectPath()).schedule();
		}
	}
	
	private void createFileset () {
		IArchiveNode selected = getSelectedNode();
		WizardDialog dialog = new WizardDialog(getShell(), new FilesetWizard(null, selected));
		dialog.open();
	}
	
	private void editSelectedNode () {
		IArchiveNode node = getSelectedNode();
		if (node != null) {
			if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET) {
				IArchiveFileSet fileset = (IArchiveFileSet) node;
				WizardDialog dialog = new WizardDialog(getShell(), new FilesetWizard(fileset, node.getParent()));
				dialog.open();
			} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
				IArchive pkg = (IArchive) node;
				WizardDialog dialog = new WizardDialog(getShell(), new NewJARWizard(pkg));
				dialog.open();
			} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER) {
				// folder can do the model save here. 
				IArchiveFolder folder = (IArchiveFolder) node;
				InputDialog dialog = new InputDialog(getShell(),
						ArchivesUIMessages.ProjectPackagesView_createFolderDialog_title,
						ArchivesUIMessages.ProjectPackagesView_createFolderDialog_message, folder.getName(), null);
				
				int response = dialog.open();
				if (response == Dialog.OK) {
					folder.setName(dialog.getValue());
					new SaveArchivesJob(folder.getProjectPath()).schedule();
				}
			} 
		}
	}
	
	private void buildSelectedNode(final Object selected) {
		new Job("Build Archive Node") {
			protected IStatus run(IProgressMonitor monitor) {
				if( selected == null ) return Status.OK_STATUS;
				if (selected instanceof IArchiveNode &&  
						((IArchiveNode)selected).getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
					new ArchiveBuildDelegate().fullArchiveBuild((IArchive)selected);
				} else if( selected != null && selected instanceof IProject ){
					new ArchiveBuildDelegate().fullProjectBuild(((IProject)selected).getProject().getLocation());
				} else {
					new ArchiveBuildDelegate().fullArchiveBuild(((IArchiveNode)selected).getRootArchive());
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}
	
	private void deleteSelectedNode () {
		IArchiveNode node = getSelectedNode();
		if (node != null) {
			final IArchiveNode parent = (IArchiveNode) node.getParent();
			parent.removeChild(node);
			SaveArchivesJob job = new SaveArchivesJob(parent.getProjectPath());
			job.schedule();
		}
	}
	
	
	private IArchiveNode getSelectedNode () {
		Object selected = getSelectedObject();
		if( selected instanceof IArchiveNode ) 
			return ((IArchiveNode)selected);
		return null;
	}
	private Object getSelectedObject() {
		IStructuredSelection selection = getSelection();
		if (selection != null && !selection.isEmpty())
			return selection.getFirstElement();
		return null;
	}
	private IStructuredSelection getSelection() {
		return (IStructuredSelection) site.getSelectionProvider().getSelection();
	}
	
	private ImageDescriptor platformDescriptor(String desc) {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(desc);
	}
	private Shell getShell() {
		return site.getShell();
	}
}

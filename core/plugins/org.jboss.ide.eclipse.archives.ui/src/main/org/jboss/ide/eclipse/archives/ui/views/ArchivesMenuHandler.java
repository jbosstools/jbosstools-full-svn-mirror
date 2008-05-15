package org.jboss.ide.eclipse.archives.ui.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.archives.core.build.ArchiveBuildDelegate;
import org.jboss.ide.eclipse.archives.core.model.ArchiveNodeFactory;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding.XbException;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.ExtensionManager;
import org.jboss.ide.eclipse.archives.ui.NodeContribution;
import org.jboss.ide.eclipse.archives.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.archives.ui.actions.ActionWithDelegate;
import org.jboss.ide.eclipse.archives.ui.actions.NewArchiveAction;
import org.jboss.ide.eclipse.archives.ui.actions.NewJARAction;
import org.jboss.ide.eclipse.archives.ui.providers.ArchivesContentProvider.WrappedProject;
import org.jboss.ide.eclipse.archives.ui.wizards.FilesetWizard;
import org.jboss.ide.eclipse.archives.ui.wizards.NewJARWizard;

/**
 * Manages the actions associated with the view
 * @author rstryker
 *
 */
public class ArchivesMenuHandler {
	public static final String NEW_PACKAGE_MENU_ID = "org.jboss.ide.eclipse.archives.ui.newPackageMenu";
	public static final String NODE_CONTEXT_MENU_ID = "org.jboss.ide.eclipse.archives.ui.nodeContextMenu";
	public static final String NEW_PACKAGE_ADDITIONS = "newPackageAdditions";

	private MenuManager newPackageManager, contextMenuManager;
	private NodeContribution[]  nodePopupMenuContributions;
	private NewArchiveAction[] newPackageActions;
	private Menu treeContextMenu;
	private TreeViewer packageTree;
	
	private Action editAction, deleteAction, newFolderAction, newFilesetAction;
	private NewJARAction newJARAction;
	private Action buildAction;

	public ArchivesMenuHandler(TreeViewer viewer) {
		this.packageTree = viewer;

		// load from extensions 
		newPackageActions = ExtensionManager.findNewArchiveActions();
		nodePopupMenuContributions = ExtensionManager.findNodePopupMenuContributions();
		Arrays.sort(nodePopupMenuContributions);

		
		createActions();
		createMenu();
		createContextMenu();
		addToActionBars();
	}

	private void addToActionBars() {
		IActionBars bars = getSite().getActionBars();
		bars.getToolBarManager().add(buildAction);
	}
	
	/**
	 * Creates the primary menu as well as adds the package actions to it
	 *
	 */
	private void createMenu () {
		newPackageManager = new MenuManager(ArchivesUIMessages.ProjectPackagesView_newPackageMenu_label, NEW_PACKAGE_MENU_ID);
		addNewPackageActions(newPackageManager);
		newPackageManager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void createContextMenu () {
		contextMenuManager = new MenuManager(NODE_CONTEXT_MENU_ID); //$NON-NLS-1$
		contextMenuManager.setRemoveAllWhenShown(true);
		contextMenuManager.addMenuListener(new IMenuListener () {
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection) packageTree.getSelection();
				if (selection != null && !selection.isEmpty()) {
					Object element = selection.getFirstElement();
					
					if (element instanceof WrappedProject) {
						newJARAction.setEnabled(true);
						manager.add(newPackageManager);
						manager.add(buildAction);
						buildAction.setText(ArchivesUIMessages.ProjectPackagesView_buildProjectAction_label);
					} else if( element instanceof IArchiveNode ){
						IArchiveNode node = (IArchiveNode)element;
						
						switch(node.getNodeType()) {
						case IArchiveNode.TYPE_ARCHIVE:
							newJARAction.setEnabled(true);
							manager.add(newPackageManager);
							manager.add(newFolderAction);
							manager.add(newFilesetAction);
							manager.add(new Separator());
							editAction.setText(ArchivesUIMessages.ProjectPackagesView_editPackageAction_label); //$NON-NLS-1$
							deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deletePackageAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_PACKAGE_EDIT));
							buildAction.setText(ArchivesUIMessages.ProjectPackagesView_buildArchiveAction_label);
							manager.add(buildAction);
							break;
						case IArchiveNode.TYPE_ARCHIVE_FOLDER:
							newJARAction.setEnabled(true);
							manager.add(newPackageManager);
							manager.add(newFolderAction);
							manager.add(newFilesetAction);
							manager.add(new Separator());
							editAction.setText(ArchivesUIMessages.ProjectPackagesView_editFolderAction_label); //$NON-NLS-1$
							deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deleteFolderAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(platformDescriptor(ISharedImages.IMG_OBJ_FOLDER));
							break;
						case IArchiveNode.TYPE_ARCHIVE_FILESET:
							editAction.setText(ArchivesUIMessages.ProjectPackagesView_editFilesetAction_label); //$NON-NLS-1$
							deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deleteFilesetAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_MULTIPLE_FILES));
							break;
						case IArchiveNode.TYPE_ARCHIVE_ACTION:
							editAction.setText(ArchivesUIMessages.ProjectPackagesView_editActionAction_label); //$NON-NLS-1$
							deleteAction.setText(ArchivesUIMessages.ProjectPackagesView_deleteActionAction_label); //$NON-NLS-1$
							//editAction.setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_MULTIPLE_FILES));
							editAction.setImageDescriptor(null);
							break;
						default:
							// TODO unknown?
							break;
						}
						manager.add(editAction);
						manager.add(deleteAction);
						addContextMenuContributions(node);
					}
				} else {
					manager.add(newPackageManager);
				}
			}
		});
		
		treeContextMenu = contextMenuManager.createContextMenu(packageTree.getTree());
		packageTree.getTree().setMenu(treeContextMenu);
		
		getSite().registerContextMenu(NEW_PACKAGE_MENU_ID, newPackageManager, packageTree);
	}

	protected void createActions() {
		newJARAction = new NewJARAction();
		newJARAction.setEnabled(false);
		
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
		
		buildAction = new ActionWithDelegate("", ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_BUILD_PACKAGES)) {
			public void run() {
				final Object selected = getSelectedObject();
				new Job("Build Archive Node") {
					protected IStatus run(IProgressMonitor monitor) {
						buildSelectedNode(selected);
						return Status.OK_STATUS;
					}
				}.schedule();
			}

			public IStructuredSelection getSelection() {
				return ProjectArchivesView.getInstance().getSelection();
			}
		};
	}

	private void addContextMenuContributions (final IArchiveNode context) {

		for( int i = 0; i < nodePopupMenuContributions.length; i++ ) {
			try {

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
					contextMenuManager.add(action);
				}
			} catch( Exception e) { System.out.println(e.getMessage()); }
		}

	}
	
	
	/**
	 * Adds the new package type actions (which come from an extension point)
	 * to the menu.
	 * @param manager
	 */
	private void addNewPackageActions (IMenuManager manager) {
		for( int i = 0; i < newPackageActions.length; i++ ) {
			final NewArchiveAction action = newPackageActions[i];
			
			Action actionWrapper = new Action () {
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
					action.getAction().run(this);
				}
			};
			
			manager.add(actionWrapper);
		}
	}


	
	
	/*
	 * Methods below are called from the standard actions, 
	 * the implementations of the action, where the action does its work etc
	 */
	
	private void createFolder ()
	{
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
		
		InputDialog dialog = new InputDialog(getSite().getShell(),
			ArchivesUIMessages.ProjectPackagesView_createFolderDialog_title,
			ArchivesUIMessages.ProjectPackagesView_createFolderDialog_message, "", validator);
		
		int response = dialog.open();
		if (response == Dialog.OK) {
			try {
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
				ArchivesModel.instance().save(selected.getProjectPath(), new NullProgressMonitor());
			} catch( ArchivesModelException ame ) {
				IStatus status = new Status(IStatus.ERROR, PackagesUIPlugin.PLUGIN_ID, "Error Attaching Archives Node", ame);
				PackagesUIPlugin.getDefault().getLog().log(status);
			}
		}
	}
	
	private void createFileset () {
		try {
			IArchiveNode selected = getSelectedNode();
			WizardDialog dialog = new WizardDialog(getSite().getShell(), new FilesetWizard(null, selected));
			
			dialog.open();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void editSelectedNode () {
		IArchiveNode node = getSelectedNode();
		if (node != null) {
			if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET) {
				IArchiveFileSet fileset = (IArchiveFileSet) node;
				WizardDialog dialog = new WizardDialog(getSite().getShell(), new FilesetWizard(fileset, node.getParent()));
				try {
					dialog.open();
				} catch( Exception e ) { e.printStackTrace(); }
			} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
				IArchive pkg = (IArchive) node;
				WizardDialog dialog = new WizardDialog(getSite().getShell(), new NewJARWizard(pkg));
				dialog.open();
			} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER) {
				// folder can do the model save here. 
				IArchiveFolder folder = (IArchiveFolder) node;
				InputDialog dialog = new InputDialog(getSite().getShell(),
					ArchivesUIMessages.ProjectPackagesView_createFolderDialog_title,
					ArchivesUIMessages.ProjectPackagesView_createFolderDialog_message, folder.getName(), null);
				
				int response = dialog.open();
				if (response == Dialog.OK) {
					folder.setName(dialog.getValue());
					try {
						ArchivesModel.instance().save(folder.getProjectPath(), new NullProgressMonitor());
					} catch( ArchivesModelException ame ) {
						IStatus status = new Status(IStatus.ERROR, PackagesUIPlugin.PLUGIN_ID, "Problem saving archives model", ame);
						PackagesUIPlugin.getDefault().getLog().log(status);
					}
				}
			} 
		}
	}
	
	private void buildSelectedNode(Object selected) {
		if( selected == null ) return;
		if (selected instanceof IArchiveNode &&  
				((IArchiveNode)selected).getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
			new ArchiveBuildDelegate().fullArchiveBuild((IArchive)selected);
		} else if( selected != null && selected instanceof WrappedProject ){
			new ArchiveBuildDelegate().fullProjectBuild(((WrappedProject)selected).getProject().getLocation());
		} else {
			new ArchiveBuildDelegate().fullArchiveBuild(((IArchiveNode)selected).getRootArchive());
		}
		
	}
	
	private void deleteSelectedNode () {
		IArchiveNode node = getSelectedNode();
		if (node != null) {
			IArchiveNode parent = (IArchiveNode) node.getParent();
			parent.removeChild(node);
			if( parent.getProjectPath() != null ) {
				try {
					ArchivesModel.instance().save(parent.getProjectPath(), new NullProgressMonitor());
				} catch( ArchivesModelException ame ) {
					IStatus status = new Status(IStatus.ERROR, PackagesUIPlugin.PLUGIN_ID, "Problem saving archives model", ame);
					PackagesUIPlugin.getDefault().getLog().log(status);
				}

			}
		}
	}

	
	
	/*
	 * Utility methods below
	 */

	private IViewSite getSite() {
		return (IViewSite) ProjectArchivesView.getInstance().getSite();
	}

	private IArchiveNode getSelectedNode () {
		Object selected = getSelectedObject();
		if( selected instanceof IArchiveNode ) 
			return ((IArchiveNode)selected);
		return null;
	}
	private Object getSelectedObject() {
		IStructuredSelection selection = (IStructuredSelection) ProjectArchivesView.getInstance().getSelection();
		if (selection != null && !selection.isEmpty())
			return selection.getFirstElement();
		return null;
	}
	
	private ImageDescriptor platformDescriptor(String desc) {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(desc);
	}

}

package org.jboss.ide.eclipse.archives.ui.views;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
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
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.ExtensionManager;
import org.jboss.ide.eclipse.archives.ui.NodeContribution;
import org.jboss.ide.eclipse.archives.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.archives.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.archives.ui.actions.ActionWithDelegate;
import org.jboss.ide.eclipse.archives.ui.actions.NewJARAction;
import org.jboss.ide.eclipse.archives.ui.actions.NewPackageAction;
import org.jboss.ide.eclipse.archives.ui.providers.ArchivesContentProvider.WrappedProject;
import org.jboss.ide.eclipse.archives.ui.util.PackageNodeFactory;
import org.jboss.ide.eclipse.archives.ui.wizards.FilesetWizard;
import org.jboss.ide.eclipse.archives.ui.wizards.NewJARWizard;

/**
 * Manages the actions associated with the view
 * @author rstryker
 *
 */
public class PackagesMenuHandler {
	public static final String NEW_PACKAGE_MENU_ID = "org.jboss.ide.eclipse.archives.ui.newPackageMenu";
	public static final String NODE_CONTEXT_MENU_ID = "org.jboss.ide.eclipse.archives.ui.nodeContextMenu";
	public static final String NEW_PACKAGE_ADDITIONS = "newPackageAdditions";

	private MenuManager newPackageManager, contextMenuManager;
	private NodeContribution[]  nodePopupMenuContributions;
	private NewPackageAction[] newPackageActions;
	private Menu treeContextMenu;
	private TreeViewer packageTree;
	
	private Action editAction, deleteAction, newFolderAction, newFilesetAction;
	private NewJARAction newJARAction;
	private Action buildAction;

	public PackagesMenuHandler(TreeViewer viewer) {
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
		newPackageManager = new MenuManager(PackagesUIMessages.ProjectPackagesView_newPackageMenu_label, NEW_PACKAGE_MENU_ID);
		addNewPackageActions(newPackageManager);
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
						buildAction.setText(PackagesUIMessages.ProjectPackagesView_buildProjectAction_label);
					} else if( element instanceof IArchiveNode ){
						IArchiveNode node = (IArchiveNode)element;
						
						if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE
								|| node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER)
						{	
							newJARAction.setEnabled(true);
							manager.add(newPackageManager);
							
							manager.add(newFolderAction);
							manager.add(newFilesetAction);
							manager.add(new Separator());
						}
						
						if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
							editAction.setText(PackagesUIMessages.ProjectPackagesView_editPackageAction_label); //$NON-NLS-1$
							deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deletePackageAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_PACKAGE_EDIT));
							buildAction.setText(PackagesUIMessages.ProjectPackagesView_buildArchiveAction_label);
							manager.add(buildAction);
						} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER) {
							editAction.setText(PackagesUIMessages.ProjectPackagesView_editFolderAction_label); //$NON-NLS-1$
							deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deleteFolderAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(platformDescriptor(ISharedImages.IMG_OBJ_FOLDER));
						} else if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET) {
							editAction.setText(PackagesUIMessages.ProjectPackagesView_editFilesetAction_label); //$NON-NLS-1$
							deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deleteFilesetAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_MULTIPLE_FILES));
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
		
		newFolderAction = new Action(PackagesUIMessages.ProjectPackagesView_newFolderAction_label, platformDescriptor(ISharedImages.IMG_OBJ_FOLDER)) { //$NON-NLS-1$
			public void run () {
				createFolder();
			}
		};
		
		newFilesetAction = new Action(PackagesUIMessages.ProjectPackagesView_newFilesetAction_label, PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_MULTIPLE_FILES)) { //$NON-NLS-1$
			public void run () {
				createFileset();
			}
		};
		
		deleteAction = new Action (PackagesUIMessages.ProjectPackagesView_deletePackageAction_label, platformDescriptor(ISharedImages.IMG_TOOL_DELETE)) { //$NON-NLS-1$
			public void run () {
				deleteSelectedNode();
			}	
		};
		
		editAction = new Action (PackagesUIMessages.ProjectPackagesView_editPackageAction_label, PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_PACKAGE_EDIT)) { //$NON-NLS-1$
			public void run () {
				editSelectedNode();
			}
		};
		
		buildAction = new ActionWithDelegate("", PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_BUILD_PACKAGES)) {
			public void run() {
				buildSelectedNode();
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
			} catch( Exception e) { e.getMessage(); }
		}

	}
	
	
	/**
	 * Adds the new package type actions (which come from an extension point)
	 * to the menu.
	 * @param manager
	 */
	private void addNewPackageActions (IMenuManager manager) {
		for( int i = 0; i < newPackageActions.length; i++ ) {
			final NewPackageAction action = newPackageActions[i];
			
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
					return PackagesUIMessages.bind(
						PackagesUIMessages.ProjectPackagesView_createFolderDialog_warnFolderExists, newText);
					
				}
				return null;
			}
		};
		
		InputDialog dialog = new InputDialog(getSite().getShell(),
			PackagesUIMessages.ProjectPackagesView_createFolderDialog_title,
			PackagesUIMessages.ProjectPackagesView_createFolderDialog_message, "", validator);
		
		int response = dialog.open();
		if (response == Dialog.OK) {
			String folderName = dialog.getValue();
			IArchiveNode selected = getSelectedNode();

			IArchiveFolder folder = PackageNodeFactory.createFolder();
			folder.setName(folderName);
			ArchivesModel.instance().attach(selected, folder, new NullProgressMonitor());
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
					PackagesUIMessages.ProjectPackagesView_createFolderDialog_title,
					PackagesUIMessages.ProjectPackagesView_createFolderDialog_message, folder.getName(), null);
				
				int response = dialog.open();
				if (response == Dialog.OK) {
					folder.setName(dialog.getValue());
					ArchivesModel.instance().saveModel(folder.getProject(), new NullProgressMonitor());
				}
			} 
		}
	}
	
	private void buildSelectedNode() {
		Object selected = getSelectedObject();
		if( selected == null ) return;
		if (selected instanceof IArchiveNode && 
				((IArchiveNode)selected).getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
			ArchivesCore.buildArchive((IArchive)selected, null);
		} else if( selected != null && selected instanceof IProject ){
			ArchivesCore.buildProject((IProject)selected, null);
		}
		
	}
	
	private void deleteSelectedNode () {
		IArchiveNode node = getSelectedNode();
		if (node != null) {
			IArchiveNode parent = (IArchiveNode) node.getParent();
			parent.removeChild(node);
			if( parent.getProject() != null ) {
				ArchivesModel.instance().saveModel(parent.getProject(), new NullProgressMonitor());
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

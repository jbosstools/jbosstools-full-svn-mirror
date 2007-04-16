package org.jboss.ide.eclipse.packages.ui.views;

import java.util.Arrays;

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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.ui.ExtensionManager;
import org.jboss.ide.eclipse.packages.ui.NodeContribution;
import org.jboss.ide.eclipse.packages.ui.PackageNodeFactory;
import org.jboss.ide.eclipse.packages.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.actions.NewJARAction;
import org.jboss.ide.eclipse.packages.ui.actions.NewPackageAction;
import org.jboss.ide.eclipse.packages.ui.providers.PackagesContentProvider.WrappedProject;
import org.jboss.ide.eclipse.packages.ui.wizards.FilesetWizard;
import org.jboss.ide.eclipse.packages.ui.wizards.NewJARWizard;

/**
 * Manages the actions associated with the view
 * @author rstryker
 *
 */
public class PackagesMenuHandler {
	public static final String NEW_PACKAGE_MENU_ID = "org.jboss.ide.eclipse.packages.ui.newPackageMenu";
	public static final String NODE_CONTEXT_MENU_ID = "org.jboss.ide.eclipse.packages.ui.nodeContextMenu";
	public static final String NEW_PACKAGE_ADDITIONS = "newPackageAdditions";

	private MenuManager newPackageManager, contextMenuManager;
	private NodeContribution[]  nodePopupMenuContributions;
	private NewPackageAction[] newPackageActions;
	private Menu treeContextMenu;
	private TreeViewer packageTree;
	
	private Action editAction, deleteAction, newFolderAction, newFilesetAction;
	private NewJARAction newJARAction;
//	private BuildPackagesAction buildAllAction, buildPackageAction;

	public PackagesMenuHandler(TreeViewer viewer) {
		this.packageTree = viewer;

		// load from extensions 
		newPackageActions = ExtensionManager.findNewPackageActions();
		nodePopupMenuContributions = ExtensionManager.findNodePopupMenuContributions();
		Arrays.sort(nodePopupMenuContributions);

		
		createActions();
		createMenu();
		createContextMenu();
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
					} else if( element instanceof IPackageNode ){
						IPackageNode node = (IPackageNode)element;
						
						if (node.getNodeType() == IPackageNode.TYPE_PACKAGE
								|| node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
						{	
							newJARAction.setEnabled(true);
							manager.add(newPackageManager);
							
							manager.add(newFolderAction);
							manager.add(newFilesetAction);
							manager.add(new Separator());
						}
						
						if (node.getNodeType() == IPackageNode.TYPE_PACKAGE) {
							editAction.setText(PackagesUIMessages.ProjectPackagesView_editPackageAction_label); //$NON-NLS-1$
							deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deletePackageAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_PACKAGE_EDIT));
//							manager.add(buildPackageAction);
						} else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER) {
							editAction.setText(PackagesUIMessages.ProjectPackagesView_editFolderAction_label); //$NON-NLS-1$
							deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deleteFolderAction_label); //$NON-NLS-1$
							editAction.setImageDescriptor(platformDescriptor(ISharedImages.IMG_OBJ_FOLDER));
						} else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET) {
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

//		buildAllAction = new BuildPackagesAction();
//		buildAllAction.init(getViewSite().getWorkbenchWindow());
//		
//		buildPackageAction = new BuildPackagesAction();
//		buildPackageAction.init(getViewSite().getWorkbenchWindow());
	}

	private void addContextMenuContributions (final IPackageNode context) {

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
				IPackageNode selected = getSelectedNode();
				
				boolean folderExists = false;
				IPackageNode[] folders = selected.getChildren(IPackageNode.TYPE_PACKAGE_FOLDER);
				for (int i = 0; i < folders.length; i++) {
					IPackageFolder folder = (IPackageFolder) folders[i];
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
			IPackageNode selected = getSelectedNode();

			IPackageFolder folder = PackageNodeFactory.createFolder();
			folder.setName(folderName);
			PackagesModel.instance().attach(selected, folder, new NullProgressMonitor());
		}
	}
	
	private void createFileset () {
		try {
			IPackageNode selected = getSelectedNode();
			WizardDialog dialog = new WizardDialog(getSite().getShell(), new FilesetWizard(null, selected));
			
			dialog.open();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void editSelectedNode () {
		IPackageNode node = getSelectedNode();
		if (node != null) {
			if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET) {
				IPackageFileSet fileset = (IPackageFileSet) node;
				WizardDialog dialog = new WizardDialog(getSite().getShell(), new FilesetWizard(fileset, node.getParent()));
				dialog.open();
			} else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE) {
				IPackage pkg = (IPackage) node;
				WizardDialog dialog = new WizardDialog(getSite().getShell(), new NewJARWizard(pkg));
				dialog.open();
			} else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER) {
				// folder can do the model save here. 
				IPackageFolder folder = (IPackageFolder) node;
				InputDialog dialog = new InputDialog(getSite().getShell(),
					PackagesUIMessages.ProjectPackagesView_createFolderDialog_title,
					PackagesUIMessages.ProjectPackagesView_createFolderDialog_message, folder.getName(), null);
				
				int response = dialog.open();
				if (response == Dialog.OK) {
					folder.setName(dialog.getValue());
					PackagesModel.instance().saveModel(folder.getProject(), new NullProgressMonitor());
				}
			} 
		}
	}
	
	private void deleteSelectedNode () {
		IPackageNode node = getSelectedNode();
		if (node != null) {
			IPackageNode parent = (IPackageNode) node.getParent();
			parent.removeChild(node);
			if( parent.getProject() != null ) {
				PackagesModel.instance().saveModel(parent.getProject(), new NullProgressMonitor());
			}
		}
	}

	
	
	/*
	 * Utility methods below
	 */

	private IWorkbenchPartSite getSite() {
		return ProjectPackagesView.getInstance().getSite();
	}

	private IPackageNode getSelectedNode () {
		IStructuredSelection selection = (IStructuredSelection) ProjectPackagesView.getInstance().getSelection();
		if (selection != null && !selection.isEmpty()) {
			Object selected = selection.getFirstElement();
			if( selected instanceof IPackageNode ) return ((IPackageNode)selected);
		}
		return null;
	}
	
	private ImageDescriptor platformDescriptor(String desc) {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(desc);
	}

}

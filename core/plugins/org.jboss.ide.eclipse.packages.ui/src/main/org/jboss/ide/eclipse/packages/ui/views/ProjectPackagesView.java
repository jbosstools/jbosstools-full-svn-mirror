package org.jboss.ide.eclipse.packages.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFolderImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.ui.ExtensionManager;
import org.jboss.ide.eclipse.packages.ui.NodeContribution;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.actions.BuildPackagesAction;
import org.jboss.ide.eclipse.packages.ui.actions.NewJARAction;
import org.jboss.ide.eclipse.packages.ui.providers.PackagesContentProvider;
import org.jboss.ide.eclipse.packages.ui.providers.PackagesLabelProvider;
import org.jboss.ide.eclipse.packages.ui.providers.PackagesContentProvider.FileSetProperty;
import org.jboss.ide.eclipse.packages.ui.util.PackagesListenerProxy;
import org.jboss.ide.eclipse.packages.ui.wizards.FilesetWizard;
import org.jboss.ide.eclipse.packages.ui.wizards.NewJARWizard;
import org.jboss.ide.eclipse.ui.IProjectSelectionListener;
import org.jboss.ide.eclipse.ui.util.ProjectSelectionService;

public class ProjectPackagesView extends ViewPart implements IProjectSelectionListener, IPackagesModelListener {
	
	private PageBook pageBook;
	private Composite noProjectSelectedComposite;
	private Composite loadingPackagesComposite;
	private Composite mainPage;
//	private Label projectLabel;
	private TreeViewer packageTree;
	private ProgressMonitorPart loadingProgress;
	private Action editAction, deleteAction, newFolderAction, newFilesetAction;
	private NewJARAction newJARAction;
	private BuildPackagesAction buildAllAction, buildPackageAction;
	private Action collapseAllAction;
	private GroupMarker newPackageContributions;
	private MenuManager newPackageManager, contextMenuManager;
	private IProject currentProject;
	private boolean loading;
	private PackagesContentProvider contentProvider;
	private ArrayList nodePopupMenuContributions;
	
	private static ProjectPackagesView _instance;
	public ProjectPackagesView ()
	{
		_instance = this;
		
		NodeContribution[] menus = ExtensionManager.findNodePopupMenuContributions();
		nodePopupMenuContributions = new ArrayList(Arrays.asList(menus));
		Collections.sort(nodePopupMenuContributions);
	}
	
	public static ProjectPackagesView instance()
	{
		return _instance;
	}
	
	public void createPartControl(Composite parent) {
		
		ProjectSelectionService.instance().addProjectSelectionListener(this);
		
		pageBook = new PageBook(parent, SWT.NONE);
		
		noProjectSelectedComposite = new Composite(pageBook, SWT.NONE);
		noProjectSelectedComposite.setLayout(new FillLayout());
		new Label(noProjectSelectedComposite, SWT.NONE).setText(PackagesUIMessages.ProjectPackagesView_noProjectSelectedMessage);
		
		createPackageLink = new Link(pageBook, SWT.NONE);
		createPackageLink.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				createPackagePressed();
			}
		});
				
		loadingPackagesComposite = new Composite(pageBook, SWT.NONE);
		loadingPackagesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		loadingProgress = new ProgressMonitorPart(loadingPackagesComposite, null);
		
		String message = PackagesUIMessages.ProjectPackagesView_noPackagesDefinedMessage;
		message += " " + PackagesUIMessages.ProjectPackagesView_createPackage_link;
		createPackageLink.setText(message);
		
		mainPage = new Composite(pageBook, SWT.NONE);
		mainPage.setLayout(createGridLayoutWithNoMargins(1));
		mainPage.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
//		final Composite labelComposite = new Composite(mainPage, SWT.NONE);
//		labelComposite.setLayout(createGridLayoutWithNoMargins(2));
//		labelComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
//
//		new Label(labelComposite, SWT.NONE).setImage(
//			PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
//		projectLabel = new Label(labelComposite, SWT.NONE);
//		projectLabel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		packageTree = new TreeViewer(mainPage, SWT.NONE);
		packageTree.getTree().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		contentProvider = new PackagesContentProvider();
		packageTree.setContentProvider(contentProvider);
		packageTree.setLabelProvider(new PackagesLabelProvider());
		pageBook.showPage(noProjectSelectedComposite);
		packageTree.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				Object selected = ((IStructuredSelection)packageTree.getSelection()).getFirstElement();
				if (selected instanceof IPackageNode)
					packageNodeSelected((IPackageNode) selected);
			}
		});
		createActions();
		createToolbar();
		createMenu();
		createContextMenu();
		
		getViewSite().setSelectionProvider(packageTree);
		
		PackagesCore.addPackagesModelListener(new PackagesListenerProxy(this));
//		new PackageNodeDragSource(packageTree);
//		new PackageDropTarget(packageTree);
	}
	
	private GridLayout createGridLayoutWithNoMargins (int columns)
	{
		GridLayout layout = new GridLayout(columns, false);
		layout.marginBottom = layout.marginHeight = layout.marginLeft = 0;
		layout.marginRight = layout.marginTop = layout.marginWidth = 0;
		
		return layout;
	}
	
	private void showCreatePackageLink ()
	{
		String message = 
			PackagesUIMessages.bind(PackagesUIMessages.ProjectPackagesView_noPackagesDefinedMessage, currentProject.getName());
		message += " " + PackagesUIMessages.ProjectPackagesView_createPackage_link;
		
		createPackageLink.setText(message);
		pageBook.showPage(createPackageLink);
	}
	
	private void packageNodeSelected (IPackageNode node)
	{
		
	}
	
	private ImageDescriptor platformDescriptor(String desc)
	{
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(desc);
	}
	
	private void createActions()
	{
		newJARAction = new NewJARAction();
		newJARAction.setEnabled(false);
		
		newFolderAction = new Action(PackagesUIMessages.ProjectPackagesView_newFolderAction_label, platformDescriptor(ISharedImages.IMG_OBJ_FOLDER)) { //$NON-NLS-1$
			public void run () {
				createFolder();
			}
		};
		
		newFilesetAction = new Action(PackagesUIMessages.ProjectPackagesView_newFilesetAction_label, PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_MULTIPLE_FILES)) { //$NON-NLS-1$
			public void run () {
				createFileset();
			}
		};
		
		collapseAllAction = new Action (PackagesUIMessages.ProjectPackagesView_collapseAllAction_label, PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_COLLAPSE_ALL)) { //$NON-NLS-1$
			public void run () {
				packageTree.collapseAll(); 
			}
		};
		collapseAllAction.setEnabled(false);
		
		deleteAction = new Action (PackagesUIMessages.ProjectPackagesView_deletePackageAction_label, platformDescriptor(ISharedImages.IMG_TOOL_DELETE)) { //$NON-NLS-1$
			public void run () {
				deleteSelectedNode();
			}	
		};
		
		editAction = new Action (PackagesUIMessages.ProjectPackagesView_editPackageAction_label, PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_PACKAGE_EDIT)) { //$NON-NLS-1$
			public void run () {
				editSelectedNode();
			}
		};

		buildAllAction = new BuildPackagesAction();
		buildAllAction.init(getViewSite().getWorkbenchWindow());
		
		buildPackageAction = new BuildPackagesAction();
		buildPackageAction.init(getViewSite().getWorkbenchWindow());
		
		newPackageContributions = new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS);
	}
	
	private void createToolbar ()
	{
		IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
		manager.add(buildAllAction);
		manager.add(newJARAction);
		manager.add(new Separator());
		manager.add(collapseAllAction);
	}
	
	private void createMenu ()
	{
		newPackageManager = new MenuManager(PackagesUIMessages.ProjectPackagesView_newPackageMenu_label, NEW_PACKAGE_MENU_ID);
		newPackageManager.add(newJARAction);
		newPackageManager.add(new Separator());
		newPackageManager.add(newPackageContributions);
		
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		
		manager.add(newPackageManager);
	}
	
	public static final String NEW_PACKAGE_MENU_ID = "org.jboss.ide.eclipse.packages.ui.newPackageMenu";
	public static final String NODE_CONTEXT_MENU_ID = "org.jboss.ide.eclipse.packages.ui.nodeContextMenu";
		
	private Link createPackageLink;
	
	private void createContextMenu ()
	{
		contextMenuManager = new MenuManager(NODE_CONTEXT_MENU_ID); //$NON-NLS-1$
		contextMenuManager.setRemoveAllWhenShown(true);
		contextMenuManager.addMenuListener(new IMenuListener () {
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection) packageTree.getSelection();
				if (selection != null && !selection.isEmpty())
				{
					if (!(selection.getFirstElement() instanceof IPackageNode)) return;
					
					IPackageNode node = (IPackageNode) selection.getFirstElement();
					
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE
							|| node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
					{	
						newJARAction.setEnabled(true);
						manager.add(newPackageManager);
						
						manager.add(newFolderAction);
						manager.add(newFilesetAction);
						manager.add(new Separator());
					}
					
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE)
					{
						editAction.setText(PackagesUIMessages.ProjectPackagesView_editPackageAction_label); //$NON-NLS-1$
						deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deletePackageAction_label); //$NON-NLS-1$
						editAction.setImageDescriptor(PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_PACKAGE_EDIT));
						manager.add(buildPackageAction);
					}
					else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
					{
						editAction.setText(PackagesUIMessages.ProjectPackagesView_editFolderAction_label); //$NON-NLS-1$
						deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deleteFolderAction_label); //$NON-NLS-1$
						editAction.setImageDescriptor(platformDescriptor(ISharedImages.IMG_OBJ_FOLDER));
					}
					else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						editAction.setText(PackagesUIMessages.ProjectPackagesView_editFilesetAction_label); //$NON-NLS-1$
						deleteAction.setText(PackagesUIMessages.ProjectPackagesView_deleteFilesetAction_label); //$NON-NLS-1$
						editAction.setImageDescriptor(PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_MULTIPLE_FILES));
					}
					manager.add(editAction);
					manager.add(deleteAction);
					
					addContextMenuContributions(node);
				}
				else {
					manager.add(newPackageManager);
				}
				
				GroupMarker additions = new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS);
				manager.add(additions);
			}
		});
		
		Menu treeContextMenu = contextMenuManager.createContextMenu(packageTree.getTree());
		packageTree.getTree().setMenu(treeContextMenu);
		
		getViewSite().registerContextMenu(NEW_PACKAGE_MENU_ID, newPackageManager, packageTree);
		getViewSite().registerContextMenu(NODE_CONTEXT_MENU_ID, contextMenuManager, packageTree);
		
//		Menu emptyContextMenu = manager.createContextMenu(createPackageLink);
//		createPackageLink.setMenu(emptyContextMenu);
	}
	
	private void addContextMenuContributions (IPackageNode context)
	{
		Collections.sort(nodePopupMenuContributions);
		
		for (Iterator iter = nodePopupMenuContributions.iterator(); iter.hasNext(); )
		{
			NodeContribution contribution = (NodeContribution) iter.next();
				
			addContextMenuContribution(contribution, context);
		}
	}
	
	private void addContextMenuContribution(final NodeContribution contribution, IPackageNode context)
	{
		if (contribution.isEnabledForNodeType(context.getNodeType())
			&& contribution.getActionDelegate().isEnabledFor(context))
		{
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
					contribution.getActionDelegate().init(ProjectPackagesView.this);
					contribution.getActionDelegate().run(this);
				}
			};
			
			contextMenuManager.add(action);
		}
	}
	
	
	public void projectSelected(final IProject project)
	{
		if (project == null) return;
		if (project.equals(currentProject)) return;
		
		currentProject = project;
//		projectLabel.setText(project.getName());
		
		if (PackagesCore.projectHasPackages(project))
		{
			pageBook.showPage(loadingPackagesComposite);
			getSite().getShell().getDisplay().asyncExec(new Runnable () {
				public void run ()
				{
					loading = true;
					
					IPackage packages[] = PackagesCore.getProjectPackages(project, loadingProgress);
					
					if (packages == null) {
						showCreatePackageLink();
					}
					
					else {
						pageBook.showPage(mainPage);
						if (packageTree.getInput() != packages)
						{
							packageTree.setInput(packages);
						}
						collapseAllAction.setEnabled(true);
					}
					
					loading = false;
				}
			});
		}
		else {
			showCreatePackageLink();
			collapseAllAction.setEnabled(false);
		}
		
		newJARAction.setEnabled(true);
		newJARAction.setProject(currentProject);
	}
	
	private void createPackagePressed ()
	{
		newJARAction.run();
//		if (createLinkMenuManager == null)
//		{
//			createLinkMenuManager = new MenuManager();
//			createLinkMenuManager.setRemoveAllWhenShown(true);
//			
//			createLinkMenuManager.addMenuListener(new IMenuListener () {
//				public void menuAboutToShow(IMenuManager manager) {
//					//	Add the newPackageManager's contributions at the top level so we can avoid redundancy in the UI
//					createLinkMenuManager.add(newPackageContributions);
//					
//					IContributionItem items[] = newPackageManager.getItems();
//					for (int i = 0; i < items.length; i++)
//					{
//						if (items[i] instanceof ActionContributionItem)
//						{
//							IAction action = ((ActionContributionItem)items[i]).getAction();
//							createLinkMenuManager.add(action);
//						}
//						else if (items[i] instanceof Separator)
//						{
//							createLinkMenuManager.add(new Separator());
//						}
//					}
//				}
//			});
//			
//			createLinkMenu = createLinkMenuManager.createContextMenu(createPackageLink);
//		}
//		
//		createLinkMenu.setVisible(true);
	}
	
	private void createFolder ()
	{
		IInputValidator validator = new IInputValidator () {
			public String isValid(String newText) {
				IPackageNode selected = getSelectedNode();
				
				boolean folderExists = false;
				IPackageNode[] folders = selected.getChildren(IPackageNode.TYPE_PACKAGE_FOLDER);
				for (int i = 0; i < folders.length; i++)
				{
					IPackageFolder folder = (IPackageFolder) folders[i];
					if (folder.getName().equals(newText))
					{
						folderExists = true; break;
					}
				}
				
				if (folderExists)
				{
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
		if (response == Dialog.OK)
		{
			String folderName = dialog.getValue();
			IPackageNode selected = getSelectedNode();

			IPackageFolder folder = PackagesCore.createPackageFolder(selected.getProject());
			folder.setName(folderName);
			
			selected.addChild(folder);
		}
	}
	
	private void createFileset ()
	{
		IPackageNode selected = getSelectedNode();
		WizardDialog dialog = new WizardDialog(getSite().getShell(), new FilesetWizard(null, selected));
		
		dialog.open();
	}
	
	private IPackageNode getSelectedNode ()
	{
		IStructuredSelection selection = (IStructuredSelection) packageTree.getSelection();
		if (selection != null && !selection.isEmpty())
		{
			Object selected = selection.getFirstElement();
			if (selected instanceof IPackageNode) return (IPackageNode)selected;
			else if (selected instanceof FileSetProperty) return ((FileSetProperty)selected).getFileSet();
		}
		return null;
	}
	
	private void editSelectedNode ()
	{
		IPackageNode node = getSelectedNode();
		if (node != null)
		{
			if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
			{
				IPackageFileSet fileset = (IPackageFileSet) node;
				WizardDialog dialog = new WizardDialog(getSite().getShell(), new FilesetWizard(fileset, node.getParent()));
				dialog.open();
			}
			else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
			{
				IPackageFolder folder = (IPackageFolder) node;
				InputDialog dialog = new InputDialog(getSite().getShell(),
					PackagesUIMessages.ProjectPackagesView_createFolderDialog_title,
					PackagesUIMessages.ProjectPackagesView_createFolderDialog_message, folder.getName(), null);
				
				int response = dialog.open();
				if (response == Dialog.OK)
				{
					folder.setName(dialog.getValue());
					PackagesModel.instance().saveModel(folder.getProject(), new NullProgressMonitor());
					((PackageFolderImpl)folder).flagAsChanged();
				}
			}
			else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE)
			{
				IPackage pkg = (IPackage) node;
				WizardDialog dialog = new WizardDialog(getSite().getShell(), new NewJARWizard(pkg));
				dialog.open();
			}
		}
	}
	
	private void deleteSelectedNode ()
	{
		IPackageNode node = getSelectedNode();
		if (node != null)
		{
			IPackageNode parent = (IPackageNode) node.getParent();
			if (parent != null)
			{
				parent.removeChild(node);
			}
			else {
				// top level package
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE) {
					PackagesModel.instance().removePackage((IPackage)node);
				}
			}
		}
	}
	
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void projectRegistered(IProject project) {
	}
	
	public void packageNodeAdded(IPackageNode added) {
		if (!loading && !packageTree.getTree().isDisposed())
		{
			pageBook.showPage(packageTree.getTree());
			if (added.getParent() == null) {
				packageTree.setInput(PackagesCore.getProjectPackages(added.getProject(), null));
				packageTree.refresh();
			}
			else {
				packageTree.add(added.getParent(), added);
//				packageTree.refresh();
				packageTree.expandToLevel(added.getParent(), 1);
			}
			
			if (added.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
			{
				contentProvider.addFilesetProperties(currentProject, (IPackageFileSet)added);
			}
		}
	}
	
	public void packageNodeChanged(IPackageNode changed) {
		if (!packageTree.getTree().isDisposed()) {
			if (changed.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
			{
				IPackageFileSet fileset = (IPackageFileSet) changed;
				contentProvider.addFilesetProperties(fileset.getProject(), fileset);
			}
			
			packageTree.refresh(changed);
		}
	}
	
	public void packageNodeRemoved(IPackageNode removed) {
		if (!packageTree.getTree().isDisposed()) {
			packageTree.remove(removed);
		}
	}
	
	public void packageNodeAttached(IPackageNode attached) {
		packageNodeAdded(attached);
	}
	
	public IProject getCurrentProject ()
	{
		return currentProject;
	}
}

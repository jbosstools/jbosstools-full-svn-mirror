package org.jboss.ide.eclipse.archives.ui.views;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelRootNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.ExtensionManager;
import org.jboss.ide.eclipse.archives.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;
import org.jboss.ide.eclipse.archives.ui.actions.NewArchiveAction;
import org.jboss.ide.eclipse.archives.ui.providers.ArchivesContentProvider;
import org.jboss.ide.eclipse.archives.ui.providers.ArchivesLabelProvider;

public class ProjectArchivesView extends ViewPart implements IArchiveModelListener {
	
	protected static ProjectArchivesView instance;
	public static ProjectArchivesView getInstance() {
		return instance;
	}
	
	
	protected ISelectionListener selectionListener;
	public ProjectArchivesView() {
		instance = this;
		selectionListener = createSelectionListener();
	}
	
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addPostSelectionListener(selectionListener);
		ArchivesModel.instance().addModelListener(this);
	}
	
    public void dispose() {
    	super.dispose();
    	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removePostSelectionListener(selectionListener);
    	ArchivesModel.instance().removeModelListener(this);
    }

	
	protected ISelectionListener createSelectionListener() {
		return new INullSelectionListener() {
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (!(selection instanceof IStructuredSelection))
					return;
				
				Object element = ((IStructuredSelection)selection).getFirstElement();
				IProject project = getProject(element);
				if( project != null ) {
					viewSelectionChanged(project);
				} else {
					if( getCurrentProject() != null && !getCurrentProject().exists() ) {
						viewSelectionChanged(null);
					}
				}
			}
			
			public IProject getProject (Object element) {
				if( element instanceof IStructuredSelection)
					element = ((IStructuredSelection)element).getFirstElement();
				
				if (element instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable)element;
					IResource resource = (IResource) adaptable.getAdapter(IResource.class);
					if( resource != null )
						return resource.getProject();
				}
				return null;
			}

		};
	}
	
	
	// parts
	private PageBook book;
	private IProject project;
	private ArchivesContentProvider contentProvider = new ArchivesContentProvider();
	private ArchivesLabelProvider labelProvider = new ArchivesLabelProvider();
	private Composite emptyComposite, viewerComposite, loadingPackagesComposite, noSelectionComposite;
	private IProgressMonitor loadingProgress;
	private TreeViewer packageViewer;
	private ArchivesMenuHandler menuHandler;
	public void createPartControl(Composite parent) {
		book = new PageBook(parent, SWT.NONE);
		addEmptyComposite(book);
		addLoadingComposite(book);
		addViewerComposite(book);
		addNoSelectionComposite(book);
		book.showPage(noSelectionComposite);
		menuHandler = new ArchivesMenuHandler(packageViewer);
	}
	
	protected void addNoSelectionComposite(PageBook book) {
		noSelectionComposite = new Composite(book, SWT.NONE);
		noSelectionComposite.setLayout(new FillLayout());
		Label label = new Label(noSelectionComposite, SWT.NONE);
		label.setText(ArchivesUIMessages.ProjectPackagesView_noProjectSelectedMessage);
	}
	protected void addEmptyComposite(PageBook book) {
		emptyComposite = new Composite(book, SWT.NONE);
		emptyComposite.setLayout(new FormLayout());
		Label l = new Label(emptyComposite, SWT.NONE);
		l.setText(ArchivesUIMessages.ProjectPackagesView_createPackagesMessage);
		
		Composite actionsComposite = new Composite(emptyComposite, SWT.NONE);
		
		
		FormData lData = new FormData();
		lData.left = new FormAttachment(0,5);
		lData.top = new FormAttachment(0,5);
		l.setLayoutData(lData);
		
		FormData actionsData = new FormData();
		actionsData.left     = new FormAttachment(0,5);
		actionsData.top      = new FormAttachment(l,5);
		actionsData.bottom   = new FormAttachment(100,-5);

		actionsComposite.setLayoutData(actionsData);
		
		actionsComposite.setLayout(new GridLayout(1, false));
		addNewPackageActions(actionsComposite);
		
	}
		
	
	private void addNewPackageActions (Composite composite) {
		NewArchiveAction[] actions = ExtensionManager.findNewArchiveActions();
		for (int i = 0; i < actions.length; i++) {
			final NewArchiveAction action = actions[i];
			
			Composite linkComposite = new Composite(composite, SWT.NONE);
			linkComposite.setLayout(createGridLayoutWithNoMargins(2));
			linkComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			new Label(linkComposite, SWT.NONE).setImage(action.getIcon());
			
			Link actionLink = new Link(linkComposite, SWT.NONE);
			actionLink.setText("<a href=\"create\">" + action.getLabel() + "</a>");
			actionLink.addSelectionListener(new SelectionListener () {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				
				public void widgetSelected(SelectionEvent e) {
					action.getAction().run(null);
				}
			});
		}
	}
	
	private GridLayout createGridLayoutWithNoMargins (int columns) {
		GridLayout layout = new GridLayout(columns, false);
		layout.marginBottom = layout.marginHeight = layout.marginLeft = 0;
		layout.marginRight = layout.marginTop = layout.marginWidth = 0;
		
		return layout;
	}

	protected void addLoadingComposite(PageBook book) {
		loadingPackagesComposite = new Composite(book, SWT.NONE);
		loadingPackagesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		loadingProgress = new ProgressMonitorPart(loadingPackagesComposite, null);

	}
	protected void addViewerComposite(PageBook book) {
		viewerComposite = new Composite(book, SWT.NONE);
		viewerComposite.setLayout(new FillLayout());
		packageViewer = new TreeViewer(viewerComposite, SWT.NONE);
		packageViewer.setContentProvider(contentProvider);
		packageViewer.setLabelProvider(labelProvider);
	}
	
	
	public void setFocus() {
	}
	public void viewSelectionChanged(IProject project) {
		if( project == null ) {
			this.project = null;
			packageViewer.setInput(null);
			book.showPage(noSelectionComposite);
			return;
		}
		
		IArchiveModelRootNode node = (IArchiveModelRootNode) packageViewer.getInput();
		if (node != null)
		{
			IPath projectPath = node.getProjectPath();
			
			if( project.getLocation().equals(projectPath)) 
				return;
		}
		
		if( !project.isAccessible() ) {
			 book.showPage(noSelectionComposite);
		} else if(  ArchivesModelCore.packageFileExists(project.getLocation()) ) {
			if( ArchivesModelCore.projectRegistered(project.getLocation()))
				book.showPage(viewerComposite);
			else {
				this.project = project;
				book.showPage(loadingPackagesComposite);
				if( PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS, project, true)) {
					registerProjects(getAllProjectsWithPackages(), this.project);
				} else {
					registerProjects(new IProject[] {this.project}, this.project);
				}
				return;
			}
		} else { 
			book.showPage(emptyComposite);
		}
		
		this.project = project;
		packageViewer.setInput(ArchivesModel.instance().getRoot(project.getLocation()));
	}
	
	public IProject getCurrentProject() {
		return project;
	}
	
	/**
	 * Registers the projects if and only if a file exists already
	 * @param projects
	 * @param projectToShow
	 */
	protected void registerProjects(final IProject[] projects, final IProject projectToShow) {
		getSite().getShell().getDisplay().asyncExec(new Runnable () {
			public void run () {
				for( int i = 0; i < projects.length; i++ ) {
					try {
						ArchivesModel.instance().registerProject(projects[i].getLocation(), loadingProgress);
					} catch( ArchivesModelException ame ) {
						IStatus status = new Status(IStatus.ERROR, PackagesUIPlugin.PLUGIN_ID, ame.getMessage(), ame);
						PackagesUIPlugin.getDefault().getLog().log(status);
					}
				}
				book.showPage(viewerComposite);
				packageViewer.setInput(ArchivesModel.instance().getRoot(projectToShow.getLocation()));
			}
		});
	}
	
	public IProject[] getAllProjectsWithPackages() {
		IProject[] projects2 = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList list = new ArrayList();
		for( int i = 0; i < projects2.length; i++ ) {
			if( projects2[i].isAccessible() && ArchivesModelCore.packageFileExists(projects2[i].getLocation())) {
				list.add(projects2[i]);
			}
		}
		return (IProject[]) list.toArray(new IProject[list.size()]);
	}
	public IStructuredSelection getSelection() {
		return (IStructuredSelection)packageViewer.getSelection();
	}


	public void modelChanged(IArchiveNodeDelta delta) {
		boolean update = true;
		try {
			if( project == null ) return;
			if( delta.getPostNode() == null && delta.getPreNode() == null ) return;
			if( delta.getPreNode() == null ) update = delta.getPostNode().getProjectPath().equals(project.getLocation());
			else if( delta.getPostNode() == null ) update = delta.getPreNode().getProjectPath().equals(project.getLocation());
			else update = delta.getPreNode().getProjectPath().equals(project.getLocation()) || delta.getPostNode().getProjectPath().equals(project);
		} catch( Exception e ) {}

		if( !update ) return;
			
		final IArchiveNode[] topChanges;
		if( delta.getKind() == IArchiveNodeDelta.DESCENDENT_CHANGED) 
			topChanges = getChanges(delta);
		else if( delta.getKind() == IArchiveNodeDelta.NO_CHANGE)
			topChanges = new IArchiveNode[]{};
		else
			topChanges = new IArchiveNode[]{delta.getPostNode()};
		
		// now go through and refresh them
		getSite().getShell().getDisplay().asyncExec(new Runnable () {
			public void run () {
				for( int i = 0; i < topChanges.length; i++ ) {
					if( topChanges.length == 1 && topChanges[0] instanceof IArchiveModelRootNode) {
						// we have a changed IArchiveModelNode. Something's different
						IArchiveModelRootNode inputModel = (IArchiveModelRootNode) packageViewer.getInput();
						IArchiveModelRootNode newModel = (IArchiveModelRootNode)topChanges[0];
						if( inputModel == null || (inputModel.getProjectPath().equals(newModel.getProjectPath()) && inputModel != newModel)) {
							// they have the same path but are different objects. 
							// Model was probably reloaded from disk and could be completely different
							book.showPage(viewerComposite);
							packageViewer.setInput(newModel);	
							packageViewer.expandAll();
							return;
						} else {
							packageViewer.refresh();
							return;
						}
					} else {
						// just refresh whatever is the top changed element (archive probably)
						packageViewer.refresh(topChanges[i]);
						packageViewer.expandToLevel(topChanges[i], 1);
					}
				}
			}
		});

	}
	protected IArchiveNode[] getChanges(IArchiveNodeDelta delta) {
		
		IArchiveNodeDelta[] children = delta.getAllAffectedChildren();
		ArrayList list = new ArrayList();
		for( int i = 0; i < children.length; i++ ) {
			if( children[i].getKind() == IArchiveNodeDelta.DESCENDENT_CHANGED) 
				list.addAll(Arrays.asList(getChanges(children[i])));
			else
				list.add(children[i].getPostNode());
		}
		return (IArchiveNode[]) list.toArray(new IArchiveNode[list.size()]);
	}
	
	public void refreshViewer(final Object node) {
		getSite().getShell().getDisplay().asyncExec(new Runnable () {
			public void run () {
				if( node == null ) {
					Object[] els = packageViewer.getExpandedElements();
					packageViewer.refresh();
					packageViewer.setExpandedElements(els);
				} else {
					packageViewer.refresh(node);
				}
			}
		});
	}
}

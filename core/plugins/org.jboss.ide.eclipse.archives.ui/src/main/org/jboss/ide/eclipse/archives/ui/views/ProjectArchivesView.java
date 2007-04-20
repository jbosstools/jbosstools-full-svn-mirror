package org.jboss.ide.eclipse.archives.ui.views;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.ExtensionManager;
import org.jboss.ide.eclipse.archives.ui.actions.NewPackageAction;
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
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);
		ArchivesCore.getInstance().addModelListener(this);
	}
	
	
	protected ISelectionListener createSelectionListener() {
		return new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (!(selection instanceof IStructuredSelection))
					return;
				
				Object element = ((IStructuredSelection)selection).getFirstElement();
				IProject project = getProject(element);
				viewSelectionChanged(project);
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
	private Composite emptyComposite, viewerComposite, loadingPackagesComposite;
	private IProgressMonitor loadingProgress;
	private TreeViewer packageViewer;
	private PackagesMenuHandler menuHandler;
	public void createPartControl(Composite parent) {
		book = new PageBook(parent, SWT.NONE);
		addEmptyComposite(book);
		addLoadingComposite(book);
		addViewerComposite(book);
		menuHandler = new PackagesMenuHandler(packageViewer);
	}
	
	protected void addEmptyComposite(PageBook book) {
		emptyComposite = new Composite(book, SWT.NONE);
		emptyComposite.setLayout(new FormLayout());
		Label l = new Label(emptyComposite, SWT.NONE);
		l.setText("Project has no packages. Create an Archive");
		
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
		NewPackageAction[] actions = ExtensionManager.findNewArchiveActions();
		for (int i = 0; i < actions.length; i++) {
			final NewPackageAction action = actions[i];
			
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
			return;
		}
		
		if( project.equals(packageViewer.getInput())) 
			return;
		
		if( ArchivesCore.packageFileExists(project) ) { 
			if( ArchivesCore.projectRegistered(project))
				book.showPage(viewerComposite);
			else {
				this.project = project;
				book.showPage(loadingPackagesComposite);
				registerProject(project);
				return;
			}
		} else { 
			book.showPage(emptyComposite);
		}
		
		this.project = project;
		packageViewer.setInput(ArchivesModel.instance().getRoot(project));
	}
	
	public IProject getCurrentProject() {
		return project;
	}
	
	protected void registerProject(final IProject project) {
		getSite().getShell().getDisplay().asyncExec(new Runnable () {
			public void run () {
				ArchivesModel.instance().registerProject(project, loadingProgress);
				book.showPage(viewerComposite);
				packageViewer.setInput(ArchivesModel.instance().getRoot(project));
			}
		});
	}
	
	public IStructuredSelection getSelection() {
		return (IStructuredSelection)packageViewer.getSelection();
	}


	public void modelChanged(IArchiveNodeDelta delta) {
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
					packageViewer.refresh(topChanges[i]);
				}
			}
		});

	}
	protected IArchiveNode[] getChanges(IArchiveNodeDelta delta) {
		
		IArchiveNodeDelta[] children = delta.getAffectedChildren();
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
				if( node == null )
					packageViewer.refresh();
				else
					packageViewer.refresh(node);
			}
		});
	}
}

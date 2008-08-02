package org.jboss.ide.eclipse.archives.ui.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.jboss.ide.eclipse.archives.core.build.ArchiveBuildDelegate;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;

public class ProjectArchivesCommonView extends CommonNavigator implements IArchiveModelListener {
	protected static ProjectArchivesCommonView instance;
	protected ISelectionListener selectionListener;
	protected IProject currentProject;
	public static ProjectArchivesCommonView getInstance() { return instance; }
	public ProjectArchivesCommonView() {
		super();
		instance = this;
		selectionListener = createSelectionListener();
	}
	protected IAdaptable getInitialInput() {
		currentProject = null;
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		addBuildActionToSite();
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
				if( part == instance ) 
					return;
				if (!(selection instanceof IStructuredSelection))
					return;
				
				Object element = ((IStructuredSelection)selection).getFirstElement();
				IProject project = getProject(element);
				if( project != null ) {
					currentProject = project;
					if( showProjectRoot())
						getCommonViewer().setInput(ResourcesPlugin.getWorkspace().getRoot());
					else
						getCommonViewer().setInput(currentProject);
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
	
	public void modelChanged(IArchiveNodeDelta delta) {
		Display.getDefault().asyncExec(new Runnable() { 
			public void run() {
				getCommonViewer().refresh();
			}
		});
	}
	
	public IProject getCurrentProject() { 
		return currentProject;
	}
	private boolean showProjectRoot () {
		return PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT);
	}
	
	public void addBuildActionToSite() {
		Action buildAction = new Action("", ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_BUILD_PACKAGES)) {
			public void run() {
				buildSelection(getSelectedObject());
			}
		};
		IActionBars bars = ((IViewSite)getSite()).getActionBars();
		bars.getToolBarManager().add(buildAction);
	}
	
	private void buildSelection(final Object selected) {
		new Job("Build Archive Node") {
			protected IStatus run(IProgressMonitor monitor) {
				if( selected == null ) return Status.OK_STATUS;
				if( selected instanceof IProject ) {
					new ArchiveBuildDelegate().fullProjectBuild(((IProject)selected).getLocation());
				} else if (selected instanceof IArchiveNode &&  
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

	private Object getSelectedObject() {
		IStructuredSelection selection = (IStructuredSelection)getCommonViewer().getSelection();
		if (selection != null && !selection.isEmpty())
			return selection.getFirstElement();
		return null;
	}
}

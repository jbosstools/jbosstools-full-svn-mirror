package org.jboss.ide.eclipse.archives.ui.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.ide.eclipse.archives.core.build.RegisterArchivesJob;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;

public class ArchivesContentProvider implements ITreeContentProvider {

	protected ArrayList<Viewer> viewersInUse = new ArrayList<Viewer>();
	protected ArrayList<IProject> loadingProjects = new ArrayList<IProject>();
	
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof IProject ) {
			IProject p = (IProject)parentElement;

			// if currently loading, always send a delay
			if( loadingProjects.contains(p))
				return new Object[]{new DelayProxy(p)};
			
			if( ArchivesModel.instance().isProjectRegistered(p.getLocation()))
				return ArchivesModel.instance().getRoot(p.getLocation()).getAllChildren();
			if( ArchivesModel.instance().canReregister(p.getLocation())) {
				loadingProjects.add(p);
				DelayProxy dp = new DelayProxy(p);
				launchRegistrationThread(dp);
				return new Object[]{dp};
			}
		}
		if( parentElement instanceof IArchiveNode ) 
			return ((IArchiveNode)parentElement).getAllChildren();
		return new Object[0];
	}
	
	public static class DelayProxy {
		public IProject project;
		public DelayProxy(IProject p) {this.project = p; }
	}
	
	protected void launchRegistrationThread(final DelayProxy dp) {
		Runnable callback = new Runnable() { 
			public void run() {
				Display.getDefault().asyncExec(new Runnable() { 
					public void run() {
						loadingProjects.remove(dp.project);
						Iterator it = viewersInUse.iterator();
						while(it.hasNext()) {
							Viewer next = ((Viewer)it.next());
							if( next instanceof StructuredViewer)
								((StructuredViewer)next).refresh(dp.project);
							else
								next.refresh();	
						}
					}
				});
			}
		};
		RegisterArchivesJob job = new RegisterArchivesJob(new IProject[]{dp.project}, callback);
		job.schedule();
	}
	
	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if( element instanceof IArchiveNode )
			return getChildren(element).length > 0;
		if( element instanceof IResource ) 
			return ArchivesModel.instance().canReregister(((IResource)element).getLocation());
		return true;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if( newInput != null) {
			if( !viewersInUse.contains(viewer)) {
				viewersInUse.add(viewer);
			}
		} else {
			viewersInUse.remove(viewer);
		}
	}

}

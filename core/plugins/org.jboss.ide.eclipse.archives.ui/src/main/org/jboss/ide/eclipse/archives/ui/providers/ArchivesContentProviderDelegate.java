package org.jboss.ide.eclipse.archives.ui.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.ide.eclipse.archives.core.build.RegisterArchivesJob;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;
import org.jboss.ide.eclipse.archives.ui.views.ProjectArchivesCommonView;

public class ArchivesContentProviderDelegate implements ITreeContentProvider, IArchiveModelListener {

	// Because all viewers need to be updated, this must be a singleton
	private static ArchivesContentProviderDelegate singleton;
	public static ArchivesContentProviderDelegate getDefault() {
		if( singleton == null )
			singleton = new ArchivesContentProviderDelegate();
		return singleton;
	}
	
	public static class WrappedProject {
		public static final int NAME = 1;
		public static final int CATEGORY = 2;
		private IProject element;
		private int type;
		public WrappedProject(IProject element) { this.element = element; }
		public WrappedProject(IProject element, int type) { this.element = element; this.type = type;}
		public IProject getElement() { return element; }
		public int getType() { return type; }
		public boolean equals(Object otherObject) {
			if( otherObject instanceof WrappedProject && element.equals(((WrappedProject)otherObject).element))
					return true;
			return false;
		}
	}

	public static class DelayProxy {
		public WrappedProject wProject;
		public IProject project;
		public DelayProxy(WrappedProject wp) {
			this.wProject = wp;
			this.project = wProject.element;
		}
	}
	
	public ArchivesContentProviderDelegate() {
		ArchivesModel.instance().addModelListener(this);
	}
	public ArchivesContentProviderDelegate(boolean addListener) {
		if( addListener)
			ArchivesModel.instance().addModelListener(this);
	}
	
	protected ArrayList<Viewer> viewersInUse = new ArrayList<Viewer>();
	protected ArrayList<IProject> loadingProjects = new ArrayList<IProject>();

	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof WrappedProject ) {
			WrappedProject wp = (WrappedProject)parentElement;
			IProject p = ((WrappedProject)parentElement).getElement();

			// if currently loading, always send a delay
			if( loadingProjects.contains(p))
				return new Object[]{new DelayProxy(wp)};
			
			if( ArchivesModel.instance().isProjectRegistered(p.getLocation()))
				return ArchivesModel.instance().getRoot(p.getLocation()).getAllChildren();
			if( ArchivesModel.instance().canReregister(p.getLocation())) {
				loadingProjects.add(p);
				DelayProxy dp = new DelayProxy(wp);
				launchRegistrationThread(dp);
				return new Object[]{dp};
			}
		}
		if( parentElement instanceof IArchiveNode ) 
			return ((IArchiveNode)parentElement).getAllChildren();
		return new Object[0];
	}

	
	protected void launchRegistrationThread(final DelayProxy dp) {
		Runnable callback = new Runnable() { 
			public void run() {
				Display.getDefault().asyncExec(new Runnable() { 
					public void run() {
						loadingProjects.remove(dp.project);
						Iterator<Viewer> it = viewersInUse.iterator();
						while(it.hasNext()) {
							Viewer next = ((Viewer)it.next());
							if( shouldRefreshProject(next))
								((StructuredViewer)next).refresh(dp.project);
							else if( next instanceof StructuredViewer)
								((StructuredViewer)next).refresh(dp.wProject);
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
	
	protected boolean shouldRefreshProject(Viewer viewer) {
		if( viewer == ProjectArchivesCommonView.getInstance().getCommonViewer() && 
				!PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT))
			return true;
		return false;
	}
	
	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if( element instanceof IArchiveNode )
			return getChildren(element).length > 0;
		if( element instanceof IResource ) 
			return ArchivesModel.instance().canReregister(((IResource)element).getLocation());
		if( element == ArchivesRootContentProvider.NO_PROJECT)
			return false;
		if( element instanceof DelayProxy)
			return false;
		return true;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		ArchivesModel.instance().removeModelListener(this);
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
	public void modelChanged(IArchiveNodeDelta delta) {
			
		final IArchiveNode[] topChanges;
		if( delta.getKind() == IArchiveNodeDelta.DESCENDENT_CHANGED) 
			topChanges = getChanges(delta);
		else if( delta.getKind() == IArchiveNodeDelta.NO_CHANGE)
			return;
		else
			topChanges = new IArchiveNode[]{delta.getPostNode()};
		
		final Viewer[] viewers = (Viewer[]) viewersInUse.toArray(new Viewer[viewersInUse.size()]);
		
		// now go through and refresh them
		Display.getDefault().asyncExec(new Runnable () {
			public void run () {
				for( int i = 0; i < topChanges.length; i++ ) {
					for (int j = 0; j < viewers.length; j++ ) {
						if( viewers[j] instanceof StructuredViewer ) {
							((StructuredViewer)viewers[j]).refresh(topChanges[i]);
							if( viewers[j] instanceof TreeViewer ) {
								((TreeViewer)viewers[j]).expandToLevel(topChanges[i], 1);
							}
						} else 
							viewers[j].refresh();
					}
				}
			}
		});
	}

	protected IArchiveNode[] getChanges(IArchiveNodeDelta delta) {
		
		IArchiveNodeDelta[] children = delta.getAllAffectedChildren();
		ArrayList<IArchiveNode> list = new ArrayList<IArchiveNode>();
		for( int i = 0; i < children.length; i++ ) {
			if( children[i].getKind() == IArchiveNodeDelta.DESCENDENT_CHANGED) 
				list.addAll(Arrays.asList(getChanges(children[i])));
			else
				list.add(children[i].getPostNode());
		}
		return list.toArray(new IArchiveNode[list.size()]);
	}

}

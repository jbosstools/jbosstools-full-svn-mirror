package org.jboss.ide.eclipse.archives.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.ide.IDE;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.providers.ArchivesLabelProvider;

public class ArchiveNodeDestinationDialog extends ElementTreeSelectionDialog {

	public ArchiveNodeDestinationDialog(Shell parent, Object destination,
			boolean showWorkspace, boolean showNodes) {
		super(parent, new DestinationLabelProvider(),
				new DestinationContentProvider(showWorkspace, showNodes));
		setAllowMultiple(false);
		setTitle(ArchivesUIMessages.PackageNodeDestinationDialog_title);
		setInput(ResourcesPlugin.getWorkspace());
	}

	private static class DestinationContentProvider implements
			ITreeContentProvider {
		private static final Object[] NO_CHILDREN = new Object[0];
		private boolean showWorkspace, showNodes;

		public DestinationContentProvider(boolean showWorkspace,
				boolean showNodes) {
			this.showWorkspace = showWorkspace;
			this.showNodes = showNodes;
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof IArchiveNode) {
				IArchiveNode node = (IArchiveNode) parentElement;
				List children = new ArrayList(Arrays.asList(node
						.getAllChildren()));
				for (Iterator iter = children.iterator(); iter.hasNext();) {
					IArchiveNode child = (IArchiveNode) iter.next();
					if (child.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET)
						iter.remove();
				}
				return children.toArray();
			} else if (parentElement instanceof IContainer) {
				IContainer container = (IContainer) parentElement;
				ArrayList result = new ArrayList();
				if (showWorkspace) {
					try {
						IResource members[] = container.members();
						for (int i = 0; i < members.length; i++) {
							if (members[i].getType() == IResource.FOLDER)
								result.add(members[i]);
						}
					} catch (CoreException e) {
					}
				}
				if (showNodes && parentElement instanceof IProject) {
					IPath path = ((IProject)parentElement).getLocation();
					IArchive[] archives = ModelUtil.getProjectArchives(path);
					result.addAll(Arrays.asList(archives));
				}
				return result.toArray();
			}
			return NO_CHILDREN;
		}

		public Object getParent(Object element) {
			if (element instanceof IArchiveNode) {
				IArchiveNode node = (IArchiveNode) element;
				return node.getParent();
			} else if (element instanceof IContainer) {
				IContainer container = (IContainer) element;
				return container.getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public Object[] getElements(Object inputElement) {
			 ArrayList destinations = new ArrayList();

			 if (showWorkspace) {
				 destinations.addAll(Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()));
			 }
			 
			 IProgressMonitor monitor = new NullProgressMonitor();
			 
			 if( showNodes ) {
				 // add ALL packages from ALL projects
				 IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				 for( int i = 0; i < projects.length; i++ ) {
					 if( projects[i].isAccessible()) {
						 IArchive[] archives = ModelUtil.getProjectArchives(projects[i].getLocation());
						 List tmp = Arrays.asList(archives);
						 if( tmp.size() > 0 && !destinations.contains(projects[i]))
							 destinations.add(projects[i]);
					 }
				 }
			 }
			 return destinations.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private static class DestinationLabelProvider implements ILabelProvider {
		private ArchivesLabelProvider delegate;

		public DestinationLabelProvider() {
			delegate = new ArchivesLabelProvider();
		}

		public Image getImage(Object element) {
			if (element instanceof IArchiveNode) {
				return delegate.getImage(element);
			} else if (element instanceof IProject) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						IDE.SharedImages.IMG_OBJ_PROJECT);
			} else if (element instanceof IFolder) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FOLDER);
			}
			return null;
		}

		public String getText(Object element) {
			if (element instanceof IArchiveNode) {
				return delegate.getText(element);
			} else if (element instanceof IContainer) {
				return ((IContainer) element).getName();
			}
			return "";
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}
}

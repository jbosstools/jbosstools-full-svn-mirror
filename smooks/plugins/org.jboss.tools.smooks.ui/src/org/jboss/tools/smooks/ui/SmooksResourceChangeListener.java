/**
 * 
 */
package org.jboss.tools.smooks.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Dart
 * 
 */
public class SmooksResourceChangeListener implements IResourceChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org
	 * .eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			switch (event.getType()) {
			case IResourceChangeEvent.POST_CHANGE:
				event.getDelta().accept(new DeltaPrinter());
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deleteFile(IPath path) {
		final IPath path1 = path;
		WorkspaceJob job = new WorkspaceJob("Delete file") { //$NON-NLS-1$

			public IStatus runInWorkspace(IProgressMonitor monitor)
					throws CoreException {
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
						path1);
				if (file.exists()) {
					file.delete(true, monitor);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void newFile(IPath path, IPath newPath) {
		final IPath path1 = path;
		final IPath newPath1 = newPath;
		WorkspaceJob job = new WorkspaceJob("New file and delete old file") { //$NON-NLS-1$
			public IStatus runInWorkspace(IProgressMonitor monitor)
					throws CoreException {
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
						path1);
				if (file.exists()) {
					IFile newFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFile(newPath1);
					if (newFile.exists()) {
						if (MessageDialog
								.openQuestion(
										new Shell(Display.getDefault()),
										Messages
												.getString("SmooksResourceChangeListener.CoverFile"), Messages.getString("SmooksResourceChangeListener.ConverFileMsg1") //$NON-NLS-1$ //$NON-NLS-2$
												+ newFile.getFullPath()
												+ Messages
														.getString("SmooksResourceChangeListener.ConverFileMsg2"))) { //$NON-NLS-1$
							newFile.setContents(file.getContents(), true, true,
									monitor);
						}
					} else {
						newFile.create(file.getContents(), true, monitor);
					}
					file.delete(true, monitor);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	class DeltaPrinter implements IResourceDeltaVisitor {

		int count = 0;

		public boolean visit(IResourceDelta delta) {
			IResource res = delta.getResource();
			String fileExtension = res.getFileExtension();
			if (!SmooksConstants.SMOOKS_EXTENTION_NAME.equals(fileExtension)) { //$NON-NLS-1$
				return true;
			}
			int flags = delta.getFlags();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				if (flags == IResourceDelta.MOVED_FROM) {
					IPath path = delta.getMovedFromPath();
					String fileName = ""; //$NON-NLS-1$
					if (path != null) {
						fileName = path.lastSegment();
						int dotIndex = fileName.lastIndexOf("."); //$NON-NLS-1$
						if (dotIndex != -1) {
							fileExtension = fileName.substring(dotIndex + 1,
									fileName.length());
						}
						if (!SmooksConstants.SMOOKS_EXTENTION_NAME.equals(fileExtension)) { //$NON-NLS-1$
							return true;
						}
						fileName += SmooksConstants.SMOOKS_GRAPH_EXTENTION_NAME_WITHDOT; //$NON-NLS-1$
						path = path.removeLastSegments(1);
						path = path.append(fileName);
					}

					IPath newPath = res.getFullPath();
					fileName = newPath.lastSegment();
					fileName += SmooksConstants.SMOOKS_GRAPH_EXTENTION_NAME_WITHDOT; //$NON-NLS-1$
					newPath = newPath.removeLastSegments(1).append(fileName);
					newFile(path, newPath);
				}
				break;
			case IResourceDelta.REMOVED:
				if (flags == IResourceDelta.MOVED_TO) {
					break;
				}
				IProject project = res.getProject();
				try {
					if (project.isOpen()) {
						IProjectNature nature = project
								.getNature(JavaCore.NATURE_ID);
						if (nature != null) {
							IJavaProject javaProject = JavaCore.create(project);
							IPath outPut = javaProject.getOutputLocation();
							IPath removeRes = res.getFullPath();
							if (outPut.isPrefixOf(removeRes)) {
								break;
							}
						}
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
				IPath path = res.getFullPath();
				String fileName = path.lastSegment();
				fileName += SmooksConstants.SMOOKS_GRAPH_EXTENTION_NAME_WITHDOT; //$NON-NLS-1$
				path = path.removeLastSegments(1).append(fileName);
				deleteFile(path);
				break;
			}
			return true; // visit the children
		}
	}

}

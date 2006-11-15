package org.jboss.ide.eclipse.packages.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.providers.PackagesLabelProvider;

public class PackageNodeDestinationDialog extends ElementTreeSelectionDialog {

//	private TreeViewer destinationTree;
	private Object packageDestination;
	private boolean showWorkspace, showNodes;
	
	 public PackageNodeDestinationDialog(Shell parent, Object destination, boolean showWorkspace, boolean showNodes) {
		 super(parent, new DestinationLabelProvider(), new DestinationContentProvider());
		 setAllowMultiple(false);
		 setTitle(PackagesUIMessages.PackageNodeDestinationDialog_title);
		 
		 this.packageDestination = destination;
		 this.showWorkspace = showWorkspace;
		 this.showNodes = showNodes;
		 setupDestinationList();
	}
	 
	 private void setupDestinationList ()
	 {
		 ArrayList destinations = new ArrayList();

		 if (showWorkspace) {
			 destinations.addAll(ProjectUtil.getAllOpenedProjects());
		 }
		 
		 if (packageDestination instanceof IContainer) {
			 if (showNodes) {
				 destinations.addAll(Arrays.asList(PackagesCore.getProjectPackages(((IContainer)packageDestination).getProject(), new NullProgressMonitor())));
			 }
		 }
		 
		 else if (packageDestination instanceof IPackageNode) {
			 IPackageNode node = (IPackageNode)packageDestination;
			 if (showNodes) {
				 destinations.addAll(Arrays.asList(PackagesCore.getProjectPackages(node.getProject(), new NullProgressMonitor())));
			 }
			 String message = PackagesUIMessages.PackageNodeDestinationDialog_messagePrefix;
			 switch (node.getNodeType()) {
				 case IPackageNode.TYPE_PACKAGE: message += PackagesUIMessages.PackageNodeDestinationDialog_packageLabel; break;
				 case IPackageNode.TYPE_PACKAGE_FILESET: message += PackagesUIMessages.PackageNodeDestinationDialog_filesetLabel; break;
				 case IPackageNode.TYPE_PACKAGE_FOLDER: message += PackagesUIMessages.PackageNodeDestinationDialog_folderLabel; break;
			 }
			 message += ".";
			 setMessage(message);
		 }
		 
		 setInput(destinations);
	 }
	 
	 private static class DestinationContentProvider implements ITreeContentProvider
	 {
		 private static final Object[] NO_CHILDREN = new Object[0];
		 
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof IPackageNode)
			{
				IPackageNode node = (IPackageNode) parentElement;
				List children = new ArrayList(Arrays.asList(node.getAllChildren()));
		 		for (Iterator iter = children.iterator(); iter.hasNext(); )
				{
					IPackageNode child = (IPackageNode) iter.next();
					if (child.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
						iter.remove();
				}
				return children.toArray();
			}
			else if (parentElement instanceof IContainer)
			{
				IContainer container = (IContainer) parentElement;
				try {
					IResource members[] = container.members();
					List folders = new ArrayList();
					for (int i = 0; i < members.length; i++)
					{
						if (members[i].getType() == IResource.FOLDER) folders.add(members[i]);
					}
					
					return folders.toArray();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return NO_CHILDREN;
		}

		public Object getParent(Object element) {
			if (element instanceof IPackageNode)
			{
				IPackageNode node = (IPackageNode) element;
				return node.getParent();
			}
			else if (element instanceof IContainer)
			{
				IContainer container = (IContainer) element;
				return container.getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof IPackageNode)
			{
				IPackageNode node = (IPackageNode) element;
				List children = new ArrayList(Arrays.asList(node.getAllChildren()));
				for (Iterator iter = children.iterator(); iter.hasNext(); )
				{
					IPackageNode child = (IPackageNode) iter.next();
					if (child.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
						iter.remove();
				}
				return children.size() > 0;
			}
			else if (element instanceof IContainer)
			{
				IContainer container = (IContainer) element;
				try {
					IResource members[] = container.members();
					List folders = new ArrayList();
					for (int i = 0; i < members.length; i++)
					{
						if (members[i].getType() == IResource.FOLDER) folders.add(members[i]);
					}
					
					return folders.size() > 0;
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Collection)
				return ((Collection)inputElement).toArray();
			
			return NO_CHILDREN;
		}

		public void dispose() {}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	 }
	 
	 private static class DestinationLabelProvider implements ILabelProvider
	 {
		private PackagesLabelProvider delegate;
		 
		public DestinationLabelProvider  () 
		{
			delegate = new PackagesLabelProvider();
		}
		
		public Image getImage(Object element) {
			if (element instanceof IPackageNode)
			{
				return delegate.getImage(element);
			}
			else if (element instanceof IProject)
			{
				return PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
			}
			else if (element instanceof IFolder)
			{
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			return null;
		}

		public String getText(Object element) {
			if (element instanceof IPackageNode)
			{
				return delegate.getText(element);
			}
			else if (element instanceof IContainer)
			{
				return ((IContainer)element).getName();
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

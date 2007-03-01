package org.jboss.ide.eclipse.packages.ui.providers;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;

public class PackagesContentProvider implements ITreeContentProvider {
	
	private Hashtable filesetProperties;
	
	public PackagesContentProvider ()
	{
		filesetProperties = new Hashtable();
	}
	
	public static class FileSetProperty {
		private IPackageFileSet fileset;
		private String name;
		
		public FileSetProperty(String name, IPackageFileSet fileset)
		{
			this.name = name;
			this.fileset = fileset;
		}
		
		public String getName () { return name; }
		public IPackageFileSet getFileSet() { return fileset; }
	}
	
	public static final String FILESET_PROP_SRC_CONTAINER = "srcContainer";
	public static final String FILESET_PROP_SRC_PROJECT = "srcProject";
	public static final String FILESET_PROP_DIR = "dir";
	public static final String FILESET_PROP_INCLUDES = "includes";
	public static final String FILESET_PROP_EXCLUDES = "excludes";
	public static final String FILESET_PROP_DEST_FILE = "destFile";
	public static final String FILESET_PROP_FILE = "file";
	
	public void addFilesetProperties (IProject project, IPackageFileSet fileset)
	{
		ArrayList props = new ArrayList();
		if (fileset.isSingleFile())
		{
			if (fileset.getDestinationFilename() != null && fileset.getDestinationFilename().length() > 0)
				props.add(new FileSetProperty(FILESET_PROP_DEST_FILE, fileset));
			
			props.add(new FileSetProperty(FILESET_PROP_FILE, fileset));
		}
		else {
			if (fileset.getExcludesPattern() != null && fileset.getExcludesPattern().length() > 0)
				props.add(new FileSetProperty(FILESET_PROP_EXCLUDES, fileset));
			
			if (fileset.getIncludesPattern() != null && fileset.getIncludesPattern().length() > 0)
				props.add(new FileSetProperty(FILESET_PROP_INCLUDES, fileset));
			
			if (fileset.isInWorkspace())
			{
				IContainer sourceContainer = fileset.getSourceContainer();
				if (!(sourceContainer.getType() == IResource.PROJECT && sourceContainer.equals(project)))
					props.add(new FileSetProperty(FILESET_PROP_SRC_CONTAINER, fileset));
			} else {
				props.add(new FileSetProperty(FILESET_PROP_DIR, fileset));
			}
			
			if (!fileset.getProject().equals(project))
				props.add(new FileSetProperty(FILESET_PROP_SRC_PROJECT, fileset));
		}
		filesetProperties.put(fileset, props);
	}
	
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IPackageFileSet)
		{
			IPackageFileSet fileset = (IPackageFileSet) parentElement;
			ArrayList result = ((ArrayList)filesetProperties.get(fileset));
			return result == null ? new Object[]{} : result.toArray();
		}
		else if (parentElement instanceof IPackageNode)
		{
			IPackageNode node = (IPackageNode)parentElement;
			return node.getAllChildren();
		}
		else return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof IPackageNode)
		{
			IPackageNode node = (IPackageNode) element;
			return node.getParent();
		}
		else if (element instanceof FileSetProperty)
		{
			return ((FileSetProperty)element).getFileSet();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		if (element instanceof IPackageFileSet)
		{
			return true;
		}
		else if (element instanceof IPackageNode)
		{
			IPackageNode node = (IPackageNode) element;
			return node.hasChildren();
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) return;
		
		IPackage packages[] = (IPackage[]) newInput;
		
		if (oldInput != newInput && packages.length > 0)
		{
			final IProject project = (IProject) packages[0].getProject();
			filesetProperties.clear();
			
			PackagesCore.visitProjectPackages(project, new IPackageNodeVisitor() {
				public boolean visit(IPackageNode node) {
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						IPackageFileSet fileset = (IPackageFileSet) node;
						addFilesetProperties(project, fileset);
					}
					return true;
				}
			});
		}
	}

}

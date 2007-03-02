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
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;

public class PackagesContentProvider implements ITreeContentProvider {
	
	private Hashtable filesetProperties;
	
	public PackagesContentProvider ()
	{
		filesetProperties = new Hashtable();
	}
	
	private boolean showProjectRoot ()
	{
		return PackagesUIPlugin.getDefault().getPluginPreferences().getBoolean(PackagesUIPlugin.PREF_SHOW_PROJECT_ROOT);
	}
	
	public static class ProjectWrapper {
		public IProject project;
		public boolean equals(Object obj) {
			if (obj instanceof IProject)
			{
				return project.equals(obj);
			}
			return this == obj;
		}
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
		if (parentElement instanceof ProjectWrapper)
		{
			return PackagesCore.getProjectPackages(((ProjectWrapper)parentElement).project, null);
		}
		else if (parentElement instanceof IPackageFileSet)
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
			if (node.getNodeType() == IPackageNode.TYPE_PACKAGE)
			{
				if (((IPackage)node).isTopLevel() && showProjectRoot()) {
					return node.getProject();
				}
			}
			return node.getParent();
		}
		else if (element instanceof FileSetProperty)
		{
			return ((FileSetProperty)element).getFileSet();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof ProjectWrapper)
		{
			return true;
		}
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
		
		if (inputElement instanceof IProject[])
		{
			IProject[] projects = (IProject[]) inputElement;
			ProjectWrapper wrappers[] = new ProjectWrapper[projects.length];
			for (int i = 0; i < projects.length; i++)
			{
				wrappers[i] = new ProjectWrapper();
				wrappers[i].project = projects[i];
			}
			return wrappers;
		}
		
		return (Object[]) inputElement;
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) return;
		
		IProject projects[] = new IProject[0];
		if (newInput instanceof IPackage[])
		{
			IPackage[] packages = (IPackage[]) newInput;
			if (packages.length > 0)
				projects = new IProject[] { packages[0].getProject() };
		} else {
			projects = (IProject[]) newInput;
		}
		
		if (oldInput != newInput)
		{
			filesetProperties.clear();
			for (int i = 0; i < projects.length; i++)
			{
				final IProject currentProject = projects[i];
				
				PackagesCore.visitProjectPackages(currentProject, new IPackageNodeVisitor() {
					public boolean visit(IPackageNode node) {
						if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
						{
							IPackageFileSet fileset = (IPackageFileSet) node;
							addFilesetProperties(currentProject, fileset);
						}
						return true;
					}
				});
			}
		}
	}

}

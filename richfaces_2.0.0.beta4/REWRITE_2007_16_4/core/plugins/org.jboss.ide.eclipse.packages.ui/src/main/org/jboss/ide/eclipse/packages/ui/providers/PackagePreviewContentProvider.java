package org.jboss.ide.eclipse.packages.ui.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;

public class PackagePreviewContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IPackageNode)
		{	
			IPackageNode node = (IPackageNode) parentElement;
			if (node.getNodeType() == IPackageNode.TYPE_PACKAGE || node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
			{
				ArrayList children = new ArrayList();
				IPackageNode[] childNodes = node.getAllChildren();
				
				for (int i = 0; i < childNodes.length; i++)
				{
					int nodeType = childNodes[i].getNodeType();
					
					if (nodeType == IPackageNode.TYPE_PACKAGE || nodeType == IPackageNode.TYPE_PACKAGE_FOLDER)
					{
						children.add(childNodes[i]);
					}
				}
			}
			else {
				return getFilesInFileset((IPackageFileSet)node);
			}
		}
		
		return new Object[0];
	}
	
	private Object[] getFilesInFileset (IPackageFileSet fileset)
	{
		ArrayList files = new ArrayList ();
		
		if (!fileset.isSingleFile())
		{
			if (fileset.isInWorkspace())
			{
				files.addAll(Arrays.asList(fileset.findMatchingFiles()));
			}
			else {
				files.addAll(Arrays.asList(fileset.findMatchingPaths()));
			}
		}
		else {
			if (fileset.isInWorkspace())
			{
				files.add(fileset.getFile());
			}
			else {
				files.add(fileset.getFilePath());
			}
		}
		
		return (Object[]) files.toArray(new Object[files.size()]);
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof IPackageNode)
		{	
			IPackageNode node = (IPackageNode) element;
			if (node.getNodeType() == IPackageNode.TYPE_PACKAGE || node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
			{
				return node.hasChildren();
			}
			else {
				IPackageFileSet fileset = (IPackageFileSet) node;
				
			}
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection)
		{
			return ((Collection)inputElement).toArray();
		}
		else if (inputElement instanceof Object[])
		{
			return (Object[])inputElement;
		}
		else return new Object[]{ inputElement };
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	
}

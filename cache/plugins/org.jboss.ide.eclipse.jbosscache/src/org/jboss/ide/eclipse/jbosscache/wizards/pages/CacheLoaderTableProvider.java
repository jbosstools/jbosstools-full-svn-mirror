package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jbosscache.model.CacheLoaderPropModel;

public class CacheLoaderTableProvider implements IStructuredContentProvider{

	public Object[] getElements(Object inputElement) {
		
		if(inputElement == null || inputElement.equals(""))
			return Collections.EMPTY_LIST.toArray();
		
		return ((List)inputElement).toArray();
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}

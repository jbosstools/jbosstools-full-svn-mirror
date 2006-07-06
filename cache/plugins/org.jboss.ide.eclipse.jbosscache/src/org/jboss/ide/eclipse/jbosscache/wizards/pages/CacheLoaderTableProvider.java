package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jbosscache.model.CacheLoaderPropModel;

public class CacheLoaderTableProvider implements IStructuredContentProvider{

	public Object[] getElements(Object inputElement) {
		
		if(inputElement == null || inputElement.equals(""))
			return Collections.EMPTY_LIST.toArray();

		String[] params = (String[])inputElement;
		
		String element = params[0];
		boolean isDs = Boolean.parseBoolean(params[1]);
		
				
		return new CacheLoaderPropModel().getPropertyModels(element,isDs).toArray();
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}

package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.model.CacheLoaderPropModel;

public class CacheLoaderLableProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		
		CacheLoaderPropModel model = (CacheLoaderPropModel)element;
		
		switch (columnIndex) {
		case 0:
			
			return model.getProperty(); 

		case 1:
			
			return model.getValue();

		}
		
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		
	}

	public void dispose() {
		
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		
	}

}

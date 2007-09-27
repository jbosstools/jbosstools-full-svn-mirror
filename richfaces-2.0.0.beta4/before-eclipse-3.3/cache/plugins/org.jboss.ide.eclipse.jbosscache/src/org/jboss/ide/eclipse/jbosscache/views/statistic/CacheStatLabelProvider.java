package org.jboss.ide.eclipse.jbosscache.views.statistic;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.views.statistic.CacheStatModel.CacheStatAttributes;

public class CacheStatLabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		
		switch (columnIndex) {
		case 0:
			if(element instanceof CacheStatModel){
				return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_STAT_PARENT_ATTRIBUTE);
			}
			
			if(element instanceof CacheStatAttributes)
				return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_STAT_ATTRIBUTE);				
		}
		
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof CacheStatModel){
				CacheStatModel model = (CacheStatModel)element;
				return model.getNameOfInterceptor();
			}
			
			if(element instanceof CacheStatAttributes){
				CacheStatAttributes model = (CacheStatAttributes)element;
				return model.getNameOfAttribute();
			}
			

		case 1:
			if(element instanceof CacheStatAttributes){
				CacheStatAttributes model = (CacheStatAttributes)element;
				return model.getValueOfAttribute();
			}			
		}
		
		return "";

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

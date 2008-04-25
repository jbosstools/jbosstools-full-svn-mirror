package org.jboss.ide.eclipse.jbosscache.views.statistic;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jbosscache.views.statistic.CacheStatModel.CacheStatAttributes;

public class CacheStatContentProvider implements ITreeContentProvider{

	public Object[] getChildren(Object parentElement) {
		
		if(parentElement instanceof List)
			return ((List)parentElement).toArray();
		
		else if(parentElement instanceof CacheStatModel){
			CacheStatModel model = (CacheStatModel)parentElement;
			return model.getAttrChilds().toArray();
		}
		
		else if(parentElement instanceof CacheStatAttributes){
			CacheStatAttributes attr = (CacheStatAttributes)parentElement;
			return new Object[]{attr};
		}
		
		else
			
			return Collections.EMPTY_LIST.toArray();
	}

	public Object getParent(Object element) {
		
		if(element instanceof CacheStatModel)
			return null;
		else if(element instanceof CacheStatAttributes){
			CacheStatAttributes attr = (CacheStatAttributes)element;
			return attr.getParent();
		}
		
		else
			return null;
			
	}

	public boolean hasChildren(Object element){
		if(element instanceof CacheStatAttributes)
			return false;
		else if(element instanceof CacheStatModel){
			CacheStatModel model = (CacheStatModel)element;
			return model.getAttrChilds().size() > 0;
		}
		
		else if(element instanceof List){
			return ((List)element).size() > 0;
		}
		
		return false;
		
	}
	
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}

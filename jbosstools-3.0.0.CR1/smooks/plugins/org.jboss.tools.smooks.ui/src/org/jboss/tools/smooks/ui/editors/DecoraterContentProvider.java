/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * @author Dart
 *
 */
public class DecoraterContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof List){
			if(!((List)parentElement).isEmpty()){
				return ((List)parentElement).toArray();
			}
		}
		return new Object[]{};
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof List){
			return !((List)element).isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			return ((List)inputElement).toArray();
		}
		return new Object[]{};
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}

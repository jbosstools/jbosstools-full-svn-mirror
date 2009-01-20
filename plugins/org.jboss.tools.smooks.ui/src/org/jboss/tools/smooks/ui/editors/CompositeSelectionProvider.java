/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * @author Dart
 *
 */
public class CompositeSelectionProvider implements ISelectionProvider {

	List<ISelectionProvider> list = new ArrayList<ISelectionProvider>();
	
	private ISelection selection;
	
	public void addSelectionProvider(ISelectionProvider provider){
		this.list.add(provider);
	}
	
	public void removeSelectionProvider(ISelectionProvider provider){
		list.remove(provider);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ISelectionProvider p = (ISelectionProvider) iterator.next();
			p.addSelectionChangedListener(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		// TODO Auto-generated method stub
		return this.selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ISelectionProvider p = (ISelectionProvider) iterator.next();
			p.removeSelectionChangedListener(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		this.selection = selection;
//		for (Iterator iterator = this.list.iterator(); iterator.hasNext();) {
//			ISelectionProvider provider = (ISelectionProvider) iterator.next();
//			provider.setSelection(selection);
//		}
	}

}

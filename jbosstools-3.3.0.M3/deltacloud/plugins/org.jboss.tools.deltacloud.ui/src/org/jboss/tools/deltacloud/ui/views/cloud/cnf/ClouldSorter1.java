package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.CategoryContent;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.ImagesPager;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.InstancesCategory;

public class ClouldSorter1 extends ViewerSorter {

	public ClouldSorter1() {
		// TODO Auto-generated constructor stub
	}

	public ClouldSorter1(Collator collator) {
		super(collator);
		// TODO Auto-generated constructor stub
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
    public int compare(Viewer viewer, Object e1, Object e2) {
    	if( e1 instanceof CategoryContent && e1 instanceof CategoryContent ) {
    		if( e1 instanceof InstancesCategory)
    			return -1;
    		return 1;
    	}
    	
    	if( e1 instanceof ImagesPager && e2 instanceof ImagesPager)
    		return ((ImagesPager)e1).getPage() - ((ImagesPager)e2).getPage();
    	return super.compare(viewer, e1, e2);
    }
}

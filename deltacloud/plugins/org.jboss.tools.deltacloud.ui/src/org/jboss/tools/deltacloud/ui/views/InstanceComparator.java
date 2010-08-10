package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

public class InstanceComparator extends ViewerComparator {
	
	private final static int UP = 1;
	private final static int DOWN = -1;
	
		
	private int column;
	private int direction;
	
	public InstanceComparator(int column) {
		this.column = column;
		this.direction = UP;
	}
	
	public void setColumn(int newColumn) {
		if (column != newColumn)
			direction = UP;
		column = newColumn;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void reverseDirection() {
		direction *= -1;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (!(e1 instanceof DeltaCloudInstance) || !(e2 instanceof DeltaCloudInstance))
			return 0;
		
		int tmp = compareByColumn(viewer, (DeltaCloudInstance)e1, (DeltaCloudInstance)e2);
		return tmp * direction;
	}
	
	private int compareByColumn(Viewer viewer, DeltaCloudInstance e1, DeltaCloudInstance e2) {
		InstanceViewLabelAndContentProvider provider = (InstanceViewLabelAndContentProvider)((TableViewer)viewer).getContentProvider();
		String s1 = provider.getColumnText(e1, column);
		String s2 = provider.getColumnText(e2, column);
		return s1.compareTo(s2);
	}
}

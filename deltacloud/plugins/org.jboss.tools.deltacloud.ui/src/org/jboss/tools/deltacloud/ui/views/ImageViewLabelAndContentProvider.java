package org.jboss.tools.deltacloud.ui.views;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;

public class ImageViewLabelAndContentProvider extends BaseLabelProvider implements IStructuredContentProvider, ITableLabelProvider {

	private DeltaCloud cloud;
	private DeltaCloudImage[] images;

	public enum Column {
		NAME(0, 30), 
		ID(1, 20), 
		ARCH(2, 20), 
		DESC(3, 30);
		
		private int column;
		private int weight;
		private static final Map<Integer,Column> lookup 
		= new HashMap<Integer,Column>();

		static {
			for(Column c : EnumSet.allOf(Column.class))
				lookup.put(c.getColumnNumber(), c);
		}

		private Column(int column, int weight) {
			this.column = column;
			this.weight = weight;
		}

		public int getColumnNumber() {
			return column;
		}
		
		public int getWeight() {
			return weight;
		}

		public static Column getColumn(int number) {
			return lookup.get(number);
		}
		
		public static int getSize() {
			return lookup.size();
		}
		
	};
	
	@Override
	public Object[] getElements(Object inputElement) {
		return images;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			if (newInput instanceof DeltaCloudImage[]) {
				images = (DeltaCloudImage[])newInput;
			} else {
				cloud = (DeltaCloud)newInput;
				images = cloud.getImages();
			}
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Column c = Column.getColumn(columnIndex);
		DeltaCloudImage i = (DeltaCloudImage)element;
		switch (c) {
		case NAME:
			return i.getName();
		case ID:
			return i.getId();
		case ARCH:
			return i.getArchitecture();
		case DESC:
			return i.getDescription();
		}
		return "";
	}

}

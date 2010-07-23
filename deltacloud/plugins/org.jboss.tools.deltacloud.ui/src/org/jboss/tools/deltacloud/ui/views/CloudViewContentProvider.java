package org.jboss.tools.deltacloud.ui.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;

public class CloudViewContentProvider implements ITreeContentProvider {

	private CloudViewElement[] elements;
	private static final String INSTANCE_CATEGORY_NAME = "InstanceCategoryName"; //$NON-NLS-1$
	private static final String IMAGE_CATEGORY_NAME = "ImageCategoryName"; //$NON-NLS-1$
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IViewSite)
			return elements;
		CloudViewElement e = (CloudViewElement)parentElement;
		return e.getChildren();
	}

	@Override
	public Object getParent(Object element) {
		CloudViewElement e = (CloudViewElement)element;
		return e.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		CloudViewElement e = (CloudViewElement)element;
		return e.hasChildren();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return elements;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void createElements() {
		ArrayList<CloudViewElement> list = new ArrayList<CloudViewElement>();
		DeltaCloudManager m = DeltaCloudManager.getDefault();
		DeltaCloud[] clouds = m.getClouds();
		for (int i = 0; i < clouds.length; ++i) {
			DeltaCloud cloud = clouds[i];
			CVCloudElement e = new CVCloudElement(cloud, cloud.getName());
			CVCategoryElement c1 = new CVCategoryElement(cloud, CVMessages.getString(INSTANCE_CATEGORY_NAME),
					CVCategoryElement.INSTANCES);
			CVCategoryElement c2 = new CVCategoryElement(cloud, CVMessages.getString(IMAGE_CATEGORY_NAME),
					CVCategoryElement.IMAGES);
			e.addChild(c1);
			e.addChild(c2);
			list.add(e);
		}
		elements = list.toArray(new CloudViewElement[list.size()]);
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		createElements();
	}

}

package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageListListener;

public class CVImagesCategoryElement extends CVCategoryElement implements IImageListListener {

	private Viewer viewer;
	private CVImagesCategoryElement category;
	
	public CVImagesCategoryElement(Object element, String name, Viewer viewer) {
		super(element, name, CVCategoryElement.INSTANCES);
		this.viewer = viewer;
		DeltaCloud cloud = (DeltaCloud)getElement();
		cloud.addImageListListener(this);
		this.category = this;
	}

	protected void finalize() throws Throwable {
		DeltaCloud cloud = (DeltaCloud)getElement();
		cloud.removeImageListListener(this);
		super.finalize();
	}
	
	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud)getElement();
			cloud.removeImageListListener(this);
			DeltaCloudImage[] images = cloud.getCurrImages();
			for (int i = 0; i < images.length; ++i) {
				DeltaCloudImage d = images[i];
				CVImageElement element = new CVImageElement(d, d.getName());
				addChild(element);
			}
			initialized = true;
			cloud.addImageListListener(this);
		}
		return super.getChildren();
	}

	@Override
	public void listChanged(DeltaCloudImage[] images) {
		clearChildren();
		for (int i = 0; i < images.length; ++i) {
			DeltaCloudImage d = images[i];
			CVImageElement element = new CVImageElement(d, d.getName());
			addChild(element);
		}
		initialized = true;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				((TreeViewer)viewer).refresh(category, false);
			}
		});
	}

}

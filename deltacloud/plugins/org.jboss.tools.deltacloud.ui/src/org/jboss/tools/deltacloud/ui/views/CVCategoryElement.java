package org.jboss.tools.deltacloud.ui.views;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

public class CVCategoryElement extends CloudViewElement {

	public final static int INSTANCES = 0;
	public final static int IMAGES = 1;
	
	private int type;
	private boolean initialized;
	
	public CVCategoryElement(Object element, String name, int type) {
		super(element, name);
		this.type = type;
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud)getElement();
			if (type == INSTANCES) {
				DeltaCloudInstance[] instances = cloud.getInstances();
				for (int i = 0; i < instances.length; ++i) {
					DeltaCloudInstance d = instances[i];
					CVInstanceElement element = new CVInstanceElement(d, d.getName());
					addChild(element);
				}
				initialized = true;
			} else if (type == IMAGES) {
				DeltaCloudImage[] images = cloud.getImages();
				for (int i = 0; i < images.length; ++i) {
					DeltaCloudImage d = images[i];
					CVImageElement element = new CVImageElement(d, d.getName());
					addChild(element);
				}
				initialized = true;
			}
		}
		return super.getChildren();
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return null;
	}

}

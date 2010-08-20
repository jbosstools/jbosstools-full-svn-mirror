package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

public class CloudViewLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof CVCloudElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_CLOUD);
		} else if (element instanceof CVCategoryElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_FOLDER);
		} else if (element instanceof CVInstanceElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_INSTANCE);
		} else if (element instanceof CVImageElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_IMAGE);
		}
		return null;
	}
	
	@Override
	public String getText(Object element) {
		CloudViewElement e = (CloudViewElement)element;
		return e.getName();
	}

}

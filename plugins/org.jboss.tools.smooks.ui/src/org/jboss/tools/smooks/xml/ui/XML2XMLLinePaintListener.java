/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.gef.figures.ILineFigurePaintListener;
import org.jboss.tools.smooks.ui.gef.figures.LinePaintListener;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.xml.XMLImageConstants;

/**
 * @author Dart
 * 
 */
public class XML2XMLLinePaintListener extends LinePaintListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.figures.ILineFigurePaintListener#
	 * drawLineAdditions(org.eclipse.draw2d.Graphics)
	 */
	public void drawLineAdditions(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {
		// graphics.drawText("Mapping", graphics.getb)
		PropertyModel type = null;
		List<PropertyModel> list = model.getProperties();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PropertyModel propertyModel = (PropertyModel) iterator.next();
			if (XMLPropertiesSection.MAPPING_TYPE.equals(propertyModel
					.getName())) {
				type = propertyModel;
				break;
			}
		}
		if (type != null) {
			Point center = hostFigure.getBounds().getCenter();
			center = center.translate(-8, -8);
			Image bindingImage = SmooksUIActivator.getDefault()
					.getImageRegistry().get(
							XMLImageConstants.IMAGE_BINDING_LINE);
			Image mappingImage = SmooksUIActivator.getDefault()
					.getImageRegistry().get(
							XMLImageConstants.IMAGE_MAPPING_LINE);
			if (XMLPropertiesSection.BINDING.equals(type.getValue())) {
				graphics.fillRectangle(center.x,center.y,16,16);
				graphics.drawImage(bindingImage, 0, 0,
						bindingImage.getBounds().width, bindingImage
								.getBounds().height, center.x, center.y, 16, 16);
			}
			if (XMLPropertiesSection.MAPPING.equals(type.getValue())) {
				graphics.fillRectangle(center.x,center.y,16,16);
				graphics.drawImage(mappingImage, 0, 0,
						mappingImage.getBounds().width, mappingImage
								.getBounds().height, center.x, center.y, 16, 16);
			}
		}
	}
}

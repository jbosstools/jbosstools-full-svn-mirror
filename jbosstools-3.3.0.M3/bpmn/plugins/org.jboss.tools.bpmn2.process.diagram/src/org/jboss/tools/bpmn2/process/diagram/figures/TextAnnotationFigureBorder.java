package org.jboss.tools.bpmn2.process.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;

public class TextAnnotationFigureBorder extends LineBorder {

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		if (getWidth() % 2 == 1) {
			tempRect.width--;
			tempRect.height--;
		}
		tempRect.shrink(getWidth() / 2, getWidth() / 2);
		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());
		graphics.drawLine(tempRect.x, tempRect.y, tempRect.x + 10, tempRect.y);
		graphics.drawLine(tempRect.x, tempRect.y, tempRect.x, tempRect.y + tempRect.height);
		graphics.drawLine(tempRect.x, tempRect.y + tempRect.height, tempRect.x + 10, tempRect.y + tempRect.height);
	}

}

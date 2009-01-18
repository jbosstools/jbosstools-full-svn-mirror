/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;

/**
 * @author Dart
 * 
 */
public class LinePaintListener implements ILineFigurePaintListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.figures.ILineFigurePaintListener#
	 * drawLineAdditions(org.eclipse.draw2d.Graphics,
	 * org.eclipse.draw2d.IFigure,
	 * org.jboss.tools.smooks.ui.gef.model.LineConnectionModel)
	 */
	public void drawLineAdditions(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.figures.ILineFigurePaintListener#
	 * drawLineSourceLocator(org.eclipse.draw2d.Graphics,
	 * org.eclipse.draw2d.IFigure,
	 * org.jboss.tools.smooks.ui.gef.model.LineConnectionModel)
	 */
	public void drawLineSourceLocator(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {
		graphics.pushState();
		graphics.setForegroundColor(ColorConstants.black);
		graphics.setBackgroundColor(GraphicsConstants.elementLabelColor);
		Point p = hostFigure.getBounds().getTopLeft();
		Point p2 = hostFigure.getBounds().getBottomLeft();
		Point p3 = hostFigure.getBounds().getTopRight();
		p3 = new Point(p3.x, p3.y + hostFigure.getSize().height / 2);
		PointList pointList = new PointList();
		pointList.addPoint(p);
		pointList.addPoint(p2.x, p2.y - 1);
		pointList.addPoint(p3);
		graphics.fillPolygon(pointList);
		graphics.drawPolygon(pointList);
		graphics.popState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.figures.ILineFigurePaintListener#
	 * drawLineTargetLocator(org.eclipse.draw2d.Graphics,
	 * org.eclipse.draw2d.IFigure,
	 * org.jboss.tools.smooks.ui.gef.model.LineConnectionModel)
	 */
	public void drawLineTargetLocator(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {
		graphics.pushState();
		graphics.setBackgroundColor(GraphicsConstants.groupHeaderColor);
		Point p = hostFigure.getBounds().getTopLeft();
		Point p2 = hostFigure.getBounds().getBottomLeft();
		Point p3 = hostFigure.getBounds().getTopRight();
		p3 = new Point(p3.x, p3.y + hostFigure.getSize().height / 2);
		PointList pointList = new PointList();
		pointList.addPoint(p);
		pointList.addPoint(p2.x, p2.y - 1);
		pointList.addPoint(p3);
		graphics.fillPolygon(pointList);
		graphics.drawPolygon(pointList);
		graphics.popState();
	}

	public PolylineConnection createHostFigure(LineConnectionModel model) {
		return null;
	}

	public PolylineConnection createDummyFigure(CreateConnectionRequest req) {
		return null;
	}

}

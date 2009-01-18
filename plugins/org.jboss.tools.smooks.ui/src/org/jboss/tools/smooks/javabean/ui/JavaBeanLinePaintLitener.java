/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.gef.figures.LinePaintListener;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;

/**
 * @author Dart
 * 
 */
public class JavaBeanLinePaintLitener extends LinePaintListener {
	
	

	@Override
	public PolylineConnection createDummyFigure(CreateConnectionRequest req) {
		EditPart editPart = req.getSourceEditPart();
		if(editPart != null){
			if(editPart.getModel() instanceof TargetModel){
				return new TargetReferenceConnectionLine();
			}
		}
		return super.createDummyFigure(req);
	}

	@Override
	public PolylineConnection createHostFigure(LineConnectionModel model) {
		IConnectableModel source = model.getSource();
		IConnectableModel target = model.getTarget();
		if(source instanceof TargetModel && target instanceof TargetModel){
			return new TargetReferenceConnectionLine();
		}
		return super.createHostFigure(model);
	}

	@Override
	public void drawLineAdditions(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {
		((Shape) hostFigure).setLineStyle(Graphics.LINE_SOLID);
		AbstractStructuredDataModel targetModel = (AbstractStructuredDataModel) model
				.getTarget();
		JavaBeanModel targetTransformModel = (JavaBeanModel) targetModel
				.getReferenceEntityModel();
		IConnectableModel source = model.getSource();
		IConnectableModel target = model.getTarget();
		boolean collapse = false;
		if (source instanceof TreeItemRelationModel) {
			if (((TreeItemRelationModel) source).isCollapse()) {
				collapse = true;
			}
		}
		if (target instanceof TreeItemRelationModel) {
			if (((TreeItemRelationModel) target).isCollapse()) {
				collapse = true;
			}
		}
		if (targetTransformModel.isPrimitive()) {
			graphics.setForegroundColor(ColorConstants.orange);
			if (collapse) {
				graphics.setAlpha(100);
			} else {
				graphics.setAlpha(255);
			}
		}
	}

	@Override
	public void drawLineSourceLocator(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {
		if(model.getSource() instanceof TargetModel){
			PointList pointList = new PointList();
			Rectangle bounds = hostFigure.getBounds();
			Point p1 = bounds.getTopLeft();
			p1.setLocation(p1.x, p1.y + bounds.height/2);
			Point p2 = bounds.getBottomRight();
			Point p3 = bounds.getTopRight();
			pointList.addPoint(p1);
			pointList.addPoint(p2);
			pointList.addPoint(p3);
			graphics.pushState();
			graphics.setBackgroundColor(ColorConstants.darkGray);
			graphics.fillPolygon(pointList);
			graphics.popState();
			return;
		}
		AbstractStructuredDataModel targetModel = (AbstractStructuredDataModel) model
				.getTarget();
		JavaBeanModel targetTransformModel = (JavaBeanModel) targetModel
				.getReferenceEntityModel();
		if (targetTransformModel.isPrimitive()) {
			drawBindingSourceLocator(graphics, hostFigure);
			return;
		}
		super.drawLineSourceLocator(graphics, hostFigure, model);
	}

	@Override
	public void drawLineTargetLocator(Graphics graphics, IFigure hostFigure,
			LineConnectionModel model) {
		if(model.getSource() instanceof TargetModel){
			super.drawLineTargetLocator(graphics, hostFigure, model);
			return;
		}
		AbstractStructuredDataModel targetModel = (AbstractStructuredDataModel) model
				.getTarget();
		JavaBeanModel targetTransformModel = (JavaBeanModel) targetModel
				.getReferenceEntityModel();
		if (targetTransformModel.isPrimitive()) {
			drawBindingTargetLocator(graphics, hostFigure);
			return;
		}
		super.drawLineTargetLocator(graphics, hostFigure, model);
	}

	private void drawValueLine(Graphics graphics, IFigure hostFigure) {

	}

	private void drawBindingSourceLocator(Graphics graphics, IFigure hostFigure) {
		graphics.setBackgroundColor(ColorConstants.gray);
		Rectangle rect = hostFigure.getBounds();
		graphics.fillRectangle(rect.x + 2 ,rect.y + 2 ,6,6);
	}

	private void drawBindingTargetLocator(Graphics graphics, IFigure hostFigure) {
		graphics.setBackgroundColor(ColorConstants.gray);
		Rectangle rect = hostFigure.getBounds();
		graphics.fillRectangle(rect.x + 5 ,rect.y + 2 ,6,6);
	}

}

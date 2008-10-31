package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.smooks.ui.gef.figures.CurveLineConnection;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.gef.policy.DeleteConnectionEditPolicy;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;

public class StructuredDataConnectionEditPart extends
		AbstractConnectionEditPart  implements PropertyChangeListener{

	public StructuredDataConnectionEditPart() {
		super();
	}
	
	public void activate() {
		super.activate();
		Object model = getModel();
		if(model instanceof AbstractStructuredDataConnectionModel){
			((AbstractStructuredDataConnectionModel)model).addPropertyChangeListener(this);
		}
	}

	public void deactivate() {
		Object model = getModel();
		if(model instanceof AbstractStructuredDataConnectionModel){
			((AbstractStructuredDataConnectionModel)model).removePropertyChangeListener(this);
		}
		super.deactivate();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		changeLineStyleWithCollapseStatus();
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteConnectionEditPolicy());
	}

	protected IFigure createFigure() {
		CurveLineConnection conn = new CurveLineConnection() {
			public void paintFigure(Graphics graphics) {
				super.paintFigure(graphics);
			}
		};
		// conn.setSmoothness(SmoothPolyLineConnection.SMOOTH_MORE);
		Figure targetFlagFigure = new Figure() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
			 */
			@Override
			public void paint(Graphics graphics) {
				graphics.pushState();
				graphics.setBackgroundColor(GraphicsConstants.groupHeaderColor);
				Point p = getBounds().getTopLeft();
				Point p2 = getBounds().getBottomLeft();
				Point p3 = getBounds().getTopRight();
				p3 = new Point(p3.x, p3.y + getSize().height / 2);
				PointList pointList = new PointList();
				pointList.addPoint(p);
				pointList.addPoint(p2.x, p2.y - 1);
				pointList.addPoint(p3);
				graphics.fillPolygon(pointList);
				graphics.drawPolygon(pointList);
				graphics.popState();
				super.paint(graphics);
			}

		};

		Figure sourceFlagFigure = new Figure() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
			 */
			@Override
			public void paint(Graphics graphics) {
				graphics.pushState();
				graphics.setForegroundColor(ColorConstants.black);
				graphics
						.setBackgroundColor(GraphicsConstants.elementLabelColor);
				Point p = getBounds().getTopLeft();
				Point p2 = getBounds().getBottomLeft();
				Point p3 = getBounds().getTopRight();
				p3 = new Point(p3.x, p3.y + getSize().height / 2);
				PointList pointList = new PointList();
				pointList.addPoint(p);
				pointList.addPoint(p2.x, p2.y - 1);
				pointList.addPoint(p3);
				graphics.fillPolygon(pointList);
				graphics.drawPolygon(pointList);
				graphics.popState();
				super.paint(graphics);
			}

		};
		targetFlagFigure.setSize(10, 10);
		sourceFlagFigure.setSize(10, 10);
		ConnectionLocator targetLocator = new ConnectionLocator(conn,
				ConnectionLocator.TARGET);
		conn.add(targetFlagFigure, targetLocator);
		ConnectionLocator sourceLocator = new ConnectionLocator(conn,
				ConnectionLocator.SOURCE);
		conn.add(sourceFlagFigure, sourceLocator);
		return conn;
	}

	protected void changeLineStyleWithCollapseStatus() {
		Shape figure = (Shape) this.getFigure();
		LineConnectionModel model = (LineConnectionModel) this.getModel();
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
		if (collapse) {
			figure.setForegroundColor(GraphicsConstants.groupBorderColor);
			figure.setLineStyle(Graphics.LINE_DOT);
		} else {
			figure.setForegroundColor(GraphicsConstants.groupBorderColor);
			figure.setLineStyle(Graphics.LINE_SOLID);
		}
	}

	protected void changeTreeItemSelectionStatus(boolean selected) {
		LineConnectionModel model = (LineConnectionModel) this.getModel();
		IConnectableModel source = model.getSource();
		IConnectableModel target = model.getTarget();
		Tree sourceTree = null;
		Tree targetTree = null;
		if (source instanceof TreeItemRelationModel) {
			TreeItem item = ((TreeItemRelationModel) source).getTreeItem();
			item.setData(TreeItemRelationModel.PRO_TREE_ITEM_SELECTION_STATUS,
					new Boolean(selected));
			sourceTree = item.getParent();
		}
		if (target instanceof TreeItemRelationModel) {
			TreeItem item = ((TreeItemRelationModel) target).getTreeItem();
			item.setData(TreeItemRelationModel.PRO_TREE_ITEM_SELECTION_STATUS,
					new Boolean(selected));
			targetTree = item.getParent();
		}
		refreshTree(sourceTree);
		refreshTree(targetTree);
	}

	private void refreshTree(Tree tree) {
		if (tree != null) {
			tree.setData(TreeItemRelationModel.PRO_TREE_REPAINT, new Object());
			tree.redraw();
			tree.setData(TreeItemRelationModel.PRO_TREE_REPAINT, null);
		}
	}

	public void setSelected(int value) {
		super.setSelected(value);
		if (value == EditPart.SELECTED_NONE) {
			((Shape) figure).setLineWidth(1);
			((Shape) figure)
					.setForegroundColor(GraphicsConstants.groupBorderColor);
			changeTreeItemSelectionStatus(false);
		} else {
			((Shape) figure).setLineWidth(2);
			figure.setForegroundColor(ColorConstants.darkBlue);
			changeTreeItemSelectionStatus(true);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		
	}
}
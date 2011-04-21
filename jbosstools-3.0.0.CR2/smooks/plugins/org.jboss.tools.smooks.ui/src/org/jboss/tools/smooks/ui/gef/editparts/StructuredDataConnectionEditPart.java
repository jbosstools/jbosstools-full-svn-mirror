package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.ui.gef.figures.CurveLineConnection;
import org.jboss.tools.smooks.ui.gef.figures.ILineFigurePaintListener;
import org.jboss.tools.smooks.ui.gef.figures.LineFigurePaintListenerManager;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.GraphicalModelListenerManager;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.gef.policy.DeleteConnectionEditPolicy;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

public class StructuredDataConnectionEditPart extends
		AbstractConnectionEditPart implements PropertyChangeListener {

	public StructuredDataConnectionEditPart() {
		super();
	}

	public void activate() {
		super.activate();
		Object model = getModel();
		if (model instanceof AbstractStructuredDataConnectionModel) {
			((AbstractStructuredDataConnectionModel) model)
					.addPropertyChangeListener(this);
		}
	}

	public void deactivate() {
		Object model = getModel();
		if (model instanceof AbstractStructuredDataConnectionModel) {
			((AbstractStructuredDataConnectionModel) model)
					.removePropertyChangeListener(this);
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

	private String getSourceDataTypeID() {
		GraphicalViewer viewer = (GraphicalViewer) this.getViewer();
		IEditorPart editor = ((DefaultEditDomain) viewer.getEditDomain())
				.getEditorPart();
		if (editor instanceof SmooksGraphicalFormPage) {
			return ((SmooksGraphicalFormPage) editor).getSourceDataTypeID();
		}
		return null;
	}

	private SmooksConfigurationFileGenerateContext getSmooksContext() {
		GraphicalViewer viewer = (GraphicalViewer) this.getViewer();
		IEditorPart editor = ((DefaultEditDomain) viewer.getEditDomain())
				.getEditorPart();
		if (editor instanceof SmooksGraphicalFormPage) {
			return ((SmooksGraphicalFormPage) editor)
					.getSmooksConfigurationFileGenerateContext();
		}
		return null;
	}

	private String getTargetDataTypeID() {
		GraphicalViewer viewer = (GraphicalViewer) this.getViewer();
		IEditorPart editor = ((DefaultEditDomain) viewer.getEditDomain())
				.getEditorPart();
		if (editor instanceof SmooksGraphicalFormPage) {
			return ((SmooksGraphicalFormPage) editor).getTargetDataTypeID();
		}
		return null;
	}

	protected IFigure createFigure() {
		final String sourceid = getSourceDataTypeID();
		final String targetid = getTargetDataTypeID();
		final ILineFigurePaintListener listener = LineFigurePaintListenerManager
				.getInstance().getPaintListener(sourceid, targetid);
		PolylineConnection connection = null;
		if (listener != null) {
			connection = listener
					.createHostFigure((LineConnectionModel) getModel());
		}
		if (connection == null) {
			connection = new CurveLineConnection(this) {
				public void paintFigure(Graphics graphics) {
					ILineFigurePaintListener listener = LineFigurePaintListenerManager
							.getInstance().getPaintListener(sourceid, targetid);
					if (listener != null) {
						listener.drawLineAdditions(graphics, this,
								(LineConnectionModel) getHostEditPart()
										.getModel());
					}
					super.paintFigure(graphics);
				}
			};
		}
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
				ILineFigurePaintListener listener = LineFigurePaintListenerManager
						.getInstance().getPaintListener(sourceid, targetid);
				if (listener != null) {
					listener.drawLineTargetLocator(graphics, this,
							(LineConnectionModel) getModel());
				}
				super.paint(graphics);
				graphics.popState();
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
				ILineFigurePaintListener listener = LineFigurePaintListenerManager
						.getInstance().getPaintListener(sourceid, targetid);
				if (listener != null) {
					listener.drawLineSourceLocator(graphics, this,
							(LineConnectionModel) getModel());
				}
				super.paint(graphics);
				graphics.popState();
			}

		};
		targetFlagFigure.setSize(10, 10);
		sourceFlagFigure.setSize(10, 10);
		ConnectionLocator targetLocator = new ConnectionLocator(connection,
				ConnectionLocator.TARGET);
		connection.add(targetFlagFigure, targetLocator);
		ConnectionLocator sourceLocator = new ConnectionLocator(connection,
				ConnectionLocator.SOURCE);
		connection.add(sourceFlagFigure, sourceLocator);
		return connection;
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
			// figure.setForegroundColor(GraphicsConstants.groupBorderColor);
			figure.setForegroundColor(new Color(null, 224, 224, 224));
			// figure.setLineStyle(Graphics.LINE_DOT);
		} else {
			figure.setForegroundColor(GraphicsConstants.groupBorderColor);
			// figure.setLineStyle(Graphics.LINE_SOLID);
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
			if (!item.isDisposed()) {
				item.setData(
						TreeItemRelationModel.PRO_TREE_ITEM_SELECTION_STATUS,
						new Boolean(selected));
				sourceTree = item.getParent();
			}
		}
		if (target instanceof TreeItemRelationModel) {
			TreeItem item = ((TreeItemRelationModel) target).getTreeItem();
			if (!item.isDisposed()) {
				item.setData(
						TreeItemRelationModel.PRO_TREE_ITEM_SELECTION_STATUS,
						new Boolean(selected));
				targetTree = item.getParent();
			}
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
		String pm = evt.getPropertyName();
		String sid = getSourceDataTypeID();
		String tid = getTargetDataTypeID();
		IGraphicalModelListener listener = GraphicalModelListenerManager
				.getInstance().getPaintListener(sid, tid);
		if (listener != null) {
			if (AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_REMOVE
					.equals(pm)) {
			}
			if (AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_ADD
					.equals(pm)) {
			}
			if (AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_UPDATE
					.equals(pm)) {
				listener.modelChanged(getModel(), getSmooksContext(), evt);
			}
		}
	}
}
package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.ViewportLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.smooks.ui.gef.figures.ContainerBoxFigure;
import org.jboss.tools.smooks.ui.gef.figures.ContainerFigure;
import org.jboss.tools.smooks.ui.gef.figures.ContainerLeftEdgeAnchor;
import org.jboss.tools.smooks.ui.gef.figures.ContainerRightEdgeAnchor;
import org.jboss.tools.smooks.ui.gef.figures.RoundedLineBorder;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.StructuredDataContentModel;
import org.jboss.tools.smooks.ui.gef.model.StructuredDataModel;
import org.jboss.tools.smooks.ui.gef.policy.CustomGraphicalNodeEditPolicy;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.gef.util.LeftSourceAnchor;
import org.jboss.tools.smooks.ui.gef.util.RightSourceAnchor;
import org.jboss.tools.smooks.ui.gef.util.figures.ContainerLayout;
import org.jboss.tools.smooks.ui.gef.util.figures.FillLayout;

/**
 * 
 */
public class StructuredDataEditPart extends AbstractStructuredDataEditPart
		implements GraphicsConstants, IConnectableEditPart {

	protected ScrollPane scrollpane;
	protected Label label;
	protected ContainerFigure outerPane;

	public IFigure getContentPane() {
		return scrollpane.getContents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String pname = evt.getPropertyName();
		if (AbstractStructuredDataModel.P_CHILDREN.equals(pname)) {
			refreshChildren();
		}
		if (AbstractStructuredDataModel.P_BOUNDS_CHANGE.equals(pname)) {
			refresh();
		}

		if (StructuredDataContentModel.P_SOURCE_CONNECTION.equals(pname)) {
			refreshSourceConnections();
		} else if (StructuredDataContentModel.P_TARGET_CONNECTION.equals(pname)) {
			refreshTargetConnections();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {

		AbstractStructuredDataModel model = (AbstractStructuredDataModel) getModel();

		outerPane = new ContainerBoxFigure();
		outerPane.setMinimumSize(new Dimension(100, 100));
		outerPane.setBorder(new RoundedLineBorder(1, 5));
		outerPane.setForegroundColor(groupBorderColor);
		outerPane.setFill(true);
		outerPane.setBackgroundColor(ColorConstants.white);
		ContainerFigure titleFigure = new ContainerFigure(){
			@Override
			protected void fillShape(Graphics graphics) {
				graphics.pushState();
				graphics.setForegroundColor(graphics.getBackgroundColor());
				graphics.setBackgroundColor(ColorConstants.white);
				graphics.fillGradient(getBounds(), true);
				graphics.popState();
			}
		};
		// r.setCornerDimensions(new Dimension(4, 4));
		titleFigure.setOutline(false);
		titleFigure.setMinimumSize(new Dimension(0, 0));

		titleFigure.setFill(true);

		titleFigure.setBackgroundColor(groupHeaderColor);
		outerPane.add(titleFigure);

		label = new Label();
		label.setForegroundColor(ColorConstants.black);
		label.setBorder(new MarginBorder(2, 4, 2, 4));
		titleFigure.add(label); // Holder);

		RectangleFigure line = new RectangleFigure();
		line.setPreferredSize(20, 1);
		outerPane.add(line);

		int minHeight = 400;
		final int theMinHeight = minHeight;
		FillLayout outerLayout = new FillLayout() {
			protected Dimension calculatePreferredSize(IFigure parent,
					int width, int height) {
				Dimension d = super.calculatePreferredSize(parent, width,
						height);
				d.union(new Dimension(250, theMinHeight));
				return d;
			}
		};

		outerLayout.setHorizontal(false);
		// layout.setSpacing(5);
		outerPane.setLayoutManager(outerLayout);

		scrollpane = new ScrollPane();
		scrollpane.setForegroundColor(ColorConstants.black);
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
		scrollpane.setHorizontalScrollBarVisibility(ScrollPane.AUTOMATIC);
		scrollpane.scrollHorizontalTo(scrollpane.getBounds().width / 2);
		scrollpane.scrollVerticalTo(scrollpane.getBounds().height / 2);
		outerPane.add(scrollpane);

		ContainerFigure pane = new ContainerFigure();
		pane.setBorder(new MarginBorder(5, 8, 5, 8));
		ContainerLayout layout = new ContainerLayout();
		layout.setHorizontal(false);
		layout.setSpacing(0);
		AbstractStructuredDataModel currentModel = (AbstractStructuredDataModel) this
				.getModel();
		if (!currentModel.isLeft()) {
			layout.setAlign(PositionConstants.RIGHT);
		}
		pane.setLayoutManager(layout);
		// pane.setBackgroundColor(ColorConstants.black);
		// pane.setFill(true);

		Viewport viewport = new Viewport();
		viewport.setContentsTracksHeight(true);
		ViewportLayout viewportLayout = new ViewportLayout() {
			protected Dimension calculatePreferredSize(IFigure parent,
					int width, int height) {
				Dimension d = super.calculatePreferredSize(parent, width,
						height);
				d.height = Math.min(d.height, theMinHeight - 25);
				return d;
			}
		};

		viewport.setLayoutManager(viewportLayout);

		scrollpane.setViewport(viewport);
		scrollpane.setContents(pane);
		if (model instanceof StructuredDataModel) {
			label.setText(((StructuredDataModel) model).getLabelName());
		}
		return outerPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ContainerEditPolicy() {

					@Override
					protected Command getCreateCommand(CreateRequest arg0) {
						return null;
					}

				});

		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CustomGraphicalNodeEditPolicy());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
	 * ()
	 */
	@Override
	protected List getModelSourceConnections() {
		return ((IConnectableModel) getModel()).getModelSourceConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	@Override
	protected List getModelTargetConnections() {
		return ((IConnectableModel) getModel()).getModelTargetConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return ((AbstractStructuredDataModel) getModel()).getChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		Rectangle constraint = null;
		AbstractStructuredDataModel model = (AbstractStructuredDataModel) getModel();

		if (model instanceof StructuredDataModel) {
			constraint = ((StructuredDataModel) model).getConstraint();
		}
		if (constraint == null)
			constraint = new Rectangle(0, 0, -1, -1);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), constraint);

		super.refreshVisuals();
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
		return new ContainerRightEdgeAnchor(label);
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
		return new RightSourceAnchor(label);
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return new ContainerLeftEdgeAnchor(label);
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return new LeftSourceAnchor(label);
	}

	public IFigure getAnchroFigure() {
		return label;
	}
}
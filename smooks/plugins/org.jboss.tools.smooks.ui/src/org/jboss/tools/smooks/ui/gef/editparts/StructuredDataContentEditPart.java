package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.smooks.ui.gef.figures.ContainerFigure;
import org.jboss.tools.smooks.ui.gef.figures.ContainerLeftEdgeAnchor;
import org.jboss.tools.smooks.ui.gef.figures.ContainerRightEdgeAnchor;
import org.jboss.tools.smooks.ui.gef.figures.LabelAreaFigure;
import org.jboss.tools.smooks.ui.gef.figures.TreeNodeContentFigure;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.StructuredDataContentModel;
import org.jboss.tools.smooks.ui.gef.policy.ConnectFeedbackEditPolicy;
import org.jboss.tools.smooks.ui.gef.policy.CustomGraphicalNodeEditPolicy;
import org.jboss.tools.smooks.ui.gef.policy.NonResizableSelectionEditPolicy;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.gef.util.LeftSourceAnchor;
import org.jboss.tools.smooks.ui.gef.util.RightSourceAnchor;
import org.jboss.tools.smooks.ui.gef.util.editparts.InteractorHelper;
import org.jboss.tools.smooks.ui.gef.util.figures.Interactor;
import org.jboss.tools.smooks.ui.gef.util.figures.Placeholder;

/**
 * 
 * @author Dart Peng
 * 
 */
public class StructuredDataContentEditPart extends
		AbstractStructuredDataEditPart implements GraphicsConstants,
		IConnectableEditPart {

	protected static final Color label2ForegroundColor = new Color(null, 82,
			82, 158);

	protected Label propertyName;
	protected Label javaTypeLabel;
	protected ContainerFigure labelHolder = new ContainerFigure();
	protected TreeNodeContentFigure contentFigure;
	protected InteractorHelper interactorHelper;
	protected boolean isExpanded = false;

	private EditPart expandPart;

	public void propertyChange(PropertyChangeEvent event) {

		if (event.getPropertyName().equals(
				StructuredDataContentModel.P_SOURCE_CONNECTION)) {
			refreshSourceConnections();
		} else if (event.getPropertyName().equals(
				StructuredDataContentModel.P_TARGET_CONNECTION)) {
			refreshTargetConnections();
		}
	}

	public IFigure getContentPane() {
		return contentFigure.getInnerContentArea();
	}

	protected IFigure createFigure() {
		createContentFigure();
		createFigureContent();
		return contentFigure;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CustomGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new NonResizableSelectionEditPolicy());
		installEditPolicy("CONNECTION_FEED_BACK", new ConnectFeedbackEditPolicy());
	}

	protected void createContentFigure() {
		contentFigure = new TreeNodeContentFigure();
		contentFigure.setFill(true);
	}

	protected void createFigureContent() {

		StructuredDataContentModel model = (StructuredDataContentModel) getModel();

		// add a bit of space between the interactor and icon
		// 

		RectangleFigure space = new RectangleFigure();
		space.setVisible(false);
		space.setPreferredSize(new Dimension(5, 5));

		labelHolder = new LabelAreaFigure();
		labelHolder.setFill(true);

		IFigure interactor = null;

		if (model.getChildren() != null
				&& !model.getChildren().equals(Collections.EMPTY_LIST)) {

			interactor = new Interactor();
			interactor.setBorder(new MarginBorder(0, 0, 0, 5));
			interactor.setForegroundColor(ColorConstants.black);
			interactor.setBackgroundColor(ColorConstants.white);
		} else {
			interactor = new Placeholder();
			interactor.setBorder(new MarginBorder(0, 0, 0, 5));
			interactor.setForegroundColor(ColorConstants.white);
		}

		// contentFigure.getIconArea().getContainerLayout().setAlign(
		// PositionConstants.RIGHT);
		//contentFigure.getIconArea().getContainerLayout().setHorizontal(false);
		if (!((AbstractStructuredDataModel) getModel()).isLeft()) {
			contentFigure.getIconArea().getContainerLayout().setAlign(
					PositionConstants.RIGHT);
			contentFigure.getIconArea().add(space);
			contentFigure.getIconArea().add(labelHolder);
			contentFigure.setInteractor(interactor);
		} else {
			contentFigure.getIconArea().getContainerLayout().setAlign(
					PositionConstants.LEFT);
			contentFigure.setInteractor(interactor);
			contentFigure.getIconArea().add(space);
			contentFigure.getIconArea().add(labelHolder);
		}

		propertyName = new Label(model.getLabelName());
		propertyName.setForegroundColor(ColorConstants.black);
//		propertyName.setTextAlignment(PositionConstants.RIGHT);
		// propertyName.setBackgroundColor(ColorConstants.red);
		// propertyName.setOpaque(true);

		// if ( model.isRootNode() ) {
		// propertyName.setIcon(SDPlugin.getImageDescriptor(IImagePaths.
		// IMAGE_XSD_ALL).createImage());
		// } else if ( model.isListType() ) {
		// propertyName.setIcon(SDPlugin.getImageDescriptor(IImagePaths.
		// IMAGE_XSD_CHOICE).createImage());
		// } else if ( !model.isListType() && model.isComplexType() ) {
		// propertyName.setIcon(SDPlugin.getImageDescriptor(IImagePaths.
		// IMAGE_XSD_COMPLEX_TYPE).createImage());
		// } else if ( !model.isListType() && !model.isComplexType() ) {
		// propertyName.setIcon(SDPlugin.getImageDescriptor(IImagePaths.
		// IMAGE_XSD_SIMPLE_TYPE).createImage());
		// }
		// contentFigure.getIconArea().setFill(true);
		// contentFigure.getIconArea().setBackgroundColor(ColorConstants.black);
		labelHolder.add(propertyName);

		if (model.getChildren() != null
				&& !model.getChildren().equals(Collections.EMPTY_LIST)) {
			interactorHelper = new InteractorHelper(this, contentFigure
					.getInteractor(), contentFigure.getInnerContentArea());
		}

		javaTypeLabel = new Label();
		javaTypeLabel.setBorder(new MarginBorder(0, 5, 0, 0));
		javaTypeLabel.setForegroundColor(label2ForegroundColor);
		// javaTypeLabel.setIcon(SDPlugin.getImageDescriptor(
		// "icons/XSDSimpleType.gif").createImage());

		if (model.getTypeString() != null) {
			javaTypeLabel.setText("- (" + model.getTypeString() + ")");
		}
		labelHolder.add(javaTypeLabel);
	}

	protected void refreshVisuals() {
		Rectangle constraint = ((StructuredDataContentModel) getModel())
				.getConstraint();

		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), constraint);

		super.refreshVisuals();
	}

	public void notifyExpand(EditPart part) {
		expandPart = part;
		List sourceConnections = this.getSourceConnections();
		for (Iterator iter = sourceConnections.iterator(); iter.hasNext();) {
			AbstractConnectionEditPart sourceC = (AbstractConnectionEditPart) iter
					.next();
			sourceC.refresh();
		}
		List targetConnections = this.getTargetConnections();
		for (Iterator iter = targetConnections.iterator(); iter.hasNext();) {
			AbstractConnectionEditPart sourceC = (AbstractConnectionEditPart) iter
					.next();
			sourceC.refresh();
		}

	}

	public IFigure getSelectionFigure() {
		return ((TreeNodeContentFigure) getFigure()).getIconArea();
	}

	public IFigure getAnchroFigure() {
		if (expandPart == null)
			return ((TreeNodeContentFigure) getFigure()).getIconArea();

		if (expandPart instanceof StructuredDataContentEditPart) {
			IFigure figure = ((StructuredDataContentEditPart) expandPart)
					.getFigure();
			if (figure instanceof TreeNodeContentFigure) {
				return ((TreeNodeContentFigure) figure).getIconArea();
			}
		}
		return ((GraphicalEditPart) expandPart).getFigure();
	}

	protected List getModelChildren() {
		return ((AbstractStructuredDataModel) getModel()).getChildren();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new ContainerRightEdgeAnchor(getAnchroFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new ContainerLeftEdgeAnchor(getAnchroFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new RightSourceAnchor(getAnchroFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new LeftSourceAnchor(getAnchroFigure());
	}

	public List getModelSourceConnections() {
		return ((StructuredDataContentModel) getModel())
				.getModelSourceConnections();
	}

	public List getModelTargetConnections() {
		return ((StructuredDataContentModel) getModel())
				.getModelTargetConnections();
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
}
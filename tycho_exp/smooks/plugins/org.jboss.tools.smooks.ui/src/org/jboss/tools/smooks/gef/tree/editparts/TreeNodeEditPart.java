/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editparts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.ConnectionDragCreationTool;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.gef.tree.editpolicy.FigureHighlightEditPolicy;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeGraphicalNodeEditPolicy;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeSelectEditPolicy;
import org.jboss.tools.smooks.gef.tree.figures.ITreeFigureListener;
import org.jboss.tools.smooks.gef.tree.figures.LeftOrRightAnchor;
import org.jboss.tools.smooks.gef.tree.figures.TreeContainerFigure;
import org.jboss.tools.smooks.gef.tree.figures.TreeFigureExpansionEvent;
import org.jboss.tools.smooks.gef.tree.figures.TreeNodeFigure;
import org.jboss.tools.smooks.gef.tree.figures.TreeNodeTextDirectManager;
import org.jboss.tools.smooks.gef.tree.figures.TreeNodeTextDirectManagerLocator;
import org.jboss.tools.smooks.gef.tree.model.IConnectableNode;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * @author DartPeng
 * 
 */
public class TreeNodeEditPart extends AbstractTreeEditPart implements ITreeFigureListener, NodeEditPart {

	protected boolean childrenLoaded = false;

	private IFigure anchorFigure = null;

	protected DirectEditManager editManager = null;

	@Override
	protected IFigure createFigure() {
		TreeNodeModel model = (TreeNodeModel) getModel();
		TreeNodeFigure figure = new TreeNodeFigure(model);
		figure.addTreeListener(this);
		return figure;
	}

	protected void performDirectEdit() {
		if (canDirectEdit()) {
			if (editManager == null) {
				editManager = createDirectEditManager();
			}
			editManager.show();
		}
	}

	protected DirectEditManager createDirectEditManager() {
		Label l = ((TreeNodeFigure) getFigure()).getLabel();
		return new TreeNodeTextDirectManager(this, TextCellEditor.class, new TreeNodeTextDirectManagerLocator(l), l);
	}

	protected boolean canDirectEdit() {
		return false;
	}

	/**
	 * @return the dragLink
	 */
	protected boolean isDragLink() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org
	 * .eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		if (isDragLink()) {
			getViewer().select(this);
			return new ConnectionDragCreationTool();
		} else {
			return super.getDragTracker(request);
		}
	}

	public TreeContainerEditPart getTreeContainerEditPart() {
		EditPart parent = this.getParent();
		while (parent != null && (parent.getClass() != TreeContainerEditPart.class)) {
			parent = parent.getParent();
		}
		if (parent != null && parent.getClass() == TreeContainerEditPart.class) {
			return (TreeContainerEditPart) parent;
		}
		return null;
	}

	public void expandNode() {
		IFigure figure1 = getFigure();
		if (figure1 instanceof TreeNodeFigure) {
			((TreeNodeFigure) figure1).expandNode();
		}
	}

	public void collapsedNode() {
		IFigure figure1 = getFigure();
		if (figure1 instanceof TreeNodeFigure) {
			((TreeNodeFigure) figure1).collapsedNode();
		}
	}

	public boolean isSourceLinkNodeEditPart() {
		TreeContainerEditPart container = getTreeContainerEditPart();
		if (container != null)
			return container.isSourceLinkNodeEditPart();
		return true;
	}

	protected Command createDirectEditCommand(DirectEditRequest request) {
		return null;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicy() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand
			 * (org.eclipse.gef.requests.DirectEditRequest)
			 */
			@Override
			protected Command getDirectEditCommand(DirectEditRequest request) {
				return createDirectEditCommand(request);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue
			 * (org.eclipse.gef.requests.DirectEditRequest)
			 */
			@Override
			protected void showCurrentEditValue(DirectEditRequest request) {

			}

		});
		this.installEditPolicy(EditPolicy.NODE_ROLE, new TreeNodeGraphicalNodeEditPolicy());
		this.installEditPolicy("SELECTED", new TreeNodeSelectEditPolicy());
		this.installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new FigureHighlightEditPolicy());
	}

	@Override
	public IFigure getContentPane() {
		TreeNodeFigure figure = (TreeNodeFigure) getFigure();
		return figure.getContentFigure();
	}

	protected void refreshVisuals() {
		TreeNodeModel node = (TreeNodeModel) getModel();
		String text = node.getText();
		if (text != null) {
			((TreeNodeFigure) getFigure()).setLabelText(text);
		}
		Image image = node.getImage();
		if (image != null) {
			((TreeNodeFigure) getFigure()).setLabelImage(image);
		}
		super.refreshVisuals();

		// Dimension size = getFigure().getPreferredSize(-1, -1);
		// Rectangle rect = getFigure().getBounds();
		// rect.setSize(size);
		// ((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this,
		// this.getFigure(), rect);
	}

	public List<?> getModelChildren() {
		if (childrenLoaded) {
			TreeNodeModel node = (TreeNodeModel) getModel();
			return node.getChildren();
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	public void childrenModelChanged() {
		refreshChildren();
	}

	public void treeCollapsed(TreeFigureExpansionEvent event) {
		caculateAnchorFigure();
	}

	public void treeExpanded(TreeFigureExpansionEvent event) {
		if (!childrenLoaded) {
			childrenLoaded = true;
			refreshChildren();
			refreshVisuals();
		}
		caculateAnchorFigure();
	}

	/**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String proName = evt.getPropertyName();
		if (TreeNodeModel.PRO_FORCE_VISUAL_CHANGED.equals(proName) || TreeNodeModel.PRO_TEXT_CHANGED.equals(proName)) {
			refreshVisuals();
		}
		if (TreeNodeModel.PRO_ADD_CHILD.equals(proName) || TreeNodeModel.PRO_MOVE_CHILD.equals(proName)
				|| TreeNodeModel.PRO_REMOVE_CHILD.equals(proName)
				|| TreeNodeModel.PRO_FORCE_CHIDLREN_CHANGED.equals(proName)) {
			refreshChildren();
			this.expandNode();
			refreshVisuals();
		}
		if (TreeNodeModel.PRO_ADD_SOURCE_CONNECTION.equals(proName)
				|| TreeNodeModel.PRO_REMOVE_SOURCE_CONNECTION.equals(proName)) {
			refreshSourceConnections();
		}
		if (TreeNodeModel.PRO_ADD_TARGET_CONNECTION.equals(proName)
				|| TreeNodeModel.PRO_REMOVE_TARGET_CONNECTION.equals(proName)) {
			refreshTargetConnections();
		}

		if (TreeNodeModel.PRO_FORCE_CONNECTION_CHANGED.equals(proName)) {
			refreshSourceConnections();
			refreshTargetConnections();
		}
	}

	protected void recordBounds(SmooksGraphicsExtType graphicsExt, Rectangle bounds) {
		GraphType graph = graphicsExt.getGraph();
		if (graph == null) {
			graph = GraphFactory.eINSTANCE.createGraphType();
			graphicsExt.setGraph(graph);
		}
		String figureId = generateFigureID();
		if (figureId == null)
			return;
		FigureType figure = SmooksGraphUtil.findFigureType(graph, figureId);

		if (figure == null) {
			figure = GraphFactory.eINSTANCE.createFigureType();
			figure.setId(figureId);
			graph.getFigure().add(figure);
		}
		recordFigureBounds(figure, bounds);
	}

	protected void recordFigureBounds(FigureType figureType, Rectangle bounds) {
		figureType.setX(String.valueOf(bounds.getLocation().x));
		figureType.setY(String.valueOf(bounds.getLocation().y));

		figureType.setHeight(String.valueOf(bounds.getSize().height));
		figureType.setWidth(String.valueOf(bounds.getSize().width));

		EObject ext = figureType;
		while (ext != null && !(ext instanceof SmooksGraphicsExtType)) {
			ext = ext.eContainer();
		}

		if (ext != null && ext instanceof SmooksGraphicsExtType) {
			List<ISmooksGraphChangeListener> listeners = ((SmooksGraphicsExtType) ext).getChangeListeners();
			for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
				ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator.next();
				smooksGraphChangeListener.graphChanged((SmooksGraphicsExtType) ext);
			}
		}
	}

	protected String generateFigureID() {
		return null;
	}

	protected IFigure getAnchorFigure() {
		if (anchorFigure != null)
			return anchorFigure;
		else {
			IFigure figure = getFigure();
			anchorFigure = figure;
			if (figure instanceof TreeContainerFigure) {
				anchorFigure = ((TreeContainerFigure) figure).getLabel();
			}
			if (figure instanceof TreeNodeFigure) {
				anchorFigure = ((TreeNodeFigure) figure).getLabel();
			}
		}
		return anchorFigure;
	}

	public void caculateAnchorFigure() {
		anchorFigure = null;
		EditPart parent = getParent();
		List<TreeNodeEditPart> parentList = new ArrayList<TreeNodeEditPart>();
		while (parent != null && parent instanceof TreeNodeEditPart) {
			parentList.add((TreeNodeEditPart) parent);
			if (parent.getParent() == null || !(parent.getParent() instanceof TreeNodeEditPart)) {
				break;
			}
			parent = parent.getParent();
		}

		for (int i = parentList.size() - 1; i >= 0; i--) {
			TreeNodeEditPart p = parentList.get(i);
			if (p.isExpanded())
				continue;
			anchorFigure = p.getAnchorFigure();
			break;
		}
		int alpha = 255;
		if (anchorFigure != null) {
			alpha = 20;
		}
		List<?> sourceConnectionEditParts = getSourceConnections();
		for (Iterator<?> iterator = sourceConnectionEditParts.iterator(); iterator.hasNext();) {
			Object con = (Object) iterator.next();
			if (con instanceof TreeNodeConnectionEditPart) {
				((TreeNodeConnectionEditPart) con).changeLineAlpha(alpha);
				((TreeNodeConnectionEditPart) con).refresh();
			}
		}

		List<?> targetConnectionEditParts = getTargetConnections();
		for (Iterator<?> iterator = targetConnectionEditParts.iterator(); iterator.hasNext();) {
			Object con = (Object) iterator.next();
			if (con instanceof TreeNodeConnectionEditPart) {
				((TreeNodeConnectionEditPart) con).changeLineAlpha(alpha);
				((TreeNodeConnectionEditPart) con).refresh();
			}
		}
		List<?> childrenEditParts = getChildren();
		for (Iterator<?> iterator = childrenEditParts.iterator(); iterator.hasNext();) {
			EditPart child = (EditPart) iterator.next();
			if (child instanceof TreeNodeEditPart) {
				((TreeNodeEditPart) child).caculateAnchorFigure();
			}
		}
	}

	public boolean isExpanded() {
		IFigure f = getFigure();
		if (f instanceof TreeNodeFigure) {
			return ((TreeNodeFigure) f).isExpand();
		}
		return true;
	}

	@Override
	protected List<?> getModelSourceConnections() {
		Object model = getModel();
		if (model instanceof IConnectableNode) {
			return ((IConnectableNode) model).getSourceConnections();
		}
		return Collections.emptyList();
	}

	@Override
	protected List<?> getModelTargetConnections() {
		Object model = getModel();
		if (model instanceof IConnectableNode) {
			return ((IConnectableNode) model).getTargetConnections();
		}
		return Collections.emptyList();

	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		try {
			return new LeftOrRightAnchor(getAnchorFigure());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		try {
			return new LeftOrRightAnchor(getAnchorFigure());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new LeftOrRightAnchor(getAnchorFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new LeftOrRightAnchor(getAnchorFigure());
	}

	public CreateConnectionCommand createCreateConnectionCommand() {
		return null;
	}
}

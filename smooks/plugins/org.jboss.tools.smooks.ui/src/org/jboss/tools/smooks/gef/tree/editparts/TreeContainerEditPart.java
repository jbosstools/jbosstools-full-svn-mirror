/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeGraphicalNodeEditPolicy;
import org.jboss.tools.smooks.gef.tree.figures.IMoveableModel;
import org.jboss.tools.smooks.gef.tree.figures.TreeContainerFigure;
import org.jboss.tools.smooks.gef.tree.figures.TreeFigureExpansionEvent;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;

/**
 * @author DartPeng
 * 
 */
public class TreeContainerEditPart extends TreeNodeEditPart {

	public TreeContainerEditPart() {
		super();
		childrenLoaded = true;
	}

	@Override
	protected IFigure createFigure() {
		return new TreeContainerFigure((TreeContainerModel) getModel());
	}

	protected void refreshVisuals() {
		TreeContainerModel model = (TreeContainerModel) getModel();
		String text = model.getText();
		if (text != null && model.isHeaderVisable() && getFigure() instanceof TreeContainerFigure) {
			TreeContainerFigure figure = (TreeContainerFigure) getFigure();
			figure.setText(text);
		}
		boolean isSource = this.isSourceLinkNodeEditPart();
		if (!isSource) {
			IFigure figure = getFigure();
			if (figure instanceof TreeContainerFigure) {
				((TreeContainerFigure) figure).setHeaderColor(ColorConstants.orange);
			}
		}
		Point location = model.getLocation();
		Dimension size = getFigure().getPreferredSize();
		try {
			((GraphicalEditPart) this.getParent()).setLayoutConstraint(this, this.getFigure(), new Rectangle(location,
					size));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IFigure getContentPane() {
		if (getFigure() instanceof TreeContainerFigure) {
			TreeContainerFigure t = (TreeContainerFigure) getFigure();
			return t.getContentFigure();
		} else {
			return getFigure();
		}
	}

	public TreeContainerEditPart getTreeContainerEditPart() {
		return this;
	}

	public boolean isSourceLinkNodeEditPart() {
		Object model = getModel();
		if (model != null && model instanceof TreeContainerModel) {
			return ((TreeContainerModel) model).isSourceLinkNode();
		}
		return false;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (IMoveableModel.PRO_BOUNDS_CHANGED.equals(evt.getPropertyName())) {
			refresh();
		}
	}

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.NODE_ROLE, new TreeNodeGraphicalNodeEditPolicy());
	}

	// @Override
	// public ConnectionAnchor getSourceConnectionAnchor(
	// ConnectionEditPart connection) {
	// return new ChopboxAnchor(getFigure());
	// }
	//
	// @Override
	// public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	// return new ChopboxAnchor(getFigure());
	// }
	//
	// @Override
	// public ConnectionAnchor getTargetConnectionAnchor(
	// ConnectionEditPart connection) {
	// return new ChopboxAnchor(getFigure());
	// }
	//
	// @Override
	// public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	// return new ChopboxAnchor(getFigure());
	// }

	@Override
	public List<?> getModelChildren() {
		TreeNodeModel node = (TreeNodeModel) getModel();
		return node.getChildren();
		// return super.getModelChildren();
	}

	@Override
	public void treeCollapsed(TreeFigureExpansionEvent event) {
		// super.treeCollapsed(event);
	}

	@Override
	public void treeExpanded(TreeFigureExpansionEvent event) {
		// super.treeExpanded(event);
	}

}

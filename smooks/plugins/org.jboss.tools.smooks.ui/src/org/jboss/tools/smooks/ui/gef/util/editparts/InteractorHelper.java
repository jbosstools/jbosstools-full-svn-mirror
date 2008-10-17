package org.jboss.tools.smooks.ui.gef.util.editparts;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.jboss.tools.smooks.ui.gef.editparts.StructuredDataContentEditPart;
import org.jboss.tools.smooks.ui.gef.figures.IExpandable;
import org.jboss.tools.smooks.ui.gef.util.figures.Interactor;

/**
 * @deprecated
 *
 */
public class InteractorHelper implements MouseListener {

	protected boolean needToPerformDefaultExpansion = true;

	protected EditPart editPart;

	protected Interactor interactor;

	protected IExpandable expandable;

	public InteractorHelper(EditPart editPart, Interactor interactor,
			IExpandable expandable) {
		this.editPart = editPart;
		this.interactor = interactor;
		this.expandable = expandable;
		interactor.addMouseListener(this);
		expandable.setExpanded(interactor.isExpanded());
	}

	public void setExpanded(boolean isExpanded) {
		interactor.setExpanded(isExpanded);
		interactorExpansionChanged(isExpanded);
	}

	protected void interactorExpansionChanged(boolean isInteractorExpanded) {
		expandable.setExpanded(isInteractorExpanded);
		expandable.setVisible(isInteractorExpanded);

		editPart.refresh();
		((StructuredDataContentEditPart) editPart).setExpanded(!expandable
				.isExpanded());
		expandChildrenLinesToMe(editPart, expandable.isExpanded());

		EditPart root = editPart.getRoot();

		if (root instanceof AbstractGraphicalEditPart) {
			IFigure rootFigure = ((AbstractGraphicalEditPart) root).getFigure();
			invalidateAll(rootFigure);
			rootFigure.validate();
			rootFigure.repaint();
		}
	}

	private void expandChildrenLinesToMe(EditPart parentEditPart,
			boolean expanded) {
		List children = parentEditPart.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Object child = iter.next();
			if (child instanceof StructuredDataContentEditPart) {
				if (!expanded) {
					((StructuredDataContentEditPart) child).notifyExpand(editPart);
				} else {
					EditPart part = null;
					if (((StructuredDataContentEditPart) parentEditPart).isExpanded()) {
						StructuredDataContentEditPart finalEditPart = (StructuredDataContentEditPart)parentEditPart;
						while(finalEditPart.isExpanded()){
							 Object myParent = finalEditPart.getParent();
							 part = finalEditPart;
							 if(myParent instanceof StructuredDataContentEditPart){
								 finalEditPart = (StructuredDataContentEditPart)myParent;
							 }
						}
					}
					((StructuredDataContentEditPart) child).notifyExpand(part);

				}
				if (!((StructuredDataContentEditPart) child).getChildren().isEmpty()) {
					expandChildrenLinesToMe((StructuredDataContentEditPart) child,
							expanded);
				}
			}
		}
	}

	protected void invalidateAll(IFigure figure) {
		figure.invalidate();
		LayoutManager manager = figure.getLayoutManager();

		if (manager != null) {
			manager.invalidate();
		}

		for (Iterator i = figure.getChildren().iterator(); i.hasNext();) {
			IFigure child = (IFigure) i.next();
			invalidateAll(child);
		}
	}

	public void mousePressed(MouseEvent me) {
		boolean newExpansionState = !interactor.isExpanded();
		interactor.setExpanded(newExpansionState);
		interactorExpansionChanged(newExpansionState);
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseDoubleClicked(MouseEvent me) {
	}
}
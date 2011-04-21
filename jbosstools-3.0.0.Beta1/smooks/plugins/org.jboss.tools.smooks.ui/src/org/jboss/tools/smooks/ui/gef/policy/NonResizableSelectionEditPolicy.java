/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.jboss.tools.smooks.ui.gef.figures.ISelectableFigure;

/**
 * 
 * @author Dart Peng
 * 
 * @CreateTime Jul 21, 2008
 */
public class NonResizableSelectionEditPolicy extends NonResizableEditPolicy {

	ISelectableFigure figure = null;

	public NonResizableSelectionEditPolicy() {
		super();
		if (this.getHost() instanceof GraphicalEditPart) {
			IFigure f = ((GraphicalEditPart) this.getHost()).getFigure();
			if (f != null) {
				if (f instanceof ISelectableFigure) {
					figure = (ISelectableFigure) f;
				}
			}
		}
	}

	/*
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#hideFocus()
	 */
	protected void hideFocus() {
		if (getFigure() != null) {
			getFigure().setFocus(false);
		} else {
			super.hideFocus();
		}
	}

	/*
	 * @see
	 * org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		if (getFigure() != null) {
			getFigure().setFocus(false);
			getFigure().setSelected(false);
		} else {
			super.hideSelection();
		}
	}

	/*
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	protected void showFocus() {
		if (getFigure() != null) {
			getFigure().setFocus(true);
		} else {
			super.showFocus();
		}
	}

	/*
	 * @see
	 * org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
	 */
	protected void showPrimarySelection() {
		if (getFigure() != null) {
			getFigure().setFocus(true);
			getFigure().setSelected(true);
		} else {
			super.showPrimarySelection();
		}
	}

	/*
	 * @see
	 * org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
	 */
	protected void showSelection() {
		if (getFigure() != null) {
			getFigure().setFocus(false);
			getFigure().setSelected(true);
		} else {
			super.showSelection();
		}
	}

	/**
	 * @return ����figure�ֶ�ֵ.
	 */
	public ISelectableFigure getFigure() {
		if (figure == null) {
			if (this.getHost() instanceof GraphicalEditPart) {
				IFigure f = ((GraphicalEditPart) this.getHost()).getFigure();
				if (f != null) {
					if (f instanceof ISelectableFigure) {
						figure = (ISelectableFigure) f;
					}
				}
			}
		}
		return figure;
	}

	/**
	 * @param ��figure ���ø� figure.
	 */
	public void setFigure(ISelectableFigure figure) {
		this.figure = figure;
	}
}

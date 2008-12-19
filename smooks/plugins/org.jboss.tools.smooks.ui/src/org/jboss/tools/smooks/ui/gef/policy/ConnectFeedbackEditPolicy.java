/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.jboss.tools.smooks.ui.gef.figures.ISelectableFigure;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 21, 2008
 */
public class ConnectFeedbackEditPolicy extends GraphicalEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#eraseSourceFeedback(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public void eraseSourceFeedback(Request request) {
		// this.showHighLight(false);
		super.eraseSourceFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#eraseTargetFeedback(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public void eraseTargetFeedback(Request request) {
		if (request.getType().equals(RequestConstants.REQ_CONNECTION_END))
			this.showHighLight(false);
		super.eraseTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#showSourceFeedback(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public void showSourceFeedback(Request request) {
		Object requestType = request.getType();
		if (requestType.equals(RequestConstants.REQ_CONNECTION_START)) {
			this.showHighLight(true);
		}
		super.showSourceFeedback(request);
	}

	protected void showHighLight(boolean show) {
		IFigure figure = ((GraphicalEditPart) this.getHost()).getFigure();
		if (figure instanceof ISelectableFigure) {
			((ISelectableFigure) figure).setSelected(show);
		}
	}

	/*
	 * @see
	 * org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost()
				: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#showTargetFeedback(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public void showTargetFeedback(Request request) {
		Object requestType = request.getType();
		if (requestType.equals(RequestConstants.REQ_CONNECTION_END)) {
			this.showHighLight(true);
		}
		super.showTargetFeedback(request);
	}

}

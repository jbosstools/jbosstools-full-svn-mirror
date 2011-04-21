/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.xulrunner.editor;

import org.eclipse.core.runtime.Platform;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSEvent;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.xpcom.Mozilla;

/**
 *
 * @author A. Yukhovich
 */
public class VpeResizerMouseListener implements nsIDOMEventListener {

	private IXulRunnerVpeResizer vpeResizer;

	/**
	 * Default contructor
	 */
	public VpeResizerMouseListener(IXulRunnerVpeResizer vpeResizer) {
		super();
		this.vpeResizer = vpeResizer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.interfaces.nsIDOMEventListener#handleEvent(org.mozilla.interfaces.nsIDOMEvent)
	 */
	public void handleEvent(nsIDOMEvent event) {
		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event
				.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);

		if (mouseEvent == null) {
			return;
		}

		if (XulRunnerConstants.EVENT_NAME_MOUSEDOWN.equals(mouseEvent.getType())) {
			mouseDown(mouseEvent);
			//Added By Max Areshkau without this code, when we resize, eclipse is crashes
			event.preventDefault();
			event.stopPropagation();
		} else if (XulRunnerConstants.EVENT_NAME_MOUSEUP.equals(mouseEvent.getType())) {
			mouseUp(mouseEvent);
		} 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
	 */
	public nsISupports queryInterface(String aIID) {
		if (aIID.equals(nsIDOMEventListener.NS_IDOMEVENTLISTENER_IID)) {
			return this;
		} // if

		return Mozilla.queryInterface(this, aIID);
	}

	/**
	 * processing event of mouseDown
	 * 
	 * @param mouseEvent
	 *            a nsIDOMMouseEvent object
	 */
	private void mouseDown(nsIDOMMouseEvent mouseEvent) {
		boolean isContextClick = false;

		if (Platform.getOS().equals("SunOS")) { //$NON-NLS-1$
			isContextClick = mouseEvent.getCtrlKey();
		} else {
			isContextClick = (mouseEvent.getButton() == 2);
		}

		if (!isContextClick && (mouseEvent.getButton() == 0)
				&& (mouseEvent.getDetail() == 1)) {
			nsIDOMNSEvent internalEvent = (nsIDOMNSEvent) mouseEvent
					.queryInterface(nsIDOMNSEvent.NS_IDOMNSEVENT_IID);
			if (internalEvent != null) {
				org.mozilla.interfaces.nsIDOMEventTarget eventTarget = internalEvent
						.getExplicitOriginalTarget();
				if (eventTarget != null) {
					nsIDOMElement domElement = (nsIDOMElement) eventTarget
							.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
					if (domElement != null) {
						int clientX = mouseEvent.getClientX();
						int clientY = mouseEvent.getClientY();
						
						vpeResizer.mouseDown(clientX, clientY, domElement);
					}
				} // if
			} // if

		}
	}

	/**
	 * processing event of mouseUp
	 * 
	 * @param mouseEvent
	 *            a nsIDOMMouseEvent object
	 */
	private void mouseUp(nsIDOMMouseEvent mouseEvent) {
		 nsIDOMEventTarget target = mouseEvent.getTarget();
		 
		 if (target != null ) {
				nsIDOMElement domElement = (nsIDOMElement) target.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				if (domElement != null) {
					int clientX = mouseEvent.getClientX();
					int clientY = mouseEvent.getClientY();
					vpeResizer.mouseUp(clientX, clientY, domElement);
				}
		 }
	}
}

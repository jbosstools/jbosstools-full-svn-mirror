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

import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.xpcom.Mozilla;

/**
 * @author A. Yukhovich
 */
public class VpeResizerMouseMotionListener implements nsIDOMEventListener {
	/** vpeRezizer */
	private IXulRunnerVpeResizer vpeResizer; 
	
	/**
	 * Default constructor
	 * @param vpeResizer a IVpeResizer object
	 */
	public VpeResizerMouseMotionListener(IXulRunnerVpeResizer vpeResizer) {
		this.vpeResizer = vpeResizer;
	}
	
	/**
	 * mouse move
	 * @param event a nsIDOMEvent object
	 */
	public void mouseMove(nsIDOMEvent event) {
		nsIDOMMouseEvent mouseEvent = XPCOM.queryInterface(event, nsIDOMMouseEvent.class);
		
		if ( mouseEvent != null ) {
			vpeResizer.mouseMove(mouseEvent);
		}
	}
	
	public void dragMove(nsIDOMEvent event) {
	}

	public void handleEvent(nsIDOMEvent event) {
		if (XulRunnerConstants.EVENT_NAME_MOUSEMOVE.equals(event.getType())) {
			mouseMove(event);
		}
	}

	public nsISupports queryInterface(String aIID) {
		if (aIID.equals(nsIDOMEventListener.NS_IDOMEVENTLISTENER_IID)) {
			return this;
		} // if

		return Mozilla.queryInterface(this, aIID);
	}

}

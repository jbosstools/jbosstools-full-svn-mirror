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

import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsISupports;

/**
 * a IVpeResizeListener interface
 * @author A. Yukhovich
 */
public interface IVpeResizeListener extends nsISupports {
	/**
	 * Event handler for 'object end resizing'
	 * @param usedResizeMarkerHandle a used resize marker handle
	 * @param top a new top position
	 * @param left a new left position
	 * @param width a new width
	 * @param height a new height
	 * @param resizedDomElement a resized nsIDOMElement 
	 */
	public void onEndResizing(int usedResizeMarkerHandle, int top, int left, int width, int height, nsIDOMElement resizedDomElement);
}

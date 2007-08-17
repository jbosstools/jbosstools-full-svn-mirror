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

import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;

/**
 * IXulRunnerVpeResizer interface class 
 * @author A. Yukhovich
 */
public interface IXulRunnerVpeResizer {
	/** RESIZER_MARKER_TOPLEFT */
	final static public int RESIZER_MARKER_TOPLEFT = 1;

	/** RESIZER_MARKER_TOP */
	final static public int RESIZER_MARKER_TOP = 2;

	/** RESIZER_MARKER_TOPRIGHT */
	final static public int RESIZER_MARKER_TOPRIGHT = 4;

	/** RESIZER_MARKER_LEFT */
	final static public int RESIZER_MARKER_LEFT = 8;

	/** RESIZER_MARKER_RIGHT */
	final static public int RESIZER_MARKER_RIGHT = 16;

	/** RESIZER_MARKER_BOTTOMLEFT */
	final static public int RESIZER_MARKER_BOTTOMLEFT = 32;

	/** RESIZER_MARKER_BOTTOM */
	final static public int RESIZER_MARKER_BOTTOM = 64;

	/** RESIZER_MARKER_BOTTOMRIGHT */
	final static public int RESIZER_MARKER_BOTTOMRIGHT = 128; 
	
	
	public void init(nsIDOMDocument domDocument); 
	
	/**
	 * Show resize markers
	 * @param domElement
	 * @param resizers
	 */
	public void show(nsIDOMElement domElement, int resizers);
	
	/**
	 * Hide resize markers
	 */
	public void hide();
	
	/**
	 * Event handler MouseDown
	 * @param clientX
	 * @param clientY
	 * @param domElement a target nsIDOMElement
	 */
	public void mouseDown(int clientX, int clientY, nsIDOMElement domElement);
	
	/**
	 * Event handler MouseMove
	 * @param event
	 */
	public void mouseMove(nsIDOMEvent event);

	/**
	 * Event handler MouseUp 
	 * @param clientX 
	 * @param clientY
	 * @param target a target nsIDOMElement
	 */
	public void mouseUp(int clientX, int clientY, nsIDOMElement target);

	/**
	 * add resize listener to queue 
	 * @param aListener a IVpeResizeListener object
	 */
	public void addResizeListener(IVpeResizeListener aListener);
	
	/**
	 * remove resize listener from queue
	 * @param aListener a IVpeResizeListener object
	 */
	public void removeResizeListener(IVpeResizeListener aListener);
}

/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import org.mozilla.interfaces.nsIDOMKeyEvent;

public interface IZoomEventManager {

	long ZOOM_IN_CH_CODE = nsIDOMKeyEvent.DOM_VK_EQUALS;

	long ZOOM_OUT_CH_CODE = 45L;
	
	long ZOOM_RESET_CH_CODE = nsIDOMKeyEvent.DOM_VK_0;

	boolean zoomIn();

	boolean zoomOut();

	boolean resetZoomView();
	
	float getCurrentZoom();
	
	boolean setCurrentZoom(float zoomValue);
	
	float getMinZoom();
	
	float getMaxZoom();
	
	float getBasicZoom();
	
	float[] getAvailableZoomValues();

}

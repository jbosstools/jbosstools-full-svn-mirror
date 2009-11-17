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

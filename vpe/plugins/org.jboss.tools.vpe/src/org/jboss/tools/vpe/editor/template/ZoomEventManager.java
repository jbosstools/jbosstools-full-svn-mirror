/******************************************************************************* 
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

/**
 * @author yzhishko
 */
import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIContentViewer;
import org.mozilla.interfaces.nsIDOMAbstractView;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMDocumentView;
import org.mozilla.interfaces.nsIDocShell;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.mozilla.interfaces.nsIMarkupDocumentViewer;
import org.mozilla.interfaces.nsIWebNavigation;

public class ZoomEventManager implements IZoomEventManager{

	private static final float basicZoom = 1f;
	private static final float minZoom = 0.3f;
	private static final float maxZoom = 4f;
	private float[] availableZoomValues;
	private int currentZoomPos = 5;
	private static final float decreaseValue = 0.14f;
	private static final float increaseValue = 0.375f;
	private nsIMarkupDocumentViewer documentViewer;

	public ZoomEventManager(XulRunnerEditor editor) {
		availableZoomValues = new float[14];
		availableZoomValues[0] = minZoom;
		availableZoomValues[13] = maxZoom;
		availableZoomValues[5] = basicZoom;
		for (int i = 1; i < 5; i++) {
			availableZoomValues[i] = availableZoomValues[i - 1] + decreaseValue;
		}
		for (int i = 6; i < 13; i++) {
			availableZoomValues[i] = availableZoomValues[i - 1] + increaseValue;
		}
		documentViewer = initMarkupViewer(editor);
	}

	public boolean zoomIn() {
		if (documentViewer != null) {
			if (availableZoomValues[currentZoomPos] == maxZoom) {
				return false;
			}
			currentZoomPos++;
			documentViewer.setFullZoom(availableZoomValues[currentZoomPos]);
			return true;
		}
		return false;
	}

	public boolean zoomOut() {
		if (documentViewer != null) {
			if (availableZoomValues[currentZoomPos] == minZoom) {
				return false;
			}
			currentZoomPos--;
			documentViewer.setFullZoom(availableZoomValues[currentZoomPos]);
			return true;
		}
		return false;
	}

	public boolean resetZoomView() {
		if (documentViewer != null) {
			currentZoomPos = 5;
			documentViewer.setFullZoom(availableZoomValues[currentZoomPos]);
			return true;
		}
		return false;
	}

	public boolean setCurrentZoom(float zoomValue) {
		if (documentViewer != null) {
			currentZoomPos = searchNearestPos(zoomValue);
			documentViewer.setFullZoom(availableZoomValues[currentZoomPos]);
		}
		return false;
	}

	public float getCurrentZoom() {
		return availableZoomValues[currentZoomPos];
	}

	public float getMaxZoom() {
		return minZoom;
	}

	public float getMinZoom() {
		return maxZoom;
	}
	
	private int searchNearestPos(float zoomValue){
		for (int i = 0; i < 5; i++) {
			if (zoomValue < availableZoomValues[i]+decreaseValue/2) {
				return i;
			}
			if (zoomValue <= availableZoomValues[i+1]) {
				return i+1;
			}
		}
		for (int i = 5; i < availableZoomValues.length-1; i++) {
			if (zoomValue < availableZoomValues[i]+increaseValue/2) {
				return i;
			}
			if (zoomValue <= availableZoomValues[i+1]) {
				return i+1;
			}
		}
		return currentZoomPos;
	}

	public float getBasicZoom() {
		return basicZoom;
	}

	public float[] getAvailableZoomValues() {
		return availableZoomValues;
	}
	
	private nsIMarkupDocumentViewer initMarkupViewer(XulRunnerEditor editor) {
		try {
			nsIDOMDocument document = editor.getDOMDocument();
			nsIDOMDocumentView documentView = queryInterface(document, nsIDOMDocumentView.class);
			nsIDOMAbstractView abstractView = documentView.getDefaultView();
			nsIInterfaceRequestor requestor = queryInterface(abstractView, nsIInterfaceRequestor.class);
			nsIWebNavigation navigation = (nsIWebNavigation) requestor
					.getInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
			nsIDocShell docShell = queryInterface(navigation, nsIDocShell.class);
			nsIContentViewer contentViewer = docShell.getContentViewer();
			nsIMarkupDocumentViewer markupDocumentViewer = queryInterface(contentViewer, nsIMarkupDocumentViewer.class);
			return markupDocumentViewer;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
}

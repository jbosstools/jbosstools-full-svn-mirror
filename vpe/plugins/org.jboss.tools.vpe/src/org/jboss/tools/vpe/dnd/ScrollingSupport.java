/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.dnd;

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIEmbeddingSiteWindow;
import org.mozilla.interfaces.nsIWebBrowser;

/**
 * @author yradtsevich
 *
 */
public class ScrollingSupport {
	private static final int SCROLLING_STEP = 10; 
	private static final int SCROLLING_BORDERS = 20;

	// XXX: the value of SCROLLBAR_HEIGHT and SCROLLBAR_WIDTH
	// should be set according to environment settings
	private static final int SCROLLBAR_HEIGHT = 16;
	private static final int SCROLLBAR_WIDTH = 16;

	private final XulRunnerEditor xulRunnerEditor;
	public ScrollingSupport(XulRunnerEditor xulRunnerEditor) {
		this.xulRunnerEditor = xulRunnerEditor;
	}

	public void scroll(nsIDOMMouseEvent mouseEvent) {
		final nsIWebBrowser webBrowser = xulRunnerEditor.getWebBrowser();

		Rectangle rect = getWindowBounds();

		final int mouseX = mouseEvent.getClientX();
		final int mouseY = mouseEvent.getClientY();
		int scrollX = 0;
		int scrollY = 0;
		if (rect.x + SCROLLING_BORDERS > mouseX) {
			scrollX-= SCROLLING_STEP;
		}
		if (rect.y + SCROLLING_BORDERS > mouseY) {
			scrollY-= SCROLLING_STEP;
		}
		if (rect.x + rect.width - SCROLLING_BORDERS - SCROLLBAR_WIDTH < mouseX) {
			scrollX+= SCROLLING_STEP;
		}
		if (rect.y + rect.height - SCROLLING_BORDERS - SCROLLBAR_HEIGHT < mouseY) {
			scrollY+= SCROLLING_STEP;
		}
		
		if (scrollX != 0 || scrollY != 0) {
			webBrowser.getContentDOMWindow().scrollBy(scrollX, scrollY);
		}
	}
	
	public Rectangle getWindowBounds() {
		nsIEmbeddingSiteWindow window = (nsIEmbeddingSiteWindow) 
			xulRunnerEditor
			.getWebBrowser()
			.getContainerWindow()
			.queryInterface(nsIEmbeddingSiteWindow.NS_IEMBEDDINGSITEWINDOW_IID);
		
		int[] xArray      = new int[1]; // Left hand corner of the outer area
		int[] yArray      = new int[1]; // Top corner of the outer area
		int[] widthArray  = new int[1]; // Width of the inner or outer area
		int[] heightArray = new int[1]; // Height of the inner or outer area
		
		window.getDimensions(nsIEmbeddingSiteWindow.DIM_FLAGS_SIZE_INNER,
				xArray, yArray, widthArray, heightArray);
		
		return new Rectangle(xArray[0], yArray[0], widthArray[0], heightArray[0]);
	}
}

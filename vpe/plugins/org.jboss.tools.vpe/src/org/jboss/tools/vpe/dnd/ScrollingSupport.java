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

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIEmbeddingSiteWindow;
import org.mozilla.interfaces.nsIWebBrowser;

/**
 * The class {@code ScrollingSupport} enables support of window 
 * scrolling in VPE during Drag and Drop sessions.
 * 
 * @author yradtsevich
 */
public class ScrollingSupport {
	/**
	 * The scrolling step. Every time when {@link #scroll(nsIDOMMouseEvent)} method
	 * is called, the window is scrolled by this value
	 */
	private static final int SCROLLING_STEP = 10;
	/**
	 * Size of scrolling borders, i.e. the thickness of the area
	 * where dragging will cause scrolling
	 */
	private static final int SCROLLING_BORDERS = 46;

	// TODO: the values of SCROLLBAR_HEIGHT and SCROLLBAR_WIDTH
	// should be set according to environment's settings
	private static final int SCROLLBAR_WIDTH = 0;
	private static final int SCROLLBAR_HEIGHT = 0;

	private final XulRunnerEditor xulRunnerEditor;
	
	/**
	 * 
	 * @param xulRunnerEditor object of current XULRunner editor 
	 */
	public ScrollingSupport(XulRunnerEditor xulRunnerEditor) {
		this.xulRunnerEditor = xulRunnerEditor;
	}

	/**
	 * Scrolls the window XULRunner if it is necessary.
	 * <P>
	 * If point of the {@code mouseEvent} lies inside the scrolling borders,
	 * it scrolls the window of XULRunner by {@link #SCROLLING_STEP} in
	 * appropriate direction.
	 *
	 * @param mouseEvent the mouse event
	 */
	public void scroll(nsIDOMMouseEvent mouseEvent) {
		final nsIWebBrowser webBrowser = xulRunnerEditor.getWebBrowser();

		final Rectangle rect = getWindowBounds();

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
	
	/**
	 * Returns {@link Rectangle} containing bounds of the {@link #xulRunnerEditor}
	 * 
	 * @return bounds of the {@link #xulRunnerEditor}
	 */
	private Rectangle getWindowBounds() {
		nsIEmbeddingSiteWindow window = 
			queryInterface(xulRunnerEditor.getWebBrowser().getContainerWindow(),
			nsIEmbeddingSiteWindow.class);
		
		int[] xArray      = new int[1]; // Left hand corner of the outer area
		int[] yArray      = new int[1]; // Top corner of the outer area
		int[] widthArray  = new int[1]; // Width of the inner or outer area
		int[] heightArray = new int[1]; // Height of the inner or outer area
		
		window.getDimensions(nsIEmbeddingSiteWindow.DIM_FLAGS_SIZE_INNER,
				xArray, yArray, widthArray, heightArray);
		
		return new Rectangle(xArray[0], yArray[0], widthArray[0], heightArray[0]);
	}
}

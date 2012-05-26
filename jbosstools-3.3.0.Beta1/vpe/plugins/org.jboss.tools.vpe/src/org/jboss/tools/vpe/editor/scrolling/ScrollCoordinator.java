/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.scrolling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.util.SourceDomUtil;
import org.jboss.tools.vpe.xulrunner.util.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDOMWindowInternal;
import org.mozilla.interfaces.nsIDOMWindowUtils;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.w3c.dom.Node;

/**
 * Base implementation of IScrollCoordinator
 * @author dmaliarevich
 */
public class ScrollCoordinator implements IScrollCoordinator {

	StructuredTextEditor sourceEditor = null;
	MozillaEditor visualEditor = null;
	VpeDomMapping domMapping = null;
	private List<Integer> resultPositions = new ArrayList<Integer>();
	private List<Integer> removeList = new ArrayList<Integer>();
	private List<Point> visualPoints = new ArrayList<Point>();
	
	public ScrollCoordinator(StructuredTextEditor sourceEditor,
			MozillaEditor visualEditor, VpeDomMapping domMapping) {
		super();
		this.sourceEditor = sourceEditor;
		this.visualEditor = visualEditor;
		this.domMapping = domMapping;
	}

	/**
	 * Compute an absolute pixel offset to scroll the xulrunner window to.
	 */
	@Override
	public int computeVisualPositionFromSource() {
		int posY = -1;
		if (sourceEditor != null) {
			ITextViewer textViewer = sourceEditor.getTextViewer();
			if (textViewer != null) {
				int topLine = textViewer.getTopIndex();
				/*
				 * Check 9 top lines. 
				 */
				for (int i = 0; i < 9; i++) {
					Node sourceNode = SourceDomUtil.getSourceNodeByEditorPosition(textViewer, topLine+i, 1);
					if (sourceNode != null) {
						nsIDOMElement visualElement = domMapping.getNearVisualElement(sourceNode);
						if (visualElement != null) {
							Point r = XulRunnerVpeUtils.getVisualNodeOffset(visualElement);
							resultPositions.add(r.y);
						}
					}
				}
				posY = findBetterPosition(resultPositions);
			}
		}
		return posY;
	}

	/**
	 * Compute top line index to scroll the source editor to.
	 */
	@Override
	public int computeSourcePositionFromVisual() {
		int line = -1;
		if ((sourceEditor != null) && (visualEditor != null)) {
			if (visualEditor.getXulRunnerEditor() != null) {
				nsIDOMWindow domWindow = visualEditor.getXulRunnerEditor().getWebBrowser().getContentDOMWindow();
				nsIInterfaceRequestor iInterfaceRequestor = (nsIInterfaceRequestor) 
						domWindow.queryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
				nsIDOMWindowInternal windowInternal = org.jboss.tools.vpe.xulrunner.util.XPCOM
						.queryInterface(domWindow, nsIDOMWindowInternal.class);
				nsIDOMWindowUtils windowUtils = (nsIDOMWindowUtils) 
						iInterfaceRequestor.getInterface(nsIDOMWindowUtils.NS_IDOMWINDOWUTILS_IID);
				/*
				 * Divide visual area into xLines*yLines=30 squares.  
				 * Get coordinates of their points of intersection. 
				 * Based on them -- source line will be searched.
				 */				
				int xLines = 6;
				int yLines = 5;
				for (int i = 1; i < xLines; i++) {
					for (int j = 1; j < yLines; j++) {
						visualPoints.add(new Point(
								windowInternal.getInnerWidth()*i/xLines, 
								windowInternal.getInnerHeight()*j/yLines));
					}
				}
				
				for (Point p : visualPoints) {
					nsIDOMElement elementFromPoint = windowUtils.elementFromPoint(p.x, p.y, true, false);
					if (elementFromPoint != null) {
						ElementImpl sourceElement = domMapping.getNearSourceElementImpl(elementFromPoint);
						if (sourceElement != null) {
							/*
							 * Transform offset to line number
							 */
							resultPositions.add(sourceEditor.getTextViewer().getTextWidget()
									.getLineAtOffset(sourceElement.getStartEndOffset()));
						}
					}
				}
				line = findBetterPosition(resultPositions);
			}
		}
		return line;
	}
	
	/**
	 * Finds better editor position.
	 * <p>
	 * List should have at least 2 values.
	 * 
	 * @param list the list with positions
	 * @return the best match
	 */
	private int findBetterPosition(List<Integer> list) {
		int pos = getBetterPositionFromList(list);
		/*
		 * Clear calculation lists
		 */
		visualPoints.clear();
		resultPositions.clear();
		removeList.clear();
		return pos;
	}
	
	/**
	 * List should have at least 2 values
	 * 
	 * @param list list with positions
	 * @return the best match
	 */
	private int getBetterPositionFromList(List<Integer> list) {
		int pos = -1;
		if (list.size() > 1) {
			/*
			 * Sort the list to get min and max values
			 */
			Collections.sort(list);
			removeList.add(list.get(0));
			removeList.add(list.get(list.size() - 1));
			/*
			 * Remove min and max values from result positions 
			 */
			list.removeAll(removeList);
			if (list.size() == 1) {
				/*
				 * Get only one available value
				 */
				pos = list.get(0);
			} else if (list.size() > 1) {
				/*
				 * Find the average
				 */
				int sum = 0;
				for (Integer position : list) {
					sum += position;
				}
				pos = sum/list.size();
			} else {
				/*
				 * Get max value
				 */
				pos = removeList.get(1);
			}
		}
		return pos;
	}
}
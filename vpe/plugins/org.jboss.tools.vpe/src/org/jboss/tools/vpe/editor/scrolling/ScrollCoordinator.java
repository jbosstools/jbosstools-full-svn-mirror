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
				Node n = SourceDomUtil.getSourceNodeByEditorPosition(textViewer, topLine, 1);
				if (n != null) {
					nsIDOMElement visualElement = domMapping.getNearVisualElement(n);
					if (visualElement != null) {
						Point r = XulRunnerVpeUtils.getVisualNodeOffset(visualElement);
						posY = r.y;
					}
				}
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
				visualPoints.add(new Point(windowInternal.getInnerWidth()/10, windowInternal.getInnerHeight()/10));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/5, windowInternal.getInnerHeight()/10));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/2, windowInternal.getInnerHeight()/10));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/10, windowInternal.getInnerHeight()/5));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/5, windowInternal.getInnerHeight()/5));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/2, windowInternal.getInnerHeight()/5));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/10, windowInternal.getInnerHeight()/2));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/5, windowInternal.getInnerHeight()/2));
				visualPoints.add(new Point(windowInternal.getInnerWidth()/2, windowInternal.getInnerHeight()/2));
				for (Point p : visualPoints) {
					nsIDOMElement elementFromPoint = windowUtils.elementFromPoint(p.x, p.y, true, false);
					if (elementFromPoint != null) {
						ElementImpl sourceElement = domMapping.getNearSourceElementImpl(elementFromPoint);
						/*
						 * Transform offset to line number
						 */
						resultPositions.add(sourceEditor.getTextViewer().getTextWidget().getLineAtOffset(sourceElement.getStartEndOffset()));
					}
				}
				/*
				 * Sort the list to get min and max values
				 */
				Collections.sort(resultPositions);
				removeList.add(resultPositions.get(0));
				removeList.add(resultPositions.get(resultPositions.size() - 1));
				/*
				 * Remove min and max values the result positions 
				 */
				resultPositions.removeAll(removeList);
				if (resultPositions.size() == 1) {
					/*
					 * Get only one available value
					 */
					line = resultPositions.get(0);
				} else if (resultPositions.size() > 1) {
					/*
					 * Find the average
					 */
					int sum = 0;
					for (Integer l : resultPositions) {
						sum += l;
					}
					line = sum/resultPositions.size();
				} else {
					/*
					 * Get max value
					 */
					line = removeList.get(1);
				}
			}
		}
		/*
		 * Clear the calculation lists
		 */
		visualPoints.clear();
		resultPositions.clear();
		removeList.clear();
		return line;
	}
}
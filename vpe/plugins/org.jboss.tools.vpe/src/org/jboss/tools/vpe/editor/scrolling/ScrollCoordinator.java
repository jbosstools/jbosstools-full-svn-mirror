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
				nsIDOMWindowUtils windowUtils = (nsIDOMWindowUtils) 
						iInterfaceRequestor.getInterface(nsIDOMWindowUtils.NS_IDOMWINDOWUTILS_IID);
				int x = 40;//windowInternal.getInnerWidth()/5;
				int y = 30;//windowInternal.getInnerHeight()/5;
				nsIDOMElement elementFromPoint = windowUtils.elementFromPoint(x, y, true, false);
				if (elementFromPoint != null) {
					ElementImpl sourceElement = domMapping.getNearSourceElementImpl(elementFromPoint);
					/*
					 * Transform offset to line number
					 */
					line = sourceEditor.getTextViewer().getTextWidget().getLineAtOffset(sourceElement.getStartEndOffset());
				}
			}
		}
		return line;
	}
}
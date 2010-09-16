/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner.editor;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.xulrunner.util.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;

/**
 * Shows a hint in a XulRunner document.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class XulRunnerHint {
	private Point position;
	private nsIDOMDocument document;
	private nsIDOMElement hintElement;
	private String hint;
	
	public XulRunnerHint(nsIDOMDocument document) {
		this.document = document;
	}
	
	/**
	 * Hint to show. If {@code null} is passed, then hide.
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public void redraw() {
		
		if (hintElement == null) {
			nsIDOMElement parent = XulRunnerVpeUtils.getRootElement(document);
			hintElement = XulRunnerVpeUtils.createAnonymousElement(document, 
					XulRunnerConstants.HTML_TAG_SPAN, parent,
					XulRunnerConstants.VPE_CLASS_NAME_MOZ_RESIZING_INFO, false);
		}
		
		nsIDOMNSHTMLElement hintHtmlElement = queryInterface(hintElement,
				nsIDOMNSHTMLElement.class);
		
		if (hint == null) {
			hintHtmlElement.setInnerHTML(""); //$NON-NLS-1$
			hintElement.setAttribute(XulRunnerConstants.HTML_ATTR_STYLE,
					XulRunnerConstants.HTML_VALUE_VISIBILITY_HIDDEN);
		} else {
			hintElement.setAttribute(XulRunnerConstants.HTML_ATTR_STYLE, ""); //$NON-NLS-1$
			hintHtmlElement.setInnerHTML(hint);			
			XulRunnerVpeUtils.setElementPosition(hintElement, position.x, position.y);
		}
	}

	public void dispose() {
		if (hintElement != null && hintElement.getParentNode() != null) {
			hintElement.getParentNode().removeChild(hintElement);
		}
		hintElement = null;
		document = null;
	}
}

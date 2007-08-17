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

import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;

/**
 * @author A. Yukhovich
 */
public class DOMElementUtils {
	
	/**
	 * @param domElement
	 * @return
	 */
	static public ElementPositionAndDimention getElementPositionAndDimention(nsIDOMElement domElement) {
		ElementPositionAndDimention elementPositionAndDimention = new ElementPositionAndDimention();
		
		nsIDOMNSHTMLElement nsElement = (nsIDOMNSHTMLElement) domElement.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
		
		if (nsElement != null) {
			elementPositionAndDimention.setWidth(nsElement.getOffsetWidth());
			elementPositionAndDimention.setHeight(nsElement.getOffsetHeight());
			elementPositionAndDimention.setTop(nsElement.getOffsetTop());
			elementPositionAndDimention.setLeft(nsElement.getOffsetLeft());
		}
		
		return elementPositionAndDimention;
	}
}

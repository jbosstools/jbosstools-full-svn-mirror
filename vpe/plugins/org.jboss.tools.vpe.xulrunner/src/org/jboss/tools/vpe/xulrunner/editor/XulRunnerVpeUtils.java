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

import org.eclipse.swt.graphics.Rectangle;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author A. Yukhovich
 */
public class XulRunnerVpeUtils {

	/**
	 * @param domElement
	 * @return
	 */
	static public Rectangle getElementBounds(nsIDOMElement domElement) {
		try {
			nsIDOMNSHTMLElement domNSHTMLElement = (nsIDOMNSHTMLElement) domElement.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
			int offsetLeft = domNSHTMLElement.getOffsetLeft();
			int offsetTop = domNSHTMLElement.getOffsetTop();
			int width = domNSHTMLElement.getOffsetWidth();
			int height = domNSHTMLElement.getOffsetHeight();
			
			while (true) {
				try {
					if (domNSHTMLElement.getOffsetParent() == null) {
						break;
					}

					domNSHTMLElement = (nsIDOMNSHTMLElement) domNSHTMLElement.getOffsetParent().queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
					offsetLeft += domNSHTMLElement.getOffsetLeft();
					offsetTop += domNSHTMLElement.getOffsetTop();
				} catch (XPCOMException ex) {
					break;
				}
			}
			return new Rectangle(offsetLeft, offsetTop, width, height);

		} catch (XPCOMException xpcomException) {
			return new Rectangle(0, 0, 0, 0);
		}
	}
}

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

package org.jboss.tools.vpe.xulrunner.util;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.xulrunner.BrowserPlugin;
import org.mozilla.interfaces.nsIBoxObject;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNSElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author dsakovich@exadel.com
 */
public class XulRunnerVpeUtils {

	private static int findPosX(nsIDOMNSHTMLElement boxObject) {
		int curleft = 0;
		
		if (boxObject.getOffsetParent() != null) {
			while (true) {
				curleft += boxObject.getOffsetLeft();
				if ( boxObject.getOffsetParent() == null)
					return curleft;
				boxObject = queryInterface(boxObject.getOffsetParent(), nsIDOMNSHTMLElement.class);
			}
		} else {
			curleft += boxObject.getOffsetLeft();
		}
		return curleft;
	}

	private static int findPosY(nsIDOMNSHTMLElement boxObject) {
		int curleft = 0;
		
		if (boxObject.getOffsetParent() != null) {
			while (true) {
				curleft += boxObject.getOffsetTop();
				if ( boxObject.getOffsetParent() == null)
					return curleft;
				boxObject = queryInterface(boxObject.getOffsetParent(), nsIDOMNSHTMLElement.class);
			}
		} else {
			curleft += boxObject.getOffsetTop();
		}
		return curleft;
	}

	/**
	 * @param domElement
	 * @return Rectangle
	 */
	static public Rectangle getElementBounds(nsIDOMNode domNode) {
		try {
			nsIDOMElement domElement = queryInterface(domNode, nsIDOMElement.class);
		
			nsIDOMNSElement htmlElement = queryInterface(domNode, nsIDOMNSElement.class);
			nsIDOMNSHTMLElement domNSHTMLElement = queryInterface(domNode, nsIDOMNSHTMLElement.class);
			nsIDOMDocument document = domElement.getOwnerDocument();

			nsIDOMNSDocument nsDocument = queryInterface(document, nsIDOMNSDocument.class);
			nsIBoxObject boxObject = nsDocument.getBoxObjectFor(domElement);
			Rectangle rectangle = new Rectangle(findPosX(domNSHTMLElement),
														 findPosY(domNSHTMLElement),
														 boxObject.getWidth(),
														 boxObject.getHeight());

			if (BrowserPlugin.PRINT_ELEMENT_BOUNDS) {
				System.out.println("getElementBounds(IDOMNode) returns "
						+ rectangle);
				System.out
						.println("nsIDOMNSHTMLElement getOffsetLeft,getOffsetTop,getOffsetWidth,getOffsetHeight"
								+ new Rectangle(domNSHTMLElement.getOffsetLeft(),
										domNSHTMLElement.getOffsetTop(), domNSHTMLElement
												.getOffsetWidth(), domNSHTMLElement
												.getOffsetHeight()));
				System.out
						.println("nsIDOMNSElement getClientLeft,getClientTop,getClientWidth,getClientHeight"
								+ new Rectangle(htmlElement.getClientLeft(),
										htmlElement.getClientTop(), htmlElement
												.getClientWidth(), htmlElement
												.getClientHeight()));
				System.out.println("nsIBoxObject getX,getY,getWidth,getHeight"
						+ new Rectangle(boxObject.getX(), boxObject.getY(),
								boxObject.getWidth(), boxObject.getHeight()));

			}
			return rectangle;

		} catch (XPCOMException xpcomException) {
			return new Rectangle(0, 0, 0, 0);
		}
	}
}

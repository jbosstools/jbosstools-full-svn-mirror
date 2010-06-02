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
import org.mozilla.interfaces.nsIAccessNode;
import org.mozilla.interfaces.nsIAccessible;
import org.mozilla.interfaces.nsIAccessibleCoordinateType;
import org.mozilla.interfaces.nsIAccessibleRetrieval;
import org.mozilla.interfaces.nsIAccessibleText;
import org.mozilla.interfaces.nsIBoxObject;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNSElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.mozilla.xpcom.Mozilla;
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
	
	/**
	 * Returns the bounds of the selected text in given
	 * {@code selectionContainer}.
	 */
	public static Rectangle getTextSelectionBounds(nsIDOMText slectionContainer) {
		nsIAccessibleText accessibleTextAncestor = getAccessibleTextAncestor(slectionContainer);
		if (accessibleTextAncestor == null) {
			// cannot get selection bounds
			return null;
		}
		if (accessibleTextAncestor.getSelectionCount() == 0) {
			// no text selected
			return null;
		}
		
		int[] startOffset = new int[1];
		int[] endOffset = new int[1];
		accessibleTextAncestor.getSelectionBounds(0, startOffset, endOffset);
		
		int[] x = new int[1];
		int[] y = new int[1];
		int[] width = new int[1];
		int[] height = new int[1];
		accessibleTextAncestor.getRangeExtents(startOffset[0], endOffset[0],
				x, y, width, height, nsIAccessibleCoordinateType.COORDTYPE_PARENT_RELATIVE);
		
		Rectangle ancestorBounds = getElementBounds(
				queryInterface(accessibleTextAncestor, nsIAccessNode.class)
						.getDOMNode());
		
		return new Rectangle(ancestorBounds.x + x[0], ancestorBounds.y + y[0],
				width[0], height[0]);
	}
	
	/**
	 * Returns the nearest ancestor of given {@code node} which supports
	 * {@link nsIAccessibleText}.
	 */
	private static nsIAccessibleText getAccessibleTextAncestor(nsIDOMNode node) {
		nsIAccessibleText accessibleTextAncestor = null;
		nsIDOMNode ancestor = node;
		while (accessibleTextAncestor == null && ancestor != null) {
			ancestor = ancestor.getParentNode();
			try {
				nsIAccessible accessibleAncestor = getAccessible(ancestor);
				accessibleTextAncestor = queryInterface(accessibleAncestor, nsIAccessibleText.class);
			} catch (XPCOMException e) {
				// it's OK, accessibleTextAncestor still = null
			}
		}
		
		return accessibleTextAncestor;
	}

	/**
	 * Returns {@link nsIAccessible} interface for given {@code node}.
	 */
	private static nsIAccessible getAccessible(nsIDOMNode node) {
		return ((nsIAccessibleRetrieval)
				Mozilla.getInstance().getServiceManager().getServiceByContractID(
						XPCOM.NS_ACCESSIBILITYSERVICE_CONTRACTID,
						nsIAccessibleRetrieval.NS_IACCESSIBLERETRIEVAL_IID))
				.getAccessibleFor(node);
	}
}

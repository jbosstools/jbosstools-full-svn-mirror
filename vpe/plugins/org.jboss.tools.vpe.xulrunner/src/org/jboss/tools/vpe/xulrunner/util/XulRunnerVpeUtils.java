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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.xulrunner.BrowserPlugin;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerConstants;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMClientRect;
import org.mozilla.interfaces.nsIDOMClientRectList;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMElementCSSInlineStyle;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMHTMLElement;
import org.mozilla.interfaces.nsIDOMNSElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author dsakovich@exadel.com
 */
public class XulRunnerVpeUtils {
	/**
	 * Get root element
	 * 
	 * @return root element
	 */
	public static nsIDOMElement getRootElement(nsIDOMDocument domDocument) {
		
		nsIDOMElement bodyElement = null;
		
		nsIDOMHTMLDocument htmlDocument = queryInterface(domDocument, nsIDOMHTMLDocument.class);
		
		if ( htmlDocument != null ) {
			 nsIDOMHTMLElement htmlBody = htmlDocument.getBody();
			 
			 if ( htmlBody != null ) {
				 bodyElement = queryInterface(htmlBody, nsIDOMElement.class);
			 } // if
		} // if
		
		return bodyElement;
	}
	
	public static void setElementPosition(nsIDOMElement domElement, int left,int top)	{
		setStylePropertyPixels(domElement,XulRunnerConstants.HTML_ATTR_LEFT, left);
		setStylePropertyPixels(domElement,XulRunnerConstants.HTML_ATTR_TOP, top);		
	}
	
	public static void setElementSize(nsIDOMElement domElement, int width,int height) {
		setStylePropertyPixels(domElement, XulRunnerConstants.HTML_ATTR_WIDTH, width);
		setStylePropertyPixels(domElement, XulRunnerConstants.HTML_ATTR_HEIGHT, height);
	}
	
	public static void setElementBounds(nsIDOMElement domElement, Rectangle bounds) {
		setElementPosition(domElement, bounds.x, bounds.y);
		setElementSize(domElement, bounds.width, bounds.height);
	}

	/**
	 * 
	 * @param aElement
	 * @param aProperty
	 * @param aValue
	 */
	public static void setStylePropertyPixels(nsIDOMElement aElement, String aProperty, int aValue) {
		setStyle(aElement, aProperty, aValue + "px"); //$NON-NLS-1$
	}
	
	/**
	 * Set style for nsIDOMElement
	 * @param domElement 
	 * @param cssPropertyName
	 * @param cssPropertyValue
	 */
	public static void setStyle(nsIDOMElement domElement, String cssPropertyName, String cssPropertyValue) {
		nsIDOMElementCSSInlineStyle inlineStyles = queryInterface(domElement, nsIDOMElementCSSInlineStyle.class);
		
	    if ( inlineStyles == null) {
	    	return;
	    }

	    nsIDOMCSSStyleDeclaration cssDecl = inlineStyles.getStyle();

	    if ( cssDecl == null) {
	    	return;
	    }

	    if (cssPropertyValue.length() == 0 ) {
			// an empty value means we have to remove the property
	    	cssDecl.removeProperty(cssPropertyName);
	    }  else {
			// let's recreate the declaration as it was
	    	String priority = cssDecl.getPropertyPriority(cssPropertyName);
	    	cssDecl.setProperty(cssPropertyName, cssPropertyValue, priority);
	    }
	}

	/**
	 * create a anonymous dom-element
	 * 
	 * @param aTag
	 *            a tag of dom element
	 * @param aParentNode
	 * @param aAnonClass
	 * @param isCreatedHidden
	 * @return
	 */
	public static nsIDOMElement createAnonymousElement(nsIDOMDocument domDocument,
			String aTag, nsIDOMNode aParentNode, String aAnonClass, boolean isCreatedHidden) {
		nsIDOMElement element = null;
	
		element = domDocument.createElement(aTag);
		
		// add the "hidden" class if needed
		if (isCreatedHidden) {
			element.setAttribute(XulRunnerConstants.HTML_ATTR_CLASS, XulRunnerConstants.HTML_VALUE_HIDDEN);
		}
		
		// add an _moz_anonclass attribute if needed
		if ( aAnonClass.length() != 0  ) {
			element.setAttribute(XulRunnerConstants.STRING_MOZ_ANONCLASS, aAnonClass);
		}		
		
		aParentNode.appendChild(element);
		
		return element;
	}
	
	public static Point getVisualNodeOffset(nsIDOMNode node) {
		Point p = new Point(-1, -1);
		try {
			p = getDocumentPos(queryInterface(node, nsIDOMNSHTMLElement.class));
		} catch (XPCOMException xpcomException) {
			// do nothing
		}
		return p;
	}
	
	private static Point getDocumentPos(nsIDOMNSHTMLElement boxElement) {
		int x = 0;
		int y = 0;
		nsIDOMNSHTMLElement curBoxElement = boxElement;
		while (curBoxElement != null) {
			x += curBoxElement.getOffsetLeft();
			y += curBoxElement.getOffsetTop();
			curBoxElement = getOffsetParent(curBoxElement);
		}
		return new Point(x, y);
	}

	private static nsIDOMNSHTMLElement getOffsetParent(
			nsIDOMNSHTMLElement boxElement) {
		nsIDOMElement offsetParent = boxElement.getOffsetParent();
		if (offsetParent != null) {
			return queryInterface(offsetParent, nsIDOMNSHTMLElement.class);
		} else {
			return null;
		}
	}
	
	private static Point getClientSize(nsIDOMNSElement element) {
		int width = 0;
		int height = 0;
		nsIDOMClientRectList clientRects = element.getClientRects();
		if (clientRects.getLength() > 0) {
			nsIDOMClientRect firstRect = clientRects.item(0);
			width = (int) firstRect.getWidth();
			height = (int) firstRect.getHeight();
		}
		
		return new Point(width, height);
	}

	/**
	 * @param domElement
	 * @return Rectangle
	 */
	static public Rectangle getElementBounds(nsIDOMNode node) {
		try {
			nsIDOMNSElement element = queryInterface(node, nsIDOMNSElement.class);
			nsIDOMNSHTMLElement htmlElement = queryInterface(node, nsIDOMNSHTMLElement.class);
			Point documentPos = getDocumentPos(htmlElement);
			Point clientSize = getClientSize(element);
			Rectangle rectangle = new Rectangle(documentPos.x, documentPos.y,
												clientSize.x, clientSize.y);

			if (BrowserPlugin.PRINT_ELEMENT_BOUNDS) {
				System.out.println("getElementBounds(IDOMNode) returns "
						+ rectangle);
				System.out
						.println("nsIDOMNSHTMLElement getOffsetLeft,getOffsetTop,getOffsetWidth,getOffsetHeight"
								+ new Rectangle(htmlElement.getOffsetLeft(),
										htmlElement.getOffsetTop(), htmlElement
												.getOffsetWidth(), htmlElement
												.getOffsetHeight()));
				System.out
						.println("nsIDOMNSElement getClientLeft,getClientTop,getClientWidth,getClientHeight"
								+ new Rectangle(element.getClientLeft(),
										element.getClientTop(), element
												.getClientWidth(), element
												.getClientHeight()));
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
	public static Rectangle getTextSelectionBounds(nsIDOMText selectionContainer) {
//		nsIAccessibleText accessibleTextAncestor = getAccessibleTextAncestor(selectionContainer);
//		if (accessibleTextAncestor == null) {
//			// cannot get selection bounds
//			return null;
//		}
//		if (accessibleTextAncestor.getSelectionCount() == 0) {
//			// no text selected
//			return null;
//		}
//		
//		int[] startOffset = new int[1];
//		int[] endOffset = new int[1];
//		accessibleTextAncestor.getSelectionBounds(0, startOffset, endOffset);
//		
//		int[] x = new int[1];
//		int[] y = new int[1];
//		int[] width = new int[1];
//		int[] height = new int[1];
//		accessibleTextAncestor.getRangeExtents(startOffset[0], endOffset[0],
//				x, y, width, height, nsIAccessibleCoordinateType.COORDTYPE_PARENT_RELATIVE);
//
//		nsIAccessible ancestorAccessibleParent
//				= queryInterface(accessibleTextAncestor, nsIAccessible.class).getParent();
//		nsIDOMNode ancestorParent
//				= queryInterface(ancestorAccessibleParent, nsIAccessNode.class).getDOMNode();
//
//		Rectangle ancestorParentBounds;
//		if (ancestorParent != null) {
//			ancestorParentBounds = getElementBounds(ancestorParent);
//		} else {
//			ancestorParentBounds = new Rectangle(0, 0, 0, 0);
//		}
//		
		return new Rectangle(0,0,0,0);
	}
	
//	/**
//	 * Returns the nearest ancestor of given {@code node} which supports
//	 * {@link nsIAccessibleText}.
//	 */
//	private static nsIAccessibleText getAccessibleTextAncestor(nsIDOMNode node) {
//		nsIAccessibleText accessibleTextAncestor = null;
//		nsIDOMNode ancestor = node;
//		while (accessibleTextAncestor == null && ancestor != null) {
//			ancestor = ancestor.getParentNode();
//			try {
//				nsIAccessible accessibleAncestor = getAccessible(ancestor);
//				accessibleTextAncestor = queryInterface(accessibleAncestor, nsIAccessibleText.class);
//			} catch (XPCOMException e) {
//				// it's OK, accessibleTextAncestor still = null
//			}
//		}
//		
//		return accessibleTextAncestor;
//	}
//
//	/**
//	 * Returns {@link nsIAccessible} interface for given {@code node}.
//	 */
//	private static nsIAccessible getAccessible(nsIDOMNode node) {
//		return ((nsIAccessibleRetrieval)
//				Mozilla.getInstance().getServiceManager().getServiceByContractID(
//						XPCOM.NS_ACCESSIBILITYSERVICE_CONTRACTID,
//						nsIAccessibleRetrieval.NS_IACCESSIBLERETRIEVAL_IID))
//				.getAccessibleFor(node);
//	}
}

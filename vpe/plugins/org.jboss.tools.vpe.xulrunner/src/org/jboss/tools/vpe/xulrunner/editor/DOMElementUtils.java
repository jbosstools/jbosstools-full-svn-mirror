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

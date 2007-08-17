/**
 * 
 */
package org.jboss.tools.vpe.editor;

import org.eclipse.swt.graphics.Rectangle;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * @author Max Areshkau
 *
 *Class which response for drag and drop functionality
 */
public class VpeDnD {

	public Rectangle getBounds(nsIDOMNode visualNode) {
		
		nsIDOMNSHTMLElement domNSHTMLElement = (nsIDOMNSHTMLElement) visualNode.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
		return new Rectangle(domNSHTMLElement.getOffsetLeft(), domNSHTMLElement.getOffsetTop(),domNSHTMLElement.getOffsetWidth(),domNSHTMLElement.getOffsetHeight());
	}

}

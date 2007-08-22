/******************************************************************************* 

* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/ 
package org.jboss.tools.vpe.editor;

import org.eclipse.swt.graphics.Rectangle;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author Max Areshkau
 *
 *Class which response for drag and drop functionality
 */
public class VpeDnD {

	public Rectangle getBounds(nsIDOMNode visualNode) {
		try {
			
		nsIDOMNSHTMLElement domNSHTMLElement = (nsIDOMNSHTMLElement) visualNode.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
		return new Rectangle(domNSHTMLElement.getOffsetLeft(), domNSHTMLElement.getOffsetTop(),domNSHTMLElement.getOffsetWidth(),domNSHTMLElement.getOffsetHeight());
		
		} catch(XPCOMException xpcomException) {
			
			//TODO Max Areshkau 
			//If node not not implement nsIDOMNSHTMLElement, may be check best take a parent node 
			return new Rectangle(0, 0, 0,0);
		}
		}

}

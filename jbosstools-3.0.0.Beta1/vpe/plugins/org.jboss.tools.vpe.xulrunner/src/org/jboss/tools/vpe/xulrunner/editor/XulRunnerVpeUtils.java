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
import org.mozilla.interfaces.nsIBoxObject;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author dsakovich@exadel.com
 */
public class XulRunnerVpeUtils {

    /**
     * @param domElement
     * @return Rectangle
     */
    static public Rectangle getElementBounds(nsIDOMNode domNode) {
	try {
	    nsIDOMElement domElement = (nsIDOMElement) domNode
		    .queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	    nsIDOMDocument document = domElement.getOwnerDocument();
	    nsIDOMNSDocument nsDocument = (nsIDOMNSDocument) document
		    .queryInterface(nsIDOMNSDocument.NS_IDOMNSDOCUMENT_IID);
	    nsIBoxObject boxObject = nsDocument.getBoxObjectFor(domElement);
	    return new Rectangle(boxObject.getX(), boxObject.getY(), boxObject
		    .getWidth(), boxObject.getHeight());

	} catch (XPCOMException xpcomException) {
	    return new Rectangle(0, 0, 0, 0);
	}
    }
}

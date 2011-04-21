/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.html.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author dmaliarevich
 *
 */
public class HtmlImgTemplate extends VpeAbstractTemplate {

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		boolean showUnresolvedImage = false;
		boolean jsfc = false;
		 
		nsIDOMElement img = visualDocument.createElement(HTML.TAG_IMG);
		/*
		 * Copy all attributes from source node to "img" tag except for "src", 
		 * store "alt" attribute state, check if facelets "jsfc" attribute presents.
		 */
		for (int i = 0; i < sourceNode.getAttributes().getLength(); i++) {
			String name = sourceNode.getAttributes().item(i).getNodeName();
			String value = sourceNode.getAttributes().item(i).getNodeValue();
			if (HTML.ATTR_SRC.equalsIgnoreCase(name)) {
				continue;
			}
			if (HTML.ATTR_JSFC.equalsIgnoreCase(name)) {
				jsfc = true;
				continue;
			}
			try{
				img.setAttribute(name, value);
			}catch(XPCOMException ex ) {
				//just ignore it
			}
		}
		

			Element  image = (Element) sourceNode;
		
			if (!jsfc && image.hasAttribute(HTML.ATTR_SRC)) {
				String src = VpeStyleUtil.addFullPathToImgSrc(image.getAttribute(HTML.ATTR_SRC), pageContext,
						showUnresolvedImage);
				try {
					img.setAttribute(HTML.ATTR_SRC, src);
				} catch (XPCOMException ex) {
					// just ignore it
				}
			} else if ((jsfc) && image.hasAttribute(HTML.ATTR_VALUE)) {
				/*
				 * in this case the tag is a facelets's tag
				 */
				String value = VpeStyleUtil.addFullPathToImgSrc(image.getAttribute(HTML.ATTR_VALUE), pageContext,
						showUnresolvedImage);
				try {
					img.setAttribute(HTML.ATTR_SRC, value);
				} catch (XPCOMException ex) {
					// just ignore it
				}
		}
		
		
		VpeCreationData creationData = new VpeCreationData(img);
		
		return creationData;
	}

}

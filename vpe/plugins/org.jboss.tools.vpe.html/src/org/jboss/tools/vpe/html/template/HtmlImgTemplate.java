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

import org.jboss.tools.jst.jsp.jspeditor.SourceEditorPageContext;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Node;

/**
 * @author dmaliarevich
 *
 */
public class HtmlImgTemplate extends VpeAbstractTemplate {

	/*
	 * Facelets "jsfc" attribute
	 */
	private static final String JSFC = "jsfc";
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	@Override
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
			if (JSFC.equalsIgnoreCase(name)) {
				jsfc = true;
				continue;
			}
			
			if (HTML.ATTR_ALT.equalsIgnoreCase(name)) {
				if ((null == value)
						|| ((null != value) && (value.trim()
								.equalsIgnoreCase(VpeStyleUtil.EMPTY_STRING)))) {
					showUnresolvedImage = true;
					continue;
				}
			}
			try{
				img.setAttribute(name, value);
			}catch(XPCOMException ex ) {
				//just ignore it
			}
		}
		
		/*
		 * Add "src" attribute in consideration of "alt" and "jsfc" attributes
		 */
		for (int i = 0; i < sourceNode.getAttributes().getLength(); i++) {
			String name = sourceNode.getAttributes().item(i).getNodeName();
			String value = sourceNode.getAttributes().item(i).getNodeValue();
			if (!jsfc && (HTML.ATTR_SRC.equalsIgnoreCase(name))) {
				value = VpeStyleUtil.addFullPathToImgSrc(value, pageContext,
						showUnresolvedImage);
				try {
					img.setAttribute(HTML.ATTR_SRC, value);
				} catch (XPCOMException ex) {
					// just ignore it
				}
			} else if ((jsfc) && (HTML.ATTR_VALUE.equalsIgnoreCase(name))) {
				/*
				 * in this case the tag is a facelets's tag
				 */
				value = VpeStyleUtil.addFullPathToImgSrc(value, pageContext,
						showUnresolvedImage);
				try {
					img.setAttribute(HTML.ATTR_SRC, value);
				} catch (XPCOMException ex) {
					// just ignore it
				}
			}
		}
		
		
		VpeCreationData creationData = new VpeCreationData(img);
		
		return creationData;
	}

}

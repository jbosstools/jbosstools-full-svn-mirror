/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jsf.vpe.facelets.template;

import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeCompositionTemplate extends VpeDefineContainerTemplate {
	
	protected VpeCreationData createStub(String fileName, Node sourceElement, nsIDOMDocument visualDocument) {
		nsIDOMElement container = visualDocument.createElement(HTML.TAG_DIV);
		container.setAttribute("style", "border: 1px dashed #2A7F00");
		VpeVisualDomBuilder.markIncludeElement(container);

		nsIDOMElement title = visualDocument.createElement(HTML.TAG_DIV);
		nsIDOMElement tag = visualDocument.createElement(HTML.TAG_SPAN);
		tag.setAttribute("class", "__any__tag__caption");
		tag.appendChild(visualDocument.createTextNode(sourceElement.getNodeName()));
		title.appendChild(tag);
		if (fileName != null) {
			title.appendChild(visualDocument.createTextNode(fileName));
		}
		container.appendChild(title);
		
		return new VpeCreationData(container);
	}
	
	public boolean isRecreateAtAttrChange(VpePageContext pageContext, Element sourceElement, Document visualDocument, Node visualNode, Object data, String name, String value) {
		return true;
	}
}

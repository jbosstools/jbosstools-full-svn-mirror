/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.docbook.template;

import org.jboss.tools.vpe.docbook.template.util.Docbook;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract Class for elements with generated output
 * 
 * @author Denis Vinnichek (dvinnichek)
 */
public abstract class ElementWithGeneratedOutputTemplate extends
		VpeAbstractTemplate {

	private static final String BACKGROUND_COLOR = "rgb(236, 243, 255)"; //$NON-NLS-1$
	private static final String BORDER = "1px solid black"; //$NON-NLS-1$

	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		Element sourceElement = (Element) sourceNode;
		nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		VisualDomUtil.copyAttributes(sourceNode, div);

		nsIDOMCSSStyleDeclaration style = VpeStyleUtil.getStyle(div);
		style.setProperty(HTML.STYLE_PARAMETER_BACKGROUND_COLOR,
				getBackgroundColor(), HTML.STYLE_PRIORITY_DEFAULT);
		style.setProperty(HTML.STYLE_PARAMETER_BORDER, getBorder(),
				HTML.STYLE_PRIORITY_DEFAULT);

		NodeList titleElements = sourceElement
				.getElementsByTagName(Docbook.ELEMENT_TITLE);
		if (titleElements.getLength() == 0) {
			nsIDOMElement h1 = visualDocument.createElement(HTML.TAG_H1);
			h1.appendChild(visualDocument.createTextNode(getGeneratedText()));
			div.appendChild(h1);
		}

		return new VpeCreationData(div);
	}

	protected abstract String getGeneratedText();

	protected String getBackgroundColor() {
		return BACKGROUND_COLOR;
	}

	protected String getBorder() {
		return BORDER;
	}
}

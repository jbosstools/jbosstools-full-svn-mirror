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
package org.jboss.tools.vpe.editor.template;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeStyleCreator extends VpeAbstractCreator {

	public VpeStyleCreator(Element element, VpeDependencyMap dependencyMap,
			boolean caseSensitive) {
	}

	@Override
	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Map visualNodeMap) {

		Node textNode = sourceNode.getFirstChild();
		String text = null;
		if (textNode != null) {
			text = textNode.getNodeValue();
			/*
			 * https://issues.jboss.org/browse/JBIDE-5861
			 * Remove CSS comments first:
			 */
			text = VpeStyleUtil.removeAllCssComments(text);
			List<String> imports = VpeStyleUtil.findCssImportConstruction(text, pageContext);
			VpeVisualDomBuilder vvdb = pageContext.getVisualBuilder();
			if (!imports.isEmpty()) {
				for (String key : imports) {
					/*
					 * Add inline <style> element for each found css @import.
					 */
					vvdb.addLinkNodeToHead(key, "css_import_construction", false); //$NON-NLS-1$
				}
				/*
				 * Replace @import constructions that've been added.
				 */
				text = VpeStyleUtil.removeAllCssImportConstructions(text);
			}
			text = VpeStyleUtil.addFullPathIntoURLValue(text, pageContext);
		}
		nsIDOMNode newStyle = pageContext.getVisualBuilder()
				.addStyleNodeToHead(text);
		visualNodeMap.put(this, newStyle);
		return null;
	}

	@Override
	public void removeElement(VpePageContext pageContext,
			Element sourceElement, Map visualNodeMap) {
		nsIDOMNode styleNode = (nsIDOMNode) visualNodeMap.get(this);
		if (styleNode != null) {
			pageContext.getVisualBuilder().removeStyleNodeFromHead(styleNode);
			visualNodeMap.remove(this);
		}
	}

	@Override
	public void refreshElement(VpePageContext pageContext,
			Element sourceElement, Map visualNodeMap) {
		nsIDOMNode oldStyleNode = (nsIDOMNode) visualNodeMap.get(this);

		Node textNode = sourceElement.getFirstChild();
		String text = null;
		if (textNode != null) {
			text = textNode.getNodeValue();
		}
		nsIDOMNode newStyleNode;
		if (oldStyleNode == null) {
			newStyleNode = pageContext.getVisualBuilder().
					addStyleNodeToHead(text);
			visualNodeMap.put(this, newStyleNode);
		} else {
			newStyleNode = pageContext.getVisualBuilder()
					.replaceStyleNodeToHead(oldStyleNode, text);
			if (visualNodeMap.containsKey(this))
				visualNodeMap.remove(this);
			visualNodeMap.put(this, newStyleNode);
		}
	}
}
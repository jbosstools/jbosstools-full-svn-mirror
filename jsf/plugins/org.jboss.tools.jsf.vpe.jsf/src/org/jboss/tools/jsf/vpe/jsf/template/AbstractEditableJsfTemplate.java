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
package org.jboss.tools.jsf.vpe.jsf.template;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.IEditableTemplate;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * general class for jsf templates.
 * 
 * @author Sergey Dzmitrovich
 */
public abstract class AbstractEditableJsfTemplate extends VpeAbstractTemplate implements IEditableTemplate {

	/**
	 * Gets the output attribute node.
	 * 
	 * @param element the element
	 * 
	 * @return the output attribute node
	 */
	public Attr getOutputAttributeNode(Element element) {
       return null;
    }

    // general jsf attributes
	/**
	 * Contains JSF attributes and appropriate HTML attributes 
	 * content of that does not have to be modified in templates.
	 */
    static final private Map<String, String> attributes = new HashMap<String, String>();

	static {
		attributes.put("style", HTML.ATTR_STYLE); //$NON-NLS-1$
		attributes.put("styleClass", HTML.ATTR_CLASS); //$NON-NLS-1$
	}

	/**
	 * Renames and copies most general JSF attributes from the
	 * {@code sourceElement} to the {@code visualElement}.
	 * 
	 * @param sourceElement the source element
	 * @param visualElement the visual element
	 * @see AbstractEditableJsfTemplate#attributes attributes
	 */
	protected void copyGeneralJsfAttributes(Element sourceElement,
			nsIDOMElement visualElement) {
		
		Set<Map.Entry<String, String>> jsfAttrEntries = attributes.entrySet();
		
		for (Map.Entry<String, String> attrEntry : jsfAttrEntries) {
			copyAttribute(visualElement, sourceElement, attrEntry.getKey(),
					attrEntry.getValue());
		}

	}

	/**
	 * copy attribute.
	 * 
	 * @param sourceElement the source element
	 * @param targetAtttributeName the target atttribute name
	 * @param sourceAttributeName the source attribute name
	 * @param visualElement the visual element
	 */
	protected void copyAttribute(nsIDOMElement visualElement,
			Element sourceElement, String sourceAttributeName,
			String targetAtttributeName) {

		if (sourceElement.hasAttribute(sourceAttributeName))
			visualElement.setAttribute(targetAtttributeName, sourceElement
					.getAttribute(sourceAttributeName));

	}
}

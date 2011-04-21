/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * @author mareshkau
 *
 */
public class VpeNodesManagingUtil {
	/**
	 * name of "view" tag
	 */
	private static final String VIEW_TAGNAME = "view"; //$NON-NLS-1$

	/**
	 * name of "locale" attribute
	 */
	private static final String LOCALE_ATTRNAME = "locale"; //$NON-NLS-1$
	/**
	 * 
	 * @param domMapping
	 * @param node
	 * @return
	 */
	public static VpeNodeMapping getNodeMapping(VpeDomMapping domMapping,
			nsIDOMNode node) {

		return domMapping.getNearNodeMappingAtVisualNode(node);

	}
	
	/**
	 * 
	 * @param domMapping
	 * @param node
	 * @return
	 */
	public static VpeNodeMapping getNodeMapping(VpeDomMapping domMapping,
			Node node) {

		return domMapping.getNearNodeMappingAtSourceNode(node);

	}
	
	/**
	 * 
	 * @param pageContext
	 * @param sourceElement
	 * @return
	 */
	public static String getPageLocale(VpePageContext pageContext,
			Node sourceNode) {

		while (sourceNode != null) {

			if (VIEW_TAGNAME.equals(sourceNode.getLocalName())) {
				break;
			}
			sourceNode = sourceNode.getParentNode();
		}

		if ((sourceNode == null) || !(sourceNode instanceof Element)
				|| !(((Element) sourceNode).hasAttribute(LOCALE_ATTRNAME)))
			return null;

		String locale = ((Element) sourceNode).getAttribute(LOCALE_ATTRNAME);

		return locale;

	}
	
	/**
	 * 
	 * @param pageContext
	 * @param startPosition
	 * @param endPosition
	 * @return
	 */
	public static String getSourceText(VpePageContext pageContext,
			int startPosition, int endPosition) {

		return pageContext.getSourceBuilder().getStructuredTextViewer()
				.getTextWidget().getText(startPosition, endPosition);
	}
}

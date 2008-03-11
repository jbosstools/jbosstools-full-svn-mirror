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
package org.jboss.tools.vpe.editor.util;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Node;

public class TemplateManagingUtil {

	/**
	 * get template of selected element
	 * 
	 * @param pageContext
	 * @return
	 */
	public static VpeTemplate getTemplateByVisualSelection(
			VpePageContext pageContext) {
		// get element mapping
		VpeElementMapping elementMapping = pageContext.getDomMapping()
				.getNearElementMapping(getVisualSelectedNode(pageContext));

		if (elementMapping != null)
			return elementMapping.getTemplate();

		// next code is necessary for get template some jsf elements (which have
		// escape="false" attribute). It's necessary for the current
		// implementation of escape="false" attribute's process.
		// When or if the implementation of escape="false" attribute's process
		// will changed, you will must review next code
		VpeNodeMapping nodeMapping = pageContext.getDomMapping()
				.getNearNodeMapping(getVisualSelectedNode(pageContext));

		if (nodeMapping != null) {

			// get node. This node is not ascribe (may be) to DOM model of page,
			// because "value" attribute is parsed (if escape ="false")
			// separately and is built in vpe. But it has correct offset
			// information
			IDOMNode mappingNode = (IDOMNode) nodeMapping.getSourceNode();

			// get source node by ofsset
			Node sourceNode = getSourceNodeByPosition(pageContext, mappingNode
					.getStartOffset());
			// find elementMapping by source node
			if (sourceNode != null) {
				VpeElementMapping mapping = pageContext.getDomMapping()
						.getNearElementMapping(sourceNode);
				if (mapping != null)
					return mapping.getTemplate();
			}

		}

		return null;
	}

	/**
	 * 
	 * @param pageContext
	 * @param focus
	 * @param anchor
	 * @return
	 */
	public static VpeTemplate getTemplateBySourceSelection(
			VpePageContext pageContext, int focus, int anchor) {

		// get source node by offset
		Node focusNode = getSourceNodeByPosition(pageContext, focus);
		// if focus node also contain anchor point (selected only 1 element)
		if ((focusNode != null)
				&& (anchor <= ((IDOMNode) focusNode).getEndOffset())
				&& (anchor >= ((IDOMNode) focusNode).getStartOffset())) {

			VpeElementMapping elementMapping = pageContext.getDomMapping()
					.getNearElementMapping(focusNode);

			if (elementMapping != null)
				return elementMapping.getTemplate();

		}
		return null;

	}

	/**
	 * get source node by position
	 * 
	 * @param pageContext
	 * @param position
	 * @return
	 */
	public static Node getSourceNodeByPosition(VpePageContext pageContext,
			int position) {

		// get document
		IDOMDocument document = (IDOMDocument) pageContext.getSourceBuilder()
				.getSourceDocument();

		// get source node by position
		return (Node) document.getModel().getIndexedRegion(position);

	}

	/**
	 * get selected element
	 * 
	 * @param pageContext
	 * @return
	 */
	private static nsIDOMElement getVisualSelectedNode(
			VpePageContext pageContext) {

		return pageContext.getEditPart().getController().getXulRunnerEditor()
				.getLastSelectedElement();
	}

}

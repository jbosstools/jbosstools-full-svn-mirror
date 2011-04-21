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
package org.jboss.tools.vpe.editor.template.expression;

import java.util.List;

import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.w3c.dom.Node;

public class VpeFunctionHasInParents extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) throws VpeExpressionException {
		String prm = getParameter(0).exec(pageContext, sourceNode)
				.stringValue();
		Node parentNode = sourceNode.getParentNode();

		while (parentNode != null) {
			String parentSourcePrefix = parentNode.getPrefix();
			//added by Maksim Areshkau, as fix for JBIDE-5352
			//
			if(parentSourcePrefix==null) {
				parentSourcePrefix=""; //$NON-NLS-1$
			}
			List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(parentNode,
					pageContext);
			TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(
					parentSourcePrefix, taglibs);

			String parentNodeName = parentNode.getNodeName();
			if (sourceNodeTaglib != null) {
				String sourceNodeUri = sourceNodeTaglib.getUri();
				String templateTaglibPrefix = VpeTemplateManager.getInstance()
						.getTemplateTaglibPrefix(sourceNodeUri);

				if (templateTaglibPrefix != null) {
					parentNodeName = templateTaglibPrefix
							+ ":" + parentNode.getLocalName(); //$NON-NLS-1$
				}
			}
			if (parentNodeName.equals(prm)) {
				return new VpeValue(true);
			}
			parentNode = parentNode.getParentNode();
		}
		return new VpeValue(false);
	}
}

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

import java.util.Map;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 * 
 */
public class VpeVisualLinkCreator extends VpeAbstractCreator {

	private boolean caseSensitive;

	private VpeExpression styleExpr;
	private VpeExpression classExpr;
	private VpeExpression valueExpr;

	private String styleStr;
	private String classStr;
	private String valueStr;

	// private Set dependencySet;

	VpeVisualLinkCreator(Element gridElement, VpeDependencyMap dependencyMap,
			boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(gridElement, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr styleAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_FORMAT_ATTRIBUTE_TYPE_STYLE_VALUE);
		if (styleAttr != null) {
			try {
				styleStr = styleAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(styleStr, caseSensitive);
				styleExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr classAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_TEMPLATE_CLASS);
		if (styleAttr != null) {
			try {
				classStr = classAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(classStr, caseSensitive);
				classExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr valueAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_ATTRIBUTE_VALUE);
		if (valueAttr != null) {
			try {
				valueStr = valueAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(valueStr, caseSensitive);
				valueExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Map visualNodeMap) {

		nsIDOMElement a = visualDocument.createElement(HTML.TAG_A);

		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(a);

		if (styleExpr != null) {
			String style = styleExpr.exec(pageContext, sourceNode)
					.stringValue();
			a.setAttribute(HTML.ATTR_STYLE, style);
		}

		if (classExpr != null) {
			String classStyle = classExpr.exec(pageContext, sourceNode)
					.stringValue();
			a.setAttribute(HTML.ATTR_CLASS, classStyle);
		}

		if (valueExpr != null) {
			String value = valueExpr.exec(pageContext, sourceNode)
					.stringValue();
			if (value != null && value.length() > 0) {
				nsIDOMElement span = visualDocument
						.createElement(HTML.TAG_SPAN);
				a.appendChild(span);
				nsIDOMText text = visualDocument.createTextNode(value);
				span.appendChild(text);
			}
		}

		return creatorInfo;
	}

	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name, String value) {
		return true;
	}

}

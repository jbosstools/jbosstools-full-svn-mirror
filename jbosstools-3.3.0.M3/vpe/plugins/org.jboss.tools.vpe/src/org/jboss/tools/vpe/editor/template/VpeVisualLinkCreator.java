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
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
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
	private VpeExpression dirExpr;

	private String styleStr;
	private String classStr;
	private String valueStr;
	private String dirStr;

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
		
		
		final Attr dirAttr = element.getAttributeNode(HTML.ATTR_DIR);

        if (dirAttr != null) {
            try {
                dirStr = dirAttr.getValue();
                VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(dirStr, caseSensitive);
                dirExpr = info.getExpression();
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

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement,
            Map visualNodeMap) throws VpeExpressionException {

        nsIDOMElement a = visualDocument.createElement(HTML.TAG_A);

        VpeCreatorInfo creatorInfo = new VpeCreatorInfo(a);

        if (dirExpr != null) {
            String dir = dirExpr.exec(pageContext, sourceNode).stringValue();
            a.setAttribute(HTML.ATTR_DIR, dir);
        }

        if (styleExpr != null) {
            String style = styleExpr.exec(pageContext, sourceNode).stringValue();
            a.setAttribute(HTML.ATTR_STYLE, style);
        }

        if (classExpr != null) {
            String classStyle = classExpr.exec(pageContext, sourceNode).stringValue();
            a.setAttribute(HTML.ATTR_CLASS, classStyle);
        }

        if (valueExpr != null) {
            String value = valueExpr.exec(pageContext, sourceNode).stringValue();
            if (value != null && value.length() > 0) {
                nsIDOMElement textContainer = VisualDomUtil
						.createBorderlessContainer(visualDocument);
                a.appendChild(textContainer);
                nsIDOMText text = visualDocument.createTextNode(value);
                textContainer.appendChild(text);
            }
        }
        copyAttribute(sourceNode, a,"id"); //$NON-NLS-1$
        copyAttribute(sourceNode, a,"rel"); //$NON-NLS-1$
        copyAttribute(sourceNode, a,"tabindex"); //$NON-NLS-1$
        return creatorInfo;
    }
	/**
	 * Copies attribute from source node to visual node
	 * @param sourceNode
	 * @param a
	 * @param attrName
	 */
	private void copyAttribute(Node sourceNode, nsIDOMElement a, String attrName) {
		Element sourceA = (Element) sourceNode;
		String attrValue = sourceA.getAttribute(attrName);
		if ((attrValue != null) && (attrValue.length() > 0)) {
			a.setAttribute(attrName, attrValue);
		}
	}

	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name, String value) {
		return true;
	}

}

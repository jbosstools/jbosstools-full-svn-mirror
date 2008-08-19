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
import java.util.Set;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeLinkCreator extends VpeAbstractCreator {
	private boolean caseSensitive;
	private VpeExpression hrefExpr;
	private VpeExpression relExpr;

	private String hrefStr;
	private String relStr;

	private Set dependencySet;

	VpeLinkCreator(Element taglibElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(taglibElement, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr hrefAttr = element.getAttributeNode(VpeTemplateManager.ATTR_LINK_HREF);
		if (hrefAttr != null) {
			try {
				hrefStr = hrefAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(hrefStr, caseSensitive);
				hrefExpr = info.getExpression();
				dependencySet = info.getDependencySet();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr relAttr = element.getAttributeNode(VpeTemplateManager.ATTR_LINK_REL);
		if (relAttr != null) {
			try {
			    relStr = relAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(relStr, caseSensitive);
				relExpr = info.getExpression();
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
		String href_value = getExprValue(pageContext, hrefExpr, sourceNode);
		String rel_value = getExprValue(pageContext, relExpr, sourceNode);

		nsIDOMNode newNode = pageContext.getVisualBuilder().addLinkNodeToHead(href_value, "no");
		visualNodeMap.put(this, newNode);
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(null);
		return creatorInfo;
	}

	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
	    nsIDOMNode linkNode = (nsIDOMNode)visualNodeMap.get(this);
		if(linkNode != null){
			pageContext.getVisualBuilder().removeLinkNodeFromHead(linkNode);
			visualNodeMap.remove(this);
		}
		//MozillaSupports.release(linkNode);
    }

    public void refreshElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		String href_value = getExprValue(pageContext, hrefExpr, sourceElement);
		String rel_value = getExprValue(pageContext, relExpr, sourceElement);

		nsIDOMNode oldNode = (nsIDOMNode)visualNodeMap.get(this);
		nsIDOMNode newNode;
		if(oldNode == null){
			newNode = pageContext.getVisualBuilder().addLinkNodeToHead(href_value, "no");
		}else{
			newNode = pageContext.getVisualBuilder().replaceLinkNodeToHead(oldNode, href_value, "no");
			if(visualNodeMap.containsKey(this)) visualNodeMap.remove(this);
		}
		visualNodeMap.put(this, newNode);
    }

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
	    refreshElement(pageContext, sourceElement, visualNodeMap);
    }

	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
	    refreshElement(pageContext, sourceElement, visualNodeMap);
    }

	private String getExprValue(VpePageContext pageContext, VpeExpression expr, Node sourceNode) {
		String value;
		if (expr != null) {
			try {
				value = expr.exec(pageContext, sourceNode).stringValue();
			} catch (VpeExpressionException ex) {
					VpePlugin.reportProblem(ex);
					value=""; //$NON-NLS-1$
			}
		} else {
			value = ""; //$NON-NLS-1$
		}
		return value;
	}
}

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
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeValueCreator extends VpeAbstractCreator implements VpeOutputAttributes {
	public static final String SIGNATURE_VPE_VALUE = ":vpe:value"; //$NON-NLS-1$

	private VpeExpression expression;
	private String outputAttrName;

	VpeValueCreator(String value, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		build(value, dependencyMap, caseSensitive);
	}
	
	private void build(String value, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		try {
			VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(value, caseSensitive);
			expression = info.getExpression();
			dependencyMap.setCreator(this, info.getDependencySet());
			outputAttrName = VpeExpressionBuilder.getOutputAttrName(value);
			if (outputAttrName != null) {
				dependencyMap.setCreator(this, SIGNATURE_VPE_VALUE);
			}
		} catch(VpeExpressionBuilderException e) {
			VpePlugin.reportProblem(e);
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) throws VpeExpressionException {
		String value;
		if (expression != null) {
			value = expression.exec(pageContext, sourceNode).stringValue();
		} else {
			value = ""; //$NON-NLS-1$
		}
		nsIDOMText valueNode = visualDocument.createTextNode(value);
		visualNodeMap.put(this, valueNode);
		return new VpeCreatorInfo(valueNode);
	}

	public void refreshElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		setValue(pageContext, sourceElement, visualNodeMap);
	}

	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		setValue(pageContext, sourceElement, visualNodeMap);
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		setValue(pageContext, sourceElement, visualNodeMap);
	}
	
	private void setValue(VpePageContext pageContext, Element sourceElement, Map<?,?> visualNodeMap) {
		String value;
		if (expression != null) {
			try {
				value = expression.exec(pageContext, sourceElement).stringValue();
			} catch (VpeExpressionException ex) {
				VpePlugin.reportProblem(ex);
				value=""; //$NON-NLS-1$
			}
		} else {
			value = ""; //$NON-NLS-1$
		}
		nsIDOMNode valueNode = (nsIDOMNode) visualNodeMap.get(this);
		valueNode.setNodeValue(value);
	}

	public String[] getOutputAttributes() {
		if (outputAttrName == null) {
			return null;
		}
		return new String[] {outputAttrName};
	}

	public void setOutputAttributeValue(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		if (outputAttrName != null) {
			Node valueNode = (Node) visualNodeMap.get(this);
			sourceElement.setAttribute(outputAttrName, valueNode.getNodeValue());
		}
	}

	public nsIDOMText getOutputTextNode(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		if (outputAttrName != null) {
			return (nsIDOMText) visualNodeMap.get(this);
		}
		return null;
	}

	public boolean isEditabledAtribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		if (outputAttrName != null && expression != null) {
			String attrValue = sourceElement.getAttribute(outputAttrName);
			String exprValue;
			try {
				exprValue = expression.exec(pageContext, sourceElement).stringValue();
				return exprValue.equals(attrValue);
			} catch (VpeExpressionException ex) {
				VpePlugin.reportProblem(ex);
			}

		}
		return false;
	}

	public void setOutputAttributeSelection(VpePageContext pageContext, Element sourceElement, int offset, int length, Map visualNodeMap) {
		if (outputAttrName != null) {
			VpeSourceDomBuilder sourceBuilder = pageContext.getSourceBuilder();
			Attr attr = sourceElement.getAttributeNode(outputAttrName);
			if (attr != null) { 
				if (isEditabledAtribute(pageContext, sourceElement, visualNodeMap)) {
					sourceBuilder.setAttributeSelection(attr, offset, length);
				} else {
					sourceBuilder.setAttributeSelection(attr, 0, 0);
				}
			}
		}
	}
}

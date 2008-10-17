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
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeAttributeCreator extends VpeAbstractCreator {
	private String name;
	private VpeExpression expression;

	VpeAttributeCreator(String name, String value, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		build(name, value, dependencyMap, caseSensitive);
	}
	
	private void build(String name, String value, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.name = name;
		try {
			VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(value, caseSensitive);
			expression = info.getExpression();
			if (dependencyMap != null) {
				dependencyMap.setCreator(this, info.getDependencySet());
			}
		} catch(VpeExpressionBuilderException e) {
			VpePlugin.reportProblem(e);
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) throws VpeExpressionException {

		if (expression != null) {
			if (visualNodeMap != null) {
				visualNodeMap.put(this, visualElement);
			}
			VpeValue vpeValue = expression.exec(pageContext, sourceNode);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
				nsIDOMAttr newVisualAttribute = visualDocument.createAttribute(name);
				newVisualAttribute.setValue(vpeValue.stringValue());
				return new VpeCreatorInfo(newVisualAttribute);
			}			
		}
		return null;
	}
	
	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		setValue(pageContext, sourceElement, visualNodeMap);
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		setValue(pageContext, sourceElement, visualNodeMap);
	}
	
	private void setValue(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		try{
		if (expression != null) {
			nsIDOMElement visualElement = (nsIDOMElement) visualNodeMap.get(this);
			VpeValue vpeValue = expression.exec(pageContext, sourceElement);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
				visualElement.setAttribute(this.name, vpeValue.stringValue());
			} else {
				visualElement.removeAttribute(this.name);
			}
		}
		} catch(VpeExpressionException ex) {
			VpeExpressionException exception = new VpeExpressionException(sourceElement.toString()+" "+expression.toString(),ex); //$NON-NLS-1$
			VpePlugin.reportProblem(exception);
		}
	}
}

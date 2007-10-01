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

import java.util.Set;

import org.w3c.dom.Node;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;

public class VpeTemplateConditionSet extends VpeTemplateSet {
	private VpeExpression expression;
	private Set dependencySet;

	VpeTemplateConditionSet(String test, boolean caseSensitive) {
		build(test, caseSensitive);
	}
	
	private void build(String test, boolean caseSensitive) {
		try {
			VpeExpressionInfo info = new VpeExpressionBuilder().buildPlainExpression(test, caseSensitive);
			expression = info.getExpression();
			dependencySet = info.getDependencySet();
		} catch(VpeExpressionBuilderException e) {
			VpePlugin.reportProblem(e);
		}
	}

	VpeTemplate getTemplate(VpePageContext pageContext, Node sourceNode, Set ifDependencySet) {
		if (dependencySet != null) {
			ifDependencySet.addAll(dependencySet);
		}
		if (!test(pageContext, sourceNode)) {
			return null;
		}
		return super.getTemplate(pageContext, sourceNode, dependencySet);
	}
	
	private boolean test(VpePageContext pageContext, Node sourceNode) {
		if (expression == null) {
			return false;
		}
		VpeValue value = expression.exec(pageContext, sourceNode);
		if (value == null) {
			return false;
		}
		return value.booleanValue();
	}
}

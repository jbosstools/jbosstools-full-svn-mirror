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

import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeCompletedExpression implements VpeExpression {
	private VpeExpression[] expressions = null;
	
	VpeCompletedExpression(VpeExpression[] expressions) {
		this.expressions = expressions;
	}

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) throws VpeExpressionException {
		if (expressions == null) {
			return new VpeValue(""); //$NON-NLS-1$
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < expressions.length; i++) {
			result.append(expressions[i].exec(pageContext, sourceNode).stringValue());
		}
		return new VpeValue(result.toString());
	}
}

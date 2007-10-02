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

public class VpeOrOperation extends VpeOperation {

	int getPriority() {
		return PRIORITY_OPERATION_OR;
	}

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		VpeValue leftValue = getLeftOperand().exec(pageContext, sourceNode);
		VpeValue rightValue = getRightOperand().exec(pageContext, sourceNode);
		return leftValue.or(rightValue);
	}
}

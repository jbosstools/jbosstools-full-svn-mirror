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

public abstract class VpeOperation extends VpeOperand {
	static final int PRIORITY_OPERATION_PLUS = 1;
	static final int PRIORITY_OPERATION_EQUAL = 2;
	static final int PRIORITY_OPERATION_AND = 3;
	static final int PRIORITY_OPERATION_OR = 4;

	private VpeOperand leftOperand;
	private VpeOperand rightOperand;
	
	VpeOperand intoExpression(VpeOperand topOperand, VpeOperation lastOperation) {
		if (lastOperation != null) {
			VpeOperation parentOperation = null;
			VpeOperand leftOperand = topOperand;
			while (getPriority() < leftOperand.getPriority() && leftOperand != lastOperation.rightOperand) { 
				parentOperation = (VpeOperation)leftOperand;
				leftOperand = parentOperation.getRightOperand();
			}
			this.leftOperand = leftOperand;
			if (parentOperation != null) {
				parentOperation.setRightOperand(this);
				return topOperand;
			} else {
				return this;
			}
		} else {
			this.leftOperand = topOperand;
			return this;
		}
	}
	
	void setRightOperand(VpeOperand rightOperand) {
		this.rightOperand = rightOperand;
	}
	
	VpeOperand getLeftOperand() {
		return leftOperand;
	}
	
	VpeOperand getRightOperand() {
		return rightOperand;
	}
}

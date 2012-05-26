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

import java.util.Arrays;

import org.jboss.tools.vpe.messages.VpeUIMessages;

public abstract class VpeFunction extends VpeOperand {
	
	private static final String[] EMPTY_SIGNS = new String[0];
	private VpeOperand[] paramertes;
	private String[] signatures = EMPTY_SIGNS;
	
	VpeFunction() {
	}
	
	void setParameters(VpeOperand[] paramertes) {
		this.paramertes = paramertes;
	}
	
	@Override
	int getPriority() {
		return PRIORITY_OPERAND;
	}

	VpeOperand getParameter(int index) throws VpeExpressionException {
		if (paramertes == null || paramertes.length < index) {
			throw new VpeExpressionException(VpeUIMessages.INCORRECT_PARAMETER_ERROR);
		}
		return paramertes[index];
	}
	
	String[] getSignatures() {
		return signatures;
	}
}

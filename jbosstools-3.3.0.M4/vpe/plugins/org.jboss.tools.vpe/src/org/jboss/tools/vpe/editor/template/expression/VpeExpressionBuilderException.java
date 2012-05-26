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

import java.text.MessageFormat;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeExpressionBuilderException extends Exception {

	private final String expression;
	private final String localizedErrorText;
	private final int pos;

	public VpeExpressionBuilderException(String expression, String errorText, int pos, String localizedErrorText) {
		super(MessageFormat.format("Expression: \"{0}\"  pos={1}  {2}", expression, pos, errorText)); //$NON-NLS-1$
		this.expression = expression;
		this.localizedErrorText = localizedErrorText;
		this.pos = pos;
	}
	
	@Deprecated // use constructor which supports localization
	public VpeExpressionBuilderException(String expression, String message, int pos) {
		this(expression, message, pos, message);
	}
	
	@Override
	public String getLocalizedMessage() {
		return MessageFormat.format(VpeUIMessages.VpeExpressionBuilderException_Message, expression,
				pos, localizedErrorText);
	}
}

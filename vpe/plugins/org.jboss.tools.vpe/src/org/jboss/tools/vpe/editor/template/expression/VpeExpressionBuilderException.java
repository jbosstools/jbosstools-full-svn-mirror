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

public class VpeExpressionBuilderException extends Exception {
	private static final long serialVersionUID = 2506700447659526980L;

	public VpeExpressionBuilderException(String expression, String message, int pos) {
		super("Expression: \"" + expression + "\"  pos=" + pos + "  " + message);
	}
}

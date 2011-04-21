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

public class VpeTextExpression implements VpeExpression {
	private String text = null;
	
	VpeTextExpression(String text) {
		this.text = text;
	}

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		if (text == null) {
			return new VpeValue(""); //$NON-NLS-1$
		} else {
			return new VpeValue(text);
		}
	}
}

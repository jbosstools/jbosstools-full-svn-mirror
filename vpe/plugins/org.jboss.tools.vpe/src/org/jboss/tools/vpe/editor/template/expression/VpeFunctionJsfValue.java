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

import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Node;

public class VpeFunctionJsfValue extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) throws VpeExpressionException {
		String value = getParameter(0).exec(pageContext, sourceNode).stringValue();
		BundleMap bundle = pageContext.getBundle();
		String jsfValue = bundle.getBundleValue(value);
		return new VpeValue(jsfValue != null ? jsfValue : value);
	}

	@Override
	String[] getSignatures() {
		return new String[] {VpeExpressionBuilder.SIGNATURE_JSF_VALUE};
	}
}

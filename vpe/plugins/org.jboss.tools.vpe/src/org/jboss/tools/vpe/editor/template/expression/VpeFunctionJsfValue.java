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

import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeFunctionJsfValue extends VpeFunction {

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		String value = getParameter(0).exec(pageContext, sourceNode).stringValue();
		BundleMap bundle = pageContext.getBundle();
		int offset = pageContext.getVisualBuilder().getCurrentMainIncludeOffset();
		if (offset == -1) offset = ((IndexedRegion)sourceNode).getStartOffset();
		String jsfValue = bundle.getBundleValue(value, offset);
		return new VpeValue(jsfValue != null ? jsfValue : value);
	}

	String[] getSignatures() {
		return new String[] {VpeExpressionBuilder.SIGNATURE_JSF_VALUE};
	}
}

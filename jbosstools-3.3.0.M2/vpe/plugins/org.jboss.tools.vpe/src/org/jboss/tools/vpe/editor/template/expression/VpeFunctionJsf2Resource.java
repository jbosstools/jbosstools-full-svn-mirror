/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template.expression;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.w3c.dom.Node;

/**
 * Implementation of the function {@code jsf2resource(library, name)}.
 * 
 * @author yradtsevich
 */
public class VpeFunctionJsf2Resource extends VpeFunction {

	/**
	 * Returns the path to the resource specified by the {@code library}
	 * and the {@code name}.
	 * 
	 * @see FileUtil#getJSF2ResourcePath(VpePageContext, String, String)
	 */
	public VpeValue exec(VpePageContext pageContext, Node sourceNode)
			throws VpeExpressionException {
		String library = getParameter(0).exec(pageContext, sourceNode).stringValue();
		if ("".equals(library)) { //$NON-NLS-1$
			library = null;
		}

		String name = getParameter(1).exec(pageContext, sourceNode).stringValue();
		
		String resourcePath = FileUtil.getJSF2ResourcePath(pageContext, library, name);
		return new VpeValue(resourcePath);
	}
}

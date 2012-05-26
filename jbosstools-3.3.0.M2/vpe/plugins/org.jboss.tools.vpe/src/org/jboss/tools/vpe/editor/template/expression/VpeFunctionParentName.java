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
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
/**
 *
 * @author mareshkau
 *
 */
public class VpeFunctionParentName extends VpeFunction {
	/**
	 * Returns parent node name, with changed prefix like in template*.xml File
	 */
	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		Node parentNode = sourceNode.getParentNode();
		
		String parentTemplateName = VpeTemplateManager.getInstance().getTemplateName(pageContext, parentNode);
			
		return new VpeValue(parentTemplateName != null ? parentTemplateName : ""); //$NON-NLS-1$
	}
}

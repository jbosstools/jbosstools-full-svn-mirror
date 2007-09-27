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
package org.jboss.tools.vpe.editor.template;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public interface VpeOutputAttributes {
	void setOutputAttributeValue(VpePageContext pageContext, Element sourceElement, Map visualNodeMap);
	String[] getOutputAttributes();
	boolean isEditabledAtribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap);
	void setOutputAttributeSelection(VpePageContext pageContext, Element sourceElement, int offset, int length, Map visualNodeMap);
	Node getOutputTextNode(VpePageContext pageContext, Element sourceElement, Map visualNodeMap);
}

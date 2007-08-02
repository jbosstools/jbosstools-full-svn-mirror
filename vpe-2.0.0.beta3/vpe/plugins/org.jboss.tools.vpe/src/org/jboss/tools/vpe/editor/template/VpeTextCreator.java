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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeTextCreator extends VpeAbstractCreator {
	private String text;
	
	VpeTextCreator(Node textNode, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		build(textNode, dependencyMap, caseSensitive);
	}

	private void build(Node textNode, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		text = textNode.getNodeValue();
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		return new VpeCreatorInfo(visualDocument.createTextNode(text));
	}
}

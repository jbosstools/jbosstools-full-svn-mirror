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
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeJspRootCreator extends VpeAbstractCreator {

	VpeJspRootCreator(Element taglibElement, VpeDependencyMap dependencyMap) {
		build(taglibElement, dependencyMap);
	}

	private void build(Element sourceElement, VpeDependencyMap dependencyMap) {
//		dependencyMap.setCreator(this, VpeExpressionBuilder.SIGNATURE_ANY_ATTR);
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
//		setTaglib(pageContext, (Element)sourceNode);
		return null;
	}
	
}

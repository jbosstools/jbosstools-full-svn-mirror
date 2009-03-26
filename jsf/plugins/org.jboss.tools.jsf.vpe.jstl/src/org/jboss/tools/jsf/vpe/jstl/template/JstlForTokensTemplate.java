/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.vpe.jstl.template;

import java.util.List;

import org.jboss.tools.jsf.vpe.jstl.template.util.JstlUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class for c:forTokens template
 * 
 * @author dmaliarevich
 *
 */
public class JstlForTokensTemplate extends VpeAbstractTemplate {

    private final int TIMES_TO_ITERATE = 3;
    
    public JstlForTokensTemplate() {
	super();
    }

    public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
	    nsIDOMDocument visualDocument) {
	Element sourceElement = (Element) sourceNode;
	nsIDOMElement span = VisualDomUtil.createBorderlessContainer(visualDocument);
	List<Node> children = JstlUtil.getChildren(sourceElement);
	VpeCreationData creationData = new VpeCreationData(span);
	VpeChildrenInfo spanInfo = new VpeChildrenInfo(span);
	creationData.addChildrenInfo(spanInfo);
	for(int i = 0; i < TIMES_TO_ITERATE; i++) {
	    for (Node child : children) {
		spanInfo.addSourceChild(child);
	    }
	}
	return creationData;
    }

}

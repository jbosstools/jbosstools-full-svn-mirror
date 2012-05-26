/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

/**
 * Used for processing templates with renderd="false" attribute.
 * 
 * @author mareshkau
 *
 */
public class VpeRenderingTemplate extends VpeAbstractTemplate{
	
	private static VpeRenderingTemplate instance;
	
	private VpeRenderingTemplate(){
		
	}
	
	public static synchronized  VpeRenderingTemplate getInstance(){
		if(instance==null)  {
			instance = new VpeRenderingTemplate();
		}
		return instance;
	}
	
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		return  new VpeCreationData(null);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractTemplate#getNodeForUpdate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.lang.Object)
	 */
	@Override
	public Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		//if rendered=false, template hasn't visual presentations
		//so we should upadte parent node
		return sourceNode.getParentNode();
	}
}

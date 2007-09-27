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

public class VpeStyleCreator extends VpeAbstractCreator{
	
	public VpeStyleCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		//this.caseSensitive = caseSensitive;
		//build(element, dependencyMap);
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode,
			Document visualDocument, Element visualElement,
			Map visualNodeMap) {
		
		Node textNode = sourceNode.getFirstChild();
		String text = null;
		if(textNode != null){
			text = textNode.getNodeValue();
		}
		Node newStyle = pageContext.getVisualBuilder().addStyleNodeToHead(text);
		visualNodeMap.put(this, newStyle);
		return null;
	}

	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		
		Node styleNode = (Node)visualNodeMap.get(this);
		
		if(styleNode != null){
			pageContext.getVisualBuilder().removeStyleNodeFromHead(styleNode);
			visualNodeMap.remove(this);
		}
		
	}

	public void refreshElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		Node oldStyleNode = (Node)visualNodeMap.get(this);
		
		Node textNode = sourceElement.getFirstChild();
		String text = null;
		if(textNode != null){
			text = textNode.getNodeValue();
		}
		Node newStyleNode;
		if(oldStyleNode == null){
			newStyleNode = pageContext.getVisualBuilder().addStyleNodeToHead(text);
			visualNodeMap.put(this, newStyleNode);
		}else{
			newStyleNode = pageContext.getVisualBuilder().replaceStyleNodeToHead(oldStyleNode, text);
			if(visualNodeMap.containsKey(this)) visualNodeMap.remove(this);
			visualNodeMap.put(this, newStyleNode);
		}
		
	}
}

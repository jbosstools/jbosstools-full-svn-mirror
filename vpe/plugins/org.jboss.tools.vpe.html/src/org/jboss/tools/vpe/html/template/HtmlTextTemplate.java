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

package org.jboss.tools.vpe.html.template;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.RangeException;

/**
 * @author mareshkau
 * Template for text nodes
 */
public class HtmlTextTemplate extends VpeAbstractTemplate {
	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		String sourceText = sourceNode.getNodeValue();
	    
		if (sourceText.trim().length() <= 0) {
	    	return new VpeCreationData(visualDocument.createTextNode(sourceText));
	    }
	    String visualText = TextUtil.visualText(sourceText);

	    nsIDOMNode visualNewTextNode = visualDocument
	        .createTextNode(visualText);
	    //Max Areshkau without this container, we can't select text element
	    nsIDOMElement element = VisualDomUtil.createBorderlessContainer(visualDocument);
	    element.appendChild(visualNewTextNode);
	    
	    VpeElementData textElementData = new VpeElementData();
	    NodeData nodeData = new NodeData(sourceNode, visualNewTextNode);
	    textElementData.addNodeData(nodeData);
	    VpeCreationData result = new VpeCreationData(element);
	    result.setElementData(textElementData);
	    
	    return result;
	}

//	/* (non-Javadoc)
//	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractTemplate#getSourceRegionForOpenOn(org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode)
//	 */
	@Override
	public IRegion getSourceRegionForOpenOn(VpePageContext pageContext, Node sourceNode, nsIDOMNode domNode) {
			
		Point selection = pageContext.getSourceBuilder().getSelectionRange();
		//processing for el expressions
		int offset = TextUtil.getStartELDocumentPosition(sourceNode);
		IRegion resultRegion;
		if(offset >= 0) {
			resultRegion = new Region(offset,0);
		} else {
			resultRegion = new Region(selection.x,0);
		}
		return resultRegion;
	}
	
	
}

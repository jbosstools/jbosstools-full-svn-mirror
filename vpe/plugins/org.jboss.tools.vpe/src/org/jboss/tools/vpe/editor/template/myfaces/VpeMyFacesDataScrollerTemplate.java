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
package org.jboss.tools.vpe.editor.template.myfaces;

import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeCreatorInfo;

public class VpeMyFacesDataScrollerTemplate extends VpeAbstractTemplate {

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode, Document visualDocument) {
		Element table = visualDocument.createElement("table");
		VpeCreationData data = new VpeCreationData(table);
		VpeChildrenInfo childInfo = createFacet(sourceNode, "first", visualDocument);
		Element td = null;
		if (childInfo != null) {
			data.addChildrenInfo(childInfo);
			td = childInfo.getVisualParent();
			table.appendChild(td);
		}

		childInfo = createFacet(sourceNode, "fastrewind", visualDocument);
		if (childInfo != null) {
			data.addChildrenInfo(childInfo);
			td = childInfo.getVisualParent();
			table.appendChild(td);
		}

		childInfo = createFacet(sourceNode, "previous", visualDocument);
		if (childInfo != null) {
			data.addChildrenInfo(childInfo);
			td = childInfo.getVisualParent();
			table.appendChild(td);
		}

		td = visualDocument.createElement("td");
		Text text = visualDocument.createTextNode("1 2 3 ...");
		td.appendChild(text);
		table.appendChild(td);
		
		childInfo = createFacet(sourceNode, "next", visualDocument);
		if (childInfo != null) {
			data.addChildrenInfo(childInfo);
			td = childInfo.getVisualParent();
			table.appendChild(td);
		}

		childInfo = createFacet(sourceNode, "fastforward", visualDocument);
		if (childInfo != null) {
			data.addChildrenInfo(childInfo);
			td = childInfo.getVisualParent();
			table.appendChild(td);
		}

		childInfo = createFacet(sourceNode, "last", visualDocument);
		if (childInfo != null) {
			data.addChildrenInfo(childInfo);
			td = childInfo.getVisualParent();
			table.appendChild(td);
		}

		return data;
	}

	private VpeChildrenInfo createFacet(Node sourceNode, String facetName, Document visualDocument) {
		if (sourceNode != null && facetName != null) {
			NodeList childs = sourceNode.getChildNodes();
			if (childs != null) {
				for (int i = 0; i < childs.getLength(); i++) {
					Node child = childs.item(i);
					if (child != null && child.getNodeType() == Node.ELEMENT_NODE &&
							child.getNodeName().indexOf(":facet") > 0) {
						Element facet = (Element)child;
						Attr nameAttr = facet.getAttributeNode("name");
						if (nameAttr != null && nameAttr.getValue().equals(facetName)) {
							Element td = visualDocument.createElement("td");
							VpeChildrenInfo info = new VpeChildrenInfo(td);
							info.addSourceChild(facet);
							return info;
						}
					}
				}
			}
		}
		return null;
	}

	public void setAttribute(VpePageContext pageContext, Element sourceElement, Document visualDocument, Node visualNode, Object data, String name, String value) {
		// TODO Auto-generated method stub
		
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Document visualDocument, Node visualNode, Object data, String name) {
		// TODO Auto-generated method stub
		
	}

	public String[] getOutputAtributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getOutputTextNode(VpePageContext pageContext, Element sourceElement, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOutputAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public VpeAnyData getAnyData() {
		// TODO Auto-generated method stub
		return null;
	}
}

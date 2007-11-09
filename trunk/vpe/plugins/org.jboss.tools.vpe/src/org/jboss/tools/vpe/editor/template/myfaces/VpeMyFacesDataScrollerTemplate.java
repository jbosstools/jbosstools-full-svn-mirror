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
import org.jboss.tools.vpe.editor.template.VpeTagDescription;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;

public class VpeMyFacesDataScrollerTemplate extends VpeAbstractTemplate {

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument) {
		nsIDOMElement table = visualDocument.createElement("table");
		VpeCreationData data = new VpeCreationData(table);
		VpeChildrenInfo childInfo = createFacet(sourceNode, "first", visualDocument);
		nsIDOMElement td = null;
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
		nsIDOMText text = visualDocument.createTextNode("1 2 3 ...");
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

	private VpeChildrenInfo createFacet(Node sourceNode, String facetName, nsIDOMDocument visualDocument) {
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
							nsIDOMElement td = visualDocument.createElement(HTML.TAG_TD);
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
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Document visualDocument, Node visualNode, Object data, String name) {
	}

	public String[] getOutputAtributeNames() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractTemplate#getOutputTextNode(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, java.lang.Object)
	 */
	@Override
	public nsIDOMText getOutputTextNode(VpePageContext pageContext,
			Element sourceElement, Object data) {
		return null;
	}

	public boolean isOutputAttributes() {
		return false;
	}

	public int getType() {
		return 0;
	}

	public VpeAnyData getAnyData() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#beforeRemove(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.lang.Object)
	 */
	@Override
	public void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getNodeForUptate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.lang.Object)
	 */
	@Override
	public Node getNodeForUptate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getTagDescription(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, java.lang.Object)
	 */
	@Override
	public VpeTagDescription getTagDescription(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualElement, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#isRecreateAtAttrChange(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNde, Object data, String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#removeAttribute(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String)
	 */
	@Override
	public void removeAttribute(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#resize(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, java.lang.Object, int, int, int, int, int)
	 */
	@Override
	public void resize(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Object data, int resizerConstrains, int top, int left, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setAttribute(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public void setAttribute(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data,
			String name, String value) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#validate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument, org.jboss.tools.vpe.editor.template.VpeCreationData)
	 */
	@Override
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data) {
		// TODO Auto-generated method stub
		
	}
	
	
}

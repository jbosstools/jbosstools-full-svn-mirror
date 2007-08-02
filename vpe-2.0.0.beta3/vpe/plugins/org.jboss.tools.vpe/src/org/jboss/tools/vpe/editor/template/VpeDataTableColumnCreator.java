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
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualDataTableElements;

public class VpeDataTableColumnCreator extends VpeAbstractCreator {
	private boolean caseSensitive;

	VpeDataTableColumnCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {

		int index = getColumnIndex(sourceNode);

		VpeCreatorInfo creatorInfo = null;

		Node visualParent = null;
		Node visualNode = pageContext.getCurrentVisualNode();
		if (visualNode != null) {
			visualParent = visualNode.getParentNode();
		}

		SourceColumnElements columnElements = new SourceColumnElements(sourceNode);
		if (visualParent != null && visualParent.getNodeName().equalsIgnoreCase("table") && columnElements != null) {
			VisualDataTableElements visualDataTableElements = VpeDataTableElements.getVisualDataTableElements(visualParent);
			Element col = visualDocument.createElement("col");
			Element colgroup = VpeDataTableElements.getNamedChild(visualParent, "colgroup", 0);
			creatorInfo = new VpeCreatorInfo(col);
			VisualColumnElements visualColumnElements = new VisualColumnElements();
			if (colgroup != null) {
				colgroup.appendChild(col);
				VpeChildrenInfo info = null;
				Element cell = VpeDataTableElements.makeCell(visualDataTableElements.getColumnsHeaderRow(), index, "th", visualDocument);
				info = new VpeChildrenInfo(cell);
				if (columnElements.hasHeader()) {
					info.addSourceChild(columnElements.getHeader());
				}
				creatorInfo.addChildrenInfo(info);
				visualColumnElements.setHeaderCell(cell);

				cell = VpeDataTableElements.makeCell(visualDataTableElements.getColumnsFooterRow(), index, "td", visualDocument);
				info = new VpeChildrenInfo(cell);
				if (columnElements.hasFooter()) {
					info.addSourceChild(columnElements.getFooter());
				}
				creatorInfo.addChildrenInfo(info);
				visualColumnElements.setFooterCell(cell);
				
				cell = VpeDataTableElements.makeCell(visualDataTableElements.getBodyRow(), index, "TD", visualDocument);
				NodeList list = sourceNode.getChildNodes();
				int cnt = list != null ? list.getLength() : 0;
				if (cnt > 0) {
					info = new VpeChildrenInfo(cell);
					boolean useTextNodeAsBody = true; // for facelets
					for (int i = 0; i < cnt; i++) {
						Node node = list.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							boolean isFacet = namesIsEquals("facet", node.getNodeName());
							Node attrName = node.getAttributes().getNamedItem("name");
							if (!isFacet || (attrName != null && !"header".equals(attrName.getNodeValue()) && !"footer".equals(attrName.getNodeValue()))) {
								info.addSourceChild(node);
								useTextNodeAsBody = false;
							}
						}
					}
					if(useTextNodeAsBody) {
						Node text = VpeCreatorUtil.getTextChildNode(sourceNode);
						if(text!=null) {
							info.addSourceChild(text);
						}
					}
					creatorInfo.addChildrenInfo(info);
					visualColumnElements.setBodyCell(cell);
				}
				visualNodeMap.put(this, visualColumnElements);
			}
		}

		return creatorInfo;
	}

	private int getColumnIndex(Node sourceNode) {
		int index = 0;
		Node prevNode = sourceNode.getPreviousSibling();
		while (prevNode != null) {
			if((sourceNode.getNodeName().indexOf("column") >= 0 || sourceNode.getNodeName().indexOf("treeColumn") >= 0) && (prevNode.getNodeName().indexOf("column") >= 0 || prevNode.getNodeName().indexOf("treeColumn") >= 0)){
				index++;
			}else if (prevNode.getNodeName().equals(sourceNode.getNodeName())) {
				index++;
			}
			prevNode = prevNode.getPreviousSibling();
		}
		return index;
	}

	public boolean isRecreateAtAttrChange(VpePageContext pageContext, Element sourceElement, Document visualDocument, Node visualNode, Object data, String name, String value) {
		return true;
	}

	public Node getNodeForUptate(VpePageContext pageContext, Node sourceNode, Node visualNode, Map visualNodeMap) {
		return sourceNode.getParentNode();
	}

	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		Object elements = visualNodeMap.get(this);
		if (elements != null && elements instanceof VisualColumnElements) {
			removeChild(((VisualColumnElements)elements).getHeaderCell());
			removeChild(((VisualColumnElements)elements).getBodyCell());
			removeChild(((VisualColumnElements)elements).getFooterCell());
		}
	}

	private static void removeChild(Element child) {
		if (child != null && child.getParentNode() != null) {
			child.getParentNode().removeChild(child);
		}
	}

	private static boolean namesIsEquals(String name1, String name2) {
		int ind = name2.indexOf(":");
		return ind < name2.length() && name1.equals(name2.substring(ind >= 0 ? ind + 1 : 0));
	}
}

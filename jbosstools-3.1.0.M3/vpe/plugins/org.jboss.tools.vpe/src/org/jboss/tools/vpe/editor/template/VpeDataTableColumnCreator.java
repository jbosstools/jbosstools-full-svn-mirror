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
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceDataTableElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualDataTableElements;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeDataTableColumnCreator extends VpeAbstractCreator {


	VpeDataTableColumnCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {

		int index = getColumnIndex(sourceNode);

		VpeCreatorInfo creatorInfo = null;

		nsIDOMNode visualParent = null;
		nsIDOMNode visualNode = pageContext.getCurrentVisualNode();
		if (visualNode != null) {
			visualParent = visualNode.getParentNode();
		}

		SourceColumnElements columnElements = new SourceColumnElements(sourceNode);
		
		if (visualParent != null && HTML.TAG_TABLE.equalsIgnoreCase(visualParent.getNodeName()) && columnElements != null) {
			VisualDataTableElements visualDataTableElements = VpeDataTableElements.getVisualDataTableElements(visualParent);
			VisualColumnElements visualColumnElements = new VisualColumnElements();
			nsIDOMElement col = visualDocument.createElement(HTML.TAG_COL);
			nsIDOMElement colgroup = VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_COLGROUP, 0);
			creatorInfo = new VpeCreatorInfo(col);
			if (colgroup != null) {
				colgroup.appendChild(col);
				VpeChildrenInfo info = null;
				nsIDOMElement cell = VpeDataTableElements.makeCell(visualDataTableElements.getColumnsHeaderRow(), index, HTML.TAG_TH, visualDocument);
				info = new VpeChildrenInfo(cell);
				if (columnElements.hasHeader()) {
					info.addSourceChild(columnElements.getHeader());
				}
				creatorInfo.addChildrenInfo(info);
				
				String styleClass = ""; //$NON-NLS-1$
				String tableHeaderClass = getNodeAttrValue(sourceNode.getParentNode(), VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS);
				String columnHeaderClass = getNodeAttrValue(sourceNode, VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS);
				if (null != columnHeaderClass) {
					styleClass = columnHeaderClass;
				} else if (null != tableHeaderClass) {
					styleClass = tableHeaderClass;
				}
				setCellClass(cell, styleClass);
				visualColumnElements.setHeaderCell(cell);

				cell = VpeDataTableElements.makeCell(visualDataTableElements.getColumnsFooterRow(), index, HTML.TAG_TD, visualDocument);
				info = new VpeChildrenInfo(cell);
				if (columnElements.hasFooter()) {
					info.addSourceChild(columnElements.getFooter());
				}
				creatorInfo.addChildrenInfo(info);
				
				styleClass = ""; //$NON-NLS-1$
				String tableFooterClass = getNodeAttrValue(sourceNode.getParentNode(), VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS);
				String columnFooterClass = getNodeAttrValue(sourceNode, VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS);
				if (null != columnFooterClass) {
					styleClass = columnFooterClass;
				} else if (null != tableFooterClass) {
					styleClass = tableFooterClass;
				}
				setCellClass(cell, styleClass);
				visualColumnElements.setFooterCell(cell);
		
				cell = VpeDataTableElements.makeCell(visualDataTableElements.getBodyRow(), index, HTML.TAG_TD, visualDocument);
				NodeList list = sourceNode.getChildNodes();
				int cnt = list != null ? list.getLength() : 0;
				if (cnt > 0) {
					info = new VpeChildrenInfo(cell);
					boolean useTextNodeAsBody = true; // for facelets
					for (int i = 0; i < cnt; i++) {
						Node node = list.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							boolean isFacet = namesIsEquals("facet", node.getNodeName()); //$NON-NLS-1$
							Node attrName = node.getAttributes().getNamedItem("name"); //$NON-NLS-1$
							if (!isFacet || (attrName != null && !"header".equals(attrName.getNodeValue()) && !"footer".equals(attrName.getNodeValue()))) { //$NON-NLS-1$ //$NON-NLS-2$
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
					
					String columnClasses = getNodeAttrValue(sourceNode.getParentNode(), VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES);
					if (null != columnClasses) {
						setColumnClassesToCell(cell, columnClasses, index);
					}
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
			if((sourceNode.getNodeName().indexOf("column") >= 0 || sourceNode.getNodeName().indexOf("treeColumn") >= 0) && (prevNode.getNodeName().indexOf("column") >= 0 || prevNode.getNodeName().indexOf("treeColumn") >= 0)){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				index++;
			}else if (prevNode.getNodeName().equals(sourceNode.getNodeName())) {
				index++;
			}
			prevNode = prevNode.getPreviousSibling();
		}
		return index;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractCreator#isRecreateAtAttrChange(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name, String value) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractCreator#getNodeForUpdate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.util.Map)
	 */
	@Override
	public Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Map visualNodeMap) {
		return sourceNode.getParentNode();
	}

	@Override
	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		Object elements = visualNodeMap.get(this);
		if (elements != null && elements instanceof VisualColumnElements) {
			removeChild(((VisualColumnElements)elements).getHeaderCell());
			removeChild(((VisualColumnElements)elements).getBodyCell());
			removeChild(((VisualColumnElements)elements).getFooterCell());
		}
	}

	private static void removeChild(nsIDOMElement child) {
		if (child != null && child.getParentNode() != null) {
			child.getParentNode().removeChild(child);
		}
	}

	private static boolean namesIsEquals(String name1, String name2) {
		int ind = name2.indexOf(":"); //$NON-NLS-1$
		return ind < name2.length() && name1.equals(name2.substring(ind >= 0 ? ind + 1 : 0));
	}
	
	/**
	 * Sets the column classes to cell.
	 * 
	 * @param cell the cell
	 * @param columnClasses the column classes
	 * @param index the index of the column in the table
	 */
	private void setColumnClassesToCell(nsIDOMElement cell,
			String columnClasses, int index) {
		if (cell != null) {
			String[] classes = splitClasses(columnClasses);
			if ((null != classes) && (classes.length > 0)) {
				int classesCount = classes.length;
				int columnCount = index + 1;
				String className = ""; //$NON-NLS-1$

				// Finds correct css style class index
				// for the column
				if (columnCount <= classesCount) {
					className = classes[columnCount - 1];
				} else {
					int remainder = columnCount % classesCount;
					int classesIndex = ((0 == remainder) ? (classesCount-1) : (remainder-1));
					className = classes[classesIndex];
				}
				if (className.trim().length() > 0) {
					cell.setAttribute("class", className); //$NON-NLS-1$
				}
			}

		}
	}
	
	/**
	 * Splits a sequence of classes to an array of separate classes.
	 * 
	 * @param value the sequence of classes
	 * 
	 * @return the array of separate classes
	 */
	private String[] splitClasses(String value) {
		if (value != null) {
			return value.split(","); //$NON-NLS-1$
		}
		return null;
	}
	
	/**
	 * Sets the css class to the cell.
	 * 
	 * @param cell the cell
	 * @param className the class name
	 */
	private void setCellClass(nsIDOMElement cell, String className) {
		if (cell != null) {
			if (className != null && className.trim().length() > 0) {
				cell.setAttribute("class", className); //$NON-NLS-1$
			}
		}
	}
	
	private String getNodeAttrValue(Node node, String attrName) {
		if (node != null) {
			Node attr = node.getAttributes().getNamedItem(attrName);
			if (attr != null) {
				return attr.getNodeValue();
			}
		}
		return null;
	}
	
}

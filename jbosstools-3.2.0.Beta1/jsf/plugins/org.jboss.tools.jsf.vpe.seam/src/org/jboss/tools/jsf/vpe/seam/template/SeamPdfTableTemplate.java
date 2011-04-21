/******************************************************************************* 
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.vpe.seam.template;

/**
 * @author yzhishko
 */
import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.jsf.vpe.seam.template.util.SeamUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SeamPdfTableTemplate extends SeamPdfAbstractTemplate {

	private nsIDOMElement visualElement;
	private Element sourceElement;

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		sourceElement = (Element) sourceNode;
		nsIDOMNode visualNode = visualDocument.createElement(HTML.TAG_DIV);
		nsIDOMNode tableNode = visualDocument.createElement(HTML.TAG_TABLE);
		nsIDOMElement visualTable = queryInterface(tableNode, nsIDOMElement.class);
		visualNode.appendChild(tableNode);
		visualElement = queryInterface(visualNode, nsIDOMElement.class);
		visualTable.setAttribute(HTML.ATTR_WIDTH, getWidthPerc(sourceElement));
		visualTable.setAttribute(HTML.ATTR_ALIGN, getAlignment(sourceElement));
		visualTable.setAttribute(HTML.ATTR_CELLSPACING, "0px"); //$NON-NLS-1$
		VpeCreationData creationData = new VpeCreationData(visualElement);
		VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualElement);
		Node[] cells = SeamUtil.getChildsByName(pageContext, sourceNode,
				"p:cell"); //$NON-NLS-1$
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				Node parentNode = SeamUtil.getParentByName(pageContext,
						cells[i], "p:table"); //$NON-NLS-1$
				if (parentNode != null && parentNode == sourceNode) {
					childrenInfo.addSourceChild(cells[i]);
				}
			}
		}
		creationData.addChildrenInfo(childrenInfo);
		return creationData;
	}

	@Override
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data) {
		setColumns(pageContext, sourceNode, visualDocument, data);
	}

	private int getNumberOfColumns(Node sourceTableNode) {
		int columnsNumber = 1;
		Element sourceTableElement = (Element) sourceTableNode;
		String columnsAttrName = "columns"; //$NON-NLS-1$		
		if (sourceTableElement.hasAttribute(columnsAttrName)) {
			try {
				String columnsNumberString = sourceTableElement.getAttribute(columnsAttrName);
				columnsNumber = Integer.parseInt(columnsNumberString);
				if (columnsNumber < 1) {
					columnsNumber = 1;
				}
			} catch (NumberFormatException e) {
				columnsNumber = 1;
			}
		} else {
			columnsNumber = 1;
		}
		return columnsNumber;
	}

	private void setColumns(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data) {
		int numberOfColumns = getNumberOfColumns(sourceNode);
		nsIDOMNode visualTable = (queryInterface(data.getNode(), nsIDOMElement.class))
				.getElementsByTagName(HTML.TAG_TABLE).item(0);
		nsIDOMNode[] visualCells = getCells(data.getNode());
		if (visualCells == null) {
			return;
		}
		int cellsLength = visualCells.length;
		if (cellsLength == 0) {
			return;
		}
		nsIDOMNode trVisualNode = visualDocument.createElement(HTML.TAG_TR);
		visualTable.appendChild(trVisualNode);
		int posCounter = 0;
		for (int i = 0; i < cellsLength; i++) {
			nsIDOMNode visualCell = visualCells[i];
			nsIDOMNode parentNode = null;
			if (visualCell != null) {
				parentNode = visualCell.getParentNode();
			}
			if (parentNode != null) {
				parentNode.removeChild(visualCell);
				trVisualNode.appendChild(visualCell);
				int colspanValue = getColspanValue(visualCell);
				posCounter++;
				if (colspanValue > 1 && posCounter % numberOfColumns != 0) {
					int posInRow = posCounter - 1;
					int numEndCells = numberOfColumns - posInRow;
					if (numEndCells <= colspanValue) {
						colspanValue = numEndCells;
						trVisualNode = visualDocument
								.createElement(HTML.TAG_TR);
						visualTable.appendChild(trVisualNode);
						posCounter = 0;
						continue;
					} else {
						for (int j = 0; j < numberOfColumns - (colspanValue); j++) {
							i++;
							visualCell = visualCells[i];
							trVisualNode.appendChild(visualCell);
						}
						i -= numberOfColumns - (colspanValue);
						posCounter += colspanValue - 1;
					}
				}
			}
			if ((posCounter % numberOfColumns == 0) && ((i + 1) != cellsLength)) {
				trVisualNode = visualDocument.createElement(HTML.TAG_TR);
				visualTable.appendChild(trVisualNode);
				posCounter = 0;
			}
		}
		if (trVisualNode.getChildNodes().getLength() != numberOfColumns) {
			trVisualNode.getParentNode().removeChild(trVisualNode);
		}
	}

	private String getWidthPerc(Element sourceElement) {
		String width = "100%"; //$NON-NLS-1$
		if (sourceElement.hasAttribute(SeamUtil.SEAM_ATTR_WIDTH_PERCENTAGE)) {
			try {
				width = sourceElement.getAttribute(SeamUtil.SEAM_ATTR_WIDTH_PERCENTAGE);
				int intWidth = Integer.parseInt(width);
				if (intWidth < 1 || intWidth > 100) {
					width = "100%"; //$NON-NLS-1$
				} else {
					width = Integer.toString(intWidth) + "%"; //$NON-NLS-1$
				}
			} catch (NumberFormatException e) {
				width = "100%"; //$NON-NLS-1$
			}
		}
		return width;
	}

	private String getAlignment(Element sourceElement) {		
		if (sourceElement.hasAttribute(SeamUtil.SEAM_ATTR_HORIZONAL_ALIGNMENT)) {
			String align = sourceElement
				.getAttribute(SeamUtil.SEAM_ATTR_HORIZONAL_ALIGNMENT);
			for (int i = 0; i < SeamUtil.POSSIBLE_ALIGNS.length; i++) {
				if (SeamUtil.POSSIBLE_ALIGNS[i].equalsIgnoreCase(align)) {
					if (SeamUtil.POSSIBLE_ALIGNS[i]
							.equalsIgnoreCase("justifyall")) { //$NON-NLS-1$
						return HTML.VALUE_ALIGN_JUSTIFY;
					}
					return align;
				}
			}
		}
		return HTML.VALUE_ALIGN_CENTER;
	}

	private int getColspanValue(nsIDOMNode visualNode) {
		int colspan = 1;
		nsIDOMElement visualElement = queryInterface(visualNode, nsIDOMElement.class);		
		if (visualElement.hasAttribute(HTML.ATTR_COLSPAN)) {
			try {
				String colspanString = visualElement.getAttribute(HTML.ATTR_COLSPAN);
				colspan = Integer.parseInt(colspanString);
				if (colspan < 1) {
					colspan = 1;
				}
			} catch (NumberFormatException e) {
				colspan = 1;
			}
		}
		return colspan;
	}

	private nsIDOMNode[] getCells(nsIDOMNode visualTable) {
		nsIDOMNodeList children = visualTable.getChildNodes();
		if (children == null) {
			return null;
		}
		List<nsIDOMElement> childrenList = new ArrayList<nsIDOMElement>();
		for (int i = 0; i < children.getLength(); i++) {
			nsIDOMNode child = children.item(i);
			if (child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				if ("TD".equals(child.getNodeName())) { //$NON-NLS-1$
					childrenList.add(queryInterface(child, nsIDOMElement.class));
				}
			}
		}
		return childrenList.toArray(new nsIDOMElement[0]);
	}

}

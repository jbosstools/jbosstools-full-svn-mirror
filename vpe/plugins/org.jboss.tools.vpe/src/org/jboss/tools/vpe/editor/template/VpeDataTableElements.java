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

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class VpeDataTableElements {
//	private static final VpeDataTableElements INSTANCE = new VpeDataTableElements();
//
//	public static VpeDataTableElements getInstance() {
//		return INSTANCE;
//	}

	public static class SourceDataTableElements {
		private Node tableCaption;
		private Node tableHeader;
		private List<SourceColumnElements> columns;
		private List<Node> redundantTextNodes;
		private Node tableFooter;

		public SourceDataTableElements(Node dataTableNode) {
			init(dataTableNode);
		}

		private void init(Node dataTableNode) {
			NodeList list = dataTableNode.getChildNodes();
			int cnt = list != null ? list.getLength() : 0;
			if (cnt > 0) {
				for (int i = 0; i < cnt; i++) {
					Node node = list.item(i);
					
					if (node.getNodeType() == Node.TEXT_NODE) {
						if (null == redundantTextNodes) {
							redundantTextNodes = new ArrayList<Node>();
						}
						redundantTextNodes.add(node);
					}
					
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						boolean isColumn = node.getNodeName().indexOf(":column") > 0 || node.getNodeName().indexOf(":treeColumn") > 0; //$NON-NLS-1$ //$NON-NLS-2$
						boolean isFacet = !isColumn && node.getNodeName().indexOf(":facet") > 0; //$NON-NLS-1$
						Node attrName = node.getAttributes().getNamedItem("name"); //$NON-NLS-1$
						if (!isColumn && isFacet && attrName != null && "header".equals(attrName.getNodeValue())) { //$NON-NLS-1$
							tableHeader = node;
						} else if (!isColumn && isFacet && attrName != null && "footer".equals(attrName.getNodeValue())) { //$NON-NLS-1$
							tableFooter = node;
						} else if (!isColumn && isFacet && attrName != null && "caption".equals(attrName.getNodeValue())) { //$NON-NLS-1$
							tableCaption = node;
						} else if (isColumn) {
							if (columns == null) columns = new ArrayList<SourceColumnElements>();
							columns.add(new SourceColumnElements(node));
						}
					}
				}
			}
		}

		public Node getRedundantTextNode(int index) {
			if (redundantTextNodes != null && index < getRedundantTextNodesCount()) {
				return redundantTextNodes.get(index);
			}
			return null;
		}
		
		public int getRedundantTextNodesCount() {
			if (redundantTextNodes != null) {
				return redundantTextNodes.size();
			}
			return 0;
		}
		
		public SourceColumnElements getColumn(int index) {
			if (columns != null && index < getColumnCount()) return (SourceColumnElements)columns.get(index);
			return null;
		}

		public int getColumnCount() {
			if (columns != null) return columns.size();
			return 0;
		}

		public boolean hasColspan() {
			return getColumnCount() >= 2;
		}

		public boolean hasHeaderSection() {
			return tableHeader != null || hasColumnsHeader();
		}

		public boolean hasBodySection() {
			for (int i = 0; i < getColumnCount(); i++) {
				SourceColumnElements column = getColumn(i);
				if (column.hasBody()) return true;
			}
			return false;
		}

		public boolean hasFooterSection() {
			return tableFooter != null || hasColumnsFooter();
		}

		public boolean hasTableHeader() {
			return tableHeader != null;
		}

		public boolean hasTableFooter() {
			return tableFooter != null;
		}
		
		public boolean hasTableCaption() {
			return tableCaption != null;
		}

		public boolean hasColumnsHeader() {
			for (int i = 0; i < getColumnCount(); i++) {
				SourceColumnElements column = getColumn(i);
				if (column.hasHeader()) return true;
			}
			return false;
		}

		public boolean hasColumnsFooter() {
			for (int i = 0; i < getColumnCount(); i++) {
				SourceColumnElements column = getColumn(i);
				if (column.hasFooter()) return true;
			}
			return false;
		}

		public Node getTableFooter() {
			return tableFooter;
		}

		public void setTableFooter(Node tableFooter) {
			this.tableFooter = tableFooter;
		}

		public Node getTableHeader() {
			return tableHeader;
		}

		public void setTableHeader(Node tableHeader) {
			this.tableHeader = tableHeader;
		}
		
		public Node getTableCaption() {
			return tableCaption;
		}
		
		public void setTableCaption(Node tableCaption) {
			this.tableCaption = tableCaption;
		}
	}

	public static class SourceColumnElements {
		private Node column;
		private Node header;
		private List<Node> body;
		private Node footer;

		public SourceColumnElements(Node columnNode) {
			init(columnNode);
		}

		private void init(Node columnNode) {
			column = columnNode;
			NodeList list = columnNode.getChildNodes();
			int cnt = list != null ? list.getLength() : 0;
			if (cnt > 0) {
				for (int i = 0; i < cnt; i++) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						boolean isFacet = node.getNodeName().indexOf(":facet") > 0; //$NON-NLS-1$
						Node attrName = node.getAttributes().getNamedItem("name"); //$NON-NLS-1$
						if (isFacet && attrName != null && "header".equals(attrName.getNodeValue())) { //$NON-NLS-1$
							header = node;
						} else if (isFacet && attrName != null && "footer".equals(attrName.getNodeValue())) { //$NON-NLS-1$
							footer = node;
						} else {
							getBody().add(node);
						}
					}
				}
				if(!hasBody()) {
					Node text = VpeCreatorUtil.getTextChildNode(columnNode);
					if(text!=null) {
						getBody().add(text);
					}
				}
			}
		}

		public boolean hasHeader() {
			return header != null;
		}

		public boolean hasBody() {
			return body != null && body.size() > 0;
		}

		public List<Node> getBody() {
			if (body == null)
				body = new ArrayList<Node>();
			
			return body;
		}

		public void setBody(List<Node> body) {
			this.body = body;
		}

		public Node getFooter() {
			return footer;
		}

		public void setFooter(Node footer) {
			this.footer = footer;
		}

		public Node getHeader() {
			return header;
		}

		public void setHeader(Node header) {
			this.header = header;
		}

		public void setColumn(Node column) {
			this.column = column;
		}

		public boolean hasFooter() {
			return footer != null;
		}

		public int getBodyElementsCount() {
			if (body != null) return body.size();
			return 0;
		}

		public Node getBodyElement(int index) {
			if (body != null) return (Node)body.get(index);
			return null;
		}

		public Node getColumn() {
			return column;
		}
	}

	public static class VisualDataTableElements {
		private nsIDOMElement caption;

		private nsIDOMElement tableHeader;
		private nsIDOMElement tableHeaderRow;
		
		private nsIDOMElement columnsHeader;
		private nsIDOMElement columnsHeaderRow;

		private nsIDOMElement body;
		private nsIDOMElement bodyRow;
		
		private nsIDOMElement contentTableBodyRow;

		private nsIDOMElement tableFooter;
		private nsIDOMElement tableFooterRow;

		private nsIDOMElement columnsFooter;
		private nsIDOMElement columnsFooterRow;

		public VisualDataTableElements() {
		}
		
		public VisualDataTableElements(nsIDOMElement caption, nsIDOMElement columnsHeader, nsIDOMElement columnsFooter, nsIDOMElement header, nsIDOMElement body, nsIDOMElement footer) {
			this.caption = caption;
			this.columnsHeader = columnsHeader;
			this.columnsFooter = columnsFooter;
			this.tableHeader = header;
			this.body = body;
			this.tableFooter = footer;
		}
		
		public nsIDOMElement getBody() {
			return body;
		}

		public void setBody(nsIDOMElement body) {
			this.body = body;
		}

		public nsIDOMElement getBodyRow() {
			return bodyRow;
		}

		public void setBodyRow(nsIDOMElement bodyRow) {
			this.bodyRow = bodyRow;
		}
		
		public nsIDOMElement getContentTableBodyRow() {
			return contentTableBodyRow;
		}
		
		public void setContentTableBodyRow(nsIDOMElement contentTableBodyRow) {
			this.contentTableBodyRow = contentTableBodyRow;
		}

		public nsIDOMElement getColumnsFooterRow() {
			return columnsFooterRow;
		}

		public void setColumnsFooterRow(nsIDOMElement columnsFooterRow) {
			this.columnsFooterRow = columnsFooterRow;
		}

		public nsIDOMElement getColumnsHeaderRow() {
			return columnsHeaderRow;
		}

		public void setColumnsHeaderRow(nsIDOMElement columnsHeaderRow) {
			this.columnsHeaderRow = columnsHeaderRow;
		}

		public nsIDOMElement getTableFooter() {
			return tableFooter;
		}
		
		public void setTableFooter(nsIDOMElement footer) {
			this.tableFooter = footer;
		}
		
		public nsIDOMElement getTableHeader() {
			return tableHeader;
		}
		
		public void setTableHeader(nsIDOMElement header) {
			this.tableHeader = header;
		}
		
		public nsIDOMElement getColumnsFooter() {
			return columnsFooter;
		}

		public void setColumnsFooter(nsIDOMElement footer) {
			this.columnsFooter = footer;
		}

		public nsIDOMElement getColumnsHeader() {
			return columnsHeader;
		}

		public void setColumnsHeader(nsIDOMElement header) {
			this.columnsHeader = header;
		}
		
		public nsIDOMElement getCaption() {
			return caption;
		}

		public void setCaption(nsIDOMElement caption) {
			this.caption = caption;
		}

		public nsIDOMElement getTableFooterRow() {
			return tableFooterRow;
		}

		public void setTableFooterRow(nsIDOMElement tableFooterRow) {
			this.tableFooterRow = tableFooterRow;
		}

		public nsIDOMElement getTableHeaderRow() {
			return tableHeaderRow;
		}

		public void setTableHeaderRow(nsIDOMElement tableHeaderRow) {
			this.tableHeaderRow = tableHeaderRow;
		}
	}

	public static class VisualColumnElements {
		private nsIDOMElement headerCell;
		private nsIDOMElement bodyCell;
		private nsIDOMElement footerCell;

//		private boolean isEmpty() {
//			return headerCell == null && bodyCell == null && footerCell == null;
//		}

		public nsIDOMElement getBodyCell() {
			return bodyCell;
		}

		public void setBodyCell(nsIDOMElement bodyCell) {
			this.bodyCell = bodyCell;
		}

		public nsIDOMElement getFooterCell() {
			return footerCell;
		}

		public void setFooterCell(nsIDOMElement footerCell) {
			this.footerCell = footerCell;
		}

		public nsIDOMElement getHeaderCell() {
			return headerCell;
		}

		public void setHeaderCell(nsIDOMElement headerCell) {
			this.headerCell = headerCell;
		}
	}

	public static nsIDOMElement getNamedChild(nsIDOMNode visualParent, String tagName) {
		return getNamedChild(visualParent, tagName, 0);
	}
	public static nsIDOMElement getNamedChild(nsIDOMNode visualParent, String tagName, int index) {
		if (visualParent != null) {
			int ind = 0;
			nsIDOMNodeList children = visualParent.getChildNodes();
			long count = children != null ? children.getLength() : 0;
			for (long i = 0; i < count; i++) {
				nsIDOMNode child = children.item(i);
				if (tagName.equalsIgnoreCase(child.getNodeName())) {
					if (ind == index) {
						return (nsIDOMElement)child.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
					} else {
						ind++;
					}
				}
			}
		}
		return null;
	}

	public static VisualDataTableElements getVisualDataTableElements(nsIDOMNode visualParent) {
		VisualDataTableElements visualDataTableElements = new VisualDataTableElements(
					VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_CAPTION),
					VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_THEAD),
					VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_TFOOT),
					VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_THEAD),
					VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_TBODY),
					VpeDataTableElements.getNamedChild(visualParent, HTML.TAG_TFOOT)
			);

		visualDataTableElements.setCaption(VpeDataTableElements.getNamedChild(visualDataTableElements.getCaption(), "caption")); //$NON-NLS-1$
		visualDataTableElements.setTableHeaderRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getTableHeader(), "tr")); //$NON-NLS-1$
		visualDataTableElements.setColumnsHeaderRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getColumnsHeader(), "tr")); //$NON-NLS-1$
		visualDataTableElements.setBodyRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getBody(), "tr")); //$NON-NLS-1$
		visualDataTableElements.setColumnsFooterRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getColumnsFooter(), "tr")); //$NON-NLS-1$
		visualDataTableElements.setTableFooterRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getTableFooter(), "tr")); //$NON-NLS-1$

		return visualDataTableElements;
	}

	public static nsIDOMElement makeCell(nsIDOMNode row, int index, String cellTag, nsIDOMDocument visualDocument) {
		nsIDOMElement visualCell = null;
		if (visualDocument != null && row != null) {
			visualCell = visualDocument.createElement(cellTag);
			if (index >= row.getChildNodes().getLength()) {
				row.appendChild(visualCell);
			} else {
				row.insertBefore(visualCell, row.getChildNodes().item(index));
			}
		}
		return visualCell;
	}
}
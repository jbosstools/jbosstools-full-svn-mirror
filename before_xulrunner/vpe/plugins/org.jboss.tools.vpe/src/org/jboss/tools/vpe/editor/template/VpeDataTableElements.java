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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class VpeDataTableElements {
//	private static final VpeDataTableElements INSTANCE = new VpeDataTableElements();
//
//	public static VpeDataTableElements getInstance() {
//		return INSTANCE;
//	}

	public static class SourceDataTableElements {
		private Node tableHeader;
		private List columns;
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
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						boolean isColumn = node.getNodeName().indexOf(":column") > 0 || node.getNodeName().indexOf(":treeColumn") > 0;
						boolean isFacet = !isColumn && node.getNodeName().indexOf(":facet") > 0;
						Node attrName = node.getAttributes().getNamedItem("name");
						if (!isColumn && isFacet && attrName != null && "header".equals(attrName.getNodeValue())) {
							tableHeader = node;
						} else if (!isColumn && isFacet && attrName != null && "footer".equals(attrName.getNodeValue())) {
							tableFooter = node;
						} else if (isColumn) {
							if (columns == null) columns = new ArrayList();
							columns.add(new SourceColumnElements(node));
						}
					}
				}
			}
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
	}

	public static class SourceColumnElements {
		private Node column;
		private Node header;
		private List body;
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
						boolean isFacet = node.getNodeName().indexOf(":facet") > 0;
						Node attrName = node.getAttributes().getNamedItem("name");
						if (isFacet && attrName != null && "header".equals(attrName.getNodeValue())) {
							header = node;
						} else if (isFacet && attrName != null && "footer".equals(attrName.getNodeValue())) {
							footer = node;
						} else {
							if (body == null) body = new ArrayList();
							body.add(node);
						}
					}
				}
				if(!hasBody()) {
					Node text = VpeCreatorUtil.getTextChildNode(columnNode);
					if(text!=null) {
						if (body == null) body = new ArrayList();
						body.add(text);
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

		public List getBody() {
			return body;
		}

		public void setBody(List body) {
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
		private Element header;
		private Element tableHeaderRow;
//		private Element tableHeaderCell;
		private Element columnsHeaderRow;

		private Element body;
		private Element bodyRow;

		private Element footer;
		private Element columnsFooterRow;
//		private Element tableFooterCell;
		private Element tableFooterRow;

		private List columns;

		public VisualDataTableElements() {
		}
		public VisualDataTableElements(Element header, Element body, Element footer) {
			this.header = header;
			this.body = body;
			this.footer = footer;
		}
		
		private VisualColumnElements getColumn(int index) {
			if (columns != null && index < getColumnCount()) return (VisualColumnElements)columns.get(index);
			return null;
		}

		private int getColumnCount() {
			if (columns != null) return columns.size();
			return 0;
		}

		private List getColumns() {
			if (columns == null) columns = new ArrayList();
			return columns;
		}

		public Element getBody() {
			return body;
		}

		public void setBody(Element body) {
			this.body = body;
		}

		public Element getBodyRow() {
			return bodyRow;
		}

		public void setBodyRow(Element bodyRow) {
			this.bodyRow = bodyRow;
		}

		public Element getColumnsFooterRow() {
			return columnsFooterRow;
		}

		public void setColumnsFooterRow(Element columnsFooterRow) {
			this.columnsFooterRow = columnsFooterRow;
		}

		public Element getColumnsHeaderRow() {
			return columnsHeaderRow;
		}

		public void setColumnsHeaderRow(Element columnsHeaderRow) {
			this.columnsHeaderRow = columnsHeaderRow;
		}

		public Element getFooter() {
			return footer;
		}

		public void setFooter(Element footer) {
			this.footer = footer;
		}

		public Element getHeader() {
			return header;
		}

		public void setHeader(Element header) {
			this.header = header;
		}

		public Element getTableFooterRow() {
			return tableFooterRow;
		}

		public void setTableFooterRow(Element tableFooterRow) {
			this.tableFooterRow = tableFooterRow;
		}

		public Element getTableHeaderRow() {
			return tableHeaderRow;
		}

		public void setTableHeaderRow(Element tableHeaderRow) {
			this.tableHeaderRow = tableHeaderRow;
		}
	}

	public static class VisualColumnElements {
		private Element headerCell;
		private Element bodyCell;
		private Element footerCell;

		private boolean isEmpty() {
			return headerCell == null && bodyCell == null && footerCell == null;
		}

		public Element getBodyCell() {
			return bodyCell;
		}

		public void setBodyCell(Element bodyCell) {
			this.bodyCell = bodyCell;
		}

		public Element getFooterCell() {
			return footerCell;
		}

		public void setFooterCell(Element footerCell) {
			this.footerCell = footerCell;
		}

		public Element getHeaderCell() {
			return headerCell;
		}

		public void setHeaderCell(Element headerCell) {
			this.headerCell = headerCell;
		}
	}

	public static Element getNamedChild(Node visualParent, String tagName) {
		return getNamedChild(visualParent, tagName, 0);
	}
	public static Element getNamedChild(Node visualParent, String tagName, int index) {
		if (visualParent != null) {
			int ind = 0;
			NodeList children = visualParent.getChildNodes();
			int count = children != null ? children.getLength() : 0;
			for (int i = 0; i < count; i++) {
				Node child = children.item(i);
				if (tagName.equalsIgnoreCase(child.getNodeName())) {
					if (ind == index) {
						return (Element)child;
					} else {
						ind++;
					}
				}
			}
		}
		return null;
	}

	public static VisualDataTableElements getVisualDataTableElements(Node visualParent) {
		VisualDataTableElements visualDataTableElements = new VisualDataTableElements(
					VpeDataTableElements.getNamedChild(visualParent, "thead"),
					VpeDataTableElements.getNamedChild(visualParent, "tbody"),
					VpeDataTableElements.getNamedChild(visualParent, "tfoot")
			);

		visualDataTableElements.setTableHeaderRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getHeader(), "tr"));
		visualDataTableElements.setColumnsHeaderRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getHeader(), "tr", 1));
		visualDataTableElements.setBodyRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getBody(), "tr"));
		visualDataTableElements.setColumnsFooterRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getFooter(), "tr"));
		visualDataTableElements.setTableFooterRow(VpeDataTableElements.getNamedChild(visualDataTableElements.getFooter(), "tr", 1));

		return visualDataTableElements;
	}

	public static Element makeCell(Node row, int index, String cellTag, Document visualDocument) {
		Element visualCell = null;
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
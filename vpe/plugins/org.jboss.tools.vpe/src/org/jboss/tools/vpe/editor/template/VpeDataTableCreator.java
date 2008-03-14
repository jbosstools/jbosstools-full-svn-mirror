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
import java.util.Map;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceDataTableElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualDataTableElements;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMHTMLTableCellElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeDataTableCreator extends VpeAbstractCreator {
	private boolean caseSensitive;

	private VpeExpression headerClassExpr;
	private VpeExpression footerClassExpr;
	private VpeExpression rowClassesExpr;
	private VpeExpression columnClassesExpr;
	
	private static final String ATTR_CAPTION_STYLE = "captionStyle";
	private static final String ATTR_CAPTION_CLASS = "captionClass";
	private static final String ATTR_STYLE = "style";
	private static final String ATTR_CLASS = "class";
	private static final String ATTR_WIDTH = "width";
	private static final String ATTR_BORDER = "border";
	private static final String ATTR_RULES = "rules";
	private static final String ATTR_RULES_VALUE_ROWS = "rows";
    private static final String TD_HIDDEN_BORDER_STYLE = "padding: 0px; border: 0px hidden;";
    private static final String TD_RULES_ROWS_BORDER_STYLE = "padding: 0px;";

	private List propertyCreators;

	VpeDataTableCreator(Element gridElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(gridElement, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr headerClassAttr = element.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS);
		if (headerClassAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(headerClassAttr.getValue(), caseSensitive);
				headerClassExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr footerClassAttr = element.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS);
		if (footerClassAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(footerClassAttr.getValue(), caseSensitive);
				footerClassExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr rowClassesAttr = element.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES);
		if (rowClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(rowClassesAttr.getValue(), caseSensitive);
				rowClassesExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr columnClassesAttr = element.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES);
		if (columnClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(columnClassesAttr.getValue(), caseSensitive);
				columnClassesExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		if (VpeTemplateManager.ATTR_DATATABLE_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_DATATABLE_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_DATATABLE_PROPERTIES[i];
				Attr attr = element.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null) propertyCreators  = new ArrayList();
					propertyCreators.add(new VpeAttributeCreator(attrName, attr.getValue(), dependencyMap, caseSensitive));
				}
			}
		}
		
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {

		SourceDataTableElements sourceElements = new SourceDataTableElements(sourceNode);
		VisualDataTableElements visualElements = new VisualDataTableElements();

		// Table with caption, header, footer, 
		// that wraps table with content
		nsIDOMElement outterTable = visualDocument.createElement(HTML.TAG_TABLE);
		// Table with main content
		nsIDOMElement visualTable = visualDocument.createElement(HTML.TAG_TABLE);
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(outterTable);
		nsIDOMElement section = null, row = null, cell = null;
		nsIDOMElement caption;

		if (true || sourceElements.hasTableCaption()) {
			caption = visualDocument.createElement(HTML.TAG_CAPTION);
			if (sourceElements.getTableCaption() != null) {
				VpeChildrenInfo info = new VpeChildrenInfo(caption);
				info.addSourceChild(sourceElements.getTableCaption());
				creatorInfo.addChildrenInfo(info);
			}
			
			// Everything concerning table caption
			// lies here (was removed from VpeFacetCreator)
			Node attr = sourceNode.getAttributes().getNamedItem(ATTR_CAPTION_STYLE);
			if (attr != null) {
				caption.setAttribute(ATTR_STYLE, attr.getNodeValue());
			}
			attr = sourceNode.getAttributes().getNamedItem(ATTR_CAPTION_CLASS);
			if (attr != null) {
				caption.setAttribute(ATTR_CLASS, attr.getNodeValue());
			}
			outterTable.appendChild(caption);
			visualElements.setCaption(caption);
		}

		if (true || sourceElements.hasTableHeader()) {
			section = visualDocument.createElement(HTML.TAG_THEAD);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setTableHeaderRow(row);
			if (sourceElements.getTableHeader() != null) {
				VpeChildrenInfo info = new VpeChildrenInfo(row);
				info.addSourceChild(sourceElements.getTableHeader());
				creatorInfo.addChildrenInfo(info);
			}
			outterTable.appendChild(section);
			visualElements.setTableHeader(section);
		}

		if (true || sourceElements.hasTableFooter()) {
			section = visualDocument.createElement(HTML.TAG_TFOOT);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setTableFooterRow(row);
			if (sourceElements.getTableFooter() != null) {
				VpeChildrenInfo info = new VpeChildrenInfo(row);
				info.addSourceChild(sourceElements.getTableFooter());
				creatorInfo.addChildrenInfo(info);
			}
			outterTable.appendChild(section);
			visualElements.setTableFooter(section);
		}

		if (true || sourceElements.hasColumnsHeader()) {
			section = visualDocument.createElement(HTML.TAG_THEAD);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setColumnsHeaderRow(row);
			visualTable.appendChild(section);
			visualElements.setColumnsHeader(section);
		}

		if (true || sourceElements.hasColumnsFooter()) {
			section = visualDocument.createElement(HTML.TAG_TFOOT);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setColumnsFooterRow(row);
			visualTable.appendChild(section);
			visualElements.setColumnsFooter(section);
		}

		if (true || sourceElements.hasBodySection()) {
			section = visualDocument.createElement(HTML.TAG_TBODY);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualTable.appendChild(section);
			visualElements.setContentTableBodyRow(row);
			//visualElements.setBody(section);
		}

		VpeChildrenInfo info = null;
		if (sourceElements.getColumnCount() > 0) {
			nsIDOMElement group = visualDocument.createElement(HTML.TAG_COLGROUP);
			visualTable.appendChild(group);
			info = new VpeChildrenInfo(group);
			creatorInfo.addChildrenInfo(info);
		}
		
		for (int i = 0; i < sourceElements.getColumnCount(); i++) {
			SourceColumnElements column = sourceElements.getColumn(i);
			info.addSourceChild(column.getColumn());
		}

		nsIDOMElement outterTBODY = visualDocument.createElement(HTML.TAG_TBODY);
		nsIDOMElement outterTR = visualDocument.createElement(HTML.TAG_TR);
		nsIDOMElement outterTD = visualDocument.createElement(HTML.TAG_TD);

		// To create appropriate visual appearance
		// borders of the body cell and content table
		// were set via styles.
		outterTD.setAttribute(ATTR_STYLE, TD_HIDDEN_BORDER_STYLE);
		visualTable.setAttribute(ATTR_WIDTH, "100%");
		visualTable.setAttribute(ATTR_BORDER, "0");
		
		outterTD.appendChild(visualTable);
		outterTR.appendChild(outterTD);
		outterTBODY.appendChild(outterTR);
		outterTable.appendChild(outterTBODY);
		
		visualElements.setBodyRow(outterTR);
		visualElements.setBody(outterTBODY);

		Object[] elements = new Object[2];
		elements[0] = visualElements;
		elements[1] = sourceElements;
		visualNodeMap.put(this, elements);

		for (int i = 0; i < propertyCreators.size(); i++) {
			VpeCreator creator = (VpeCreator)propertyCreators.get(i);
			if (creator != null) {
				// Sets attributes for the wrapper table
				VpeCreatorInfo info1 = creator.create(pageContext, (Element) sourceNode, visualDocument, outterTable, visualNodeMap);
				if (info1 != null && info1.getVisualNode() != null) {
					nsIDOMAttr attr = (nsIDOMAttr) info1.getVisualNode();
					// Fixes creation 'border="1"' 
					// when setting border attribute to the table.
					// Also skips empty attributes to fix layout problems.
					if (null == attr.getNodeValue() || "".equalsIgnoreCase(attr.getNodeValue())) {
						continue;
					}
					outterTable.setAttributeNode(attr);
				}
				// Sets attributes for the content table
				VpeCreatorInfo info2 = creator.create(pageContext, (Element) sourceNode, visualDocument, visualTable, visualNodeMap);
				if (info2 != null && info2.getVisualNode() != null) {
					nsIDOMAttr attr = (nsIDOMAttr) info2.getVisualNode();
					// Fixes creation 'border="1"' 
					// when setting border attribute to the table.
					// Also skips empty attributes to fix layout problems.
					if (null == attr.getNodeValue() || "".equalsIgnoreCase(attr.getNodeValue())) {
						continue;
					}
					// Setting row classes to the table row 
					if (VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES.equalsIgnoreCase(attr.getNodeName())) {
						setRowClass(visualElements.getContentTableBodyRow(), attr.getNodeValue());
						continue;
					}
					// Skip setting content table border
					if (ATTR_BORDER.equalsIgnoreCase(attr.getNodeName())) {
						continue;
					}
					// Fixes creation of a border around content table
					// when attribute rules="rows" is set
					if (ATTR_RULES.equalsIgnoreCase(attr.getNodeName())) {
						if (ATTR_RULES_VALUE_ROWS.equalsIgnoreCase(attr.getNodeValue())) {
							outterTD.setAttribute(ATTR_STYLE, TD_RULES_ROWS_BORDER_STYLE);
						}
					}
					visualTable.setAttributeNode(attr);
				}
			}
		}
		return creatorInfo;
	}
	
	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		VisualDataTableElements visualElements = getVisualDataTableElements(visualNodeMap);
		if (visualElements != null) {
			if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS.equals(name) : VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getTableHeaderRow(), value);
				setCellsClass(visualElements.getColumnsHeaderRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS.equals(name) : VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getColumnsFooterRow(), value);
				setCellsClass(visualElements.getTableFooterRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES.equals(name) : VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES.equalsIgnoreCase(name)) {
				setRowClass(visualElements.getContentTableBodyRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES.equals(name) : VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getContentTableBodyRow(), value);
			}
		}
	}

	public void validate(VpePageContext pageContext, Element sourceElement, Document visualDocument, Element visualParent, Element visualElement, Map visualNodeMap) {
		VisualDataTableElements visualElements = null;
		SourceDataTableElements sourceElements = null;
		if (visualNodeMap != null) {
			visualElements = getVisualDataTableElements(visualNodeMap);
			sourceElements = getSourceDataTableElements(visualNodeMap);
		} else if (sourceElement != null) {
			sourceElements = new SourceDataTableElements(sourceElement);
			nsIDOMNode visualNode = pageContext.getCurrentVisualNode();
			if (visualNode != null) {
				visualElements = VpeDataTableElements.getVisualDataTableElements(visualNode);
			}
		}
		if (visualElements != null) {
			setCellsClass(visualElements.getTableHeaderRow(), sourceElement.getAttribute(VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS));
			setCellsClass(visualElements.getColumnsHeaderRow(), sourceElement.getAttribute(VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS));
			setCellsClass(visualElements.getColumnsFooterRow(), sourceElement.getAttribute(VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS));
			setCellsClass(visualElements.getTableFooterRow(), sourceElement.getAttribute(VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS));

			setRowClass(visualElements.getBodyRow(), sourceElement.getAttribute(VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES));
			setCellsClass(visualElements.getBodyRow(), sourceElement.getAttribute(VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES));
		}
		if (sourceElements != null && visualElements != null) {
			setRowDisplayStyle(visualElements.getTableHeaderRow(), sourceElements.hasTableHeader());
			setRowDisplayStyle(visualElements.getColumnsHeaderRow(), sourceElements.hasColumnsHeader());
			setRowDisplayStyle(visualElements.getBodyRow(), sourceElements.hasBodySection());
			setRowDisplayStyle(visualElements.getColumnsFooterRow(), sourceElements.hasColumnsFooter());
			setRowDisplayStyle(visualElements.getTableFooterRow(), sourceElements.hasTableFooter());
		}
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		VisualDataTableElements visualElements = getVisualDataTableElements(visualNodeMap);
		if (visualElements != null) {
			if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS.equals(name) : VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS.equalsIgnoreCase(name)) {
				removeCellsClass(visualElements.getTableHeaderRow());
				removeCellsClass(visualElements.getColumnsHeaderRow());
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS.equals(name) : VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS.equalsIgnoreCase(name)) {
				removeCellsClass(visualElements.getColumnsFooterRow());
				removeCellsClass(visualElements.getTableFooterRow());
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES.equals(name) : VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES.equalsIgnoreCase(name)) {
				removeRowClass(visualElements.getBodyRow());
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES.equals(name) : VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES.equalsIgnoreCase(name)) {
				removeCellsClass(visualElements.getBodyRow());
			}
		}
	}

	private String[] getClasses(String value) {
		if (value != null) {
			return value.split(",");
		}
		return null;
	}

	private void setCellsClass(nsIDOMElement row, String value) {
		if (row != null && value != null) {
			String[] classes = getClasses(value);
			int ind = 0;

			nsIDOMNodeList children = row.getChildNodes();
			long count = children != null ? children.getLength() : 0;
			for (long i = 0; i < count; i++) {
				nsIDOMNode child = children.item(i);
				if (child != null && child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
					try {
						nsIDOMHTMLTableCellElement cell = (nsIDOMHTMLTableCellElement) child
							.queryInterface(nsIDOMHTMLTableCellElement.NS_IDOMHTMLTABLECELLELEMENT_IID);						
						cell.setAttribute(ATTR_CLASS, classes[ind]);
						ind = ind < (classes.length - 1) ? ind + 1 : 0;
					} catch (XPCOMException ex) {
					    // just ignore this exception
					}
				}
			}
		}
	}

	private void removeCellsClass(nsIDOMElement row) {
		if (row != null) {
			nsIDOMNodeList children = row.getChildNodes();
			long count = children != null ? children.getLength() : 0;
			for (long i = 0; i < count; i++) {
				nsIDOMNode child = children.item(i);
				if (child != null && child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
					try {
						nsIDOMHTMLTableCellElement cell = (nsIDOMHTMLTableCellElement) child
							.queryInterface(nsIDOMHTMLTableCellElement.NS_IDOMHTMLTABLECELLELEMENT_IID);
						cell.removeAttribute(ATTR_CLASS);
					} catch (XPCOMException ex) {
					    // just ignore this exception
					}
				}
			}
		}
	}

	private void setRowClass(nsIDOMElement row, String value) {
		if (row != null && value != null) {
			String[] rowClasses = getClasses(value);
			String rowClass = (rowClasses != null && rowClasses.length > 0) ? rowClasses[0] : null;
			if (rowClass.trim().length() > 0) {
				row.setAttribute(ATTR_CLASS, rowClass);
			} else {
				row.removeAttribute(ATTR_CLASS);
			}
		}
	}

	private void setRowDisplayStyle(nsIDOMElement row, boolean visible) {
		if (row != null) {
			row.setAttribute(ATTR_STYLE, "display:" + (visible ? "" : "none"));
		}
	}

	private void removeRowClass(nsIDOMElement row) {
		if (row != null) {
			row.removeAttribute(ATTR_CLASS);
		}
	}

	private VisualDataTableElements getVisualDataTableElements(Map visualNodeMap) {
		if (visualNodeMap != null) {
			Object o = visualNodeMap.get(this);
			try {
				if (o != null && o instanceof Object[] && ((Object[])o)[0] instanceof VisualDataTableElements) {
					return (VisualDataTableElements)((Object[])o)[0];
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	private SourceDataTableElements getSourceDataTableElements(Map visualNodeMap) {
		if (visualNodeMap != null) {
			Object o = visualNodeMap.get(this);
			try {
				if (o != null && o instanceof Object[] && ((Object[])o)[1] instanceof SourceDataTableElements) {
					return (SourceDataTableElements)((Object[])o)[1];
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

}

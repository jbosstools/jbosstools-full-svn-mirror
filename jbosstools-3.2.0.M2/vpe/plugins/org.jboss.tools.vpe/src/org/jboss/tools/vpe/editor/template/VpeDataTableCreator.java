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

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceDataTableElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualDataTableElements;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMHTMLTableCellElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeDataTableCreator extends VpeAbstractCreator {
	private boolean caseSensitive;

	private final static String EMPTY = ""; //$NON-NLS-1$
	private final static String NONE = "none"; //$NON-NLS-1$
	private final static String DISPLAY_STYLE_NAME = "display:"; //$NON-NLS-1$
	private final static String ZERO = "0"; //$NON-NLS-1$
	private final static String ONE = "1"; //$NON-NLS-1$
	private final static String HUNDRED_PERCENTS = "100%"; //$NON-NLS-1$

	private final static String ATTR_CAPTION_STYLE = "captionStyle"; //$NON-NLS-1$
	private final static String ATTR_CAPTION_CLASS = "captionClass"; //$NON-NLS-1$
	private final static String ATTR_HEADER_CLASS = "headerClass"; //$NON-NLS-1$
	private final static String ATTR_FOOTER_CLASS = "footerClass"; //$NON-NLS-1$
	private final static String ATTR_RULES = "rules"; //$NON-NLS-1$
	private final static String ATTR_RULES_VALUE_ROWS = "rows"; //$NON-NLS-1$
	private final static String TD_HIDDEN_BORDER_STYLE = "padding: 0px; border: 0px hidden;"; //$NON-NLS-1$
	private final static String TD_RULES_ROWS_BORDER_STYLE = "padding: 0px;"; //$NON-NLS-1$
	private final static String RULES_HIDDEN_BORDER_STYLE = "border: 0px hidden;"; //$NON-NLS-1$

	private List<VpeCreator> propertyCreators;

	VpeDataTableCreator(Element gridElement, VpeDependencyMap dependencyMap,
			boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(gridElement, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr headerClassAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS);
		if (headerClassAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(headerClassAttr.getValue(),
								caseSensitive);
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr footerClassAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS);
		if (footerClassAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(footerClassAttr.getValue(),
								caseSensitive);
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr rowClassesAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES);
		if (rowClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(rowClassesAttr.getValue(),
								caseSensitive);
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr columnClassesAttr = element
				.getAttributeNode(VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES);
		if (columnClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(columnClassesAttr.getValue(),
								caseSensitive);
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		if (VpeTemplateManager.ATTR_DATATABLE_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_DATATABLE_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_DATATABLE_PROPERTIES[i];
				Attr attr = element.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null)
						propertyCreators = new ArrayList<VpeCreator>();
					propertyCreators.add(new VpeAttributeCreator(attrName, attr
							.getValue(), dependencyMap, caseSensitive));
				}
			}
		}

	}

	@Override
	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Map visualNodeMap) throws VpeExpressionException {

		SourceDataTableElements sourceElements = new SourceDataTableElements(
				sourceNode);
		VisualDataTableElements visualElements = new VisualDataTableElements();

		Element element = (Element) sourceNode;
		
		/*
		 * Fixes http://jira.jboss.com/jira/browse/JBIDE-2001 Selection borders
		 * are fixed.
		 */
		nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		nsIDOMElement selectionTable = visualDocument
				.createElement(HTML.TAG_TABLE);
		nsIDOMElement tr = visualDocument.createElement(HTML.TAG_TR);
		nsIDOMElement td = visualDocument.createElement(HTML.TAG_TD);

		td.appendChild(div);
		tr.appendChild(td);
		selectionTable.appendChild(tr);

		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(selectionTable);

		/*
		 * Table with caption, header, footer, that wraps table with content
		 */
		nsIDOMElement outterTable = visualDocument
				.createElement(HTML.TAG_TABLE);

		/*
		 * Table with main content
		 */
		nsIDOMElement visualTable = visualDocument
				.createElement(HTML.TAG_TABLE);
		nsIDOMElement section = null;
		nsIDOMElement row = null;
		nsIDOMElement caption = null;

// Fix https://jira.jboss.org/jira/browse/JBIDE-3223
//		/*
//		 * Fixes http://jira.jboss.com/jira/browse/JBIDE-1944 author: Denis
//		 * Maliarevich Any text which is placed outside of the tags will be
//		 * displayed above the table.
//		 */
//		String redundantText = REDUNDANT_TEXT_SEPARATOR;
//		for (int i = 0; i < sourceElements.getRedundantTextNodesCount(); i++) {
//			Node node = sourceElements.getRedundantTextNode(i);
//			redundantText += node.getNodeValue();
//			redundantText += REDUNDANT_TEXT_SEPARATOR;
//		}
//		div.appendChild(visualDocument.createTextNode(redundantText));
		div.appendChild(outterTable);

		if (sourceElements.hasTableCaption()) {
			caption = visualDocument.createElement(HTML.TAG_CAPTION);
			if (sourceElements.getTableCaption() != null) {
				VpeChildrenInfo info = new VpeChildrenInfo(caption);
				info.addSourceChild(sourceElements.getTableCaption());
				creatorInfo.addChildrenInfo(info);
			}

			/*
			 * Everything concerning table caption lies here (was removed from
			 * VpeFacetCreator)
			 */
			Node attr = sourceNode.getAttributes().getNamedItem(
					ATTR_CAPTION_STYLE);
			if (attr != null) {
				caption.setAttribute(HTML.ATTR_STYLE, attr.getNodeValue());
			}
			attr = sourceNode.getAttributes().getNamedItem(ATTR_CAPTION_CLASS);
			if (attr != null) {
				caption.setAttribute(HTML.ATTR_CLASS, attr.getNodeValue());
			}
			outterTable.appendChild(caption);
			visualElements.setCaption(caption);
		}

		if (sourceElements.hasTableHeader()) {
			section = visualDocument.createElement(HTML.TAG_THEAD);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setTableHeaderRow(row);

			nsIDOMElement thHeader = visualDocument.createElement(HTML.TAG_TH);
			thHeader.setAttribute(HTML.ATTR_CLASS, element
					.getAttribute(ATTR_HEADER_CLASS));
			row.appendChild(thHeader);
			if (sourceElements.getTableHeader() != null) {

				VpeChildrenInfo info = new VpeChildrenInfo(thHeader);
				info.addSourceChild(sourceElements.getTableHeader());
				creatorInfo.addChildrenInfo(info);
			}
			outterTable.appendChild(section);
			visualElements.setTableHeader(section);
		}

		if (sourceElements.hasTableFooter()) {
			section = visualDocument.createElement(HTML.TAG_TFOOT);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			nsIDOMElement tdFooter = visualDocument.createElement(HTML.TAG_TD);
			tdFooter.setAttribute(HTML.ATTR_CLASS, element
					.getAttribute(ATTR_FOOTER_CLASS));
			row.appendChild(tdFooter);
			visualElements.setTableFooterRow(row);
			if (sourceElements.getTableFooter() != null) {
				VpeChildrenInfo info = new VpeChildrenInfo(tdFooter);
				info.addSourceChild(sourceElements.getTableFooter());
				creatorInfo.addChildrenInfo(info);
			}
			outterTable.appendChild(section);
			visualElements.setTableFooter(section);
		}

		if (sourceElements.hasColumnsHeader()) {
			section = visualDocument.createElement(HTML.TAG_THEAD);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setColumnsHeaderRow(row);
			visualTable.appendChild(section);
			visualElements.setColumnsHeader(section);
		}

		if (sourceElements.hasColumnsFooter()) {
			section = visualDocument.createElement(HTML.TAG_TFOOT);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualElements.setColumnsFooterRow(row);
			visualTable.appendChild(section);
			visualElements.setColumnsFooter(section);
		}

		if (sourceElements.hasBodySection()) {
			section = visualDocument.createElement(HTML.TAG_TBODY);
			row = visualDocument.createElement(HTML.TAG_TR);
			section.appendChild(row);
			visualTable.appendChild(section);
			visualElements.setContentTableBodyRow(row);
			// visualElements.setBody(section);
		}

		VpeChildrenInfo info = null;
		if (sourceElements.getColumnCount() > 0) {
			nsIDOMElement group = visualDocument
					.createElement(HTML.TAG_COLGROUP);
			visualTable.appendChild(group);
			info = new VpeChildrenInfo(group);
			creatorInfo.addChildrenInfo(info);
		}

		for (int i = 0; i < sourceElements.getColumnCount(); i++) {
			SourceColumnElements column = sourceElements.getColumn(i);
			info.addSourceChild(column.getColumn());
		}

		nsIDOMElement outterTBODY = visualDocument
				.createElement(HTML.TAG_TBODY);
		nsIDOMElement outterTR = visualDocument.createElement(HTML.TAG_TR);
		nsIDOMElement outterTD = visualDocument.createElement(HTML.TAG_TD);

		/*
		 * To create appropriate visual appearance borders of the body cell and
		 * content table were set via styles.
		 */
		outterTD.setAttribute(HTML.ATTR_STYLE, TD_HIDDEN_BORDER_STYLE);
		visualTable.setAttribute(HTML.ATTR_WIDTH, HUNDRED_PERCENTS);
		visualTable.setAttribute(HTML.ATTR_BORDER, ZERO);

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
			VpeCreator creator = (VpeCreator) propertyCreators.get(i);
			if (creator != null) {

				/*
				 * Sets attributes for the wrapper table
				 */
				VpeCreatorInfo info1 = creator.create(pageContext,
						(Element) sourceNode, visualDocument, outterTable,
						visualNodeMap);
				if (info1 != null && info1.getVisualNode() != null) {
					nsIDOMAttr attr = (nsIDOMAttr) info1.getVisualNode();

					/*
					 * Fixes creation 'border="1"' when setting border attribute
					 * to the table. Also skips empty attributes to fix layout
					 * problems.
					 */
					if (null == attr.getNodeValue()
							|| EMPTY.equalsIgnoreCase(attr.getNodeValue())) {
						continue;
					}
					outterTable.setAttributeNode(attr);
				}

				/*
				 * Sets attributes for the content table
				 */
				VpeCreatorInfo info2 = creator.create(pageContext,
						(Element) sourceNode, visualDocument, visualTable,
						visualNodeMap);
				if (info2 != null && info2.getVisualNode() != null) {
					nsIDOMAttr attr = (nsIDOMAttr) info2.getVisualNode();

					/*
					 * Fixes creation 'border="1"' when setting border attribute
					 * to the table. Also skips empty attributes to fix layout
					 * problems.
					 */
					if (null == attr.getNodeValue()
							|| EMPTY.equalsIgnoreCase(attr.getNodeValue())) {
						continue;
					}

					/*
					 * Sets attributes for the content table
					 */
					if (VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES
							.equalsIgnoreCase(attr.getNodeName())) {
						setRowClass(visualElements.getContentTableBodyRow(),
								attr.getNodeValue());
						continue;
					}

					/*
					 * Skip setting content table border
					 */
					if (HTML.ATTR_BORDER.equalsIgnoreCase(attr.getNodeName())) {

						/*
						 * If attribute border is set then table cells have
						 * borders. Because two table are used border should
						 * appear around content table cells but not the table
						 * itself. By default content table has no border.
						 */
						String value = attr.getNodeValue();
						int val = -1;
						if ((null != value) && (!EMPTY.equalsIgnoreCase(value))) {
							try {
								val = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								// ignore
							}
						}
						if (val > 0) {
							visualTable.setAttribute(HTML.ATTR_BORDER, ONE);
							visualTable.setAttribute(HTML.ATTR_STYLE,
									RULES_HIDDEN_BORDER_STYLE);
						}

						continue;
					}

					/*
					 * Fixes creation of a border around content table when
					 * attribute rules="rows" is set
					 */
					if (ATTR_RULES.equalsIgnoreCase(attr.getNodeName())) {
						if (ATTR_RULES_VALUE_ROWS.equalsIgnoreCase(attr
								.getNodeValue())) {
							outterTD.setAttribute(HTML.ATTR_STYLE,
									TD_RULES_ROWS_BORDER_STYLE);
						}
					}
					visualTable.setAttributeNode(attr);
				}
			}
		}
		return creatorInfo;
	}

	public void setAttribute(VpePageContext pageContext, Element sourceElement,
			Map visualNodeMap, String name, String value) {
		VisualDataTableElements visualElements = getVisualDataTableElements(visualNodeMap);
		if (visualElements != null) {
			if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS
							.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getTableHeaderRow(), value);
				setCellsClass(visualElements.getColumnsHeaderRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS
							.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getColumnsFooterRow(), value);
				setCellsClass(visualElements.getTableFooterRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES
							.equalsIgnoreCase(name)) {
				setRowClass(visualElements.getContentTableBodyRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES
							.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getContentTableBodyRow(), value);
			}
		}
	}

	public void validate(VpePageContext pageContext, Element sourceElement,
			Document visualDocument, Element visualParent,
			Element visualElement, Map visualNodeMap) {
		VisualDataTableElements visualElements = null;
		SourceDataTableElements sourceElements = null;
		if (visualNodeMap != null) {
			visualElements = getVisualDataTableElements(visualNodeMap);
			sourceElements = getSourceDataTableElements(visualNodeMap);
		} else if (sourceElement != null) {
			sourceElements = new SourceDataTableElements(sourceElement);
			nsIDOMNode visualNode = pageContext.getCurrentVisualNode();
			if (visualNode != null) {
				visualElements = VpeDataTableElements
						.getVisualDataTableElements(visualNode);
			}
		}
		if (visualElements != null) {
			setCellsClass(
					visualElements.getTableHeaderRow(),
					sourceElement
							.getAttribute(VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS));
			setCellsClass(
					visualElements.getColumnsHeaderRow(),
					sourceElement
							.getAttribute(VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS));
			setCellsClass(
					visualElements.getColumnsFooterRow(),
					sourceElement
							.getAttribute(VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS));
			setCellsClass(
					visualElements.getTableFooterRow(),
					sourceElement
							.getAttribute(VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS));

			setRowClass(
					visualElements.getBodyRow(),
					sourceElement
							.getAttribute(VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES));
			setCellsClass(
					visualElements.getBodyRow(),
					sourceElement
							.getAttribute(VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES));
		}
		if (sourceElements != null && visualElements != null) {
			setRowDisplayStyle(visualElements.getTableHeaderRow(),
					sourceElements.hasTableHeader());
			setRowDisplayStyle(visualElements.getColumnsHeaderRow(),
					sourceElements.hasColumnsHeader());
			setRowDisplayStyle(visualElements.getBodyRow(), sourceElements
					.hasBodySection());
			setRowDisplayStyle(visualElements.getColumnsFooterRow(),
					sourceElements.hasColumnsFooter());
			setRowDisplayStyle(visualElements.getTableFooterRow(),
					sourceElements.hasTableFooter());
		}
	}

	public void removeAttribute(VpePageContext pageContext,
			Element sourceElement, Map visualNodeMap, String name) {
		VisualDataTableElements visualElements = getVisualDataTableElements(visualNodeMap);
		if (visualElements != null) {
			if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_HEADER_CLASS
							.equalsIgnoreCase(name)) {
				removeCellsClass(visualElements.getTableHeaderRow());
				removeCellsClass(visualElements.getColumnsHeaderRow());
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_FOOTER_CLASS
							.equalsIgnoreCase(name)) {
				removeCellsClass(visualElements.getColumnsFooterRow());
				removeCellsClass(visualElements.getTableFooterRow());
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_ROW_CLASSES
							.equalsIgnoreCase(name)) {
				removeRowClass(visualElements.getBodyRow());
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES
					.equals(name)
					: VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES
							.equalsIgnoreCase(name)) {
				removeCellsClass(visualElements.getBodyRow());
			}
		}
	}

	private String[] getClasses(String value) {
		if (value != null) {
			return value.split(","); //$NON-NLS-1$
		}
		return new String[0];
	}

	private void setCellsClass(nsIDOMElement row, String value) {
		if (row != null && value != null) {
			String[] classes = getClasses(value);
			int ind = 0;

			nsIDOMNodeList children = row.getChildNodes();
			long count = children != null ? children.getLength() : 0;
			for (long i = 0; i < count; i++) {
				nsIDOMNode child = children.item(i);
				if (child != null
						&& child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
					try {
						nsIDOMHTMLTableCellElement cell = queryInterface(child, nsIDOMHTMLTableCellElement.class);
						cell.setAttribute(HTML.ATTR_CLASS, classes[ind]);
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
				if (child != null
						&& child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
					try {
						nsIDOMHTMLTableCellElement cell = queryInterface(child, nsIDOMHTMLTableCellElement.class);
						cell.removeAttribute(HTML.ATTR_CLASS);
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
			String rowClass = (rowClasses != null && rowClasses.length > 0) ? rowClasses[0]
					: null;
			if (rowClass!=null && rowClass.trim().length() > 0) {
				row.setAttribute(HTML.ATTR_CLASS, rowClass);
			} else {
				row.removeAttribute(HTML.ATTR_CLASS);
			}
		}
	}

	private void setRowDisplayStyle(nsIDOMElement row, boolean visible) {
		if (row != null) {
			row.setAttribute(HTML.ATTR_STYLE, DISPLAY_STYLE_NAME
					+ (visible ? EMPTY : NONE));
		}
	}

	private void removeRowClass(nsIDOMElement row) {
		if (row != null) {
			row.removeAttribute(HTML.ATTR_CLASS);
		}
	}

	private VisualDataTableElements getVisualDataTableElements(Map visualNodeMap) {
		if (visualNodeMap != null) {
			Object o = visualNodeMap.get(this);
			if (o != null && o instanceof Object[]
					&& ((Object[]) o)[0] instanceof VisualDataTableElements) {
				return (VisualDataTableElements) ((Object[]) o)[0];
			}
		}
		return null;
	}

	private SourceDataTableElements getSourceDataTableElements(Map visualNodeMap) {
		if (visualNodeMap != null) {
			Object o = visualNodeMap.get(this);
			if (o != null && o instanceof Object[]
					&& ((Object[]) o)[1] instanceof SourceDataTableElements) {
				return (SourceDataTableElements) ((Object[]) o)[1];
			}
		}
		return null;
	}

}

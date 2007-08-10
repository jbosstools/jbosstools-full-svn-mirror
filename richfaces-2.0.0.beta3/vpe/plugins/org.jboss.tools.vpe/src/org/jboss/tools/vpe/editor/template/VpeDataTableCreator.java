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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceColumnElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.SourceDataTableElements;
import org.jboss.tools.vpe.editor.template.VpeDataTableElements.VisualDataTableElements;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.util.MozillaSupports;

public class VpeDataTableCreator extends VpeAbstractCreator {
	private boolean caseSensitive;

	private VpeExpression headerClassExpr;
	private VpeExpression footerClassExpr;
	private VpeExpression rowClassesExpr;
	private VpeExpression columnClassesExpr;

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

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {

		SourceDataTableElements sourceElements = new SourceDataTableElements(sourceNode);
		VisualDataTableElements visualElements = new VisualDataTableElements();

		Element visualTable = visualDocument.createElement("table");
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(visualTable);

		Element section = null, row = null, cell = null;
		if (true || sourceElements.hasHeaderSection()) {
			section = visualDocument.createElement("thead");
			if (true || sourceElements.hasTableHeader()) {
				row = visualDocument.createElement("tr");
				section.appendChild(row);
				visualElements.setTableHeaderRow(row);
				if (sourceElements.getTableHeader() != null) {
					VpeChildrenInfo info = new VpeChildrenInfo(row);
					info.addSourceChild(sourceElements.getTableHeader());
					creatorInfo.addChildrenInfo(info);
				}
			}
			if (true || sourceElements.hasColumnsHeader()) {
				row = visualDocument.createElement("tr");
				section.appendChild(row);
				visualElements.setColumnsHeaderRow(row);
			}
			visualTable.appendChild(section);
			visualElements.setHeader(section);
		}

		if (true || sourceElements.hasFooterSection()) {
			section = visualDocument.createElement("tfoot");
			if (true || sourceElements.hasColumnsFooter()) {
				row = visualDocument.createElement("tr");
				section.appendChild(row);
				visualElements.setColumnsFooterRow(row);
			}
			if (true || sourceElements.hasTableFooter()) {
				row = visualDocument.createElement("tr");
				section.appendChild(row);
				visualElements.setTableFooterRow(row);
				if (sourceElements.getTableFooter() != null) {
					VpeChildrenInfo info = new VpeChildrenInfo(row);
					info.addSourceChild(sourceElements.getTableFooter());
					creatorInfo.addChildrenInfo(info);
				}
			}
			visualTable.appendChild(section);
			visualElements.setFooter(section);
		}

		if (true || sourceElements.hasBodySection()) {
			section = visualDocument.createElement("tbody");
			row = visualDocument.createElement("tr");
			section.appendChild(row);
			visualTable.appendChild(section);
			visualElements.setBodyRow(row);
			visualElements.setBody(section);
		}

		VpeChildrenInfo info = null;
		if (sourceElements.getColumnCount() > 0) {
			Element group = visualDocument.createElement("colgroup");
			visualTable.appendChild(group);
			info = new VpeChildrenInfo(group);
			creatorInfo.addChildrenInfo(info);
		}

		for (int i = 0; i < sourceElements.getColumnCount(); i++) {
			SourceColumnElements column = sourceElements.getColumn(i);
			info.addSourceChild(column.getColumn());
		}

		Object[] elements = new Object[2];
		elements[0] = visualElements;
		elements[1] = sourceElements;
		visualNodeMap.put(this, elements);

		for (int i = 0; i < propertyCreators.size(); i++) {
			VpeCreator creator = (VpeCreator)propertyCreators.get(i);
			if (creator != null) {
				VpeCreatorInfo info1 = creator.create(pageContext, (Element) sourceNode, visualDocument, visualTable, visualNodeMap);
				if (info1 != null && info1.getVisualNode() != null) {
					Attr attr = (Attr)info1.getVisualNode();
					visualTable.setAttributeNode(attr);
					MozillaSupports.release(attr);
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
				setRowClass(visualElements.getBodyRow(), value);
			} else if (caseSensitive ? VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES.equals(name) : VpeTemplateManager.ATTR_DATATABLE_COLUMN_CLASSES.equalsIgnoreCase(name)) {
				setCellsClass(visualElements.getBodyRow(), value);
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
			Node visualNode = pageContext.getCurrentVisualNode();
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

	private void setCellsClass(Element row, String value) {
		if (row != null && value != null) {
			String[] classes = getClasses(value);
			int ind = 0;

			NodeList children = row.getChildNodes();
			int count = children != null ? children.getLength() : 0;
			for (int i = 0; i < count; i++) {
				Node child = children.item(i);
				if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
					((Element)child).setAttribute("class", classes[ind]);
					ind = ind < (classes.length - 1) ? ind + 1 : 0;
				}
				MozillaSupports.release(child);
			}
			MozillaSupports.release(children);
		}
	}

	private void removeCellsClass(Element row) {
		if (row != null) {
			NodeList children = row.getChildNodes();
			int count = children != null ? children.getLength() : 0;
			for (int i = 0; i < count; i++) {
				Node child = children.item(i);
				if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
					((Element)child).removeAttribute("class");
				}
				MozillaSupports.release(child);
			}
			MozillaSupports.release(children);
		}
	}

	private void setRowClass(Element row, String value) {
		if (row != null && value != null) {
			String[] rowClasses = getClasses(value);
			String rowClass = (rowClasses != null && rowClasses.length > 0) ? rowClasses[0] : null;
			if (rowClass.trim().length() > 0) {
				row.setAttribute("class", rowClass);
			} else {
				row.removeAttribute("class");
			}
		}
	}

	private void setRowDisplayStyle(Element row, boolean visible) {
		if (row != null) {
			row.setAttribute("style", "display:" + (visible ? "" : "none"));
		}
	}

	private void removeRowClass(Element row) {
		if (row != null) {
			row.removeAttribute("class");
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

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
import java.util.Set;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeGridCreator extends VpeAbstractCreator {
	static final String VAL_PAGE_DIRECTION = "pageDirection"; //$NON-NLS-1$

	private boolean caseSensitive;
	private VpeExpression layoutExpr;
	private VpeExpression tableSizeExpr;
	private List propertyCreators;
	private Set dependencySet;
	
	VpeGridCreator(Element gridElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(gridElement, dependencyMap);
	}

	private void build(Element gridElement, VpeDependencyMap dependencyMap) {
		Attr layoutAttr = gridElement.getAttributeNode(VpeTemplateManager.ATTR_GRID_LAYOUT);
		if (layoutAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(layoutAttr.getValue(), caseSensitive);
				layoutExpr = info.getExpression();
				dependencySet = info.getDependencySet();
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
		Attr tableSizeAttr = gridElement.getAttributeNode(VpeTemplateManager.ATTR_GRID_TABLE_SIZE);
		if (tableSizeAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(tableSizeAttr.getValue(), caseSensitive);
				tableSizeExpr = info.getExpression();
				if (dependencySet == null) {
					dependencySet = info.getDependencySet();
				} else {
					dependencySet.addAll(info.getDependencySet());
				}
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		if (VpeTemplateManager.ATTR_GRID_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_GRID_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_GRID_PROPERTIES[i];
				Attr attr = gridElement.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null) propertyCreators  = new ArrayList();
					propertyCreators.add(new VpeAttributeCreator(attrName, attr.getValue(), dependencyMap, caseSensitive));
				}
			}
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) throws VpeExpressionException {
		boolean layoutHorizontal = true;
		if (layoutExpr != null) {
			VpeValue vpeValue = layoutExpr.exec(pageContext, sourceNode);
			if (vpeValue != null) {
				String strValue = vpeValue.stringValue();
				if (caseSensitive) {
					layoutHorizontal = !VAL_PAGE_DIRECTION.equals(strValue);
				} else {
					layoutHorizontal = !VAL_PAGE_DIRECTION.equalsIgnoreCase(strValue);
				}
			}
		}
		int tableSize = 0;
		if (tableSizeExpr != null) {
			VpeValue vpeValue = tableSizeExpr.exec(pageContext, sourceNode);
			if (vpeValue != null) {
				String strValue = vpeValue.stringValue();
				try {
					int val = Integer.parseInt(strValue);
					if (val > 0) {
						tableSize = val;
					}
				} catch (NumberFormatException e) {
					VpePlugin.getPluginLog().logError(e);
				}
			}
		}
		
		nsIDOMElement visualTable = visualDocument.createElement(HTML.TAG_TABLE);
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(visualTable);

		if (propertyCreators != null) {
			for (int i = 0; i < propertyCreators.size(); i++) {
				VpeCreator creator = (VpeCreator)propertyCreators.get(i);
				if (creator != null) {
					VpeCreatorInfo info = creator.create(pageContext, (Element) sourceNode, visualDocument, visualTable, visualNodeMap);
					if (info != null && info.getVisualNode() != null) {
						nsIDOMAttr attr = (nsIDOMAttr)info.getVisualNode();
						visualTable.setAttributeNode(attr);
					}
				}
			}
		}
		
		NodeList children = sourceNode.getChildNodes();
		int count = children != null ? children.getLength() : 0;
		if (count > 0) {
			Node[] sourceChildren = new Node[count];
			int childrenCount = 0;
			for (int i = 0; i < count; i++) {
				Node node = children.item(i);
				int type = node.getNodeType();
				if (type == Node.ELEMENT_NODE || type == Node.TEXT_NODE && node.getNodeValue().trim().length() > 0) {
					sourceChildren[childrenCount] = node;
					childrenCount++;
				}
			}
			if (childrenCount > 0) {
				if (tableSize == 0) {
					tableSize = childrenCount;
				}
				int rowCount;
				int rowLength;
				if (layoutHorizontal) {
					rowCount = (childrenCount + tableSize - 1) / tableSize;
					rowLength = tableSize;
				} else {
					rowCount = tableSize;
					rowLength = (childrenCount + tableSize - 1) / tableSize;
				}
				for (int i = 0; i < rowCount; i++) {
					nsIDOMElement visualRow = visualDocument.createElement(HTML.TAG_TR);
					for (int j = 0; j < rowLength; j++) {
						nsIDOMElement visualCell = visualDocument.createElement(HTML.TAG_TD);
						visualRow.appendChild(visualCell);
						int sourceIndex = layoutHorizontal ? rowLength * i + j : rowCount * j + i;
						if (sourceIndex < childrenCount) {
							VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualCell);
							childrenInfo.addSourceChild(sourceChildren[sourceIndex]);
							creatorInfo.addChildrenInfo(childrenInfo);
						}
					}
					visualTable.appendChild(visualRow);
				}
			}
		}
		creatorInfo.addDependencySet(dependencySet);
		return creatorInfo;
	}
}

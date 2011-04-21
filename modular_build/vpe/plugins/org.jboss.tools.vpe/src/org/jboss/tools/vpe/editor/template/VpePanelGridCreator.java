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
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeClassUtil;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpePanelGridCreator extends VpeAbstractCreator {

	private boolean caseSensitive;
	private VpeExpression tableSizeExpr;
	private VpeExpression captionClassExpr;
	private VpeExpression captionStyleExpr;
	private VpeExpression headerClassExpr;
	private VpeExpression footerClassExpr;
	private VpeExpression rowClassesExpr;
	private VpeExpression columnClassesExpr;
	private VpeExpression rulesExpr;

	private List propertyCreators;

	// private Set dependencySet;

	VpePanelGridCreator(Element gridElement, VpeDependencyMap dependencyMap,
			boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(gridElement, dependencyMap);
	}

	private void build(Element gridElement, VpeDependencyMap dependencyMap) {

		Attr tableSizeAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_TABLE_SIZE);
		if (tableSizeAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(tableSizeAttr.getValue(),
								caseSensitive);
				tableSizeExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr headerClassAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_HEADER_CLASS);
		if (headerClassAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(headerClassAttr.getValue(),
								caseSensitive);
				headerClassExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr footerClassAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_FOOTER_CLASS);
		if (footerClassAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(footerClassAttr.getValue(),
								caseSensitive);
				footerClassExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr rowClassesAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_ROW_CLASSES);
		if (rowClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(rowClassesAttr.getValue(),
								caseSensitive);
				rowClassesExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr columnClassesAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_COLUMN_CLASSES);
		if (columnClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(columnClassesAttr.getValue(),
								caseSensitive);
				columnClassesExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr captionClassesAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_CAPTION_CLASS);
		if (captionClassesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(
								captionClassesAttr.getValue(), caseSensitive);
				captionClassExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr captionStyleAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_CAPTION_STYLE);
		if (captionStyleAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(captionStyleAttr.getValue(),
								caseSensitive);
				captionStyleExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr rulesAttr = gridElement
				.getAttributeNode(VpeTemplateManager.ATTR_PANELGRID_RULES);
		if (rulesAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder
						.buildCompletedExpression(rulesAttr.getValue(),
								caseSensitive);
				rulesExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch (VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		if (VpeTemplateManager.ATTR_GRID_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_GRID_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_GRID_PROPERTIES[i];
				Attr attr = gridElement.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null)
						propertyCreators = new ArrayList();
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
		int tableSize = 1;
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
				}
			}
		}

		nsIDOMElement visualTable = visualDocument.createElement(HTML.TAG_TABLE);
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(visualTable);

		if (propertyCreators != null) {
			for (int i = 0; i < propertyCreators.size(); i++) {
				VpeCreator creator = (VpeCreator) propertyCreators.get(i);
				if (creator != null) {
					VpeCreatorInfo info = creator.create(pageContext,
							sourceNode, visualDocument, visualTable,
							visualNodeMap);
					if (info != null && info.getVisualNode() != null) {
						nsIDOMAttr attr = (nsIDOMAttr) info.getVisualNode();
						visualTable.setAttributeNode(attr);
					}
				}
			}
		}

		if (rulesExpr != null) {
			String rules = rulesExpr.exec(pageContext, sourceNode)
					.stringValue();
			if (rules.length() > 0)
				visualTable.setAttribute(
						VpeTemplateManager.ATTR_PANELGRID_RULES, rules);
		}
		NodeList children = sourceNode.getChildNodes();
		int count = children != null ? children.getLength() : 0;
		if (count > 0) {
		    Node header = null;
		    Node footer = null;
		    Node caption = null;
		    Node[] sourceChildren = new Node[count];
		    int childrenCount = 0;
		    for (int i = 0; i < count; i++) {
			Node node = children.item(i);
			int type = node.getNodeType();
			if ((type == Node.ELEMENT_NODE)
				|| ((type == Node.TEXT_NODE) 
						&& (node.getNodeValue() != null) 
						&& (node.getNodeValue().trim().length() > 0))) {
			    switch (VpeCreatorUtil.getFacetType(node, pageContext)) {
			    case VpeCreatorUtil.FACET_TYPE_HEADER:
				header = node;
				break;
			    case VpeCreatorUtil.FACET_TYPE_FOOTER:
				footer = node;
				break;
			    case VpeCreatorUtil.FACET_TYPE_CAPTION:
				caption = node;
				break;
			    default:
				sourceChildren[childrenCount] = node;
			    childrenCount++;
			    break;
			    }
			}
		    }

		    if (childrenCount > 0) {
			if (tableSize == 0) {
			    tableSize = childrenCount;
			}
			int rowCount = (childrenCount + tableSize - 1) / tableSize;

			nsIDOMElement visualHead = null;
			nsIDOMElement visualFoot = null;
			nsIDOMElement visualCaption = null;

			if (caption != null) {
			    visualCaption = visualDocument
			    .createElement(HTML.TAG_CAPTION);
			    visualTable.appendChild(visualCaption);
			    VpeChildrenInfo childrenInfo = new VpeChildrenInfo(
				    visualCaption);
			    childrenInfo.addSourceChild(caption);
			    creatorInfo.addChildrenInfo(childrenInfo);
			    if (captionClassExpr != null
				    && caption.getParentNode() != null) {
				String captionClass = captionClassExpr.exec(
					pageContext, caption.getParentNode())
					.stringValue();
				visualCaption.setAttribute(HTML.ATTR_CLASS, captionClass);
			    }

			    if (captionStyleExpr != null
				    && caption.getParentNode() != null) {
				String captionStyle = captionStyleExpr.exec(
					pageContext, caption.getParentNode())
					.stringValue();
				visualCaption.setAttribute(HTML.ATTR_STYLE, captionStyle);
			    }
			}
			if (header != null) {
			    visualHead = visualDocument.createElement(HTML.TAG_THEAD);
			    visualTable.appendChild(visualHead);
			}
			if (footer != null) {
			    visualFoot = visualDocument.createElement(HTML.TAG_TFOOT);
			    visualTable.appendChild(visualFoot);
			}

			nsIDOMElement visualBody = visualDocument
			.createElement(HTML.TAG_TBODY);
			visualTable.appendChild(visualBody);

			List<String> rowClasses = VpeClassUtil.getClasses(rowClassesExpr, sourceNode,
				pageContext);
			List<String> columnClasses = VpeClassUtil.getClasses(columnClassesExpr, sourceNode,
				pageContext);

			int rci = 0; // index of row class
			for (int i = 0; i < rowCount; i++) {
			    int cci = 0; // index of column class. Reset on every new row.

			    nsIDOMElement visualRow = visualDocument
			    .createElement(HTML.TAG_TR);
			    if (rowClasses.size() > 0) {
				visualRow.setAttribute(HTML.ATTR_CLASS, rowClasses.get(rci)
					.toString());
				rci++;
				if (rci >= rowClasses.size())
				    rci = 0;
			    }
			    for (int j = 0; j < tableSize; j++) {
				if (i*tableSize+j >= childrenCount) {
				    break;
				}
				nsIDOMElement visualCell = visualDocument
				.createElement(HTML.TAG_TD);
				if (columnClasses.size() > 0) {
				    visualCell.setAttribute(HTML.ATTR_CLASS, columnClasses.get(
					    cci).toString());
				    cci++;
				    if (cci >= columnClasses.size())
					cci = 0;
				}
				visualRow.appendChild(visualCell);
				int sourceIndex = tableSize * i + j;
				if (sourceIndex < childrenCount) {
				    Node child = sourceChildren[sourceIndex];
				    if (child != header && child != footer) {
					VpeChildrenInfo childrenInfo = new VpeChildrenInfo(
						visualCell);
					childrenInfo.addSourceChild(child);
					creatorInfo.addChildrenInfo(childrenInfo);
				    }
				}
			    }
			    if (visualBody != null) {
				visualBody.appendChild(visualRow);
			    } else {
				visualTable.appendChild(visualRow);
			    }
			}
			makeSpecial(header, visualHead, visualDocument, tableSize,
				creatorInfo,  HTML.TAG_TH, headerClassExpr, pageContext);
			makeSpecial(footer, visualFoot, visualDocument, tableSize,
				creatorInfo, HTML.TAG_TD, footerClassExpr, pageContext);

			for (int i = 0; i < propertyCreators.size(); i++) {
			    VpeCreator creator = (VpeCreator) propertyCreators.get(i);
			    if (creator != null) {
				VpeCreatorInfo info = creator.create(pageContext,
					sourceNode, visualDocument,
					visualTable, visualNodeMap);
				if (info != null && info.getVisualNode() != null) {
				    nsIDOMAttr attr = (nsIDOMAttr) info.getVisualNode();
				    if (attr.getValue().length() > 0) {
					visualTable.setAttributeNode(attr);
				    }
				}
			    }
			}
		    }
		}

		return creatorInfo;
	}

	private void makeSpecial(Node header, nsIDOMElement visualHead,
			nsIDOMDocument visualDocument, int tableSize,
			VpeCreatorInfo creatorInfo, String cellTag,
			VpeExpression headerClassExpr, VpePageContext pageContext) throws VpeExpressionException {
		if (header != null && visualHead != null) {
			nsIDOMElement visualRow = visualDocument.createElement(HTML.TAG_TR);
			visualHead.appendChild(visualRow);
			nsIDOMElement visualCell = visualDocument.createElement(cellTag);
			visualCell.setAttribute(HTML.ATTR_COLSPAN, "" + tableSize); //$NON-NLS-1$
			if (headerClassExpr != null && header.getParentNode() != null) {
				String headerClass = headerClassExpr.exec(pageContext,
						header.getParentNode()).stringValue();
				visualCell.setAttribute(HTML.ATTR_CLASS, headerClass);
			}
			visualRow.appendChild(visualCell);
			VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualCell);
			childrenInfo.addSourceChild(header);
			creatorInfo.addChildrenInfo(childrenInfo);
		}
	}

	private Node getFirstChildElement(Node parentNode) {
		if (parentNode != null) {
			NodeList childrens = parentNode.getChildNodes();
			int length = childrens != null ? childrens.getLength() : 0;
			for (int i = 0; i < length; i++) {
				Node child = childrens.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					return child;
				}
			}
		}
		return null;
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

}

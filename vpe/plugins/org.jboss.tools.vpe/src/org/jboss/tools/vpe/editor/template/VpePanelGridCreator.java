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
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpePanelGridCreator extends VpeAbstractCreator {

	private final String REDUNDANT_TEXT_SEPARATOR = "\n\n";
	
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

		NamedNodeMap map = gridElement.getAttributes();

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

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Map visualNodeMap) {
		int tableSize = 1;
		if (tableSizeExpr != null) {
			VpeValue vpeValue = tableSizeExpr.exec(pageContext, sourceNode);
			if (vpeValue != null) {
				String strValue = vpeValue.stringValue();
				try {
					int val = Integer.valueOf(strValue).intValue();
					if (val > 0) {
						tableSize = val;
					}
				} catch (Exception e) {
				}
			}
		}

		nsIDOMElement visualTable = visualDocument
				.createElement(HTML.TAG_TABLE);
		nsIDOMElement div = visualDocument
				.createElement(HTML.TAG_DIV);
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(div);

		if (propertyCreators != null) {
			for (int i = 0; i < propertyCreators.size(); i++) {
				VpeCreator creator = (VpeCreator) propertyCreators.get(i);
				if (creator != null) {
					VpeCreatorInfo info = creator.create(pageContext,
							(Element) sourceNode, visualDocument, visualTable,
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
			List<Node> sourceTextChildren = new ArrayList<Node>();
			int childrenCount = 0;
			int textChildrenCount = 0;
			for (int i = 0; i < count; i++) {
				Node node = children.item(i);
				int type = node.getNodeType();
				if (type == Node.ELEMENT_NODE || type == Node.TEXT_NODE
						&& node.getNodeValue().trim().length() > 0) {
					
					/*
					 * Fixes http://jira.jboss.com/jira/browse/JBIDE-1944
					 * author: Denis Maliarevich 
					 * Finds all unattended text nodes
					 */
					if (type == Node.TEXT_NODE) {
						sourceTextChildren.add(node);
						textChildrenCount++;
					} else {
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
			}
			
			/*
			 * Fixes http://jira.jboss.com/jira/browse/JBIDE-1944
			 * author: Denis Maliarevich
			 * Any text which is placed outside of the tags
			 * will be displayed above the table.
			 */
			if (textChildrenCount > 0) {
				String redundantText = REDUNDANT_TEXT_SEPARATOR;
				for (Node node : sourceTextChildren) {
					redundantText += node.getNodeValue();
					redundantText += REDUNDANT_TEXT_SEPARATOR;
				}
				div.appendChild(visualDocument.createTextNode(redundantText));
			}
			div.appendChild(visualTable);
			
			if (childrenCount > 0) {
				if (tableSize == 0) {
					tableSize = childrenCount;
				}
				int rowCount = (childrenCount + tableSize - 1) / tableSize;

				nsIDOMElement visualHead = null;
				nsIDOMElement visualFoot = null;
				nsIDOMElement visualCaption = null;
				nsIDOMElement visualBody = visualDocument
						.createElement(HTML.TAG_TBODY);
				visualTable.appendChild(visualBody);
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
						visualCaption.setAttribute("class", captionClass);
					}

					if (captionStyleExpr != null
							&& caption.getParentNode() != null) {
						String captionStyle = captionStyleExpr.exec(
								pageContext, caption.getParentNode())
								.stringValue();
						visualCaption.setAttribute("style", captionStyle);
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

				List rowClasses = getClasses(rowClassesExpr, sourceNode,
						pageContext);
				List columnClasses = getClasses(columnClassesExpr, sourceNode,
						pageContext);
				int rci = 0, cci = 0;
				for (int i = 0; i < rowCount; i++) {
					nsIDOMElement visualRow = visualDocument
							.createElement(HTML.TAG_TR);
					if (rowClasses.size() > 0) {
						visualRow.setAttribute("class", rowClasses.get(rci)
								.toString());
						rci++;
						if (rci >= rowClasses.size())
							rci = 0;
					}
					for (int j = 0; j < tableSize; j++) {
						nsIDOMElement visualCell = visualDocument
								.createElement(HTML.TAG_TD);
						if (columnClasses.size() > 0) {
							visualCell.setAttribute("class", columnClasses.get(
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
						creatorInfo, "th", headerClassExpr, pageContext);
				makeSpecial(footer, visualFoot, visualDocument, tableSize,
						creatorInfo, "td", footerClassExpr, pageContext);

				for (int i = 0; i < propertyCreators.size(); i++) {
					VpeCreator creator = (VpeCreator) propertyCreators.get(i);
					if (creator != null) {
						VpeCreatorInfo info = creator.create(pageContext,
								(Element) sourceNode, visualDocument,
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

	private List getClasses(VpeExpression expression, Node sourceNode,
			VpePageContext pageContext) {
		List b = new ArrayList();
		if (expression != null && sourceNode != null) {
			String classes = expression.exec(pageContext, sourceNode)
					.stringValue();
			String[] a = classes.split(",");
			for (int i = 0; i < a.length; i++) {
				if (a[i].trim().length() > 0) {
					b.add(a[i].trim());
				}
			}
		}
		return b;
	}

	private void makeSpecial(Node header, nsIDOMElement visualHead,
			nsIDOMDocument visualDocument, int tableSize,
			VpeCreatorInfo creatorInfo, String cellTag,
			VpeExpression headerClassExpr, VpePageContext pageContext) {
		if (header != null && visualHead != null) {
			nsIDOMElement visualRow = visualDocument.createElement(HTML.TAG_TR);
			visualHead.appendChild(visualRow);
			nsIDOMElement visualCell = visualDocument.createElement(cellTag);
			visualCell.setAttribute("colspan", "" + tableSize);
			if (headerClassExpr != null && header.getParentNode() != null) {
				String headerClass = headerClassExpr.exec(pageContext,
						header.getParentNode()).stringValue();
				visualCell.setAttribute("class", headerClass);
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

	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, Document visualDocument, Node visualNde,
			Object data, String name, String value) {
		return true;
	}
}

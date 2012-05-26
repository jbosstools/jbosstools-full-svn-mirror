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
package org.jboss.tools.vpe.editor.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.xpath.core.util.XSLTXPathHelper;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.proxy.VpeProxyUtil;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SourceDomUtil {
	private static final Set<String> TEMPLATES_NAMESPACES_WITH_RENDERED=new HashSet<String>();
	static {
		TEMPLATES_NAMESPACES_WITH_RENDERED.add("h:"); //$NON-NLS-1$
		TEMPLATES_NAMESPACES_WITH_RENDERED.add("a4j:"); //$NON-NLS-1$
		TEMPLATES_NAMESPACES_WITH_RENDERED.add("rich:"); //$NON-NLS-1$
		TEMPLATES_NAMESPACES_WITH_RENDERED.add("seam:"); //$NON-NLS-1$
	}
	
	static public Node getAncestorNode(Node sourceNode, String tagName) {
		if (tagName == null)
			return null;
		Node element = sourceNode;
		while (true) {
			if (tagName.equalsIgnoreCase(element.getNodeName()))
				return element;
			element = element.getParentNode();
			if (element == null)
				break;
		}
		return null;
	}

	/**
	 * Finds first n-th parent of <code>sourceNode</code> that has a linked
	 * non-null nodeMaping in <code>domMapping</code>.
	 * 
	 * @param domMapping
	 * @param sourceNode
	 * @return first n-th parent of <code>sourceNode</code> that has a linked
	 *         non-null nodeMaping in <code>domMapping</code> or
	 *         <code>null</code> if there is not any.
	 */
	public static Node getParentHavingDomMapping(final Node sourceNode,
			final VpeDomMapping domMapping) {
		VpeNodeMapping nodeMapping = null;
		Node parent = sourceNode;
		do {
			parent = parent.getParentNode();
			nodeMapping = domMapping.getNodeMapping(parent);
		} while (nodeMapping == null && parent != null);

		return parent;
	}

	/**
	 * @author mareshkau FIX for JBIDE-4179 check if node have rendered=false
	 *         attribute, checks el expresion to
	 * @return true if rendered="false"
	 */
	public static boolean isRenderedAttrEqFalse(VpePageContext pageContext,
			Element sourceNode) {
		boolean result = false;
		final String ATTR_RENDERED = "rendered"; //$NON-NLS-1$
		Element tempElement = sourceNode;
		if (sourceNode.hasAttribute(ATTR_RENDERED)) {
			if (pageContext.getElService().isELNode(sourceNode)) {
				tempElement = (Element) VpeProxyUtil
						.createProxyForELExpressionNode(pageContext, sourceNode);
			}
			if ("false".equals(tempElement.getAttribute(ATTR_RENDERED))) { //$NON-NLS-1$
				String templateName = VpeTemplateManager.getInstance().getTemplateName(pageContext, sourceNode);
				String [] templatePrefix = templateName.split(":"); //$NON-NLS-1$
				if(templatePrefix.length>1 && TEMPLATES_NAMESPACES_WITH_RENDERED.contains(templatePrefix[0]+":")) { //$NON-NLS-1$
					result = true;
				}
			}
		}
		return result;
	}
	
	public static Element getFacetByName(VpePageContext pageContext,
			Element sourceElement, String facetName) {
		if (facetName == null) {
			return null;
		}

		Element facetElement = null; 
		NodeList children = sourceElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
			if (isFacetElement(pageContext, node)
					&& facetName.equalsIgnoreCase( ((Element) node).getAttribute("name") )) { //$NON-NLS-1$
				facetElement = (Element) node;
			}
        }
        return facetElement;
	}
	
	public static boolean isFacetElement(VpePageContext pageContext, Node node) {
		if (node instanceof Element) {
			String templateName = VpeTemplateManager.getInstance()
					.getTemplateName(pageContext, node);
			return "f:facet".equals(templateName); //$NON-NLS-1$
		} else {
			return false;
		}
	}

	/**
	 * Returns a XPath expression given a DOM Node.
	 * 
	 * @param node The DOM Node to create the XPath expression.
	 * 
	 * @see XSLTXPathHelper#calculateXPathToNode(Node)
	 */
	public static String getXPath(Node node) {
		return XSLTXPathHelper.calculateXPathToNode(node);
	}
	
	/**
	 * Inverse function for {@link #getXPath(Node)}.
	 * 
	 * @param document ancestor document for xPath
	 * @param xPath XPath to a node in one of the following form:
	 * <code>
	 * <br>&nbsp;"/html/body/table/tr/td",
	 * <br>&nbsp;"/html/body/table/tr/td[1]",
	 * <br>&nbsp;"/html/body/table/tr/td[1]/@onclick".</code>
	 * 
	 * @return node for the given {@code xPath}, or {@code null}
	 * if the node is not found.
	 */
	public static Node getNodeByXPath(Document document, String xPath) {
		Node currentNode = document;
		String[] nodeNames = xPath.split("/"); //$NON-NLS-1$
		
		// begin from 1 to skip the first element which is empty
		for (int i = 1; i < nodeNames.length; i++) {
			String nodeName = nodeNames[i];
			if (nodeName.charAt(0) != '@') {
				currentNode = currentNode.getFirstChild();
				if (nodeName.charAt(nodeName.length() - 1) != ']') {
					while (currentNode.getNodeType() != Node.ELEMENT_NODE 
							|| !currentNode.getNodeName().equals(nodeName)) {
						currentNode = currentNode.getNextSibling();
					}
				} else {
					int openingBracketIndex = nodeName.lastIndexOf('[');
					String stringPosition = nodeName.substring(
							openingBracketIndex + 1,
							nodeName.length() - 1);
					nodeName = nodeName.substring(0, openingBracketIndex);
					
					int position = Integer.parseInt(stringPosition);
					int curPosition = 0;
					while (true) {
						if (currentNode.getNodeType() == Node.ELEMENT_NODE 
								&& currentNode.getNodeName().equals(nodeName)) {
							++curPosition;
							if (curPosition == position) {
								break;
							}
						}
						currentNode = currentNode.getNextSibling();
					}
				}
			} else {
				String attributeName = nodeName.substring(1, nodeName.length());
				currentNode = currentNode.getAttributes().getNamedItem(attributeName);
			}
		}

		return currentNode;
	}
	
	/**
	 * Utility function which is used to calculate offset in document by line number and character position.
	 * 
	 * @param linePosition the line position
	 * @param textWidget the text viewer
	 * @param lineIndex the line index
	 * 
	 * @return offset in document, -1 for wrong offset
	 * 
	 */
	public static final int getLinePositionOffset(ITextViewer itextViewer, int lineIndex, int linePosition) {
		int resultOffset = -1;
		StyledText textWidget = itextViewer.getTextWidget();
		if ((textWidget != null) && (lineIndex <= textWidget.getLineCount())) {
			resultOffset = textWidget.getOffsetAtLine(lineIndex);
			// here we get's tabs length
			// for more example you can see code
			// org.eclipse.ui.texteditor.AbstractTextEditor@getCursorPosition()
			// and class $PositionLabelValue
			int tabWidth = textWidget.getTabs();
			int characterOffset = 0;
			String currentString = textWidget.getLine(lineIndex);
			int pos = 1;
			for (int i = 0; (i < currentString.length())
					&& (pos < linePosition); i++) {
				if ('\t' == currentString.charAt(i)) {
					characterOffset += (tabWidth == 0 ? 0 : 1);
					pos += tabWidth;
				} else {
					pos++;
					characterOffset++;
				}
			}
			resultOffset += characterOffset;
			if (textWidget.getLineAtOffset(resultOffset) != lineIndex) {
				resultOffset = -1;
			}
		}
		return resultOffset;
	}
	
	/**
	 * Returns source node by source editor position(line and position in line).
	 * 
	 * @param linePosition the line position
	 * @param lineIndex the line index
	 * @param itextViewer the itext viewer
	 * 
	 * @return node for specified src position
	 */
	@SuppressWarnings("restriction")
    public static Node getSourceNodeByEditorPosition(ITextViewer itextViewer, int lineIndex, int linePosition) {
		int offset = getLinePositionOffset(itextViewer, lineIndex, linePosition);
		if (offset != -1) {
			return (Node) ContentAssistUtils.getNodeAt(itextViewer, offset);
		} else {
			return null;
		}
	}
}

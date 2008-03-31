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
package org.jboss.tools.jsf.vpe.richfaces.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeToggableTemplate;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RichFacesPanelBarTemplate extends VpeAbstractTemplate implements
	VpeToggableTemplate, VpeTemplate {

    private static final String SEMI_COLON = ";"; //$NON-NLS-1$
    private static final String PERCENT_100 = "100%"; //$NON-NLS-1$
    private static final String PANEL_BAR_ITEM = ":panelBarItem"; //$NON-NLS-1$
    private static final String SPACE = " "; //$NON-NLS-1$
    private static final String EMPTY = ""; //$NON-NLS-1$
    private static final String DR_PNLBAR_RICH_PANELBAR_DR_PNLBAR_B = "dr-pnlbar rich-panelbar dr-pnlbar-b "; //$NON-NLS-1$
    private static final String PANEL_BAR_PANEL_BAR_CSS = "panelBar/panelBar.css"; //$NON-NLS-1$

    private static Map<Node, String> toggleMap = new HashMap<Node, String>();

    public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
	    nsIDOMDocument visualDocument) {

	Element sourceElement = (Element) sourceNode;
	nsIDOMElement table = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TABLE);

	VpeCreationData creationData = new VpeCreationData(table);

	ComponentUtil.setCSSLink(pageContext, PANEL_BAR_PANEL_BAR_CSS,
		"richFacesPanelBar"); //$NON-NLS-1$
	String styleClass = sourceElement
		.getAttribute(HtmlComponentUtil.HTML_STYLECLASS_ATTR);
	table.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
		DR_PNLBAR_RICH_PANELBAR_DR_PNLBAR_B
			+ (styleClass == null ? EMPTY : styleClass));

	// Set style attribute
	StringBuffer styleValue = new StringBuffer("padding: 0px; "); //$NON-NLS-1$
	styleValue.append(height(sourceElement)).append(SPACE).append(
		width(sourceElement)).append(SPACE).append(
		ComponentUtil.getAttribute(sourceElement,
			HtmlComponentUtil.HTML_STYLE_ATTR));

	// Encode Body
	List<Node> children = ComponentUtil.getChildren(sourceElement);
	int activeId = getActiveId(sourceElement, children);
	int i = 0;

	String style = ComponentUtil.getAttribute(sourceElement,
		HtmlComponentUtil.HTML_STYLE_ATTR);

	String contentClass = ComponentUtil.getAttribute(sourceElement,
		RichFacesPanelItemTemplate.CONTENT_CLASS);
	String contentStyle = ComponentUtil.getAttribute(sourceElement,
		RichFacesPanelItemTemplate.CONTENT_STYLE);
	String headerClass = ComponentUtil.getAttribute(sourceElement,
		RichFacesPanelItemTemplate.HEADER_CLASS);
	String headerStyle = ComponentUtil.getAttribute(sourceElement,
		RichFacesPanelItemTemplate.HEADER_STYLE);
	String headerActiveStyle = ComponentUtil.getAttribute(sourceElement,
		RichFacesPanelItemTemplate.HEADER_ACTIVE_STYLE);
	String headerActiveClass = ComponentUtil.getAttribute(sourceElement,
		RichFacesPanelItemTemplate.HEADER_ACTIVE_CLASS);

	for (Node child : children) {
	    boolean active = (i == activeId);

	    if (child.getNodeName().endsWith(PANEL_BAR_ITEM)) {

		RichFacesPanelItemTemplate.encode(creationData, pageContext,
			(Element) child, visualDocument, table, active,
			ComponentUtil.getAttribute(sourceElement,
				HtmlComponentUtil.HTML_STYLECLASS_ATTR), style,
			headerClass, headerStyle, headerActiveClass,
			headerActiveStyle, contentClass, contentStyle, String
				.valueOf(i));

		i++;
	    }
	}

	table.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR, styleValue
		.toString());
	return creationData;
    }

    /**
     * 
     * @param sourceElement
     * @return
     */
    private String height(Element sourceElement) {
	String height = sourceElement
		.getAttribute(HtmlComponentUtil.HTML_HEIGHT_ATTR);
	if (height == null || height.length() == 0) {

	    height = PERCENT_100;
	}
	return "height: " + height + SEMI_COLON; //$NON-NLS-1$
    }

    /**
     * 
     * @param sourceElement
     * @return
     */
    public String width(Element sourceElement) {
	String width = sourceElement
		.getAttribute(HtmlComponentUtil.HTML_ATR_WIDTH);
	if (width == null || width.length() == 0) {
	    width = PERCENT_100;
	}
	return "width: " + width + SEMI_COLON; //$NON-NLS-1$
    }

    /**
     * 
     */
    public void toggle(VpeVisualDomBuilder builder, Node sourceNode,
	    String toggleId) {
	toggleMap.put(sourceNode, toggleId);
    }

    /**
     * 
     */
    public void stopToggling(Node sourceNode) {
	toggleMap.remove(sourceNode);
    }

    /**
     * 
     * @param sourceElement
     * @param children
     * @return
     */
    private int getActiveId(Element sourceElement, List<Node> children) {
	int activeId = -1;
	try {
	    activeId = Integer.valueOf((String) toggleMap.get(sourceElement));
	} catch (NumberFormatException nfe) {
	    activeId = -1;
	}

	if (activeId == -1)
	    activeId = 0;

	int count = getChildrenCount(children);
	if (count - 1 < activeId) {
	    activeId = count - 1;
	}

	return activeId;
    }

    /**
     * 
     * @param children
     * @return
     */
    private int getChildrenCount(List<Node> children) {
	int count = 0;
	for (Node child : children) {
	    if (child.getNodeName().endsWith(PANEL_BAR_ITEM)) {
		count++;
	    }
	}
	return count;
    }

    @Override
    public boolean isRecreateAtAttrChange(VpePageContext pageContext,
	    Element sourceElement, nsIDOMDocument visualDocument,
	    nsIDOMElement visualNode, Object data, String name, String value) {
	if (name.equalsIgnoreCase(HtmlComponentUtil.HTML_WIDTH_ATTR)
		|| name.equalsIgnoreCase(HtmlComponentUtil.HTML_HEIGHT_ATTR)
		|| name.equalsIgnoreCase(HtmlComponentUtil.HTML_STYLE_ATTR))
	    return true;
	return false;
    }

}
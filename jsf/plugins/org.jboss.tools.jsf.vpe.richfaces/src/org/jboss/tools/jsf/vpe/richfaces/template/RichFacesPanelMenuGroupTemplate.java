/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.jsf.vpe.richfaces.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class RichFacesPanelMenuGroupTemplate extends VpeAbstractTemplate {

	/*
	 * pich:panelMenuGroup attributes
	 */
	private static final String DISABLED = "disabled"; //$NON-NLS-1$
	private static final String DISABLED_CLASS = "disabledClass"; //$NON-NLS-1$
	private static final String DISABLED_STYLE = "disabledStyle"; //$NON-NLS-1$
	private static final String ICON_EXPANDED = "iconExpanded"; //$NON-NLS-1$
	private static final String ICON_COLLAPSED = "iconCollapsed"; //$NON-NLS-1$
	private static final String ICON_DISABLED = "iconDisabled"; //$NON-NLS-1$
	private static final String ICON_LABEL = "label"; //$NON-NLS-1$
	private static final String STYLE = "style"; //$NON-NLS-1$
	private static final String STYLE_CLASS = "styleClass"; //$NON-NLS-1$
	
	private static final String NAME_COMPONENT = "panelMenuGroup"; //$NON-NLS-1$
	private static final String PANEL_MENU_END_TAG = ":panelMenu"; //$NON-NLS-1$
	private static final String PANEL_MENU_GROUP_END_TAG = ":panelMenuGroup"; //$NON-NLS-1$
	private static final String PANEL_MENU_ITEM_END_TAG = ":panelMenuItem"; //$NON-NLS-1$

	private static final String COMPONENT_ATTR_VPE_SUPPORT = "vpeSupport"; //$NON-NLS-1$
	private static final String COMPONENT_ATTR_VPE_USER_TOGGLE_ID = "vpe-user-toggle-id"; //$NON-NLS-1$
	private static final String PANEL_MENU_GROUP_ICON_SPACER_PATH = "/panelMenuGroup/spacer.gif"; //$NON-NLS-1$
	private static final String STYLE_PATH = "/panelMenuGroup/style.css"; //$NON-NLS-1$
	private static final String EMPTY_DIV_STYLE = "display: none;"; //$NON-NLS-1$
	
	private static final Map<String, String> DEFAULT_ICON_MAP = new HashMap<String, String>();
	
	private static final String TRUE = "true"; //$NON-NLS-1$
	private static final String RIGHT = "right"; //$NON-NLS-1$
	private static final String SPACE = " "; //$NON-NLS-1$
	private static final String EMPTY = ""; //$NON-NLS-1$
	
	/*
	 *	rich:panelMenu attributes for groups
	 */ 
	private static String pm_iconGroupPosition;
	private static String pm_iconGroupTopPosition;
	private static String pm_iconCollapsedGroup;
	private static String pm_iconCollapsedTopGroup;
	private static String pm_iconExpandedGroup;
	private static String pm_iconExpandedTopGroup;
	private static String pm_iconDisableGroup;
	private static String pm_iconTopDisableGroup;
	
	/*
	 *	rich:panelMenu style classes for groups
	 */ 
	private static String pm_disabled;
	private static String pm_disabledGroupClass;
	private static String pm_disabledGroupStyle;
	private static String pm_topGroupClass;
	private static String pm_topGroupStyle;
	private static String pm_groupClass;
	private static String pm_groupStyle;
	private static String pm_style;
	private static String pm_styleClass;
	
	/*
	 * pich:panelMenuGroup attributes
	 */
	private static String pmg_disabledStyle;
	private static String pmg_disabledClass;
	private static String pmg_disabled;
	private static String pmg_iconExpanded;
	private static String pmg_iconCollapsed;
	private static String pmg_iconDisabled;
	private static String pmg_label;
	private static String pmg_style;
	private static String pmg_styleClass;

	static {
		DEFAULT_ICON_MAP.put("chevron", "/panelMenuGroup/chevron.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP.put("chevronUp", "/panelMenuGroup/chevronUp.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP.put("chevronDown", "/panelMenuGroup/chevronDown.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP.put("triangle", "/panelMenuGroup/triangle.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP.put("triangleUp", "/panelMenuGroup/triangleUp.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP
				.put("triangleDown", "/panelMenuGroup/triangleDown.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP.put("disc", "/panelMenuGroup/disc.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		DEFAULT_ICON_MAP.put("grid", "/panelMenuGroup/grid.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		nsIDOMElement div = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_DIV);
		div.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR, EMPTY_DIV_STYLE);
		return new VpeCreationData(div);
	}

	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}

	public static VpeCreationData encode(VpePageContext pageContext,
			VpeCreationData creationData, Element sourceParentElement,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement parentVisualElement, List<String> activeIds,
			String childId) {
			
		/*
		 * Counts child groups in a parent group 
		 */
		int childGroupCount = 1;
		boolean disabled = false;
		Element parent = getRichPanelParent(sourceElement);
		
		readPanelMenuGroupAttributes(sourceElement);
		readPanelMenuAttributes(sourceParentElement);
		
		ComponentUtil.setCSSLink(pageContext, STYLE_PATH, NAME_COMPONENT);
		boolean expanded = activeIds.contains(childId);
		nsIDOMElement div = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_DIV);
		parentVisualElement.appendChild(div);
		div.setAttribute(COMPONENT_ATTR_VPE_SUPPORT, NAME_COMPONENT);
		div.setAttribute(COMPONENT_ATTR_VPE_USER_TOGGLE_ID, childId);

		if (TRUE.equalsIgnoreCase(pm_disabled)) {
			disabled = true;
		} else if (TRUE.equalsIgnoreCase(parent
				.getAttribute(DISABLED))) {
			disabled = true;
		} else if (TRUE.equalsIgnoreCase(pmg_disabled)) {
			disabled = true;
		}

		buildTable(pageContext, sourceParentElement, parent, sourceElement,
				visualDocument, div, expanded, disabled, childId);

		List<Node> children = ComponentUtil.getChildren(sourceElement);

		if (!children.isEmpty()) {
			nsIDOMElement childSpan = visualDocument
					.createElement(HtmlComponentUtil.HTML_TAG_SPAN);
			VpeChildrenInfo childrenInfo = new VpeChildrenInfo(childSpan);
			for (Node child : children) {
				if (!child.getNodeName().endsWith(PANEL_MENU_GROUP_END_TAG)
						&& !child.getNodeName().endsWith(
								PANEL_MENU_ITEM_END_TAG)) {
					if (childrenInfo.getSourceChildren() == null
							|| childrenInfo.getSourceChildren().size() == 0) {
						div.appendChild(childSpan);
					}
					childrenInfo.addSourceChild(child);
				} else {
					if (expanded && !disabled) {
						if (child.getNodeName().endsWith(
								PANEL_MENU_GROUP_END_TAG)) {
							RichFacesPanelMenuGroupTemplate.encode(pageContext,
									creationData, sourceParentElement,
									(Element) child, visualDocument, div, activeIds,
									childId + "-" + childGroupCount); //$NON-NLS-1$
									childGroupCount++;
						} else {
							RichFacesPanelMenuItemTemplate
									.encode(pageContext, creationData,
											sourceParentElement,
											(Element) child, visualDocument,
											div);
						}
					}

					if (childrenInfo.getSourceChildren() != null
							&& childrenInfo.getSourceChildren().size() > 0) {
						creationData.addChildrenInfo(childrenInfo);
						childSpan = visualDocument
								.createElement(HtmlComponentUtil.HTML_TAG_SPAN);
						childrenInfo = new VpeChildrenInfo(childSpan);
					}
				}
			}

			if (childrenInfo.getSourceChildren() != null
					&& childrenInfo.getSourceChildren().size() > 0) {
				creationData.addChildrenInfo(childrenInfo);
			}
		}
		return creationData;
	}

	private static final void buildTable(VpePageContext pageContext,
			Element sourceParentElement, Element parent, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement div, boolean expanded,
			boolean disabled, String activeChildId) {
		String disabledStyle = EMPTY;
		String disableClass = EMPTY;
		String style = EMPTY;
		String styleClass = EMPTY;

		nsIDOMElement table = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TABLE);
		div.appendChild(table);
		table.setAttribute(HtmlComponentUtil.HTML_CELLSPACING_ATTR, "0"); //$NON-NLS-1$
		table.setAttribute(HtmlComponentUtil.HTML_CELLPADDING_ATTR, "0"); //$NON-NLS-1$
		table.setAttribute(HtmlComponentUtil.HTML_BORDER_ATTR, "0"); //$NON-NLS-1$

		nsIDOMElement tableBody = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);
		table.appendChild(tableBody);

		nsIDOMElement column1 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		column1.setAttribute(COMPONENT_ATTR_VPE_USER_TOGGLE_ID, String
				.valueOf(activeChildId));
		tableBody.appendChild(column1);

		nsIDOMElement img1 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		column1.appendChild(img1);
		ComponentUtil.setImg(img1, PANEL_MENU_GROUP_ICON_SPACER_PATH);
		img1.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
				"rich-pmenu-group-icon"); //$NON-NLS-1$

		nsIDOMElement column2 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tableBody.appendChild(column2);
		column2.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR, "width: 100%;"); //$NON-NLS-1$

		nsIDOMText name = visualDocument.createTextNode(pmg_label);
		column2.appendChild(name);
		column2.setAttribute(COMPONENT_ATTR_VPE_USER_TOGGLE_ID, String
				.valueOf(activeChildId));

		nsIDOMElement column3 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		column3.setAttribute(COMPONENT_ATTR_VPE_USER_TOGGLE_ID, String
				.valueOf(activeChildId));
		tableBody.appendChild(column3);

		nsIDOMElement img2 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		column3.appendChild(img2);
		img2.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
				"rich-pmenu-group-icon"); //$NON-NLS-1$
		ComponentUtil.setImg(img2, PANEL_MENU_GROUP_ICON_SPACER_PATH);

		setIcon(pageContext, parent, sourceParentElement, sourceElement, img1,
				img2, expanded, disabled);

		boolean childOfPanelMenu = parent.getNodeName().endsWith(
				PANEL_MENU_END_TAG);

		if (childOfPanelMenu) {
			if (attrPresents(pm_topGroupClass)) {
				styleClass = "dr-pmenu-group-self-label dr-pmenu-top-group" //$NON-NLS-1$
						+ SPACE + pm_topGroupClass;
			} else {
				styleClass = "dr-pmenu-group-self-label dr-pmenu-top-group"; //$NON-NLS-1$
			}

			if (attrPresents(pm_topGroupStyle)) {
				style += pm_topGroupStyle;
			}
			div.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
					"dr-pmenu-top-group-div"); //$NON-NLS-1$
		} else {
			if (attrPresents(pm_groupClass)) {
				styleClass = "dr-pmenu-group-self-label dr-pmenu-group rich-pmenu-group-self-label" //$NON-NLS-1$
						+ SPACE + pm_groupClass;
			} else {
				styleClass = "dr-pmenu-group-self-label dr-pmenu-group rich-pmenu-group-self-label"; //$NON-NLS-1$
			}
			if (attrPresents(pm_groupStyle)) {
				style += pm_groupStyle;
			}
			div.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
					"dr-pmenu-top-self-div"); //$NON-NLS-1$
		}

		if (attrPresents(pmg_style)) {
			style += SPACE + pmg_style;
		}
		if (attrPresents(pm_styleClass)) {
			styleClass += SPACE + pm_styleClass;
		}
		if (attrPresents(pmg_styleClass)) {
			styleClass += SPACE + pmg_styleClass;
		}

		if (disabled) {
			if (attrPresents(pm_disabledGroupClass)) {
				disableClass += SPACE + pm_disabledGroupClass;
			}
			if (attrPresents(pmg_disabledClass)) {
				disableClass += SPACE + pmg_disabledClass;
			} 
			disableClass += SPACE + "rich-pmenu-group-disabled"; //$NON-NLS-1$
			styleClass += SPACE + disableClass;
			
			if (attrPresents(pm_disabledGroupStyle)) {
				disabledStyle += pm_disabledGroupStyle;
			}
			if (attrPresents(pmg_disabledStyle)) {
				disabledStyle += pmg_disabledStyle;
			}
			style += SPACE + disabledStyle;
		}
		
		if (!EMPTY.equals(style.trim())) {
			table.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR, style);
		}
		table.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, styleClass);
	}

	private static final Element getRichPanelParent(Element sourceElement) {
		Element parent = (Element) sourceElement.getParentNode();

		while (true) {
			if (parent.getNodeName().endsWith(PANEL_MENU_END_TAG)
					|| parent.getNodeName().endsWith(PANEL_MENU_GROUP_END_TAG)) {
				break;
			} else {
				parent = (Element) parent.getParentNode();
			}
		}

		return parent;
	}

	private static final void setIcon(VpePageContext pageContext, Node parent,
			Element sourceParentElement, Element sourceElement,
			nsIDOMElement img1, nsIDOMElement img2, boolean expanded,
			boolean disabled) {
		boolean needChangePosition = false;
		String pathIconExpanded = pmg_iconExpanded;
		String pathIconCollapsed = pmg_iconCollapsed;
		String pathIconDisabled = pmg_iconDisabled;

		if (parent.getNodeName().endsWith(PANEL_MENU_END_TAG)) {
			if (pathIconExpanded == null) {
				pathIconExpanded = pm_iconExpandedTopGroup;
			}
			if (pathIconCollapsed == null) {
				pathIconCollapsed = pm_iconCollapsedTopGroup;
			}
			if (pathIconDisabled == null) {
				pathIconDisabled = pm_iconTopDisableGroup;
			}

			if (RIGHT.equals(pm_iconGroupTopPosition)) {
				needChangePosition = true;
			}
		} else {
			if (pathIconExpanded == null) {
				pathIconExpanded = pm_iconExpandedGroup;
			}
			if (pathIconCollapsed == null) {
				pathIconCollapsed = pm_iconCollapsedGroup;
			}
			if (pathIconDisabled == null) {
				pathIconDisabled = pm_iconDisableGroup;
			}

			if (RIGHT.equals(pm_iconGroupPosition)) {
				needChangePosition = true;
			}
		}

		if (needChangePosition) {
			nsIDOMElement temp = img2;
			img2 = img1;
			img1 = temp;
		}

		if (disabled) {
			if (pathIconDisabled != null
					&& DEFAULT_ICON_MAP.containsKey(pathIconDisabled)) {
				pathIconDisabled = DEFAULT_ICON_MAP.get(pathIconDisabled);
				ComponentUtil.setImg(img1, pathIconDisabled);
			} else {
				ComponentUtil.setImgFromResources(pageContext, img1,
						pathIconDisabled, PANEL_MENU_GROUP_ICON_SPACER_PATH);
			}
		} else {
			if (expanded) {
				if (pathIconExpanded != null
						&& DEFAULT_ICON_MAP.containsKey(pathIconExpanded)) {
					pathIconExpanded = DEFAULT_ICON_MAP.get(pathIconExpanded);
					ComponentUtil.setImg(img1, pathIconExpanded);
				} else {
					ComponentUtil
							.setImgFromResources(pageContext, img1,
									pathIconExpanded,
									PANEL_MENU_GROUP_ICON_SPACER_PATH);
				}
			} else {
				if (pathIconCollapsed != null
						&& DEFAULT_ICON_MAP.containsKey(pathIconCollapsed)) {
					pathIconCollapsed = DEFAULT_ICON_MAP.get(pathIconCollapsed);
					ComponentUtil.setImg(img1, pathIconCollapsed);
				} else {
					ComponentUtil.setImgFromResources(pageContext, img1,
							pathIconCollapsed,
							PANEL_MENU_GROUP_ICON_SPACER_PATH);
				}
			}
		}
	}
	
	/**
	 * Read attributes from the source element.
	 * 
	 * @param sourceNode the source node
	 */
	private static void readPanelMenuAttributes(Element sourceParentElement) {
		
		if (null == sourceParentElement) {
			return;
		}
		
		/*
		 *	rich:panelMenu attributes for groups
		 */ 
		pm_iconGroupPosition = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_GROUP_POSITION);
		pm_iconGroupTopPosition = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_GROUP_TOP_POSITION);
		pm_iconCollapsedGroup = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_COLLAPSED_GROUP);
		pm_iconCollapsedTopGroup = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_COLLAPSED_TOP_GROUP);
		pm_iconExpandedGroup = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_EXPANDED_GROUP);
		pm_iconExpandedTopGroup = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_EXPANDED_TOP_GROUP);
		pm_iconDisableGroup = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_DISABLE_GROUP);
		pm_iconTopDisableGroup = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.ICON_TOP_DISABLE_GROUP);
		
		/*
		 *	rich:panelMenu style classes for groups
		 */ 
		pm_disabled = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.DISABLED);
		pm_disabledGroupClass = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.DISABLED_GROUP_CLASS);
		pm_disabledGroupStyle = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.DISABLED_GROUP_STYLE);
		pm_topGroupClass = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.TOP_GROUP_CLASS);
		pm_topGroupStyle = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.TOP_GROUP_STYLE);
		pm_groupClass = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.GROUP_CLASS);
		pm_groupStyle = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.GROUP_STYLE);
		pm_style = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.STYLE);
		pm_styleClass = sourceParentElement.getAttribute(RichFacesPanelMenuTemplate.STYLE_CLASS);
	}
	
	/**
	 * Read attributes from the source element.
	 * 
	 * @param sourceNode the source node
	 */
	private static void readPanelMenuGroupAttributes(Element sourceElement) {
		
		if (null == sourceElement) {
			return;
		}
		
		/*
		 * pich:panelMenuGroup attributes
		 */
		pmg_disabledStyle = sourceElement.getAttribute(DISABLED_STYLE);
		pmg_disabledClass = sourceElement.getAttribute(DISABLED_CLASS);
		pmg_disabled = sourceElement.getAttribute(DISABLED);
		pmg_iconExpanded = sourceElement.getAttribute(ICON_EXPANDED);
		pmg_iconCollapsed = sourceElement.getAttribute(ICON_COLLAPSED);
		pmg_iconDisabled = sourceElement.getAttribute(ICON_DISABLED);
		pmg_label = sourceElement.getAttribute(ICON_LABEL);
		pmg_style = sourceElement.getAttribute(STYLE);
		pmg_styleClass = sourceElement.getAttribute(STYLE_CLASS);
	}

	
    /**
     * Checks is attribute presents.
     * 
     * @param attr the attribute
     * 
     * @return true, if successful
     */
    private static boolean attrPresents(String attr) {
		return null != attr;
	}
}
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

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.template.util.RichFaces;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Template for Rich Faces DataTableScroller
 */
public class RichFacesDataTableScrollerTemplate extends VpeAbstractTemplate {


    private static final String COMPONENT_NAME = "richFacesDataTableScroller"; //$NON-NLS-1$
    private static final String STYLE_PATH = "dataTableScroller/dataTableScroller.css"; //$NON-NLS-1$

    private static final String RIGHT_DOUBLE_SCROLL_SYMBOL = "��"; //$NON-NLS-1$
    private static final String RIGHT_SINGLE_SCROLL_SYMBOL = "�"; //$NON-NLS-1$
    private static final String LEFT_DOUBLE_SCROLL_SYMBOL = "��"; //$NON-NLS-1$
    private static final String LEFT_SINGLE_SCROLL_SYMBOL = "�"; //$NON-NLS-1$

    /*
     * Minimal cells number in datascroller.
     */
    private static final int MINIMAL_CELLS_NUMBER = 5;
    /*
     * Default active datascroller page number.
     */
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private static final String CSS_RICH_DATASCR = "rich-datascr"; //$NON-NLS-1$
    private static final String CSS_RICH_DATASCROLLER_TABLE = "rich-dtascroller-table"; //$NON-NLS-1$
    private static final String CSS_RICH_DATASCR_BUTTON = "rich-datascr-button"; //$NON-NLS-1$
    private static final String CSS_RICH_DATASCR_CTRLS_SEPARATOR = "rich-datascr-ctrls-separator"; //$NON-NLS-1$
    private static final String CSS_RICH_DATASCR_ACT = "rich-datascr-act"; //$NON-NLS-1$
    private static final String CSS_RICH_DATASCR_INACT = "rich-datascr-inact"; //$NON-NLS-1$
    private static final String CSS_RICH_DATASCR_BUTTON_DSBLD = "rich-datascr-button-dsbld"; //$NON-NLS-1$

    private static final String ATTR_BOUNDARY_CONTROLS = "boundaryControls"; //$NON-NLS-1$
    private static final String ATTR_FAST_CONTROLS = "fastControls"; //$NON-NLS-1$
    private static final String ATTR_MAX_PAGES = "maxPages"; //$NON-NLS-1$
    private static final String ATTR_PAGE = "page"; //$NON-NLS-1$
    private static final String ATTR_STEP_CONTROLS = "stepControls"; //$NON-NLS-1$

    private static final String ATTR_INACTIVE_STYLE = "inactiveStyle"; //$NON-NLS-1$
    private static final String ATTR_INACTIVE_STYLE_CLASS = "inactiveStyleClass"; //$NON-NLS-1$
    private static final String ATTR_SELECTED_STYLE = "selectedStyle"; //$NON-NLS-1$
    private static final String ATTR_SELECTED_STYLE_CLASS = "selectedStyleClass"; //$NON-NLS-1$
    private static final String ATTR_TABLE_STYLE = "tableStyle"; //$NON-NLS-1$
    private static final String ATTR_TABLE_STYLE_CLASS = "tableStyleClass"; //$NON-NLS-1$

    private static final String ATTR_VALUE_SHOW = "show"; //$NON-NLS-1$

    private boolean showBoundaryControls;
    private boolean showFastControls;
    private int maxPages;
    private int page;
    private boolean showStepControls;
    private String inactiveStyle;
    private String inactiveStyleClass;
    private String selectedStyle;
    private String selectedStyleClass;
    private String tableStyle;
    private String tableStyleClass;
    private String style;
    private String styleClass;

    /**
     * 
     * Constructor.
     */
    public RichFacesDataTableScrollerTemplate() {
	super();
    }

    /**
     * Creates a node of the visual tree on the node of the source tree. This
     * visual node should not have the parent node This visual node can have
     * child nodes.
     * 
     * @param pageContext
     *            Contains the information on edited page.
     * @param sourceNode
     *            The current node of the source tree.
     * @param visualDocument
     *            The document of the visual tree.
     * @return The information on the created node of the visual tree.
     */
    public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
	    nsIDOMDocument visualDocument) {
	readAttributes(sourceNode);
	ComponentUtil.setCSSLink(pageContext, STYLE_PATH,
		COMPONENT_NAME);
	nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
	VpeCreationData creationData = new VpeCreationData(div);

	div.setAttribute(HTML.ATTR_ALIGN, HTML.VALUE_ALIGN_CENTER);
	div.setAttribute(HTML.ATTR_CLASS, styleClass);
	if (ComponentUtil.isNotBlank(style)) {
	    div.setAttribute(HTML.ATTR_STYLE, style);
	}

	nsIDOMElement table = visualDocument.createElement(HTML.TAG_TABLE);
	nsIDOMElement tbody = visualDocument.createElement(HTML.TAG_TBODY);
	nsIDOMElement tr = visualDocument.createElement(HTML.TAG_TR);
	tbody.appendChild(tr);
	table.appendChild(tbody);
	div.appendChild(table);

	table.setAttribute(HTML.ATTR_ALIGN, HTML.VALUE_ALIGN_CENTER);
	table.setAttribute(HTML.ATTR_CLASS, tableStyleClass);
	if (ComponentUtil.isNotBlank(tableStyle)) {
	    table.setAttribute(HTML.ATTR_STYLE, tableStyle);
	}
	table.setAttribute(HTML.ATTR_CELLSPACING, "1"); //$NON-NLS-1$
	table.setAttribute(HTML.ATTR_CELLPADDING, "0"); //$NON-NLS-1$
	table.setAttribute(HTML.ATTR_BORDER, "0"); //$NON-NLS-1$

	/*
	 * Create left side controls
	 */
	if (showBoundaryControls) {
	    createCell(visualDocument, tr, LEFT_DOUBLE_SCROLL_SYMBOL,
		    CSS_RICH_DATASCR_BUTTON + Constants.WHITE_SPACE
			    + CSS_RICH_DATASCR_BUTTON_DSBLD, Constants.EMPTY);
	}
	if (showFastControls) {
	    createCell(visualDocument, tr, LEFT_SINGLE_SCROLL_SYMBOL,
		    CSS_RICH_DATASCR_BUTTON + Constants.WHITE_SPACE
			    + CSS_RICH_DATASCR_BUTTON_DSBLD, Constants.EMPTY);
	}
	if (showStepControls) {
	    createCell(visualDocument, tr, Constants.EMPTY,
		    CSS_RICH_DATASCR_BUTTON + Constants.WHITE_SPACE
			    + CSS_RICH_DATASCR_BUTTON_DSBLD, Constants.EMPTY);
	}

	/*
	 * Create page numbers controls
	 */
	for (int i = 1; i <= maxPages; i++) {
	    createCell(visualDocument, tr, String.valueOf(i),
		    (i == 1 ? selectedStyleClass : inactiveStyleClass),
		    (i == 1 ? selectedStyle : inactiveStyle));
	}

	/*
	 * Create right side controls
	 */
	if (showStepControls) {
	    createCell(visualDocument, tr, Constants.EMPTY,
		    CSS_RICH_DATASCR_BUTTON, Constants.EMPTY);
	}
	if (showFastControls) {
	    createCell(visualDocument, tr, RIGHT_SINGLE_SCROLL_SYMBOL,
		    CSS_RICH_DATASCR_BUTTON, Constants.EMPTY);
	}
	if (showBoundaryControls) {
	    createCell(visualDocument, tr, RIGHT_DOUBLE_SCROLL_SYMBOL,
		    CSS_RICH_DATASCR_BUTTON, Constants.EMPTY);
	}

	return creationData;
    }

    /**
     * Creates the cell with central alignment.
     * 
     * @param visualDocument the visual document
     * @param tr the table row to add the cell
     * @param text the text in the cell
     * @param styleClass the style class for the cell
     * @param style the style for the cell
     */
    private void createCell(nsIDOMDocument visualDocument, nsIDOMElement tr,
	    String text, String styleClass, String style) {
	nsIDOMElement td = visualDocument.createElement(HTML.TAG_TD);
	td.setAttribute(HTML.ATTR_ALIGN, HTML.VALUE_ALIGN_CENTER);
	nsIDOMText cellText = visualDocument.createTextNode(text);

	if (ComponentUtil.isNotBlank(styleClass)) {
	    td.setAttribute(HTML.ATTR_CLASS, styleClass);
	}
	if (ComponentUtil.isNotBlank(style)) {
	    td.setAttribute(HTML.ATTR_STYLE, style);
	}

	td.appendChild(cellText);
	tr.appendChild(td);

    }

    /**
     * Read attributes from the source element.
     * 
     * @param sourceNode
     *            the source node
     */
    private void readAttributes(Node sourceNode) {

	Element sourceElement = (Element) sourceNode;
	String attrValue = null;

	showBoundaryControls = (!sourceElement
		.hasAttribute(ATTR_BOUNDARY_CONTROLS) || ATTR_VALUE_SHOW
		.equalsIgnoreCase(sourceElement
			.getAttribute(ATTR_BOUNDARY_CONTROLS)));

	showFastControls = (!sourceElement.hasAttribute(ATTR_FAST_CONTROLS) || ATTR_VALUE_SHOW
		.equalsIgnoreCase(sourceElement
			.getAttribute(ATTR_FAST_CONTROLS)));

	maxPages = ComponentUtil.parseNumberAttribute(sourceElement,
		ATTR_MAX_PAGES, MINIMAL_CELLS_NUMBER);

	page = ComponentUtil.parseNumberAttribute(sourceElement, ATTR_PAGE, DEFAULT_PAGE_NUMBER);

	showStepControls = (!sourceElement.hasAttribute(ATTR_STEP_CONTROLS) || ATTR_VALUE_SHOW
		.equalsIgnoreCase(sourceElement
			.getAttribute(ATTR_STEP_CONTROLS)));

	inactiveStyle = sourceElement.getAttribute(ATTR_INACTIVE_STYLE);

	inactiveStyleClass = CSS_RICH_DATASCR_INACT;
	attrValue = sourceElement.getAttribute(ATTR_INACTIVE_STYLE_CLASS);
	if (ComponentUtil.isNotBlank(attrValue)) {
	    inactiveStyleClass += Constants.WHITE_SPACE + attrValue;
	}

	selectedStyle = sourceElement.getAttribute(ATTR_SELECTED_STYLE);

	selectedStyleClass = CSS_RICH_DATASCR_ACT;
	attrValue = sourceElement.getAttribute(ATTR_SELECTED_STYLE_CLASS);
	if (ComponentUtil.isNotBlank(attrValue)) {
	    selectedStyleClass += Constants.WHITE_SPACE + attrValue;
	}

	tableStyle = sourceElement.getAttribute(ATTR_TABLE_STYLE);

	tableStyleClass = CSS_RICH_DATASCROLLER_TABLE;
	attrValue = sourceElement.getAttribute(ATTR_TABLE_STYLE_CLASS);
	if (ComponentUtil.isNotBlank(attrValue)) {
	    tableStyleClass += Constants.WHITE_SPACE + attrValue;
	}

	style = sourceElement.getAttribute(HTML.ATTR_STYLE);

	styleClass = CSS_RICH_DATASCR;
	attrValue = sourceElement.getAttribute(RichFaces.ATTR_STYLE_CLASS);
	if (ComponentUtil.isNotBlank(attrValue)) {
	    styleClass += Constants.WHITE_SPACE + attrValue;
	}

    }

    public boolean isRecreateAtAttrChange(VpePageContext pageContext,
	    Element sourceElement, nsIDOMDocument visualDocument,
	    nsIDOMElement visualNode, Object data, String name, String value) {
	return true;
    }

}
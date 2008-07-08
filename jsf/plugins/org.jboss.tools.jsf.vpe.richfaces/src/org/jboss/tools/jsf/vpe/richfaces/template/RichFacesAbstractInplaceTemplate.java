/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/


package org.jboss.tools.jsf.vpe.richfaces.template;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.template.util.RichFaces;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeToggableTemplate;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Base class for both {@link RichFacesInplaceInputTemplate} and {@link
 * RichFacesInplaceSelectTemplate}.
 * 
 * @author Eugene Stherbin
 * @see RichFacesInplaceInputTemplate
 * @see RichFacesInplaceSelectTemplate
 */
public abstract class RichFacesAbstractInplaceTemplate extends AbstractRichFacesTemplate implements VpeToggableTemplate {

    /**
     * 
     */
    private static final String CONTROLS_VERTICAL_POSITION_DEFAULT_VALUE = "center";

    /**
     * 
     */
    private static final String CONTROLS_HORIZONTAL_POSITION_DEFAULT_VALUE = "right";

    /** The Constant APPLY_BUTTON_GIF. */
    protected static final String APPLY_BUTTON_GIF = "/applyButton.gif";

    /** The Constant CANCEL_BUTTON_GIF. */
    protected static final String CANCEL_BUTTON_GIF = "/cancelButton.gif";

    /** The Constant controlsVerticalPositions. */
    protected final Map<String, String> controlsVerticalPositions = new HashMap<String, String>();

    /** The Constant DEFAULT_INPUT_WIDTH_VALUE. */
    protected static final String DEFAULT_INPUT_WIDTH_VALUE = "66px";

    /** The Constant DEFAULT_NULL_VALUE. */
    protected static final String DEFAULT_NULL_VALUE = "null";

    /** The Constant DEFAULT_VERTICAL_POSITION. */
    protected static final String DEFAULT_VERTICAL_POSITION = null;

    /** The Constant defaultButtonImages. */
    protected static final Map<String, String> defaultButtonImages = new HashMap<String, String>();

    /** The default style classes. */
    protected static final Map<String, String> defaultStyleClasses = new HashMap<String, String>();

    /** The Constant RICH_INPLACE_VIEW_DEFAULT_STYLE_CLASS. */
    protected static final String RICH_INPLACE_VIEW_DEFAULT_STYLE_CLASS = "rich-inplace-view";

    /** The Constant VPE_USER_TOGGLE_ID_ATTR. */
    public static final String VPE_USER_TOGGLE_ID_ATTR = "vpe-user-toggle-id";

    /** The button images. */
    protected final Map<String, String> buttonImages = new HashMap<String, String>();

    /** The controls horizontal position. */
    protected String controlsHorizontalPosition;

    /** The controls horizontal positions. */
    protected final Map<String, String> controlsHorizontalPositions = new HashMap<String, String>();

    /** The controls vertical position. */
    protected String controlsVerticalPosition;

    /** The default label. */
    protected String defaultLabel;

    /** The edit class. */
    protected String editClass;

    /** The is show input. */
    protected boolean isToggle = false;

    /** The show controls. */
    protected boolean showControls;

    /** The source value. */
    protected String sourceValue;

    /** The Constant SPACER_GIF. */
    protected final String SPACER_GIF = getCssExtension()+"/spacer.gif";

    /** The style class. */
    protected String styleClass;

    /** The view class. */
    protected String viewClass;

    /**
     * The Constructor.
     */
    public RichFacesAbstractInplaceTemplate() {
        super();
        initDefaultStyleClasses();
        initDefaultButtonImages();
        initPositions();
    }


    
    /**
     * Creates the root span template method.
     * 
     * @param visualDocument the visual document
     * @param source the source
     * 
     * @return the ns IDOM element
     */
    protected nsIDOMElement createRootSpanTemplateMethod(Element source, nsIDOMDocument visualDocument) {
        final nsIDOMElement rootSpan = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_SPAN);
        // if(!(this.showControls && this.isToggle)){
        rootSpan.setAttribute(VPE_USER_TOGGLE_ID_ATTR, String.valueOf(this.isToggle));
        // }
        final String rootClass = MessageFormat.format(defaultStyleClasses.get("rootSpan"), getRootSpanClasses());
        rootSpan.setAttribute(HTML.ATTR_CLASS, rootClass);
        String style = "";
        if (this.isToggle) {
            style = "position: relative;";
        }
        rootSpan.setAttribute(HTML.ATTR_STYLE, style);
        return rootSpan;

    }
    

    /**
     * Gets the css extension.
     * 
     * @return the css extension
     */
    protected abstract String getCssExtension();

    /**
     * Gets the css style.
     * 
     * @return the css style
     */
    protected abstract String getCssStyle();

    /**
     * Gets the css styles suffix.
     * 
     * @return the css styles suffix
     */
    protected abstract String getCssStylesSuffix();

    /**
     * Gets the root span classes.
     * 
     * @return the root span classes
     */
    protected abstract Object[] getRootSpanClasses();

    /**
     * Gets the value.
     * 
     * @return the value
     */
    protected String getValue() {
        String rst = "";
        if (ComponentUtil.isNotBlank(this.defaultLabel)) {
            rst = this.defaultLabel;
        } else if (ComponentUtil.isBlank(this.defaultLabel) && ComponentUtil.isNotBlank(this.sourceValue)) {
            rst = this.sourceValue;
        } else {
            rst = DEFAULT_NULL_VALUE;
        }
        return rst;
    }

    /**
     * Inits the default button images.
     */
    protected void initDefaultButtonImages() {
        if (defaultButtonImages.isEmpty()) {
            defaultButtonImages.put("cancelControlIcon", getCssExtension() + CANCEL_BUTTON_GIF);
            defaultButtonImages.put("saveControlIcon", getCssExtension() + APPLY_BUTTON_GIF);
        }

    }

    /**
     * Inits the default style classes.
     */
    protected void initDefaultStyleClasses() {
        if (defaultStyleClasses.isEmpty()) {
            defaultStyleClasses.put("rootSpan", "rich-inplace" + getCssStylesSuffix() + " {0} {1}");
        }

    }

    /**
     * Inits the positions.
     */
    protected void initPositions() {
        if (controlsVerticalPositions.isEmpty()) {
            controlsVerticalPositions.put("bottom", "18px");
            controlsVerticalPositions.put("top", "-12px");
            controlsVerticalPositions.put(CONTROLS_VERTICAL_POSITION_DEFAULT_VALUE, "0px");
        }
        if (controlsHorizontalPositions.isEmpty()) {
            controlsHorizontalPositions.put("left", "0px");
            controlsHorizontalPositions.put(CONTROLS_VERTICAL_POSITION_DEFAULT_VALUE, "53px");
        }
    }

    /**
     * Checks if is in key set.
     * 
     * @param controlsVerticalPosition2      *
     * param value2 the value2
     * @param value2 the value2
     * @param map the map
     * @param controlsVerticalPositions2      *
     * 
     * @return true, if is in key set
     */
    protected boolean isInKeySet(Map<String, String> map, String value2) {
        boolean rst = false;
        for (String key : map.keySet()) {
            if (key.equalsIgnoreCase(value2)) {
                rst = true;
                break;
            }
        }
        return rst;
    }

    /**
     * Checks if is recreate at attr change.
     * 
     * @param sourceElement the source element
     * @param value the value
     * @param visualDocument the visual document
     * @param visualNode the visual node
     * @param data the data
     * @param pageContext the page context
     * @param name the name
     * 
     * @return true, if is recreate at attr change
     */
    @Override
    public boolean isRecreateAtAttrChange(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument,
            nsIDOMElement visualNode, Object data, String name, String value) {
        return true;
    }

    /**
     * Prepare data.
     * 
     * @param source the source
     */
    protected void prepareData(Element source) {
        this.styleClass = source.getAttribute(RichFaces.ATTR_STYLE_CLASS);
        this.editClass = source.getAttribute("editClass");
        this.viewClass = source.getAttribute("viewClass");
        this.sourceValue = source.getAttribute(RichFaces.ATTR_VALUE);
        this.defaultLabel = source.getAttribute("defaultLabel");
        if (ComponentUtil.isBlank(this.sourceValue)) {
            this.sourceValue = DEFAULT_NULL_VALUE;
        }

        this.showControls = Boolean.parseBoolean(source.getAttribute("showControls"));
        this.controlsVerticalPosition = source.getAttribute("controlsVerticalPosition");
        if (ComponentUtil.isBlank(this.controlsVerticalPosition) || !isInKeySet(controlsVerticalPositions, this.controlsVerticalPosition)) {
            this.controlsVerticalPosition = CONTROLS_VERTICAL_POSITION_DEFAULT_VALUE;
        }
        this.controlsHorizontalPosition = source.getAttribute("controlsHorizontalPosition");

        if (ComponentUtil.isBlank(this.controlsHorizontalPosition)
                || !isInKeySet(controlsHorizontalPositions, this.controlsHorizontalPosition)) {
            this.controlsHorizontalPosition = CONTROLS_HORIZONTAL_POSITION_DEFAULT_VALUE;
        }

        prepareImages(source);

    }

    /**
     * Prepare images.
     * 
     * @param source the source
     */
    protected void prepareImages(Element source) {
        for (String key : defaultButtonImages.keySet()) {
            String value = ComponentUtil.getAttribute(source, key);
            // if (ComponentUtil.isNotBlank(value)) {
            // this.buttonImages.put(key, value);
            // } else {
            this.buttonImages.put(key, defaultButtonImages.get(key));
            // }
        }

    }

    /**
     * Read attributes.
     * 
     * @param source the source
     */
    protected void readAttributes(Element source) {
        this.styleClass = ComponentUtil.getAttribute(source, RichFaces.ATTR_STYLE_CLASS);
        this.sourceValue = ComponentUtil.getAttribute(source, RichFaces.ATTR_VALUE);

    }


    /**
     * Sets the up img.
     * 
     * @param i      * @param width the width
     * @param height the height
     * @param img the img
     * @param image the image
     * @param j      * @param border the border
     * @param td1Img      */
    protected void setUpImg(nsIDOMElement img, int width, int height, int border, String image) {
        ComponentUtil.setImg(img, image);
        img.setAttribute(HTML.ATTR_WIDTH, String.valueOf(width));
        img.setAttribute(HTML.ATTR_HEIGHT, String.valueOf(height));
        img.setAttribute(HTML.ATTR_BORDER, String.valueOf(border));

    }

    /**
     * Sets the up span root.
     * 
     * @param visualDocument the visual document
     * @param spanRoot the span root
     * @param source the source
     */
    protected void setUpSpanRoot(nsIDOMElement spanRoot, Element source, nsIDOMDocument visualDocument) {
        if (this.styleClass.length() > 0) {
            spanRoot.setAttribute(HTML.ATTR_CLASS, this.styleClass);
        } else {
            spanRoot.setAttribute(HTML.ATTR_CLASS, RICH_INPLACE_VIEW_DEFAULT_STYLE_CLASS);
        }
        String value = DEFAULT_NULL_VALUE;
        if (this.sourceValue.length() > 0) {
            value = this.sourceValue;
        }
        final nsIDOMText text = visualDocument.createTextNode(value);
        spanRoot.appendChild(text);
    }
    

    /**
     * Stop toggling.
     * 
     * @param sourceNode the source node
     * 
     * @see
     * org.jboss.tools.vpe.editor.template.VpeToggableTemplate#stopToggling(
     * org.w3c.dom.Node)
     */
    public void stopToggling(Node sourceNode) {
        this.isToggle = false;
    }

    /**
     * Toggle.
     * 
     * @param builder the builder
     * @param sourceNode the source node
     * @param toggleId the toggle id
     * 
     * @see
     * org.jboss.tools.vpe.editor.template.VpeToggableTemplate#toggle(org.jboss
     * .tools.vpe.editor.VpeVisualDomBuilder, org.w3c.dom.Node,
     * java.lang.String)
     */
    public void toggle(VpeVisualDomBuilder builder, Node sourceNode, String toggleId) {
        isToggle = !isToggle;

    }
    
    protected abstract String getCssStylesControlSuffix(); 
    
    
    protected abstract String getControlPositionsSubStyles();
    
    protected abstract String getMainControlsDivCssClass();
    

    /**
     * Creates the controls div.
     * 
     * @param visualDocument the visual document
     * @param sourceNode the source node
     * @param pageContext the page context
     * 
     * @return the ns IDOM element
     */
    protected nsIDOMElement createControlsDiv(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument) {
        final nsIDOMElement element = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_DIV);

        element.setAttribute(HTML.ATTR_CLASS, getMainControlsDivCssClass());
        element.setAttribute(HTML.ATTR_STYLE, "position: absolute; "+getControlPositionsSubStyles());

        final nsIDOMElement divShadov = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_DIV);

        divShadov.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-shadow");
        final nsIDOMElement divShadovTable = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TABLE);
        divShadovTable.setAttribute(HTML.ATTR_CELLPADDING, "0");
        divShadovTable.setAttribute(HTML.ATTR_CELLSPACING, "0");
        divShadovTable.setAttribute(HTML.ATTR_BORDER, "0");
        final nsIDOMElement divShadovTBody = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TBODY);
        final nsIDOMElement divShadovTr1 = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TR);
        final nsIDOMElement divShadovTr2 = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TR);
        final nsIDOMElement divShadovTd1 = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TD);
        final nsIDOMElement divShadovTd2 = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TD);
        final nsIDOMElement divShadovTd1Tr2 = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TD);
        final nsIDOMElement divShadovTd2Tr2 = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_TD);

        final nsIDOMElement td1Img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
        final nsIDOMElement td2Img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
        final nsIDOMElement td3Img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
        final nsIDOMElement td4Img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
        setUpImg(td1Img, 10, 1, 0, SPACER_GIF);
        setUpImg(td2Img, 1, 10, 0, SPACER_GIF);
        setUpImg(td3Img, 1, 10, 0, SPACER_GIF);
        setUpImg(td4Img, 10, 1, 0, SPACER_GIF);
        divShadovTd1.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-shadow-tl");
        divShadovTd2.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-shadow-tr");

        divShadovTd1Tr2.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-shadow-bl");
        divShadovTd2Tr2.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-shadow-br");

        final nsIDOMElement divButtons = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_DIV);
        divButtons.setAttribute(HTML.ATTR_STYLE, "position: relative; height: 18px;");
        final nsIDOMElement applyButtonImg = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_INPUT);

        applyButtonImg.setAttribute(HTML.ATTR_TYPE, "image");
        applyButtonImg.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-control");
        ComponentUtil.setImg(applyButtonImg, buttonImages.get("saveControlIcon"));
        applyButtonImg.setAttribute(VPE_USER_TOGGLE_ID_ATTR, String.valueOf(0));

        final nsIDOMElement cancelButtonImg = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_INPUT);

        cancelButtonImg.setAttribute(HTML.ATTR_TYPE, "image");
        cancelButtonImg.setAttribute(HTML.ATTR_CLASS, "rich-inplace"+getCssStylesSuffix()+"-control");
        cancelButtonImg.setAttribute(VPE_USER_TOGGLE_ID_ATTR, String.valueOf(0));
        ComponentUtil.setImg(cancelButtonImg, buttonImages.get("cancelControlIcon"));

        cancelButtonImg.setAttribute(HTML.ATTR_TYPE, "image");

        element.appendChild(divShadov);
        element.appendChild(divButtons);
        divButtons.appendChild(applyButtonImg);
        divButtons.appendChild(cancelButtonImg);
        divShadov.appendChild(divShadovTable);
        divShadovTable.appendChild(divShadovTBody);
        divShadovTBody.appendChild(divShadovTr1);
        divShadovTr1.appendChild(divShadovTd1);
        divShadovTd1.appendChild(td1Img);
        divShadovTr1.appendChild(divShadovTd2);
        divShadovTd2.appendChild(td2Img);

        divShadovTBody.appendChild(divShadovTr2);
        divShadovTr2.appendChild(divShadovTd1Tr2);
        divShadovTd1Tr2.appendChild(td3Img);
        divShadovTr2.appendChild(divShadovTd2Tr2);
        divShadovTd2Tr2.appendChild(td4Img);
        return element;
    }

}

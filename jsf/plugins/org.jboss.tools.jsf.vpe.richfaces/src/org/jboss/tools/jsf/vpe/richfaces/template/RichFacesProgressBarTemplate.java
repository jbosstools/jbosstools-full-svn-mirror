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


import java.util.List;

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.template.util.RichFaces;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Template for <rich:progressBar/> tag.
 * 
 * @author Eugene Stherbin
 */
public class RichFacesProgressBarTemplate extends AbstractRichFacesTemplate {

    /** The Constant CSS_EXTENSION. */
    private static final String CSS_EXTENSION = "progressBar";

    /** The Constant CSS_STYLE. */
    private static final String CSS_STYLE = "/progressBar.css";

    /** The Constant FACET. */
    private static final String FACET = "facet";

    /** The Constant OUTPUT_TEXT. */
    private static final String OUTPUT_TEXT = "outputText";

    /** The Constant PROGRESS_DIV_STYLE_CLASSES. */
    private static final String PROGRESS_DIV_STYLE_CLASSES = "rich-progress-bar-block rich-progress-bar-width rich-progress-bar-shell";

    /** The Constant TEXT_ALIGN_LEFT. */
    private static final String TEXT_ALIGN_LEFT = "; text-align:left;";

    /** The Constant UPLOADED_DIV. */
    private static final String UPLOADED_DIV = "rich-progress-bar-height rich-progress-bar-uploaded null";

    /** The percentage. */
    private String percentage = "60%";

    /** The style. */
    private String style;

    /** The style class. */
    private String styleClass;

    /**
     * Create.
     * 
     * @param visualDocument the visual document
     * @param sourceNode the source node
     * @param pageContext the page context
     * 
     * @return the vpe creation data
     * 
     * @see
     * org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools
     * .vpe.editor.context.VpePageContext, org.w3c.dom.Node,
     * org.mozilla.interfaces.nsIDOMDocument)
     */
    public VpeCreationData create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument) {

        ComponentUtil.setCSSLink(pageContext, getCssStyle(), getCssExtension());
        final Element source = (Element) sourceNode;
        prepareData(source);

        final nsIDOMElement progressDiv = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_DIV);
        final nsIDOMElement uploadDiv = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_DIV);
        String clazz = PROGRESS_DIV_STYLE_CLASSES;
        if (ComponentUtil.isNotBlank(this.styleClass)) {
            clazz = clazz + " " + this.styleClass;
        }
        progressDiv.setAttribute(HTML.ATTR_CLASS, clazz);
        progressDiv.setAttribute(HTML.ATTR_STYLE, this.style + TEXT_ALIGN_LEFT);
        uploadDiv.setAttribute(HTML.ATTR_CLASS, UPLOADED_DIV);

        uploadDiv.setAttribute(HTML.ATTR_STYLE, this.style + VpeStyleUtil.SEMICOLON_STRING + VpeStyleUtil.PARAMETER_WIDTH
                + VpeStyleUtil.COLON_STRING + this.percentage);
        // rootDiv.appendChild(progressDiv);
        progressDiv.appendChild(uploadDiv);
        List<Node> childrens = ComponentUtil.getChildren(source);
        final VpeCreationData data = new VpeCreationData(progressDiv);
        if (childrens.size() > 0) {
            final VpeChildrenInfo info = new VpeChildrenInfo(progressDiv);
            data.addChildrenInfo(info);
            for (Node n : childrens) {
                if (n.getNodeName().indexOf(FACET) > 1 || n.getNodeName().indexOf(OUTPUT_TEXT) > 1) {
                    info.addSourceChild(n);
                }
            }
        }

//        DOMTreeDumper dump = new DOMTreeDumper();
//        dump.dumpToStream(System.err, progressDiv);

        return data;
    }

    /**
     * Gets the css extension.
     * 
     * @return the css extension
     */
    private String getCssExtension() {
        return CSS_EXTENSION;
    }

    /**
     * Gets the css style.
     * 
     * @return the css style
     */
    private String getCssStyle() {
        return getCssExtension() + CSS_STYLE;
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
    private void prepareData(Element source) {
        this.styleClass = ComponentUtil.getAttribute(source, RichFaces.ATTR_STYLE_CLASS);
        this.style = ComponentUtil.getAttribute(source, HTML.ATTR_STYLE);

    }

}

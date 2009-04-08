package org.jboss.tools.jsf.vpe.seam.template;

import org.jboss.tools.jsf.vpe.jsf.template.util.ComponentUtil;
import org.jboss.tools.jsf.vpe.seam.template.util.Seam;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeDefineContainerTemplate;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SeamDecorateTemplate extends VpeDefineContainerTemplate {

    public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
	    nsIDOMDocument visualDocument) {
	
	Element sourceElement = (Element) sourceNode;
	
	/*
	 * Reading attributes
	 */
	String fileName = sourceElement.getAttribute(Seam.ATTR_TEMPLATE);
	String styleClass = sourceElement.getAttribute(Seam.ATTR_STYLE_CLASS);
	String style = sourceElement.getAttribute(HTML.ATTR_STYLE);
	
	/*
	 * Creating template
	 */
	VpeCreationData creationData = createTemplate(fileName, pageContext, sourceNode, visualDocument);
	
	/*
	 * Setting style and class attributes
	 */
	if (ComponentUtil.isNotBlank(style)) {
	    ((nsIDOMElement)creationData.getNode()).setAttribute(HTML.ATTR_STYLE, style);
	}
	if (ComponentUtil.isNotBlank(styleClass)) {
	    ((nsIDOMElement)creationData.getNode()).setAttribute(HTML.ATTR_CLASS, styleClass);
	}
	
	return creationData;
    }

}

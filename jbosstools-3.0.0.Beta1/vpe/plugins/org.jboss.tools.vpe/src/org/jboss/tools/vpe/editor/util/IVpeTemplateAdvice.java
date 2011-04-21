package org.jboss.tools.vpe.editor.util;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Node;

/**
 * The Interface IVpeTemplateAdvice.
 */
public interface IVpeTemplateAdvice {

    
    /**
     * After template created.
     * 
     * @param visualDocument the visual document
     * @param sourceNode the source node
     * @param pageContext the page context
     */
    void afterTemplateCreated(VpePageContext pageContext, nsIDOMElement sourceNode,
            nsIDOMDocument visualDocument);
    
    
    void beforeTemplateCreated(VpePageContext pageContext,Node sourceNode,nsIDOMDocument domDocument);
}

package org.jboss.tools.jsf.vpe.jsf.template;

import org.jboss.tools.jsf.vpe.jsf.template.util.JSF;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.AttributeData;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JsfInputTextTemplate extends AbstractEditableJsfTemplate {

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		Element sourceElement = (Element) sourceNode;

		nsIDOMElement input = visualDocument.createElement(HTML.TAG_INPUT);

		VpeCreationData creationData = new VpeCreationData(input);

		copyGeneralJsfAttributes(input, sourceElement);

		copyAttribute(input, sourceElement, JSF.ATTR_VALUE, HTML.ATTR_VALUE);
		copyAttribute(input, sourceElement, JSF.ATTR_SIZE, HTML.ATTR_SIZE);
		copyAttribute(input, sourceElement, JSF.ATTR_DIR, HTML.ATTR_DIR);

		VpeElementData elementData = new VpeElementData();
		if (sourceElement.hasAttribute(JSF.ATTR_VALUE)) {

			Attr attr = sourceElement.getAttributeNode(JSF.ATTR_VALUE);
			elementData
					.addNodeData(new AttributeData(attr, input, true));

		} else {

			elementData.addNodeData(new AttributeData(JSF.ATTR_VALUE,
					input, true));

		}
		creationData.setElementData(elementData);

		return creationData;
	}

	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}

}

package org.jboss.tools.vpe.html.template;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Base template for {@code <object>} and {@code <embed>} tags.
 * 
 * @author Yahor Radtsevich
 */
public abstract class HtmlFlashAbstractTemplate extends VpeAbstractTemplate {
	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		Element sourceElement = (Element) sourceNode;
		nsIDOMElement wrapper = VisualDomUtil
				.createBorderlessContainer(visualDocument, HTML.TAG_DIV);
		String wrapperStyle = "display:inline-block;";  //$NON-NLS-1$
		if (sourceElement.hasAttribute(HTML.ATTR_WIDTH)) {
			try {
				int width = Integer.parseInt(sourceElement.getAttribute(HTML.ATTR_WIDTH));
				wrapperStyle = VpeStyleUtil.setSizeInStyle(wrapperStyle,
						HTML.STYLE_PARAMETER_WIDTH, width);
			} catch (NumberFormatException e) {
			}
		}
		if (sourceElement.hasAttribute(HTML.ATTR_HEIGHT)) {
			try {
				int height = Integer.parseInt(sourceElement.getAttribute(HTML.ATTR_HEIGHT));
				wrapperStyle = VpeStyleUtil.setSizeInStyle(wrapperStyle,
						HTML.STYLE_PARAMETER_HEIGHT, height);
			} catch (NumberFormatException e) {
			}
		}

		wrapper.setAttribute(HTML.ATTR_STYLE, wrapperStyle);
		
		nsIDOMElement objectElement = visualDocument.createElement(getTagName());
		wrapper.appendChild(objectElement);
		VisualDomUtil.copyAttributes(sourceElement, objectElement, getAttributesToBeCopied());

		VpeChildrenInfo childrenInfo = new VpeChildrenInfo(objectElement);
		NodeList children = sourceElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childrenInfo.addSourceChild(children.item(i));
		}

		VpeCreationData creationData = new VpeCreationData(wrapper);
		creationData.addChildrenInfo(childrenInfo);

		return creationData;
	}
	
	public abstract String getTagName();
	public abstract List<String> getAttributesToBeCopied();

}

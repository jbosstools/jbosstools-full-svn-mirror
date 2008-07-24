package org.jboss.tools.vpe.editor.util;

import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FaceletUtil {

	public static final String TAG_COMPOSITION = "composition"; //$NON-NLS-1$
	public static final String TAG_COMPONENT = "component"; //$NON-NLS-1$

	public static final String ATTR_TEMPLATE = "template"; //$NON-NLS-1$

	/**
	 * facelet elements, if there are these elements on a page then other
	 * elements are deleted
	 */
	static public HashSet<String> componentElements = new HashSet<String>();

	static {
		componentElements.add(TAG_COMPOSITION); 
		componentElements.add(TAG_COMPONENT); 
	}

	/**
	 * 
	 * @param root
	 * @return
	 */
	public static Element findComponentElement(Element root) {

		if(root==null) {
			
			return null;
		}
		NodeList children = root.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				Element trimmedElement = findComponentElement((Element) child);
				if (trimmedElement != null)
					return trimmedElement;

			}
		}

		if (componentElements.contains(root.getLocalName()))
			return root;

		return null;
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	public static Element getRootFaceletElement(Document document) {

		Element root = document.getDocumentElement();

		Element component = findComponentElement(root);

		return component != null ? component : root;

	}

}

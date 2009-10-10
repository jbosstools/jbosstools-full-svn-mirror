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
package org.jboss.tools.vpe.editor.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.mozilla.interfaces.nsIAccessible;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSRange;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class VisualDomUtil {
	
	public static String JSF_CORE_URI = "http://java.sun.com/jsf/core"; //$NON-NLS-1$
    public static String JSF_HTML_URI = "http://java.sun.com/jsf/html"; //$NON-NLS-1$
    public static String RICH_FACES_URI = "http://richfaces.org/rich"; //$NON-NLS-1$
    public static String A4J_URI = "http://richfaces.org/a4j"; //$NON-NLS-1$
    public static String FACELETS_URI = "http://java.sun.com/jsf/facelets"; //$NON-NLS-1$

	private static final String ACCESSIBILITY_SERVICE_CONTRACT_ID = "@mozilla.org/accessibilityService;1"; //$NON-NLS-1$
//	private static Reference<nsIAccessibilityService> accessibilityServiceCache = null;
	
	private static Set<String> escapedTags;
	
	static {
		escapedTags = new HashSet<String>();
		escapedTags.add("f:facet"); //$NON-NLS-1$
		escapedTags.add("f:selectItem"); //$NON-NLS-1$
		escapedTags.add("f:selectItems"); //$NON-NLS-1$
	}

	static public nsIDOMNode getAncestorNode(nsIDOMNode visualNode, String tagName){
		if (tagName == null) return null;
		nsIDOMNode element = visualNode;
		while (true){
			if (tagName.equalsIgnoreCase(element.getNodeName())) {
				return element;
			}
			element = element.getParentNode();
			if (element == null) {
				break;
			}
		}
		return null;
	}

	public static Point getMousePoint(nsIDOMMouseEvent mouseEvent) {
		nsIDOMNSUIEvent uiEvent = (nsIDOMNSUIEvent) mouseEvent.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		return new Point(uiEvent.getPageX(), uiEvent.getPageY());
	}
	
	public static long getChildCount(nsIDOMNode node) {
		long count = 0;
		nsIDOMNodeList children = node.getChildNodes();
		if (children != null) {
			count = children.getLength();
		}
		return count;
	}
	
	public static nsIDOMNode getChildNode(nsIDOMNode node, long index) {
		nsIDOMNode child = null;
		nsIDOMNodeList children = node.getChildNodes();
		if (children != null && index >= 0 && index < children.getLength()) {
			child = children.item(index);
		}
		return child;
	}
	
	public static long getOffset(nsIDOMNode node) {
		long offset = 0;
		nsIDOMNode previousSibling = node;
		while ((previousSibling = previousSibling.getPreviousSibling()) != null) {
			offset++;
		}
		return offset;
	}
	
	public static nsIDOMNode getTargetNode(nsIDOMEvent event) {
		return (nsIDOMNode) event.getTarget().queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
	}
	
	public static boolean isSelectionContains(nsISelection selection, nsIDOMNode parent, int offset) {
		if (selection.getIsCollapsed()) {
			return false;
		}
		nsIDOMRange range = selection.getRangeAt(0);
		boolean inSelection = isRangeContains(range, parent, offset);
		if (inSelection) {
			nsIDOMNode endContainer = (nsIDOMNode)range.getEndContainer();
			if (endContainer.getNodeType() != Node.TEXT_NODE) {
				int endOffset = range.getEndOffset();
				inSelection = !(parent.equals(endContainer) && offset == endOffset);
			}
		}
		return inSelection;
	}

	public static boolean isRangeContains(nsIDOMRange range, nsIDOMNode parent, int offset) {
		nsIDOMNSRange domNSRange = (nsIDOMNSRange) range.queryInterface(nsIDOMNSRange.NS_IDOMNSRANGE_IID);
		boolean inRange = domNSRange.isPointInRange(parent, offset);
		return inRange;
	}
	
	/**
	 * Appends all children of the {@code node} to its parent at the point before the {@code node}
	 * and removes the {@code node} from the list of its parent's children.
	 * 
	 * @param node should be not null
	 */
	public static void replaceNodeByItsChildren(nsIDOMNode node) {
		final nsIDOMNodeList subTableContainerChildren = node.getChildNodes();
		final nsIDOMNode containerParent = node.getParentNode();
		if (subTableContainerChildren != null) {
			final int length = (int) subTableContainerChildren.getLength();
			for (int i = 0; i < length; i++) {
				final nsIDOMNode child = subTableContainerChildren.item(i);
				node.removeChild(child);
				containerParent.insertBefore(child, node);
			}
		}
		containerParent.removeChild(node);
	}
	
    /**
     * Sets the style of the attribute of the element to the value.
     * If the sub-attribute is present already, it replaces its value.
     * <P/>
     * Example: {@code <element attributeName="subAttributeName : subAttributeValue;">}
     * <P/>
     * Should be used mainly to set HTML STYLE attribute:
     *  {@code setSubAttribute(div, "STYLE", "width", "100%")} 
     */
	public static void setSubAttribute(nsIDOMElement element,
			String attributeName, String subAttributeName,
			String subAttributeValue) {
		String attributeValue = element.getAttribute(attributeName);
		if (attributeValue == null) {
		    attributeValue = ""; //$NON-NLS-1$
		} else {// remove old sub-attribute from the attributeValue
		    attributeValue = VpeStyleUtil.deleteFromString(attributeValue, 
		    		subAttributeName, Constants.SEMICOLON);
		}
		if (attributeValue.length() > 0) {
		    if (!attributeValue.endsWith(Constants.SEMICOLON))
			attributeValue += Constants.SEMICOLON;
		}

		attributeValue += subAttributeName + Constants.COLON
			+ subAttributeValue + Constants.SEMICOLON;

		element.setAttribute(attributeName, attributeValue);
	}

	/**
	 * Copies all attributes from source node to visual node.
	 * 
	 * @param sourceNode the source node
	 * @param visualElement the visual element
	 */
	public static void copyAttributes(Node sourceNode, nsIDOMElement visualElement) {
	    NamedNodeMap namedNodeMap = sourceNode.getAttributes();
	    for (int i = 0; i < namedNodeMap.getLength(); i++) {
	        Node attribute = namedNodeMap.item(i);
	        // added by Max Areshkau fix for JBIDE-1568
	        try {
	
	            visualElement.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
	        } catch (XPCOMException ex) {
	            // if error-code not equals error for incorrect name throws
	            // exception
	            // error code is NS_ERROR_DOM_INVALID_CHARACTER_ERR=0x80530005
	            if (ex.errorcode != 2152923141L) {
	                throw ex;
	            }
	            // else we ignore this exception
	        }
	    }
	}

	/**
	 * Copies all attributes from source node to visual node.
	 * 
	 * @param sourceElement the source element
	 * @param attributes list names of attributes which will copy
	 * @param visualElement the visual element
	 */
	public static void copyAttributes(Element sourceElement, List<String> attributes, nsIDOMElement visualElement) {
	
	    if (attributes == null)
	        return;
	
	    for (String attributeName : attributes) {
	
	        String attributeValue = sourceElement.getAttribute(attributeName);
	        if (attributeValue != null)
	            visualElement.setAttribute(attributeName, attributeValue);
	    }
	
	}
	
	/**
	 * Returns instance of {@link nsIAccessibilityService}.
	 * 
	 * @author yradtsevich
	 */
//	public static nsIAccessibilityService getAccessibilityService() {
//		nsIAccessibilityService accessibilityService = null;
//		if (accessibilityServiceCache != null) {
//			// get accessibilityService from cache
//			accessibilityService = accessibilityServiceCache.get();
//		}
//		if (accessibilityService == null) {
//			accessibilityService = (nsIAccessibilityService) Mozilla.getInstance()
//				.getServiceManager()
//				.getServiceByContractID(ACCESSIBILITY_SERVICE_CONTRACT_ID,
//	        		nsIAccessibilityService.NS_IACCESSIBILITYSERVICE_IID);
//			
//			// cache accessibilityService
//			accessibilityServiceCache = new SoftReference<nsIAccessibilityService>(accessibilityService);
//		}
//		return accessibilityService;
//	}
	
	/**
	 * Returns on screen bounds of the {@code node}
	 * 
	 * @param node cannot be {@code null}
	 * @return bounds of the {@code node} or {@code null} if it is not accessible
	 * 
	 * @author yradtsevich
	 */
	public static Rectangle getBounds(nsIDOMNode node) {
		Rectangle bounds = null;

		nsIAccessible accessible = (nsIAccessible) node.queryInterface(nsIAccessible.NS_IACCESSIBLE_IID);
		if (accessible != null) {
			int[] xArray      = new int[1]; // Left hand corner of the node
			int[] yArray      = new int[1]; // Top corner of the node
			int[] widthArray  = new int[1]; // Width of the node
			int[] heightArray = new int[1]; // Height of the node
			
			accessible.getBounds(xArray, yArray, widthArray, heightArray);
			bounds = new Rectangle(xArray[0], yArray[0], widthArray[0], heightArray[0]);
		}
		
		return bounds;
	}
	
	/**
	 * Creates HTML container (element) borders of that are
	 * complete invisible to user.
	 * <P>
	 * It can be used to wrap visual HTML elements and text nodes without
	 * changing of their view in VPE.
	 * <P>
	 * Tag <code>span</code> is used as a default container.
	 *   
	 * @param visualDocument the visual document. It is used to create the container.
	 * @return created borderless container
	 * @author yradtsevich
	 * @see #createBorderlessContainer(nsIDOMDocument, String)
	 */
	public static nsIDOMElement createBorderlessContainer(
			nsIDOMDocument visualDocument) {
		return createBorderlessContainer(visualDocument, HTML.TAG_SPAN);
	}
	
	/**
	 * Creates a HTML tag that is used as a borderless container 
	 * for other elements. 
	 * 
	 * @param visualDocument the visual document.
	 * @param containerName the name of the tag, that will be used as a container.
	 * 
	 * @return created borderless container
	 */
	public static nsIDOMElement createBorderlessContainer(
			nsIDOMDocument visualDocument, String containerName) {
		nsIDOMElement element = visualDocument.createElement(containerName);
	    element.setAttribute(HTML.ATTR_CLASS, HTML.CLASS_VPE_TEXT);
		return element;
	}

	/**
	 * Appends a container for {@code source}'s children
	 * to {@code target} and adds a new object of {@link VpeChildrenInfo} 
	 * in a way that all children will be placed in this container.
	 * @param source source element, cannot be {@code null}
	 * @param target target element, cannot be {@code null}
	 * @param creationData the creation data, cannot be {@code null}
	 * @param visualDocument the visual document, cannot be {@code null}
	 */
	public static void appendChildrenInsertionPoint(Element source,
			nsIDOMElement target, VpeCreationData creationData, nsIDOMDocument visualDocument) {
		nsIDOMElement childrenContainer = createBorderlessContainer(visualDocument);
		target.appendChild(childrenContainer);
		
		VpeChildrenInfo childrenInfo = new VpeChildrenInfo(childrenContainer);

		NodeList childNodes = source.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			childrenInfo.addSourceChild(childNodes.item(i));
		}

		creationData.addChildrenInfo(childrenInfo);
	}

	/**
	 * Creates visual tag with additional text container
	 * 
	 * @param sourceElement
	 *            the source element
	 * @param templateContainer
	 *            visual tag that contains template element
	 * @param borderlessContainerName
	 *            the name of the borderless container
	 * @param visualDocument
	 *            the visual document
	 * @return created {@code VpeCreationData} based on specified container name
	 *         or template container.
	 */
	public static VpeCreationData createTemplateWithTextContainer(
			Element sourceElement, nsIDOMElement templateContainer,
			String borderlessContainerName, nsIDOMDocument visualDocument) {
		List<Node> children = new ArrayList<Node>();
		VpeCreationData creationData = null;
        NodeList nodeList = sourceElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            /*
             * Do not display escaped tags.
             * These tags are correct and should be processed 
             * by templates themselves. 
             */
            if (!escapedTags.contains(child.getNodeName()) ) {
                children.add(child);
            }
        }
		if (children != null && children.size() > 0) {
	        nsIDOMElement topContainer = createBorderlessContainer(visualDocument, borderlessContainerName);
	        nsIDOMElement textContainer = createBorderlessContainer(visualDocument, borderlessContainerName);
			topContainer.appendChild(textContainer);
			topContainer.appendChild(templateContainer);
			creationData = new VpeCreationData(topContainer);
			VpeChildrenInfo textInfo = new VpeChildrenInfo(textContainer);
			creationData.addChildrenInfo(textInfo);
			for (Node child : children) {
				textInfo.addSourceChild(child);
			}
		} else {
			creationData = new VpeCreationData(templateContainer);
		}
		return creationData;
	}
}

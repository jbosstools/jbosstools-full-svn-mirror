/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.base.test;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.tools.common.model.ui.util.StringUtil;
import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.css.common.CSSStyleManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ElementCSSInlineStyle;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class TestDomUtil {
	/**
	 * Attributes names that will be skipped in attribute comparison.
	 */
	public static final Set<String> skippedAtributes = new HashSet<String>();
	static {
		// Add here all attributes names to be skipped (IN UPPER CASE!)		
		skippedAtributes.addAll(Arrays.asList("DIR"));//$NON-NLS-1$
	}
	
	final public static String ID_ATTRIBUTE = "id"; //$NON-NLS-1$

	final public static String ILLEGAL_ATTRIBUTES = "illegalAttributes"; //$NON-NLS-1$

	final public static String ILLEGAL_ATTRIBUTES_SEPARATOR = Constants.COMMA;

	final public static String START_REGEX = "/"; //$NON-NLS-1$

	final public static String END_REGEX = "/"; //$NON-NLS-1$

	public static Document getDocument(File file) throws FileNotFoundException {
		// create reader
		FileReader reader = new FileReader(file);
		try {
			// return document
			return XMLUtilities.getDocument(reader, null);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				VPEBaseTestPlugin.getDefault().logError(e);
			}
		}
	}

	public static Document getDocument(String content)
			throws FileNotFoundException {
		// create reader
		StringReader reader = new StringReader(content);

		// return document
		return XMLUtilities.getDocument(reader, null);
	}

	/**
	 * 
	 * @param document
	 * @param elementId
	 * @return
	 */
	public static Element getElemenById(Document document, String elementId) {

		Element element = document.getDocumentElement();

		NodeList children = element.getChildNodes();
		int length = children.getLength();
		for (int i = 0; i < length; i++) {
			Node child = children.item(i);
			if ((child.getNodeType() == Node.ELEMENT_NODE)
					&& elementId.equals(((Element) child)
							.getAttribute(ID_ATTRIBUTE)))
				return (Element) child;

		}

		return null;

	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static Element getFirstChildElement(Element element) {

		if (element != null) {
			NodeList children = element.getChildNodes();
			int length = children.getLength();
			for (int i = 0; i < length; i++) {
				Node child = children.item(i);

				if (child.getNodeType() == Node.ELEMENT_NODE)
					return (Element) child;

			}
		}
		return null;

	}

	/**
	 * 
	 * @param vpeNode
	 * @param schemeNode
	 * @return
	 * @throws DOMComparisonException
	 */
	public static void compareNodes(nsIDOMNode vpeNode, Node modelNode)
			throws DOMComparisonException {

		if (!modelNode.getNodeName().equalsIgnoreCase(vpeNode.getNodeName())) {
			throw new DOMComparisonException("name of tag is \"" //$NON-NLS-1$
					+ vpeNode.getNodeName() + "\"but must be \"" //$NON-NLS-1$
					+ modelNode.getNodeName() + "\"", modelNode); //$NON-NLS-1$
		}
		if ((modelNode.getNodeValue() != null)
				&& (!modelNode.getNodeValue().trim().equalsIgnoreCase(
						vpeNode.getNodeValue().trim()))) {
			throw new DOMComparisonException("value of " + vpeNode.getNodeName() //$NON-NLS-1$
					+ " is \"" + vpeNode.getNodeValue().trim() //$NON-NLS-1$
					+ "\" but must be \"" + modelNode.getNodeValue().trim() //$NON-NLS-1$
					+ "\"", modelNode); //$NON-NLS-1$
		}

		// compare node's attributes
		if (modelNode.getNodeType() == Node.ELEMENT_NODE) {
			compareAttributes(modelNode.getAttributes(), vpeNode
					.getAttributes());
		}

		// compare children
		nsIDOMNodeList vpeChildren = vpeNode.getChildNodes();
		NodeList schemeChildren = modelNode.getChildNodes();
		int realCount = 0;
		int length = schemeChildren.getLength();
		for (int i = 0; i < length; i++) {

			Node schemeChild = schemeChildren.item(i);

			// leave out empty text nodes in test dom model
			String nodeValue = schemeChild.getNodeValue();
			if ((schemeChild.getNodeType() != Node.TEXT_NODE)
					|| ((nodeValue != null) && (nodeValue.trim().length() > 0))) {
				
				nsIDOMNode vpeChild = vpeChildren.item(realCount++);
	
				if (null == vpeChild) {
					throw new DOMComparisonException(
							"Child of node \"" //$NON-NLS-1$
									+ vpeNode.getNodeName()
									+ "\" is \"null\", but should be \"" + schemeChild.getNodeName() + "\"",//$NON-NLS-1$ //$NON-NLS-2$
							schemeChild); 
				}
				// leave out empty text nodes in vpe dom model
				while (((vpeChild.getNodeType() == Node.TEXT_NODE) && ((vpeChild
						.getNodeValue() == null) || (vpeChild.getNodeValue().trim()
						.length() == 0)))) {
					vpeChild = vpeChildren.item(realCount++);
					if (null == vpeChild) {
						throw new DOMComparisonException(
								"Child of node \"" //$NON-NLS-1$
										+ vpeNode.getNodeName()
										+ "\" is \"null\", but should be \"" + schemeChild.getNodeName() + "\"",//$NON-NLS-1$ //$NON-NLS-2$
								schemeChild); 
					}
				}
				compareNodes(vpeChild, schemeChild);
			}
		}

	}

	/**
	 * get ids of tests
	 * 
	 * @param testDocument
	 * @return
	 */
	public static List<String> getTestIds(Document testDocument) {
		Element rootElement = testDocument.getDocumentElement();
		List<String> ids = new ArrayList<String>();
		NodeList children = rootElement.getChildNodes();
		int length = children.getLength();
		for (int i = 0; i < length; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				ids.add(((Element) child).getAttribute(ID_ATTRIBUTE));

		}
		return ids;
	}

	private static void compareAttributes(NamedNodeMap modelAttributes,
			nsIDOMNamedNodeMap vpeAttributes) throws DOMComparisonException {

		for (int i = 0; i < modelAttributes.getLength(); i++) {
			Attr modelAttr = (Attr) modelAttributes.item(i);
			String name = modelAttr.getName();
			// if the attribute has to be skipped, then do it
			if ( name != null 
					&& skippedAtributes.contains(name.toUpperCase()) ) {
				continue;
			}
			// if there are limitation of attributes
			if (ILLEGAL_ATTRIBUTES.equals(name)) {
				String[] illegalAttributes = modelAttr.getNodeValue().split(
						ILLEGAL_ATTRIBUTES_SEPARATOR);
				for (String illegalAttributeName : illegalAttributes) {
					if (vpeAttributes.getNamedItem(illegalAttributeName.trim()) != null)
						throw new DOMComparisonException("illegal attribute :" //$NON-NLS-1$
								+ illegalAttributeName, modelAttr);
				}
			} else {
				if (vpeAttributes.getNamedItem(name) == null) {
					throw new DOMComparisonException("there is not : \"" + name //$NON-NLS-1$
							+ "\" attribute", modelAttr); //$NON-NLS-1$
				}
				nsIDOMAttr vpeAttr = queryInterface(
						vpeAttributes.getNamedItem(name), nsIDOMAttr.class);
				/*
				 * By default every attribute show pass through
				 * compareComplexStrings(..) method.
				 * For "style" attribute there is a separate comparison.  
				 */
				String xmlAttrValue = modelAttr.getNodeValue();
				boolean performRegExComparison =
						xmlAttrValue.startsWith(START_REGEX)
							&& xmlAttrValue.endsWith(END_REGEX);
				if (HTML.ATTR_STYLE.equalsIgnoreCase(name) && !performRegExComparison) {
					compareStyle(modelAttr, vpeAttr);
				} else if (performRegExComparison) {
					compareRegExAttribute(modelAttr, vpeAttr);
				} else { // perform simple comparison
					String modelString = modelAttr.getNodeValue().trim();
					String vpeString = vpeAttr.getNodeValue().trim();
					if (!modelString.equals(vpeString)) {
						throw new DOMComparisonException("string is\"" + vpeString //$NON-NLS-1$
								+ "\" but must be \"" + modelString + "\"", modelAttr); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
	}

	private static void compareStyle(Attr modelAttr, nsIDOMAttr vpeAttr)
			throws DOMComparisonException {
		/*
		 * Parse style attribute value
		 */
		Map<String, String> vpeStyle = CSSStyleManager
				.getStyleAttributes(vpeAttr.getNodeValue());
		Map<String, String> xmlStyle = CSSStyleManager
				.getStyleAttributes(modelAttr.getNodeValue());
		/*
		 * Major condition is that
		 * all styles from the xml file should present 
		 * in the style attribute of the vpe element. 
		 */
		if (xmlStyle.size() > vpeStyle.size()) {
			throw new DOMComparisonException(
					"VPE element has less style parameters [" //$NON-NLS-1$
							+ vpeStyle.size()
							+ "] than was specified [" //$NON-NLS-1$
							+ xmlStyle.size() + "]."  //$NON-NLS-1$ 
							+ "\n Expected: " + xmlStyle //$NON-NLS-1$ 
							+ "\n Was: " + vpeStyle, //$NON-NLS-1$ 
					modelAttr); 
		} else {
			if ((xmlStyle.size() > 0) && (vpeStyle.size() > 0)) {
				for (String key : xmlStyle.keySet()) {
					if (vpeStyle.containsKey(key)) {
						if (!areStylePropertiesEqual(xmlStyle.get(key), vpeStyle.get(key))) {
							throw new DOMComparisonException(
									"Style value for parameter [" //$NON-NLS-1$
									+ key
									+ "] is different. Expected [" //$NON-NLS-1$
									+ xmlStyle.get(key)
									+ "] but was [" //$NON-NLS-1$
									+ vpeStyle.get(key)
									+ "]", modelAttr); //$NON-NLS-1$
						}
					} else {
						throw new DOMComparisonException(
								"Style parameter [" //$NON-NLS-1$
								+ key
								+ "] is missing in the VPE element", //$NON-NLS-1$
								modelAttr);
					}
				}
			}
		}
	}

	/**
	 * Compares style properties, performing comparisons word by word.
	 * 
	 * So the property {@code "grey none repeat scroll 0% 0%"} will be equal
	 * to {@code "none repeat scroll 0% 0% grey"} 
	 */
	private static boolean areStylePropertiesEqual(String property1, String property2) {
		List<String> propertyParts1 = splitAndSort(property1);
		List<String> propertyParts2 = splitAndSort(property2);
		
		return propertyParts1.equals(propertyParts2);		
	}
	
	private static List<String> splitAndSort(String property) {
		List<String> propertyParts = new ArrayList<String>();
		for (String propertyPart : property.split("[\\s]+")) {
			if (!propertyPart.isEmpty()) {
				propertyParts.add(propertyPart);
			}
		}
		Collections.sort(propertyParts);
		
		return propertyParts;
	}

	static private void compareRegExAttribute(Attr modelAttr, nsIDOMAttr vpeAttr)
			throws DOMComparisonException {
		String modelString = modelAttr.getNodeValue().trim();
		String vpeString = vpeAttr.getNodeValue().trim();

		String regex = modelString.substring(START_REGEX.length(),
				modelString.length() - END_REGEX.length());
		int firstPos = regex.indexOf("url\\("); //$NON-NLS-1$
		if (firstPos > -1) {
			String subString = regex.substring(firstPos + 5, firstPos + 5 + 2);
			if (!"\"?".equalsIgnoreCase(subString)) { //$NON-NLS-1$
				String firstPart = regex.substring(0, firstPos + 5);
				String secondPart = regex.substring(firstPos + 5, regex.length());
				int lastPos = secondPart.indexOf("\\)"); //$NON-NLS-1$
				if (lastPos > -1) {
					String subs = secondPart.substring(lastPos - 2, lastPos);
					if (!"\"?".equalsIgnoreCase(subs)) { //$NON-NLS-1$
						String fpart = secondPart.substring(0, lastPos);
						String spart = secondPart.substring(lastPos, secondPart.length());
						regex = firstPart + "\"?" + fpart + "\"?" + spart; //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
		Matcher matcher = Pattern.compile(regex).matcher(vpeString);
		if (!matcher.find()) {
			throw new DOMComparisonException("string is\"" + vpeString //$NON-NLS-1$
					+ "\" but pattern is \"" + regex + "\"", modelAttr); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * is created to be sure that attributes/parameters will be correctly
	 * compared ( ignore case )
	 * 
	 * @param list
	 * @param string
	 * @return
	 */
	static private boolean findIgnoreCase(String[] strings,
			String requiredString) {
		for (String string : strings) {
			if (string.equalsIgnoreCase(requiredString))
				return true;
		}

		return false;
	}
}

/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Class created for processing jsf 2.0 resources, see following issues
 * JBIDE-2550, JBIDE-4812,
 * 
 * @author mareshkau
 */
public class Jsf2ResourceUtil {
	private static final Pattern resourcePatternWithSinglCoat = Pattern
			.compile("[#\\$]\\{\\s*resource\\s*\\[\\s*'(.*)'\\s*\\]\\s*\\}"); //$NON-NLS-1$
	private static final Pattern resourcePatternWithDoableCoat = Pattern
			.compile("[#\\$]\\{\\s*resource\\s*\\[\\s*\"(.*)\"\\s*\\]\\s*\\}"); //$NON-NLS-1$
	private static final Pattern jsfExternalContextPath = Pattern
			.compile("^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}"); //$NON-NLS-1$
	/**
	 * Check if node contains attributes like this src="#{facesContext.externalContext.requestContextPath}/images/sample.gif"
	 * @param sourceNode
	 * @return true if node contains #{facesContext.externalContext.requestContextPath}/images/sample.gif
	 * @author mareshkau, fix for https://jira.jboss.org/jira/browse/JBIDE-5985
	 */
	public static boolean isContainJSFExternalContextPath(Node sourceNode) {
		boolean result = false;
		if (sourceNode.getNodeType() == Node.TEXT_NODE) {
			String textValue = sourceNode.getNodeValue();
			if (textValue != null) {
				if (Jsf2ResourceUtil.isExternalContextPathString(textValue)) {
					result = true;
				}
			}
		} else {
			final NamedNodeMap nodeMap = sourceNode.getAttributes();
			if ((nodeMap != null) && (nodeMap.getLength() > 0)) {
				for (int i = 0; i < nodeMap.getLength(); i++) {
					if (Jsf2ResourceUtil
							.isExternalContextPathString(((Attr) nodeMap
									.item(i)).getValue())) {
						result = true;

					}
				}
			}
		}
		return result;
	}
	/**
	 * Checks string for jsf declaration
	 * @param attributeValue
	 * @return true is string contains #{facesContext.externalContext.requestContextPath}
	 * @author mareshkau, fix for https://jira.jboss.org/jira/browse/JBIDE-5985
	 */
	public static boolean isExternalContextPathString(String attributeValue) {
		Matcher externalContextPathMatcher = jsfExternalContextPath
				.matcher(attributeValue);
		boolean result = false;
		if (externalContextPathMatcher.find()) {
			result = true;
		}
		return result;
	}

	/**
	 * Checks is node contained jsf attributes declaration
	 * 
	 * @param sourceNode
	 * @return true if node has #{resource[...]} declarations false otherwise
	 * @author mareshkau
	 */
	public static boolean isContainJSF2ResourceAttributes(Node sourceNode) {
		boolean result = false;
		if (sourceNode.getNodeType() == Node.TEXT_NODE) {
			String textValue = sourceNode.getNodeValue();
			if (textValue != null) {
				if (Jsf2ResourceUtil.isJSF2ResourceString(textValue)) {
					result = true;
				}
			}
		} else {
			final NamedNodeMap nodeMap = sourceNode.getAttributes();
			if ((nodeMap != null) && (nodeMap.getLength() > 0)) {
				for (int i = 0; i < nodeMap.getLength(); i++) {
					if (Jsf2ResourceUtil.isJSF2ResourceString(((Attr) nodeMap
							.item(i)).getValue())) {
						result = true;

					}
				}
			}
		}
		return result;
	}

	/**
	 * Replaces custom jsf attribute with attribute from VPE
	 * 
	 * @param pageContext
	 * @param value
	 * @return
	 */
	public static final String processCustomJSFAttributes(
			VpePageContext pageContext, String value) {
		String result = null;
		// fix for JBIDE-2550, author Maksim Areshkau
		Matcher singleCoatMatcher = resourcePatternWithSinglCoat.matcher(value);
		Matcher doubleCoatMatcher = resourcePatternWithDoableCoat
				.matcher(value);
		if (doubleCoatMatcher.find()) {
			result = FileUtil.getJSF2ResourcePath(pageContext,
					doubleCoatMatcher.group(1));
		} else if (singleCoatMatcher.find()) {
			result = FileUtil.getJSF2ResourcePath(pageContext,
					singleCoatMatcher.group(1));
		}

		return result;
	}

	/**
	 * Checks if string is jsf 2 resource
	 * 
	 * @param attributeValue
	 * @return
	 */
	public static boolean isJSF2ResourceString(String attributeValue) {
		Matcher singleCoatMatcher = resourcePatternWithSinglCoat
				.matcher(attributeValue);
		Matcher doubleCoatMatcher = resourcePatternWithDoableCoat
				.matcher(attributeValue);
		boolean result = false;
		if (doubleCoatMatcher.find() || singleCoatMatcher.find()) {
			result = true;
		}
		return result;
	}
	/**
	 * Replaced "^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}" with ""
	 * @param value
	 * @return replaced "^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}" with ""
	 */
	public static String processExternalContextPath(String value) {
		return value.replaceFirst("^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}
}

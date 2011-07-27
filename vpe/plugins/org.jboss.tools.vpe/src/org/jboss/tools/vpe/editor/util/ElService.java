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

package org.jboss.tools.vpe.editor.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.el.core.GlobalELReferenceList;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.jst.jsp.bundle.BundleMapUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * @author Eugeny Stherbin
 * @author Vitali Yemialyanchyk
 */
public final class ElService {

	public static final String DOLLAR_PREFIX = "${"; //$NON-NLS-1$

	private static final String SUFFIX = "}"; //$NON-NLS-1$

	public static final String SHARP_PREFIX = "#{"; //$NON-NLS-1$

	/**
	 * Replace el.
	 * 
	 * @param resourceFile
	 *            the resource file
	 * @param resourceString
	 *            the resource string
	 * 
	 * @return the string
	 * 
	 * @see IELService#replaceEl(IFile, String)
	 */
	public static String replaceEl(IFile file, String resourceString) {
		if (resourceString == null) {
			return ""; //$NON-NLS-1$
		}
		String rst = resourceString;
		ResourceReference[] references = getAllResources(file);
		if (references == null || references.length == 0) {
			return rst;
		}
		references = sortReferencesByScope2(references);
		rst = replace(resourceString, references);
		return rst;
	}

	public static String replaceEl(VpePageContext pageContext, String resourceString) {
		if (resourceString == null) {
			return ""; //$NON-NLS-1$
		}
		String rst = resourceString;
		final ResourceReference[] references = getResourceReferences(pageContext);
		if (references == null || references.length == 0) {
			return rst;
		}
		rst = replace(resourceString, references);
		return rst;
	}
	
	/**
	 * Replace.
	 * 
	 * @param resourceString
	 *            the resource string
	 * @param references
	 *            the references
	 * 
	 * @return the string
	 */
	private static String replace(String resourceString, ResourceReference[] sortedReferences) {
		String result = resourceString;
		// Values with higher precedence should be replaced in the first place.
		// Expect sorted by scope references.
		final int nPrefLen = getPrefLen(); // for small-simple optimization
		for (ResourceReference rf : sortedReferences) {
			final String tmp = rf.getLocation();
			if (tmp.length() + nPrefLen > resourceString.length()) {
				continue; // no sense for sequence contains checks...
			}
			final String dollarEl = DOLLAR_PREFIX + tmp + SUFFIX;
			final String sharpEl = SHARP_PREFIX + tmp + SUFFIX;
			if (resourceString.contains(dollarEl)) {
				result = result.replace(dollarEl, rf.getProperties());
			}
			if (resourceString.contains(sharpEl)) {
				result = result.replace(sharpEl, rf.getProperties());
			}
		}
		return result;
	}

	private static String replaceCustomAttributes(VpePageContext pageContext, String value) {
		String result = value;
		final int nPrefLen = getPrefLen(); // for small-simple optimization
		final Map<String, String> tmp = pageContext.getCustomElementsAttributes();
		for (String el : tmp.keySet()) {
			if (el.length() + nPrefLen > result.length()) {
				continue; // no sense for sequence contains checks...
			}
			final String dollarEl = DOLLAR_PREFIX + el + SUFFIX;
			final String sharpEl = SHARP_PREFIX + el + SUFFIX;

			if (result.contains(dollarEl)) {
				result = result.replace(dollarEl, tmp.get(el));
			}
			if (result.contains(sharpEl)) {
				result = result.replace(sharpEl, tmp.get(el));
			}
		}
		return result;
	}

	private static int getPrefLen() {
		int nPrefLen = DOLLAR_PREFIX.length();
		if (nPrefLen > SHARP_PREFIX.length()) {
			nPrefLen = SHARP_PREFIX.length();
		}
		nPrefLen += SUFFIX.length();
		return nPrefLen;
	}

	/**
	 * Creates a copy of {@code references} array and sorts its elements by scope value.
	 * 
	 * References with the lowest scope value ({@link ResourceReference#FILE_SCOPE}) become the first in the array and
	 * so on.
	 * 
	 * @param references
	 *            array to be sorted
	 * @return sorted copy of {@code references}
	 * @author yradtsevich
	 */
	public static ResourceReference[] sortReferencesByScope(ResourceReference[] references) {
		ResourceReference[] sortedReferences = references.clone();
		return sortReferencesByScope(sortedReferences);
	}

	public static ResourceReference[] sortReferencesByScope2(ResourceReference[] references) {
		Arrays.sort(references, new Comparator<ResourceReference>() {
			public int compare(ResourceReference r1, ResourceReference r2) {
				return r1.getScope() - r2.getScope();
			}
		});
		return references;
	}

	/**
	 * Checks if is cloneable node.
	 * 
	 * @param sourceNode
	 *            the source node
	 * @param pageContext
	 *            the page context
	 * 
	 * @return true, if is cloneable node
	 */
	public static boolean isELNode(VpePageContext pageContext, Node sourceNode) {
		boolean rst = false;
		if (isInCustomElementsAttributes(pageContext, sourceNode)) {
			return true;
		}
		if (isAvailableForNode(pageContext, sourceNode)
				|| BundleMapUtil.isInResourcesBundle(pageContext.getBundle(), sourceNode)) {
			rst = true;
		} else if (Jsf2ResourceUtil.isContainJSFContextPath(sourceNode)) {
			rst = true;
		}
		if (Jsf2ResourceUtil.isContainJSF2ResourceAttributes(sourceNode)) {
			// added by Maksim Areshkau, see JBIDE-4812
			rst = true;
		}
		return rst;
	}

	/**
	 * Checks is node exist in source custom element attribute map and if so, then retrun true
	 * 
	 * @param pageContext
	 * @param sourceNode
	 * @return
	 */
	private static boolean isInCustomElementsAttributes(VpePageContext pageContext, Node sourceNode) {
		boolean res = false;
		String textValue = null;
		if (sourceNode.getNodeType() == Node.TEXT_NODE) {
			textValue = sourceNode.getNodeValue();
			res = isInCustomElementsAttributes(pageContext, textValue);
		} else if (sourceNode.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap attributesMap = sourceNode.getAttributes();
			for (int i = 0; !res && i < attributesMap.getLength(); i++) {
				Attr attr = (Attr) attributesMap.item(i);
				textValue = attr.getValue();
				res = isInCustomElementsAttributes(pageContext, textValue);
			}
		}
		return res;
	}

	private static boolean isInCustomElementsAttributes(VpePageContext pageContext, String textValue) {
		boolean res = false;
		if (textValue != null) {
			for (String key : pageContext.getCustomElementsAttributes().keySet()) {
				if (equalsExppression(textValue, key)) {
					res = true;
					break;
				}
			}
		}
		return res;
	}

	/**
	 * Checks if is available for node.
	 * 
	 * @param resourceFile
	 *            the resource file
	 * @param sourceNode
	 *            the source node
	 * 
	 * @return true, if is available for node
	 */
	private static boolean isAvailableForNode(VpePageContext pageContext, Node sourceNode) {
		boolean rst = findForNode(pageContext, sourceNode);
		return rst;
	}

	/**
	 * @param sourceNode
	 * @param resourceFile
	 * @return
	 */
	private static boolean findForNode(VpePageContext pageContext, Node sourceNode) {
		final ResourceReference[] references = getResourceReferences(pageContext);
		if (references == null || references.length == 0) {
			return false;
		}
		String textValue = null;
		if (sourceNode.getNodeType() == Node.TEXT_NODE) {
			textValue = sourceNode.getNodeValue();
			if (textValue != null && isInReferenceResourcesList(references, textValue)) {
				return true;
			}
		}
		final NamedNodeMap nodeMap = sourceNode.getAttributes();
		if (nodeMap != null) {
			for (int i = 0; i < nodeMap.getLength(); i++) {
				if (isInReferenceResourcesList(references, ((Attr) nodeMap.item(i)).getValue())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static ResourceReference[] getResourceReferences(VpePageContext pageContext) {
		final ResourceReference[] res = (ResourceReference[])pageContext.getValue(VpePageContext.RES_REFERENCES);
		return res;
	}


	/**
	 * Checks if is in reference resources list.
	 * 
	 * @param value
	 *            the value
	 * @param references
	 *            the references
	 * 
	 * @return true, if is in reference resources list
	 */
	private static boolean isInReferenceResourcesList(ResourceReference[] references, String value) {
		for (ResourceReference ref : references) {
			// FIXED FOR JBIDE-3149 by sdzmitrovich
			if (equalsExppression(value, ref.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public static ResourceReference[] getAllResources(IFile resourceFile) {
		final IPath workspacePath = Platform.getLocation();
		final ResourceReference[] gResources = GlobalELReferenceList.getInstance().getAllResources(workspacePath);
		final ResourceReference[] elResources = ELReferenceList.getInstance().getAllResources(resourceFile);

		int size = (gResources == null ? 0 : gResources.length);
		size += (elResources == null ? 0 : elResources.length);
		ResourceReference[] rst = new ResourceReference[size];

		if ((gResources != null) && (gResources.length > 0)) {
			System.arraycopy(gResources, 0, rst, 0, gResources.length);
		}
		if ((elResources != null) && (elResources.length > 0)) {
			System.arraycopy(elResources, 0, rst, gResources == null ? 0 : gResources.length, elResources.length);
		}
		return rst;

	}

	public static String replaceElAndResources(VpePageContext pageContext, String value) {
		String rst = value;
		rst = ResourceUtil.getBundleValue(pageContext, value);
		// replace custom attributes
		rst = replaceCustomAttributes(pageContext, rst);
		if (Jsf2ResourceUtil.isExternalContextPathString(value)) {
			rst = Jsf2ResourceUtil.processExternalContextPath(value);
		}
		if (Jsf2ResourceUtil.isRequestContextPathString(value)) {
			rst = Jsf2ResourceUtil.processRequestContextPath(value);
		}
		if (Jsf2ResourceUtil.isJSF2ResourceString(rst)) {
			rst = Jsf2ResourceUtil.processCustomJSFAttributes(pageContext, rst);
		}
		rst = replaceEl(pageContext, rst);
		return rst;
	}

	private static boolean equalsExppression(String value, String expression) {
		final String dollarEl = DOLLAR_PREFIX + expression + SUFFIX;
		final String sharpEl = SHARP_PREFIX + expression + SUFFIX;
		if (value.contains(dollarEl) || value.contains(sharpEl)) {
			return true;
		}
		return false;
	}

}

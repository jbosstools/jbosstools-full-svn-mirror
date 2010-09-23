/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.spring.template.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;

/**
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public class VpeSpringUtil {
	
	public static final Map<String, String> COMMON_SPRING_FORM_ATTRIBUTES_MAP
			= new HashMap<String, String>() {{
		put(Spring.ATTR_ID, HTML.ATTR_ID);
		put(Spring.ATTR_CSS_STYLE, HTML.ATTR_STYLE);
		put(Spring.ATTR_CSS_CLASS, HTML.ATTR_CLASS);
	}};
	
	public static void copyAttribute(Element sourceElement, String sourceAttrName,
			nsIDOMElement visualElement, String visualAttrName) {
		if (sourceElement.hasAttribute(sourceAttrName)) {
			String attrValue = sourceElement.getAttribute(sourceAttrName);
			visualElement.setAttribute(visualAttrName, attrValue);
		}
	}
	
	public static void copyAttributes(Element sourceElement,
			nsIDOMElement visualElement, Map<String, String> sourceToVisualMap) {
		for (Entry<String, String> sourceToVisual : sourceToVisualMap.entrySet()) {
			String sourceAttrName = sourceToVisual.getKey();
			String visualAttrName = sourceToVisual.getValue();
			copyAttribute(sourceElement, sourceAttrName, visualElement, visualAttrName);
		}
	}
	
	public static void copyCommonAttributes(Element sourceElement,
			nsIDOMElement visualElement) {
		copyAttributes(sourceElement, visualElement,
				COMMON_SPRING_FORM_ATTRIBUTES_MAP);
	}
}

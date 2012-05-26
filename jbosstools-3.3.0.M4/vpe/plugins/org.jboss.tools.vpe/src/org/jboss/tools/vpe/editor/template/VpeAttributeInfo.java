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
package org.jboss.tools.vpe.editor.template;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * The class <code>VpeAttributeInfo</code> be used for creating
 * attribute of an element of a visual tree on attribute
 * of an element of an source tree or with constant value
 */

public class VpeAttributeInfo {
	private String visualName;
	private String sourceName;
	private String defaultValue;

	/**
     * Constructs a new <code>VpeAttributeInfo</code>.
     * The visual attribute does not create,
     * if the conforming source attribute no.
	 * @param name Name of attributes of elements of visual and
	 * source trees
	 */
	public VpeAttributeInfo(String name) {
		this.visualName = name;
		this.sourceName = name;
	}

	/**
     * Constructs a new <code>VpeAttributeInfo</code>.
     * The attribute receives constant value
	 * @param name Name of attributes of elements of visual and
	 * source trees
	 */
	public VpeAttributeInfo(String name, String value) {
		this.visualName = name;
		this.defaultValue = value;
	}

	/**
     * Constructs a new <code>VpeAttributeInfo</code>.
	 * @param visualName Name of attribute of element of visual tree
	 * @param sourceName Name of attribute of element of source tree
	 * @param defaultValue Default value for visual attribute, if the source attribute no.
	 * If <code> defaultValue</code> is null, visual attribute does not create,
	 * when source attribute no.
	 */
	public VpeAttributeInfo(String visualName, String sourceName, String defaultValue) {
		this.visualName = visualName;
		this.sourceName = sourceName;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Sets value of attribute for an element or constant defaultValue
	 * Ssets value of attribute for visualElement on attribute of sourceElement
	 * or constant defaultValue
	 * @param visualElement Element of a visual tree
	 * @param sourceElement Element of a source tree
	 */
	public void setAttribure(Element visualElement, Element sourceElement) {
		if (visualElement == null) return;

		Attr sourceAttr = null;
		if (sourceElement != null && sourceName != null) {
			sourceAttr = sourceElement.getAttributeNode(sourceName);
		}
		if (sourceAttr != null) {
			visualElement.setAttribute(visualName, sourceAttr.getValue());
		} else if (defaultValue != null) {
			visualElement.setAttribute(visualName, defaultValue);
		} else {
			visualElement.removeAttribute(visualName);
		}
	}
}

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
package org.jboss.tools.vpe.editor.template.textformating;

import org.w3c.dom.Element;

import org.jboss.tools.vpe.editor.template.VpeTemplateManager;

/**
 * @author Igels
 */
public class FormatAttributeData implements Cloneable{

	public static final String STYLE_TYPE = "style"; //$NON-NLS-1$

	private String type;
	private String name;
	private String value;
	private boolean caseSensitive = false;
	private FormatData parentFormatData;

	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public FormatAttributeData clone() throws CloneNotSupportedException {
		
		FormatAttributeData result = new FormatAttributeData(parentFormatData, null);
		
		result.type = new String(this.type);
		result.name = new String(this.name);
		result.value = new String(this.value);
		result.caseSensitive = caseSensitive;
		result.parentFormatData =  this.parentFormatData;
		
		return result;
	}

	/**
	 * @param formatAttribute - Element <vpe:formatAttribute>
	 */
	public FormatAttributeData(FormatData parentFormatData, Element formatAttribute) {
		this.parentFormatData = parentFormatData;
		type = formatAttribute.getAttribute(VpeTemplateManager.ATTR_FORMAT_ATTRIBUTE_TYPE);
		name = formatAttribute.getAttribute(VpeTemplateManager.ATTR_FORMAT_ATTRIBUTE_NAME);
		value = formatAttribute.getAttribute(VpeTemplateManager.ATTR_FORMAT_ATTRIBUTE_VALUE);
		caseSensitive = VpeTemplateManager.ATTR_FORMAT_ATTRIBUTE_TRUE_VALUE.equals(formatAttribute.getAttribute(VpeTemplateManager.ATTR_FORMAT_ATTRIBUTE_CASE_SENSITIVE));
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		if(name==null || name.length()==0) {
			return type;
		}
		return name;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return Returns the caseSensitive.
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @return Returns the parentFormatData.
	 */
	public FormatData getParentFormatData() {
		return parentFormatData;
	}

	/**
	 * @param parentFormatData the parentFormatData to set
	 */
	public void setParentFormatData(FormatData parentFormatData) {
		this.parentFormatData = parentFormatData;
	}
}
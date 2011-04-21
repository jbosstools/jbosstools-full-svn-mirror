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
package org.jboss.tools.vpe.editor.toolbar.format.css;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Attr;

/**
 * @author Igels
 */
public class StyleAttribute {

	/*
	 * Attribute name. For examlpe - "Style"
	 */
	private String name;

	/*
	 * For examples - {" FONT ",":"," BOLD SMTHELSE",";","OTHERPROPERTY ",":"," VALUE"}
	 */
	private ArrayList tokens = new ArrayList();

	/**
	 * Constructor
	 */
	public StyleAttribute(Attr styleAttribute) {
		name = styleAttribute.getName();
		parse(styleAttribute.getValue());
	}

	private void parse(String value) {
		tokens.clear();
		StringTokenizer tokenizer = new StringTokenizer(value, ";", true); //$NON-NLS-1$
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(token.equals(";")) { //$NON-NLS-1$
				tokens.add(new Token(token));
			} else {
				tokens.add(new StyleProperty(token));
			}
		}
	}

	/**
	 * @param name
	 * @param value
	 */
	public boolean addStyleProperty(String name, String value) {
		StyleProperty property = getProperty(name);
		if(property==null) {
			String space = ""; //$NON-NLS-1$
			if(tokens.size()>0) {
				Token token = (Token)tokens.get(0);
				if(!token.toString().startsWith(" ")) { //$NON-NLS-1$
					space = " "; //$NON-NLS-1$
				}
			}
			StyleProperty newProperty = new StyleProperty(name + ": " + value + ";" + space); //$NON-NLS-1$ //$NON-NLS-2$
			tokens.add(0, newProperty);
		} else {
			SinglePropertyValue propertyValue = property.getSinglePropertyValue(value);
			if(propertyValue==null) {
				property.addSingleValue(value);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean isStylePropertySet(String name, String value) {
		StyleProperty property = getProperty(name);
		if(property==null) {
			return false;
		}
		SinglePropertyValue propertyValue = property.getSinglePropertyValue(value);
		if(propertyValue==null) {
			return false;
		}
		return true;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean removeStyleProperty(String name, String value) {
		StyleProperty property = getProperty(name);
		if(property==null) {
			return false;
		}
		boolean result = property.removeSingleValue(value);
		if(!property.hasPropertyValue()) {
			removeStyleProperty(name);
		}
		return result;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean removeStyleProperty(String name) {
		StyleProperty property = getProperty(name);
		if(property!=null) {
			int index = tokens.indexOf(property) + 1;
			if(index<tokens.size()) {
				Token nextToken = (Token)tokens.get(index);
				if(nextToken.getDirtyValue().equals(";")) { //$NON-NLS-1$
					tokens.remove(index);
				}
			}
			return tokens.remove(property);
		}
		return false;
	}

	/**
	 */
	public void format() {
		parse(toString().trim());
	}

	/**
	 * If stylePropertyValue is set than just remove this value from style property.
	 * If stylePropertyValue is not set than add this value to style property.
	 * Example:
	 *   stylePropertyName="TEXT-DECORATION"
	 *   stylePropertyValue="overline"
	 *   Befor simple inverting -  "TEXT-DECORATION: overline underline"
	 *   After simple inverting -  "TEXT-DECORATION: underline"
	 *   After one more invering - "TEXT-DECORATION: underline overline"
	 * @param name
	 * @param value
	 */
	public void invertStyleProperty(String name, String value) {
		StyleProperty property = getProperty(name);
		if(property==null) {
			addStyleProperty(name, value);
		} else {
			SinglePropertyValue propertyValue = property.getSinglePropertyValue(value);
			if(propertyValue==null) {
				property.addSingleValue(value);
			} else {
				property.removeSingleValue(value);
				if(!property.hasPropertyValue()) {
					removeStyleProperty(name);
				}
			}
		}
	}

	/**
	 * If stylePropertyValue is set than just remove this style property.
	 * If stylePropertyValue is not set than set this value to style property.
	 * Example:
	 *   stylePropertyName="FONT-STYLE"
	 *   stylePropertyValue="none"
	 *   Befor simple inverting -  "FONT-STYLE: italic"
	 *   After simple inverting -  "FONT-STYLE: none"
	 *   After one more invering - ""
	 *   After one more invering - "FONT-STYLE: none"
	 * @param styleAttribute
	 * @param stylepPropertyName
	 * @param setValue
	 */
	public void ivertSingleStyleProperty(String stylePropertyName, String stylePropertyValue) {
		StyleProperty property = getProperty(stylePropertyName);
		if(property==null) {
			addStyleProperty(stylePropertyName, stylePropertyValue);
		} else {
			SinglePropertyValue value = property.getSinglePropertyValue(stylePropertyValue);
			if(value!=null) {
				removeStyleProperty(stylePropertyName);
			} else {
				removeStyleProperty(stylePropertyName);
				addStyleProperty(stylePropertyName, stylePropertyValue);
			}
		}
	}

	/**
	 * @param stylePropertyName
	 * @param stylePropertyValue
	 */
	public void setSingleStyleProperty(String stylePropertyName, String stylePropertyValue) {
		StyleProperty property = getProperty(stylePropertyName);
		if(property!=null) {
			removeStyleProperty(stylePropertyName);
		}
		addStyleProperty(stylePropertyName, stylePropertyValue);
	}

	/**
	 * @param name
	 * @return
	 */
	public StyleProperty getProperty(String name) {
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof StyleProperty) {
				StyleProperty property = (StyleProperty)tokens.get(i);
				if (property.getName()!=null && name.equalsIgnoreCase(property.getName().getCleanValue())) {
					return property;
				}
			}
		}
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<tokens.size(); i++) {
			buffer.append(tokens.get(i).toString());
		}
		return buffer.toString();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
}
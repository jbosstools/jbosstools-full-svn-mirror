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

/**
 * @author Igels
 */
public class StyleProperty extends Token {

	/*
	 * For example - {" FONT ",":"," BOLD SMTHELSE"}
	 */
	private ArrayList<Token> tokens = new ArrayList<Token>();

	/*
	 * For example - " FONT";
	 */
	private Token name;

	/**
	 * @param dirtyValue
	 */
	public StyleProperty(String dirtyValue) {
		super(dirtyValue);
		parse(dirtyValue);
	}

	private void parse(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, ":", true); //$NON-NLS-1$
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(token.equals(":")) { //$NON-NLS-1$
				tokens.add(new Token(token));
			} else if(name==null) {
				name = new Token(token);
				tokens.add(name);
			} else {
				tokens.add(new MultiPropertyValue(token));
			}
		}
	}

	/**
	 * @return Returns the name.
	 */
	public Token getName() {
		return name;
	}

	/**
	 * @param value
	 * @return
	 */
	public SinglePropertyValue getSinglePropertyValue(String value) {
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof MultiPropertyValue) {
				MultiPropertyValue multiPropertyValue = (MultiPropertyValue)tokens.get(i);
				SinglePropertyValue singlePropertyValue = multiPropertyValue.getSinglePropertyValue(value);
				if(singlePropertyValue!=null) {
					return singlePropertyValue;
				}
			}
		}
		return null;
	}

	private MultiPropertyValue getMultiPropertyValue(String value) {
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof MultiPropertyValue) {
				MultiPropertyValue multiPropertyValue = (MultiPropertyValue)tokens.get(i);
				SinglePropertyValue singlePropertyValue = multiPropertyValue.getSinglePropertyValue(value);
				if(singlePropertyValue!=null) {
					return multiPropertyValue;
				}
			}
		}
		return null;
	}

	/**
	 * @return MultiPropertyValue
	 */
	public MultiPropertyValue getPropertyValue() {
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof MultiPropertyValue) {
				MultiPropertyValue multiPropertyValue = (MultiPropertyValue)tokens.get(i);
				return multiPropertyValue;
			}
		}
		return null;
	}

	/**
	 * @return FirstSinglePropertyValue
	 */
	public String getFirstSinglePropertyValue() {
		MultiPropertyValue multiPropertyValue = getPropertyValue();
		if(multiPropertyValue!=null) {
			SinglePropertyValue[] singlePropertyValues = multiPropertyValue.getSinglePropertyValues();
			if(singlePropertyValues.length>0) {
				return singlePropertyValues[0].getCleanValue();
			}
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean addSingleValue(String value) {
		SinglePropertyValue singlePropertyValue = getSinglePropertyValue(value);
		if(singlePropertyValue==null) {
			MultiPropertyValue propertyValue = getPropertyValue();
			if(propertyValue==null) {
				MultiPropertyValue newPropertyValue = new MultiPropertyValue(" " + value); //$NON-NLS-1$
				tokens.add(newPropertyValue);
				return true;
			}
			return propertyValue.addSingleValue(value);
		}
		return false;
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean removeSingleValue(String value) {
		MultiPropertyValue multiPropertyValue = getMultiPropertyValue(value);
		if(multiPropertyValue!=null) {
			boolean result = multiPropertyValue.removeSinglePropertyValue(value);
			if(!multiPropertyValue.hasPropertyValue()) {
				tokens.remove(multiPropertyValue);
			}
			return result;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean hasPropertyValue() {
		return getPropertyValue()!=null;
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
}
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
public class MultiPropertyValue extends Token {

	/*
	 * For example - {" ","BOLD"," ","SMTHELSE"}
	 */
	private ArrayList tokens =  new ArrayList();

	/**
	 * @param dirtyValue
	 */
	public MultiPropertyValue(String dirtyValue) {
		super(dirtyValue);
		parse(dirtyValue);
	}

	private void parse(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, " ", true); //$NON-NLS-1$
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(token.equals(" ")) { //$NON-NLS-1$
				tokens.add(new Token(token));
			} else {
				tokens.add(new SinglePropertyValue(token));
			}
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public SinglePropertyValue getSinglePropertyValue(String value) {
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof SinglePropertyValue) {
				SinglePropertyValue propertyValue = (SinglePropertyValue)tokens.get(i);
				if(value.equalsIgnoreCase(propertyValue.getCleanValue())) {
					return propertyValue;
				}
			}
		}
		return null;
	}

	/**
	 * @return SinglePropertyValue[]
	 */
	public SinglePropertyValue[] getSinglePropertyValues() {
		ArrayList values = new ArrayList();
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof SinglePropertyValue) {
				values.add(tokens.get(i));
			}
		}
		return (SinglePropertyValue[])values.toArray(new SinglePropertyValue[values.size()]);
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean addSingleValue(String value) {
		SinglePropertyValue propertyValue = getSinglePropertyValue(value);
		if(propertyValue==null) {
			String newValue = value;
			if(!((Token)tokens.get(tokens.size()-1)).getDirtyValue().endsWith(" ")) { //$NON-NLS-1$
				newValue = " " + newValue; //$NON-NLS-1$
			}
			return tokens.add(new SinglePropertyValue(newValue));
		}
		return false;
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean removeSinglePropertyValue(String value) {
		SinglePropertyValue propertyValue = getSinglePropertyValue(value);
		if(propertyValue!=null) {
			return tokens.remove(propertyValue);
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean hasPropertyValue() {
		for(int i=0; i<tokens.size(); i++) {
			if(tokens.get(i) instanceof SinglePropertyValue) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
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
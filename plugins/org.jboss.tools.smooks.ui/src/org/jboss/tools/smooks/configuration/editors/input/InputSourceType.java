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
package org.jboss.tools.smooks.configuration.editors.input;

import org.jboss.tools.smooks.configuration.editors.input.contributors.SimpleMessageContributor;

/**
 * Smooks filter input Source type.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public enum InputSourceType {	
	
	// NOTE: If a new type is added, be sure and update the fromTypeIndex/fromTypeName static methods !!!
	NONE(0, new SimpleMessageContributor(Messages.InputSourceType_Warning_Specify_Input_Type)),
	XML(1, new SimpleMessageContributor(Messages.InputSourceType_Warning_Specify_Sample_Data)),
	XSD(2, new SimpleMessageContributor(Messages.InputSourceType_Warning_Specify_Sample_Data)),
	JAVA(3, new SimpleMessageContributor(Messages.InputSourceType_Warning_Specify_Sample_Data));
	
	private int typeIndex;
	private InputReaderConfigurationContributor configurationContributor;
	
	InputSourceType(int typeIndex, InputReaderConfigurationContributor configurationContributor) {
		this.typeIndex = typeIndex;
		this.configurationContributor = configurationContributor;
	}
	
	public int getTypeIndex() {
		return typeIndex;
	}
	
	public InputReaderConfigurationContributor getConfigurationContributor() {
		return configurationContributor;
	}
	
	public boolean isType(int typeIndex) {
		return (this.typeIndex == typeIndex);
	}
	
	public boolean isType(String typeString) {
		return (name().equals(typeString) || toTypeString().equals(typeString));
	}
	
	public String toTypeString() {
		return ("input." + name().toLowerCase());
	}
	
	public static InputSourceType fromTypeIndex(int typeIndex) throws InvalidInputSourceTypeException {
		
		if(InputSourceType.XML.isType(typeIndex)) {
			return InputSourceType.XML;
		} else if(InputSourceType.XSD.isType(typeIndex)) {
			return InputSourceType.XSD;
		} else if(InputSourceType.JAVA.isType(typeIndex)) {
			return InputSourceType.JAVA;
		} else if(InputSourceType.NONE.isType(typeIndex)) {
			return InputSourceType.NONE;
		}		
		
		throw new InvalidInputSourceTypeException("");
	}	
	
	public static InputSourceType fromTypeName(String typeName) throws InvalidInputSourceTypeException {
		
		if(InputSourceType.XML.isType(typeName)) {
			return InputSourceType.XML;
		} else if(InputSourceType.XSD.isType(typeName)) {
			return InputSourceType.XSD;
		} else if(InputSourceType.JAVA.isType(typeName)) {
			return InputSourceType.JAVA;
		} else if(InputSourceType.NONE.isType(typeName)) {
			return InputSourceType.NONE;
		} 		
				
		throw new InvalidInputSourceTypeException("");
	}
	
	public static boolean isValidIndexNamePair(int typeIndex, String typeName) throws InvalidInputSourceTypeException {		
		return (fromTypeIndex(typeIndex) == fromTypeName(typeName));
	}
}

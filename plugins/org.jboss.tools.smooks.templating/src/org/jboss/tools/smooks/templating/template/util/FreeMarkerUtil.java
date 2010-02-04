/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.tools.smooks.templating.template.util;

import java.util.Properties;

import org.jboss.tools.smooks.templating.template.ValueMapping;
import org.jboss.tools.smooks.templating.template.exception.TemplateBuilderException;
import org.milyn.javabean.decoders.DateDecoder;

/**
 * FreeMarker utility methods.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class FreeMarkerUtil {

	public static String extractJavaPath(String dollarVariable) throws TemplateBuilderException {
		return splitDollarVariable(dollarVariable)[0];
	}

	public static String extractRawFormatting(String dollarVariable) throws TemplateBuilderException {
		return splitDollarVariable(dollarVariable)[1];
	}

	public static String[] splitDollarVariable(String dollarVariable) throws TemplateBuilderException {
		String[] splitTokens = new String[2];
		
		dollarVariable = dollarVariable.trim();
		
		if(isDollarVariable(dollarVariable)) {
			String withoutDollarBrace = dollarVariable.substring(2, dollarVariable.length() - 1);
			int questionMarkIdx = withoutDollarBrace.indexOf('?'); //$NON-NLS-1$
			
			if(questionMarkIdx != -1) {
				splitTokens[0] = withoutDollarBrace.substring(0, questionMarkIdx);
				splitTokens[1] = withoutDollarBrace.substring(questionMarkIdx + 1); // the raw formatting
			} else {				
				splitTokens[0] = withoutDollarBrace;
				splitTokens[1] = null; // no formatting
			}
			
			if(splitTokens[0].endsWith("!")) { //$NON-NLS-1$
				splitTokens[0] = splitTokens[0].substring(0, splitTokens[0].length() - 1);
			}
		} else {
			throw new TemplateBuilderException("Unsupported FreeMarker variable syntax '" + dollarVariable + "'."); //$NON-NLS-1$
		}
		
		return splitTokens;
	}

	public static boolean isDollarVariable(String variable) {
		return (variable.startsWith("${") && variable.endsWith("}")); //$NON-NLS-1$
	}
	
	public static String toFreeMarkerVariable(ValueMapping mapping) {
		StringBuilder builder = new StringBuilder();
		Properties encodeProperties = mapping.getEncodeProperties();
		String rawFormatting;
		
		if(encodeProperties == null) {
			encodeProperties = new Properties();
		}
				
		builder.append("${" + mapping.getSrcPath() + "!?");
		
		rawFormatting = encodeProperties.getProperty(ValueMapping.RAW_FORMATING_KEY);
		if(rawFormatting != null) {
			builder.append(rawFormatting);			
			builder.append("}");			
		} else {
			Class<?> valueType = mapping.getValueType();
			if(valueType != null) {
				
				if(valueType == java.util.Date.class) {
					String format = encodeProperties.getProperty(DateDecoder.FORMAT);
					if(format != null) {					
						builder.append("string('" + format + "')}");								
					} else {
						builder.append("string.medium}");								
					}
				} else if(Number.class.isAssignableFrom(valueType)) {
					builder.append("c}");								
				} else if(valueType == Double.TYPE || valueType == Float.TYPE || valueType == Integer.TYPE || valueType == Long.TYPE || valueType == Short.TYPE) {
					builder.append("c}");								
				} else {
					builder.append("string}");			
				}
			} else {
				builder.append("string}");			
			}
		}
		
		return builder.toString();
	}
}
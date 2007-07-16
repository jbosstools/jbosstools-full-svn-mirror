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
package org.jboss.tools.vpe.editor.util;

import org.w3c.dom.Element;

public class VpeStyleUtil {
	
	public static final String ATTRIBUTE_STYLE    = "style";

	public static final String PARAMETER_POSITION = "position";
	public static final String PARAMETER_TOP = "top";
	public static final String PARAMETER_LEFT = "left";
	public static final String PARAMETER_WIDTH = "width";
	public static final String PARAMETER_HEIGHT = "height";

	public static final String VALUE_ABSOLUTE = "absolute";
	
	public static final String DOT_STRING = ".";
	public static final String COLON_STRING = ":";
	public static final String SEMICOLON_STRING = ";";
	public static final String PX_STRING = "px";
	public static final String SPACE_STRING = " ";
	public static final String EMPTY_STRING = "";
	
	
	// sets parameter position in atribute style to absolute value
	public static void setAbsolute(Element sourceElement){
		String style = sourceElement.getAttribute(ATTRIBUTE_STYLE);
		if(style == null){
			style = EMPTY_STRING;
		}else{ // remove old sizes
			style = deleteFromString(style, PARAMETER_POSITION, SEMICOLON_STRING);
		}
		if(style.length() > 0){
			if(!style.endsWith(SEMICOLON_STRING))style += SEMICOLON_STRING;
		}
		
		style += SPACE_STRING+PARAMETER_POSITION+SPACE_STRING+COLON_STRING+SPACE_STRING+VALUE_ABSOLUTE+SEMICOLON_STRING;
		
		sourceElement.setAttribute(ATTRIBUTE_STYLE, style);
	}

//	 sets parameter position in absolute value
	public static String setAbsolute(String styleString){
		String style = new String(styleString);
		if(style == null){
			style = EMPTY_STRING;
		}else{ // remove old sizes
			style = deleteFromString(style, PARAMETER_POSITION, SEMICOLON_STRING);
		}
		if(style.length() > 0){
			if(!style.endsWith(SEMICOLON_STRING))style += SEMICOLON_STRING;
		}
		
		style += SPACE_STRING+PARAMETER_POSITION+SPACE_STRING+COLON_STRING+SPACE_STRING+VALUE_ABSOLUTE+SEMICOLON_STRING;
		
		return style;						
	}

//	 return true if parameter position was set to absolute 
	public static boolean getAbsolute(Element sourceElement){
		String style = sourceElement.getAttribute(ATTRIBUTE_STYLE);
		if(style == null){
			return false;
		}else{ // remove old sizes
			if(style.indexOf(VALUE_ABSOLUTE) >= 0)return true;
		}
		return false;
	}

//	 return true if parameter position was set to absolute
	public static boolean getAbsolute(String style){
		if(style == null){
			return false;
		}else{ // remove old sizes
			if(style.indexOf(VALUE_ABSOLUTE) >= 0)return true;
		}
		return false;
	}
	
	// return value of parameter described in sizeAttribute, for example "style.width"
	public static int getSizeFromStyle(Element sourceElement, String sizeAttribute){
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0,dotPosition);
		String parameter = sizeAttribute.substring(dotPosition+1, sizeAttribute.length());
		
		String style = sourceElement.getAttribute(attribute);
		if(style == null || EMPTY_STRING.equals(style)) return -1;
		
		int parameterPosition = style.indexOf(parameter);
		if(parameterPosition >= 0){
			int valuePosition = style.indexOf(COLON_STRING,parameterPosition);
			if(valuePosition >= 0){
				int endPosition = style.indexOf(PX_STRING,valuePosition);
				if(endPosition >= 0){
					return new Integer(style.substring(valuePosition+1, endPosition).trim()).intValue();
				}
			}
		}
		return -1;
	}

	// return value of parameter described in sizeAttribute, for example "style.width"
	public static String getParameterFromStyle(Element sourceElement, String sizeAttribute){
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0,dotPosition);
		String parameter = sizeAttribute.substring(dotPosition+1, sizeAttribute.length());
		
		String style = sourceElement.getAttribute(attribute);
		if(style == null || EMPTY_STRING.equals(style)) return null;
		
		int parameterPosition = style.indexOf(parameter);
		if(parameterPosition >= 0){
			int valuePosition = style.indexOf(COLON_STRING,parameterPosition);
			if(valuePosition >= 0){
				int endPosition = style.indexOf(PX_STRING,valuePosition);
				if(endPosition >= 0){
					return style.substring(valuePosition+1, endPosition).trim();
				}
			}
		}
		return null;
	}
	
	// sets value of parameter described in sizeAttribute, for example "style.width"	
	public static void setSizeInStyle(Element sourceElement, String sizeAttribute, int size){
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0,dotPosition);
		String parameter = sizeAttribute.substring(dotPosition+1, sizeAttribute.length());
		
		String style = sourceElement.getAttribute(attribute);
		if(style == null){
			style = EMPTY_STRING;
		}else{ // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if(style.length() > 0){
			if(!style.endsWith(SEMICOLON_STRING))style += SEMICOLON_STRING;
		}
		
		style += SPACE_STRING+parameter+SPACE_STRING+COLON_STRING+SPACE_STRING+size+PX_STRING+SEMICOLON_STRING;
		
		sourceElement.setAttribute(attribute, style);						
	}

	// sets value of parameter described in sizeAttribute, for example "style.width"	
	public static void setParameterInStyle(Element sourceElement, String sizeAttribute, String value){
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0,dotPosition);
		String parameter = sizeAttribute.substring(dotPosition+1, sizeAttribute.length());
		
		String style = sourceElement.getAttribute(attribute);
		if(style == null){
			style = EMPTY_STRING;
		}else{ // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if(style.length() > 0){
			if(!style.endsWith(SEMICOLON_STRING))style += SEMICOLON_STRING;
		}
		
		style += SPACE_STRING+parameter+SPACE_STRING+COLON_STRING+SPACE_STRING+value+SEMICOLON_STRING;
		
		sourceElement.setAttribute(attribute, style);						
	}
	
	public static String setSizeInStyle(String style, String parameter, int size){
		if(style == null){
			style = EMPTY_STRING;
		}else{ // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if(style.length() > 0){
			if(!style.endsWith(SEMICOLON_STRING))style += SEMICOLON_STRING;
		}
		
		style += SPACE_STRING+parameter+SPACE_STRING+COLON_STRING+SPACE_STRING+size+PX_STRING+SEMICOLON_STRING;
		
		return style;						
	}

	public static String setParameterInStyle(String style, String parameter, String value){
		if(style == null){
			style = EMPTY_STRING;
		}else{ // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if(style.length() > 0){
			if(!style.endsWith(SEMICOLON_STRING))style += SEMICOLON_STRING;
		}
		
		style += SPACE_STRING+parameter+SPACE_STRING+COLON_STRING+SPACE_STRING+value+SEMICOLON_STRING;
		
		return style;						
	}
	
	// selets parameter from atribute style
	public static void deleteFromStyle(Element sourceElement, String begin, String end){
		String style = sourceElement.getAttribute(ATTRIBUTE_STYLE);
		style = deleteFromString(style, begin, end);
		sourceElement.setAttribute(ATTRIBUTE_STYLE, style);
	}
	
	// selets parameter from atribute style
	public static String deleteFromString(String data, String begin, String end){
		int startPosition = data.indexOf(begin);
		
		if(startPosition < 0) return data;
		
		int endPosition = data.indexOf(end, startPosition);
		
		String result = data.substring(0, startPosition).trim();
		if(endPosition > 0){
			result += data.substring(endPosition+1, data.length()).trim();
		}
		
		return result;
	}

}

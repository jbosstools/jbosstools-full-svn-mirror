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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.w3c.dom.Element;

public class VpeStyleUtil {
	
	public static final String ATTRIBUTE_STYLE    = "style";

	public static final String PARAMETER_POSITION = "position";
	public static final String PARAMETER_TOP = "top";
	public static final String PARAMETER_LEFT = "left";
	public static final String PARAMETER_WIDTH = "width";
	public static final String PARAMETER_HEIGHT = "height";
	public static final String PARAMETR_BACKGROND = "background";

	public static final String VALUE_ABSOLUTE = "absolute";
	
	public static final String DOT_STRING = ".";
	public static final String COLON_STRING = ":";
	public static final String SEMICOLON_STRING = ";";
	public static final String PX_STRING = "px";
	public static final String SPACE_STRING = " ";
	public static final String EMPTY_STRING = "";
	
	public static String ATTR_URL = "url";
	public static String OPEN_BRACKET = "(";
	public static String CLOSE_BRACKET = ")";
	public static String FILE_PRTOCOL = "file:///";
	public static String FILE_STR = "file:";
	public static String FILE_SEPARAROT = "/";
	
	
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
	
	/**
	 * 
	 * @param value
	 *            Css string
	 * @param input
	 *            The editor input
	 * @return format style string
	 */
	public static String addFullPathIntoURLValue(String value,
			IEditorInput input) {

		String urls[] = value.split(ATTR_URL);

		if (urls.length == 1)
			return value;

		String finalStr = EMPTY_STRING;
		for (int i = 1; i < urls.length; i++) {

			urls[i] = urls[i].replace("\'", EMPTY_STRING);
			urls[i] = ATTR_URL + urls[i];

			int startAttr = urls[i].indexOf(ATTR_URL);

			int startPathIndex = urls[i].indexOf(OPEN_BRACKET, startAttr);
			int endPathIndex = urls[i].indexOf(CLOSE_BRACKET,
					startPathIndex + 1);

			if (startPathIndex == -1 || endPathIndex == -1)
				continue;

			String filePath = urls[i].substring(startPathIndex + 1,
					endPathIndex);
			if (filePath.indexOf(FILE_STR) != -1)
				continue;

			if (!new File(filePath).isAbsolute())
				filePath = getFilePath(input, filePath);

			filePath = FILE_PRTOCOL + filePath;
			URL url = null;
			try {
				url = new URL(filePath);
			} catch (MalformedURLException e) {
				continue;
			}
			filePath = url.toString();

			String firstPartValue = urls[i].substring(0, startPathIndex + 1);
			String secondPartValue = urls[i].substring(endPathIndex, urls[i]
					.length());

			urls[i] = firstPartValue + filePath + secondPartValue;
		}
		for (int i = 0; i < urls.length; i++)
			finalStr += urls[i];
		return finalStr;
	}

	/**
	 * 
	 * @param nput
	 *            The editor input
	 * @param fileName
	 *            Relative path file
	 * @return Absolute path file
	 */
	public static String getFilePath(IEditorInput input, String fileName) {
		IPath inputPath = getInputParentPath(input);
		return inputPath.toOSString() + File.separator + fileName;
	}

	/**
	 * 
	 * @param input
	 *            The editor input
	 * @return Path
	 */
	public static IPath getInputParentPath(IEditorInput input) {
		IPath inputPath = null;
		if (input instanceof ILocationProvider) {
			inputPath = ((ILocationProvider) input).getPath(input);
		} else if (input instanceof IFileEditorInput) {
			IFile inputFile = ((IFileEditorInput) input).getFile();
			if (inputFile != null) {
				inputPath = inputFile.getLocation();
			}
		}
		if (inputPath != null && !inputPath.isEmpty()) {
			inputPath = inputPath.removeLastSegments(1);
		}
		return inputPath;
	}
	
	/**
	 * 
	 * @param value
	 *            Css string
	 * @param input
	 *            The editor input
	 * @return format style string
	 */
	public static String addFullPathIntoBackgroundValue(String value,
			IEditorInput input) {

		if (value.indexOf(FILE_STR) != -1)
			return value;

		if (!new File(value).isAbsolute())
			value = getFilePath(input, value);

		value = FILE_PRTOCOL + value;
		URL url = null;
		try {
			url = new URL(value);
		} catch (MalformedURLException e) {
			return value;
		}

		return url.toString();
	}

	/**
	 * 
	 * @param value
	 *            Css string
	 * @param href_val
	 *            Path of css file
	 * @return Format style string
	 */
	public static String addFullPathIntoURLValue(String value, String href_val) {

		String urls[] = value.split(ATTR_URL);

		if (urls.length == 1)
			return value;

		String finalStr = EMPTY_STRING;

		for (int i = 1; i < urls.length; i++) {

			urls[i] = urls[i].replace("\'", EMPTY_STRING);
			urls[i] = ATTR_URL + urls[i];

			int startAttr = urls[i].indexOf(ATTR_URL);

			int startPathIndex = urls[i].indexOf(OPEN_BRACKET, startAttr);
			int endPathIndex = urls[i].indexOf(CLOSE_BRACKET,
					startPathIndex + 1);

			String filePath = urls[i].substring(startPathIndex + 1,
					endPathIndex);
			if (filePath.indexOf(FILE_STR) != -1)
				continue;

			if (!new File(filePath).isAbsolute())
				filePath = getAbsolutePathImage(filePath, href_val);
			else
				filePath = FILE_PRTOCOL + filePath;

			URL url = null;
			try {
				url = new URL(filePath);
			} catch (MalformedURLException e) {
				continue;
			}
			filePath = url.toString();

			String firstPartValue = urls[i].substring(0, startPathIndex + 1);
			String secondPartValue = urls[i].substring(endPathIndex, urls[i]
					.length());

			urls[i] = firstPartValue + filePath + secondPartValue;
		}
		for (int i = 0; i < urls.length; i++)
			finalStr += urls[i];
		return finalStr;
	}

	/**
	 * 
	 * @param pathImgRelative
	 *            Relative path img file
	 * @param pathCssAbsolute
	 *            Absolute path css file
	 * @return Absolute path img file
	 */
	private static String getAbsolutePathImage(String pathImgRelative,
			String pathCssAbsolute) {

		int k = 0;
		int j = 0;
		URL url = null;
		try {
			url = new URL(pathCssAbsolute);
		} catch (MalformedURLException e) {
			return pathImgRelative;
		}
		pathCssAbsolute = url.toString();
		while (j != -1) {
			j = pathCssAbsolute.indexOf(FILE_SEPARAROT, j + 1);
			if (j == -1)
				break;
			k = j;
		}
		return pathCssAbsolute.substring(0, k + 1) + pathImgRelative;
	}
}
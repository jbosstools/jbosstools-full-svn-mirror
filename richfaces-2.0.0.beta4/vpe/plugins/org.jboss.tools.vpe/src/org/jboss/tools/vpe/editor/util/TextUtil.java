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

import java.util.HashMap;
import java.util.Map;

public class TextUtil {
	private final static String SOURCE_BREAK = "\r\n"; 
	private final static String VISUAL_BREAK = "\n"; 
	private final static char SOURCE_SPACE = ' '; 
	private final static char VISUAL_SPACE = 160; 
	private final static int CHR_ESC_START = '&';
	private final static int CHR_ESC_STOP = ';';
	private final static String SPCHARS = "\f\n\r\t\u0020\u2028\u2029";
	private final static Map textSet = new HashMap();
	static{
		textSet.put(new Character('"'), "&quot;");
		textSet.put(new Character('&'),"&amp;");
		textSet.put(new Character('<'),"&lt;");
		textSet.put(new Character('>'),"&gt;");
		textSet.put(new Character(' '),"&nbsp;");
		textSet.put(new Character('\u00A1'),"&iexcl;");
		textSet.put(new Character('\u00A2'),"&cent;");
		textSet.put(new Character('\u00A3'),"&pound;");
		textSet.put(new Character('\u00A4'),"&curren;");
		textSet.put(new Character('\u00A5'),"&yen;");
		textSet.put(new Character('\u00A6'),"&brvbar;");
		textSet.put(new Character('\u00A7'),"&sect;");
		textSet.put(new Character('\u00A8'),"&uml;");
		textSet.put(new Character('\u00A9'),"&copy;");
		textSet.put(new Character('\u00AA'),"&ordf;");
		textSet.put(new Character('\u00AB'),"&laquo;");
		textSet.put(new Character('\u00AC'),"&not;");
		//textSet.put(new Character('\u00AD'),"&shy;");
		textSet.put(new Character('\u00AE'),"&reg;");
		textSet.put(new Character('\u00AF'),"&macr;");
		textSet.put(new Character('\u00B0'),"&deg;");
		textSet.put(new Character('\u00B1'),"&plusmn;");
		textSet.put(new Character('\u00B2'),"&sup2;");
		textSet.put(new Character('\u00B3'),"&sup3;");
		textSet.put(new Character('\u00B4'),"&acute;");
		textSet.put(new Character('\u00B5'),"&micro;");
		textSet.put(new Character('\u00B6'),"&para;");
		textSet.put(new Character('\u00B7'),"&middot;");
		textSet.put(new Character('\u00B8'),"&cedil;");
		textSet.put(new Character('\u00B9'),"&sup1;");
		textSet.put(new Character('\u00BA'),"&ordm;");
		textSet.put(new Character('\u00BB'),"&raquo;");
		textSet.put(new Character('\u00BC'),"&frac14;");
		textSet.put(new Character('\u00BD'),"&frac12;");
		textSet.put(new Character('\u00BE'),"&frac34;");
		textSet.put(new Character('\u00BF'),"&iquest;");
		textSet.put(new Character('\u00C0'),"&Agrave;");
		textSet.put(new Character('\u00E0'),"&agrave;");
		textSet.put(new Character('\u00C1'),"&Aacute;");
		textSet.put(new Character('\u00E1'),"&aacute;");
		textSet.put(new Character('\u00C2'),"&Acirc;");
		textSet.put(new Character('\u00E2'),"&acirc;");
		textSet.put(new Character('\u00C3'),"&Atilde;");
		textSet.put(new Character('\u00E3'),"&atilde;");
		textSet.put(new Character('\u00C4'),"&Auml;");
		textSet.put(new Character('\u00E4'),"&auml;");
		textSet.put(new Character('\u00C5'),"&Aring;");
		textSet.put(new Character('\u00E5'),"&aring;");
		textSet.put(new Character('\u00C6'),"&AElig;");
		textSet.put(new Character('\u00E6'),"&aelig;");
		textSet.put(new Character('\u00C7'),"&Ccedil;");
		textSet.put(new Character('\u00E7'),"&ccedil;");
		textSet.put(new Character('\u00C8'),"&Egrave;");
		textSet.put(new Character('\u00E8'),"&egrave;");
		textSet.put(new Character('\u00C9'),"&Eacute;");
		textSet.put(new Character('\u00E9'),"&eacute;");
		textSet.put(new Character('\u00CA'),"&Ecirc;");
		textSet.put(new Character('\u00EA'),"&ecirc;");
		textSet.put(new Character('\u00CB'),"&Euml;");
		textSet.put(new Character('\u00EB'),"&euml;");
		textSet.put(new Character('\u00CC'),"&Igrave;");
		textSet.put(new Character('\u00EC'),"&igrave;");
		textSet.put(new Character('\u00CD'),"&Iacute;");
		textSet.put(new Character('\u00ED'),"&iacute;");
		textSet.put(new Character('\u00CE'),"&Icirc;");
		textSet.put(new Character('\u00EE'),"&icirc;");
		textSet.put(new Character('\u00CF'),"&Iuml;");
		textSet.put(new Character('\u00EF'),"&iuml;");
		textSet.put(new Character('\u00D0'),"&ETH;");
		textSet.put(new Character('\u00F0'),"&eth;");
		textSet.put(new Character('\u00D1'),"&Ntilde;");
		textSet.put(new Character('\u00F1'),"&ntilde;");
		textSet.put(new Character('\u00D2'),"&Ograve;");
		textSet.put(new Character('\u00F2'),"&ograve;");
		textSet.put(new Character('\u00D3'),"&Oacute;");
		textSet.put(new Character('\u00F3'),"&oacute;");
		textSet.put(new Character('\u00D4'),"&Ocirc;");
		textSet.put(new Character('\u00F4'),"&ocirc;");
		textSet.put(new Character('\u00D5'),"&Otilde;");
		textSet.put(new Character('\u00F5'),"&otilde;");
		textSet.put(new Character('\u00D6'),"&Ouml;");
		textSet.put(new Character('\u00F6'),"&ouml;");
		textSet.put(new Character('\u00D7'),"&times;");
		textSet.put(new Character('\u00F7'),"&divide;");
		textSet.put(new Character('\u00D8'),"&Oslash;");
		textSet.put(new Character('\u00F8'),"&oslash;");
		textSet.put(new Character('\u00D9'),"&Ugrave;");
		textSet.put(new Character('\u00F9'),"&ugrave;");
		textSet.put(new Character('\u00DA'),"&Uacute;");
		textSet.put(new Character('\u00FA'),"&uacute;");
		textSet.put(new Character('\u00DB'),"&Ucirc;");
		textSet.put(new Character('\u00FB'),"&ucirc;");
		textSet.put(new Character('\u00DC'),"&Uuml;");
		textSet.put(new Character('\u00FC'),"&uuml;");
		textSet.put(new Character('\u00DD'),"&Yacute;");
		textSet.put(new Character('\u00FD'),"&yacute;");
		textSet.put(new Character('\u00DE'),"&THORN;");
		textSet.put(new Character('\u00FE'),"&thorn;");
		textSet.put(new Character('\u00DF'),"&szlig;");
		textSet.put(new Character('\u00FF'),"&yuml;");
		textSet.put(new Character('\u2013'),"&ndash;");
		textSet.put(new Character('\u2014'),"&mdash;");
		textSet.put(new Character('\u2018'),"&lsquo;");
		textSet.put(new Character('\u2019'),"&rsquo;");
		textSet.put(new Character('\u201C'),"&ldquo;");
		textSet.put(new Character('\u201D'),"&rdquo;");
		textSet.put(new Character('\u20AC'),"&euro;");
	}
	
	public static boolean containsKey(char key){
		return textSet.containsKey(new Character(key));
	}

	public static String getValue(char key){
		return (String)textSet.get(new Character(key));
	}
	
	public static String isEcsToLeft(String sourceText, int sourcePosition) {
		String s1 = sourceText;
		
		String s2 = s1.substring(0, Math.min(sourcePosition, s1.length()));
		int startIndex = s2.lastIndexOf(CHR_ESC_START);
		int endIndex = s2.lastIndexOf(CHR_ESC_STOP);
		if(startIndex >= 0 && endIndex >= 0 && endIndex == s2.length()-1){
			String value = s2.substring(startIndex, endIndex+1);
			if(textSet.containsValue(value)) return value;
		}
		
		return null;
	}

	public static String isEcsToRight(String sourceText, int sourcePosition) {
		String s1 = sourceText;
		
		String s2 = s1.substring(sourcePosition, s1.length());
		int startIndex = s2.indexOf(CHR_ESC_START);
		int endIndex = s2.indexOf(CHR_ESC_STOP);
		if(startIndex >= 0 && endIndex >= 0 && startIndex == 0){
			String value = s2.substring(startIndex, endIndex+1);
			if(textSet.containsValue(value)) return value;
		}
		
		return null;
	}
	
	public static boolean isEcs(String sourceText, String visualText, int visualPosition) {
		String s1 = visualText;
		
		String s3 = s1.substring(0, Math.min(visualPosition+1, s1.length()));
		s3 = sourceText(s3);
		
		Character ch;
		int sourceIndex = 0;
		int visualIndex = 0;
		for(visualIndex=0;visualIndex<s3.length();visualIndex++){
			ch = new Character(s3.charAt(visualIndex));
			
			String value = (String)textSet.get(ch);
			char sourceChar = sourceText.charAt(sourceIndex);
			if(value != null){
				if(sourceChar != ch.charValue() || (ch.charValue() == '&' && sourceText.indexOf("&amp;",sourceIndex) >= 0)){
					if(visualIndex == visualPosition) return true;
					sourceIndex += value.length()-1;
				}
			}else if(s3.charAt(visualIndex) == 160){
				if(sourceChar != ' ' && sourceChar != 160){
					if(visualIndex == visualPosition) return true;
					sourceIndex += 5;
				}
			}
			sourceIndex++;
		}
		return false;
	}

//	public static int sourcePosition(String sourceText, String visualText, int visualPosition) {
//		String s1 = visualText;
//		
//		String s3 = s1.substring(0, Math.min(visualPosition, s1.length()));
//		s3 = sourceText(s3);
//		
//		Character ch;
//		int sourceIndex = 0;
//		int visualIndex = 0;
//		for(visualIndex=0;visualIndex<s3.length();visualIndex++){
//			ch = new Character(s3.charAt(visualIndex));
//			
//			String value = (String)textSet.get(ch);
//			char sourceChar = sourceText.charAt(sourceIndex);
//			if(value != null){
//				if(sourceChar != ch.charValue() || (ch.charValue() == '&' && sourceText.indexOf("&amp;",sourceIndex) >= 0)){
//					sourceIndex += value.length()-1;
//				}
//			}else if(s3.charAt(visualIndex) == 160){
//				if(sourceChar != ' ' && sourceChar != 160){
//					sourceIndex += 5;
//				}
//			}
//			sourceIndex++;
//		}
//		return s3.length()+(sourceIndex-visualIndex);
//	}

	public static int sourcePosition(String sourceText, String visualText, int visualPosition) {
		int sourceIndex = 0;
		int visualIndex = 0;
		
		while (sourceIndex < sourceText.length() && visualIndex < visualPosition) {
			char sourceChar = sourceText.charAt(sourceIndex);
			if (sourceChar == '\r') {
				if (visualText.charAt(visualIndex) == '\r') {
					visualIndex++;
				}
				sourceIndex++;
			} else if (sourceChar == CHR_ESC_START) {
				int end = sourceText.indexOf(CHR_ESC_STOP, sourceIndex + 1);
				if (end != -1 && textSet.containsValue(sourceText.substring(sourceIndex, end + 1))) {
					sourceIndex += end - sourceIndex;
				}
				sourceIndex++;
				visualIndex++;
			} else {
				sourceIndex++;
				visualIndex++;
			}
		}
		return sourceIndex;
	}

	public static int sourceInnerPosition(String visualText, int visualPosition) {
		visualText = visualText.substring(0, Math.min(visualPosition, visualText.length()));
		String sourceText = visualText.replaceAll(VISUAL_BREAK, SOURCE_BREAK);
		return sourceText.length();
	}
	
	public static int visualPosition(String sourceText, int sourcePosition) {
		int calcPosition = sourcePosition;
		int start = sourceText.indexOf(CHR_ESC_START);
		while (start != -1 && start < sourcePosition && start + 1 < sourceText.length()) {
			int stop = sourceText.indexOf(CHR_ESC_STOP, start + 1);
			if (stop == -1) {
				break;
			} else if (stop < sourcePosition) {
				if (textSet.containsValue(sourceText.substring(start, stop + 1))) {
					calcPosition -= stop - start;
				}
				if (stop + 1 < sourceText.length()) {
					start = sourceText.indexOf(CHR_ESC_START, stop + 1);
				} else {
					break;
				} 
			} else {
				calcPosition -= sourcePosition - start;
				break;
			}
		}
		String s1 = sourceText.substring(0, sourcePosition);
		String s2 = s1.replaceAll(SOURCE_BREAK, VISUAL_BREAK);
		return calcPosition - (s1.length() - s2.length());
	}
	
	public static int visualInnerPosition(String sourceText, int sourcePosition) {
		sourceText = sourceText.substring(0, Math.min(sourcePosition, sourceText.length()));
		String visualText = sourceText.replaceAll(SOURCE_BREAK, VISUAL_BREAK);
		return visualText.length();
	}
	
	public static int _visualPosition(String sourceText, int sourcePosition) {
		int delta = 7;
		if(sourcePosition+delta > sourceText.length()) delta = sourceText.length()-sourcePosition;
		int position = Math.min(sourcePosition+delta, sourceText.length());
		
		String s1 = sourceText.substring(0, position);
		int start1 = s1.length()-delta;
		if(position < sourceText.length()){
			while(position > sourcePosition){
				int ampersandPosition = s1.lastIndexOf("&");
				int semicolonPosition = s1.lastIndexOf(";");
				if(ampersandPosition > 0 && semicolonPosition > 0 && ampersandPosition < semicolonPosition && (semicolonPosition+1) > sourcePosition){
					String value = s1.substring(ampersandPosition, semicolonPosition+1);
					if(textSet.containsValue(value))
						position = ampersandPosition;
					else 
						position--;
				}else if(ampersandPosition > 0 && semicolonPosition > 0 && semicolonPosition > sourcePosition){
					position = semicolonPosition+1;
				}else{
					position--;
				}
				s1 = s1.substring(0, position);
			}
		}else{
			position -= delta;
			s1 = s1.substring(0, position);
		}
		int start2 = s1.length();
		int startLength = s1.length();
		
		String[] strings = (String[]) textSet.values().toArray(new String[]{});
		for(int i=0;i<strings.length;i++){
			s1 = s1.replaceAll(strings[i]," ");
		}
		s1 = visualText(s1);
		return sourcePosition-(startLength - s1.length())-(start1-start2);
	}
	
	public static String visualText(String sourceText) {
		return sourceText.replaceAll(SOURCE_BREAK, VISUAL_BREAK);
	}
	
	public static String sourceText(String visualText) {
		return visualText.replaceAll(VISUAL_BREAK, SOURCE_BREAK).replace(VISUAL_SPACE, SOURCE_SPACE);
	}

	public static boolean isWhitespace(char sourceString) {
		return SPCHARS.indexOf(sourceString) != -1;
	}
	
	public static boolean isWhitespaceText(String sourceString) {
		if (sourceString != null && sourceString.length() > 0) {
			for (int i = 0; i < sourceString.length(); i++) {
				if (!TextUtil.isWhitespace(sourceString.charAt(i))) 
					return false;
			}
		}
		return true;
	}
}

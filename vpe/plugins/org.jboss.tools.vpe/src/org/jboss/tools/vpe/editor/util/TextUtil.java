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

import org.mozilla.interfaces.nsIDOMKeyEvent;

public class TextUtil {
	private final static String SOURCE_BREAK = "\r\n";  //$NON-NLS-1$
	private final static String VISUAL_BREAK = "\n";  //$NON-NLS-1$
	private final static char SOURCE_SPACE = ' '; 
	private final static char VISUAL_SPACE = 160; 
	private final static int CHR_ESC_START = '&';
	private final static int CHR_ESC_STOP = ';';
	private final static String SPCHARS = "\f\n\r\t\u0020\u2028\u2029"; //$NON-NLS-1$
	private final static Map<Character,String> textSet = new HashMap<Character,String>();
	static{
		textSet.put(new Character('"'), "&quot;"); //$NON-NLS-1$
		textSet.put(new Character('&'),"&amp;"); //$NON-NLS-1$
		textSet.put(new Character('<'),"&lt;"); //$NON-NLS-1$
		textSet.put(new Character('>'),"&gt;"); //$NON-NLS-1$
//		textSet.put(new Character(' '),"&nbsp;");
		textSet.put(new Character('\u00A1'),"&iexcl;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A2'),"&cent;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A3'),"&pound;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A4'),"&curren;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A5'),"&yen;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A6'),"&brvbar;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A7'),"&sect;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A8'),"&uml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00A9'),"&copy;"); //$NON-NLS-1$
		textSet.put(new Character('\u00AA'),"&ordf;"); //$NON-NLS-1$
		textSet.put(new Character('\u00AB'),"&laquo;"); //$NON-NLS-1$
		textSet.put(new Character('\u00AC'),"&not;"); //$NON-NLS-1$
		//textSet.put(new Character('\u00AD'),"&shy;");
		textSet.put(new Character('\u00AE'),"&reg;"); //$NON-NLS-1$
		textSet.put(new Character('\u00AF'),"&macr;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B0'),"&deg;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B1'),"&plusmn;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B2'),"&sup2;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B3'),"&sup3;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B4'),"&acute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B5'),"&micro;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B6'),"&para;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B7'),"&middot;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B8'),"&cedil;"); //$NON-NLS-1$
		textSet.put(new Character('\u00B9'),"&sup1;"); //$NON-NLS-1$
		textSet.put(new Character('\u00BA'),"&ordm;"); //$NON-NLS-1$
		textSet.put(new Character('\u00BB'),"&raquo;"); //$NON-NLS-1$
		textSet.put(new Character('\u00BC'),"&frac14;"); //$NON-NLS-1$
		textSet.put(new Character('\u00BD'),"&frac12;"); //$NON-NLS-1$
		textSet.put(new Character('\u00BE'),"&frac34;"); //$NON-NLS-1$
		textSet.put(new Character('\u00BF'),"&iquest;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C0'),"&Agrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E0'),"&agrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C1'),"&Aacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E1'),"&aacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C2'),"&Acirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E2'),"&acirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C3'),"&Atilde;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E3'),"&atilde;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C4'),"&Auml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E4'),"&auml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C5'),"&Aring;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E5'),"&aring;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C6'),"&AElig;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E6'),"&aelig;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C7'),"&Ccedil;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E7'),"&ccedil;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C8'),"&Egrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E8'),"&egrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00C9'),"&Eacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00E9'),"&eacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00CA'),"&Ecirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00EA'),"&ecirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00CB'),"&Euml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00EB'),"&euml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00CC'),"&Igrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00EC'),"&igrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00CD'),"&Iacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00ED'),"&iacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00CE'),"&Icirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00EE'),"&icirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00CF'),"&Iuml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00EF'),"&iuml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D0'),"&ETH;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F0'),"&eth;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D1'),"&Ntilde;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F1'),"&ntilde;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D2'),"&Ograve;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F2'),"&ograve;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D3'),"&Oacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F3'),"&oacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D4'),"&Ocirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F4'),"&ocirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D5'),"&Otilde;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F5'),"&otilde;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D6'),"&Ouml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F6'),"&ouml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D7'),"&times;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F7'),"&divide;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D8'),"&Oslash;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F8'),"&oslash;"); //$NON-NLS-1$
		textSet.put(new Character('\u00D9'),"&Ugrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00F9'),"&ugrave;"); //$NON-NLS-1$
		textSet.put(new Character('\u00DA'),"&Uacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00FA'),"&uacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00DB'),"&Ucirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00FB'),"&ucirc;"); //$NON-NLS-1$
		textSet.put(new Character('\u00DC'),"&Uuml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00FC'),"&uuml;"); //$NON-NLS-1$
		textSet.put(new Character('\u00DD'),"&Yacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00FD'),"&yacute;"); //$NON-NLS-1$
		textSet.put(new Character('\u00DE'),"&THORN;"); //$NON-NLS-1$
		textSet.put(new Character('\u00FE'),"&thorn;"); //$NON-NLS-1$
		textSet.put(new Character('\u00DF'),"&szlig;"); //$NON-NLS-1$
		textSet.put(new Character('\u00FF'),"&yuml;"); //$NON-NLS-1$
		textSet.put(new Character('\u2013'),"&ndash;"); //$NON-NLS-1$
		textSet.put(new Character('\u2014'),"&mdash;"); //$NON-NLS-1$
		textSet.put(new Character('\u2018'),"&lsquo;"); //$NON-NLS-1$
		textSet.put(new Character('\u2019'),"&rsquo;"); //$NON-NLS-1$
		textSet.put(new Character('\u201C'),"&ldquo;"); //$NON-NLS-1$
		textSet.put(new Character('\u201D'),"&rdquo;"); //$NON-NLS-1$
		textSet.put(new Character('\u20AC'),"&euro;"); //$NON-NLS-1$
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
				if(sourceChar != ch.charValue() || (ch.charValue() == '&' && sourceText.indexOf("&amp;",sourceIndex) >= 0)){ //$NON-NLS-1$
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

	public static int sourceInnerPosition(String visualText, long visualPosition) {
		visualText = visualText.substring(0, (int)Math.min(visualPosition, visualText.length()));
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
				int ampersandPosition = s1.lastIndexOf("&"); //$NON-NLS-1$
				int semicolonPosition = s1.lastIndexOf(";"); //$NON-NLS-1$
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
			s1 = s1.replaceAll(strings[i]," "); //$NON-NLS-1$
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
		String visualString = visualText((new Character(sourceString)).toString());
		return SPCHARS.indexOf(sourceString) != -1;
	}
	
	public static boolean isWhitespaceText(String sourceString) {
		if (sourceString != null && sourceString.length() > 0) {
			char[] chars = sourceString.toCharArray();
			for (int i = 0; i < sourceString.length(); i++) {
				if (!TextUtil.isWhitespace(sourceString.charAt(i))) 
					return false;
			}
		}
		return true;
	}
	
	public static String getChar(nsIDOMKeyEvent keyEvent) {
		// get inserted string
		long charCode = keyEvent.getCharCode();
		char[] s = new char[1];
		s[0] = (char) charCode;
		String str = new String(s);
		if (TextUtil.containsKey(s[0])) {
			str = TextUtil.getValue(s[0]);
		}

		return str;
	}
	

    /**
     * @param value
     * @return
     */
    public static boolean isContainsEl(final String value) {
        return (value.contains("#{") || value.contains("${")); //$NON-NLS-1$//$NON-NLS-2$
    }

}

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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.tools.vpe.VpePlugin;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.w3c.dom.Node;

public class TextUtil {

	// ISO codes are contained in interval [0,2^31 -1]
	private final static int MIN_ISO_CODE_RANGE = 0;
	private final static int MAX_ISO_CODE_RANGE = Integer.MAX_VALUE;

	private final static String CODES_FILE = "htmlCodes.properties"; //$NON-NLS-1$

	private final static String SOURCE_BREAK = "\r\n"; //$NON-NLS-1$
	private final static String VISUAL_BREAK = "\n"; //$NON-NLS-1$
	private final static char SOURCE_SPACE = ' ';
	private final static char VISUAL_SPACE = 160;
	private final static char CHR_ESC_START = '&';
	private final static char CHR_SHARP = '#';
	private final static char CHR_ESC_STOP = ';';
	private final static char CHR_HEX_FLAG = 'x';
	private final static String SPCHARS = "\f\n\r\t\u0020\u2028\u2029"; //$NON-NLS-1$
	private static final Pattern elPattern = Pattern.compile("(#|\\$)\\{\\s*([^\\s])"); //$NON-NLS-1$
	
	private final static Map<Character, String> textSet = new HashMap<Character, String>();
	static {
		try {
			InputStream is = VpePlugin.getDefault().getBundle().getResource(
					CODES_FILE).openStream();
			Properties prop = new Properties();
			prop.load(is);
			for (Entry<Object, Object> e : prop.entrySet()) {
				textSet.put((char) Integer.parseInt((String) e.getKey()),
						(String) e.getValue());
			}
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean containsKey(char key) {

		return textSet.containsKey(key);
	}

	public static String getValue(char key) {
		return (String) textSet.get(key);
	}

	/**
	 * @deprecated
	 * @param sourceText
	 * @param sourcePosition
	 * @return
	 */
	public static String isEcsToLeft(String sourceText, int sourcePosition) {
		String s1 = sourceText;

		String s2 = s1.substring(0, Math.min(sourcePosition, s1.length()));
		int startIndex = s2.lastIndexOf(CHR_ESC_START);
		int endIndex = s2.lastIndexOf(CHR_ESC_STOP);
		if (startIndex >= 0 && endIndex >= 0 && endIndex == s2.length() - 1) {
			String value = s2.substring(startIndex, endIndex + 1);
			if (textSet.containsValue(value))
				return value;
		}

		return null;
	}

	/**
	 * @deprecated
	 * @param sourceText
	 * @param sourcePosition
	 * @return
	 */
	public static String isEcsToRight(String sourceText, int sourcePosition) {
		String s1 = sourceText;

		String s2 = s1.substring(sourcePosition, s1.length());
		int startIndex = s2.indexOf(CHR_ESC_START);
		int endIndex = s2.indexOf(CHR_ESC_STOP);
		if (startIndex >= 0 && endIndex >= 0 && startIndex == 0) {
			String value = s2.substring(startIndex, endIndex + 1);
			if (textSet.containsValue(value))
				return value;
		}

		return null;
	}

	/**
	 * check if text to the right of sourcePosition is escape sequence and
	 * return offset to the end of the escape sequence
	 * 
	 * @param sourceText
	 * @param sourcePosition
	 * @return offset to the end of the escape sequence or 0 if it as not found
	 */
	public static int checkEscToRight(String sourceText, int sourcePosition) {

		if ((sourceText != null) && (sourceText.length() > sourcePosition)
				&& (sourceText.charAt(sourcePosition) == CHR_ESC_START)) {
			int end = sourceText.indexOf(CHR_ESC_STOP, sourcePosition);
			if ((end != -1)
					&& (isEsc(sourceText.substring(sourcePosition, end + 1))))
				return end - sourcePosition + 1;
		}

		return 0;
	}

	/**
	 * check if text to the left of sourcePosition is escape sequence and return
	 * offset to the start of the escape sequence
	 * 
	 * @param sourceText
	 * @param sourcePosition
	 * @return offset to the start of the escape sequence or 0 if it as not
	 *         found
	 */
	public static int checkEscToLeft(String sourceText, int sourcePosition) {

		if ((sourceText != null) && (sourceText.length() > sourcePosition)
				&& (sourceText.charAt(sourcePosition - 1) == CHR_ESC_STOP)) {
			String cuttedString = sourceText.substring(0, sourcePosition);
			int start = cuttedString.lastIndexOf(CHR_ESC_START);
			if ((start != -1)
					&& (isEsc(sourceText.substring(start, sourcePosition)))) {

				return start - sourcePosition;

			}

		}

		return 0;
	}

	/**
	 * check if text is escape sequence
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEsc(String text) {

		if (text.charAt(0) == CHR_ESC_START
				&& text.charAt(text.length() - 1) == CHR_ESC_STOP) {
			// find index of the escape sequence's end

			int end = text.length() - 1;

			// if next symbol is '#' then escape sequence may be iso code
			if ((text.length() > 2) && text.charAt(1) == CHR_SHARP) {

				// convert string to number

				int isoCode = MIN_ISO_CODE_RANGE - 1;

				// if number has prefix 'x' means that it is hex number
				int radix = text.charAt(2) == CHR_HEX_FLAG ? 16 : 10;
				int offcet = text.charAt(2) == CHR_HEX_FLAG ? 3 : 2;

				try {
					isoCode = Integer.parseInt(text.substring(offcet, end),
							radix);
				} catch (NumberFormatException exception) {
					//just ignore
				}

				// if escape sequence is iso code
				if (isoCode >= MIN_ISO_CODE_RANGE
						&& isoCode <= MAX_ISO_CODE_RANGE) {
					return true;
				}

			} else if (textSet.containsValue(text.substring(0, end + 1))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @deprecated
	 * @param sourceText
	 * @param visualText
	 * @param visualPosition
	 * @return
	 */
	public static boolean isEcs(String sourceText, String visualText,
			int visualPosition) {
		String s1 = visualText;

		String s3 = s1.substring(0, Math.min(visualPosition + 1, s1.length()));
		s3 = sourceText(s3);

		Character ch;
		int sourceIndex = 0;
		int visualIndex = 0;
		for (visualIndex = 0; visualIndex < s3.length(); visualIndex++) {
			ch = s3.charAt(visualIndex);

			String value = (String) textSet.get(ch);
			char sourceChar = sourceText.charAt(sourceIndex);
			if (value != null) {
				if (sourceChar != ch.charValue()
						|| (ch.charValue() == '&' && sourceText.indexOf(
								"&amp;", sourceIndex) >= 0)) { //$NON-NLS-1$
					if (visualIndex == visualPosition)
						return true;
					sourceIndex += value.length() - 1;
				}
			} else if (s3.charAt(visualIndex) == 160) {
				if (sourceChar != ' ' && sourceChar != 160) {
					if (visualIndex == visualPosition)
						return true;
					sourceIndex += 5;
				}
			}
			sourceIndex++;
		}
		return false;
	}

	// public static int sourcePosition(String sourceText, String visualText,
	// int visualPosition) {
	// String s1 = visualText;
	//		
	// String s3 = s1.substring(0, Math.min(visualPosition, s1.length()));
	// s3 = sourceText(s3);
	//		
	// Character ch;
	// int sourceIndex = 0;
	// int visualIndex = 0;
	// for(visualIndex=0;visualIndex<s3.length();visualIndex++){
	// ch = new Character(s3.charAt(visualIndex));
	//			
	// String value = (String)textSet.get(ch);
	// char sourceChar = sourceText.charAt(sourceIndex);
	// if(value != null){
	// if(sourceChar != ch.charValue() || (ch.charValue() == '&' &&
	// sourceText.indexOf("&amp;",sourceIndex) >= 0)){
	// sourceIndex += value.length()-1;
	// }
	// }else if(s3.charAt(visualIndex) == 160){
	// if(sourceChar != ' ' && sourceChar != 160){
	// sourceIndex += 5;
	// }
	// }
	// sourceIndex++;
	// }
	// return s3.length()+(sourceIndex-visualIndex);
	// }
	public static int sourcePosition(String sourceText, String visualText,
			int visualPosition) {
		int sourceIndex = 0;
		int visualIndex = 0;
		while (sourceIndex < sourceText.length()
				&& visualIndex < visualPosition) {
			char sourceChar = sourceText.charAt(sourceIndex);
			if (sourceChar == '\r') {
				if (visualText.charAt(visualIndex) == '\r') {
					visualIndex++;
				}
				sourceIndex++;
			} else if (sourceChar == CHR_ESC_START) {
				// find index of the escape sequence's end
				int end = sourceText.indexOf(CHR_ESC_STOP, sourceIndex + 1);

				// if it is escape sequence
				if ((end != -1)
						&& isEsc(sourceText.substring(sourceIndex, end + 1))) {

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
		visualText = visualText.substring(0, (int) Math.min(visualPosition,
				visualText.length()));
		String sourceText = visualText.replaceAll(VISUAL_BREAK, SOURCE_BREAK);
		return sourceText.length();
	}

	public static int visualPosition(String sourceText, int sourcePosition) {
		int calcPosition = sourcePosition;
		if (sourceText == null) {
			return 0;
		}
		int start = sourceText.indexOf(CHR_ESC_START);
		while (start != -1 && start < sourcePosition
				&& start + 1 < sourceText.length()) {
			int stop = sourceText.indexOf(CHR_ESC_STOP, start + 1);
			if (stop == -1) {
				break;
			} else if (stop < sourcePosition) {
				if (textSet
						.containsValue(sourceText.substring(start, stop + 1))) {
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
		sourceText = sourceText.substring(0, Math.min(sourcePosition,
				sourceText.length()));
		String visualText = sourceText.replaceAll(SOURCE_BREAK, VISUAL_BREAK);
		return visualText.length();
	}

	public static int _visualPosition(String sourceText, int sourcePosition) {
		int delta = 7;
		if (sourcePosition + delta > sourceText.length())
			delta = sourceText.length() - sourcePosition;
		int position = Math.min(sourcePosition + delta, sourceText.length());

		String s1 = sourceText.substring(0, position);
		int start1 = s1.length() - delta;
		if (position < sourceText.length()) {
			while (position > sourcePosition) {
				int ampersandPosition = s1.lastIndexOf("&"); //$NON-NLS-1$
				int semicolonPosition = s1.lastIndexOf(";"); //$NON-NLS-1$
				if (ampersandPosition > 0 && semicolonPosition > 0
						&& ampersandPosition < semicolonPosition
						&& (semicolonPosition + 1) > sourcePosition) {
					String value = s1.substring(ampersandPosition,
							semicolonPosition + 1);
					if (textSet.containsValue(value))
						position = ampersandPosition;
					else
						position--;
				} else if (ampersandPosition > 0 && semicolonPosition > 0
						&& semicolonPosition > sourcePosition) {
					position = semicolonPosition + 1;
				} else {
					position--;
				}
				s1 = s1.substring(0, position);
			}
		} else {
			position -= delta;
			s1 = s1.substring(0, position);
		}
		int start2 = s1.length();
		int startLength = s1.length();

		String[] strings = (String[]) textSet.values().toArray(new String[] {});
		for (int i = 0; i < strings.length; i++) {
			s1 = s1.replaceAll(strings[i], " "); //$NON-NLS-1$
		}
		s1 = visualText(s1);
		return sourcePosition - (startLength - s1.length()) - (start1 - start2);
	}

	public static String visualText(String sourceText) {
		return sourceText.replaceAll(SOURCE_BREAK, VISUAL_BREAK);
	}

	public static String sourceText(String visualText) {
		return visualText.replaceAll(VISUAL_BREAK, SOURCE_BREAK).replace(
				VISUAL_SPACE, SOURCE_SPACE);
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
	 * @author mareshkau
	 * @param node or attribute for which we want calculate position start el position
	 * 
	 * @return position if we can find position
	 * 			-1 if we can't find pisition, <document_offcet>'#{el}', return start position of el
	 */
	public static int getStartELDocumentPosition(Node node) {

		if (node != null && node.getNodeValue() != null
				&& node.getNodeValue().length() > 0) {
			int elPosition = 0;
			Matcher beginELExpresion = elPattern.matcher(node.getNodeValue());
			if (beginELExpresion.find()) {
				// +1 becouse we should have position of first symbol
				elPosition = beginELExpresion.start(2) + 1;
			}
			int offset = NodesManagingUtil.getStartOffsetNode(node)
					+ elPosition;
			return offset;
		}
		return -1;
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isContainsEl(final String value) {
		return (value.contains("#{") || value.contains("${")); //$NON-NLS-1$//$NON-NLS-2$
	}

}

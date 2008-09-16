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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.swt.graphics.RGB;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;

/**
 * 
 * Utility class
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class Util {

    private static String RGB = "rgb"; //$NON-NLS-1$

    private static String NONE = "none"; //$NON-NLS-1$

    private static String THIN = "thin"; //$NON-NLS-1$

    private static String COMMA = ","; //$NON-NLS-1$

    private static int START_INDEX_RED = 1;
    private static int END_INDEX_RED = 3;
    private static int START_INDEX_GRENN = 3;
    private static int END_INDEX_GREEN = 5;
    private static int START_INDEX_BLUE = 5;
    private static int END_INDEX_BLUE = 7;

    private static int MAX_VALUE_RGB = 255;
    private static int MIN_VALUE_RGB = 0;

    private static int COUNT_COLORS = 3;

    private static int RADIX = 16;

    private static int COLOR_LENGTH = 7;

    private static int START_COLOR_INDEX = 0;

    private static char OPEN_BRACKET = '{';
    private static char CLOSE_BRACKET = '}';

    private static char SHARP = '#';
    private static String SHARP_STRING = "#"; //$NON-NLS-1$

    private static String ZERO_STR = "0"; //$NON-NLS-1$
    private static int NORMAL_MIN_VALUE = 10;

    /**
     * Method for checking contain or not css attribute folder
     * 
     * @param name
     *                Name css attribute
     * @return true - contain, or else - dont contain
     */
    public static boolean containFolder(String name) {

	for (int i = 0; i < Constants.elemFolder.length; i++)
	    if (Constants.elemFolder[i].equals(name))
		return true;
	return false;
    }

    /**
     * Method for search css attribute in block elements (Aural, Boxes,....)
     * 
     * @param name
     *                Name css attribute
     * @param set
     *                Set of block elemnts
     * @return true - find, or else - dont find
     */
    public static boolean searchOnBlock(String name, Set<String> set) {

	for (String str1 : set)
	    if (str1.equals(name))
		return true;

	return false;
    }

    /**
     * Method for search css attribute in combo elements (text-decoration,
     * font-weight,....)
     * 
     * @param name
     *                Name css attribute
     * @param set
     *                Set of combo elemnts
     * @return true - find, or else - dont find
     */
    public static boolean searchInCombo(String name, Set<String> set) {

	for (String str1 : set)
	    if (str1.equals(name))
		return true;

	return false;
    }

    /**
     * Method for search string into css attributes
     * 
     * @param name
     *                Name
     * @param elementMap
     *                Map of css attributes
     * @return true - find, or else - dont find
     */
    public static boolean searchInElement(String name,
	    HashMap<String, ArrayList<String>> elementMap) {

	Set<String> set = elementMap.keySet();

	for (String str : set) {

	    ArrayList<String> list = elementMap.get(str);
	    for (String str1 : list)
		if (str1.equals(name))
		    return true;
	}

	return false;
    }

    /**
     * Method for get RGB from string
     * 
     * @param color
     *                Color string
     * @return RGB color, or null, if color invalid
     */
    public static RGB getColor(String color) {

	if (color.equals(Constants.EMPTY_STRING) || color.equals(NONE))
	    return null;

	if (color.charAt(START_COLOR_INDEX) == SHARP
		&& color.length() == COLOR_LENGTH) {

	    String strR = color.substring(START_INDEX_RED, END_INDEX_RED);
	    String strG = color.substring(START_INDEX_GRENN, END_INDEX_GREEN);
	    String strB = color.substring(START_INDEX_BLUE, END_INDEX_BLUE);

	    try {
		Integer.parseInt(strR, RADIX);
		Integer.parseInt(strG, RADIX);
		Integer.parseInt(strB, RADIX);
	    } catch (RuntimeException e) {
		return null;
	    }
	    return convertColorHEX(color);
	} else if (color.toLowerCase().indexOf(RGB) != Constants.DONT_CONTAIN) {

	    int start = color.indexOf(OPEN_BRACKET);
	    int end = color.indexOf(CLOSE_BRACKET);
	    String str = color.substring(start + 1, end);

	    StringTokenizer st = new StringTokenizer(str, COMMA);

	    int j = 0;
	    while (st.hasMoreTokens()) {
		try {
		    int i = Integer.parseInt(st.nextToken().trim());
		    if (i < MIN_VALUE_RGB || i > MAX_VALUE_RGB)
			return null;

		} catch (NumberFormatException e) {
		    return null;
		}
		j++;
	    }

	    if (j == COUNT_COLORS)
		return convertColorRGB(color);
	} else {
	    HashMap<String, String> colorMap = ColorParser.getInstance().getMap();

	    for (String key : colorMap.keySet())
		if (colorMap.get(key).equalsIgnoreCase(color))
		    return convertColorHEX(key);
	}

	return null;
    }

    /**
     * Method for convert string(123px) into two string (123 and px)
     * 
     * @param str
     *                String for convert
     * @return Array two strings, or null, if str uncorrect
     */
    public static String[] convertExtString(String str) {

	if (str.equalsIgnoreCase(THIN))
	    return new String[] { THIN, Constants.EMPTY_STRING };

	if (str == null)
	    return null;

	if (str.trim().equals(Constants.EMPTY_STRING))
	    return new String[] { Constants.EMPTY_STRING,
		    Constants.EMPTY_STRING };

	String newStr = str.toLowerCase().trim();

	int index = -1;
	for (int i = 1; i < Constants.extSizes.length; i++) {

	    index = newStr.indexOf(Constants.extSizes[i]);
	    if (index != -1)
		break;
	}

	if (index == -1)
	    return new String[] { newStr, Constants.EMPTY_STRING };

	String number = newStr.substring(0, index);
	String ext = newStr.substring(index, index + 2);

	return new String[] { number, ext };
    }

    /**
     * Method for search css attribute into extElements
     * 
     * @param name
     *                Name of css attribute
     * @return true - find, or else - dont find
     */
    public static boolean searchInExtElement(String name) {

	for (int i = 0; i < Constants.extElem.length; i++)
	    if (Constants.extElem[i].equals(name))
		return true;
	return false;
    }

    /**
     * Method for getting RGB color from string color
     * 
     * @param color
     *                String color
     * @return RGB color
     */
    public static RGB convertColorRGB(String color) {

	String newStr = color.trim().toLowerCase();

	int rgb[] = new int[COUNT_COLORS];

	int start = newStr.indexOf(OPEN_BRACKET);
	int end = newStr.indexOf(CLOSE_BRACKET);
	String str = newStr.substring(start + 1, end);

	StringTokenizer st = new StringTokenizer(str, COMMA);
	int i = 0;
	while (st.hasMoreTokens())
	    rgb[i++] = Integer.parseInt(st.nextToken().trim());
	return new RGB(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Method for getting RGB color from hex string
     * 
     * @param color
     *                String color
     * @return RGB color
     */
    public static RGB convertColorHEX(String color) {

	String newStr = color.trim().toLowerCase();

	String strR = newStr.substring(START_INDEX_RED, END_INDEX_RED);
	String strG = newStr.substring(START_INDEX_GRENN, END_INDEX_GREEN);
	String strB = newStr.substring(START_INDEX_BLUE, END_INDEX_BLUE);

	int red = Integer.parseInt(strR, RADIX);
	int green = Integer.parseInt(strG, RADIX);
	int blue = Integer.parseInt(strB, RADIX);

	return new RGB(red, green, blue);
    }

    /**
     * Method for convert RGB to String
     * 
     * @param rgb
     *                RGB color
     * @return String color
     */
    public static String createColorString(RGB rgb) {
	String colorStr = SHARP_STRING
		+ (rgb.red < NORMAL_MIN_VALUE ? ZERO_STR
			: Constants.EMPTY_STRING)
		+ Integer.toHexString(rgb.red)
		+ (rgb.green < NORMAL_MIN_VALUE ? ZERO_STR
			: Constants.EMPTY_STRING)
		+ Integer.toHexString(rgb.green)
		+ Constants.EMPTY_STRING
		+ (rgb.blue < NORMAL_MIN_VALUE ? ZERO_STR
			: Constants.EMPTY_STRING)
		+ Integer.toHexString(rgb.blue);
	colorStr = colorStr.toUpperCase();
	if (ColorParser.getInstance().getMap().get(colorStr) != null)
	    return ColorParser.getInstance().getMap().get(colorStr);
	return colorStr;
    }
}
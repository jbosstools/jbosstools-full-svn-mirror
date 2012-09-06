/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.common.util;

/**
 * Miscellaneous String utility functions
 * 
 * @author Victor V. Rubezhny
 */
public class StringUtil {
	
	private static final char QUOT = '\"';
	private static final char APO = '\'';

	/**
	 * Cuts of the starting and ending quotes from a given text value
	 * 
	 * @param Quoted
	 *            text value
	 * @return Non-quoted text value
	 */
	public static String trimQuotes(String result) {

		int start = 0, end = result.length();
		char first = result.charAt(start);
		char last = result.charAt(end - 1);
		if (first == APO || first == QUOT) {
			start++;
		}
		if (last == APO || last == QUOT) {
			end--;
		}
		return result.substring(start, end);
	}
}

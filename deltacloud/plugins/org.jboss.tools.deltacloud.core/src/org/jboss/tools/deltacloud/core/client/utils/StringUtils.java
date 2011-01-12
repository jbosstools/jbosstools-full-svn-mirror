/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.core.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andre Dietisheim
 */
public class StringUtils {

	/**
	 * Returns a formatted string for a collection of elements that get
	 * formatted by a user supplied element formatter.
	 * 
	 * @param elements
	 *            the elements to be processed
	 * @param elements
	 *            the elements
	 * @param elementFormatter
	 *            the formatter to apply on each element to be processed
	 * @return the formatted string
	 */
	public static <E> String getFormattedString(Collection<E> elements, IElementFormatter<E> elementFormatter) {
		StringBuilder builder = new StringBuilder();
		for (E element : elements) {
			String formattedElement = elementFormatter.format(element);
			if (formattedElement != null && formattedElement.length() > 0) {
				builder.append(formattedElement);
			}
		}
		if (builder.length() > 0) {
			return builder.toString();
		} else {
			return "";
		}
	}

	public interface IElementFormatter<E> {
		public String format(E element);
	}

	/**
	 * Returns a collection of formatted strings for the given collection of
	 * elements and given formatter
	 * 
	 * @param <E>
	 *            the type of elements that shall be processed
	 * @param elements
	 *            the elements to be processed
	 * @param elementFormatter
	 *            the formatter to apply on each element to be processed
	 * @return the formatted strings
	 */
	public static <E> Collection<String> getFormattedStrings(Collection<E> elements,
			IElementFormatter<E> elementFormatter) {
		List<String> strings = new ArrayList<String>();
		for (E element : elements) {
			String formattedElement = elementFormatter.format(element);
			if (formattedElement != null && formattedElement.length() > 0) {
				strings.add(formattedElement);
			}
		}
		return strings;
	}
	
	public static String null2EmptyString(String stringValue) {
		if (stringValue == null) {
			return "";
		}
		return stringValue;
	}

	public static String emptyString2Null(String stringValue) {
		if (stringValue != null 
				&& stringValue.length() == 0) {
			return null;
		}
		return stringValue;
	}

	public static String toString(InputStream inputStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(int character = -1; (character = inputStream.read()) != -1; ) {
			builder.append((char) character);
		}
		return builder.toString();
	}
}

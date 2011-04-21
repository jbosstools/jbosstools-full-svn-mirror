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
package org.jboss.tools.internal.deltacloud.ui.utils;

import java.util.Collection;

/**
 * @author Andre Dietisheim
 */
public class StringUtils {

	/**
	 * Returns a formatted string for a collection of elements that get
	 * formatted by a user supplied element formatter.
	 * 
	 * @param <E>
	 *            the element type
	 * @param elements
	 *            the elements
	 * @param elementFormatter
	 *            the element formatter
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

}

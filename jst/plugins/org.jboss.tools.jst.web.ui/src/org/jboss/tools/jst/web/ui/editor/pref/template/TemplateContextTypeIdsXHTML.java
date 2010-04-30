/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.pref.template;

/**
 * @author mareshkau
 *
 */
public class TemplateContextTypeIdsXHTML {
	public static final String ALL = getAll();

	public static final String ATTRIBUTE = getAttribute();

	public static final String ATTRIBUTE_VALUE = getAttributeValue();

	public static final String NEW = getNew();

	public static final String TAG = getTag();

	private static String getAll() {
		return getPrefix() + "_all"; //$NON-NLS-1$
	}

	private static String getAttribute() {
		return getPrefix() + "_attribute"; //$NON-NLS-1$
	}

	private static String getAttributeValue() {
		return getPrefix() + "_attribute_value"; //$NON-NLS-1$
	}

	private static String getNew() {
		return getPrefix() + "_new"; //$NON-NLS-1$
	}

	private static String getTag() {
		return getPrefix() + "_tag"; //$NON-NLS-1$
	}

	private static String getPrefix() {
		return "xhtml"; //$NON-NLS-1$
	}
}

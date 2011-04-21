/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks10.model.smooks.util;

/**
 * @author Dart Peng
 * @Date Aug 20, 2008
 */
public class SmooksModelConstants {
	public static final String GLOBAL_PARAMETERS = "global-parameters";
	public static final String STREAM_FILTER_TYPE = "stream.filter.type";
	public static final String SAX = "SAX";
	public static final String DOM = "DOM";
	public static final String BEAN_POPULATOR = "org.milyn.javabean.BeanPopulator";
	
	public static final String[] DECODER_CLASSES = new String[] {
		"org.milyn.javabean.decoders.DateDecoder",
		"org.milyn.javabean.decoders.CalendarDecoder" };
	/**
	 * @deprecated
	 */
	public static final String DATE_DECODER = "org.milyn.javabean.decoders.DateDecoder";
	
	public static final String AT_DOCUMENT = "@document";
	
	public static final String BEAN_ID = "beanId";
	
	public static final String FORMATE = "format";
	
	public static final String LOCALE_LANGUAGE = "Locale-Language";
	
	public static final String LOCALE_CONTRY = "Locale-Country";
	
	public static final String BEAN_CLASS = "beanClass";
	
	public static final String BINDINGS = "bindings";
}

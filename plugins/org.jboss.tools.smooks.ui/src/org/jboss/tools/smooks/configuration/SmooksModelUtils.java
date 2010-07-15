/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration;

/**
 * @author Dart Peng
 * 
 */

public class SmooksModelUtils {

	public static final String KEY_TEMPLATE_TYPE = "messageType"; //$NON-NLS-1$

	public static final String FREEMARKER_TEMPLATE_TYPE_CSV = "CSV"; //$NON-NLS-1$

	public static final String FREEMARKER_TEMPLATE_TYPE_XML = "XML"; //$NON-NLS-1$

	public static final String KEY_CSV_FIELDS = "csvFields"; //$NON-NLS-1$

	public static final String KEY_INCLUDE_FIELD_NAMES = "includeFieldNames"; //$NON-NLS-1$

	public static final String KEY_TASK_ID_REF = "idref"; //$NON-NLS-1$

	public static final String KEY_OBJECT_ID = "id"; //$NON-NLS-1$

	public static final String KEY_XML_FILE_TYPE = "modelSrcType"; //$NON-NLS-1$

	public static final String KEY_XML_FILE_PATH = "modelSrc"; //$NON-NLS-1$

	public static final String KEY_XML_ROOT_NAME = "rootElementName"; //$NON-NLS-1$

	public static final String KEY_XML_FILE_TYPE_XSD = "XSD"; //$NON-NLS-1$

	public static final String KEY_XML_FILE_TYPE_XML = "XML"; //$NON-NLS-1$

	public static final String KEY_CSV_SEPERATOR = "seperator"; //$NON-NLS-1$

	public static final String KEY_CSV_QUOTE = "quote"; //$NON-NLS-1$

	public static final String INPUT_TYPE_JAVA = SmooksInputType.INPUT_TYPE_JAVA;

	public static final String INPUT_TYPE = "inputType"; //$NON-NLS-1$

	public static final String INPUT_TYPE_CUSTOME = SmooksInputType.INPUT_TYPE_CUSTOM;

	public static final String INPUT_TYPE_JSON_1_1 = SmooksInputType.INPUT_TYPE_JSON;

	public static final String INPUT_TYPE_CSV = SmooksInputType.INPUT_TYPE_CSV;

	public static final String INPUT_ACTIVE_TYPE = "input.type.actived"; //$NON-NLS-1$

	public static final String INPUT_DEACTIVE_TYPE = "input.type.deactived"; //$NON-NLS-1$

	public static final String INPUT_TYPE_CSV_1_2 = SmooksInputType.INPUT_TYPE_CSV;

	public static final String PARAM_NAME_CLASS = "class"; //$NON-NLS-1$

	public static final String PARAM_NAME_PATH = "path"; //$NON-NLS-1$

	public static final String PARAM_NAME_ACTIVED = "actived"; //$NON-NLS-1$

	public static final String INPUT_TYPE_XML = SmooksInputType.INPUT_TYPE_XML;

	public static final String INPUT_TYPE_XSD = SmooksInputType.INPUT_TYPE_XSD;

	public static final String TYPE_XSL = "xsl"; //$NON-NLS-1$

	public static final String[] TEMPLATE_TYPES = new String[] { "xsl", "ftl" }; //$NON-NLS-1$ //$NON-NLS-2$

	public static final String BEAN_CLASS = "beanClass"; //$NON-NLS-1$

	public static final String BEAN_ID = "beanId"; //$NON-NLS-1$

	public static final String BINDINGS = "bindings"; //$NON-NLS-1$

	public static final String INPUT_TYPE_EDI_1_1 = SmooksInputType.INPUT_TYPE_EDI;

	public static final String INPUT_TYPE_EDI_1_2 = SmooksInputType.INPUT_TYPE_EDI;

	public static final String INPUT_TYPE_JSON_1_2 = SmooksInputType.INPUT_TYPE_JSON;
}

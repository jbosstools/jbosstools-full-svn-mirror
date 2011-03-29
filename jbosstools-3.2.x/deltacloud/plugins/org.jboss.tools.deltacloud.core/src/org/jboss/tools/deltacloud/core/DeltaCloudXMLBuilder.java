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
package org.jboss.tools.deltacloud.core;

import java.io.PrintWriter;

public class DeltaCloudXMLBuilder {
	
	public static final String ENCODING = "UTF-8";

	public static final String TAG_CLOUDS = "clouds";
	public static final String TAG_CLOUD = "cloud";
	public static final String TAG_INSTANCE = "instance";
	public static final String ATTR_ID = "id";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_USERNAME = "username";
	public static final String ATTR_URL = "url";
	public static final String ATTR_ALIAS = "alias";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_IMAGEFILTER = "imagefilter";
	public static final String ATTR_INSTANCEFILTER = "instancefilter";
	public static final String ATTR_LASTKEYNAME = "lastkeyname";
	public static final String ATTR_LASTIMAGE = "lastimage";
	
	public static final void xmlHeader(PrintWriter printWriter) {
		printWriter.print("<?xml version=\"1.0\" encoding=\"");
		printWriter.print(ENCODING);
		printWriter.println("\"?>");
	}
	
	public static final void attribute(String attributeName, String attributeValue, PrintWriter printWriter) {
		printWriter.print(" ");
		printWriter.print(attributeName);
		printWriter.print("=\"");
		printWriter.print(attributeValue);
		printWriter.print("\"");
		
	}

	public static final void tag(String tagName, PrintWriter printWriter) {
		printWriter.print("<");
		printWriter.print(tagName);
	}

	public static final void closeTag(PrintWriter printWriter) {
		printWriter.println(" >");
	}
	
	public static final void endTag(String tagName, PrintWriter printWriter) {
		printWriter.print("</");
		printWriter.print(tagName);
		printWriter.println(">");
	}

}

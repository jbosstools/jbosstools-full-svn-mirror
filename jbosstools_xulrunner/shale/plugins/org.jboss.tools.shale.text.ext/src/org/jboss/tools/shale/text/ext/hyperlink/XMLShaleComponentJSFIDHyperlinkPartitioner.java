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
package org.jboss.tools.shale.text.ext.hyperlink;

public class XMLShaleComponentJSFIDHyperlinkPartitioner extends JSPShaleComponentJSFIDHyperlinkPartitioner {
	public static final String XML_ComponentJSFID_PARTITION = "org.jboss.tools.common.text.xml.XML_ComponentJSFID";
	
	protected String getDefaultPartitionType() {
		return XML_ComponentJSFID_PARTITION;
	}

}

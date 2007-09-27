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

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.tools.vpe.VpePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtil {

	public static Element getDocumentElement(String xmlFileName) throws Exception {
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(xmlFileName);
			InputSource inSource = new InputSource(inStream);
			return getDocumentElement(inSource);
		} finally {
			try {
				if (inStream != null) inStream.close();
			} catch (Exception e) {
				VpePlugin.getPluginLog().logError(e);
			}
		}
	}

	public static Element getDocumentElement(InputSource is) throws Exception {
		return getDocument(is).getDocumentElement();
	}
    
	public static Document getDocument(InputSource is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(is);
	}
    
	public static void removeChildren(Node node) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = len - 1; i >= 0; i--) {
				node.removeChild(children.item(i));
			}
		}
	}
}

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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
	
	/**
	 * Returns List of taglibs which are available for current node.
	 *  
	 * @param node
	 * @return
	 */
	public static List<TaglibData> processNode(Node node) {
		
		List<TaglibData> taglibs = new ArrayList<TaglibData>();
		
		if (node == null) {
			
			return taglibs;
		}
		
		Node currentNode = node;
		do {
			NamedNodeMap attribList = currentNode.getAttributes();
			if (null != attribList) {
				for (int i = 0; i < attribList.getLength(); i++) {
					Node tmp = attribList.item(i);
					processAttribute(taglibs,(Attr)tmp, false);
				}
			}	
			currentNode = currentNode.getParentNode();			

		} while(currentNode!=null);

		return taglibs;
	}
	/**
	 * Processes taglib attribute
	 * @param taglibs
	 * @param attr
	 * @param bScopePrefix
	 */	
	private static void processAttribute(List<TaglibData> taglibs, Attr attr, boolean bScopePrefix) {

		String startStr = "xmlns:";
		String name = attr.getName();
		if (!name.startsWith(startStr)) {
			return;
		}
		name = name.substring(startStr.length());
		addTaglib(taglibs , attr.getValue(), name, true, bScopePrefix);
		return;
	}
	
	/**
	 * Adds taglib to current taglibs
	 * @param taglibs
	 * @param newUri
	 * @param newPrefix
	 * @param ns
	 * @param bScopePrefix
	 */
	private static void addTaglib(List<TaglibData> taglibs, String newUri, String newPrefix, boolean ns, boolean bScopePrefix) {	
		boolean bHasSame = false;
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			if (bScopePrefix && newPrefix.equals(taglib.getPrefix())) {
				return;
			}
			if (newUri.equals(taglib.getUri()) && newPrefix.equals(taglib.getPrefix()) && ns == taglib.isNs()) {
				bHasSame = true;
				break;
			}
		}
		if (!bHasSame) {
			taglibs.add(new TaglibData(taglibs.size(), newUri, newPrefix, ns));
		}
	}
	/**
	 * Returns Taglib data by prefix
	 * 
	 * @param prefix
	 * @param taglibData
	 * @return
	 */
	public static TaglibData getTaglibForPrefix(String prefix, List<TaglibData> taglibData){
				
			for (TaglibData data : taglibData) {
				
				if(data.getPrefix()!=null && data.getPrefix().equalsIgnoreCase(prefix)) {
					 return data;
				}
			}
			
			return null;
	}
}

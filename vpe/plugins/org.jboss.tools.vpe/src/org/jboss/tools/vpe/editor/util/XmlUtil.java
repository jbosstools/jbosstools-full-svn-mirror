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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.mozilla.interfaces.nsIDOMNode;
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
			} catch (IOException e) {
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
	 * should be used only for xml documents
	 * @param node
	 * @return
	 */
	private static List<TaglibData> getTaglibsForNode(Node node) {
		
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
					processAttribute(taglibs,(Attr)tmp);
				}
			}	
			currentNode = currentNode.getParentNode();			

		} while(currentNode!=null);

		return taglibs;
	}
	
	/**
	* Calculates list of taglibs for current node adn document
	* 	we use document only if we works with jsp pages
	*  otherwise  we works with node 
	* @param source
	* @param document
	* @return collection of taglibs
	*/	
	public static List<TaglibData> getTaglibsForNode(Node source,
			VpePageContext pageContext) {

		List<TaglibData> taglibData = new ArrayList<TaglibData>();

		
		// Added by Sergey Dzmitrovich Fix for JBIDE-2581
		IPath path = FileUtil.getInputPath(pageContext.getEditPart()
				.getEditorInput());
		
		if (path != null
				&& path.getFileExtension().equals(Constants.JSP_FILE_EXTENSION)) {
			IDocument document = pageContext.getSourceBuilder()
					.getStructuredTextViewer().getDocument();

			TLDCMDocumentManager tldcmDocumentManager = TaglibController
					.getTLDCMDocumentManager(document);
			if (tldcmDocumentManager != null) {
				List<TaglibTracker> taglibs_JSP = tldcmDocumentManager
						.getTaglibTrackers();
				for (TaglibTracker taglibTracker : taglibs_JSP) {
					addTaglib(taglibData, taglibTracker.getURI(), taglibTracker
							.getPrefix(), true);
				}
			}
		} else {
			taglibData = getTaglibsForNode(source);

		}
		// add internal taglibs JBIDE-2065
		List<TaglibData> includeTaglibs = pageContext.getIncludeTaglibs();
		for (TaglibData includedTaglib : includeTaglibs) {
			addTaglib(taglibData, includedTaglib.getUri(), includedTaglib
					.getPrefix(), true);
		}
		return taglibData;
	}

	//helper method
	public static boolean hasTaglib(Node sourceNode,
			VpePageContext pageContext, String sourcePrefix) {
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,
			    pageContext);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(
			    sourcePrefix, taglibs);
		return sourceNodeTaglib != null;
	}

	//helper method
	public static String getTaglibUri(Node sourceNode,
			VpePageContext pageContext, String sourcePrefix) {
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,
			    pageContext);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(
			    sourcePrefix, taglibs);
		return sourceNodeTaglib == null ? null : sourceNodeTaglib.getUri();
	}

	/**
	 * Processes taglib attribute
	 * @param taglibs
	 * @param attr
	 * @param bScopePrefix
	 */	
	private static void processAttribute(List<TaglibData> taglibs, Attr attr) {

		String startStr = "xmlns:"; //$NON-NLS-1$
		String name = attr.getName();
		if (!name.startsWith(startStr)) {
			return;
		}
		name = name.substring(startStr.length());
		addTaglib(taglibs , attr.getValue(), name, true);
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
	private static void addTaglib(List<TaglibData> taglibs, String newUri, String newPrefix, boolean ns) {	
		boolean bHasSame = false;
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			if (newPrefix.equals(taglib.getPrefix())) {
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
	
	/**
	 * 
	 * @param uri
	 * @param taglibData
	 * @return
	 */
	public static TaglibData getTaglibForURI(String uri,
			List<TaglibData> taglibData) {

		for (TaglibData data : taglibData) {

			if (data.getUri() != null && data.getUri().equalsIgnoreCase(uri)) {
				return data;
			}
		}
		return null;
	}
	/**
	 * Creates clone node
	 * @param cloneNode
	 * @return
	 */
	public static final nsIDOMNode createClone(nsIDOMNode nodeToClone){
		nsIDOMNode result = nodeToClone.cloneNode(true);
		return result;
	}
	

}

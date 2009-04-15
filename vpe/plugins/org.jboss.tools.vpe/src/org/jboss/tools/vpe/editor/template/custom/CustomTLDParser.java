/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template.custom;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeCreatorUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * Class which created for parsing custom class definitions
 * @author mareshkau
 *
 */
public class CustomTLDParser {

	private static final String NAMESPACE="namespace"; //$NON-NLS-1$
	private static final String TAG="tag"; //$NON-NLS-1$
	private static final String TAG_NAME="tag-name"; //$NON-NLS-1$
	private static final String SOURCE="source"; //$NON-NLS-1$
	
	/**
	 * Looks for namespace, defined for custom template definition file
	 * 
	 * @param absolute path for template definition file
	 * @return namespace element if such was founded or null otherwise
	 */
	public static final String getNameSpace(IPath pathToFile){
		
		IFile  file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(pathToFile);
		if(file!=null && file.exists()) {
			Document document=null;
			try {
				document = VpeCreatorUtil.getDocumentForRead(file);
				Element rootElement = document.getDocumentElement();
				NodeList nodeList = rootElement.getElementsByTagName(NAMESPACE);
				return nodeList.item(0).getFirstChild().getNodeValue();
			} catch (Exception e) {
					VpePlugin.reportProblem(e);
			} finally {
				if(document!=null) {
					VpeCreatorUtil.releaseDocumentFromRead(document);
				}
			}
		}
		return null;
	}
	/**
	 * Looks for source value like in example bellow  
	 <tag>
	  <tag-name>paginator</tag-name>
	  <source>components/paginator.xhtml</source>
	 </tag>
	 * @param pathToFile
	 * @param name of tag
	 * @return source value if exists or null otherwise
	 */
	public static final String getSourceValuetInTag(IPath pathToFile, String name){
		IFile  file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(pathToFile);
		Document document=null;
		if(file!=null &&file.exists()) {
		try{
			document= VpeCreatorUtil.getDocumentForRead(file);
			Element rootElement = document.getDocumentElement();
			NodeList nodeList =	rootElement.getElementsByTagName(TAG);
			for(int i=0;i<nodeList.getLength();i++) {
				Node child = nodeList.item(i);
				if(!(child instanceof Element)) {
					continue;
				}
				NodeList tagsList =((Element)child).getElementsByTagName(TAG_NAME);
				for(int j=0;j<tagsList.getLength();j++) {
					if(name.equals(tagsList.item(j).getFirstChild().getNodeValue())){
						NodeList sourceList =((Element)child).getElementsByTagName(SOURCE);
						//no source element has been founded
						if(sourceList.getLength()==0) {
							return null;
						}
						return sourceList.item(0).getFirstChild().getNodeValue();
					}
				}
			}
			
		} finally {
			if(document!=null) {
				VpeCreatorUtil.releaseDocumentFromRead(document);
			}
		}
		}
		return null;
	}
}

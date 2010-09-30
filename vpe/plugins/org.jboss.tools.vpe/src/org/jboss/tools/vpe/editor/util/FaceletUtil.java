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

package org.jboss.tools.vpe.editor.util;

import java.util.HashSet;
import java.util.List;

import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FaceletUtil {

	public static final String TAG_COMPOSITION = "composition"; //$NON-NLS-1$
	public static final String TAG_COMPONENT = "component"; //$NON-NLS-1$

	public static final String ATTR_TEMPLATE = "template"; //$NON-NLS-1$

	/**
	 * facelet elements, if there are these elements on a page then other
	 * elements are deleted
	 */
	static public HashSet<String> componentElements = new HashSet<String>();

	static {
		componentElements.add(TAG_COMPOSITION); 
		componentElements.add(TAG_COMPONENT); 
	}

	/**
	 * 
	 * @param root
	 * @return
	 */
	public static Element findComponentElement(Element root) {
		if(root==null) {
			return null;
		}
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element trimmedElement = findComponentElement((Element) child);
				if (trimmedElement != null)
					return trimmedElement;
			}
		}
		if (componentElements.contains(root.getLocalName())) {
			return root;
		}
		return null;
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	public static Element getRootFaceletElement(Document document) {
		Element root = document.getDocumentElement();
		Element component = findComponentElement(root);
		return component != null ? component : root;
	}

	
	/**
	 * Checks if this node is facelet tag 
	 * 
	 * @param sourceNode the node
	 * @param taglibs specified taglibs
	 * @return <code>true</code> if this node is facelet tag
	 */
	public static boolean isFacelet(Node sourceNode, List<TaglibData> taglibs) {
		boolean isFacelet = false;
		String sourcePrefix = sourceNode.getPrefix();
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);
		if (null != sourceNodeTaglib) {
			String sourceNodeUri = sourceNodeTaglib.getUri();
			if (VisualDomUtil.FACELETS_URI.equalsIgnoreCase(sourceNodeUri)) {
				isFacelet = true;
			}
		}
//		IEditorInput iEditorInput = pageContext.getEditPart().getEditorInput();
//		if (iEditorInput instanceof IFileEditorInput) {
//			IFileEditorInput iFileEditorInput = (IFileEditorInput) iEditorInput;
//			IFile iFile = iFileEditorInput.getFile();
//			IProject project = iFile.getProject();
//			IModelNature nature = EclipseResourceUtil.getModelNature(project);
//			if (nature != null) {
//				XModel model = nature.getModel();
//				XModelObject webXML = WebAppHelper.getWebApp(model);
//				XModelObject param = WebAppHelper.findWebAppContextParam(
//						webXML, "javax.faces.DEFAULT_SUFFIX"); //$NON-NLS-1$
//				if (param != null) {
//					String value = param.getAttributeValue("param-value"); //$NON-NLS-1$
//					if (value.length() != 0 && iFile.getName().endsWith(value)) {
//						isFacelet = true;
//					}
//				}
//			}
//		}
		return isFacelet;
	}
	
}

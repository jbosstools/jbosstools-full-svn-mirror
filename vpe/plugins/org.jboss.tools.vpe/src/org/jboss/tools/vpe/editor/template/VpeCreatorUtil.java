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
package org.jboss.tools.vpe.editor.template;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.StructuredModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.FileUtil;

public class VpeCreatorUtil {
	public static final int FASET_TYPE_NONE = 0;
	public static final int FASET_TYPE_HEADER = 1;
	public static final int FASET_TYPE_FOOTER = 2;
	public static final int FASET_TYPE_BODY = 3;
	public static final int FASET_TYPE_NAVIGATION = 4;

	public static boolean isFacet(Node node) {
		if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			return "facet".equals(node.getLocalName());
		}
		return false;
	}

	public static String getFacetName(Node node) {
		if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			return ((Element)node).getAttribute("name");
		}
		return null;
	}

	public static int getFacetType(Node node) {
		String value = getFacetName(node);
		if (value != null) {
			if ("header".equals(value)) {
				return FASET_TYPE_HEADER;
			} else if ("footer".equals(value)) {
				return FASET_TYPE_FOOTER;
			} else if ("body".equals(value)) {
				return FASET_TYPE_BODY;
			} else if ("navigation".equals(value)) {
				return FASET_TYPE_NAVIGATION;
			}
		}
		return FASET_TYPE_NONE;
	}

	public static boolean isInclude(Node node) {
		if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			return node.getNodeName().indexOf("jsp:include") >=0 || node.getNodeName().indexOf("jsp:directive.include")>=0;
		}
		return false;
	}
	
	private static IDOMModel getWtpModelForRead(String fileName, VpePageContext pageContext) {
		IEditorInput input = pageContext.getEditPart().getEditorInput();
		IFile file = FileUtil.getFile(input, fileName);
		if (file != null && file.exists()) {
			try {
				return (IDOMModel)StructuredModelManager.getModelManager().getModelForRead(file);
			} catch(Exception ex) {
				VpePlugin.reportProblem(ex);
			}
		}
		return null;
	}

	public static Document getIncludeDocument(Node includeNode, VpePageContext pageContext) {
		if (isInclude(includeNode)) {
			String pageName = ((Element)includeNode).getAttribute("page");
			if (pageName == null) {
				pageName = ((Element)includeNode).getAttribute("file");
			}
			if (pageName != null) {
				IDOMModel wtpModel = wtpModel = getWtpModelForRead(pageName, pageContext);
				if (wtpModel != null) {
					return wtpModel.getDocument();												
				}
			}							
		}
		return null;
	}

	public static void releaseDocumentFromRead(Document document) {
		if (document instanceof IDOMNode) {
			IDOMModel wtpModel = ((IDOMNode)document).getModel();
			if (wtpModel != null) {
				wtpModel.releaseFromRead();
			}
		}
	}

	public static IFile getFile(String fileName, VpePageContext pageContext) {
		IEditorInput input = pageContext.getEditPart().getEditorInput();
		IFile file = null;
		if(pageContext.getVisualBuilder().getCurrentIncludeInfo()==null) {
			file = FileUtil.getFile(input, fileName);
		} else {
			IFile includedFile = 
				pageContext.getVisualBuilder().getCurrentIncludeInfo().getFile();
			file = FileUtil.getFile(fileName, includedFile);
		}
		if (file != null) { 
			if (!file.isSynchronized(0)){
				try {
					file.refreshLocal(0, null);
				} catch (Exception ex) {
					VpePlugin.getPluginLog().logError(ex);
				}
			}
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	public static Document getDocumentForRead(IFile file, VpePageContext pageContext) {
		IDOMModel wtpModel = null;
		try {
			wtpModel = (IDOMModel)StructuredModelManager.getModelManager().getModelForRead(file);
			if (wtpModel != null) return wtpModel.getDocument();
		} catch(Exception e) {
			VpePlugin.getPluginLog().logError(e);
		} finally {
			if (wtpModel != null) wtpModel.releaseFromRead();
		}
		return null;
	}

	public static int getFacetType(Node node, VpePageContext pageContext) {
		if (VpeCreatorUtil.isFacet(node)) {
			return getFacetType(node);
		} else {
			Document document = getIncludeDocument(node, pageContext);
			if (document != null) {
				try {
					NodeList list = document.getChildNodes();
					int cnt = list != null ? list.getLength() : 0;
					for (int i = 0; i < cnt; i++) {
						Node child = list.item(i);
						int type = getFacetType(child, pageContext);
						if (type != FASET_TYPE_NONE) {
							return type;
						}
					}
				} finally {
					releaseDocumentFromRead(document);
				}
			}
		}
		return FASET_TYPE_NONE;
	}
	
	public static void setAttributes(Element visualElement, Element sourceElement, VpeAttributeInfo[] attrsInfo) {
		if (attrsInfo != null) {
			for (int i = 0; i < attrsInfo.length; i++) {
				attrsInfo[i].setAttribure(visualElement, sourceElement);
			}
		}
	}
	
	public static void setAttributes(Element visualElement, VpeAttributeInfo[] attrsInfo) {
		setAttributes(visualElement, null, attrsInfo);
	}

	public static Node getTextChildNode(Node sourceElement) {
		// ignore empty text
		NodeList children = sourceElement.getChildNodes();
		for (int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().length()>0) {
				return child;
			}
		}
		return null;
	}
}

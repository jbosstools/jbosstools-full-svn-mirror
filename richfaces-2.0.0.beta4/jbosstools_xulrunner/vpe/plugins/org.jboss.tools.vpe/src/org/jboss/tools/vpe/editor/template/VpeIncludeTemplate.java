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
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeIncludeTemplate extends VpeAbstractTemplate {
	private static final String ATTR_FILE = "file";
	private VpeExpression fileNameExpression;
	
	protected void init(Element templateElement) {
		modify = false;
		Attr fileAttr = ((Element)templateElement).getAttributeNode(ATTR_FILE);
		if (fileAttr != null && fileAttr.getValue().trim().length() > 0) {
			try {
				fileNameExpression = VpeExpressionBuilder.buildCompletedExpression(fileAttr.getValue().trim(), caseSensitive).getExpression();
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
		initTemplateSections(templateElement, false, true, false, false, false);
	}

	public VpeCreationData create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument) {
		String fileName = null;
		if (fileNameExpression != null) {
			VpeValue vpeValue = fileNameExpression.exec(pageContext, sourceNode);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
				fileName = vpeValue.stringValue();
				VpeIncludeInfo info = pageContext.getVisualBuilder().getCurrentIncludeInfo();
				if(info != null) {
					IFile templateFile = info.getFile();
					IFile file = FileUtil.getFile(fileName, templateFile);
					
					if (file != null) {
						if (!pageContext.getVisualBuilder().isFileInIncludeStack(file)) {
							Document document = VpeCreatorUtil.getDocumentForRead(file, pageContext);
							if (document != null) {
								VpeCreationData creationData = createInclude(document, visualDocument);
								creationData.setData(file);
								pageContext.getVisualBuilder().pushIncludeStack(new VpeIncludeInfo((Element)sourceNode, file, document));
								return creationData;
							}
						}
					}
				}
			}
		}
		
		VpeCreationData creationData = createStub(fileName, visualDocument);
		creationData.setData(null);
		return creationData;
	}

	public void validate(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, VpeCreationData data) {
		if (data.getData() != null) {
			VpeIncludeInfo includeInfo = pageContext.getVisualBuilder().popIncludeStack();
			if (includeInfo != null) {
				VpeCreatorUtil.releaseDocumentFromRead(includeInfo.getDocument());
			}
		}
	}

	public void beforeRemove(VpePageContext pageContext, Node sourceNode, nsIDOMNode visualNode, Object data) {
		IFile file = (IFile)data;
		if (file != null) {
			pageContext.getEditPart().getController().getIncludeList().removeIncludeModel(file);
		}
	}

	public boolean isRecreateAtAttrChange(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}
	
	private VpeCreationData createInclude(Document sourceDocument, nsIDOMDocument visualDocument) {
		nsIDOMElement visualNewElement = visualDocument.createElement(HTML.TAG_DIV);
		VpeVisualDomBuilder.markIncludeElement(visualNewElement);
		VpeCreationData creationData = new VpeCreationData(visualNewElement);
		if (children) {
			VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualNewElement);
			NodeList sourceChildren = sourceDocument.getChildNodes();
			int len = sourceChildren.getLength();
			for (int i = 0; i < len; i++) {
				childrenInfo.addSourceChild(sourceChildren.item(i));
			}
			creationData.addChildrenInfo(childrenInfo);
		}
		return creationData;
	}
	
	private VpeCreationData createStub(String fileName, nsIDOMDocument visualDocument) {
		nsIDOMElement visualNewElement = visualDocument.createElement(HTML.TAG_DIV);
		visualNewElement.setAttribute("style", "background-color:#ECF3FF;cursor:pointer;padding:0 5px;margin:3px 0;font-style:italic;color:#0051DD;");
		VpeVisualDomBuilder.markIncludeElement(visualNewElement);
		if (fileName != null) {
			visualNewElement.appendChild(visualDocument.createTextNode(fileName));
		}
		return new VpeCreationData(visualNewElement);
	}

	public void openIncludeEditor(VpePageContext pageContext, Element sourceElement, Object data) {
		if (sourceElement != null && fileNameExpression != null) {
			VpeValue vpeValue = fileNameExpression.exec(pageContext, sourceElement);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
			    pageContext.openIncludeFile(vpeValue.stringValue());
			}
		}
	}

	
	
}

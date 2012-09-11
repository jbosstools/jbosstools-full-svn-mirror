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
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.jboss.tools.jst.jsp.util.NodesManagingUtil;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeAttributeOperand;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.FaceletUtil;
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
	private static final String ATTR_FILE = "file"; //$NON-NLS-1$
	private VpeExpression fileNameExpression;
	
	@Override
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
			VpeValue vpeValue;
			try {
				vpeValue = fileNameExpression.exec(pageContext, sourceNode);
			if (vpeValue != null && vpeValue.stringValue().length() > 0) {
				fileName = vpeValue.stringValue();
				VpeIncludeInfo info = pageContext.getVisualBuilder().getCurrentIncludeInfo();
				if(info != null && info.getStorage() instanceof IFile) {
					IFile templateFile = (IFile) info.getStorage();
					IFile file = FileUtil.getFile(fileName, templateFile);
					
					if (file != null) {
						if (!pageContext.getVisualBuilder().isFileInIncludeStack(file)) {
							Document document = pageContext.getVisualBuilder().getIncludeDocuments().get(file);
							if (document == null) {
								document = VpeCreatorUtil.getDocumentForRead(file);
								if (document != null) {
									pageContext.getVisualBuilder().getIncludeDocuments().put(file, document);
								}
							}
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
			} catch (VpeExpressionException e) {
				VpeExpressionException exception = new VpeExpressionException(
						sourceNode.toString(),e);
				VpePlugin.reportProblem(exception);
			}
		}
		
		VpeCreationData creationData = createStub(fileName, visualDocument);
		creationData.setData(null);
		return creationData;
	}

	@Override
	public void validate(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, VpeCreationData data) {
		if (data.getData() != null) {
			VpeIncludeInfo includeInfo = pageContext.getVisualBuilder().popIncludeStack();
			if (includeInfo != null) {
//				VpeCreatorUtil.releaseDocumentFromRead(includeInfo.getDocument());
			}
		}
	}

	@Override
	public void beforeRemove(VpePageContext pageContext, Node sourceNode, nsIDOMNode visualNode, Object data) {
		IFile file = (IFile)data;
		if (file != null) {
			pageContext.getEditPart().getController().getIncludeList().removeIncludeModel(file);
		}
	}
	
	protected VpeCreationData createInclude(Document sourceDocument, nsIDOMDocument visualDocument) {
		nsIDOMElement visualNewElement = visualDocument.createElement(HTML.TAG_DIV);
		VpeVisualDomBuilder.markIncludeElement(visualNewElement);
		VpeCreationData creationData = new VpeCreationData(visualNewElement);
		if (children) {
			VpeChildrenInfo childrenInfo = new VpeChildrenInfo(visualNewElement);
			Element root = FaceletUtil.findComponentElement(sourceDocument.getDocumentElement());
			NodeList sourceChildren=null;
			//fix for JBIDE-3482
			if(root==null) {
				sourceChildren = sourceDocument.getChildNodes();
			} else {
				sourceChildren = root.getChildNodes();
			}
			int len = sourceChildren.getLength();
			for (int i = 0; i < len; i++) {
				childrenInfo.addSourceChild(sourceChildren.item(i));
			}
			creationData.addChildrenInfo(childrenInfo);
		}
		return creationData;
	}
	
	protected VpeCreationData createStub(String fileName, nsIDOMDocument visualDocument) {
		nsIDOMElement visualNewElement = visualDocument.createElement(HTML.TAG_DIV);
		visualNewElement.setAttribute("style", "background-color:#ECF3FF;cursor:pointer;padding:0 5px;margin:3px 0;font-style:italic;color:#0051DD;"); //$NON-NLS-1$ //$NON-NLS-2$
		VpeVisualDomBuilder.markIncludeElement(visualNewElement);
		if (fileName != null) {
			visualNewElement.appendChild(visualDocument.createTextNode(fileName));
		}
		return new VpeCreationData(visualNewElement);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractTemplate#getSourceRegionForOpenOn(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode)
	 */
	/**
	 * @author mareshkau
	 */
	@Override
	public IRegion getSourceRegionForOpenOn(VpePageContext pageContext,
			Node sourceNode, nsIDOMNode domNode) {

		if (sourceNode != null && this.fileNameExpression != null && this.fileNameExpression instanceof VpeAttributeOperand) {
			Element sourceElement = (Element) sourceNode;
			Node paramAttr = sourceElement.getAttributeNode(((VpeAttributeOperand)this.fileNameExpression).getAttributeName());
			return new Region(NodesManagingUtil.getStartOffsetNode(paramAttr),0);			
		}
		return null;
	}

//	@Override
//	public void openIncludeEditor(VpePageContext pageContext, Element sourceElement, Object data) {
//		if (sourceElement != null && fileNameExpression != null) {
//			VpeValue vpeValue;
//			try {
//				vpeValue = fileNameExpression.exec(pageContext, sourceElement);
//				if (vpeValue != null && vpeValue.stringValue().length() > 0) {
//				    pageContext.openIncludeFile(vpeValue.stringValue());
//				}
//			} catch (VpeExpressionException e) {
//					
//					VpePlugin.reportProblem(e);
//			}
//		}
//	}

	
	
}

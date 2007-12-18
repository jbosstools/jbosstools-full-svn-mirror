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
package org.jboss.tools.vpe.editor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionHelper;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeSourceDomBuilder extends VpeDomBuilder {
	private StructuredTextViewer structuredTextViewer;
	private IContentOutlinePage outline;
	private ISelectionProvider selectionManager;
	IDOMModel model;
	private Document sourceDocument;
	private VpePageContext pageContext;
	private StructuredTextEditor sourceEditor;
	
	public VpeSourceDomBuilder(VpeDomMapping domMapping, INodeAdapter sorceAdapter, StructuredTextEditor sourceEditor, VpePageContext pageContext) {
		super(domMapping, sorceAdapter);
		this.sourceEditor = sourceEditor;
		structuredTextViewer = sourceEditor.getTextViewer();
		outline = (IContentOutlinePage)sourceEditor.getAdapter(IContentOutlinePage.class);
//		selectionManager = sourceEditor.getViewerSelectionManager();
		selectionManager = sourceEditor.getSelectionProvider();
		model = (IDOMModel)sourceEditor.getModel();
		if (model != null) {
			sourceDocument = model.getDocument();
		}
		this.pageContext = pageContext;
	}

	public void addNode(nsIDOMNode visualNode) {
		nsIDOMNode visualContainer = visualNode.getParentNode();
		Node sourceContainer = domMapping.getSourceNode(visualContainer);

		if (sourceContainer != null && (sourceContainer.getNodeType() == Node.ELEMENT_NODE || sourceContainer.getNodeType() == Node.DOCUMENT_NODE)) {
			nsIDOMNode visualNextNode = visualNode.getNextSibling();
			Node sourceNextNode = domMapping.getSourceNode(visualNextNode);
			addNode(visualNode, sourceNextNode, sourceContainer);
		}
	}

	public void removeNode(nsIDOMNode visualNode) {
		Node sourceNode = domMapping.getSourceNode(visualNode);
		if (sourceNode != null) {
			Node sourceContainer = sourceNode.getParentNode();
			if (sourceContainer != null) {
				sourceContainer.removeChild(sourceNode);
			}
		} else {
			sourceNode = domMapping.getNearSourceNode(visualNode);
			if (sourceNode != null) {
				if (sourceNode.getNodeType() == Node.COMMENT_NODE) {
					sourceNode.setNodeValue("");
				}
			}
		}
	}
	
	public void setText(nsIDOMNode visualText) {
		Node sourceText = domMapping.getSourceNode(visualText);
		if (sourceText != null) {
			sourceText.setNodeValue(TextUtil.sourceText(visualText.getNodeValue()));
		} else {
			nsIDOMNode visualParent = visualText.getParentNode();
			if (visualParent != null) {
				Node sourceParent = domMapping.getNearSourceNode(visualText);
				if (sourceParent != null) {
					if (sourceParent.getNodeType() == Node.ELEMENT_NODE) {
						VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceParent);
						if (elementMapping != null) {
							VpeTemplate template = elementMapping.getTemplate();
							template.setSourceAttributeValue(pageContext, (Element)sourceParent, elementMapping.getData());
						}
					} else if (sourceParent.getNodeType() == Node.COMMENT_NODE) {
						setComment(sourceParent, visualParent);
//						VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceParent);
//						if (elementMapping != null) {
//							VpeTemplate template = elementMapping.getTemplate();
//							template.setSourceCommentValue(pageContext, (Comment)sourceParent, elementMapping.getData());
//						}
					}
				}
			}
		}
	}
	
	public void setSelectedRange(Node sourceNode, int start, int length) {
//glory
		structuredTextViewer.setSelectedRange(start, length);
		structuredTextViewer.revealRange(start, length);
//		ISelection selection = new StructuredSelection(sourceNode);
//		SelectionChangedEvent event = new SelectionChangedEvent(outline, selection);
//		selectionManager.selectionChanged(event);
//		selectionManager.setSelection(new TextSelection(start, length));
	}
	
	public void setSelectChanged(Node sourceNode) {
//		structuredTextViewer.setSelectedRange(start, length);
//		structuredTextViewer.revealRange(start, length);
		
//glory
//		ISelection selection = new StructuredSelection(sourceNode);
//		SelectionChangedEvent event = new SelectionChangedEvent(outline, selection);
//		selectionManager.selectionChanged(event);
		if(sourceNode instanceof IDOMNode) {
			IDOMNode n = (IDOMNode)sourceNode;
			int start = n.getStartOffset();
			int length = n.getLength();
			selectionManager.setSelection(new TextSelection(start, length));
		}
	}
	
	void setSelectionAtDocumentEnd() {
		if (sourceDocument == null) return;
		int offset = ((IndexedRegion)sourceDocument).getEndOffset();
		structuredTextViewer.setSelectedRange(offset, 0);
		structuredTextViewer.revealRange(offset, 0);
	}
	
	public void setSelection(Node sourceNode, int offset, int length) {
		setSelection(sourceNode, offset, length, false);
	}
	
	void setSelection(Node sourceNode, int offset, int length, boolean innerFlag) {
		if (VpeDebug.PRINT_VISUAL_MOUSE_EVENT) {
			System.out.println("VpeSourceDomBuilder.setSelection: offset " + offset + 
					" length " + length + " innerFlag " + innerFlag);
		}
		if (sourceNode != null) {
			int start = ((IndexedRegion)sourceNode).getStartOffset() + offset;
			if (innerFlag && offset == 0 && sourceNode instanceof ElementImpl) {
		 		ElementImpl element = (ElementImpl)sourceNode;
		 		if (element.isContainer()) {
		 			start  = element.getStartEndOffset();
		 			length = 0;
		 		}
			} else if (sourceNode.getNodeType() == Node.COMMENT_NODE) {
				start += 4;
			}
//glory
//			ISelection selection = new StructuredSelection(sourceNode);
//			SelectionChangedEvent event = new SelectionChangedEvent(outline, selection);
//			selectionManager.selectionChanged(event);
			structuredTextViewer.setSelectedRange(start, length);
			structuredTextViewer.revealRange(start, length);
//			selectionManager.setSelection(new TextSelection(start, length));
		}
	}
	
	Node getSelectedNode() {
		List nodes = VpeSelectionHelper.getTextWidgetSelectedNodes(model, selectionManager);
			//selectionManager.getSelectedNodes();
		if (nodes != null && nodes.size() > 0) {
			return (Node)nodes.get(0);
		} else {
			return null;
		}
	}
	
	int getCaretPosition() {
//		return selectionManager.getCaretPosition();
		ITextViewer v = (sourceEditor == null) ? null : sourceEditor.getTextViewer();
		StyledText t = (v == null) ? null : v.getTextWidget(); 
		return (t == null) ? 0 : t.getCaretOffset();
	}
	
	private void addNode(nsIDOMNode visualNewNode, Node sourceNextNode, Node sourceContainer) {
		Node sourceNewNode = createNode(visualNewNode);
		if (sourceNewNode != null) {
			if (sourceNextNode == null) {
				sourceContainer.appendChild(sourceNewNode);
			} else {
				sourceContainer.insertBefore(sourceNewNode, sourceNextNode);  
			}
		}
	}
	
	private Node createNode(nsIDOMNode visualNewNode) {
		if (sourceDocument != null) {
			switch (visualNewNode.getNodeType()) {
			case Node.ELEMENT_NODE:
				Element sourceNewElement = sourceDocument.createElement(visualNewNode.getNodeName());
				Set ifDependencySet = new HashSet();
				//VpeVisualDomBuilder visualBuildet = 
					pageContext.getVisualBuilder();
				VpeTemplateManager templateManager = VpeTemplateManager.getInstance();
				VpeTemplate template = templateManager.getTemplate(pageContext, sourceNewElement, ifDependencySet);
				registerNodes(new VpeElementMapping(sourceNewElement, (nsIDOMElement)visualNewNode, null, template, ifDependencySet, null));
				addChildren(visualNewNode, sourceNewElement);
				return sourceNewElement;
			case Node.TEXT_NODE:
				Node sourceTextNode = sourceDocument.createTextNode(visualNewNode.getNodeValue());
				registerNodes(new VpeNodeMapping(sourceTextNode, visualNewNode));
				return sourceTextNode;
			}
		}
		return null;
	}

	private void addChildren(nsIDOMNode visualContainer, Element sourceContainer) {
		nsIDOMNodeList visualNodes = visualContainer.getChildNodes();
		long len = visualNodes.getLength();

		for (long i = 0; i < len; i++) {
			nsIDOMNode visualNode = visualNodes.item(i);
			addNode(visualNode, null, sourceContainer);
		}
	}
	
	boolean openBundleEditors(nsIDOMNode visualNode) {
		Node sourceNode = domMapping.getNearSourceNode(visualNode);
		if (sourceNode != null && sourceNode.getNodeType() == Node.ELEMENT_NODE) {
			VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceNode);
			if (elementMapping != null) {
				VpeTemplate template = elementMapping.getTemplate();
				template.openBundleEditors(pageContext, (Element)sourceNode, elementMapping.getData());
			}
		}
		return false;
	}
	
	boolean openIncludeEditor(nsIDOMNode visualNode) {
		Node sourceNode = domMapping.getNearSourceNode(visualNode);
		if (sourceNode != null && sourceNode.getNodeType() == Node.ELEMENT_NODE) {
			VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceNode);
			if (elementMapping != null) {
				VpeTemplate template = elementMapping.getTemplate();
				template.openIncludeEditor(pageContext, (Element)sourceNode, elementMapping.getData());
			}
		}
		return false;
	}
	
	boolean isEmptyDocument() {
		if (sourceDocument == null) return true;
		boolean empty = false;
		NodeList sourceNodes = sourceDocument.getChildNodes();
		int len = sourceNodes.getLength();
		if (len == 0) {
			empty = true;
		} else if (len == 1) {
			Node sourceNode = sourceNodes.item(0);
			if (sourceNode.getNodeType() == Node.TEXT_NODE && sourceNode.getNodeValue().trim().length() == 0) {
				empty = true;
			}
		}
		return empty;
	}
	
	int getPosition(Node sourceNode, int offset, boolean innerFlag) {
		int start = 0;
		if (sourceNode != null) {
			start = ((IndexedRegion)sourceNode).getStartOffset() + offset;
			if (innerFlag && offset == 0 && sourceNode instanceof ElementImpl) {
		 		ElementImpl element = (ElementImpl)sourceNode;
		 		if (element.isContainer()) {
		 			start  = element.getStartEndOffset();
		 		}
			}
		}
		return start;
	}
	
	void setAttributeSelection(nsIDOMNode visualText, int offset, int length) {
		nsIDOMNode visualParent = visualText.getParentNode();
		if (visualParent != null) {
			Node sourceParent = domMapping.getNearSourceNode(visualText);
			if (sourceParent != null) {
				if (sourceParent.getNodeType() == Node.ELEMENT_NODE) {
					VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceParent);
					if (elementMapping != null) {
						VpeTemplate template = elementMapping.getTemplate();
						template.setSourceAttributeSelection(pageContext, (Element)sourceParent, offset, length, elementMapping.getData());
					}
				} else if (sourceParent.getNodeType() == Node.COMMENT_NODE) {
//					VpeVisualElementInfo info = domMapping.getVisualElementInfo(sourceParent);
//					if (info != null) {
//						info.setSourceCommentValue(pageContext, (Comment)sourceParent);
//					}
					//Added by Max Areshkau in scope of bug JBIDE-1209
				} else if (sourceParent.getNodeType()==Node.TEXT_NODE) {
					
					setSelection(sourceParent, 1, 0);
				}
			}
		}
	}
	
//	boolean 
	
	public void setAttributeSelection(Attr sourceAttr, int offset, int length) {
		IDOMAttr xmlAttr = (IDOMAttr)sourceAttr;
		int start = xmlAttr.getValueRegionStartOffset() + offset;
		String value = xmlAttr.getValueRegionText();
		if (value.startsWith("\"") || value.startsWith("\'")) {
			start++;
		}
		int end = xmlAttr.getEndOffset();
		if (start > end) {
			start = end;
		}
		if (start + length > end) {
			length = end - start;
		}
		
//glory
//		ISelection selection = new StructuredSelection(sourceAttr);
//		SelectionChangedEvent event = new SelectionChangedEvent(outline, selection);
//		selectionManager.selectionChanged(event);
		structuredTextViewer.setSelectedRange(start, length);
		structuredTextViewer.revealRange(start, length);
		
//		selectionManager.setSelection(new TextSelection(start, length));
	}
	
	Point getOutputAttributesPositions(Element sourceElement) {
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate();
			if (template.isOutputAttributes()) {
				int start = ((IndexedRegion)sourceElement).getStartOffset();
				int end = ((IndexedRegion)sourceElement).getEndOffset();
				return new Point(start, end);
			}
		}
		return null;
	}

	Point getSelectionRange() {
		return sourceEditor.getTextViewer().getSelectedRange();
	}
	
	public void setComment(Node sourceComment, nsIDOMNode visualElement) {
		nsIDOMNodeList visualNodes = visualElement.getChildNodes();
		long len = visualNodes.getLength();
		
		if (len > 0) {
			nsIDOMNode visualText = visualNodes.item(0); 
			sourceComment.setNodeValue(visualText.getNodeValue());
		}
	}

	public StructuredTextViewer getStructuredTextViewer() {
		return structuredTextViewer;
	}
	
	Document getSourceDocument() {
		return sourceDocument;
	}
}

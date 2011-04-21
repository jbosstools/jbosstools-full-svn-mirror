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
package org.jboss.tools.vpe.editor.toolbar.format.handler;

import java.util.Properties;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.textformating.FormatData;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.toolbar.format.IFormatController;

/**
 * @author Igels
 */
abstract public class FormatHandler implements IFormatHandler {

	protected FormatControllerManager manager;
	protected FormatData formatData;
	protected IFormatController controller;

	public FormatHandler() {
		super();
	}

	public FormatHandler(FormatControllerManager manager) {
		this();
		this.manager = manager;
	}

	/**
	 * @param manager The FormatControllerManager to set.
	 */
	void setManager(FormatControllerManager manager) {
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler#run()
	 */
	public void run(FormatData formatData) {
		this.formatData = formatData;
		run();
	}

	abstract protected void run();

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler#setFormatController(org.jboss.tools.vpe.editor.toolbar.format.IFormatController)
	 */
	public void setFormatController(IFormatController controller) {
		this.controller = controller;
	}

	protected void insertNewElementAroundNode(String elementName, Node node, StructuredTextViewer viewer, boolean format) {
		IDocument document = viewer.getDocument();
		IndexedRegion region = (IndexedRegion)node;

		int startOffcet = region.getStartOffset();
		int length = region.getEndOffset() - startOffcet;

		String nodeSource = getNodeSource(node);

		if(!(node instanceof Element)) {
			// This node is text
			TextSelection sel = (TextSelection)viewer.getSelectionProvider().getSelection();
			if(sel.getLength()>0) {
				String value = node.getNodeValue();
				String trimValue = value.trim();
				if(trimValue.equals(sel.getText())) {
					startOffcet = startOffcet + value.indexOf(trimValue);
					length = trimValue.length();
				} else {
					startOffcet = sel.getOffset();
					length = sel.getLength();
				}
			} else {
				String value = node.getNodeValue();
				String trimValue = value.trim();
				startOffcet = startOffcet + value.indexOf(trimValue);
				length = trimValue.length();
			}
			nodeSource = nodeSource.trim();
		}

		Properties p = new Properties();
		p.setProperty(PaletteInsertHelper.PROPOPERTY_TAG_NAME, elementName);
		p.setProperty(PaletteInsertHelper.PROPOPERTY_START_TEXT, "<" + elementName + ">"); //$NON-NLS-1$ //$NON-NLS-2$
		p.setProperty(PaletteInsertHelper.PROPOPERTY_END_TEXT, "</" + elementName + ">"); //$NON-NLS-1$ //$NON-NLS-2$
		if(format) {
			p.setProperty(PaletteInsertHelper.PROPOPERTY_REFORMAT_BODY, "yes"); //$NON-NLS-1$
		}
		p.setProperty(PaletteInsertHelper.PROPOPERTY_TAGLIBRARY_URI, "http://www.w3.org/TR/REC-html40"); //$NON-NLS-1$
		p.setProperty(PaletteInsertHelper.PROPOPERTY_DEFAULT_PREFIX, ""); //$NON-NLS-1$

		ITextSelection sel = new TextSelection(startOffcet, length);
		ISelectionProvider selProvider = viewer.getSelectionProvider();
		selProvider.setSelection(sel);

		p.put("selectionProvider", selProvider); //$NON-NLS-1$

		PaletteInsertHelper.insertIntoEditor(viewer, p);

		String documentText = document.get();
		int newStartOffset = documentText.substring(startOffcet).indexOf(nodeSource);
		if(newStartOffset>-1) {
			newStartOffset = startOffcet + newStartOffset;
			viewer.setSelectedRange(newStartOffset, 0);
			viewer.revealRange(newStartOffset, 0);
		}
	}

	protected String getNodeBody(Node node) {
		StringBuffer buffer = new StringBuffer();
		NodeList list = node.getChildNodes();
		for(int i=0; i<list.getLength(); i++) {
			Node child = list.item(i);
			if(child instanceof ElementImpl) {
				ElementImpl element = (ElementImpl)child;
				buffer.append(element.getSource());
			} else {
				buffer.append(child.getNodeValue());
			}
		}
		return buffer.toString();
	}

	protected String getNodeSource(Node node) {
		if(node instanceof ElementImpl) {
			return ((ElementImpl)node).getSource();
		} else {
			return node.getNodeValue();
		}
	}

	protected String stripElement(ElementImpl element, Node selectedNode, StructuredTextViewer viewer) {
		IDocument document = viewer.getDocument();

		String body = getNodeBody(element);
		int length = element.getEndOffset() - element.getStartOffset();
		String selectedSource = getNodeSource(selectedNode);
		int newStartOffset = element.getStartOffset();
		if(element!=selectedNode) {
			newStartOffset = newStartOffset + body.indexOf(selectedSource);
		}
		if(body.startsWith("\r\n") || body.startsWith("\n")) { //$NON-NLS-1$ //$NON-NLS-2$
			//most probably, in this case white spaces were used 
			//for formatting inside parent tag, and this formatting must be removed 
			body = body.trim();
		}
		try {
			document.replace(element.getStartOffset(), length, body);
		} catch (BadLocationException e) {
			VpePlugin.getPluginLog().logError("Can't format text", e); //$NON-NLS-1$
		}

		viewer.setSelectedRange(newStartOffset, 0);
		viewer.revealRange(newStartOffset, 0);
		return body;
	}

	protected String replaseElementName(ElementImpl element, String newName, StructuredTextViewer viewer, Node selectedNode) {
		IDocument document = viewer.getDocument();

		// Append start part - "<tag";
		StringBuffer resultNode = new StringBuffer("<").append(newName); //$NON-NLS-1$

		int endOffcet = element.getEndOffset() - element.getStartOffset();
		int startEndOffcet = element.getStartEndOffset() - element.getStartOffset();
		int endStartOffcet = element.getEndStartOffset() - element.getStartOffset();
		int length = endOffcet;

		String source = element.getSource();
		String name = element.getNodeName();
		int indexOfStartAttributes = name.length() + 1;

		// Append middle part - " a1='v1' a2='v2'> body ";
		resultNode.append(source.substring(indexOfStartAttributes, endStartOffcet));
		if(endOffcet!=startEndOffcet) {
			String endtagName = element.getEndTagName();
			if(endtagName!=null) {
				// Append - "</tag";
				resultNode.append("</").append(newName); //$NON-NLS-1$
				// Append - ">";
				resultNode.append(source.substring(endStartOffcet + 2 + endtagName.length()));
			}
		}

		String newElement = resultNode.toString();
		int newStartOffset = element.getStartOffset();
		if(element!=selectedNode) {
			newStartOffset = newStartOffset + newElement.indexOf(getNodeSource(selectedNode));
		}
		try {
			document.replace(element.getStartOffset(), length, newElement);
		} catch (BadLocationException e) {
			VpePlugin.getPluginLog().logError("Can't format text", e); //$NON-NLS-1$
		}

		viewer.setSelectedRange(newStartOffset, 0);
		viewer.revealRange(newStartOffset, 0);

		return newElement;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler#formatIsAllowable()
	 */
	public boolean formatIsAllowable() {
		TextFormatingData data = manager.getFormatTemplateForSelectedNode();
		if(data!=null) {
			Node node = manager.getCurrentSelectedNodeInfo().getNode();

			TextFormatingData formatingData = manager.getFormatTemplateForTag(node);
			if(formatingData!=null) {
				FormatData[] formatDatas = formatingData.getFormatDatas(controller.getType());
				FormatData formatData = null;
				for(int i=0; i<formatDatas.length; i++) {
					if(this.getClass().getName().equals(formatDatas[i].getHandler())){
						formatData = formatDatas[i];
						break;
					}
				}
			    if(formatData!=null) {
			    	if(formatData.isAddingParentAllow()) {
			    		return true;
			    	} else if(formatData.isAddingParentDeny()) {
			    		return false;
			    	}
			    }
			}

			Node parentNode = getParentNodeWhichDenyAddChild(node);
			if(parentNode!=null) {
				FormatData[] formatDatas = manager.getFormatTemplateForTag(parentNode).getFormatDatas(controller.getType());
				for(int i=0; i<formatDatas.length; i++) {
					FormatData formatData = formatDatas[i];
					if(formatData.isAddingChildrenByItself()) {
						IAddNodeHandler handler = manager.getHandlerFactory().createAddNodeHandler(formatData);
						if(handler!=null) {
							return true;
						}
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	protected Node getParentNodeWhichDenyAddChild(Node childNode) {
		Node parentNode = childNode.getParentNode();
		if(parentNode == null) {
			return null;
		}
		TextFormatingData data = manager.getFormatTemplateForTag(parentNode);
		boolean returnParent = false;
		if(data!=null) {
			FormatData[] formatDatas = data.getFormatDatas(controller.getType());
			for(int i=0; i<formatDatas.length; i++) {
				FormatData formatData = formatDatas[i];
				if(formatData.isAddingChildrenAllow() || formatData.isAddingParentAllow()) {
					return null;
				}
				if(formatData.isAddingChildrenByItself() || formatData.isAddingChildrenDeny() || formatData.isAddingParentByItself() || formatData.isAddingParentDeny()) {
					returnParent = true;
				}
			}
		}
		if(returnParent) {
			return parentNode;
		}
		return getParentNodeWhichDenyAddChild(parentNode);
	}
}
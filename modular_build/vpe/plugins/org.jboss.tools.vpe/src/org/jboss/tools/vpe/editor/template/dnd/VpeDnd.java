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
package org.jboss.tools.vpe.editor.template.dnd;

import java.util.HashSet;

import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDropInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class VpeDnd {
	static final String TAG_DRAG            = VpeTemplateManager.VPE_PREFIX + "drag"; //$NON-NLS-1$
	static final String TAG_DROP            = VpeTemplateManager.VPE_PREFIX + "drop"; //$NON-NLS-1$
	static final String TAG_CONTAINER_CHILD = VpeTemplateManager.VPE_PREFIX + "container-child"; //$NON-NLS-1$
	
	static final String ATTRIBUTE_START_ENABLE  = "start-enable"; //$NON-NLS-1$
	static final String ATTRIBUTE_CONTAINER     = "container"; //$NON-NLS-1$
	static final String ATTRIBUTE_TAG_NAME      = "tag-name"; //$NON-NLS-1$
	
	static final String STRING_YES              = "yes"; //$NON-NLS-1$
	
	private boolean dragEnabled = false;
	private boolean isContainer = false;
	private HashSet enabledTags = null;
	
	
	
	public VpeDnd(){
		
	}
	
	public void setDndData(boolean dragEnabled, boolean isContainer){
		this.dragEnabled = dragEnabled;
		this.isContainer = isContainer;
		enabledTags = null;
	}
	
	public void setDndData(Element node){
		enabledTags = null;
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			boolean dragFlag = false;
			boolean dropFlag = false;
			for (int i = 0; i < len; i++) {
				Node innerNode = children.item(i);
				if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
					String name = innerNode.getNodeName();
					if (name.startsWith(VpeTemplateManager.VPE_PREFIX)) {
						if (!dragFlag && TAG_DRAG.equals(name)) {
							if(STRING_YES.equalsIgnoreCase(((Element)innerNode).getAttribute(ATTRIBUTE_START_ENABLE))) dragEnabled = true;
							else dragEnabled = false;
							dragFlag = true;
						}
						if (!dropFlag && TAG_DROP.equals(name)){
							if(STRING_YES.equalsIgnoreCase(((Element)innerNode).getAttribute(ATTRIBUTE_CONTAINER))) isContainer = true;
							else isContainer = false;

							dropFlag = true;
							NodeList list = innerNode.getChildNodes();
							if (list != null) {
								int l = list.getLength();
								if(l > 0)enabledTags = new HashSet();
								for (int j = 0; j < l; j++) {
									Node child = list.item(j);
									if (child.getNodeType() == Node.ELEMENT_NODE) {
										String tagName = child.getNodeName();
										if (tagName.startsWith(VpeTemplateManager.VPE_PREFIX)) {
											if (TAG_CONTAINER_CHILD.equals(tagName)) {
												enabledTags.add(((Element)child).getAttribute(ATTRIBUTE_TAG_NAME).toLowerCase());
											}
										}
									}
								}
							}
						}
						if(dragFlag && dropFlag)break;
					}
				}
			}
		}
	}
	
	public boolean isDragEnabled(){
		return dragEnabled;
	}
	
	public boolean isDropEnabled(VpePageContext pageContext, Node container, Node node){
		if(!pageContext.isAbsolutePosition() && isAncestor(container, node)) return false;
		if(isContainer){
			if(enabledTags != null && enabledTags.size() > 0){
				String name = node.getNodeType() == Node.ELEMENT_NODE ? node.getLocalName().toLowerCase() : node.getNodeName();
				if(enabledTags.contains(name)) return true;
				else return false;
			}else return true;
		}
		return false;
	}
	
	private boolean isAncestor(Node container, Node node){
		Node curent = container;
		while(curent != null){
			if(curent.equals(node))return true;
			curent = curent.getParentNode();
		}
		return false;
	}
	
	private void removeTextFromTextNode(Node node, int start, int end){
		Node parent = node.getParentNode();
		boolean split1=true, split2=true;
		String text = node.getNodeValue();
		if(start == 0)split1 = false;
		if(end == (text.length()+1))split2 = false;
		if(split1 && split2){
			String text1 = text.substring(0,start);
			String text2 = text.substring(end, text.length());
			
			node.setNodeValue(text1+text2);
		}else if(split1){
			String text1 = text.substring(0,start);
			
			node.setNodeValue(text1);
		}else if(split2){
			String text1 = text.substring(end, text.length());
			
			node.setNodeValue(text1);
		}else{
			parent.removeChild(node);
		}	
	}
	
	private void insertTextIntoTextNode(Node node, String text, int offset) {
		String oldText = node.getNodeValue();
		if(oldText == null) return;
		String newText = oldText.substring(0, offset)+text+oldText.substring(offset, oldText.length());
		node.setNodeValue(newText);
	}
	
	private void replaceTextInTextNode(Node node, int offset, int selStart, int selEnd) {
		if (offset >= selStart && offset <= selEnd) {
			return;
		}
		String oldText = node.getNodeValue();
		if (oldText == null) {
			return;
		}
		String selText = oldText.substring(selStart, selEnd);
		String newText = null;
		if (offset < selStart) {
			newText = oldText.substring(0, offset) + selText + oldText.substring(offset, selStart) + oldText.substring(selEnd);
		} else {
			newText = oldText.substring(0, selStart) + oldText.substring(selEnd, offset) + selText + oldText.substring(offset);
		}
		node.setNodeValue(newText);
	}
	
	private void insertAnyTextIntoAnyText(Node container, int offset, Node node, int start, int end) {
		String nodeText = node.getNodeValue();
		String draggedText = nodeText.substring(start, end);

		if (node == container) {
			replaceTextInTextNode(container, offset, start, end);
		} else {
			removeTextFromTextNode(node, start, end);
			insertTextIntoTextNode(container, draggedText, offset);
		}
	}
	
	public void drop(VpePageContext pageContext, VpeSourceInnerDragInfo dragInfo, VpeSourceInnerDropInfo dropInfo){
		Node container = dropInfo.getContainer();
		int offset = dropInfo.getOffset();
		Node node = dragInfo.getNode();

		switch (node.getNodeType()) {
		case Node.TEXT_NODE:
		case Node.ATTRIBUTE_NODE:
			String nodeText = node.getNodeValue();
			int beginPosition = Math.min(dragInfo.getOffset(), nodeText.length());
			int endPosition = Math.min(dragInfo.getOffset() + dragInfo.getLength(), nodeText.length());

			switch (container.getNodeType()) {
			case Node.TEXT_NODE:
			case Node.ATTRIBUTE_NODE:
				insertAnyTextIntoAnyText(container, offset, node, beginPosition, endPosition);
				break;

			case Node.ELEMENT_NODE:
			case Node.DOCUMENT_NODE:
				NodeList children = container.getChildNodes();
				int count = children.getLength();
				Node child = null;
				boolean insertBefore = true;
				if (offset < count) {
					if (offset > 0) {
						child = children.item(offset - 1);
						insertBefore = false;
						if (child.getNodeType() != Node.TEXT_NODE) {
							child = children.item(offset);
							insertBefore = true;
						}
					} else {
						child = children.item(offset);
					}
				} else if (count > 0) {
					child = children.item(count - 1);
					insertBefore = false;
				}
				if (child != null && child.getNodeType() == Node.TEXT_NODE) {
					if (insertBefore) {
						insertAnyTextIntoAnyText(child, 0, node, beginPosition, endPosition);
					} else {
						insertAnyTextIntoAnyText(child, child.getNodeValue().length(), node, beginPosition, endPosition);
					}
				} else {
					removeTextFromTextNode(node, beginPosition, endPosition);
					Document document = container.getNodeType()== Node.DOCUMENT_NODE ? (Document) container : container.getOwnerDocument();
					Node textNode = document.createTextNode(nodeText.substring(beginPosition, endPosition));
					if (offset < count) {
						container.insertBefore(textNode, child);
					} else {
						container.appendChild(textNode);
					}
				}
				break;
			}
			break;

		case Node.ELEMENT_NODE:
			if (!isAncestor(container, node)) {
				Node parent = node.getParentNode();

				switch (container.getNodeType()) {
				case Node.TEXT_NODE:
					Text text = (Text)container;
					String str1 = text.getNodeValue().substring(0,offset);
					String str2 = text.getNodeValue().substring(offset,text.getNodeValue().length());
					//text.setNodeValue(str1);
					//Text newText = text.splitText(offset);
					Text newText = text.getOwnerDocument().createTextNode(str2);
					text.getParentNode().insertBefore(newText, text.getNextSibling());
					parent.removeChild(node);
					text.getParentNode().insertBefore(node, newText);
					text.getParentNode().removeChild(text);
					Text oldText = text.getOwnerDocument().createTextNode(str1);
					node.getParentNode().insertBefore(oldText, node);
					break;

				case Node.ELEMENT_NODE:
				case Node.DOCUMENT_NODE:
					NodeList children = container.getChildNodes();
					int count = children.getLength();
					Node child = null;
					if (offset < count) {
						child = children.item(offset);
					} else if (count > 0) {
						child = children.item(count - 1);
					}
					if (node != child) {
						parent.removeChild(node);
						if (offset < count) {
							container.insertBefore(node, child);
						} else {
							container.appendChild(node);
						}
					}
					break;
				}
			}
			break;
		}
	}
}



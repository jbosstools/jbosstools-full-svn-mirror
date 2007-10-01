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

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.actions.StructuredTextEditorActionConstants;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectedNodeInfo;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelectionBuilder;
import org.jboss.tools.vpe.editor.template.VpeHtmlTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.util.FlatIterator;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMKeyEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIFrameSelection;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Class created for process key events in VPE editor
 * @author Max Areshkau
 *
 */
public class VpeVisualKeyHandler {
    
	public static final int VK_ENTER			= 0x0D;
	public static final int VK_LEFT				= 0x25;
	public static final int VK_UP				= 0x26;
	public static final int VK_RIGHT			= 0x27;
	public static final int VK_DOWN				= 0x28;
	public static final int VK_BACK_SPACE		= 0x08;
	public static final int VK_DELETE			= 0x2E;
	public static final int VK_INSERT 			= 0x2D;
	public static final int VK_F4 				= 115;
	public static final int VK_PAGE_UP 			= 0x21;
	public static final int VK_HOME 			= 0x24;
	public static final int VK_END 				= 0x23;

    StructuredTextEditor sourceEditor;
	VpeDomMapping domMapping;
	VpePageContext pageContext;
	nsIFrameSelection frameSelection;
	
	VpeVisualKeyHandler(StructuredTextEditor sourceEditor, VpeDomMapping domMapping, VpePageContext pageContext, nsIFrameSelection frameSelection) {
		this.sourceEditor = sourceEditor;
		this.domMapping = domMapping;
		this.pageContext = pageContext;
		this.frameSelection = frameSelection;
	}
	
	boolean keyPressHandler(nsIDOMKeyEvent keyEvent) {
		boolean handled = false;
		if (keyEvent.isCtrlKey() || keyEvent.isMetaKey()) {
			handled = ctrlKeyPressHandler(keyEvent);
		} else {
			handled = nonctrlKeyPressHandler(keyEvent);
		}
		if (VpeDebug.printVisualKeyEvent) {
			System.out.println("    handled: " + handled);
		}
		if (handled) {
			keyEvent.stopPropagation();
			keyEvent.preventDefault();
		}
		return handled;
	}

	private boolean ctrlKeyPressHandler(nsIDOMKeyEvent keyEvent) {
		switch (keyEvent.getCharCode()) {
		case 0:
			switch (keyEvent.getKeyCode()) {
			case VK_F4:
				IWorkbenchPage workbenchPage = VpePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if(keyEvent.isShiftKey()) workbenchPage.closeAllEditors(true);
				else workbenchPage.closeEditor(pageContext.getEditPart().getParentEditor(),true);
				break;
			case VK_HOME:
				if (keyEvent.isShiftKey()) {
					
					return selectToBegin();
				}
				break;
			case VK_END:
				if (keyEvent.isShiftKey()) {
					
					return selectToEnd();
				}
				break;
			}
			break;
		case 'z':
		case 'Z':
			sourceEditor.getAction(ActionFactory.UNDO.getId()).run();
			return true;
		case 'y':
		case 'Y':
			sourceEditor.getAction(ActionFactory.REDO.getId()).run();
			return true;
		case 's':
		case 'S':
			//sourceEditor.getAction(ActionFactory.SAVE.getId()).run();
			doSave(new NullProgressMonitor());
			//IWorkbenchPage page = sourceEditor.getSite().getPage();
			//page.saveEditor(sourceEditor, false);
			return true;
		case 'x':
		case 'X':
			sourceEditor.getAction(ActionFactory.CUT.getId()).run();
			return true;
		case 'c':
		case 'C':
			sourceEditor.getAction(ActionFactory.COPY.getId()).run();
			return true;
		case 'v':
		case 'V':
			sourceEditor.getAction(ActionFactory.PASTE.getId()).run();
			return true;
		}
		return false;
	}
	
	public void doSave(IProgressMonitor monitor){
		sourceEditor.doSave(monitor);
	}

	private boolean nonctrlKeyPressHandler(nsIDOMKeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();
		boolean shiftKey = keyEvent.isShiftKey();
		
		if (keyCode == VK_ENTER) {
			return split();
		} else if (keyCode == VK_LEFT && !shiftKey) {
			return moveLeft();
		} else if (keyCode == VK_UP && !shiftKey) {
			return moveUp(false);
		} else if (keyCode == VK_RIGHT && !shiftKey) {
			return moveRight();
		} else if (keyCode == VK_DOWN && !shiftKey) {
			return moveDown(false);
		} else if (keyCode == VK_HOME && !shiftKey) {
			return moveHome(false);
		} else if (keyCode == VK_END && !shiftKey) {
			return moveEnd(false);
		} else if (keyCode == VK_BACK_SPACE && !shiftKey) {
			return deleteLeft(); //
		} else if (keyCode == VK_DELETE && !shiftKey) {
			return deleteRight(); // 
		} else if (keyCode == VK_PAGE_UP && shiftKey) {
			return false;
		} else if (keyEvent.getCharCode() != 0) { //
			return handleKey(keyEvent);
		} else if (shiftKey && keyEvent.getKeyCode() == VK_INSERT) {
			return paste();
		}
		return false;
	}

	private boolean selectToBegin() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			Node focusNode = selection.getFocusNode();
			int focusOffset = selection.getFocusOffset();
			if (focusNode != null) {
				int so = ((IndexedRegion)focusNode).getStartOffset() + focusOffset;
				setSelectionRange(so, 0);
				return true;
			}
		}
		return false;
	}
	
	private boolean selectToEnd() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			Node focusNode = selection.getFocusNode();
			int focusOffset = selection.getFocusOffset();
			if (focusNode != null) {
				int so = ((IndexedRegion)focusNode).getStartOffset() + focusOffset;
				int eo = ((IndexedRegion)focusNode.getOwnerDocument()).getEndOffset();
				setSelectionRange(so, eo);
				return true;
			}
		}
		return false;
	}
	
	
	private boolean paste() {
		try {
			sourceEditor.getAction(ActionFactory.PASTE.getId()).run();
			return true;
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return false;
		}
	}
	
	private boolean deleteRight() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		if (processNonCollapsedSelection(sourceSelectionBuilder, VK_DELETE)) {
			return true;
		}

		if (processAttributeSelection(sourceSelectionBuilder, VK_DELETE)) {
			return true;
		}
		
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			if (!selection.isCollapsed()) {
				return false;
			}

			Node focusNode = selection.getFocusNode();
			if (VpeDebug.printVisualKeyEvent) {
				System.out.println(">>>   1. VpeVisualKeyHandler.deleteRight(): focusNode = " + focusNode + "   |   focusOffset = " + selection.getFocusOffset());
			}
			if (focusNode != null) {
				if (focusNode.getNodeType() == Node.TEXT_NODE && 
						selection.getFocusOffset() < (((TextImpl)selection.getFocusNode()).getValueSource().length())) {
					deleteRightChar(sourceSelectionBuilder, selection);
					return true;
				} else if ((focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() != 2)) {
					return deleteRightCharOrElement(sourceSelectionBuilder, selection, focusNode);
				} else if (focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() == 2) {
					if (isEmptyElement(focusNode)) {
						return deleteRightCharOrElement(sourceSelectionBuilder, selection, focusNode);
					}
					Node deepestChild = FlatIterator.findDeepestLastChild(focusNode);
					Node next = getNextFlatNode((deepestChild == null ? focusNode : deepestChild));
					if (next != null) {
						focusNode = next;
						return deleteRightCharOrElement(sourceSelectionBuilder, selection, focusNode);
					}
				}
			}
		}
		return false;
	}

	private boolean deleteRightCharOrElement(VpeSourceSelectionBuilder sourceSelectionBuilder, VpeSourceSelection selection, Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			int a = 1;
			if (!hasNoEmptyChildren(node)) {
				AttrImpl attr = (AttrImpl)getVisualNodeSourceAttribute(node);
				if (attr == null || !isVisualEditableForNode(node)) {
					// Delete the node itself
					if (isRemovable(node)) {
						int offset = ((IndexedRegion)node).getStartOffset();
						removeNode(node);
						setSourceFocus(offset);
						return true;
					} else 
						return false;
				} else if (attr != null && isVisualEditableForNode(node)) {
					// Begin new deletion from end of the attr value
					if (isWhitespaceNode(attr)) {
						// Delete the node itself
						if (isRemovable(node)) {
							int offset = ((IndexedRegion)node).getStartOffset();
							removeNode(node);
							setSourceFocus(offset);
							return true;
						} else 
							return false;
					}
					int offset = ((IndexedRegion)node).getStartOffset() + attr.getValueRegion().getStart() + 1;
					setSourceFocus(offset);
					return deleteRight();
				}
				// else go to the next node
			} 
			Node atRight = getNextFlatNode(node);
			while (atRight != null) {
				AttrImpl attr = (AttrImpl)getVisualNodeSourceAttribute(atRight);
				
				if (attr == null) 
					break;

				if (!isVisualEditableForNode(atRight))
					break;

				// Begin new deletion from end of the attr value
				int offset = ((IndexedRegion)atRight).getStartOffset() + attr.getValueRegion().getStart() + 1;
				setSourceFocus(offset);
				return deleteRight();
			}
			if (atRight != null) {
				selectNode(atRight, false);
				return deleteRightCharOrElement(sourceSelectionBuilder, sourceSelectionBuilder.getSelection(), atRight);
			} 
			return false;
		} else {
			int startOffset = ((IndexedRegion)node).getStartOffset();
			setSourceFocus(startOffset);
			deleteRightChar(sourceSelectionBuilder, sourceSelectionBuilder.getSelection());
			return true;
		}
	}
	/**
	 * Deletes right char
	 * @param sourceSelectionBuilder
	 * 
	 * @param selection -contains information about current selection
	 * 
	 * @return
	 */

	private VpeSourceSelection deleteRightChar(VpeSourceSelectionBuilder sourceSelectionBuilder, VpeSourceSelection selection) {
		boolean atLeastOneCharIsDeleted  = false;
		Node focusNode = null;
		while (selection != null && (focusNode = selection.getFocusNode()) != null) {

			if (focusNode == null || focusNode.getNodeType() != Node.TEXT_NODE) break;
			
			if (isWhitespaceNode(focusNode)) {
				int offset = ((IndexedRegion)focusNode).getStartOffset();
				Node atRight = getNextFlatNode(focusNode); 
				if (isRemovable(focusNode)) {
					removeNode(focusNode);
					setSourceFocus(offset);
					if (atRight == null) break;
					selection = sourceSelectionBuilder.getSelection();
					continue;
				}
			}
			
			int offset = (focusNode == selection.getFocusNode() ? selection.getFocusOffset() : (((TextImpl)focusNode).getValueSource().length()));

			int length = (((TextImpl)selection.getFocusNode()).getValueSource()).length();
			if (length <= offset) {
					break;
			}

			if (!atLeastOneCharIsDeleted) {
				if(focusNode.getNodeType() == Node.TEXT_NODE){
					IndexedRegion region = (IndexedRegion)focusNode;
					try{
						String sourceText = sourceEditor.getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
						String escString = TextUtil.isEcsToRight(sourceText, offset);
						if(escString != null){
							Point range = sourceEditor.getTextViewer().getSelectedRange();
							sourceEditor.getTextViewer().getTextWidget().replaceTextRange(range.x, escString.length(), "");
							atLeastOneCharIsDeleted = true;
							continue;
						}
					} catch(Exception e) {
						VpePlugin.getPluginLog().logError(e);
					}
				}
				
				if (!TextUtil.isWhitespace(((TextImpl)focusNode).getValueSource().toCharArray()[offset])) {
					sourceEditor.getAction(ITextEditorActionDefinitionIds.DELETE_NEXT).run();
					selection = sourceSelectionBuilder.getSelection();
					atLeastOneCharIsDeleted = true;
					continue;
				}
			}
			if (!TextUtil.isWhitespace(((TextImpl)focusNode).getValueSource().toCharArray()[offset])
						) {
				break;
			}else if(!atLeastOneCharIsDeleted) {
				
				//delete whitespaces 
				int endPos = 0;
				while(TextUtil.isWhitespace(((TextImpl)focusNode).getValueSource().toCharArray()[offset+endPos])){
					endPos++;
				}
				Point range = sourceEditor.getTextViewer().getSelectedRange();
				sourceEditor.getTextViewer().getTextWidget().replaceTextRange(range.x, endPos, "");
				selection = sourceSelectionBuilder.getSelection();
				atLeastOneCharIsDeleted = true;
				break;
			} else {
				break;
			}
//			sourceEditor.getAction(ITextEditorActionDefinitionIds.DELETE_NEXT).run();
//			selection = sourceSelectionBuilder.getSelection();
		}
		
		return selection;
	}

	private boolean processNonCollapsedSelection(VpeSourceSelectionBuilder sourceSelectionBuilder, int keyCode) {
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			List selectedNodes = selection.getSelectedNodes();
			int focusOffset = -1;
			if (!selection.isCollapsed()) {
				for (int i = 0; i < selectedNodes.size(); i++) {
					VpeSelectedNodeInfo nodeInfo = (VpeSelectedNodeInfo)selectedNodes.get(i);
					focusOffset = processNode(nodeInfo, keyCode, selection);
				}
				Node commonAncestor = selection.getCommonAncestor();
				if (commonAncestor != null) {
					selection.getCommonAncestor().normalize();
				}
				if (focusOffset > 0)
					setSourceFocus(focusOffset);
				return true;
			}
		}
		return false;
	}

	private boolean processAttributeSelection(VpeSourceSelectionBuilder sourceSelectionBuilder, int keyCode) {
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			if (selection.getFocusAttribute() != null) {
				AttrImpl attr = (AttrImpl)selection.getFocusAttribute();
				Point range = selection.getFocusAttributeRange();
				if (range == null) return false;
				if (range.y > 0) {
					String value = attr.getValue();
					int attrSelStart = ((ElementImpl)attr.getOwnerElement()).getStartOffset() + ((AttrImpl)attr).getValueRegion().getStart() + 1 + range.x;
					int so = range.x;
					int eo = range.x + range.y;
					value = value.substring(0, so) + value.substring(eo);
					attr.setNodeValue(value);
					setSourceFocus(attrSelStart);
					return true;
				} else {
					int attrSelStart = ((ElementImpl)attr.getOwnerElement()).getStartOffset() + ((AttrImpl)attr).getValueRegion().getStart() + 1 + range.x;
					switch (keyCode) {
					case VK_DELETE: {
						String value = attr.getValue();
						int so = range.x;
						int eo = range.x + 1;
						if (!isVisualEditableForNode(attr.getOwnerElement()) || so < 0) {
							int offset = ((IndexedRegion)attr.getOwnerElement()).getStartOffset();
							setSourceFocus(offset);
							return false;
						}
						if (eo > value.length()) {
							// Proceed with next element if it possible
							int offset = ((IndexedRegion)attr.getOwnerElement()).getEndOffset();
							setSourceFocus(offset);
							return false;
						}
						value = value.substring(0, so) + value.substring(eo);
						attr.setNodeValue(value);
						setSourceFocus(attrSelStart);
						return true;
					}
					case VK_BACK_SPACE: {
						String value = attr.getValue();
						int so = range.x - 1;
						int eo = range.x;
						if (!isVisualEditableForNode(attr.getOwnerElement()) || so < 0 || eo > value.length()) {
							int offset = ((IndexedRegion)attr.getOwnerElement()).getStartOffset();
							setSourceFocus(offset);
							return false;
						}
						value = value.substring(0, so) + value.substring(eo);
						attr.setNodeValue(value);
						setSourceFocus(attrSelStart - 1);
						return true;
					}
					default: return false;
					}
				}
			}
		}
		return false;
	}

	private boolean deleteLeft() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		if (processNonCollapsedSelection(sourceSelectionBuilder, VK_BACK_SPACE)) {
			return true;
		}

		if (processAttributeSelection(sourceSelectionBuilder, VK_BACK_SPACE)) {
			return true;
		}
		
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			if (!selection.isCollapsed()) {
				return false;
			}

			Node focusNode = selection.getFocusNode();
			if (VpeDebug.printVisualKeyEvent) {
				System.out.println(">>>   1. VpeVisualKeyHandler.deleteLeft(): focusNode = " + focusNode + "   |   focusOffset = " + selection.getFocusOffset());
			}
			if (focusNode != null) {
				if (focusNode.getNodeType() == Node.TEXT_NODE && selection.getFocusOffset() >= 0) {
					deleteLeftChar(sourceSelectionBuilder, selection);
					return true;
				} else if (focusNode.getNodeType() == Node.TEXT_NODE && selection.getFocusOffset() == 0) {
					focusNode = getPreviousFlatNode(focusNode);
					if (focusNode == null) 
						return false;

					return deleteLeftCharOrElement(sourceSelectionBuilder, selection, focusNode);
				} else if ((focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() == 0)) {
					Node prev = getPreviousFlatNode(focusNode);
					if (prev != null) {
						return deleteLeftCharOrElement(sourceSelectionBuilder, selection, prev);
					} else {
						return true;
					}
				} else if (focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() == 2) {
					Node deepestChild = FlatIterator.findDeepestLastChild(focusNode);
					if (deepestChild != null) 
						focusNode = deepestChild;
					
					return deleteLeftCharOrElement(sourceSelectionBuilder, selection, focusNode);
				} else if (focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() == 1) {
					Node deepestChild = FlatIterator.findDeepestLastChild(focusNode);
					if (deepestChild != null) { 
						focusNode = deepestChild;
					}
					return deleteLeftCharOrElement(sourceSelectionBuilder, selection, focusNode);
				}
			}
		}
		return false;
	}

	private boolean deleteLeftCharOrElement(VpeSourceSelectionBuilder sourceSelectionBuilder, VpeSourceSelection selection, Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (!hasNoEmptyChildren(node)) {
				AttrImpl attr = (AttrImpl)getVisualNodeSourceAttribute(node);
				if (attr == null || !isVisualEditableForNode(node)) {
					// Delete the node itself
					Node atLeft = getPreviousFlatNode(node);
					if (isRemovable(node)) {
						boolean isParent = (atLeft == node.getParentNode());
						Node prev = node.getPreviousSibling();
						Node next = node.getNextSibling();
						removeNode(node);
						if (prev != null && next != null && prev.getNodeType() == Node.TEXT_NODE && next.getNodeType() == Node.TEXT_NODE) {
							int len = ((Text)prev).getData().length();
							((Text)prev).appendData(((Text)next).getData());
							next.getParentNode().removeChild(next);
							setSourceFocus(((IndexedRegion)prev).getStartOffset() + len);
						} else {
							selectNode(atLeft, !isParent);
						}
						return true;
					} else 
						return false;
				} else if (attr != null && isVisualEditableForNode(node)) {
					// Begin new deletion from end of the attr value
					if (isWhitespaceNode(attr)) {
						// Delete the node itself
						Node atLeft = getPreviousFlatNode(node);
						if (isRemovable(node)) {
							boolean isParent = (atLeft == node.getParentNode());
							removeNode(node);
							selectNode(atLeft, !isParent);
							return true;
						} else {
							return false;
						}
					}
					int offset = ((IndexedRegion)node).getStartOffset() + attr.getValueRegion().getStart() + attr.getValue().length() + 1;
					setSourceFocus(offset);
					return deleteLeft();
				}
				// else go to the previous node
			} 
			Node atLeft = getPreviousFlatNode(node);
			while (atLeft != null) {
				AttrImpl attr = (AttrImpl)getVisualNodeSourceAttribute(atLeft);
				
				if (attr == null) 
					break;

				if (isVisualEditableForNode(atLeft)) {
					// Begin new deletion from end of the attr value
					int offset = ((IndexedRegion)atLeft).getStartOffset() + attr.getValueRegion().getStart() + attr.getValue().length() + 1;
					setSourceFocus(offset);
					return deleteLeft();
				} else {
					// select prev node
					atLeft = getPreviousFlatNode(atLeft);
				}
			}
			if (atLeft != null) {
				selectNode(atLeft, true);
				return deleteLeftCharOrElement(sourceSelectionBuilder, sourceSelectionBuilder.getSelection(), atLeft);
			} 
			return false;
		} else {
			int endOffset = ((IndexedRegion)node).getEndOffset();
			setSourceFocus(endOffset);
			deleteLeftChar(sourceSelectionBuilder, sourceSelectionBuilder.getSelection());
			return true;
		}
	}
	
	private void selectNode(Node node, boolean atEnd) {
		if (node != null) {
			int offset = ((IndexedRegion)node).getStartOffset();
			if (atEnd) {
				if (node.getNodeType() == Node.TEXT_NODE)
					offset = ((IndexedRegion)node).getEndOffset();
				else if (node.getNodeType() == Node.ELEMENT_NODE) 
					offset = ((ElementImpl)node).getEndStartOffset();
			}
			setSourceFocus(offset);
		}
	}
	
	private boolean isRemovable(Node node) {
//		if (node.)
		return true;
	}

	private Node getNodeAtLeft(Node focusNode, int offset) {
		if (focusNode != null) {
			int focusNodeOffset = -1;
			if (focusNode.getNodeType() == Node.TEXT_NODE) {
				if (isWhitespaceNode(focusNode, offset)) {
					focusNodeOffset = ((IndexedRegion)focusNode).getStartOffset();
				} else {
					return focusNode;
				}
			} else if (focusNode.getNodeType() == Node.ELEMENT_NODE && offset == 1) {
				focusNodeOffset = ((IndexedRegion)focusNode).getStartOffset();
			} else if (focusNode.getNodeType() == Node.ELEMENT_NODE && offset == 2) {
				focusNodeOffset = ((ElementImpl)focusNode).getEndStartOffset();
			}
			if (focusNodeOffset > 0) {
				focusNodeOffset--;
				return getSourceNodeAt(focusNodeOffset);
			}
		}
		return null;
	}
	
	private Node getSourceNodeAt(int offset) {
		if (sourceEditor != null && sourceEditor.getModel() != null) {
			IndexedRegion node = sourceEditor.getModel().getIndexedRegion(offset);
			if (node == null) {
				node = sourceEditor.getModel().getIndexedRegion(offset - 1);
			}
			if (node instanceof Node) {
				return (Node) node;
			}
		}
		return null;
	}

	private VpeSourceSelection deleteLeftChar(VpeSourceSelectionBuilder sourceSelectionBuilder, VpeSourceSelection selection) {
		boolean atLeastOneCharIsDeleted = false;
		Node focusNode = null;
		while (selection != null && (focusNode = selection.getFocusNode()) != null) {
			if (focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() == 2) {
				focusNode = FlatIterator.findDeepestLastChild(focusNode);
			}
			if (focusNode != null && focusNode.getNodeType() == Node.ELEMENT_NODE && selection.getFocusOffset() != 2) {
				focusNode = getPreviousFlatNode(focusNode);
			}
			if (focusNode == null || focusNode.getNodeType() != Node.TEXT_NODE) break;
			if (isWhitespaceNode(focusNode)) {
				Node atLeft = getPreviousFlatNode(focusNode);
				if (isRemovable(focusNode)) {
					int offset = ((IndexedRegion)focusNode).getStartOffset();
					removeNode(focusNode);
					selectNode(atLeft, true);
					selection = sourceSelectionBuilder.getSelection();
					continue;
				}
			}

			int offset = (focusNode == selection.getFocusNode() ? selection.getFocusOffset() : (((TextImpl)focusNode).getValueSource().length()));
							
			
			if (offset == 0) { 
				break;
			}

			if (!atLeastOneCharIsDeleted) {
				if(focusNode.getNodeType() == Node.TEXT_NODE){
					IndexedRegion region = (IndexedRegion)focusNode;
					try {
						String sourceText = sourceEditor.getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
						String escString = TextUtil.isEcsToLeft(sourceText,offset);

						if(escString != null){
							Point range = sourceEditor.getTextViewer().getSelectedRange();
							sourceEditor.getTextViewer().getTextWidget().replaceTextRange(range.x-escString.length(), escString.length(), "");

							atLeastOneCharIsDeleted = true;
							return selection;
						}
					} catch(Exception ex) {
						VpePlugin.getPluginLog().logError(ex);
					}
				}

				if(!atLeastOneCharIsDeleted) {
					
					int endPos = 1;
					while(TextUtil.isWhitespace(((TextImpl)focusNode).getValueSource().toCharArray()[offset-endPos])){
						endPos++;
					}
					endPos--;
					if(endPos!=0){
					Point range = sourceEditor.getTextViewer().getSelectedRange();
					sourceEditor.getTextViewer().getTextWidget().replaceTextRange(range.x-endPos, endPos, "");
					selection = sourceSelectionBuilder.getSelection();
					atLeastOneCharIsDeleted = true;
					}
				}
				if(!atLeastOneCharIsDeleted){
				sourceEditor.getAction(ITextEditorActionDefinitionIds.DELETE_PREVIOUS).run();
				selection = sourceSelectionBuilder.getSelection();
				atLeastOneCharIsDeleted = true;
				continue;}
			}
			String valueSource = ((TextImpl)focusNode).getValueSource();
			if (valueSource==null) {
				break;
			} else {
				char[] charArray = valueSource.toCharArray();
				if(offset>charArray.length || !TextUtil.isWhitespace(valueSource.toCharArray()[offset - 1])) {
					break;
				}
			} 
			if(!atLeastOneCharIsDeleted) {			
				sourceEditor.getAction(ITextEditorActionDefinitionIds.DELETE_PREVIOUS).run();
			}else {
				break;
			}
			selection = sourceSelectionBuilder.getSelection();
		}

		return selection;
	}

	private Node getPreviousFlatNode (Node node) {
		for (Node prev = FlatIterator.previous(node); prev != null; prev = FlatIterator.previous(prev)) {
			if (prev.getNodeType() == Node.TEXT_NODE || getVisualNode(prev) != null) {
				return prev;
			}
		}
		return null;
	}
	private Node getNextFlatNode (Node node) {
		for (Node next = FlatIterator.next(node); next != null; next = FlatIterator.next(next)) {
			if (getVisualNode(next) != null || next.getNodeType() == Node.TEXT_NODE)
				return next;
		}
		return null;
	}
	
	private void selectPreviousNode(Node focusNode) {
		Node prevNode = getPreviousTextNode(focusNode);
		if (prevNode != null) {
			int endOffset = ((IndexedRegion)prevNode).getEndOffset();
			setSourceFocus(endOffset);
		}
	}
	
	private Node getPreviousTextNode(Node focusNode) {
		Node next = getPreviousEditableNode(focusNode);
		while (next != null) {
			if (next.getNodeType() == Node.TEXT_NODE && !isWhitespaceNode(next)) {
				return next;
			}
			next = getPreviousEditableNode(next);
		}
		return null;
	}

	private Node getPreviousEditableNode(Node currentNode) {
		if (currentNode != null)  {
			Node prev = currentNode.getPreviousSibling();
			if (prev != null) return getLastSourceChild(prev);
			return currentNode.getParentNode();
		}
		return null;
	}

	private Node getLastSourceChild(Node node) {
		if (node != null) {
			Node lastChild = node.getLastChild();
			if (lastChild == null) return node;
			Node lastChildOld = lastChild;
			while (lastChild != null) {
				lastChild = lastChild.getLastChild();
				if (lastChild != null) {
					lastChildOld = lastChild;
				}
			}
			return lastChildOld;
		}
		return null;
	}
	
	private boolean hasNoEmptyChildren(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (node.hasChildNodes()) {
				for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (!isEmptyElement(child)) return true; 
				}
			}
			return false; 
		}
		return false;
	}
	
	
	
	private boolean isEmptyElement(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			return isWhitespaceNode(node, ((TextImpl)node).getValueSource().length());
		} else {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.hasChildNodes()) {
					for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
						if (!isEmptyElement(child)) return false; 
					}
				}
				return (getVisualNodeSourceAttribute(node) == null || isVisualNodeEmpty(node) || (!isVisualNodeEmpty(node) && !isVisualEditableForNode(node))); 
			}
		}
		return false;
	}

	private boolean isEmptyVisualElement(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			return isWhitespaceNode(node, ((TextImpl)node).getValueSource().length());
		} else {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.hasChildNodes()) {
					for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
						if (!isEmptyElement(child)) return false; 
					}
				}
				return true;
			}
		}
		return false;
	}

	private boolean isVisualEditableForNode(Node node) {
		try {
			Node visualNode = domMapping.getVisualNode(node);
			if (visualNode.getNodeType() == Node.ELEMENT_NODE) {
				Node styleAttr = visualNode.getAttributes().getNamedItem("style");
				if (styleAttr != null) {
					String style = styleAttr.getNodeValue();
					if (style != null) {
						if (style.indexOf(VpeHtmlTemplate.ATTR_STYLE_MODIFY_READ_ONLY_VALUE) < 0) {
							return true;
						}
					}
				}
			} else if (visualNode.getNodeType() == Node.TEXT_NODE) {
				return true;
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return false;
	}
	
	private boolean isVisualNodeSourceAttribute(Node node) {
		try {
			Node visualNode = domMapping.getVisualNode(node);
			VpeNodeMapping nm = domMapping.getNodeMapping(visualNode);
			if (nm.getType() == VpeNodeMapping.ELEMENT_MAPPING) {
				VpeElementMapping em = (VpeElementMapping)nm;
				return em.getTemplate().isOutputAttributes();
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return false;
	}

	private boolean isVisualNodeEmpty(Node node) {
		try {
			Node visualNode = domMapping.getVisualNode(node);
			return pageContext.getVisualBuilder().isEmptyElement(visualNode);
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return false;
	}

	private Attr getVisualNodeSourceAttribute(Node node) {
		try {
			Node visualNode = domMapping.getVisualNode(node);
			if(visualNode==null) {
				return null;
			}
			VpeNodeMapping nm = domMapping.getNodeMapping(visualNode);
			if (nm.getType() == VpeNodeMapping.ELEMENT_MAPPING) {
				VpeElementMapping mapping = (VpeElementMapping)nm;
				String[] names = mapping.getTemplate().getOutputAtributeNames();
				if(names!=null) {
					String name = names[0];
					return (Attr)node.getAttributes().getNamedItem(name);
				}
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return null;
	}

	private Node getVisualNode(Node node) {
		if (domMapping != null) {
			Node visualNode = domMapping.getVisualNode(node);
			boolean isEditable = false;
			if (visualNode != null) {
				if (visualNode.getNodeType() == Node.ELEMENT_NODE || 
						visualNode.getNodeType() == Node.TEXT_NODE) {
					return visualNode;
				}
			}
		} 
		return null;
	}

	private boolean isWhitespaceNode(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			return isWhitespaceNode(node, ((TextImpl)node).getValueSource().length());
		} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			return isWhitespaceNode(node, ((AttrImpl)node).getValue().length());
		} else {
			return false;
		}
	}

	private boolean isWhitespaceNode(Node node, int offset) {
		if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ATTRIBUTE_NODE) {
			char[] value = (node.getNodeType() == Node.TEXT_NODE ? 
						((TextImpl)node).getValueSource().toCharArray() : 
							((AttrImpl)node).getValue().toCharArray());
			for (int i = 0; i < Math.min(value.length, offset); i++) {
				if (!TextUtil.isWhitespace(value[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	private void setSourceFocus(int offset) {
		pageContext.getSourceBuilder().getStructuredTextViewer().setSelectedRange(offset, 0);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(offset, 0);
	}

	private void setSelectionRange(int startOffset, int endOffset) {
		pageContext.getSourceBuilder().getStructuredTextViewer().setSelectedRange(endOffset, startOffset - endOffset);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(endOffset, startOffset - endOffset);
	}
	
	private void removeNode(Node focusNode) {
		if (focusNode != null && focusNode.getParentNode() != null) {
			focusNode.getParentNode().removeChild(focusNode);
		}
	}

	private boolean handleKey(nsIDOMKeyEvent keyEvent) {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		processNonCollapsedSelection(sourceSelectionBuilder, keyEvent.getCharCode());
		processAttributeSelection(sourceSelectionBuilder, keyEvent.getCharCode());

		boolean isEditable = false;
		int start = 0;
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			Node sourceNode = selection.getFocusNode();
			if (sourceNode != null) {
				VpeDomMapping mapping = pageContext.getDomMapping();
				if (mapping != null) {
					Node visualNode = mapping.getVisualNode(sourceNode);
					if (visualNode != null) {
						if (visualNode.getNodeType() == Node.ELEMENT_NODE) {
							Node styleAttr = visualNode.getAttributes().getNamedItem("style");
							if (styleAttr != null) {
								String style = styleAttr.getNodeValue();
								if (style != null) {
									if (style.indexOf(VpeHtmlTemplate.ATTR_STYLE_MODIFY_READ_ONLY_VALUE) < 0) {
										isEditable = true;
									}
								}
							}
						} else if (visualNode.getNodeType() == Node.TEXT_NODE) {
							isEditable = true;
						}
					} else {
						isEditable = true;
					}
					if (isEditable) {
						start = sourceEditor.getTextViewer().getSelectedRange().x;
					}
				}
			}
		} else if (pageContext.getSourceBuilder().isEmptyDocument()) {
			isEditable = true;
			start = 0;
		}
		if (isEditable) {
			int charCode = keyEvent.getCharCode();
			char[] s = new char[1];
			s[0] = (char) charCode;
			String str = new String(s);
			if(TextUtil.containsKey(s[0])){
				str = TextUtil.getValue(s[0]);
			}
			sourceEditor.getTextViewer().getTextWidget().replaceTextRange(start, 0, str);
			pageContext.getSourceBuilder().getStructuredTextViewer().setSelectedRange(start + str.length(), 0);
			pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(start + str.length(), 0);
			return true;
		}
		return false;
	}

	private boolean _nonctrlKeyPressHandler(nsIDOMKeyEvent keyEvent) {
		ITextFormatter formatter = null;
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			int keyCode = keyEvent.getKeyCode();
			List selectedNodes = selection.getSelectedNodes();
			switch (keyCode) {
				case VK_ENTER:
					if (!selection.isCollapsed()) {
						for (int i = 0; i < selectedNodes.size(); i++) {
							VpeSelectedNodeInfo nodeInfo = (VpeSelectedNodeInfo)selectedNodes.get(i);
							processNode(nodeInfo, keyCode, selection);
						}
						Node commonAncestor = selection.getCommonAncestor();
						if (commonAncestor != null) {
							selection.getCommonAncestor().normalize();
						}
					}

					Node focusNode = selection.getFocusNode();
					VpeNodeMapping parentMapping = domMapping.getNearParentMapping(focusNode);
					if (parentMapping != null) {
						boolean handled = false;
						while (!handled && parentMapping != null && parentMapping instanceof VpeElementMapping) {
							VpeTemplate template = ((VpeElementMapping)parentMapping).getTemplate();
							Node srcNode = parentMapping.getSourceNode();
							Node visualNode = parentMapping.getVisualNode();
							handled = template.nonctrlKeyPressHandler(pageContext, srcNode.getOwnerDocument(), srcNode, visualNode, null, keyCode, selection, formatter);
							parentMapping = domMapping.getParentMapping(srcNode);
						}
						if (!handled && focusNode.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
							if (focusNode.getNodeType() == Node.TEXT_NODE) {
								Point range = sourceEditor.getTextViewer().getSelectedRange();
								Node p1 = focusNode.getOwnerDocument().createElement("p");
								Node p2 = focusNode.getOwnerDocument().createElement("p");
								Text newNode = ((Text)focusNode).splitText(getSourceNodeOffset(focusNode, range.x));
								focusNode.getParentNode().insertBefore(p1, focusNode);
								focusNode.getParentNode().insertBefore(p2, newNode);
								focusNode = focusNode.getParentNode().removeChild(focusNode);
								newNode = (Text)newNode.getParentNode().removeChild(newNode);
								p1.appendChild(focusNode);
								p2.appendChild(newNode);
								setCursor(pageContext, newNode);
							}
						}
					} else if (focusNode instanceof Comment) {
						String focusText = focusNode.getNodeValue().substring(0, selection.getFocusOffset());
						String cloneText = focusNode.getNodeValue().substring(selection.getFocusOffset());
						if (focusText.length() > 0 && cloneText.length() > 0) {
							Node clone = focusNode.cloneNode(true);
							clone.setNodeValue(cloneText);
							clone = focusNode.getParentNode().insertBefore(clone, focusNode.getNextSibling());
							focusNode.setNodeValue(focusText);
						}
					}
					IAction formatAll = sourceEditor.getAction(StructuredTextEditorActionConstants.ACTION_NAME_FORMAT_ACTIVE_ELEMENTS);
					formatAll.run();
					break;
			}
		}
		return false;
	}

	private boolean split() {
		ITextFormatter formatter = null;
		IRegion region = null;
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null) {
			List selectedNodes = selection.getSelectedNodes();
			if (!selection.isCollapsed()) {
				for (int i = 0; i < selectedNodes.size(); i++) {
					VpeSelectedNodeInfo nodeInfo = (VpeSelectedNodeInfo)selectedNodes.get(i);
					processNode(nodeInfo, VK_ENTER, selection);
				}
				Node commonAncestor = selection.getCommonAncestor();
				if (commonAncestor != null) {
					selection.getCommonAncestor().normalize();
				}
			}

			Node focusNode = selection.getFocusNode();
			
			int formatStart = 0;
			try {
				formatStart = ((IDOMNode)focusNode).getStartOffset();
			} catch (Exception e) {
				VpePlugin.reportProblem(e);
			}
			
			VpeNodeMapping parentMapping = domMapping.getNearParentMapping(focusNode);
			if (parentMapping != null) {
				boolean handled = false;
				while (!handled && parentMapping != null && parentMapping instanceof VpeElementMapping) {
					VpeTemplate template = ((VpeElementMapping)parentMapping).getTemplate();
					Node srcNode = parentMapping.getSourceNode();
					Node visualNode = parentMapping.getVisualNode();
					handled = template.nonctrlKeyPressHandler(pageContext, srcNode.getOwnerDocument(), srcNode, visualNode, null, VK_ENTER, selection, formatter);
					parentMapping = domMapping.getParentMapping(srcNode);
				}
				if (!handled) {
					if (focusNode.getNodeType() == Node.TEXT_NODE) {
						if (focusNode.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
							Point range = sourceEditor.getTextViewer().getSelectedRange();
							Node p1 = focusNode.getOwnerDocument().createElement("p");
							Node p2 = focusNode.getOwnerDocument().createElement("p");
							Text newNode = ((Text)focusNode).splitText(getSourceNodeOffset(focusNode, range.x));
							focusNode.getParentNode().insertBefore(p1, focusNode);
							focusNode.getParentNode().insertBefore(p2, newNode);
							focusNode = focusNode.getParentNode().removeChild(focusNode);
							newNode = (Text)newNode.getParentNode().removeChild(newNode);
							p1.appendChild(focusNode);
							p2.appendChild(newNode);
							setCursor(pageContext, newNode);
							
							int p1Start = ((IndexedRegion)p1).getStartOffset();
							int p2End = ((IndexedRegion)p2).getEndOffset();
							region = new Region(p1Start, p2End - p1Start);
						} else {
							int n1Start = ((IndexedRegion)focusNode).getStartOffset();
							int focusOffset = selection.getFocusOffset();
							if (focusOffset == 0) {
								String text = ((Text)focusNode).getData();
								String trimText =  text.trim();
								if (trimText.length() <= 0) {
									focusOffset = text.length(); 
								} else {
									focusOffset = text.indexOf(text.trim()); 
								}
							}
							Text newNode = ((Text)focusNode).splitText(focusOffset);
							Node br = focusNode.getParentNode().insertBefore(focusNode.getOwnerDocument().createElement("br"), newNode);
							Text newNode1 = (Text)focusNode.getParentNode().insertBefore(focusNode.getOwnerDocument().createTextNode("\r\n" + getLinePrefix(n1Start + focusOffset) + newNode.getData()), newNode);
							focusNode.getParentNode().removeChild(newNode);
							newNode = newNode1;
							setCursor(pageContext, newNode);
							int n2End = ((IndexedRegion)newNode).getEndOffset();
							region = new Region(n1Start, n2End - n1Start);
						}
					}
				}
			} else if (focusNode instanceof Comment) {
				String focusText = focusNode.getNodeValue().substring(0, selection.getFocusOffset());
				String cloneText = focusNode.getNodeValue().substring(selection.getFocusOffset());
				if (focusText.length() > 0 && cloneText.length() > 0) {
					Node clone = focusNode.cloneNode(true);
					clone.setNodeValue(cloneText);
					clone = focusNode.getParentNode().insertBefore(clone, focusNode.getNextSibling());
					focusNode.setNodeValue(focusText);
				}
			}
			
			try {
				StructuredTextViewer viewer = sourceEditor.getTextViewer();
				if (region != null){
					if (sourceEditor instanceof ITextFormatter) {
						((ITextFormatter)sourceEditor).formatTextRegion(sourceEditor.getTextViewer().getDocument(), region);
					}						
				}
			} catch (Exception x) {
				VpePlugin.reportProblem(x);
			}
			return true;
		}
		return false;
	}

	private String getLinePrefix(int offset) {
		try {
			final IDocument document = sourceEditor.getTextViewer().getDocument();
			final int linePrefixBeginning = document.getLineInformationOfOffset(offset).getOffset();
			String linePrefix = document.get(linePrefixBeginning, offset - linePrefixBeginning);
			int linePrefixLength = 0;
			for (; linePrefixLength < linePrefix.length(); linePrefixLength++) {
				if (!Character.isWhitespace(linePrefix.charAt(linePrefixLength))) 
					break;
			}
			return linePrefix.substring(0, linePrefixLength);
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return "";
		}
	}
	
	private int processNode(VpeSelectedNodeInfo nodeInfo, int keyCode, VpeSourceSelection selection) {
		Node node = nodeInfo.getNode();
		if (domMapping.getNodeMapping(node) != null) {
			int type = node.getNodeType();
			if (type == Node.TEXT_NODE || type == Node.COMMENT_NODE) {
				int so = nodeInfo.getStartOffset();
				int eo = nodeInfo.getEndOffset();
				if (so > eo) {
					int tmp = so;
					so = eo;
					eo = tmp;
				}
				int pos = ((IndexedRegion)node).getStartOffset() + so;
				try {
					String source = ((TextImpl)node).getSource();
					((TextImpl)node).setSource(source.substring(0, so) + source.substring(eo));
				} catch (Exception e) {
					VpePlugin.getPluginLog().logError(e);
				}
				if (node.getNodeValue().trim().length() == 0) {
					node.getParentNode().removeChild(node);
				}
				return pos;
			} else {
				int so = ((IndexedRegion)node).getStartOffset();
				node.getParentNode().removeChild(node);
				return so;
			}
		} else {
			goToParentTemplate(node, selection, keyCode);
			return -1;
		}
	}

	private Node getParent(Node node, String tagName) {
		Node p = node.getParentNode();
		while (p != null) {
			if (p.getNodeName().equalsIgnoreCase(tagName)) {
				return p;
			}
			p = p.getParentNode();
		}
		return null;
	}
	
	private void goToParentTemplate(Node node, VpeSourceSelection selection, int keyCode) {
		ITextFormatter formatter = null;
		VpeNodeMapping nearNodeMapping = domMapping.getNearParentMapping(node);
		if (nearNodeMapping != null && nearNodeMapping instanceof VpeElementMapping) {
			VpeTemplate template = ((VpeElementMapping)nearNodeMapping).getTemplate();
			if (template != null) {
				Node sourceNode = nearNodeMapping.getSourceNode();
				Node visualNode = nearNodeMapping.getVisualNode();
				template.nonctrlKeyPressHandler(null, sourceNode.getOwnerDocument(), sourceNode, visualNode, null, keyCode, selection, formatter);
			}
		}
	}

	 private int getSourceNodeOffset(Node node, int pos) {
	 	if (node == null) return 0;
		int start = ((IndexedRegion)node).getStartOffset();
		int end = ((IndexedRegion)node).getEndOffset();
		
		if (node.getNodeType() == Node.TEXT_NODE) {
			if (pos > end) {
				return end - start;
			} else {
				return pos - start;
			}
		}
		return 0;
	 }

	private void setCursor(VpePageContext pageContext, Node node) {
		int nodeOffset = ((IndexedRegion)node).getStartOffset();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			ElementImpl element = (ElementImpl)node;
			nodeOffset = element.getStartEndOffset();
		}
		pageContext.getSourceBuilder().getStructuredTextViewer().setSelectedRange(nodeOffset, 0);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(nodeOffset, 0);
	}
	
	private boolean moveForward() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null && selection.isCollapsed()) {
			Node node = getNextNode(selection.getStartNode());
			if (node != null) {
				int nodeOffset = ((IndexedRegion)node).getStartOffset();
				pageContext.getSourceBuilder().getStructuredTextViewer().setSelectedRange(nodeOffset, 0);
				pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(nodeOffset, 0);
				return true;
			}
		}
		return false;
	}
	
	private boolean moveRight() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection == null || selection.getFocusNode() == null) 
			return false; 

		// We're in the visible attribute
		if (selection.getFocusAttribute() != null) {
			if (isVisualEditableForNode(selection.getFocusNode())) {
				AttrImpl attr = (AttrImpl)selection.getFocusAttribute();
				
				Point range = selection.getFocusAttributeRange();
				if (range != null) {
					int eo = range.x + range.y;
					int attrSelStart = ((ElementImpl)attr.getOwnerElement()).getStartOffset() + ((AttrImpl)attr).getValueRegion().getStart() + 1 + eo;
					
					if (eo + 1 <= ((AttrImpl)attr).getValue().length()) { 
						setSourceFocus(attrSelStart + 1);
						return true;
					} else {
						return moveCursorToNextVisualNode(selection.getFocusNode());
					}
				}
			} else {
				return moveCursorToNextVisualNode(selection.getFocusNode());
			}
		} else {
			if (selection.getFocusNode().getNodeType() == Node.TEXT_NODE ||
					selection.getFocusNode().getNodeType() == Node.COMMENT_NODE) {
				int so = ((IndexedRegion)selection.getFocusNode()).getStartOffset();
				int eo = ((IndexedRegion)selection.getFocusNode()).getEndOffset();
				if (selection.getFocusOffset() >= eo-so) {
					return moveCursorToNextVisualNode(selection.getFocusNode());
				}
				int fo = selection.getFocusOffset();
				char[] chars = ((TextImpl)selection.getFocusNode()).getValueSource().toCharArray();
				
				String sourceValue=((TextImpl)selection.getFocusNode()).getValueSource();
				String nodeValue=selection.getFocusNode().getNodeValue();				
				
				int visualOffser=TextUtil.visualPosition(sourceValue, fo);

				int sourseOffset=TextUtil.sourcePosition(sourceValue, nodeValue, visualOffser+1);
				int diff = sourseOffset-fo;
				if(isTextToSkip(chars, fo + diff-1)){
				while ((fo + diff < chars.length) && isTextToSkip(chars, fo + diff)) {
					diff++;
				}
				}
				if (fo + diff > chars.length||diff==0) {
					return moveCursorToNextVisualNode(selection.getFocusNode());
				}
				setSourceFocus(so + fo + diff);
				
				return true;
			} else if (selection.getFocusNode().getNodeType() == Node.ELEMENT_NODE) {
				// Move to second position of the visual attribute (because we're placed 
				//   either before first char of visual attribute or not in visual attribute 
				//   but still in the beginning of the element
				return moveCursorToNextVisualNode(selection.getFocusNode());
			}
		}
		
		return false;
	}

	//moves selection to left
	private boolean moveLeft() {
		
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection == null || selection.getFocusNode() == null) {
	
			return false; 
		}
		// We're in the visible attribute
		if (selection.getFocusAttribute() != null) {
			if (isVisualEditableForNode(selection.getFocusNode())) {
				AttrImpl attr = (AttrImpl)selection.getFocusAttribute();
				
				Point range = selection.getFocusAttributeRange();
				if (range != null) {
					int eo = range.x + range.y;
					int attrSelStart = ((ElementImpl)attr.getOwnerElement()).getStartOffset() + ((AttrImpl)attr).getValueRegion().getStart() + 1 + eo;
					
					if (eo > 0) { 
						setSourceFocus(attrSelStart - 1);
						return true;
					} else {
						return moveCursorToPrevVisualNode(selection.getFocusNode());
					}
				}
			} else {
				return moveCursorToPrevVisualNode(selection.getFocusNode());
			}
		} else {
			if (selection.getFocusNode().getNodeType() == Node.TEXT_NODE ||
					selection.getFocusNode().getNodeType() == Node.COMMENT_NODE) {
				int so = ((IndexedRegion)selection.getFocusNode()).getStartOffset();
				int eo = ((IndexedRegion)selection.getFocusNode()).getEndOffset();
				
				if (selection.getFocusOffset() <= 0) {

					return moveCursorToPrevVisualNode(selection.getFocusNode());
				}
				int fo = selection.getFocusOffset();


				
				char[] chars = ((TextImpl)selection.getFocusNode()).getValueSource().toCharArray();
				String sourceValue=((TextImpl)selection.getFocusNode()).getValueSource();
				String nodeValue=selection.getFocusNode().getNodeValue();
				
				int visualOffser=TextUtil.visualPosition(sourceValue, fo);
				int sourseOffset=TextUtil.sourcePosition(sourceValue, nodeValue, visualOffser-1);
				int diff = sourseOffset-fo;
				boolean cicle=false;
				
				while ((fo + diff >= 0)&&(fo+diff<chars.length) && isTextToSkip(chars, fo + diff)) {
					diff--;
					cicle=true;
				}
				
				if(cicle==true){
					diff++;
				}

				if (fo + diff < 0||diff==0) {

					return moveCursorToPrevVisualNode(selection.getFocusNode());
				}

				setSourceFocus(so + fo + diff);
				return true;
			} else if (selection.getFocusNode().getNodeType() == Node.ELEMENT_NODE) {
				// Move to second position of the visual attribute (because we're placed 
				//   either before first char of visual attribute or not in visual attribute 
				//   but still in the beginning of the element

				return moveCursorToPrevVisualNode(selection.getFocusNode());
			}
		}
		
		return false;
	}
	
	private boolean isTextToSkip(char[] chars, int position) {
		try {
			if (chars[position] == ' ')
				return true;
			
			if (TextUtil.isWhitespaceText(new String(chars, 0, position + 1)) ||
					TextUtil.isWhitespaceText(new String(chars, position, chars.length - position))) {
				return true;
			}
			
			return false;
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return true;
		}
	}
	
	
	boolean moveCursorToNextVisualNode(Node node) {
		Node next = node;
		while (next != null) {
			next = getNextFlatNode(next);
			if (next == null) {
				// Fix for last char in last text node
				setSourceFocus(((IndexedRegion)node.getOwnerDocument()).getEndOffset());
				return true;
			}
			if (next.getNodeType() == Node.TEXT_NODE) {
				int so = ((IndexedRegion)next).getStartOffset();
				int shift = 0;
				char[] chars = ((TextImpl)next).getValueSource().toCharArray();
				while ((shift < chars.length) && isTextToSkip(chars, shift)) {
					shift++;
				}
				if (shift < chars.length) {
					so += shift;
				}
				setSourceFocus(so);
				return true;
			} else if (next.getNodeType() == Node.ELEMENT_NODE) {
				Attr attr = getVisualNodeSourceAttribute(next);
				if (attr != null) {
					int attrSelStart = ((ElementImpl)attr.getOwnerElement()).getStartOffset() + ((AttrImpl)attr).getValueRegion().getStart() + 1;
					setSourceFocus(attrSelStart);
					return true;
				}
				Node visualNode = domMapping.getVisualNode(next);
				if (visualNode != null && "input".equalsIgnoreCase(visualNode.getNodeName())) {
					setSourceFocus(((IndexedRegion)next).getStartOffset());
					return true;
				}
			}
		}
		return true;
	}

	boolean moveCursorToPrevVisualNode(Node node) {
		Node prev = node;
		while (prev != null) {
			prev = getPreviousFlatNode(prev);
			if (prev == null) {
				// Fix for first char in first text node
				setSourceFocus(0);
				return true;
			}
			if (prev.getNodeType() == Node.TEXT_NODE) {
				int eo = ((IndexedRegion)prev).getEndOffset();
				int shift = 0;
				char[] chars = ((TextImpl)prev).getValueSource().toCharArray();
				while ((shift < chars.length) && isTextToSkip(chars, chars.length - shift - 1)) {
					shift++;
				}
				if (shift < chars.length) {
					eo -= shift;
				}
				setSourceFocus(eo);
				return true;
			} else if (prev.getNodeType() == Node.ELEMENT_NODE) {
				Attr attr = getVisualNodeSourceAttribute(prev);
				if (attr != null) {
					int attrSelStart = ((ElementImpl)attr.getOwnerElement()).getStartOffset() + ((AttrImpl)attr).getValueRegion().getStart() + 1;
					int attrSelLength = ((AttrImpl)attr).getValue().length();
					setSourceFocus(attrSelStart + attrSelLength - 1);
					return true;
				}
				Node visualNode = domMapping.getVisualNode(prev);
				if (visualNode != null && "input".equalsIgnoreCase(visualNode.getNodeName())) {
					setSourceFocus(((IndexedRegion)prev).getStartOffset());
					return true;
				}
			}
		}
		return true;
	}

	private boolean moveBack() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(sourceEditor);
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection != null && selection.isCollapsed()) {
			Node node = getPrevNode(selection.getStartNode());
			if (node != null) {
				int nodeOffset = ((IndexedRegion)node).getStartOffset();
				pageContext.getSourceBuilder().getStructuredTextViewer().setSelectedRange(nodeOffset, 0);
				pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(nodeOffset, 0);
				return true;
			}
		}
		return false;
	}
	
	private Node _getNextNode(Node node) {
		Node next = null;
		do {
			next = node.getNextSibling();
			node = node.getParentNode();
		} while (next == null && node != null);
		return next;
	}
	
	private Node getNextNode(Node node) {
		Node next = null;
		while (node != null && next == null) {
			next = getNextSibling(node);
			node = node.getParentNode();
		}
		while (next != null && next.getNodeType() == Node.ELEMENT_NODE) {
			Node child = getFirstChild(next);
			if (child != null) {
				next = child;
			} else {
				break;
			}
		}
		return next;
	}
	
	private Node getNextSibling(Node node) {
		while (node != null) {
			node = node.getNextSibling();
			if (node == null || domMapping.getVisualNode(node) != null) {
				return node;
			}
		}
		return null;
	}
	
	private Node getFirstChild(Node node) {
		node = node.getFirstChild();
		while (node != null && domMapping.getVisualNode(node) == null) {
			node = node.getNextSibling();
		}
		return node;
	}
	
	private Node getPrevNode(Node node) {
		Node prev = null;
		while (node != null && prev == null) {
			prev = getPrevSibling(node);
			node = node.getParentNode();
		}
		while (prev != null && prev.getNodeType() == Node.ELEMENT_NODE) {
			Node child = getLastChild(prev);
			if (child != null) {
				prev = child;
			} else {
				break;
			}
		}
		return prev;
	}
	
	private Node getPrevSibling(Node node) {
		while (node != null) {
			node = node.getPreviousSibling();
			if (node == null || domMapping.getVisualNode(node) != null) {
				return node;
			}
		}
		return null;
	}
	
	private Node getLastChild(Node node) {
		node = node.getLastChild();
		while (node != null && domMapping.getVisualNode(node) == null) {
			node = node.getPreviousSibling();
		}
		return node;
	}

	private boolean moveUp(boolean extend) {
		frameSelection.lineMove(false, extend);
		return true;
	}

	private boolean moveDown(boolean extend) {
		frameSelection.lineMove(true, extend);
		return true;
	}

	private boolean moveHome(boolean extend) {
		frameSelection.intraLineMove(false, extend);
		return true;
	}

	private boolean moveEnd(boolean extend) {
		frameSelection.intraLineMove(true, extend);
		return true;
	}

	private boolean moveLeft(boolean extend) {
		frameSelection.characterMove(false, extend);
		return true;
	}

	private boolean moveRight(boolean extend) {
		frameSelection.characterMove(true, extend);
		return true;
	}
}
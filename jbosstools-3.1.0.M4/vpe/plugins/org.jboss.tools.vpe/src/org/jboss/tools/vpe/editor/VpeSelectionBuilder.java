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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.CommentImpl;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.template.VpePseudoContentCreator;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;
import org.mozilla.interfaces.nsISelectionDisplay;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *	
 *	@deprecated 
 *
 */
public class VpeSelectionBuilder {
	private static int HUGE_DISTANCE = 999999;
	private VpeDomMapping domMapping;
	private VpeSourceDomBuilder sourceBuilder;
	private VpeVisualDomBuilder visualBuilder;
	// TODO Sergey Vasilyev figure out with press shell and selection controller
//	private nsIPresShell presShell;
	private VpeSelectionController visualSelectionController;

	VpeSelectionBuilder(VpeDomMapping domMapping, VpeSourceDomBuilder sourceBuilder, VpeVisualDomBuilder visualBuilder, VpeSelectionController visualSelectionController) {	
//	VpeSelectionBuilder(VpeDomMapping domMapping, VpeSourceDomBuilder sourceBuilder, VpeVisualDomBuilder visualBuilder, nsIPresShell presShell, nsISelectionController visualSelectionController) {
		this.domMapping = domMapping;
		this.sourceBuilder = sourceBuilder;
		this.visualBuilder = visualBuilder;
		// TODO Sergey Vasilyev figure out with selection controller and press shell
//		this.presShell = presShell;
		this.visualSelectionController = visualSelectionController;
		visualSelectionController.setSelectionFlags(nsISelectionDisplay.DISPLAY_ALL);
	}

	void setVisualSelection(Node sourceNode, int caretPosition) {
		setSelection(sourceNode, caretPosition, false);
	}
	
	public void setSelection(nsISelection selection) {
		
//		VpeTemplate vpeTemplate = SelectionUtil
//				.getTemplateByVisualSelection(visualBuilder.getPageContext(),
//						SelectionUtil.getSelectedNode(selection));
//		if (vpeTemplate instanceof ITemplateSelectionManager) {
//			((ITemplateSelectionManager) vpeTemplate).setSelection(
//					visualBuilder.getPageContext(), selection);
//			return;
//		}
		
		if (selection.getIsCollapsed()) {
			VisualSelectionInfo info = getVisualFocusSelectedInfo(selection);
			if (info != null) {
				nsIDOMNode visualNode = info.node;
				Node node = domMapping.getSourceNode(visualNode);
				Node sourceNode;
				if (node == null) {
					sourceNode = domMapping.getNearSourceNode(visualNode);
				} else {
					sourceNode = node;
				}
				nsIDOMNode visualSelectedNode = domMapping.getVisualNode(sourceNode);
				if (visualSelectedNode == null) {
					visualSelectedNode = visualNode;
				}
				if (VpeDebug.PRINT_VISUAL_SELECTION_EVENT) {
					System.out.println("      visualNode: " + visualSelectedNode.getNodeName() + "(" + visualSelectedNode + ")  sourceNode: " + (sourceNode == null ? null : sourceNode.getNodeName()) + "  node: " + node); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
				if (sourceNode != null) {
					switch (visualSelectedNode.getNodeType()) {
					case nsIDOMNode.TEXT_NODE:
						nsIDOMElement visualParentElement = (nsIDOMElement)visualSelectedNode.getParentNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
						visualBuilder.setSelectionRectangle(visualParentElement, false);
//						int pos = DataHelper.textPos(visualSelectedNode.getNodeValue(), selection.getFocusOffset());
						int pos = selection.getFocusOffset();
						
						try{
							IndexedRegion region = (IndexedRegion)sourceNode;
							String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
							pos = TextUtil.sourcePosition(text, visualSelectedNode.getNodeValue(), selection.getFocusOffset());
						}catch(BadLocationException ex){
							VpePlugin.reportProblem(ex);
						}
						sourceBuilder.setSelection(sourceNode, pos, 0);
						break;
					case nsIDOMNode.ELEMENT_NODE:
						if (VpeVisualDomBuilder.isIncludeElement((nsIDOMElement)visualSelectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))) {
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
							visualSelectionController.setCaretEnabled(false);
							sourceBuilder.setSelection(sourceNode, 0, 0);
						} else if (sourceNode.getNodeType() == Node.COMMENT_NODE) {
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
//							pos = DataHelper.textPos(visualNode.getNodeValue(), selection.getFocusOffset());
							pos = selection.getFocusOffset();
							try{
								IndexedRegion region = (IndexedRegion)sourceNode;
								String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
								pos = TextUtil.sourcePosition(text, visualSelectedNode.getNodeValue(), selection.getFocusOffset());
							}catch(BadLocationException ex){
								VpePlugin.reportProblem(ex);
							}
							sourceBuilder.setSelection(sourceNode, pos, 0);
						} else if (visualBuilder.isContentArea(visualSelectedNode) && visualBuilder.isEmptyDocument()) {
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
							sourceBuilder.setSelectionAtDocumentEnd();
						} else {
							nsIDOMNode containerForPseudoContent = VpePseudoContentCreator.getContainerForPseudoContent(visualNode);
							if (containerForPseudoContent != null) {
								sourceNode = domMapping.getNearSourceNode(containerForPseudoContent);
								visualBuilder.setSelectionRectangle((nsIDOMElement)containerForPseudoContent.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
								sourceBuilder.setSelection(sourceNode, 0, 0, true);
								visualSelectionController.setCaretEnabled(false);
							} else {
								boolean border = false;
								if(domMapping.getNodeMapping(visualSelectedNode) instanceof VpeElementMapping){
									VpeElementMapping element = (VpeElementMapping)domMapping.getNodeMapping(visualSelectedNode);
									if(element.isBorder(visualNode)){
										info.startFlag = true;
										border = true;
									}
								}
								
								if (!border && visualNode.getNodeType() == Node.TEXT_NODE && node == null) {
									visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
									sourceBuilder.setAttributeSelection(visualNode, selection.getFocusOffset(), 0);
									if (!visualBuilder.isTextEditable(visualNode)) {
										visualSelectionController.setCaretEnabled(false);
									}
								} else {
									if (info.startFlag) {
										visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
									} else {
										visualParentElement = (nsIDOMElement)visualSelectedNode.getParentNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
										visualBuilder.setSelectionRectangle(visualParentElement, false);
									}
									int offset = info.startFlag ? 0 : ((IndexedRegion)sourceNode).getEndOffset() -
											((IndexedRegion)sourceNode).getStartOffset();
									if (info.startFlag && info.isPseudoElement()) {
										sourceBuilder.setSelection(sourceNode, 0, 0, true);
									} else {
										sourceBuilder.setSelection(sourceNode, offset, 0);
									}
								}
							}
						}
						break;
					}
				}
			}
		} else {
			nsIDOMRange range = selection.getRangeAt(0);
			nsIDOMNode visualAncestor = range.getCommonAncestorContainer();
			
			Node sourceAncestor = domMapping.getNearSourceNode(visualAncestor);
			nsIDOMNode visualSelectedAncestor = domMapping.getVisualNode(sourceAncestor);
			
			if (visualSelectedAncestor == null) {
				visualSelectedAncestor = visualAncestor;
			}

			boolean border = false;
			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(visualSelectedAncestor);
			
			if (visualSelectedAncestor.getNodeType() == Node.ELEMENT_NODE){
				if (nodeMapping != null && (nodeMapping instanceof VpeElementMapping)  && ((VpeElementMapping)nodeMapping).isBorder(visualAncestor)){
					visualSelectedAncestor = ((VpeElementMapping)nodeMapping).getBorder();
					border = true;
				}
			}
			
			if (sourceAncestor != null) {
				switch (visualSelectedAncestor.getNodeType()) {
				case Node.TEXT_NODE:
					nsIDOMElement visualParentElement = (nsIDOMElement)visualSelectedAncestor.getParentNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
					visualBuilder.setSelectionRectangle(visualParentElement, false);
					
//					int start = DataHelper.textPos(visualSelectedAncestor.getNodeValue(), selection.getAnchorOffset());
//					int end = DataHelper.textPos(visualSelectedAncestor.getNodeValue(), selection.getFocusOffset());
					int start = selection.getAnchorOffset();
					int end = selection.getFocusOffset();
					try{
						IndexedRegion region = (IndexedRegion)sourceAncestor;
						String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
						start = TextUtil.sourcePosition(text, visualSelectedAncestor.getNodeValue(), selection.getAnchorOffset());
						end = TextUtil.sourcePosition(text, visualSelectedAncestor.getNodeValue(), selection.getFocusOffset());
					}catch(BadLocationException ex){
						VpePlugin.reportProblem(ex);
					}
					
					sourceBuilder.setSelection(sourceAncestor, start, end - start);
					break;
				case Node.ELEMENT_NODE:
					if (sourceAncestor.getNodeType() == Node.COMMENT_NODE) {
						visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedAncestor.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
//						start = DataHelper.textPos(sourceAncestor.getNodeValue(), selection.getAnchorOffset());
//						end = DataHelper.textPos(sourceAncestor.getNodeValue(), selection.getFocusOffset());
						start = selection.getAnchorOffset();
						end = selection.getFocusOffset();
						try{
							IndexedRegion region = (IndexedRegion)sourceAncestor;
							String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
							start = TextUtil.sourcePosition(text, sourceAncestor.getNodeValue(), selection.getAnchorOffset());
							end = TextUtil.sourcePosition(text, sourceAncestor.getNodeValue(), selection.getFocusOffset());
						}catch(BadLocationException ex){
							VpePlugin.reportProblem(ex);
						}
						sourceBuilder.setSelection(sourceAncestor, start, end - start);
					} else { 
						nsIDOMNode visualAnchorNode = selection.getAnchorNode();
						int visualAnchorOffset = selection.getAnchorOffset();
						nsIDOMNode visualFocusNode = selection.getFocusNode();
						
						int visualFocusOffset = selection.getFocusOffset();
						if (visualFocusNode.equals(visualAnchorNode) && visualFocusOffset - visualAnchorOffset == 1 && visualFocusNode.getNodeType() == Node.ELEMENT_NODE) {
							VisualSelectionInfo info = getVisualSelectedInfo(visualAnchorNode, visualAnchorOffset);
							if (info != null) {
								nsIDOMNode visualNode = info.node;
								visualBuilder.setSelectionRectangle((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
								Node sourceNode = domMapping.getNearSourceNode(visualNode);
								int sourceStartOffset = ((IndexedRegion)sourceNode).getStartOffset();
								int sourceEndOffset = ((IndexedRegion)sourceNode).getEndOffset();
								sourceBuilder.setSelection(sourceNode, 0, sourceEndOffset - sourceStartOffset);
							}
						} else {
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualSelectedAncestor.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID), false);
	
							if (!border && visualAncestor.getNodeType() == Node.TEXT_NODE) {
								sourceBuilder.setAttributeSelection(visualAncestor, visualAnchorOffset, visualFocusOffset - visualAnchorOffset);
							} else {
								int sourceAncestorOffset = ((IndexedRegion)sourceAncestor).getStartOffset();
								int sourceAnchorOffset = getSourceOffset(visualAnchorNode, visualAnchorOffset);
								int sourceFocusOffset = getSourceOffset(visualFocusNode, visualFocusOffset);
								sourceBuilder.setSelection(sourceAncestor, sourceAnchorOffset - sourceAncestorOffset, sourceFocusOffset - sourceAnchorOffset);
							}
						}
					}
					break;
				}
			}
		}
	}
	
	private int getSourceOffset(nsIDOMNode visualPrmNode, int visualPrmOffset) {
		VisualSelectionInfo info = getVisualSelectedInfo(visualPrmNode, visualPrmOffset);
		if (info != null) {
			nsIDOMNode visualNode = info.node;
			Node sourceNode = domMapping.getNearSourceNode(visualNode);
			if (sourceNode != null) {
				switch (sourceNode.getNodeType()) {
				case Node.TEXT_NODE:
					try{
						IndexedRegion region = (IndexedRegion)sourceNode;
						int start = region.getStartOffset();
						String sourceText = sourceBuilder.getStructuredTextViewer().getDocument().get(start, region.getEndOffset() - start);
						if (visualPrmNode.getNodeType() == Node.TEXT_NODE) {
							return start + TextUtil.sourcePosition(sourceText, visualNode.getNodeValue(), visualPrmOffset);
						} else if (info.startFlag) {
							return start;
						} else {
							String visualValue = visualNode.getNodeValue();
							return start + TextUtil.sourcePosition(sourceText, visualValue, visualValue.length());
						}
					} catch(BadLocationException ex){
						VpePlugin.reportProblem(ex);
						return 0;
					}
				case Node.ELEMENT_NODE:
					return info.startFlag ? ((IndexedRegion)sourceNode).getStartOffset() : ((IndexedRegion)sourceNode).getEndOffset();
				case Node.COMMENT_NODE:
					return ((IndexedRegion)sourceNode).getStartOffset() + visualPrmOffset + 4;
				}
			}
		}
		return 0;
	}
	
	void setClickSelection(nsIDOMNode visualNode) {
		Node sourceNode = domMapping.getNearSourceNode(visualNode);
		nsIDOMNode visualSelectedNode = domMapping.getVisualNode(sourceNode);
		if (visualSelectedNode == null) {
			visualSelectedNode = visualNode;
		}
		setVisualSelectionAtVisualNode(visualSelectedNode, 0);
		sourceBuilder.setSelection(sourceNode, 0, 0);
	}
	
	void _setClickContentAreaSelection() {
		Node sourceNode = sourceBuilder.getSelectedNode();
		if (sourceNode != null) {
			int caretPosition = sourceBuilder.getCaretPosition();
			setSelection(sourceNode, caretPosition, true);
		}
	}
	
	void setClickContentAreaSelection() {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		setSelection(selection);
	}
	
	void setClickContentAreaSelection(nsIDOMMouseEvent mouseEvent) {
//		Node visualNode = mouseEvent.getTargetNode();
//		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
//		Node anchorNode = selection.getAnchorNode();
//		Node focusNode = selection.getFocusNode();
	}
	
	Node setContextMenuSelection(nsIDOMNode visualNode) {
		if (VpeDebug.PRINT_VISUAL_CONTEXTMENU_EVENT) {
			System.out.println(">>>>>>>>>>>>>> onShowContextMenu  visualNode: " + visualNode.getNodeName() + "(" + visualNode + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		visualSelectionController.setCaretEnabled(false);
		
		nsIDOMNode visualParentNode = visualNode.getParentNode();
		if (visualParentNode.getNodeType() == Node.DOCUMENT_NODE) {
			visualNode = visualBuilder.getContentArea();
		}
		Node selectedText = getSelectedTextOnly(visualNode);
		if (selectedText != null) {
			return selectedText;
		} else {
			return setContextMenuElementSelection(visualNode);
		}
	}
	
	private Node setContextMenuElementSelection(nsIDOMNode visualNode) {
		Node sourceNode = domMapping.getNearSourceNode(visualNode);
		nsIDOMNode visualSelectedNode = domMapping.getVisualNode(sourceNode);
		if (visualSelectedNode == null) {
			visualSelectedNode = visualNode;
		}
		setVisualSelectionAtVisualNode(visualSelectedNode, 0);
		//added by Max Areshkau in scope of JBIDE-1209
		if(sourceNode.getNodeType()!=Node.TEXT_NODE) {
				sourceBuilder.setSelection(sourceNode, 0, 0);
		} else if(sourceNode.getNodeType()==Node.TEXT_NODE) {
			IndexedRegion region = (IndexedRegion)sourceNode;
			sourceBuilder.setSelection(sourceNode, 0, region.getLength());
		}
		return sourceNode;
	}
	
	private Node getSelectedTextOnly(nsIDOMNode element) {
		Node selectedText = null;
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		if (!selection.getIsCollapsed()) {
			nsIDOMNode anchorNode = selection.getAnchorNode();
			nsIDOMNode focusNode = selection.getFocusNode();
			if (anchorNode != null && anchorNode.getNodeType() == Node.TEXT_NODE && anchorNode.equals(focusNode)) {
				nsIDOMNode anchorParent = anchorNode.getParentNode();
				if (anchorParent != null) {
					if (anchorParent.equals(element)) {
						selectedText = domMapping.getSourceNode(anchorNode);
					}
				}
			}
		}
		return selectedText;
	}
	
	private void setVisualSelectionAtVisualNode(nsIDOMNode visualNode, int offset) {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		if (visualNode != null) {
			switch (visualNode.getNodeType()) {
			case nsIDOMNode.TEXT_NODE:
				if (offset > visualNode.getNodeValue().length()) {
					offset = visualNode.getNodeValue().length();
				}
				selection.collapse(visualNode, offset);
				nsIDOMElement visualParentElement = (nsIDOMElement) visualNode.getParentNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				visualBuilder.setSelectionRectangle(visualParentElement);
				break;
			case nsIDOMNode.ELEMENT_NODE:
				Node node = domMapping.getSourceNode(visualNode);
				if (node != null && node.getNodeType() == Node.COMMENT_NODE) {
					nsIDOMNodeList visualNodes = visualNode.getChildNodes();
					long len = visualNodes.getLength();
					if (len > 0) {
						nsIDOMNode visualText = visualNodes.item(0);
						String text = visualText.getNodeValue();
						if (offset >  text.length()) {
							offset = text.length();
						}
						selection.collapse(visualText, offset);
						visualBuilder.setSelectionRectangle((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
					}
				} else {
					if (offset == 0) {
						offset = (int)VisualDomUtil.getOffset(visualNode);
						nsIDOMNode visualParentNode = visualNode.getParentNode();
						if (visualParentNode != null && visualParentNode.getNodeType() == Node.ELEMENT_NODE) {
							selection.collapse(visualParentNode, offset);
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
						} else {
							selection.removeAllRanges();
							visualBuilder.setSelectionRectangle(null);
						}
					} else if (offset == 1) {
						nsIDOMNode appreciableVisualChild = VpeVisualDomBuilder.getLastAppreciableVisualChild(visualNode);
						if (appreciableVisualChild != null) {
							if (appreciableVisualChild.getNodeType() == nsIDOMNode.TEXT_NODE) {
								offset = appreciableVisualChild.getNodeValue().length();
								selection.collapse(appreciableVisualChild, offset);
								visualBuilder.setSelectionRectangle((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
							} else {
								offset = (int)VisualDomUtil.getOffset(appreciableVisualChild) + 1;
								selection.collapse((nsIDOMNode)visualNode, offset);
								visualBuilder.setSelectionRectangle((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
							}
						} else {
							offset = 0;
							selection.collapse(visualNode, offset);
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
						}
					} else {
						offset = (int)VisualDomUtil.getOffset(visualNode);
						nsIDOMNode visualParentNode = visualNode.getParentNode();
						if (visualParentNode.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
							selection.collapse(visualParentNode, offset);
							visualBuilder.setSelectionRectangle((nsIDOMElement)visualParentNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
						} else {
							selection.removeAllRanges();
							visualBuilder.setSelectionRectangle(null);
						}
					}
				}
				break;
			default:
				selection.removeAllRanges();
				visualBuilder.setSelectionRectangle(null);
			}
		} else {
			selection.removeAllRanges();
			visualBuilder.setSelectionRectangle(null);
		}
	}
	
	private VisualSelectionInfo getVisualFocusSelectedInfo(nsISelection selection) {
		nsIDOMNode focusNode = selection.getFocusNode();
		if (focusNode != null && focusNode.getNodeType() == Node.TEXT_NODE) {
			nsIDOMNode parent = focusNode.getParentNode();
			if (parent == null) {
				return null;
			}
		}
		VisualSelectionInfo info = getVisualSelectedInfo(focusNode, selection.getFocusOffset());
		return info;
	}
	
	private VisualSelectionInfo getVisualSelectedInfo(nsIDOMNode visualNode, int visualOffset) {
		if (visualNode != null) {
			switch (visualNode.getNodeType()) {
			case nsIDOMNode.TEXT_NODE:
				return new VisualSelectionInfo(visualNode);
			case nsIDOMNode.ELEMENT_NODE:
				nsIDOMNodeList visualNodes = visualNode.getChildNodes();
				nsIDOMNode visualChild;
				boolean startFlag = true;
				long len = visualNodes.getLength();
				if (visualOffset < len) {
					visualChild = visualNodes.item(visualOffset);
					if (visualOffset > 0 && visualChild != null && visualChild.getNodeType() == Node.TEXT_NODE) {
						nsIDOMNode visualPrevChild = visualNodes.item(visualOffset - 1);
						if (visualPrevChild != null && visualPrevChild.getNodeType() != Node.TEXT_NODE) {
							visualChild = visualPrevChild;
							startFlag = false;
						}
					}
				} else if (len > 0) {
					visualChild = visualNodes.item(len - 1);
					startFlag = false;
				} else {
					visualChild = visualNode;
				}
				return new VisualSelectionInfo(visualChild, startFlag);
			}
		}
		return null;
	}

	private void setSelection(Node sourceNode, int caretPosition, boolean showCaret) {
		nsIDOMNode visualNode = domMapping.getNearVisualNode(sourceNode);
		int startOffset =  ((IndexedRegion)sourceNode).getStartOffset();
		int endOffset =  ((IndexedRegion)sourceNode).getEndOffset();
		if (caretPosition >= startOffset && caretPosition <= endOffset) {
			int offset = caretPosition - ((IndexedRegion)sourceNode).getStartOffset();
			if (sourceNode.getNodeType() == Node.TEXT_NODE && offset > sourceNode.getNodeValue().length()) {
				offset = sourceNode.getNodeValue().length();
			}
			if (!showCaret) {
				visualSelectionController.setCaretEnabled(false);
			}
			setVisualSelectionAtVisualNode(visualNode, offset);
		}
	}

	public void setVisualSelection(Node sourceAnchorNode, int sourceAnchorOffset, Node sourceFocusNode, int sourceFocusOffset, boolean reversionFlag, boolean showCaret) {
		setVisualSelection(sourceAnchorNode, sourceAnchorOffset, sourceFocusNode, sourceFocusOffset, reversionFlag, showCaret, false);
	}

	public void setVisualSelection(Node sourceAnchorNode, int sourceAnchorOffset, Node sourceFocusNode, int sourceFocusOffset, boolean reversionFlag, boolean showCaret, boolean into) {
//		if (sourceAnchorNode.getNodeType() == Node.TEXT_NODE) {
//			sourceAnchorOffset = DataHelper.focusPos(sourceAnchorNode.getNodeValue(), sourceAnchorOffset);
//		}
//		if (sourceFocusNode.getNodeType() == Node.TEXT_NODE) {
//			sourceFocusOffset = DataHelper.focusPos(sourceFocusNode.getNodeValue(), sourceFocusOffset);
//		}
		if (!showCaret) {
			visualSelectionController.setCaretEnabled(false);
		}
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		if (sourceAnchorNode == null || sourceFocusNode == null) {
			selection.removeAllRanges();
			visualBuilder.setSelectionRectangle(null);
		} else if (sourceAnchorNode == sourceFocusNode && sourceAnchorOffset == sourceFocusOffset) {
			nsIDOMNode visualNode = domMapping.getNearVisualNode(sourceAnchorNode);
			if (visualNode != null) {
				if (sourceAnchorNode.getNodeType() == Node.TEXT_NODE) {
					if (visualNode.getNodeType() != Node.TEXT_NODE) {
						sourceAnchorOffset = 1;
					} else if (sourceAnchorOffset > sourceAnchorNode.getNodeValue().length()) {
						sourceAnchorOffset = sourceAnchorNode.getNodeValue().length();
					}
				}
				if (into && visualNode != null && visualNode.getChildNodes().getLength() == 1) {
					nsIDOMNode br = VisualDomUtil.getChildNode(visualNode, 0);
					if (HTML.TAG_BR.equalsIgnoreCase(br.getNodeName())) {
						visualNode = VisualDomUtil.getChildNode(visualNode, 0);
						sourceAnchorOffset = 2;
					}
				}
			} else if (sourceAnchorNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				visualNode = visualBuilder.getOutputTextNode((Attr)sourceAnchorNode);
				if (visualNode != null) {
					if (!visualBuilder.isTextEditable(visualNode)) {
						sourceAnchorOffset = 0;
					}
				} else {
					visualNode = domMapping.getNearVisualNode(((Attr)sourceAnchorNode).getOwnerElement());
					sourceAnchorOffset = 0;
				}
			}
			// comment selection was changed
			if(sourceAnchorNode instanceof CommentImpl){
				sourceAnchorOffset=0;
			}
			setVisualSelectionAtVisualNode(visualNode, sourceAnchorOffset);
		} else {
			nsIDOMNode visualAnchorNode = null;
			nsIDOMNode visualFocusNode = null;
			boolean anchorStartFlag = false;
			boolean focusStartFlag = false;
			boolean anchorDirectFlag = true;
			boolean focusDirectFlag = true;

			if (!reversionFlag) {
				VisualSelectionInfo anchorInfo = getStartSelectionInfo(sourceAnchorNode, sourceAnchorOffset == 0);
				if (anchorInfo != null) {
					visualAnchorNode = anchorInfo.node;
					anchorStartFlag = anchorInfo.startFlag;
					anchorDirectFlag = anchorInfo.directFlag;
				}
				VisualSelectionInfo focusInfo = getEndSelectionInfo(sourceFocusNode, sourceFocusOffset == 0);
				if (focusInfo != null) {
					visualFocusNode = focusInfo.node;
					focusStartFlag = focusInfo.startFlag;
					focusDirectFlag = focusInfo.directFlag;
				}
			} else {
				VisualSelectionInfo anchorInfo = getEndSelectionInfo(sourceAnchorNode, sourceAnchorOffset == 0);
				if (anchorInfo != null) {
					visualAnchorNode = anchorInfo.node;
					anchorStartFlag = anchorInfo.startFlag;
					anchorDirectFlag = anchorInfo.directFlag;
				}
				VisualSelectionInfo focusInfo = getStartSelectionInfo(sourceFocusNode, sourceFocusOffset == 0);
				if (focusInfo != null) {
					visualFocusNode = focusInfo.node;
					focusStartFlag = focusInfo.startFlag;
					focusDirectFlag = focusInfo.directFlag;
				}
			}

			if (visualAnchorNode == null || visualFocusNode == null) {
				selection.removeAllRanges();
				visualBuilder.setSelectionRectangle(null);
				return;
			}

			nsIDOMNode visualAnchorContainer = null;
			long visualAnchorOffset = 0;
			if (visualAnchorNode.getNodeType() == Node.TEXT_NODE) {
				visualAnchorContainer = visualAnchorNode;
				if (anchorDirectFlag && sourceAnchorNode.getNodeType() == Node.TEXT_NODE) {
					if (sourceAnchorOffset > sourceAnchorNode.getNodeValue().length()) {
						visualAnchorOffset = sourceAnchorNode.getNodeValue().length();
					} else {
						visualAnchorOffset = sourceAnchorOffset;
					}
					if (visualAnchorOffset > visualAnchorNode.getNodeValue().length()) {
						visualAnchorOffset = visualAnchorNode.getNodeValue().length();
					}
				} else {
					if (anchorStartFlag) {
						visualAnchorOffset = 0;
					} else {
						visualAnchorOffset = visualAnchorNode.getNodeValue().length();
					}
				}
			} else if (sourceAnchorNode.getNodeType() == Node.COMMENT_NODE) {
				nsIDOMNodeList visualNodes = visualAnchorNode.getChildNodes();
				long len = visualNodes.getLength();
				if (len > 0) {
					visualAnchorContainer = visualNodes.item(0); 
					String text = visualAnchorContainer.getNodeValue();
					if (text!=null&& sourceAnchorOffset <= text.length()) {
						visualAnchorOffset = sourceAnchorOffset;
					} else if (text!=null) {
						visualAnchorOffset = text.length();
					}
				}
			} else {
				visualAnchorContainer = visualAnchorNode.getParentNode();
				visualAnchorOffset = VisualDomUtil.getOffset(visualAnchorNode);
				if (!anchorStartFlag) visualAnchorOffset++;
			}
			if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
				System.out.println("setVisualSelection"); //$NON-NLS-1$
				System.out.println("                     visualAnchorNode: " + visualAnchorNode.getNodeName() + "(" + visualAnchorNode + ")  visualAnchorContainer: " + visualAnchorContainer.getNodeName() + "(" + visualAnchorContainer + ")  visualAnchorOffset: " + visualAnchorOffset +  "  anchorStartFlag: " + anchorStartFlag); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			}
			
			nsIDOMNode visualFocusContainer = null;
			long visualFocusOffset = 0;
			if (visualFocusNode.getNodeType() == Node.TEXT_NODE) {
				visualFocusContainer = visualFocusNode;
				if (focusDirectFlag && sourceFocusNode.getNodeType() == Node.TEXT_NODE) {
					if (sourceFocusOffset > sourceFocusNode.getNodeValue().length()) {
						visualFocusOffset = sourceFocusNode.getNodeValue().length();
					} else {
						visualFocusOffset = sourceFocusOffset;
					}
					if (visualFocusOffset > visualFocusNode.getNodeValue().length()) {
						visualFocusOffset = visualFocusNode.getNodeValue().length();
					}
				} else {
					if (focusStartFlag) {
						visualFocusOffset = 0;
					} else {
						visualFocusOffset = visualFocusNode.getNodeValue().length();
					}
				}
			} else if (sourceFocusNode.getNodeType() == Node.COMMENT_NODE) {
				nsIDOMNodeList visualNodes = visualFocusNode.getChildNodes();
				long len = visualNodes.getLength();
				if (len > 0) {
					visualFocusContainer = visualNodes.item(0); 
					String text = visualFocusContainer.getNodeValue();
					if (text!=null&&sourceFocusOffset <= text.length()) {
						visualFocusOffset = sourceFocusOffset;
					} else if(text!=null) {
						visualFocusOffset = text.length();
					}
				}
			} else {
				visualFocusContainer = visualFocusNode.getParentNode();
				visualFocusOffset = VisualDomUtil.getOffset(visualFocusNode);
				if (!focusStartFlag) visualFocusOffset++;
			}
			if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
				System.out.println("                     visualFocusNode: " + //$NON-NLS-1$
						(visualFocusNode != null ? 
								visualFocusNode.getNodeName() + "(" + visualFocusNode + ")" : null) +  //$NON-NLS-1$ //$NON-NLS-2$
						"  visualFocusContainer: " + //$NON-NLS-1$
						(visualFocusContainer != null ?
								visualFocusContainer.getNodeName() + "(" + visualFocusContainer + ") visualFocusOffset: " + visualFocusOffset + "  focusStartFlag: " + focusStartFlag : null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
// Ed tmp
if (visualAnchorContainer == null || visualFocusContainer == null) {
	return;
}
			
			if (visualAnchorContainer.equals((visualFocusContainer)) && visualAnchorOffset == visualFocusOffset) {
				if (!reversionFlag) {
					setVisualSelectionAtVisualNode(visualFocusNode, 0);
				} else {
					setVisualSelectionAtVisualNode(visualAnchorNode, 0);
				}
			} else {
				selection.collapse(visualAnchorContainer, (int)visualAnchorOffset);
				selection.extend(visualFocusContainer, (int)visualFocusOffset);
				nsIDOMElement commonElement = getVisualCommonElement(visualAnchorNode, visualFocusNode);
				visualBuilder.setSelectionRectangle(commonElement);
				if (commonElement != null && !commonElement.equals(visualAnchorNode) && !commonElement.equals(visualFocusNode) &&
						!commonElement.equals(visualAnchorContainer) && !commonElement.equals(visualFocusContainer)) {
				}
			}
		}
	}
	
	private VisualSelectionInfo getStartSelectionInfo(Node sourceNode, boolean initialStartFlag) {
		boolean startFlag = initialStartFlag;
		nsIDOMNode visualNode = domMapping.getVisualNode(sourceNode);
		boolean directFlag = visualNode != null;
		while (sourceNode != null && visualNode == null) {
			Node sourcePrevNode = sourceNode.getPreviousSibling();
			if (sourcePrevNode != null) {
				sourceNode = sourcePrevNode;
				startFlag = false;
			} else {
				sourceNode = sourceNode.getParentNode();
				startFlag = true;
			}
			visualNode = domMapping.getVisualNode(sourceNode);
		}
		if (visualNode != null) {
			return new VisualSelectionInfo(visualNode, startFlag, directFlag);
		} else {
			return null;
		}
	}
	
	private VisualSelectionInfo getEndSelectionInfo(Node sourceNode, boolean initialStartFlag) {
		boolean startFlag = initialStartFlag;
		nsIDOMNode visualNode = domMapping.getVisualNode(sourceNode);
		boolean directFlag = visualNode != null;
		while (sourceNode != null && visualNode == null) {
			Node sourceNextNode = sourceNode.getNextSibling();
			if (sourceNextNode != null) {
				sourceNode = sourceNextNode;
				startFlag = true;
			} else {
				sourceNode = sourceNode.getParentNode();
				startFlag = false;
			}
			visualNode = domMapping.getVisualNode(sourceNode);
		}
		if (visualNode != null) {
			return new VisualSelectionInfo(visualNode, startFlag, directFlag);
		} else {
			return null;
		}
	}
	
	private nsIDOMElement getVisualCommonElement(nsIDOMNode nodeA, nsIDOMNode nodeB) {
		nsIDOMNode[] nodesA = getVisualPath(nodeA);
		nsIDOMNode[] nodesB = getVisualPath(nodeB);
		int len = Math.min(nodesA.length, nodesB.length);
		int index = len;
		for (int i = 0; i < len; i++) {
			if (!nodesA[i].equals(nodesB[i])) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			index = len;
		}

		if (index > 0) {
			nsIDOMNode commonNode = nodesA[index - 1];
			if (commonNode.getNodeType() == Node.TEXT_NODE) {
				nsIDOMNode parent = commonNode.getParentNode();
				commonNode = parent;
			}
			return (nsIDOMElement)commonNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		} else {
			return null;
		}
	}
	
	private nsIDOMNode[] getVisualPath(nsIDOMNode node) {
		return getVisualPath(node, 0);
	}
	
	private nsIDOMNode[] getVisualPath(nsIDOMNode node, int height) {
		height++;
		nsIDOMNode[] path;
		nsIDOMNode parent = node.getParentNode();
		if (parent != null) {
			path = getVisualPath(parent, height);
		} else {
			path = new nsIDOMNode[height];
		}
		path[path.length - height] = node;
		return path;
	}

	private static class VisualSelectionInfo {
		nsIDOMNode node;
		boolean startFlag;
		boolean directFlag;
		
		private VisualSelectionInfo(nsIDOMNode node) {
			this.node = node;
		}
		
		private VisualSelectionInfo(nsIDOMNode node, boolean startFlag) {
			this(node, startFlag, true);
		}
		
		private VisualSelectionInfo(nsIDOMNode node, boolean startFlag, boolean directFlag) {
			this(node);
			this.startFlag = startFlag;
			this.directFlag = directFlag;
		}
		
		private boolean isPseudoElement() {
			return VpeVisualDomBuilder.isPseudoElement(node);
		}
	}
	
	public VpeVisualCaretInfo getVisualCaretInfo(nsIDOMEvent event) {
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent) event.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		
		return new VpeVisualCaretInfo(this, nsuiEvent.getRangeParent(), nsuiEvent.getRangeOffset());
	}
	
	void showVisualDragCaret(nsIDOMNode node, int offset) {
		visualBuilder.showDragCaret(node, offset);
	}
	
	void hideVisualDragCaret() {
		visualBuilder.hideDragCaret();
	}
	
	int getSourcePosition(nsIDOMNode visualInitNode, int visualInitOffset) {
		int position = 0;
		VisualSelectionInfo info = getVisualSelectedInfo(visualInitNode, visualInitOffset);
		if (info != null) {
			nsIDOMNode visualNode = info.node;
			Node sourceNode = domMapping.getNearSourceNode(visualNode);
			nsIDOMNode visualSelectedNode = domMapping.getVisualNode(sourceNode);
			if (visualSelectedNode == null) {
				visualSelectedNode = visualNode;
			}
			if (sourceNode != null) {
				switch (visualSelectedNode.getNodeType()) {
				case Node.TEXT_NODE:
					if (visualInitNode.getNodeType() != Node.TEXT_NODE) {
						visualInitOffset = info.startFlag ? 0 : visualSelectedNode.getNodeValue().length(); 
					}
//					int ofset = DataHelper.textPos(visualSelectedNode.getNodeValue(), visualInitOffset);
					int ofset = visualInitOffset;
					position = sourceBuilder.getPosition(sourceNode, ofset, false);

					
					try{
						IndexedRegion region = (IndexedRegion)sourceNode;
						int start = region.getStartOffset();
						String text = sourceBuilder.getStructuredTextViewer().getDocument().get(start, region.getEndOffset() - start);
						position = start + TextUtil.sourcePosition(text, visualSelectedNode.getNodeValue(), visualInitOffset);
					}catch(BadLocationException ex){
						VpePlugin.reportProblem(ex);
					}
					
					
					
					break;
				case Node.ELEMENT_NODE:
					int offset = info.startFlag ? 0 : ((IndexedRegion)sourceNode).getEndOffset() -
							((IndexedRegion)sourceNode).getStartOffset();
					if (info.startFlag && info.isPseudoElement()) {
						position = sourceBuilder.getPosition(sourceNode, 0, true);
					} else {
						position = sourceBuilder.getPosition(sourceNode, offset, false);
					}
					break;
				}
			}
		}
		return position;
	}
	
	Point getSourceSelectionRangeAtVisualNode(nsIDOMNode visualInitNode, int visualInitOffset) {
		if (visualInitNode.getNodeType() == Node.TEXT_NODE) {
			Node sourceNode = domMapping.getSourceNode(visualInitNode);
			if (sourceNode == null) {
				sourceNode = domMapping.getNearSourceNode(visualInitNode);
				if (sourceNode != null && sourceNode.getNodeType() == Node.ELEMENT_NODE) {
					Point outputAttributesPositions = sourceBuilder.getOutputAttributesPositions((Element)sourceNode);
					if (outputAttributesPositions != null) {
						return new Point(outputAttributesPositions.x, outputAttributesPositions.y - outputAttributesPositions.x);
					}
				}
			}
		}
		Point range = sourceBuilder.getSelectionRange();
		int pos = getSourcePosition(visualInitNode, visualInitOffset);
		if (pos >= range.x && pos <= range.x + range.y) {
			return range;
		} else {
			return new Point(pos, 0);
		}
	}

	Point getSourceSelectionRange(Node sourceInitNode, int sourceInitOffset) {
		int offset=0;
		int position=0;
		switch (sourceInitNode.getNodeType()) {
		case Node.TEXT_NODE:
			offset = Math.min(sourceInitOffset, sourceInitNode.getNodeValue().length());
			position = sourceBuilder.getPosition(sourceInitNode, offset, false);
			break;
		case Node.ELEMENT_NODE:
		case Node.DOCUMENT_NODE:
			NodeList children = sourceInitNode.getChildNodes();
			int count = children.getLength();
			if (sourceInitOffset < count) {
				Node sourceNode = children.item(sourceInitOffset);
				position = ((IndexedRegion)sourceNode).getStartOffset();
			} else if (count > 0) {
				Node sourceNode = children.item(count - 1);
				position = ((IndexedRegion)sourceNode).getEndOffset();
			} else {
				position = ((IndexedRegion)sourceInitNode).getStartOffset();
				if (sourceInitNode instanceof ElementImpl) {
			 		ElementImpl element = (ElementImpl)sourceInitNode;
			 		if (element.isContainer()) {
			 			position  = element.getStartEndOffset();
			 		}
				}
			}
			break;
		}
		return new Point(position, offset);
	}
	
	nsIDOMNode getOriginalTargetNode(nsIDOMEvent event) {
		nsIDOMNode targetNode = VisualDomUtil.getTargetNode(event);
		if (HTML.TAG_INPUT.equalsIgnoreCase(targetNode.getNodeName())) {
			return targetNode;
		}
		nsIDOMNSEvent nsEvent = (nsIDOMNSEvent) event.queryInterface(nsIDOMNSEvent.NS_IDOMNSEVENT_IID);
		// TODO Sergey Vasilyev figure out with TmpRealOriginalTarget
//		nsIDOMEventTarget target = nsEvent.getTmpRealOriginalTarget();	
		nsIDOMEventTarget target = nsEvent.getOriginalTarget();
		nsIDOMNode originalNode = (nsIDOMNode) target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
		if (VpeVisualDomBuilder.isAnonElement(originalNode)) {
			originalNode = visualBuilder.getLastSelectedElement(); 
		}
		return originalNode;
	}
	
	public VpeVisualInnerDropInfo getInnerDropInfo(nsIDOMEvent event) {
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent) event.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		nsIDOMNode dropContainer = null;
		int dropOffset = 0;
		int mouseX = nsuiEvent.getPageX();
		int mouseY = nsuiEvent.getPageY();
		nsIDOMNode originalNode = getOriginalTargetNode(event);
		if (originalNode == null || originalNode.getParentNode() == null ||
				originalNode.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
			return  new VpeVisualInnerDropInfo(null, 0, mouseX, mouseY);
		}
		if (originalNode.getNodeType() == Node.TEXT_NODE) {
			dropContainer = nsuiEvent.getRangeParent();
			nsIDOMNode containerForPseudoContent = VpePseudoContentCreator.getContainerForPseudoContent(dropContainer);
			if (containerForPseudoContent != null) {
				dropContainer = containerForPseudoContent;
				dropOffset = 0;
			} else {
				dropOffset = nsuiEvent.getRangeOffset();
			}
		} else {
		    int closestXDistance = HUGE_DISTANCE;
		    int closestYDistance = HUGE_DISTANCE;
		    boolean inNodeFlag = false;
		    nsIDOMNode closestNode = null;

		    nsIDOMNodeList childen = originalNode.getChildNodes();
			long count = childen.getLength();
			for (long i = 0; i < count; i++) {
				nsIDOMNode child = childen.item(i);
				if (VpeVisualDomBuilder.isPseudoElement(child) || VpeVisualDomBuilder.isAnonElement(child)) {
					continue;
				}
				Rectangle rect = visualBuilder.getNodeBounds(child);
				int fromTop = mouseY - rect.y;
				int fromBottom = mouseY - rect.y - rect.height;

				int yDistance;
				if (fromTop > 0 && fromBottom < 0) {
					yDistance = 0;
				} else {
					yDistance = Math.min(Math.abs(fromTop), Math.abs(fromBottom));
				}
			      
				if (yDistance <= closestYDistance && rect.width > 0 && rect.height > 0) {
					if (yDistance < closestYDistance) {
						closestXDistance = HUGE_DISTANCE;
					}
					int fromLeft = mouseX - rect.x;
					int fromRight = mouseX - rect.x - rect.width;

					int xDistance;
					if (fromLeft > 0 && fromRight < 0) {
						xDistance = 0;
					} else {
						xDistance = Math.min(Math.abs(fromLeft), Math.abs(fromRight));
					}
					if (xDistance == 0 && yDistance == 0) {
						closestNode = child;
						inNodeFlag = true;
						break;
					}

					if (xDistance < closestXDistance || (xDistance == closestXDistance && rect.x <= mouseX)) {
						closestXDistance = xDistance;
						closestYDistance = yDistance;
						closestNode = child;
					}
				}
			}
			
			if (closestNode == null) {
				closestNode = originalNode;
				inNodeFlag = true;
			}
			if (inNodeFlag) {
				if (closestNode.getNodeType() == Node.TEXT_NODE) {
					dropContainer = nsuiEvent.getRangeParent();
					dropOffset = nsuiEvent.getRangeOffset();
				} else {
					if (HTML.TAG_COLGROUP.equalsIgnoreCase(closestNode.getNodeName())) {
						nsIDOMNode nearChild = getNearChild(closestNode, mouseX, mouseY);
						if (nearChild != null && nearChild.getNodeType() == Node.ELEMENT_NODE) {
							dropContainer = nearChild;
						} else {
							dropContainer = closestNode;
						}
					} else {
						dropContainer = closestNode;
					}
					dropOffset = 0;
				}
			} else {
				dropContainer = closestNode.getParentNode();
				dropOffset = (int)VisualDomUtil.getOffset(closestNode);
				Rectangle rect = visualBuilder.getNodeBounds(closestNode);
				if (VpeVisualDomBuilder.canInsertAfter(mouseX, mouseY, rect)) {
					dropOffset++;
				}
			}
		}
		VpeVisualInnerDropInfo info = new VpeVisualInnerDropInfo(dropContainer, dropOffset, mouseX, mouseY);
		return info;
	}
	
	private nsIDOMNode getNearChild(nsIDOMNode container, int x, int y) {
	    int closestXDistance = HUGE_DISTANCE;
	    int closestYDistance = HUGE_DISTANCE;
	    nsIDOMNode closestNode = null;

	    nsIDOMNodeList childen = container.getChildNodes();
		long count = childen.getLength();
		for (long i = 0; i < count; i++) {
			nsIDOMNode child = childen.item(i);
			if (VpeVisualDomBuilder.isPseudoElement(child) || VpeVisualDomBuilder.isAnonElement(child)) {
				continue;
			}
			Rectangle rect = visualBuilder.getNodeBounds(child);
			int fromTop = y - rect.y;
			int fromBottom = y - rect.y - rect.height;

			int yDistance;
			if (fromTop > 0 && fromBottom < 0) {
				yDistance = 0;
			} else {
				yDistance = Math.min(Math.abs(fromTop), Math.abs(fromBottom));
			}
		      
			if (yDistance <= closestYDistance && rect.width > 0 && rect.height > 0) {
				if (yDistance < closestYDistance) {
					closestXDistance = HUGE_DISTANCE;
				}
				int fromLeft = x - rect.x;
				int fromRight = x - rect.x - rect.width;

				int xDistance;
				if (fromLeft > 0 && fromRight < 0) {
					xDistance = 0;
				} else {
					xDistance = Math.min(Math.abs(fromLeft), Math.abs(fromRight));
				}
				if (xDistance == 0 && yDistance == 0) {
					closestNode = child;
					break;
				}
				if (xDistance < closestXDistance || (xDistance == closestXDistance && rect.x <= x)) {
					closestXDistance = xDistance;
					closestYDistance = yDistance;
					closestNode = child;
				}
			}
		}
		return closestNode;
	}
	
	void setVisualElementSelection(nsIDOMMouseEvent mouseEvent) {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		nsIDOMNode visualNode = getOriginalTargetNode(mouseEvent);
		if (selection.containsNode(visualNode, false) || VpeVisualDomBuilder.isAnonElement(visualNode)) {
			return;
		}
		if (!selection.containsNode((nsIDOMNode)visualNode, false)) {
			if (visualNode.getNodeType() == Node.ELEMENT_NODE && !VpeVisualDomBuilder.isAnonElement(visualNode)) {
				nsIDOMNode visualParent = visualNode.getParentNode();
				long offset = VisualDomUtil.getOffset(visualNode);
				selection.removeAllRanges();
				selection.collapse(visualParent, (int)offset);
				selection.extend(visualParent, (int)offset + 1);
				setSelection(selection);
			}
		}

	}

	nsIDOMElement getDragElement(nsIDOMMouseEvent mouseEvent) {
		nsIDOMElement dragElement = visualBuilder.getDragElement(mouseEvent);
		if (dragElement != null) {
			return dragElement;
		}

		nsIDOMNode visualNode = VisualDomUtil.getTargetNode(mouseEvent);
		//FIX FOR JBIDE-1468 added by Sergey Dzmitrovich
		if (visualNode != null
				&& visualNode.getNodeType() == Node.ELEMENT_NODE
				&& (HTML.TAG_INPUT.equalsIgnoreCase(visualNode.getNodeName())
						|| HTML.TAG_OPTION.equalsIgnoreCase(visualNode
								.getNodeName())
						|| HTML.TAG_BUTTON.equalsIgnoreCase(visualNode
								.getNodeName()) || HTML.TAG_SELECT
						.equalsIgnoreCase(visualNode.getNodeName()))
				/*&& !selection.containsNode(visualNode, false)*/
				&& visualBuilder.canInnerDrag((nsIDOMElement) visualNode
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))) {
			return (nsIDOMElement) visualNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		}
		return null;
	}
	
	nsIDOMElement getAppropriateElementForSelection(nsIDOMEvent event) {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		nsIDOMNode visualNode = VisualDomUtil.getTargetNode(event);
		if (visualNode != null) { 
			if (!HTML.TAG_INPUT.equalsIgnoreCase(visualNode.getNodeName())) {
				visualNode = getOriginalTargetNode(event);
			}
			if (visualNode != null && visualNode.getNodeType() == Node.ELEMENT_NODE && !selection.containsNode((nsIDOMNode)visualNode, false)) {
				VpeElementMapping elementMapping = domMapping.getNearElementMapping(visualNode);
				if (elementMapping != null) {
					return elementMapping.getVisualElement();
				}
			}
		}
		return null;
	}
	
	void setVisualElementSelection(nsIDOMElement visualElement) {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		visualElement.removeAttribute(XulRunnerEditor.VPEFLASHERCOLORATTRIBUTE);
		nsIDOMNode visualParent = visualElement.getParentNode();
		int offset = (int) VisualDomUtil.getOffset(visualElement);
		selection.removeAllRanges();
		selection.collapse(visualParent, offset);
		selection.extend(visualParent, offset + 1);
		
		setSelection(selection);
	}
	
	VpeVisualInnerDragInfo getInnerDragInfo(nsIDOMMouseEvent event) {
		VpeVisualInnerDragInfo info = null;
	
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		nsIDOMNode focusNode = selection.getFocusNode();
		nsIDOMNode anchorNode = selection.getAnchorNode();
		//when we select input this function return null
		//but we select elemnt
		if(focusNode==null && anchorNode==null) {
		
			nsIDOMNode  visualNode =(nsIDOMNode) event.getTarget().queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
			//fix of JBIDE-1097
			if(HTML.TAG_SPAN.equalsIgnoreCase(visualNode.getNodeName()))
			{
				if(visualBuilder.getXulRunnerEditor().getLastSelectedElement()!=null&&!visualBuilder.getNodeBounds(visualBuilder.getXulRunnerEditor().getLastSelectedElement()).contains(VisualDomUtil.getMousePoint(event))){
					return null;
				}
			}
			int offset = (int) VisualDomUtil.getOffset(visualNode);
			selection.removeAllRanges();
			selection.collapse(visualNode.getParentNode(), offset);
			try {
			selection.extend(visualNode.getParentNode(), offset + 1);
			} catch(XPCOMException ex) {
				//just ignore exception
				// throws when we trying drag element which already resizing
				return null;
			}
			focusNode = selection.getFocusNode();
			anchorNode = selection.getAnchorNode();
		}
		if (focusNode != null && focusNode.equals(anchorNode)) {
			int focusOffset = selection.getFocusOffset();
			int anchorOffset = selection.getAnchorOffset();
			int offset = Math.min(focusOffset, anchorOffset);
			int length = Math.max(focusOffset, anchorOffset) - offset;

			switch (focusNode.getNodeType()) {
			case nsIDOMNode.ELEMENT_NODE:
				if (length == 1) {
					nsIDOMNodeList children = focusNode.getChildNodes();
					nsIDOMNode selectedNode = children.item(Math.min(focusOffset, anchorOffset));
					if (visualBuilder.getNodeBounds(selectedNode).contains(VisualDomUtil.getMousePoint(event))) {
						switch(selectedNode.getNodeType()) {
						case nsIDOMNode.ELEMENT_NODE:
							info = new VpeVisualInnerDragInfo((nsIDOMElement)selectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
							break;
						case nsIDOMNode.TEXT_NODE:
							info = new VpeVisualInnerDragInfo(selectedNode, 0, selectedNode.getNodeValue().length());
							break;
						}
					}
				}
				break;
			case nsIDOMNode.TEXT_NODE:
				info = new VpeVisualInnerDragInfo(focusNode, offset, length);
				break;
			}
		}
		return info;
	}
	
	void setMouseUpSelection(nsIDOMMouseEvent mouseEvent) {
	}
	
	void printVisualSelection() {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		boolean collapsed = selection.getIsCollapsed();
		System.out.println("  ## VisualSelection"); //$NON-NLS-1$
		System.out.println("  ## collapsed: " + collapsed); //$NON-NLS-1$
		if (!collapsed) {
			nsIDOMNode anchorNode = selection.getAnchorNode();
			if (anchorNode != null) {
				System.out.println("  ## anchorNode: " + anchorNode.getNodeName() + " (" + anchorNode + ")  offset: " + selection.getAnchorOffset()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				System.out.println("  ## anchorNode: null"); //$NON-NLS-1$
			}
		}
		nsIDOMNode focusNode = selection.getFocusNode();
		if (focusNode != null) {
			System.out.println("  ## focusNode: " + focusNode.getNodeName() + " (" + focusNode + ")  offset: " + selection.getFocusOffset()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			System.out.println("  ## focusNode: null"); //$NON-NLS-1$
		}
	}
	
	void setCaretAtMouse(nsIDOMMouseEvent mouseEvent) {
		nsIDOMNode visualNode = VisualDomUtil.getTargetNode(mouseEvent);
		boolean isAnonElement = VpeVisualDomBuilder.isAnonElement(visualNode);
		if (isAnonElement) return;
		VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(mouseEvent);
		nsIDOMNode visuaDropContainer = visualDropInfo.getDropContainer();
		if (visuaDropContainer.getNodeType() == Node.TEXT_NODE) return;
		int visualDropOffset = (int)visualDropInfo.getDropOffset();
		if (visualDropOffset > 0) {
			nsIDOMNodeList visualChildren = visuaDropContainer.getChildNodes();
			nsIDOMNode visualChild = visualChildren.item(visualDropOffset - 1);
			boolean isText = visualChild.getNodeType() == Node.TEXT_NODE;
			if (isText) return;
		}
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		if (!VisualDomUtil.isSelectionContains(selection, visuaDropContainer, visualDropOffset)) {
			setVisualCaret(visuaDropContainer, visualDropOffset);
			// TODO Sergey Vasilyev figure out with nsIFrameSelection
//			nsIFrameSelection frameSelection = presShell.getFrameSelection();
//			if (frameSelection != null) {
//				frameSelection.setMouseDownState(true);
//			}
			
			// was commented by Max Areshkau (with this  code scrolling doesn't works)
			// 
//			mouseEvent.preventDefault();
//			mouseEvent.stopPropagation();
		}
	}
	
	void setVisualCaret(nsIDOMNode visualNode, int offset) {
		nsISelection selection = visualSelectionController.getSelection(nsISelectionController.SELECTION_NORMAL);
		if (visualNode != null) {
			if (visualNode.getNodeType() == Node.TEXT_NODE && offset > visualNode.getNodeValue().length()) {
				offset = visualNode.getNodeValue().length();
			}
			selection.collapse(visualNode, offset);
		} else {
			selection.removeAllRanges();
		}
		visualSelectionController.setCaretEnabled(true);
		setSelection(selection);
	}
}

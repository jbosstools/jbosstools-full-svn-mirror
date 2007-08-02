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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.CommentImpl;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpePoint;
import org.jboss.tools.vpe.editor.template.VpePseudoContentCreator;
import org.jboss.tools.vpe.editor.util.MozillaSupports;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.DataHelper;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEventTarget;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNSEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNSUIEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNode;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMRange;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIFrameSelection;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIPresShell;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelection;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelectionController;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelectionDisplay;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISupports;

public class VpeSelectionBuilder {
	private static int HUGE_DISTANCE = 999999;
	private VpeDomMapping domMapping;
	private VpeSourceDomBuilder sourceBuilder;
	private VpeVisualDomBuilder visualBuilder;
	private nsIPresShell presShell;
	private nsISelectionController visualSelectionController;
	
	VpeSelectionBuilder(VpeDomMapping domMapping, VpeSourceDomBuilder sourceBuilder, VpeVisualDomBuilder visualBuilder, nsIPresShell presShell, nsISelectionController visualSelectionController) {
		this.domMapping = domMapping;
		this.sourceBuilder = sourceBuilder;
		this.visualBuilder = visualBuilder;
		this.presShell = presShell;
		this.visualSelectionController = visualSelectionController;
//		visualSelectionController.setSelectionFlags(nsISelectionDisplay.DISPLAY_TEXT);
//		visualSelectionController.setSelectionFlags((short)(nsISelectionDisplay.DISPLAY_TEXT + nsISelectionDisplay.DISPLAY_IMAGES));
//		visualSelectionController.setSelectionFlags(nsISelectionDisplay.DISPLAY_FRAMES);
		visualSelectionController.setSelectionFlags(nsISelectionDisplay.DISPLAY_ALL);
	}

	void setVisualSelection(Node sourceNode, int caretPosition) {
		setSelection(sourceNode, caretPosition, false);
	}
	
	public void setSelection(nsISelection selection) {
		if (selection.isCollapsed()) {
			VisualSelectionInfo info = getVisualFocusSelectedInfo(selection);
			if (info != null) {
				Node visualNode = info.node;
				Node node = domMapping.getSourceNode(visualNode);
				Node sourceNode;
				if (node == null) {
					sourceNode = domMapping.getNearSourceNode(visualNode);
				} else {
					sourceNode = node;
				}
				Node visualSelectedNode = domMapping.getVisualNode(sourceNode);
				if (visualSelectedNode == null) {
					visualSelectedNode = visualNode;
				}
				if (VpeDebug.printVisualSelectionEvent) {
					System.out.println("      visualNode: " + visualSelectedNode.getNodeName() + "(" + MozillaSupports.getAddress(visualSelectedNode) + ")(" + MozillaSupports.getRefCount(visualSelectedNode) + ")  sourceNode: " + (sourceNode == null ? null : sourceNode.getNodeName()) + "  node: " + node);
				}
				if (sourceNode != null) {
					switch (visualSelectedNode.getNodeType()) {
					case Node.TEXT_NODE:
						Element visualParentElement = (Element)visualSelectedNode.getParentNode();
						visualBuilder.setSelectionRectangle(visualParentElement, false);
						MozillaSupports.release(visualParentElement);
						int pos = DataHelper.textPos(visualSelectedNode.getNodeValue(), selection.getFocusOffset());
						
						try{
							IndexedRegion region = (IndexedRegion)sourceNode;
							String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
							pos = TextUtil.sourcePosition(text, visualSelectedNode.getNodeValue(), selection.getFocusOffset());
						}catch(Exception ex){
							VpePlugin.reportProblem(ex);
						}
						sourceBuilder.setSelection(sourceNode, pos, 0);
						break;
					case Node.ELEMENT_NODE:
						if (VpeVisualDomBuilder.isIncludeElement((Element)visualSelectedNode)) {
							visualBuilder.setSelectionRectangle((Element)visualSelectedNode, false);
							visualSelectionController.SetCaretEnabled(false);
							sourceBuilder.setSelection(sourceNode, 0, 0);
						} else if (sourceNode.getNodeType() == Node.COMMENT_NODE) {
							visualBuilder.setSelectionRectangle((Element)visualSelectedNode, false);
							pos = DataHelper.textPos(visualNode.getNodeValue(), selection.getFocusOffset());
							try{
								IndexedRegion region = (IndexedRegion)sourceNode;
								String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
								pos = TextUtil.sourcePosition(text, visualSelectedNode.getNodeValue(), selection.getFocusOffset());
							}catch(Exception ex){
								VpePlugin.reportProblem(ex);
							}
							sourceBuilder.setSelection(sourceNode, pos, 0);
						} else if (visualBuilder.isContentArea(visualSelectedNode) && visualBuilder.isEmptyDocument()) {
							visualBuilder.setSelectionRectangle((Element)visualSelectedNode, false);
							sourceBuilder.setSelectionAtDocumentEnd();
						} else {
							Node containerForPseudoContent = VpePseudoContentCreator.getContainerForPseudoContent(visualNode);
							if (containerForPseudoContent != null) {
								sourceNode = domMapping.getNearSourceNode(containerForPseudoContent);
								visualBuilder.setSelectionRectangle((Element)containerForPseudoContent, false);
								MozillaSupports.release(containerForPseudoContent);
								sourceBuilder.setSelection(sourceNode, 0, 0, true);
								visualSelectionController.SetCaretEnabled(false);
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
									visualBuilder.setSelectionRectangle((Element)visualSelectedNode, false);
									sourceBuilder.setAttributeSelection(visualNode, selection.getFocusOffset(), 0);
									if (!visualBuilder.isTextEditable(visualNode)) {
										visualSelectionController.SetCaretEnabled(false);
									}
								} else {
									if (info.startFlag) {
										visualBuilder.setSelectionRectangle((Element)visualSelectedNode, false);
									} else {
										visualParentElement = (Element)visualSelectedNode.getParentNode();
										visualBuilder.setSelectionRectangle(visualParentElement, false);
										MozillaSupports.release(visualParentElement);
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
				MozillaSupports.release(visualNode);
			}
		} else {
			nsIDOMRange range = selection.getRangeAt(0);
			Node visualAncestor = range.getCommonAncestorContainer();
			
			Node sourceAncestor = domMapping.getNearSourceNode(visualAncestor);
			Node visualSelectedAncestor = domMapping.getVisualNode(sourceAncestor);
			
			if (visualSelectedAncestor == null) {
				visualSelectedAncestor = visualAncestor;
			}

			boolean border = false;
			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(visualSelectedAncestor);
			
			if (visualSelectedAncestor.getNodeType() == Node.ELEMENT_NODE){
				if (nodeMapping != null && nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING && ((VpeElementMapping)nodeMapping).isBorder(visualAncestor)){
					visualSelectedAncestor = ((VpeElementMapping)nodeMapping).getBorder();
					border = true;
				}
			}
			
			if (sourceAncestor != null) {
				switch (visualSelectedAncestor.getNodeType()) {
				case Node.TEXT_NODE:
					Element visualParentElement = (Element)visualSelectedAncestor.getParentNode();
					visualBuilder.setSelectionRectangle(visualParentElement, false);
					MozillaSupports.release(visualParentElement);
					
					int start = DataHelper.textPos(visualSelectedAncestor.getNodeValue(), selection.getAnchorOffset());
					int end = DataHelper.textPos(visualSelectedAncestor.getNodeValue(), selection.getFocusOffset());
					try{
						IndexedRegion region = (IndexedRegion)sourceAncestor;
						String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
						start = TextUtil.sourcePosition(text, visualSelectedAncestor.getNodeValue(), selection.getAnchorOffset());
						end = TextUtil.sourcePosition(text, visualSelectedAncestor.getNodeValue(), selection.getFocusOffset());
					}catch(Exception ex){
						VpePlugin.reportProblem(ex);
					}
					
					sourceBuilder.setSelection(sourceAncestor, start, end - start);
					break;
				case Node.ELEMENT_NODE:
					if (sourceAncestor.getNodeType() == Node.COMMENT_NODE) {
						visualBuilder.setSelectionRectangle((Element)visualSelectedAncestor, false);
						start = DataHelper.textPos(sourceAncestor.getNodeValue(), selection.getAnchorOffset());
						end = DataHelper.textPos(sourceAncestor.getNodeValue(), selection.getFocusOffset());
						try{
							IndexedRegion region = (IndexedRegion)sourceAncestor;
							String text = sourceBuilder.getStructuredTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
							start = TextUtil.sourcePosition(text, sourceAncestor.getNodeValue(), selection.getAnchorOffset());
							end = TextUtil.sourcePosition(text, sourceAncestor.getNodeValue(), selection.getFocusOffset());
						}catch(Exception ex){
							VpePlugin.reportProblem(ex);
						}
						sourceBuilder.setSelection(sourceAncestor, start, end - start);
					} else { 
						Node visualAnchorNode = selection.getAnchorNode();
						int visualAnchorOffset = selection.getAnchorOffset();
						Node visualFocusNode = selection.getFocusNode();
						
						int visualFocusOffset = selection.getFocusOffset();
						if (visualFocusNode.equals(visualAnchorNode) && visualFocusOffset - visualAnchorOffset == 1 && visualFocusNode.getNodeType() == Node.ELEMENT_NODE) {
							VisualSelectionInfo info = getVisualSelectedInfo(visualAnchorNode, visualAnchorOffset);
							if (info != null) {
								Node visualNode = info.node;
								visualBuilder.setSelectionRectangle((Element)visualNode, false);
								Node sourceNode = domMapping.getNearSourceNode(visualNode);
								int sourceStartOffset = ((IndexedRegion)sourceNode).getStartOffset();
								int sourceEndOffset = ((IndexedRegion)sourceNode).getEndOffset();
								sourceBuilder.setSelection(sourceNode, 0, sourceEndOffset - sourceStartOffset);
								MozillaSupports.release(visualNode);
							}
						} else {
							visualBuilder.setSelectionRectangle((Element)visualSelectedAncestor, false);
	
							if (!border && visualAncestor.getNodeType() == Node.TEXT_NODE) {
								sourceBuilder.setAttributeSelection(visualAncestor, visualAnchorOffset, visualFocusOffset - visualAnchorOffset);
							} else {
								int sourceAncestorOffset = ((IndexedRegion)sourceAncestor).getStartOffset();
								int sourceAnchorOffset = getSourceOffset(visualAnchorNode, visualAnchorOffset);
								int sourceFocusOffset = getSourceOffset(visualFocusNode, visualFocusOffset);
								sourceBuilder.setSelection(sourceAncestor, sourceAnchorOffset - sourceAncestorOffset, sourceFocusOffset - sourceAnchorOffset);
							}
						}
						MozillaSupports.release(visualAnchorNode, visualFocusNode);
					}
					break;
				}
			}
			if (!border) {
				MozillaSupports.release(visualAncestor);
			}
			range.Release();
		}
	}
	
	private int getSourceOffset(Node visualPrmNode, int visualPrmOffset) {
		VisualSelectionInfo info = getVisualSelectedInfo(visualPrmNode, visualPrmOffset);
		if (info != null) {
			Node visualNode = info.node;
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
					} catch(Exception ex){
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
	
	void setClickSelection(Node visualNode) {
		Node sourceNode = domMapping.getNearSourceNode(visualNode);
		Node visualSelectedNode = domMapping.getVisualNode(sourceNode);
		if (visualSelectedNode == null) {
			visualSelectedNode = visualNode;
		}
		setVisualSelectionAtVisualNode(visualSelectedNode, 0);
		sourceBuilder.setSelection(sourceNode, 0, 0);
	}
	
	void setClickSelection(nsIDOMMouseEvent mouseEvent) {
		Node visualNode = mouseEvent.getTargetNode();
		nsISelection selection = visualSelectionController.getSelection();
		Node anchorNode = selection.getAnchorNode();
		Node focusNode = selection.getFocusNode();
		if (focusNode != null) {
			
		}
	}
	
	void _setClickContentAreaSelection() {
		Node sourceNode = sourceBuilder.getSelectedNode();
		if (sourceNode != null) {
			int caretPosition = sourceBuilder.getCaretPosition();
			setSelection(sourceNode, caretPosition, true);
		}
	}
	
	void setClickContentAreaSelection() {
		nsISelection selection = visualSelectionController.getSelection();
		setSelection(selection);
	}
	
	void setClickContentAreaSelection(nsIDOMMouseEvent mouseEvent) {
//		Node visualNode = mouseEvent.getTargetNode();
//		nsISelection selection = visualSelectionController.getSelection();
//		Node anchorNode = selection.getAnchorNode();
//		Node focusNode = selection.getFocusNode();
	}
	
	Node setContextMenuSelection(Node visualNode) {
		if (VpeDebug.printVisualContextMenuEvent) {
			System.out.println(">>>>>>>>>>>>>> onShowContextMenu  visualNode: " + visualNode.getNodeName() + "(" + MozillaSupports.getAddress(visualNode) + ")");
		}
		visualSelectionController.setCaretEnabled(false);
		
		Node visualParentNode = visualNode.getParentNode();
		if (visualParentNode.getNodeType() == Node.DOCUMENT_NODE) {
			visualNode = visualBuilder.getContentArea();
		}
		if (visualParentNode != null) {
			MozillaSupports.release(visualParentNode);
		}
		Node selectedText = getSelectedTextOnly(visualNode);
		if (selectedText != null) {
			return selectedText;
		} else {
			return setContextMenuElementSelection(visualNode);
		}
	}
	
	private Node setContextMenuElementSelection(Node visualNode) {
		Node sourceNode = domMapping.getNearSourceNode(visualNode);
		Node visualSelectedNode = domMapping.getVisualNode(sourceNode);
		if (visualSelectedNode == null) {
			visualSelectedNode = visualNode;
		}
		setVisualSelectionAtVisualNode(visualSelectedNode, 0);
		sourceBuilder.setSelection(sourceNode, 0, 0);
		return sourceNode;
	}
	
	private Node getSelectedTextOnly(Node element) {
		Node selectedText = null;
		nsISelection selection = visualSelectionController.getSelection();
		if (!selection.isCollapsed()) {
			Node anchorNode = selection.getAnchorNode();
			Node focusNode = selection.getFocusNode();
			if (anchorNode != null && anchorNode.getNodeType() == Node.TEXT_NODE && anchorNode.equals(focusNode)) {
				Node anchorParent = anchorNode.getParentNode();
				if (anchorParent != null) {
					if (anchorParent.equals(element)) {
						selectedText = domMapping.getSourceNode(anchorNode);
					}
					MozillaSupports.release(anchorParent);
				}
			}
			MozillaSupports.release(anchorNode, focusNode);
		}
		selection.Release();
		return selectedText;
	}
	
	private void setVisualSelectionAtVisualNode(Node visualNode, int offset) {
		nsISelection selection = visualSelectionController.getSelection();
		if (visualNode != null) {
			switch (visualNode.getNodeType()) {
			case Node.TEXT_NODE:
				if (offset > visualNode.getNodeValue().length()) {
					offset = visualNode.getNodeValue().length();
				}
				selection.collapse((nsIDOMNode)visualNode, offset);
				Element visualParentElement = (Element)visualNode.getParentNode();
				visualBuilder.setSelectionRectangle(visualParentElement);
				MozillaSupports.release(visualParentElement);
				break;
			case Node.ELEMENT_NODE:
				Node node = domMapping.getSourceNode(visualNode);
				if (node != null && node.getNodeType() == Node.COMMENT_NODE) {
					NodeList visualNodes = visualNode.getChildNodes();
					int len = visualNodes.getLength();
					if (len > 0) {
						Node visualText = visualNodes.item(0);
						String text = visualText.getNodeValue();
						if (offset >  text.length()) {
							offset = text.length();
						}
						selection.collapse((nsIDOMNode)visualText, offset);
						visualBuilder.setSelectionRectangle((Element)visualNode);
						MozillaSupports.release(visualText);
					}
					MozillaSupports.release(visualNodes);
				} else {
					if (offset == 0) {
						offset = MozillaSupports.getOffset(visualNode);
						Node visualParentNode = visualNode.getParentNode();
						if (visualParentNode != null && visualParentNode.getNodeType() == Node.ELEMENT_NODE) {
							selection.collapse((nsIDOMNode)visualParentNode, offset);
//							selection.collapse((nsIDOMNode)visualParentNode, offset + 1);
							visualBuilder.setSelectionRectangle((Element)visualNode);
//							visualBuilder.setSelectionRectangle((Element)visualParentNode);
						} else {
							selection.removeAllRanges();
							visualBuilder.setSelectionRectangle(null);
						}
					} else if (offset == 1) {
						Node appreciableVisualChild = VpeVisualDomBuilder.getLastAppreciableVisualChild(visualNode);
						if (appreciableVisualChild != null) {
							// Edward (bag ESP-290)
							if (appreciableVisualChild.getNodeType() == Node.TEXT_NODE) {
								offset = appreciableVisualChild.getNodeValue().length();
								selection.collapse((nsIDOMNode)appreciableVisualChild, offset);
								visualBuilder.setSelectionRectangle((Element)visualNode);
							} else {
								offset = MozillaSupports.getOffset(appreciableVisualChild) + 1;
								selection.collapse((nsIDOMNode)visualNode, offset);
								visualBuilder.setSelectionRectangle((Element)visualNode);
							}
						} else {
							offset = 0;
							selection.collapse((nsIDOMNode)visualNode, offset);
							visualBuilder.setSelectionRectangle((Element)visualNode);
						}
					} else {
						offset = MozillaSupports.getOffset(visualNode);
						Node visualParentNode = visualNode.getParentNode();
						if (visualParentNode.getNodeType() == Node.ELEMENT_NODE) {
							selection.collapse((nsIDOMNode)visualParentNode, offset);
							visualBuilder.setSelectionRectangle((Element)visualParentNode);
						} else {
							selection.removeAllRanges();
							visualBuilder.setSelectionRectangle(null);
						}
						MozillaSupports.release(visualParentNode);
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
		selection.Release();
	}
	
	private VisualSelectionInfo getVisualFocusSelectedInfo(nsISelection selection) {
		Node focusNode = selection.getFocusNode();
		if (focusNode != null && focusNode.getNodeType() == Node.TEXT_NODE) {
			Node parent = focusNode.getParentNode();
			if (parent == null) {
				return null;
			}
			MozillaSupports.release(parent);
		}
		VisualSelectionInfo info = getVisualSelectedInfo(focusNode, selection.getFocusOffset());
		return info;
	}
	
	private VisualSelectionInfo getVisualSelectedInfo(Node visualNode, int visualOffset) {
		if (visualNode != null) {
			switch (visualNode.getNodeType()) {
			case Node.TEXT_NODE:
				return new VisualSelectionInfo(visualNode);
			case Node.ELEMENT_NODE:
				NodeList visualNodes = visualNode.getChildNodes();
				Node visualChild;
				boolean startFlag = true;
				int len = visualNodes.getLength();
				if (visualOffset < len) {
					visualChild = visualNodes.item(visualOffset);
					if (visualOffset > 0 && visualChild != null && visualChild.getNodeType() == Node.TEXT_NODE) {
						Node visualPrevChild = visualNodes.item(visualOffset - 1);
						if (visualPrevChild != null && visualPrevChild.getNodeType() != Node.TEXT_NODE) {
							MozillaSupports.release(visualChild);
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
				MozillaSupports.release(visualNodes);
				return new VisualSelectionInfo(visualChild, startFlag);
			}
		}
		return null;
	}

	private void setSelection(Node sourceNode, int caretPosition, boolean showCaret) {
		Node visualNode = domMapping.getNearVisualNode(sourceNode);
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
		if (sourceAnchorNode.getNodeType() == Node.TEXT_NODE) {
			sourceAnchorOffset = DataHelper.focusPos(sourceAnchorNode.getNodeValue(), sourceAnchorOffset);
		}
		if (sourceFocusNode.getNodeType() == Node.TEXT_NODE) {
			sourceFocusOffset = DataHelper.focusPos(sourceFocusNode.getNodeValue(), sourceFocusOffset);
		}
		if (!showCaret) {
			visualSelectionController.setCaretEnabled(false);
		}
		nsISelection selection = visualSelectionController.getSelection();
		if (sourceAnchorNode == null || sourceFocusNode == null) {
			selection.removeAllRanges();
			visualBuilder.setSelectionRectangle(null);
		} else if (sourceAnchorNode == sourceFocusNode && sourceAnchorOffset == sourceFocusOffset) {
			Node visualNode = domMapping.getNearVisualNode(sourceAnchorNode);
			if (visualNode != null) {
				if (sourceAnchorNode.getNodeType() == Node.TEXT_NODE) {
					if (visualNode.getNodeType() != Node.TEXT_NODE) {
						sourceAnchorOffset = 1;
					} else if (sourceAnchorOffset > sourceAnchorNode.getNodeValue().length()) {
						sourceAnchorOffset = sourceAnchorNode.getNodeValue().length();
					}
				}
				if (into && visualNode != null && visualNode.getChildNodes().getLength() == 1) {
					Node br = MozillaSupports.getChildNode(visualNode, 0);
					if (br.getNodeName().equalsIgnoreCase("br")) {
						visualNode = MozillaSupports.getChildNode(visualNode, 0);
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
			Node visualAnchorNode = null;
			Node visualFocusNode = null;
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
				selection.Release();
				return;
			}

			Node visualAnchorContainer = null;
			int visualAnchorOffset = 0;
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
				NodeList visualNodes = visualAnchorNode.getChildNodes();
				int len = visualNodes.getLength();
				if (len > 0) {
					visualAnchorContainer = visualNodes.item(0); 
					String text = visualAnchorContainer.getNodeValue();
					if (sourceAnchorOffset <= text.length()) {
						visualAnchorOffset = sourceAnchorOffset;
					} else {
						visualAnchorOffset = text.length();
					}
				}
				MozillaSupports.release(visualNodes);
			} else {
				visualAnchorContainer = visualAnchorNode.getParentNode();
				visualAnchorOffset = MozillaSupports.getOffset(visualAnchorNode);
				if (!anchorStartFlag) visualAnchorOffset++;
			}
			if (VpeDebug.printSourceSelectionEvent) {
				System.out.println("setVisualSelection");
				System.out.println("                     visualAnchorNode: " + visualAnchorNode.getNodeName() + "(" + MozillaSupports.getAddress(visualAnchorNode) + ")  visualAnchorContainer: " + visualAnchorContainer.getNodeName() + "(" + MozillaSupports.getAddress(visualAnchorContainer) + ")  visualAnchorOffset: " + visualAnchorOffset +  "  anchorStartFlag: " + anchorStartFlag);
			}
			
			Node visualFocusContainer = null;
			int visualFocusOffset = 0;
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
				NodeList visualNodes = visualFocusNode.getChildNodes();
				int len = visualNodes.getLength();
				if (len > 0) {
					visualFocusContainer = visualNodes.item(0); 
					String text = visualFocusContainer.getNodeValue();
					if (sourceFocusOffset <= text.length()) {
						visualFocusOffset = sourceFocusOffset;
					} else {
						visualFocusOffset = text.length();
					}
				}
				MozillaSupports.release(visualNodes);
			} else {
				visualFocusContainer = visualFocusNode.getParentNode();
				visualFocusOffset = MozillaSupports.getOffset(visualFocusNode);
				if (!focusStartFlag) visualFocusOffset++;
			}
			if (VpeDebug.printSourceSelectionEvent) {
				System.out.println("                     visualFocusNode: " +
						(visualFocusNode != null ? 
								visualFocusNode.getNodeName() + "(" + MozillaSupports.getAddress(visualFocusNode) + ")" : null) + 
						"  visualFocusContainer: " +
						(visualFocusContainer != null ?
								visualFocusContainer.getNodeName() + "(" + MozillaSupports.getAddress(visualFocusContainer) + ") visualFocusOffset: " + visualFocusOffset + "  focusStartFlag: " + focusStartFlag : null));
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
				selection.collapse((nsIDOMNode)visualAnchorContainer, visualAnchorOffset);
				selection.extend((nsIDOMNode)visualFocusContainer, visualFocusOffset);
				Element commonElement = getVisualCommonElement(visualAnchorNode, visualFocusNode);
				visualBuilder.setSelectionRectangle(commonElement);
				if (commonElement != null && !commonElement.equals(visualAnchorNode) && !commonElement.equals(visualFocusNode) &&
						!commonElement.equals(visualAnchorContainer) && !commonElement.equals(visualFocusContainer)) {
					MozillaSupports.release(commonElement);
				}
			}
			if (visualAnchorContainer != visualAnchorNode) {
				MozillaSupports.release(visualAnchorContainer);
			}
			if (visualFocusContainer != visualFocusNode) {
				MozillaSupports.release(visualFocusContainer);
			}
		}
		selection.Release();
	}
	
	private VisualSelectionInfo getStartSelectionInfo(Node sourceNode, boolean initialStartFlag) {
		boolean startFlag = initialStartFlag;
		Node visualNode = domMapping.getVisualNode(sourceNode);
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
		Node visualNode = domMapping.getVisualNode(sourceNode);
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
	
	private Element getVisualCommonElement(Node nodeA, Node nodeB) {
		Node[] nodesA = getVisualPath(nodeA);
		Node[] nodesB = getVisualPath(nodeB);
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
		releaseVisualPath(nodesA, index - 1);
		releaseVisualPath(nodesB);

		if (index > 0) {
			Node commonNode = nodesA[index - 1];
			if (commonNode.getNodeType() == Node.TEXT_NODE) {
				Node parent = commonNode.getParentNode();
				commonNode = parent;
			}
			return (Element)commonNode;
		} else {
			return null;
		}
	}
	
	private Node[] getVisualPath(Node node) {
		return getVisualPath(node, 0);
	}
	
	private Node[] getVisualPath(Node node, int height) {
		height++;
		Node[] path;
		Node parent = node.getParentNode();
		if (parent != null) {
			path = getVisualPath(parent, height);
		} else {
			path = new Node[height];
		}
		path[path.length - height] = node;
		return path;
	}

	private void releaseVisualPath(Node[] path) {
		releaseVisualPath(path, -1);
	}

	private void releaseVisualPath(Node[] path, int index) {
		for (int i = 0; i < path.length - 1; i++) {
			if (i != index) { 
				MozillaSupports.release(path[i]);
			}
		}
	}
	
	private static class VisualSelectionInfo {
		Node node;
		boolean startFlag;
		boolean directFlag;
		
		private VisualSelectionInfo(Node node) {
			this.node = node;
		}
		
		private VisualSelectionInfo(Node node, boolean startFlag) {
			this(node, startFlag, true);
		}
		
		private VisualSelectionInfo(Node node, boolean startFlag, boolean directFlag) {
			this(node);
			this.startFlag = startFlag;
			this.directFlag = directFlag;
		}
		
		private boolean isPseudoElement() {
			return VpeVisualDomBuilder.isPseudoElement(node);
		}
	}
	
	public VpeVisualCaretInfo getVisualCaretInfo(nsIDOMEvent event) {
		int aNsuiEvent = nsISupports.queryInterface(event.getAddress(), nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		nsIDOMNSUIEvent nsuiEvent = new nsIDOMNSUIEvent(aNsuiEvent);
		Node parent = nsuiEvent.getRangeParent();
		int offset = nsuiEvent.getRangeOffset();

		VpeVisualCaretInfo info = new VpeVisualCaretInfo(this, parent, offset);
		nsuiEvent.Release();
		
		return info;
	}
	
	void showVisualDragCaret(Node node, int offset) {
		visualBuilder.showDragCaret(node, offset);
	}
	
	void hideVisualDragCaret() {
		visualBuilder.hideDragCaret();
	}
	
	int getSourcePosition(Node visualInitNode, int visualInitOffset) {
		int position = 0;
		VisualSelectionInfo info = getVisualSelectedInfo(visualInitNode, visualInitOffset);
		if (info != null) {
			Node visualNode = info.node;
			Node sourceNode = domMapping.getNearSourceNode(visualNode);
			Node visualSelectedNode = domMapping.getVisualNode(sourceNode);
			if (visualSelectedNode == null) {
				visualSelectedNode = visualNode;
			}
			if (sourceNode != null) {
				switch (visualSelectedNode.getNodeType()) {
				case Node.TEXT_NODE:
					if (visualInitNode.getNodeType() != Node.TEXT_NODE) {
						visualInitOffset = info.startFlag ? 0 : visualSelectedNode.getNodeValue().length(); 
					}
					int ofset = DataHelper.textPos(visualSelectedNode.getNodeValue(), visualInitOffset);
					position = sourceBuilder.getPosition(sourceNode, ofset, false);

					
					try{
						IndexedRegion region = (IndexedRegion)sourceNode;
						int start = region.getStartOffset();
						String text = sourceBuilder.getStructuredTextViewer().getDocument().get(start, region.getEndOffset() - start);
						position = start + TextUtil.sourcePosition(text, visualSelectedNode.getNodeValue(), visualInitOffset);
					}catch(Exception ex){
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
			MozillaSupports.release(visualNode);
		}
		return position;
	}
	
	Point getSourceSelectionRangeAtVisualNode(Node visualInitNode, int visualInitOffset) {
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
	
	Node getOriginalTargetNode(nsIDOMEvent event) {
		Node targetNode = event.getTargetNode();
		if ("INPUT".equalsIgnoreCase(targetNode.getNodeName())) {
			return targetNode;
		} else {
			MozillaSupports.release(targetNode);
		}
		int aNsEvent = event.queryInterface(nsIDOMNSEvent.NS_IDOMNSEVENT_IID);
		nsIDOMNSEvent nsEvent = new nsIDOMNSEvent(aNsEvent);
		nsIDOMEventTarget target = nsEvent.getTmpRealOriginalTarget();	
		int aDragNode = target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
		Node originalNode = nsIDOMNode.getNodeAtAddress(aDragNode);
		if (VpeVisualDomBuilder.isAnonElement(originalNode)) {
			MozillaSupports.release(originalNode);
			originalNode = visualBuilder.getLastSelectedElement(); 
		}
		target.Release();
		nsEvent.Release();
		return originalNode;
	}
	
	VpeVisualInnerDropInfo getInnerDropInfo(nsIDOMEvent event) {
		int aNsuiEvent = nsISupports.queryInterface(event.getAddress(), nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		nsIDOMNSUIEvent nsuiEvent = new nsIDOMNSUIEvent(aNsuiEvent);
		Node dropContainer = null;
		int dropOffset = 0;
		int mouseX = nsuiEvent.getPageX();
		int mouseY = nsuiEvent.getPageY();
		Node originalNode = getOriginalTargetNode(event);
		if (originalNode == null || originalNode.getParentNode() == null ||
				originalNode.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
			return  new VpeVisualInnerDropInfo(null, 0, mouseX, mouseY);
		}
		if (originalNode.getNodeType() == Node.TEXT_NODE) {
			dropContainer = nsuiEvent.getRangeParent();
			Node containerForPseudoContent = VpePseudoContentCreator.getContainerForPseudoContent(dropContainer);
			if (containerForPseudoContent != null) {
				MozillaSupports.release(dropContainer);
				dropContainer = containerForPseudoContent;
				dropOffset = 0;
			} else {
				dropOffset = nsuiEvent.getRangeOffset();
			}
		} else {
		    int closestXDistance = HUGE_DISTANCE;
		    int closestYDistance = HUGE_DISTANCE;
		    boolean inNodeFlag = false;
		    Node closestNode = null;

		    NodeList childen = originalNode.getChildNodes();
			int count = childen.getLength();
			for (int i = 0; i < count; i++) {
				Node child = childen.item(i);
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
					if ("COLGROUP".equalsIgnoreCase(closestNode.getNodeName())) {
						Node nearChild = getNearChild(closestNode, mouseX, mouseY);
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
				dropOffset = MozillaSupports.getOffset(closestNode);
				Rectangle rect = visualBuilder.getNodeBounds(closestNode);
				if (VpeVisualDomBuilder.canInsertAfter(mouseX, mouseY, rect)) {
					dropOffset++;
				}
			}
		}
		VpeVisualInnerDropInfo info = new VpeVisualInnerDropInfo(dropContainer, dropOffset, mouseX, mouseY);
		return info;
	}
	
	private Node getNearChild(Node container, int x, int y) {
	    int closestXDistance = HUGE_DISTANCE;
	    int closestYDistance = HUGE_DISTANCE;
	    Node closestNode = null;

	    NodeList childen = container.getChildNodes();
		int count = childen.getLength();
		for (int i = 0; i < count; i++) {
			Node child = childen.item(i);
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
		nsISelection selection = visualSelectionController.getSelection();
		Node visualNode = getOriginalTargetNode(mouseEvent);
		if (selection.containsNode((nsIDOMNode)visualNode, false) || VpeVisualDomBuilder.isAnonElement(visualNode)) {
			return;
		}
		if (visualNode.getNodeType() == Node.ELEMENT_NODE) {
			
		}
		if (!selection.containsNode((nsIDOMNode)visualNode, false)) {
			if (visualNode.getNodeType() == Node.ELEMENT_NODE && !VpeVisualDomBuilder.isAnonElement(visualNode)) {
				Node visualParent = visualNode.getParentNode();
				int offset = MozillaSupports.getOffset(visualNode);
				selection.removeAllRanges();
				selection.collapse((nsIDOMNode)visualParent, offset);
				selection.extend((nsIDOMNode)visualParent, offset + 1);
				setSelection(selection);
//				mouseEvent.stopPropagation();
//				mouseEvent.preventDefault();
			}
		}

	}

	Element getDragElement(nsIDOMMouseEvent mouseEvent) {
		Element dragElement = visualBuilder.getDragElement(mouseEvent);
		if (dragElement != null) {
			return dragElement;
		}

		nsISelection selection = visualSelectionController.getSelection();
		Node visualNode = mouseEvent.getTargetNode();
		if (visualNode != null && visualNode.getNodeType() == Node.ELEMENT_NODE &&
				("INPUT".equalsIgnoreCase(visualNode.getNodeName()) || "BUTTON".equalsIgnoreCase(visualNode.getNodeName()) || "SELECT".equalsIgnoreCase(visualNode.getNodeName())) &&
				!selection.containsNode((nsIDOMNode)visualNode, false) && visualBuilder.canInnerDrag((Element)visualNode)) { 
			return (Element)visualNode;
		}
		return null;
	}
	
	Element getAppropriateElementForSelection(nsIDOMEvent event) {
		nsISelection selection = visualSelectionController.getSelection();
		Node visualNode = event.getTargetNode();
		if (visualNode != null) { 
			if (!"INPUT".equalsIgnoreCase(visualNode.getNodeName())) {
				visualNode = getOriginalTargetNode(event);
			}
			if (visualNode != null && visualNode.getNodeType() == Node.ELEMENT_NODE && !selection.containsNode((nsIDOMNode)visualNode, false)) {
				VpeElementMapping elementMapping = domMapping.getNearElementMapping(visualNode);
				if (elementMapping != null) {
					Element element = (Element)elementMapping.getVisualNode();
					MozillaSupports.addRef(element);
					return element;
				}
			}
		}
		return null;
	}
	
	void setVisualElementSelection(Element visualElement) {
		nsISelection selection = visualSelectionController.getSelection();
		visualElement.removeAttribute(MozillaBrowser.VPEFLASHERCOLORATTRIBUTE);
		Node visualParent = visualElement.getParentNode();
		int offset = MozillaSupports.getOffset(visualElement);
		selection.removeAllRanges();
		selection.collapse((nsIDOMNode)visualParent, offset);
		selection.extend((nsIDOMNode)visualParent, offset + 1);
		setSelection(selection);
		selection.Release();
	}
	
	VpeVisualInnerDragInfo getInnerDragInfo(nsIDOMMouseEvent event) {
		VpeVisualInnerDragInfo info = null;
		nsISelection selection = visualSelectionController.getSelection();
		Node focusNode = selection.getFocusNode();
		Node anchorNode = selection.getAnchorNode();
		if (focusNode != null && focusNode.equals(anchorNode)) {
			int focusOffset = selection.getFocusOffset();
			int anchorOffset = selection.getAnchorOffset();
			int offset = Math.min(focusOffset, anchorOffset);
			int length = Math.max(focusOffset, anchorOffset) - offset;

			switch (focusNode.getNodeType()) {
			case Node.ELEMENT_NODE:
				if (length == 1) {
					NodeList children = focusNode.getChildNodes();
					Node selectedNode = children.item(Math.min(focusOffset, anchorOffset));
					if (visualBuilder.getNodeBounds(selectedNode).contains(event.getMousePoint())) {
						switch(selectedNode.getNodeType()) {
						case Node.ELEMENT_NODE:
							info = new VpeVisualInnerDragInfo((Element)selectedNode);
							break;
						case Node.TEXT_NODE:
							info = new VpeVisualInnerDragInfo(selectedNode, 0, selectedNode.getNodeValue().length());
							break;
						}
					}
					if (info == null) {
						MozillaSupports.release(selectedNode);
					}
					MozillaSupports.release(children);
				}
				break;
			case Node.TEXT_NODE:
				// at bag ESP-75
//				if (visualBuilder.getNodeBounds(focusNode).contains(event.getMousePoint())) {
					MozillaSupports.addRef(focusNode);
					info = new VpeVisualInnerDragInfo(focusNode, offset, length);
//				}
				break;
			}
		}
		MozillaSupports.release(focusNode, anchorNode);
		return info;
	}
	
	void setMouseUpSelection(nsIDOMMouseEvent mouseEvent) {
		nsISelection selection = visualSelectionController.getSelection();
		boolean collapsed = selection.isCollapsed();
	}
	
	void printVisualSelection() {
		nsISelection selection = visualSelectionController.getSelection();
		boolean collapsed = selection.isCollapsed();
		System.out.println("  ## VisualSelection");
		System.out.println("  ## collapsed: " + collapsed);
		if (!collapsed) {
			Node anchorNode = selection.getAnchorNode();
			if (anchorNode != null) {
				System.out.println("  ## anchorNode: " + anchorNode.getNodeName() + " (" + MozillaSupports.getAddress(anchorNode) + ")  offset: " + selection.getAnchorOffset());
			} else {
				System.out.println("  ## anchorNode: " + anchorNode);
			}
		}
		Node focusNode = selection.getFocusNode();
		if (focusNode != null) {
			System.out.println("  ## focusNode: " + focusNode.getNodeName() + " (" + MozillaSupports.getAddress(focusNode) + ")  offset: " + selection.getFocusOffset());
		} else {
			System.out.println("  ## focusNode: " + focusNode);
		}
	}
	
	void setCaretAtMouse(nsIDOMMouseEvent mouseEvent) {
		Node visualNode = mouseEvent.getTargetNode();
		boolean isAnonElement = VpeVisualDomBuilder.isAnonElement(visualNode);
		MozillaSupports.release(visualNode);
		if (isAnonElement) return;
		VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(mouseEvent);
		Node visuaDropContainer = visualDropInfo.getDropContainer();
		if (visuaDropContainer.getNodeType() == Node.TEXT_NODE) return;
		int visualDropOffset = visualDropInfo.getDropOffset();
		if (visualDropOffset > 0) {
			NodeList visualChildren = visuaDropContainer.getChildNodes();
			Node visualChild = visualChildren.item(visualDropOffset - 1);
			boolean isText = visualChild.getNodeType() == Node.TEXT_NODE;
			MozillaSupports.release(visualChild, visualChildren);
			if (isText) return;
		}
		nsISelection selection = visualSelectionController.getSelection();
		if (!selection.contains((nsIDOMNode)visuaDropContainer, visualDropOffset)) {
			setVisualCaret(visuaDropContainer, visualDropOffset);
			nsIFrameSelection frameSelection = presShell.getFrameSelection();
			if (frameSelection != null) {
				frameSelection.setMouseDownState(true);
			}
			mouseEvent.preventDefault();
			mouseEvent.stopPropagation();
		}
		selection.Release();
	}
	
	void setVisualCaret(Node visualNode, int offset) {
		nsISelection selection = visualSelectionController.getSelection();
		if (visualNode != null) {
			if (visualNode.getNodeType() == Node.TEXT_NODE && offset > visualNode.getNodeValue().length()) {
				offset = visualNode.getNodeValue().length();
			}
			selection.collapse((nsIDOMNode)visualNode, offset);
		} else {
			selection.removeAllRanges();
		}
		visualSelectionController.setCaretEnabled(true);
		setSelection(selection);
		selection.Release();
	}
}

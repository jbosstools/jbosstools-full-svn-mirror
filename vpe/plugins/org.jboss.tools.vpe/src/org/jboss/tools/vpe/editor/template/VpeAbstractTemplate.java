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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDropInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.template.dnd.VpeDnd;
import org.jboss.tools.vpe.editor.template.resize.VpeResizer;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Class which response for configuration template element from configuration
 * file.
 * 
 */
public abstract class VpeAbstractTemplate implements VpeTemplate {
	protected boolean caseSensitive;
	protected boolean children;
	protected boolean modify;

	protected boolean hasImaginaryBorder;

	/** a resizer instance */
	private VpeResizer resizer;

	// TODO Max Areshkau add DnD support
	private VpeDnd dragger;
	private TextFormatingData textFormatingData;
	private VpePseudoContentCreator pseudoContentCreator;

	private static final String TAG_BREAKER = VpeTemplateManager.VPE_PREFIX
			+ "breaker"; //$NON-NLS-1$
	private static final String ATTR_BREAKER_TYPE = "type"; //$NON-NLS-1$
	private static final String ATTR_BREAKER_TYPE_IGNORE = "ignore"; //$NON-NLS-1$
	private static final String ATTR_BREAKER_TYPE_SELECTITEM = "selectItem"; //$NON-NLS-1$
	private static final int BREAKER_TYPE_NONE = 0;
	private static final int BREAKER_TYPE_IGNORE = 1;
	private static final int BREAKER_TYPE_SELECTITEM = 2;

	private static final String TAG_PSEUDOCONTENT = VpeTemplateManager.VPE_PREFIX
			+ "pseudoContent"; //$NON-NLS-1$
	private static final String ATTR_PSEUDOCONTENT_DEFAULTTEXT = "defaultText"; //$NON-NLS-1$
	private static final String ATTR_PSEUDOCONTENT_ATTRNAME = "attrName"; //$NON-NLS-1$

	private int breakerType = BREAKER_TYPE_NONE;

	static private HashSet<String> inlineTags = new HashSet<String>();
	static {
		inlineTags.add("b"); //$NON-NLS-1$
		inlineTags.add("i"); //$NON-NLS-1$
		inlineTags.add("u"); //$NON-NLS-1$
		inlineTags.add("img"); //$NON-NLS-1$ 
		inlineTags.add("sub"); //$NON-NLS-1$
		inlineTags.add("sup"); //$NON-NLS-1$
		inlineTags.add("strike"); //$NON-NLS-1$
		inlineTags.add("font"); //$NON-NLS-1$
		inlineTags.add("a"); //$NON-NLS-1$
		inlineTags.add("input"); //$NON-NLS-1$
		inlineTags.add("textarea"); //$NON-NLS-1$
		inlineTags.add("span"); //$NON-NLS-1$
		inlineTags.add("button"); //$NON-NLS-1$
		inlineTags.add("label"); //$NON-NLS-1$
	}
	static private HashMap<String, Integer> tagResizeConstrans = new HashMap<String, Integer>();
	static {
		tagResizeConstrans
				.put(
						"table", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_ALL)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"tr", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_ALL)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"br", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"b", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"i", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"u", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"sub", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"sup", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"strike", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans
				.put(
						"font", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
		tagResizeConstrans.put(
				"a", Integer.valueOf(VpeTagDescription.RESIZE_CONSTRAINS_NONE)); //$NON-NLS-1$
	}

	static private HashSet<String> breakWithParagraphTags = new HashSet<String>();
	static {
		breakWithParagraphTags.add("b"); //$NON-NLS-1$
		breakWithParagraphTags.add("a"); //$NON-NLS-1$
		breakWithParagraphTags.add("abbr"); //$NON-NLS-1$
		breakWithParagraphTags.add("acronym"); //$NON-NLS-1$
		breakWithParagraphTags.add("b"); //$NON-NLS-1$
		breakWithParagraphTags.add("bdo"); //$NON-NLS-1$
		breakWithParagraphTags.add("big"); //$NON-NLS-1$
		breakWithParagraphTags.add("blink"); //$NON-NLS-1$
		breakWithParagraphTags.add("cite"); //$NON-NLS-1$
		breakWithParagraphTags.add("code"); //$NON-NLS-1$
		breakWithParagraphTags.add("del"); //$NON-NLS-1$
		breakWithParagraphTags.add("dfn"); //$NON-NLS-1$
		breakWithParagraphTags.add("em"); //$NON-NLS-1$
		breakWithParagraphTags.add("font"); //$NON-NLS-1$
		breakWithParagraphTags.add("ins"); //$NON-NLS-1$
		breakWithParagraphTags.add("kbd"); //$NON-NLS-1$
		breakWithParagraphTags.add("nobr"); //$NON-NLS-1$
		breakWithParagraphTags.add("q"); //$NON-NLS-1$
		breakWithParagraphTags.add("s"); //$NON-NLS-1$
		breakWithParagraphTags.add("samp"); //$NON-NLS-1$
		breakWithParagraphTags.add("small"); //$NON-NLS-1$
		breakWithParagraphTags.add("span"); //$NON-NLS-1$
		breakWithParagraphTags.add("strike"); //$NON-NLS-1$
		breakWithParagraphTags.add("strong"); //$NON-NLS-1$
		breakWithParagraphTags.add("tt"); //$NON-NLS-1$
		breakWithParagraphTags.add("u"); //$NON-NLS-1$
		breakWithParagraphTags.add("var"); //$NON-NLS-1$
	}
	static private HashSet<String> breakWithoutParagraphTags = new HashSet<String>();
	static {
		breakWithoutParagraphTags.add("p"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("address"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("blockquote"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("center"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("div"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h1"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h2"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h3"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h4"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h5"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("h6"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("p"); //$NON-NLS-1$
		breakWithoutParagraphTags.add("pre"); //$NON-NLS-1$
	}

	/**
	 * Initiates template after its creating
	 * 
	 * @param templateElement
	 *            <code>Element</code> with a name "vpe:template" from the
	 *            template file
	 * @param caseSensitive
	 *            The case sensitive of an element of a source file
	 */
	public void init(Element templateElement, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		children = "yes".equals(templateElement.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_CHILDREN)); //$NON-NLS-1$
		modify = "yes".equals(templateElement.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_MODIFY)); //$NON-NLS-1$

		String strHasImaginaryBorder = templateElement
				.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_HAS_IMAGINARY_BORDER);

		if (strHasImaginaryBorder != null
				&& strHasImaginaryBorder.length() != 0) {
			hasImaginaryBorder = "yes".equalsIgnoreCase(strHasImaginaryBorder); //$NON-NLS-1$			
		} else {
			hasImaginaryBorder = false;
		}

		init(templateElement);
	}

	protected void init(Element templateElement) {
		initTemplateSections(templateElement, true, true, true, true, true);
	}

	protected void initTemplateSections(Element templateElement,
			boolean resizeHandler, boolean dndHandler,
			boolean textFormatingHandler, boolean breakHandler,
			boolean pseudoContentHandler) {
		NodeList children = templateElement.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node section = children.item(i);
				if (section.getNodeType() == Node.ELEMENT_NODE) {
					String sectionName = section.getNodeName();
					if (resizeHandler
							&& VpeTemplateManager.TAG_RESIZE
									.equals(sectionName)) {
						initResizeHandler((Element) section);
					} else if (dndHandler
							&& VpeTemplateManager.TAG_DND.equals(sectionName)) {
						initDndHandler((Element) section);
					} else if (textFormatingHandler
							&& VpeTemplateManager.TAG_TEXT_FORMATING
									.equals(sectionName)) {
						initTextFormatingHandler((Element) section);
					} else if (breakHandler && TAG_BREAKER.equals(sectionName)) {
						initBreakHandler((Element) section);
					} else if (pseudoContentHandler
							&& TAG_PSEUDOCONTENT.equals(sectionName)) {
						initPseudoContentHandler((Element) section);
					} else {
						initTemplateSection((Element) section);
					}
				}
			}
		}
	}

	private void initResizeHandler(Element templateSection) {
		if (resizer == null) {
			resizer = new VpeResizer();
			resizer.setResizeData(templateSection);
		}
	}

	private void initDndHandler(Element templateSection) {

		if (getDragger() == null) {
			setDragger(new VpeDnd());
			getDragger().setDndData(templateSection);
		}
	}

	private void initTextFormatingHandler(Element templateSection) {
		if (textFormatingData == null) {
			textFormatingData = new TextFormatingData(templateSection);
		}
	}

	private void initBreakHandler(Element templateSection) {
		if (breakerType == BREAKER_TYPE_NONE) {
			String typeValue = templateSection.getAttribute(ATTR_BREAKER_TYPE);
			if (typeValue != null) {
				if (ATTR_BREAKER_TYPE_IGNORE.equalsIgnoreCase(typeValue)) {
					breakerType = BREAKER_TYPE_IGNORE;
				} else if (ATTR_BREAKER_TYPE_SELECTITEM
						.equalsIgnoreCase(typeValue)) {
					breakerType = BREAKER_TYPE_SELECTITEM;
				}
			}
		}
	}

	private void initPseudoContentHandler(Element templateSection) {
		if (pseudoContentCreator == null) {
			if ("yes".equalsIgnoreCase(templateSection.getAttribute(ATTR_PSEUDOCONTENT_DEFAULTTEXT))) { //$NON-NLS-1$
				pseudoContentCreator = new VpeTextPseudoContentCreator(null,
						templateSection
								.getAttribute(ATTR_PSEUDOCONTENT_ATTRNAME));
			} else {
				NodeList children = templateSection.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++) {
						Node child = children.item(i);
						if (child.getNodeType() == Node.ELEMENT_NODE) {
							pseudoContentCreator = new VpeHtmlPseudoContentCreator(
									(Element) child);
							return;
						}
					}
					for (int i = 0; i < len; i++) {
						Node child = children.item(i);
						if (child.getNodeType() == Node.TEXT_NODE) {
							String text = child.getNodeValue().trim();
							if (text.length() > 0) {
								pseudoContentCreator = new VpeTextPseudoContentCreator(
										text, null);
								return;
							}
						}
					}
				}
				pseudoContentCreator = VpeEmptyPseudoContentCreator
						.getInstance();
			}
		}
	}

	protected void initTemplateSection(Element templateSection) {
	}

	/**
	 * Is invoked after construction of all child nodes of the current visual
	 * node.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param data
	 *            Object <code>VpeCreationData</code>, built by a method
	 *            <code>create</code>
	 */
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data) {
	}

	IRegion fCurrentRegionToFormat = null;

	private IRegion getRegionToFormat() {
		return fCurrentRegionToFormat;
	}

	private void clearRegionToFormat() {
		fCurrentRegionToFormat = null;
	}

	private void updateRegionToFormat(Node node) {
		if (node instanceof IndexedRegion) {
			IndexedRegion region = (IndexedRegion) node;
			int start = region.getStartOffset();
			int end = region.getEndOffset();

			if (fCurrentRegionToFormat == null) {
				fCurrentRegionToFormat = new Region(start, end - start);
			} else {
				int cStart = fCurrentRegionToFormat.getOffset();
				int cEnd = fCurrentRegionToFormat.getOffset()
						+ fCurrentRegionToFormat.getLength();

				if (start < cStart)
					cStart = start;
				if (end > cEnd)
					cEnd = end;

				fCurrentRegionToFormat = new Region(cStart, cEnd - cStart);
			}
		}
	}

	private void reformatCallback(VpePageContext pageContext) {
		try {
			StructuredTextEditor editor = pageContext.getEditPart()
					.getSourceEditor();
			StructuredTextViewer viewer = editor.getTextViewer();
			if (getRegionToFormat() != null) {
				if (editor instanceof ITextFormatter) {
					((ITextFormatter) editor).formatTextRegion(viewer
							.getDocument(), getRegionToFormat());
				}
			}
		} catch (Exception e) {
			VpePlugin.reportProblem(e);
		} finally {
			clearRegionToFormat();
		}
	}

	/**
	 * Processes keyboard input (without the pressed key Ctrl)
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceDocument
	 *            The document of the source tree.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param charCode
	 *            Code of the pressed key
	 * @param selection
	 *            The current selection
	 * @param formatter
	 *            Interface for formatting the source text
	 * @return <code>true</code> if the key is processed
	 */
	public boolean nonctrlKeyPressHandler(VpePageContext pageContext,
			Document sourceDocument, Node sourceNode, nsIDOMNode visualNode,
			Object data, long charCode, VpeSourceSelection selection,
			ITextFormatter formatter) {
		switch (breakerType) {
		case BREAKER_TYPE_IGNORE:
			return true;
		case BREAKER_TYPE_SELECTITEM:
			return VpeBreackerHelper.selectItem(pageContext, sourceDocument,
					sourceNode, visualNode, data, charCode, selection);
		default:
			return nonctrlKeyPressHandlerImpl(pageContext, sourceDocument,
					sourceNode, visualNode, data, charCode, selection,
					formatter);
		}
	}

	private boolean nonctrlKeyPressHandlerImpl(VpePageContext pageContext,
			Document sourceDocument, Node sourceNode, nsIDOMNode visualNode,
			Object data, long charCode, VpeSourceSelection selection,
			ITextFormatter formatter) {
		clearRegionToFormat();
		Node focusNode = selection.getFocusNode();
		int focusNodeType = focusNode.getNodeType();
		if (sourceNode.getNodeName().equalsIgnoreCase("li")) { //$NON-NLS-1$
			Node where = null;
			if (selection.getFocusOffset() == 0) {
				where = sourceNode;
			} else {
				where = sourceNode.getNextSibling();
			}
			Node newLi = sourceNode.cloneNode(false);
			newLi = sourceNode.getParentNode().insertBefore(newLi, where);

			if (focusNodeType == Node.TEXT_NODE
					|| (focusNodeType == Node.ELEMENT_NODE && sourceNode != focusNode)) {

				Node parent = focusNode.getParentNode();
				Node newText = focusNode.cloneNode(false);
				Node node1 = newText;
				if (focusNodeType == Node.TEXT_NODE) {
					String value1 = focusNode.getNodeValue().substring(0,
							selection.getFocusOffset());
					String value2 = node1.getNodeValue().substring(
							selection.getFocusOffset());
					if (where != sourceNode) {
						focusNode.setNodeValue(value1);
						node1.setNodeValue(value2);
					} else {
						focusNode.setNodeValue(value2);
						node1.setNodeValue(value1);
					}
				}
				Node newParent = null;
				while (parent != sourceNode) {
					newParent = parent.cloneNode(false);
					newParent.appendChild(node1);
					node1 = newParent;
					parent = parent.getParentNode();
				}
				node1 = newLi.appendChild(node1);

				Node next = focusNode.getNextSibling();
				if (next != null) {
					while (next != null) {
						Node next1 = next.getNextSibling();
						newText.getParentNode().appendChild(next);
						next = next1;
					}
				}
				if (selection.getFocusOffset() == 0) {
					focusNode.getParentNode().removeChild(focusNode);
				}
				setCursor(pageContext, newText);
			} else {
				setCursor(pageContext, newLi);
			}
			updateRegionToFormat(sourceNode);
			updateRegionToFormat(newLi);
			reformatCallback(pageContext);
			return true;
		} else if (focusNodeType == Node.ELEMENT_NODE
				&& focusNode.getNodeName().equalsIgnoreCase("br")) { //$NON-NLS-1$
			Node newNode = focusNode
					.getParentNode()
					.insertBefore(
							focusNode.getOwnerDocument().createElement("br"), focusNode); //$NON-NLS-1$
			updateRegionToFormat(focusNode);
			updateRegionToFormat(newNode);
			reformatCallback(pageContext);
			return true;
		} else if (focusNodeType == Node.ELEMENT_NODE
				&& focusNode.getNodeName().equalsIgnoreCase("p")) { //$NON-NLS-1$
			Node nextElement = focusNode.getNextSibling();
			Node parent = focusNode.getParentNode();
			if (parent != null) {
				Node newP = null;
				if (nextElement != null) {
					newP = focusNode.getParentNode().insertBefore(
							focusNode.cloneNode(false), nextElement);
				} else {
					newP = focusNode.getParentNode().appendChild(
							focusNode.cloneNode(false));
				}
				setCursor(pageContext, newP);
				updateRegionToFormat(newP);
			}
			updateRegionToFormat(focusNode);
			reformatCallback(pageContext);
			return true;
		} else if (focusNodeType == Node.TEXT_NODE
				&& focusNode.getParentNode().getNodeName().equalsIgnoreCase(
						"td")) { //$NON-NLS-1$
			Text newNode = ((Text) focusNode).splitText(selection
					.getFocusOffset());
			setCursor(pageContext, newNode);
			updateRegionToFormat(focusNode);
			updateRegionToFormat(newNode);
			reformatCallback(pageContext);
			return true;
		} else if (sourceNode.getNodeType() == Node.ELEMENT_NODE
				&& sourceNode.getNodeName().equalsIgnoreCase("tr")) { //$NON-NLS-1$
			return true;
		} else if (focusNodeType == Node.TEXT_NODE
				&& !focusNode.getParentNode().getNodeName().equalsIgnoreCase(
						"body")) { //$NON-NLS-1$
			Node parent = focusNode.getParentNode();
			if (parent != null) {
				String parentName = parent.getNodeName();
				if (breakWithParagraphTags.contains(parentName)) {
					Node p1 = null, p2 = null;
					Node parentParent = parent.getParentNode();
					if (parentParent != null) {
						if (!parentParent.getNodeName().equalsIgnoreCase("p")) { //$NON-NLS-1$
							if (parentParent.getNodeType() != Node.DOCUMENT_NODE) {
								p1 = parentParent.getOwnerDocument()
										.createElement("p"); //$NON-NLS-1$
								parentParent.insertBefore(p1, parent);
								parent = parentParent.removeChild(parent);
								p1.appendChild(parent);
							} else {
								p1 = ((Document) parentParent)
										.createElement("p"); //$NON-NLS-1$
								parentParent.insertBefore(p1, parent);
								parent = parentParent.removeChild(parent);
								p1.appendChild(parent);
							}
						} else {
							p1 = parentParent;
						}
						if (p1 != null) {
							Text newNode = ((Text) focusNode)
									.splitText(selection.getFocusOffset());
							p2 = p1.getParentNode().insertBefore(
									p1.cloneNode(false), p1.getNextSibling());
							Node newParent = p2.appendChild(parent
									.cloneNode(false));
							Node currentNode = newNode;
							while (currentNode != null) {
								Node currentNode1 = currentNode
										.getNextSibling();
								currentNode = parent.removeChild(currentNode);
								newParent.appendChild(currentNode);
								currentNode = currentNode1;
							}
							setCursor(pageContext, newNode);

							updateRegionToFormat(p1);
							updateRegionToFormat(p2);
							reformatCallback(pageContext);
							return true;
						}
					}
				} else if (breakWithoutParagraphTags.contains(parentName)) {
					Text newNode = ((Text) focusNode).splitText(selection
							.getFocusOffset());
					Node parentParent = parent.getParentNode();
					if (parentParent != null) {
						boolean clone = newNode.getNodeValue().trim().length() > 0;
						Node newParent = parentParent.insertBefore(
								makeNewParent(parent, clone), parent
										.getNextSibling());
						Node currentNode = newNode;
						while (currentNode != null) {
							Node currentNode1 = currentNode.getNextSibling();
							currentNode = parent.removeChild(currentNode);
							newParent.appendChild(currentNode);
							currentNode = currentNode1;
						}
						setCursor(pageContext, newNode);

						updateRegionToFormat(sourceNode);
						updateRegionToFormat(newParent);
						reformatCallback(pageContext);
						return true;
					}
				}
			}
		} else if (focusNodeType == Node.ELEMENT_NODE
				&& selection.getFocusOffset() == 2) {
			Node parent = focusNode;
			if (parent != null) {
				String parentName = parent.getNodeName();
				if (breakWithParagraphTags.contains(parentName)) {
					Node p1 = null, p2 = null;
					Node parentParent = parent.getParentNode();
					if (parentParent != null) {
						if (!parentParent.getNodeName().equalsIgnoreCase("p")) {
							if (parentParent.getNodeType() != Node.DOCUMENT_NODE) {
								p1 = parentParent.getOwnerDocument()
										.createElement("p");
								parentParent.insertBefore(p1, parent);
								parent = parentParent.removeChild(parent);
								p1.appendChild(parent);
							} else {
								p1 = ((Document) parentParent)
										.createElement("p");
								parentParent.insertBefore(p1, parent);
								parent = parentParent.removeChild(parent);
								p1.appendChild(parent);
							}
						} else {
							p1 = parentParent;
						}
						if (p1 != null) {
							p2 = p1.getParentNode().insertBefore(
									p1.cloneNode(false), p1.getNextSibling());
							Node newParent = p2.appendChild(parent
									.cloneNode(false));
							setCursor(pageContext, newParent);

							updateRegionToFormat(p1);
							updateRegionToFormat(p2);
							updateRegionToFormat(newParent);
							reformatCallback(pageContext);
							return true;
						}
					}
				} else if (breakWithoutParagraphTags.contains(parentName)) {
					Node parentParent = parent.getParentNode();
					if (parentParent != null) {
						Node newParent = parentParent.insertBefore(
								makeNewParent(parent, false), parent
										.getNextSibling());
						setCursor(pageContext, newParent);

						updateRegionToFormat(newParent);
						reformatCallback(pageContext);

						return true;
					}
				}
			}
		} else if (focusNodeType == Node.TEXT_NODE
				&& focusNode.getParentNode().getNodeName().equalsIgnoreCase(
						"body")) { //$NON-NLS-1$
			Node p1 = focusNode.getOwnerDocument().createElement("p"); //$NON-NLS-1$
			Node p2 = focusNode.getOwnerDocument().createElement("p"); //$NON-NLS-1$
			Text newNode = ((Text) focusNode).splitText(selection
					.getFocusOffset());
			focusNode.getParentNode().insertBefore(p1, focusNode);
			focusNode.getParentNode().insertBefore(p2, newNode);
			focusNode = focusNode.getParentNode().removeChild(focusNode);
			newNode = (Text) newNode.getParentNode().removeChild(newNode);
			p1.appendChild(focusNode);
			p2.appendChild(newNode);
			setCursor(pageContext, newNode);

			updateRegionToFormat(p1);
			updateRegionToFormat(p2);
			updateRegionToFormat(newNode);
			reformatCallback(pageContext);

			return true;
		} else if (focusNodeType == Node.ELEMENT_NODE
				&& selection.getFocusOffset() == 0) {
			Node newNode = focusNode
					.getParentNode()
					.insertBefore(
							focusNode.getOwnerDocument().createElement("br"), focusNode); //$NON-NLS-1$
			updateRegionToFormat(focusNode);
			updateRegionToFormat(newNode);
			reformatCallback(pageContext);
			return true;
		}
		return false;
	}

	private Node makeNewParent(Node parent, boolean clone) {
		Node newParent = null;
		if (parent != null) {
			boolean isH = false;
			for (int i = 1; i < 7 && !isH; i++) {
				isH = parent.getNodeName().equalsIgnoreCase("h" + i); //$NON-NLS-1$
			}
			if (!isH || clone) {
				newParent = parent.cloneNode(false);
			} else {
				newParent = parent.getOwnerDocument().createElement("p"); //$NON-NLS-1$
			}
		}
		return newParent;
	}

	private void setCursor(VpePageContext pageContext, Node node) {
		int nodeOffset = ((IndexedRegion) node).getStartOffset();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			ElementImpl element = (ElementImpl) node;
			nodeOffset = element.getStartEndOffset();
		}
		pageContext.getSourceBuilder().getStructuredTextViewer()
				.setSelectedRange(nodeOffset, 0);
		pageContext.getSourceBuilder().getStructuredTextViewer().revealRange(
				nodeOffset, 0);
	}

	/**
	 * Is invoked before removal of the visiblis node from the tree
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	public void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
	}

	/**
	 * At a modification of the node of an source tree, the method update for
	 * this node is invoked. Template can indicate other node for update
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceNode
	 *            The current node of the source tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return For this node of an source tree the method update is invoked. If
	 *         null, that is invoked update for current source node
	 */
	public Node getNodeForUptate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		return null;
	}

	/**
	 * Is invoked at resize of an element visual tree
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualElement
	 *            The current element of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param resizerConstrains
	 *            Code of resizer:<br>
	 *            top-left: 1<br>
	 *            top: 2<br>
	 *            top-right: 4<br>
	 *            left: 8<br>
	 *            right: 16<br>
	 *            bottomleft: 32<br>
	 *            bottom: 64<br>
	 *            bottom-right: 128<br>
	 * @param top
	 *            Element top
	 * @param left
	 *            Element left
	 * @param width
	 *            Element width
	 * @param height
	 *            Element height
	 */
	public void resize(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Object data, int resizerConstrains, int top, int left, int width,
			int height) {
		if (resizer != null) {
			resizer.resize(pageContext, sourceElement, visualDocument,
					visualElement, data, resizerConstrains, top, left, width,
					height);
		}
	}

	/**
	 * Checks a capability of drag of visual element
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualElement
	 *            The current element of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return <code>true</code> The element can be dragged
	 */
	public boolean canInnerDrag(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualElement, Object data) {
		// TODO Max Areshkau add DnD support
		if (getDragger() != null) {

			return getDragger().isDragEnabled();
		} else {
			return true;
		}
	}

	/**
	 * Checks a capability to drop an element in the container
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param container
	 *            Element-Container
	 * @param sourceDragNode
	 *            Node for drop
	 * @return <code>true</code> The node can be dropped
	 */
	public boolean canInnerDrop(VpePageContext pageContext, Node container,
			Node sourceDragNode) {

		if (dragger != null) {
			return dragger
					.isDropEnabled(pageContext, container, sourceDragNode);
		} else {
			return false;
		}
	}

	/**
	 * Is invoked at drop of an element visual tree
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param dragInfo
	 *            The information on the dragged element
	 * @param dropInfo
	 *            The information on the drop container
	 */
	public void innerDrop(VpePageContext pageContext,
			VpeSourceInnerDragInfo dragInfo, VpeSourceInnerDropInfo dropInfo) {
		// TODO Max Areshkau add DnD support
		// if (dragger != null) {
		// dragger.drop(pageContext, dragInfo, dropInfo);
		// }
	}

	protected String deleteFromString(String data, String begin, String end) {
		int startPosition = data.indexOf(begin);

		if (startPosition < 0)
			return data;

		int endPosition = data.indexOf(end, startPosition);

		String result = data.substring(0, startPosition).trim();
		if (endPosition > 0) {
			result += data.substring(endPosition + 1, data.length()).trim();
		}

		return result;
	}

	/**
	 * Checks, whether it is necessary to re-create an element at change of
	 * attribute
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Atrribute name
	 * @param value
	 *            Attribute value
	 * @return <code>true</code> if it is required to re-create an element at a
	 *         modification of attribute, <code>false</code> otherwise.
	 */
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return false;
	}

	/**
	 * @return <code>true</code> if the element can have children
	 */
	public boolean isChildren() {
		return children;
	}

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	/**
	 * @return <code>true</code> if the element is case sensitive
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Returns the data for formatting an element of source tree
	 * 
	 * @return <code>TextFormatingData</code>
	 */
	public TextFormatingData getTextFormatingData() {
		return textFormatingData;
	}

	/**
	 * Returns <code>VpeTagDescription</code>
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualElement
	 *            The current element of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return <code>VpeTagDescription</code>
	 */
	public VpeTagDescription getTagDescription(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualElement, Object data) {
		VpeTagDescription tagDescription = new VpeTagDescription();

		if (inlineTags.contains(visualElement.getNodeName().toLowerCase())) {
			tagDescription
					.setDisplayType(VpeTagDescription.DISPLAY_TYPE_INLINE);
		}

		if (resizer != null) {
			resizer.modifyTagDescription(tagDescription);
		}
		return tagDescription;
	}

	/**
	 * Sets value of attribute of the current visual element. Is invoked at
	 * change of attribute of an source element.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Attribute name.
	 * @param value
	 *            Attribute value.
	 */
	public void setAttribute(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data,
			String name, String value) {
	}

	/**
	 * Informs on remove of attribute of the current source element.
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Attribute name.
	 */
	public void removeAttribute(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name) {
	}

	/**
	 * Returns a list of attributes of an element of the source tree, the values
	 * which one are mapped in the visiblis editor
	 * 
	 * @return attrubute name array
	 */
	public String[] getOutputAtributeNames() {
		return null;
	}

	/**
	 * If the value of attribute of an element of an source tree is mapped by
	 * the way of text node of a visual treer, this method returns the text
	 * node, otherwise - null
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @return Text node or null
	 */
	public nsIDOMText getOutputTextNode(VpePageContext pageContext,
			Element sourceElement, Object data) {
		return null;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public boolean isOutputAttributes() {
		return false;
	}

	/**
	 * @deprecated
	 */
	public int getType() {
		return 0;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VpeAnyData getAnyData() {
		return null;
	}

	/**
	 * @deprecated
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param offset
	 * @param length
	 * @param data
	 */
	public void setSourceAttributeSelection(VpePageContext pageContext,
			Element sourceElement, int offset, int length, Object data) {
	}

	/**
	 * @deprecated
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	public void setSourceAttributeValue(VpePageContext pageContext,
			Element sourceElement, Object data) {
	}

	/**
	 * Is invoked at a change of bundle values
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	public void refreshBundleValues(VpePageContext pageContext,
			Element sourceElement, Object data) {
	}

	/**
	 * Opens proprties editor for bundle value Is invoked at double mouse click
	 * on visual element
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	public void openBundleEditors(VpePageContext pageContext,
			Element sourceElement, Object data) {
	}

	/**
	 * Opens editor of source file for include-element
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 */
	public void openIncludeEditor(VpePageContext pageContext,
			Element sourceElement, Object data) {
	}

	/**
	 * The unfilled element of an source tree can be mapped in the visiblis
	 * editor with the default contents This method fills default contents
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceContainer
	 *            The current element of the source tree.
	 * @param visualContainer
	 *            The current element of the visual tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 */
	public void setPseudoContent(VpePageContext pageContext,
			Node sourceContainer, nsIDOMNode visualContainer,
			nsIDOMDocument visualDocument) {
		if (pseudoContentCreator != null) {
			pseudoContentCreator.setPseudoContent(pageContext, sourceContainer,
					visualContainer, visualDocument);
		} else {
			VpeDefaultPseudoContentCreator.getInstance().setPseudoContent(
					pageContext, sourceContainer, visualContainer,
					visualDocument);
		}
	}

	public boolean containsText() {
		return true;
	}

	public boolean hasImaginaryBorder() {
		return hasImaginaryBorder;
	}

	/**
	 * @return the dragger
	 */
	public VpeDnd getDragger() {

		return dragger;
	}

	/**
	 * @param dragger
	 *            the dragger to set
	 */
	public void setDragger(VpeDnd dragger) {
		this.dragger = dragger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeTemplate#getNodeData(org.mozilla
	 * .interfaces.nsIDOMNode,
	 * org.jboss.tools.vpe.editor.mapping.VpeElementData,
	 * org.jboss.tools.vpe.editor.mapping.VpeDomMapping)
	 */
	public NodeData getNodeData(nsIDOMNode node, VpeElementData elementData,
			VpeDomMapping domMapping) {

		// if input data is correct
		if ((node != null) && (elementData != null)
				&& (elementData.getNodesData() != null)) {

			List<NodeData> nodesData = elementData.getNodesData();

			for (NodeData nodeData : nodesData) {

				// if source nodes equals
				if (node.equals(nodeData.getVisualNode()))
					return nodeData;
			}
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.vpe.editor.template.ITemplateNodesManager#
	 * getTargetVisualNodeByBySourcePosition
	 * (org.jboss.tools.vpe.editor.mapping.VpeElementMapping, int, int,
	 * org.jboss.tools.vpe.editor.mapping.VpeDomMapping)
	 */
	public nsIDOMNode getVisualNodeByBySourcePosition(
			VpeElementMapping elementMapping, int focusPosition,
			int anchorPosition, VpeDomMapping domMapping) {

		// find focus attribute by position
		nsIDOMNode focusNode = findNodeByPosition(elementMapping
				.getElementData(), focusPosition);

		// fond anchor attribute by position
		nsIDOMNode anchorNode = findNodeByPosition(elementMapping
				.getElementData(), anchorPosition);

		// if anchor and focus attributes are equal return focused attribute
		if ((focusNode != null) && (focusNode == anchorNode))
			return focusNode;

		// else return all element
		return elementMapping.getVisualNode();

	}

	private nsIDOMNode findNodeByPosition(VpeElementData elementData,
			int position) {

		// if data are correct
		if ((elementData != null) && (elementData.getNodesData() != null)) {

			List<NodeData> attributesMapping = elementData.getNodesData();

			// for each defined attribute
			for (NodeData attributeData : attributesMapping) {

				// if position is in attribute's bound
				if ((position >= (NodesManagingUtil
						.getStartOffsetNode(attributeData.getSourceNode())))
						&& (position <= (NodesManagingUtil
								.getEndOffsetNode(attributeData.getSourceNode()))))
					return attributeData.getVisualNode();
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeTemplate#openBundle(org.jboss.
	 * tools.vpe.editor.context.VpePageContext,
	 * org.mozilla.interfaces.nsIDOMNode,
	 * org.jboss.tools.vpe.editor.mapping.VpeElementMapping)
	 */
	public boolean openBundle(VpePageContext pageContext,
			nsIDOMNode visualNode, VpeElementMapping elementMapping) {

		nsIDOMNode lastSelectedNode = SelectionUtil
				.getLastSelectedNode(pageContext);

		if (elementMapping == null)
			return false;

		NodeData nodeData = getNodeData(lastSelectedNode, elementMapping
				.getElementData(), pageContext.getDomMapping());

		if (nodeData != null && nodeData.getSourceNode() != null)
			return pageContext.getBundle().openBundle(
					nodeData.getSourceNode().getNodeValue(),
					NodesManagingUtil.getPageLocale(pageContext, nodeData
							.getSourceNode()));

		return false;
	}
}

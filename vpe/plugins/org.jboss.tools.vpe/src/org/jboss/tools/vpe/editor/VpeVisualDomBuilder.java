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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.dnd.VpeDnD;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.proxy.VpeProxyUtil;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeCreatorUtil;
import org.jboss.tools.vpe.editor.template.VpeDefaultPseudoContentCreator;
import org.jboss.tools.vpe.editor.template.VpeHtmlTemplate;
import org.jboss.tools.vpe.editor.template.VpeTagDescription;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.template.VpeToggableTemplate;
import org.jboss.tools.vpe.editor.template.dnd.VpeDnd;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.ElService;
import org.jboss.tools.vpe.editor.util.FaceletUtil;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.jboss.tools.vpe.resref.core.CSSReferenceList;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsIDOMText;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeVisualDomBuilder extends VpeDomBuilder {

	/**
     * 
     */
    public static final String SRC_NODE = "SRC_NODE"; //$NON-NLS-1$
    public static final String PARENT = "PARENT"; //$NON-NLS-1$
    public static final String VPE_USER_TOGGLE_ID = "vpe-user-toggle-id"; //$NON-NLS-1$
	public static final String VPE_USER_TOGGLE_LOOKUP_PARENT = "vpe-user-toggle-lookup-parent"; //$NON-NLS-1$

	private static final String PSEUDO_ELEMENT = "br"; //$NON-NLS-1$
	private static final String PSEUDO_ELEMENT_ATTR = "vpe:pseudo-element"; //$NON-NLS-1$
	private static final String INIT_ELEMENT_ATTR = "vpe:init-element"; //$NON-NLS-1$
	private static final String MOZ_ANONCLASS_ATTR = "_MOZ_ANONCLASS"; //$NON-NLS-1$
	private static final String INCLUDE_ELEMENT_ATTR = "vpe:include-element"; //$NON-NLS-1$
	private static final int DRAG_AREA_WIDTH = 10;
	private static final int DRAG_AREA_HEIGHT = 10;
	private static final String ATTR_DRAG_AVAILABLE_CLASS = "__drag__available_style"; //$NON-NLS-1$
	private static String DOTTED_BORDER = "border: 1px dotted #FF6600; padding: 5px;"; //$NON-NLS-1$
	private static final String CSS_STYLE_FOR_BORDER_FOR_UNKNOWN_TAGS = ";border: 1px solid green;"; //$NON-NLS-1$

	private MozillaEditor visualEditor;
	private XulRunnerEditor xulRunnerEditor;

	// JBIDE-2170 Sergey Dzmitrovich
	// private nsIDOMElement visualContentArea;
	private VpePageContext pageContext;
	private VpeDnD dnd;
	// JBIDE-2170 Sergey Dzmitrovich
	// private nsIDOMNode headNode;
	private List includeStack;
	// TODO Max Areshkau JBIDE-1457
	// boolean rebuildFlag = false;


	private static final String ATTR_VPE = "vpe"; //$NON-NLS-1$
	private static final String ATTR_VPE_INLINE_LINK_VALUE = "inlinelink"; //$NON-NLS-1$

	private static final String ATTR_REL_STYLESHEET_VALUE = "stylesheet"; //$NON-NLS-1$

	private static final String YES_STRING = "yes"; //$NON-NLS-1$
	private static final String ZERO_STRING = "0"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String ATRIBUTE_BORDER = "border"; //$NON-NLS-1$
	private static final String ATRIBUTE_CELLSPACING = "cellspacing"; //$NON-NLS-1$
	private static final String ATRIBUTE_CELLPADDING = "cellpadding"; //$NON-NLS-1$

	private static final String DOTTED_BORDER_STYLE = "border : 1px dotted #808080"; //$NON-NLS-1$
	private static final String DOTTED_BORDER_STYLE_FOR_IMG = "1px dotted #808080"; //$NON-NLS-1$
	private static final String DOTTED_BORDER_STYLE_FOR_TD = "border-left : 1px dotted #808080; border-right : 1px dotted #808080; border-top : 1px dotted #808080; border-bottom : 0px; color:#0051DD; background-color:#ECF3FF; padding-left: 3px;  padding-right: 3px;  line-height : 10px; font-family : arial; font-size : 10px; text-align:top; margin : 1px; -moz-user-modify : read-only"; //$NON-NLS-1$
	private static final String DOTTED_BORDER_STYLE_FOR_SPAN = "border : 1px solid #0051DD; color:#0051DD; background-color:#ECF3FF; padding-left: 3px;  padding-right: 3px;  line-height : 10px; font-family : arial; font-size : 10px; text-align:top; margin : 1px; -moz-user-modify : read-only"; //$NON-NLS-1$

	static private HashSet<String> unborderedSourceNodes = new HashSet<String>();
	static {
		unborderedSourceNodes.add(HTML.TAG_HTML);
		unborderedSourceNodes.add(HTML.TAG_HEAD);
		unborderedSourceNodes.add(HTML.TAG_BODY);
	}

	static private HashSet<String> unborderedVisualNodes = new HashSet<String>();
	static {
		unborderedVisualNodes.add(HTML.TAG_TBODY);
		unborderedVisualNodes.add(HTML.TAG_THEAD);
		unborderedVisualNodes.add(HTML.TAG_TR);
		unborderedVisualNodes.add(HTML.TAG_TD);
		unborderedVisualNodes.add(HTML.TAG_COL);
		unborderedVisualNodes.add(HTML.TAG_COLS);
		unborderedVisualNodes.add(HTML.TAG_COLGROUP);
		unborderedVisualNodes.add(HTML.TAG_LI);
		unborderedVisualNodes.add(HTML.TAG_BR);
	}
	private VpeDnd dropper;

	private Map<IStorage, Document> includeDocuments = new HashMap<IStorage, Document>();
	private boolean showInvisibleTags;
	private boolean showBorderForUnknownTags;

	public VpeVisualDomBuilder(VpeDomMapping domMapping,
			INodeAdapter sorceAdapter,
			MozillaEditor visualEditor, VpePageContext pageContext) {

		super(domMapping, sorceAdapter);
		this.visualEditor = visualEditor;
		xulRunnerEditor = visualEditor.getXulRunnerEditor();

		// this.visualContentArea = visualEditor.getContentArea();
		this.dnd = new VpeDnD();
		this.pageContext = pageContext;
		// this.headNode = visualEditor.getHeadNode();
		dropper = new VpeDnd();
		dropper.setDndData(false, true);

//		if (isFacelet()) {
//			faceletFile = true;
//		} else {
//			faceletFile = false;
//		}

		this.showInvisibleTags = JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				IVpePreferencesPage.SHOW_NON_VISUAL_TAGS);
		this.showBorderForUnknownTags = JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS);
		
	}

	public void buildDom(Document sourceDocument) {
		nsIDOMNodeList children = getContentArea().getChildNodes();
		long len = children.getLength();
		for (long i = len - 1; i >= 0; i--) {
			getContentArea().removeChild(children.item(i));
		}
		VpeSourceDomBuilder sourceBuilder = pageContext.getSourceBuilder();
		IDocument document = sourceBuilder.getStructuredTextViewer()
				.getDocument();
		if (document == null)
			return;
		includeStack = new ArrayList();
		IEditorInput input = pageContext.getEditPart().getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			if (file != null) {
				includeStack.add(new VpeIncludeInfo(null, file, pageContext
						.getSourceBuilder().getSourceDocument()));
			}
		}

		pageContext.refreshConnector();
		
		// FIXED FOR JBIDE-3799 by sdzmitrovich
		// it code is not necessary because addExternalLinks() does the same but
		// better
		// pageContext.installIncludeElements();
		
		refreshExternalLinks();
		
//		if (isFacelet()) {
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4398
		 * Additional check for facelet's taglibs should be added
		 * to distinguish it from custom tags and pages without facelets support.
		 */
		Element root = FaceletUtil.findComponentElement(sourceDocument.getDocumentElement());
		if ((root != null) && (isFacelet(root))){
				addNode(root, null, getContentArea());
		} else {
			addChildren(null, sourceDocument, getContentArea());
		}
		/*
		 * Fixes http://jira.jboss.com/jira/browse/JBIDE-2126. To provide
		 * appropriate context menu functionality visual content area should be
		 * mapped in any case.
		 */
		registerNodes(new VpeNodeMapping(sourceDocument, getContentArea()));
	}

	public void rebuildDom(Document sourceDocument) {
		// clearIncludeDocuments();
		cleanHead();
		domMapping.clear(getContentArea());
		super.dispose();

		pageContext.clearAll();
		// FIXED FOR JBIDE-3799 by sdzmitrovich, moved calling of this method to buid dom 
		// refreshExternalLinks();
		pageContext.getBundle().refreshRegisteredBundles();
		
		//Next path was moved to buildDom method
		//to avoid <br> in a visual DOM before editor browser
		//load and after reload 
//		nsIDOMNodeList children = getContentArea().getChildNodes();
//		long len = children.getLength();
//		for (long i = len - 1; i >= 0; i--) {
//			getContentArea().removeChild(children.item(i));
//		}

		if (sourceDocument != null) {
			buildDom(sourceDocument);
		}

	}

	// temporary, will be change to prefference's variable
	// private boolean borderVisible = true;

	/**
	 * Adds visual representation of {@code sourceNode} and its descendants
	 * to {@code visualContainer}.
	 * 
	 * If {@code visualNextNode} is not {@code null}, then created
	 * representation will be inserted before {@code visualNextNode}, 
	 * otherwise it will be inserted at the end of {@code visualContainer}
	 * 
	 * @param sourceNode source node, cannot be {@code null} 
	 * @param visualNextNode next visual node, can be {@code null}
	 * @param visualContainer visual container, cannot be {@code null} 
	 * @return {@code true} if and only if the visual representation is created and added successfully 
	 */
	private boolean addNode(Node sourceNode, nsIDOMNode visualNextNode,
			nsIDOMNode visualContainer) {
		try {
		nsIDOMNode visualNewNode = createNode(sourceNode, visualContainer);
// Commented as fix for JBIDE-3012.	
//		// Fix for JBIDE-1097
//		try {
//			if (visualNewNode != null) {
//				nsIDOMHTMLInputElement iDOMInputElement = (nsIDOMHTMLInputElement) visualNewNode
//						.queryInterface(nsIDOMHTMLInputElement.NS_IDOMHTMLINPUTELEMENT_IID);
//				iDOMInputElement.setReadOnly(true);
//			}
//		} catch (XPCOMException ex) {
//			// just ignore this exception
//		}
		if (visualNewNode != null) {
			if (visualNextNode == null) {
				visualContainer.appendChild(visualNewNode);
			} else {
				visualContainer.insertBefore(visualNewNode, visualNextNode);
			}
			return true;
		}
		} catch(XPCOMException xpcomException) {
			VpePlugin.reportProblem(xpcomException);
		}

		return false;
	}

	/**
	 * Creates border around each tag.
	 * 
	 * @param sourceNode the source node
	 * @param visualNode the visual node
	 * @param block show if it is block or inline tag
	 * @return the border element around visual tag
	 */
	private nsIDOMElement createBorder(Node sourceNode,
			nsIDOMElement visualNode, boolean block) {
		nsIDOMElement border = null;
		if (visualNode == null)
			return null;
		if (unborderedSourceNodes.contains(sourceNode.getNodeName()
				.toLowerCase()))
			return null;
		if (unborderedVisualNodes.contains(visualNode.getNodeName()
				.toLowerCase()))
			return null;
		if (HTML.TAG_IMG.equalsIgnoreCase(visualNode.getNodeName())) {
			String width = visualNode.getAttribute(ATRIBUTE_BORDER);
			if (width == null || ZERO_STRING.equalsIgnoreCase(width)
					|| EMPTY_STRING.equalsIgnoreCase(width)) {
				String style = visualNode
						.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
				style = VpeStyleUtil.setParameterInStyle(style,
						ATRIBUTE_BORDER, DOTTED_BORDER_STYLE_FOR_IMG);
				visualNode.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
			}
			return null;
		}
		if (block) {
			if (YES_STRING.equals(VpePreference.USE_DETAIL_BORDER.getValue())) {
				border = getVisualDocument().createElement(HTML.TAG_TABLE);
				border.setAttribute(ATRIBUTE_CELLSPACING, ZERO_STRING);
				border.setAttribute(ATRIBUTE_CELLPADDING, ZERO_STRING);

				nsIDOMElement tr1 = getVisualDocument().createElement(HTML.TAG_TR);
				border.appendChild(tr1);
				nsIDOMElement td1 = getVisualDocument().createElement(HTML.TAG_TD);
				td1.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE,
						DOTTED_BORDER_STYLE_FOR_TD);
				nsIDOMText text = getVisualDocument().createTextNode(sourceNode
						.getNodeName());
				td1.appendChild(text);
				tr1.appendChild(td1);
				nsIDOMElement tr2 = getVisualDocument().createElement(HTML.TAG_TR);
				border.appendChild(tr2);
				nsIDOMElement td2 = getVisualDocument().createElement(HTML.TAG_TD);
				tr2.appendChild(td2);
				nsIDOMElement p = getVisualDocument().createElement(HTML.TAG_P);
				p.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE,
						DOTTED_BORDER_STYLE);
				td2.appendChild(p);

				p.appendChild(visualNode);

			} else {
				border = getVisualDocument().createElement(HTML.TAG_TABLE);
				border.setAttribute(ATRIBUTE_CELLSPACING, ZERO_STRING);
				border.setAttribute(ATRIBUTE_CELLPADDING, ZERO_STRING);

				nsIDOMElement tr2 = getVisualDocument().createElement(HTML.TAG_TR);
				border.appendChild(tr2);
				nsIDOMElement td2 = getVisualDocument().createElement(HTML.TAG_TD);
				tr2.appendChild(td2);
				nsIDOMElement p = getVisualDocument().createElement(HTML.TAG_P);
				p.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE,
						DOTTED_BORDER_STYLE);
				td2.appendChild(p);

				p.appendChild(visualNode);
			}
		} else {
			border = getVisualDocument().createElement(HTML.TAG_SPAN);
			border.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE,
					DOTTED_BORDER_STYLE);
			if (YES_STRING.equals(VpePreference.USE_DETAIL_BORDER.getValue())) {
				nsIDOMElement name = getVisualDocument()
						.createElement(HTML.TAG_SPAN);
				name.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE,
						DOTTED_BORDER_STYLE_FOR_SPAN);
				nsIDOMText text = getVisualDocument().createTextNode(sourceNode
						.getNodeName());
				name.appendChild(text);
				border.appendChild(name);
			}
			border.appendChild(visualNode);
		}
		if (VpeStyleUtil.getAbsolute((Element) sourceNode)) {
			int top = VpeStyleUtil.getSizeFromStyle((Element) sourceNode,
					VpeStyleUtil.ATTRIBUTE_STYLE + VpeStyleUtil.DOT_STRING
							+ VpeStyleUtil.PARAMETER_TOP);
			int left = VpeStyleUtil.getSizeFromStyle((Element) sourceNode,
					VpeStyleUtil.ATTRIBUTE_STYLE + VpeStyleUtil.DOT_STRING
							+ VpeStyleUtil.PARAMETER_LEFT);

			String style = visualNode
					.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
			style = VpeStyleUtil.deleteFromString(style,
					VpeStyleUtil.PARAMETER_POSITION,
					VpeStyleUtil.SEMICOLON_STRING);
			style = VpeStyleUtil.deleteFromString(style,
					VpeStyleUtil.PARAMETER_TOP, VpeStyleUtil.SEMICOLON_STRING);
			style = VpeStyleUtil.deleteFromString(style,
					VpeStyleUtil.PARAMETER_LEFT, VpeStyleUtil.SEMICOLON_STRING);
			visualNode.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);

			style = border.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
			style = VpeStyleUtil.setAbsolute(style);
			if (top != -1)
				style = VpeStyleUtil.setSizeInStyle(style,
						VpeStyleUtil.PARAMETER_TOP, top);
			if (left != -1)
				style = VpeStyleUtil.setSizeInStyle(style,
						VpeStyleUtil.PARAMETER_LEFT, left);
			border.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
		}
		return border;
	}

	/**
	 * Creates new visual node representing {@code sourceNode} and its descendants.
	 * 
	 * @param sourceNode source node
	 * @param visualOldContainer visual node, in which the caller plans to insert new visual node 
	 * @return new visual node
	 */
	protected nsIDOMNode createNode(Node sourceNode,
			nsIDOMNode visualOldContainer) {
			
		boolean registerFlag = isCurrentMainDocument();

		//it's check for initialization visualController,
		//if we trying to process some event when controller
		//hasn't been initialized, it's causes 
		//org.eclipse.ui.PartInitException: Warning: Detected recursive 
		//attempt by part org.jboss.tools.jst.jsp.jspeditor.HTMLTextEditor to create itself 
		//(this is probably, but not necessarily, a bug)

		if(visualEditor.getController().getSelectionManager()!=null) {
			// reads and dispatch events, this code prevent eclipse
			//from sleeping during processing big pages
			getPageContext().processDisplayEvents();
		}
		// JBIDE-675, checks if editor was disposed or not
		if (getPageContext().getSourceBuilder() == null
				|| includeDocuments == null) {

			throw new VpeDisposeException();
		}
		// check source node can be changed and link can be a null in this case
		// we shouldn't process this node
		if (sourceNode == null
				|| (sourceNode.getNodeType() != Node.TEXT_NODE
						&& sourceNode.getNodeType() != Node.ELEMENT_NODE && sourceNode
						.getNodeType() != Node.COMMENT_NODE)) {
			return null;
		}

		Set<Node> ifDependencySet = new HashSet<Node>();
		pageContext.setCurrentVisualNode(visualOldContainer);
		VpeTemplate template = getTemplateManager().getTemplate(pageContext,
				sourceNode, ifDependencySet);

		VpeCreationData creationData = null;
		Node sourceNodeProxy = null;
		// FIX FOR JBIDE-1568, added by Max Areshkau
		try {
			if (ElService.getInstance().isELNode(getPageContext(),
					sourceNode)) {
				
				sourceNodeProxy = VpeProxyUtil.createProxyForELExpressionNode(getPageContext(),
					sourceNode);
				try {
				creationData = template.create(getPageContext(),
						sourceNodeProxy, getVisualDocument());
				//Fix for JBIDE-3144, we use proxy and some template can 
				//try to cast for not supported interface
				} catch(ClassCastException ex) {
					VpePlugin.reportProblem(ex);
					sourceNodeProxy = null;
					//then we create template without using proxy
					creationData = template.create(getPageContext(), sourceNode,
							getVisualDocument());
				}
				
			} else {
				creationData = template.create(getPageContext(), sourceNode,
						getVisualDocument());
			}

		} catch (XPCOMException ex) {
			VpePlugin.getPluginLog().logError(ex);
			VpeTemplate defTemplate = getTemplateManager().getDefTemplate();
			creationData = defTemplate.create(getPageContext(), sourceNode,
					getVisualDocument());
		}

		getPageContext().setCurrentVisualNode(null);

		nsIDOMNode visualNewNode = creationData.getNode();
		
		if (sourceNode.getNodeType() == Node.ELEMENT_NODE && visualNewNode == null && isShowInvisibleTags())
			visualNewNode = createInvisbleElementLabel(sourceNode);

		nsIDOMElement border = null;

		if (visualNewNode != null
				&& visualNewNode.getNodeType() == nsIDOMNode.ELEMENT_NODE) {

			nsIDOMElement visualNewElement = (nsIDOMElement) visualNewNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);

			if ((visualNewElement != null) && template.hasImaginaryBorder()) {

				visualNewElement.setAttribute(HTML.ATTR_STYLE, visualNewElement
						.getAttribute(HTML.ATTR_STYLE)
						+ VpeStyleUtil.SEMICOLON_STRING + DOTTED_BORDER);

			}
			if (visualNewElement != null) {
				correctVisualAttribute(visualNewElement);
			}

			/*
			 * Create border for unknown tags if specified.
			 * Update the style attribute. Usually it's DIV or SPAN with text, 
			 * so it's harmless to update the style.
			 * For more complex action #createBorder(..) method should be used.
			 *	Also "Create border for all tags" option is never used so it was removed. 
			 */
			if ((template.getType() == VpeHtmlTemplate.TYPE_ANY) && showBorderForUnknownTags) {
					String style = visualNewElement
						.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
					style += CSS_STYLE_FOR_BORDER_FOR_UNKNOWN_TAGS;
					visualNewElement.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
					
			}
			if (!isCurrentMainDocument() && visualNewElement != null) {
				setReadOnlyElement(visualNewElement);
			}
		}

		if (sourceNode instanceof Element && visualNewNode != null
				&& visualNewNode.getNodeType() == nsIDOMNode.ELEMENT_NODE) {

			setTooltip((Element) sourceNode, (nsIDOMElement) visualNewNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
		}
		if (registerFlag) {

			final VpeElementData data = creationData.getElementData();
			
			if ((sourceNodeProxy != null) && (data != null)
					&& (data.getNodesData() != null)
					&& (data.getNodesData().size() > 0)) {

				for (org.jboss.tools.vpe.editor.mapping.NodeData nodeData : data
						.getNodesData()) {

					if (nodeData.getSourceNode() != null) {
						Node attr = null;
						if(sourceNode.getAttributes()!=null) {
							attr =	sourceNode.getAttributes().getNamedItem(nodeData
								.getSourceNode().getNodeName());
						} else {
							//Text node haven't child nodes, but it's  node.
							attr = sourceNode;
						}
						nodeData.setSourceNode(attr);
						nodeData.setEditable(false);
					}
				}

			}
			VpeElementMapping elementMapping = new VpeElementMapping(
					sourceNode, visualNewNode, border, template,
					ifDependencySet, creationData.getData(), data);
			registerNodes(elementMapping);
		}
		if (template.hasChildren()) {
			List<?> childrenInfoList = creationData.getChildrenInfoList();
			if (childrenInfoList == null) {
				addChildren(template, sourceNode,
						visualNewNode != null ? visualNewNode
								: visualOldContainer);
			} else {
				addChildren(template, sourceNode, visualOldContainer,
						childrenInfoList);
			}
		} else if(sourceNode.getNodeType() == Node.ELEMENT_NODE&&visualNewNode != null && isShowInvisibleTags()){
			nsIDOMElement span =  getVisualDocument().createElement(HTML.TAG_SPAN);
			span.appendChild(visualNewNode);
			addChildren(template, sourceNode,span);
			visualNewNode= span;
		}
		getPageContext().setCurrentVisualNode(visualOldContainer);
		template.validate(getPageContext(), sourceNode, getVisualDocument(),
				creationData);
		getPageContext().setCurrentVisualNode(null);


		if (border != null)
			return border;
		else
			return visualNewNode;
	}

	protected void correctVisualAttribute(nsIDOMElement element) {

		String styleValue = element.getAttribute(HTML.TAG_STYLE);
		String backgroundValue = element
				.getAttribute(VpeStyleUtil.PARAMETR_BACKGROND);

		if (styleValue != null) {
			styleValue = VpeStyleUtil.addFullPathIntoURLValue(styleValue,
					pageContext);
			element.setAttribute(HTML.TAG_STYLE, styleValue);
		}
		if (backgroundValue != null) {
			backgroundValue = VpeStyleUtil
					.addFullPathIntoBackgroundValue(backgroundValue,
							pageContext.getEditPart().getEditorInput());
			element.setAttribute(VpeStyleUtil.PARAMETR_BACKGROND,
					backgroundValue);
		}
		//fix for jbide-3209		
		if(element.hasAttribute(HTML.ATTR_DIR)) {
			element.removeAttribute(HTML.ATTR_DIR);
		}
	}

	/**
	 * Adds visual representations of {@code sourceContainer}'s children 
	 * and their descendants to {@code visualContainer}.
	 * 
	 * @param containerTemplate
	 * @param sourceContainer
	 * @param visualContainer
	 */
	protected void addChildren(VpeTemplate containerTemplate,
			Node sourceContainer, nsIDOMNode visualContainer) {

		NodeList sourceNodes = sourceContainer.getChildNodes();
		int len = sourceNodes.getLength();
		int childrenCount = 0;
		for (int i = 0; i < len; i++) {
			Node sourceNode = sourceNodes.item(i);
			if (addNode(sourceNode, null, visualContainer)) {
				if (Node.ELEMENT_NODE == sourceNode.getNodeType()) {
				}
				childrenCount++;
			}
		}

		if (childrenCount == 0) {
			setPseudoContent(containerTemplate, sourceContainer,
					visualContainer);
		}
	}

	protected void addChildren(VpeTemplate containerTemplate,
			Node sourceContainer, nsIDOMNode visualOldContainer,
			List<?> childrenInfoList) {
		for (int i = 0; i < childrenInfoList.size(); i++) {
			VpeChildrenInfo info = (VpeChildrenInfo) childrenInfoList.get(i);
			nsIDOMNode visualParent = info.getVisualParent();
			if (visualParent == null)
				visualParent = visualOldContainer;
			List<?> sourceChildren = info.getSourceChildren();
			int childrenCount = 0;
			if (sourceChildren != null) {
				for (int j = 0; j < sourceChildren.size(); j++) {
					Node child = (Node) sourceChildren.get(j);
					if ((!isInvisibleNode(child))
							&& addNode((Node) sourceChildren.get(j), null,
									visualParent)) {
						childrenCount++;
					}
				}
			}
			if (childrenCount == 0 && childrenInfoList.size() == 0) {
				setPseudoContent(containerTemplate, sourceContainer,
						visualParent);
			}
		}
	}

	/**
	 * 
	 * @param containerTemplate
	 * @param sourceContainer
	 * @param visualContainer
	 */
	protected nsIDOMNode addInvisibleChildren(Node sourceContainer,
			nsIDOMNode visualContainer) {
		nsIDOMElement wrapper = null;
		int count = 0;
		// node is not null
		if (visualContainer != null) {

			// wrap node to span
			wrapper = getVisualDocument().createElement(HTML.TAG_SPAN);
			wrapper.appendChild(visualContainer);

			// add all invisible tags to wrapper
			NodeList sourceNodes = sourceContainer.getChildNodes();

			for (int i = 0; i < sourceNodes.getLength(); i++) {

				Node child = sourceNodes.item(i);

				if (isInvisibleNode(child)) {
					addNode(child, null, wrapper);
					count++;
				}
			}
		}

		return count > 0 ? wrapper : visualContainer;

	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	private boolean isInvisibleNode(Node node) {

		// get template
		Set<Node> ifDependencySet = new HashSet<Node>();
		VpeTemplate template = getTemplateManager().getTemplate(pageContext, node,
				ifDependencySet);
		// check if invisible tag
		if (template.isInvisible())
			return true;
		return false;

	}

	// /////////////////////////////////////////////////////////////////////////
	public nsIDOMNode addStyleNodeToHead(String styleText) {
		nsIDOMNode newStyle = getVisualDocument()
				.createElement(VpeStyleUtil.ATTRIBUTE_STYLE);

		if (styleText != null) {
			nsIDOMText newText = getVisualDocument().createTextNode(styleText);
			newStyle.appendChild(newText);
		}
		getHeadNode().appendChild(newStyle);
		return newStyle;
	}

	public nsIDOMNode replaceStyleNodeToHead(nsIDOMNode oldStyleNode,
			String styleText) {
		nsIDOMElement newStyle = getVisualDocument()
				.createElement(VpeStyleUtil.ATTRIBUTE_STYLE);

		if (styleText != null) {
			nsIDOMNode newText = getVisualDocument().createTextNode(styleText);
			newStyle.appendChild(newText);
		}

		getHeadNode().replaceChild(newStyle, oldStyleNode);
		return newStyle;
	}

	public void removeStyleNodeFromHead(nsIDOMNode oldStyleNode) {
		getHeadNode().removeChild(oldStyleNode);
	}

	void addExternalLinks() {
		IEditorInput input = pageContext.getEditPart().getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}
		ResourceReference[] l = null;
		if (file != null) {
			l = CSSReferenceList.getInstance().getAllResources(file);
		}
		if (l != null) {
			for (int i = 0; i < l.length; i++) {
				ResourceReference item = l[i];
				addLinkNodeToHead("file:///" + item.getLocation(), YES_STRING, false); //$NON-NLS-1$
			}
		}
	}

	void removeExternalLinks() {
		nsIDOMNodeList childs = getHeadNode().getChildNodes();
		long length = childs.getLength();
		for (long i = length - 1; i >= 0; i--) {
			nsIDOMNode node = childs.item(i);
			if (node.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				boolean isLink = false;
				boolean isStyle = false;
				if ((isLink = HTML.TAG_LINK
						.equalsIgnoreCase(node.getNodeName()))
						|| (isStyle = HTML.TAG_STYLE.equalsIgnoreCase(node
								.getNodeName()))) {
					nsIDOMElement element = (nsIDOMElement) node
							.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
					if ((isLink || (isStyle && ATTR_VPE_INLINE_LINK_VALUE
							.equalsIgnoreCase(element.getAttribute(ATTR_VPE))))
							&& YES_STRING
									.equalsIgnoreCase(element
											.getAttribute(VpeTemplateManager.ATTR_LINK_EXT))) {
						getHeadNode().removeChild(node);
					}
				}
			}
		}
	}

	void refreshExternalLinks() {
		removeExternalLinks();
		addExternalLinks();
	}

	// ==========================================================
	void resetPseudoElement(nsIDOMNode visualNode) {
		if (visualNode != null) {
			nsIDOMNode visualParent = visualNode.getParentNode();
			if (visualParent != null) {
				PseudoInfo info = getPseudoInfo(visualParent);
				if (info.pseudoNode == null && !info.isElements) {
					addPseudoElementImpl(visualParent);
				} else if (info.pseudoNode != null && info.isElements) {
					visualParent.removeChild(info.pseudoNode);
				}
			}
		}
	}

	private PseudoInfo getPseudoInfo(nsIDOMNode visualParent) {
		nsIDOMNode pseudoNode = null;
		boolean isElements = false;

		if (visualParent == null)
			return new PseudoInfo();
		nsIDOMNodeList visualNodes = visualParent.getChildNodes();
		if (visualNodes == null)
			return new PseudoInfo();

		long length = visualNodes.getLength();
		for (long i = 0; i < length; i++) {
			nsIDOMNode visualNode = visualNodes.item(i);
			if (pseudoNode == null && isPseudoElement(visualNode)) {
				pseudoNode = visualNode;
			} else if (!isEmptyText(visualNode)) {
				isElements = true;
			}
			if (pseudoNode != null && isElements) {
				break;
			}
		}
		return new PseudoInfo(pseudoNode, isElements);
	}

	static boolean isInitElement(nsIDOMNode visualNode) {
		if (visualNode == null) {
			return false;
		}

		if (visualNode.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		}

		if (YES_STRING.equalsIgnoreCase(((nsIDOMElement) visualNode)
				.getAttribute(INIT_ELEMENT_ATTR))) {
			return true;
		}

		return false;
	}

	static boolean isPseudoElement(nsIDOMNode visualNode) {
		if (visualNode == null) {
			return false;
		}

		if (visualNode.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		}

		if (YES_STRING.equalsIgnoreCase(((nsIDOMElement) visualNode
				.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))
				.getAttribute(PSEUDO_ELEMENT_ATTR))) {
			return true;
		}

		return false;
	}

	private void setPseudoContent(VpeTemplate containerTemplate,
			Node sourceContainer, nsIDOMNode visualContainer) {
		if (containerTemplate != null) {
			containerTemplate.setPseudoContent(pageContext, sourceContainer,
					visualContainer, getVisualDocument());
		} else {
			try {
				VpeDefaultPseudoContentCreator.getInstance().setPseudoContent(
						pageContext, sourceContainer, visualContainer,
						getVisualDocument());
			} catch (VpeExpressionException ex) {
				VpeExpressionException exception = new VpeExpressionException(
						"Error for source node" + sourceContainer.toString(), ex); //$NON-NLS-1$
				VpePlugin.reportProblem(exception);
			}
		}

	}

	private void addPseudoElementImpl(nsIDOMNode visualParent) {
		if (!getTemplateManager().isWithoutPseudoElementContainer(visualParent
				.getNodeName())) {
			if (VpeDebug.VISUAL_ADD_PSEUDO_ELEMENT) {
				System.out.println("-------------------- addPseudoElement: " //$NON-NLS-1$
						+ visualParent.getNodeName());
			}
			nsIDOMElement visualPseudoElement = getVisualDocument()
					.createElement(PSEUDO_ELEMENT);
			visualPseudoElement.setAttribute(PSEUDO_ELEMENT_ATTR, YES_STRING);
			visualParent.appendChild(visualPseudoElement);
		}
	}

	public boolean isEmptyElement(nsIDOMNode visualParent) {
		nsIDOMNodeList visualNodes = visualParent.getChildNodes();
		long len = visualNodes.getLength();

		if ((len == 0) || (len == 1 && isEmptyText(visualNodes.item(0)))) {
			return true;
		}

		return false;
	}

	public boolean isEmptyDocument() {
		nsIDOMNodeList visualNodes = getContentArea().getChildNodes();
		long len = visualNodes.getLength();
		if ((len == 0)
				|| (len == 1 && (isEmptyText(visualNodes.item(0)) || isPseudoElement(visualNodes
						.item(0))))) {
			return true;
		}

		return false;
	}

	private boolean isEmptyText(nsIDOMNode visualNode) {
		if (visualNode == null
				|| (visualNode.getNodeType() != nsIDOMNode.TEXT_NODE)) {
			return false;
		}

		if (visualNode.getNodeValue().trim().length() == 0) {
			return true;
		}

		return false;
	}

	// ==========================================================

	public void updateNode(Node sourceNode) {
		if (sourceNode == null)
			return;

		switch (sourceNode.getNodeType()) {
		case Node.DOCUMENT_NODE:
			rebuildDom((Document) sourceNode);
			break;
		default:
			updateElement(getNodeForUpdate(sourceNode));
		}
	}

	// TODO Ssergey Vasilyev make a common code for figuring out
	// if it is need to update parent node or not
	private Node getNodeForUpdate(Node sourceNode) {
		/* Changing of <tr> or <td> tags can affect whole the table */
		Node sourceTable = getParentTable(sourceNode, 2);
		if (sourceTable != null) {
			return sourceTable;
		}

		/* Changing of an <option> tag can affect the parent select */
		Node sourceSelect = getParentSelect(sourceNode);
		if (sourceSelect != null) {
			return sourceSelect;
		}

		return sourceNode;
	}

	private void updateElement(Node sourceNode) {
		VpeElementMapping elementMapping = null;
		VpeNodeMapping nodeMapping = domMapping.getNodeMapping(sourceNode);
		if (nodeMapping instanceof VpeElementMapping) {

			elementMapping = (VpeElementMapping) nodeMapping;
			if (elementMapping != null && elementMapping.getTemplate() != null) {
				Node updateNode = elementMapping.getTemplate()
						.getNodeForUpdate(pageContext,
								elementMapping.getSourceNode(),
								elementMapping.getVisualNode(),
								elementMapping.getData());

				/*
				 * special processing of "style" element
				 * 
				 * for unification of updating nodes - or redevelop updating
				 * mechanism (for example : transfer this function to template )
				 * or redevelop template of "style" element
				 */
				if (HTML.TAG_STYLE.equalsIgnoreCase(sourceNode.getNodeName())) {
					// refresh style node
					VpeStyleUtil.refreshStyleElement(this, elementMapping);
					return;
				}
				if (updateNode != null && updateNode != sourceNode) {
					updateNode(updateNode);
					return;
				}
			}
		}

		nsIDOMNode visualOldNode = domMapping.remove(sourceNode);
		getSourceNodes().remove(sourceNode);

		if (sourceNode instanceof INodeNotifier) {
			((INodeNotifier) sourceNode).removeAdapter(getSorceAdapter());
		}
		if (visualOldNode != null) {
			if (elementMapping != null) {
				nsIDOMElement border = elementMapping.getBorder();
				if (border != null) {
					visualOldNode = border;
				}
			}
			nsIDOMNode visualContainer = visualOldNode.getParentNode();
			nsIDOMNode visualNextNode = visualOldNode.getNextSibling();
			if (visualContainer != null) {
				addNode(sourceNode, visualNextNode, visualContainer);
				// If add the new node after deleting the old, in some cases
				// XULRunner will work in unexpected way (see JBIDE-3473)
				// so it is necessary to remove the old child AFTER adding the new 
				visualContainer.removeChild(visualOldNode);
			}
		} else {
			// Max Areshkau Why we need update parent node when we update text
			// node?
			// looks like we haven't need do it.
			if (sourceNode.getNodeType() == Node.TEXT_NODE) {
				updateNode(sourceNode.getParentNode());
			}else if(HTML.TAG_LINK.equalsIgnoreCase(sourceNode.getNodeName())) {
				addNode(sourceNode, null, getHeadNode());
			}
		}
	}

	public void removeNode(Node sourceNode) {
		// remove from cash should be called first
		domMapping.remove(sourceNode);
		getSourceNodes().remove(sourceNode);
		if (sourceNode instanceof INodeNotifier) {
			((INodeNotifier) sourceNode).removeAdapter(getSorceAdapter());
		}
	}

	private Node getParentTable(Node sourceNode, int depth) {
		Node parentNode = sourceNode.getParentNode();
		for (int i = 0; parentNode != null && i < depth; parentNode = parentNode
				.getParentNode(), i++) {
			if (HTML.TAG_TABLE.equalsIgnoreCase(parentNode.getNodeName())) {
				return parentNode;
			}
		}
		return null;
	}

	private Node getParentSelect(Node sourceNode) {
		if (HTML.TAG_OPTION.equalsIgnoreCase(sourceNode.getNodeName())) {
			Node parentNode = sourceNode.getParentNode();
			if (HTML.TAG_SELECT.equalsIgnoreCase(parentNode.getNodeName())) {
				return parentNode;
			}
		}
		return null;
	}

	public boolean setText(Node sourceText) {
		Node sourceParent = sourceText.getParentNode();
		if (sourceParent != null && sourceParent.getLocalName() != null) {
			String sourceParentName = sourceParent.getLocalName();
			if (HTML.TAG_TEXTAREA.equalsIgnoreCase(sourceParentName)
					|| HTML.TAG_OPTION.equalsIgnoreCase(sourceParentName)
					|| HTML.TAG_STYLE.equalsIgnoreCase(sourceParentName)) {
				updateNode(sourceText.getParentNode());
				return true;
			}
		}
		nsIDOMNode visualText = domMapping.getVisualNode(sourceText);
		if (visualText != null) {
			String visualValue = TextUtil.visualText(sourceText.getNodeValue());
			visualText.setNodeValue(visualValue);
		} else {
			VpeNodeMapping nodeMapping = domMapping
					.getNodeMapping(sourceParent);
			if (nodeMapping != null && nodeMapping instanceof VpeElementMapping) {
				VpeTemplate template = ((VpeElementMapping) nodeMapping)
						.getTemplate();
				if (template != null) {
					if (!template.containsText()) {
						return false;
					}
				}
			}
			updateNode(sourceText);
			return true;
		}

		// }
		// updateNode(sourceText);
		return false;
	}

	// }

	public void setAttribute(Element sourceElement, String name, String value) {
		VpeElementMapping elementMapping = (VpeElementMapping) domMapping
				.getNodeMapping(sourceElement);
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4110
		 * Update any template automatically on attribute adding.
		 */
		if (elementMapping != null) {
		    updateElement(sourceElement);
		}
	}

	public void stopToggle(Node sourceNode) {
		if (!(sourceNode instanceof Element))
			return;

		Element sourceElement = (Element) sourceNode;
		VpeElementMapping elementMapping = (VpeElementMapping) domMapping
				.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate();

			if (template instanceof VpeToggableTemplate) {
				((VpeToggableTemplate) template).stopToggling(sourceElement);
			}
		}
	}

	public boolean doToggle(nsIDOMNode visualNode) {
		if (visualNode == null) {
			return false;
		}
		nsIDOMElement visualElement = null;
		try {
			visualElement = (nsIDOMElement) visualNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		} catch (XPCOMException exception) {
			visualElement = (nsIDOMElement) visualNode.getParentNode()
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		}
		if (visualElement == null) {
			return false;
		}

		nsIDOMAttr toggleIdAttr = visualElement
				.getAttributeNode(VPE_USER_TOGGLE_ID);
		if (toggleIdAttr == null) {
			return false;
		}
		String toggleId = toggleIdAttr.getNodeValue();

		if (toggleId == null) {
			return false;
		}

		boolean toggleLookup = false;
		nsIDOMAttr toggleLookupAttr = visualElement
				.getAttributeNode(VPE_USER_TOGGLE_LOOKUP_PARENT);
		if (toggleLookupAttr != null) {
			toggleLookup = "true".equals(toggleLookupAttr.getNodeValue()); //$NON-NLS-1$
		}

		nsIDOMElement selectedElem = getLastSelectedElement();
		// Fixes JBIDE-1823 author dmaliarevich
		if (null == selectedElem) {
			return false;
		}
		VpeElementMapping elementMapping = null;
		VpeNodeMapping nodeMapping = domMapping.getNodeMapping(selectedElem);
		if (nodeMapping instanceof VpeElementMapping) {
			elementMapping = (VpeElementMapping) nodeMapping;
		}
		// end of fix
		if (elementMapping == null) {
			// may be toggle with facet
			while (!selectedElem.getNodeName().equals(HTML.TAG_TABLE)) {
				selectedElem = (nsIDOMElement) selectedElem.getParentNode()
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			}
			// Fixes JBIDE-1823 author dmaliarevich
			nodeMapping = domMapping.getNodeMapping(selectedElem);
			if (nodeMapping instanceof VpeElementMapping) {
				elementMapping = (VpeElementMapping) nodeMapping;
			}
			// end of fix
		}
		Node sourceNode = (Node) domMapping.getSourceNode(selectedElem);
		if (sourceNode == null) {
			return false;
		}

		Element sourceElement = (Element) (sourceNode instanceof Element ? sourceNode
				: sourceNode.getParentNode());

		// Fixes JBIDE-1823 author dmaliarevich
		// Template is looked according to <code>selectedElem</code>
		// so <code>toggleLookupAttr</code> should be retrieved
		// from this element
		toggleLookupAttr = selectedElem
				.getAttributeNode(VPE_USER_TOGGLE_LOOKUP_PARENT);
		if (toggleLookupAttr != null) {
			toggleLookup = "true".equals(toggleLookupAttr.getNodeValue()); //$NON-NLS-1$
		}
		// end of fix

		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate();

			while (toggleLookup && sourceElement != null
					&& !(template instanceof VpeToggableTemplate)) {
				sourceElement = (Element) sourceElement.getParentNode();
				if (sourceElement == null) {
					break;
				}
				// Fixes JBIDE-1823 author dmaliarevich
				nodeMapping = domMapping.getNodeMapping(sourceElement);
				if (nodeMapping instanceof VpeElementMapping) {
					elementMapping = (VpeElementMapping) nodeMapping;
				}
				// end of fix
				if (elementMapping == null) {
					continue;
				}
				template = elementMapping.getTemplate();
			}

			if (template instanceof VpeToggableTemplate) {
				((VpeToggableTemplate) template).toggle(this, sourceElement,
						toggleId);
				updateElement(sourceElement);
				return true;
			}
		}
		return false;
	}

	public void removeAttribute(Element sourceElement, String name) {
		VpeElementMapping elementMapping = (VpeElementMapping) domMapping
				.getNodeMapping(sourceElement);
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4110
		 * Update any template automatically on attribute deleting.
		 */
		if (elementMapping != null) {
		    updateElement(sourceElement);
		}
	}

	public void refreshBundleValues(Element sourceElement) {
		VpeElementMapping elementMapping = (VpeElementMapping) domMapping
				.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate();
			template.refreshBundleValues(pageContext, sourceElement,
					elementMapping.getData());
		}
	}

	boolean isContentArea(nsIDOMNode visualNode) {
		return getContentArea().equals(visualNode);
	}

	public nsIDOMElement getContentArea() {
		return visualEditor.getContentArea();
	}

	public void setSelectionRectangle(/* nsIDOMElement */nsIDOMNode visualElement) {
		setSelectionRectangle(visualElement, true);
	}

	void setSelectionRectangle(/* nsIDOMElement */nsIDOMNode visualElement,
			boolean scroll) {
		int resizerConstrains = getResizerConstrains(visualElement);
		visualEditor.setSelectionRectangle(visualElement, resizerConstrains,
				scroll);
	}

    /**
     * 
     * @param href_val
     * @param ext_val
     * @param firstElement
     *            true - first node in head, false - last node
     * @return
     */
    public nsIDOMNode addLinkNodeToHead(String href_val, String ext_val,
	    boolean firstElement) {
	nsIDOMElement newNode = createLinkNode(href_val,
		ATTR_REL_STYLESHEET_VALUE, ext_val);

	// TODO Dzmitry Sakovich
	// Fix priority CSS classes JBIDE-1713
	if (firstElement) {
	    nsIDOMNode firstNode = getHeadNode().getFirstChild();
	    getHeadNode().insertBefore(newNode, firstNode);
	} else {
	    getHeadNode().appendChild(newNode);
	}
	return newNode;
    }

	public nsIDOMNode replaceLinkNodeToHead(nsIDOMNode oldNode,
			String href_val, String ext_val) {
		nsIDOMNode newNode = createLinkNode(href_val,
				ATTR_REL_STYLESHEET_VALUE, ext_val);
		getHeadNode().replaceChild(newNode, oldNode);
		return newNode;
	}

	public nsIDOMNode replaceLinkNodeToHead(String href_val, String ext_val, boolean firstElement) {
		nsIDOMNode newNode = null;
		nsIDOMNode oldNode = getLinkNode(href_val, ext_val);
		if (oldNode == null) {
			newNode = addLinkNodeToHead(href_val, ext_val, firstElement);
		}
		return newNode;
	}

	public void removeLinkNodeFromHead(nsIDOMNode node) {
		getHeadNode().removeChild(node);
	}

	private nsIDOMElement createLinkNode(String href_val, String rel_val,
			String ext_val) {
		nsIDOMElement linkNode = null;
		if ((ATTR_REL_STYLESHEET_VALUE.equalsIgnoreCase(rel_val))
				&& href_val.startsWith("file:")) { //$NON-NLS-1$
			/*
			 * Because of the Mozilla caches the linked css files we replace tag
			 * <link rel="styleseet" href="file://..."> with tag <style
			 * vpe="ATTR_VPE_INLINE_LINK_VALUE">file content</style> It is
			 * LinkReplacer
			 */
			linkNode = getVisualDocument().createElement(HTML.TAG_STYLE);
			linkNode.setAttribute(ATTR_VPE, ATTR_VPE_INLINE_LINK_VALUE);

			/* Copy links attributes into our <style> */
			linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_HREF, href_val);
			linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_EXT, ext_val);
			BufferedReader in = null;
			try {
				StringBuffer styleText = new StringBuffer(EMPTY_STRING);
				URL url = new URL((new Path(href_val)).toOSString());
				String fileName = url.getFile();
				in = new BufferedReader(new FileReader(
						(fileName)));
				String str = EMPTY_STRING;
				while ((str = in.readLine()) != null) {
					styleText.append(str);
				}

				String styleForParse = styleText.toString();
				styleForParse = VpeStyleUtil.addFullPathIntoURLValue(
						styleForParse, href_val);

				in.close();
				nsIDOMText textNode = getVisualDocument()
						.createTextNode(styleForParse);
				linkNode.appendChild(textNode);
				return linkNode;
			} catch (FileNotFoundException fnfe) {
				/* File which was pointed by user is not exists. Do nothing. */
			} catch (IOException ioe) {
				VpePlugin.getPluginLog().logError(ioe.getMessage(), ioe);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						VpePlugin.getPluginLog().logError(e);
					}
				}
			}
		}

		linkNode = getVisualDocument().createElement(HTML.TAG_LINK);
		linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_REL, rel_val);
		linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_HREF, href_val);
		linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_EXT, ext_val);

		return linkNode;
	}

	private boolean isLinkReplacer(nsIDOMNode node) {
		return HTML.TAG_STYLE.equalsIgnoreCase(node.getNodeName())
				&& ATTR_VPE_INLINE_LINK_VALUE
						.equalsIgnoreCase(((nsIDOMElement) node
								.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))
								.getAttribute(ATTR_VPE));
	}

	private nsIDOMNode getLinkNode(String href_val, String ext_val) {
		nsIDOMNodeList children = getHeadNode().getChildNodes();
		long len = children.getLength();
		for (long i = len - 1; i >= 0; i--) {
			nsIDOMNode node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (HTML.TAG_LINK.equalsIgnoreCase(node.getNodeName())
						|| isLinkReplacer(node)) {
					nsIDOMElement element = (nsIDOMElement) node
							.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
					if (ext_val.equalsIgnoreCase(element
							.getAttribute(VpeTemplateManager.ATTR_LINK_EXT))
							&& href_val
									.equalsIgnoreCase(element
											.getAttribute(VpeTemplateManager.ATTR_LINK_HREF))) {
						return node;
					}
				}
			}
		}
		return null;
	}

	private void cleanHead() {
		//Fix for JBIDE-3205, mareshkau
		if(getHeadNode()==null) {
			return;
		}
		nsIDOMNodeList children = getHeadNode().getChildNodes();
		long len = children.getLength();
		for (long i = len - 1; i >= 0; i--) {
			nsIDOMNode node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (isLinkReplacer(node)) {
					/*
					 * Added by Max Areshkau(Fix for JBIDE-1941) Ext. attribute
					 * used for adding external styles to editor. If was added
					 * external attribute, this property is true.
					 */
					if (!YES_STRING.equalsIgnoreCase(((nsIDOMElement) node
							.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))
							.getAttribute(VpeTemplateManager.ATTR_LINK_EXT))) {
						// int linkAddress =
						// MozillaSupports.queryInterface(node,
						// nsIStyleSheetLinkingElement.
						// NS_ISTYLESHEETLINKINGELEMENT_IID);
						// nsIStyleSheetLinkingElement linkingElement = new
						// nsIStyleSheetLinkingElement(linkAddress);
						// linkingElement.removeStyleSheet();
						node = getHeadNode().removeChild(node);
					}
				} else if (HTML.TAG_STYLE.equalsIgnoreCase(node.getNodeName())
						&& (!YES_STRING
								.equalsIgnoreCase(((nsIDOMElement) node
										.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))
										.getAttribute(ATTR_VPE)))) {
					node = getHeadNode().removeChild(node);
				}
			}
		}
		
	}

	private static class PseudoInfo {
		private nsIDOMNode pseudoNode;
		private boolean isElements;

		private PseudoInfo() {
			this(null, false);
		}

		private PseudoInfo(nsIDOMNode pseudoNode, boolean isElements) {
			this.pseudoNode = pseudoNode;
			this.isElements = isElements;
		}
	}

	void showDragCaret(nsIDOMNode node, int offset) {
		xulRunnerEditor.showDragCaret(node, offset);
	}

	void hideDragCaret() {

		xulRunnerEditor.hideDragCaret();
	}

	private int getResizerConstrains(nsIDOMNode visualNode) {
		VpeNodeMapping nodeMapping = domMapping.getNodeMapping(visualNode);
		if (nodeMapping != null
				&& (nodeMapping instanceof VpeElementMapping)
				&& (nodeMapping.getSourceNode() instanceof Element)
				&& (nodeMapping.getVisualNode().getNodeType() == nsIDOMNode.ELEMENT_NODE)) {
			return ((VpeElementMapping) nodeMapping).getTemplate()
					.getTagDescription(
							pageContext,
							(Element) nodeMapping.getSourceNode(),
							getVisualDocument(),
							(nsIDOMElement) nodeMapping.getVisualNode()
									.queryInterface(
											nsIDOMElement.NS_IDOMELEMENT_IID),
							((VpeElementMapping) nodeMapping).getData())
					.getResizeConstrains();
		}
		return VpeTagDescription.RESIZE_CONSTRAINS_NONE;
	}

	public void resize(nsIDOMElement element, int constrains, int top,
			int left, int width, int height) {
		VpeElementMapping elementMapping = (VpeElementMapping) domMapping
				.getNodeMapping(element);
		if (elementMapping != null) {
			elementMapping.getTemplate().resize(pageContext,
					(Element) elementMapping.getSourceNode(), getVisualDocument(),
					element, elementMapping.getData(), constrains, top,
					left, width, height);
		}
	}

	static boolean isAnonElement(nsIDOMNode visualNode) {
		if (visualNode != null
				&& visualNode.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
			String attrValue = ((nsIDOMElement) visualNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID))
					.getAttribute(MOZ_ANONCLASS_ATTR);

			return attrValue != null && attrValue.length() > 0;
		}

		return false;
	}

	boolean canInnerDrag(nsIDOMElement visualDragElement) {
		VpeNodeMapping node = domMapping.getNodeMapping(visualDragElement);
		if (node instanceof VpeElementMapping) {
			VpeElementMapping elementMapping = (VpeElementMapping) node;
			if (elementMapping != null
					&& elementMapping.getSourceNode() instanceof Element) {
				return elementMapping.getTemplate().canInnerDrag(pageContext,
						(Element) elementMapping.getSourceNode(),
						getVisualDocument(), visualDragElement,
						elementMapping.getData());
			}
		}
		return false;
	}

	VpeSourceInnerDropInfo getSourceInnerDropInfo(Node sourceDragNode,
			VpeVisualInnerDropInfo visualDropInfo, boolean checkParentTemplates) {
		nsIDOMNode visualDropContainer = visualDropInfo.getDropContainer();
		long visualDropOffset = visualDropInfo.getDropOffset();
		Node sourceDropContainer = null;
		int sourceDropOffset = 0;

		switch (visualDropContainer.getNodeType()) {
		case nsIDOMNode.ELEMENT_NODE:
			nsIDOMNode visualOffsetNode = null;
			boolean afterFlag = false;
			long visualChildCount = VisualDomUtil
					.getChildCount(visualDropContainer);
			if (visualDropOffset < visualChildCount) {
				visualOffsetNode = VisualDomUtil.getChildNode(
						visualDropContainer, visualDropOffset);
				if (isPseudoElement(visualOffsetNode)
						|| isAnonElement(visualOffsetNode)) {
					visualOffsetNode = getLastAppreciableVisualChild(visualDropContainer);
					afterFlag = true;
				}
			} else {
				visualOffsetNode = getLastAppreciableVisualChild(visualDropContainer);
				afterFlag = visualChildCount != 0;
			}
			if (visualOffsetNode != null) {
				Node sourceOffsetNode = domMapping
						.getSourceNode(visualOffsetNode);
				if (sourceOffsetNode != null) {
					sourceDropContainer = sourceOffsetNode.getParentNode();
					sourceDropOffset = ((NodeImpl) sourceOffsetNode).getIndex();
					if (afterFlag) {
						sourceDropOffset++;
					}
				}
			}
			if (sourceDropContainer == null) {
				sourceDropContainer = domMapping
						.getNearSourceNode(visualDropContainer);
				if (sourceDropContainer != null) {
					sourceDropOffset = sourceDropContainer.getChildNodes()
							.getLength();
				}
			}
			if (sourceDropContainer == null) {
				sourceDropContainer = domMapping
						.getNearSourceNode(getContentArea());
				sourceDropOffset = sourceDropContainer.getChildNodes()
						.getLength();
			}
			break;
		case nsIDOMNode.TEXT_NODE:
			VpeNodeMapping nodeMapping = domMapping
					.getNearNodeMapping(visualDropContainer);
			// switch (nodeMapping.getType()) {
			// case VpeNodeMapping.TEXT_MAPPING:
			sourceDropContainer = nodeMapping.getSourceNode();
			sourceDropOffset = TextUtil.sourceInnerPosition(sourceDropContainer
					.getNodeValue(), visualDropOffset);
			// break;
			// case VpeNodeMapping.ELEMENT_MAPPING:
			// it's attribute
			if (isTextEditable(visualDropContainer)) {
				String[] atributeNames = ((VpeElementMapping) nodeMapping)
						.getTemplate().getOutputAttributeNames();
				if (atributeNames != null && atributeNames.length > 0) {
					Element sourceElement = (Element) nodeMapping
							.getSourceNode();
					sourceDropContainer = sourceElement
							.getAttributeNode(atributeNames[0]);
					sourceDropOffset = TextUtil.sourceInnerPosition(
							sourceDropContainer.getNodeValue(),
							visualDropOffset);
				}
			}
			nodeMapping.getVisualNode();
			// }
			// break;
		}
		if (sourceDropContainer != null) {
			return getSourceInnerDropInfo(sourceDragNode, sourceDropContainer,
					sourceDropOffset, checkParentTemplates);
		} else {
			return new VpeSourceInnerDropInfo(null, 0, false);
		}
	}

	VpeSourceInnerDropInfo getSourceInnerDropInfo(Node dragNode,
			Node container, int offset, boolean checkParentsTemplates) {
		// Thread.dumpStack();
		boolean canDrop = false;
		switch (container.getNodeType()) {
		case Node.ELEMENT_NODE:
			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(container);
			if (nodeMapping != null && nodeMapping instanceof VpeElementMapping) {
				canDrop = ((VpeElementMapping) nodeMapping).getTemplate()
						.canInnerDrop(pageContext, container, dragNode);
			}
			if (!canDrop) {
				if (!checkParentsTemplates)
					return new VpeSourceInnerDropInfo(container, offset,
							canDrop);
				// offset = ((NodeImpl)container).getIndex();
				// container = container.getParentNode();
				// TODO Max Areshkau unclear logic , if we can drop on element
				// why we trying to drop
				// this on parent
				// return getSourceInnerDropInfo(dragNode, container, offset,
				// false);
				return new VpeSourceInnerDropInfo(container, offset, canDrop);
			}
			break;
		case Node.TEXT_NODE:
		case Node.DOCUMENT_NODE:
			canDrop = true;
			break;
		case Node.ATTRIBUTE_NODE:
			canDrop = true;
			break;
		}
		if (canDrop) {
			return new VpeSourceInnerDropInfo(container, offset, canDrop);
		} else {
			return new VpeSourceInnerDropInfo(null, 0, canDrop);
		}
	}

	public void innerDrop(Node dragNode, Node container, int offset) {
		VpeNodeMapping mapping = domMapping.getNearNodeMapping(container);
		if (mapping != null) {
			nsIDOMNode visualDropContainer = mapping.getVisualNode();
			// switch (mapping.getType()) {
			// case VpeNodeMapping.TEXT_MAPPING:
			// break;
			// case VpeNodeMapping.ELEMENT_MAPPING:
			nsIDOMNode visualParent = visualDropContainer.getParentNode();
			VpeNodeMapping oldMapping = mapping;
			mapping = domMapping.getNearNodeMapping(visualParent);
			if (mapping != null && mapping instanceof VpeElementMapping) {
				((VpeElementMapping) mapping).getTemplate().innerDrop(
						pageContext,
						new VpeSourceInnerDragInfo(dragNode, 0, 0),
						new VpeSourceInnerDropInfo(container, offset, true));
			} else {
				if (oldMapping instanceof VpeElementMapping) {
					((VpeElementMapping) oldMapping).getTemplate().innerDrop(
							pageContext,
							new VpeSourceInnerDragInfo(dragNode, 0, 0),
							new VpeSourceInnerDropInfo(container, offset, true));
				} else {
					/* TODO: implement this case or completely
					 * remove this method?
					 * At the time of writing this comment 
					 * the implementation of template.innerDrop() method above
					 * was empty, so there are no differences between
					 * calling this method and doing nothing.
					 */
				}
			}
			// }

		}
	}

	void innerDrop(VpeSourceInnerDragInfo dragInfo,
			VpeSourceInnerDropInfo dropInfo) {
		dropper.drop(pageContext, dragInfo, dropInfo);
	}

	nsIDOMElement getNearDragElement(Element visualElement) {
		VpeElementMapping elementMapping = domMapping
				.getNearElementMapping(visualElement);
		while (elementMapping != null) {
			if (canInnerDrag(elementMapping.getVisualElement())) {
				return elementMapping.getVisualElement();
			}
			elementMapping = domMapping.getNearElementMapping(elementMapping
					.getVisualNode().getParentNode());
		}
		return null;
	}

	nsIDOMElement getDragElement(nsIDOMElement visualElement) {
		VpeElementMapping elementMapping = domMapping
				.getNearElementMapping(visualElement);
		if (elementMapping != null
				&& canInnerDrag(elementMapping.getVisualElement())) {
			return elementMapping.getVisualElement();
		}
		return null;
	}

	public boolean isTextEditable(nsIDOMNode visualNode) {

		if (visualNode != null) {
			nsIDOMNode parent = visualNode.getParentNode();
			if (parent != null
					&& parent.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				nsIDOMElement element = (nsIDOMElement) parent
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				nsIDOMAttr style = element.getAttributeNode("style"); //$NON-NLS-1$
				if (style != null) {
					String styleValue = style.getNodeValue();
					String[] items = styleValue.split(";"); //$NON-NLS-1$
					for (int i = 0; i < items.length; i++) {
						String[] item = items[i].split(":"); //$NON-NLS-1$
						if ("-moz-user-modify".equals(item[0].trim()) //$NON-NLS-1$
								&& "read-only".equals(item[1].trim())) { //$NON-NLS-1$
							return false;
						}
					}
				}
				nsIDOMAttr classAttr = element.getAttributeNode("class"); //$NON-NLS-1$
				if (classAttr != null) {
					String classValue = classAttr.getNodeValue().trim();
					if ("__any__tag__caption".equals(classValue)) { //$NON-NLS-1$
						return false;
					}
				}
			}
		}
		return true;
	}

	VpeVisualInnerDropInfo getInnerDropInfo(Node sourceDropContainer,
			int sourceDropOffset) {
		nsIDOMNode visualDropContainer = null;
		long visualDropOffset = 0;

		switch (sourceDropContainer.getNodeType()) {
		case Node.TEXT_NODE:
			visualDropContainer = domMapping.getVisualNode(sourceDropContainer);
			visualDropOffset = TextUtil.visualInnerPosition(sourceDropContainer
					.getNodeValue(), sourceDropOffset);
			break;
		case Node.ELEMENT_NODE:
		case Node.DOCUMENT_NODE:
			NodeList sourceChildren = sourceDropContainer.getChildNodes();
			if (sourceDropOffset < sourceChildren.getLength()) {
				Node sourceChild = sourceChildren.item(sourceDropOffset);
				nsIDOMNode visualChild = domMapping.getVisualNode(sourceChild);
				if (visualChild != null) {
					visualDropContainer = visualChild.getParentNode();

					visualDropOffset = VisualDomUtil.getOffset(visualChild);
				}
			}
			if (visualDropContainer == null) {
				visualDropContainer = domMapping
						.getNearVisualNode(sourceDropContainer);
				nsIDOMNode visualChild = getLastAppreciableVisualChild(visualDropContainer);
				if (visualChild != null) {
					visualDropOffset = VisualDomUtil.getOffset(visualChild) + 1;
				} else {
					visualDropOffset = 0;
				}
			}
			break;
		case Node.ATTRIBUTE_NODE:
			Element sourceElement = ((Attr) sourceDropContainer)
					.getOwnerElement();
			VpeElementMapping elementMapping = domMapping
					.getNearElementMapping(sourceElement);
			nsIDOMNode textNode = elementMapping.getTemplate()
					.getOutputTextNode(pageContext, sourceElement,
							elementMapping.getData());
			if (textNode != null) {
				visualDropContainer = textNode;
				visualDropOffset = TextUtil.visualInnerPosition(
						sourceDropContainer.getNodeValue(), sourceDropOffset);
			}
			break;
		}
		if (visualDropContainer == null) {
			return null;
		}
		return new VpeVisualInnerDropInfo(visualDropContainer,
				visualDropOffset, 0, 0);
	}

	protected void setTooltip(Element sourceElement, nsIDOMElement visualElement) {
		if (visualElement != null && sourceElement != null
				&& !((IDOMElement) sourceElement).isJSPTag()) {
			if (HTML.TAG_HTML.equalsIgnoreCase(sourceElement.getNodeName()))
				return;
			String titleValue = getTooltip(sourceElement);

			if (titleValue != null) {
				titleValue = titleValue.replaceAll("&", "&amp;"); //$NON-NLS-1$ //$NON-NLS-2$
				titleValue = titleValue.replaceAll("<", "&lt;"); //$NON-NLS-1$ //$NON-NLS-2$
				titleValue = titleValue.replaceAll(">", "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if (titleValue != null) {
				// visualElement.setAttribute("title", titleValue);
				setTooltip(visualElement, titleValue);
			}
		}
	}

	protected void setTooltip(nsIDOMElement visualElement, String titleValue) {
		visualElement.setAttribute(HTML.ATTR_TITLE, titleValue);
		nsIDOMNodeList children = visualElement.getChildNodes();
		long len = children.getLength();
		for (long i = 0; i < len; i++) {
			nsIDOMNode child = children.item(i);
			if (child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				setTooltip(((nsIDOMElement) child
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID)),
						titleValue);
			}
		}
	}
	
	private void resetTooltip(nsIDOMElement visualElement, String titleValue) {
		visualElement.setAttribute(HTML.ATTR_TITLE, titleValue);
		nsIDOMNodeList children = visualElement.getChildNodes();
		long len = children.getLength();
		for (long i = 0; i < len; i++) {
			nsIDOMNode child = children.item(i);
			if (child.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				if (domMapping.getNodeMapping(child) == null) {
					resetTooltip((nsIDOMElement) child
							.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID),
							titleValue);
				}
			}
		}
	}

	private String getTooltip(Element sourceElement) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(sourceElement.getNodeName());
		NamedNodeMap attrs = sourceElement.getAttributes();
		int len = attrs.getLength();
		for (int i = 0; i < len; i++) {
			if (i == 7) {
				return buffer.append("\n\t... ").toString(); //$NON-NLS-1$
			}
			int valueLength = attrs.item(i).getNodeValue().length();
			if (valueLength > 30) {
				StringBuffer temp = new StringBuffer();
				temp.append(attrs.item(i).getNodeValue().substring(0, 15)
						+ " ... " //$NON-NLS-1$
						+ attrs.item(i).getNodeValue().substring(
								valueLength - 15, valueLength));
				buffer.append("\n" + attrs.item(i).getNodeName() + ": " + temp); //$NON-NLS-1$ //$NON-NLS-2$
			} else
				buffer.append("\n" + attrs.item(i).getNodeName() + ": " //$NON-NLS-1$ //$NON-NLS-2$
						+ attrs.item(i).getNodeValue());

		}

		return buffer.toString();
	}

	Rectangle getNodeBounds(nsIDOMNode visualNode) {

		return XulRunnerVpeUtils.getElementBounds(visualNode);
	}

	static boolean canInsertAfter(int x, int y, Rectangle rect) {
		if (y > (rect.y + rect.height) || x > (rect.x + rect.width)) {
			return true;
		}
		return y >= rect.x && x > (rect.x + rect.width / 2);
	}

	static nsIDOMNode getLastAppreciableVisualChild(nsIDOMNode visualParent) {
		nsIDOMNode visualLastChild = null;
		nsIDOMNodeList visualChildren = visualParent.getChildNodes();
		long len = visualChildren.getLength();
		for (long i = len - 1; i >= 0; i--) {
			nsIDOMNode visualChild = visualChildren.item(i);
			if (!isPseudoElement(visualChild) && !isAnonElement(visualChild)) {
				visualLastChild = visualChild;
				break;
			}
		}
		return visualLastChild;
	}

	void correctVisualDropPosition(VpeVisualInnerDropInfo newVisualDropInfo,
			VpeVisualInnerDropInfo oldVisualDropInfo) {
		nsIDOMNode newVisualDropContainer = newVisualDropInfo
				.getDropContainer();
		nsIDOMNode oldVisualDropContainer = oldVisualDropInfo
				.getDropContainer();

		if (newVisualDropContainer.equals(oldVisualDropContainer)) {
			newVisualDropInfo.setDropOffset(oldVisualDropInfo.getDropOffset());
			return;
		}

		nsIDOMNode child = oldVisualDropContainer;
		while (child != null && child.getNodeType() != Node.DOCUMENT_NODE) {
			nsIDOMNode parent = child.getParentNode();
			if (newVisualDropContainer.equals(parent)) {
				long offset = VisualDomUtil.getOffset(child);
				Rectangle rect = getNodeBounds(child);
				if (canInsertAfter(oldVisualDropInfo.getMouseX(),
						oldVisualDropInfo.getMouseY(), rect)) {
					offset++;
				}
				newVisualDropInfo.setDropOffset(offset);
			}
			child = parent;
		}
	}

	public nsIDOMRange createDOMRange() {
		return xulRunnerEditor.createDOMRange();
	}

	public nsIDOMRange createDOMRange(nsIDOMNode selectedNode) {
		nsIDOMRange range = createDOMRange();
		range.selectNode(selectedNode);
		return range;
	}

	public static boolean isIncludeElement(nsIDOMElement visualElement) {
		return YES_STRING.equalsIgnoreCase(visualElement
				.getAttribute(INCLUDE_ELEMENT_ATTR));
	}

	public static void markIncludeElement(nsIDOMElement visualElement) {
		visualElement.setAttribute(INCLUDE_ELEMENT_ATTR, YES_STRING);
	}

	protected void setReadOnlyElement(nsIDOMElement node) {
		String style = node.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
		style = VpeStyleUtil.setParameterInStyle(style, "-moz-user-modify", //$NON-NLS-1$
				"read-only"); //$NON-NLS-1$
		node.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
	}

	void setMoveCursor(nsIDOMMouseEvent mouseEvent) {

		nsIDOMElement selectedElement = xulRunnerEditor
				.getLastSelectedElement();
		if (selectedElement != null && canInnerDrag(selectedElement)) {
			String styleClasses = selectedElement.getAttribute(HTML.ATTR_CLASS);
			if (inDragArea(getNodeBounds(selectedElement), VisualDomUtil
					.getMousePoint(mouseEvent))) {
				// change cursor
				if (styleClasses == null
						|| !(styleClasses.contains(ATTR_DRAG_AVAILABLE_CLASS))) {
					// change cursor style to move
					styleClasses = ATTR_DRAG_AVAILABLE_CLASS + " " //$NON-NLS-1$
							+ styleClasses;
				}
				//if mouse in drag area, and left button is pressed than we should
				//start drag session manually.
			} else {
				// change cursor style to normal
				if (styleClasses != null) {

					styleClasses = styleClasses.replaceAll(
							ATTR_DRAG_AVAILABLE_CLASS, ""); //$NON-NLS-1$
				}
			}
			selectedElement.setAttribute(HTML.ATTR_CLASS, styleClasses);
		}
	}

	public static boolean inDragArea(Rectangle dragArea, Point mousePoint) {
		// TODO add drag and drop support
		return dragArea.contains(mousePoint)
				&& mousePoint.x < (dragArea.x + DRAG_AREA_WIDTH)
				&& mousePoint.y < (dragArea.y + DRAG_AREA_HEIGHT);
	}

	nsIDOMElement getDragElement(nsIDOMMouseEvent mouseEvent) {

		nsIDOMElement selectedElement = xulRunnerEditor
				.getLastSelectedElement();
		if (selectedElement != null && canInnerDrag(selectedElement)) {
			if (inDragArea(getNodeBounds(selectedElement), VisualDomUtil
					.getMousePoint(mouseEvent))) {
				return selectedElement;
			}
		}
		return null;
	}

	VpeSourceInnerDragInfo getSourceInnerDragInfo(
			VpeVisualInnerDragInfo visualDragInfo) {
		nsIDOMNode visualNode = visualDragInfo.getNode();
		int offset = visualDragInfo.getOffset();
		int length = visualDragInfo.getLength();

		VpeNodeMapping nodeMapping = domMapping.getNearNodeMapping(visualNode);
		Node sourceNode = nodeMapping.getSourceNode();

		if (sourceNode != null) {
			switch (sourceNode.getNodeType()) {
			case Node.TEXT_NODE:
				int end = TextUtil.sourceInnerPosition(visualNode
						.getNodeValue(), offset + length);
				offset = TextUtil.sourceInnerPosition(
						visualNode.getNodeValue(), offset);
				length = end - offset;
				break;
			case Node.ELEMENT_NODE:
				if (visualNode.getNodeType() == Node.TEXT_NODE) {
					// it's attribute
					sourceNode = null;
					if (isTextEditable(visualNode)) {
						String[] atributeNames = ((VpeElementMapping) nodeMapping)
								.getTemplate().getOutputAttributeNames();
						if (atributeNames != null && atributeNames.length > 0) {
							Element sourceElement = (Element) nodeMapping
									.getSourceNode();
							sourceNode = sourceElement
									.getAttributeNode(atributeNames[0]);
							end = TextUtil.sourceInnerPosition(visualNode
									.getNodeValue(), offset + length);
							offset = TextUtil.sourceInnerPosition(visualNode
									.getNodeValue(), offset);
							length = end - offset;
						}
					}
				}
				break;
			}
		}
		return new VpeSourceInnerDragInfo(sourceNode, offset, length);
	}

	public nsIDOMText getOutputTextNode(Attr attr) {
		Element sourceElement = ((Attr) attr).getOwnerElement();
		VpeElementMapping elementMapping = domMapping
				.getNearElementMapping(sourceElement);
		if (elementMapping != null) {

			return elementMapping.getTemplate().getOutputTextNode(pageContext,
					sourceElement, elementMapping.getData());
		}
		return null;
	}

	nsIDOMElement getLastSelectedElement() {

		return xulRunnerEditor.getLastSelectedElement();
	}

	public void pushIncludeStack(VpeIncludeInfo includeInfo) {
		includeStack.add(includeInfo);
	}

	public VpeIncludeInfo popIncludeStack() {
		VpeIncludeInfo includeInfo = null;
		if (includeStack.size() > 0) {
			includeInfo = (VpeIncludeInfo) includeStack.remove(includeStack
					.size() - 1);
		}
		return includeInfo;
	}

	public boolean isFileInIncludeStack(IStorage file) {
		if (file == null)
			return false;
		for (int i = 0; i < includeStack.size(); i++) {
			if (file.equals(((VpeIncludeInfo) includeStack.get(i)).getStorage())) {
				return true;
			}
		}
		return false;
	}

	public boolean isCurrentMainDocument() {
		return includeStack.size() <= 1;
	}

	public int getCurrentMainIncludeOffset() {
		if (includeStack.size() <= 1)
			return -1;
		VpeIncludeInfo info = (VpeIncludeInfo) includeStack.get(1);
		return ((IndexedRegion) info.getElement()).getStartOffset();
	}
	/**
	 * Can be a null in some cases, for example when we open an external file, see JBIDE-3030
	 * @return file include info
	 */
	public VpeIncludeInfo getCurrentIncludeInfo() {
		if (includeStack.size() <= 0)
			return null;
		return (VpeIncludeInfo) includeStack.get(includeStack.size() - 1);
	}

	public VpeIncludeInfo getRootIncludeInfo() {
		if (includeStack.size() <= 1)
			return null;
		return (VpeIncludeInfo) includeStack.get(1);
	}

	public void dispose() {
		clearIncludeDocuments();
		includeDocuments = null;
		cleanHead();
		domMapping.clear(getContentArea());
		pageContext.dispose();
		super.dispose();
	}

	private void clearIncludeDocuments() {
		Collection<Document> documents = includeDocuments.values();
		for (Iterator iterator = documents.iterator(); iterator.hasNext();) {
			Document document = (Document) iterator.next();
			VpeCreatorUtil.releaseDocumentFromRead(document);
		}
		includeDocuments.clear();
	}

	/**
	 * @return the dnd
	 */
	public VpeDnD getDnd() {

		return dnd;
	}

	/**
	 * @param dnd
	 *            the dnd to set
	 */
	public void setDnd(VpeDnD dnd) {
		this.dnd = dnd;
	}

	/**
	 * @return the pageContext
	 */
	protected VpePageContext getPageContext() {
		return pageContext;
	}

	/**
	 * @param pageContext
	 *            the pageContext to set
	 */
	protected void setPageContext(VpePageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
	 * @return the visualDocument
	 */
	protected nsIDOMDocument getVisualDocument() {
		return visualEditor.getDomDocument();
	}

	/**
	 * Check this file is facelet
	 * 
	 * @return this if file is facelet, otherwize false
	 */
	private boolean isFacelet(Node sourceNode) {
		boolean isFacelet = false;
		String sourcePrefix = sourceNode.getPrefix();
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode, pageContext);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);
		if (null != sourceNodeTaglib) {
			String sourceNodeUri = sourceNodeTaglib.getUri();
			if (VisualDomUtil.FACELETS_URI.equalsIgnoreCase(sourceNodeUri)) {
				isFacelet = true;
			}
		}
//		IEditorInput iEditorInput = pageContext.getEditPart().getEditorInput();
//		if (iEditorInput instanceof IFileEditorInput) {
//			IFileEditorInput iFileEditorInput = (IFileEditorInput) iEditorInput;
//
//			IFile iFile = iFileEditorInput.getFile();
//
//			IProject project = iFile.getProject();
//			IModelNature nature = EclipseResourceUtil.getModelNature(project);
//			if (nature != null) {
//				XModel model = nature.getModel();
//				XModelObject webXML = WebAppHelper.getWebApp(model);
//				XModelObject param = WebAppHelper.findWebAppContextParam(
//						webXML, "javax.faces.DEFAULT_SUFFIX"); //$NON-NLS-1$
//				if (param != null) {
//					String value = param.getAttributeValue("param-value"); //$NON-NLS-1$
//
//					if (value.length() != 0 && iFile.getName().endsWith(value)) {
//						isFacelet = true;
//					}
//				}
//			}
//		}

		return isFacelet;
	}

	/**
	 * @return the xulRunnerEditor
	 */
	public XulRunnerEditor getXulRunnerEditor() {
		return xulRunnerEditor;
	}

	/**
	 * @param xulRunnerEditor
	 *            the xulRunnerEditor to set
	 */
	public void setXulRunnerEditor(XulRunnerEditor xulRunnerEditor) {
		this.xulRunnerEditor = xulRunnerEditor;
	}

	public Map<IStorage, Document> getIncludeDocuments() {
		return includeDocuments;
	}

	public nsIDOMNode getHeadNode() {
		return visualEditor.getHeadNode();
	}

	public boolean isShowInvisibleTags() {
		return showInvisibleTags;
	}

	public void setShowInvisibleTags(boolean showInvisibleTags) {
		this.showInvisibleTags = showInvisibleTags;
	}
	
	public boolean isShowBorderForUnknownTags() {
		return showBorderForUnknownTags;
	}

	public void setShowBorderForUnknownTags(boolean showBorderForUnknownTags) {
		this.showBorderForUnknownTags = showBorderForUnknownTags;
	}

	/**
	 * 
	 * @param sourceNode
	 * @return
	 */
	public nsIDOMNode createInvisbleElementLabel(Node sourceNode) {
		nsIDOMElement span = getVisualDocument().createElement(HTML.TAG_SPAN);

		span.setAttribute(HTML.TAG_STYLE,
				"border: 1px dashed GREY; color: GREY; font-size: 12px;"); //$NON-NLS-1$

		nsIDOMText text = getVisualDocument().createTextNode(sourceNode
				.getNodeName());

		span.appendChild(text);

		return span;
	}
}

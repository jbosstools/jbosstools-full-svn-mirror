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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.css.CSSReferenceList;
import org.jboss.tools.vpe.editor.css.ResourceReference;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.template.VpeChildrenInfo;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeDefaultPseudoContentCreator;
import org.jboss.tools.vpe.editor.template.VpeTagDescription;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.template.VpeToggableTemplate;
import org.jboss.tools.vpe.editor.template.dnd.VpeDnd;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.MozillaSupports;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.VpeDnD;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNode;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMRange;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class VpeVisualDomBuilder extends VpeDomBuilder {
	/** REGEX_EL */
	private static final Pattern REGEX_EL = Pattern.compile("[\\$|\\#]\\{.*\\}",  Pattern.MULTILINE + Pattern.DOTALL);

	
	private static final String PSEUDO_ELEMENT = "br";
	private static final String PSEUDO_ELEMENT_ATTR = "vpe:pseudo-element";
	private static final String INIT_ELEMENT_ATTR = "vpe:init-element";
	private static final String MOZ_ANONCLASS_ATTR = "_MOZ_ANONCLASS";
	private static final String COMMENT_STYLE = "font-style:italic; color:green";
	private static final String COMMENT_PREFIX = "";
	private static final String COMMENT_SUFFIX = "";
	private static final String INCLUDE_ELEMENT_ATTR = "vpe:include-element";
	private static final int DRAG_AREA_WIDTH = 10;
	private static final int DRAG_AREA_HEIGHT = 10;
	private static final String ATTR_XMLNS = "xmlns";
	
	protected MozillaEditor visualEditor;
	private MozillaBrowser browser; 
	protected Document visualDocument;
	private Element visualContentArea;
	protected VpePageContext pageContext;
	private VpeDnD dnd;
	private Node headNode;
	private List<VpeIncludeInfo> includeStack;
	boolean rebuildFlag = false;
	
	private static final String EMPTY_STRING = ""; 
	
	private static final String TAG_BODY     = "body";
	private static final String TAG_HEAD     = "head";
	private static final String TAG_HTML     = "html";
	private static final String TAG_LINK     = "link";
	private static final String TAG_STYLE    = "style";
	private static final String TAG_TABLE    = "table";
	private static final String TAG_TBODY    = "tbody";
	private static final String TAG_THEAD    = "thead";
	private static final String TAG_TR       = "tr";
	private static final String TAG_TD       = "td";
	private static final String TAG_COL      = "col";
	private static final String TAG_COLS     = "cols";
	private static final String TAG_COLGROUP = "colgroup";
	private static final String TAG_BR       = "br";
	private static final String TAG_LI       = "li";
	private static final String TAG_IMG      = "img";
	private static final String TAG_DIV      = "div";
	private static final String TAG_SPAN     = "span";
	private static final String TAG_P        = "p";
	
	private static final String ATTR_VPE     = "vpe";
	private static final String ATTR_VPE_INLINE_LINK_VALUE = "inlinelink";
	
	private static final String ATTR_REL_STYLESHEET_VALUE = "stylesheet";
	
	private static final String YES_STRING   = "yes";
	private static final String NO_STRING    = "no";
	private static final String ZERO_STRING  = "0";
	
	private static final String ATRIBUTE_BORDER       = "border"; 
	private static final String ATRIBUTE_CELLSPACING  = "cellspacing";
	private static final String ATRIBUTE_CELLPADDING  = "cellpadding";
	
	private static final String DOTTED_BORDER_STYLE          = "border : 1px dotted #808080";
	private static final String DOTTED_BORDER_STYLE_FOR_IMG  = "1px dotted #808080";
	private static final String DOTTED_BORDER_STYLE_FOR_TD   = "border-left : 1px dotted #808080; border-right : 1px dotted #808080; border-top : 1px dotted #808080; border-bottom : 0px; color:#0051DD; background-color:#ECF3FF; padding-left: 3px;  padding-right: 3px;  line-height : 10px; font-family : arial; font-size : 10px; text-align:top; margin : 1px; -moz-user-modify : read-only";
	private static final String DOTTED_BORDER_STYLE_FOR_SPAN = "border : 1px solid #0051DD; color:#0051DD; background-color:#ECF3FF; padding-left: 3px;  padding-right: 3px;  line-height : 10px; font-family : arial; font-size : 10px; text-align:top; margin : 1px; -moz-user-modify : read-only";
	
	
	static private HashSet<String> unborderedSourceNodes = new HashSet<String>();
	static{
		unborderedSourceNodes.add(TAG_HTML);
		unborderedSourceNodes.add(TAG_HEAD);
		unborderedSourceNodes.add(TAG_BODY);
	}
	
	static private HashSet<String> unborderedVisualNodes = new HashSet<String>();
	static{
		unborderedVisualNodes.add(TAG_TBODY);
		unborderedVisualNodes.add(TAG_THEAD);
		unborderedVisualNodes.add(TAG_TR);
		unborderedVisualNodes.add(TAG_TD);
		unborderedVisualNodes.add(TAG_COL);
		unborderedVisualNodes.add(TAG_COLS);
		unborderedVisualNodes.add(TAG_COLGROUP);
		unborderedVisualNodes.add(TAG_LI);
		unborderedVisualNodes.add(TAG_BR);
	}
	private VpeDnd dropper;
	private boolean faceletFile;

	public VpeVisualDomBuilder(VpeDomMapping domMapping, INodeAdapter sorceAdapter, VpeTemplateManager templateManager, MozillaEditor visualEditor, VpePageContext pageContext) {
		super(domMapping, sorceAdapter, templateManager);
		this.visualEditor = visualEditor;
		browser = (MozillaBrowser)visualEditor.getControl();
		this.visualDocument = visualEditor.getDomDocument();
		this.visualContentArea = visualEditor.getContentArea();
		this.dnd = visualEditor.getLocalDnD();
		this.pageContext = pageContext;
		this.headNode = visualEditor.getHeadNode();
		dropper = new VpeDnd();
		dropper.setDndData(false, true);
		
		if ( isFacelet() ) {
			faceletFile = true;
		} else {
			faceletFile = false;
		}
	}
	
	public void buildDom(Document sourceDocument) {
		includeStack = new ArrayList<VpeIncludeInfo>();
		IEditorInput input = pageContext.getEditPart().getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)input).getFile();
			if (file != null) {
				includeStack.add(new VpeIncludeInfo(null, file, pageContext.getSourceBuilder().getSourceDocument()));
			}
		}
		pageContext.refreshConnector();
		pageContext.installIncludeElements();
		addChildren(null, sourceDocument, visualContentArea);
		registerNodes(new VpeNodeMapping(sourceDocument, visualContentArea));
	}
	
	public void rebuildDom(Document sourceDocument) {
	    cleanHead();
		domMapping.clear(visualContentArea);
		pageContext.clearAll();
		refreshExternalLinks();
		pageContext.getBundle().refreshRegisteredBundles();

		NodeList children = visualContentArea.getChildNodes();
		int len = children.getLength();
		for (int i = len - 1; i >= 0; i--) {
			Node child = children.item(i);
			Node visualRemovedNode = visualContentArea.removeChild(child);
			MozillaSupports.release(child, visualRemovedNode);
		}
		MozillaSupports.release(children);

		if (sourceDocument != null) {
			buildDom(sourceDocument);
		}
		
		rebuildFlag = true;
	}

	// temporary, will be change to prefference's variable
	//private boolean borderVisible = true;
	
	private boolean addNode(Node sourceNode, Node visualNextNode, Node visualContainer) {
		Node visualNewNode = createNode(sourceNode, visualContainer);
		if (visualNewNode != null) {
			Node visualAddedNode = visualNextNode == null ? 
						visualContainer.appendChild(visualNewNode) : 
						visualContainer.insertBefore(visualNewNode, visualNextNode);
			MozillaSupports.release(visualAddedNode);
			return true;
		} else {
			return false;
		}
	}
	

	private Element createBorder(Node sourceNode, Element visualNode, boolean block){
		Element border = null;
		if (visualNode == null) return null;
		if(unborderedSourceNodes.contains(sourceNode.getNodeName().toLowerCase())) return null;
		if(unborderedVisualNodes.contains(visualNode.getNodeName().toLowerCase())) return null;
		if(TAG_IMG.equalsIgnoreCase(visualNode.getNodeName())){
			String width = visualNode.getAttribute(ATRIBUTE_BORDER);
			if(width == null || ZERO_STRING.equalsIgnoreCase(width) || "".equalsIgnoreCase(width)){
				String style = visualNode.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
				style = VpeStyleUtil.setParameterInStyle(style, ATRIBUTE_BORDER, DOTTED_BORDER_STYLE_FOR_IMG);
				visualNode.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
			}
			return null;
		}
		if( block){
			if (YES_STRING.equals(VpePreference.USE_DETAIL_BORDER.getValue())){
				border = visualDocument.createElement(TAG_TABLE);
				border.setAttribute(ATRIBUTE_CELLSPACING, ZERO_STRING);
				border.setAttribute(ATRIBUTE_CELLPADDING, ZERO_STRING);

				Element tr1 = visualDocument.createElement(TAG_TR);
				border.appendChild(tr1);
				Element td1 = visualDocument.createElement(TAG_TD);
				td1.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, DOTTED_BORDER_STYLE_FOR_TD);
				Text text = visualDocument.createTextNode(sourceNode.getNodeName());
				td1.appendChild(text);
				MozillaSupports.release(text);
				tr1.appendChild(td1);
				Element tr2 = visualDocument.createElement(TAG_TR);
				border.appendChild(tr2);
				Element td2 = visualDocument.createElement(TAG_TD);
				tr2.appendChild(td2);
				Element p = visualDocument.createElement(TAG_P);
				p.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, DOTTED_BORDER_STYLE);
				td2.appendChild(p);

				p.appendChild(visualNode);
				
			}else{
				border = visualDocument.createElement(TAG_TABLE);
				border.setAttribute(ATRIBUTE_CELLSPACING, ZERO_STRING);
				border.setAttribute(ATRIBUTE_CELLPADDING, ZERO_STRING);
				
				Element tr2 = visualDocument.createElement(TAG_TR);
				border.appendChild(tr2);
				Element td2 = visualDocument.createElement(TAG_TD);
				tr2.appendChild(td2);
				Element p = visualDocument.createElement(TAG_P);
				p.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, DOTTED_BORDER_STYLE);
				td2.appendChild(p);

				p.appendChild(visualNode);
			}
		}else{
			border = visualDocument.createElement(TAG_SPAN);
			border.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, DOTTED_BORDER_STYLE);
			if(YES_STRING.equals(VpePreference.USE_DETAIL_BORDER.getValue())){
				Element name = visualDocument.createElement(TAG_SPAN);
				name.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, DOTTED_BORDER_STYLE_FOR_SPAN);
				Text text = visualDocument.createTextNode(sourceNode.getNodeName());
				name.appendChild(text);
				border.appendChild(name);
				MozillaSupports.release(text, name);
			}
			border.appendChild(visualNode);
		}
		if(VpeStyleUtil.getAbsolute((Element)sourceNode) && border != null){
			int top = VpeStyleUtil.getSizeFromStyle((Element)sourceNode, VpeStyleUtil.ATTRIBUTE_STYLE+VpeStyleUtil.DOT_STRING+VpeStyleUtil.PARAMETER_TOP);
			int left = VpeStyleUtil.getSizeFromStyle((Element)sourceNode, VpeStyleUtil.ATTRIBUTE_STYLE+VpeStyleUtil.DOT_STRING+VpeStyleUtil.PARAMETER_LEFT);
			
			String style = visualNode.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
			style = VpeStyleUtil.deleteFromString(style, VpeStyleUtil.PARAMETER_POSITION, VpeStyleUtil.SEMICOLON_STRING);
			style = VpeStyleUtil.deleteFromString(style, VpeStyleUtil.PARAMETER_TOP, VpeStyleUtil.SEMICOLON_STRING);
			style = VpeStyleUtil.deleteFromString(style, VpeStyleUtil.PARAMETER_LEFT, VpeStyleUtil.SEMICOLON_STRING);
			visualNode.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);

			style = border.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
			style = VpeStyleUtil.setAbsolute(style);
			if(top != -1)style = VpeStyleUtil.setSizeInStyle(style, VpeStyleUtil.PARAMETER_TOP, top);
			if(left != -1)style = VpeStyleUtil.setSizeInStyle(style, VpeStyleUtil.PARAMETER_LEFT, left);
			border.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
		}
		return border;
	}
		
	protected Node createNode(Node sourceNode, Node visualOldContainer) {
		boolean registerFlag = isCurrentMainDocument();
		switch (sourceNode.getNodeType()) {
		case Node.ELEMENT_NODE:
			Map<String, Integer> xmlnsMap = createXmlns((Element)sourceNode);
			Set ifDependencySet = new HashSet();
			pageContext.setCurrentVisualNode(visualOldContainer);
			VpeTemplate template = templateManager.getTemplate(pageContext, (Element)sourceNode, ifDependencySet);
			VpeCreationData creationData = template.create(pageContext, sourceNode, visualDocument);
			pageContext.setCurrentVisualNode(null);
			Element visualNewElement;
			visualNewElement = (Element)creationData.getNode();
			
			if (visualNewElement != null)
				correctVisualAttribute(visualNewElement);
			
			Element border = null;
			setTooltip((Element)sourceNode, visualNewElement);
			if (YES_STRING.equals(VpePreference.SHOW_BORDER_FOR_ALL_TAGS.getValue()) && visualNewElement != null) {
				boolean block = true;
				if (template.getTagDescription(null, null, null,visualNewElement,null).getDisplayType() == VpeTagDescription.DISPLAY_TYPE_INLINE) {
					block = false;
				}
				border = createBorder(sourceNode, visualNewElement, block);
			}
			if (!isCurrentMainDocument() && visualNewElement != null) {
				setReadOnlyElement(visualNewElement);
			}
			if (registerFlag) {
				VpeElementMapping elementMapping = new VpeElementMapping((Element)sourceNode, visualNewElement, border, template, ifDependencySet, creationData.getData());
				elementMapping.setXmlnsMap(xmlnsMap);
				registerNodes(elementMapping);
			}
			if (template.isChildren()) {
				List<VpeChildrenInfo> childrenInfoList = creationData.getChildrenInfoList();
				if (childrenInfoList == null) {
					addChildren(template, sourceNode, visualNewElement != null ? visualNewElement : visualOldContainer);
				} else {
					addChildren(template, sourceNode, visualOldContainer, childrenInfoList);
				}
			}
			pageContext.setCurrentVisualNode(visualOldContainer);
			template.validate(pageContext, (Element)sourceNode, visualDocument, creationData);
			pageContext.setCurrentVisualNode(null);
			if(border != null) return border;
			else return visualNewElement;
		case Node.TEXT_NODE:			
			return createTextNode(sourceNode, registerFlag);
		case Node.COMMENT_NODE:
			if(!YES_STRING.equals(VpePreference.SHOW_COMMENTS.getValue())) {
				return null;
			}
			Element visualNewComment = createComment(sourceNode);
			if (registerFlag) {
				registerNodes(new VpeNodeMapping(sourceNode, visualNewComment));
			}
			return visualNewComment;
		}
		return null;
	}
	
	private void correctVisualAttribute(Element element) {

		String styleValue = element.getAttribute(HTML.TAG_STYLE);
		String backgroundValue = element
				.getAttribute(VpeStyleUtil.PARAMETR_BACKGROND);

		if (styleValue != null) {
			styleValue = VpeStyleUtil.addFullPathIntoURLValue(styleValue,
					pageContext.getEditPart().getEditorInput());
			element.setAttribute(HTML.TAG_STYLE, styleValue);
		}
		if (backgroundValue != null) {
			backgroundValue = VpeStyleUtil
					.addFullPathIntoBackgroundValue(backgroundValue,
							pageContext.getEditPart().getEditorInput());
			element.setAttribute(VpeStyleUtil.PARAMETR_BACKGROND,
					backgroundValue);
		}
	}
	
	/**
	 * Create a visual element for text node
	 * @param sourceNode
	 * @param registerFlag
	 * @return a visual element for text node
	 */
	
	protected Node createTextNode(Node sourceNode, boolean registerFlag ) {
		String sourceText = sourceNode.getNodeValue();
		if (sourceText.trim().length() <= 0) {
			registerNodes(new VpeNodeMapping(sourceNode, null));
			return null;
		}

		if (faceletFile) {
			Matcher matcher_EL = REGEX_EL.matcher(sourceText);
			if (matcher_EL.find()) {
				BundleMap bundle = pageContext.getBundle();
				int offset = pageContext.getVisualBuilder().getCurrentMainIncludeOffset();
				if (offset == -1) offset = ((IndexedRegion)sourceNode).getStartOffset();
				String jsfValue = bundle.getBundleValue(sourceText, offset);
				sourceText  = jsfValue;
			}
		}
		String visualText = TextUtil.visualText(sourceText);

		Node visualNewTextNode = visualDocument.createTextNode(visualText);
		if (registerFlag) {
			registerNodes(new VpeNodeMapping(sourceNode, visualNewTextNode));
		}

		return visualNewTextNode;
	}
	
	protected Element createComment(Node sourceNode) {
		Element div = visualDocument.createElement(TAG_DIV);
		div.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, COMMENT_STYLE);
		String value = COMMENT_PREFIX + sourceNode.getNodeValue() + COMMENT_SUFFIX;
		Node text = visualDocument.createTextNode(value);
		div.appendChild(text);
		return div;
	}

	protected void addChildren(VpeTemplate containerTemplate, Node sourceContainer, Node visualContainer) {
		NodeList sourceNodes = sourceContainer.getChildNodes();
		int len = sourceNodes.getLength();
		int childrenCount = 0;
		for (int i = 0; i < len; i++) {
			Node sourceNode = sourceNodes.item(i);
			if (addNode(sourceNode, null, visualContainer)) {
				childrenCount++;
			}
		}
		if (childrenCount == 0) {
			setPseudoContent(containerTemplate, sourceContainer, visualContainer);
		}
	}

	protected void addChildren(VpeTemplate containerTemplate, Node sourceContainer, Node visualOldContainer, List<VpeChildrenInfo> childrenInfoList) {
		for (int i = 0; i < childrenInfoList.size(); i++) {
			VpeChildrenInfo info = childrenInfoList.get(i);
			Node visualParent = info.getVisualParent();
			if (visualParent == null) visualParent = visualOldContainer;
			List<Node> sourceChildren = info.getSourceChildren();
			int childrenCount = 0;
			if (sourceChildren != null) {
				for (int j = 0; j < sourceChildren.size(); j++) {
					if (addNode((Node)sourceChildren.get(j), null, visualParent)) {
						childrenCount++;
					}
				}
			}
			if (childrenCount == 0) {
				setPseudoContent(containerTemplate, sourceContainer, visualParent);
			}
		}
	}
	
	
	
	

///////////////////////////////////////////////////////////////////////////	
	public Node addStyleNodeToHead(String styleText){
		Node newStyle = visualDocument.createElement(VpeStyleUtil.ATTRIBUTE_STYLE);
		
		if(styleText != null){
			Node newText = visualDocument.createTextNode(styleText);
			newStyle.appendChild(newText);
			MozillaSupports.release(newText);
		}
		headNode.appendChild(newStyle);
		return newStyle;
	}

	public Node replaceStyleNodeToHead(Node oldStyleNode, String styleText){
		Node newStyle = visualDocument.createElement(VpeStyleUtil.ATTRIBUTE_STYLE);
		
		if(styleText != null){
			Node newText = visualDocument.createTextNode(styleText);
			newStyle.appendChild(newText);
			MozillaSupports.release(newText);
		}
		
		headNode.replaceChild(newStyle, oldStyleNode);
		MozillaSupports.release(oldStyleNode);
		return newStyle;
	}
	
	public void removeStyleNodeFromHead(Node oldStyleNode){
		headNode.removeChild(oldStyleNode);
		MozillaSupports.release(oldStyleNode);
	}
	
	void addExternalLinks() {
	    IEditorInput input = pageContext.getEditPart().getEditorInput();
        IFile file = null;
	    if (input instanceof IFileEditorInput) {
	        file = ((IFileEditorInput)input).getFile();
	    }
	    ResourceReference[] l = null;
	    if (file != null) {
	        l = CSSReferenceList.getInstance().getAllResources(file);
	    }
	    if (l != null) {
		    for (int i = 0; i < l.length; i++) {
		        ResourceReference item = l[i];
		        addLinkNodeToHead("file:///" + item.getLocation(), YES_STRING);
		    }
	    }
	}

	void removeExternalLinks() {
	    NodeList childs = headNode.getChildNodes();
	    int length = childs.getLength();
	    for (int i = length - 1; i >= 0; i--) {
	        Node node = childs.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	boolean isLink = false;
	        	boolean isStyle = false;
	        	if((isLink = TAG_LINK.equalsIgnoreCase(node.getNodeName()))
	        			|| (isStyle = TAG_STYLE.equalsIgnoreCase(node.getNodeName()))) {
	        		Element element = (Element)node;
		            if ((isLink || (isStyle && ATTR_VPE_INLINE_LINK_VALUE.equalsIgnoreCase(element.getAttribute(ATTR_VPE))))
		            		&& YES_STRING.equalsIgnoreCase(element.getAttribute("ext"))) {
		                headNode.removeChild(node);
		            }
	        	}
	        }
	        MozillaSupports.release(node);
	    }
        MozillaSupports.release(childs);
	}

	void refreshExternalLinks() {
	    removeExternalLinks();
	    addExternalLinks();
	}
	
//	==========================================================
	void resetPseudoElement(Node visualNode) {
		if (visualNode != null) {
			Node visualParent = visualNode.getParentNode();
			if (visualParent != null) {
				PseudoInfo info = getPseudoInfo(visualParent);
				if (info.pseudoNode == null && !info.isElements) {
					addPseudoElementImpl(visualParent);
				} else  if (info.pseudoNode != null && info.isElements) {
					visualParent.removeChild(info.pseudoNode);
					MozillaSupports.release(info.pseudoNode);
				}
				MozillaSupports.release(visualParent);
			}
		}
	}
	
	private PseudoInfo getPseudoInfo(Node visualParent) {
		Node pseudoNode = null;
		boolean isElements = false;

		if (visualParent == null) return new PseudoInfo();
	    NodeList visualNodes = visualParent.getChildNodes();
	    if (visualNodes == null) return new PseudoInfo();

	    int length = visualNodes.getLength();
	    for (int i = 0; i < length; i++) {
	        Node visualNode = visualNodes.item(i);
	        if (pseudoNode == null && isPseudoElement(visualNode)) {
	        	pseudoNode = visualNode;
	        } else if (!isEmptyText(visualNode)) {
	        	isElements = true;
	        }
	        if (visualNode != pseudoNode) {
	        	MozillaSupports.release(visualNode);
	        }
	        if (pseudoNode != null && isElements) {
	        	break;
	        }
	    }
	    MozillaSupports.release(visualNodes);
		return new PseudoInfo(pseudoNode, isElements);
	}
	
	static boolean isInitElement(Node visualNode) {
		if (visualNode == null) return false;
		if (visualNode.getNodeType() != Node.ELEMENT_NODE) return false;
        if (YES_STRING.equalsIgnoreCase(((Element)visualNode).getAttribute(INIT_ELEMENT_ATTR))) return true;
		return false;
	}
	
	static boolean isPseudoElement(Node visualNode) {
		if (visualNode == null) return false;
		if (visualNode.getNodeType() != Node.ELEMENT_NODE) return false;
        if (YES_STRING.equalsIgnoreCase(((Element)visualNode).getAttribute(PSEUDO_ELEMENT_ATTR))) return true;
		return false;
	}
	
	private void setPseudoContent(VpeTemplate containerTemplate, Node sourceContainer, Node visualContainer) {
		if (containerTemplate != null) {
			containerTemplate.setPseudoContent(pageContext, sourceContainer, visualContainer, visualDocument);
		} else {
			VpeDefaultPseudoContentCreator.getInstance().setPseudoContent(pageContext, sourceContainer, visualContainer, visualDocument);
		}
		
//		if (isEmptyElement(visualContainer)) {
//			addPseudoElementImpl(visualContainer);
//		}
	}
	
	private void addPseudoElementImpl(Node visualParent) {
		if (!templateManager.isWithoutPseudoElementContainer(visualParent.getNodeName())) {
			if (VpeDebug.VISUAL_ADD_PSEUDO_ELEMENT) {
				System.out.println("-------------------- addPseudoElement: " + visualParent.getNodeName());
			}
			Element visualPseudoElement = visualDocument.createElement(PSEUDO_ELEMENT);
			visualPseudoElement.setAttribute(PSEUDO_ELEMENT_ATTR, "yes");
			visualParent.appendChild(visualPseudoElement);
			MozillaSupports.release(visualPseudoElement);
		}
	}
	
	public boolean isEmptyElement(Node visualParent) {
		boolean empty = false;
		NodeList visualNodes = visualParent.getChildNodes();
		int len = visualNodes.getLength();
		if (len == 0) {
			empty = true;
		} else if (len == 1) {
			Node visualNode = visualNodes.item(0);
			if (isEmptyText(visualNode)) {
				empty = true;
			}
			MozillaSupports.release(visualNode);
		}
		MozillaSupports.release(visualNodes);
		return empty;
	}
	
	public boolean isEmptyDocument() {
		boolean empty = false;
		NodeList visualNodes = visualContentArea.getChildNodes();
		int len = visualNodes.getLength();
		if (len == 0) {
			empty = true;
		} else if (len == 1) {
			Node visualNode = visualNodes.item(0);
			if (isEmptyText(visualNode) || isPseudoElement(visualNode)) {
				empty = true;
			}
			MozillaSupports.release(visualNode);
		}
		MozillaSupports.release(visualNodes);
		return empty;
	}
	
	private boolean isEmptyText(Node visualNode) {
		if (visualNode == null) return false;
		if (visualNode.getNodeType() != Node.TEXT_NODE) return false;
		if (visualNode.getNodeValue().trim().length() == 0) return true;
		return false;
	}
//	==========================================================
	
	public void updateNode(Node sourceNode) {
		if (sourceNode == null)
			return;
		
		switch (sourceNode.getNodeType()) {
		case Node.DOCUMENT_NODE :
			rebuildDom((Document)sourceNode);
			break;
		case Node.COMMENT_NODE :
			updateComment(sourceNode);
			break;
		default :
			updateElement(getNodeForUpdate(sourceNode));
		}
	}
	
	// TODO S.Vasilyev make a common code for figuring out
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
	
	private void updateComment(Node sourceNode) {
		VpeNodeMapping mapping = domMapping.getNodeMapping(sourceNode);
		if (mapping != null && mapping.getType() == VpeNodeMapping.COMMENT_MAPPING) {
			Node visualCommentFrame = mapping.getVisualNode(); 
			NodeList visualNodes = visualCommentFrame.getChildNodes();
			int len = visualNodes.getLength();
			if (len > 0) {
				Node visualText = visualNodes.item(0);
				visualText.setNodeValue(sourceNode.getNodeValue());
				MozillaSupports.release(visualText);
			}
			MozillaSupports.release(visualNodes);
		}
	}
	
	private void updateElement(Node sourceNode) {
		VpeElementMapping elementMapping = null;
		VpeNodeMapping nodeMapping = domMapping.getNodeMapping(sourceNode);
		if (nodeMapping instanceof VpeElementMapping) {
			elementMapping = (VpeElementMapping)nodeMapping;
			if (elementMapping != null && elementMapping.getTemplate() != null) {
				Node updateNode = elementMapping.getTemplate().getNodeForUptate(pageContext, elementMapping.getSourceNode(), elementMapping.getVisualNode(), elementMapping.getData());
				if (updateNode != null && updateNode != sourceNode) {
					updateNode(updateNode);
					return;
				}
			}
		}
		Node visualOldNode = domMapping.remove(sourceNode);
		if (visualOldNode != null) {
			if(elementMapping != null){
				Element border = elementMapping.getBorder();
				if(border != null){
					MozillaSupports.release(visualOldNode);
					visualOldNode = border;
				}
			}
			Node visualContainer = visualOldNode.getParentNode();
			Node visualNextNode = visualOldNode.getNextSibling();
			if (visualContainer != null) {
				visualContainer.removeChild(visualOldNode);
				addNode(sourceNode, visualNextNode, visualContainer);
			}
			MozillaSupports.release(visualOldNode, visualNextNode, visualContainer);
		} else {
			if (sourceNode.getNodeType() == Node.TEXT_NODE) {
				updateNode(sourceNode.getParentNode());
			}
		}
	}
	
	public void removeNode(Node sourceNode) {
		Node visualNode = domMapping.remove(sourceNode);
		if (visualNode != null) {
			MozillaSupports.release(visualNode);
		}
	}
	
	private Node getParentTable(Node sourceNode, int depth) {
		Node parentNode = sourceNode.getParentNode();
		for (int i = 0; parentNode != null && i < depth; parentNode = parentNode.getParentNode(), i++) {
			if (TAG_TABLE.equalsIgnoreCase(parentNode.getNodeName())) {
				return parentNode;
			}
		}
		return null;
	}
	
	private Node getParentSelect(Node sourceNode) {
		if ("OPTION".equalsIgnoreCase(sourceNode.getNodeName())) {
			Node parentNode = sourceNode.getParentNode();
			if ("SELECT".equalsIgnoreCase(parentNode.getNodeName())) {
				return parentNode;
			}
		}
		return null;
	}
	
	public void setText(Node sourceText) {
		Node sourceParent = sourceText.getParentNode();
		if (sourceParent != null && sourceParent.getLocalName() != null) {
			String sourceParentName = sourceParent.getLocalName();
			if ("textarea".equalsIgnoreCase(sourceParentName) || "option".equalsIgnoreCase(sourceParentName)) {
				updateNode(sourceText.getParentNode());
				return;
			}
		}
		Node visualText = domMapping.getVisualNode(sourceText);
		if (visualText != null) {
			String visualValue = TextUtil.visualText(sourceText.getNodeValue());
			visualText.setNodeValue(visualValue);
		} else {
			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(sourceParent);
			if (nodeMapping != null && nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {
				VpeTemplate template = ((VpeElementMapping)nodeMapping).getTemplate();
				if (template != null) {
					if (!template.containsText()) {
						return;
					}
				}
			}
			updateNode(sourceText);
		}
	}
	
	public void setAttribute(Element sourceElement, String name, String value) {
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			if (elementMapping.isIfDependencyFromAttribute(name)) {
				updateElement(sourceElement);
			} else {
				VpeTemplate template = elementMapping.getTemplate();
				if (elementMapping.getBorder() != null) {
					updateElement(sourceElement);
				} else if (template.isRecreateAtAttrChange(pageContext, sourceElement, visualDocument, (Element)elementMapping.getVisualNode(), elementMapping.getData(), name, value)) {
					updateElement(sourceElement);
				} else {
					Element visualElement = (Element)elementMapping.getVisualNode();
					if (visualElement != null) {
						String visualElementName = visualElement.getNodeName().toLowerCase();
						if ("select".equals(visualElementName)) {
							updateElement(sourceElement);
							return;
						} else if ("option".equals(visualElementName)) {
							updateElement(sourceElement.getParentNode());
							return;
						}
					}
					setXmlnsAttribute(elementMapping, name, value);
					template.setAttribute(pageContext, sourceElement, visualDocument, visualElement, elementMapping.getData(), name, value);
					resetTooltip(sourceElement, visualElement);
				}
			}
		}
	}
	
	
	public void stopToggle(Node sourceNode) {
		if (!(sourceNode instanceof Element)) 
			return;
		
		Element sourceElement = (Element)sourceNode; 
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate();
			
			if (template instanceof VpeToggableTemplate) {
				((VpeToggableTemplate)template).stopToggling(sourceElement);
			}
		}
	}
	public boolean doToggle(Node visualNode) {
		if (visualNode == null) return false;
		
		Element visualElement = (Element) (visualNode instanceof Element ? 
				visualNode : visualNode.getParentNode());
		
		if (visualElement == null) return false;

		Attr toggleIdAttr = visualElement.getAttributeNode("vpe-user-toggle-id");
		if (toggleIdAttr == null) return false;
		String toggleId = toggleIdAttr.getNodeValue();
		MozillaSupports.release(toggleIdAttr);

		if (toggleId == null) return false;

		boolean toggleLookup = false;
		Attr toggleLookupAttr = visualElement.getAttributeNode("vpe-user-toggle-lookup-parent");
		if (toggleLookupAttr != null) {
			toggleLookup = "true".equals(toggleLookupAttr.getNodeValue());
			MozillaSupports.release(toggleIdAttr);
		}
		
		
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(getLastSelectedElement());
		Node sourceNode = (Node)domMapping.getSourceNode(getLastSelectedElement());
		if (sourceNode == null) return false;

		Element sourceElement = (Element)(sourceNode instanceof Element ?
				sourceNode : sourceNode.getParentNode());

		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate(); 
				
			while (toggleLookup && sourceElement != null && !(template instanceof VpeToggableTemplate)) {
				sourceElement = (Element)sourceElement.getParentNode();
				if (sourceElement == null) break;
				elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceElement);
				if (elementMapping == null) continue;
				template = elementMapping.getTemplate();
			}
			
			if (template instanceof VpeToggableTemplate) {
				((VpeToggableTemplate)template).toggle(this, sourceElement, toggleId);
				updateElement(sourceElement);
				return true;
			}
		}
		return false;
	}
	
	public void removeAttribute(Element sourceElement, String name) {
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			if (elementMapping.isIfDependencyFromAttribute(name)) {
				updateElement(sourceElement);
			} else {
				VpeTemplate template = elementMapping.getTemplate();
				if (template.isRecreateAtAttrChange(pageContext, sourceElement, visualDocument, (Element)elementMapping.getVisualNode(), elementMapping.getData(), name, null)) {
					updateElement(sourceElement);
				} else {
					removeXmlnsAttribute(elementMapping, name);
					template.removeAttribute(pageContext, sourceElement, visualDocument, (Element)elementMapping.getVisualNode(), elementMapping.getData(), name);
					resetTooltip(sourceElement, (Element)elementMapping.getVisualNode());
				}
			}
		}
	}
	
	public void refreshBundleValues(Element sourceElement) {
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(sourceElement);
		if (elementMapping != null) {
			VpeTemplate template = elementMapping.getTemplate();
			template.refreshBundleValues(pageContext, sourceElement, elementMapping.getData());
		}
	}
	
	boolean isContentArea(Node visualNode) {
		return visualContentArea.equals(visualNode);
	}
	
	Element getContentArea() {
		return visualContentArea;
	}

	void setSelectionRectangle(Element visualElement) {
		setSelectionRectangle(visualElement, true);
	}

	void setSelectionRectangle(Element visualElement, boolean scroll) {
		int resizerConstrains = getResizerConstrains(visualElement);
		visualEditor.setSelectionRectangle(visualElement, resizerConstrains, scroll);
	}

	public Node addLinkNodeToHead(String href_val, String ext_val) {
	    Element newNode = createLinkNode(href_val, ATTR_REL_STYLESHEET_VALUE, ext_val);
		headNode.appendChild(newNode);
		return newNode;
	}

	public Node replaceLinkNodeToHead(Node oldNode, String href_val, String ext_val) {
	    Node newNode = createLinkNode(href_val, ATTR_REL_STYLESHEET_VALUE, ext_val);
		headNode.replaceChild(newNode, oldNode);
		MozillaSupports.release(oldNode);
		return newNode;
	}
	
	public Node replaceLinkNodeToHead(String href_val, String ext_val) {
		Node newNode = null;
		Node oldNode = getLinkNode(href_val, ext_val);
	    if (oldNode == null) {
	    	newNode = addLinkNodeToHead(href_val, ext_val);
	    }
		return newNode;
	}
	
	public void removeLinkNodeFromHead(Node node){
		headNode.removeChild(node);
		MozillaSupports.release(node);
	}
	
	private Element createLinkNode(String href_val, String rel_val, String ext_val) {
		Element linkNode = null;
		if ((ATTR_REL_STYLESHEET_VALUE.equalsIgnoreCase(rel_val))
				&& href_val.startsWith("file:")) {
			/* Because of the Mozilla caches the linked css files we replace
			 * tag <link rel="styleseet" href="file://..."> with tag
			 * <style vpe="ATTR_VPE_INLINE_LINK_VALUE">file content</style>
			 * It is LinkReplacer
			 */
			linkNode = visualDocument.createElement(TAG_STYLE);
			linkNode.setAttribute(ATTR_VPE, ATTR_VPE_INLINE_LINK_VALUE);
			
			/* Copy links attributes into our <style> */
			linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_HREF, href_val);
		    linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_EXT, ext_val);
			try	{
				StringBuffer styleText = new StringBuffer(EMPTY_STRING);
				BufferedReader in = new BufferedReader(new FileReader((new Path(href_val)).setDevice("").toOSString()));
				String str = EMPTY_STRING;
				while ((str = in.readLine()) != null) {
					styleText.append(str);
				}
	
				String styleForParse = styleText.toString();
				styleForParse = VpeStyleUtil.addFullPathIntoURLValue(styleForParse, href_val);
			
				in.close();
				Node textNode = visualDocument.createTextNode(styleForParse);
				linkNode.appendChild(textNode);
				return linkNode;
			} catch (FileNotFoundException fnfe) {
				MozillaSupports.release(linkNode);
				/* File which was pointed by user is not exists. Do nothing. */
			} catch (IOException ioe) {
				MozillaSupports.release(linkNode);
				VpePlugin.getPluginLog().logError(ioe.getMessage(), ioe);
			}
		}
		
		linkNode = visualDocument.createElement(TAG_LINK);
	    linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_REL, rel_val);
	    linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_HREF, href_val);
	    linkNode.setAttribute(VpeTemplateManager.ATTR_LINK_EXT, ext_val);

	    return linkNode;
	}

	private boolean isLinkReplacer(Node node) {
		return TAG_STYLE.equalsIgnoreCase(node.getNodeName())
				&& ATTR_VPE_INLINE_LINK_VALUE.equalsIgnoreCase(((Element)node).getAttribute(ATTR_VPE));
	}

	private Node getLinkNode(String href_val, String ext_val) {
		NodeList children = headNode.getChildNodes();
		int len = children.getLength();
		for (int i = len - 1; i >= 0; i--) {
			Node node = children.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            if (TAG_LINK.equalsIgnoreCase(node.getNodeName())
	            		|| isLinkReplacer(node) ) {
		            Element element = (Element)node;
		            if (ext_val.equalsIgnoreCase(element.getAttribute(VpeTemplateManager.ATTR_LINK_EXT))
		            		&& href_val.equalsIgnoreCase(element.getAttribute(VpeTemplateManager.ATTR_LINK_HREF))) {
		        		MozillaSupports.release(children);
		                return node;
		            }
	            }
	        }
	        MozillaSupports.release(node);
		}
		MozillaSupports.release(children);
		return null;
	}
	
	private void cleanHead() {
		NodeList children = headNode.getChildNodes();
		int len = children.getLength();
		for (int i = len - 1; i >= 0; i--) {
			Node node = children.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	String name = node.getNodeName();
	        	if (TAG_LINK.equalsIgnoreCase(name) || isLinkReplacer(node) ) {
	        		if (NO_STRING.equalsIgnoreCase(((Element)node).getAttribute("ext"))) {
//		                int linkAddress = MozillaSupports.queryInterface(node, nsIStyleSheetLinkingElement.NS_ISTYLESHEETLINKINGELEMENT_IID);
//	                	nsIStyleSheetLinkingElement linkingElement = new nsIStyleSheetLinkingElement(linkAddress);
//	                	linkingElement.removeStyleSheet();
		                node = headNode.removeChild(node);
	        		}
	        	} else if (TAG_STYLE.equalsIgnoreCase(node.getNodeName())
	        			&& (!YES_STRING.equalsIgnoreCase(((Element)node).getAttribute(ATTR_VPE)))) {
	                node = headNode.removeChild(node);
	        	} 
	        }
	        MozillaSupports.release(node);
		}
		MozillaSupports.release(children);
	}
	
	private class PseudoInfo {
		private Node pseudoNode;
		private boolean isElements;
		
		private PseudoInfo() {
			this(null, false);
		}
		
		private PseudoInfo(Node pseudoNode, boolean isElements) {
			this.pseudoNode = pseudoNode;
			this.isElements = isElements;
		}
	}

	
	void showDragCaret(Node node, int offset) {
		browser.showDragCaret((nsIDOMNode)node, offset);
	}
	
	void hideDragCaret() {
		browser.hideDragCaret();
	}
	
	private int getResizerConstrains(Node visualNode) {
		VpeNodeMapping nodeMapping = domMapping.getNodeMapping(visualNode);
		if (nodeMapping != null && nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {
			return ((VpeElementMapping)nodeMapping).getTemplate().getTagDescription(pageContext, (Element)nodeMapping.getSourceNode(), visualDocument, (Element)nodeMapping.getVisualNode(), ((VpeElementMapping)nodeMapping).getData()).getResizeConstrains();
		}
		return VpeTagDescription.RESIZE_CONSTRAINS_NONE;
	}

	public void resize(Element element, int resizerConstrains, int top, int left, int width, int height) {
		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(element);
		if (elementMapping != null) {
			elementMapping.getTemplate().resize(pageContext, (Element)elementMapping.getSourceNode(), visualDocument, element, elementMapping.getData(), resizerConstrains, top, left, width, height);
		}
	}
	
	static boolean isAnonElement(Node visualNode) {
		if (visualNode != null && visualNode.getNodeType() == Node.ELEMENT_NODE) {
			String attrValue = ((Element)visualNode).getAttribute(MOZ_ANONCLASS_ATTR);
			return attrValue != null && attrValue.length() > 0;
		}
		return false;
	}

	boolean canInnerDrag(Element visualDragElement) {
		VpeNodeMapping node = domMapping.getNodeMapping(visualDragElement);
		if(node instanceof VpeElementMapping) {
			VpeElementMapping elementMapping = (VpeElementMapping)node;
			if (elementMapping != null) {
				return elementMapping.getTemplate().canInnerDrag(pageContext, (Element)elementMapping.getSourceNode(), visualDocument, visualDragElement, elementMapping.getData());
			}
		}
		return false;
	}

	VpeSourceInnerDropInfo getSourceInnerDropInfo(Node sourceDragNode, VpeVisualInnerDropInfo visualDropInfo, boolean checkParentTemplates) {
		Node visualDropContainer = visualDropInfo.getDropContainer();
		int visualDropOffset = visualDropInfo.getDropOffset();
		Node sourceDropContainer = null;
		int sourceDropOffset = 0;
		
		switch (visualDropContainer.getNodeType()) {
		case Node.ELEMENT_NODE:
			Node visualOffsetNode = null;
			boolean afterFlag = false;
			int visualChildCount = MozillaSupports.getChildCount(visualDropContainer);
			if (visualDropOffset < visualChildCount) {
				visualOffsetNode = MozillaSupports.getChildNode(visualDropContainer, visualDropOffset);
				if (isPseudoElement(visualOffsetNode) || isAnonElement(visualOffsetNode)) {
					visualOffsetNode = getLastAppreciableVisualChild(visualDropContainer);
					afterFlag = true;
				}
			} else {
				visualOffsetNode = getLastAppreciableVisualChild(visualDropContainer);
				afterFlag = visualChildCount != 0;
			}
			if (visualOffsetNode != null) {
				Node sourceOffsetNode = domMapping.getSourceNode(visualOffsetNode);
				if (sourceOffsetNode != null) {
					sourceDropContainer = sourceOffsetNode.getParentNode();
					sourceDropOffset = ((NodeImpl)sourceOffsetNode).getIndex();
					if (afterFlag) {
						sourceDropOffset++;
					}
				}
			}
			if (sourceDropContainer == null) {
				sourceDropContainer = domMapping.getNearSourceNode(visualDropContainer);
				if (sourceDropContainer != null) {
					sourceDropOffset = sourceDropContainer.getChildNodes().getLength();
				}
			}
			if (sourceDropContainer == null){
				sourceDropContainer = domMapping.getNearSourceNode(visualContentArea);
				sourceDropOffset = sourceDropContainer.getChildNodes().getLength();
			}
			break;
		case Node.TEXT_NODE:
			VpeNodeMapping nodeMapping = domMapping.getNearNodeMapping(visualDropContainer);
			switch (nodeMapping.getType()) {
			case VpeNodeMapping.TEXT_MAPPING:
				sourceDropContainer = nodeMapping.getSourceNode();
				sourceDropOffset = TextUtil.sourceInnerPosition(sourceDropContainer.getNodeValue(), visualDropOffset);
				break;
			case VpeNodeMapping.ELEMENT_MAPPING:
				// it's attribute
				if (isTextEditable(visualDropContainer)) {
					String[] atributeNames = ((VpeElementMapping)nodeMapping).getTemplate().getOutputAtributeNames();
					if (atributeNames != null && atributeNames.length > 0) {
						Element sourceElement = (Element)nodeMapping.getSourceNode();
						sourceDropContainer = sourceElement.getAttributeNode(atributeNames[0]);
						sourceDropOffset = TextUtil.sourceInnerPosition(sourceDropContainer.getNodeValue(), visualDropOffset);
					}
				}
				nodeMapping.getVisualNode();
			}
			break;
		}
		if (sourceDropContainer != null) {
			return getSourceInnerDropInfo(sourceDragNode, sourceDropContainer, sourceDropOffset, checkParentTemplates);
		} else {
			return new VpeSourceInnerDropInfo(null, 0, false);
		}
	}
	
	VpeSourceInnerDropInfo getSourceInnerDropInfo(Node dragNode, Node container, int offset, boolean checkParentsTemplates) {
		//Thread.dumpStack();
		boolean canDrop = false;
		switch (container.getNodeType()) {
		case Node.ELEMENT_NODE:
			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(container);
			if (nodeMapping != null && nodeMapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {
			canDrop = ((VpeElementMapping)nodeMapping).getTemplate().canInnerDrop(pageContext, container,  dragNode);
			}
			if (!canDrop) {
				if(!checkParentsTemplates) return new VpeSourceInnerDropInfo(container, offset, canDrop);
				offset = ((NodeImpl)container).getIndex();
				container = container.getParentNode();
				return getSourceInnerDropInfo(dragNode, container, offset, false);
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
			Node visualDropContainer = mapping.getVisualNode();
			switch (mapping.getType()) {
			case VpeNodeMapping.TEXT_MAPPING:
				break;
			case VpeNodeMapping.ELEMENT_MAPPING:
				Node visualParent = visualDropContainer.getParentNode();
				VpeNodeMapping oldMapping = mapping;
				mapping = domMapping.getNearNodeMapping(visualParent);
				MozillaSupports.release(visualParent);
				if (mapping != null && mapping.getType() == VpeNodeMapping.ELEMENT_MAPPING) {
					((VpeElementMapping)mapping).getTemplate().innerDrop(pageContext,
							new VpeSourceInnerDragInfo(dragNode, 0, 0),
							new VpeSourceInnerDropInfo(container, offset, true));
				} else {
					((VpeElementMapping)oldMapping).getTemplate().innerDrop(pageContext,
							new VpeSourceInnerDragInfo(dragNode, 0, 0),
							new VpeSourceInnerDropInfo(container, offset, true));
				}
			}
			
		}
	}

	void innerDrop(VpeSourceInnerDragInfo dragInfo, VpeSourceInnerDropInfo dropInfo) {
		dropper.drop(pageContext, dragInfo, dropInfo);
	}
	
	Element getNearDragElement(Element visualElement) {
		VpeElementMapping elementMapping = domMapping.getNearElementMapping(visualElement);
		while (elementMapping != null) {
			if (canInnerDrag((Element)elementMapping.getVisualNode())) {
				return (Element)elementMapping.getVisualNode();
			}
			elementMapping = domMapping.getNearElementMapping(elementMapping.getVisualNode().getParentNode());
		}
		return null;
	}
	
	Element getDragElement(Element visualElement) {
		VpeElementMapping elementMapping = domMapping.getNearElementMapping(visualElement);
		if (elementMapping != null && canInnerDrag((Element)elementMapping.getVisualNode())) {
			return (Element)elementMapping.getVisualNode();
		}
		return null;
	}

	public boolean isTextEditable(Node visualNode) {
		if (visualNode != null) {
			Node parent = visualNode.getParentNode();
			if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)parent;
				Attr style = element.getAttributeNode("style");
				if (style != null) {
					String styleValue = style.getNodeValue();
					MozillaSupports.release(style);
					String [] items = styleValue.split(";");
					for (int i = 0; i < items.length; i++) {
						String[] item = items[i].split(":");
						if ("-moz-user-modify".equals(item[0].trim()) && "read-only".equals(item[1].trim())) {
							MozillaSupports.release(parent);
							return false;
						}
					}
				}
				Attr classAttr = element.getAttributeNode("class");
				if (classAttr != null) {
					String classValue = classAttr.getNodeValue().trim();
					MozillaSupports.release(classAttr);
					if ("__any__tag__caption".equals(classValue)) {
						MozillaSupports.release(parent);
						return false;
					}
				}
			}
			MozillaSupports.release(parent);
		}
		return true;
	}
	
	VpeVisualInnerDropInfo getInnerDropInfo(Node sourceDropContainer, int sourceDropOffset) {
		Node visualDropContainer = null;
		int visualDropOffset = 0;

		switch (sourceDropContainer.getNodeType()) {
		case Node.TEXT_NODE:
			visualDropContainer = domMapping.getVisualNode(sourceDropContainer);
			visualDropOffset = TextUtil.visualInnerPosition(sourceDropContainer.getNodeValue(), sourceDropOffset);
			break;
		case Node.ELEMENT_NODE:
		case Node.DOCUMENT_NODE:
			NodeList sourceChildren = sourceDropContainer.getChildNodes();
			if (sourceDropOffset < sourceChildren.getLength()) {
				Node sourceChild = sourceChildren.item(sourceDropOffset);
				Node visualChild = domMapping.getVisualNode(sourceChild);
				if (visualChild != null) {
					visualDropContainer = visualChild.getParentNode();
					visualDropOffset = MozillaSupports.getOffset(visualChild);
				}
			}
			if (visualDropContainer == null) {
				visualDropContainer = domMapping.getNearVisualNode(sourceDropContainer);
				Node visualChild = getLastAppreciableVisualChild(visualDropContainer);
				if (visualChild != null) {
					visualDropOffset = MozillaSupports.getOffset(visualChild) + 1;
				} else {
					visualDropOffset = 0;
				}
			}
			break;
		case Node.ATTRIBUTE_NODE:
			Element sourceElement = ((Attr)sourceDropContainer).getOwnerElement();
			VpeElementMapping elementMapping = domMapping.getNearElementMapping(sourceElement);
			Node textNode = elementMapping.getTemplate().getOutputTextNode(pageContext, sourceElement, elementMapping.getData());
			if (textNode != null) {
				visualDropContainer = textNode;
				visualDropOffset = TextUtil.visualInnerPosition(sourceDropContainer.getNodeValue(), sourceDropOffset);
			}
			break;
		}
		if (visualDropContainer == null) {
			return null;
		}
		return new VpeVisualInnerDropInfo(visualDropContainer, visualDropOffset, 0, 0); 
	}
	
	protected void setTooltip(Element sourceElement, Element visualElement) {
		if (visualElement != null && sourceElement != null && !((ElementImpl)sourceElement).isJSPTag()) {
			if ("HTML".equalsIgnoreCase(sourceElement.getNodeName())) return;
			String titleValue = getTooltip(sourceElement);
			
			if (titleValue != null) {
				titleValue = titleValue.replaceAll("&", "&amp;");
				titleValue = titleValue.replaceAll("<", "&lt;");
				titleValue = titleValue.replaceAll(">", "&gt;");
			}
			
			if (titleValue != null) {
//				visualElement.setAttribute("title", titleValue);
				setTooltip(visualElement, titleValue);
			}
		}
	}
	
	private void setTooltip(Element visualElement, String titleValue) {
		visualElement.setAttribute("title", titleValue);
		NodeList children = visualElement.getChildNodes();
		int len = children.getLength();
		for (int i = 0; i < len; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				setTooltip((Element)child, titleValue);
			}
			MozillaSupports.release(child);
		}
		MozillaSupports.release(children);
	}
	
	private void resetTooltip(Element sourceElement, Element visualElement) {
		if (visualElement != null && sourceElement != null && !((ElementImpl)sourceElement).isJSPTag()) {
			if ("HTML".equalsIgnoreCase(sourceElement.getNodeName())) return;
			String titleValue = getTooltip(sourceElement);
			
			if (titleValue != null) {
				titleValue = titleValue.replaceAll("&", "&amp;");
				titleValue = titleValue.replaceAll("<", "&lt;");
				titleValue = titleValue.replaceAll(">", "&gt;");
			}
			
			if (titleValue != null) {
				resetTooltip(visualElement, titleValue);
			}
		}
	}
	
	private void resetTooltip(Element visualElement, String titleValue) {
		visualElement.setAttribute("title", titleValue);
		NodeList children = visualElement.getChildNodes();
		int len = children.getLength();
		for (int i = 0; i < len; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (domMapping.getNodeMapping(child) == null) {
					resetTooltip((Element)child, titleValue);
				}
			}
			MozillaSupports.release(child);
		}
		MozillaSupports.release(children);
	}
	
	private String getTooltip(Element sourceElement) {
		 StringBuffer buffer = new StringBuffer(); 
		 buffer.append(sourceElement.getNodeName());
		 NamedNodeMap attrs = sourceElement.getAttributes();
		 int len = attrs.getLength();
		 for (int i = 0; i < len; i++){
			if (i == 7)
			{
				 return buffer.append("\n\t... ").toString();				 
			}
			int valueLength = attrs.item(i).getNodeValue().length();
			if (valueLength > 30) {
				StringBuffer temp = new StringBuffer(); 
				temp.append(attrs.item(i).getNodeValue().substring(0,15) + " ... " 
						+ attrs.item(i).getNodeValue().substring(valueLength - 15,valueLength));
				buffer.append("\n" + attrs.item(i).getNodeName() + ": " + temp);
			}
			else 
			 buffer.append("\n" + attrs.item(i).getNodeName() + ": " + attrs.item(i).getNodeValue());
			
		 }
		 
		return buffer.toString();
	}
	
	Rectangle getNodeBounds(Node visualNode) {
		return dnd.getBounds((nsIDOMNode)visualNode);
	}
	
	static boolean canInsertAfter(int x, int y, Rectangle rect) {
		if (y > (rect.y + rect.height) || x > (rect.x + rect.width)) {
			return true;
		}
		return y >= rect.x && x > (rect.x + rect.width / 2);
	}
	
	static Node getLastAppreciableVisualChild(Node visualParent) {
		Node visualLastChild = null;
		NodeList visualChildren = visualParent.getChildNodes();
		int len = visualChildren.getLength();
		for (int i = len - 1; i >= 0; i--) {
			Node visualChild = visualChildren.item(i);
			if (!isPseudoElement(visualChild) && !isAnonElement(visualChild)) {
				visualLastChild = visualChild;
				break;
			}
			MozillaSupports.release(visualChild);
		}
		MozillaSupports.release(visualChildren);
		return visualLastChild;
	}
	
	void correctVisualDropPosition(VpeVisualInnerDropInfo newVisualDropInfo, VpeVisualInnerDropInfo oldVisualDropInfo) {
		Node newVisualDropContainer = newVisualDropInfo.getDropContainer();
		Node oldVisualDropContainer = oldVisualDropInfo.getDropContainer();

		if (newVisualDropContainer.equals(oldVisualDropContainer)) {
			newVisualDropInfo.setDropOffset(oldVisualDropInfo.getDropOffset());
			return;
		}
		
		Node child = oldVisualDropContainer;
		while (child != null && child.getNodeType() != Node.DOCUMENT_NODE) {
			Node parent = child.getParentNode();
			if (newVisualDropContainer.equals(parent)) {
				int offset = MozillaSupports.getOffset(child);
				Rectangle rect = getNodeBounds(child);
				if (canInsertAfter(oldVisualDropInfo.getMouseX(), oldVisualDropInfo.getMouseY(), rect)) {
					offset++;
				}
				newVisualDropInfo.setDropOffset(offset);
			}
			child = parent;
		}
	}

	public nsIDOMRange createDOMRange() {
		return browser.createDOMRange();
	}

	public nsIDOMRange createDOMRange(Node selectedNode) {
		nsIDOMRange range = createDOMRange();
		range.selectNode((nsIDOMNode)selectedNode);
		return range;
	}
	
	public static boolean isIncludeElement(Element visualElement) {
        return YES_STRING.equalsIgnoreCase(visualElement.getAttribute(INCLUDE_ELEMENT_ATTR));
	}
	
	public static void markIncludeElement(Element visualElement) {
		visualElement.setAttribute(INCLUDE_ELEMENT_ATTR, YES_STRING);
	}
	
	protected void setReadOnlyElement(Element node) {
		String style = node.getAttribute(VpeStyleUtil.ATTRIBUTE_STYLE);
		style = VpeStyleUtil.setParameterInStyle(style, "-moz-user-modify", "read-only");
		node.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, style);
	}
	
	void setMoveCursor(nsIDOMMouseEvent mouseEvent) {
		Element selectedElement = browser.getSelectedElement();
		if (selectedElement != null && canInnerDrag(selectedElement)) {
			if (inDragArea(getNodeBounds(selectedElement), mouseEvent.getMousePoint())) {
				dnd.setMoveCursor();
			}
		}
	}
	
	private boolean inDragArea(Rectangle dragArea, Point mousePoint) {
		return dragArea.contains(mousePoint) &&
					mousePoint.x < (dragArea.x + DRAG_AREA_WIDTH) && 
					mousePoint.y < (dragArea.y + DRAG_AREA_HEIGHT); 
	}
	
	Element getDragElement(nsIDOMMouseEvent mouseEvent) {
		Element selectedElement = browser.getSelectedElement();
		if (selectedElement != null && canInnerDrag(selectedElement)) {
			if (inDragArea(getNodeBounds(selectedElement), mouseEvent.getMousePoint())) {
				return selectedElement;
			}
		}
		return null;
	}
	
	VpeSourceInnerDragInfo getSourceInnerDragInfo(VpeVisualInnerDragInfo visualDragInfo) {
		Node visualNode = visualDragInfo.getNode();
		int offset = visualDragInfo.getOffset();
		int length = visualDragInfo.getLength();
		
		VpeNodeMapping nodeMapping = domMapping.getNearNodeMapping(visualNode);
		Node sourceNode = nodeMapping.getSourceNode();

		if (sourceNode != null) {
			switch (sourceNode.getNodeType()) {
			case Node.TEXT_NODE:
				int end = TextUtil.sourceInnerPosition(visualNode.getNodeValue(), offset + length);
				offset = TextUtil.sourceInnerPosition(visualNode.getNodeValue(), offset);
				length = end - offset;
				break;
			case Node.ELEMENT_NODE:
				if (visualNode.getNodeType() == Node.TEXT_NODE) {
					// it's attribute
					sourceNode = null;
					if (isTextEditable(visualNode)) {
						String[] atributeNames = ((VpeElementMapping)nodeMapping).getTemplate().getOutputAtributeNames();
						if (atributeNames != null && atributeNames.length > 0) {
							Element sourceElement = (Element)nodeMapping.getSourceNode();
							sourceNode = sourceElement.getAttributeNode(atributeNames[0]);
							end = TextUtil.sourceInnerPosition(visualNode.getNodeValue(), offset + length);
							offset = TextUtil.sourceInnerPosition(visualNode.getNodeValue(), offset);
							length = end - offset;
						}
					}
				}
				break;
			}
		}
		return new VpeSourceInnerDragInfo(sourceNode, offset, length);
	}
	
	/* Sergey Vasilyev
	* for testing control mouse pointer from VpeController
	*/
	public void SetCursor(String aCursorName, int aLock) {
		dnd.SetCursor(aCursorName, aLock);
	}

	public Node getOutputTextNode(Attr attr) {
		Element sourceElement = ((Attr)attr).getOwnerElement();
		VpeElementMapping elementMapping = domMapping.getNearElementMapping(sourceElement);
		if (elementMapping != null) {
			return elementMapping.getTemplate().getOutputTextNode(pageContext, sourceElement, elementMapping.getData());
		}
		return null;
	}
	
	Element getLastSelectedElement() {
		return browser.getSelectedElement();
	}
	
	public void pushIncludeStack(VpeIncludeInfo includeInfo) {
		includeStack.add(includeInfo);
	}
	
	public VpeIncludeInfo popIncludeStack() {
		VpeIncludeInfo includeInfo = null;
		if (includeStack.size() > 0) {
			includeInfo = (VpeIncludeInfo)includeStack.remove(includeStack.size() - 1);
		}
		return includeInfo;
	}

	public boolean isFileInIncludeStack(IFile file) {
		if (file == null) return false;
		for (int i = 0; i < includeStack.size(); i++) {
			if (file.equals(((VpeIncludeInfo)includeStack.get(i)).getFile())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isCurrentMainDocument() {
		return includeStack.size() <= 1;
	}
	
	public int getCurrentMainIncludeOffset() {
		if (includeStack.size() <= 1) return -1;
		VpeIncludeInfo info = (VpeIncludeInfo)includeStack.get(1);
		return ((IndexedRegion)info.getElement()).getStartOffset();
	}
	
	public VpeIncludeInfo getCurrentIncludeInfo() {
		if (includeStack.size() <= 0) return null;
		return (VpeIncludeInfo)includeStack.get(includeStack.size() - 1);
	}
	
	public VpeIncludeInfo getRootIncludeInfo() {
		if (includeStack.size() <= 1) return null;
		return (VpeIncludeInfo)includeStack.get(1);
	}

	void dispose() {
	    cleanHead();
		domMapping.clear(visualContentArea);
		pageContext.dispose();
	}
	
	protected Map<String, Integer> createXmlns(Element sourceNode) {
		NamedNodeMap attrs = ((Element)sourceNode).getAttributes();
		if (attrs != null) {
			Map<String, Integer> xmlnsMap = new HashMap<String, Integer>();
			for (int i = 0; i < attrs.getLength(); i++) {
				addTaglib(sourceNode, xmlnsMap, attrs.item(i).getNodeName(), true);
			}
			if (xmlnsMap.size() > 0) {
				return xmlnsMap;
			}
		}
		return null;
	}

	private void setXmlnsAttribute(VpeElementMapping elementMapping, String name, String value) {
		Element sourceElement = (Element)elementMapping.getSourceNode();
		if (sourceElement != null) {
			Map<String, Integer> xmlnsMap = elementMapping.getXmlnsMap();
			if (xmlnsMap == null) xmlnsMap = new HashMap<String, Integer>();
			addTaglib(sourceElement, xmlnsMap, name, true);
			elementMapping.setXmlnsMap(xmlnsMap.size() > 0 ? xmlnsMap : null);
		}
	}

	private void removeXmlnsAttribute(VpeElementMapping elementMapping, String name) {
		Element sourceElement = (Element)elementMapping.getSourceNode();
		if (sourceElement != null) {
			Map<String, Integer> xmlnsMap = elementMapping.getXmlnsMap();
			if (xmlnsMap != null) {
				Object id = xmlnsMap.remove(name);
				if (id != null) {
					pageContext.setTaglib(((Integer)id).intValue(), null, null, true);
					elementMapping.setXmlnsMap(xmlnsMap.size() > 0 ? xmlnsMap : null);
				}
			}
		}
	}

	private void addTaglib(Element sourceElement, Map<String, Integer> xmlnsMap, String attrName, boolean ns) {
		Attr attr = sourceElement.getAttributeNode(attrName);
		if (ATTR_XMLNS.equals(attr.getPrefix())) {
			xmlnsMap.put(attr.getNodeName(), new Integer(attr.hashCode()));
			pageContext.setTaglib(attr.hashCode(), attr.getNodeValue(), attr.getLocalName(), ns);
		}
	}
	
	/**
	 * Check this file is facelet
	 * @return this if file is facelet, otherwize false
	 */
	private boolean isFacelet() {
		boolean isFacelet = false;
		
		IEditorInput iEditorInput = pageContext.getEditPart().getEditorInput();
		if ( iEditorInput instanceof IFileEditorInput ) {
			IFileEditorInput iFileEditorInput = (IFileEditorInput) iEditorInput;
			
			IFile iFile = iFileEditorInput.getFile();
		
			IProject project = iFile.getProject();
			IModelNature nature = EclipseResourceUtil.getModelNature(project);
			if (nature != null) { 
				XModel model = nature.getModel();
				XModelObject webXML = WebAppHelper.getWebApp(model); 
				XModelObject param = WebAppHelper.findWebAppContextParam(webXML, "javax.faces.DEFAULT_SUFFIX");
				if ( param != null ) {
					String value = param.getAttributeValue("param-value");
			
					if ( value.length() != 0 && iFile.getName().endsWith(value)) {
						isFacelet = true;
					}
				}
			}
		}
		
		return isFacelet;		
	}
}

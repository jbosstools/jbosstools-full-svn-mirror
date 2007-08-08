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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * class  VpePreviewDomBuilder for create DOM-tree for Mozilla preview
 * @author A. Yukhovich
 */
public class VpePreviewDomBuilder extends VpeVisualDomBuilder {
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
	
	private MozillaEditor visualEditor;
	private XulRunnerEditor browser;
//	private MozillaBrowser browser; 
	private nsIDOMDocument visualDocument;
	private nsIDOMElement visualContentArea;
	private VpePageContext pageContext;
	private nsIDOMNode headNode;
	private List includeStack;
	boolean rebuildFlag = false;
	
	private static final String EMPTY_STRING = ""; 
	
	private static final String ATTR_VPE     = "vpe";
	private static final String ATTR_VPE_INLINE_LINK_VALUE = "inlinelink";
	
	private static final String ATTR_REL_STYLESHEET_VALUE = "stylesheet";
	
	private static final String YES_STRING   = "yes";
	private static final String NO_STRING    = "no";	
	
	/**
	 * 
	 * @param domMapping
	 * @param sorceAdapter
	 * @param templateManager
	 * @param visualEditor
	 * @param pageContext
	 */
	public VpePreviewDomBuilder(VpeDomMapping domMapping, INodeAdapter sorceAdapter, VpeTemplateManager templateManager, MozillaEditor visualEditor, VpePageContext pageContext) {
		super(domMapping, sorceAdapter, templateManager, visualEditor, pageContext);
		this.visualEditor = visualEditor;
		browser = (XulRunnerEditor)visualEditor.getControl();
		this.visualDocument = visualEditor.getDomDocument();
		this.visualContentArea = visualEditor.getContentArea();
		this.pageContext = pageContext;
		this.headNode = visualEditor.getHeadNode();
	}
	
	/**
	 * 
	 */
	public void buildDom(Document sourceDocument) {
		includeStack = new ArrayList();
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
	
	/**
	 * 
	 * @param sourceNode
	 * @param visualNextNode
	 * @param visualContainer
	 * @return
	 */
	private boolean addNode(Node sourceNode, nsIDOMNode visualNextNode, nsIDOMNode visualContainer) {
		nsIDOMNode visualNewNode = createNode(sourceNode, visualContainer);
		if (visualNewNode != null) {
			nsIDOMNode visualAddedNode = visualNextNode == null ? 
						visualContainer.appendChild(visualNewNode) : 
						visualContainer.insertBefore(visualNewNode, visualNextNode);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param sourceNode
	 * @param visualOldContainer
	 * @return
	 */
	private nsIDOMNode createNode(Node sourceNode, nsIDOMNode visualOldContainer) {
		boolean registerFlag = isCurrentMainDocument();
		switch (sourceNode.getNodeType()) {
		case Node.ELEMENT_NODE:
			Map xmlnsMap = createXmlns((Element)sourceNode);
			Set ifDependencySet = new HashSet();
			pageContext.setCurrentVisualNode(visualOldContainer);
			VpeTemplate template = templateManager.getTemplate(pageContext, (Element)sourceNode, ifDependencySet);
			VpeCreationData creationData;
			
			if ( template.isHaveVisualPreview() ) {
				creationData = template.create(pageContext, sourceNode, visualDocument);
			} else {
				nsIDOMElement tempHTMLElement = visualDocument.createElement(HTML.TAG_DIV);
				creationData = new VpeCreationData(tempHTMLElement);				
			}
			
			pageContext.setCurrentVisualNode(null);
			nsIDOMElement visualNewElement;
			visualNewElement = (nsIDOMElement)creationData.getNode();
			setTooltip((Element)sourceNode, visualNewElement);

			if (!isCurrentMainDocument() && visualNewElement != null) {
				setReadOnlyElement(visualNewElement);
			}

			if (template.isChildren()) {
				List childrenInfoList = creationData.getChildrenInfoList();
				if (childrenInfoList == null) {
					addChildren(template, sourceNode, visualNewElement != null ? visualNewElement : visualOldContainer);
				} else {
					addChildren(template, sourceNode, visualOldContainer, childrenInfoList);
				}
			}
			pageContext.setCurrentVisualNode(visualOldContainer);
			template.validate(pageContext, (Element)sourceNode, visualDocument, creationData);
			pageContext.setCurrentVisualNode(null);
			return visualNewElement;
		case Node.TEXT_NODE:
			String sourceText = sourceNode.getNodeValue();
			if (sourceText.trim().length() <= 0) {
				registerNodes(new VpeNodeMapping(sourceNode, null));
				return null;
			}
			String visualText = TextUtil.visualText(sourceText);
			nsIDOMNode visualNewTextNode = visualDocument.createTextNode(visualText);
			if (registerFlag) {
				registerNodes(new VpeNodeMapping(sourceNode, visualNewTextNode));
			}
			return visualNewTextNode;
		case Node.COMMENT_NODE:
			if(!YES_STRING.equals(VpePreference.SHOW_COMMENTS.getValue())) {
				return null;
			}
			nsIDOMElement visualNewComment = createComment(sourceNode);
			if (registerFlag) {
				registerNodes(new VpeNodeMapping(sourceNode, visualNewComment));
			}
			return visualNewComment;
		}
		return null;
	}
}

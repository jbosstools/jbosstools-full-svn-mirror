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
import java.util.Map;
import java.util.Set;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * class  VpePreviewDomBuilder for create DOM-tree for Mozilla preview
 * @author A. Yukhovich
 */
public class VpePreviewDomBuilder extends VpeVisualDomBuilder {


	boolean rebuildFlag = false;

	
	private static final String YES_STRING   = "yes";
	
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

	}
	
	/**
	 * 
	 * @param sourceNode
	 * @param visualOldContainer
	 * @return
	 */
	@Override
	protected nsIDOMNode createNode(Node sourceNode, nsIDOMNode visualOldContainer) {
		boolean registerFlag = isCurrentMainDocument();
		switch (sourceNode.getNodeType()) {
		case Node.ELEMENT_NODE:
//			Map xmlnsMap = createXmlns((Element)sourceNode);
			Set ifDependencySet = new HashSet();
			VpeTemplate template = templateManager.getTemplate(getPageContext(), (Element)sourceNode, ifDependencySet);
			VpeCreationData creationData;
			
			//FIX FOR JBIDE-1568, added by Max Areshkau
			try {
			if ( template.isHaveVisualPreview() ) {
				creationData = template.create(getPageContext(), sourceNode, getVisualDocument());
			} else {
				nsIDOMElement tempHTMLElement = getVisualDocument().createElement(HTML.TAG_DIV);
				creationData = new VpeCreationData(tempHTMLElement);				
			}
			}catch (XPCOMException ex) {
				VpePlugin.getPluginLog().logError(ex);
				VpeTemplate defTemplate = templateManager.getDefTemplate();
				creationData = defTemplate.create(getPageContext(), sourceNode, getVisualDocument());
			}
			
			nsIDOMElement visualNewElement;
			visualNewElement = (nsIDOMElement)creationData.getNode();
			setTooltip((Element)sourceNode, visualNewElement);

			if (!isCurrentMainDocument() && visualNewElement != null) {
				setReadOnlyElement(visualNewElement);
			}

			if (template.isChildren()) {
				List<?> childrenInfoList = creationData.getChildrenInfoList();
				if (childrenInfoList == null) {
					addChildren(template, sourceNode, visualNewElement != null ? visualNewElement : visualOldContainer);
				} else {
					addChildren(template, sourceNode, visualOldContainer, childrenInfoList);
				}
			}
			template.validate(getPageContext(), (Element)sourceNode, getVisualDocument(), creationData);
			return visualNewElement;
		case Node.TEXT_NODE:
			return createTextNode(sourceNode, registerFlag);
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

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

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.proxy.VpeProxyUtil;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
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
	
	/**
	 * 
	 * @param domMapping
	 * @param sorceAdapter
	 * @param templateManager
	 * @param visualEditor
	 * @param pageContext
	 */
	public VpePreviewDomBuilder(VpeDomMapping domMapping, INodeAdapter sorceAdapter, VpeTemplateManager templateManager, MozillaEditor visualEditor, VpePageContext pageContext) {
		super(domMapping, sorceAdapter, visualEditor, pageContext);
	}
	
	@Override
	public nsIDOMNode createNode(Node sourceNode, nsIDOMNode visualOldContainer, boolean onlyOneIncludeStack) {
			Set<?> ifDependencySet = new HashSet();
		    if(sourceNode==null||(
		    		sourceNode.getNodeType()!=Node.TEXT_NODE
		    		&&sourceNode.getNodeType()!=Node.ELEMENT_NODE
		    		&&sourceNode.getNodeType()!=Node.COMMENT_NODE
		    		&&sourceNode.getNodeType()!=Node.DOCUMENT_NODE)) {
		        return null;
		    }
			getPageContext().setCurrentVisualNode(visualOldContainer);
			VpeTemplate template = getTemplateManager().getTemplate(getPageContext(), sourceNode, ifDependencySet);
			VpeCreationData creationData;
			
			//FIX FOR JBIDE-1568, added by Max Areshkau
			try {
		          if (getPageContext().getElService().isELNode(sourceNode)) {
                    final Node sourceNodeProxy =  VpeProxyUtil.createProxyForELExpressionNode(
                    		getPageContext(), sourceNode);
    				try {
    					creationData = template.create(getPageContext(),
    							sourceNodeProxy, getVisualDocument());
    					//Fix for JBIDE-3144, we use proxy and some template can 
    					//try to cast for not supported interface
    					} catch(ClassCastException ex) {
    						VpePlugin.reportProblem(ex);
    						//then we create template without using proxy
    						creationData = template.create(getPageContext(), 
    								sourceNode, getVisualDocument());
    					}
		          } else {
                    creationData = template.create(getPageContext(), sourceNode, getVisualDocument());
                }
			}catch (XPCOMException ex) {
				VpePlugin.getPluginLog().logError(ex);
				VpeTemplate defTemplate = getTemplateManager().getDefTemplate();
				creationData = defTemplate.create(getPageContext(), sourceNode, getVisualDocument());
			}
			getPageContext().setCurrentVisualNode(null);
			nsIDOMNode visualNewNode;
			visualNewNode = creationData.getNode();
			if(sourceNode instanceof Element && visualNewNode != null) {		
				setTooltip((Element)sourceNode, queryInterface(visualNewNode, nsIDOMElement.class));
				correctVisualAttribute(queryInterface(visualNewNode, nsIDOMElement.class));
			}
			if (template.hasChildren()) {
				List<?> childrenInfoList = creationData.getChildrenInfoList();
				if (childrenInfoList == null) {
					addChildren(template, sourceNode, visualNewNode != null ? visualNewNode : visualOldContainer);
				} else {
					addChildren(template, sourceNode, visualOldContainer, childrenInfoList);
				}
			}
			/*
			 * Setting current visual node was added
			 * to fix h:dataTable content visibility on Preview tab.
			 * http://jira.jboss.com/jira/browse/JBIDE-2059
			 */
			getPageContext().setCurrentVisualNode(visualOldContainer);
			template.validate(getPageContext(), sourceNode, getVisualDocument(), creationData);
			getPageContext().setCurrentVisualNode(null);
			
			return visualNewNode;
	}
}
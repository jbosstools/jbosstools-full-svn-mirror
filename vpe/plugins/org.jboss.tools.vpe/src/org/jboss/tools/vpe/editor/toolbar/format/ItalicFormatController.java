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
package org.jboss.tools.vpe.editor.toolbar.format;

import org.eclipse.swt.widgets.ToolItem;
import org.w3c.dom.Attr;

import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;

/**
 * @author Igels
 */
public class ItalicFormatController extends ToolItemFormatController {

	public static String TYPE = "ItalicFormat"; //$NON-NLS-1$

	private static String STYLE_PROPERTY_NAME = "FONT-STYLE"; //$NON-NLS-1$
	private static String STYLE_PROPERTY_ITALIC_VALUE = "italic"; //$NON-NLS-1$
	private static String STYLE_PROPERTY_NORMAL_VALUE = "normal"; //$NON-NLS-1$

	/**
	 * @param manager
	 * @param toolItem
	 */
	public ItalicFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager, toolItem);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
	 */
	public String getType() {
		return TYPE;
	}

//	protected boolean isStyleSet(Attr styleAttribute) {
//		return isStyleSet(STYLE_PROPERTY_NAME, STYLE_PROPERTY_VALUE, styleAttribute);
//	}

	protected void setStyle(Attr styleAttribute, FormatAttributeData templateData) {
		String setValue = templateData.getParentFormatData().isSetDefault()?STYLE_PROPERTY_NORMAL_VALUE:STYLE_PROPERTY_ITALIC_VALUE;
		invertSingleStyleProperty(styleAttribute, STYLE_PROPERTY_NAME, setValue);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.AttributeFormatController#isStyleSet(org.w3c.dom.Attr, org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData)
	 */
	protected boolean isStyleSet(Attr attribute, FormatAttributeData templateData) {
		boolean isSetDefault = templateData.getParentFormatData().isSetDefault();
		String value = isSetDefault?STYLE_PROPERTY_NORMAL_VALUE:STYLE_PROPERTY_ITALIC_VALUE;
		boolean isStyleSet = isStyleSet(STYLE_PROPERTY_NAME, value, attribute);
		if(isSetDefault) {
			return !isStyleSet;
		} else {
			return isStyleSet;
		}
	}

/*
	public void run() {
		VpeSourceSelection selection = manager.getSelection();
//		VpeDomMapping domMapping = manager.getVpeController().getDomMapping();
		List nodeList = selection.getSelectedNodes();
		VpeSelectedNodeInfo nodeInfo = manager.getSelectedNode();
		Node selectedNode = nodeInfo.getNode();
		if(selectedNode instanceof TextImplForJSP) {
			String value = selectedNode.getNodeValue();
			int startOffset = nodeInfo.getStartOffset()>nodeInfo.getEndOffset()?nodeInfo.getEndOffset():nodeInfo.getStartOffset();
			int endOffset = nodeInfo.getStartOffset()>nodeInfo.getEndOffset()?nodeInfo.getStartOffset():nodeInfo.getEndOffset();
			value = value.substring(startOffset, endOffset);
		}

		for(int i=0; i<nodeList.size(); i++) {
			VpeSelectedNodeInfo node = (VpeSelectedNodeInfo)nodeList.get(i);
		}
		nodeList = manager.getCleanSelectedNodesList();
		for(int i=0; i<nodeList.size(); i++) {
			VpeSelectedNodeInfo node = (VpeSelectedNodeInfo)nodeList.get(i);
		}
//		VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(node);
//		VpeTemplate template = elementMapping.getTemplate();

		// TODO remove this method
	}

	public void setToolbarItemEnabled(boolean enabled) {
		// TODO remove this method.
		toolItem.setEnabled(true);
	}
*/
}
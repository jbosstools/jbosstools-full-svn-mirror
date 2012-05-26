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
package org.jboss.tools.vpe.editor.template.resize;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeTagDescription;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.SourceDomUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 */
public class VpeResizer {
	/** TAG_WIDTH */
	private static final String TAG_WIDTH  = VpeTemplateManager.VPE_PREFIX + "width"; //$NON-NLS-1$
	
	/** TAG_HEIGHT */
	private static final String TAG_HEIGHT = VpeTemplateManager.VPE_PREFIX + "height"; //$NON-NLS-1$
	
	private static final String ATTRIBUTE_WIDTH                     = "width-attr"; //$NON-NLS-1$
	private static final String ATTRIBUTE_HEIGHT                    = "height-attr"; //$NON-NLS-1$
	private static final String ATTRIBUTE_TAG_XPATH                 = "tag-xpath"; //$NON-NLS-1$
	private static final String ATTRIBUTE_TEST                      = "test"; //$NON-NLS-1$
	private static final String ATTRIBUTE_DISABLE_ABSOLUTE_POSITION = "disable-absolute-position"; //$NON-NLS-1$
	
	private TemplateResizeData horizontalData;
	private TemplateResizeData verticalData;
	
	public void setResizeData(Element node) {
		TemplateResizeData resizeWidth = null;
		TemplateResizeData resizeHeight = null;
		
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node innerNode = children.item(i);
				if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
					String name = innerNode.getNodeName();
					if (name.startsWith(VpeTemplateManager.VPE_PREFIX)) {
						if (TAG_WIDTH.equals(name)) {
							if(horizontalData == null){
								horizontalData = new TemplateResizeData(((Element)innerNode).getAttribute(ATTRIBUTE_WIDTH), VpeStyleUtil.PARAMETER_LEFT, ((Element)innerNode).getAttribute(ATTRIBUTE_TAG_XPATH), ((Element)innerNode).getAttribute(ATTRIBUTE_TEST), ((Element)innerNode).getAttribute(ATTRIBUTE_DISABLE_ABSOLUTE_POSITION));
								resizeWidth = horizontalData;
							}else{
								resizeWidth.setNext(new TemplateResizeData(((Element)innerNode).getAttribute(ATTRIBUTE_WIDTH), VpeStyleUtil.PARAMETER_LEFT, ((Element)innerNode).getAttribute(ATTRIBUTE_TAG_XPATH), ((Element)innerNode).getAttribute(ATTRIBUTE_TEST), ((Element)innerNode).getAttribute(ATTRIBUTE_DISABLE_ABSOLUTE_POSITION)));
								resizeWidth = resizeWidth.getNext();
							}
						}else if (TAG_HEIGHT.equals(name)){
							if(verticalData == null){
								verticalData = new TemplateResizeData(((Element)innerNode).getAttribute(ATTRIBUTE_HEIGHT), VpeStyleUtil.PARAMETER_TOP, ((Element)innerNode).getAttribute(ATTRIBUTE_TAG_XPATH), ((Element)innerNode).getAttribute(ATTRIBUTE_TEST), ((Element)innerNode).getAttribute(ATTRIBUTE_DISABLE_ABSOLUTE_POSITION));
								resizeHeight = verticalData;
							}else{
								resizeHeight.setNext(new TemplateResizeData(((Element)innerNode).getAttribute(ATTRIBUTE_HEIGHT), VpeStyleUtil.PARAMETER_TOP, ((Element)innerNode).getAttribute(ATTRIBUTE_TAG_XPATH), ((Element)innerNode).getAttribute(ATTRIBUTE_TEST), ((Element)innerNode).getAttribute(ATTRIBUTE_DISABLE_ABSOLUTE_POSITION)));
								resizeHeight = resizeHeight.getNext();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param pageContext
	 * @param sourceElement
	 * @param visualDocument
	 * @param visualElement
	 * @param data
	 * @param resizeConstrant
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 */
	public void resize(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Object data, int resizeConstrant, int top, int left, int width, int height) {
		pageContext.getEditPart().getSourceEditor().getTextViewer().getUndoManager().beginCompoundChange();
		if (pageContext.isAbsolutePosition() && (
				resizeConstrant == VpeTagDescription.RESIZE_CONSTRAINS_BOTTOMLEFT||
				resizeConstrant == VpeTagDescription.RESIZE_CONSTRAINS_LEFT||
				resizeConstrant == VpeTagDescription.RESIZE_CONSTRAINS_TOPLEFT||
				resizeConstrant == VpeTagDescription.RESIZE_CONSTRAINS_TOP||
				resizeConstrant == VpeTagDescription.RESIZE_CONSTRAINS_TOPRIGHT)) {
			if (horizontalData != null) {
				runResize(pageContext, sourceElement, true, left, width, horizontalData);
			}
			if (verticalData   != null) {
				runResize(pageContext, sourceElement, true, top, height, verticalData);
			}
		} else {
			if (horizontalData != null && resizeConstrant != VpeTagDescription.RESIZE_CONSTRAINS_TOP &&
						resizeConstrant != VpeTagDescription.RESIZE_CONSTRAINS_BOTTOM) {
				runResize(pageContext, sourceElement, false, left, width, horizontalData);
			}
			if (verticalData != null && resizeConstrant != VpeTagDescription.RESIZE_CONSTRAINS_LEFT &&
					resizeConstrant != VpeTagDescription.RESIZE_CONSTRAINS_RIGHT) {
				runResize(pageContext, sourceElement, false, top, height, verticalData);
			}
		}
		
		pageContext.getEditPart().getSourceEditor().getTextViewer().getUndoManager().endCompoundChange();
	}
	
	private void runResize(VpePageContext pageContext, Element sourceElement, boolean absolute, int position, int size, TemplateResizeData data){
		TemplateResizeData resizeData = data;
		while(resizeData != null){
			if(resizeData.getExpression() != null){
				try{
					VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(resizeData.getExpression(), false);
					VpeExpression testExpr = info.getExpression();
					VpeValue value = testExpr.exec(pageContext, sourceElement);
					if(value.booleanValue()){
						setSize(resizeData, sourceElement, absolute, position, size);
						break;
					}else{
						resizeData = resizeData.getNext();
					}
				}catch(VpeExpressionBuilderException ex){
					VpePlugin.reportProblem(ex);
					resizeData = resizeData.getNext();
				} catch (VpeExpressionException ex) {
					VpePlugin.reportProblem(ex);
					resizeData = resizeData.getNext();
				}
			}else{
				setSize(resizeData, sourceElement, absolute, position, size);
				break;
			}
		}
	}
	
	public void modifyTagDescription(VpeTagDescription tagDescription){
		if(horizontalData != null && verticalData != null){
			tagDescription.setResizeConstrains(VpeTagDescription.RESIZE_CONSTRAINS_ALL);
		}else if(horizontalData != null){
			tagDescription.setResizeConstrains(VpeTagDescription.RESIZE_CONSTRAINS_LEFT|VpeTagDescription.RESIZE_CONSTRAINS_RIGHT);
		}else if(verticalData != null){
			tagDescription.setResizeConstrains(VpeTagDescription.RESIZE_CONSTRAINS_TOP|VpeTagDescription.RESIZE_CONSTRAINS_BOTTOM);
		}else tagDescription.setResizeConstrains(VpeTagDescription.RESIZE_CONSTRAINS_NONE);
	}
	
	private void setSize(TemplateResizeData resizeData, Element sourceElement, boolean absolute, int position, int size){
		String sizeAttribute = resizeData.getSizeAttribute();
		String positionAttribute = resizeData.getPositionAttribute();
		if(absolute && resizeData.isAbsolutePositionEnabled()){
			VpeStyleUtil.setAbsolute(sourceElement);
			VpeStyleUtil.setParameterInStyle(sourceElement, VpeStyleUtil.ATTRIBUTE_STYLE+VpeStyleUtil.DOT_STRING+positionAttribute, position+VpeStyleUtil.PX_STRING);			
		}else{
			if(VpeStyleUtil.getAbsolute(sourceElement)){
				VpeStyleUtil.setParameterInStyle(sourceElement, VpeStyleUtil.ATTRIBUTE_STYLE+VpeStyleUtil.DOT_STRING+positionAttribute, position+VpeStyleUtil.PX_STRING);
			}
		}
		if(resizeData.getTagXpath() != null){
			Element ancestor = (Element)SourceDomUtil.getAncestorNode(sourceElement, resizeData.getTagXpath());
			if(ancestor != null){
				if(sizeAttribute.indexOf(VpeStyleUtil.DOT_STRING) < 0){
					ancestor.setAttribute(sizeAttribute, ""+size); //$NON-NLS-1$
				}else{
					VpeStyleUtil.setParameterInStyle(ancestor, sizeAttribute, size+VpeStyleUtil.PX_STRING);					
				}
			}
		}else{
			if(sizeAttribute.indexOf(VpeStyleUtil.DOT_STRING) < 0){
				sourceElement.setAttribute(sizeAttribute, ""+size); //$NON-NLS-1$
			}else{
				VpeStyleUtil.setParameterInStyle(sourceElement, sizeAttribute, size+VpeStyleUtil.PX_STRING);					
			}
		}
	}
	
	
}

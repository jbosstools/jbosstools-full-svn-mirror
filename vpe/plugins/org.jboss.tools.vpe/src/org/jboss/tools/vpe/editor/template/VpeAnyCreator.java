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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * Creator for any attribute
 * @author mareshkau
 *
 */
public class VpeAnyCreator extends VpeAbstractCreator {
	
	private static final String DEFAULT_TAG_FOR_DISPLAY=HTML.TAG_DIV;
	
	static final String CLASS_TAG_BLOCK = "__any__tag__block"; //$NON-NLS-1$
	static final String CLASS_TAG_INLINE = "__any__tag__inline"; //$NON-NLS-1$
	static final String CLASS_TAG_NONE = "__any__tag__none"; //$NON-NLS-1$
	static final String CLASS_TAG_CAPTION = "__any__tag__caption"; //$NON-NLS-1$


	private VpeExpression valueExpr;
	private VpeExpression tagForDisplayExpr;
	private VpeExpression styleExpr;
	
	private List propertyCreators;
	private Set dependencySet;

	private String tagForDisplayStr;
	private String valueStr;
	private String styleStr;
	private boolean showIconBool;

	VpeAnyCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		build(element, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr tagForDisplay = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_TAG_FOR_DISPLAY);
		if (tagForDisplay != null) {
			try {
				tagForDisplayStr = tagForDisplay.getNodeValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(tagForDisplayStr,true);
				tagForDisplayExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException ex) {
				VpePlugin.reportProblem(ex);
			}
		}
		
		Attr styleAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_STYLE);
		if (styleAttr!=null) {
			try {
				//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
				Node attrBorder = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_BORDER);
				StringBuffer stringBuffer =  new StringBuffer();
				
				if (attrBorder != null) {
					stringBuffer.append("border-width:").append(attrBorder.getNodeValue()) //$NON-NLS-1$
					.append(";"); //$NON-NLS-1$
				}
				//-----------END
				//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
				Node attrValueColor = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_VALUE_COLOR);
				if (attrValueColor  != null) {
					stringBuffer.append("color:").append(attrValueColor.getNodeValue()).append(";");  //$NON-NLS-1$//$NON-NLS-2$
				}
				//-----------END
				//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
				Node attrValueBackgroundColor = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_VALUE_BACKGROUND_COLOR);
				if (attrValueBackgroundColor != null) {
					stringBuffer.append("background-color:").append(attrValueBackgroundColor.getNodeValue()).append(";");  //$NON-NLS-1$//$NON-NLS-2$
				}
				//-----------END
				//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
				Node attrBachkgroundColor = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_BACKGROUND_COLOR);
				if (attrBachkgroundColor != null) {
					//early for displaying any tag was used <div><span></span></div>
					//and this property was for inner span, now used only one element 
					//and this property duplicates
					stringBuffer.append("background-color:").append(attrBachkgroundColor.getNodeValue()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				//-----------END
				//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
				Node attrBorderColor = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_BORDER_COLOR);
				if (attrBorderColor  != null) {
					stringBuffer.append("border-color:").append(attrBorderColor.getNodeValue()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				//-----------END
				if(stringBuffer.toString().length()>0) {
					styleStr = stringBuffer.toString();
				} else {
					styleStr = styleAttr.getValue();
				}
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(styleStr, true);
				styleExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
		
		Attr valueAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_VALUE);
		if (valueAttr != null) {
			try {
				valueStr = valueAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(valueStr, true);
				valueExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		if (VpeTemplateManager.ATTR_ANY_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_ANY_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_ANY_PROPERTIES[i];
				Attr attr = element.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null) propertyCreators  = new ArrayList();
					propertyCreators.add(new VpeAttributeCreator(attrName, attr.getValue(), dependencyMap, true));
				}
			}
		}
		Attr attr = element.getAttributeNode("title"); //$NON-NLS-1$
		if (attr == null) {
			if (propertyCreators == null) propertyCreators  = new ArrayList();
			propertyCreators.add(new VpeAttributeCreator("title", "{tagstring()}", dependencyMap, true)); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	@Override
	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) throws VpeExpressionException {
		
		String tagForDisplay =getExprValue(pageContext, tagForDisplayExpr, sourceNode);
		
		if(tagForDisplay == null||tagForDisplay.length()==0) {
			tagForDisplay = DEFAULT_TAG_FOR_DISPLAY;
		}
		
		nsIDOMElement anyElement = visualDocument.createElement(tagForDisplay);
		
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(anyElement);

		if(showIconBool){
			nsIDOMElement img = visualDocument.createElement(HTML.TAG_IMG);
			img.setAttribute("src","any.gif");  //$NON-NLS-1$//$NON-NLS-2$
			img.setAttribute("width","16"); //$NON-NLS-1$ //$NON-NLS-2$
			img.setAttribute("height","16"); //$NON-NLS-1$ //$NON-NLS-2$
			anyElement.appendChild(img);
		}
		
		anyElement.setAttribute(HTML.ATTR_CLASS, CLASS_TAG_CAPTION);
		
		String styleString = getExprValue(pageContext, styleExpr, sourceNode);
		
		if (JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS)) {
			styleString =  "border: 1px solid green;" +styleString; //$NON-NLS-1$
		}
		
		anyElement.setAttribute(HTML.ATTR_STYLE, styleString);
		
		if (propertyCreators != null) {
			for (int i = 0; i < propertyCreators.size(); i++) {
				VpeCreator creator = (VpeCreator)propertyCreators.get(i);
				if (creator != null) {
					VpeCreatorInfo info = creator.create(pageContext, (Element) sourceNode, visualDocument, anyElement, visualNodeMap);
					if (info != null && info.getVisualNode() != null) {
						nsIDOMAttr attr = (nsIDOMAttr)info.getVisualNode();
						anyElement.setAttributeNode(attr);
					}
				}
			}
		}

		String valueStr = getExprValue(pageContext, valueExpr, sourceNode);
		nsIDOMNode valueNode = visualDocument.createTextNode(valueStr);
		anyElement.appendChild(valueNode);
		creatorInfo.addDependencySet(dependencySet);
		return creatorInfo;
	}

//	private void setStyles(VpePageContext pageContext, Node sourceNode, nsIDOMElement div, nsIDOMElement span) throws VpeExpressionException {
//		boolean display = true;
//		boolean displayBlock = true;
//
//		if (displayExpr != null) {
//			VpeValue vpeValue = displayExpr.exec(pageContext, sourceNode);
//			if (vpeValue != null) {
//				String displayStr = vpeValue.stringValue();
//				if (caseSensitive) {
//					display = !VAL_DISPLAY_NONE.equals(displayStr);
//					displayBlock = display && !VAL_DISPLAY_INLINE.equals(displayStr);
//				} else {
//					display = !VAL_DISPLAY_NONE.equalsIgnoreCase(displayStr);
//					displayBlock = display && !VAL_DISPLAY_INLINE.equalsIgnoreCase(displayStr);
//				}
//			}
//		}
//
//		if (display) {
//			div.setAttribute("class", displayBlock ? CLASS_TAG_BLOCK : CLASS_TAG_INLINE);
//
//			String styleStr = "";
//			String borderStr = getExprValue(pageContext, borderExpr, sourceNode);
//
//			if ("yes".equalsIgnoreCase(VpePreference.SHOW_BORDER_FOR_UNKNOWN_TAGS.getValue())) {
//				styleStr += borderStr.length() > 0 ? "border-width:" + borderStr + ";" : "";
//			} else {
//				styleStr += "border-width:0px;";
//			}
//
//			String borderColorStr = getExprValue(pageContext, borderColorExpr, sourceNode);
//			styleStr += borderColorStr.length() > 0 ? "border-color:" + borderColorStr + ";" : "";
//			String backgroundColorStr = getExprValue(pageContext, backgroundColorExpr, sourceNode);
//			styleStr += backgroundColorStr.length() > 0 ? "background-color:" + backgroundColorStr : "";
//			if (styleStr.trim().length() > 0) div.setAttribute("style", styleStr);
//		} else {
//			div.setAttribute("class", CLASS_TAG_NONE);
//		}
//
//		span.setAttribute("class", CLASS_TAG_CAPTION);
//
//		String styleStr = "";
//		String valueColorStr = getExprValue(pageContext, valueColorExpr, sourceNode);
//		styleStr += valueColorStr.length() > 0 ? "color:" + valueColorStr + ";" : ""; 
//		String valueBackgroundColorStr = getExprValue(pageContext, valueBackgroundColorExpr, sourceNode);
//		styleStr += valueBackgroundColorStr.length() > 0 ? "background-color:" + valueBackgroundColorStr : "";
//		if (styleStr.trim().length() > 0) span.setAttribute("style", styleStr);
//	}

	public VpeAnyData getAnyData() {
		return new VpeAnyData(
					tagForDisplayStr,
					valueStr,
					styleStr
				);
	}

	private String getExprValue(VpePageContext pageContext, VpeExpression expr, Node sourceNode) {
		String value;
		if (expr != null) {
			try {
				value = expr.exec(pageContext, sourceNode).stringValue();
			} catch (VpeExpressionException e) {
				
					VpeExpressionException exception = new VpeExpressionException(sourceNode.toString()+" "+expr.toString(),e); //$NON-NLS-1$
					VpePlugin.reportProblem(exception);
					value=""; //$NON-NLS-1$
			}
		} else {
			value = ""; //$NON-NLS-1$
		}
		return value;
	}
	
//	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
//		Object elements = visualNodeMap.get(this);
//		if (elements != null && elements instanceof VisualElements) {
//			VisualElements o = (VisualElements)elements;
//			try {
//				setStyles(pageContext, sourceElement, o.div, o.span);
//			} catch (VpeExpressionException e) {
//				VpeExpressionException exception = new VpeExpressionException(sourceElement.toString()+" "+name+" "+value,e); //$NON-NLS-1$ //$NON-NLS-2$
//				VpePlugin.reportProblem(exception) ;
//			}
//		}
//	}

//	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
//		Object elements = visualNodeMap.get(this);
//		if (elements != null && elements instanceof VisualElements) {
//			VisualElements o = (VisualElements)elements;
//			try {
//				setStyles(pageContext, sourceElement, o.div, o.span);
//			} catch (VpeExpressionException e) {
//				VpeExpressionException exception = new VpeExpressionException(sourceElement.toString()+" "+name,e); //$NON-NLS-1$
//				VpePlugin.reportProblem(exception);
//			}
//		}
//	}

//	private class VisualElements {
//		private nsIDOMElement div;
//		private nsIDOMElement span;
//
//		private VisualElements(nsIDOMElement div, nsIDOMElement span) {
//			this.div = div;
//			this.span = span;
//		}
//	}
}
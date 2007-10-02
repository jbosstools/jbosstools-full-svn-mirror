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

import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.MozillaSupports;

public class VpeAnyCreator extends VpeAbstractCreator {
	static final String CLASS_TAG_BLOCK = "__any__tag__block";
	static final String CLASS_TAG_INLINE = "__any__tag__inline";
	static final String CLASS_TAG_NONE = "__any__tag__none";
	static final String CLASS_TAG_CAPTION = "__any__tag__caption";

	static final String VAL_DISPLAY_BLOCK = "block";
	static final String VAL_DISPLAY_INLINE = "inline";
	static final String VAL_DISPLAY_NONE = "none";

	private boolean caseSensitive;
	private VpeExpression displayExpr;
	private VpeExpression valueExpr;
	private VpeExpression borderExpr;
	private VpeExpression valueColorExpr;
	private VpeExpression valueBackgroundColorExpr;
	private VpeExpression backgroundColorExpr;
	private VpeExpression borderColorExpr;
	private VpeExpression showIconExpr;

	private List propertyCreators;
	private Set dependencySet;

	private String displayStr;
	private String valueStr;
	private String borderStr;
	private String valueColorStr;
	private String valueBackgroundColorStr;
	private String backgroundColorStr;
	private String borderColorStr;
	private boolean showIconBool;

	VpeAnyCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(element, dependencyMap);
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Attr displayAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_DISPLAY);
		if (displayAttr != null) {
			try {
				displayStr = displayAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(displayStr, caseSensitive);
				displayExpr = info.getExpression();
				dependencySet = info.getDependencySet();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr valueAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_VALUE);
		if (valueAttr != null) {
			try {
				valueStr = valueAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(valueStr, caseSensitive);
				valueExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr showIconAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_ICON);
		if (showIconAttr != null) {
			try {
				if("yes".equals(showIconAttr.getValue())) showIconBool = true;
				else showIconBool = false;
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(showIconAttr.getValue(), caseSensitive);
				showIconExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr borderAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_BORDER);
		if (borderAttr != null) {
			try {
				borderStr = borderAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(borderStr, caseSensitive);
				borderExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr valueColorAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_VALUE_COLOR);
		if (valueColorAttr != null) {
			try {
				valueColorStr = valueColorAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(valueColorStr, caseSensitive);
				valueColorExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr valueBackgroundColorAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_VALUE_BACKGROUND_COLOR);
		if (valueBackgroundColorAttr != null) {
			try {
				valueBackgroundColorStr = valueBackgroundColorAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(valueBackgroundColorStr, caseSensitive);
				valueBackgroundColorExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr backgroundColorAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_BACKGROUND_COLOR);
		if (backgroundColorAttr != null) {
			try {
				backgroundColorStr = backgroundColorAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(backgroundColorStr, caseSensitive);
				backgroundColorExpr = info.getExpression();
				dependencyMap.setCreator(this, info.getDependencySet());
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}

		Attr borderColorAttr = element.getAttributeNode(VpeTemplateManager.ATTR_ANY_BORDER_COLOR);
		if (borderColorAttr != null) {
			try {
				borderColorStr = borderColorAttr.getValue();
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(borderColorStr, caseSensitive);
				borderColorExpr = info.getExpression();
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
					propertyCreators.add(new VpeAttributeCreator(attrName, attr.getValue(), dependencyMap, caseSensitive));
				}
			}
		}
		Attr attr = element.getAttributeNode("title");
		if (attr == null) {
			if (propertyCreators == null) propertyCreators  = new ArrayList();
			propertyCreators.add(new VpeAttributeCreator("title", "{tagstring()}", dependencyMap, caseSensitive));
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		Element div = visualDocument.createElement("div");
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(div);

		Element span = visualDocument.createElement("span");
		div.appendChild(span);
		if(showIconBool){
			Element img = visualDocument.createElement("img");
			img.setAttribute("src","any.gif");
			img.setAttribute("width","16");
			img.setAttribute("height","16");
			span.appendChild(img);
		}
		

		visualNodeMap.put(this, new VisualElements(div, span));

		if (propertyCreators != null) {
			for (int i = 0; i < propertyCreators.size(); i++) {
				VpeCreator creator = (VpeCreator)propertyCreators.get(i);
				if (creator != null) {
					VpeCreatorInfo info = creator.create(pageContext, (Element) sourceNode, visualDocument, div, visualNodeMap);
					if (info != null && info.getVisualNode() != null) {
						Attr attr = (Attr)info.getVisualNode();
						div.setAttributeNode(attr);
						MozillaSupports.release(attr);
					}
				}
			}
		}

		setStyles(pageContext, sourceNode, div, span);

		String valueStr = getExprValue(pageContext, valueExpr, sourceNode);
		Node valueNode = visualDocument.createTextNode(valueStr);
		span.appendChild(valueNode);
		creatorInfo.addDependencySet(dependencySet);
		return creatorInfo;
	}

	private void setStyles(VpePageContext pageContext, Node sourceNode, Element div, Element span) {
		boolean display = true;
		boolean displayBlock = true;

		if (displayExpr != null) {
			VpeValue vpeValue = displayExpr.exec(pageContext, sourceNode);
			if (vpeValue != null) {
				String displayStr = vpeValue.stringValue();
				if (caseSensitive) {
					display = !VAL_DISPLAY_NONE.equals(displayStr);
					displayBlock = display && !VAL_DISPLAY_INLINE.equals(displayStr);
				} else {
					display = !VAL_DISPLAY_NONE.equalsIgnoreCase(displayStr);
					displayBlock = display && !VAL_DISPLAY_INLINE.equalsIgnoreCase(displayStr);
				}
			}
		}

		if (display) {
			div.setAttribute("class", displayBlock ? CLASS_TAG_BLOCK : CLASS_TAG_INLINE);

			String styleStr = "";
			String borderStr = getExprValue(pageContext, borderExpr, sourceNode);

			if ("yes".equalsIgnoreCase(VpePreference.SHOW_BORDER_FOR_UNKNOWN_TAGS.getValue())) {
				styleStr += borderStr.length() > 0 ? "border-width:" + borderStr + ";" : "";
			} else {
				styleStr += "border-width:0px;";
			}

			String borderColorStr = getExprValue(pageContext, borderColorExpr, sourceNode);
			styleStr += borderColorStr.length() > 0 ? "border-color:" + borderColorStr + ";" : "";
			String backgroundColorStr = getExprValue(pageContext, backgroundColorExpr, sourceNode);
			styleStr += backgroundColorStr.length() > 0 ? "background-color:" + backgroundColorStr : "";
			if (styleStr.trim().length() > 0) div.setAttribute("style", styleStr);
		} else {
			div.setAttribute("class", CLASS_TAG_NONE);
		}

		span.setAttribute("class", CLASS_TAG_CAPTION);

		String styleStr = "";
		String valueColorStr = getExprValue(pageContext, valueColorExpr, sourceNode);
		styleStr += valueColorStr.length() > 0 ? "color:" + valueColorStr + ";" : ""; 
		String valueBackgroundColorStr = getExprValue(pageContext, valueBackgroundColorExpr, sourceNode);
		styleStr += valueBackgroundColorStr.length() > 0 ? "background-color:" + valueBackgroundColorStr : "";
		if (styleStr.trim().length() > 0) span.setAttribute("style", styleStr);
	}

	public VpeAnyData getAnyData() {
		return new VpeAnyData(
					displayStr,
					valueStr,
					borderStr,
					valueColorStr,
					valueBackgroundColorStr,
					backgroundColorStr,
					borderColorStr,
					showIconBool
				);
	}

	private String getExprValue(VpePageContext pageContext, VpeExpression expr, Node sourceNode) {
		String value;
		if (expr != null) {
			value = expr.exec(pageContext, sourceNode).stringValue();
		} else {
			value = "";
		}
		return value;
	}
	
	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		Object elements = visualNodeMap.get(this);
		if (elements != null && elements instanceof VisualElements) {
			VisualElements o = (VisualElements)elements;
			setStyles(pageContext, sourceElement, o.div, o.span);
		}
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		Object elements = visualNodeMap.get(this);
		if (elements != null && elements instanceof VisualElements) {
			VisualElements o = (VisualElements)elements;
			setStyles(pageContext, sourceElement, o.div, o.span);
		}
	}

	private class VisualElements {
		private Element div;
		private Element span;

		private VisualElements(Element div, Element span) {
			this.div = div;
			this.span = span;
		}
	}
}
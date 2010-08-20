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

import org.eclipse.core.runtime.Status;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;
import org.jboss.tools.vpe.editor.template.textformating.FormatData;
import org.jboss.tools.vpe.editor.toolbar.format.css.StyleAttribute;

/**
 * @author Igels
 */
abstract public class AttributeFormatController extends FormatController {

	/**
	 * @param manager
	 */
	public AttributeFormatController(FormatControllerManager manager) {
		super(manager);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.FormatController#run(FormatData templateData)
	 */
	protected void run(FormatData templateData) {
		FormatAttributeData[] attributeData = templateData.getFormatAttributes();
		Node node = manager.getCurrentSelectedNode();
		for(int i=0; i<attributeData.length; i++) {
//			Attr attribute = getAttribute(node, attributeData[i]);
			runAttribute(attributeData[i], node);
		}
	}

	abstract protected void setStyle(Attr styleAttribute, FormatAttributeData templateData);

	protected void runAttribute(FormatAttributeData templateData, Node selectedNode) {
		Attr attribute = getAttribute(selectedNode, templateData);
		if(attribute==null) {
			manager.setIgnoreSelectionChanges(true);
			attribute = createAttribute(selectedNode, templateData);
		}
		if(attribute!=null) {
			if(FormatAttributeData.STYLE_TYPE.equals(templateData.getType())) {
				manager.setIgnoreSelectionChanges(false);
				setStyle(attribute, templateData);
				if (!(attribute.getValue().length() > 0)) {
				    Element element = (Element) selectedNode;
				    element.removeAttributeNode(attribute);
				}
				return;
			}
		}
		manager.setIgnoreSelectionChanges(false);
		// Continue processing if attribute type is not style.		
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemSelection()
	 */
	public void setToolbarItemSelection() {
		FormatData data = manager.getCurrentFormatData();
		Node node = manager.getCurrentSelectedNode();
		if(data!=null && node!=null) {
			FormatAttributeData[] attributeData = data.getFormatAttributes();
			for(int i=0; i<attributeData.length; i++) {
				setAttributeToolbarItemSelection(attributeData[i], node);
			}
		}
	}

	protected boolean formatIsAllowable(FormatData templateData) {
		FormatAttributeData[] attributeData = templateData.getFormatAttributes();
		for(int i=0; i<attributeData.length; i++) {
			if(FormatAttributeData.STYLE_TYPE.equals(attributeData[i].getType())) {
				return true;
			}
		}
		// Continue processing if attribute type is not style.
		return false;
	}

	protected void setAttributeToolbarItemSelection(FormatAttributeData templateData, Node selectedNode) {
		Attr attribute = getAttribute(selectedNode, templateData);
		if(FormatAttributeData.STYLE_TYPE.equals(templateData.getType())) {
			boolean isStyleSet = isStyleSet(attribute, templateData);
			setToolbarItemSelection(isStyleSet);
		}
		// Continue processing if attribute type is not style.		
	}

	protected abstract boolean isStyleSet(Attr attribute, FormatAttributeData templateData);

	protected abstract void setToolbarItemSelection(boolean selected);

	protected static Attr getAttribute(Node node, FormatAttributeData data) {
		if(node instanceof Element) {
			Element element = (Element)node;
			NamedNodeMap map = element.getAttributes();
			for(int i=0; i<map.getLength(); i++) {
				Node atNode = map.item(i);
				if(atNode instanceof Attr) {
					Attr attribute = (Attr)atNode;
					boolean equal = data.isCaseSensitive()?attribute.getName().equals(data.getName()):attribute.getName().equalsIgnoreCase(data.getName());
					if(equal) {
						return attribute;
					}
				}
			}
		}
		return null;
	}

	protected Attr getAttributeFromSelectedNode(String attributeName, boolean ignoreCase) {
		Node node = this.getManager().getCurrentSelectedNode();
		if(node!=null && node instanceof Element) {
			NamedNodeMap attributes = ((Element)node).getAttributes();
			for(int i=0; i<attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				boolean equals = ignoreCase?attribute.getNodeName().equalsIgnoreCase(attributeName):attribute.getNodeName().equals(attributeName);
				if(equals) {
					return (Attr)attribute;
				}
			}
		}
		return null;
	}

	protected Attr getStyleAttributeFromSelectedNode(boolean ignoreCase) {
		return getAttributeFromSelectedNode("style", ignoreCase); //$NON-NLS-1$
	}

	protected static Attr createAttribute(Node node, FormatAttributeData data) {
		if(node instanceof Element) {
			Element element = (Element)node;
			String name = data.getName();
			if(name==null) {
				String message = "Wrong vpe template for tag " + node.getNodeName() + ". Attribute 'name' or 'type' of <vpe:formatAttribute> must be set."; //$NON-NLS-1$ //$NON-NLS-2$
				VpePlugin.getDefault().getLog().log(new Status(Status.ERROR, VpePlugin.PLUGIN_ID, Status.OK, message, new Exception(message)));
			}
			element.setAttribute(name, ""); //$NON-NLS-1$
			return element.getAttributeNode(name);
		}
		return null;
	}

	protected static boolean isStyleSet(String stylePropertyName, String stylePropertyValue, Attr styleAttribute) {
		if(styleAttribute==null) {
			return false;
		}
		StyleAttribute style = new StyleAttribute(styleAttribute);
		return style.isStylePropertySet(stylePropertyName, stylePropertyValue);
	}

	/*
	 * If stylePropertyValue is set than just remove this value from style property.
	 * If stylePropertyValue is not set than add this value to style property.
	 * Example:
	 *   stylePropertyName="TEXT-DECORATION"
	 *   stylePropertyValue="overline"
	 *   Befor simple inverting -  "TEXT-DECORATION: overline underline"
	 *   After simple inverting -  "TEXT-DECORATION: underline"
	 *   After one more invering - "TEXT-DECORATION: underline overline"
	 * @param stylePropertyName
	 * @param stylePropertyValue
	 * @param styleAttribute
	 */
	protected static void simpleInvertStyleProperty(String stylePropertyName, String stylePropertyValue, Attr styleAttribute) {
		if(styleAttribute==null) {
			return;
		}
		StyleAttribute style = new StyleAttribute(styleAttribute);
		style.invertStyleProperty(stylePropertyName, stylePropertyValue);

		String value = style.toString().trim();
		if(value.length()==0) {
			styleAttribute.getOwnerElement().removeAttributeNode(styleAttribute);
		} else {
			styleAttribute.setValue(value);
		}
	}

	/*
	 * If stylePropertyValue is set than just remove this style property.
	 * If stylePropertyValue is not set than set this value to style property.
	 * Example:
	 *   stylePropertyName="FONT-STYLE"
	 *   stylePropertyValue="none"
	 *   Befor simple inverting -  "FONT-STYLE: italic"
	 *   After simple inverting -  "FONT-STYLE: none"
	 *   After one more invering - ""
	 *   After one more invering - "FONT-STYLE: none"
	 * @param styleAttribute
	 * @param templateData
	 * @param stylepPropertyName
	 * @param setValue
	 */
	protected static void invertSingleStyleProperty(Attr styleAttribute, String stylePropertyName, String stylePropertyValue) {
		if(styleAttribute==null) {
			return;
		}
		StyleAttribute style = new StyleAttribute(styleAttribute);
		style.ivertSingleStyleProperty(stylePropertyName, stylePropertyValue);

		String value = style.toString().trim();
		if(value.length()==0) {
			styleAttribute.getOwnerElement().removeAttributeNode(styleAttribute);
		} else {
			styleAttribute.setValue(value);
		}
	}

	protected static void setSingleStyleProperty(Attr styleAttribute, String stylePropertyName, String stylePropertyValue) {
		if(styleAttribute==null) {
			return;
		}
		StyleAttribute style = new StyleAttribute(styleAttribute);
		style.setSingleStyleProperty(stylePropertyName, stylePropertyValue);

		String value = style.toString().trim();
		styleAttribute.setValue(value);
	}
}
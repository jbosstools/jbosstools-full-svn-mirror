/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.model.freemarker;

import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Dart
 * 
 */
public class FreemarkerTemplateXMLModel extends TagObject implements IFreemarkerTemplateModel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.xml.TagObject#addChildTag
	 * (org.jboss.tools.smooks.configuration.editors.xml.TagObject)
	 */
	@Override
	public void addChildTag(TagObject tag) {
		if (FreemarkerModelAnalyzer.isChoiceElement(tag.getReferenceElement())) {
			this.getXMLNodeChildren().add(tag);
			if (tag != null)
				tag.setParent(this);
			return;
		}
		super.addChildTag(tag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.xml.TagObject#removeChildTag
	 * (org.jboss.tools.smooks.configuration.editors.xml.TagObject)
	 */
	@Override
	public void removeChildTag(TagObject tag) {
		// TODO Auto-generated method stub
		super.removeChildTag(tag);
	}

	public boolean isRequired() {
		Element element = this.getReferenceElement();
		if (!this.getXMLNodeChildren().isEmpty()) {
			return false;
		}
		if (element != null) {
			NamedNodeMap nodeMap = element.getAttributes();
			for (int i = 0; i < nodeMap.getLength(); i++) {
				Attr attr = (Attr) nodeMap.item(i);
				if (attr != null) {
					if (FreemarkerModelAnalyzer.SPECIAL_ELEMENT_UIR.equals(attr.getNamespaceURI())) {
						String name = attr.getLocalName();
						if (name == null) {
							name = attr.getNodeName();
						}
						if (FreemarkerModelAnalyzer.MINOCCURS.equals(name)) {
							String value = attr.getValue();
							try {
								int intValue = Integer.parseInt(value);
								if (intValue > 0) {
									return true;
								}
							} catch (Exception e) {
								return false;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isManyOccurs() {
		Element refElement = this.getReferenceElement();
		if (refElement != null) {
			NamedNodeMap nodeMap = refElement.getAttributes();
			for (int i = 0; i < nodeMap.getLength(); i++) {
				Attr attr = (Attr) nodeMap.item(i);
				if (attr != null) {
					if (FreemarkerModelAnalyzer.SPECIAL_ELEMENT_UIR.equals(attr.getNamespaceURI())) {
						String name = attr.getLocalName();
						if (name == null) {
							name = attr.getNodeName();
						}
						if (FreemarkerModelAnalyzer.MAXOCCURS.equals(name)) {
							String value = attr.getValue();
							try {
								int intValue = Integer.parseInt(value);
								if (intValue == -1 || intValue > 1) {
									return true;
								}
							} catch (Exception e) {
								return false;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isHidden(RootModel graphRoot) {
		Element refElement = this.getReferenceElement();
		if (refElement != null) {
			NamedNodeMap nodeMap = refElement.getAttributes();
			for (int i = 0; i < nodeMap.getLength(); i++) {
				Attr attr = (Attr) nodeMap.item(i);
				if (attr != null) {
					if (FreemarkerModelAnalyzer.SPECIAL_ELEMENT_UIR.equals(attr.getNamespaceURI())) {
						String name = attr.getLocalName();
						if (name == null) {
							name = attr.getNodeName();
						}
						if (FreemarkerModelAnalyzer.HIDDEN.equals(name)) {
							String value = attr.getValue();
							try {
								boolean booleanValue = Boolean.parseBoolean(value);
								return booleanValue;
							} catch (Exception e) {
								return false;
							}
						}
					}
				}
			}

			if (graphRoot != null && FreemarkerModelAnalyzer.isChoiceElement(refElement)) {
				Node parent = refElement.getParentNode();
				NodeList nodeList = parent.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node child = nodeList.item(i);
					if (child == refElement)
						continue;
					FreemarkerTemplateXMLModel model = localBrotherModel(child);
					if (model != null) {
						AbstractSmooksGraphicalModel cgm = SmooksGraphUtil.findSmooksGraphModel(graphRoot, model);
						if (!cgm.getTargetConnections().isEmpty()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private FreemarkerTemplateXMLModel localBrotherModel(Node refNode) {
		AbstractXMLObject parent = this.getParent();
		List<IXMLStructuredObject> children = parent.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject ixmlStructuredObject = (IXMLStructuredObject) iterator.next();
			if (ixmlStructuredObject instanceof TagObject) {
				if (((TagObject) ixmlStructuredObject).getReferenceElement() == refNode) {
					return (FreemarkerTemplateXMLModel) ixmlStructuredObject;
				}
			}
			if (ixmlStructuredObject instanceof TagPropertyObject) {
				if (((TagPropertyObject) ixmlStructuredObject).getReferenceAttibute() == refNode) {
					return (FreemarkerTemplateXMLModel) ixmlStructuredObject;
				}
			}
		}
		return null;
	}

}

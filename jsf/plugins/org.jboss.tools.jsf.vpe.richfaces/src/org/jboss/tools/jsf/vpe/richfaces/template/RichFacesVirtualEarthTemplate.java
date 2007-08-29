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
package org.jboss.tools.jsf.vpe.richfaces.template;

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * @author ezheleznyakov@exadel.com
 * 
 */
public class RichFacesVirtualEarthTemplate extends VpeAbstractTemplate {

	private static String EARTH_AERIAL = "/virtualearth/earth_aerial.png";
	private static String EARTH_HYBRID = "/virtualearth/earth_hybrid.png";
	private static String EARTH_ROAD = "/virtualearth/earth_road.png";

	private static String MAP_STYLE_ATTRIBUTE_NAME = "mapStyle";

	private static String MAP_STYLE_VALUES[] = { "road", "aerial", "hybrid" };

	private static String STYLE_CLASS_ATTR_NAME = "styleClass";

	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		nsIDOMElement img = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_IMG);

		String mapStyleValue = ((Element) sourceNode).getAttribute(
				MAP_STYLE_ATTRIBUTE_NAME);

		if (mapStyleValue != null && searchInMapStyleValues(mapStyleValue)) {
			if (mapStyleValue.equalsIgnoreCase(MAP_STYLE_VALUES[0]))
				ComponentUtil.setImg(img, EARTH_ROAD);
			else if (mapStyleValue.equalsIgnoreCase(MAP_STYLE_VALUES[1]))
				ComponentUtil.setImg(img, EARTH_AERIAL);
			else if (mapStyleValue.equalsIgnoreCase(MAP_STYLE_VALUES[2]))
				ComponentUtil.setImg(img, EARTH_HYBRID);
		} else
			ComponentUtil.setImg(img, EARTH_ROAD);

		copyStyleAttribute(img, sourceNode);

		if (((Element) sourceNode).getAttribute(STYLE_CLASS_ATTR_NAME) != null)
			img.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
					((Element) sourceNode).getAttribute(STYLE_CLASS_ATTR_NAME));

		return new VpeCreationData(img);
	}

	/**
	 * 
	 * @param value
	 *            Value of mapStyle attribute
	 * @return True or value of mapStyle attribute correct or false
	 */
	private boolean searchInMapStyleValues(String mapStyleValue) {

		for (int i = 0; i < MAP_STYLE_VALUES.length; i++)
			if (MAP_STYLE_VALUES[i].equalsIgnoreCase(mapStyleValue.trim()))
				return true;
		return false;
	}

	/**
	 * 
	 * @param img
	 * @param sourceNode
	 *            The current node of the source tree.
	 */
	private void copyStyleAttribute(nsIDOMElement img, Node sourceNode) {
		NamedNodeMap namedNodeMap = sourceNode.getAttributes();
		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Node attribute = namedNodeMap.item(i);
			if (attribute.getNodeName().equalsIgnoreCase(
					HtmlComponentUtil.HTML_STYLE_ATTR)) {
				img.setAttribute(attribute.getNodeName(), attribute
						.getNodeValue());
				return;
			}
		}
	}
}
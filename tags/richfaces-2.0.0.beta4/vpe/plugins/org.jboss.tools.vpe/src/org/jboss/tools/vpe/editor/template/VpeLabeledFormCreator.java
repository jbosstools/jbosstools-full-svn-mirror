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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionInfo;
import org.jboss.tools.vpe.editor.template.expression.VpeValue;
import org.jboss.tools.vpe.editor.util.MozillaSupports;

/**
 * @author Nick Belaevski
 *
 */

// TODO: labels in VPE doesn't respond to changes in source because parentElement (mapped to table)
// 		 doesn't know that some of its children have their content changed

public class VpeLabeledFormCreator extends VpeAbstractCreator {

	private boolean caseSensitive;
	private VpeExpression labelNameExpr;
	private List propertyCreators;
	private Set dependencySet;
	
	VpeLabeledFormCreator(Element gridElement, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(gridElement, dependencyMap);
	}

	private void build(Element gridElement, VpeDependencyMap dependencyMap) {
		Attr labelNameAttr = gridElement.getAttributeNode(VpeTemplateManager.ATTR_LABELED_FORM_LABEL);
		if (labelNameAttr != null) {
			try {
				VpeExpressionInfo info = VpeExpressionBuilder.buildCompletedExpression(labelNameAttr.getValue(), caseSensitive);
				labelNameExpr = info.getExpression();
				dependencySet = info.getDependencySet();
			} catch(VpeExpressionBuilderException e) {
				VpePlugin.reportProblem(e);
			}
		}
		
		// Prepare to transfer attributes to resulting visual node 
		if (VpeTemplateManager.ATTR_LABELED_FORM_PROPERTIES != null) {
			for (int i = 0; i < VpeTemplateManager.ATTR_LABELED_FORM_PROPERTIES.length; i++) {
				String attrName = VpeTemplateManager.ATTR_LABELED_FORM_PROPERTIES[i];
				Attr attr = gridElement.getAttributeNode(attrName);
				if (attr != null) {
					if (propertyCreators == null) propertyCreators  = new ArrayList();
					propertyCreators.add(new VpeAttributeCreator(attrName, attr.getValue(), dependencyMap, caseSensitive));
				}
			}
		}
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, Document visualDocument, Element visualElement, Map visualNodeMap) {
		String labelAttrName = VpeTemplateManager.ATTR_LABELED_FORM_DEFAULT_LABEL;
		if (labelNameExpr != null) {
			VpeValue vpeValue = labelNameExpr.exec(pageContext, sourceNode);
			if (vpeValue != null) {
				labelAttrName = vpeValue.stringValue();
			}
		}
		
		Element visualTable = visualDocument.createElement("table");
		VpeCreatorInfo creatorInfo = new VpeCreatorInfo(visualTable);

		// transfer attributes to resulting visual node 
		if (propertyCreators != null) {
			for (int i = 0; i < propertyCreators.size(); i++) {
				VpeCreator creator = (VpeCreator)propertyCreators.get(i);
				if (creator != null) {
					VpeCreatorInfo info = creator.create(pageContext, (Element) sourceNode, visualDocument, visualTable, visualNodeMap);
					if (info != null && info.getVisualNode() != null) {
						Attr attr = (Attr)info.getVisualNode();
						visualTable.setAttributeNode(attr);
						MozillaSupports.release(attr);
					}
				}
			}
		}
		
		NodeList children = sourceNode.getChildNodes();
		int count = children != null ? children.getLength() : 0;
		if (count > 0) {
			Node[] sourceChildren = new Node[count];
			int childrenCount = 0;
			
			// Do nodes filtering, throw away empty text and PI's, comments etc.
			for (int i = 0; i < count; i++) {
				Node node = children.item(i);
				int type = node.getNodeType();
				if (type == Node.ELEMENT_NODE || type == Node.TEXT_NODE && node.getNodeValue().trim().length() > 0) 
				{
					sourceChildren[childrenCount] = node;
					childrenCount++;
				}
			}
			
			Node attrNode = null;
			for (int i = 0; i < childrenCount; i++) {
				NamedNodeMap attrMap = sourceChildren[i].getAttributes();
				if (attrMap != null)
					attrNode = attrMap.getNamedItem(labelAttrName);

				Element row = visualDocument.createElement("tr");
				
				Element labelCell = visualDocument.createElement("td");
				if (attrNode != null)
				{
					String labelValue = attrNode.getNodeValue();
					Text text = visualDocument.createTextNode(labelValue);
					labelCell.appendChild(text);
				}
				row.appendChild(labelCell);

					
				Element valueCell = visualDocument.createElement("td");

				row.appendChild(valueCell);
				visualTable.appendChild(row);
				VpeChildrenInfo rowInfo = new VpeChildrenInfo(row);
				rowInfo.addSourceChild(sourceChildren[i]);
				creatorInfo.addChildrenInfo(rowInfo);
			}
		}

		creatorInfo.addDependencySet(dependencySet);
		return creatorInfo;
	}
}

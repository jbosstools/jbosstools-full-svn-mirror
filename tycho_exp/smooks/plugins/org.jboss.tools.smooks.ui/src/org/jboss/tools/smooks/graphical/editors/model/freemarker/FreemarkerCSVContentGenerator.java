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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.template.csv.CSVModelBuilder;
import org.jboss.template.csv.CSVToFreemarkerTemplateBuilder;
import org.jboss.template.exception.InvalidMappingException;
import org.jboss.template.exception.TemplateBuilderException;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanChildGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Dart
 * 
 */
public class FreemarkerCSVContentGenerator {

	public String generateCSVContents(FreemarkerCSVNodeGraphicalModel csvRecordGraphicalModel)
			throws TemplateBuilderException {

		CSVNodeModel csvRecordModel = (CSVNodeModel) csvRecordGraphicalModel.getData();

		String collectionName = csvRecordModel.getName();
		char sperator = csvRecordModel.getSperator();
		char quote = csvRecordModel.getQuto();
		List<AbstractSmooksGraphicalModel> childrenGraphModel = csvRecordGraphicalModel.getChildren();
		List<String> fieldsName = new ArrayList<String>();
		for (Iterator<?> iterator = childrenGraphModel.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator.next();
			Object cd = abstractSmooksGraphicalModel.getData();
			cd = AdapterFactoryEditingDomain.unwrap(cd);
			if (cd instanceof IXMLStructuredObject) {
				fieldsName.add(((IXMLStructuredObject) cd).getNodeName());
			}
		}

		CSVModelBuilder modelBuilder = new CSVModelBuilder(fieldsName.toArray(new String[] {}));
		Document model = modelBuilder.buildModel();
		CSVToFreemarkerTemplateBuilder builder;

		builder = new CSVToFreemarkerTemplateBuilder(model, sperator, quote);

		List<TreeNodeConnection> connections = csvRecordGraphicalModel.getTargetConnections();
		if (!connections.isEmpty() && connections.size() == 1) {
			TreeNodeConnection recordConnection = connections.get(0);
			AbstractSmooksGraphicalModel sourceGraphModel = recordConnection.getSourceNode();
			if (sourceGraphModel instanceof JavaBeanGraphModel) {
				Object data = ((JavaBeanGraphModel) sourceGraphModel).getData();
				data = AdapterFactoryEditingDomain.unwrap(data);
				String beanName = null;
				if (data instanceof BindingsType) {
					beanName = ((BindingsType) data).getBeanId();
				}
				if (data instanceof BeanType) {
					beanName = ((BeanType) data).getBeanId();
				}
				if (beanName != null)
					builder.addCollectionMapping(beanName, getRecordElement(model), collectionName);

				List<AbstractSmooksGraphicalModel> graphChildren = csvRecordGraphicalModel.getChildren();
				for (Iterator<?> iterator = graphChildren.iterator(); iterator.hasNext();) {
					AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
							.next();
					addValueMapping(builder, (FreemarkerCSVNodeGraphicalModel) abstractSmooksGraphicalModel, model,
							collectionName, (JavaBeanGraphModel) sourceGraphModel);
				}
			}
		}

		return builder.buildTemplate();
	}

	protected void addValueMapping(CSVToFreemarkerTemplateBuilder builder, FreemarkerCSVNodeGraphicalModel filedNode,
			Document model, String recordName, JavaBeanGraphModel recordRootNode) throws InvalidMappingException {
		CSVNodeModel csvFieldNode = (CSVNodeModel) filedNode.getData();
		String elementName = csvFieldNode.getName();
		List<TreeNodeConnection> connections = filedNode.getTargetConnections();
		if (!connections.isEmpty() && connections.size() == 1) {
			TreeNodeConnection recordConnection = connections.get(0);
			AbstractSmooksGraphicalModel sourceGraphModel = recordConnection.getSourceNode();
			String mappingString = generateMappingString(sourceGraphModel, recordRootNode);
			if (mappingString != null) {
				mappingString = recordName + "." + mappingString;
			}
			builder.addValueMapping(mappingString, getFieldElement(model, elementName));
		}
	}

	protected String generateMappingString(AbstractSmooksGraphicalModel sourceGraphModel,
			JavaBeanGraphModel recordRootNode) {
		AbstractSmooksGraphicalModel parentModel = sourceGraphModel;
		Object sourceModel = sourceGraphModel.getData();
		sourceModel = AdapterFactoryEditingDomain.unwrap(sourceModel);
		String s = null;
		if (sourceModel instanceof ValueType) {
			s = ((ValueType) sourceModel).getProperty();
		}
		if (sourceModel instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
			s = ((org.jboss.tools.smooks.model.javabean12.ValueType) sourceModel).getProperty();
		}

		List<Object> nodesList = new ArrayList<Object>();
		fillParentList(nodesList, parentModel, recordRootNode);
		String mappingString = "";
		for (int i = nodesList.size() - 1; i >= 0; i--) {
			Object node = nodesList.get(i);
			String beanName = null;
			if (node instanceof BindingsType) {
				beanName = ((BindingsType) node).getBeanId();
			}
			if (node instanceof BeanType) {
				beanName = ((BeanType) node).getBeanId();
			}
			if (beanName != null) {
				mappingString += (beanName + ".");
			}
		}
		if (s != null) {
			mappingString += s;
		}
		return mappingString;
	}

	private void fillParentList(List<Object> list, AbstractSmooksGraphicalModel node, JavaBeanGraphModel recordRootNode) {
		if (node instanceof JavaBeanChildGraphModel) {
			node = node.getParent();
		}
		if (node != recordRootNode) {
			if (node instanceof JavaBeanGraphModel) {
				Object parent = node.getData();
				parent = AdapterFactoryEditingDomain.unwrap(parent);
				list.add(parent);
				List<TreeNodeConnection> connections = ((JavaBeanGraphModel) node).getTargetConnections();
				if (!connections.isEmpty() && connections.size() == 1) {
					TreeNodeConnection connection = connections.get(0);
					AbstractSmooksGraphicalModel sourcenode = connection.getSourceNode();
					fillParentList(list, sourcenode, recordRootNode);
				}
			}
		}
	}

	private Element getRecordElement(Document model) {
		return model.getDocumentElement();
	}

	private Element getFieldElement(Document model, String fieldName) {
		return (Element) model.getElementsByTagName(fieldName).item(0);
	}

}

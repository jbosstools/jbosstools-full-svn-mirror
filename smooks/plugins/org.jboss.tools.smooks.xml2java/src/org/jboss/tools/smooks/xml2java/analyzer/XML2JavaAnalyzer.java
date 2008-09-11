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
package org.jboss.tools.smooks.xml2java.analyzer;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.smooks.analyzer.AbstractAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;
import org.milyn.xsd.smooks.ParamType;
import org.milyn.xsd.smooks.ResourceConfigType;
import org.milyn.xsd.smooks.ResourceType;
import org.milyn.xsd.smooks.SmooksFactory;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.util.SmooksConstants;
import org.milyn.xsd.smooks.util.SmooksModelUtils;

/**
 * @author Dart Peng
 * @Date Aug 20, 2008
 */
public class XML2JavaAnalyzer extends AbstractAnalyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.analyzer.IAnalyzer#analyzeMappingGraphModel(org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public void analyzeMappingGraphModel(
			SmooksConfigurationFileGenerateContext context)
			throws SmooksAnalyzerException {
		SmooksResourceListType listType = context.getSmooksResourceListModel();
		GraphRootModel rootModel = context.getDataMappingRootModel();
		List children = rootModel.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			TreeItemRelationModel dataModel = (TreeItemRelationModel) iterator
					.next();
			if (dataModel.getClass() == SourceModel.class) {
				List sourceConnections = dataModel.getModelSourceConnections();
				if (sourceConnections.isEmpty())
					continue;
				processSourceConnections(sourceConnections, context, listType,
						(SourceModel) dataModel);
			}
		}
	}

	protected void processSourceConnections(List sourceConnections,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType, SourceModel sourceModel) {
		for (Iterator iterator = sourceConnections.iterator(); iterator
				.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			processLineConnection(connection, context, listType, sourceModel,
					null);
		}
	}

	protected void processLineConnection(LineConnectionModel connection,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType, SourceModel sourceModel,
			String beanID) {
		if (this.connectionIsUsed(connection))
			return;
		setConnectionUsed(connection);
		IConnectableModel sourceg = connection.getSource();
		if (sourceg != sourceModel)
			return;
		IConnectableModel targetg = connection.getTarget();
		AbstractXMLObject source = (AbstractXMLObject) sourceModel
				.getReferenceEntityModel();
		JavaBeanModel target = (JavaBeanModel) ((AbstractStructuredDataModel) targetg)
				.getReferenceEntityModel();
		ResourceConfigType resourceConfigType = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		addResourceConfigType(listType, resourceConfigType);
		// set the selector string value
		resourceConfigType.setSelector(source.getName());
		// create a resource and add it to resourceConfig
		ResourceType resourceType = SmooksFactory.eINSTANCE
				.createResourceType();
		resourceType.setValue(SmooksConstants.BEAN_POPULATOR);
		resourceConfigType.setResource(resourceType);

		// create param for resourceConfig
		if (beanID == null) {
			beanID = getBeanID(target);
		} else {
			if (beanID.startsWith("${") && beanID.endsWith("}")) {
				beanID = beanID.substring(2, beanID.length() - 1);
			}
		}

		addParamTypeToResourceConfig(resourceConfigType,
				SmooksConstants.BEAN_ID, beanID);

		// add beanClass param
		addParamTypeToResourceConfig(resourceConfigType,
				SmooksConstants.BEAN_CLASS, target.getBeanClassString());

		// add bindings param
		ParamType bindingsParam = addParamTypeToResourceConfig(
				resourceConfigType, SmooksConstants.BINDINGS, null);
		processBindingsParam(bindingsParam, target,source, context, listType);
		// 
	}

	protected void processBindingsParam(ParamType bindingsParam,
			JavaBeanModel javaBean,AbstractXMLObject source,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType) {
		List properties = javaBean.getProperties();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			boolean isComplex = true;
			JavaBeanModel child = (JavaBeanModel) iterator.next();
			AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(
					context.getDataMappingRootModel(), child);
			LineConnectionModel connection = UIUtils
					.getFirstTargetModelViaConnection(graphModel);
			if (connection == null)
				continue;
			AbstractStructuredDataModel sourceModel = (AbstractStructuredDataModel) connection
					.getSource();

			if (child.isPrimitive() || child.getProperties().isEmpty()) {
				isComplex = false;
			}

			String selector = getSelectorID(child);
			if (!isComplex) {
				selector = getSelectorIDViaXMLObject((AbstractXMLObject) sourceModel
						.getReferenceEntityModel(),source);
			}
			SmooksModelUtils.addBindingTypeToParamType(bindingsParam, child
					.getName(), selector, null, null);
			if (isComplex) {
				processLineConnection(connection, context, listType,
						(SourceModel) sourceModel, selector);
			} else {
				setConnectionUsed(connection);
				continue;
			}
		}
	}

	protected String getSelectorIDViaXMLObject(AbstractXMLObject sourceModel,AbstractXMLObject currentRoot) {
		String name = sourceModel.getName();
		if (sourceModel instanceof TagPropertyObject) {
			name = "@" + name;
		}
		AbstractXMLObject parent = sourceModel.getParent();
		while ( parent != null && parent.getName() != null) {
			name = parent.getName() + " " + name;
			if(parent == currentRoot) break;
			parent = parent.getParent();
		}
		return name;
	}

	private String getSelectorID(JavaBeanModel javaBean) {
		String selectorName = javaBean.getName();
		return "${" + selectorName + "}";
	}

	protected String getBeanID(JavaBeanModel target) {
		return target.getName();
	}

	public List<LineConnectionModel> analyzeMappingSmooksModel(
			SmooksResourceListType listType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType, Object sourceObject,
			Object targetObject) {
		// TODO Auto-generated method stub
		return null;
	}

}

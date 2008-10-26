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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.TagAction;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.analyzer.AbstractAnalyzer;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.DocumentObject;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

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
		GraphRootModel rootModel = context.getGraphicalRootModel();
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
		context.getGeneratorResourceList().add(resourceConfigType);
		// addResourceConfigType(listType, resourceConfigType);
		// set the selector string value
		resourceConfigType.setSelector(source.getName());
		// create a resource and add it to resourceConfig
		ResourceType resourceType = SmooksFactory.eINSTANCE
				.createResourceType();
		resourceType.setValue(SmooksModelConstants.BEAN_POPULATOR);
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
				SmooksModelConstants.BEAN_ID, beanID);

		// add beanClass param
		addParamTypeToResourceConfig(resourceConfigType,
				SmooksModelConstants.BEAN_CLASS, target.getBeanClassString());

		// add bindings param
		ParamType bindingsParam = addParamTypeToResourceConfig(
				resourceConfigType, SmooksModelConstants.BINDINGS, null);
		processBindingsParam(bindingsParam, target, source, context, listType);
		// 
	}

	protected void processBindingsParam(ParamType bindingsParam,
			JavaBeanModel javaBean, AbstractXMLObject source,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType) {
		List properties = javaBean.getProperties();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			boolean isComplex = true;
			JavaBeanModel child = (JavaBeanModel) iterator.next();
			AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(
					context.getGraphicalRootModel(), child);
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
				selector = getSelectorIDViaXMLObject(
						(AbstractXMLObject) sourceModel
								.getReferenceEntityModel(), source);
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

	protected String getSelectorIDViaXMLObject(AbstractXMLObject sourceModel,
			AbstractXMLObject currentRoot) {
		String name = sourceModel.getName();
		if (sourceModel instanceof TagPropertyObject) {
			name = "@" + name;
		}
		AbstractXMLObject parent = sourceModel.getParent();
		while (parent != null && parent.getName() != null) {
			name = parent.getName() + " " + name;
			if (parent == currentRoot)
				break;
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

	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType, Object sourceObject,
			Object targetObject) {
		if(sourceObject instanceof DocumentObject){
			sourceObject = ((DocumentObject)sourceObject).getRootTag();
		}
		if(targetObject instanceof List){
			targetObject = (JavaBeanModel) ((List)targetObject).get(0);
		}
		if (!(sourceObject instanceof AbstractXMLObject)
				|| !(targetObject instanceof JavaBeanModel)) {
			return MappingResourceConfigList.createEmptyList();
		}
		AbstractXMLObject sourceRoot = (AbstractXMLObject) sourceObject;
		JavaBeanModel sourceTarget = (JavaBeanModel) targetObject;

		ResourceConfigType rootResourceConfig = findFirstMappingResourceConfig(listType);
		// if can't find the root , return null
		if(rootResourceConfig ==null) return null;
		String xmlName = rootResourceConfig.getSelector();
		AbstractXMLObject source = findXMLObjectByName(xmlName, sourceRoot);
		if (source == null)
			return MappingResourceConfigList.createEmptyList();

		MappingResourceConfigList rcl = new MappingResourceConfigList();
		this.createMappingResourceConfigList(rcl, listType, rootResourceConfig,
				source, sourceTarget);
		return rcl;
	}

	public static AbstractXMLObject findXMLObjectByName(String selector,
			AbstractXMLObject root) {
		if (selector == null)
			return null;
		if (selector.equals(root.getName())) {
			return root;
		}
		if (root instanceof TagObject) {
			List properties = ((TagObject) root).getProperties();
			for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
				TagPropertyObject pro = (TagPropertyObject) iterator.next();
				if (selector.equals(pro.getName()))
					return pro;
			}
			List<AbstractXMLObject> tags = ((TagObject) root).getChildren();
			for (Iterator iterator = tags.iterator(); iterator.hasNext();) {
				AbstractXMLObject tagChild = (AbstractXMLObject) iterator
						.next();
				AbstractXMLObject result = findXMLObjectByName(selector,
						tagChild);
				if (result != null)
					return result;
			}
		}
		return null;

	}

	protected void createMappingResourceConfigList(
			MappingResourceConfigList configList, SmooksResourceListType list,
			ResourceConfigType config, AbstractXMLObject sourceRoot,
			JavaBeanModel targetJavaBean) {
		String selector = config.getSelector();
		String beanID = SmooksModelUtils.getParmaText(
				SmooksModelConstants.BEAN_ID, config);
		String beanClass = SmooksModelUtils.getParmaText(
				SmooksModelConstants.BEAN_CLASS, config);

		MappingModel mapping = new MappingModel(sourceRoot, targetJavaBean);
		configList.getMappingModelList().add(mapping);
		configList.addResourceConfig(config);
		this.setSelectorIsUsed(config.getSelector());

		List<ParamType> paramList = config.getParam();
		ParamType bindingParam = null;
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			String name = paramType.getName();
			if (SmooksModelConstants.BINDINGS.equals(name)) {
				bindingParam = paramType;
				break;
			}
		}

		if (bindingParam != null) {
			List bindings = (List) bindingParam.getMixed().get(
					SmooksModelUtils.ELEMENT_BINDING, true);
			if (bindings != null) {
				for (Iterator iterator = bindings.iterator(); iterator
						.hasNext();) {
					AnyType binding = (AnyType) iterator.next();
					String property = SmooksModelUtils
							.getAttributeValueFromAnyType(binding,
									SmooksModelUtils.ATTRIBUTE_PROPERTY);
					String selectorStr = SmooksModelUtils
							.getAttributeValueFromAnyType(binding,
									SmooksModelUtils.ATTRIBUTE_SELECTOR);
					JavaBeanModel childBean = JavaBeanAnalyzer
							.findTheChildJavaBeanModel(property, targetJavaBean);
					processXMLSelector(configList, config, sourceRoot,
							childBean, list, selectorStr);
				}
			}
		}
	}

	protected void processXMLSelector(MappingResourceConfigList configList,
			ResourceConfigType resourceConfig, AbstractXMLObject root,
			JavaBeanModel targetBean, SmooksResourceListType listType,
			String selector) {
		if (isReferenceSelector(selector)) {
			ResourceConfigType resourceConfig1 = this
					.findResourceConfigTypeWithSelector(selector, listType);
			String newSelector = resourceConfig1.getSelector();
			if (newSelector == null)
				return;
			AbstractXMLObject newRoot = findXMLObjectByName(newSelector, root);
			createMappingResourceConfigList(configList, listType,
					resourceConfig1, newRoot, targetBean);
		} else {
			AbstractXMLObject source = findXMLObjectWithSelectorString(
					selector, root);
			if (source != null) {
				MappingModel mapping = new MappingModel(source, targetBean);
				configList.getMappingModelList().add(mapping);
				configList.addResourceConfig(resourceConfig);
				this.setSelectorIsUsed(selector);
			}
		}
	}

	public static AbstractXMLObject findXMLObjectWithSelectorString(
			String selector, AbstractXMLObject parent) {
		if (selector == null)
			return null;
		selector = selector.trim();
		String[] names = selector.split(" ");
		if (names == null)
			return null;
		AbstractXMLObject current = parent;
		for (int i = 0; i < names.length; i++) {
			String name = names[i].trim();
			if (current instanceof TagObject && isXMLAttributeObject(name)) {
				List properties = ((TagObject) current).getProperties();
				name = name.substring(1);
				for (Iterator iterator = properties.iterator(); iterator
						.hasNext();) {
					TagPropertyObject property = (TagPropertyObject) iterator
							.next();
					if (name.equals(property.getName())) {
						current = property;
					}
				}
			} else {
				List list = parent.getChildren();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					AbstractXMLObject child = (AbstractXMLObject) iterator
							.next();
					if (name.equals(child.getName())) {
						current = child;
						break;
					}
				}
			}
		}
		if (current == parent)
			return null;
		return current;
	}

	public static boolean isXMLAttributeObject(String name) {
		if (name != null && name.startsWith("@"))
			return true;
		return false;
	}

	private ResourceConfigType findFirstMappingResourceConfig(
			SmooksResourceListType listType) {
		List list = listType.getAbstractResourceConfig();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ResourceConfigType resource = (ResourceConfigType) iterator.next();
			ResourceType rt = resource.getResource();
			if (rt == null)
				continue;
			String value = rt.getValue();
			if (SmooksModelConstants.BEAN_POPULATOR.equals(value)) {
				return resource;
			}
		}
		return null;
	}

	public DesignTimeAnalyzeResult[] analyzeGraphModel(
			SmooksConfigurationFileGenerateContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}

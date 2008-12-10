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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
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
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
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

	private static final String SPACE_SPLITER = " ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.analyzer.IAnalyzer#analyzeMappingGraphModel(org
	 * .jboss
	 * .tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
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
				AbstractXMLObject newParent = (AbstractXMLObject) dataModel
						.getReferenceEntityModel();
				String newParentSelector = generateParentSelector(newParent);
				processSourceConnections(sourceConnections, context, listType,
						(SourceModel) dataModel, newParentSelector);
			}
		}
	}
	
	protected String generateParentSelector(AbstractXMLObject xmlObject){
		String selector = xmlObject.getName();
		while(xmlObject.getParent() != null && !(xmlObject.getParent() instanceof DocumentObject)){
			xmlObject = xmlObject.getParent();
			selector = xmlObject.getName() + SPACE_SPLITER + selector;
		}
		return selector;
	}

	protected void processSourceConnections(List sourceConnections,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType, SourceModel sourceModel,
			String parentSelector) {
		for (Iterator iterator = sourceConnections.iterator(); iterator
				.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			processLineConnection(connection, context, listType, sourceModel,
					null, parentSelector);
		}
	}

	protected void processLineConnection(LineConnectionModel connection,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType, SourceModel sourceModel,
			String beanID, String parentSelector) {
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
		resourceConfigType.setSelector(parentSelector);

		// add the properties of connection
		List<PropertyModel> propertyList = connection.getProperties();
		for (Iterator<PropertyModel> iterator = propertyList.iterator(); iterator
				.hasNext();) {
			PropertyModel propertyModel = (PropertyModel) iterator.next();
			if (propertyModel.getName().equals("selector-namespace")) {
				resourceConfigType.setSelectorNamespace(propertyModel
						.getValue());
				break;
			}
		}
		// create a resource and add it to resourceConfig
		ResourceType resourceType = SmooksFactory.eINSTANCE
				.createResourceType();
		resourceType.setStringValue(SmooksModelConstants.BEAN_POPULATOR);
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
		processBindingsParam(bindingsParam, target, source, context, listType,
				parentSelector);
		// 
	}

	protected void processBindingsParam(ParamType bindingsParam,
			JavaBeanModel javaBean, AbstractXMLObject source,
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType listType, String parentSelector) {
		List properties = javaBean.getProperties();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			boolean isComplex = true;
			JavaBeanModel targetJavaChild = (JavaBeanModel) iterator.next();
			AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(
					context.getGraphicalRootModel(), targetJavaChild);
			LineConnectionModel connection = UIUtils
					.getFirstTargetModelViaConnection(graphModel);
			if (connection == null)
				continue;
			AbstractStructuredDataModel sourceModel = (AbstractStructuredDataModel) connection
					.getSource();

			if (targetJavaChild.isPrimitive() || targetJavaChild.getProperties().isEmpty()) {
				isComplex = false;
			}
			String resourceConfigSelector = parentSelector;
			String selector = getSelectorID(targetJavaChild);
			if (!isComplex) {
				selector = getSelectorIDViaXMLObject(
						(AbstractXMLObject) sourceModel
								.getReferenceEntityModel(), source,
						resourceConfigSelector);
			}
			String propertyName = targetJavaChild.getName();
			// if the parent bean is Array or List , the binding property name should be set NULL
			if(javaBean.isArray() || javaBean.isList()){
				propertyName = null;
			}
			AnyType binding = SmooksModelUtils.addBindingTypeToParamType(
					bindingsParam, propertyName , selector, null, null);
			// add connection's properties on the "binding" element
			UIUtils.assignConnectionPropertyToBinding(connection, binding,
					new String[] { "property", "selector" });
			if (isComplex) {
				AbstractXMLObject newParent = (AbstractXMLObject) sourceModel
						.getReferenceEntityModel();
				String newParentSelector = newParent.getName();
				processLineConnection(connection, context, listType,
						(SourceModel) sourceModel, selector, newParentSelector);
			} else {
				setConnectionUsed(connection);
				continue;
			}
		}
	}

	protected String getSelectorIDViaXMLObject(AbstractXMLObject sourceModel,
			AbstractXMLObject currentRoot, String resourceConfigSelector) {
		String name = sourceModel.getName();
		if (sourceModel instanceof TagPropertyObject) {
			name = "@" + name;
		}
		AbstractXMLObject parent = sourceModel.getParent();
		while (parent != null && parent.getName() != null) {
			if (parent == currentRoot)
				break;
			name = parent.getName() + SPACE_SPLITER + name;
			parent = parent.getParent();
		}
		if (resourceConfigSelector != null) {
			name = resourceConfigSelector + SPACE_SPLITER + name;
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
		JavaBeanModel tempBean = null;
		if (sourceObject instanceof DocumentObject) {
			sourceObject = ((DocumentObject) sourceObject).getRootTag();
		}
		if (targetObject instanceof List && !((List) targetObject).isEmpty()) {
			tempBean = (JavaBeanModel) ((List) targetObject).get(0);
		}
		if (!(sourceObject instanceof AbstractXMLObject)
				|| !(tempBean instanceof JavaBeanModel)) {
			// TODO if the type of input source/target data is illegal , throw
			// exceptions.
			// MODIFY by Dart 2008.11.07
			// throw new RuntimeException(
			// "[XMLBeanAnalyzer]Can't load the source/target data from Smooks configuration file."
			// );
			return MappingResourceConfigList.createEmptyList();
		}
		AbstractXMLObject sourceRoot = (AbstractXMLObject) sourceObject;

		ResourceConfigType rootResourceConfig = findFirstMappingResourceConfig(listType);
		// if can't find the root , return null
		if (rootResourceConfig == null) {
			// TODO if can't find the org.milyn.javabean.BeanPopulator , throw
			// exception
			// MODIFY by Dart 2008.11.07
			// throw new RuntimeException("Can't parse the config file.");
			return null;
		}
		MappingResourceConfigList rcl = new MappingResourceConfigList();
		List rlist = listType.getAbstractResourceConfig();
		int startIndex = rlist.indexOf(rootResourceConfig);
		for (int i = startIndex; i < rlist.size(); i++) {
			ResourceConfigType resourceConfig = (ResourceConfigType) rlist
					.get(i);
			ResourceType resource = resourceConfig.getResource();
			if (resource == null)
				continue;
			String populator = resource.getStringValue();
			if (populator != null)
				populator = populator.trim();
			if (!JavaBeanAnalyzer.BEANPOPULATOR.equals(populator))
				continue;
			String selector = resourceConfig.getSelector();
			if (isSelectorIsUsed(selector))
				continue;
			AbstractXMLObject source = findChildXMLObjectByName(selector,
					sourceRoot);
			if (source == null) {
				// TODO if can't find the root , throw exception
				// MODIFY by Dart 2008.11.17
				throw new RuntimeException(
						Messages
								.getString("XML2JavaAnalyzer.CantFindRootNodeErrorMessage") + " : " + selector); //$NON-NLS-1$
				// return MappingResourceConfigList.createEmptyList();
			}
			JavaBeanModel target = findJavaBeanModel(((List) targetObject),
					resourceConfig);
			if (target == null) {
				// TODO if can't find the root , throw exception
				// MODIFY by Dart 2008.11.17
				String clazzName = SmooksModelUtils.getParmaText("beanClass",
						resourceConfig);
				if (clazzName != null)
					throw new RuntimeException("Can't find the Java class"
							+ " : " + clazzName);
				else
					throw new RuntimeException("Java class name is NULL ");

			}
			this.createMappingResourceConfigList(rcl, listType, resourceConfig,
					source, target);
		}
		return rcl;
	}

	protected JavaBeanModel findJavaBeanModel(List javaBeanModelList,
			ResourceConfigType resourceConfig) {
		for (Iterator iterator = javaBeanModelList.iterator(); iterator
				.hasNext();) {
			JavaBeanModel bean = (JavaBeanModel) iterator.next();
			Class clazz = bean.getBeanClass();
			if (clazz != null) {
				String clazzName = SmooksModelUtils.getParmaText("beanClass",
						resourceConfig);
				if (clazzName != null)
					clazzName = clazzName.trim();
				if (clazz.getName().equals(clazzName)) {
					return bean;
				}
			}
		}
		return null;
	}

	public static AbstractXMLObject findChildXMLObjectByName(String selector,
			AbstractXMLObject root) {
		if (selector == null)
			return null;
		selector = selector.trim();
		String[] nameArray = null;
		if (selector.indexOf(SPACE_SPLITER) != -1) {
			nameArray = selector.split(SPACE_SPLITER);
		}
		if (nameArray != null) {
			if (nameArray.length > 1) {
				String firstNodeName = nameArray[0];
				root = findChildXMLObjectByName(firstNodeName, root);
				if (root == null)
					return null;
				for (int i = 1; i < nameArray.length; i++) {
					String name = nameArray[i];
					if (root instanceof TagObject) {
						List<AbstractXMLObject> childrenTags = root
								.getChildren();
						for (Iterator iterator = childrenTags.iterator(); iterator
								.hasNext();) {
							AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
									.next();
							if (abstractXMLObject.getName().equalsIgnoreCase(
									name)) {
								root = abstractXMLObject;
								break;
							}
						}
					}
				}
				return root;
			}
		}
		if (selector.equalsIgnoreCase(root.getName())) {
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
				AbstractXMLObject result = findChildXMLObjectByName(selector,
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

		MappingModel mapping = new MappingModel(sourceRoot, targetJavaBean);
		configList.getMappingModelList().add(mapping);
		String namespace = config.getSelectorNamespace();
		if (namespace != null) {
			PropertyModel np = new PropertyModel();
			np.setName("selector-namespace");
			np.setValue(namespace);
			mapping.getProperties().add(np);
		}

		configList.addResourceConfig(config);

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
							childBean, list, selectorStr, binding);
				}
			}
		}
	}

	protected void processXMLSelector(MappingResourceConfigList configList,
			ResourceConfigType resourceConfig, AbstractXMLObject root,
			JavaBeanModel targetBean, SmooksResourceListType listType,
			String selector, AnyType currentBinding) {
		if (isReferenceSelector(selector)) {
			ResourceConfigType resourceConfig1 = this
					.findResourceConfigTypeWithSelector(selector, listType);
			String newSelector = resourceConfig1.getSelector();
			if (newSelector == null)
				return;
			AbstractXMLObject newRoot = findChildXMLObjectByName(newSelector,
					root);
			if (newRoot == null) {
				// TODO If can't find the element , throw exception
				// MODIFY by Dart , 2008.11.07
				throw new RuntimeException(
						Messages
								.getString("XML2JavaAnalyzer.CantFindNodeErrorMessage1") + newSelector + Messages.getString("XML2JavaAnalyzer.CantFindNodeErrorMessage2")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			this.setSelectorIsUsed(resourceConfig1.getSelector());
			createMappingResourceConfigList(configList, listType,
					resourceConfig1, newRoot, targetBean);
		} else {

			AbstractXMLObject source = findXMLObjectWithSelectorString(
					selector, root);
			if (source == null) {
				// TODO If can't find the element , throw exception
				// MODIFY by Dart , 2008.11.07
				throw new RuntimeException(
						Messages
								.getString("XML2JavaAnalyzer.CantFindNodeErrorMessage1") + selector + Messages.getString("XML2JavaAnalyzer.CantFindNodeErrorMessage2")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (source != null) {
				MappingModel mapping = new MappingModel(source, targetBean);
				UIUtils.assignBindingPropertyToMappingModel(currentBinding,
						mapping, new Object[] {
								SmooksModelUtils.ATTRIBUTE_PROPERTY,
								SmooksModelUtils.ATTRIBUTE_SELECTOR });
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
		String[] names = selector.split(SPACE_SPLITER);
		if (names == null)
			return null;
		int startIndex = 0;
		String s = null;
		if (names.length > 1)
			s = names[0];
		else
			s = selector;
		AbstractXMLObject current = findChildXMLObjectByName(s, parent);
		if (current == parent) {

		} else {
			while (!(parent.getParent() instanceof DocumentObject)) {
				parent = parent.getParent();
			}
			current = parent;
		}
		for (int i = startIndex; i < names.length; i++) {
			String name = names[i].trim();
			if (current instanceof TagObject && isXMLAttributeObject(name)) {
				List properties = ((TagObject) current).getProperties();
				name = name.substring(1);
				for (Iterator iterator = properties.iterator(); iterator
						.hasNext();) {
					TagPropertyObject property = (TagPropertyObject) iterator
							.next();
					if (name.equalsIgnoreCase(property.getName())) {
						current = property;
						break;
					}
				}
			} else {
				List list = current.getChildren();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					AbstractXMLObject child = (AbstractXMLObject) iterator
							.next();
					if (name.equalsIgnoreCase(child.getName())) {
						current = child;
						break;
					}
				}
			}
		}
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
			String value = rt.getStringValue();
			if (value != null)
				value = value.trim();
			if (SmooksModelConstants.BEAN_POPULATOR.equals(value)) {
				return resource;
			}
		}
		return null;
	}

	private void checkRootNodeConnected(
			SmooksConfigurationFileGenerateContext context) {
		GraphRootModel root = context.getGraphicalRootModel();
		List sourceList = root.loadSourceModelList();
		List targetList = root.loadTargetModelList();

		AbstractXMLObject rootSource = null;
		JavaBeanModel rootTarget = null;
		boolean needCheck = false;

		for (Iterator iterator = sourceList.iterator(); iterator.hasNext();) {
			AbstractStructuredDataModel sourcegm = (AbstractStructuredDataModel) iterator
					.next();
			if (sourcegm instanceof IConnectableModel) {
				if (!((IConnectableModel) sourcegm).getModelSourceConnections()
						.isEmpty()
						|| !((IConnectableModel) sourcegm)
								.getModelTargetConnections().isEmpty()) {
					needCheck = true;
					break;
				}
			}
		}

		if (needCheck) {
			for (Iterator iterator = sourceList.iterator(); iterator.hasNext();) {
				AbstractStructuredDataModel sourceGraphModel = (AbstractStructuredDataModel) iterator
						.next();
				AbstractXMLObject source = (AbstractXMLObject) sourceGraphModel
						.getReferenceEntityModel();
				if (source.getParent().getClass() == DocumentObject.class) {
					rootSource = source;
					break;
				}
			}

			for (Iterator iterator = targetList.iterator(); iterator.hasNext();) {
				AbstractStructuredDataModel targetGraphModel = (AbstractStructuredDataModel) iterator
						.next();
				JavaBeanModel target = (JavaBeanModel) targetGraphModel
						.getReferenceEntityModel();
				if (target.isRoot()) {
					rootTarget = target;
					break;
				}
			}
			if (rootSource != null && rootTarget != null) {
				AbstractStructuredDataModel rootSourceGraphModel = UIUtils
						.findGraphModel(root, rootSource);
				AbstractStructuredDataModel rootTargetGraphModel = UIUtils
						.findGraphModel(root, rootTarget);
				if (rootSourceGraphModel instanceof IConnectableModel
						&& rootTargetGraphModel instanceof IConnectableModel) {
					if (((IConnectableModel) rootSourceGraphModel)
							.isSourceConnectWith((IConnectableModel) rootTargetGraphModel)) {
						// do nothing
					} else {
						// ask user if they want to connect the root model
						Shell displayParent = context.getShell();
						boolean connectAuto = MessageDialog
								.openQuestion(
										displayParent,
										Messages
												.getString("XML2JavaAnalyzer.ConnectQDlgTitle"), //$NON-NLS-1$
										Messages
												.getString("XML2JavaAnalyzer.ConnectQDlgContent")); //$NON-NLS-1$
						if (connectAuto) {
							// connect root model
							LineConnectionModel connectionModel = new LineConnectionModel();
							connectionModel
									.setSource((IConnectableModel) rootSourceGraphModel);
							connectionModel
									.setTarget((IConnectableModel) rootTargetGraphModel);
							connectionModel.connect();
						}
					}
				}
			}
		}
	}

	public DesignTimeAnalyzeResult[] analyzeGraphModel(
			SmooksConfigurationFileGenerateContext context) {
		checkRootNodeConnected(context);
		List<DesignTimeAnalyzeResult> typeCheckResults = UIUtils
				.checkTargetJavaModelType(context);
		List<DesignTimeAnalyzeResult> connectionCheckResults = UIUtils
				.checkJavaModelNodeConnection(context);
		typeCheckResults.addAll(connectionCheckResults);
		return typeCheckResults.toArray(new DesignTimeAnalyzeResult[] {});
	}

}

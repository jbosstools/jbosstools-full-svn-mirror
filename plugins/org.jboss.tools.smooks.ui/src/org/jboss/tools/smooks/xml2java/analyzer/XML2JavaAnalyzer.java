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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.analyzer.AbstractAnalyzer;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
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
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

/**
 * @author Dart Peng
 * @Date Aug 20, 2008
 */
public class XML2JavaAnalyzer extends AbstractAnalyzer {

	private static final String SPACE_SPLITER = " ";

	private List<AbstractResourceConfig> usedResourceConfigList = new ArrayList<AbstractResourceConfig>();

	private HashMap<String, ResourceConfigType> createdResourceConfigMap = new HashMap<String, ResourceConfigType>();

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

	protected String generateParentSelector(AbstractXMLObject xmlObject) {
		String selector = xmlObject.getName();
		while (xmlObject.getParent() != null
				&& !(xmlObject.getParent() instanceof TagList)) {
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

		// generate the beanId value:
		if (beanID == null) {
			beanID = getBeanID(target);
		} else {
			if (beanID.startsWith("${") && beanID.endsWith("}")) {
				beanID = beanID.substring(2, beanID.length() - 1);
			}
		}
		// if the resource config was created , return
		if (beanID != null) {
			if (isResourceConfigCreated(beanID)) {
				return;
			}
		}

		ResourceConfigType resourceConfigType = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		context.getGeneratorResourceList().add(resourceConfigType);
		// registe it
		registeCreatedResourceConfig(beanID, resourceConfigType);
		// addResourceConfigType(listType, resourceConfigType);
		// set the selector string value
		resourceConfigType.setSelector(parentSelector);

		// add the properties of connection
		List<PropertyModel> propertyList = connection.getProperties();
		for (Iterator<PropertyModel> iterator = propertyList.iterator(); iterator
				.hasNext();) {
			PropertyModel propertyModel = (PropertyModel) iterator.next();
			if (propertyModel.getName().equals("selector-namespace")) {
				Object value = propertyModel.getValue();
				if (value != null)
					resourceConfigType.setSelectorNamespace(value.toString());
				break;

			}
		}
		// create a resource and add it to resourceConfig
		ResourceType resourceType = SmooksFactory.eINSTANCE
				.createResourceType();
		resourceType.setStringValue(SmooksModelConstants.BEAN_POPULATOR);
		resourceConfigType.setResource(resourceType);

		// create param for resourceConfig
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

			if (targetJavaChild.isPrimitive()
					|| targetJavaChild.getProperties().isEmpty()) {
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
			// if the parent bean is Array or List , the binding property name
			// should be set NULL
			if (javaBean.isArray() || javaBean.isList()) {
				propertyName = null;
			}
			AnyType binding = SmooksModelUtils.addBindingTypeToParamType(
					bindingsParam, propertyName, selector, null, null);
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
		boolean isChild = false;
		String name = sourceModel.getName();
		if (sourceModel instanceof TagPropertyObject) {
			name = "@" + name;
		}
		AbstractXMLObject parent = sourceModel.getParent();
		while (parent != null && parent.getName() != null
				&& !(parent instanceof TagList)) {
			if (parent == currentRoot) {
				isChild = true;
				break;
			}
			name = parent.getName() + SPACE_SPLITER + name;
			parent = parent.getParent();
		}
		if (resourceConfigSelector != null) {
			name = resourceConfigSelector + SPACE_SPLITER + name;
		}
		// if the node is not the child of current root node , reload the name
		if (!isChild) {
			name = sourceModel.getName();
			if (sourceModel instanceof TagPropertyObject) {
				name = "@" + name;
			}
			parent = sourceModel.getParent();
			if (parent == null)
				return name;
			while (!(parent instanceof TagList)) {
				name = name + SPACE_SPLITER + parent.getName();
				parent = parent.getParent();
			}
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
		if (sourceObject instanceof TagList
				&& !((TagList) sourceObject).getRootTagList().isEmpty()) {
			sourceObject = ((TagList) sourceObject).getRootTagList().get(0);
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
			AbstractResourceConfig abstractRC = (AbstractResourceConfig) rlist
					.get(i);
			if (!(abstractRC instanceof ResourceConfigType))
				continue;
			ResourceConfigType resourceConfig = (ResourceConfigType) abstractRC;
			ResourceType resource = resourceConfig.getResource();
			if (resource == null)
				continue;
			String populator = resource.getStringValue();
			if (populator != null)
				populator = populator.trim();
			if (!JavaBeanAnalyzer.BEANPOPULATOR.equals(populator))
				continue;
			// if the ResourCeconfig element was used to render , don't analyze
			// it again.
			if (isResourceConfigUsed(resourceConfig)) {
				continue;
			}
			String selector = resourceConfig.getSelector();
			AbstractXMLObject source = (AbstractXMLObject) UIUtils
					.localXMLNodeWithPath(selector, sourceRoot);
			// findXMLNodeWithSelector(selector, sourceRoot);
			if (source == null) {
				// TODO if can't find the root , throw exception
				// MODIFY by Dart 2008.11.17
				throw new RuntimeException(
						 NLS.bind(Messages.XML2JavaAnalyzer_CantFindRootNodeErrorMessage, selector));
				// return MappingResourceConfigList.createEmptyList();
			}
			JavaBeanModel target = findJavaBeanModel(((List) targetObject),
					resourceConfig);
			if (target == null) {
				// TODO if can't find the root , throw exception
				// MODIFY by Dart 2008.11.17
				String clazzName = SmooksModelUtils.getParmaText("beanClass", //$NON-NLS-1$
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
				String clazzName = SmooksModelUtils.getParmaText("beanClass", //$NON-NLS-1$
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

	public static AbstractXMLObject findXMLNodeWithSelector(String selector,
			AbstractXMLObject root, boolean fromRoot, boolean includeProperty) {
		if (fromRoot) {
			AbstractXMLObject parent = root.getParent();
			if (parent == null)
				return null;
			while (!(parent instanceof TagList)) {
				root = parent;
				parent = parent.getParent();
			}
		}

		if (selector != null)
			selector = selector.trim();
		if (isXMLAttributeObject(selector))
			selector = selector.substring(1);

		if (selector.equalsIgnoreCase(root.getName()))
			return root;
		if (root instanceof TagObject) {
			List<AbstractXMLObject> children = ((TagObject) root)
					.getXMLNodeChildren();
			List<TagPropertyObject> properties = ((TagObject) root)
					.getProperties();
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				AbstractXMLObject child = (AbstractXMLObject) iterator.next();
				AbstractXMLObject result = findXMLNodeWithSelector(selector,
						child, false, includeProperty);
				if (result != null)
					return result;
			}
			if (includeProperty) {
				for (Iterator iterator = properties.iterator(); iterator
						.hasNext();) {
					TagPropertyObject property = (TagPropertyObject) iterator
							.next();
					if (selector.equalsIgnoreCase(property.getName())) {
						return property;
					}
				}
			}
		}

		return null;

	}

	public static AbstractXMLObject findXMLNodeWithSelector(String selector,
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
				root = findXMLNodeWithSelector(firstNodeName, root);
				if (root == null) {
					root = findXMLNodeWithSelector(firstNodeName, root, true,
							false);
				}
				if (root == null)
					return null;
				for (int i = 1; i < nameArray.length; i++) {
					String name = nameArray[i];
					root = findXMLNodeWithSelector(name, root, false, false);
				}
				return root;
			}
		}
		return findXMLNodeWithSelector(selector, root, false, false);
	}

	protected void checkNamespaceProperty(MappingModel mapping,
			ResourceConfigType config) {
		String namespace = config.getSelectorNamespace();
		if (namespace != null) {
			PropertyModel np = new PropertyModel();
			np.setName("selector-namespace"); //$NON-NLS-1$
			np.setValue(namespace);
			mapping.getProperties().add(np);
		}
	}

	protected void createMappingResourceConfigList(
			MappingResourceConfigList configList, SmooksResourceListType list,
			ResourceConfigType processingConfig, AbstractXMLObject sourceRoot,
			JavaBeanModel targetJavaBean) {

		MappingModel mapping = new MappingModel(sourceRoot, targetJavaBean);
		configList.getMappingModelList().add(mapping);
		checkNamespaceProperty(mapping, processingConfig);

		// this ResourceConfig was used to render , so record it in the
		// RelatingResourceConfig of MappingResourceConfigList
		// to make it dosen't to show in the ConfigurationPage.
		configList.addResourceConfig(processingConfig);

		// Load the bindings of parameter element:
		List<Object> bindings = SmooksModelUtils
				.getBindingListFromResourceConfigType(processingConfig);
		// If the resourceConfig was be used , don't to generate binding for it
		// and return.
		if (isResourceConfigUsed(processingConfig)) {
			return;
		} else {
			// when starting to process the ResourceConfig , mark it be used
			setResourceConfigUsed(processingConfig);
		}
		// If the bindings isn't NULL to visit the "property" and "selector" of
		// binding to render the graphical
		if (bindings != null) {
			for (Iterator<Object> iterator = bindings.iterator(); iterator
					.hasNext();) {
				Object anyType = iterator.next();
				if (!(anyType instanceof AnyType))
					continue;
				AnyType binding = (AnyType) anyType;
				String property = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_PROPERTY);
				String selectorStr = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_SELECTOR);

				JavaBeanModel childBean = JavaBeanAnalyzer
						.findTheChildJavaBeanModel(property, targetJavaBean);
				processXMLSelector(configList, processingConfig, sourceRoot,
						childBean, list, selectorStr, binding);
			}
		}
	}

	/**
	 * 
	 * @param configList
	 * @param resourceConfig
	 * @param root
	 * @param targetBean
	 * @param listType
	 * @param selector
	 * @param currentBinding
	 */
	protected void processXMLSelector(MappingResourceConfigList configList,
			ResourceConfigType resourceConfig, AbstractXMLObject root,
			JavaBeanModel targetBean, SmooksResourceListType listType,
			String selector, AnyType currentBinding) {
		if (isReferenceSelector(selector)) {
			ResourceConfigType processingResourceConfig = this
					.findResourceConfigTypeWithSelector(selector, listType);
			if (processingResourceConfig == null) {
				throw new RuntimeException(
						"Can't find some ResourceConfig element in the config file.Maybe some ResourceConfig element miss <param name = \"beanId\">"
								+ selector + "</param>");
			}
			String newSelector = processingResourceConfig.getSelector();
			if (newSelector == null)
				return;
			AbstractXMLObject newRoot = (AbstractXMLObject) UIUtils
					.localXMLNodeWithPath(newSelector, root);
			// findXMLNodeWithSelector(newSelector,root);
			if (newRoot == null) {
				// TODO If can't find the element , throw exception
				// MODIFY by Dart , 2008.11.07
				throw new RuntimeException(
						NLS.bind(Messages.XML2JavaAnalyzer_CantFindNodeErrorMessage, newSelector)); 
			}
			createMappingResourceConfigList(configList, listType,
					processingResourceConfig, newRoot, targetBean);
		} else {

			AbstractXMLObject source = (AbstractXMLObject) UIUtils
					.localXMLNodeWithPath(selector, root);
			if (source == null) {
				// TODO If can't find the element , throw exception
				// MODIFY by Dart , 2008.11.07
				throw new RuntimeException(
						NLS.bind(Messages.XML2JavaAnalyzer_CantFindNodeErrorMessage, selector)); 
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
		String firstName = null;
		if (names.length > 1)
			firstName = names[0];
		else
			firstName = selector;
		AbstractXMLObject current = findXMLNodeWithSelector(firstName, parent,
				false, false);
		// if can't find the node , to search it from the root node
		if (current == null) {
			current = findXMLNodeWithSelector(firstName, parent, true, false);
		}
		if (current == null) {
			return null;
		}
		for (int i = startIndex; i < names.length; i++) {
			String name = names[i].trim();
			current = findXMLNodeWithSelector(name, current, false, true);
		}
		return current;
	}

	public static boolean isXMLAttributeObject(String name) {
		if (name != null && name.startsWith("@")) //$NON-NLS-1$
			return true;
		return false;
	}

	private ResourceConfigType findFirstMappingResourceConfig(
			SmooksResourceListType listType) {
		List list = listType.getAbstractResourceConfig();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractRC = (AbstractResourceConfig) iterator
					.next();
			if (!(abstractRC instanceof ResourceConfigType))
				continue;
			ResourceConfigType resource = (ResourceConfigType) abstractRC;
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
				if (source.getParent().getClass() == TagList.class) {
					rootSource = source;
					break;
				}
			}

			for (Iterator iterator = targetList.iterator(); iterator.hasNext();) {
				AbstractStructuredDataModel targetGraphModel = (AbstractStructuredDataModel) iterator
						.next();
				JavaBeanModel target = (JavaBeanModel) targetGraphModel
						.getReferenceEntityModel();
				if (target.isRootClassModel()) {
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
										Messages.XML2JavaAnalyzer_ConnectQDlgTitle,
										Messages.XML2JavaAnalyzer_ConnectQDlgContent);
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

	public void setResourceConfigUsed(ResourceConfigType resourceConfig) {
		usedResourceConfigList.add(resourceConfig);
	}

	public boolean isResourceConfigUsed(ResourceConfigType resourceConfig) {
		return (usedResourceConfigList.indexOf(resourceConfig) != -1);
	}

	public void registeCreatedResourceConfig(String beanId,
			ResourceConfigType resourceConfig) {
		if (beanId == null)
			return;
		beanId = beanId.trim().toLowerCase();
		createdResourceConfigMap.put(beanId, resourceConfig);
	}

	public ResourceConfigType getCreatedResourceConfig(String beanId) {
		if (beanId == null)
			return null;
		beanId = beanId.trim().toLowerCase();
		return createdResourceConfigMap.get(beanId);
	}

	public boolean isResourceConfigCreated(String beanId) {
		if (beanId == null)
			return false;
		beanId = beanId.trim().toLowerCase();
		return getCreatedResourceConfig(beanId) != null;
	}

	public boolean isResourceConfigCreated(ResourceConfigType resourceConfig) {
		return createdResourceConfigMap.containsValue(resourceConfig);
	}
}

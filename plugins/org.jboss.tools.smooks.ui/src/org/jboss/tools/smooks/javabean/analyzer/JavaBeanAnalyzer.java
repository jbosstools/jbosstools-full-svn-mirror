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
package org.jboss.tools.smooks.javabean.analyzer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.ProjectClassLoader;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * 
 */
public class JavaBeanAnalyzer implements IMappingAnalyzer,
		ISourceModelAnalyzer, ITargetModelAnalyzer {

	public static final String BEANPOPULATOR = "org.milyn.javabean.BeanPopulator"; //$NON-NLS-1$

	public static final String PRO_CLASS_NAME = "__pro_class_name_"; //$NON-NLS-1$

	public static final Object PRO_PROJECT_NAME = "__pro_project_name_"; //$NON-NLS-1$

	public static final String SPACE_STRING = " "; //$NON-NLS-1$

	public static final String COMPLEX_PRIX_START = "${"; //$NON-NLS-1$

	public static final String COMPLEX_PRIX_END = "}"; //$NON-NLS-1$

	private static final int TARGET_DATA = 1;

	private static final int SOURCE_DATA = 0;

	private List usedConnectionList = new ArrayList();

	private ComposedAdapterFactory adapterFactory;

	private AdapterFactoryEditingDomain editingDomain;

	private HashMap userdSelectorString = new HashMap();

	private HashMap<String, JavaBeanModel> javaBeanModelCatch = new HashMap<String, JavaBeanModel>();

	private HashMap<ResourceConfigType, Object> usedResourceConfigMap = new HashMap<ResourceConfigType, Object>();

	private HashMap usedBeanIDMap = new HashMap();

	private List sourceModelList = null;

	private List targetModelList = null;

	public JavaBeanAnalyzer() {

		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new SmooksItemProviderAdapterFactory());

		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				createCommandStack(), new HashMap<Resource, Boolean>());
	}

	protected boolean resourceConfigIsUsed(ResourceConfigType config) {
		return (usedResourceConfigMap.get(config) != null);
	}

	protected void registeSourceJavaBeanWithResourceConfig(
			ResourceConfigType config, JavaBeanModel model) {
		javaBeanModelCatch.put(config.getSelector(), model);
	}

	protected JavaBeanModel loadJavaBeanWithResourceConfig(
			ResourceConfigType config) {
		return javaBeanModelCatch.get(config.getSelector());
	}

	protected void setResourceConfigUsed(ResourceConfigType config) {
		if (!usedResourceConfigMap.containsValue(config))
			usedResourceConfigMap.put(config, new Object());
	}

	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}

	public void analyzeGraphicalModel(AbstractStructuredDataModel root,
			List resouceList) {

		List children = root.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			TreeItemRelationModel dataModel = (TreeItemRelationModel) iterator
					.next();
			List targetConnections = dataModel.getModelTargetConnections();
			if (targetConnections != null && !targetConnections.isEmpty()) {
				this.analyzeStructuredDataModel(resouceList, root, dataModel,
						null, null);
			}
		}
	}

	protected void setSelectorIsUsed(String selector) {
		if (selector == null)
			return;
		userdSelectorString.put(selector, new Object());
	}

	protected boolean beanIDIsUsed(String beanID) {
		return (usedBeanIDMap.get(beanID) != null);
	}

	protected void setBeanIDIsUsed(String beanID) {
		usedBeanIDMap.put(beanID, new Object());
	}

	protected boolean isSelectorIsUsed(String resourceType) {
		if (resourceType == null)
			return false;
		return (userdSelectorString.get(resourceType) != null);
	}

	private boolean connectionIsUsed(Object connection) {
		return (usedConnectionList.indexOf(connection) != -1);
	}

	private void setConnectionUsed(Object connection) {
		usedConnectionList.add(connection);
	}

	/**
	 * 
	 * @param resourceList
	 * @param root
	 * @param dataModel
	 * @param parentResourceConfigType
	 * @param beanId
	 */
	protected void analyzeStructuredDataModel(List resourceList,
			AbstractStructuredDataModel root,
			AbstractStructuredDataModel dataModel,
			ResourceConfigType parentResourceConfigType, String beanId) {
		if (dataModel instanceof IConnectableModel) {
			// if the mode have no target connections, return
			if (((IConnectableModel) dataModel).getModelTargetConnections()
					.isEmpty())
				return;
			// if the datamodel is a root structured data model
			if (dataModel instanceof IConnectableModel) {
				List targetConnections = ((IConnectableModel) dataModel)
						.getModelTargetConnections();
				for (Iterator iterator = targetConnections.iterator(); iterator
						.hasNext();) {
					LineConnectionModel connection = (LineConnectionModel) iterator
							.next();
					Object source = connection.getSource();
					Object target = connection.getTarget();

					// create the first smooks resource fragment

					JavaBeanModel sourceJavaBean = (JavaBeanModel) ((AbstractStructuredDataModel) source)
							.getReferenceEntityModel();
					JavaBeanModel targetJavaBean = (JavaBeanModel) ((AbstractStructuredDataModel) target)
							.getReferenceEntityModel();
					ResourceConfigType resourceConfig = analyzeMajorConnectionsToCreateResourceConfig(
							connection, resourceList, beanId);
					if (resourceConfig == null) {
						continue;
					}
					// to dispatch the target's children (Order processing)
					List children = targetJavaBean.getProperties();
					for (Iterator iterator2 = children.iterator(); iterator2
							.hasNext();) {
						JavaBeanModel childJavaBean = (JavaBeanModel) iterator2
								.next();
						AbstractStructuredDataModel child = UIUtils
								.findGraphModel(
										(AbstractStructuredDataModel) root,
										childJavaBean);
						if (child == null)
							continue;
						if (child instanceof IConnectableModel) {
							if (((IConnectableModel) child)
									.getModelTargetConnections().isEmpty())
								continue;
							// how dispatch more than one connection???
							List<Object> targetConnectionModelList = ((IConnectableModel) child)
									.getModelTargetConnections();
							for (Iterator<Object> iterator3 = targetConnectionModelList
									.iterator(); iterator3.hasNext();) {
								LineConnectionModel childConnection = (LineConnectionModel) iterator3
										.next();
								analyzeChildrenConnectionsToCreateBindingType(
										childConnection, child,
										(AbstractStructuredDataModel) source,
										resourceConfig, root, resourceList,
										getBindingsParamType(resourceConfig));
							}
						}

					}
				}
			}
		}
	}

	private ResourceConfigType analyzeMajorConnectionsToCreateResourceConfig(
			LineConnectionModel connection, List resourceList, String beanId) {
		if (connectionIsUsed(connection))
			return null;
		Object source = connection.getSource();
		Object target = connection.getTarget();

		// create the first smooks resource fragment

		JavaBeanModel sourceJavaBean = (JavaBeanModel) ((AbstractStructuredDataModel) source)
				.getReferenceEntityModel();
		JavaBeanModel targetJavaBean = (JavaBeanModel) ((AbstractStructuredDataModel) target)
				.getReferenceEntityModel();
		String sourceClassName = sourceJavaBean.getBeanClass().getName();

		ResourceConfigType resourceConfig = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		resourceList.add(resourceConfig);
		resourceConfig
				.setSelector(getSourceBeanSelectorString((AbstractStructuredDataModel) source));
		setConnectionUsed(connection);

		ResourceType resource = SmooksFactory.eINSTANCE.createResourceType();
		resource.setStringValue(BEANPOPULATOR);
		resourceConfig.setResource(resource);

		ParamType beanIdParam = SmooksFactory.eINSTANCE.createParamType();
		beanIdParam.setName(SmooksModelUtils.BEAN_ID);
		if (beanId == null)
			beanId = targetJavaBean.getName();
		if (beanId.startsWith(COMPLEX_PRIX_START)) {
			beanId = beanId.substring(2, beanId.indexOf(COMPLEX_PRIX_END));
		}
		SmooksModelUtils.appendTextToSmooksType(beanIdParam, beanId);
		resourceConfig.getParam().add(beanIdParam);

		ParamType beanClassParam = SmooksFactory.eINSTANCE.createParamType();
		beanClassParam.setName(SmooksModelUtils.BEAN_CLASS);
		SmooksModelUtils.appendTextToSmooksType(beanClassParam, targetJavaBean
				.getBeanClassString());
		resourceConfig.getParam().add(beanClassParam);

		ParamType bindingsParam = SmooksFactory.eINSTANCE.createParamType();
		bindingsParam.setName(SmooksModelUtils.BINDINGS);
		resourceConfig.getParam().add(bindingsParam);
		return resourceConfig;
	}

	private ParamType getBindingsParamType(ResourceConfigType config) {
		List<ParamType> list = config.getParam();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			String name = paramType.getName();
			if (name != null)
				name = name.trim();
			if (SmooksModelUtils.BINDINGS.equals(name)) {
				return paramType;
			}
		}
		return null;
	}

	private void analyzeChildrenConnectionsToCreateBindingType(
			LineConnectionModel childConnection,
			AbstractStructuredDataModel child,
			AbstractStructuredDataModel source,
			ResourceConfigType resourceConfig,
			AbstractStructuredDataModel root, List resourceList,
			ParamType bindingsParam) {
		if (connectionIsUsed(childConnection))
			return;
		JavaBeanModel childTargetJavaBean = (JavaBeanModel) child
				.getReferenceEntityModel();
		String currentSelectorName = getSelectorString(
				(AbstractStructuredDataModel) childConnection.getTarget(),
				(AbstractStructuredDataModel) childConnection.getSource(),
				(AbstractStructuredDataModel) source);
		String propertyName = childTargetJavaBean.getName();
		JavaBeanModel targetParent = childTargetJavaBean.getParent();
		if (targetParent.isList() || targetParent.isArray())
			propertyName = null;
		AnyType binding = SmooksModelUtils.addBindingTypeToParamType(
				bindingsParam, propertyName, currentSelectorName, null, null);
		UIUtils.assignConnectionPropertyToBinding(childConnection, binding,
				new String[] { "property", "selector" }); //$NON-NLS-1$ //$NON-NLS-2$
		if (!childTargetJavaBean.isPrimitive()) {
			analyzeStructuredDataModel(resourceList, root,
					(AbstractStructuredDataModel) child, resourceConfig,
					currentSelectorName);
		}
		this.setConnectionUsed(childConnection);
	}

	/**
	 * TODO change the method name to be "getTheBindingPropertySelectorString"
	 * 
	 * @param target
	 * @param source
	 * @param currentRootModel
	 * @return
	 */
	protected String getSelectorString(AbstractStructuredDataModel target,
			AbstractStructuredDataModel source,
			AbstractStructuredDataModel currentRootModel) {
		JavaBeanModel sourcebean = (JavaBeanModel) source
				.getReferenceEntityModel();
		JavaBeanModel rootbean = (JavaBeanModel) currentRootModel
				.getReferenceEntityModel();
		JavaBeanModel currentbean = (JavaBeanModel) target
				.getReferenceEntityModel();
		if (sourcebean.getParent() == rootbean
				|| sourcebean.getParent() == null) {
			if (!currentbean.isPrimitive()) {
				String currentbeanName = currentbean.getName();
				if (currentbeanName.length() > 1) {
					char firstChar = currentbeanName.charAt(0);
					currentbeanName = currentbeanName.substring(1);
					currentbeanName = new String(new char[] { firstChar })
							+ currentbeanName;
				}
				return COMPLEX_PRIX_START + currentbeanName + COMPLEX_PRIX_END;
			} else {
				return rootbean.getBeanClassString() + SPACE_STRING
						+ sourcebean.getName();
			}
		} else {
			JavaBeanModel jbParent = ((JavaBeanModel) source
					.getReferenceEntityModel()).getParent();
			AbstractStructuredDataModel parent = UIUtils.findGraphModel(
					currentRootModel.getParent(), jbParent);
			String returnString = sourcebean.getName();
			while (parent != currentRootModel && parent != null) {
				JavaBeanModel jbm = (JavaBeanModel) parent
						.getReferenceEntityModel();
				if (jbm != null)
					returnString = jbm.getName() + SPACE_STRING + returnString;

				JavaBeanModel jb = ((JavaBeanModel) parent
						.getReferenceEntityModel()).getParent();
				parent = UIUtils.findGraphModel(currentRootModel.getParent(),
						jb);
			}
			// if no property
			if (returnString.equals(sourcebean.getName())) {
				returnString = ((JavaBeanModel) parent
						.getReferenceEntityModel()).getBeanClassString()
						+ SPACE_STRING + returnString;
			}
			return returnString;
		}
	}

	protected String getSourceBeanSelectorString(
			AbstractStructuredDataModel sourceModel) {
		JavaBeanModel source = (JavaBeanModel) sourceModel
				.getReferenceEntityModel();
		if (source.getParent() == null) {
			return source.getBeanClassString();
		}
		if (source.getBeanClass().isArray()) {
			return source.getName();
		}
		if (Collection.class.isAssignableFrom(source.getBeanClass())) {
			return source.getName();
		}
		return source.getBeanClassString();

	}

	/**
	 * If root node don't connect , it will ask user to connect them .
	 * 
	 * @param context
	 */
	private void checkRootNodeConnected(
			SmooksConfigurationFileGenerateContext context) {
		GraphRootModel root = context.getGraphicalRootModel();
		List sourceList = root.loadSourceModelList();
		List targetList = root.loadTargetModelList();

		JavaBeanModel rootSource = null;
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
				JavaBeanModel source = (JavaBeanModel) sourceGraphModel
						.getReferenceEntityModel();
				if (source.isRoot()) {
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
												.getString("JavaBeanAnalyzer.ConnectionQuestion"), //$NON-NLS-1$
										Messages
												.getString("JavaBeanAnalyzer.ConnectRootQuestion")); //$NON-NLS-1$
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.analyzer.IMappingAnalyzer#analyzeMappingGraphModel
	 * (
	 * org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext
	 * )
	 */
	public void analyzeMappingGraphModel(
			SmooksConfigurationFileGenerateContext context)
			throws SmooksAnalyzerException {
		GraphRootModel root = context.getGraphicalRootModel();
		this.analyzeGraphicalModel(root, context.getGeneratorResourceList());
	}

	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType, Object sourceObject,
			Object targetObject) {
		if (sourceObject instanceof List) {
			sourceModelList = (List) sourceObject;
			if (!((List) sourceObject).isEmpty()) {
				sourceObject = (JavaBeanModel) ((List) sourceObject).get(0);
			}
		}
		if (targetObject instanceof List) {
			targetModelList = (List) targetObject;
			if (!((List) targetObject).isEmpty()) {
				targetObject = (JavaBeanModel) ((List) targetObject).get(0);
			}
		}
		if (!(sourceObject instanceof JavaBeanModel)
				|| !(targetObject instanceof JavaBeanModel)) {
			// throw new RuntimeException(
			// "[JavaBeanAnalyzer]Can't load the source/target data from Smooks configuration file."
			// );
			return MappingResourceConfigList.createEmptyList();
		}
		MappingResourceConfigList resourceConfigList = new MappingResourceConfigList();
		JavaBeanModel source = (JavaBeanModel) sourceObject;
		JavaBeanModel target = (JavaBeanModel) targetObject;
		List<MappingModel> mappingModelList = new ArrayList<MappingModel>();
		List<AbstractResourceConfig> rcl = listType.getAbstractResourceConfig();
		for (Iterator iterator = rcl.iterator(); iterator.hasNext();) {
			ResourceConfigType rc = (ResourceConfigType) iterator.next();
			ResourceType rt = rc.getResource();
			// find the first BeanPopulator resource config , this is the root.
			String resourceClazz = null;
			if (resourceConfigIsUsed(rc)) {
				continue;
			}
			if (rt != null) {
				resourceClazz = rt.getStringValue();
			}
			if (resourceClazz != null)
				resourceClazz = resourceClazz.trim();
			if (rt != null && BEANPOPULATOR.equals(resourceClazz)) {
				String sourceName = source.getName();
				Class sourceClazz = source.getBeanClass();
				if (sourceClazz != null) {
					sourceName = sourceClazz.getName();
				}
				String selector = rc.getSelector();
				if (selector != null) {
					selector = selector.trim();
				}
				if (!sourceName.equals(selector)) {
					source = findJavaBeanModelFormList(selector,
							sourceModelList);
					if (source != null) {
						sourceClazz = source.getBeanClass();
						if (sourceClazz != null) {
							sourceName = sourceClazz.getName();
						}
					}
				}
				if (sourceName.equals(selector)) {
					String targetName = target.getName();
					Class targetClazz = target.getBeanClass();
					if (targetClazz != null) {
						targetName = targetClazz.getName();
					}
					String beanClass = SmooksModelUtils.getParmaText(
							SmooksModelUtils.BEAN_CLASS, rc);
					if (targetName != null
							&& targetName.trim().equals(beanClass)) {
					} else {
						JavaBeanModel temptarget = findJavaBeanModelFormList(
								beanClass, targetModelList);
						if (temptarget != null) {
							target = temptarget;
						}
					}
					if (target != null) {
						mappingModelList.add(new MappingModel(source, target));
						resourceConfigList.addResourceConfig(rc);
						analyzeMappingModelFromResourceConfig(mappingModelList,
								resourceConfigList, listType, rc, source,
								target);
						setResourceConfigUsed(rc);
					}
				}
			}
		}
		resourceConfigList.setMappingModelList(mappingModelList);
		return resourceConfigList;
	}

	private JavaBeanModel findJavaBeanModelFormList(String beanClassString,
			List objectList) {
		if (beanClassString == null)
			return null;
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof JavaBeanModel) {
				if (beanClassString.equals(((JavaBeanModel) object)
						.getBeanClassString())) {
					return (JavaBeanModel) object;
				}
			}
		}
		return null;
	}

	protected boolean isReferenceSelector(String selector) {
		return (selector.startsWith(COMPLEX_PRIX_START) && selector
				.endsWith(COMPLEX_PRIX_END));
	}

	protected String getBeanIdWithRawSelectorString(String selector) {
		selector = selector.substring(2, selector.length() - 1);
		return selector;
	}

	protected void analyzeMappingModelFromResourceConfig(
			List<MappingModel> mappingModelList,
			MappingResourceConfigList mappingResourceConfigList,
			SmooksResourceListType resourceList,
			ResourceConfigType resourceConfig, JavaBeanModel source,
			JavaBeanModel target) {
		List<Object> bindingList = SmooksModelUtils
				.getBindingListFromResourceConfigType(resourceConfig);
		if (bindingList == null)
			return;

		registeSourceJavaBeanWithResourceConfig(resourceConfig, source);
		for (Iterator<Object> iterator = bindingList.iterator(); iterator
				.hasNext();) {
			AnyType binding = (AnyType) iterator.next();
			String property = SmooksModelUtils.getAttributeValueFromAnyType(
					binding, SmooksModelUtils.ATTRIBUTE_PROPERTY);
			if (property != null) {
				property = property.trim();
			}
			String selector = SmooksModelUtils.getAttributeValueFromAnyType(
					binding, SmooksModelUtils.ATTRIBUTE_SELECTOR);
			if (selector != null) {
				selector = selector.trim();
			}
			JavaBeanModel childTargetModel = findTheChildJavaBeanModel(
					property, target);
			JavaBeanModel sourceModel = null;
			if (childTargetModel == null) {
				// TODO if can't find the child node , throw exception
				// MODIFY by Dart 2008.11.07
				throw new RuntimeException(
						"[JavaBeanAnalyzer]There isn't any child property named \""
								+ property + "\" of \"" + target.getName()
								+ "\" JavaBean model");
			}
			if (isReferenceSelector(selector)) {
				ResourceConfigType rc = this.findResourceConfigTypeWithBeanId(
						selector, resourceList);
				if (rc != null && !resourceConfigIsUsed(rc)) {
					sourceModel = findModelWithResourceConfig(rc, source);
					if (sourceModel != null) {
						mappingResourceConfigList.addResourceConfig(rc);
						analyzeMappingModelFromResourceConfig(mappingModelList,
								mappingResourceConfigList, resourceList, rc,
								sourceModel, childTargetModel);
						setResourceConfigUsed(rc);
					}
				}
			} else {
				sourceModel = findModelWithSelectorString(selector, source);
			}
			if (sourceModel != null) {
				MappingModel model = new MappingModel(sourceModel,
						childTargetModel);
				UIUtils.assignBindingPropertyToMappingModel(binding, model,
						new Object[] { SmooksModelUtils.ATTRIBUTE_PROPERTY,
								SmooksModelUtils.ATTRIBUTE_SELECTOR });
				mappingModelList.add(model);
			}
		}
	}

	protected JavaBeanModel findModelWithResourceConfig(
			ResourceConfigType config, JavaBeanModel parentModel) {
		String newSelector = config.getSelector();
		JavaBeanModel model = findTheChildJavaBeanModel(newSelector,
				parentModel);
		if (model == null) {
			model = loadJavaBeanWithResourceConfig(config);
		}
		return model;
	}

	protected JavaBeanModel findModelWithSelectorString(String selector,
			JavaBeanModel parentModel) {
		String[] s = selector.trim().split(SPACE_STRING);
		String parentName = parentModel.getName();
		Class clazz = parentModel.getBeanClass();
		JavaBeanModel current = parentModel;
		if (clazz != null) {
			parentName = clazz.getName();
		} else {
			// TODO if can't find the class, throw exception
			// MODIFY by Dart 08.11.07
			throw new RuntimeException("[JavaBeanAnalyzer]JavaBean \""
					+ parentModel.getName() + "\" can't load its class.");
		}
		if (s != null) {
			for (int i = 0; i < s.length; i++) {
				String childName = s[i];
				if (childName.equals(parentName))
					continue;
				JavaBeanModel child = findTheChildJavaBeanModel(childName,
						current);
				if (child == null) {
					// TODO if can't find the child node , throw exception
					// MODIFY by Dart 2008.11.07
					throw new RuntimeException(
							"[JavaBeanAnalyzer]There isn't any child property named \""
									+ childName + "\" of \""
									+ parentModel.getName()
									+ "\" JavaBean model");
				}
				current = child;
			}
			return current;
		}
		return null;
	}

	private String getDataSourceClass(GraphInformations info, int dataMode) {
		String key = "sourceDataPath"; //$NON-NLS-1$
		if (dataMode == SOURCE_DATA) {
			key = "sourceDataPath"; //$NON-NLS-1$
		}
		if (dataMode == TARGET_DATA) {
			key = "targetDataPath"; //$NON-NLS-1$
		}

		Params params = info.getParams();
		List paramList = params.getParam();
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			Param param = (Param) iterator.next();
			if (key.equals(param.getName())) {
				return param.getValue();
			}
		}

		return null;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile,
			ClassLoader classLoader) throws InvocationTargetException {
		List resourceConfigList = listType.getAbstractResourceConfig();
		String rootClassName = null;
		ResourceConfigType current = null;
		List<JavaBeanModel> list = new ArrayList<JavaBeanModel>();
		for (Iterator iterator = resourceConfigList.iterator(); iterator
				.hasNext();) {
			AbstractResourceConfig ar = (AbstractResourceConfig) iterator
					.next();
			if (ar instanceof ResourceConfigType) {
				ResourceConfigType rc = (ResourceConfigType) ar;
				if (resourceConfigIsUsed(rc)) {
					continue;
				}
				ResourceType resourceType = rc.getResource();
				if (resourceType == null)
					continue;
				String resource = resourceType.getStringValue();
				if (resource != null)
					resource = resource.trim();
				if (BEANPOPULATOR.equals(resource)) {
					// create root beanmodel
					rootClassName = rc.getSelector();
					current = rc;
					if (rootClassName == null) {
						rootClassName = this.getDataSourceClass(graphInfo,
								SOURCE_DATA);
					}
					if (rootClassName == null) {
						return null;
					} else {
						rootClassName = rootClassName.trim();
					}
					boolean isWarning = false;
					boolean isError = false;
					Class clazz = null;
					try {
						if (classLoader == null) {
							IProject project = sourceFile.getProject();
							classLoader = new ProjectClassLoader(JavaCore
									.create(project));
						}

						clazz = classLoader.loadClass(rootClassName);
					} catch (ClassNotFoundException e) {
						// TODO if can't find the class throws exception
						// MODIFY by Dart 2008.11.12
						throw new RuntimeException("Can't find the class : \""
								+ rootClassName
								+ "\" to create the JavaBean model");
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
					JavaBeanModel model = null;
					if (clazz != null) {
						model = JavaBeanModelFactory
								.getJavaBeanModelWithLazyLoad(clazz);
					} else {
						model = new JavaBeanModel(null, rootClassName);
						model.setRootClassModel(true);
						model
								.setError(Messages
										.getString("JavaBeanAnalyzer.ClassNotExist") + rootClassName); //$NON-NLS-1$
						model.setProperties(new ArrayList());
						isError = true;
					}
					if (model != null) {
						setCollectionsInstanceClassName(model, current);
						this.setSelectorIsUsed(rootClassName);
						buildSourceInputProperties(listType, model, false,
								isError, current, classLoader);
						this.setResourceConfigUsed(current);
					}
					if (model != null) {
						if (!modelIsInList(list, model.getBeanClassString()))
							list.add(model);
					}
				}
			}
		}
		if (current == null) {
			// rootClassName = this.getDataSourceClass(graphInfo, TARGET_DATA);
			// TODO if there isn't any BeanPopulater throws exception
			// MODIFY by Dart 2008.11.17
			try{
			if (classLoader == null) {
				IProject project = sourceFile.getProject();
				classLoader = new ProjectClassLoader(JavaCore.create(project));
			}
			String classString = getDataSourceClass(graphInfo, TARGET_DATA);
			if (classString != null && classLoader != null) {
					String[] classes = classString.split(";");
					if (classes != null) {
						for (int i = 0; i < classes.length; i++) {
							String clazzName = classes[i];
							list.add(JavaBeanModelFactory
									.getJavaBeanModelWithLazyLoad(classLoader
											.loadClass(clazzName)));
						}
						return list;
					}
			}
			}catch (ClassNotFoundException e) {
				// TODO if can't find the class throws exception
				// MODIFY by Dart 2008.11.12
				throw new RuntimeException("Can't find the class : \""
						+ rootClassName
						+ "\" to create the JavaBean model");
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			// throw new RuntimeException(
			// "Can't load Java bean model form the config file.");
		}
		// if can't load the source from GraphicalInformation , return NULL
		if (current == null && rootClassName == null)
			return list;
		return list;
	}

	private void setCollectionsInstanceClassName(JavaBeanModel model,
			ResourceConfigType resourceConfig) {
		if (Collection.class.isAssignableFrom(model.getBeanClass())) {
			String instanceName = SmooksModelUtils.getParmaText(
					SmooksModelUtils.BEAN_CLASS, resourceConfig);
			if (instanceName != null) {
				model.setBeanClassString(instanceName);
			}
		}
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		UIUtils.checkSelector(listType);
		return this.buildSourceInputObjects(graphInfo, listType, sourceFile,
				null);
	}

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		UIUtils.checkSelector(listType);
		return this.buildTargetInputObjects(graphInfo, listType, sourceFile,
				null);
	}

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile,
			ClassLoader loader) throws InvocationTargetException {
		if (loader == null) {
			try {
				loader = new ProjectClassLoader(JavaCore.create(sourceFile
						.getProject()));
			} catch (JavaModelException e) {
				// ignore
			}
		}
		List resourceConfigList = listType.getAbstractResourceConfig();
		String rootClassName = null;
		ResourceConfigType current = null;
		for (Iterator iterator = resourceConfigList.iterator(); iterator
				.hasNext();) {
			AbstractResourceConfig ar = (AbstractResourceConfig) iterator
					.next();
			if (ar instanceof ResourceConfigType) {
				ResourceConfigType rc = (ResourceConfigType) ar;
				ResourceType resourceType = rc.getResource();
				if (resourceType == null)
					continue;
				String resource = resourceType.getStringValue();
				if (resource != null)
					resource = resource.trim();
				if (BEANPOPULATOR.equals(resource)) {
					// create root beanmodel
					current = rc;
					break;
				}
			}
		}
		List<JavaBeanModel> list = new ArrayList<JavaBeanModel>();
		if (current == null) {
			// rootClassName = this.getDataSourceClass(graphInfo, TARGET_DATA);
			// TODO if there isn't any BeanPopulater throws exception
			// MODIFY by Dart 2008.11.17
			String classString = getDataSourceClass(graphInfo, TARGET_DATA);
			if (classString != null && loader != null) {
				try {
					String[] classes = classString.split(";");
					if (classes != null) {
						for (int i = 0; i < classes.length; i++) {
							String clazzName = classes[i];
							list.add(JavaBeanModelFactory
									.getJavaBeanModelWithLazyLoad(loader
											.loadClass(clazzName)));
						}
						return list;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			// throw new RuntimeException(
			// "Can't load Java bean model form the config file.");
		}
		// if can't load the source from GraphicalInformation , return NULL
		if (current == null && rootClassName == null)
			return list;
		int startIndex = resourceConfigList.indexOf(current);
		for (int i = startIndex; i < resourceConfigList.size(); i++) {
			ResourceConfigType resourceConfig = (ResourceConfigType) resourceConfigList
					.get(i);
			ResourceType resourceType = resourceConfig.getResource();
			String selector = resourceConfig.getSelector();
			if (selector != null)
				selector = selector.trim();
			if (isSelectorIsUsed(selector))
				continue;
			if (resourceType == null)
				continue;
			String resource = resourceType.getStringValue();
			if (resource != null)
				resource = resource.trim();
			if (!BEANPOPULATOR.equals(resource)) {
				continue;
			}
			Class rootClass = null;
			rootClassName = SmooksModelUtils.getParmaText(
					SmooksModelUtils.BEAN_CLASS, resourceConfig);
			if (rootClassName != null && loader != null) {
				try {
					rootClass = loader.loadClass(rootClassName);
				} catch (ClassNotFoundException e) {
					// TODO if can't find the class throws exception
					// MODIFY by Dart 2008.11.12
					// throw new RuntimeException("Can't find the class : \""
					// + rootClassName + "\" to create the JavaBean model");
				}
			}
			boolean rootIsError = false;
			JavaBeanModel rootModel = null;
			if (rootClass != null) {
				rootModel = JavaBeanModelFactory
						.getJavaBeanModelWithLazyLoad(rootClass);
			} else {
				rootModel = new JavaBeanModel(null, rootClassName);
				rootIsError = true;
			}
			if (modelIsInList(list, rootModel.getBeanClassString())) {
				continue;
			}
			if (resourceConfig != null) {
				rootModel.setBeanClassString(SmooksModelUtils.getParmaText(
						SmooksModelUtils.BEAN_CLASS, resourceConfig));
				// setSelectorIsUsed(selector);
				buildChildrenOfTargetInputModel(listType, rootModel, false,
						rootIsError, resourceConfig, loader);
				list.add(rootModel);
			}
		}
		return list;
	}

	private boolean modelIsInList(List list, String modelClassString) {
		if (list == null || modelClassString == null)
			return false;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof JavaBeanModel) {
				if (modelClassString.equals(((JavaBeanModel) object)
						.getBeanClassString())) {
					return true;
				}
			}
		}
		return false;
	}

	protected void buildChildrenOfTargetInputModel(
			SmooksResourceListType listType, JavaBeanModel beanModel,
			boolean rootIsWarning, boolean rootIsError,
			ResourceConfigType currentResourceConfigType,
			ClassLoader classLoader) {
		if (currentResourceConfigType != null) {
			List bindingList = SmooksModelUtils
					.getBindingListFromResourceConfigType(currentResourceConfigType);
			if (bindingList == null)
				return;
			for (Iterator iterator = bindingList.iterator(); iterator.hasNext();) {
				AnyType binding = (AnyType) iterator.next();
				String property = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_PROPERTY);
				String selector = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_SELECTOR);
				if (selector != null)
					selector = selector.trim();
				String tempSelector = selector;
				if (isReferenceSelector(selector)) {
					tempSelector = tempSelector.substring(2,
							selector.length() - 1);
				}
				if (!isSelectorIsUsed(tempSelector)) {
					processBindingPropertyFromTargetModel(listType, property,
							selector, beanModel, classLoader);
				}
			}
		}

	}

	protected void processBindingPropertyFromTargetModel(
			SmooksResourceListType listType, String property, String selector,
			JavaBeanModel parentModel, ClassLoader classLoader) {
		parentModel.getProperties();
		JavaBeanModel model = this.findTheChildJavaBeanModel(property,
				parentModel);
		if (model == null) {
			// TODO if the model can't be found , throw exception
			if (true)
				throw new RuntimeException("Can't find the \"" + property
						+ "\" from \"" + parentModel.getName() + "\" model");
			model = new JavaBeanModel(null, property);
			parentModel.addProperty(model);
			model.setError(Messages.getString("JavaBeanAnalyzer.DontExist")); //$NON-NLS-1$
		}
		if (isSelectorIsUsed(selector))
			return;
		if (isReferenceSelector(selector)) {
			selector = selector.substring(2, selector.length() - 1);
			ResourceConfigType resourceConfig = findResourceConfigTypeWithBeanId(
					selector, listType);
			if (resourceConfig == null) {
				throw new RuntimeException(
						"Can't find some ResourceConfig element in the config file.Maybe some ResourceConfig element miss <param name = \"beanId\">"
								+ selector + "</param>");
			}
			String resourceConfigSelector = resourceConfig.getSelector();
			if (resourceConfigSelector != null) {
				resourceConfigSelector = resourceConfigSelector.trim();
				setSelectorIsUsed(resourceConfigSelector);
			}
			String beanClassText = SmooksModelUtils.getParmaText(
					SmooksModelUtils.BEAN_CLASS, resourceConfig);
			model.setBeanClassString(beanClassText);
			if (resourceConfig != null) {
				this.buildChildrenOfTargetInputModel(listType, model, false,
						false, resourceConfig, classLoader);
			}
		}
	}

	// protected List getBindingListFromResourceConfigType(
	// ResourceConfigType resourceConfig) {
	// List paramList = resourceConfig.getParam();
	// for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
	// ParamType param = (ParamType) iterator.next();
	// if (SmooksModelUtils.BINDINGS.equals(param.getName())) {
	// if (param.eContents().isEmpty())
	// continue;
	// List bindingList = (List) param.getMixed().get(
	// SmooksModelUtils.ELEMENT_BINDING, false);
	// return bindingList;
	// }
	// }
	// return null;
	// }

	protected void buildSourceInputProperties(SmooksResourceListType listType,
			JavaBeanModel beanModel, boolean rootIsWarning,
			boolean rootIsError, ResourceConfigType currentResourceConfigType,
			ClassLoader classLoader) {
		if (currentResourceConfigType != null) {
			List bindingList = SmooksModelUtils
					.getBindingListFromResourceConfigType(currentResourceConfigType);
			if (bindingList == null)
				return;
			for (Iterator iterator2 = bindingList.iterator(); iterator2
					.hasNext();) {
				AnyType binding = (AnyType) iterator2.next();
				String selector = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_SELECTOR);
				setSelectorIsUsed(currentResourceConfigType.getSelector());
				analyzeBindingSelector(selector, beanModel, listType,
						classLoader);
			}
		}
	}

	protected ResourceConfigType findResourceConfigTypeWithBeanId(
			String selector, SmooksResourceListType listType) {
		if (isReferenceSelector(selector)) {
			selector = this.getBeanIdWithRawSelectorString(selector);
		}
		List rl = listType.getAbstractResourceConfig();
		ResourceConfigType resourceConfig = null;
		for (Iterator iterator = rl.iterator(); iterator.hasNext();) {
			ResourceConfigType rct = (ResourceConfigType) iterator.next();
			// if (this.isSelectorIsUsed(rct.getSelector()))
			// continue;
			String beanId = getBeanIDFromParam(rct);
			if (selector.equals(beanId)) {
				resourceConfig = rct;
				break;
			}
		}
		return resourceConfig;
	}

	protected void analyzeBindingSelector(String selector,
			JavaBeanModel currentModel, SmooksResourceListType listType,
			ClassLoader classLoader) {
		if (selector != null) {
			selector = selector.trim();
		}
		if (selector.startsWith(COMPLEX_PRIX_START)
				&& selector.endsWith(COMPLEX_PRIX_END)) {
			// should get the bean properties
			// memory out???
			currentModel.getProperties();
			selector = this.getBeanIdWithRawSelectorString(selector);
			ResourceConfigType resourceConfig = findResourceConfigTypeWithBeanId(
					selector, listType);
			if (resourceConfig != null && !resourceConfigIsUsed(resourceConfig)) {
				String referenceSelector = resourceConfig.getSelector();
				JavaBeanModel model = findTheChildJavaBeanModel(
						referenceSelector, currentModel);
				// try to test the selector is can be loaded by classloader??
				if (model == null) {
					Class clazz = null;
					try {
						if (classLoader != null) {
							clazz = classLoader.loadClass(referenceSelector);
							if (clazz != null && model == null)
								model = JavaBeanModelFactory
										.getJavaBeanModelWithLazyLoad(clazz);
						}
					} catch (ClassNotFoundException e) {
						// TODO if can't find the class throws exception
						// MODIFY by Dart 2008.11.12
						throw new RuntimeException("Can't find the class : \""
								+ referenceSelector
								+ "\" to create the JavaBean model");
					}
				}
				// something wrong
				if (model == null) {
					model = new JavaBeanModel(null, referenceSelector);
					model.setError(Messages
							.getString("JavaBeanAnalyzer.DontExist")); //$NON-NLS-1$
					model.setProperties(new ArrayList());
					setCollectionsInstanceClassName(model, resourceConfig);
				}
				// if there occurs error so we need to add the child node by
				// hand , if no , don't care that.
				if (currentModel.getError() != null) {
					currentModel.addProperty(model);
				}
				buildSourceInputProperties(listType, model, false, true,
						resourceConfig, classLoader);
				setResourceConfigUsed(resourceConfig);
			}
		} else {
			selector = selector.trim();
			String[] properties = selector.split(SPACE_STRING);
			if (properties != null) {
				JavaBeanModel currentParent = currentModel;
				for (int i = 0; i < properties.length; i++) {
					String property = properties[i].trim();
					Class currentClazz = currentParent.getBeanClass();
					if (currentClazz != null
							&& property.equals(currentClazz.getName())) {

					} else {
						JavaBeanModel pm = findTheChildJavaBeanModel(property,
								currentParent);
						if (pm != null) {

						} else {
							pm = new JavaBeanModel(null, property);
							pm.setProperties(new ArrayList());
							pm.setError(Messages
									.getString("JavaBeanAnalyzer.DontExist")); //$NON-NLS-1$
							currentParent.addProperty(pm);
						}
						currentParent = pm;
					}
				}
			} else {
				// if properties is null , how to process?
			}
		}
	}

	protected String getBeanIDFromParam(ResourceConfigType config) {
		List list = config.getParam();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ParamType p = (ParamType) iterator.next();
			if (SmooksModelUtils.BEAN_ID.equals(p.getName())) {
				return SmooksModelUtils.getAnyTypeText(p);
			}

		}
		return null;
	}

	/**
	 * Find the child JavaBeanModel from parent with the child name <br>
	 * If the parent JavabeanModel is "Collection" or "Array" , the child
	 * JavabeanModel is the first child of the parent model,it means that the
	 * param "name" is useless with the status.<br>
	 * 
	 * TODO The method need to improve!!!!!
	 * 
	 * @param name
	 * @param parentModel
	 * @return
	 */
	public static JavaBeanModel findTheChildJavaBeanModel(String name,
			JavaBeanModel parentModel) {
		List list = parentModel.getProperties();
		if (list == null)
			return null;
		if (parentModel.isList() || parentModel.isArray()) {
			if (list.size() >= 1 && parentModel.hasGenericType()) {
				JavaBeanModel m = (JavaBeanModel) list.get(0);
				return m;
			}
		}

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			JavaBeanModel child = (JavaBeanModel) iterator.next();
			if (name.equals(child.getName())) {
				return child;
			}
		}
		return null;
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

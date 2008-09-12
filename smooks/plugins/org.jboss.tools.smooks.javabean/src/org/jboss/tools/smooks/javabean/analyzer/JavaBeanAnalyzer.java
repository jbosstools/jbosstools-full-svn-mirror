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
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.ProjectClassLoader;
import org.jboss.tools.smooks.utils.UIUtils;
import org.milyn.xsd.smooks.AbstractResourceConfig;
import org.milyn.xsd.smooks.ParamType;
import org.milyn.xsd.smooks.ResourceConfigType;
import org.milyn.xsd.smooks.ResourceType;
import org.milyn.xsd.smooks.SmooksFactory;
import org.milyn.xsd.smooks.SmooksPackage;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.provider.SmooksItemProviderAdapterFactory;
import org.milyn.xsd.smooks.util.SmooksModelUtils;

/**
 * @author Dart Peng
 * 
 */
public class JavaBeanAnalyzer implements IMappingAnalyzer,
		ISourceModelAnalyzer, ITargetModelAnalyzer {

	public static final String BEANPOPULATOR = "org.milyn.javabean.BeanPopulator";

	public static final String PRO_CLASS_NAME = "__pro_class_name_";

	public static final Object PRO_PROJECT_NAME = "__pro_project_name_";

	public static final String SPACE_STRING = " ";

	private List usedConnectionList = new ArrayList();

	private ComposedAdapterFactory adapterFactory;

	private AdapterFactoryEditingDomain editingDomain;

	private HashMap userdResourceTypeMap = new HashMap();

	private HashMap usedBeanIDMap = new HashMap();

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

	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}

	public void analyzeGraphicalModel(AbstractStructuredDataModel root,
			SmooksResourceListType resouceList) {

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
		userdResourceTypeMap.put(selector, new Object());
	}

	protected boolean beanIDIsUsed(String beanID) {
		return (usedBeanIDMap.get(beanID) != null);
	}

	protected void setBeanIDIsUsed(String beanID) {
		usedBeanIDMap.put(beanID, new Object());
	}

	protected boolean isSelectorIsUsed(String resourceType) {
		return (userdResourceTypeMap.get(resourceType) != null);
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
	protected void analyzeStructuredDataModel(
			SmooksResourceListType resourceList,
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
					if (connectionIsUsed(connection))
						continue;
					Object source = connection.getSource();
					Object target = connection.getTarget();
					if (target != dataModel) {
						continue;
					}
					// create the first smooks resource fragment

					JavaBeanModel sourceJavaBean = (JavaBeanModel) ((AbstractStructuredDataModel) source)
							.getReferenceEntityModel();
					JavaBeanModel targetJavaBean = (JavaBeanModel) ((AbstractStructuredDataModel) target)
							.getReferenceEntityModel();
					String sourceClassName = sourceJavaBean.getBeanClass()
							.getName();

					ResourceConfigType resourceConfig = SmooksFactory.eINSTANCE
							.createResourceConfigType();

					Command addResourceConfigCommand = AddCommand
							.create(
									editingDomain,
									resourceList,
									SmooksPackage.eINSTANCE
											.getSmooksResourceListType_AbstractResourceConfig(),
									resourceConfig);
					addResourceConfigCommand.execute();
					// resouceConfig.
					resourceConfig
							.setSelector(getSourceBeanSelectorString((AbstractStructuredDataModel) source));
					setConnectionUsed(connection);

					ResourceType resource = SmooksFactory.eINSTANCE
							.createResourceType();
					resource.setValue(BEANPOPULATOR);
					resourceConfig.setResource(resource);

					ParamType beanIdParam = SmooksFactory.eINSTANCE
							.createParamType();
					beanIdParam.setName("beanId");
					if (beanId == null)
						beanId = targetJavaBean.getName();
					if (beanId.startsWith("${")) {
						beanId = beanId.substring(2, beanId.indexOf("}"));
					}
					SmooksModelUtils
							.appendTextToSmooksType(beanIdParam, beanId);
					resourceConfig.getParam().add(beanIdParam);

					ParamType beanClassParam = SmooksFactory.eINSTANCE
							.createParamType();
					beanClassParam.setName("beanClass");
					SmooksModelUtils.appendTextToSmooksType(beanClassParam,
							targetJavaBean.getBeanClassString());
					resourceConfig.getParam().add(beanClassParam);

					ParamType bindingsParam = SmooksFactory.eINSTANCE
							.createParamType();
					bindingsParam.setName("bindings");
					resourceConfig.getParam().add(bindingsParam);

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
							LineConnectionModel childConnection = (LineConnectionModel) ((IConnectableModel) child)
									.getModelTargetConnections().get(0);
							if (connectionIsUsed(childConnection))
								continue;
							Object[] properties = childConnection
									.getPropertyArray();
							JavaBeanModel jbean = (JavaBeanModel) child
									.getReferenceEntityModel();
							String currentSelectorName = getSelectorString(
									(AbstractStructuredDataModel) childConnection
											.getTarget(),
									(AbstractStructuredDataModel) childConnection
											.getSource(),
									(AbstractStructuredDataModel) source);
							AnyType binding = SmooksModelUtils
									.addBindingTypeToParamType(bindingsParam,
											jbean.getName(),
											currentSelectorName, null, null);
							for (int i = 0; i < properties.length; i++) {
								PropertyModel property = (PropertyModel) properties[i];
								String pname = property.getName();
								String pvalue = property.getValue();
								binding.getAnyAttribute().add(
										ExtendedMetaData.INSTANCE
												.demandFeature(null, pname,
														false), pvalue);
							}

							if (!jbean.isPrimitive()) {
								analyzeStructuredDataModel(resourceList, root,
										(AbstractStructuredDataModel) child,
										resourceConfig, currentSelectorName);
							}
							this.setConnectionUsed(childConnection);
						}

					}
				}
			}
		}
	}

	/**
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
		if (sourcebean.getParent() == currentRootModel
				.getReferenceEntityModel()) {
			if (!currentbean.isPrimitive()) {
				return "${" + currentbean.getName() + "}";
			} else {
				return rootbean.getBeanClassString() + " "
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
					returnString = jbm.getName() + " " + returnString;

				JavaBeanModel jb = ((JavaBeanModel) parent
						.getReferenceEntityModel()).getParent();
				parent = UIUtils.findGraphModel(currentRootModel.getParent(),
						jb);
			}
			// if no property
			if (returnString.equals(sourcebean.getName())) {
				returnString = ((JavaBeanModel) parent
						.getReferenceEntityModel()).getBeanClassString()
						+ " " + returnString;
			}
			return returnString;
		}
	}

	protected String getSourceBeanSelectorString(
			AbstractStructuredDataModel sourceModel) {
		JavaBeanModel source = (JavaBeanModel) sourceModel
				.getReferenceEntityModel();
		if (Collection.class.isAssignableFrom(source.getBeanClass())) {
			return source.getName();
		}
		return source.getBeanClassString();

	}

	public void analyzeMappingGraphModel(
			SmooksConfigurationFileGenerateContext context)
			throws SmooksAnalyzerException {
		GraphRootModel root = context.getDataMappingRootModel();
		SmooksResourceListType listType = context.getSmooksResourceListModel();
		this.analyzeGraphicalModel(root, listType);
	}

	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType, Object sourceObject,
			Object targetObject) {
		if (!(sourceObject instanceof JavaBeanModel)
				|| !(targetObject instanceof JavaBeanModel)) {
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
			if (rt != null && BEANPOPULATOR.equals(rt.getValue())) {
				String sourceName = source.getName();
				Class sourceClazz = source.getBeanClass();
				if (sourceClazz != null) {
					sourceName = sourceClazz.getName();
				}
				String selector = rc.getSelector();
				if (sourceName.equals(selector)) {
					String targetName = target.getName();
					Class targetClazz = target.getBeanClass();
					if (targetClazz != null) {
						targetName = targetClazz.getName();
					}
					String beanClass = SmooksModelUtils.getParmaText(
							"beanClass", rc);
					if (targetName.equals(beanClass)) {
						setSelectorIsUsed(sourceName);
						// create the first connection
						mappingModelList.add(new MappingModel(source, target));
						resourceConfigList.addResourceConfig(rc);
						analyzeMappingModelFromResourceConfig(mappingModelList,
								resourceConfigList, listType, rc, source,
								target);
					}
				}
			}
		}
		resourceConfigList.setMappingModelList(mappingModelList);
		return resourceConfigList;
	}

	protected boolean isReferenceSelector(String selector) {
		return (selector.startsWith("${") && selector.endsWith("}"));
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
		List bindingList = this
				.getBindingListFromResourceConfigType(resourceConfig);
		if (bindingList == null)
			return;
		for (Iterator iterator = bindingList.iterator(); iterator.hasNext();) {
			AnyType binding = (AnyType) iterator.next();
			String property = SmooksModelUtils.getAttributeValueFromAnyType(
					binding, SmooksModelUtils.ATTRIBUTE_PROPERTY);
			String selector = SmooksModelUtils.getAttributeValueFromAnyType(
					binding, SmooksModelUtils.ATTRIBUTE_SELECTOR);
			JavaBeanModel targetModel = findTheChildJavaBeanModel(property,
					target);
			JavaBeanModel sourceModel = null;
			if (targetModel == null)
				continue;
			if (isReferenceSelector(selector)) {
				ResourceConfigType rc = this
						.findResourceCinfigTypeWithSelector(selector,
								resourceList);
				if (rc != null) {
					String newSelector = rc.getSelector();
					sourceModel = findTheChildJavaBeanModel(newSelector, source);
					if (sourceModel != null) {
						setSelectorIsUsed(newSelector);
						mappingResourceConfigList.addResourceConfig(rc);
						analyzeMappingModelFromResourceConfig(mappingModelList,
								mappingResourceConfigList, resourceList, rc,
								sourceModel, targetModel);
					}
				}
			} else {
				sourceModel = findModelWithSelectorString(selector, source);
			}
			if (sourceModel != null) {
				MappingModel model = new MappingModel(sourceModel, targetModel);
				FeatureMap it = binding.getAnyAttribute();
				for (int i = 0; i < it.size(); i++) {
					EStructuralFeature feature = it.getEStructuralFeature(i);
					if (feature.equals(SmooksModelUtils.ATTRIBUTE_PROPERTY)
							|| feature
									.equals(SmooksModelUtils.ATTRIBUTE_SELECTOR))
						continue;
					String pname = feature.getName();
					String pvalue = it.get(feature, false).toString();
					PropertyModel pmodel = new PropertyModel();
					pmodel.setName(pname);
					pmodel.setValue(pvalue);
					model.getProperties().add(pmodel);
				}
				mappingModelList.add(model);
			}
		}
	}

	protected JavaBeanModel findModelWithSelectorString(String selector,
			JavaBeanModel parentModel) {
		String[] s = selector.trim().split(" ");
		String pname = parentModel.getName();
		Class clazz = parentModel.getBeanClass();
		JavaBeanModel current = parentModel;
		if (clazz != null)
			pname = clazz.getName();
		if (s != null) {
			for (int i = 0; i < s.length; i++) {
				String p = s[i];
				if (p.equals(pname))
					continue;
				JavaBeanModel child = findTheChildJavaBeanModel(p, current);
				if (child == null)
					return null;
				current = child;
			}
			return current;
		}
		return null;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile,
			ClassLoader classLoader) throws InvocationTargetException {
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
				if(resourceType == null) continue;
				String resource = resourceType.getValue();
				if (BEANPOPULATOR.equals(resource)) {
					// create root beanmodel
					rootClassName = rc.getSelector();
					current = rc;
					break;
				}
			}
		}

		if (rootClassName == null)
			return null;

		boolean isWarning = false;
		boolean isError = false;
		Class clazz = null;
		try {
			if (classLoader == null) {
				IProject project = sourceFile.getProject();
				classLoader = new ProjectClassLoader(JavaCore.create(project));
			}

			clazz = classLoader.loadClass(rootClassName);
		} catch (Exception e) {
			// ignore
		}
		JavaBeanModel model = null;
		if (clazz != null) {
			model = JavaBeanModelFactory.getJavaBeanModelWithLazyLoad(clazz);
		} else {
			model = new JavaBeanModel(null, rootClassName);
			model.setRootClassModel(true);
			model.setError("Can't find the class : " + rootClassName);
			model.setProperties(new ArrayList());
			isError = true;
		}
		if (model != null) {
			this.setSelectorIsUsed(rootClassName);
			buildSourceInputProperties(listType, model, false, isError,
					current, classLoader);
		}
		return model;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		return this.buildSourceInputObjects(graphInfo, listType, sourceFile,
				null);
	}

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
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
				if(resourceType == null) continue;
				String resource = resourceType.getValue();
				if (BEANPOPULATOR.equals(resource)) {
					// create root beanmodel
					current = rc;
					break;
				}
			}
		}
		Class rootClass = null;
		rootClassName = SmooksModelUtils.getParmaText("beanClass", current);
		if (rootClassName != null && loader != null) {
			try {
				rootClass = loader.loadClass(rootClassName);
			} catch (ClassNotFoundException e) {
				// ignore
			}
		}
		boolean rootIsError = false;
		JavaBeanModel rootModel = null;
		if (rootClass != null) {
			rootModel = JavaBeanModelFactory
					.getJavaBeanModelWithLazyLoad(rootClass);
			rootIsError = true;
		} else {
			rootModel = new JavaBeanModel(null, rootClassName);
		}
		setSelectorIsUsed(rootClassName);
		buildChildrenOfTargetInputModel(listType, rootModel, false,
				rootIsError, current, loader);
		return rootModel;
	}

	protected void buildChildrenOfTargetInputModel(
			SmooksResourceListType listType, JavaBeanModel beanModel,
			boolean rootIsWarning, boolean rootIsError,
			ResourceConfigType currentResourceConfigType,
			ClassLoader classLoader) {
		if (currentResourceConfigType != null) {
			List bindingList = getBindingListFromResourceConfigType(currentResourceConfigType);
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
				// if(property == null){
				// continue;
				// }
				processBindingPropertyFromTargetModel(listType, property,
						selector, beanModel, classLoader);
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
			model = new JavaBeanModel(null, property);
			parentModel.addProperty(model);
			model.setError("don't exist");
		}

		if (selector.startsWith("${") && selector.endsWith("}")) {
			selector = selector.substring(2, selector.length() - 1);
			ResourceConfigType resourceConfig = findResourceCinfigTypeWithSelector(
					selector, listType);
			if (resourceConfig != null) {
				this.buildChildrenOfTargetInputModel(listType, model, false,
						false, resourceConfig, classLoader);
			}
		}
	}

	protected List getBindingListFromResourceConfigType(
			ResourceConfigType resourceConfig) {
		List paramList = resourceConfig.getParam();
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType param = (ParamType) iterator.next();
			if ("bindings".equals(param.getName())) {
				if (param.eContents().isEmpty())
					continue;
				List bindingList = (List) param.getMixed().get(
						SmooksModelUtils.ELEMENT_BINDING, false);
				return bindingList;
			}
		}
		return null;
	}

	protected void buildSourceInputProperties(SmooksResourceListType listType,
			JavaBeanModel beanModel, boolean rootIsWarning,
			boolean rootIsError, ResourceConfigType currentResourceConfigType,
			ClassLoader classLoader) {
		if (currentResourceConfigType != null) {
			List bindingList = this
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

	protected ResourceConfigType findResourceCinfigTypeWithSelector(
			String selector, SmooksResourceListType listType) {
		if (isReferenceSelector(selector)) {
			selector = this.getBeanIdWithRawSelectorString(selector);
		}
		List rl = listType.getAbstractResourceConfig();
		ResourceConfigType resourceConfig = null;
		for (Iterator iterator = rl.iterator(); iterator.hasNext();) {
			ResourceConfigType rct = (ResourceConfigType) iterator.next();
			if (this.isSelectorIsUsed(rct.getSelector()))
				continue;
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
		if (selector.startsWith("${") && selector.endsWith("}")) {
			// should get the bean properties
			// memory out???
			currentModel.getProperties();
			selector = this.getBeanIdWithRawSelectorString(selector);
			ResourceConfigType resourceConfig = findResourceCinfigTypeWithSelector(
					selector, listType);
			if (resourceConfig != null) {
				String referenceSelector = resourceConfig.getSelector();
				JavaBeanModel model = findTheChildJavaBeanModel(
						referenceSelector, currentModel);
				// try to test the selector is can be loaded by classloader??
				Class clazz = null;
				try {
					if (classLoader != null) {
						clazz = classLoader.loadClass(referenceSelector);
						if (clazz != null && model == null)
							model = JavaBeanModelFactory
									.getJavaBeanModelWithLazyLoad(clazz);
					}
				} catch (Exception e) {
					// ignore
				}
				// something wrong
				if (model == null) {
					model = new JavaBeanModel(null, referenceSelector);
					model.setError("don't exist");
					model.setProperties(new ArrayList());
				}
				if (currentModel.getError() != null) {
					currentModel.addProperty(model);
				}
				buildSourceInputProperties(listType, model, false, true,
						resourceConfig, classLoader);
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
							pm.setError("don't exist");
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
			if ("beanId".equals(p.getName())) {
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
	protected JavaBeanModel findTheChildJavaBeanModel(String name,
			JavaBeanModel parentModel) {
		List list = parentModel.getProperties();
		if (list == null)
			return null;
		if (parentModel.isList() || parentModel.isArray()) {
			if (list.size() >= 1) {
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
}

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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.analyzer.CompositeResolveCommand;
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
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
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

	public static final String BEANPOPULATOR = "org.milyn.javabean.BeanPopulator";

	public static final String PRO_CLASS_NAME = "__pro_class_name_";

	public static final Object PRO_PROJECT_NAME = "__pro_project_name_";

	public static final String SPACE_STRING = " ";

	private static final int TARGET_DATA = 1;

	private static final int SOURCE_DATA = 0;

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
					resourceList.add(resourceConfig);
					// Command addResourceConfigCommand = AddCommand
					// .create(
					// editingDomain,
					// resourceList,
					// SmooksPackage.eINSTANCE
					// .getSmooksResourceListType_AbstractResourceConfig(),
					// resourceConfig);
					// addResourceConfigCommand.execute();
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
							List<Object> targetConnectionModelList = ((IConnectableModel) child)
									.getModelTargetConnections();
							for (Iterator iterator3 = targetConnectionModelList
									.iterator(); iterator3.hasNext();) {
								LineConnectionModel childConnection = (LineConnectionModel) iterator3
										.next();
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
										.addBindingTypeToParamType(
												bindingsParam, jbean.getName(),
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
									analyzeStructuredDataModel(
											resourceList,
											root,
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
		if (source.getBeanClass().isArray()) {
			return source.getName();
		}
		if (Collection.class.isAssignableFrom(source.getBeanClass())) {
			return source.getName();
		}
		return source.getBeanClassString();

	}

	private DesignTimeAnalyzeResult[] checkOtherNodeConnected(
			SmooksConfigurationFileGenerateContext context) {
		GraphRootModel root = context.getGraphicalRootModel();
		List sourceList = root.loadSourceModelList();
		List targetList = root.loadTargetModelList();
		List<DesignTimeAnalyzeResult> arList = new ArrayList<DesignTimeAnalyzeResult>();
		for (Iterator iterator = targetList.iterator(); iterator.hasNext();) {
			AbstractStructuredDataModel targetm = (AbstractStructuredDataModel) iterator
					.next();
			if (targetm instanceof IConnectableModel) {
				if (((IConnectableModel) targetm).getModelTargetConnections()
						.isEmpty()) {
					continue;
				}

				JavaBeanModel javaModel = (JavaBeanModel) targetm
						.getReferenceEntityModel();
				JavaBeanModel parent = javaModel.getParent();
				if (parent != null) {
					AbstractStructuredDataModel pgm = UIUtils.findGraphModel(
							root, parent);
					if (pgm != null && pgm instanceof IConnectableModel) {
						if (((IConnectableModel) pgm)
								.getModelTargetConnections().isEmpty()) {
							String errorMessage = "The parent of Java node \""
									+ javaModel.getName()
									+ "\" : \""
									+ parent.getName()
									+ "\" doesn't be connected by any source node";
							DesignTimeAnalyzeResult dr = new DesignTimeAnalyzeResult();
							dr.setErrorMessage(errorMessage);
							createResolveCommand(dr, context, javaModel, parent);
							arList.add(dr);
						}
					}
				}
			}
		}
		return arList.toArray(new DesignTimeAnalyzeResult[0]);
	}

	private void createResolveCommand(DesignTimeAnalyzeResult result,
			SmooksConfigurationFileGenerateContext context,
			JavaBeanModel currentNode, JavaBeanModel parentNode) {
		GraphRootModel root = context.getGraphicalRootModel();
		HashMap<AbstractStructuredDataModel, AbstractStructuredDataModel> tempMap = new HashMap<AbstractStructuredDataModel, AbstractStructuredDataModel>();
		// Disconnect all connections command
		Java2JavaResolveCommand disconnectCommand = new Java2JavaResolveCommand(
				context);
		CompositeResolveCommand compositeCommand = new CompositeResolveCommand(
				context);
		compositeCommand.setResolveDescription("Connect all needed connections");
		disconnectCommand
				.setResolveDescription("Disconnect all connections of the current \""
						+ currentNode.getName() + "\"node");
		AbstractStructuredDataModel targetNode = UIUtils.findGraphModel(root,
				currentNode);
		if (targetNode instanceof IConnectableModel) {
			List<Object> connections = ((IConnectableModel) targetNode)
					.getModelTargetConnections();
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				LineConnectionModel line = (LineConnectionModel) iterator
						.next();
				AbstractStructuredDataModel source = (AbstractStructuredDataModel) line
						.getSource();
				JavaBeanModel sourceBean = (JavaBeanModel) source
						.getReferenceEntityModel();
				JavaBeanModel sourceParent = sourceBean.getParent();
				if (sourceParent == null) {
					sourceParent = sourceBean;
				}
				AbstractStructuredDataModel sourceParentNode = UIUtils
						.findGraphModel(root, sourceParent);
				// Connect the parent command
				AbstractStructuredDataModel targetParentNode = UIUtils
						.findGraphModel(root, parentNode);
				if (tempMap.get(sourceParentNode) == null) {
					Java2JavaResolveCommand connectParent = new Java2JavaResolveCommand(
							context);
					connectParent.setResolveDescription("Connect the \""
							+ sourceParent.getName() + "\" to the \""
							+ parentNode.getName() + "\"");
					connectParent.setSourceModel(sourceParentNode);
					connectParent.setTargetModel(targetParentNode);
					result.addResolveCommand(connectParent);
					tempMap.put(sourceParentNode,targetParentNode);
					compositeCommand.addCommand(connectParent);
				}

				disconnectCommand.addDisconnectionModel(line);
			}
		}
		result.addResolveCommand(disconnectCommand);
		if (!compositeCommand.isEmpty()) {
			result.addResolveCommand(compositeCommand);
		}
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
										"Connection Question",
										"The root models don't be connected , it will make some errors with the generation config file contents.\nDo you wan to connect them?");
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
			if (!((List) sourceObject).isEmpty()) {
				sourceObject = (JavaBeanModel) ((List) sourceObject).get(0);
			}
		}
		if (targetObject instanceof List) {
			if (!((List) targetObject).isEmpty()) {
				targetObject = (JavaBeanModel) ((List) targetObject).get(0);
			}
		}
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
					if (targetName != null
							&& targetName.trim().equals(beanClass)) {
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
						.findResourceConfigTypeWithSelector(selector,
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

	private String getDataSourceClass(GraphInformations info, int dataMode) {
		String key = "sourceDataPath";
		if (dataMode == SOURCE_DATA) {
			key = "sourceDataPath";
		}
		if (dataMode == TARGET_DATA) {
			key = "targetDataPath";
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
		for (Iterator iterator = resourceConfigList.iterator(); iterator
				.hasNext();) {
			AbstractResourceConfig ar = (AbstractResourceConfig) iterator
					.next();
			if (ar instanceof ResourceConfigType) {
				ResourceConfigType rc = (ResourceConfigType) ar;
				ResourceType resourceType = rc.getResource();
				if (resourceType == null)
					continue;
				String resource = resourceType.getValue();
				if (BEANPOPULATOR.equals(resource)) {
					// create root beanmodel
					rootClassName = rc.getSelector();
					current = rc;
					break;
				}
			}
		}
		if (rootClassName == null) {
			rootClassName = this.getDataSourceClass(graphInfo, SOURCE_DATA);
		}
		if (rootClassName == null) {
			return null;
		}

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
		List list = new ArrayList();
		if (model != null)
			list.add(model);
		return list;
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
				if (resourceType == null)
					continue;
				String resource = resourceType.getValue();
				if (BEANPOPULATOR.equals(resource)) {
					// create root beanmodel
					current = rc;
					break;
				}
			}
		}
		if (current == null) {
			rootClassName = this.getDataSourceClass(graphInfo, TARGET_DATA);
		}
		// if can't load the source from GraphicalInformation , return NULL
		if (current == null && rootClassName == null)
			return null;
		Class rootClass = null;
		if (rootClassName == null) {
			rootClassName = SmooksModelUtils.getParmaText("beanClass", current);
		}
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
		List list = new ArrayList();
		list.add(rootModel);
		return list;
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

		if (isReferenceSelector(selector)) {
			selector = selector.substring(2, selector.length() - 1);
			ResourceConfigType resourceConfig = findResourceConfigTypeWithSelector(
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

	protected ResourceConfigType findResourceConfigTypeWithSelector(
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
			ResourceConfigType resourceConfig = findResourceConfigTypeWithSelector(
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
		return checkOtherNodeConnected(context);
	}
}

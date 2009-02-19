/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.SelectorAttributes;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart
 * 
 */
public class BeanPopulatorMappingAnalyzer implements IMappingAnalyzer {
	public static final String PRO_BINDING_TYPE = "bindingType";

	public static final String PRO_REFERENCE_RESOURCE_CONFIG = "reference_resourceConfig";

	public static final String PRO_SELECTOR_ATTRIBUTES = "__pro_selector_attributes";

	public static final String BEAN_CREATION = "beanCreation";

	public static final String PROPERTY_BINDING = "propertyBinding";

	public static final String REFERENCE_BINDING = "referenceBinding";

	public static final String[] SELECTOR_SPERATORS = new String[] { " ", "/"};


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.analyzer.IMappingAnalyzer#analyzeGraphModel(org
	 * .jboss
	 * .tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public DesignTimeAnalyzeResult[] analyzeGraphModel(
			SmooksConfigurationFileGenerateContext context) {
		return null;
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

	}

	protected boolean checkSourceAndTarget(Object sourceObject,
			Object targetObject) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.analyzer.IMappingAnalyzer#analyzeMappingSmooksModel
	 * (org.jboss.tools.smooks.model.SmooksResourceListType, java.lang.Object,
	 * java.lang.Object)
	 */
	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType, Object sourceObject,
			Object targetObject) {
		MappingResourceConfigList mappingList = new MappingResourceConfigList();

		if (!checkSourceAndTarget(sourceObject, targetObject)) {
			return mappingList;
		}

		Object sourceModel = sourceObject;

		JavaBeanList targetModel = (JavaBeanList) targetObject;

		List<AbstractResourceConfig> resourceList = listType
				.getAbstractResourceConfig();
		for (Iterator<AbstractResourceConfig> iterator = resourceList
				.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator
					.next();
			if (abstractResourceConfig instanceof ResourceConfigType) {
				ResourceConfigType resourceConfig = (ResourceConfigType) abstractResourceConfig;
				ResourceType resource = resourceConfig.getResource();
				if (resource != null) {
					String resourceValue = resource.getStringValue();
					if (resourceValue == null)
						continue;

					if (!SmooksModelConstants.BEAN_POPULATOR
							.equals(resourceValue))
						continue;

					String selector = resourceConfig.getSelector();
					if (selector == null)
						continue;
					selector = selector.trim();
					IXMLStructuredObject sourceNode = null;
					if (sourceModel instanceof IXMLStructuredObject) {
						sourceNode = UIUtils.localXMLNodeWithPath(selector,
								(IXMLStructuredObject) sourceModel);
					}
					if (sourceNode == null) {
						throw new RuntimeException(
								"Can't find the node from resource-config selector string : "
										+ selector);
					}

					String bindingClass = SmooksModelUtils.getParmaText(
							SmooksModelConstants.BEAN_CLASS, resourceConfig);
					if (bindingClass == null) {
						continue;
					}

					bindingClass = bindingClass.trim();
					JavaBeanModel targetNode = findJavaBeanFrombeanList(
							bindingClass, targetModel, mappingList, false);
					if (targetNode == null) {
						throw new RuntimeException(
								"Can't find the class node : " + bindingClass);
					}
					SelectorAttributes selectorAttributes = UIUtils
							.guessSelectorProperty(selector, sourceNode);
					MappingModel mapping = new MappingModel(sourceNode,
							targetNode);
					PropertyModel bindingProperty = new PropertyModel();
					bindingProperty.setName(PRO_BINDING_TYPE);
					bindingProperty.setValue(BEAN_CREATION);
					mapping.getProperties().add(bindingProperty);

					PropertyModel referenceProperty = new PropertyModel(
							PRO_REFERENCE_RESOURCE_CONFIG, resourceConfig);
					mapping.getProperties().add(referenceProperty);
					mapping.getProperties().add(
							new PropertyModel(PRO_SELECTOR_ATTRIBUTES,
									selectorAttributes));
					mappingList.getMappingModelList().add(mapping);
					mappingList.addResourceConfig(resourceConfig);

					createPropertyConnection(listType, mappingList,
							resourceConfig, sourceNode, targetNode);
				}
			}
		}
		return mappingList;
	}

	public static boolean isReferenceSelector(String selector) {
		if (selector == null)
			return false;
		if (selector.startsWith("${") && selector.endsWith("}"))
			return true;
		return false;
	}

	protected void createPropertyConnection(SmooksResourceListType listType,
			MappingResourceConfigList mappingList,
			ResourceConfigType resourceConfig,
			IXMLStructuredObject sourceModel, JavaBeanModel targetNode) {
		List<Object> bindingList = SmooksModelUtils
				.getBindingListFromResourceConfigType(resourceConfig);
		if (bindingList != null) {
			for (Iterator<Object> iterator = bindingList.iterator(); iterator
					.hasNext();) {
				Object object = (Object) iterator.next();
				if (!(object instanceof AnyType))
					continue;
				AnyType binding = (AnyType) object;
				String property = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_PROPERTY);
				if (property != null) {
					property = property.trim();
				}
				String selector = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_SELECTOR);
				if (selector != null) {
					selector = selector.trim();
				}
				boolean isReferenceBinding = false;
				IXMLStructuredObject childSourceNode = null;
				if (isReferenceSelector(selector)) {
					ResourceConfigType referenceConfig = findResourceConfigTypeWithBeanId(
							selector, listType);
					if (referenceConfig == null)
						continue;
					String beanClass = SmooksModelUtils.getParmaText(
							SmooksModelConstants.BEAN_CLASS, referenceConfig);
					if (beanClass == null)
						continue;
					beanClass = beanClass.trim();
					childSourceNode = findJavaBeanFrombeanList(beanClass,
							(JavaBeanList) targetNode.getParent(), mappingList,
							true);
					if (childSourceNode == null) {
						throw new RuntimeException("Can't find the class "
								+ beanClass);
					}
					isReferenceBinding = true;
				}

				JavaBeanModel childTargetNode = findTheChildJavaBeanModel(
						property, targetNode);
				if (childTargetNode == null) {
					throw new RuntimeException("Can't find the property "
							+ property + " in the " + targetNode.getName()
							+ " node");
				}
				if (childSourceNode == null) {
					childSourceNode = UIUtils.localXMLNodeWithPath(selector,
							sourceModel);
				}

				if (childSourceNode == null) {
					throw new RuntimeException(
							"Can't find the node from path : " + selector);
				}

				MappingModel mapping = null;
				if (isReferenceBinding) {
					mapping = new MappingModel(childTargetNode, childSourceNode);
				} else {
					mapping = new MappingModel(childSourceNode, childTargetNode);
					SelectorAttributes selectorAttributes = UIUtils
							.guessSelectorProperty(selector, childSourceNode);
					mapping.getProperties().add(
							new PropertyModel(PRO_SELECTOR_ATTRIBUTES,
									selectorAttributes));
				}
				UIUtils.assignBindingPropertyToMappingModel(binding, mapping,
						new Object[] { SmooksModelUtils.ATTRIBUTE_PROPERTY,
								SmooksModelUtils.ATTRIBUTE_SELECTOR });
				PropertyModel bindingProperty = new PropertyModel();
				bindingProperty.setName(PRO_BINDING_TYPE);
				if (!isReferenceBinding) {
					bindingProperty.setValue(PROPERTY_BINDING);
				} else {
					bindingProperty.setValue(REFERENCE_BINDING);
				}
				mapping.getProperties().add(
						new PropertyModel(PRO_REFERENCE_RESOURCE_CONFIG,
								resourceConfig));
				mapping.getProperties().add(bindingProperty);
				mappingList.getMappingModelList().add(mapping);

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
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			
			if(!(abstractResourceConfig instanceof ResourceConfigType)) continue;
			
			ResourceConfigType rct = (ResourceConfigType) abstractResourceConfig;
			// if (this.isSelectorIsUsed(rct.getSelector()))
			// continue;
			String beanId = getBeanIDFromParam(rct);
			if (selector.equals(beanId)) {
				resourceConfig = rct;
				break;
			}
			String selector1 = rct.getSelector();
			if (selector1 != null)
				selector1 = selector1.trim();
			if (selector.equals(selector1)) {
				resourceConfig = rct;
				break;
			}
		}
		return resourceConfig;
	}

	protected String getBeanIdWithRawSelectorString(String selector) {
		selector = selector.substring(2, selector.length() - 1);
		return selector;
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

	protected JavaBeanModel findJavaBeanFrombeanList(String className,
			JavaBeanList list, MappingResourceConfigList mappingList,
			boolean ignoreMultipleConnection) {
		List children = list.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			JavaBeanModel javaBean = (JavaBeanModel) iterator.next();
			if (className.equals(javaBean.getBeanClassString())) {
				// If the node has bean connected
				if (!ignoreMultipleConnection) {
					if (isHasBeenConnected(mappingList, javaBean)) {
						continue;
					}
				}
				return javaBean;
			}
		}
		return null;
	}

	private boolean isHasBeenConnected(MappingResourceConfigList mappingList,
			JavaBeanModel javaBean) {
		List<MappingModel> list = mappingList.getMappingModelList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MappingModel mappingModel = (MappingModel) iterator.next();
			if (mappingModel.getTarget() == javaBean) {
				if (BEAN_CREATION.equals(mappingModel
						.getPropertyValue(PRO_BINDING_TYPE))) {
					return true;
				}
			}
		}
		return false;
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
			if (list.size() >= 1) {
				JavaBeanModel m = (JavaBeanModel) list.get(0);
				return m;
			}
		}

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			JavaBeanModel child = (JavaBeanModel) iterator.next();
			if (name.equals(child.getSelectorString())) {
				return child;
			}
		}
		return null;
	}

}

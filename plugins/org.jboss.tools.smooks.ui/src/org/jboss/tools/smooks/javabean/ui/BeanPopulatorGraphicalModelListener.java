/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.internal.ui.preferences.PropertyAndPreferencePage;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.SelectorAttributes;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.editors.SmooksFormEditor;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart
 * 
 */
public class BeanPopulatorGraphicalModelListener implements
		IGraphicalModelListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener#modelAdded
	 * (java.lang.Object,
	 * org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext
	 * )
	 */
	public void modelAdded(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context) {
		if (graphicalModel instanceof LineConnectionModel) {
			if (isBeanCreationConnection((LineConnectionModel) graphicalModel)) {
				addNewResourceConfig((LineConnectionModel) graphicalModel,
						context);
				return;
			}

			if (isPropertyBindingConnection((LineConnectionModel) graphicalModel)) {
				addNewPropertyBinding((LineConnectionModel) graphicalModel,
						context);
				return;
			}

			if (isReferenceBindingConnection((LineConnectionModel) graphicalModel)) {
				addNewReferenceBinding((LineConnectionModel) graphicalModel,
						context);
				return;
			}
		}
	}

	protected void addNewReferenceBinding(LineConnectionModel line,
			SmooksConfigurationFileGenerateContext context) {
		Object source = ((AbstractStructuredDataModel) line.getSource())
				.getReferenceEntityModel();
		if (!(source instanceof JavaBeanModel)) {
			return;
		}

		Object target = ((AbstractStructuredDataModel) line.getTarget())
				.getReferenceEntityModel();
		if (!(target instanceof JavaBeanModel)) {
			return;
		}
		JavaBeanModel sourceParent = ((JavaBeanModel) source).getParent();
		IConnectableModel sourceParentGraph = (IConnectableModel) UIUtils
				.findGraphModel(context.getGraphicalRootModel(), sourceParent);
		IConnectableModel targetGraph = (IConnectableModel) UIUtils
				.findGraphModel(context.getGraphicalRootModel(), target);

		if (sourceParentGraph == null || targetGraph == null)
			return;
		List sourceParentConnections = sourceParentGraph
				.getModelTargetConnections();
		List targetConnections = targetGraph.getModelTargetConnections();
		if (sourceParentConnections.size() <= 0
				|| sourceParentConnections.size() > 2) {
			return;
		}
		if (targetConnections.size() <= 0 || targetConnections.size() > 2) {
			return;
		}
		ResourceConfigType hostResourceConfig = null;
		ResourceConfigType referenceResourceConfig = null;
		for (Iterator iterator = sourceParentConnections.iterator(); iterator
				.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			if (isBeanCreationConnection(connection)) {
				ResourceConfigType re = getResourceConfig(connection);
				if (re != null)
					hostResourceConfig = re;
				break;
			}
		}

		for (Iterator iterator = targetConnections.iterator(); iterator
				.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			if (isBeanCreationConnection(connection)) {
				ResourceConfigType re = getResourceConfig(connection);
				if (re != null)
					referenceResourceConfig = re;
				break;
			}
		}
		if (hostResourceConfig != null) {
			setReferenceResourceConfig(hostResourceConfig, line);
			if (referenceResourceConfig != null) {
				String beanId = SmooksModelUtils.getParmaText(
						SmooksModelConstants.BEAN_ID, referenceResourceConfig);
				if (beanId == null) {
					return;
				}
				beanId = beanId.trim();
				String propertyName = ((JavaBeanModel) source).getName();
				if (((JavaBeanModel) sourceParent).isArray()
						|| ((JavaBeanModel) sourceParent).isList()) {
					propertyName = null;
				}
				String selector = "${" + beanId + "}";
				addBindingToParamType(hostResourceConfig, propertyName,
						selector, line);
			}
		}
	}

	private void setReferenceResourceConfig(ResourceConfigType resourceConfig,
			LineConnectionModel connection) {
		connection.addPropertyModel(
				BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG,
				resourceConfig);
	}

	private ResourceConfigType getResourceConfig(LineConnectionModel line) {
		Object obj = line
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG);
		if (obj != null && obj instanceof ResourceConfigType) {
			return (ResourceConfigType) obj;
		}
		return null;
	}

	protected void addNewPropertyBinding(LineConnectionModel graphicalModel,
			SmooksConfigurationFileGenerateContext context) {
		JavaBeanModel target = (JavaBeanModel) ((AbstractStructuredDataModel) graphicalModel
				.getTarget()).getReferenceEntityModel();
		ResourceConfigType reference = findReferenceResourceConfig(context
				.getGraphicalRootModel(), target);
		if (reference != null) {
			addPropertyBinding(reference, graphicalModel);
		}
	}

	private ResourceConfigType findReferenceResourceConfig(GraphRootModel root,
			JavaBeanModel target) {
		JavaBeanModel javaBeanModel = target.getParent();
		if (javaBeanModel == null || javaBeanModel instanceof JavaBeanList) {
			return null;
		}
		AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(root,
				javaBeanModel);
		if (graphModel != null && graphModel instanceof IConnectableModel) {
			List connections = ((IConnectableModel) graphModel)
					.getModelTargetConnections();
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				LineConnectionModel connection = (LineConnectionModel) iterator
						.next();
				if (isBeanCreationConnection(connection)) {
					Object obj = connection
							.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG);
					if (obj == null || !(obj instanceof ResourceConfigType))
						return null;
					return (ResourceConfigType) obj;
				}
			}
		}
		return null;
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

	protected IXMLStructuredObject ignoreRootParentNode(
			IXMLStructuredObject node) {
		IXMLStructuredObject parent = node.getParent();
		if (parent == null || parent.isRootNode())
			return node;
		IXMLStructuredObject temp = parent;
		while (temp != null && !temp.isRootNode()) {
			temp = temp.getParent();
			if (temp == null || temp.isRootNode()) {
				return parent;
			}
			parent = temp;
		}
		return parent;
	}

	protected IXMLStructuredObject rootParentNode(IXMLStructuredObject node) {
		return UIUtils.getRootParent(node);
	}

	protected IXMLStructuredObject parentNode(IXMLStructuredObject node) {
		IXMLStructuredObject parent = node.getParent();
		if (parent != null)
			return parent;
		return node;
	}

	private void addPropertyBinding(ResourceConfigType resourceConfig,
			LineConnectionModel line) {
		JavaBeanModel target = (JavaBeanModel) ((AbstractStructuredDataModel) line
				.getTarget()).getReferenceEntityModel();
		Object source = ((AbstractStructuredDataModel) line.getSource())
				.getReferenceEntityModel();
		String propertyName = target.getName();
		String currentSelectorName = null;
		if (source instanceof IXMLStructuredObject) {
			currentSelectorName = UIUtils.generateFullPath(
					(IXMLStructuredObject) source, " ");
		}
		if (currentSelectorName == null)
			return;
		addBindingToParamType(resourceConfig, propertyName,
				currentSelectorName, line);
	}

	private void addBindingToParamType(ResourceConfigType resourceConfig,
			String propertyName, String currentSelectorName,
			LineConnectionModel line) {
		ParamType bindingsParam = getBindingsParamType(resourceConfig);
		if (bindingsParam == null) {
			bindingsParam = SmooksFactory.eINSTANCE.createParamType();
			bindingsParam.setName(SmooksModelConstants.BINDINGS);
			resourceConfig.getParam().add(bindingsParam);
		}
		AnyType binding = SmooksModelUtils.addBindingTypeToParamType(
				bindingsParam, propertyName, currentSelectorName, null, null);
		UIUtils
				.assignConnectionPropertyToBinding(
						line,
						binding,
						new String[] {
								"property", "selector", BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE, BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG }); //$NON-NLS-1$ //$NON-NLS-2$
		if (!this.isReferenceBindingConnection(line)) {
			PropertyModel bindingType = new PropertyModel(
					BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG,
					resourceConfig);
			line.getProperties().add(bindingType);
			SelectorAttributes sa = new SelectorAttributes();
			sa.setSelectorPolicy(SelectorAttributes.FULL_PATH);
			sa.setSelectorSperator(" ");
			PropertyModel selectorProperty = new PropertyModel(
					BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES, sa);
			line.getProperties().add(selectorProperty);
		}
	}

	protected void removeResourceConfigAssociatedConnections(
			JavaBeanModel parent, GraphRootModel root,
			ResourceConfigType referenceResourceConfig) {
		IConnectableModel parentGraphModel = (IConnectableModel) UIUtils
				.findGraphModel(root, parent);
		List targetConnections = parentGraphModel.getModelTargetConnections();
		List temp1 = new ArrayList(targetConnections);
		for (Iterator iterator = temp1.iterator(); iterator.hasNext();) {
			LineConnectionModel targetConnection = (LineConnectionModel) iterator
					.next();
			if (isReferenceBindingConnection(targetConnection)) {
				targetConnection.disConnect();
			}
		}
		temp1.clear();
		temp1 = null;

		List<IXMLStructuredObject> children = parent.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator
					.next();
			IConnectableModel childTarget = (IConnectableModel) UIUtils
					.findGraphModel(root, structuredObject);
			if (childTarget == null)
				continue;
			List connections1 = childTarget.getModelTargetConnections();
			List temp = new ArrayList(connections1);
			for (Iterator iterator2 = temp.iterator(); iterator2.hasNext();) {
				LineConnectionModel connection1 = (LineConnectionModel) iterator2
						.next();
				if (referenceResourceConfig == connection1
						.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG)) {
					connection1.disConnect();
				}
			}
			temp.clear();

			List connections2 = childTarget.getModelSourceConnections();
			temp = new ArrayList(connections2);
			for (Iterator iterator2 = temp.iterator(); iterator2.hasNext();) {
				LineConnectionModel connection2 = (LineConnectionModel) iterator2
						.next();
				if (referenceResourceConfig == connection2
						.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG)) {
					connection2.disConnect();
				}
			}
			temp.clear();
			temp = null;
		}
	}

	protected void removeResourceConfig(LineConnectionModel line,
			SmooksConfigurationFileGenerateContext context) {
		Object value = line
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG);
		if (value == null || !(value instanceof ResourceConfigType))
			return;
		ResourceConfigType resourceConfig = (ResourceConfigType) value;
		SmooksResourceListType listType = context.getSmooksResourceListModel();
		listType.getAbstractResourceConfig().remove(resourceConfig);
		GraphRootModel graphRoot = context.getGraphicalRootModel();
		AbstractStructuredDataModel transformData = (AbstractStructuredDataModel) line
				.getTarget();
		JavaBeanModel targetModel = (JavaBeanModel) transformData
				.getReferenceEntityModel();
		removeResourceConfigAssociatedConnections(targetModel, graphRoot,
				resourceConfig);
	}

	protected void addNewResourceConfig(LineConnectionModel graphicalModel,
			SmooksConfigurationFileGenerateContext context) {
		ResourceConfigType resourceConfig = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		configNewResourceConfig(resourceConfig,
				(LineConnectionModel) graphicalModel);
		SmooksResourceListType listType = context.getSmooksResourceListModel();
		EditingDomain domain = context.getDomain();
		UIUtils.addResourceConfigType(domain, listType, resourceConfig);
		PropertyModel bindingModel = new PropertyModel(
				BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG,
				resourceConfig);
		SelectorAttributes sa = new SelectorAttributes();
		sa.setSelectorPolicy(SelectorAttributes.FULL_PATH);
		sa.setSelectorSperator(" ");
		PropertyModel selectorProperty = new PropertyModel(
				BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES, sa);
		((LineConnectionModel) graphicalModel).addPropertyModel(bindingModel);
		((LineConnectionModel) graphicalModel)
				.addPropertyModel(selectorProperty);
	}

	private boolean isPropertyBindingConnection(LineConnectionModel connection) {
		return BeanPopulatorMappingAnalyzer.PROPERTY_BINDING.equals(connection
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE));
	}

	private boolean isReferenceBindingConnection(LineConnectionModel connection) {
		return BeanPopulatorMappingAnalyzer.REFERENCE_BINDING.equals(connection
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE));
	}

	private boolean isBeanCreationConnection(LineConnectionModel connection) {
		return BeanPopulatorMappingAnalyzer.BEAN_CREATION.equals(connection
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE));
	}

	protected void configNewResourceConfig(ResourceConfigType resourceConfig,
			LineConnectionModel line) {
		JavaBeanModel target = (JavaBeanModel) ((AbstractStructuredDataModel) line
				.getTarget()).getReferenceEntityModel();
		Object source = ((AbstractStructuredDataModel) line.getSource())
				.getReferenceEntityModel();
		String sourceSelector = null;
		if (source instanceof IXMLStructuredObject) {
			sourceSelector = UIUtils.generateFullPath(
					(IXMLStructuredObject) source, " ");
		}
		if (sourceSelector != null) {
			resourceConfig.setSelector(sourceSelector);
		}
		ResourceType resource = SmooksFactory.eINSTANCE.createResourceType();
		resourceConfig.setResource(resource);
		resource.setStringValue(SmooksModelConstants.BEAN_POPULATOR);

		ParamType beanIdParam = SmooksFactory.eINSTANCE.createParamType();
		String beanId = generateBeanId(resourceConfig, target);
		beanIdParam.setName(SmooksModelConstants.BEAN_ID);
		SmooksModelUtils.setTextToAnyType(beanIdParam, beanId);
		resourceConfig.getParam().add(beanIdParam);

		ParamType beanClassParam = SmooksFactory.eINSTANCE.createParamType();
		beanClassParam.setName(SmooksModelConstants.BEAN_CLASS);
		SmooksModelUtils.setTextToAnyType(beanClassParam, target
				.getBeanClassString());
		resourceConfig.getParam().add(beanClassParam);

		ParamType bindingsParam = SmooksFactory.eINSTANCE.createParamType();
		bindingsParam.setName(SmooksModelConstants.BINDINGS);
		resourceConfig.getParam().add(bindingsParam);
	}

	private String generateBeanId(ResourceConfigType resourceConfig,
			JavaBeanModel target) {
		return target.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener#modelChanged
	 * (java.lang.Object,
	 * org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext
	 * )
	 */
	public void modelChanged(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context,
			PropertyChangeEvent event) {
		LineConnectionModel line = null;
		if (graphicalModel instanceof LineConnectionModel) {
			line = (LineConnectionModel) graphicalModel;
		}
		String pm = event.getPropertyName();
		if (pm
				.equals(AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_UPDATE)) {
			Object obj = event.getNewValue();
			if (obj != null && obj instanceof PropertyModel && line != null) {
				String name = ((PropertyModel) obj).getName();
				if (name
						.equals(BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES)) {
					SelectorAttributes sa = (SelectorAttributes) ((PropertyModel) obj)
							.getValue();
					if (sa != null) {
						if (isBeanCreationConnection(line)) {
							modifyResourceConfigSelector(line, sa);
						}
						if (isPropertyBindingConnection(line)) {
							modifyPropertyBindingSelector(line, sa);
						}
					}
				}
			}
		}
	}

	protected void modifyResourceConfigSelector(LineConnectionModel line,
			SelectorAttributes sa) {
		ResourceConfigType resourceConfig = getResourceConfig(line);
		if (resourceConfig != null) {
			AbstractStructuredDataModel source = (AbstractStructuredDataModel) line
					.getSource();
			Object s = source.getReferenceEntityModel();
			if (s instanceof IXMLStructuredObject) {
				String selector = UIUtils.generatePath(
						(IXMLStructuredObject) s, sa);
				if (selector != null) {
					resourceConfig.setSelector(selector);
					return;
				}
			}
		}
	}

	protected void modifyPropertyBindingSelector(LineConnectionModel line,
			SelectorAttributes sa) {
		ResourceConfigType resourceConfig = getResourceConfig(line);
		if (resourceConfig != null) {
			AbstractStructuredDataModel source = (AbstractStructuredDataModel) line
					.getSource();
			AbstractStructuredDataModel target = (AbstractStructuredDataModel) line
					.getTarget();
			Object t = target.getReferenceEntityModel();
			JavaBeanModel targetModel = null;
			if (t instanceof JavaBeanModel) {
				targetModel = (JavaBeanModel) t;
			}
			if (targetModel == null)
				return;
			Object s = source.getReferenceEntityModel();
			if (s instanceof IXMLStructuredObject) {
				String selector = UIUtils.generatePath(
						(IXMLStructuredObject) s, sa);
				if (selector != null) {
					String pro = targetModel.getName();
					AnyType binding = SmooksModelUtils.getBindingViaProperty(
							resourceConfig, pro);
					if (binding == null)
						return;
					SmooksModelUtils.setPropertyValueToAnyType(selector,
							SmooksModelUtils.ATTRIBUTE_SELECTOR, binding);
					return;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener#modelRemoved
	 * (java.lang.Object,
	 * org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext
	 * )
	 */
	public void modelRemoved(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context) {
		if (graphicalModel instanceof LineConnectionModel) {
			LineConnectionModel line = ((LineConnectionModel) graphicalModel);
			if (isBeanCreationConnection(line)) {
				removeResourceConfig(line, context);
				return;
			}
			if (isPropertyBindingConnection(line)) {
				removePropertyBinding(line, context);
				return;
			}

			if (isReferenceBindingConnection(line)) {
				removeReferenceBinding(line, context);
				return;
			}
		}
	}

	protected void removeReferenceBinding(LineConnectionModel line,
			SmooksConfigurationFileGenerateContext context) {
		Object value = line
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG);
		if (value == null || !(value instanceof ResourceConfigType))
			return;
		ResourceConfigType resourceConfig = (ResourceConfigType) value;
		AbstractStructuredDataModel transformTargetData = (AbstractStructuredDataModel) line
				.getTarget();
		String beanId = getBeanId((IConnectableModel) transformTargetData);
		if (beanId == null)
			return;
		beanId = "${" + beanId + "}";
		AnyType binding = findBindingViaSelector(beanId, resourceConfig);
		ParamType param = getBindingsParamType(resourceConfig);
		if (binding == null || param == null)
			return;
		List<Object> bindingList = (List<Object>) param.getMixed().get(
				SmooksModelUtils.ELEMENT_BINDING, false);
		bindingList.remove(binding);
	}

	private String getBeanId(IConnectableModel targetPoint) {
		List connections = targetPoint.getModelTargetConnections();
		if (connections.size() < 1 || connections.size() > 2)
			return null;
		for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			if (BeanPopulatorMappingAnalyzer.BEAN_CREATION
					.equals(connection
							.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE))) {
				Object obj = connection
						.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG);
				if (obj == null || !(obj instanceof ResourceConfigType)) {

				} else {
					return SmooksModelUtils.getParmaText(
							SmooksModelUtils.BEAN_ID, (ResourceConfigType) obj);
				}
			}
		}
		return null;
	}

	protected void removePropertyBinding(LineConnectionModel line,
			SmooksConfigurationFileGenerateContext context) {
		Object value = line
				.getProperty(BeanPopulatorMappingAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG);
		if (value == null || !(value instanceof ResourceConfigType))
			return;
		ResourceConfigType resourceConfig = (ResourceConfigType) value;
		AbstractStructuredDataModel transformData = (AbstractStructuredDataModel) line
				.getTarget();
		JavaBeanModel targetModel = (JavaBeanModel) transformData
				.getReferenceEntityModel();
		AnyType binding = findBindingViaProperty(targetModel.getName(),
				resourceConfig);
		ParamType param = getBindingsParamType(resourceConfig);
		if (binding == null || param == null)
			return;
		List<Object> bindingList = (List<Object>) param.getMixed().get(
				SmooksModelUtils.ELEMENT_BINDING, false);
		bindingList.remove(binding);
	}

	protected AnyType findBindingViaProperty(String property,
			ResourceConfigType resourceConfig) {
		List bindingList = SmooksModelUtils
				.getBindingListFromResourceConfigType(resourceConfig);
		for (Iterator iterator = bindingList.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (!(object instanceof AnyType))
				continue;
			String value = SmooksModelUtils.getAttributeValueFromAnyType(
					(AnyType) object, SmooksModelUtils.ATTRIBUTE_PROPERTY);
			if (value == null)
				continue;
			value = value.trim();
			if (property.equalsIgnoreCase(value)) {
				return (AnyType) object;
			}
		}
		return null;
	}

	protected AnyType findBindingViaSelector(String selector,
			ResourceConfigType resourceConfig) {
		List bindingList = SmooksModelUtils
				.getBindingListFromResourceConfigType(resourceConfig);
		for (Iterator iterator = bindingList.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (!(object instanceof AnyType))
				continue;
			String value = SmooksModelUtils.getAttributeValueFromAnyType(
					(AnyType) object, SmooksModelUtils.ATTRIBUTE_SELECTOR);
			if (value == null)
				continue;
			value = value.trim();
			if (selector.equalsIgnoreCase(value)) {
				return (AnyType) object;
			}
		}
		return null;
	}

}

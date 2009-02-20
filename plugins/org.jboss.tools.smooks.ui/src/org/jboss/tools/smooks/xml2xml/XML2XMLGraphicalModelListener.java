/**
 * 
 */
package org.jboss.tools.smooks.xml2xml;

import java.beans.PropertyChangeEvent;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.jboss.tools.smooks.javabean.model.SelectorAttributes;
import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;
import org.jboss.tools.smooks.xml.ui.XMLPropertiesSection;

/**
 * @author Dart
 * 
 */
public class XML2XMLGraphicalModelListener implements IGraphicalModelListener {
	public static final String PRO_REFERENCE_RESOURCE_CONFIG = "__reference_resource_config_x2x";

	public static final String PRO_SELECTOR_ATTRIBUTES = BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES;

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
			AbstractStructuredDataModel source = (AbstractStructuredDataModel) ((LineConnectionModel) graphicalModel)
					.getSource();
			AbstractStructuredDataModel target = (AbstractStructuredDataModel) ((LineConnectionModel) graphicalModel)
					.getTarget();
			Object mappingType = ((LineConnectionModel) graphicalModel)
					.getProperty(XMLPropertiesSection.MAPPING_TYPE);
			if (XMLPropertiesSection.BINDING.equals(mappingType)) {
				bindingConnectionAdded(source, target,
						(LineConnectionModel) graphicalModel, context);
			}
			if (XMLPropertiesSection.MAPPING.equals(mappingType)) {
				mappingConnectionAdded(source, target,
						(LineConnectionModel) graphicalModel, context);
			}
		}
	}

	private void mappingConnectionAdded(AbstractStructuredDataModel source,
			AbstractStructuredDataModel target, LineConnectionModel connection,
			SmooksConfigurationFileGenerateContext context) {
		Object obj = source.getReferenceEntityModel();
		IXMLStructuredObject sourceModel = null;
		if (obj instanceof IXMLStructuredObject) {
			sourceModel = (IXMLStructuredObject) obj;
		}
		if (sourceModel == null)
			return;

		SelectorAttributes selectorAttributes = (SelectorAttributes) connection
				.getProperty(PRO_SELECTOR_ATTRIBUTES);
		if (selectorAttributes == null) {
			selectorAttributes = newDefaultSelectorAttribute();
			connection.getProperties().add(
					new PropertyModel(PRO_SELECTOR_ATTRIBUTES,
							selectorAttributes));
		}

		String selector = UIUtils.generatePath(sourceModel, selectorAttributes);
		if (selector == null)
			return;

		ResourceConfigType resourceConfig = newMappingResourceConfig(selector,
				target, context);
		if (resourceConfig != null) {
			PropertyModel property = new PropertyModel();
			property.setName(PRO_REFERENCE_RESOURCE_CONFIG);
			property.setValue(resourceConfig);
			connection.addPropertyModel(property);
		}
	}

	private ResourceConfigType newMappingResourceConfig(String selector,
			AbstractStructuredDataModel target,
			SmooksConfigurationFileGenerateContext context) {
		SmooksResourceListType list = context.getSmooksResourceListModel();
		ResourceConfigType resourceConfig = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		resourceConfig.setSelector(selector);

		ResourceType resource = SmooksFactory.eINSTANCE.createResourceType();
		resource.setType(XML2XMLAnalyzer.XSL);
		resourceConfig.setResource(resource);

		AbstractXMLObject xmlNode = (AbstractXMLObject) target
				.getReferenceEntityModel();
		String cdata = getXMLString(xmlNode.getReferenceElement());

		if (cdata != null) {
			resource.setCDATAValue(cdata);
		}

		UIUtils
				.addResourceConfigType(context.getDomain(), list,
						resourceConfig);
		return resourceConfig;
	}

	private SelectorAttributes newDefaultSelectorAttribute() {
		SelectorAttributes sa = new SelectorAttributes();
		sa.setSelectorPolicy(SelectorAttributes.FULL_PATH);
		sa.setSelectorSperator(" ");
		return sa;
	}

	private void bindingConnectionAdded(AbstractStructuredDataModel source,
			AbstractStructuredDataModel target, LineConnectionModel line,
			SmooksConfigurationFileGenerateContext context) {
		AbstractXMLObject targetModel = (AbstractXMLObject) target
				.getReferenceEntityModel();
		AbstractXMLObject mappingModel = findDefaultRelateMappingTargetModel(
				targetModel, context);
		IConnectableModel mappingGraph = (IConnectableModel) UIUtils
				.findGraphModel(context.getGraphicalRootModel(), mappingModel);
		if (mappingGraph != null) {
			List list = mappingGraph.getModelTargetConnections();
			ResourceConfigType resourceConfig = null;
			if (list.size() > 1)
				return;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				LineConnectionModel connection = (LineConnectionModel) iterator
						.next();
				resourceConfig = (ResourceConfigType) connection
						.getProperty(PRO_REFERENCE_RESOURCE_CONFIG);
				if (resourceConfig == null)
					return;
				break;
			}
			// link resourceConfig to the connection model;
			line
					.addPropertyModel(PRO_REFERENCE_RESOURCE_CONFIG,
							resourceConfig);
			Element valueOf = createValueOfElement(source, target, context);
			ResourceType resource = resourceConfig.getResource();
			if (resource != null
					&& XML2XMLAnalyzer.XSL.equals(resource.getType())) {
				SmooksModelUtils.setCDATAToAnyType(resource,
						getXMLString(valueOf));
			}
		}
	}

	public static void setResourceCDATAViaTargetNode(Element element,
			IConnectableModel node) {
		List list = node.getModelTargetConnections();
		ResourceConfigType resourceConfig = null;
		if (list.size() > 1 || list.isEmpty())
			return;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator
					.next();
			resourceConfig = (ResourceConfigType) connection
					.getProperty(PRO_REFERENCE_RESOURCE_CONFIG);
			if (resourceConfig == null)
				return;
			break;
		}
		ResourceType resource = resourceConfig.getResource();
		if (resource != null && XML2XMLAnalyzer.XSL.equals(resource.getType())) {
			resource.setCDATAValue(getXMLString(element.getDocument()
					.getRootElement()));
		}
	}

	public static String getXMLString(Element node) {
		String sss = node.asXML();
		String string = null;
		ByteArrayOutputStream stream = null;
		XMLWriter writer = null;
		try {
			stream = new ByteArrayOutputStream();
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(stream, format);
			writer.write(node);
			string = new String(stream.toByteArray());
		} catch (Exception e) {
			// ignore
		} finally {
			try {
				if (stream != null)
					stream.close();
				if (writer != null)
					writer.close();
			} catch (Throwable t) {
			}
		}
		if (string == null) {
			return node.asXML();
		}
		String s = XML2XMLAnalyzer.XSL_NAMESPACE;
		s = s.trim();
		if (string.indexOf(s) != -1) {
			string = string.replaceAll(s, "");
		}
		return string;
	}

	private Element createValueOfElement(AbstractStructuredDataModel source,
			AbstractStructuredDataModel target,
			SmooksConfigurationFileGenerateContext context) {
		AbstractXMLObject sourceModel = (AbstractXMLObject) source
				.getReferenceEntityModel();
		AbstractXMLObject targetModel = (AbstractXMLObject) target
				.getReferenceEntityModel();

		AbstractXMLObject mappingModel = findDefaultRelateMappingSourceModel(
				sourceModel, context);

		String select = generateSelectProValue(mappingModel, sourceModel);
		Element valueOf = newValueOfElement(select);

		Element referenceElement = targetModel.getReferenceElement();
		if (referenceElement != null && valueOf != null) {
			referenceElement.add(valueOf);
		}
		return referenceElement.getDocument().getRootElement();
	}

	private AbstractXMLObject findDefaultRelateMappingTargetModel(
			AbstractXMLObject targetModel,
			SmooksConfigurationFileGenerateContext context) {
		AbstractXMLObject parent = targetModel.getParent();
		AbstractStructuredDataModel parentGraph = UIUtils.findGraphModel(
				context.getGraphicalRootModel(), parent);
		if (parentGraph instanceof IConnectableModel) {
			List connections = ((IConnectableModel) parentGraph)
					.getModelTargetConnections();
			if (connections.isEmpty()) {
				AbstractXMLObject p1 = findDefaultRelateMappingTargetModel(
						parent, context);
				if (p1 != null)
					return p1;
			} else {
				for (Iterator iterator = connections.iterator(); iterator
						.hasNext();) {
					LineConnectionModel connection = (LineConnectionModel) iterator
							.next();
					Object mt = connection
							.getProperty(XMLPropertiesSection.MAPPING_TYPE);
					if (XMLPropertiesSection.MAPPING.equals(mt)) {
						return parent;
					}
				}
			}
		}
		return null;
	}

	private AbstractXMLObject findDefaultRelateMappingSourceModel(
			AbstractXMLObject sourceModel,
			SmooksConfigurationFileGenerateContext context) {
		AbstractXMLObject parent = sourceModel.getParent();
		AbstractStructuredDataModel parentGraph = UIUtils.findGraphModel(
				context.getGraphicalRootModel(), parent);
		if (parentGraph instanceof IConnectableModel) {
			List connections = ((IConnectableModel) parentGraph)
					.getModelSourceConnections();
			if (connections.isEmpty()) {
				AbstractXMLObject p1 = findDefaultRelateMappingSourceModel(
						parent, context);
				if (p1 != null)
					return p1;
			} else {
				for (Iterator iterator = connections.iterator(); iterator
						.hasNext();) {
					LineConnectionModel connection = (LineConnectionModel) iterator
							.next();
					Object mt = connection
							.getProperty(XMLPropertiesSection.MAPPING_TYPE);
					if (XMLPropertiesSection.MAPPING.equals(mt)) {
						return parent;
					}
				}
			}
		}
		return null;
	}

	private String generateSelectProValue(
			AbstractXMLObject mappingTransformModel,
			AbstractXMLObject bindingTransformModel) {
		String select = bindingTransformModel.getName();
		if (bindingTransformModel instanceof TagPropertyObject) {
			select = "@" + select;
		}
		boolean hasParent = false;
		AbstractXMLObject bindingParent = bindingTransformModel.getParent();
		while (bindingParent != null && bindingParent != mappingTransformModel) {
			hasParent = true;
			String select1 = bindingParent.getName();
			if (bindingParent instanceof TagPropertyObject) {
				select1 = "@" + select1;
			}
			select = select1 + "/" + select;
			bindingParent = bindingParent.getParent();
		}

		if (hasParent) {
			select = "./" + select;
		}
		return select;
	}

	private Element newValueOfElement(String select) {
		Element valueOf = new DefaultElement("xsl:value-of");
		if (select != null) {
			valueOf.addAttribute(XML2XMLAnalyzer.XSL_PRO_SELECT, select);
		}
		return valueOf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener#modelChanged
	 * (java.lang.Object,
	 * org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext
	 * , java.beans.PropertyChangeEvent)
	 */
	public void modelChanged(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context,
			PropertyChangeEvent event) {
		String pm = event.getPropertyName();
		if (AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_UPDATE
				.equals(pm)) {
			LineConnectionModel line = (LineConnectionModel) graphicalModel;
			ResourceConfigType rc = (ResourceConfigType) line
					.getProperty(PRO_REFERENCE_RESOURCE_CONFIG);
			if (rc == null)
				return;
			SelectorAttributes sa = (SelectorAttributes) line
					.getProperty(PRO_SELECTOR_ATTRIBUTES);
			Object obj = ((AbstractStructuredDataModel) line.getSource())
					.getReferenceEntityModel();
			if (obj instanceof IXMLStructuredObject) {
				String newSelector = UIUtils.generatePath(
						(IXMLStructuredObject) obj, sa);
				if (newSelector != null)
					rc.setSelector(newSelector);
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
			LineConnectionModel connection = (LineConnectionModel) graphicalModel;
			Object mt = connection
					.getProperty(XMLPropertiesSection.MAPPING_TYPE);
			if (XMLPropertiesSection.BINDING.equals(mt)) {
				AbstractStructuredDataModel target = (AbstractStructuredDataModel) connection
						.getTarget();
				AbstractXMLObject xmlNode = (AbstractXMLObject) target
						.getReferenceEntityModel();
				Element element = xmlNode.getReferenceElement();
				Element valueOf = null;
				List elements = element.elements();
				for (Iterator iterator = elements.iterator(); iterator
						.hasNext();) {
					Element e1 = (Element) iterator.next();
					if (e1.getName().equalsIgnoreCase("value-of")
							|| e1.getName().equalsIgnoreCase("xsl:value-of")) {
						valueOf = e1;
						break;
					}
				}
				if (valueOf != null) {
					element.remove(valueOf);
				}
				setResourceCDATAViaTargetNode(element,
						(IConnectableModel) target);
			}
			if (XMLPropertiesSection.MAPPING.equals(mt)) {
				ResourceConfigType resourceConfig = (ResourceConfigType) connection
						.getProperty(PRO_REFERENCE_RESOURCE_CONFIG);
				if (resourceConfig == null)
					return;
				context.getSmooksResourceListModel()
						.getAbstractResourceConfig().remove(resourceConfig);
			}
		}
	}

}

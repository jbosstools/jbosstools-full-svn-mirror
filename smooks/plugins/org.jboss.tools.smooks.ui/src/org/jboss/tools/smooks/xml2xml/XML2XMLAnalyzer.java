/**
 * 
 */
package org.jboss.tools.smooks.xml2xml;

import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.jboss.tools.smooks.analyzer.AbstractAnalyzer;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.javabean.model.SelectorAttributes;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;
import org.jboss.tools.smooks.xml.model.XMLObjectAnalyzer;
import org.jboss.tools.smooks.xml.ui.XMLPropertiesSection;
import org.jboss.tools.smooks.xml2java.analyzer.AbstractXMLModelAnalyzer;

/**
 * @author dart
 * 
 */
public class XML2XMLAnalyzer extends AbstractAnalyzer {

	public static final String XSL_PRO_SELECT = "select";

	public static final String XSL_ELEMENT_VALUE_OF = "value-of";

	public static final String XSL = "xsl";

	public static final String XSL_NAMESPACE = " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ";

	private List<AbstractXMLObject> xmlUsedList = new ArrayList<AbstractXMLObject>();

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
		if (true)
			return;
		GraphRootModel root = context.getGraphicalRootModel();
		List<SourceModel> sourceList = root.loadSourceModelList();
		for (Iterator<SourceModel> iterator = sourceList.iterator(); iterator
				.hasNext();) {
			SourceModel sourceModel = (SourceModel) iterator.next();
			List sourceConnectList = sourceModel.getModelSourceConnections();
			if (sourceConnectList.isEmpty())
				continue;
			for (Iterator iterator2 = sourceConnectList.iterator(); iterator2
					.hasNext();) {
				LineConnectionModel connection = (LineConnectionModel) iterator2
						.next();
				if (isMappingConnection(connection)
						&& !connectionIsUsed(connection)) {
					ResourceConfigType resourceConfig = generateSmooksResourceConfig(
							context, connection);
					if (resourceConfig != null) {
						context.getGeneratorResourceList().add(resourceConfig);
						setConnectionUsed(connection);
					}
				}
			}
		}
		return;
	}

	private ResourceConfigType generateSmooksResourceConfig(
			SmooksConfigurationFileGenerateContext context,
			LineConnectionModel connection) {
		ResourceConfigType resourceConfig = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		String selector = generateSelectorString(context,
				(SourceModel) connection.getSource());
		if (selector != null) {
			resourceConfig.setSelector(selector);
		}
		ResourceType resource = SmooksFactory.eINSTANCE.createResourceType();
		resource.setType(XSL);
		resourceConfig.setResource(resource);
		String cdata = generateResourceCDATAContents(context, connection);
		if (cdata != null)
			SmooksModelUtils.setCDATAToAnyType(resource, cdata);
		return resourceConfig;
	}

	private String generateResourceCDATAContents(
			SmooksConfigurationFileGenerateContext context,
			LineConnectionModel connection) {
		TargetModel target = (TargetModel) connection.getTarget();
		AbstractXMLObject xmlNode = (AbstractXMLObject) target
				.getReferenceEntityModel();
		SourceModel mappingSource = (SourceModel) connection.getSource();
		if (xmlNode instanceof TagObject) {
			Element element = new DefaultElement(xmlNode.getName());
			List<AbstractXMLObject> childrenList = ((TagObject) xmlNode)
					.getXMLNodeChildren();
			for (Iterator iterator = childrenList.iterator(); iterator
					.hasNext();) {
				AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
						.next();
				Element childElement = generateMappingChilrenNodes(context,
						abstractXMLObject, mappingSource);
				if (childElement != null)
					element.add(childElement);
			}
			ByteArrayOutputStream stream = null;
			XMLWriter writer = null;
			try {
				stream = new ByteArrayOutputStream();
				OutputFormat format = OutputFormat.createPrettyPrint();
				writer = new XMLWriter(stream, format);
				writer.write(element);
				return new String(stream.toByteArray());
			} catch (Exception e) {

			} finally {
				try {
					if (stream != null)
						stream.close();
					if (writer != null)
						writer.close();
				} catch (Throwable t) {
				}
			}
		}
		return null;
	}

	private Element generateMappingChilrenNodes(
			SmooksConfigurationFileGenerateContext context,
			AbstractXMLObject xmlNode, SourceModel mappingSource) {
		Element element = new DefaultElement(xmlNode.getName());
		AbstractStructuredDataModel graphNode = UIUtils.findGraphModel(context
				.getGraphicalRootModel(), xmlNode);
		if (graphNode != null) {
			List connectionList = ((IConnectableModel) graphNode)
					.getModelTargetConnections();
			// only one connection , didn't support multiple connection
			if (connectionList.size() == 1) {
				LineConnectionModel connection = (LineConnectionModel) connectionList
						.get(0);
				if (isBindingConnection(connection)
						&& !connectionIsUsed(connection)) {
					Element valueOf = createXSLValueOfElement(context,
							(SourceModel) connection.getSource(), mappingSource);
					if (valueOf != null) {
						element.add(valueOf);
					}
				}
				setConnectionUsed(connection);
			}
		}
		return element;
	}

	private String generateSelectorString(
			SmooksConfigurationFileGenerateContext context,
			SourceModel mappingSource) {
		AbstractXMLObject transformModel = (AbstractXMLObject) mappingSource
				.getReferenceEntityModel();
		String selector = transformModel.getName();
		AbstractXMLObject parent = transformModel.getParent();
		while (parent != null && !(parent instanceof TagList)
				&& !(parent.getParent() instanceof TagList)) {
			selector = parent.getName() + " " + selector;
			parent = parent.getParent();
		}
		return selector;
	}

	private Element createXSLValueOfElement(
			SmooksConfigurationFileGenerateContext context,
			SourceModel bindingSource, SourceModel mappingSource) {
		// Element element = new DefaultElement(new QName("value-of",
		// new Namespace("xsl", null)));
		Element element = new DefaultElement("xsl:value-of");
		String select = generateXSLValueOfSelectValue(context, mappingSource,
				bindingSource);
		if (select != null)
			element.addAttribute(XSL_PRO_SELECT, select);
		return element;
	}

	private String generateXSLValueOfSelectValue(
			SmooksConfigurationFileGenerateContext context,
			SourceModel mappingSource, SourceModel bindingSource) {
		Object mappingTransformModel = mappingSource.getReferenceEntityModel();
		AbstractXMLObject bindingTransformModel = (AbstractXMLObject) bindingSource
				.getReferenceEntityModel();

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

	private boolean isMappingConnection(LineConnectionModel connection) {
		List<PropertyModel> list = connection.getProperties();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PropertyModel propertyModel = (PropertyModel) iterator.next();
			if (XMLPropertiesSection.MAPPING_TYPE.equals(propertyModel
					.getName())) {
				return XMLPropertiesSection.MAPPING.equals(propertyModel
						.getValue());
			}
		}
		return false;
	}

	private boolean isBindingConnection(LineConnectionModel connection) {
		List<PropertyModel> list = connection.getProperties();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PropertyModel propertyModel = (PropertyModel) iterator.next();
			if (XMLPropertiesSection.MAPPING_TYPE.equals(propertyModel
					.getName())) {
				return XMLPropertiesSection.BINDING.equals(propertyModel
						.getValue());
			}
		}
		return false;
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
		TagList sourceList = null;
		TagList targetList = null;
		PropertyChangeListener[] listeners = null;
		if (sourceObject instanceof TagList && targetObject instanceof TagList) {
			sourceList = (TagList) sourceObject;
			targetList = (TagList) targetObject;
			List<TagObject> child = targetList.getRootTagList();
			List<TagObject> temp = new ArrayList(child);
			listeners = targetList.getPropertyChangeListeners();
			for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
				TagObject tagObject = (TagObject) iterator.next();
				targetList.removeRootTag(tagObject);
			}
			temp.clear();
			temp = null;
		} else {
			return null;
		}
		MappingResourceConfigList mappingList = new MappingResourceConfigList();
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
					if (XSL.equals(resource.getType())) {
						mappingList.getGraphRenderResourceConfigList().add(
								resourceConfig);
						processResourceConfigToRender(listType, sourceList,
								targetList, mappingList, resourceConfig);
					}
				}
			}
		}
		removeXSLElements(targetList, new String[] { "value-of" });
		if (listeners != null) {
			List<TagObject> tagList = targetList.getRootTagList();
			for (Iterator iterator = tagList.iterator(); iterator.hasNext();) {
				TagObject tagObject = (TagObject) iterator.next();
				for (int i = 0; i < listeners.length; i++) {
					PropertyChangeListener listener = listeners[i];
					AbstractXMLModelAnalyzer.hookNodes(tagObject, listener);
				}
			}
		}
		return mappingList;
	}

	private void removeXSLElements(AbstractXMLObject tagList,
			String[] xslElementNames) {
		if (tagList instanceof TagList) {
			List<TagObject> tags = ((TagList) tagList).getRootTagList();
			List<TagObject> tempTags = new ArrayList<TagObject>(tags);
			for (Iterator<TagObject> iterator = tempTags.iterator(); iterator
					.hasNext();) {
				TagObject tagObject = (TagObject) iterator.next();
				removeXSLElements(tagObject, xslElementNames);
			}
			tempTags.clear();
			tempTags = null;
		}

		String name = ((AbstractXMLObject) tagList).getName();
		if (isXSLElements(name, xslElementNames)) {
			if (tagList instanceof TagObject) {
				Object parent = ((TagObject) tagList).getParent();
				if (parent instanceof TagObject) {
					((TagObject) parent).removeChildTag((TagObject) tagList);
				}
				if (parent instanceof TagList) {
					((TagList) parent).removeRootTag((TagObject) tagList);
				}
			}
			if (tagList instanceof TagPropertyObject) {
				Object parent = ((TagObject) tagList).getParent();
				if (parent instanceof TagObject) {
					((TagObject) parent)
							.removeProperty((TagPropertyObject) tagList);
				}
			}
		} else {
			if (tagList instanceof TagObject) {
				List<AbstractXMLObject> tags = ((TagObject) tagList)
						.getXMLNodeChildren();
				List<AbstractXMLObject> tempTags = new ArrayList<AbstractXMLObject>(
						tags);
				for (Iterator<AbstractXMLObject> iterator = tempTags.iterator(); iterator
						.hasNext();) {
					AbstractXMLObject tagObject = (AbstractXMLObject) iterator
							.next();
					removeXSLElements(tagObject, xslElementNames);
				}
				tempTags.clear();
				tempTags = null;

				List<TagPropertyObject> tagps = ((TagObject) tagList)
						.getProperties();
				List<TagPropertyObject> tempTagps = new ArrayList<TagPropertyObject>(
						tagps);
				for (Iterator<TagPropertyObject> iterator = tempTagps
						.iterator(); iterator.hasNext();) {
					TagPropertyObject tagObject = (TagPropertyObject) iterator
							.next();
					removeXSLElements(tagObject, xslElementNames);
				}
				tempTagps.clear();
				tempTagps = null;
			}
		}
	}

	private boolean isXSLElements(String name, String[] xslElementNames) {
		if (xslElementNames == null)
			return false;
		for (int i = 0; i < xslElementNames.length; i++) {
			String n = xslElementNames[i];
			if (n.equals(name))
				return true;
		}
		return false;
	}

	private void processResourceConfigToRender(SmooksResourceListType listType,
			TagList sourceList, TagList targetList,
			MappingResourceConfigList mappingList,
			ResourceConfigType resourceConfig) {
		AbstractXMLObject sourceNode = findSourceObjectFromSelector(
				resourceConfig, sourceList);
		if (sourceNode != null) {
			if (sourceNode instanceof TagObject) {
				AbstractXMLObject targetNode = findTargetObjectFromCDATA(resourceConfig);
				targetList.addRootTag((TagObject) targetNode);
				if (targetNode != null) {
					if (isXMLObjectUsed(targetNode)) {
						return;
					}
					MappingModel mapping = new MappingModel(sourceNode,
							targetNode);
					mapping.getProperties().add(
							new PropertyModel(
									XMLPropertiesSection.MAPPING_TYPE,
									XMLPropertiesSection.MAPPING));
					// link resourceConfig to the connection model;
					mapping
							.getProperties()
							.add(
									new PropertyModel(
											XML2XMLGraphicalModelListener.PRO_REFERENCE_RESOURCE_CONFIG,
											resourceConfig));

					SelectorAttributes sa = UIUtils.guessSelectorProperty(
							resourceConfig.getSelector(), sourceNode);
					if (sa == null) {
						sa = new SelectorAttributes();
						sa.setSelectorPolicy(SelectorAttributes.FULL_PATH);
						sa.setSelectorSperator(" ");
					}
					mapping
							.getProperties()
							.add(
									new PropertyModel(
											XML2XMLGraphicalModelListener.PRO_SELECTOR_ATTRIBUTES,
											sa));

					mappingList.getMappingModelList().add(mapping);
					setXMLObjectUsed(sourceNode);
					setXMLObjectUsed(targetNode);
					processCDATA(resourceConfig, mappingList, sourceList,
							targetNode, sourceNode);
				}
			}
		}
	}

	private void processCDATA(ResourceConfigType resourceConfig,
			MappingResourceConfigList mappingList, TagList sourceList,
			AbstractXMLObject targetNode, AbstractXMLObject mappingSource) {
		if (resourceConfig != null) {
			ResourceType resource = resourceConfig.getResource();
			if (resource != null) {
				processCDATA(resource.getCDATAValue(), mappingList, sourceList,
						targetNode, mappingSource, resourceConfig);
			}
		}
	}

	private void processCDATA(String cdata,
			MappingResourceConfigList mappingList, TagList sourceList,
			AbstractXMLObject targetNode, AbstractXMLObject mappingSource,
			ResourceConfigType referenceRC) {
		if (cdata == null)
			return;
		cdata = cdata.trim();
		cdata = transformCDATA(cdata);
		handleXSLNode(targetNode, mappingSource, mappingList, referenceRC);
	}

	private void handleXSLNode(AbstractXMLObject node,
			AbstractXMLObject mappingNode,
			MappingResourceConfigList mappingList,
			ResourceConfigType referenceResourceConfig) {
		if (XSL_ELEMENT_VALUE_OF.equals(node.getName())) {
			if (node instanceof TagObject) {
				List<TagPropertyObject> propertyList = ((TagObject) node)
						.getProperties();
				for (Iterator iterator = propertyList.iterator(); iterator
						.hasNext();) {
					TagPropertyObject tagPropertyObject = (TagPropertyObject) iterator
							.next();
					if (XSL_PRO_SELECT.equals(tagPropertyObject.getName())) {
						String selectValue = tagPropertyObject.getValue();
						AbstractXMLObject sourceNode = findSourceNodeFromXSLSelect(
								selectValue, mappingNode);
						if (sourceNode != null) {
							AbstractXMLObject targetNode = node.getParent();
							if (targetNode != null) {
								if (isXMLObjectUsed(targetNode))
									return;
								MappingModel mapping = new MappingModel(
										sourceNode, targetNode);
								setXMLObjectUsed(sourceNode);
								setXMLObjectUsed(targetNode);
								mapping
										.getProperties()
										.add(
												new PropertyModel(
														XMLPropertiesSection.MAPPING_TYPE,
														XMLPropertiesSection.BINDING));
								mapping
										.getProperties()
										.add(
												new PropertyModel(
														XML2XMLGraphicalModelListener.PRO_REFERENCE_RESOURCE_CONFIG,
														referenceResourceConfig));
								mappingList.getMappingModelList().add(mapping);
							}
						}
					}
				}
			}
		} else {
			if (node instanceof TagObject) {
				List<AbstractXMLObject> children = ((TagObject) node)
						.getXMLNodeChildren();
				for (Iterator iterator = children.iterator(); iterator
						.hasNext();) {
					AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
							.next();
					handleXSLNode(abstractXMLObject, mappingNode, mappingList,
							referenceResourceConfig);
				}
			}
		}
	}

	private AbstractXMLObject findSourceNodeFromXSLSelect(String selectValue,
			AbstractXMLObject mappingNode) {
		String[] names = new String[] {};
		if (selectValue == null)
			return null;
		selectValue = selectValue.trim();
		if (selectValue.startsWith(".")) {
			selectValue = selectValue.substring(1);
		}
		if (selectValue.startsWith("/"))
			selectValue = selectValue.substring(1);
		names = selectValue.split("/");
		return this.findTheXMLObjectFromNameArray(names, mappingNode);
	}

	private AbstractXMLObject findTargetObjectFromCDATA(
			ResourceConfigType resourceConfig) {
		if (resourceConfig != null) {
			ResourceType resource = resourceConfig.getResource();
			if (resource != null) {
				return findTargetObjectFromCDATA(resource.getCDATAValue());
			}
		}
		return null;
	}

	private AbstractXMLObject findTargetObjectFromCDATA(String cdata) {
		if (cdata == null)
			return null;
		cdata = cdata.trim();
		cdata = transformCDATA(cdata);
		XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
		try {
			TagObject list = analyzer.analyzeFregment(new ByteArrayInputStream(
					cdata.getBytes()), null);
			return list;
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return null;

	}

	private String transformCDATA(String cdata) {
		// cdata = cdata.replace(":", "-");
		// return cdata;
		int start_index = cdata.indexOf("<");
		int end_index = cdata.indexOf(">");
		if (start_index == -1 || end_index == -1)
			return cdata;
		String contents = cdata.substring(start_index, end_index);
		if (contents.indexOf("\"http://www.w3.org/1999/XSL/Transform\"") != -1) {
			return cdata;
		}
		String second_frg = cdata.substring(end_index, cdata.length());
		cdata = contents + XSL_NAMESPACE + second_frg;
		return cdata;
	}

	private AbstractXMLObject findSourceObjectFromSelector(
			ResourceConfigType resourceConfig, TagList sourceTagList) {
		String selector = resourceConfig.getSelector();
		if (selector == null)
			return null;
		selector = selector.trim();
		return findSourceObjectFromSelector(selector, sourceTagList);
	}

	private AbstractXMLObject findSourceObjectFromSelector(String selector,
			TagList sourceTagList) {
		if (selector == null)
			return null;
		String[] names = selector.trim().split(" ");
		List<TagObject> list = sourceTagList.getRootTagList();
		AbstractXMLObject firstNode = null;
		if (names != null) {
			// find the first node:
			String name = names[0].trim();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
						.next();
				AbstractXMLObject node = findTheXMLNode(name, abstractXMLObject);
				if (node != null && !isXMLObjectUsed(node)) {
					firstNode = node;
					break;
				}
			}

			AbstractXMLObject sourceNode = findTheXMLObjectFromNameArray(names,
					firstNode);
			if (sourceNode != null && !isXMLObjectUsed(sourceNode)) {
				setXMLObjectUsed(sourceNode);
				return sourceNode;
			}
		}
		return null;
	}

	private AbstractXMLObject findTheXMLObjectFromNameArray(String[] names,
			AbstractXMLObject firstNode) {
		if (names != null) {
			AbstractXMLObject node = firstNode;
			for (int i = 0; i < names.length; i++) {
				String name = names[i].trim();
				if (name.length() == 0)
					continue;
				node = findTheXMLNode(name, node);
			}
			return node;
		}
		return null;
	}

	private AbstractXMLObject findTheXMLNode(String name,
			AbstractXMLObject xmlObject) {
		if (xmlObject == null)
			return null;
		if (name.startsWith("@")) {
			String tempName = name.substring(1);
			if (tempName.equals(xmlObject.getName())) {
				return xmlObject;
			}
			if (xmlObject instanceof TagObject) {
				List<TagPropertyObject> children = ((TagObject) xmlObject)
						.getProperties();
				for (Iterator iterator = children.iterator(); iterator
						.hasNext();) {
					TagPropertyObject abstractXMLObject = (TagPropertyObject) iterator
							.next();
					if (tempName.equals(abstractXMLObject.getName())) {
						return abstractXMLObject;
					}
				}
			}
		} else {
			if (name.equals(xmlObject.getName())) {
				return xmlObject;
			}
			if (xmlObject instanceof TagObject) {
				List<AbstractXMLObject> children = ((TagObject) xmlObject)
						.getXMLNodeChildren();
				for (Iterator iterator = children.iterator(); iterator
						.hasNext();) {
					AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
							.next();
					AbstractXMLObject child = findTheXMLNode(name,
							abstractXMLObject);
					if (child != null)
						return child;
				}
			}
		}
		return null;
	}

	private void setXMLObjectUsed(AbstractXMLObject xmlObject) {
		xmlUsedList.add(xmlObject);
	}

	private boolean isXMLObjectUsed(AbstractXMLObject xmlObject) {
		return xmlUsedList.indexOf(xmlObject) != -1;
	}

	public DesignTimeAnalyzeResult[] analyzeGraphModel(
			SmooksConfigurationFileGenerateContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}

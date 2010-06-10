/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.tools.smooks.templating.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.Assert;
import org.jboss.tools.smooks.templating.model.TemplatingModelBuilder;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.jboss.tools.smooks.templating.model.ModelNodeResolver;
import org.jboss.tools.smooks.templating.template.exception.InvalidMappingException;
import org.jboss.tools.smooks.templating.template.exception.TemplateBuilderException;
import org.jboss.tools.smooks.templating.template.exception.UnmappedCollectionNodeException;
import org.jboss.tools.smooks.templating.template.util.FreeMarkerUtil;
import org.milyn.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract Template Builder.
 * <p/>
 * See <a href="http://www.jboss.org/community/wiki/SmooksEditorTemplateGeneration">Wiki Docs</a>.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public abstract class TemplateBuilder {

	private TemplatingModelBuilder modelBuilder;
	private Document model;
	private List<Mapping> mappings = new ArrayList<Mapping>();
	private XPathFactory xpathFactory = XPathFactory.newInstance();
	private XPathNamespaceContext namespaceContext;

	/**
	 * Public constructor.
	 * 
	 * @param modelBuilder
	 *            The model builder underlying this template builder instance.
	 * @throws ModelBuilderException
	 *             Error building model.
	 */
	public TemplateBuilder(TemplatingModelBuilder modelBuilder) throws ModelBuilderException {
		Assert.isNotNull(modelBuilder, "modelBuilder"); //$NON-NLS-1$
		this.modelBuilder = modelBuilder;
		this.model = modelBuilder.buildModel();
		this.namespaceContext = new XPathNamespaceContext(modelBuilder.getNamespaces());
	}

	/**
	 * Build the template for the specified model, based on the supplied
	 * mappings.
	 * 
	 * @return The template.
	 * @throws org.jboss.tools.smooks.templating.template.exception.TemplateBuilderException
	 *             Exception building template.
	 */
	public abstract String buildTemplate() throws TemplateBuilderException;

	/**
	 * Get the model associated with the template.
	 * 
	 * @return The model.
	 */
	public Document getModel() {
		return model;
	}

	/**
	 * Get the model DOM Node specified by the supplied XPath expression
	 * 
	 * @param xpathExpr
	 *            The XPath expression.
	 * @return The Model node specified by the XPath expression, or null.
	 * @throws XPathExpressionException
	 *             error evaluating XPath expression.
	 */
	public Node getModelNode(String xpathExpr) throws XPathExpressionException {
		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(namespaceContext);
		return (Node) xpath.evaluate(xpathExpr, model, XPathConstants.NODE);
	}

	/**
	 * Get the {@link TemplatingModelBuilder} instance associated with this
	 * {@link TemplateBuilder}.
	 * 
	 * @return The {@link TemplatingModelBuilder} instance associated with this
	 *         {@link TemplateBuilder}.
	 */
	public TemplatingModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	/**
	 * Add a source to model value mapping.
	 * 
	 * @param srcPath
	 *            Source path. Depends on the source type e.g. will be a java
	 *            object graph path for a bean property and will be an xml path
	 *            for an XML source.
	 * @param modelPath
	 *            The mapping path in the target model.
	 * @return The mapping instance.
	 * @throws InvalidMappingException
	 *             Invalid mapping.
	 */
	public ValueMapping addValueMapping(String srcPath, Node modelPath) throws InvalidMappingException {
		asserValidMappingNode(modelPath);
		assertCollectionsMapped(modelPath);

		ValueMapping mapping = new ValueMapping(srcPath, modelPath);
		mappings.add(mapping);
		addHideNodes(modelPath, mapping);

		return mapping;
	}

	/**
	 * Add a source to model collection mapping.
	 * 
	 * @param srcCollectionPath
	 *            Source path.
	 * @param modelCollectionPath
	 *            The mapping path in the target model.
	 * @param collectionItemName
	 *            The name associated with the individual collection items.
	 * @return The mapping instance.
	 * @throws InvalidMappingException
	 *             Invalid mapping.
	 */
	public CollectionMapping addCollectionMapping(String srcCollectionPath, Element modelCollectionPath, String collectionItemName)
			throws InvalidMappingException {
		asserValidMappingNode(modelCollectionPath);
		assertCollectionsMapped(modelCollectionPath.getParentNode());

		CollectionMapping mapping = new CollectionMapping(srcCollectionPath, modelCollectionPath, collectionItemName);
		mappings.add(mapping);
		addHideNodes(modelCollectionPath, mapping);

		return mapping;
	}

	/**
	 * Remove the specified mapping
	 * 
	 * @param mapping
	 *            The mapping instance to be removed.
	 * @return A list of hidden Nodes that should be shown again.
	 */
	public List<Node> removeMapping(Mapping mapping) {
		List<Node> showNodes = new ArrayList<Node>();

		mappings.remove(mapping);
		List<Node> hideNodes = mapping.getHideNodes();
		if (hideNodes != null) {
			for (Node hiddenNode : hideNodes) {
				if (hiddenNode.getNodeType() == Node.ELEMENT_NODE) {
					if (!isOnMappingPath((Element) hiddenNode)) {
						TemplatingModelBuilder.unhideFragment((Element) hiddenNode);
						showNodes.add(hiddenNode);
					}
				}
			}
		}

		return showNodes;
	}

	private void addHideNodes(Node modelPath, Mapping mapping) {
		Node parent = TemplatingModelBuilder.getParentNode(modelPath);

		while (parent != null) {
			if (TemplatingModelBuilder.isCompositor(parent)) {
				Element compositor = (Element) parent;
				int maxOccurs = TemplatingModelBuilder.getMaxOccurs(compositor);
				int numElementsOnMappingPath = getNumElementsOnMappingPath(compositor);

				if (numElementsOnMappingPath == maxOccurs) {
					hideUnmappedPaths(compositor, mapping);
				}
			}
			parent = TemplatingModelBuilder.getParentNode(parent);
		}
	}

	private void hideUnmappedPaths(Element compositor, Mapping mapping) {
		NodeList children = compositor.getChildNodes();
		int numChildren = children.getLength();

		for (int i = 0; i < numChildren; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element nodeToHide = (Element) child;
				if (!isOnMappingPath(nodeToHide)) {
					mapping.addHideNode(nodeToHide);
					TemplatingModelBuilder.hideFragment(nodeToHide);
				}
			}
		}
	}

	private int getNumElementsOnMappingPath(Element compositor) {
		NodeList children = compositor.getChildNodes();
		int numChildren = children.getLength();
		int count = 0;

		for (int i = 0; i < numChildren; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (isOnMappingPath((Element) child)) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Get the full list of mappings.
	 * 
	 * @return The full list of mappings.
	 */
	public List<Mapping> getMappings() {
		return mappings;
	}

	private void asserValidMappingNode(Node mappingNode) throws InvalidMappingException {
		if (mappingNode == null) {
			throw new InvalidMappingException("Node is null."); //$NON-NLS-1$
		}
		if (mappingNode.getNodeType() != Node.ATTRIBUTE_NODE && mappingNode.getNodeType() != Node.ELEMENT_NODE) {
			throw new InvalidMappingException(
					"Unsupported XML target node mapping.  Support XML elements and attributes only."); //$NON-NLS-1$
		}
		if (TemplatingModelBuilder.NAMESPACE.equals(mappingNode.getNamespaceURI())) {
			throw new InvalidMappingException(
					"Unsupported XML target node mapping.  Cannot map to a reserved model node from the '" + TemplatingModelBuilder.NAMESPACE + "' namespace."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (TemplatingModelBuilder.isHidden(mappingNode)) {
			throw new InvalidMappingException(
					"Illegal XML target node mapping for node '" + mappingNode + "'.  This node (or one of it's ancestors) is hidden."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void assertCollectionsMapped(Node mappingNode) throws UnmappedCollectionNodeException {
		Element collectionElement = getNearestCollectionElement(mappingNode);
		if (collectionElement != null) {
			CollectionMapping parentCollectionMapping = getCollectionMapping(collectionElement);
			if (parentCollectionMapping == null && TemplatingModelBuilder.getEnforceCollectionSubMappingRules(collectionElement)) {
				throw new UnmappedCollectionNodeException(collectionElement);
			}
		}
	}

	/**
	 * Get the nearest "Collection" element to for the supplied model node.
	 * <p/>
	 * This can be the node itself, if it is a Collection node.
	 * 
	 * @param modelPath
	 *            The starting point of the search.
	 * @return The nearest Collection element (possibly itself), or null if
	 *         there is non.
	 */
	public Element getNearestCollectionElement(Node modelPath) {
		Node nextNode = modelPath;

		while (nextNode != null) {
			if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
				if(TemplatingModelBuilder.isCollection((Element) nextNode)) {					
					return (Element) nextNode;
				}
			}

			nextNode = TemplatingModelBuilder.getParentNode(nextNode);
		}

		return null;
	}

	protected CollectionMapping getCollectionMapping(Element collectionElement) {
		Mapping mapping = getMapping(collectionElement);

		if (mapping instanceof CollectionMapping) {
			return (CollectionMapping) mapping;
		}

		return null;
	}

	protected Mapping getMapping(Node node) {
		for (Mapping mapping : mappings) {
			if (mapping.getMappingNode() == node) {
				return mapping;
			}
		}

		return null;
	}

	/**
	 * Assert whether or not the specified model node details should be added to
	 * the template.
	 * 
	 * @param element
	 *            The model element.
	 * @return True if the model node details should be added to the template,
	 *         otherwise false.
	 */
	protected boolean assertAddNodeToTemplate(Element element) {
		// Don't write the element if any of the following are true:
		// 1. Is from the reserved namespace (ModelBuilder.NAMESPACE).
		// 2. Is hidden.
		// 3. Has a minOccurs of 0 and has no mappings onto it (including any of
		// it's attributes and
		// child elements) i.e. it is not a parent of any of the mappings.

		if (TemplatingModelBuilder.isInReservedNamespace(element)) {
			return false;
		} else if (TemplatingModelBuilder.isHidden(element)) {
			return false;
		} else if (TemplatingModelBuilder.getMinOccurs(element) == 0 && !isOnMappingPath(element)) {
			return false;
		}

		return true;
	}

	/**
	 * Is the supplied element on the path of any of the currently mapped model
	 * nodes.
	 * 
	 * @param element
	 *            The element to be checked.
	 * @return True if the element is on the path of one of the Mappings,
	 *         otherwise false.
	 */
	public boolean isOnMappingPath(Element element) {
		for (Mapping mapping : mappings) {
			Node pathNode = mapping.getMappingNode();

			while (pathNode != null) {
				if (element == pathNode) {
					return true;
				}
				pathNode = TemplatingModelBuilder.getParentNode(pathNode);
			}
		}

		return false;
	}

	/**
	 * Add a collection mapping from the supplied template model element and
	 * target {@link ModelNodeResolver}.
	 * 
	 * @param element
	 *            The element from the template model (DOM model built from a
	 *            template).
	 * @param modelNodeResolver
	 *            The target model node resolver.
	 * @throws TemplateBuilderException
	 *             Invalid collection element.
	 */
	protected void addCollectionMapping(Element element, ModelNodeResolver modelNodeResolver)
			throws TemplateBuilderException {
		NodeList children = element.getChildNodes();
		int childCount = children.getLength();

		for (int i = 0; i < childCount; i++) {
			Node child = children.item(i);

			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Node targetModelNode = modelNodeResolver.resolveNodeMapping(child);
				String srcPath = element.getAttributeNS(TemplatingModelBuilder.NAMESPACE, "srcPath"); //$NON-NLS-1$
				String collectionItemName = element.getAttributeNS(TemplatingModelBuilder.NAMESPACE, "collectionItemName"); //$NON-NLS-1$

				addCollectionMapping(srcPath, (Element) targetModelNode, collectionItemName);

				return;
			}
		}

		throw new TemplateBuilderException(
				"Unexpected Exception.  Invalid <smk:list> collection node.  Has no child elements!"); //$NON-NLS-1$
	}

	protected void addValueMapping(Node modelNode, ModelNodeResolver modelNodeResolver, String dollarVariable) throws TemplateBuilderException, InvalidMappingException {
		Node targetModelNode = modelNodeResolver.resolveNodeMapping(modelNode);
		addValueMapping(targetModelNode, dollarVariable);
	}

	protected void addValueMapping(Node modelNode, String dollarVariable) throws TemplateBuilderException, InvalidMappingException {
		String srcPath = FreeMarkerUtil.extractJavaPath(dollarVariable);
		String rawFormatting = FreeMarkerUtil.extractRawFormatting(dollarVariable);
		
		ValueMapping mapping = addValueMapping(srcPath, modelNode);
		if(rawFormatting != null) {
			Properties encodeProperties = new Properties();
			encodeProperties.setProperty(ValueMapping.RAW_FORMATING_KEY, rawFormatting);
			mapping.setEncodeProperties(encodeProperties);
		}
	}

	public static void writeListStart(StringWriter writer, String srcPath, String collectionItemName) {
		writer.write("<smk:list smk:srcPath=\"" + srcPath + "\" smk:collectionItemName=\"" + collectionItemName + "\" xmlns:smk=\"" + TemplatingModelBuilder.NAMESPACE + "\">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public static void writeListEnd(StringWriter writer) {
		writer.write("</smk:list>"); //$NON-NLS-1$
	}

	public static boolean isListElement(Element element) {
		if (TemplatingModelBuilder.isInReservedNamespace(element)) {
			if (DomUtils.getName(element).equals("list")) { //$NON-NLS-1$
				return true;
			}
		}

		return false;
	}

	protected static void writeIndent(int indent, Writer writer) {
		try {
			// 1 indent == 4 spaces...
			for (int i = 0; i < indent * 4; i++) {
				writer.write(' ');
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unexpected IOException writing template.", e); //$NON-NLS-1$
		}
	}

	private class XPathNamespaceContext implements NamespaceContext {

		private Properties namespaces;

		private XPathNamespaceContext(Properties namespaces) {
			this.namespaces = namespaces;
		}

		public String getNamespaceURI(String prefix) {
			if (prefix.equals("smk")) { //$NON-NLS-1$
				return TemplatingModelBuilder.NAMESPACE;
			} else {
				return namespaces.getProperty(prefix);
			}
		}

		public String getPrefix(String namespaceURI) {
			return null;
		}

		public Iterator getPrefixes(String namespaceURI) {
			return null;
		}
	}
}

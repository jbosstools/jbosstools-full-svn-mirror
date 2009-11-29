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
package org.jboss.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.jboss.template.exception.InvalidMappingException;
import org.jboss.template.exception.TemplateBuilderException;
import org.jboss.template.exception.UnmappedCollectionNodeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public abstract class TemplateBuilder {

    private Document model;
    private List<Mapping> mappings = new ArrayList<Mapping>();

    /**
     * Public constructor.
     * @param model The model on which the template will be constructed.
     */
    public TemplateBuilder(Document model) {
    	Assert.isNotNull(model, "model"); //$NON-NLS-1$
        this.model = model;
    }

    /**
     * Build the template for the specified model, based on the supplied mappings.
     * @return The template.
     * @throws org.jboss.template.exception.TemplateBuilderException Exception building template.
     */
    public abstract String buildTemplate() throws TemplateBuilderException;

    /**
     * Get the model associated with the template.
     * @return The model.
     */
    public Document getModel() {
        return model;
    }

    /**
     * Add a source to model value mapping.
     * @param srcPath Source path.  Depends on the source type e.g. will be a java
     * object graph path for a bean property and will be an xml path for an XML source.
     * @param modelPath The mapping path in the target model.
     * @return The mapping instance.
     * @throws InvalidMappingException Invalid mapping.
     */
    public Mapping addValueMapping(String srcPath, Node modelPath) throws InvalidMappingException {
        asserValidMappingNode(modelPath);
        Mapping mapping = new Mapping(srcPath, modelPath);
        mappings.add(mapping);
        return mapping;
    }

    /**
     * Add a source to model collection mapping.
     * @param srcCollectionPath Source path.
     * @param modelCollectionPath The mapping path in the target model.
     * @param collectionItemName The name associated with the individual collection items.
     * @return The mapping instance.
     * @throws InvalidMappingException Invalid mapping.
     */
    public Mapping addCollectionMapping(String srcCollectionPath, Element modelCollectionPath, String collectionItemName) throws InvalidMappingException {
        asserValidMappingNode(modelCollectionPath);
        Mapping mapping = new CollectionMapping(srcCollectionPath, modelCollectionPath, collectionItemName);
        mappings.add(mapping);
        return mapping;
    }

    /**
     * Remove the specified mapping
     * @param mapping The mapping instance to be removed.
     * @return True if the mapping was successfully removed, otherwise false.
     */
    public boolean removeMapping(Mapping mapping) {
        return mappings.remove(mapping);
    }
    
    /**
     * Get the full list of mappings.
     * @return The full list of mappings.
     */
    public List<Mapping> getMappings() {
		return mappings;
	}

	private void asserValidMappingNode(Node mappingNode) throws InvalidMappingException {
        if(mappingNode.getNodeType() != Node.ATTRIBUTE_NODE && mappingNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new InvalidMappingException("Unsupported XML target node mapping.  Support XML elements and attributes only."); //$NON-NLS-1$
        }
        if(ModelBuilder.NAMESPACE.equals(mappingNode.getNamespaceURI())) {
            throw new InvalidMappingException("Unsupported XML target node mapping.  Cannot map to a reserved model node from the '" + ModelBuilder.NAMESPACE + "' namespace."); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if(ModelBuilder.isHidden(mappingNode)) {
            throw new InvalidMappingException("Illegal XML target node mapping for node '" + mappingNode + "'.  This node (or one of it's ancestors) is hidden."); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Make sure any parent collection nodes are mapped...
        Element collectionElement = getParentCollectionElement(mappingNode);
        if(collectionElement != null) {
            CollectionMapping parentCollectionMapping = getCollectionMapping(collectionElement);
            if(parentCollectionMapping == null) {
                throw new UnmappedCollectionNodeException(collectionElement);
            }
        }
    }

    private Element getParentCollectionElement(Node modelPath) {
        Node parent = modelPath.getParentNode();

        while(parent != null) {
            if(parent.getNodeType() != Node.ELEMENT_NODE) {
                return null;
            }

            int maxOccurs = ModelBuilder.getMaxOccurs((Element) parent);
            if(maxOccurs > 1 || maxOccurs == -1) {
                return (Element) parent;
            }

            parent = parent.getParentNode();
        }

        return null;
    }

    protected CollectionMapping getCollectionMapping(Element collectionElement) {
        Mapping mapping = getMapping(collectionElement);

        if(mapping instanceof CollectionMapping) {
            return (CollectionMapping) mapping;
        }

        return null;
    }

    protected Mapping getMapping(Node node) {
        for(Mapping mapping : mappings) {
            if(mapping.getMappingNode() == node) {
                return mapping;
            }
        }

        return null;
    }
}

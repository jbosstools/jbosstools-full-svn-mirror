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
package org.jboss.template.xsd;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.eclipse.xsd.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.jboss.template.exception.TemplateBuilderException;
import org.jboss.template.ModelBuilder;

import java.util.*;
import java.io.IOException;
import java.io.File;

/**
 * Model Buidler for an XSD messages.
 * <p/>
 * Uses the Eclipse Schema Infoset Model API.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class XSDModelBuilder extends ModelBuilder {

    private Map<String, XSDElementDeclaration> elements = new LinkedHashMap<String, XSDElementDeclaration>();
    private Map<String, XSDTypeDefinition> types = new LinkedHashMap<String, XSDTypeDefinition>();
    private Set<String> loadedSchemas = new HashSet<String>();
    private Stack<XSDTypeDefinition> elementExpandStack = new Stack<XSDTypeDefinition>();
    private String rootElementName;

    public XSDModelBuilder(URI schemaURI) throws IOException, TemplateBuilderException {
        loadSchema(schemaURI);
    }

    public Set<String> getRootElementNames() {
        return Collections.unmodifiableSet(elements.keySet());
    }

    public void setRootElementName(String rootElementName) {
        this.rootElementName = rootElementName;
    }

    public Document buildModel() throws TemplateBuilderException {
        if(rootElementName == null) {
            throw new IllegalStateException("The 'rootElementName' property has not been set.");
        }

        XSDElementDeclaration rootElement = elements.get(rootElementName);

        if(rootElement == null) {
            throw new IllegalArgumentException("Unknown root element '" + rootElementName + "'.");
        }

        Document model = createModelInstance();
        expand(rootElement, 1, 1, model, model);

        return model;
    }

    private void loadSchema(URI baseURI, String schemaLocation) throws IOException, TemplateBuilderException {
        File baseFile = new File(baseURI.toFileString());
        java.net.URI resolvedURI = baseFile.toURI().resolve(schemaLocation);
        File schemaFile = new File(resolvedURI);

        loadSchema(URI.createFileURI(schemaFile.getAbsolutePath()));
    }

    private void loadSchema(URI schemaURI) throws IOException, TemplateBuilderException {
        if(loadedSchemas.contains(schemaURI.toFileString())) {
            return;
        }

        loadedSchemas.add(schemaURI.toFileString());

        Resource resource = new XSDResourceFactoryImpl().createResource(schemaURI);
        Map<String, Object> options = new HashMap<String, Object>();

        options.put(XSDResourceImpl.XSD_TRACK_LOCATION, true);

        resource.load(options);

        if(resource.getContents().isEmpty()) {
            throw new TemplateBuilderException("Failed to load schema '" + schemaURI + "'.");
        }

        XSDSchema schema = (XSDSchema) resource.getContents().get(0);

        List<XSDElementDeclaration> elementDeclarations = schema.getElementDeclarations();
        for(XSDElementDeclaration elementDeclaration : elementDeclarations) {
            if(!elementDeclaration.isAbstract()) {
                elements.put(elementDeclaration.getName(), elementDeclaration);
            }
        }

        EList<XSDTypeDefinition> typeDefs = schema.getTypeDefinitions();
        for(int i = 0; i < typeDefs.size(); i++) {
            XSDTypeDefinition type = (XSDTypeDefinition) typeDefs.get(i);
            types.put(type.getName(), type);
        }

        // Load includes and imports types...
        List<XSDSchemaContent> contents = schema.getContents();
        for(Object schemaComponentObj : contents) {
            if(schemaComponentObj instanceof XSDImport) {
                XSDImport xsdImport = (XSDImport) schemaComponentObj;
                loadSchema(schemaURI, xsdImport.getSchemaLocation());
            } else if(schemaComponentObj instanceof XSDInclude) {
                XSDInclude xsdInclude = (XSDInclude) schemaComponentObj;
                loadSchema(schemaURI, xsdInclude.getSchemaLocation());
            }
        }
    }

    private void expand(XSDElementDeclaration elementDeclaration, int minOccurs, int maxOccurs, Node parent, Document document) {
        XSDTypeDefinition typeDef;

        if(elementDeclaration.isElementDeclarationReference()) {
            elementDeclaration = elementDeclaration.getResolvedElementDeclaration();
            typeDef = elementDeclaration.getTypeDefinition();
        } else {
            typeDef = elementDeclaration.getTypeDefinition();
        }

        if(elementDeclaration.isAbstract()) {
            if(typeDef == null) {
                addTypeImpls(typeDef, minOccurs, maxOccurs, parent, document);
            }
            return;
        }

        if(elementExpandStack.contains(typeDef)) {
            return;
        }

        elementExpandStack.push(typeDef);
        try {
            String elementNS = elementDeclaration.getTargetNamespace();
            Element element;

            String elementName = elementDeclaration.getQName();
            if(elementNS != null) {
                element = document.createElementNS(elementNS, elementName);
            } else {
                element = document.createElement(elementName);
            }

            setMinMax(element, minOccurs, maxOccurs);
            parent.appendChild(element);

            if(typeDef instanceof XSDComplexTypeDefinition) {
                processComplexType(document, element, (XSDComplexTypeDefinition) typeDef);
            } else if(typeDef instanceof XSDSimpleTypeDefinition) {
                XSDSimpleTypeDefinition simpleTypeDef = (XSDSimpleTypeDefinition) typeDef;
                XSDTypeDefinition loadedType = types.get(simpleTypeDef.getName());

                if(loadedType instanceof XSDComplexTypeDefinition) {
                    processComplexType(document, element, (XSDComplexTypeDefinition) loadedType);
                }
            } else if(typeDef != null) {
                System.out.println("?? " + typeDef);
            }
        } finally {
            elementExpandStack.pop();
        }
    }

    private void processComplexType(Document document, Element element, XSDComplexTypeDefinition complexTypeDef) {
        XSDParticle particle = complexTypeDef.getComplexType();
        EList<XSDAttributeGroupContent> attributes = complexTypeDef.getAttributeContents();

        addAttributes(element, attributes);

        if(particle != null) {
            XSDParticleContent particleContent = particle.getContent();
            if (particleContent instanceof XSDModelGroup) {
                processModelGroup((XSDModelGroup) particleContent, particle.getMinOccurs(), particle.getMaxOccurs(), element, document);
            }
        }
    }

    private void processModelGroup(XSDModelGroup modelGroup, int minOccurs, int maxOccurs, Element element, Document document) {
        List<XSDParticle> particles = modelGroup.getParticles();
        XSDCompositor compositor = modelGroup.getCompositor();
        String compositorType = compositor.getName();

        if(particles.size() > 1 && compositorType.equals("choice")) {
            Element composite = document.createElementNS(NAMESPACE, "smk:composite");

            composite.setAttribute("type", compositorType);
            setMinMax(composite, minOccurs, maxOccurs);
            element.appendChild(composite);
            element = composite;
        }

        for (XSDParticle particle : particles) {
            XSDParticleContent content = particle.getContent();

            if (content instanceof XSDElementDeclaration) {
                expand((XSDElementDeclaration) content, particle.getMinOccurs(), particle.getMaxOccurs(), element, document);
            } else if (content instanceof XSDModelGroup) {
                processModelGroup((XSDModelGroup) content, particle.getMinOccurs(), particle.getMaxOccurs(), element, document);
            }
        }
    }

    private void addTypeImpls(XSDTypeDefinition baseType, int minOccurs, int maxOccurs, Node parent, Document document) {
        Set<Map.Entry<String, XSDElementDeclaration>> elementEntrySet = elements.entrySet();

        for(Map.Entry<String, XSDElementDeclaration> elementEntry : elementEntrySet) {
            XSDElementDeclaration elementDecl = elementEntry.getValue();

            if(isInstanceOf(baseType, elementDecl.getType())) {
                expand(elementDecl, minOccurs, maxOccurs, parent, document);
            }
        }
    }

    private void addAttributes(Element element, EList attributes) {
        // Add the attributes...
        if(attributes != null) {
            for(int i = 0; i < attributes.size(); i++) {
                XSDAttributeUse attributeUse = (XSDAttributeUse) attributes.get(i);
                XSDAttributeDeclaration attributeDecl = attributeUse.getAttributeDeclaration();
                String name = attributeDecl.getQName();
                String attributeNS = attributeDecl.getTargetNamespace();
                String value = "";
                XSDAttributeUseCategory use = attributeUse.getUse();

                if(use == XSDAttributeUseCategory.REQUIRED_LITERAL) {
                    value = REQUIRED;
                } else if(attributeUse.getValue() != null) {
                    value = OPTIONAL + "=" + attributeUse.getValue().toString();
                } else {
                    value = OPTIONAL;
                }

                if(attributeNS != null) {
                    element.setAttributeNS(attributeNS, name, value);
                } else {
                    element.setAttribute(name, value);
                }
            }
        }
    }

    private boolean isInstanceOf(XSDTypeDefinition baseType, XSDTypeDefinition type) {
        if(type == null) {
            return false;
        } else if(type.equals(baseType)) {
            return true;
        } else if(type.equals(type.getBaseType())) {
            // The base type is equal to the type itself when we've reached the root of the inheritance hierarchy...
            return false;
        } else {
            return isInstanceOf(baseType, type.getBaseType());
        }
    }
}
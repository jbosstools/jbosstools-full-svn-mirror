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
package org.jboss.template.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.jboss.template.*;
import org.jboss.template.exception.TemplateBuilderException;
import org.jboss.template.exception.UnmappedCollectionNodeException;
import org.jboss.template.util.FreeMarkerUtil;

import au.com.bytecode.opencsv.CSVReader;

import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Freemarker Template Builder for a CSV messages.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class CSVFreeMarkerTemplateBuilder extends TemplateBuilder {

    private char separatorChar;
    private char quoteChar;    

    /**
     * Construct a new FreeMarker Template Builder with no mappings.
     * @param model The model.
     * @param separatorChar The CSV field separator character.
     * @param quoteChar The CSV field quote character.
     */
    public CSVFreeMarkerTemplateBuilder(Document model, char separatorChar, char quoteChar) {
        super(model);
        this.separatorChar = separatorChar;
        this.quoteChar = quoteChar;
    }

    /**
     * Construct a new FreeMarker Template Builder, extracting the mappings from the
     * supplied FreeMarker template String.
     * @param model The model.
     * @param separatorChar The CSV field separator character.
     * @param quoteChar The CSV field quote character.
     * @param ftlTemplate The FreeMarker Template from which the mappings are to be extracted.
     * @throws TemplateBuilderException Error parsing the supplied FreeMarker template.
     */
    public CSVFreeMarkerTemplateBuilder(Document model, char separatorChar, char quoteChar, String ftlTemplate) throws TemplateBuilderException {
        this(model, separatorChar, quoteChar);
        addMappings(ftlTemplate);
    }

    private void addMappings(String ftlTemplate) throws TemplateBuilderException {
    	Template template;
    	
    	try {
			template = new Template("csvTemplate", new StringReader(ftlTemplate), new Configuration()); //$NON-NLS-1$
		} catch (IOException e) {
			throw new TemplateBuilderException ("Failed to parse the Supplied FreeMarker template.", e); //$NON-NLS-1$
		}

		TemplateElement listElement = template.getRootTreeNode();
		if(!listElement.getNodeName().equals("IteratorBlock") || !listElement.getDescription().startsWith("list")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new TemplateBuilderException ("Unable to recognize template as being a CSV template.  Expecting first template token to be a 'list' IteratorBlock node."); //$NON-NLS-1$
		}
		
		// Add the mapping for the list itself...
		addCSVListMapping(listElement.getDescription());
		
		// Add the mappings for the individual fields...
		addCSVFieldMappings(listElement);
    }

	private void addCSVListMapping(String description) throws TemplateBuilderException {
		String[] tokens = description.split(" +?"); //$NON-NLS-1$
		Element csvRecordElement = getModel().getDocumentElement();
		
		// 2nd and 4th tokens contain the info we're looking for e.g. "list message.people as person"
		addCollectionMapping(tokens[1], csvRecordElement, tokens[3]);
	}

	private void addCSVFieldMappings(TemplateElement listElement) throws TemplateBuilderException {
		StringBuilder parseBuffer = new StringBuilder();
		Enumeration children = listElement.children();
		
		while(children != null && children.hasMoreElements()) {
			TemplateElement child = (TemplateElement) children.nextElement();
			parseBuffer.append(child.getCanonicalForm());
		}
		
		Element csvRecordElement = getModel().getDocumentElement();
		CSVReader csvReader = new CSVReader(new StringReader(parseBuffer.toString()), separatorChar, quoteChar);
		try {
			String[] fields = csvReader.readNext();
			int fieldIndex = 0;
			NodeList csvFieldModelNodes = csvRecordElement.getChildNodes();
			
			for(int i = 0; i < csvFieldModelNodes.getLength(); i++) {
				Node node = csvFieldModelNodes.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					if(fieldIndex >= fields.length) {
						throw new TemplateBuilderException("CSV Template fieldset size does not match that of the specified message model.  Check the supplied fieldset.  Check the specified 'separator' and 'quote' characters match those used in the template."); //$NON-NLS-1$
					}
					
					if(FreeMarkerUtil.isDollarVariable(fields[fieldIndex])) {
						addValueMapping(FreeMarkerUtil.extractJavaPath(fields[fieldIndex]), node);
					}					
					fieldIndex++;
				}
			}
		} catch (IOException e) {
			throw new TemplateBuilderException("Failed to parse CSV fields in CSV template.", e); //$NON-NLS-1$
		}
	}

	public String buildTemplate() throws TemplateBuilderException {
        Element recordElement = getModel().getDocumentElement();
        CollectionMapping collectionMapping = getCollectionMapping(recordElement);

        if(collectionMapping == null) {
            throw new UnmappedCollectionNodeException(recordElement);
        } else {
            StringBuilder template = new StringBuilder();
            NodeList nodeList = recordElement.getChildNodes();
            int fieldIndex = 0;

            template.append("<#list " + collectionMapping.getSrcPath() + " as " + collectionMapping.getCollectionItemName() + ">\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Mapping fieldMapping = getMapping(node);

                    if(fieldIndex > 0) {
                        template.append(separatorChar);
                    }

                    if(fieldMapping != null) {
                        template.append(quoteChar);
                        template.append("${" + fieldMapping.getSrcPath() + "}"); //$NON-NLS-1$ //$NON-NLS-2$
                        template.append(quoteChar);
                    }

                    fieldIndex++;
                }
            }
            template.append("\n</#list>"); //$NON-NLS-1$

            return template.toString();
        }
    }
}
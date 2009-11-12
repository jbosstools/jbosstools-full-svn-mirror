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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.jboss.template.*;
import org.jboss.template.exception.TemplateBuilderException;
import org.jboss.template.exception.UnmappedCollectionNodeException;

/**
 * Freemarker Template Buidler for a CSV messages.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class CSVToFreemarkerTemplateBuilder extends TemplateBuilder {

    private char separatorChar;
    private char quoteChar;    

    public CSVToFreemarkerTemplateBuilder(Document model, char separatorChar, char quoteChar) {
        super(model);
        this.separatorChar = separatorChar;
        this.quoteChar = quoteChar;
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

            template.append("<#list " + collectionMapping.getSrcPath() + " as " + collectionMapping.getCollectionItemName() + ">\n");
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Mapping fieldMapping = getMapping(node);

                    if(fieldIndex > 0) {
                        template.append(separatorChar);
                    }

                    if(fieldMapping != null) {
                        template.append(quoteChar);
                        template.append("${" + fieldMapping.getSrcPath() + "}");
                        template.append(quoteChar);
                    }

                    fieldIndex++;
                }
            }
            template.append("\n</#list>");

            return template.toString();
        }
    }
}
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.jboss.template.exception.TemplateBuilderException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Abstract templating model builder.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public abstract class ModelBuilder {
    
    public static final String NAMESPACE = "http://www.jboss.org/xsd/tools/smooks"; // TODO: Make same as JBT eclipse extension namespace ?? //$NON-NLS-1$
    public static final String REQUIRED = "#required"; //$NON-NLS-1$
    public static final String OPTIONAL = "#optional"; //$NON-NLS-1$

    private static final String HIDDEN_ELEMENT = "hidden"; //$NON-NLS-1$

    /**
     * Build the templating model.
     * @return The templating model.
     * @throws TemplateBuilderException Error building model.
     */
    public abstract Document buildModel() throws TemplateBuilderException;

    /**
     * Mark a fragment as being hidden.
     * <p/>
     * When hidden, it is illegal to attempt a mapping onto a fragment.
     *
     * @param fragment The fragment to be marked as hidden.
     */
    public static void hideFragment(Element fragment) {
        fragment.setAttributeNS(NAMESPACE, HIDDEN_ELEMENT, "true"); //$NON-NLS-1$
    }

    /**
     * Unmark a fragment as being hidden.
     * <p/>
     * When hidden, it is illegal to attempt a mapping onto a fragment.
     *
     * @param fragment The fragment to be unmarked as hidden.
     */
    public static void unhideFragment(Element fragment) {
        fragment.removeAttributeNS(NAMESPACE, HIDDEN_ELEMENT);
    }

    /**
     * Is the specified node marked as being hidden.
     * <p/>
     * When hidden, it is illegal to attempt a mapping onto a fragment.
     *
     * @return True if the node is hidden, otherwise false.
     */
    public static boolean isHidden(Node node) {
        if(node != null) {
            switch(node.getNodeType()) {
                case Node.ATTRIBUTE_NODE :
                    return isHidden(node.getParentNode());
                case Node.ELEMENT_NODE :
                    if(((Element)node).getAttributeNS(NAMESPACE, HIDDEN_ELEMENT).equals("true")) { //$NON-NLS-1$
                        return true;
                    }
                    return isHidden(node.getParentNode());
            }
        }
        
        return false;
    }

    protected Document createModelInstance() throws TemplateBuilderException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new TemplateBuilderException("Error constructing DOM DocumentBuilder.", e); //$NON-NLS-1$
        }

        return builder.newDocument();
    }

    protected void setMinMax(Element element, int minOccurs, int maxOccurs) {
        element.setAttributeNS(NAMESPACE, "smk:minOccurs", Integer.toString(minOccurs)); //$NON-NLS-1$
        element.setAttributeNS(NAMESPACE, "smk:maxOccurs", Integer.toString(maxOccurs)); //$NON-NLS-1$
    }

    public static int getMinOccurs(Element element) {
        String minOccurs = element.getAttributeNS(NAMESPACE, "minOccurs"); //$NON-NLS-1$

        if(minOccurs.equals("")) { //$NON-NLS-1$
            return 1;
        }

        return Integer.parseInt(minOccurs);
    }

    public static int getMaxOccurs(Element element) {
        String maxOccurs = element.getAttributeNS(NAMESPACE, "maxOccurs"); //$NON-NLS-1$

        if(maxOccurs.equals("")) { //$NON-NLS-1$
            return 1;
        }

        return Integer.parseInt(maxOccurs);
    }
}

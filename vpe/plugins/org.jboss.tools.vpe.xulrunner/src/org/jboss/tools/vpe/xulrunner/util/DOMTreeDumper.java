/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/


package org.jboss.tools.vpe.xulrunner.util;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.io.PrintStream;
import java.util.List;

import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;


/**
 * The Class DOMTreeDumper.
 */
public class DOMTreeDumper {
    
    /** The ps. */
    private PrintStream ps = System.out;

    /** The in A. */
    private boolean inA;

    /** Flag to print hash code of object while dump node default true. */
    private boolean printHashCode;

    /** The end tag forbidden names. */
    private final String[] endTagForbiddenNames = { "AREA", //$NON-NLS-1$
            "BASE", //$NON-NLS-1$
            "BASEFONT", //$NON-NLS-1$
            "BR", //$NON-NLS-1$
            "COL", //$NON-NLS-1$
            "FRAME", //$NON-NLS-1$
            "HR", //$NON-NLS-1$
            "IMG", //$NON-NLS-1$
            "INPUT", //$NON-NLS-1$
            "ISINDEX", //$NON-NLS-1$
            "LINK", //$NON-NLS-1$
            "META", //$NON-NLS-1$
            "PARAM" }; //$NON-NLS-1$
    
    private List<String> ignoredAttributes = null;

    /**
     * The Constructor.
     */
    public DOMTreeDumper() {
        this.printHashCode = true;
    }

    /**
     * The Constructor.
     * 
     * @param isPrintHashCode the is print hash code
     */
    public DOMTreeDumper(boolean isPrintHashCode) {
        this.printHashCode = isPrintHashCode;
    }

    /**
     * Checks if is print hash code.
     * 
     * @return true, if is print hash code
     */
    public boolean isPrintHashCode() {
        return printHashCode;
    }

    /**
     * Sets the print hash code.
     * 
     * @param printHashCode the print hash code
     */
    public void setPrintHashCode(boolean printHashCode) {
        this.printHashCode = printHashCode;
    }

    /**
     * Dump to stream.
     * 
     * @param doc the doc
     * @param ps the ps
     */
    public void dumpToStream(PrintStream ps, nsIDOMDocument doc) {
        this.ps = ps;
        dumpDocument(doc);
    }

    /**
     * Dump to stream.
     * 
     * @param element the element
     * @param ps the ps
     */
    public void dumpToStream(PrintStream ps, nsIDOMElement element) {
        this.ps = ps;
        dumpNode(queryInterface(element, nsIDOMNode.class));
        ps.println();
        ps.flush();
    }

    /**
     * Dump document.
     * 
     * @param doc the doc
     */
    private void dumpDocument(nsIDOMDocument doc) {
        if (doc == null) {
            return;
        }
        nsIDOMElement element = doc.getDocumentElement();
        if (element == null)
            return;

        dumpNode(queryInterface(element, nsIDOMNode.class));
        ps.println();
        ps.flush();

        element = null;
        doc = null;
    }

    /**
     * Dump node.
     * 
     * @param node the node
     */
    public void dumpNode(nsIDOMNode node) {
        dumpNode(node, false);
    }

    /**
     * Dump node.
     * 
     * @param isMapNode the is map node
     * @param node the node
     */
    private void dumpNode(nsIDOMNode node, boolean isMapNode) {
        if (node == null) {
            return;
        }

        int type = node.getNodeType();
        String name = node.getNodeName();
        String value = node.getNodeValue();

        switch (type) {
        case nsIDOMNode.ELEMENT_NODE:
            if (name.equals("A")) { //$NON-NLS-1$
                inA = true;
            }
            if (!(inA || name.equals("BR"))) { //$NON-NLS-1$
                ps.println();
            }
            ps.print("<" + name); //$NON-NLS-1$
            dumpAttributes(node);
            
            if (endTagForbidden(name)) {
				ps.print("/>"); //$NON-NLS-1$
			} else {
				ps.print(">"); //$NON-NLS-1$
			}
            printHashCode(node);

            dumpChildren(node);
            if (name.equals("A")) { //$NON-NLS-1$
                inA = false;
            }
            if (!endTagForbidden(name)) {
                ps.print("</" + name + ">"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            break;

        case nsIDOMNode.ATTRIBUTE_NODE:
            nsIDOMAttr attr = queryInterface(node, nsIDOMAttr.class);
          
            if (!(ignoredAttributes != null && listContains(ignoredAttributes,
					attr.getName())))
				if (attr.getSpecified()) {
					ps
							.print(" " + attr.getName().toUpperCase() + "=\"" + attr.getValue() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} else {
					ps.print(" " + attr.getName().toUpperCase()); //$NON-NLS-1$
				}
            break;

        case nsIDOMNode.TEXT_NODE:
            if (!node.getParentNode().getNodeName().equals("PRE")) { //$NON-NLS-1$
                value = value.trim();
            }
            if (!value.equals("")) { //$NON-NLS-1$
                if (!inA) {
                    ps.println();
                }
                ps.print(canonicalize(value));
            }
            printHashCode(node);
            break;
        case nsIDOMNode.COMMENT_NODE:
            ps.print("\n<!--" + value + "-->"); //$NON-NLS-1$ //$NON-NLS-2$
            break;
        case nsIDOMNode.CDATA_SECTION_NODE:
        case nsIDOMNode.ENTITY_REFERENCE_NODE:
        case nsIDOMNode.ENTITY_NODE:
        case nsIDOMNode.PROCESSING_INSTRUCTION_NODE:
        case nsIDOMNode.DOCUMENT_NODE:
        case nsIDOMNode.DOCUMENT_TYPE_NODE:
        case nsIDOMNode.DOCUMENT_FRAGMENT_NODE:
        case nsIDOMNode.NOTATION_NODE:
            ps.println("\n<!-- NOT HANDLED: " + name + //$NON-NLS-1$
                    "  value=" + value + " -->"); //$NON-NLS-1$ //$NON-NLS-2$
            break;
        }
    }

    /**
     * Dump attributes.
     * 
     * @param node the node
     */
    private void dumpAttributes(nsIDOMNode node) {
        nsIDOMNamedNodeMap map = node.getAttributes();
        if (map == null) {
            return;
        }
        long length = map.getLength();
        for (int i = 0; i < length; i++) {
            dumpNode(map.item(i), true);
        }
    }

    /**
     * Dump children.
     * 
     * @param node the node
     */
    private void dumpChildren(nsIDOMNode node) {
        nsIDOMNodeList children = node.getChildNodes();
        if (children == null) {
            return;
        }
        long length = children.getLength();
        for (int i = 0; i < length; i++) {
            dumpNode(children.item(i));
        }
        if (!inA) {
            ps.println();
        }
    }

    /**
     * Canonicalize.
     * 
     * @param str the str
     * 
     * @return the string
     */
    private String canonicalize(String str) {
        StringBuffer in = new StringBuffer(str);
        int length = in.length();
        StringBuffer out = new StringBuffer(length);
        char c;
        for (int i = 0; i < length; i++) {
            switch (c = in.charAt(i)) {
            case '&':
                out.append("&amp;"); //$NON-NLS-1$
                break;
            case '<':
                out.append("&lt;"); //$NON-NLS-1$
                break;
            case '>':
                out.append("&gt;"); //$NON-NLS-1$
                break;
            case '\u00A0':
                out.append("&nbsp;"); //$NON-NLS-1$
                break;
            default:
                out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * End tag forbidden.
     * 
     * @param name the name
     * 
     * @return true, if end tag forbidden
     */
    private boolean endTagForbidden(String name) {
        for (int i = 0; i < endTagForbiddenNames.length; i++) {
            if (name.equals(endTagForbiddenNames[i])) {
                return true;
            }
        }
        return false;
    }
    
    private boolean listContains(List<String> list, String string) {

		for (String listString : list) {
			if (string.equalsIgnoreCase(listString))
				return true;
		}
		return false;

	}

    /**
     * Prints the hash code.
     * 
     * @param o the o
     */
    private void printHashCode(Object o) {
        if (isPrintHashCode()) {
            ps.print("(" + o.hashCode() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

	public List<String> getIgnoredAttributes() {
		return ignoredAttributes;
	}

	public void setIgnoredAttributes(List<String> ignoredAttributes) {
		this.ignoredAttributes = ignoredAttributes;
	}
}
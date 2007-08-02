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
package org.jboss.tools.vpe.mozilla.browser.util;

import java.io.PrintStream;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMTreeDumper {
    private PrintStream ps;
    private boolean inA;
    private final String[] endTagForbiddenNames = {"AREA",    
			   "BASE",    
			   "BASEFONT",
			   "BR",      
			   "COL",     
			   "FRAME",   
			   "HR",      
			   "IMG",     
			   "INPUT",   
			   "ISINDEX", 
			   "LINK",    
			   "META",    
			   "PARAM"};

    public void dumpToStream(PrintStream ps, Document doc) {
    	this.ps = ps;
    	dumpDocument(doc);
    }

    private void dumpDocument(Document doc) {
    	if (doc == null) {
    		return;
    	}
    	Element element = doc.getDocumentElement();
    	if (element == null) return;

    	dumpNode(element);
    	ps.println();
    	ps.flush();
    	
    	element = null;
    	doc = null;
    }

    private void dumpNode(Node node) {
    	dumpNode(node, false);
    }

    private void dumpNode(Node node, boolean isMapNode) {
    	if (node == null) {
    	    return;
    	}

    	int type = node.getNodeType();
    	String name = node.getNodeName();
    	String value = node.getNodeValue();

    	switch (type) {
		case Node.ELEMENT_NODE:
		    if (name.equals("A")) {
		    	inA = true;
		    }
    	    if (!(inA || name.equals("BR"))) {
    	    	ps.println();
    	    }
    	    ps.print("<" + name);
    	    dumpAttributes(node);
    	    ps.print(">(" + node.hashCode() + ")");	
    	    dumpChildren(node);
    	    if (name.equals("A")) {
    	    	inA = false;
    	    }
    	    if (!endTagForbidden(name)) {
    	    	ps.print("</" + name + ">");	
    	    }
    	    break;

		case Node.ATTRIBUTE_NODE:
			Attr attr = (Attr) node;
			if (attr.getSpecified()) {
				ps.print(" " + attr.getName().toUpperCase() + "=\"" + attr.getValue() + "\"");
			} else {
				ps.print(" " + attr.getName().toUpperCase());
			}
			break;

		case Node.TEXT_NODE: 
		    if (!node.getParentNode().getNodeName().equals("PRE")) {
		    	value = value.trim();
		    }
		    if (!value.equals("")) {
		    	if (!inA) {
		    		ps.println();
		    	}
		    	ps.print(canonicalize(value));
		    }
	    	ps.print("(" + node.hashCode() + ")");
		    break;	
		case Node.COMMENT_NODE:
		    ps.print("\n<!--" + value + "-->");
		    break;
		case Node.CDATA_SECTION_NODE:
		case Node.ENTITY_REFERENCE_NODE:
		case Node.ENTITY_NODE:
		case Node.PROCESSING_INSTRUCTION_NODE:
		case Node.DOCUMENT_NODE:
		case Node.DOCUMENT_TYPE_NODE:
		case Node.DOCUMENT_FRAGMENT_NODE:
		case Node.NOTATION_NODE:
		    ps.println("\n<!-- NOT HANDLED: " + name + 
			       "  value=" + value + " -->");
		    break;
    	}
	}
    
    private void dumpAttributes(Node node) {
    	NamedNodeMap map = node.getAttributes();
		if (map == null) {
			return;
		}
		int length = map.getLength();
		for (int i=0; i < length; i++) {
		    dumpNode(map.item(i), true);
		}
    }
    
    private void dumpChildren(Node node) {
    	NodeList children = node.getChildNodes();
    	if (children == null) {
    		return;
    	}
    	int length = children.getLength();
    	for (int i = 0; i < length; i++) {
    	    dumpNode(children.item(i));
    	}
    	if (!inA) {
    	    ps.println();
    	}
    }

	private String canonicalize(String str) {
		StringBuffer in = new StringBuffer(str);
		int length = in.length();
		StringBuffer out = new StringBuffer(length);
		char c;
		for (int i = 0; i < length; i++) {
			switch (c = in.charAt(i)) {  			
			case '&' :
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '\u00A0':
				out.append("&nbsp;");
				break;
			default:
				out.append(c);
			}
		}
		return out.toString();
	}

    private boolean endTagForbidden(String name) {
		for (int i = 0; i < endTagForbiddenNames.length; i++) {
		    if (name.equals(endTagForbiddenNames[i])) {
		    	return true;
		    }
		}
		return false;
    }
}

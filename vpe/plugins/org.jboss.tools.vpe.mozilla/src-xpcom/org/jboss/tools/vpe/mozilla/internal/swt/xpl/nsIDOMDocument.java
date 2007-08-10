/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

public class nsIDOMDocument extends nsIDOMNode implements Document {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + 17;

	public static final String NS_IDOMDOCUMENT_IID_STRING =
		"a6cf9075-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMDOCUMENT_IID =
		new nsID(NS_IDOMDOCUMENT_IID_STRING);

	public nsIDOMDocument(int address) {
		super(address);
	}

	public int GetDoctype(int[] aDoctype) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aDoctype);
	}

	public int GetImplementation(int[] aImplementation) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aImplementation);
	}

	public int GetDocumentElement(int[] aDocumentElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aDocumentElement);
	}

	public int CreateElement(int tagName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), tagName, _retval);
	}

	public int CreateDocumentFragment(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), _retval);
	}

	public int CreateTextNode(int data, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), data, _retval);
	}

	public int CreateComment(int data, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), data, _retval);
	}

	public int CreateCDATASection(int data, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), data, _retval);
	}

	public int CreateProcessingInstruction(int target, int data, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), target, data, _retval);
	}

	public int CreateAttribute(int name, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), name, _retval);
	}

	public int CreateEntityReference(int name, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), name, _retval);
	}

	public int GetElementsByTagName(int tagname, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), tagname, _retval);
	}

	public int ImportNode(int importedNode, boolean deep, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), importedNode, deep, _retval);
	}

	public int CreateElementNS(int namespaceURI, int qualifiedName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int CreateAttributeNS(int namespaceURI, int qualifiedName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int GetElementsByTagNameNS(int namespaceURI, int localName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), namespaceURI, localName, _retval);
	}

	public int GetElementById(int elementId, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), elementId, _retval);
	}
	
	//=========================================================================

    /**
     * This is a convenience attribute that allows direct access to the child 
     * node that is the root element of the document. For HTML documents, 
     * this is the element with the tagName "HTML".
     */
	public Element getDocumentElement() {
		int[] result = new int[1];
		int rc = GetDocumentElement(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMElement(result[0]);
	}

    /**
     * Creates an element of the type nsIDOMElement.
     * @param tagName The name of the element type to instantiate. 
     * @return A new <code>Element</code> object with the 
     */
	public Element createElement(String tagName) {
		nsString nsTagName = new nsString(tagName);
		int[] result = new int[1];
		int rc = CreateElement(nsTagName.getAddress(), result);
		nsTagName.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMElement(result[0]);
	}

    /**
     * Creates a <code>nsIDOMText</code> node given the specified string.
     * @param data The data for the node.
     * @return The new <code>nsIDOMText</code> object.
     */
	public Text createTextNode(String data) {
		nsString nsData = new nsString(data);
		int[] result = new int[1];
		int rc = CreateTextNode(nsData.getAddress(), result);
		nsData.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMText(result[0]);
	}

	/**
     * Creates an <code>nsIDOMAttr</code> of the given name. 
     * @param name The name of the attribute.
     * @return A new <code>nsIDOMAttr</code> object with the <code>nodeName</code> 
     *   attribute set to <code>name</code>, and <code>localName</code>, 
     *   <code>prefix</code>, and <code>namespaceURI</code> set to 
     *   <code>null</code>. The value of the attribute is the empty string.
	 */
	public Attr createAttribute(String name) {
		nsString nsName = new nsString(name);
		int[] result = new int[1];
		int rc = CreateAttribute(nsName.getAddress(), result);
		nsName.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMAttr(result[0]);
	}

	public DOMImplementation getImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

	public DocumentFragment createDocumentFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	public DocumentType getDoctype() {
		// TODO Auto-generated method stub
		return null;
	}

	public CDATASection createCDATASection(String data) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Comment createComment(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	public Element getElementById(String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityReference createEntityReference(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeList getElementsByTagName(String tagname) {
		// TODO Auto-generated method stub
		return null;
	}

	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInputEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getXmlEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getXmlStandalone() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public String getXmlVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setXmlVersion(String xmlVersion) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public boolean getStrictErrorChecking() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setStrictErrorChecking(boolean strictErrorChecking) {
		// TODO Auto-generated method stub
		
	}

	public String getDocumentURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDocumentURI(String documentURI) {
		// TODO Auto-generated method stub
		
	}

	public Node adoptNode(Node source) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void normalizeDocument() {
		// TODO Auto-generated method stub
		
	}

	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public DOMConfiguration getDomConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
}
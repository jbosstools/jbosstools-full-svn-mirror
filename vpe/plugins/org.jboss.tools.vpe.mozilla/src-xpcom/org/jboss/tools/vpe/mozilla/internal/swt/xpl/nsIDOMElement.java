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
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class nsIDOMElement extends nsIDOMNode implements Element {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + 16;

	public static final String NS_IDOMELEMENT_IID_STRING =
		"a6cf9078-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMELEMENT_IID =
		new nsID(NS_IDOMELEMENT_IID_STRING);

	public nsIDOMElement(int address) {
		super(address);
	}

	public int GetTagName(int aTagName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aTagName);
	}

	public int GetAttribute(int name, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), name, _retval);
	}

	public int SetAttribute(int name, int value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), name, value);
	}

	public int RemoveAttribute(int name) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), name);
	}

	public int GetAttributeNode(int name, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), name, _retval);
	}

	public int SetAttributeNode(int newAttr, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), newAttr, _retval);
	}

	public int RemoveAttributeNode(int oldAttr, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), oldAttr, _retval);
	}

	public int GetElementsByTagName(int name, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), name, _retval);
	}

	public int GetAttributeNS(int namespaceURI, int localName, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), namespaceURI, localName, _retval);
	}

	public int SetAttributeNS(int namespaceURI, int qualifiedName, int value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), namespaceURI, qualifiedName, value);
	}

	public int RemoveAttributeNS(int namespaceURI, int localName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), namespaceURI, localName);
	}

	public int GetAttributeNodeNS(int namespaceURI, int localName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), namespaceURI, localName, _retval);
	}

	public int SetAttributeNodeNS(int newAttr, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), newAttr, _retval);
	}

	public int GetElementsByTagNameNS(int namespaceURI, int localName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), namespaceURI, localName, _retval);
	}

	public int HasAttribute(int name, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), name, _retval);
	}

	public int HasAttributeNS(int namespaceURI, int localName, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), namespaceURI, localName, _retval);
	}

	//=========================================================================

	/**
     * Retrieves an attribute value by name.
     * @param name The name of the attribute to retrieve.
     * @return The <code>Attr</code> value as a string, or the empty string 
     *   if that attribute does not have a specified or default value.
	 */
	public String getAttribute(String name) {
		nsString nsName = new nsString(name);
		nsString nsValue = new nsString();
		int rc = GetAttribute(nsName.getAddress(), nsValue.getAddress());
		nsName.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
		String value = nsValue.toString();
		nsValue.dispose();
		return value;
	}

    /**
     * Adds a new attribute. If an attribute with that name is already present 
     * in the element, its value is changed to be that of the value parameter. 
     * @param name The name of the attribute to create or alter.
     * @param value Value to set in string form.
     */
	public void setAttribute(String name, String value) {
		nsString nsName = new nsString(name);
		nsString nsValue = new nsString(value);
		int rc = SetAttribute(nsName.getAddress(), nsValue.getAddress());
		nsName.dispose();
		nsValue.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
	}

    /**
     * Retrieves an attribute node by name.
     * @param name The name (<code>nodeName</code>) of the attribute to 
     *   retrieve.
     * @return The <code>nsIDOMAttr</code> node with the specified name (
     *   <code>nodeName</code>) or <code>null</code> if there is no such 
     *   attribute.
     */
	public Attr getAttributeNode(String name) {
		nsString nsName = new nsString(name);
		int[] result = new int[1];
		int rc = GetAttributeNode(nsName.getAddress(), result);
		nsName.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsIDOMAttr(result[0]);
		}
	}

    /**
     * Adds a new attribute node. If an attribute with that name (
     * <code>nodeName</code>) is already present in the element, it is 
     * replaced by the new one.
     * @param newAttr The <code>nsIDOMAttr</code> node to add to the attribute list.
     * @return If the <code>newAttr</code> attribute replaces an existing 
     *   attribute, the replaced <code>Attr</code> node is returned, 
     *   otherwise <code>null</code> is returned.
     */
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		int[] result = new int[] {0};
		int rc = SetAttributeNode(((nsIDOMAttr)newAttr).getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		return (Attr)getNodeAtAddress(result[0]);
	}

    /**
     * Removes an attribute by name. If the removed attribute is known to have 
     * a default value, an attribute immediately appears containing the 
     * default value as well as the corresponding namespace URI, local name, 
     * and prefix when applicable.
     * @param name The name of the attribute to remove.
     */
	public void removeAttribute(String name) {
		nsString nsName = new nsString(name);
		int rc = RemoveAttribute(nsName.getAddress());
		nsName.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
	}
	
	/**
     * Removes the specified attribute node. If the removed <code>Attr</code> 
     * has a default value it is immediately replaced.
     * @param oldAttr The <code>nsIDOMAttr</code> node to remove from the attribute 
     *   list.
     * @return The <code>nsIDOMAttr</code> node that was removed.
     */
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		int[] result = new int[] {0};
		int rc = RemoveAttributeNode(((nsIDOMAttr)oldAttr).getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		nsIDOMAttr domAttr = new nsIDOMAttr(result[0]);
		return domAttr;
	}

	public String getTagName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasAttribute(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public boolean hasAttributeNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return false;
	}

	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeList getElementsByTagName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttributeNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public Attr getAttributeNodeNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIdAttribute(String name, boolean isId) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
}

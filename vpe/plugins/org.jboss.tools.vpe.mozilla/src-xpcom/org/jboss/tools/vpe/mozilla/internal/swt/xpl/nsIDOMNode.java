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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class nsIDOMNode extends nsISupports implements Node {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 25;

	public static final String NS_IDOMNODE_IID_STRING =
		"a6cf907c-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMNODE_IID =
		new nsID(NS_IDOMNODE_IID_STRING);

	public nsIDOMNode(int address) {
		super(address);
	}

	public static final int ELEMENT_NODE = 1;
	public static final int ATTRIBUTE_NODE = 2;
	public static final int TEXT_NODE = 3;
	public static final int CDATA_SECTION_NODE = 4;
	public static final int ENTITY_REFERENCE_NODE = 5;
	public static final int ENTITY_NODE = 6;
	public static final int PROCESSING_INSTRUCTION_NODE = 7;
	public static final int COMMENT_NODE = 8;
	public static final int DOCUMENT_NODE = 9;
	public static final int DOCUMENT_TYPE_NODE = 10;
	public static final int DOCUMENT_FRAGMENT_NODE = 11;
	public static final int NOTATION_NODE = 12;

	public int GetNodeName(int aNodeName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aNodeName);
	}

	public int GetNodeValue(int aNodeValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aNodeValue);
	}

	public int SetNodeValue(int aNodeValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aNodeValue);
	}

	public int GetNodeType(int aNodeType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aNodeType);
	}

	public int GetParentNode(int[] aParentNode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aParentNode);
	}

	public int GetChildNodes(int[] aChildNodes) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aChildNodes);
	}

	public int GetFirstChild(int[] aFirstChild) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aFirstChild);
	}

	public int GetLastChild(int[] aLastChild) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aLastChild);
	}

	public int GetPreviousSibling(int[] aPreviousSibling) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aPreviousSibling);
	}

	public int GetNextSibling(int[] aNextSibling) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aNextSibling);
	}

	public int GetAttributes(int[] aAttributes) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aAttributes);
	}

	public int GetOwnerDocument(int[] aOwnerDocument) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aOwnerDocument);
	}

	public int InsertBefore(int newChild, int refChild, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), newChild, refChild, _retval);
	}

	public int ReplaceChild(int newChild, int oldChild, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), newChild, oldChild, _retval);
	}

	public int RemoveChild(int oldChild, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), oldChild, _retval);
	}

	public int AppendChild(int newChild, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), newChild, _retval);
	}

	public int HasChildNodes(int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), _retval);
	}

	public int CloneNode(boolean deep, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), deep, _retval);
	}

	public int Normalize() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress());
	}

	public int IsSupported(int feature, int version, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), feature, version, _retval);
	}

	public int GetNamespaceURI(int aNamespaceURI) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aNamespaceURI);
	}

	public int GetPrefix(int aPrefix) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), aPrefix);
	}

	public int SetPrefix(int aPrefix) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 23, getAddress(), aPrefix);
	}

	public int GetLocalName(int aLocalName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), aLocalName);
	}

	public int HasAttributes(int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 25, getAddress(), _retval);
	}

	//=========================================================================

	private static int getNodeType(int address) {
		int ptrType = XPCOM.PR_Malloc(2);
		int rc = XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, address, ptrType);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] aType = new int[] {0};
		XPCOM.memmove(aType, ptrType, 2);
		XPCOM.PR_Free(ptrType);
		return aType[0];
	}

	public static nsIDOMNode getNodeAtAddress(int address) {
		if (address == 0) return null;
		
		switch (getNodeType(address)) {
		case nsIDOMNode.ELEMENT_NODE:
			return new nsIDOMElement(address);
		case nsIDOMNode.ATTRIBUTE_NODE:
			return new nsIDOMAttr(address);
		case nsIDOMNode.TEXT_NODE:
			return new nsIDOMText(address);
		case nsIDOMNode.COMMENT_NODE:
			return new nsIDOMComment(address);
		case nsIDOMNode.DOCUMENT_NODE:
			return new nsIDOMDocument(address);
		default:
			return new nsIDOMNode(address);
		}
	}

	//=========================================================================

    /**
     * A code representing the type of the underlying object.
     */
	public short getNodeType() {
		int ptrType = XPCOM.PR_Malloc(2);
		int rc = GetNodeType(ptrType);
		if (rc != XPCOM.NS_OK) error(rc);
		int[] aType = new int[] {0};
		XPCOM.memmove(aType, ptrType, 2);
		XPCOM.PR_Free(ptrType);
		return (short)aType[0];
	}

    /**
     * The name of this node, depending on its type.
     */
	public String getNodeName() {
		nsString nsName = new nsString();
		int rc = GetNodeName(nsName.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String name = nsName.toString();
		nsName.dispose();
		return name;
	}

    /**
     * The value of this node, depending on its type. 
     * When it is defined to be <code>null</code>, setting it has no effect.
     */
	public String getNodeValue() {
		nsString nsValue = new nsString();
		int rc = GetNodeValue(nsValue.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String value = nsValue.toString();
		nsValue.dispose();
		return value;
	}

    /**
     * The value of this node, depending on its type. 
     * When it is defined to be <code>null</code>, setting it has no effect.
     */
	public void setNodeValue(String value) {
		nsString nsValue = new nsString(value);
		int rc = SetNodeValue(nsValue.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		nsValue.dispose();
	}

    /**
     * The parent of this node. All nodes, except <code>nsIDOMAttr</code>, 
     * <code>nsIDOMDocument</code> may have a parent. 
     * However, if a node has just been created and not yet added to the 
     * tree, or if it has been removed from the tree, this is 
     * <code>null</code>.
     */
	public Node getParentNode() {
		int[] result = new int[1];
		int rc = GetParentNode(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return getNodeAtAddress(result[0]);
	}

    /**
     * The <code>nsIDOMDocument</code> object associated with this node
     */
	public Document getOwnerDocument() {
		int[] result = new int[1];
		int rc = GetOwnerDocument(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return new nsIDOMDocument(result[0]);
	}

    /**
     * A <code>nsIDOMNodeList</code> that contains all children of this node. If 
     * there are no children, this is a <code>nsIDOMNodeList</code> containing no 
     * nodes.
     */
	public NodeList getChildNodes() {
		int[] result = new int[1];
		int rc = GetChildNodes(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		nsIDOMNodeList domNodeList = new nsIDOMNodeList(result[0]);
		return domNodeList;
	}

	public Node getChildNode(int index) {
		Node child = null;
		NodeList children = getChildNodes();
		if (children != null && index >= 0 && index < children.getLength()) {
			child = children.item(index);
			((nsISupports)children).Release();
		}
		return child;
	}

	public int getChildCount() {
		int count = 0;
		NodeList children = getChildNodes();
		if (children != null) {
			count = children.getLength();
			((nsISupports)children).Release();
		}
		return count;
	}

    /**
     * A <code>nsIDOMNamedNodeMap</code> containing the attributes of this node (if 
     * it is an <code>nsIDOMElement</code>) or <code>null</code> otherwise.
     */
	public NamedNodeMap getAttributes() {
		int[] result = new int[1];
		int rc = GetAttributes(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		nsIDOMNamedNodeMap domNamedNodeMap = new nsIDOMNamedNodeMap(result[0]);
		return domNamedNodeMap;
	}

    /**
     * Adds the node <code>newChild</code> to the end of the list of children 
     * of this node. If the <code>newChild</code> is already in the tree, it 
     * is first removed.
     * @param newChild The node to add.
     * @return The node added.
     */
	public Node appendChild(Node newChild) throws DOMException {
		int[] result = new int[1];
		int rc = AppendChild(((nsIDOMNode)newChild).getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return getNodeAtAddress(result[0]);
	}

    /**
     * Removes the child node indicated by <code>oldChild</code> from the list 
     * of children, and returns it.
     * @param oldChild The node being removed.
     * @return The node removed.
     */
	public Node removeChild(Node oldChild) throws DOMException {
		int[] result = new int[1];
		int rc = RemoveChild(((nsIDOMNode)oldChild).getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return getNodeAtAddress(result[0]);
	}

    /**
     * Replaces the child node <code>oldChild</code> with <code>newChild</code>
     *  in the list of children, and returns the <code>oldChild</code> node.
     * If the <code>newChild</code> is already in the tree, it is first removed.
     * @param newChild The new node to put in the child list.
     * @param oldChild The node being replaced in the list.
     * @return The node replaced.
     */
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		int[] result = new int[1];
		int rc = ReplaceChild(((nsIDOMNode)newChild).getAddress(), ((nsIDOMNode)oldChild).getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return getNodeAtAddress(result[0]);
	}

    /**
     * Inserts the node <code>newChild</code> before the existing child node 
     * <code>refChild</code>. If <code>refChild</code> is <code>null</code>, 
     * insert <code>newChild</code> at the end of the list of children.
     * If the <code>newChild</code> is already in the tree, it is first removed.
     * @param newChild The node to insert.
     * @param refChild The reference node, i.e., the node before which the 
     *   new node must be inserted.
     * @return The node being inserted.
     */
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		int[] result = new int[] {0};
		int rc = InsertBefore(((nsIDOMNode)newChild).getAddress(), ((nsIDOMNode)refChild).getAddress(), result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		return getNodeAtAddress(result[0]);
	}

    /**
     * The node immediately preceding this node. If there is no such node, 
     * this returns <code>null</code>.
     */
	public Node getPreviousSibling() {
		int[] result = new int[] {0};
		int rc = GetPreviousSibling(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return getNodeAtAddress(result[0]);
	}

    /**
     * The node immediately following this node. If there is no such node, 
     * this returns <code>null</code>.
     */
	public Node getNextSibling() {
		int[] result = new int[] {0};
		int rc = GetNextSibling(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return getNodeAtAddress(result[0]);
	}
	
	public int getOffset() {
		int offset = 0;
		int address = getAddress();
		while ((address = getPreviousSiblingAddress(address)) != 0) {
			release(address);
			offset++;
		}
		return offset;
	}

    /**
     * The namespace prefix of this node, or <code>null</code> if it is unspecified.
     */
	public String getPrefix() {
		nsString nsPrefix = new nsString();
		int rc = GetPrefix(nsPrefix.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String prefix = nsPrefix.toString();
		nsPrefix.dispose();
		return prefix;
	}

	static int getPreviousSiblingAddress(int address) {
		int[] result = new int[] {0};
		int rc = XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, address, result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public void normalize() {
		// TODO Auto-generated method stub
		
	}

	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPrefix(String prefix) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public Node getFirstChild() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getLastChild() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node cloneNode(boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSupported(String feature, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public short compareDocumentPosition(Node other) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
}
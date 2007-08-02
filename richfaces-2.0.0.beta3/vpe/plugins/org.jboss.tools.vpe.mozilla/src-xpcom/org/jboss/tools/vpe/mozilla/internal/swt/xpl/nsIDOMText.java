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
import org.w3c.dom.Text;

public class nsIDOMText extends nsIDOMCharacterData implements Text {

	static final int LAST_METHOD_ID = nsIDOMCharacterData.LAST_METHOD_ID + 1;

	public static final String NS_IDOMTEXT_IID_STRING =
		"a6cf9082-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMTEXT_IID =
		new nsID(NS_IDOMTEXT_IID_STRING);

	public nsIDOMText(int address) {
		super(address);
	}

	public int SplitText(int offset, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), offset, _retval);
	}
	
	//=========================================================================

	public Text splitText(int offset) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteData(int offset, int count) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public String getData() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void replaceData(int offset, int count, String arg) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void insertData(int offset, String arg) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void appendData(String arg) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public void setData(String data) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	public boolean isElementContentWhitespace() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getWholeText() {
		// TODO Auto-generated method stub
		return null;
	}

	public Text replaceWholeText(String content) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
}

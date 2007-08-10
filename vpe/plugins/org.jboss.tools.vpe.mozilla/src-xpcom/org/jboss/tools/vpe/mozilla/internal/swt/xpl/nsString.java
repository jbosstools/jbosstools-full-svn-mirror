/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

public class nsString {
	private int handle;
	
public nsString() {
	handle = XPCOM.nsString_new();
}

public nsString(int handle) {
	this.handle = handle;
}

public nsString(String string) {
	if (string != null) {
	   char[] aString = new char[string.length() + 1];
	   string.getChars(0, string.length(), aString, 0);
	   handle = XPCOM.nsString_new(aString);
	}   
}

public int getAddress() {
	return handle;
}	
	
public String toString() {
	if (handle == 0) return null;
	int length = XPCOM.nsString_Length(handle);
	int buffer = XPCOM.nsString_get(handle);
	char[] dest = new char[length];
	XPCOM.memmove(dest, buffer, length * 2);
	return new String(dest);
}	
	
public void dispose() {
	if (handle == 0) return;			
	XPCOM.nsString_delete(handle);
	handle = 0; 	
}	
}
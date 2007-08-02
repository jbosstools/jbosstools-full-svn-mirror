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

public class nsID {
	
	public int m0;
	public short m1;
	public short m2;
	public byte[] m3 = new byte[8];
	public static final int sizeof = 16;

public nsID() {
}

public nsID(String id) {
	Parse(id);
}
	
public boolean Equals(nsID other) {
	int ptr = XPCOM.nsID_new();
	XPCOM.memmove(ptr, this, nsID.sizeof);
	int otherPtr = XPCOM.nsID_new();
	XPCOM.memmove(otherPtr, other, nsID.sizeof);
	boolean result = XPCOM.nsID_Equals(ptr, otherPtr);
	XPCOM.nsID_delete(ptr);
	XPCOM.nsID_delete(otherPtr);
	return result;
}

public boolean Parse(String aIDStr) {
	int ptr = XPCOM.nsID_new();
	boolean result = XPCOM.nsID_Parse(ptr, aIDStr);
	XPCOM.memmove(this, ptr, nsID.sizeof);
	XPCOM.nsID_delete(ptr);
	return result;
}	


//=========================================================================

public String ToString() {
	return	hexString(m0) + "-" + 
			hexString(m1) + "-" +
			hexString(m2) + "-" +
			hexString(m3[0]) +
			hexString(m3[1]) + "-" +
			hexString(m3[2]) +
			hexString(m3[3]) +
			hexString(m3[4]) +
			hexString(m3[5]) +
			hexString(m3[6]) +
			hexString(m3[7]);
}

private String hexString(int n) {
	String s = Integer.toHexString(n);
	if (s.length() < 8) {
		s = zeroString(8 - s.length()) + s;
	}
	return s;
}

private String hexString(short n) {
	String s = Integer.toHexString(n);
	if (s.length() > 4) {
		s = s.substring(s.length() - 4);
	} else if (s.length() < 4) {
		s = zeroString(4 - s.length()) + s;
	}
	return s;
}

private String hexString(byte n) {
	String s = Integer.toHexString(n);
	if (s.length() > 2) {
		s = s.substring(s.length() - 2);
	} else if (s.length() < 2) {
		s = zeroString(2 - s.length()) + s;
	}
	return s;
}

private String zeroString(int len) {
	StringBuffer s = new StringBuffer(len);
	for (int i = 0; i < len; i++) {
		s.append('0');
	}
	return s.toString();
}
}
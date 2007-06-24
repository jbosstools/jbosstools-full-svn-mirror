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

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOM;

public class PRUnicharUtil {

	public static int length(int address) {
		int length = 0;
		char[] aChar = new char[] {0};
		XPCOM.memmove(aChar, address, 2);
		while (aChar[0] != 0) {
			length++;
			XPCOM.memmove(aChar, address + 2 * length , 2);
		}
		return length;
	}

	public static String toString(int address) {
		if (address == 0) return null;
		int length = length(address);
		if (length <= 0) return "";
		char[] aChars = new char[length];
		XPCOM.memmove(aChars, address, length * 2);
		return new String(aChars);
	}
}

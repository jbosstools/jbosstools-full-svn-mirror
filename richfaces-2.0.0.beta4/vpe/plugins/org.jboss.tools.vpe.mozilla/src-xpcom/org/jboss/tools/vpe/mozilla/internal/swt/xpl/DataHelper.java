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

public class DataHelper {
	
	public static byte[] getNullTerminatedString(String str) {
		byte[] buffer = str.getBytes();
		byte[] buffer1 = new byte[buffer.length + 1];
		System.arraycopy(buffer, 0, buffer1, 0, buffer.length);
		return buffer1;
	}

	public static int focusPos(String text, int textPos) {
		return textPos;
//		String s1 = text.substring(0, Math.min(textPos, text.length()));
//		int startCount = 0;
//		while (s1.length() >= 2 && s1.startsWith("\r\n")) {
//			startCount += 2;
//			s1 = s1.substring(2);
//		}
//		String s2 = s1.replaceAll("\r\n", "");
//		return startCount + (s1.length() + s2.length()) / 2;
	}

	public static int textPos(String text, int focusPos) {
		return focusPos;
//		int startCount = 0;
//		String s0 = text;
//		while (s0.length() >= 2 && s0.startsWith("\r\n")) {
//			startCount += 2;
//			s0 = s0.substring(2);
//		}
//		String s1 = (startCount > 0 ? text.substring(0, startCount) : "") + s0.replaceAll("\r\n", "\u0001");
//		String s2 = s1.substring(0, Math.min(focusPos, s1.length()));
//		String s3 = s2.replaceAll("\u0001", "\r\n");
//		return s3.length();
	}
}

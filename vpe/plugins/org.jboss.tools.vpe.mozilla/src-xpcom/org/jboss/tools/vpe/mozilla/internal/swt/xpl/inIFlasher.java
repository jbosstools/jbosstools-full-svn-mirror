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

public class inIFlasher extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 9;

	public static final String INIFLASHER_IID_STRING =
		"7b4a099f-6f6e-4565-977b-fb622adbff49";

	public static final nsID INIFLASHER_IID =
		new nsID(INIFLASHER_IID_STRING);

	public inIFlasher(int address) {
		super(address);
	}

	public int GetColor(int aColor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aColor);
	}

	public int SetColor(int aColor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aColor);
	}

	public int GetInvert(boolean[] aInvert) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aInvert);
	}

	public int SetInvert(boolean aInvert) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aInvert);
	}

	public int GetThickness(char[] aThickness) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aThickness);
	}

	public int SetThickness(char aThickness) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aThickness);
	}

	public int DrawElementOutline(int aElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aElement);
	}

	public int RepaintElement(int aElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aElement);
	}

	public int ScrollElementIntoView(int aElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aElement);
	}

	//=========================================================================

	public String getColor() {
		nsString nsColor = new nsString();
		int rc = GetColor(nsColor.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		String color = nsColor.toString();
		nsColor.dispose();
		return color;
	}

	public void setColor(String color) {
		nsString nsColor = new nsString(color);
		int rc = SetColor(nsColor.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		nsColor.dispose();
	}

	public boolean getInvert() {
		boolean[] aInvert = new boolean[] {true};
		int rc = GetInvert(aInvert);
		if (rc != XPCOM.NS_OK) error(rc);
		return aInvert[0];
	}

	public void setInvert(boolean invert) {
		int rc = SetInvert(invert);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public int getThickness() {
		char[] aThickness = new char[] {0};
		int rc = GetThickness(aThickness);
		if (rc != XPCOM.NS_OK) error(rc);
		return aThickness[0];
	}

	public void setThickness(int thickness) {
		int rc = SetThickness((char)thickness);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void drawElementOutline(nsIDOMElement element) {
		int rc = DrawElementOutline(element.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void repaintElement(nsIDOMElement element) {
		int rc = RepaintElement(element.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public void scrollElementIntoView(nsIDOMElement element) {
		int rc = ScrollElementIntoView(element.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
	}
}

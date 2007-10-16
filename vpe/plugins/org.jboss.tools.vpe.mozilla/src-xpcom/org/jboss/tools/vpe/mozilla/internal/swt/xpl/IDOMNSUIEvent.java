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

/**
 * @author Daniel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IDOMNSUIEvent extends nsIDOMUIEvent{
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;
	
	/* starting interface:    nsIDOMNSUIEvent */
	public static final String NS_IDOMNSUIEVENT_IID_STR = 
		"a6cf90c4-15b3-11d2-932e-00805f8add32";

	public static final nsID IDOMEVENT_IID =
		new nsID(NS_IDOMEVENT_IID_STRING);

	public IDOMNSUIEvent(int address) {
		super(address);
	}

	public int GetPreventDefault(boolean[] retval){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), retval); 
	}
	
	public int GetLayerX(int[] aLayerX){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aLayerX);
	}
	
	public int GetLayerY(int[] aLayerY){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aLayerY);
	}
	
	public int GetPageX(int[] aPageX){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aPageX);
	}
	
	public int GetPageY(int[] aPageY){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aPageY);
	}
	
	public int GetWhich(int[] aWhich){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aWhich);
	}
	
	public int GetRangeParent(int[] aRangeParent){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aRangeParent);
	}
	
	public int GetRangeOffset(int[] aRangeOffset){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aRangeOffset);
	}
	
	public int GetCancelBubble(boolean[] aCancelBubble){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aCancelBubble);
	}
	
	public int SetCancelBubble(boolean aCancelBubble){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aCancelBubble);
	}
	
	public int GetIsChar(boolean[] aIsChar){
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aIsChar);
	} 

	public boolean getPreventDefault(){
		boolean[] retval = new boolean[1];
		int rc = GetPreventDefault(retval);
		if (rc != XPCOM.NS_OK) error(rc);
		return retval[0];
	}
	
	public int getLayerX(){
		int[] aLayerX = new int[1];
		int rc = GetLayerX(aLayerX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aLayerX[0];
	}
	
	public int getLayerY(){
		int[] aLayerY = new int[1];
		int rc = GetLayerY(aLayerY);
		if (rc != XPCOM.NS_OK) error(rc);
		return aLayerY[0];
	}
	
	public int getPageX(){
		int[] aPageX = new int[1];
		int rc = GetPageX(aPageX);
		if (rc != XPCOM.NS_OK) error(rc);
		return aPageX[0];
	}
	
	public int getPageY(){
		int[] aPageY = new int[1];
		int rc = GetPageY(aPageY);
		if (rc != XPCOM.NS_OK) error(rc);
		return aPageY[0];
	}
	
	public int getWhich(){
		int[] aWhich = new int[1];
		int rc = GetWhich(aWhich);
		if (rc != XPCOM.NS_OK) error(rc);
		return aWhich[0];
	}
	
	public nsIDOMNode getRangeParent(){
		int[] result = new int[] {0};
		int rc = GetRangeParent(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		}
		int nodeAddress = nsISupports.queryInterface(result[0], nsIDOMNode.NS_IDOMNODE_IID);
		return nsIDOMNode.getNodeAtAddress(nodeAddress);
	}
	
	public int getRangeOffset(){
		int[] aRangeOffset = new int[1];
		int rc = GetRangeOffset(aRangeOffset);
		if (rc != XPCOM.NS_OK) error(rc);
		return aRangeOffset[0];
	}
	
	public boolean getCancelBubble(){
		boolean[] aCancelBubble = new boolean[1];
		int rc = GetCancelBubble(aCancelBubble);
		if (rc != XPCOM.NS_OK) error(rc);
		return aCancelBubble[0];
	}
	
	public void setCancelBubble(boolean aCancelBubble){
		int rc = SetCancelBubble(aCancelBubble);
		if (rc != XPCOM.NS_OK) error(rc);
	}
	
	public boolean getIsChar(){
		boolean[] aIsChar = new boolean[1];
		int rc = GetIsChar(aIsChar);
		if (rc != XPCOM.NS_OK) error(rc);
		return aIsChar[0];
	} 
	
}

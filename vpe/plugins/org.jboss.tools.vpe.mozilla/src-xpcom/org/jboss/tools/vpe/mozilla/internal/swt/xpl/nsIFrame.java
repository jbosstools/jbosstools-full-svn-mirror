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

import org.eclipse.swt.graphics.Point;

public class nsIFrame extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 50;

	public static final String NS_IFRAME_IID_STRING =
		"a6cf9050-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IFRAME_IID =
		new nsID(NS_IFRAME_IID_STRING);

	public nsIFrame(int address) {
		super(address);
	}

//	NS_IMETHOD GetContentAndOffsetsFromPoint(nsIPresContext* aCX,
//              const nsPoint&  aPoint,
//              nsIContent **   aNewContent,
//              PRInt32&        aContentOffset,
//              PRInt32&        aContentOffsetEnd,
//              PRBool&         aBeginFrameContent) = 0;

	public int GetContentAndOffsetsFromPoint(int aPresContext, int aPoint, int aNewContent, int aContentOffset, int aContentOffsetEnd, int aBeginFrameContent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), aPresContext, aPoint, aNewContent, aContentOffset, aContentOffsetEnd, aBeginFrameContent);
	}

//	NS_IMETHOD  GetCursor(nsIPresContext* aPresContext,
//              nsPoint&        aPoint,
//              PRInt32&        aCursor) = 0;

	public int GetCursor(int aPresContext, int aPoint, int aCursor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), aPresContext, aPoint, aCursor);
	}

	public int GetOffsetFromView(int aPresContext, int aOffset, int[] aView) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 35, getAddress(), aPresContext, aOffset, aView);
	}

	public int GetSelectionController(int aPresContext, int[] aSelCon) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 41, getAddress(), aPresContext, aSelCon);
	}


//	NS_IMETHOD  GetSelectionController(nsIPresContext *aPresContext, nsISelectionController **aSelCon) = 0;
//	NS_IMETHOD  GetOffsetFromView(nsIPresContext* aPresContext,
//            nsPoint&        aOffset,
//            nsIView**       aView) const = 0;

	//=========================================================================
	
	public void getContentAndOffsetsFromPoint(nsIPresContext presContext, int x, int y) {
		int ptrPoint = XPCOM.PR_Malloc(8);
		XPCOM.memmove(ptrPoint, new int[] {x, y}, 8);
//		XPCOM.memmove(ptrPoint, new int[] {0, 0}, 8);
		int[] aPoint = new int[] {0, 0}; 
		XPCOM.memmove(aPoint, ptrPoint, 8);
		
		int ptrNewContentPtr = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrNewContentPtr, new int[] {0}, 4);
		int ptrContentOffset = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrContentOffset, new int[] {0}, 4);
		int ptrContentOffsetEnd = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrContentOffsetEnd, new int[] {0}, 4);
		int ptrBeginFrameContent = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrBeginFrameContent, new int[] {0}, 4);
		int rc = GetContentAndOffsetsFromPoint(presContext.getAddress(), ptrPoint, ptrNewContentPtr, ptrContentOffset, ptrContentOffsetEnd, ptrBeginFrameContent);
		if (rc != XPCOM.NS_OK) error(rc);
	}
	
	public void getCursor(nsIPresContext presContext) {
		
	}

	public nsISelectionController getSelectionController(nsIPresContext presContext) {
		int[] aSelCon = new int[] {0};
		int rc = GetSelectionController(presContext.getAddress(), aSelCon);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aSelCon[0] == 0) {
			return null;
		} else {
			return new nsISelectionController(aSelCon[0]);
		}
	}

	public nsILineIterator getLineIterator() {
		return new nsILineIterator(queryInterface(getAddress(), nsILineIterator.NS_ILINE_ITERATOR_IID));
	}
	
//	public int getOffsetFromView(int aPresContext, int aOffset, int[] aView) {
//		return 0;
//	}
}

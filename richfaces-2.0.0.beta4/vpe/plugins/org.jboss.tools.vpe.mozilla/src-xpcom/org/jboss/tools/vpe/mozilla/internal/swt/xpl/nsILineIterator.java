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

import org.eclipse.swt.graphics.Rectangle;

public class nsILineIterator extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 7;

	public static final String NS_ILINE_ITERATOR_IID_STRING =
		"a6cf90ff-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_ILINE_ITERATOR_IID =
		new nsID(NS_ILINE_ITERATOR_IID_STRING);

	public nsILineIterator(int address) {
		super(address);
	}

	public int GetNumLines(int[] aResult) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aResult);
	}

	public int GetDirection(boolean[] aIsRightToLeft) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aIsRightToLeft);
	}

	public int GetLine(int aLineNumber, int aFirstFrameOnLine, int aNumFramesOnLine, int aLineBounds, int aLineFlags) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aLineNumber, aFirstFrameOnLine, aNumFramesOnLine, aLineBounds, aLineFlags);
	}

	public int FindLineContaining(int aFrame, int[] aLineNumberResult) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aFrame, aLineNumberResult);
	}

	public int FindLineAt(int aY, int[] aLineNumberResult) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aY, aLineNumberResult);
	}


//	  NS_IMETHOD FindLineAt(nscoord aY,
//            PRInt32* aLineNumberResult) = 0;

	//=========================================================================

	public int getNumLines() {
		int[] result = new int[] {0};
		int rc = GetNumLines(result);
		if (rc != XPCOM.NS_OK) error(rc);
		return result[0];
	}

	public boolean isIsRightToLeftDirection() {
		boolean[] aIsRightToLeft = new boolean[] {false};
		int rc = GetDirection(aIsRightToLeft);
		if (rc != XPCOM.NS_OK) error(rc);
		return aIsRightToLeft[0];
	}
	
	public nsILine getLine(int lineNumber) {
		int firstFramePtr = XPCOM.PR_Malloc(4);
		int numFramesPtr = XPCOM.PR_Malloc(4);
		int lineBoundsPtr = XPCOM.PR_Malloc(16);
		int lineFlagsPtr = XPCOM.PR_Malloc(4);

		int rc = GetLine(lineNumber, firstFramePtr, numFramesPtr, lineBoundsPtr, lineFlagsPtr);
		if (rc != XPCOM.NS_OK) error(rc);

		int[] firstFrameBuffer = new int[] {0};
		XPCOM.memmove(firstFrameBuffer, firstFramePtr, 4);
		XPCOM.PR_Free(firstFramePtr);
		nsIFrame firstFrame = null;
		if (firstFrameBuffer[0] != 0) firstFrame = new nsIFrame(firstFrameBuffer[0]);

		int[] numFramesBuffer = new int[] {0};
		XPCOM.memmove(numFramesBuffer, numFramesPtr, 4);
		XPCOM.PR_Free(numFramesPtr);

		int[] lineBoundsXBuffer = new int[] {0};
		int[] lineBoundsYBuffer = new int[] {0};
		int[] lineBoundsWidthBuffer = new int[] {0};
		int[] lineBoundsHeightBuffer = new int[] {0};
		XPCOM.memmove(lineBoundsXBuffer, lineBoundsPtr, 4);
		XPCOM.memmove(lineBoundsYBuffer, lineBoundsPtr + 4, 4);
		XPCOM.memmove(lineBoundsWidthBuffer, lineBoundsPtr + 8, 4);
		XPCOM.memmove(lineBoundsHeightBuffer, lineBoundsPtr + 12, 4);
		XPCOM.PR_Free(lineBoundsPtr);
		Rectangle rect = new Rectangle(lineBoundsXBuffer[0], lineBoundsYBuffer[0], lineBoundsWidthBuffer[0], lineBoundsHeightBuffer[0]);
		
		int[] lineFlagsBuffer = new int[] {0};
		XPCOM.memmove(lineFlagsBuffer, lineFlagsPtr, 4);
		XPCOM.PR_Free(lineFlagsPtr);
		
		return new nsILine(firstFrame, numFramesBuffer[0], rect, lineFlagsBuffer[0]);
	}

	public int findLineAt(int y) {
		int[] aLineNumberResult = new int[] {0};
		int rc = FindLineAt(y, aLineNumberResult);
		if (rc != XPCOM.NS_OK) error(rc);
		return aLineNumberResult[0];
	}
}

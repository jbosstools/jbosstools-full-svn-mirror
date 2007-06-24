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

public class nsITransferable extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_ITRANSFERABLE_IID_STRING =
		"8b5314bc-db01-11d2-96ce-0060b0fb9956";

	public static final nsID NS_ITRANSFERABLE_IID =
		new nsID(NS_ITRANSFERABLE_IID_STRING);

	public nsITransferable(int address) {
		super(address);
	}

	public static final String kHTMLMime = "text/html";
	public static final String kURLDataMime = "text/x-moz-url-data";
	public static final String kFileMime = "application/x-moz-file";
	public static final String kURLMime = "text/x-moz-url";
	public static final String kUnicodeMime = "text/unicode";
	public static final String kNativeHTMLMime = "application/x-moz-nativehtml";

	public int FlavorsTransferableCanExport(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), _retval);
	}

//	  NS_IMETHOD GetTransferData(const char *aFlavor, nsISupports **aData, PRUint32 *aDataLen); \
	public int GetTransferData(byte[]  aFlavor, int aData, int aDataLen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aFlavor, aData, aDataLen);
	}

//	  NS_IMETHOD GetAnyTransferData(char **aFlavor, nsISupports **aData, PRUint32 *aDataLen); \
	public int GetAnyTransferData(int aFlavor, int aData, int[] aDataLen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aFlavor, aData, aDataLen);
	}
	
	public int IsLargeDataSet(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), _retval);
	}

	public int FlavorsTransferableCanImport(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), _retval);
	}

	public int SetTransferData(byte[] aFlavor, int aData, int aDataLen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aFlavor, aData, aDataLen);
	}

	public int AddDataFlavor(byte[] aDataFlavor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aDataFlavor);
	}

	public int RemoveDataFlavor(byte[] aDataFlavor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aDataFlavor);
	}

	public int GetConverter(int[] aConverter) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aConverter);
	}

	public int SetConverter(int aConverter) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aConverter);
	}

	//=========================================================================

	public void getTransferData(String flavor) {
		int ptrDataPtr = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrDataPtr, new int[] {0}, 4);
		int ptrDataLen = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrDataLen, new int[] {0}, 4);
		int rc = GetTransferData(DataHelper.getNullTerminatedString(flavor), ptrDataPtr, ptrDataLen);

		int type = 0;
		
		if (rc == XPCOM.NS_OK) {
			String data = null;
			int[] aDataPtr = new int[] {0};
			XPCOM.memmove(aDataPtr, ptrDataPtr, 4);
			if (aDataPtr[0] != 0) {
				int aSupportsPrimitive = nsISupports.queryInterface(aDataPtr[0], nsISupportsPrimitive.NS_ISUPPORTSPRIMITIVE_IID);
				nsISupportsPrimitive supportsPrimitive = new nsISupportsPrimitive(aSupportsPrimitive);
				type = supportsPrimitive.getType();
				if (type == nsISupportsPrimitive.TYPE_STRING) {
					int aSupportsString = nsISupports.queryInterface(supportsPrimitive, nsISupportsString.NS_ISUPPORTSSTRING_IID);
					nsISupportsString supportsString = new nsISupportsString(aSupportsString);
					data = supportsString.getData();
				}
			}
		}
	}

	public void getAnyTransferData() {
		int ptrFlavorPtr = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrFlavorPtr, new int[] {0}, 4);
		int ptrDataPtr = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrDataPtr, new int[] {0}, 4);
		int[] aDataLen = new int[] {0};
		int rc = GetAnyTransferData(ptrFlavorPtr, ptrDataPtr, aDataLen);
		if (rc == XPCOM.NS_OK) {
			String flavor = null;
			int[] aFlavorPtr = new int[] {0};
			XPCOM.memmove(aFlavorPtr, ptrFlavorPtr, 4);
			if (aFlavorPtr[0] != 0) {
				int flavorLength = XPCOM.strlen(aFlavorPtr[0]);
				byte[] aFlavor = new byte[flavorLength];
				XPCOM.memmove(aFlavor, aFlavorPtr[0], flavorLength);
				flavor = new String(aFlavor);
			}

			int type = 0;
			String data = null;
			int[] aDataPtr = new int[] {0};
			XPCOM.memmove(aDataPtr, ptrDataPtr, 4);
			if (aDataPtr[0] != 0) {
				int aSupportsPrimitive = nsISupports.queryInterface(aDataPtr[0], nsISupportsPrimitive.NS_ISUPPORTSPRIMITIVE_IID);
				nsISupportsPrimitive supportsPrimitive = new nsISupportsPrimitive(aSupportsPrimitive);
				type = supportsPrimitive.getType();
				if (type == nsISupportsPrimitive.TYPE_STRING) {
					int aSupportsString = nsISupports.queryInterface(supportsPrimitive, nsISupportsString.NS_ISUPPORTSSTRING_IID);
					nsISupportsString supportsString = new nsISupportsString(aSupportsString);
					data = supportsString.getData();
				}
			}
		}
		XPCOM.PR_Free(ptrFlavorPtr);
		XPCOM.PR_Free(ptrDataPtr);
		
//		if (rc != XPCOM.NS_OK) error(rc);
	}

//	public int GetAnyTransferData(int aFlavor, int aData, int[] aDataLen) {
//		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aFlavor, aData, aDataLen);
//	}

	public nsISupportsArray flavorsTransferableCanExport() {
		int[] result = new int[1];
		int rc = FlavorsTransferableCanExport(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsISupportsArray(result[0]);
		}
	}
	
	public nsISupportsArray flavorsTransferableCanImport() {
		int[] result = new int[1];
		int rc = FlavorsTransferableCanImport(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) {
			return null;
		} else {
			return new nsISupportsArray(result[0]);
		}
	}

	public void addDataFlavor(String flavor) {
		int rc = AddDataFlavor(DataHelper.getNullTerminatedString(flavor));

//		char[] buffer = new char[aDataFlavor.length() + 1];
//		aDataFlavor.getChars(0, aDataFlavor.length(), buffer, 0);
//		buffer[aDataFlavor.length()] = 0;
//		int rc = AddDataFlavor(buffer);
		if (rc != XPCOM.NS_OK) error(rc);
	}

	public boolean isMozillaTransferData() {
		int ptrFlavor = XPCOM.PR_Malloc(4);
		XPCOM.memmove(ptrFlavor, new int[] {0}, 4);
		int ptrData = XPCOM.PR_Malloc(4);
		int[] aDataLen = new int[] {0};
		XPCOM.memmove(ptrData, new int[] {0}, 4);
		int rc = GetAnyTransferData(ptrFlavor, ptrData, aDataLen);
		return rc == XPCOM.NS_OK;
	}
}

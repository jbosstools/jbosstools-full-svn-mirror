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

public class nsIFile extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 45;

	public static final String NS_IFILE_IID_STRING =
		"c8c0a080-0868-11d3-915f-d9d889d48e3c";

	public static final nsID NS_IFILE_IID =
		new nsID(NS_IFILE_IID_STRING);

	public nsIFile(int address) {
		super(address);
	}

	public static final int NORMAL_FILE_TYPE = 0;

	public static final int DIRECTORY_TYPE = 1;

	public int Append(int node) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), node);
	}

	public int AppendNative(int node) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), node);
	}

	public int Normalize() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress());
	}

	public int Create(int type, int permissions) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), type, permissions);
	}

	public int GetLeafName(int aLeafName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aLeafName);
	}

	public int SetLeafName(int aLeafName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aLeafName);
	}

	public int GetNativeLeafName(int aNativeLeafName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aNativeLeafName);
	}

	public int SetNativeLeafName(int aNativeLeafName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aNativeLeafName);
	}

	public int CopyTo(int newParentDir, int newName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), newParentDir, newName);
	}

	public int CopyToNative(int newParentDir, int newName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), newParentDir, newName);
	}

	public int CopyToFollowingLinks(int newParentDir, int newName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), newParentDir, newName);
	}

	public int CopyToFollowingLinksNative(int newParentDir, int newName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), newParentDir, newName);
	}

	public int MoveTo(int newParentDir, int newName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), newParentDir, newName);
	}

	public int MoveToNative(int newParentDir, int newName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), newParentDir, newName);
	}

	public int Remove(boolean recursive) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), recursive);
	}

	public int GetPermissions(int[] permissions) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), permissions);
	}

	public int SetPermissions(int permissions) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), permissions);
	}

	public int GetPermissionsOfLink(int[] aPermissionsOfLink) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), aPermissionsOfLink);
	}

	public int SetPermissionsOfLink(int aPermissionsOfLink) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), aPermissionsOfLink);
	}

	public int GetLastModifiedTime(long[] aLastModifiedTime) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aLastModifiedTime);
	}

	public int SetLastModifiedTime(long aLastModifiedTime) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aLastModifiedTime);
	}

	public int GetLastModifiedTimeOfLink(long[] aLastModifiedTimeOfLink) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), aLastModifiedTimeOfLink);
	}

	public int SetLastModifiedTimeOfLink(long aLastModifiedTimeOfLink) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 23, getAddress(), aLastModifiedTimeOfLink);
	}

	public int GetFileSize(long[] aFileSize) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), aFileSize);
	}

	public int SetFileSize(long aFileSize) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 25, getAddress(), aFileSize);
	}

	public int GetFileSizeOfLink(long[] aFileSizeOfLink) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 26, getAddress(), aFileSizeOfLink);
	}

	public int GetTarget(int aTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 27, getAddress(), aTarget);
	}

	public int GetNativeTarget(int aNativeTarget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 28, getAddress(), aNativeTarget);
	}

	public int GetPath(int aPath) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 29, getAddress(), aPath);
	}

	public int GetNativePath(int aNativePath) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 30, getAddress(), aNativePath);
	}

	public int Exists(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 31, getAddress(), _retval);
	}

	public int IsWritable(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 32, getAddress(), _retval);
	}

	public int IsReadable(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 33, getAddress(), _retval);
	}

	public int IsExecutable(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 34, getAddress(), _retval);
	}

	public int IsHidden(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 35, getAddress(), _retval);
	}

	public int IsDirectory(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 36, getAddress(), _retval);
	}

	public int IsFile(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 37, getAddress(), _retval);
	}

	public int IsSymlink(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 38, getAddress(), _retval);
	}

	public int IsSpecial(boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 39, getAddress(), _retval);
	}

	public int CreateUnique(int type, int permissions) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 40, getAddress(), type, permissions);
	}

	public int Clone(int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 41, getAddress(), _retval);
	}

	public int Equals(int inFile, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 42, getAddress(), inFile, _retval);
	}

	public int Contains(int inFile, boolean recur, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 43, getAddress(), inFile, recur, _retval);
	}

	public int GetParent(int[] aParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 44, getAddress(), aParent);
	}

	public int GetDirectoryEntries(int[] directoryEntries) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 45, getAddress(), directoryEntries);
	}
}
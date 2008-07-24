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
package org.jboss.tools.vpe.editor.template;

import org.eclipse.core.resources.IFile;


public class VpeIncludeList{
	private IFile[] fileList = new IFile[0];
	
	public VpeIncludeList(){
	}
	
	public IFile[] getFileList(){
		return fileList;
	}
	
	public boolean includesRefresh(){
		boolean sync;
		for(int i=0; i < fileList.length;i++){
			sync = fileList[i].isSynchronized(0);
			if(!sync)return true;
		}
		return false;
	}
	
	public void addIncludeModel(IFile model) {
		if (model != null) {
			IFile[] newBundleMapListener = new IFile[fileList.length + 1];
			System.arraycopy(fileList, 0, newBundleMapListener, 0, fileList.length);
			fileList = newBundleMapListener;
			fileList[fileList.length - 1] = model;
		}
	}
	
	public void removeIncludeModel(IFile model) {
		if (model == null || fileList.length == 0) return;
		int index = -1;
		for (int i = 0; i < fileList.length; i++) {
			if (model == fileList[i]){
				index = i;
				break;
			}
		}
		if (index == -1) return;
		if (fileList.length == 1) {
			fileList = new IFile[0];
			return;
		}
		IFile[] newBundleMapListener = new IFile[fileList.length - 1];
		System.arraycopy(fileList, 0, newBundleMapListener, 0, index);
		System.arraycopy(fileList, index + 1, newBundleMapListener, index, fileList.length - index - 1);
		fileList = newBundleMapListener;
	}
}

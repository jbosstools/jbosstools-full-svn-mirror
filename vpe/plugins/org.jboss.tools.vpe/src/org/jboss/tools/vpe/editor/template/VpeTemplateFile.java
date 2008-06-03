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

import java.io.File;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;

public class VpeTemplateFile {
	private IPath path;
	long stamp;
	IConfigurationElement configElement;
	
	VpeTemplateFile(String fileName,IConfigurationElement element) throws Exception {
		this(element);
		path = VpeTemplateFileList.getFilePath(fileName,element);
		File file = path.toFile();
		if (file.exists() && file.isFile()) {
			stamp = file.lastModified();
		} else {
			throw new Exception("File " + path.toOSString() + " not exist");
		}
	}
	
	VpeTemplateFile(IConfigurationElement element) throws Exception {

		configElement = element;
	}
		
	boolean isEqual(VpeTemplateFile otherTemplateFile) {
		if (otherTemplateFile == null) {
			return false;
		}
		if (!path.equals(otherTemplateFile.getPath())) {
			return false;
		}
		if (stamp != otherTemplateFile.getStamp()) {
			return false;
		}
		return true;
	}
	
	IPath getPath() {
		return path;
	}
	
	long getStamp() {
		return stamp;
	}
	
	public IConfigurationElement getConfigurableElement() { 
		return configElement;
	}
}

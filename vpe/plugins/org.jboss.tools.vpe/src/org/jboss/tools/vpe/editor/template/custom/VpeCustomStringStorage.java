/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template.custom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * @author mareshkau
 *
 */
public class VpeCustomStringStorage implements IStorage {

	private String jarFileContent;
	private String name;

	public VpeCustomStringStorage(String input,String resourceName) {
		this.jarFileContent = input;
		this.name = resourceName;
	}

	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(this.jarFileContent.getBytes());
	}

	public IPath getFullPath() {
		return null;
	}

	public String getName() {
		return this.name;
	}
	/**
	 * 
	 * @return content of file in jar archive
	 */
	public String getContentString() {
		return this.jarFileContent;
	}
	
	public boolean isReadOnly() {
		return true;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

}

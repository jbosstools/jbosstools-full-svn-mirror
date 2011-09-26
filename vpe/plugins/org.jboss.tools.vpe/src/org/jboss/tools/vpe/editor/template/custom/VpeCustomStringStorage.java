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

	@Override
	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(this.jarFileContent.getBytes());
	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
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
	
	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (jarFileContent == null ? 0 : jarFileContent.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VpeCustomStringStorage other = (VpeCustomStringStorage) obj;
		if (jarFileContent == null) {
			if (other.jarFileContent != null) {
				return false;
			}
		} else if (!jarFileContent.equals(other.jarFileContent)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}

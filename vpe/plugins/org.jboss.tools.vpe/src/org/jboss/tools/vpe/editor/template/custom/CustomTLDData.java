/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template.custom;

import org.eclipse.core.runtime.IPath;

/**
 * Class which contains information about 
 * @author mareshkau
 *
 */
public class CustomTLDData  {

	private IPath tldFilePath;
	private String namespace;
	
	/**
	 * 
	 * @param tldFilePath
	 */
	public CustomTLDData(IPath tldFilePath, String namespace) {
		this.tldFilePath=tldFilePath;
		this.namespace=namespace;
	}

	/**
	 * @return the tldFilePath
	 */
	public IPath getTldFilePath() {
		return this.tldFilePath;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return this.namespace;
	}

}

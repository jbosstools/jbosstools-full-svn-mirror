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
package org.jboss.tools.seam.core;

import org.eclipse.core.resources.IFile;

/**
 * Seam Property defined in Component.xml 
 * @param <T>
 */
public interface ISeamProperty<T extends Object> extends ISeamXmlElement {

	public String getName();

	public T getValue();
	public String getStringValue();

	public void setObject(Object value);
}
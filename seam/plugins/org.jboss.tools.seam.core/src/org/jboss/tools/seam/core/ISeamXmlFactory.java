 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.core;

/**
 * @author Alexey Kazakov
 */
public interface ISeamXmlFactory extends ISeamFactory, ISeamTextSourceReference {

	/**
	 * @return value
	 */
	public String getValue();

	/**
	 * Sets value
	 * @param value
	 */
	public void setValue(String value);

	/**
	 * @return method
	 */
	public String getMethod();

	/**
	 * Sets method
	 * @param method
	 */
	public void setMethod(String method);
}
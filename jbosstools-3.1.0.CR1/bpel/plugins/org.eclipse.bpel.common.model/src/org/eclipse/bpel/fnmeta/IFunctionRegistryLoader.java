/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.fnmeta;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Aug 4, 2008
 *
 */
public interface IFunctionRegistryLoader {

	/**
	 * @param registry
	 */
	
	void load ( FunctionRegistry registry );
	
}

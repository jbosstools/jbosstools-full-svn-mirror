/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core;

public class DeltaCloudException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeltaCloudException(String message, Throwable clause)
	{
		super(message, clause);
	}
	
	public DeltaCloudException(Throwable clause)
	{
		super(clause);
	}
	
	public DeltaCloudException(String message)
	{
		super(message);
	}
	
}

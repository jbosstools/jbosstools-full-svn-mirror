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
package org.jboss.tools.deltacloud.core.client;

public class DeltaCloudAuthException extends DeltaCloudClientException {

	private static final long serialVersionUID = 1L;
	
	public DeltaCloudAuthException(String message, Throwable clause)
	{
		super(message, clause);
	}
	
	public DeltaCloudAuthException(Throwable clause)
	{
		super(clause);
	}
	
	public DeltaCloudAuthException(String message)
	{
		super(message);
	}
	
}

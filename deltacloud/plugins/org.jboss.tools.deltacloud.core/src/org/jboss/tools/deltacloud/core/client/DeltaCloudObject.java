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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Martyn Taylor
 */
public abstract class DeltaCloudObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String id;
	
	@SuppressWarnings("unused")
	private void setId(String id) 
	{
		this.id = id;
	}
	
	@XmlAttribute
	public String getId() 
	{
		return id;
	}
}

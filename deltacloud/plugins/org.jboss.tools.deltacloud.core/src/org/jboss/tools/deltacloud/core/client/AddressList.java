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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Martyn Taylor
 * @author André Dietisheim
 */
@XmlType
public class AddressList
{
	private static final long serialVersionUID = 1L;

	@XmlElement
	@XmlList
	private List<String> addresses;

	protected AddressList()
	{
	}
	
	protected AddressList(List<String> addresses)
	{
		setAddress(addresses);
	}

	protected void setAddress(List<String> addresses)
	{
		this.addresses = addresses;
	}

	public List<String> getAddress()
	{
		return addresses;
	}
}

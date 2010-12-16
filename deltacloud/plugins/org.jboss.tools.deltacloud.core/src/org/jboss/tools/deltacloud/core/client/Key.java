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

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Andre Dietisheim
 */
public class Key extends DeltaCloudObject {

	private static final long serialVersionUID = 1L;

	public static enum InstanceState {
		RUNNING, STOPPED, PENDING, TERMINATED, BOGUS
	};

	@XmlElement
	private String name;

	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}
	
}

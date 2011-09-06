/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.openshift.internal.core;

import org.jboss.ide.eclipse.as.openshift.core.IOpenshiftObject;

/**
 * @author André Dietisheim
 */
public class Cartridge implements IOpenshiftObject {

	private String name;

	private Cartridge(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

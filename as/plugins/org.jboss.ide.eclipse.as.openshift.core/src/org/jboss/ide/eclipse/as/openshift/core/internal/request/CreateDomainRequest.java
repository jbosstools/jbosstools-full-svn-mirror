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
package org.jboss.ide.eclipse.as.openshift.core.internal.request;

import org.jboss.ide.eclipse.as.openshift.core.ISSHPublicKey;


/**
 * @author André Dietisheim
 */
public class CreateDomainRequest extends AbstractDomainRequest {

	public CreateDomainRequest(String name, ISSHPublicKey sshKey, ApplicationAction action, String username) {
		this(name, sshKey, username, false);
	}

	public CreateDomainRequest(String name, ISSHPublicKey sshKey, String username, boolean debug) {
		super(name, sshKey, username, debug);
	}

	public boolean isAlter() {
		return false;
	}
}

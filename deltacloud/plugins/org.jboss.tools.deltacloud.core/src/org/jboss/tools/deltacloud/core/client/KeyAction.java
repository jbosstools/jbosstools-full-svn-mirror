/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.core.client;

/**
 * An action that is executable on a deltacloud key
 * 
 * @author André Dietisheim
 */
public class KeyAction extends AbstractDeltaCloudResourceAction implements IKeyAction {

	private Key key;

	protected KeyAction() {
	}

	protected KeyAction(String name, Key key, String url, String method) {
		super(name, url, method);
		this.key = key;
	}

	protected void setKey(Key key) {
		this.key = key;
	}

	@Override
	public Key getKey() {
		return key;
	}
}

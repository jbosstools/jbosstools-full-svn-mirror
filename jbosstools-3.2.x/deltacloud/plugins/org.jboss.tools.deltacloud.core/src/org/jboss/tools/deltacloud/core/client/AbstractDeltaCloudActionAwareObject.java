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

/**
 * @author Martyn Taylor
 * @author André Dietisheim
 */
public abstract class AbstractDeltaCloudActionAwareObject<ACTION> extends AbstractDeltaCloudObject {

	private List<ACTION> actions;

	public void setActions(List<ACTION> actions) {
		this.actions = actions;
	}

	public List<ACTION> getActions() {
		return actions;
	}

}

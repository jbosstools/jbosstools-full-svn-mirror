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
 * An action that is executable on an instance
 * 
 * @author Martin Taylor
 * @author André Dietisheim
 * @see Instance
 * @see DeltaCloudClient#performInstanceAction(String, String);
 *
 */
public class InstanceAction extends AbstractDeltaCloudResourceAction implements IInstanceAction {

	private Instance instance;

	protected InstanceAction() {
		super();
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	@Override
	public Instance getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return "InstanceAction [name=" + getName() + ", url=" + getUrl() + ", method=" + getMethod() + "]";
	}
}

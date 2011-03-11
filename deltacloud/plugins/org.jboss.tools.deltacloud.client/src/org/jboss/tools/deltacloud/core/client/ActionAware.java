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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martyn Taylor
 * @author André Dietisheim
 */
public abstract class ActionAware<OWNER> extends IdAware {

	private List<Action<OWNER>> actions;

	public Action<OWNER> getAction(String name) {
		if (name == null) {
			return null;
		}

		for (Action<OWNER> action : getActions()) {
			if (name.equals(action.getName())) {
				return action;
			}
		}
		return null;
	}

	public List<String> getActionNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Action<OWNER> action : getActions()) {
			names.add(action.getName());
		}
		return names;
	}

	public boolean start(DeltaCloudClient client) throws DeltaCloudClientException {
		InputStream in = client.performAction(getAction(Action.START_NAME));
		update(in);
		return in != null;
	}

	public boolean stop(DeltaCloudClient client) throws DeltaCloudClientException {
		InputStream in = client.performAction(getAction(Action.STOP_NAME));
		update(in);
		return in != null;
	}

	public boolean destroy(DeltaCloudClient client) throws DeltaCloudClientException {
		InputStream in = client.performAction(getAction(Action.DESTROY_NAME));
		return in != null;
	}

	public boolean reboot(DeltaCloudClient client) throws DeltaCloudClientException {
		InputStream in = client.performAction(getAction(Action.REBOOT_NAME));
		update(in);
		return in != null;
	}

	protected void update(InputStream in) throws DeltaCloudClientException {
		if (in == null) {
			return;
		}
		
		doUpdate(in);
	}
	
	protected abstract void doUpdate(InputStream in) throws DeltaCloudClientException;

	public boolean canStart() {
		return getAction(Action.START_NAME) != null;
	}

	public boolean canStop() {
		return getAction(Action.STOP_NAME) != null;
	}

	public boolean canReboot() {
		return getAction(Action.REBOOT_NAME) != null;
	}

	public boolean canDestroy() {
		return getAction(Action.DESTROY_NAME) != null;
	}

	public void setActions(List<Action<OWNER>> actions) {
		this.actions = actions;
	}

	public List<Action<OWNER>> getActions() {
		return actions;
	}
}

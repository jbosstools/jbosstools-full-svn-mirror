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
package org.jboss.tools.deltacloud.core;

import org.apache.deltacloud.client.Action;


/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public enum DeltaCloudResourceAction {

	START(Action.START_NAME),
	STOP(Action.STOP_NAME),
	REBOOT(Action.REBOOT_NAME),
	DESTROY(Action.DESTROY_NAME);

	private String name;

	private DeltaCloudResourceAction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static DeltaCloudResourceAction getByName(String name) {
		DeltaCloudResourceAction action = null;
		if (name != null) {
			for (DeltaCloudResourceAction availableAction : values()) {
				if (name.equalsIgnoreCase(availableAction.name)) {
					action = availableAction;
					break;
				}
			}
		}
		return action;
	}
}

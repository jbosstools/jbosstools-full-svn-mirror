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

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl.DeltaCloudServerType;

/**
 * @author André Dietisheim
 */
public enum Driver {

	MOCK(DeltaCloudServerType.MOCK), EC2(DeltaCloudServerType.EC2), UNKNOWN(DeltaCloudServerType.UNKNOWN);

	private DeltaCloudServerType deltaCloudServerType;

	private Driver(DeltaCloudServerType deltaCloudServerType) {
		this.deltaCloudServerType = deltaCloudServerType;
	}
	
	public static Driver valueOf(DeltaCloudServerType deltaCloudServerType) {
		for(Driver type : values()) {
			if (type.deltaCloudServerType.equals(deltaCloudServerType)) {
				return type;
			}
		}
		return UNKNOWN;
	}

	public static Driver checkedValueOf(String name) {
		for (Driver type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		return UNKNOWN;
	}
}

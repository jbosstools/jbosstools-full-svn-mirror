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

import org.apache.deltacloud.client.API.Driver;


/**
 * @author André Dietisheim
 */
public enum DeltaCloudDriver {

	MOCK(Driver.MOCK), EC2(Driver.EC2), UNKNOWN(Driver.UNKNOWN);

	private Driver driver;

	private DeltaCloudDriver(Driver driver) {
		this.driver = driver;
	}
	
	public static DeltaCloudDriver valueOf(Driver driver) {
		for(DeltaCloudDriver deltaCloudDriver : values()) {
			if (deltaCloudDriver.driver.equals(driver)) {
				return deltaCloudDriver;
			}
		}
		return UNKNOWN;
	}

	public static DeltaCloudDriver checkedValueOf(String name) {
		for (DeltaCloudDriver driver : values()) {
			if (driver.name().equals(name)) {
				return driver;
			}
		}
		return UNKNOWN;
	}
}

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

/**
 * @author Andre Dietisheim
 */
public class API extends AbstractDeltaCloudActionAwareObject<KeyAction> {

	private static final long serialVersionUID = 1L;

	public static enum Driver {
		UNKNOWN, MOCK, EC2;

		public static Driver checkedValueOf(String name) {
			for (Driver driver : values()) {
				if (driver.name().equals(name)) {
					return driver;
				}
			}
			return UNKNOWN;
		}

	}

	private Driver driver;

	public API() {
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public void setDriver(String driver) {
		setDriver(Driver.checkedValueOf(driver.toUpperCase()));
	}

	public Driver getDriver() {
		return driver;
	}

	@Override
	public String toString() {
		return "API [driver=" + driver.name() + super.toString() + "]";
	}
}

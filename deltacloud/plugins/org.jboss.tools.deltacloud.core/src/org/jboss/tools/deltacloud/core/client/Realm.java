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

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Martyn Taylor
 */
public class Realm extends AbstractDeltaCloudObject {
	private static final long serialVersionUID = 1L;

	public static enum RealmState {
		AVAILABLE, UNAVAILABLE, UNKNOWN
	}

	@XmlElement
	private String name;

	@XmlElement
	private RealmState state;

	@XmlElement
	private int limit;

	protected Realm() {
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setState(String state) {
		try {
			this.state = RealmState.valueOf(state);
		} catch (Exception e) {
			this.state = RealmState.UNKNOWN;
		}
	}

	protected void setLimit(int limit) {
		this.limit = limit;
	}

	protected void setLimit(String limit) {
		try {
			this.limit = Integer.parseInt(limit);
		} catch (Exception e) {
			this.limit = -1;
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public RealmState getState() {
		return state;
	}

	public int getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		String s = "";
		s += "Realm:\t\t" + getId() + "\n";
		s += "Name\t\t" + getName() + "\n";
		s += "State:\t\t" + getState() + "\n";
		s += "Limit:\t\t" + getLimit() + "\n";
		return s;
	}
}

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
 * @author Martyn Taylor
 * @author André Dietisheim
 */
public class Realm extends AbstractDeltaCloudObject {
	private static final long serialVersionUID = 1L;

	public static final int LIMIT_DEFAULT = -1;

	private String name;
	private RealmState state;
	private int limit;

	public static enum RealmState {
		AVAILABLE, UNAVAILABLE, UNKNOWN
	}

	public Realm() {
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLimit(String limit) {
		try {
			this.limit = Integer.parseInt(limit);
		} catch (Exception e) {
			this.limit = LIMIT_DEFAULT;
		}
	}
	
	public int getLimit() {
		return limit;
	}

	public String getName() {
		return name;
	}

	public void setState(String state) {
		try {
			this.state = RealmState.valueOf(state.toUpperCase());
		} catch (Exception e) {
			this.state = RealmState.UNKNOWN;
		}
	}

	public RealmState getState() {
		return state;
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

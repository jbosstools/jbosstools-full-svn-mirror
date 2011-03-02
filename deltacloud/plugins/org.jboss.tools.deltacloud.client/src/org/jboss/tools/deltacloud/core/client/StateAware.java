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
 * @author André Dietisheim
 *
 * @param <OWNER>
 */
public class StateAware<OWNER> extends ActionAware<OWNER> {

	public static enum State {
		RUNNING, STOPPED, PENDING, TERMINATED, BOGUS
	};

	private State state;

	public void setState(String state) {
		try {
			this.state = State.valueOf(state);
		} catch (Exception e) {
			this.state = State.BOGUS;
		}
	}

	public State getState() {
		return state;
	}

	public boolean isRunning() {
		return getState() == State.RUNNING;
	}

	public boolean isStopped() {
		return getState() == State.STOPPED;
	}
}

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
 * @author Andre Dietisheim
 */
public enum HttpStatusRange {

	CLIENT_ERROR(400, 499), SERVER_ERROR(500, 599);

	private int start;
	private int stop;

	HttpStatusRange(int start, int stop) {
		this.start = start;
		this.stop = stop;
	}

	public boolean isInRange(int statusCode) {
		return statusCode >= start
				&& statusCode <= stop;
	}
}

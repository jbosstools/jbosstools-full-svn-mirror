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

import java.util.ArrayList;
import java.util.List;

/**
 * An exception that captures several exceptions that may have occured while
 * delaing with the deltacloud server
 */
public class DeltaCloudMultiException extends DeltaCloudException {

	private static final long serialVersionUID = 1L;

	private ArrayList<Throwable> throwables;

	public DeltaCloudMultiException(String message) {
		super(message);
		this.throwables = new ArrayList<Throwable>();
	}

	public void addError(Throwable throwable) {
		throwables.add(throwable);
	}

	public List<Throwable> getThrowables() {
		return throwables;
	}

	public boolean isEmpty() {
		return throwables.size() == 0;
	}
}

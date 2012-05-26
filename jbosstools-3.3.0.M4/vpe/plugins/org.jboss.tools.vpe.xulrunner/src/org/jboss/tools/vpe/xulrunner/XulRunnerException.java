/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner;

public class XulRunnerException extends Exception {
	private static final long serialVersionUID = -7973228286977485907L;

	public XulRunnerException() {
		super();
	}

	public XulRunnerException(String message, Throwable cause) {
		super(message, cause);
	}

	public XulRunnerException(String message) {
		super(message);
	}

	public XulRunnerException(Throwable cause) {
		super(cause);
	}
	
}

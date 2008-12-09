/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor;

/**
 * This exception throws from vpe editor update job,
 * when editor is disposed, but we trying to update preview
 * 
 * Fix for JBIDE-675.
 * 
 * @author mareshkau
 */

public class VpeDisposeException extends RuntimeException {

	private static final long serialVersionUID = 2984302764077199215L;

	public VpeDisposeException() {
		super();
	}

	public VpeDisposeException(String message, Throwable cause) {
		super(message, cause);
	}

	public VpeDisposeException(String message) {
		super(message);
	}

	public VpeDisposeException(Throwable cause) {
		super(cause);
	}
}

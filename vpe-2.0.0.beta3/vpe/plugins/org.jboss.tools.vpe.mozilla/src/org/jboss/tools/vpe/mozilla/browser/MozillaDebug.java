/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.mozilla.browser;

public class MozillaDebug {
	public static final boolean printVisualNoInterface = false;
	public static final boolean debugRefCount = false;

	// usePrintStackTrace = false - The exception is sent on our site 
	// usePrintStackTrace = true - The exception is printed through printStackTrace 
	public static final boolean usePrintStackTrace = false;
}

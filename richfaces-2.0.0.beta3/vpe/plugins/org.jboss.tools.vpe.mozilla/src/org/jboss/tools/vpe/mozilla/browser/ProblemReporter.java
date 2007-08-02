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

import org.jboss.tools.common.reporting.ProblemReportingHelper;


public class ProblemReporter {
	private final static String PLUGIN_ID = "org.jboss.tools.vpe.mozilla";

	public static void reportProblem(Throwable throwable) {
		if (MozillaDebug.usePrintStackTrace) {
			throwable.printStackTrace();
		} else {
			ProblemReportingHelper.reportProblem(PLUGIN_ID, throwable);
		}
	}
}

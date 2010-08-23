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
package org.jboss.tools.usage.preferences;

/**
 * @author Andre Dietisheim
 */
public interface IUsageReportPreferenceConstants {

	/** The value that determines if usage shall be reported if there's no user set setting. */
	public static final boolean USAGEREPORT_ENABLED_DEFAULTVALUE = false;

	/**
	 * The identifier to be used to identify the value that determines if usage
	 * shall be reported.
	 */
	public static final String USAGEREPORT_ENABLED_ID = "allowUsageReportPreference"; //$NON-NLS-1$

	/**
	 * The identifier to be used for the value that determines if the user shall
	 * be asked for reporting.
	 */
	public static final String ASK_USER_USAGEREPORT_ID = "askUserForUsageReportPreference"; //$NON-NLS-1$

	/**
	 * The identifier to be used for the value that determines this eclipse
	 * instance.
	 */
	public static final String ECLIPSE_INSTANCE_ID = "eclipseInstanceId"; //$NON-NLS-1$
	
	public static final String FIRST_VISIT = "firstVisit";

	public static final String LAST_VISIT = "lastVisit";

	public static final String VISIT_COUNT = "visitCount";
}

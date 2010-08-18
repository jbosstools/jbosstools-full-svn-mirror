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
package org.jboss.tools.usage.internal;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.usage.ITracker;
import org.jboss.tools.usage.googleanalytics.FocusPoint;
import org.jboss.tools.usage.googleanalytics.IGoogleAnalyticsParameters;
import org.jboss.tools.usage.googleanalytics.ILoggingAdapter;
import org.jboss.tools.usage.googleanalytics.IURLBuildingStrategy;
import org.jboss.tools.usage.googleanalytics.Tracker;
import org.jboss.tools.usage.preferences.IUsageReportPreferenceConstants;
import org.jboss.tools.usage.util.PreferencesUtil;
import org.jboss.tools.usage.util.StatusUtils;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Andre Dietisheim
 */
public class UsageReport {

	private static final String GANALYTICS_ACCOUNTNAME = Messages.UsageReport_GoogleAnalyticsAccount;

	private static final String HOST_NAME = Messages.UsageReport_HostName;

	private FocusPoint focusPoint = new FocusPoint("jboss.org").setChild(new FocusPoint("tools") //$NON-NLS-1$ //$NON-NLS-2$
			.setChild(new FocusPoint("usage").setChild(new FocusPoint("action") //$NON-NLS-1$ //$NON-NLS-2$
					.setChild(new FocusPoint("wsstartup"))))); //$NON-NLS-1$

	public void report() {

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				UsageReportEnablementDialog dialog = new UsageReportEnablementDialog(Messages.UsageReport_DialogTitle,
						Messages.UsageReport_DialogMessage,
						Messages.UsageReport_Checkbox_Text,
						true,
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				if (isAskUser()) {
					if (dialog.open() == Window.OK) {
						setReportEnabled(dialog.isReportEnabled());
						setAskUser(false);
					}
				}

				if (isReportEnabled()) {
					report(getAnalyticsTracker());
				}
			}
		});
	}

	private void setReportEnabled(boolean enabled) {
		PreferencesUtil.getConfigurationPreferences().putBoolean(
				IUsageReportPreferenceConstants.USAGEREPORT_ENABLED_ID, enabled);
	}

	private void report(ITracker tracker) {
		tracker.trackAsynchronously(focusPoint);
	}

	private ITracker getAnalyticsTracker() {
		IGoogleAnalyticsParameters eclipseSettings = new EclipseEnvironment(
				GANALYTICS_ACCOUNTNAME
				, HOST_NAME
				, IGoogleAnalyticsParameters.VALUE_NO_REFERRAL);
		ILoggingAdapter loggingAdapter = new PluginLogger(JBossToolsUsageActivator.getDefault());
		IURLBuildingStrategy urlStrategy = new GoogleAnalyticsUrlStrategy(eclipseSettings);
		return new Tracker(urlStrategy, eclipseSettings.getUserAgent(), loggingAdapter);
	}

	private boolean isAskUser() {
		IEclipsePreferences preferences = PreferencesUtil.getConfigurationPreferences();
		return preferences.getBoolean(IUsageReportPreferenceConstants.ASK_USER_USAGEREPORT_ID, true);
	}

	private void setAskUser(boolean askUser) {
		try {
			IEclipsePreferences preferences = PreferencesUtil.getConfigurationPreferences();
			preferences.putBoolean(IUsageReportPreferenceConstants.ASK_USER_USAGEREPORT_ID, askUser);
			preferences.flush();
		} catch (BackingStoreException e) {
			JBossToolsUsageActivator.getDefault().getLog().log(
					StatusUtils.getErrorStatus(JBossToolsUsageActivator.PLUGIN_ID,
							Messages.UsageReport_Error_SavePreferences, e,
							IUsageReportPreferenceConstants.ASK_USER_USAGEREPORT_ID));
		}
	}

	private boolean isReportEnabled() {
		return PreferencesUtil.getConfigurationPreferences().getBoolean(
				IUsageReportPreferenceConstants.USAGEREPORT_ENABLED_ID,
				IUsageReportPreferenceConstants.USAGEREPORT_ENABLED_DEFAULTVALUE);
	}
}

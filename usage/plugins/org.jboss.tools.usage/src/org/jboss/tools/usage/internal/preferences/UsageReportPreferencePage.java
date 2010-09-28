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
package org.jboss.tools.usage.internal.preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.usage.googleanalytics.IJBossToolsEclipseEnvironment;
import org.jboss.tools.usage.googleanalytics.eclipse.IEclipseUserAgent;
import org.jboss.tools.usage.internal.JBDSUtils;
import org.jboss.tools.usage.internal.JBossToolsUsageActivator;
import org.jboss.tools.usage.util.StatusUtils;
import org.jboss.tools.usage.util.StringUtils;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Andre Dietisheim
 */
public class UsageReportPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM,
			DateFormat.SHORT);

	public UsageReportPreferencePage() {
		super(GRID);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		createReportingData((Composite) control);
		return control;
	}

	private void createReportingData(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(PreferencesMessages.UsageReportPreferencePage_ReportedValues);
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.FILL, SWT.FILL).applyTo(group);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 4;
		fillLayout.marginWidth = 8;
		group.setLayout(fillLayout);
		StyledText text = new StyledText(group, SWT.BORDER | SWT.V_SCROLL);
		IJBossToolsEclipseEnvironment eclipseEnvironment = JBossToolsUsageActivator.getDefault()
				.getJBossToolsEclipseEnvironment();
		createText(eclipseEnvironment, text);
		text.setEditable(false);
	}

	private void createText(IJBossToolsEclipseEnvironment eclipseEnvironment, StyledText text) {
		List<StyleRange> styles = new ArrayList<StyleRange>();
		StringBuilder builder = new StringBuilder();

		appendLabeledValue("JBoss Tools Version: ", eclipseEnvironment.getJBossToolsVersion(), builder, styles);
		appendLabeledValue("JBoss Tools Components: ", eclipseEnvironment.getKeyword(), builder, styles);
		builder.append(StringUtils.getLineSeparator());

		IEclipseUserAgent eclipseUserAgent = eclipseEnvironment.getEclipseUserAgent();
		appendLabeledValue("Product id: ", eclipseUserAgent.getApplicationName(), builder, styles);

		appendLabeledValue("Product version: ", eclipseUserAgent.getApplicationVersion(), builder, styles);
		builder.append(StringUtils.getLineSeparator());

		appendLabeledValue("Operating system: ", eclipseUserAgent.getOS(), builder, styles);
		appendLabeledValue("Operating system version: ", eclipseUserAgent.getOSVersion(), builder, styles);
		builder.append(StringUtils.getLineSeparator());

		appendLabeledValue("Locale: ", eclipseUserAgent.getBrowserLanguage(), builder, styles);
		appendLabeledValue("Screen Colors: ", eclipseEnvironment.getScreenColorDepth(), builder, styles);
		appendLabeledValue("Screen Resolution: ", eclipseEnvironment.getScreenResolution(), builder, styles);
		builder.append(StringUtils.getLineSeparator());

		appendLabeledValue("Product owner: ", eclipseEnvironment.getHostname(), builder, styles);
		builder.append(StringUtils.getLineSeparator());

		appendLabeledValue("Number of usage-hits: ", String.valueOf(eclipseEnvironment.getVisitCount()), builder,
				styles);
		appendLabeledValue("First usage-hit: ", getFormattedDate(eclipseEnvironment.getFirstVisit()), builder, styles);
		appendLabeledValue("Last usage-hit: ", getFormattedDate(eclipseEnvironment.getLastVisit()), builder, styles);
		appendLabeledValue("Current usage-hit: ", getFormattedDate(eclipseEnvironment.getCurrentVisit()), builder,
				styles);

		text.setText(builder.toString());

		for (StyleRange style : styles) {
			text.setStyleRange(style);
		}

	}

	/**
	 * Appends a labeled value to the given string builder and adds a bold font
	 * style range to the given styles.
	 * 
	 * @param label
	 *            the label to append
	 * @param value
	 *            the value to append
	 * @param builder
	 *            the builder to append the strings (label, value) to
	 * @param styles
	 *            the styles list to add the style range to
	 */
	private void appendLabeledValue(String label, String value, StringBuilder builder, List<StyleRange> styles) {
		StyleRange styleRange = startLabelStyleRange(builder);
		builder.append(label);
		finishLabelStyleRange(builder, styleRange);
		builder.append(value)
				.append(StringUtils.getLineSeparator());
		styles.add(styleRange);
	}

	private StyleRange startLabelStyleRange(StringBuilder builder) {
		StyleRange styleRange = new StyleRange();
		styleRange.start = builder.length();
		styleRange.fontStyle = SWT.BOLD;
		return styleRange;
	}

	private StyleRange finishLabelStyleRange(StringBuilder builder, StyleRange styleRange) {
		styleRange.length = builder.length() - styleRange.start;
		return styleRange;
	}

	public void createFieldEditors() {
		addField(new BooleanFieldEditor(
				IUsageReportPreferenceConstants.USAGEREPORT_ENABLED_ID
				, getCheckBoxlabel()
				, getFieldEditorParent()));
	}

	private String getCheckBoxlabel() {
		if (JBDSUtils.isJBDS()) {
			return PreferencesMessages.UsageReportPreferencePage_AllowReporting_JBDS;
		} else {
			return PreferencesMessages.UsageReportPreferencePage_AllowReporting;
		}
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(UsageReportPreferences.createPreferenceStore());
		setDescription(getPageDescription());
	}

	private String getPageDescription() {
		if (JBDSUtils.isJBDS()) {
			return PreferencesMessages.UsageReportPreferencePage_Description_JBDS;
		} else {
			return PreferencesMessages.UsageReportPreferencePage_Description;
		}
	}

	@Override
	public boolean performOk() {
		try {
			UsageReportPreferences.flush();
		} catch (BackingStoreException e) {
			IStatus status = StatusUtils.getErrorStatus(JBossToolsUsageActivator.PLUGIN_ID,
					getPrefsSaveErrorMessage(), e);
			JBossToolsUsageActivator.getDefault().getLog().log(status);
		}
		return super.performOk();
	}

	private String getPrefsSaveErrorMessage() {
		if (JBDSUtils.isJBDS()) {
			return PreferencesMessages.UsageReportPreferencePage_Error_Saving_JBDS;
		} else {
			return PreferencesMessages.UsageReportPreferencePage_Error_Saving;
		}
	}

	private String getFormattedDate(String timeStamp) {
		try {

			Date date = new Date(Long.parseLong(timeStamp));
			if (date != null) {
				return DATE_FORMAT.format(date);
			}
		} catch (NumberFormatException e) {
		}
		return "";
	}
}
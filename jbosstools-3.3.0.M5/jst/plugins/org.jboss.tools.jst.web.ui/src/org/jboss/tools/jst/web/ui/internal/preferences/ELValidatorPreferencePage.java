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
package org.jboss.tools.jst.web.ui.internal.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.jboss.tools.common.ui.preferences.SeverityPreferencePage;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.validation.ELValidator;

/**
 * @author Viacheslav Kabanovich
 */
public class ELValidatorPreferencePage extends SeverityPreferencePage {
	public static final String PREF_ID = ELValidator.PREFERENCE_PAGE_ID;
	public static final String PROP_ID = "org.jboss.tools.jst.web.ui.properties.ELValidatorPreferencePage"; //$NON-NLS-1$

	public ELValidatorPreferencePage() {
		setPreferenceStore(WebKbPlugin.getDefault().getPreferenceStore());
		setTitle(ELSeverityPreferencesMessages.JSF_VALIDATOR_PREFERENCE_PAGE_JSF_VALIDATOR);
	}

	@Override
	protected String getPreferencePageID() {
		return PREF_ID;
	}

	@Override
	protected String getPropertyPageID() {
		return PROP_ID;
	}

	@Override
	public void createControl(Composite parent) {
		IWorkbenchPreferenceContainer container = (IWorkbenchPreferenceContainer) getContainer();
		fConfigurationBlock = new ELValidatorConfigurationBlock(getNewStatusChangedListener(), getProject(), container);

		super.createControl(parent);
	}
}
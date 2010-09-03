/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class DeltaCloudPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private final static String LAUNCH_WARN = "LaunchWarnPreference.msg"; //$NON-NLS-1$
	
	private Button warnLaunch;
	
	public DeltaCloudPreferencePage() {
		// TODO Auto-generated constructor stub
	}

	public DeltaCloudPreferencePage(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public DeltaCloudPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void performDefaults() {
		warnLaunch.setSelection(false);
	}
	

	@Override
	public boolean performOk() {
		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		
		boolean oldWarn = prefs.getBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE, false);
		if (oldWarn != warnLaunch.getSelection()) {
			prefs.putBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE, warnLaunch.getSelection());
		}
		
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Activator.log(e);
		}
		return super.performOk();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		
		warnLaunch = new Button(container, SWT.CHECK);
		warnLaunch.setText(PreferenceMessages.getString(LAUNCH_WARN));
		warnLaunch.setSelection(prefs.getBoolean(IDeltaCloudPreferenceConstants.DONT_CONFIRM_CREATE_INSTANCE, false));
		
		return container;
	}

	@Override
	public void init(IWorkbench workbench) {
    //		Do nothing
	}

}

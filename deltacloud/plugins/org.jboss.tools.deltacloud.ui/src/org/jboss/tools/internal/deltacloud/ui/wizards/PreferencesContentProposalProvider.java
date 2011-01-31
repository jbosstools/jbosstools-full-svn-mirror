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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringEntriesPreferenceValue;

/**
 * @author André Dietisheim
 */
public class PreferencesContentProposalProvider extends SimpleContentProposalProvider {

	private StringEntriesPreferenceValue preferencesValues;

	public PreferencesContentProposalProvider(String preferencesKey, String pluginId) {
		super(new String[] {});
		this.preferencesValues = new StringEntriesPreferenceValue(",", preferencesKey, pluginId);
		setProposals(preferencesValues.get());
	}
	
	public void add(String newEntry) {
		preferencesValues.add(newEntry);
	}
	
	public void save() {
		preferencesValues.store();
	}
}

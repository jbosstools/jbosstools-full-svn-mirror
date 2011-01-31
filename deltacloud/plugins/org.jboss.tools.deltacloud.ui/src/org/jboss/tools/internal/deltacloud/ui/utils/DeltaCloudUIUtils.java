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
package org.jboss.tools.internal.deltacloud.ui.utils;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringEntriesPreferenceValue;

/**
 * @author André Dietisheim
 */
public class DeltaCloudUIUtils {

	public static ContentProposalAdapter createPreferencesProposalAdapter(final Text text, String preferencesKey) {
		final StringEntriesPreferenceValue preferencesValues = new StringEntriesPreferenceValue(",", preferencesKey, Activator.PLUGIN_ID);
		SimpleContentProposalProvider proposalProvider = new SimpleContentProposalProvider(preferencesValues.get());
		proposalProvider.setFiltering(true);
		text.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				String currentValue = text.getText();
				preferencesValues.add(currentValue);
				preferencesValues.store();
			}
		});

		KeyStroke keyStroke = KeyStroke.getInstance(SWT.CONTROL, ' ');
		ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(text, new TextContentAdapter(), proposalProvider, keyStroke, null);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		return proposalAdapter;
	}
}

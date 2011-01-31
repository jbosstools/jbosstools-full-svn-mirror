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

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.internal.deltacloud.ui.wizards.PreferencesContentProposalProvider;

/**
 * @author André Dietisheim
 */
public class DeltaCloudUIUtils {

	private static char[] ACTIVATION_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ:.,-".toCharArray();
	
	public static ContentProposalAdapter createPreferencesProposalAdapter(final Text text, String preferencesKey) {
		final PreferencesContentProposalProvider proposalProvider = new PreferencesContentProposalProvider(preferencesKey, Activator.PLUGIN_ID);
		proposalProvider.setFiltering(true);
		text.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				String currentValue = text.getText();
				proposalProvider.add(currentValue);
				proposalProvider.save();
			}
		});
		ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(text, new TextContentAdapter(), proposalProvider, null, ACTIVATION_CHARS);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		return proposalAdapter;
	}
}

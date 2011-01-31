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
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.internal.deltacloud.ui.preferences.StringEntriesPreferenceValue;

/**
 * @author André Dietisheim
 */
public class DeltaCloudUIUtils {

	public static ContentProposalAdapter addPreferencesProposalAdapter(final Text text, String preferencesKey) {
		final ControlDecoration decoration = createContenAssistDecoration(text);
		final StringEntriesPreferenceValue preferencesValues =
				new StringEntriesPreferenceValue(",", preferencesKey, Activator.PLUGIN_ID);
		SimpleContentProposalProvider proposalProvider = new SimpleContentProposalProvider(preferencesValues.get());
		proposalProvider.setFiltering(true);
		text.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				decoration.show();
			}

			@Override
			public void focusLost(FocusEvent e) {
				decoration.hide();
				
				preferencesValues.add(text.getText());
				preferencesValues.store();
			}

		});
		KeyStroke keyStroke = KeyStroke.getInstance(SWT.CONTROL, ' ');
		ContentProposalAdapter proposalAdapter =
				new ContentProposalAdapter(text, new TextContentAdapter(), proposalProvider, keyStroke, null);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		return proposalAdapter;
	}

	private static ControlDecoration createContenAssistDecoration(Control control) {
		ControlDecoration decoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();
		decoration.setImage(errorImage);
		decoration.setDescriptionText("Content Assist Available");
		decoration.setShowHover(true);
		decoration.hide();
		return decoration;
	}
}

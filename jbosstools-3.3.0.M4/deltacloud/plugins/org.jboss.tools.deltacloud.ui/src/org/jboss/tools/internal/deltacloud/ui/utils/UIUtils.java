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

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.ui.preferencevalue.StringsPreferenceValue;
import org.jboss.tools.deltacloud.ui.Activator;
import org.osgi.service.prefs.Preferences;

/**
 * @author Andre Dietisheim
 */
public class UIUtils {

	public static ContentProposalAdapter createPreferencesProposalAdapter(final Text text, String preferencesKey) {
		final ControlDecoration decoration = createContenAssistDecoration(text);

		final StringsPreferenceValue preferencesValues =
				new StringsPreferenceValue(',', preferencesKey, Activator.PLUGIN_ID);
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
				String value = text.getText();
				if (value != null && value.length() > 0) {
					preferencesValues.add(text.getText());
					preferencesValues.store();
				}
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
		decoration.setDescriptionText("History available");
		decoration.setShowHover(true);
		decoration.hide();
		return decoration;
	}

	public static ControlDecoration createErrorDecoration(String errorText, Control control) {
		return createDecoration(errorText, FieldDecorationRegistry.DEC_ERROR, control);
	}

	public static ControlDecoration createDecoration(String text, String imageKey, Control control) {
		ControlDecoration decoration = new ControlDecoration(control, SWT.LEFT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(imageKey).getImage();
		decoration.setImage(errorImage);
		decoration.setDescriptionText(text);
		decoration.setShowHover(true);
		decoration.hide();
		return decoration;
	}

	/**
	 * Opens a confirmation dialog that offers a checkbox that allows the user
	 * to turn further aksing off. The checkbox state is stored to the
	 * preferences
	 * 
	 * @param title
	 *            dialog title
	 * @param message
	 *            dialog message
	 * @param dontShowAgainMessage
	 *            message for further asking ("dont show this dialog again")
	 * @param preferencesKey
	 *            the preferences key to store state of further asking
	 * @param pluginId
	 *            the plugin id to use when storing the preferences
	 * @param shell
	 *            the shell to use
	 * @return true, if successful
	 */
	public static boolean openConfirmationDialog(String title, String message, String dontShowAgainMessage,
			String preferencesKey, String pluginId, Shell shell) {
		boolean confirmed = true;
		Preferences prefs = new InstanceScope().getNode(pluginId);
		boolean dontShowDialog =
				prefs.getBoolean(preferencesKey, false);
		if (!dontShowDialog) {
			MessageDialogWithToggle dialog =
					MessageDialogWithToggle.openOkCancelConfirm(shell, title, message, dontShowAgainMessage,
							false, null, null);
			confirmed = dialog.getReturnCode() == Dialog.OK;
			boolean toggleState = dialog.getToggleState();
			// If warning turned off by user, set the preference for future
			// usage
			if (toggleState) {
				prefs.putBoolean(preferencesKey, true);
			}
		}
		return confirmed;
	}
}

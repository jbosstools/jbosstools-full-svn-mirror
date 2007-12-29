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

package org.jboss.tools.seam.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamRuntimeManager;
import org.jboss.tools.seam.ui.widget.editor.SeamRuntimeListFieldEditor;

/**
 * Seam preference page that allows editing list of available Seam Runtimes:
 * <ul>
 * <li>define new </li>
 * <li>change exists</li>
 * <li>remove</li>
 * </ul>
 * 
 * @author eskimo
 */
public class SeamPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * Seam Preferences page ID
	 */
	public static final String SEAM_PREFERENCES_ID = "org.jboss.tools.common.model.ui.seam";
	
	private static final int COLUMNS = 3;

	SeamRuntimeListFieldEditor seamRuntimes = new SeamRuntimeListFieldEditor(
			"rtlist", SeamPreferencesMessages.SEAM_PREFERENCE_PAGE_SEAM_RUNTIMES, new ArrayList<SeamRuntime>(Arrays.asList(SeamRuntimeManager.getInstance().getRuntimes()))); //$NON-NLS-1$
	
	SeamRuntime initialDefault;

	/**
	 * Create contents of Seam preferences page. SeamRuntime list editor is
	 * created
	 * 
	 * @return Control
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite root = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(COLUMNS, false);
		root.setLayout(gl);
		seamRuntimes.doFillIntoGrid(root);

		initialDefault = SeamRuntimeManager.getInstance().getDefaultRuntime();

		return root;
	}

	/**
	 * Inherited from IWorkbenchPreferencePage
	 * 
	 * @param workbench
	 *            {@link IWorkbench}
	 * 
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Save SeamRuntime list
	 */
	@Override
	protected void performApply() {
		for (SeamRuntime rt : seamRuntimes.getAddedSeamRuntimes()) {
			SeamRuntimeManager.getInstance().addRuntime(rt);
		}
		seamRuntimes.getAddedSeamRuntimes().clear();
		for (SeamRuntime rt : seamRuntimes.getRemoved()) {
			SeamRuntimeManager.getInstance().removeRuntime(rt);
		}
		seamRuntimes.getRemoved().clear();
		if (initialDefault != null
				&& seamRuntimes.getDefaultSeamRuntime() != initialDefault) {
			initialDefault.setDefault(false);
		}
		Map<SeamRuntime, SeamRuntime> changed = seamRuntimes
				.getChangedSeamRuntimes();
		for (SeamRuntime c : changed.keySet()) {
			SeamRuntime o = changed.get(c);
			o.setHomeDir(c.getHomeDir());
			o.setVersion(c.getVersion());
			String oldName = o.getName();
			String newName = c.getName();
			if (!oldName.equals(newName)) {
				SeamRuntimeManager.getInstance().changeRuntimeName(oldName,
						newName);
			}
		}
		seamRuntimes.getChangedSeamRuntimes().clear();

		if (seamRuntimes.getDefaultSeamRuntime() != null) {
			seamRuntimes.getDefaultSeamRuntime().setDefault(true);
		}
		SeamRuntimeManager.getInstance().save();
	}

	/**
	 * Restore original preferences values
	 */
	@Override
	protected void performDefaults() {
		setValid(true);
		setMessage(null);
		performApply();
	}

	/**
	 * See {@link PreferencePage} for details
	 * 
	 * @return Boolean
	 */
	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}
}

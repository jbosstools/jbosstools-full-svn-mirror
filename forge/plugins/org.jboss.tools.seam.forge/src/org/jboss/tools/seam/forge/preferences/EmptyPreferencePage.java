package org.jboss.tools.seam.forge.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class EmptyPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	public EmptyPreferencePage() {
		super();
		setDescription("Expand the tree to edit preferences for a specific feature.");
	}

	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		return new Composite(parent, SWT.NULL);
	}

	public void init(IWorkbench workbench) {
	}

}

package org.jboss.ide.eclipse.packages.ui.properties;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;

public class MainPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Button showPackageOutputPath;
	private Button showFullFilesetRootDir;
	private IWorkbench workbench;
	
	public MainPreferencePage() {
		super ("Package Preferences", PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_PACKAGE));
	}

	protected Control createContents(Composite parent) {
		Preferences prefs = PackagesUIPlugin.getDefault().getPluginPreferences();
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		showPackageOutputPath = new Button(main, SWT.CHECK);
		showPackageOutputPath.setText("Show package output path in Project Packages view.");
		showPackageOutputPath.setSelection(
			prefs.getBoolean(PackagesUIPlugin.PREF_SHOW_PACKAGE_OUTPUT_PATH));
		
		showFullFilesetRootDir = new Button(main, SWT.CHECK);
		showFullFilesetRootDir.setText("Show the full root directory of a fileset in the Project Packages view.");
		showFullFilesetRootDir.setSelection(
			prefs.getBoolean(PackagesUIPlugin.PREF_SHOW_FULL_FILESET_ROOT_DIR));
		
		return main;
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	public boolean performOk() {
		Preferences prefs = PackagesUIPlugin.getDefault().getPluginPreferences();
		
		prefs.setValue(PackagesUIPlugin.PREF_SHOW_PACKAGE_OUTPUT_PATH, showPackageOutputPath.getSelection());
		prefs.setValue(PackagesUIPlugin.PREF_SHOW_FULL_FILESET_ROOT_DIR, showFullFilesetRootDir.getSelection());
		PackagesUIPlugin.getDefault().savePluginPreferences();
		
		return true;
	}
}

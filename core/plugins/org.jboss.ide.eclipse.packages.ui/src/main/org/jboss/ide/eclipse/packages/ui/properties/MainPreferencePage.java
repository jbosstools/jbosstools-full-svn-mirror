package org.jboss.ide.eclipse.packages.ui.properties;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;

public class MainPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Button showPackageOutputPath;
	private Button showFullFilesetRootDir;
	private Button showProjectRoot, showAllProjects;
	private IWorkbench workbench;
	
	public MainPreferencePage() {
		super ("Package Preferences", PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_PACKAGE));
	}

	protected Control createContents(Composite parent) {
		Preferences prefs = PackagesUIPlugin.getDefault().getPluginPreferences();
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		Group viewPrefGroup = new Group(main, SWT.NONE);
		viewPrefGroup.setText("Project Packages View");
		viewPrefGroup.setLayout(new GridLayout(1, false));
		viewPrefGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		showPackageOutputPath = new Button(viewPrefGroup, SWT.CHECK);
		showPackageOutputPath.setText("Show full output path next to packages.");
		showPackageOutputPath.setSelection(
			prefs.getBoolean(PackagesUIPlugin.PREF_SHOW_PACKAGE_OUTPUT_PATH));
		
		showFullFilesetRootDir = new Button(viewPrefGroup, SWT.CHECK);
		showFullFilesetRootDir.setText("Show the full root directory of filesets.");
		showFullFilesetRootDir.setSelection(
			prefs.getBoolean(PackagesUIPlugin.PREF_SHOW_FULL_FILESET_ROOT_DIR));
		
		showProjectRoot = new Button(viewPrefGroup, SWT.CHECK);
		showProjectRoot.setText("Show project at the root");
		showProjectRoot.setSelection(
			prefs.getBoolean(PackagesUIPlugin.PREF_SHOW_PROJECT_ROOT));
		
		showProjectRoot.addSelectionListener(new SelectionListener () {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);	
			}
			public void widgetSelected(SelectionEvent e) {
				showAllProjects.setEnabled(showProjectRoot.getSelection());
				
				if (!showProjectRoot.getSelection())
				{
					showAllProjects.setSelection(false);
				}
			}
		});
		
		showAllProjects = new Button(viewPrefGroup, SWT.CHECK);
		showAllProjects.setText("Show all projects that contain packages");
		showAllProjects.setSelection(
			prefs.getBoolean(PackagesUIPlugin.PREF_SHOW_ALL_PROJECTS));
		
		return main;
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	public boolean performOk() {
		Preferences prefs = PackagesUIPlugin.getDefault().getPluginPreferences();
		
		prefs.setValue(PackagesUIPlugin.PREF_SHOW_PACKAGE_OUTPUT_PATH, showPackageOutputPath.getSelection());
		prefs.setValue(PackagesUIPlugin.PREF_SHOW_FULL_FILESET_ROOT_DIR, showFullFilesetRootDir.getSelection());
		prefs.setValue(PackagesUIPlugin.PREF_SHOW_PROJECT_ROOT, showProjectRoot.getSelection());
		prefs.setValue(PackagesUIPlugin.PREF_SHOW_ALL_PROJECTS, showAllProjects.getSelection());
		PackagesUIPlugin.getDefault().savePluginPreferences();
		
		return true;
	}
}

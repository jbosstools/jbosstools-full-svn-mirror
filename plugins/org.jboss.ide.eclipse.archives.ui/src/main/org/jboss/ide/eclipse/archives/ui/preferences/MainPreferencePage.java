package org.jboss.ide.eclipse.archives.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.ide.eclipse.archives.core.CorePreferenceManager;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;
import org.jboss.ide.eclipse.archives.ui.views.ProjectArchivesView;

public class MainPreferencePage extends PropertyPage implements
		IWorkbenchPreferencePage {

	private Button showPackageOutputPath, showFullFilesetRootDir;
	private Button showProjectRoot, showAllProjects;
	private Button automaticBuilder, overrideButton;
	private Group corePrefGroup, viewPrefGroup;
	private Composite overrideComp;
	
	
	public MainPreferencePage() {
		super();
		setTitle("Packaging Archives");
		setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_PACKAGE));
	}

	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		createOverridePrefs(main);
		createCorePrefs(main);
		createViewPrefs(main);
		fillValues();
		return main;
	}
	
	protected void fillValues() {
		if( getElement() != null ) 
			overrideButton.setSelection(CorePreferenceManager.areProjectSpecificPrefsEnabled(getElement()));
		automaticBuilder.setSelection(CorePreferenceManager.isBuilderEnabled(getElement()));
		showAllProjects.setSelection(
				PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS, getElement(), false));
		showPackageOutputPath.setSelection(
				PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PACKAGE_OUTPUT_PATH, getElement(), false));
		showFullFilesetRootDir.setSelection(
				PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_FULL_FILESET_ROOT_DIR, getElement(), false));
		showProjectRoot.setSelection(
				PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT, getElement(), false));

		showAllProjects.setEnabled(showProjectRoot.getSelection());
		if (!showProjectRoot.getSelection()) 
			showAllProjects.setSelection(false);
		
		if( getElement() != null ) {
			setWidgetsEnabled(overrideButton.getSelection());
		}
	}
	
	protected void createOverridePrefs(Composite main) {
		if( getElement() != null ) {
			overrideComp = new Composite(main, SWT.NONE);
			overrideComp.setLayout(new FillLayout());
			overrideButton = new Button(overrideComp, SWT.CHECK);
			overrideButton.setText("Enable Project Specific Settings");
			
			overrideButton.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				public void widgetSelected(SelectionEvent e) {
					setWidgetsEnabled(overrideButton.getSelection());
				}
			});
		}
	}
	
	protected void setWidgetsEnabled(boolean val) {
		showPackageOutputPath.setEnabled(val);
		showProjectRoot.setEnabled(val);
		showFullFilesetRootDir.setEnabled(val);
		if( showProjectRoot.getSelection())
			showAllProjects.setEnabled(val);
		automaticBuilder.setEnabled(val);
	}
	
	protected void createCorePrefs(Composite main) {
		corePrefGroup = new Group(main, SWT.NONE);
		corePrefGroup.setText("Core Preferences");
		corePrefGroup.setLayout(new GridLayout(1, false));
		corePrefGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		automaticBuilder = new Button(corePrefGroup, SWT.CHECK);
		automaticBuilder.setText("Enable automatic builder");
	}
	
	protected void createViewPrefs(Composite main) {
		
		viewPrefGroup = new Group(main, SWT.NONE);
		viewPrefGroup.setText("Project Packages View");
		viewPrefGroup.setLayout(new GridLayout(1, false));
		viewPrefGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		showPackageOutputPath = new Button(viewPrefGroup, SWT.CHECK);
		showPackageOutputPath.setText("Show full output path next to packages.");
		
		showFullFilesetRootDir = new Button(viewPrefGroup, SWT.CHECK);
		showFullFilesetRootDir.setText("Show the full root directory of filesets.");
		
		showProjectRoot = new Button(viewPrefGroup, SWT.CHECK);
		showProjectRoot.setText("Show project at the root");
		
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
		showAllProjects.setEnabled(showProjectRoot.getSelection());
		if( !showProjectRoot.getSelection() ) 
			showAllProjects.setSelection(false);

	}

	public void init(IWorkbench workbench) {
	}

	public boolean performOk() {
		if( getElement() != null ) {
			CorePreferenceManager.setProjectSpecificPrefs(getElement(), overrideButton.getSelection());
		}
		CorePreferenceManager.setBuilderEnabled(getElement(), automaticBuilder.getSelection());
		PrefsInitializer.setBoolean(PrefsInitializer.PREF_SHOW_PACKAGE_OUTPUT_PATH, showPackageOutputPath.getSelection(), getElement());
		PrefsInitializer.setBoolean(PrefsInitializer.PREF_SHOW_FULL_FILESET_ROOT_DIR, showFullFilesetRootDir.getSelection(), getElement());
		PrefsInitializer.setBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT, showProjectRoot.getSelection(), getElement());
		PrefsInitializer.setBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS, showAllProjects.getSelection(), getElement());
		ProjectArchivesView.getInstance().refreshViewer(null);
		
		return true;
	}
}

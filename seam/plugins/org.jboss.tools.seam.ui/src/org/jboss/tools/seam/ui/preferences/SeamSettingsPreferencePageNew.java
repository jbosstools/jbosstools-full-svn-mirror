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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.SeamProjectsSet;
import org.jboss.tools.seam.core.project.facet.SeamProjectPreferences;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamVersion;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;
import org.jboss.tools.seam.ui.SeamGuiPlugin;
import org.jboss.tools.seam.ui.SeamUIMessages;
import org.jboss.tools.seam.ui.internal.project.facet.IValidator;
import org.jboss.tools.seam.ui.internal.project.facet.ValidatorFactory;
import org.jboss.tools.seam.ui.widget.editor.IFieldEditor;
import org.jboss.tools.seam.ui.widget.editor.IFieldEditorFactory;
import org.jboss.tools.seam.ui.widget.editor.SeamRuntimeListFieldEditor;
import org.jboss.tools.seam.ui.wizard.IParameter;
import org.jboss.tools.seam.ui.wizard.SeamWizardFactory;
import org.jboss.tools.seam.ui.wizard.SeamWizardUtils;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Seam Settings Preference Page
 * @author Alexey Kazakov
 */
public class SeamSettingsPreferencePageNew extends PropertyPage implements PropertyChangeListener {

	private Map<String,IFieldEditor> editorRegistry = new HashMap<String,IFieldEditor>();
	private IProject project;
	private IProject warProject;
	private IEclipsePreferences preferences;
	private ISeamProject warSeamProject;
	private boolean suportSeam;
	private boolean runtimeIsSelected;
	private List<Group> groups = new ArrayList<Group>();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.PropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
		project = (IProject) getElement().getAdapter(IProject.class);
		warProject = SeamWizardUtils.getRootSeamProject(project);
		if(warProject!=null) {
			preferences = SeamCorePlugin.getSeamPreferences(warProject);
			warSeamProject = SeamCorePlugin.getSeamProject(warProject, false);
		} else {
			preferences = SeamCorePlugin.getSeamPreferences(project);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite root = new Composite(parent, SWT.NONE);

		GridData gd = new GridData();

		gd.horizontalSpan = 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;

		GridLayout gridLayout = new GridLayout(1, false);
		root.setLayout(gridLayout);
		
		Composite generalGroup = new Composite(root, SWT.NONE);
		generalGroup.setLayoutData(gd);
		gridLayout = new GridLayout(4, false);

		generalGroup.setLayout(gridLayout);

		IFieldEditor seamSupportCheckBox = IFieldEditorFactory.INSTANCE.createCheckboxEditor(
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_SUPPORT, SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_SUPPORT, warSeamProject!=null);
		seamSupportCheckBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Object value = evt.getNewValue();
				if (value instanceof Boolean) {
					boolean v = ((Boolean) value).booleanValue();
					setEnabledSeamSuport(v);
					validate();
				}
			}
		});
		registerEditor(seamSupportCheckBox, generalGroup);

		IFieldEditor seamRuntimeEditor = 
			SeamWizardFactory.createSeamRuntimeSelectionFieldEditor(
					getSeamVersions(), 
					getSeamRuntimeName());
		seamRuntimeEditor.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Object value = evt.getNewValue();
				if(value.toString().length()>0) {
					setRuntimeIsSelected(true);
				} else {
					setRuntimeIsSelected(false);
				}
				validate();
			}
		});
		registerEditor(seamRuntimeEditor, generalGroup);

		IFieldEditor projectNameEditor = 
			IFieldEditorFactory.INSTANCE.createUneditableTextEditor(
					IParameter.SEAM_PROJECT_NAME, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_SEAM_PROJECT, 
					getPrefValue(
							IParameter.SEAM_PROJECT_NAME, 
							getSeamProjectName()));
		
		registerEditor(projectNameEditor, generalGroup);

		IFieldEditor connProfileEditor = SeamWizardFactory.createConnectionProfileSelectionFieldEditor(getConnectionProfile(), new IValidator() {
			public Map<String, String> validate(Object value, Object context) {
				SeamSettingsPreferencePageNew.this.validate();
				return ValidatorFactory.NO_ERRORS;
			}
		});
		registerEditor(connProfileEditor, generalGroup);

		Group deploymentGroup = createGroup(
				root,
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_DEPLOYMENT, 
				4);

		IFieldEditor deployTypeEditor = IFieldEditorFactory.INSTANCE.createRadioEditor(
				ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS,
				SeamUIMessages.SEAM_INSTALL_WIZARD_PAGE_DEPLOY_AS, 
				Arrays.asList(new String[] {ISeamFacetDataModelProperties.DEPLOY_AS_WAR.toUpperCase(), ISeamFacetDataModelProperties.DEPLOY_AS_EAR.toUpperCase()}),
				Arrays.asList(new Object[] {ISeamFacetDataModelProperties.DEPLOY_AS_WAR, ISeamFacetDataModelProperties.DEPLOY_AS_EAR}),
				getDeployAsValue());
		
		deployTypeEditor.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				setEnabledDeploymentGroup();
			}
		});

		registerEditor(deployTypeEditor, deploymentGroup);

		IFieldEditor ejbProjectEditor = SeamWizardFactory.createSeamProjectSelectionFieldEditor(
				ISeamFacetDataModelProperties.SEAM_EJB_PROJECT, 
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_EJB_PROJECT, 
				getEjbProjectName(),
				true);
		registerEditor(ejbProjectEditor, deploymentGroup);

		Group viewGroup = createGroup(
				root,
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_VIEW, 
				3);

		IFieldEditor viewFolderEditor = SeamWizardFactory.createViewFolderFieldEditor(getViewFolder());
		registerEditor(viewFolderEditor, viewGroup);

		Group modelGroup = createGroup(root,
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_MODEL,
				3);

		IFieldEditor modelSourceFolderEditor = 
			IFieldEditorFactory.INSTANCE.createBrowseSourceFolderEditor(
					ISeamFacetDataModelProperties.ENTITY_BEAN_SOURCE_FOLDER, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_SOURCE_FOLDER, 
					getModelSourceFolder());
		
		IFieldEditor modelPackageEditor = 
			IFieldEditorFactory.INSTANCE.createBrowsePackageEditor(
					ISeamFacetDataModelProperties.ENTITY_BEAN_PACKAGE_NAME, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_PACKAGE, 
					"");	
		
		registerEditor(modelSourceFolderEditor, modelGroup);
		registerEditor(modelPackageEditor, modelGroup);
		
		Group actionGroup = createGroup(root,
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_ACTION,
				3);

		IFieldEditor actionSourceFolderEditor = 
			IFieldEditorFactory.INSTANCE.createBrowseSourceFolderEditor(
					ISeamFacetDataModelProperties.SESSION_BEAN_SOURCE_FOLDER, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_SOURCE_FOLDER, 
					getModelSourceFolder());
		
		IFieldEditor actionPackageEditor = 
			IFieldEditorFactory.INSTANCE.createBrowsePackageEditor(
					ISeamFacetDataModelProperties.SESSION_BEAN_PACKAGE_NAME, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_PACKAGE, 
					"");	
		registerEditor(actionSourceFolderEditor, actionGroup);
		registerEditor(actionPackageEditor, actionGroup);
		
		Group testGroup = createGroup(root,
				SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_TEST,
				3);

		IFieldEditor createTestCheckBox = IFieldEditorFactory.INSTANCE.createCheckboxEditor(
				ISeamFacetDataModelProperties.TEST_CREATING, SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_CREATE_TEST, false);
		createTestCheckBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Object value = evt.getNewValue();
				if (value instanceof Boolean) {
					setEnabledTestGroup();					
				}
			}
		});
		
		registerEditor(createTestCheckBox, testGroup);
		
		IFieldEditor testProjectEditor = SeamWizardFactory.createSeamProjectSelectionFieldEditor(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT, SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_TEST_PROJECT, getTestProjectName(),false);
		registerEditor(testProjectEditor, testGroup);
		
		IFieldEditor testSourceFolderEditor = 
			IFieldEditorFactory.INSTANCE.createBrowseSourceFolderEditor(
					ISeamFacetDataModelProperties.TEST_SOURCE_FOLDER, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_SOURCE_FOLDER, 
					getModelSourceFolder());
		
		IFieldEditor testPackageEditor = 
			IFieldEditorFactory.INSTANCE.createBrowsePackageEditor(
					ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_NAME, 
					SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCES_PAGE_PACKAGE, 
					"");
		
		registerEditor(testSourceFolderEditor, testGroup);
		registerEditor(testPackageEditor, testGroup);
		
		setEnabledSeamSuport(warSeamProject!=null);
//		setRuntimeIsSelected(getSeamRuntimeName().length()>0);

		return root;
	}

	private String getPrefValue(String prefName,String defaultValue) {
		return preferences.get(
				prefName, 
				defaultValue);
	}

	private Group createGroup(Composite parent, String title, int rows) {
		return createGroupWithSpan(parent,title,rows,1);
	}

	private Group createGroupWithSpan(Composite parent, String title, int rows, int span) {
		GridData gd;
		GridLayout gridLayout;
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = span;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;

		Group newGroup = new Group(parent, SWT.NONE);
		newGroup.setLayoutData(gd);
		newGroup.setText(title);
		
		gridLayout = new GridLayout(rows, false);
		newGroup.setLayout(gridLayout);
		groups.add(newGroup);
		return newGroup;
	}
	
	private String getModelSourceFolder() {
		String folder = null;
		if(preferences!=null) {
			folder = preferences.get(ISeamFacetDataModelProperties.ENTITY_BEAN_SOURCE_FOLDER, null);
		}
		if(folder==null) {
			SeamProjectsSet set = new SeamProjectsSet(project);
			IFolder f = set.getModelFolder();
			folder = f!=null?f.getFullPath().toString():"";
		}
		return folder;
	}

	private String getViewFolder() {
		String folder = null;
		if(preferences!=null) {
			folder = preferences.get(ISeamFacetDataModelProperties.WEB_CONTENTS_FOLDER, null);
		}
		if(folder==null) {
			SeamProjectsSet set = new SeamProjectsSet(project);
			IFolder f = set.getViewsFolder();
			folder = f!=null?f.getFullPath().toString():"";
		}
		return folder;
	}

	private List<String> getProfileNameList() {
		IConnectionProfile[] profiles = ProfileManager.getInstance()
				.getProfilesByCategory("org.eclipse.datatools.connectivity.db.category"); //$NON-NLS-1$
		List<String> names = new ArrayList<String>();
		for (IConnectionProfile connectionProfile : profiles) {
			names.add(connectionProfile.getName());
		}
		return names;
	}

	private String getConnectionProfile() {
		String defaultDs = SeamProjectPreferences.getStringPreference(
				SeamProjectPreferences.SEAM_DEFAULT_CONNECTION_PROFILE);
		return getProfileNameList().contains(defaultDs)?defaultDs:""; //$NON-NLS-1$
	}

	private String getEjbProjectName() {
		if(preferences!=null) {
			return preferences.get(ISeamFacetDataModelProperties.SEAM_EJB_PROJECT, project.getName());
		}
		return project.getName();
	}

	private Object getDeployAsValue() {
		if(preferences!=null) {
			return preferences.get(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS, ISeamFacetDataModelProperties.DEPLOY_AS_WAR);
		}
		return ISeamFacetDataModelProperties.DEPLOY_AS_WAR;
	}

	private void validate() {
//		if(getSeamSupport() && (runtime.getValue()== null || "".equals(runtime.getValue()))) { //$NON-NLS-1$
////			setValid(false);
//			setMessage(SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_RUNTIME_IS_NOT_SELECTED, IMessageProvider.WARNING);
////			setErrorMessage(SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_RUNTIME_IS_NOT_SELECTED);
//		} else {
//			setValid(true);
//			String value = runtime.getValueAsString();
//			if(Boolean.TRUE.equals(seamEnablement.getValue()) && SeamRuntimeManager.getInstance().findRuntimeByName(value) == null) {
//				setErrorMessage("Runtime " + value + " does not exist.");
//			} else {
//				setErrorMessage(null);
//				setMessage(null, IMessageProvider.WARNING);
//			}
//		}
	}

	private String getSeamRuntimeName() {
		if(preferences!=null) {
			return preferences.get(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME, "");
		}
		return "";
	}

	private String getSeamProjectName() {
		return warProject!=null ? warProject.getName() : project.getName();
	}
	
	private String getTestProjectName() {
		String projectName = "";
		if(preferences!=null) {
			projectName = preferences.get(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT, getSeamProjectName());
		}
		return projectName;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
	}

	private void registerEditor(IFieldEditor editor, Composite parent) {
		editorRegistry.put(editor.getName(), editor);
		editor.doFillIntoGrid(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if (isSeamSupported()) {
			addSeamSupport();
			storeSettigs();
		} else {
			removeSeamSupport();
		}
		return true;
	}

	private void storeSettigs() {
		IScopeContext projectScope = new ProjectScope(project);
		IEclipsePreferences prefs = projectScope.getNode(SeamCorePlugin.PLUGIN_ID);

		prefs.put(ISeamFacetDataModelProperties.SEAM_SETTINGS_VERSION, 
				"1.1");
		
		prefs.put(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS, 
				getValue(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS));

		prefs.put(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME, 
				getValue(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME));
		prefs.put(ISeamFacetDataModelProperties.SEAM_CONNECTION_PROFILE, 
				getValue(ISeamFacetDataModelProperties.SEAM_CONNECTION_PROFILE));
		prefs.put(ISeamFacetDataModelProperties.SESSION_BEAN_PACKAGE_NAME,
				getValue(ISeamFacetDataModelProperties.SESSION_BEAN_PACKAGE_NAME));
		prefs.put(ISeamFacetDataModelProperties.ENTITY_BEAN_PACKAGE_NAME, 
				getValue(ISeamFacetDataModelProperties.ENTITY_BEAN_PACKAGE_NAME));
		prefs.put(ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_NAME, 
				getValue(ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_NAME));
		prefs.put(ISeamFacetDataModelProperties.TEST_CREATING,
				"true");
		prefs.put(ISeamFacetDataModelProperties.TEST_SOURCE_FOLDER, 
				getValue(ISeamFacetDataModelProperties.TEST_CREATING));
		prefs.put(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT, 
				getValue(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT));
		prefs.put(ISeamFacetDataModelProperties.SEAM_EJB_PROJECT, 
				getValue(ISeamFacetDataModelProperties.SEAM_EJB_PROJECT));
		prefs.put(ISeamFacetDataModelProperties.ENTITY_BEAN_SOURCE_FOLDER, 
				getValue(ISeamFacetDataModelProperties.ENTITY_BEAN_SOURCE_FOLDER));
		prefs.put(ISeamFacetDataModelProperties.SESSION_BEAN_SOURCE_FOLDER, 
				getValue(ISeamFacetDataModelProperties.SESSION_BEAN_SOURCE_FOLDER));
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			SeamGuiPlugin.getPluginLog().logError(e);
		}
	}

	private String getValue(String editorName) {
		return editorRegistry.get(editorName).getValue().toString();
	}

	private boolean isSeamSupported() {
		return suportSeam;
	}

	private void setEnabledSeamSuport(boolean enabled) {
		// just for enabling/disabling groups 
		setEnabledGroups(enabled);
		suportSeam = enabled;
		if(!enabled) {
			// disable all below
			for (String key : editorRegistry.keySet()) {
				if(key!=SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_SUPPORT) {
					editorRegistry.get(key).setEnabled(enabled);
				}
			}			
		} else {
			for (String key : editorRegistry.keySet()) {
				if(key!=SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_SUPPORT
						&& key!=ISeamFacetDataModelProperties.SEAM_TEST_PROJECT
						&& key!=ISeamFacetDataModelProperties.TEST_SOURCE_FOLDER
						&& key!=ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_PATH
						&& key!=ISeamFacetDataModelProperties.SEAM_EJB_PROJECT) {
					editorRegistry.get(key).setEnabled(enabled);
				}
			}
			setEnabledTestGroup();
			setEnabledDeploymentGroup();
		}

	}

	private void setEnabledDeploymentGroup() {
		IFieldEditor deployment = 
			editorRegistry.get(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS);
		
		editorRegistry.get(ISeamFacetDataModelProperties.SEAM_EJB_PROJECT)
			.setEnabled(
					ISeamFacetDataModelProperties.DEPLOY_AS_EAR.equals(
							deployment.getValue()));
		
	}

	private void setEnabledTestGroup() {
		IFieldEditor createTestCheckBox = editorRegistry.get(ISeamFacetDataModelProperties.TEST_CREATING);
		boolean enabled = ((Boolean)createTestCheckBox.getValue()).booleanValue();
		editorRegistry.get(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT).setEnabled(enabled);
		editorRegistry.get(ISeamFacetDataModelProperties.TEST_SOURCE_FOLDER).setEnabled(enabled);
		editorRegistry.get(ISeamFacetDataModelProperties.TEST_CASES_PACKAGE_NAME).setEnabled(enabled);						
	}
	
	private void setEnabledGroups(boolean enabled) {
		for (Group group : groups) {
				group.setEnabled(enabled);
		}
	}

	private void setRuntimeIsSelected(boolean selected) {
		runtimeIsSelected = selected;
		for (String key : editorRegistry.keySet()) {
			if(key!=SeamPreferencesMessages.SEAM_SETTINGS_PREFERENCE_PAGE_SEAM_SUPPORT && key!=ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME) {
				editorRegistry.get(key).setEnabled(selected);
			}
		}
		setEnabledGroups(selected);
	}

	private void removeSeamSupport() {
		try {
			EclipseResourceUtil.removeNatureFromProject(project,
					ISeamProject.NATURE_ID);
		} catch (CoreException e) {
			SeamGuiPlugin.getPluginLog().logError(e);
		}
	}

	private void addSeamSupport() {
		try {
			EclipseResourceUtil.addNatureToProject(project,	ISeamProject.NATURE_ID);
		} catch (CoreException e) {
			SeamGuiPlugin.getPluginLog().logError(e);
		}
	}

	private SeamVersion[] getSeamVersions() {
		if(warSeamProject != null) {
			SeamRuntime r = warSeamProject.getRuntime();
			if(r != null) {
				return new SeamVersion[]{r.getVersion()};
			}
			String jarLocation = getJBossSeamJarLocation();
			if(jarLocation != null) {
				String folder = new File(jarLocation).getParent();
				String vs = SeamRuntimeListFieldEditor.SeamRuntimeWizardPage.getSeamVersion(folder);
				SeamVersion v = findMatchingVersion(vs);
				if(v != null) {
					return new SeamVersion[]{v};
				}
			}
		}
		return SeamVersion.ALL_VERSIONS;
	}

	private SeamVersion findMatchingVersion(String vs) {
		if(vs == null) return null;
		if(vs.matches(SeamVersion.SEAM_1_2.toString().replace(".", "\\.") + ".*")) {
			return SeamVersion.SEAM_1_2;
		}
		if(vs.matches(SeamVersion.SEAM_2_0.toString().replace(".", "\\.") + ".*")) {
			return SeamVersion.SEAM_2_0;
		}
		return null;
	}

	private String getJBossSeamJarLocation() {
		IJavaProject jp = EclipseResourceUtil.getJavaProject(project);
		if(jp == null) return null;
		IClasspathEntry[] es = null;
		try {
			es = jp.getResolvedClasspath(true);
		} catch (JavaModelException e) {
			//ignore
			return null;
		}
		if(es == null) return null;
		for (int i = 0; i < es.length; i++) {
			IPath p = es[i].getPath();
			if(p != null && p.lastSegment().equalsIgnoreCase("jboss-seam.jar")) {
				IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(p);
				if(f != null && f.exists()) return f.getLocation().toString();
			}
		}
		return null;
	}

}
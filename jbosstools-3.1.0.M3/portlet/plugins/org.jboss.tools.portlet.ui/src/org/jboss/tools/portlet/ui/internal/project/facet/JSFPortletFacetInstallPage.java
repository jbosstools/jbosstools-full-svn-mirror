package org.jboss.tools.portlet.ui.internal.project.facet;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelSynchHelper;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.ide.eclipse.as.core.server.IJBossServerConstants;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * @author snjeza
 */
public class JSFPortletFacetInstallPage extends AbstractFacetWizardPage {

	private IDialogSettings dialogSettings;
	private IDataModel model;
	private IDialogSettings jsfSection;
	private boolean deployJars;
	private String portletbridgeRuntime;
	private Text folderText;
	private DataModelSynchHelper synchHelper;
	private Button folderButton;
	private Button deployButton;
	private Combo portletBridgeLibraryCombo;
	private Combo userLibraries;
	private String userLibraryName;
	private Button add;
	private String implementationLibrary;
	private Button addRichFacesCapabilities;
	private boolean hasSeamFacet;
	private Button addRichFacesLibraries;
	private Combo richFacesLibraryCombo;
	private Text richfacesText;
	private Button richfacesButton;
	private String richfacesRuntime;
	private boolean richFacesLibrariesSelected;

	public JSFPortletFacetInstallPage() {
		super("JSFPortletProjectConfigurationWizardPage"); //$NON-NLS-1$
		setTitle(Messages.JSFPortletFacetInstallPage_JBoss_JSF_Portlet_Capabilities);
		setDescription(Messages.JSFPortletFacetInstallPage_Add_JBoss_JSF_Portlet_capabilities_to_this_Web_Project);
		// ImageDescriptor imageDesc = getDefaultPageImageDescriptor( );
		// if ( imageDesc != null )
		// setImageDescriptor( imageDesc );
		dialogSettings = PortletUIActivator.getDefault().getDialogSettings();
		jsfSection = dialogSettings.getSection(IPortletConstants.JSF_SECTION);
		if (jsfSection == null) {
			jsfSection = dialogSettings
					.addNewSection(IPortletConstants.JSF_SECTION);
			deployJars = true;
			portletbridgeRuntime = ""; //$NON-NLS-1$
			jsfSection.put(IPortletConstants.PORTLET_BRIDGE_RUNTIME,
					portletbridgeRuntime);
			
			richfacesRuntime = ""; //$NON-NLS-1$
			jsfSection.put(IPortletConstants.RICHFACES_RUNTIME,
					richfacesRuntime);
			
			implementationLibrary = IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE;
			jsfSection.put(IPortletConstants.IMPLEMENTATION_LIBRARY, implementationLibrary);
			
			richFacesLibrariesSelected = false;
			jsfSection.put(IPortletConstants.RICHFACES_LIBRARIES_SELECTED, richFacesLibrariesSelected);
			
			
		} else {
			deployJars = jsfSection.getBoolean(IPortletConstants.DEPLOY_JARS);
			portletbridgeRuntime = jsfSection
					.get(IPortletConstants.PORTLET_BRIDGE_RUNTIME);
			if (portletbridgeRuntime == null) {
				portletbridgeRuntime = ""; //$NON-NLS-1$
				jsfSection.put(IPortletConstants.PORTLET_BRIDGE_RUNTIME,
						portletbridgeRuntime);
			}
			implementationLibrary = jsfSection.get(IPortletConstants.IMPLEMENTATION_LIBRARY);
			if (implementationLibrary == null) {
				implementationLibrary = IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE;
				jsfSection.put(IPortletConstants.IMPLEMENTATION_LIBRARY, implementationLibrary);
			}
			richfacesRuntime = jsfSection
			.get(IPortletConstants.RICHFACES_RUNTIME);
			if (richfacesRuntime == null) {
				richfacesRuntime = ""; //$NON-NLS-1$
				jsfSection.put(IPortletConstants.RICHFACES_RUNTIME,
				richfacesRuntime);
			}
			richFacesLibrariesSelected = jsfSection.getBoolean(IPortletConstants.RICHFACES_LIBRARIES_SELECTED);
		}

	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		synchHelper = new DataModelSynchHelper(model);
		addFolderGroup(composite);
		// createButton(composite);
		setControl(composite);
	}

	private void createButton(Composite composite) {
		deployButton = new Button(composite, SWT.CHECK);
		deployButton.setText(Messages.JSFPortletFacetInstallPage_Deploy_jars);
		deployButton.setSelection(deployJars);
		deployButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setBooleanProperty(IPortletConstants.DEPLOY_JARS,
						deployButton.getSelection());
			}

		});
	}

	public void setConfig(Object config) {
		this.model = (IDataModel) config;
	}

	private void addFolderGroup(Composite composite) {
		portletBridgeLibraryCombo = new Combo(composite, SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		portletBridgeLibraryCombo.setLayoutData(gd);
		portletBridgeLibraryCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean enabled = IPortletConstants.USER_LIBRARY
						.equals(portletBridgeLibraryCombo.getText());
				userLibraries.setEnabled(enabled);
				add.setEnabled(enabled);

				enabled = IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE.equals(portletBridgeLibraryCombo.getText());
				folderText.setEnabled(enabled);
				folderButton.setEnabled(enabled);
				model.setProperty(IPortletConstants.IMPLEMENTATION_LIBRARY,
						portletBridgeLibraryCombo.getText());
				model.setProperty(IPortletConstants.PORTLET_BRIDGE_RUNTIME,folderText.getText().trim());
				model.setProperty(IPortletConstants.USER_LIBRARY_NAME,userLibraries.getText());
				if (IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE.equals(portletBridgeLibraryCombo.getText())) {
					richFacesLibraryCombo.removeAll();
					richFacesLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE);
					richFacesLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES);
					richFacesLibraryCombo.select(0);
				} else {
					richFacesLibraryCombo.removeAll();
					richFacesLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES);
					richFacesLibraryCombo.select(0);
				}
				validatePage();
			}

		});
		
		if (portletBridgeLibrariesExistOnServer()) {
			portletBridgeLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME);
		}
		portletBridgeLibraryCombo.add(IPortletConstants.USER_LIBRARY);
		
		addUserLibraryGroup(composite);
		
		portletBridgeLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE);
		
		addPortletBridgeGroup(composite);
		
		portletBridgeLibraryCombo.setText(implementationLibrary);
		
		synchHelper.synchCombo(portletBridgeLibraryCombo, IPortletConstants.IMPLEMENTATION_LIBRARY, null);
		
		Group richfacesGroup = new Group(composite, SWT.NONE);
		richfacesGroup.setLayout(new GridLayout(2, false));
		richfacesGroup.setText(Messages.JSFPortletFacetInstallPage_Richfaces_Capabilities);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		richfacesGroup.setLayoutData(gd);
		/*
		addRichFacesCapabilities= new Button(richfacesGroup,SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		addRichFacesCapabilities.setLayoutData(gd);
		synchHelper.synchCheckbox(addRichFacesCapabilities, IPortletConstants.RICHFACES_CAPABILITIES, null);
		
		addRichFacesCapabilities.setText("Add Richfaces Capabilities");
		
		if (hasSeamFacet) {
			addRichFacesCapabilities.setSelection(true);
			addRichFacesCapabilities.setEnabled(false);
		}
		*/
		addRichFacesLibraries= new Button(richfacesGroup,SWT.CHECK);
		addRichFacesLibraries.setText(Messages.JSFPortletFacetInstallPage_Add_Change_Richfaces_Libraries);
		synchHelper.synchCheckbox(addRichFacesLibraries, IPortletConstants.RICHFACES_LIBRARIES_SELECTED, null);
		
		addRichFacesLibraries.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				enableRichfacesLibraries();
			}
			
		});
		richFacesLibraryCombo = new Combo(richfacesGroup, SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		richFacesLibraryCombo.setLayoutData(gd);
		richFacesLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE);
		richFacesLibraryCombo.add(IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES);
		synchHelper.synchCombo(richFacesLibraryCombo, IPortletConstants.RICHFACES_LIBRARIES_TYPE, null);
		richFacesLibraryCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean enabled =IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES.equals(richFacesLibraryCombo.getText());
				richfacesText.setEnabled(enabled);
				richfacesButton.setEnabled(enabled);
				validatePage();
			}
			
		});
		addRichFaces(richfacesGroup);
		
		addRichFacesLibraries.setSelection(richFacesLibrariesSelected);
		enableRichfacesLibraries();
		richFacesLibraryCombo.select(0);
		
		validatePage();
	}

	private void addRichFaces(Composite parent) {
		richfacesText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		richfacesText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(richfacesText,
				IPortletConstants.RICHFACES_RUNTIME, null);

		richfacesText.setText(richfacesRuntime);
		richfacesText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validateRichFaces();
			}

		});

		richfacesButton = new Button(parent, SWT.PUSH);
		richfacesButton.setText(Messages.JSFPortletFacetInstallPage_Browse);
		richfacesButton
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		richfacesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleRichFacesButtonPressed();
			}

		});
	}

	private void addPortletBridgeGroup(Composite composite) {
		GridData gd;
		Group portletBridgeGroup = new Group(composite, SWT.NONE);
		portletBridgeGroup.setText(Messages.JSFPortletFacetInstallPage_Portletbridge_Runtime);
		portletBridgeGroup.setLayout(new GridLayout(2, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		portletBridgeGroup.setLayoutData(gd);
		
		folderText = new Text(portletBridgeGroup, SWT.SINGLE | SWT.BORDER);
		folderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(folderText,
				IPortletConstants.PORTLET_BRIDGE_RUNTIME, null);

		folderText.setText(portletbridgeRuntime);
		folderText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validatePortletBridge();
			}

		});

		folderButton = new Button(portletBridgeGroup, SWT.PUSH);
		folderButton.setText(Messages.JSFPortletFacetInstallPage_Browse);
		folderButton
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		folderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleFolderButtonPressed();
			}

		});
	}

	private void addUserLibraryGroup(Composite composite) {
		GridData gd;
		Group userLibrariesGroup = new Group(composite, SWT.NONE);
		userLibrariesGroup.setText(Messages.PortletFacetInstallPage_User_Library);
		userLibrariesGroup.setLayout(new GridLayout(2, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		userLibrariesGroup.setLayoutData(gd);
		userLibraries = new Combo(userLibrariesGroup, SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		userLibraries.setLayoutData(gd);
		synchHelper.synchCombo(userLibraries, IPortletConstants.USER_LIBRARY_NAME, null);
		setLibraryCombo();
		add = new Button(userLibrariesGroup, SWT.PUSH);
		add.setText(Messages.PortletFacetInstallPage_Add);
		add.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				WorkbenchPreferenceDialog dialog = WorkbenchPreferenceDialog
				.createDialogOn(shell, "org.eclipse.jdt.ui.preferences.UserLibraryPreferencePage"); //$NON-NLS-1$
				if (dialog != null) {
					dialog.open();
					setLibraryCombo();
				}
			}
			
		});
		boolean enabled = IPortletConstants.USER_LIBRARY.equals(portletBridgeLibraryCombo
				.getText());
		userLibraries.setEnabled(enabled);
		userLibraries.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setProperty(IPortletConstants.USER_LIBRARY_NAME,
										userLibraries
												.getText());
				userLibraryName = userLibraries.getText();
				validatePage();
			}
		});
		add.setEnabled(enabled);
	}

	private void setLibraryCombo() {
		String[] libraryNames = JavaCore.getUserLibraryNames();
		boolean exists = false;
		for (int i = 0; i < libraryNames.length; i++) {
			userLibraries.add(libraryNames[i]);
			if (libraryNames[i].equals(userLibraryName)) {
				exists = true;
			}
		}
		if (exists) {
			userLibraries.setText(userLibraryName);
		} else {
			userLibraries.select(0);
			userLibraryName = userLibraries.getText();
		}
		validatePage();
	}
	
	private void validatePage() {
		if (!validatePortletBridge()) {
			return;
		}
		if (!validateRichFaces()) {
			return;
		}
		if (portletBridgeLibraryCombo.getText().equals(IPortletConstants.USER_LIBRARY) && userLibraries.getText().trim().length() <= 0) {
			setErrorMessage(Messages.PortletFacetInstallPage_You_have_to_choose_an_user_library);
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}

	}
	
	private boolean portletBridgeLibrariesExistOnServer() {
		IFacetedProjectWorkingCopy fpwc = (IFacetedProjectWorkingCopy) model.getProperty(IFacetDataModelProperties.FACETED_PROJECT_WORKING_COPY);
		Set<IProjectFacetVersion> facets = fpwc.getProjectFacets();
		hasSeamFacet = false;
		for(IProjectFacetVersion facet:facets) {
			IProjectFacet projectFacet = facet.getProjectFacet();
			if ("jst.seam".equals(projectFacet.getId())) { //$NON-NLS-1$
				hasSeamFacet = true;
				break;
			}
		}
		
		org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime = fpwc.getPrimaryRuntime();
		if (facetRuntime == null) {
			return false;
		}
		IRuntime runtime = PortletCoreActivator.getRuntime(facetRuntime);
		IJBossServerRuntime jbossRuntime = (IJBossServerRuntime)runtime.loadAdapter(IJBossServerRuntime.class, new NullProgressMonitor());
		if (jbossRuntime != null) {
			// JBoss Portal server
			IPath jbossLocation = runtime.getLocation();
			IPath configPath = jbossLocation.append(IJBossServerConstants.SERVER).append(jbossRuntime.getJBossConfiguration());
			File configFile = configPath.toFile();
			if (hasPortletbridgeLibraries(new File(configFile,IPortletConstants.PORTLET_SAR_LIB))) {
				return true;
			}
			if (hasPortletbridgeLibraries(new File(configFile,IPortletConstants.PORTLET_SAR_HA_LIB))) {
				return true;
			}
		}
		return false;
	}

	private boolean hasPortletbridgeLibraries(File file) {
		if (file != null && file.isDirectory()) {
			String[] list = file.list(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					if ("portletbridge-api.jar".equals(name) || //$NON-NLS-1$
							"portletbridge-impl.jar".equals(name)) { //$NON-NLS-1$
						return true;
					}
					return false;
				}
				
			});
			return list.length == 2;
		}
		return false;
	}
	
	private boolean validatePortletBridge() {
		if (!IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE.equals(portletBridgeLibraryCombo.getText())) {
			setErrorMessage(null);
			setPageComplete(true);
			return true;
		}
		String folderString = folderText.getText();
		folderString = folderString.trim();
		if (folderString.length() <= 0) {
			setErrorMessage(Messages.JSFPortletFacetInstallPage_Portletbridge_Runtime_directory_is_required);
			setPageComplete(false);
			return false;
		}
		File folder = new File(folderString);
		if (!folder.exists() || !folder.isDirectory()) {
			setInvalidPortletbridgeRuntime();
			return false;
		}
		String[] fileList = folder.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.startsWith("portletbridge") || name.endsWith(".jar")) { //$NON-NLS-1$ //$NON-NLS-2$
					return true;
				}
				return false;
			}

		});
		if (fileList.length < 2) {
			setInvalidPortletbridgeRuntime();
			return false;
		}
		portletbridgeRuntime = folderText.getText();
		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	private boolean validateRichFaces() {
		if (richFacesLibraryCombo == null) {
			return true;
		}
		if (!IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES.equals(richFacesLibraryCombo.getText())) {
			setErrorMessage(null);
			setPageComplete(true);
			return true;
		}
		String folderString = richfacesText.getText();
		folderString = folderString.trim();
		if (folderString.length() <= 0) {
			setErrorMessage(Messages.JSFPortletFacetInstallPage_RichFaces_Runtime_directory_is_required);
			setPageComplete(false);
			return false;
		}
		File folder = new File(folderString);
		if (!folder.exists() || !folder.isDirectory()) {
			setInvalidRichfacesRuntime();
			return false;
		}
		folder = new File(folder,"lib"); //$NON-NLS-1$
		if (!folder.exists() || !folder.isDirectory()) {
			setInvalidRichfacesRuntime();
			return false;
		}
		String[] fileList = folder.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.startsWith("richfaces") || name.endsWith(".jar")) { //$NON-NLS-1$ //$NON-NLS-2$
					return true;
				}
				return false;
			}

		});
		if (fileList.length < 3) {
			setInvalidRichfacesRuntime();
			return false;
		}
		richfacesRuntime = richfacesText.getText();
		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	private void setInvalidRichfacesRuntime() {
		setErrorMessage(Messages.JSFPortletFacetInstallPage_Invalid_Richfaces_Runtime_directory);
		setPageComplete(false);
	}
	
	private void setInvalidPortletbridgeRuntime() {
		setErrorMessage(Messages.JSFPortletFacetInstallPage_Invalid_Portletbridge_Runtime_directory);
		setPageComplete(false);
	}

	private void handleFolderButtonPressed() {
		String lastPath = folderText.getText();
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.MULTI);
		dialog.setText(Messages.JSFPortletFacetInstallPage_Select_Portletbridge_Runtime);
		dialog.setFilterPath(lastPath);
		String res = dialog.open();
		if (res == null) {
			return;
		}
		String newPath = dialog.getFilterPath();
		folderText.setText(newPath);
	}
	
	private void handleRichFacesButtonPressed() {
		String lastPath = richfacesText.getText();
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.MULTI);
		dialog.setText(Messages.JSFPortletFacetInstallPage_Select_RichFaces_Runtime);
		dialog.setFilterPath(lastPath);
		String res = dialog.open();
		if (res == null) {
			return;
		}
		String newPath = dialog.getFilterPath();
		richfacesText.setText(newPath);
	}

	@Override
	public void transferStateToConfig() {
		super.transferStateToConfig();
		if (deployButton != null) {
			jsfSection.put(IPortletConstants.DEPLOY_JARS, deployButton
					.getSelection());
		}
		jsfSection.put(IPortletConstants.PORTLET_BRIDGE_RUNTIME,
				portletbridgeRuntime);
		jsfSection.put(IPortletConstants.RICHFACES_RUNTIME,
				richfacesRuntime);
		jsfSection.put(IPortletConstants.IMPLEMENTATION_LIBRARY, 
				implementationLibrary);
		jsfSection.put(IPortletConstants.RICHFACES_LIBRARIES_SELECTED, 
				richFacesLibrariesSelected);
		
	}

	private void enableRichfacesLibraries() {
		boolean enabled = addRichFacesLibraries.getSelection();
		richFacesLibraryCombo.setEnabled(enabled);
		if (enabled) {
			enabled =IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES.equals(richFacesLibraryCombo.getText());
			richfacesText.setEnabled(enabled);
			richfacesButton.setEnabled(enabled);
		} else {
			richfacesText.setEnabled(false);
			richfacesButton.setEnabled(false);
		}
	}

}

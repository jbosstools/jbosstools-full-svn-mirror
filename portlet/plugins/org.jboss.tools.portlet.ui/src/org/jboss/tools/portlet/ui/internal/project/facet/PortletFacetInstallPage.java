package org.jboss.tools.portlet.ui.internal.project.facet;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.eclipse.wst.common.project.facet.ui.IWizardContext;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * @author snjeza
 */
public class PortletFacetInstallPage extends DataModelWizardPage implements
		IFacetWizardPage {

	private IDialogSettings dialogSettings;
	private Combo implementationLibraryCombo;
	private boolean deployPortletJars;
	private boolean enableImplementationLibraryValue;
	private Button enableImplementationLibrary;
	//private Button deployButton;
	private IDialogSettings portletSection;
	private String implementationLibrary;
	private Combo userLibraries;
	private String userLibraryName=null;
	private Button add;

	public PortletFacetInstallPage() {
		super(DataModelFactory.createDataModel(new AbstractDataModelProvider() {
		}), "jboss.portal.facet.install.page"); //$NON-NLS-1$
		setTitle(Messages.PortletFacetInstallPage_JBoss_Portlet_Capabilities);
		setDescription(Messages.PortletFacetInstallPage_Add_JBoss_Portlet_capabilities_to_this_Web_Project);
		dialogSettings = PortletUIActivator.getDefault().getDialogSettings();
		portletSection = dialogSettings
				.getSection(IPortletConstants.PORTLET_SECTION);
		if (portletSection == null) {
			portletSection = dialogSettings
					.addNewSection(IPortletConstants.PORTLET_SECTION);
			deployPortletJars = false;
			enableImplementationLibraryValue = true;
			portletSection.put(IPortletConstants.DEPLOY_PORTLET_JARS,
					deployPortletJars);
			portletSection.put(IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY,
					enableImplementationLibraryValue);
			implementationLibrary = getDefaultImplementationLibrary();
			portletSection.put(IPortletConstants.IMPLEMENTATION_LIBRARY,
					implementationLibrary);
			userLibraryName = null;
		} else {
			deployPortletJars = portletSection
					.getBoolean(IPortletConstants.DEPLOY_PORTLET_JARS);
			enableImplementationLibraryValue = portletSection
					.getBoolean(IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY);
			implementationLibrary = portletSection
					.get(IPortletConstants.IMPLEMENTATION_LIBRARY);
			if (implementationLibrary == null
					|| implementationLibrary.trim().length() <= 0) {
				implementationLibrary = getDefaultImplementationLibrary();
				portletSection.put(IPortletConstants.IMPLEMENTATION_LIBRARY,
						implementationLibrary);
			}
			userLibraryName = portletSection.get(IPortletConstants.USER_LIBRARY_NAME);
			
		}
	}

	private String getDefaultImplementationLibrary() {
		boolean checkRuntimes = PortletCoreActivator.getDefault()
				.getPluginPreferences().getBoolean(
						PortletCoreActivator.CHECK_RUNTIMES);
		if (checkRuntimes) {
			implementationLibrary = IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME;
		} else {
			implementationLibrary = IPortletConstants.LIBRARY_PROVIDED_BY_JBOSS_TOOLS;
		}
		return implementationLibrary;
	}

	@Override
	protected Composite createTopLevelComposite(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		enableImplementationLibrary = new Button(composite, SWT.CHECK);
		enableImplementationLibrary.setText(Messages.PortletFacetInstallPage_Enable_implementation_library);
		enableImplementationLibrary
				.setSelection(enableImplementationLibraryValue);
		enableImplementationLibrary
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						boolean enabled = enableImplementationLibrary
						.getSelection();
						model.setBooleanProperty(
										IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY,
										enabled);
						implementationLibraryCombo.setEnabled(enabled);
						if (enabled) {
							boolean enableUserLibs = IPortletConstants.USER_LIBRARY.equals(implementationLibraryCombo.getText());
							userLibraries.setEnabled(enableUserLibs);
							add.setEnabled(enableUserLibs);
						} else {
							userLibraries.setEnabled(enabled);
							add.setEnabled(enabled);
						}
						//deployButton.setEnabled(enabled);
						validatePage();
					}

				});
		implementationLibraryCombo = new Combo(composite, SWT.READ_ONLY);
		implementationLibraryCombo.add(IPortletConstants.USER_LIBRARY);
		implementationLibraryCombo.add(IPortletConstants.LIBRARY_PROVIDED_BY_JBOSS_TOOLS);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		implementationLibraryCombo.setLayoutData(gd);
		boolean checkRuntimes = PortletCoreActivator.getDefault()
				.getPluginPreferences().getBoolean(
						PortletCoreActivator.CHECK_RUNTIMES);
		if (checkRuntimes) {
			implementationLibraryCombo
					.add(IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME);
			implementationLibraryCombo.setText(implementationLibrary);
		} else {
			if (IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME
					.equals(implementationLibrary)) {
				implementationLibraryCombo
						.setText(IPortletConstants.LIBRARY_PROVIDED_BY_JBOSS_TOOLS);
			} else {
				implementationLibraryCombo.setText(implementationLibrary);
			}
		}
		model.setProperty(IPortletConstants.IMPLEMENTATION_LIBRARY,
				implementationLibraryCombo
						.getText());
		Group userLibrariesGroup = new Group(composite, SWT.NONE);
		userLibrariesGroup.setText(Messages.PortletFacetInstallPage_User_Library);
		userLibrariesGroup.setLayout(new GridLayout(2, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		userLibrariesGroup.setLayoutData(gd);
		userLibraries = new Combo(userLibrariesGroup, SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		userLibraries.setLayoutData(gd);
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
		boolean enabled = IPortletConstants.USER_LIBRARY.equals(implementationLibraryCombo
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
		implementationLibraryCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean enabled = IPortletConstants.USER_LIBRARY
						.equals(implementationLibraryCombo.getText());
				userLibraries.setEnabled(enabled);
				add.setEnabled(enabled);
				model.setProperty(IPortletConstants.IMPLEMENTATION_LIBRARY,
										implementationLibraryCombo
												.getText());
				validatePage();
			}

		});

		model.setProperty(IPortletConstants.USER_LIBRARY_NAME,
				userLibraries
						.getText());
		/*deployButton = new Button(composite, SWT.CHECK);
		deployButton.setText(Messages.JSFPortletFacetInstallPage_Deploy_jars);
		deployButton.setSelection(deployPortletJars);
		deployButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setBooleanProperty(IPortletConstants.DEPLOY_PORTLET_JARS,
						deployButton.getSelection());
			}

		}); */
		validatePage();
		return composite;
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

	@Override
	protected String[] getValidationPropertyNames() {
		return new String[0];
	}

	public void setConfig(Object config) {
		model.removeListener(this);
		synchHelper.dispose();

		model = (IDataModel) config;
		model.addListener(this);
		synchHelper = initializeSynchHelper(model);
	}

	@Override
	public void dispose() {
		model.removeListener(this);
		super.dispose();
	}

	public void setWizardContext(IWizardContext context) {

	}

	public void transferStateToConfig() {

	}

	@Override
	public void storeDefaultSettings() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				/*if (deployButton != null) {
					portletSection.put(IPortletConstants.DEPLOY_PORTLET_JARS,
							deployButton.getSelection());
				}*/
				if (enableImplementationLibrary != null) {
					portletSection.put(IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY,
							enableImplementationLibrary.getSelection());
				}
				if (implementationLibraryCombo != null) {
					portletSection.put(IPortletConstants.IMPLEMENTATION_LIBRARY,
							implementationLibraryCombo.getText());
				}
				if (userLibraries != null) {
					portletSection.put(IPortletConstants.USER_LIBRARY_NAME,
							userLibraries.getText());
				}
				
			}
			
		});
		super.storeDefaultSettings();
	}
	
	@Override
	protected void validatePage() {
		//super.validatePage();
		if (enableImplementationLibrary == null) {
			return;
		}
		if (enableImplementationLibrary.getSelection() && implementationLibraryCombo.getText().equals(IPortletConstants.USER_LIBRARY) && userLibraries.getText().trim().length() <= 0) {
			setErrorMessage(Messages.PortletFacetInstallPage_You_have_to_choose_an_user_library);
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

}

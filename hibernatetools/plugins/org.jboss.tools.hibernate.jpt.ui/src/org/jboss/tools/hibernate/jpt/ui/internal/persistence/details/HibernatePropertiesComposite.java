/*******************************************************************************
 * Copyright (c) 2008-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.jpt.ui.internal.persistence.details;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.listeners.SWTPropertyChangeListenerWrapper;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.StringConverter;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.SimpleListValueModel;
import org.eclipse.jpt.common.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.common.utility.model.listener.PropertyChangeListener;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.WritablePropertyValueModel;
import org.eclipse.jpt.jpa.ui.details.JpaPageComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.utils.DialogSelectionHelper;
import org.hibernate.eclipse.console.utils.DriverClassHelpers;
import org.hibernate.eclipse.console.wizards.NewConfigurationWizard;
import org.hibernate.eclipse.console.wizards.NewConfigurationWizardPage;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.BasicHibernateProperties;
import org.jboss.tools.hibernate.jpt.ui.wizard.Messages;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernatePropertiesComposite extends Pane<BasicHibernateProperties> implements
		JpaPageComposite {

	private Text cfgFile;

	DriverClassHelpers helper;

	/**
	 * @param subjectHolder
	 * @param container
	 * @param widgetFactory
	 */
	public HibernatePropertiesComposite(PropertyValueModel<BasicHibernateProperties> subjectHolder,
			Composite container, WidgetFactory widgetFactory) {
		super(subjectHolder, container, widgetFactory);
	}

	@Override
	protected void initializeLayout(Composite container) {

		Composite section = addSection(container, Messages.HibernatePropertiesComposite_basic_properties);

		this.helper = new DriverClassHelpers();

		final SimpleListValueModel<String> lvmDialect = new SimpleListValueModel<String>(Arrays.asList(this.helper
				.getDialectNames()));
		PropertyValueModel<BasicHibernateProperties> p = getSubjectHolder();
		List<String> drivers = new ArrayList<String>();
		BasicHibernateProperties props = p.getValue();
		if (props != null) {
			String dialectClass = this.helper.getDialectClass(props.getDialect());
			String[] driverClasses = this.helper.getDriverClasses(dialectClass);
			drivers.addAll(Arrays.asList(driverClasses));
		}

		final SimpleListValueModel<String> lvmDriver = new SimpleListValueModel<String>(drivers);

		List<String> urls = new ArrayList<String>();
		if (props != null) {
			String driverClass = props.getDriver();
			String[] connectionURLS = this.helper.getConnectionURLS(driverClass);
			urls.addAll(Arrays.asList(connectionURLS));
		}
		final SimpleListValueModel<String> lvmUrl = new SimpleListValueModel<String>(urls);

		WritablePropertyValueModel<String> dialectHolder = buildDialectHolder();
		final WritablePropertyValueModel<String> driverHolder = buildDriverHolder();
		final WritablePropertyValueModel<String> urlHolder = buildUrlHolder();

		Button b = addButton(section, HibernateConsoleMessages.CodeGenerationSettingsTab_setup, createSetupAction());
		this.cfgFile = addLabeledText(section,
				HibernateConsoleMessages.ConsoleConfigurationPropertySource_config_file + ':', buildConfigFileHolder(),
				b, null);

		addLabeledEditableCombo(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_database_dialect,
				lvmDialect,
				dialectHolder,
				StringConverter.Default.<String>instance(),
				null);

		addLabeledEditableCombo(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_driver_class,
				lvmDriver,
				driverHolder,
				StringConverter.Default.<String>instance(),
				null);

		addLabeledEditableCombo(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_connection_url,
				lvmUrl,
				urlHolder,
				StringConverter.Default.<String>instance(),
				null);

		dialectHolder.addPropertyChangeListener(PropertyValueModel.VALUE, new SWTPropertyChangeListenerWrapper(
				new PropertyChangeListener() {
					@Override
					public void propertyChanged(PropertyChangeEvent event) {
						String dialectClass = HibernatePropertiesComposite.this.helper.getDialectClass((String) event.getNewValue());
						String[] driverClasses = HibernatePropertiesComposite.this.helper.getDriverClasses(dialectClass);
						String driver = driverHolder.getValue();//save value
						lvmDriver.clear();
						lvmDriver.addAll(Arrays.asList(driverClasses));
						driverHolder.setValue(driver);		//restore value
					}
				}
			)
		);

		driverHolder.addPropertyChangeListener( PropertyValueModel.VALUE, new SWTPropertyChangeListenerWrapper(
				new PropertyChangeListener() {
					@Override
					public void propertyChanged(PropertyChangeEvent event) {
						String driverClass = (String) event.getNewValue();
						String[] connectionURLS = HibernatePropertiesComposite.this.helper.getConnectionURLS(driverClass);
						String url = urlHolder.getValue();//save value
						lvmUrl.clear();
						lvmUrl.addAll(Arrays.asList(connectionURLS));
						urlHolder.setValue(url);		//restore value
					}
				}
			) );

		addLabeledText(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_default_schema,
				buildSchemaDefaultHolder());

		addLabeledText(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_default_catalog,
				buildCatalogDefaultHolder());

		addLabeledText(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_user_name,
				buildUsernameHolder());

		addLabeledText(
				section,
				HibernateConsoleMessages.NewConfigurationWizardPage_password,
				buildPasswordHolder());
	}

	private IPath getConfigurationFilePath() {
		BasicHibernateProperties props = getSubject();
		String filePath = cfgFile.getText().trim();
		if (props != null && filePath.length() > 0 ){
			IProject project = props.getJpaProject().getProject();
			IJavaProject jProject = JavaCore.create(project);
			if (jProject != null){
				try {
					IPackageFragmentRoot[] allPackageFragmentRoots = jProject.getAllPackageFragmentRoots();
					for (IPackageFragmentRoot iPackageFragmentRoot : allPackageFragmentRoots) {
						if (!iPackageFragmentRoot.isArchive()){
							IResource sourceFolder = iPackageFragmentRoot.getResource();
							if (sourceFolder instanceof IContainer) {
								IContainer folder = (IContainer) sourceFolder;
								if (folder.findMember(filePath) != null){
									return folder.findMember(filePath).getFullPath();
								}
							}
						}
					}
				} catch (JavaModelException e) {
					//ignore
				}
			}
		}
		return null;
	}

	private Runnable createSetupAction() {
		return new Runnable() {
			@Override
			public void run() {
				IPath initialPath = getConfigurationFilePath();
				int defaultChoice = 0;
				if(initialPath!=null) {
		    		defaultChoice = 1;
		    	}
				MessageDialog dialog = createSetupDialog(HibernateConsoleMessages.ConsoleConfigurationMainTab_setup_configuration_file, HibernateConsoleMessages.ConsoleConfigurationMainTab_do_you_want_to_create_new_cfgxml, defaultChoice);
				int answer = dialog.open();
				IPath cfgFile = null;
				if(answer==0) { // create new
					cfgFile = handleConfigurationFileCreate();
				} else if (answer==1) { // use existing
					cfgFile = handleConfigurationFileBrowse();
				}
				if (cfgFile != null){
					HibernatePropertiesComposite.this.cfgFile.setText( makeClassPathRelative(cfgFile).toString() );
				}
			}
			
			protected IPath makeClassPathRelative(IPath cfgFile){
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IResource res = root.findMember(cfgFile);
				if ( res != null && res.exists() && res.getType() == IResource.FILE) {
					IProject project = res.getProject();
					IJavaProject jProject = JavaCore.create(project);
					if (jProject != null){
						try {
							IPackageFragmentRoot[] allPackageFragmentRoots = jProject.getAllPackageFragmentRoots();
							for (IPackageFragmentRoot iPackageFragmentRoot : allPackageFragmentRoots) {
								if (!iPackageFragmentRoot.isArchive()){
									if (iPackageFragmentRoot.getResource().getFullPath().isPrefixOf(cfgFile)){
										cfgFile = cfgFile.removeFirstSegments(iPackageFragmentRoot.getResource().getFullPath().segmentCount());
										return cfgFile.makeAbsolute();
									}
								}
							}
						} catch (JavaModelException e) {
							//ignore
						}
					}
				}
				return cfgFile;
			}

			private MessageDialog createSetupDialog(String title, String question, int defaultChoice){
				return new MessageDialog(getShell(),
						title,
						null,
						question,
						MessageDialog.QUESTION,
						new String[] { HibernateConsoleMessages.ConsoleConfigurationMainTab_create_new, HibernateConsoleMessages.ConsoleConfigurationMainTab_use_existing, IDialogConstants.CANCEL_LABEL},
						defaultChoice);
			}

			private IPath handleConfigurationFileBrowse() {
				IPath initialPath = getConfigurationFilePath();
				IPath[] paths = DialogSelectionHelper.chooseFileEntries(getShell(),  initialPath, new IPath[0], HibernateConsoleMessages.ConsoleConfigurationMainTab_select_hibernate_cfg_xml_file, HibernateConsoleMessages.ConsoleConfigurationMainTab_choose_file_to_use_as_hibernate_cfg_xml, new String[] {HibernateConsoleMessages.ConsoleConfigurationMainTab_cfg_xml}, false, false, true);
				if(paths!=null && paths.length==1) {
					return paths[0];
				}
				return null;
			}
			
			private IPath handleConfigurationFileCreate() {
				NewConfigurationWizard wizard = new NewConfigurationWizard();
				wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY );
				IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

				WizardDialog wdialog = new WizardDialog(win.getShell(), wizard);
				wdialog.create();
				IWizardPage configPage = wizard.getPage(HibernateConsoleMessages.ConsoleConfigurationMainTab_wizard_page);
				if (configPage != null && configPage instanceof NewConfigurationWizardPage){
					((NewConfigurationWizardPage)configPage).setCreateConsoleConfigurationVisible(false);
				}
				// This opens a dialog
				if (wdialog.open() == Window.OK){
					WizardNewFileCreationPage createdFilePath = ((WizardNewFileCreationPage)wizard.getStartingPage());
					if(createdFilePath!=null) {
						// createNewFile() does not creates new file if it was created by wizard (OK was pressed)
						return createdFilePath.createNewFile().getFullPath();
					}
				}
				return null;
			}
		};
	}

	private WritablePropertyValueModel<String> buildConfigFileHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.CONFIG_FILE_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getConfigurationFile();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setConfigurationFile(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildDialectHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.DIALECT_PROPERTY) {
			@Override
			protected String buildValue_() {
				return HibernatePropertiesComposite.this.helper.getShortDialectName(this.subject.getDialect());
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null; //$NON-NLS-1$
				this.subject.setDialect(HibernatePropertiesComposite.this.helper.getDialectClass(value));
			}
		};
	}

	private WritablePropertyValueModel<String> buildDriverHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.DRIVER_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getDriver();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setDriver(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildUrlHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.URL_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getUrl();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setUrl(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildSchemaDefaultHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.SCHEMA_DEFAULT_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getSchemaDefault();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setSchemaDefault(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildCatalogDefaultHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.CATALOG_DEFAULT_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getCatalogDefault();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setCatalogDefault(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildUsernameHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.USERNAME_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getUsername();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setUsername(value);
			}

			@Override
			protected void propertyChanged() {
				// TODO Auto-generated method stub
				super.propertyChanged();
			}
		};
	}

	private WritablePropertyValueModel<String> buildPasswordHolder() {
		return new PropertyAspectAdapter<BasicHibernateProperties, String>(getSubjectHolder(),
				BasicHibernateProperties.PASSWORD_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getPassword();
			}

			@Override
			protected void setValue_(String value) {
				if ("".equals(value))value = null;//$NON-NLS-1$
				this.subject.setPassword(value);
			}
		};
	}

	@Override
	public String getHelpID() {
		// TODO help
		return null;
	}

	public Image getPageImage() {
		return null;
	}

	@Override
	public String getPageText() {
		return Messages.HibernatePropertiesComposite_hibernate;
	}

	@Override
	public ImageDescriptor getPageImageDescriptor() {
		// TODO hibernate tab in persistence.xml image
		return null;
	}
}

/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.smooks.ConditionType;
import org.jboss.tools.smooks.model.smooks.ConditionsType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.ParamsType;
import org.jboss.tools.smooks.model.smooks.SmooksFactory;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class SmooksConfigurationOverviewPage extends FormPage implements ISmooksModelValidateListener,
		ISmooksGraphChangeListener, ISourceSynchronizeListener {

	private ISmooksModelProvider smooksModelProvider;
	private Button newParamButton;
	private Button removeParamButton;
	private Button upParamButton;
	private Button downParamButton;
	private Button paramPropertiesButton;
	private TableViewer paramViewer;
	private TableViewer conditionViewer;
	private Button newConditionButton;
	private Button removeConditionButton;
	private Button upConditionButton;
	private Button downConditionButton;
	private Button conditionPropertiesButton;
	private Composite defaultSettingComposite;
	private Button newProfileButton;
	private Button removeProfileButton;
	private Button upProfileButton;
	private Button downProfileButton;
	private Button profilePropertiesButton;
	private TableViewer profileViewer;
	private ModelPanelCreator defaultSettingPanelCreator;
	private Section generalSettingSection;
	private Section globalParamSection;
	private Section conditionSection;
	private Section profilesSection;
	private Section settingSection;

	public SmooksConfigurationOverviewPage(FormEditor editor, String id, String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
	}

	public SmooksConfigurationOverviewPage(String id, String title, ISmooksModelProvider provider) {
		super(id, title);
		this.smooksModelProvider = provider;
	}

	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		String title = getTitle();
		form.setText(title);
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		form.getBody().setLayout(gl);

		ScrolledPageBook pageBook = new ScrolledPageBook(form.getBody());
		pageBook.setBackground(toolkit.getColors().getBackground());
		Composite mainComposite = pageBook.createPage(pageBook);
		pageBook.showPage(pageBook);

		// Composite mainComposite = toolkit.createComposite(form.getBody());

		GridData gd = new GridData(GridData.FILL_BOTH);
		pageBook.setLayoutData(gd);

		GridLayout mgl = new GridLayout();
		mgl.numColumns = 2;
		mgl.marginHeight = 13;
		mgl.horizontalSpacing = 20;
		mainComposite.setLayout(mgl);

		settingSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR);
		settingSection.setLayout(new FillLayout());
		settingSection.setText("Smooks configuration");
		settingSection.setDescription("Set the description for this Smooks configuration file.");
		Composite settingComposite = toolkit.createComposite(settingSection);
		settingSection.setClient(settingComposite);
		gd = new GridData();
		gd.widthHint = 500;
		settingSection.setLayoutData(gd);

		GridLayout sgl = new GridLayout();
		settingComposite.setLayout(sgl);
		sgl.numColumns = 2;
		// sgl.verticalSpacing = 8;

		createSettingSection(settingComposite, toolkit);

		createSmooksEditorNavigator(mainComposite, toolkit);

		globalParamSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR
				| Section.TWISTIE | Section.EXPANDED);
		globalParamSection.setDescription("Define the global parameters for the Smooks configuration file");
		globalParamSection.setText("Global Paramters");
		globalParamSection.setLayout(new FillLayout());
		Composite globalParamComposite = toolkit.createComposite(globalParamSection);
		globalParamSection.setClient(globalParamComposite);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.widthHint = 500;
		globalParamSection.setLayoutData(gd);

		GridLayout gpgl = new GridLayout();
		globalParamComposite.setLayout(gpgl);
		gpgl.numColumns = 2;

		createGlobalParamterSection(globalParamComposite, toolkit);

		generalSettingSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR
				| Section.TWISTIE | Section.EXPANDED);
		generalSettingSection.setLayout(new FillLayout());
		generalSettingSection.setText("Smooks Default Setting");
		generalSettingSection.setDescription("Define the Smooks configuration file default setting");
		defaultSettingComposite = toolkit.createComposite(generalSettingSection);
		generalSettingSection.setClient(defaultSettingComposite);
		gd = new GridData();
		gd.widthHint = 500;
		generalSettingSection.setLayoutData(gd);

		GridLayout ggl = new GridLayout();
		defaultSettingComposite.setLayout(ggl);
		ggl.numColumns = 2;
		ggl.verticalSpacing = 0;

		createDefaultSection(defaultSettingComposite, toolkit);

		conditionSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR
				| Section.TWISTIE);
		conditionSection.setText("Conditions");
		conditionSection.setDescription("Define the conditions");
		conditionSection.setLayout(new FillLayout());
		Composite conditionComposite = toolkit.createComposite(conditionSection);
		conditionSection.setClient(conditionComposite);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.widthHint = 500;
		conditionSection.setLayoutData(gd);

		GridLayout cgl = new GridLayout();
		conditionComposite.setLayout(cgl);
		cgl.numColumns = 2;

		createConditionsSection(conditionComposite, toolkit);

		profilesSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR
				| Section.TWISTIE);
		profilesSection.setDescription("Define the profiles");
		profilesSection.setText("Profiles");
		profilesSection.setLayout(new FillLayout());
		Composite profilesComposite = toolkit.createComposite(profilesSection);
		profilesSection.setClient(profilesComposite);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.widthHint = 500;
		profilesSection.setLayoutData(gd);

		GridLayout pgl = new GridLayout();
		profilesComposite.setLayout(pgl);
		pgl.numColumns = 2;

		createProfilesSection(profilesComposite, toolkit);

	}

	private void createSettingSection(Composite settingComposite, FormToolkit toolkit) {
		toolkit.createLabel(settingComposite, "Smooks Platform Version : ").setForeground(
				toolkit.getColors().getColor(IFormColors.TITLE));
		int type = SWT.BORDER;
		if (SmooksUIUtils.isLinuxOS()) {
			type = SWT.BORDER;
		}
		final Combo combo = new Combo(settingComposite, type | SWT.READ_ONLY);
		combo.setEnabled(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gd);
		for (int i = 0; i < SmooksConstants.SMOOKS_VERSIONS.length; i++) {
			String version = SmooksConstants.SMOOKS_VERSIONS[i];
			combo.add(version);
		}

		String version = getSmooksVersion();
		if (version != null)
			combo.setText(version);
		combo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String v = combo.getText();
				if (smooksModelProvider != null) {
					smooksModelProvider.getSmooksGraphicsExt().setPlatformVersion(v);
				}
			}
		});

		toolkit.createLabel(settingComposite, "Name : ").setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		final Text nameText = toolkit.createText(settingComposite, "", SWT.NONE);
		nameText.setLayoutData(gd);
		String name = smooksModelProvider.getSmooksGraphicsExt().getName();
		if (name != null)
			nameText.setText(name);
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (smooksModelProvider != null) {
					smooksModelProvider.getSmooksGraphicsExt().setName(nameText.getText());
				}
			}
		});

		toolkit.paintBordersFor(settingComposite);

		toolkit.createLabel(settingComposite, "Author : ").setForeground(
				toolkit.getColors().getColor(IFormColors.TITLE));
		final Text authorText = toolkit.createText(settingComposite, "", SWT.NONE);
		authorText.setLayoutData(gd);

		String author = smooksModelProvider.getSmooksGraphicsExt().getAuthor();
		if (author != null)
			authorText.setText(author);
		authorText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (smooksModelProvider != null) {
					smooksModelProvider.getSmooksGraphicsExt().setAuthor(authorText.getText());
				}
			}
		});

		toolkit.createLabel(settingComposite, "");

		toolkit.paintBordersFor(settingComposite);
	}

	protected void createProfilesSection(Composite profilesComposite, FormToolkit toolkit) {
		if (smooksModelProvider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
					.getEditingDomain();
			EObject profiles = getProfilesType();
			// if (m == null)
			// return;

			profileViewer = new TableViewer(profilesComposite);
			GridData gd = new GridData(GridData.FILL_BOTH);
			profileViewer.getControl().setLayoutData(gd);
			toolkit.paintBordersFor(profilesComposite);
			Composite buttonArea = toolkit.createComposite(profilesComposite);
			gd = new GridData(GridData.FILL_VERTICAL);
			gd.widthHint = 30;
			GridLayout bgl = new GridLayout();
			buttonArea.setLayout(bgl);

			newProfileButton = toolkit.createButton(buttonArea, "New", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			newProfileButton.setLayoutData(gd);

			removeProfileButton = toolkit.createButton(buttonArea, "Remove", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			removeProfileButton.setLayoutData(gd);

			upProfileButton = toolkit.createButton(buttonArea, "Up", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			upProfileButton.setLayoutData(gd);

			downProfileButton = toolkit.createButton(buttonArea, "Down", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			downProfileButton.setLayoutData(gd);

			profilePropertiesButton = toolkit.createButton(buttonArea, "Properties..", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			profilePropertiesButton.setLayoutData(gd);

			profileViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()) {

				@Override
				public boolean hasChildren(Object object) {
					return false;
				}

			});

			profileViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
					.getAdapterFactory()) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
				 * # getText(java.lang.Object)
				 */
				@Override
				public String getText(Object object) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (obj instanceof AbstractAnyType) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));

			if (profiles != null) {
				profileViewer.setInput(profiles);
			}

			profileViewer.addDoubleClickListener(new IDoubleClickListener() {

				public void doubleClick(DoubleClickEvent event) {
					openProfilePropertiesModifyDialog();
				}
			});

			profileViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					updateProfilesButtons();
				}
			});

			hookProfilesButtons();
			updateProfilesButtons();
		}
	}

	protected void openProfilePropertiesModifyDialog() {
		IStructuredSelection selection = (IStructuredSelection) profileViewer.getSelection();
		if (selection == null)
			return;
		Object obj = selection.getFirstElement();
		if (obj instanceof EObject) {
			EObject profile = (EObject) obj;
			EObject parent = getProfilesType();
			EStructuralFeature profileFeature = null;
			if (SmooksConstants.VERSION_1_0.equals(getSmooksVersion())) {
				profileFeature = org.jboss.tools.smooks10.model.smooks.SmooksPackage.Literals.PROFILES_TYPE__PROFILE;
			}
			if (SmooksConstants.VERSION_1_1.equals(getSmooksVersion())) {
				profileFeature = SmooksPackage.Literals.PROFILES_TYPE__PROFILE;
			}

			NewOrModifySmooksElementDialog dialog = new NewOrModifySmooksElementDialog(getEditorSite().getShell(),
					profileFeature, profile, parent, getManagedForm().getToolkit(), smooksModelProvider,
					SmooksConfigurationOverviewPage.this, true);
			dialog.open();
		}
	}

	protected void updateProfilesButtons() {
		if (getSmooksVersion() == null) {
			profilePropertiesButton.setEnabled(false);
			newProfileButton.setEnabled(false);
			removeProfileButton.setEnabled(false);
			upProfileButton.setEnabled(false);
			downProfileButton.setEnabled(false);
			return;
		}
		profilePropertiesButton.setEnabled(true);
		removeProfileButton.setEnabled(true);
		IStructuredSelection selection = (IStructuredSelection) profileViewer.getSelection();
		if (selection == null) {
			profilePropertiesButton.setEnabled(false);
			removeProfileButton.setEnabled(false);
			upProfileButton.setEnabled(false);
			downProfileButton.setEnabled(false);
		} else {
			if (selection.getFirstElement() == null) {
				profilePropertiesButton.setEnabled(false);
				removeProfileButton.setEnabled(false);
				upProfileButton.setEnabled(false);
				downProfileButton.setEnabled(false);
				return;
			}

			Object obj = selection.getFirstElement();
			if (obj instanceof EObject) {
				EObject profilesType = getProfilesType();
				if (profilesType == null)
					return;
				EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
				EObject parent = v.eContainer();
				int index = parent.eContents().indexOf(v);
				Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
						index - 1);
				upProfileButton.setEnabled(command.canExecute());

				Command command1 = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
						index + 1);
				downProfileButton.setEnabled(command1.canExecute());
			}

			if (selection.size() > 1) {
				profilePropertiesButton.setEnabled(false);
				removeProfileButton.setEnabled(false);
			}
		}

	}

	protected void hookProfilesButtons() {
		newProfileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSmooksVersion() == null) {
					return;
				}
				EObject model = null;
				if (SmooksConstants.VERSION_1_0.equals(getSmooksVersion())) {
					model = org.jboss.tools.smooks10.model.smooks.SmooksFactory.eINSTANCE.createProfileType();
				}
				if (SmooksConstants.VERSION_1_1.equals(getSmooksVersion())) {
					model = SmooksFactory.eINSTANCE.createProfileType();
				}
				EObject parent = getProfilesType();
				boolean newParent = false;
				if (parent == null) {
					newParent = true;
					if (SmooksConstants.VERSION_1_0.equals(getSmooksVersion())) {
						parent = org.jboss.tools.smooks10.model.smooks.SmooksFactory.eINSTANCE.createProfilesType();
					}
					if (SmooksConstants.VERSION_1_1.equals(getSmooksVersion())) {
						parent = SmooksFactory.eINSTANCE.createProfilesType();
					}
				}
				EStructuralFeature profileFeature = null;
				if (SmooksConstants.VERSION_1_0.equals(getSmooksVersion())) {
					profileFeature = org.jboss.tools.smooks10.model.smooks.SmooksPackage.Literals.PROFILES_TYPE__PROFILE;
				}
				if (SmooksConstants.VERSION_1_1.equals(getSmooksVersion())) {
					profileFeature = SmooksPackage.Literals.PROFILES_TYPE__PROFILE;
				}
				NewOrModifySmooksElementDialog dialog = new NewOrModifySmooksElementDialog(getEditorSite().getShell(),
						profileFeature, model, parent, getManagedForm().getToolkit(), smooksModelProvider,
						SmooksConfigurationOverviewPage.this, false);

				EStructuralFeature profilesFeature = null;
				if (SmooksConstants.VERSION_1_0.equals(getSmooksVersion())) {
					profilesFeature = org.jboss.tools.smooks10.model.smooks.SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PROFILES;
				}
				if (SmooksConstants.VERSION_1_1.equals(getSmooksVersion())) {
					profilesFeature = SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PROFILES;
				}
				if (dialog.open() == Dialog.OK && newParent) {
					EObject resource = getSmooksResourceList();
					if (resource == null)
						return;
					Command command = SetCommand.create(smooksModelProvider.getEditingDomain(), resource,
							profilesFeature, parent);
					if (command.canExecute()) {
						smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
						profileViewer.setInput(parent);
					}
				}
				super.widgetSelected(e);
			}

		});
		removeProfileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) profileViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof EObject) {
					EObject profile = (EObject) obj;
					EObject parent = getProfilesType();
					if (parent == null)
						return;
					CompoundCommand compoundCommand = new CompoundCommand();
					Command command = RemoveCommand.create(smooksModelProvider.getEditingDomain(), profile);
					compoundCommand.append(command);
					if (parent.eContents().size() == 1) {
						// remove parent;
						Command command1 = RemoveCommand.create(smooksModelProvider.getEditingDomain(), parent);
						compoundCommand.append(command1);
					}
					smooksModelProvider.getEditingDomain().getCommandStack().execute(compoundCommand);
				}
			}

		});
		upProfileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) profileViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof EObject) {
					EObject profilesType = getProfilesType();
					if (profilesType == null)
						return;
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
							index - 1);
					smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
				}
			}

		});
		downProfileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) profileViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof EObject) {
					EObject profilesType = getProfilesType();
					if (profilesType == null)
						return;
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
							index + 1);
					smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
				}
			}

		});
		profilePropertiesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openProfilePropertiesModifyDialog();
				super.widgetSelected(e);
			}

		});

	}

	private EObject getProfilesType() {
		if (smooksModelProvider != null) {
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				EObject m = ((DocumentRoot) smooksModel).getSmooksResourceList().getProfiles();
				return m;
			}
			if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
				EObject m = ((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel).getSmooksResourceList()
						.getProfiles();
				return m;
			}
		}
		return null;
	}

	private ConditionsType getConditionsType() {
		if (smooksModelProvider != null) {
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				EObject m = ((DocumentRoot) smooksModel).getSmooksResourceList().getConditions();
				return (ConditionsType) m;
			}
		}
		return null;
	}

	private void createConditionsSection(Composite conditionComposite, FormToolkit toolkit) {
		if (smooksModelProvider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
					.getEditingDomain();
			ConditionsType conditions = getConditionsType();
			// if (m == null)
			// return;

			conditionViewer = new TableViewer(conditionComposite);
			GridData gd = new GridData(GridData.FILL_BOTH);
			conditionViewer.getControl().setLayoutData(gd);
			toolkit.paintBordersFor(conditionComposite);
			Composite buttonArea = toolkit.createComposite(conditionComposite);
			gd = new GridData(GridData.FILL_VERTICAL);
			gd.widthHint = 30;
			GridLayout bgl = new GridLayout();
			buttonArea.setLayout(bgl);

			newConditionButton = toolkit.createButton(buttonArea, "New", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			newConditionButton.setLayoutData(gd);

			removeConditionButton = toolkit.createButton(buttonArea, "Remove", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			removeConditionButton.setLayoutData(gd);

			upConditionButton = toolkit.createButton(buttonArea, "Up", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			upConditionButton.setLayoutData(gd);

			downConditionButton = toolkit.createButton(buttonArea, "Down", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			downConditionButton.setLayoutData(gd);

			conditionPropertiesButton = toolkit.createButton(buttonArea, "Properties..", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			conditionPropertiesButton.setLayoutData(gd);

			conditionViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()) {

				@Override
				public boolean hasChildren(Object object) {
					return false;
				}

			});

			conditionViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
					.getAdapterFactory()) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
				 * # getText(java.lang.Object)
				 */
				@Override
				public String getText(Object object) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (obj instanceof AbstractAnyType) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));

			if (conditions != null) {
				conditionViewer.setInput(conditions);
			}

			conditionViewer.addDoubleClickListener(new IDoubleClickListener() {

				public void doubleClick(DoubleClickEvent event) {
					openConditionPropertiesModifyDialog();
				}
			});

			conditionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					updateConditionsButtons();
				}
			});

			hookConditionsButtons();
			updateConditionsButtons();
		}
	}

	protected void hookConditionsButtons() {
		newConditionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSmooksVersion() == null || getSmooksVersion().equals(SmooksConstants.VERSION_1_0)) {
					return;
				}
				ConditionType condition = SmooksFactory.eINSTANCE.createConditionType();
				ConditionsType parent = getConditionsType();
				boolean newParent = false;
				if (parent == null) {
					newParent = true;
					parent = SmooksFactory.eINSTANCE.createConditionsType();

				}
				NewOrModifySmooksElementDialog dialog = new NewOrModifySmooksElementDialog(getEditorSite().getShell(),
						SmooksPackage.Literals.CONDITIONS_TYPE__CONDITION, condition, parent, getManagedForm()
								.getToolkit(), smooksModelProvider, SmooksConfigurationOverviewPage.this, false);
				if (dialog.open() == Dialog.OK && newParent) {
					EObject resource = getSmooksResourceList();
					if (resource == null)
						return;
					Command command = SetCommand.create(smooksModelProvider.getEditingDomain(), resource,
							SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__CONDITIONS, parent);
					if (command.canExecute()) {
						smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
						conditionViewer.setInput(parent);
					}
				}
				super.widgetSelected(e);
			}

		});
		removeConditionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) conditionViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof ConditionType) {
					ConditionType condition = (ConditionType) obj;
					ConditionsType parent = getConditionsType();
					if (parent == null)
						return;
					CompoundCommand compoundCommand = new CompoundCommand();
					Command command = RemoveCommand.create(smooksModelProvider.getEditingDomain(), condition);
					compoundCommand.append(command);
					if (parent.getCondition().size() == 1) {
						// remove parent;
						Command command1 = RemoveCommand.create(smooksModelProvider.getEditingDomain(), parent);
						compoundCommand.append(command1);
					}
					smooksModelProvider.getEditingDomain().getCommandStack().execute(compoundCommand);
				}
			}

		});
		upConditionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) conditionViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof ConditionType) {
					ConditionsType conditionsType = getConditionsType();
					if (conditionsType == null)
						return;
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
							index - 1);
					smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
				}
			}

		});
		downConditionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) conditionViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof ConditionType) {
					ConditionsType conditionsType = getConditionsType();
					if (conditionsType == null)
						return;
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
							index + 1);
					smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
				}
			}

		});
		conditionPropertiesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openConditionPropertiesModifyDialog();
				super.widgetSelected(e);
			}

		});

	}

	protected void updateConditionsButtons() {
		if (getSmooksVersion() == null || getSmooksVersion().equals(SmooksConstants.VERSION_1_0)) {
			conditionPropertiesButton.setEnabled(false);
			newConditionButton.setEnabled(false);
			removeConditionButton.setEnabled(false);
			upConditionButton.setEnabled(false);
			downConditionButton.setEnabled(false);
			return;
		}
		conditionPropertiesButton.setEnabled(true);
		removeConditionButton.setEnabled(true);
		IStructuredSelection selection = (IStructuredSelection) conditionViewer.getSelection();
		if (selection == null) {
			conditionPropertiesButton.setEnabled(false);
			removeConditionButton.setEnabled(false);
			upConditionButton.setEnabled(false);
			downConditionButton.setEnabled(false);
		} else {
			if (selection.getFirstElement() == null) {
				conditionPropertiesButton.setEnabled(false);
				removeConditionButton.setEnabled(false);
				upConditionButton.setEnabled(false);
				downConditionButton.setEnabled(false);
				return;
			}

			Object obj = selection.getFirstElement();
			if (obj instanceof ConditionType) {
				ConditionsType conditionsType = getConditionsType();
				if (conditionsType == null)
					return;
				EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
				EObject parent = v.eContainer();
				int index = parent.eContents().indexOf(v);
				Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
						index - 1);
				upConditionButton.setEnabled(command.canExecute());

				Command command1 = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
						index + 1);
				downConditionButton.setEnabled(command1.canExecute());
			}

			if (selection.size() > 1) {
				conditionPropertiesButton.setEnabled(false);
				removeConditionButton.setEnabled(false);
			}
		}
	}

	protected void openConditionPropertiesModifyDialog() {
		IStructuredSelection selection = (IStructuredSelection) conditionViewer.getSelection();
		if (selection == null)
			return;
		Object obj = selection.getFirstElement();
		if (obj instanceof ConditionType) {
			ConditionType condition = (ConditionType) obj;
			ConditionsType parent = getConditionsType();
			NewOrModifySmooksElementDialog dialog = new NewOrModifySmooksElementDialog(getEditorSite().getShell(),
					SmooksPackage.Literals.CONDITIONS_TYPE__CONDITION, condition, parent,
					getManagedForm().getToolkit(), smooksModelProvider, SmooksConfigurationOverviewPage.this, true);
			dialog.open();
		}
	}

	private ParamsType getParamsType() {
		if (smooksModelProvider != null) {
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				EObject m = ((DocumentRoot) smooksModel).getSmooksResourceList().getParams();
				return (ParamsType) m;
			}
		}
		return null;
	}

	private void createGlobalParamterSection(Composite globalParamComposite, FormToolkit toolkit) {
		// ModelPanelCreator creator = new ModelPanelCreator();
		if (smooksModelProvider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
					.getEditingDomain();
			ParamsType m = getParamsType();
			// if (m == null)
			// return;

			paramViewer = new TableViewer(globalParamComposite);
			GridData gd = new GridData(GridData.FILL_BOTH);
			paramViewer.getControl().setLayoutData(gd);
			toolkit.paintBordersFor(globalParamComposite);
			Composite buttonArea = toolkit.createComposite(globalParamComposite);
			gd = new GridData(GridData.FILL_VERTICAL);
			gd.widthHint = 30;
			GridLayout bgl = new GridLayout();
			buttonArea.setLayout(bgl);

			newParamButton = toolkit.createButton(buttonArea, "New", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			newParamButton.setLayoutData(gd);

			removeParamButton = toolkit.createButton(buttonArea, "Remove", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			removeParamButton.setLayoutData(gd);

			upParamButton = toolkit.createButton(buttonArea, "Up", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			upParamButton.setLayoutData(gd);

			downParamButton = toolkit.createButton(buttonArea, "Down", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			downParamButton.setLayoutData(gd);

			paramPropertiesButton = toolkit.createButton(buttonArea, "Properties..", SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			paramPropertiesButton.setLayoutData(gd);

			paramViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()) {

				@Override
				public boolean hasChildren(Object object) {
					return false;
				}

			});

			paramViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
					.getAdapterFactory()) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
				 * # getText(java.lang.Object)
				 */
				@Override
				public String getText(Object object) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (obj instanceof AbstractAnyType) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));
			if (m != null) {
				paramViewer.setInput(m);
			}

			paramViewer.addDoubleClickListener(new IDoubleClickListener() {

				public void doubleClick(DoubleClickEvent event) {
					openParamPropertiesModifyDialog();
				}
			});

			paramViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					updateParamButtons();
				}
			});

			hookGlobalParamterButtons();
			updateParamButtons();
		}

	}

	protected void updateParamButtons() {
		if (getSmooksVersion() == null || getSmooksVersion().equals(SmooksConstants.VERSION_1_0)) {
			paramPropertiesButton.setEnabled(false);
			newParamButton.setEnabled(false);
			removeParamButton.setEnabled(false);
			upParamButton.setEnabled(false);
			downParamButton.setEnabled(false);
			return;
		}
		paramPropertiesButton.setEnabled(true);
		removeParamButton.setEnabled(true);
		IStructuredSelection selection = (IStructuredSelection) paramViewer.getSelection();
		if (selection == null) {
			paramPropertiesButton.setEnabled(false);
			removeParamButton.setEnabled(false);
			upParamButton.setEnabled(false);
			downParamButton.setEnabled(false);
		} else {
			if (selection.getFirstElement() == null) {
				paramPropertiesButton.setEnabled(false);
				removeParamButton.setEnabled(false);
				upParamButton.setEnabled(false);
				downParamButton.setEnabled(false);
				return;
			}

			Object obj = selection.getFirstElement();
			if (obj instanceof ParamType) {
				ParamsType paramsType = getParamsType();
				if (paramsType == null)
					return;
				EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
				EObject parent = v.eContainer();
				int index = parent.eContents().indexOf(v);
				Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
						index - 1);
				upParamButton.setEnabled(command.canExecute());

				Command command1 = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
						index + 1);
				downParamButton.setEnabled(command1.canExecute());
			}

			if (selection.size() > 1) {
				paramPropertiesButton.setEnabled(false);
				removeParamButton.setEnabled(false);
			}
		}

	}

	private String getSmooksVersion() {
		if (smooksModelProvider != null) {
			SmooksGraphicsExtType ext = smooksModelProvider.getSmooksGraphicsExt();
			if (ext != null) {
				return ext.getPlatformVersion();
			}
		}
		return null;
	}

	private void hookGlobalParamterButtons() {
		newParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSmooksVersion() == null || getSmooksVersion().equals(SmooksConstants.VERSION_1_0)) {
					return;
				}
				ParamType param = SmooksFactory.eINSTANCE.createParamType();
				ParamsType parent = getParamsType();
				boolean newParent = false;
				if (parent == null) {
					newParent = true;
					parent = SmooksFactory.eINSTANCE.createParamsType();
					EObject resource = getSmooksResourceList();
					if (resource == null)
						return;
					Command command = SetCommand.create(smooksModelProvider.getEditingDomain(), resource,
							SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PARAMS, parent);
					if (command.canExecute()) {
						((SmooksResourceListType) resource).setParams((ParamsType) parent);
						paramViewer.setInput(parent);
					}
				}
				NewOrModifySmooksElementDialog dialog = new NewOrModifySmooksElementDialog(getEditorSite().getShell(),
						SmooksPackage.Literals.PARAMS_TYPE__PARAM, param, parent, getManagedForm().getToolkit(),
						smooksModelProvider, SmooksConfigurationOverviewPage.this, false);
				if (dialog.open() == Dialog.CANCEL && newParent) {
					EObject resource = getSmooksResourceList();
					((SmooksResourceListType) resource).setParams(null);
					paramViewer.setInput(null);
				}
				super.widgetSelected(e);
			}

		});
		removeParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) paramViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof ParamType) {
					ParamType param = (ParamType) obj;
					ParamsType parent = getParamsType();
					if (parent == null)
						return;
					CompoundCommand compoundCommand = new CompoundCommand();
					Command command = RemoveCommand.create(smooksModelProvider.getEditingDomain(), param);
					compoundCommand.append(command);
					if (parent.getParam().size() == 1) {
						// remove parent;
						Command command1 = RemoveCommand.create(smooksModelProvider.getEditingDomain(), parent);
						compoundCommand.append(command1);
					}
					smooksModelProvider.getEditingDomain().getCommandStack().execute(compoundCommand);
				}
			}

		});
		upParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) paramViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof ParamType) {
					ParamsType paramsType = getParamsType();
					if (paramsType == null)
						return;
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
							index - 1);
					smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
				}
			}

		});
		downParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) paramViewer.getSelection();
				if (selection == null)
					return;
				Object obj = selection.getFirstElement();
				if (obj instanceof ParamType) {
					ParamsType paramsType = getParamsType();
					if (paramsType == null)
						return;
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(smooksModelProvider.getEditingDomain(), parent, null, obj,
							index + 1);
					smooksModelProvider.getEditingDomain().getCommandStack().execute(command);
				}
			}

		});
		paramPropertiesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openParamPropertiesModifyDialog();
				super.widgetSelected(e);
			}

		});
	}

	protected void openParamPropertiesModifyDialog() {
		IStructuredSelection selection = (IStructuredSelection) paramViewer.getSelection();
		if (selection == null)
			return;
		Object obj = selection.getFirstElement();
		if (obj instanceof ParamType) {
			ParamType param = (ParamType) obj;
			ParamsType parent = getParamsType();
			NewOrModifySmooksElementDialog dialog = new NewOrModifySmooksElementDialog(getEditorSite().getShell(),
					SmooksPackage.Literals.PARAMS_TYPE__PARAM, param, parent, getManagedForm().getToolkit(),
					smooksModelProvider, SmooksConfigurationOverviewPage.this, true);
			dialog.open();
		}
	}

	private void createNavigatorSection(Composite mainNavigatorComposite, FormToolkit toolkit, String title,
			String navigatorFilePath) {
		Section navigator = toolkit.createSection(mainNavigatorComposite, Section.TITLE_BAR);
		navigator.setText(title);
		navigator.setLayout(new FillLayout());
		Composite navigatorComposite = toolkit.createComposite(navigator);
		navigator.setClient(navigatorComposite);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		navigator.setLayoutData(gd);

		FormText formText = toolkit.createFormText(navigatorComposite, true);
		StringBuffer buf = new StringBuffer();
		InputStream inputStream = this.getClass().getResourceAsStream(navigatorFilePath);
		BufferedReader reader = null;
		if (inputStream != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String line = reader.readLine();
				while (line != null) {
					buf.append(line);
					line = reader.readLine();
				}
			} catch (IOException e) {

			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (Throwable t) {

				}
			}
		}
		formText.setWhitespaceNormalized(true);
		String content = buf.toString();
		if (content != null) {
			try {
				formText.setText(content, true, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		formText.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				Object href = e.getHref();
				if (href == null)
					return;
				activeNavigatorLink(href.toString());
			}
		});
		navigatorComposite.setLayout(new GridLayout());
		gd = new GridData(GridData.FILL_BOTH);
		formText.setLayoutData(gd);

		toolkit.createLabel(navigatorComposite, "");
	}

	private void createSmooksEditorNavigator(Composite mainComposite, FormToolkit toolkit) {
		Composite mainNavigatorComposite = toolkit.createComposite(mainComposite);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 5;
		mainNavigatorComposite.setLayoutData(gd);

		GridLayout gl = new GridLayout();
		// gl.numColumns = 2;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		mainNavigatorComposite.setLayout(gl);

		createNavigatorSection(mainNavigatorComposite, toolkit, "Configuring Smooks Input",
				"/org/jboss/tools/smooks/configuration/navigator/DefaultSetting.htm");
		createNavigatorSection(mainNavigatorComposite, toolkit, "Configuring Message Filter(s)",
				"/org/jboss/tools/smooks/configuration/navigator/MessageFilterNavigator.htm");
	}

	protected void activeNavigatorLink(String href) {
		if (href == null)
			return;
		if (href.equals("reader_page")) {
			this.getEditor().setActivePage("reader_page");
		}
		if (href.equals("message_filter_page")) {
			this.getEditor().setActivePage("message_filter_page");
		}
		if (href.equals("source_page")) {
			this.getEditor().setActiveEditor(((AbstractSmooksFormEditor) getEditor()).getTextEditor());
		}
		if (href.equals("overview_default_setting")) {
			generalSettingSection.setFocus();
		}
		if (href.equals("overview_global_param")) {
			globalParamSection.setFocus();
			globalParamSection.setExpanded(true);
		}
		if (href.equals("overview_condition")) {
			conditionSection.setFocus();
			conditionSection.setExpanded(true);
		}
		if (href.equals("overview_profile")) {
			profilesSection.setFocus();
			profilesSection.setExpanded(true);
		}
		if (href.equals("selector_dialog")) {
			SelectorCreationDialog dialog = new SelectorCreationDialog(getEditorSite().getShell(),
					this.smooksModelProvider.getSmooksGraphicsExt(), this.getEditor());
			try {
				dialog.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private EObject getSmooksResourceList() {
		if (smooksModelProvider != null) {
			EObject m = null;
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
				m = ((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel).getSmooksResourceList();
			}
			if (smooksModel instanceof DocumentRoot) {
				m = ((DocumentRoot) smooksModel).getSmooksResourceList();
			}
			return m;
		}
		return null;
	}

	private void createDefaultSection(Composite parent, FormToolkit toolkit) {
		ModelPanelCreator defaultSettingPanelCreator = getDefaultSettingPanelCreator();
		EObject model = getSmooksResourceList();
		if (model != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
					.getEditingDomain();
			IItemPropertySource itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					model, IItemPropertySource.class);
			if (model != null) {
				defaultSettingPanelCreator.createModelPanel(model, toolkit, parent, itemPropertySource,
						smooksModelProvider, getEditor());
			}
		}

	}

	public ModelPanelCreator getDefaultSettingPanelCreator() {
		if (defaultSettingPanelCreator == null) {
			defaultSettingPanelCreator = new ModelPanelCreator();
		}
		return defaultSettingPanelCreator;
	}

	public void inputTypeChanged(SmooksGraphicsExtType extType) {

	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		ModelPanelCreator creator = getDefaultSettingPanelCreator();
		creator.markPropertyUI(diagnosticResult, getSmooksResourceList());
	}

	public void validateStart() {
	}

	public void sourceChange(Object model) {
		disposeDefaultSettingCompositeControls();
		createDefaultSection(defaultSettingComposite, this.getManagedForm().getToolkit());
		defaultSettingComposite.getParent().layout();

		paramViewer.setInput(getParamsType());
		conditionViewer.setInput(getConditionsType());
		profileViewer.setInput(getProfilesType());
	}

	protected void disposeCompositeControls(Composite composite, Control[] ignoreControl) {
		if (composite != null) {
			Control[] children = composite.getChildren();
			for (int i = 0; i < children.length; i++) {
				Control child = children[i];
				if (ignoreControl != null) {
					for (int j = 0; j < ignoreControl.length; j++) {
						if (child == ignoreControl[j]) {
							continue;
						}
					}
				}
				child.dispose();
				child = null;
			}
		}
	}

	private void disposeDefaultSettingCompositeControls() {
		disposeCompositeControls(defaultSettingComposite, null);
	}

	public void graphChanged(SmooksGraphicsExtType extType) {

	}

	public void graphPropertyChange(EStructuralFeature featre, Object value) {

	}

}

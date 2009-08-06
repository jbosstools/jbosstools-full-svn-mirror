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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
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

	public SmooksConfigurationOverviewPage(FormEditor editor, String id, String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
		((SmooksMultiFormEditor) editor).getSmooksGraphicsExt().addSmooksGraphChangeListener(this);

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
		Composite mainComposite = toolkit.createComposite(form.getBody());

		GridData gd = new GridData(GridData.FILL_BOTH);
		mainComposite.setLayoutData(gd);

		GridLayout mgl = new GridLayout();
		mgl.numColumns = 2;
		mgl.marginHeight = 13;
		mainComposite.setLayout(mgl);

		Section generalSettingSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR);
		generalSettingSection.setLayout(new FillLayout());
		generalSettingSection.setText("Default Setting");
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

		createSmooksEditorNavigator(mainComposite, toolkit);

		Section globalParamSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR);
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

		Section conditionSection = toolkit.createSection(mainComposite, Section.DESCRIPTION | Section.TITLE_BAR
				| Section.TWISTIE);
		conditionSection.setText("Conditions");
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
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				return SmooksConstants.VERSION_1_1;
			}
			if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
				return SmooksConstants.VERSION_1_0;
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

	private void createSmooksEditorNavigator(Composite mainComposite, FormToolkit toolkit) {
		Composite mainNavigatorComposite = toolkit.createComposite(mainComposite);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 3;
		mainNavigatorComposite.setLayoutData(gd);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		mainNavigatorComposite.setLayout(gl);

		Section navigator = toolkit.createSection(mainNavigatorComposite, Section.DESCRIPTION | Section.TITLE_BAR);
		navigator.setText("Smooks Editor Navigator");
		navigator.setLayout(new FillLayout());
		Composite navigatorComposite = toolkit.createComposite(navigator);
		navigator.setClient(navigatorComposite);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		navigator.setLayoutData(gd);

		FormText formText = toolkit.createFormText(navigatorComposite, true);
		StringBuffer buf = new StringBuffer();
		InputStream inputStream = this.getClass().getResourceAsStream(
				"/org/jboss/tools/smooks/configuration/navigator/DefaultSetting.htm");
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
				if(href == null) return;
				activeNavigatorLink(href.toString());
			}
		});
		navigatorComposite.setLayout(new GridLayout());
		gd = new GridData(GridData.FILL_BOTH);
		formText.setLayoutData(gd);
	}
	
	protected void activeNavigatorLink(String href){
		if(href == null)return;
		if(href.equals("reader_page")){
			this.getEditor().setActivePage("Reader");
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
		ModelPanelCreator creator = new ModelPanelCreator();
		EObject model = getSmooksResourceList();
		AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
				.getEditingDomain();
		IItemPropertySource itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(model,
				IItemPropertySource.class);
		if (model != null) {
			creator.createModelPanel(model, toolkit, parent, itemPropertySource, smooksModelProvider, getEditor());
		}

	}

	public void saveComplete(SmooksGraphicsExtType extType) {

	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {

	}

	public void validateStart() {

	}

	public void sourceChange(Object model) {
		disposeDefaultSettingCompositeControls();
		createDefaultSection(defaultSettingComposite, this.getManagedForm().getToolkit());
		defaultSettingComposite.getParent().layout();

		paramViewer.setInput(getParamsType());
		conditionViewer.setInput(getConditionsType());
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

}

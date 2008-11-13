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
package org.jboss.tools.smooks.ui.editors;

import java.util.List;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.AnalyzeResult;
import org.jboss.tools.smooks.ui.IAnalyzeListener;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng Date : 2008-9-9
 */
public class SmooksNormalContentEditFormPage extends FormPage implements
		IAnalyzeListener {

	protected NormalSmooksModelPackage modelPackage = null;

	protected SmooksResourceConfigFormBlock resourceBlock = null;

	protected ResourceConfigType transformType = null;

	private Button saxButton;

	private Button domButton;

	private Button saxdomButton;

	private EditingDomain domain;

	private List hidenResourceConfigs;

	private boolean disableGUI = false;

	public boolean isDisableGUI() {
		return disableGUI;
	}

	public void setDisableGUI(boolean disableGUI) {
		this.disableGUI = disableGUI;
		setGUIStates();
	}

	private Section parseTypeSection;

	public SmooksNormalContentEditFormPage(FormEditor editor, String id,
			String title, NormalSmooksModelPackage modelPacakge) {
		super(editor, id, title);
		domain = ((SmooksFormEditor) editor).getEditingDomain();
		this.createResourceConfigFormBlock();
		this.setModelPackage(modelPacakge);
	}

	public SmooksNormalContentEditFormPage(String id, String title,
			NormalSmooksModelPackage modelPacakge) {
		super(id, title);
		this.createResourceConfigFormBlock();
		this.setModelPackage(modelPacakge);
	}

	protected void createResourceConfigFormBlock() {
		resourceBlock = new SmooksResourceConfigFormBlock();
		resourceBlock.setDomain(getEditingDomain());
		resourceBlock.setParentEditor((SmooksFormEditor) this.getEditor());
	}

	protected EditingDomain getEditingDomain() {
		return ((SmooksFormEditor) getEditor()).getEditingDomain();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit tool = managedForm.getToolkit();
		tool.decorateFormHeading(form.getForm());
		GridLayout gridLayout = UIUtils.createGeneralFormEditorLayout(1);
		resourceBlock.createContent(managedForm);
		Composite rootMainControl = form.getBody();
		form.setText(Messages.getString("SmooksNormalContentEditFormPage.ConfigurationPageText")); //$NON-NLS-1$
		createSmooksTypeGUI(rootMainControl, tool);
		form.getBody().setLayout(gridLayout);
		form.pack();
		this.initTransformTypeResourceConfig();
		resourceBlock.initViewers(transformType);

		setGUIStates();
	}

	public void setGUIStates() {
		if (resourceBlock != null) {
			resourceBlock.setSectionStates(!disableGUI);
		}

		if (this.parseTypeSection != null && !parseTypeSection.isDisposed()) {
			parseTypeSection.setEnabled(!disableGUI);
		}
	}

	private ResourceConfigType createTransformType() {
		ResourceConfigType transformType = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		AddCommand.create(
				domain,
				modelPackage.getSmooksResourceList(),
				SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
				transformType).execute();
		MoveCommand.create(domain, modelPackage.getSmooksResourceList(),
				SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
				transformType, 0);
		transformType.setSelector(SmooksModelConstants.GLOBAL_PARAMETERS);
		ParamType typeParam = SmooksFactory.eINSTANCE.createParamType();
		typeParam.setName(SmooksModelConstants.STREAM_FILTER_TYPE);
		transformType.getParam().add(typeParam);

		return transformType;
	}

	protected void initTransformTypeResourceConfig() {
		if (saxButton != null)
			saxButton.setSelection(false);
		if (domButton != null)
			domButton.setSelection(false);
		if (saxdomButton != null)
			saxdomButton.setSelection(false);
		if (this.getModelPackage() != null) {
			List list = modelPackage.getSmooksResourceList()
					.getAbstractResourceConfig();
			if (list != null && !list.isEmpty()) {
				ResourceConfigType first = (ResourceConfigType) list.get(0);
				if (SmooksModelUtils.isTransformTypeResourceConfig(first)) {
					this.transformType = first;
				}
			}
			if (transformType == null) {
				transformType = createTransformType();
			}
			if (transformType != null) {
				String type = SmooksModelUtils.getTransformType(transformType);
				if (SmooksModelConstants.SAX.equals(type)) {
					if (saxButton != null)
						saxButton.setSelection(true);
				}
				if (SmooksModelConstants.DOM.equals(type)) {
					if (domButton != null)
						domButton.setSelection(true);
				}
				if ("SAX/DOM".equals(type)) { //$NON-NLS-1$
					if (saxdomButton != null)
						saxdomButton.setSelection(true);
				}
			}
		}
	}

	protected void createSmooksTypeGUI(Composite mainComposite, FormToolkit tool) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		parseTypeSection = tool.createSection(mainComposite, Section.TITLE_BAR
				| Section.DESCRIPTION | Section.TWISTIE);
		parseTypeSection.setLayoutData(gd);
		Composite typeSelectComposite = tool.createComposite(parseTypeSection);
		parseTypeSection.setClient(typeSelectComposite);
		parseTypeSection.setText(Messages.getString("SmooksNormalContentEditFormPage.SmooksParseType")); //$NON-NLS-1$
		GridLayout layout1 = new GridLayout();
		typeSelectComposite.setLayout(layout1);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		typeSelectComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		typeSelectComposite.setLayout(layout);

		saxButton = createTypeSelectRadioButton(typeSelectComposite, tool,
				SmooksModelConstants.SAX);
		domButton = createTypeSelectRadioButton(typeSelectComposite, tool,
				SmooksModelConstants.DOM);
		saxdomButton = createTypeSelectRadioButton(typeSelectComposite, tool,
				SmooksModelConstants.SAX_DOM);
		hookButtons();
	}

	private void setTransformType(String type) {
		if (this.transformType != null) {
			SmooksModelUtils.setTransformType(transformType, type);
			((SmooksFormEditor) getEditor()).fireEditorDirty(true);
		}
	}

	protected void hookButtons() {
		saxButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setTransformType(SmooksModelConstants.SAX);
			}

		});

		domButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setTransformType(SmooksModelConstants.DOM);
			}

		});

		saxdomButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setTransformType(SmooksModelConstants.SAX_DOM);
			}

		});
	}

	private Button createTypeSelectRadioButton(Composite parent,
			FormToolkit tool, String labelName) {
		Button button = tool.createButton(parent, labelName, SWT.RADIO);
		return button;
	}

	/**
	 * @return the modelPackage
	 */
	public NormalSmooksModelPackage getModelPackage() {
		return modelPackage;
	}

	/**
	 * @param modelPackage
	 *            the modelPackage to set
	 */
	public void setModelPackage(NormalSmooksModelPackage modelPackage) {
		if (modelPackage == this.modelPackage)
			return;
		this.modelPackage = modelPackage;
		if (resourceBlock != null)
			this.resourceBlock.setModelPackage(this.modelPackage);
	}

	public void endAnalyze(AnalyzeResult result) {
		if (result.getError() == null) {
			disableGUI = false;
			SmooksFormEditor parentEditor = (SmooksFormEditor) getEditor();
			NormalSmooksModelPackage pa = parentEditor
					.createNewSmooksModelPackage();
			SmooksGraphicalFormPage graphicalEditor = (SmooksGraphicalFormPage) result
					.getSourceEdtior();
			MappingResourceConfigList rclist = graphicalEditor
					.getMappingResourceConfigList();
			if (rclist != null) {
				pa.setHidenSmooksElements(rclist
						.getRelationgResourceConfigList());
			}
			setModelPackage(pa);
		} else {
			setModelPackage(null);
			disableGUI = true;
		}
		initTransformTypeResourceConfig();
		if (resourceBlock != null)
			this.resourceBlock.initViewers(transformType);
		setGUIStates();
	}
}

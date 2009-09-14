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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.wizard.StructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.csv12.CSV12Reader;
import org.jboss.tools.smooks.model.csv12.Csv12Package;
import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.edi.EdiPackage;
import org.jboss.tools.smooks.model.edi12.EDI12Reader;
import org.jboss.tools.smooks.model.edi12.Edi12Package;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json12.Json12Package;
import org.jboss.tools.smooks.model.json12.Json12Reader;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.ReaderType;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class SmooksReaderFormPage extends FormPage implements ISmooksModelValidateListener, ISmooksGraphChangeListener,
		ISourceSynchronizeListener {

	private CheckboxTableViewer inputDataViewer;
	private TreeViewer inputModelViewer;
	private Combo readerCombo;

	private List<Object> readerTypeList = new ArrayList<Object>();
	private Composite readerConfigComposite;
	private ModelPanelCreator modelPanelCreator;
	protected boolean lockCheck = false;

	public SmooksReaderFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		((SmooksMultiFormEditor) editor).getSmooksGraphicsExt().addSmooksGraphChangeListener(this);
	}

	public SmooksReaderFormPage(String id, String title) {
		super(id, title);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.setText("Input");
		// // create master details UI
		// createMasterDetailBlock(managedForm);
		Composite leftComposite = toolkit.createComposite(form.getBody());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 700;
		leftComposite.setLayoutData(gd);

		Composite rightComposite = toolkit.createComposite(form.getBody());
		gd = new GridData(GridData.FILL_BOTH);
		rightComposite.setLayoutData(gd);

		GridLayout lgl = new GridLayout();
		lgl.marginWidth = 0;
		lgl.marginHeight = 0;
		leftComposite.setLayout(lgl);

		GridLayout rgl = new GridLayout();
		rgl.marginWidth = 0;
		rgl.marginHeight = 0;
		rightComposite.setLayout(rgl);

		createReaderSection(toolkit, leftComposite);
		createInputDataSection(toolkit, rightComposite);
		createReaderConfigSection(toolkit, leftComposite);
		createInputModelViewerSection(toolkit, rightComposite);

		handleReaderCombo(readerCombo);

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 13;
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 20;
		gridLayout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(gridLayout);
	}

	private void createInputModelViewerSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.verticalAlignment = GridData.BEGINNING;
		section.setLayoutData(gd);
		section.setText("Input Model View");
		section.setDescription("View the XML structure model of the input data");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainContainer = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		mainContainer.setLayout(gl);
		section.setClient(mainContainer);

		Hyperlink refreshLink = toolkit.createHyperlink(mainContainer, "Refresh Input Model Viewer", SWT.NONE);
		refreshLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub

			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub

			}

			public void linkActivated(HyperlinkEvent e) {
				refreshInputModelView();
			}
		});

		Composite viewerContainer = toolkit.createComposite(mainContainer);
		gd = new GridData(GridData.FILL_BOTH);
		viewerContainer.setLayoutData(gd);

		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		viewerContainer.setBackground(GraphicsConstants.BORDER_CORLOER);
		viewerContainer.setLayout(fillLayout);
		inputModelViewer = new TreeViewer(viewerContainer, SWT.NONE);
		inputModelViewer.setContentProvider(new CompoundStructuredDataContentProvider());
		inputModelViewer.setLabelProvider(new CompoundStructuredDataLabelProvider());
		List<Object> inputList = generateInputData();
		inputModelViewer.setInput(inputList);
		inputModelViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
			}
		});
		inputModelViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// currentSelection = ((IStructuredSelection)
				// event.getSelection()).getFirstElement();
			}
		});
		SmooksUIUtils.expandSelectorViewer(inputList, inputModelViewer);

	}

	protected List<Object> generateInputData() {
		Object obj = ((SmooksMultiFormEditor) getEditor()).getSmooksModel();
		SmooksResourceListType resourceList = null;
		if (obj instanceof DocumentRoot) {
			resourceList = ((DocumentRoot) obj).getSmooksResourceList();
		}
		return SelectorCreationDialog.generateInputData(getSmooksGraphicsExtType(), resourceList);
	}

	private void createReaderConfigSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		GridData gd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gd);
		section.setText("Input Configuration");
		section.setDescription("Configurate the input data");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		ScrolledPageBook pageBook = new ScrolledPageBook(section);
		pageBook.setBackground(toolkit.getColors().getBackground());
		section.setClient(pageBook);

		readerConfigComposite = pageBook.createPage(pageBook);
		pageBook.showPage(pageBook);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		readerConfigComposite.setLayout(gl);

		initReaderConfigSection();
	}

	private void createReaderSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		section.setLayoutData(gd);
		section.setText("Input Type");
		section.setDescription("Select the input type");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(section);
		section.setClient(mainComposite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);

		toolkit.createLabel(mainComposite, "Input Type : ");

		readerCombo = new Combo(mainComposite, SWT.NONE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		readerCombo.setLayoutData(gd);

		toolkit.createLabel(mainComposite, " ");

		bindingReaderCombo();

		initReaderCombo();

		gd = new GridData(GridData.FILL_HORIZONTAL);
		readerCombo.setLayoutData(gd);

		toolkit.paintBordersFor(mainComposite);
	}

	private void initReaderConfigSection() {
		Object reader = getCurrentReaderModel();
		String type = getSmooksGraphicsExtType().getInputType();
		if (reader instanceof EObject && type != null) {
			SmooksResourceListType list = getSmooksConfigResourceList();
			createReaderPanel((EObject) list.getAbstractReader().get(0));
		} else {
			disposeCompositeControls(readerConfigComposite, null);
		}
	}

	private void selectCorrectReaderItem(Object reader) {
		for (int i = 0; i < readerTypeList.size(); i++) {
			Object r = readerTypeList.get(i);
			if (r instanceof EObject) {
				if (r.getClass() == reader.getClass()) {
					readerCombo.select(i);
					break;
				}
			}
		}
	}

	private String getCurrentReaderType() {
		Object reader = getCurrentReaderModel();
		if (reader instanceof XMLReader) {
			return SmooksModelUtils.INPUT_TYPE_XML;
		}
		if (reader instanceof JavaReader) {
			return SmooksModelUtils.INPUT_TYPE_JAVA;
		}
		if (reader instanceof XSDReader) {
			return SmooksModelUtils.INPUT_TYPE_XSD;
		}
		if (reader instanceof EObject) {
			Object obj = ((EObject) reader);

			if (obj instanceof CsvReader || obj instanceof CSV12Reader) {
				return SmooksModelUtils.INPUT_TYPE_CSV;
			}
			if (obj instanceof EDIReader || obj instanceof EDI12Reader) {
				return SmooksModelUtils.INPUT_TYPE_EDI_1_1;
			}
			if (obj instanceof JsonReader || obj instanceof Json12Reader) {
				return SmooksModelUtils.INPUT_TYPE_JSON_1_1;
			}
			if (obj instanceof ReaderType) {
				return SmooksModelUtils.INPUT_TYPE_CUSTOME;
			}
		}
		return null;
	}

	private void initReaderCombo() {
		if (readerCombo == null)
			return;
		SmooksResourceListType rlist = getSmooksConfigResourceList();
		if (rlist == null) {
			readerCombo.select(-1);
			return;
		}
		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
		String inputType = ext.getInputType();

		if (inputType == null) {
			// for the first time to open the file.
			if (rlist.getAbstractReader().isEmpty()) {
				readerCombo.select(0);
				return;
			} else {
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_XML.equals(inputType)) {
			readerCombo.select(1);
		}
		if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(inputType)) {
			readerCombo.select(2);
		}
		if (SmooksModelUtils.INPUT_TYPE_XSD.equals(inputType)) {
			readerCombo.select(3);
		}

		if (SmooksModelUtils.INPUT_TYPE_CSV.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (CsvReader.class.isInstance(reader) || CSV12Reader.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_EDI_1_1.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (EDIReader.class.isInstance(reader) || EDI12Reader.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_JSON_1_1.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (JsonReader.class.isInstance(reader) || Json12Reader.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_CUSTOME.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (ReaderType.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		return;
	}

	private void handleReaderCombo(final Combo combo) {
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				Object newreader = getCurrentReaderModel();
				if (newreader == null)
					return;
				String type = getCurrentReaderType();
				if (type == null) {
					getSmooksGraphicsExtType().eUnset(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE);
				} else {
					getSmooksGraphicsExtType().setInputType(type);
				}
				readerChanged(newreader);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Object createReaderEntry(Object reader, boolean clone) {
		if (clone) {
			reader = EcoreUtil.copy((EObject) reader);
		}
		if (reader instanceof CsvReader) {
			return FeatureMapUtil.createEntry(CsvPackage.Literals.CSV_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof CSV12Reader) {
			return FeatureMapUtil.createEntry(Csv12Package.Literals.CSV12_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof EDIReader) {
			return FeatureMapUtil.createEntry(EdiPackage.Literals.EDI_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof EDI12Reader) {
			return FeatureMapUtil.createEntry(Edi12Package.Literals.EDI12_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof JsonReader) {
			return FeatureMapUtil.createEntry(JsonPackage.Literals.JSON_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof Json12Reader) {
			return FeatureMapUtil.createEntry(Json12Package.Literals.JSON12_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof ReaderType) {
			return FeatureMapUtil.createEntry(SmooksPackage.Literals.DOCUMENT_ROOT__READER, reader);
		}
		return null;
	}

	private Command createRemoveReaderCommand() {
		SmooksResourceListType rlist = getSmooksConfigResourceList();
		List<AbstractReader> readerList = rlist.getAbstractReader();
		CompoundCommand compoundCommand = new CompoundCommand();
		for (Iterator<?> iterator = readerList.iterator(); iterator.hasNext();) {
			AbstractReader abstractReader = (AbstractReader) iterator.next();
			Object readerEntry = createReaderEntry(abstractReader, false);
			if (readerEntry == null)
				continue;
			Command removeCommand = RemoveCommand.create(getEditingDomain(), rlist,
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, readerEntry);
			if (removeCommand.canExecute()) {
				compoundCommand.append(removeCommand);
			}
		}
		if (compoundCommand.isEmpty()) {
			return null;
		}
		return compoundCommand;
	}

	private void readerChanged(Object reader) {
		
		CompoundCommand compoundCommand = new CompoundCommand();
		Command removeCommand = createRemoveReaderCommand();
		if (removeCommand != null && removeCommand.canExecute()) {
			compoundCommand.append(removeCommand);
		}
		if (readerConfigComposite != null) {
			disposeCompositeControls(readerConfigComposite, null);
		}
		boolean hasRun = false;
		if (reader instanceof EObject) {
			Object obj = ((EObject) reader);
			obj = AdapterFactoryEditingDomain.unwrap(obj);
			Command command = AddCommand.create(getEditingDomain(), getSmooksConfigResourceList(),
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, createReaderEntry(obj,
							false));
			if (command.canExecute()) {
				compoundCommand.append(command);
			}
			if (!compoundCommand.isEmpty()) {
				getEditingDomain().getCommandStack().execute(compoundCommand);
				hasRun = true;
				createReaderPanel((EObject) obj);
			}
		}

		if (!hasRun) {
			if (!compoundCommand.isEmpty()) {
				getEditingDomain().getCommandStack().execute(compoundCommand);
			}
		}

		deactiveAllInputFile();

		if (inputDataViewer != null) {
			inputDataViewer.refresh();
		}
		refreshInputModelView();
	}

	private void deactiveAllInputFile() {
		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
		List<InputType> inputList = ext.getInput();
		for (Iterator<?> iterator = inputList.iterator(); iterator.hasNext();) {
			InputType inputType = (InputType) iterator.next();
			setInputDataActiveStatus(false, inputType);
		}
	}

	private void createReaderPanel(EObject reader) {
		disposeCompositeControls(readerConfigComposite, null);
		try {
			ModelPanelCreator modelPanelCreator = getModelPanelCreator();
			IItemPropertySource ps = (IItemPropertySource) getEditingDomain().getAdapterFactory().adapt(reader,
					IItemPropertySource.class);
			modelPanelCreator.createModelPanel(reader, getManagedForm().getToolkit(), readerConfigComposite, ps,
					(ISmooksModelProvider) getEditor(), getEditor());
			readerConfigComposite.getParent().layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ModelPanelCreator getModelPanelCreator() {
		if (modelPanelCreator == null) {
			modelPanelCreator = new ModelPanelCreator();
		}
		return modelPanelCreator;
	}

	private void bindingReaderCombo() {
		if (readerCombo == null)
			return;

		readerCombo.removeAll();
		readerTypeList.clear();

		readerCombo.add("No Input");
		readerTypeList.add(new NullReader());
		readerCombo.add("XML");
		readerTypeList.add(new XMLReader());
		readerCombo.add("Java");
		readerTypeList.add(new JavaReader());
		readerCombo.add("XSD/WSDL");
		readerTypeList.add(new XSDReader());
		
		SmooksResourceListType resourceList = getSmooksConfigResourceList();
		
		if(resourceList == null){
			return;
		}

		AdapterFactoryEditingDomain editDomain = getEditingDomain();
		IEditingDomainItemProvider provider = (IEditingDomainItemProvider) editDomain.getAdapterFactory().adapt(
				resourceList, IEditingDomainItemProvider.class);
		Collection<?> collections = provider.getNewChildDescriptors(resourceList, editDomain, null);

		SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
		String version = extType.getPlatformVersion();
		OnlyReaderViewerFilter filter = new OnlyReaderViewerFilter();
		for (Iterator<?> iterator = collections.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof CommandParameter) {
				Object value = ((CommandParameter) object).getValue();

				value = AdapterFactoryEditingDomain.unwrap(value);

				if (filter.select(null, null, value)) {
					if (SmooksUIUtils.isUnSupportElement(version, (EObject) value)) {
						continue;
					}

					IItemLabelProvider lp = (IItemLabelProvider) editDomain.getAdapterFactory().adapt(value,
							IItemLabelProvider.class);
					String text = lp.getText(value);
					readerCombo.add(text);
					readerTypeList.add(value);
				}
			}
		}
	}

	private void setInputDataActiveStatus(boolean active, InputType inputType) {
		List<ParamType> params = inputType.getParam();
		boolean newOne = true;
		for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
				paramType.setValue(String.valueOf(active));
				newOne = false;
				break;
			}
		}
		if (newOne) {
			ParamType p = GraphFactory.eINSTANCE.createParamType();
			p.setName(SmooksModelUtils.PARAM_NAME_ACTIVED);
			p.setValue(String.valueOf(active));
			inputType.getParam().add(p);
		}
		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
		if (ext != null) {
			List<ISmooksGraphChangeListener> listeners = ((SmooksGraphicsExtType) ext).getChangeListeners();
			for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
				ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator.next();
				smooksGraphChangeListener.inputTypeChanged((SmooksGraphicsExtType) ext);
			}
		}
	}

	protected void createInputDataSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gd);
		section.setText("Input Data");
		section.setDescription("Add a file to be the input data");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(section, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);
		section.setClient(mainComposite);

		Composite tableComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.BORDER_CORLOER);
		tableComposite.setLayout(fillLayout);

		inputDataViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		// inputDataViewer.set
		inputDataViewer.setCheckStateProvider(new ICheckStateProvider() {

			public boolean isGrayed(Object element) {
				return isIncorrectInputType((InputType) element);
			}

			public boolean isChecked(Object element) {
				if (element instanceof InputType) {
					List<ParamType> params = ((InputType) element).getParam();
					for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
						ParamType paramType = (ParamType) iterator.next();
						if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
							String value = paramType.getValue();
							if (value == null)
								return false;
							value = value.trim();
							return "true".equalsIgnoreCase(value);
						}
					}
				}
				return false;
			}
		});
		inputDataViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				if (lockCheck)
					return;
				boolean checked = event.getChecked();
				InputType inputType = (InputType) event.getElement();
				if (isIncorrectInputType(inputType)) {
					lockCheck = true;
					inputDataViewer.setChecked(inputType, false);
					lockCheck = false;
					return;
				}
				List<ParamType> params = inputType.getParam();

				if (checked) {
					boolean newOne = true;
					for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
						ParamType paramType = (ParamType) iterator.next();
						if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
							paramType.setValue(String.valueOf(checked));
							newOne = false;
							break;
						}
					}
					if (newOne) {
						ParamType p = GraphFactory.eINSTANCE.createParamType();
						p.setName(SmooksModelUtils.PARAM_NAME_ACTIVED);
						p.setValue(String.valueOf(checked));
						inputType.getParam().add(p);
					}

					Object[] checkedObjects = inputDataViewer.getCheckedElements();
					for (int i = 0; i < checkedObjects.length; i++) {
						InputType type = (InputType) checkedObjects[i];
						if (type == event.getElement())
							continue;
						List<ParamType> params1 = type.getParam();
						for (Iterator<?> iterator = params1.iterator(); iterator.hasNext();) {
							ParamType paramType = (ParamType) iterator.next();
							if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
								paramType.setValue(String.valueOf(!checked));
								break;
							}
						}
						lockCheck = true;
						inputDataViewer.setChecked(type, false);
						lockCheck = false;
					}

				} else {
					for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
						ParamType paramType = (ParamType) iterator.next();
						if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
							paramType.setValue(String.valueOf(checked));
							break;
						}
					}
				}

				SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
				if (ext != null) {
					List<ISmooksGraphChangeListener> listeners = ((SmooksGraphicsExtType) ext).getChangeListeners();
					for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
						ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator
								.next();
						smooksGraphChangeListener.inputTypeChanged((SmooksGraphicsExtType) ext);
					}
				}

			}
		});
		TableColumn header = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		header.setText("Type");
		header.setWidth(100);
		TableColumn pathColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		pathColumn.setText("Path");
		pathColumn.setWidth(300);

		// TableColumn extColumn = new TableColumn(inputDataViewer.getTable(),
		// SWT.NONE);
		// extColumn.setText("Extension Paramers");
		// extColumn.setWidth(400);
		inputDataViewer.setContentProvider(new ExtentionInputContentProvider());
		inputDataViewer.setLabelProvider(new InputDataViewerLabelProvider());
		inputDataViewer.getTable().setHeaderVisible(true);
		inputDataViewer.getTable().setLinesVisible(true);
		SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
		if (extType != null) {
			inputDataViewer.setInput(extType);
		}
		Composite buttonComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);
		GridLayout l = new GridLayout();
		buttonComposite.setLayout(l);

		Button addButton = toolkit.createButton(buttonComposite, "Add", SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				showInputDataWizard();
			}

		});

		Button removeButton = toolkit.createButton(buttonComposite, "Delete", SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) inputDataViewer.getSelection();
				if (selection != null) {
					SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
					if (extType != null) {
						boolean canSave = false;
						Object[] objs = selection.toArray();
						for (int i = 0; i < objs.length; i++) {
							Object obj = objs[i];
							if (obj instanceof InputType) {
								extType.getInput().remove(obj);
								canSave = true;
							}
						}

						if (!canSave)
							return;

						List<ISmooksGraphChangeListener> listeners = extType.getChangeListeners();
						for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
							ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator
									.next();
							smooksGraphChangeListener.inputTypeChanged(extType);
						}
					}
				}
			}
		});
	}

	protected SmooksGraphicsExtType getSmooksGraphicsExtType() {
		SmooksGraphicsExtType extType = ((SmooksMultiFormEditor) getEditor()).getSmooksGraphicsExt();
		return extType;
	}

	protected AdapterFactoryEditingDomain getEditingDomain() {
		AdapterFactoryEditingDomain editDomain = (AdapterFactoryEditingDomain) ((SmooksMultiFormEditor) this
				.getEditor()).getEditingDomain();
		return editDomain;
	}

	protected SmooksResourceListType getSmooksConfigResourceList() {
		EObject doc = ((SmooksMultiFormEditor) this.getEditor()).getSmooksModel();
		if (doc instanceof DocumentRoot) {
			return ((DocumentRoot) doc).getSmooksResourceList();
		}
		return null;
	}

	protected void showInputDataWizard() {
		StructuredDataSelectionWizard wizard = new StructuredDataSelectionWizard();
		wizard.setInput(getEditorInput());
		wizard.setSite(getEditorSite());
		wizard.setForcePreviousAndNextButtons(true);
		StructuredDataSelectionWizardDailog dialog = new StructuredDataSelectionWizardDailog(
				getEditorSite().getShell(), wizard, getSmooksGraphicsExtType(), (SmooksMultiFormEditor) getEditor());
		if (dialog.show() == Dialog.OK) {
			SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
			String type = dialog.getType();
			String path = dialog.getPath();
			Properties pros = dialog.getProperties();
			SmooksUIUtils.recordInputDataInfomation(null, extType, type, path, pros);
		}
	}

	private void refreshInputModelView() {
		if (inputModelViewer != null) {
			List<Object> input = generateInputData();
			inputModelViewer.setInput(input);
			SmooksUIUtils.expandSelectorViewer(input, inputModelViewer);
		}
	}

	public void inputTypeChanged(SmooksGraphicsExtType extType) {
		if (inputDataViewer != null)
			inputDataViewer.refresh();
		refreshInputModelView();
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

	private Object getCurrentReaderModel() {
		if (readerCombo == null || readerCombo.isDisposed())
			return null;
		int index = readerCombo.getSelectionIndex();
		if (index < 0)
			return null;
		return readerTypeList.get(index);
	}

	public void sourceChange(Object model) {
		SmooksResourceListType list = getSmooksConfigResourceList();
		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
		if (list == null) {
			ext.setInputType(null, false);
		} else {
			if (list.getAbstractReader().isEmpty()) {
				ext.setInputType(null, false);
			} else {
				AbstractReader reader = list.getAbstractReader().get(0);
				if (CsvReader.class.isInstance(reader) || CSV12Reader.class.isInstance(reader)) {
					ext.setInputType(SmooksModelUtils.INPUT_TYPE_CSV);
				}
				if (EDIReader.class.isInstance(reader) || EDI12Reader.class.isInstance(reader)) {
					ext.setInputType(SmooksModelUtils.INPUT_TYPE_EDI_1_1);
				}
				if (JsonReader.class.isInstance(reader) || Json12Reader.class.isInstance(reader)) {
					ext.setInputType(SmooksModelUtils.INPUT_TYPE_JSON_1_1);
				}
				if (ReaderType.class.isInstance(reader)) {
					ext.setInputType(SmooksModelUtils.INPUT_TYPE_CUSTOME);
				}
			}

		}
		bindingReaderCombo();
		initReaderCombo();
		initReaderConfigSection();
		deactiveAllInputFile();
		if (inputDataViewer != null) {
			inputDataViewer.refresh();
		}
		refreshInputModelView();
	}

	public void graphChanged(SmooksGraphicsExtType extType) {
		// TODO Auto-generated method stub

	}

	public void graphPropertyChange(EStructuralFeature featre, Object value) {
		// TODO Auto-generated method stub

	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		Object model = getCurrentReaderModel();
		if (model == null)
			return;
		if (model instanceof EObject) {
			this.getModelPanelCreator().markPropertyUI(diagnosticResult, (EObject) model);
		}
	}

	public void validateStart() {

	}

	protected boolean isIncorrectInputType(InputType element) {
		if (element == null)
			return false;
		if (element instanceof InputType) {
			String type = ((InputType) element).getType();
			int index = readerCombo.getSelectionIndex();
			if (index == -1)
				return true;

			Object reader = readerTypeList.get(index);
			if (reader instanceof NullReader) {
				return true;
			}
			if (reader instanceof XMLReader || reader instanceof XSDReader || reader instanceof JavaReader) {

			}

			if (reader instanceof XMLReader) {
				if (!SmooksModelUtils.INPUT_TYPE_XML.equals(type)) {
					return true;
				}
			}
			if (reader instanceof XSDReader) {
				if (!SmooksModelUtils.INPUT_TYPE_XSD.equals(type)) {
					return true;
				}
			}
			if (reader instanceof JavaReader) {
				if (!SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
					return true;
				}
			}

			if (reader instanceof EObject) {
				Object obj = ((EObject) reader);
				obj = AdapterFactoryEditingDomain.unwrap(obj);
				if (obj instanceof EDIReader || obj instanceof EDI12Reader) {
					if (!SmooksModelUtils.INPUT_TYPE_EDI_1_1.equals(type)) {
						return true;
					}
				}
				if (obj instanceof CsvReader || obj instanceof CSV12Reader) {
					if (!SmooksModelUtils.INPUT_TYPE_CSV.equals(type)) {
						return true;
					}
				}
				if (obj instanceof JsonReader || obj instanceof Json12Reader) {
					if (!SmooksModelUtils.INPUT_TYPE_JSON_1_1.equals(type)) {
						return true;
					}
				}
				if (obj instanceof ReaderType) {
					if (!SmooksModelUtils.INPUT_TYPE_CUSTOME.equals(type)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private class NullReader {

	}

	private class XMLReader {

	}

	private class XSDReader {

	}

	private class JavaReader {

	}

	private class InputDataViewerLabelProvider extends ExtentionInputLabelProvider implements ITableColorProvider {

		public Color getBackground(Object element, int columnIndex) {
			if (isIncorrectInputType((InputType) element)) {
				// return ColorConstants.darkGray;
			}
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			if (isIncorrectInputType((InputType) element)) {
				return ColorConstants.lightGray;
			}
			return null;
		}
	}
}

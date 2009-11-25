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
package org.jboss.tools.smooks.configuration.editors_10;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;

/**
 * @author Dart
 * 
 */
public class Smooks10InputFormPage extends FormPage implements ISmooksModelValidateListener, ISourceSynchronizeListener {

	
	
	public Smooks10InputFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
	}

	public Smooks10InputFormPage(String id, String title) {
		super(id, title);
		// TODO Auto-generated constructor stub
	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		// TODO Auto-generated method stub
		
	}

	public void validateStart() {
		// TODO Auto-generated method stub
		
	}

	public void sourceChange(Object model) {
		// TODO Auto-generated method stub
		
	}

//	private CheckboxTableViewer inputDataViewer;
//	private TreeViewer inputModelViewer;
//	private Combo readerCombo;
//	private List<Object> readerTypeList = new ArrayList<Object>();
//	private Composite readerConfigComposite;
//	private ModelPanelCreator modelPanelCreator;
//	protected boolean lockCheck = false;
//
//	public Smooks10InputFormPage(FormEditor editor, String id, String title) {
//		super(editor, id, title);
//	}
//
//	public Smooks10InputFormPage(String id, String title) {
//		super(id, title);
//	}
//
//	@Override
//	protected void createFormContent(IManagedForm managedForm) {
//		final ScrolledForm form = managedForm.getForm();
//		FormToolkit toolkit = managedForm.getToolkit();
//		toolkit.decorateFormHeading(form.getForm());
//		form.setText("Input");
//		// // create master details UI
//		// createMasterDetailBlock(managedForm);
//		Composite leftComposite = toolkit.createComposite(form.getBody());
//		GridData gd = new GridData(GridData.FILL_BOTH);
//		gd.widthHint = 700;
//		leftComposite.setLayoutData(gd);
//
//		Composite rightComposite = toolkit.createComposite(form.getBody());
//		gd = new GridData(GridData.FILL_BOTH);
//		rightComposite.setLayoutData(gd);
//
//		GridLayout lgl = new GridLayout();
//		lgl.marginWidth = 0;
//		lgl.marginHeight = 0;
//		leftComposite.setLayout(lgl);
//
//		GridLayout rgl = new GridLayout();
//		rgl.marginWidth = 0;
//		rgl.marginHeight = 0;
//		rightComposite.setLayout(rgl);
//
//		createReaderSection(toolkit, leftComposite);
//		createInputDataSection(toolkit, rightComposite);
//		createReaderConfigSection(toolkit, leftComposite);
//		createInputModelViewerSection(toolkit, rightComposite);
//
//		handleReaderCombo(readerCombo);
//
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.marginHeight = 13;
//		gridLayout.numColumns = 2;
//		gridLayout.horizontalSpacing = 20;
//		gridLayout.makeColumnsEqualWidth = true;
//		form.getBody().setLayout(gridLayout);
//	}
//
//	private void createInputModelViewerSection(FormToolkit toolkit, Composite parent) {
//		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
//		GridData gd = new GridData(GridData.FILL_BOTH);
//		// gd.verticalAlignment = GridData.BEGINNING;
//		section.setLayoutData(gd);
//		section.setText("Input Model View");
//		section.setDescription("View the XML structure model of the input data");
//		FillLayout flayout = new FillLayout();
//		section.setLayout(flayout);
//
//		Composite mainContainer = toolkit.createComposite(section);
//		GridLayout gl = new GridLayout();
//		mainContainer.setLayout(gl);
//		section.setClient(mainContainer);
//
//		Hyperlink refreshLink = toolkit.createHyperlink(mainContainer, "Refresh Input Model Viewer", SWT.NONE);
//		refreshLink.addHyperlinkListener(new IHyperlinkListener() {
//
//			public void linkExited(HyperlinkEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//
//			public void linkEntered(HyperlinkEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//
//			public void linkActivated(HyperlinkEvent e) {
//				refreshInputModelView();
//			}
//		});
//
//		Composite viewerContainer = toolkit.createComposite(mainContainer);
//		gd = new GridData(GridData.FILL_BOTH);
//		viewerContainer.setLayoutData(gd);
//
//		FillLayout fillLayout = new FillLayout();
//		fillLayout.marginHeight = 1;
//		fillLayout.marginWidth = 1;
//		viewerContainer.setBackground(GraphicsConstants.BORDER_CORLOR);
//		viewerContainer.setLayout(fillLayout);
//		inputModelViewer = new TreeViewer(viewerContainer, SWT.NONE);
//		inputModelViewer.setContentProvider(new CompoundStructuredDataContentProvider());
//		inputModelViewer.setLabelProvider(new CompoundStructuredDataLabelProvider());
//		List<Object> inputList = generateInputData();
//		inputModelViewer.setInput(inputList);
//		inputModelViewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//			}
//		});
//		inputModelViewer.addSelectionChangedListener(new ISelectionChangedListener() {
//			public void selectionChanged(SelectionChangedEvent event) {
//				// currentSelection = ((IStructuredSelection)
//				// event.getSelection()).getFirstElement();
//			}
//		});
//		SmooksUIUtils.expandSelectorViewer(inputList, inputModelViewer);
//
//	}
//
//	protected List<Object> generateInputData() {
//		return SelectorCreationDialog.generateInputDataForSmooks10();
//	}
//
//	private void createReaderConfigSection(FormToolkit toolkit, Composite parent) {
//		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
//		GridData gd = new GridData(GridData.FILL_BOTH);
//		section.setLayoutData(gd);
//		section.setText("Input Configuration");
//		section.setDescription("Configurate the input data");
//		FillLayout flayout = new FillLayout();
//		section.setLayout(flayout);
//
//		ScrolledPageBook pageBook = new ScrolledPageBook(section);
//		pageBook.setBackground(toolkit.getColors().getBackground());
//		section.setClient(pageBook);
//
//		readerConfigComposite = pageBook.createPage(pageBook);
//		pageBook.showPage(pageBook);
//
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 2;
//		readerConfigComposite.setLayout(gl);
//
//		initReaderConfigSection();
//	}
//
//	private void createReaderSection(FormToolkit toolkit, Composite parent) {
//		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.verticalAlignment = GridData.BEGINNING;
//		section.setLayoutData(gd);
//		section.setText("Input Type");
//		section.setDescription("Select the input type");
//		FillLayout flayout = new FillLayout();
//		section.setLayout(flayout);
//
//		Composite mainComposite = toolkit.createComposite(section);
//		section.setClient(mainComposite);
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 2;
//		mainComposite.setLayout(gl);
//
//		toolkit.createLabel(mainComposite, "Input Type : ");
//
//		readerCombo = new Combo(mainComposite, SWT.NONE | SWT.READ_ONLY);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		readerCombo.setLayoutData(gd);
//
//		toolkit.createLabel(mainComposite, " ");
//
//		bindingReaderCombo();
//
//		initReaderCombo();
//
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		readerCombo.setLayoutData(gd);
//
//		toolkit.paintBordersFor(mainComposite);
//	}
//
//	private void initReaderConfigSection() {
//		Object reader = getCurrentReaderModel();
//		String type = getSmooksGraphicsExtType().getInputType();
//		if (reader instanceof EObject && type != null) {
//			// createReaderPanel((EObject) list.getAbstractReader().get(0));
//		} else {
//			disposeCompositeControls(readerConfigComposite, null);
//		}
//	}
//
//	private String getCurrentReaderType() {
//		Object reader = getCurrentReaderModel();
//		return getReaderType(reader);
//	}
//
//	private void initReaderCombo() {
//		if (readerCombo == null)
//			return;
//		SmooksResourceListType rlist = getSmooksConfigResourceList();
//		if (rlist == null) {
//			readerCombo.select(-1);
//			return;
//		}
//		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
//		String inputType = ext.getInputType();
//
//		if (inputType == null) {
//			readerCombo.select(0);
//			return;
//		}
//		if (SmooksModelUtils.INPUT_TYPE_XML.equals(inputType)) {
//			readerCombo.select(1);
//		}
//		if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(inputType)) {
//			readerCombo.select(2);
//		}
//		if (SmooksModelUtils.INPUT_TYPE_XSD.equals(inputType)) {
//			readerCombo.select(3);
//		}
//		return;
//	}
//
//	private void handleReaderCombo(final Combo combo) {
//		combo.addSelectionListener(new SelectionListener() {
//
//			public void widgetSelected(SelectionEvent e) {
//				Object newreader = getCurrentReaderModel();
//				if (newreader == null)
//					return;
//				// String type = getCurrentReaderType();
//				// if (type == null) {
//				// getSmooksGraphicsExtType().eUnset(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE);
//				// } else {
//				// getSmooksGraphicsExtType().setInputType(type);
//				// }
//				readerChanged(newreader);
//			}
//
//			public void widgetDefaultSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//
//	// private Command createRemoveReaderCommand() {
//	// SmooksResourceListType rlist = getSmooksConfigResourceList();
//	// List<AbstractReader> readerList = rlist.getAbstractReader();
//	// CompoundCommand compoundCommand = new CompoundCommand();
//	// for (Iterator<?> iterator = readerList.iterator(); iterator.hasNext();) {
//	// AbstractReader abstractReader = (AbstractReader) iterator.next();
//	// Object readerEntry = createReaderEntry(abstractReader, false);
//	// if (readerEntry == null)
//	// continue;
//	// Command removeCommand = RemoveCommand.create(getEditingDomain(), rlist,
//	// SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP,
//	// readerEntry);
//	// if (removeCommand.canExecute()) {
//	// compoundCommand.append(removeCommand);
//	// }
//	// }
//	// if (compoundCommand.isEmpty()) {
//	// return null;
//	// }
//	// return compoundCommand;
//	// }
//
//	private String getReaderType(Object reader) {
//		if (reader instanceof XMLReader) {
//			return SmooksModelUtils.INPUT_TYPE_XML;
//		}
//		if (reader instanceof JavaReader) {
//			return SmooksModelUtils.INPUT_TYPE_JAVA;
//		}
//		if (reader instanceof XSDReader) {
//			return SmooksModelUtils.INPUT_TYPE_XSD;
//		}
//		return null;
//	}
//
//	private void readerChanged(Object reader) {
//		String type = getCurrentReaderType();
//		String oldType = this.getSmooksGraphicsExtType().getInputType();
//
//		if (type == null && oldType == null) {
//			return;
//		}
//		if (type != null && type.equals(oldType)) {
//			return;
//		}
//		if (oldType != null && oldType.equals(type)) {
//			return;
//		}
//
//		Command setTypeCommand = SetCommand.create(getEditingDomain(), getSmooksGraphicsExtType(),
//				GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE, type);
//		CompoundCommand compoundCommand = new CompoundCommand();
//		compoundCommand.append(setTypeCommand);
//		if (readerConfigComposite != null) {
//			disposeCompositeControls(readerConfigComposite, null);
//		}
//		deactiveAllInputFile(compoundCommand);
//		if (!compoundCommand.isEmpty()) {
//			getEditingDomain().getCommandStack().execute(compoundCommand);
//			// createReaderPanel(((EObject) reader));
//		}
//
//		if (inputDataViewer != null) {
//			inputDataViewer.refresh();
//		}
//		refreshInputModelView();
//	}
//
//	private void deactiveAllInputFile(CompoundCommand command) {
//		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
//		List<InputType> inputList = ext.getInput();
//		for (Iterator<?> iterator = inputList.iterator(); iterator.hasNext();) {
//			InputType inputType = (InputType) iterator.next();
//			setInputDataActiveStatus(false, inputType, command);
//		}
//	}
//
//	private void createReaderPanel(EObject reader) {
//		// disposeCompositeControls(readerConfigComposite, null);
//		// try {
//		// ModelPanelCreator modelPanelCreator = getModelPanelCreator();
//		// IItemPropertySource ps = (IItemPropertySource)
//		// getEditingDomain().getAdapterFactory().adapt(reader,
//		// IItemPropertySource.class);
//		// modelPanelCreator.createModelPanel(reader,
//		// getManagedForm().getToolkit(), readerConfigComposite, ps,
//		// (ISmooksModelProvider) getEditor(), getEditor());
//		// readerConfigComposite.getParent().layout();
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// }
//	}
//
//	private ModelPanelCreator getModelPanelCreator() {
//		if (modelPanelCreator == null) {
//			modelPanelCreator = new ModelPanelCreator();
//		}
//		return modelPanelCreator;
//	}
//
//	private void bindingReaderCombo() {
//		if (readerCombo == null)
//			return;
//
//		readerCombo.removeAll();
//		readerTypeList.clear();
//
//		readerCombo.add("No Input");
//		readerTypeList.add(new NullReader());
//		readerCombo.add("XML");
//		readerTypeList.add(new XMLReader());
//		readerCombo.add("Java");
//		readerTypeList.add(new JavaReader());
//		readerCombo.add("XSD/WSDL");
//		readerTypeList.add(new XSDReader());
//
//		SmooksResourceListType resourceList = getSmooksConfigResourceList();
//
//		if (resourceList == null) {
//			return;
//		}
//
//		AdapterFactoryEditingDomain editDomain = getEditingDomain();
//		IEditingDomainItemProvider provider = (IEditingDomainItemProvider) editDomain.getAdapterFactory().adapt(
//				resourceList, IEditingDomainItemProvider.class);
//		Collection<?> collections = provider.getNewChildDescriptors(resourceList, editDomain, null);
//
//		SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
//		String version = extType.getPlatformVersion();
//		OnlyReaderViewerFilter filter = new OnlyReaderViewerFilter();
//		for (Iterator<?> iterator = collections.iterator(); iterator.hasNext();) {
//			Object object = (Object) iterator.next();
//			if (object instanceof CommandParameter) {
//				Object value = ((CommandParameter) object).getValue();
//
//				value = AdapterFactoryEditingDomain.unwrap(value);
//
//				if (filter.select(null, null, value)) {
//					if (SmooksUIUtils.isUnSupportElement(version, (EObject) value)) {
//						continue;
//					}
//
//					IItemLabelProvider lp = (IItemLabelProvider) editDomain.getAdapterFactory().adapt(value,
//							IItemLabelProvider.class);
//					String text = lp.getText(value);
//					readerCombo.add(text);
//					readerTypeList.add(value);
//				}
//			}
//		}
//	}
//
//	private void setInputDataActiveStatus(boolean active, InputType inputType, final CompoundCommand command) {
//		List<ParamType> params = inputType.getParam();
//		boolean newOne = true;
//		for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
//			ParamType paramType = (ParamType) iterator.next();
//			if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
//				Command setCommand = SetCommand.create(getEditingDomain(), paramType,
//						GraphPackage.Literals.PARAM_TYPE__VALUE, String.valueOf(active));
//				if (command != null) {
//					try {
//						command.append(setCommand);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				} else {
//					getEditingDomain().getCommandStack().execute(setCommand);
//				}
//				newOne = false;
//				break;
//			}
//		}
//		if (newOne) {
//			ParamType p = GraphFactory.eINSTANCE.createParamType();
//			p.setName(SmooksModelUtils.PARAM_NAME_ACTIVED);
//			p.setValue(String.valueOf(active));
//			inputType.getParam().add(p);
//			Command addCommand = AddCommand.create(getEditingDomain(), inputType,
//					GraphPackage.Literals.INPUT_TYPE__PARAM, p);
//			if (command != null) {
//				command.append(addCommand);
//			} else {
//				getEditingDomain().getCommandStack().execute(addCommand);
//			}
//		}
//		SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
//		if (ext != null) {
//			List<ISmooksGraphChangeListener> listeners = ((SmooksGraphicsExtType) ext).getChangeListeners();
//			for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
//				ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator.next();
//				smooksGraphChangeListener.inputTypeChanged((SmooksGraphicsExtType) ext);
//			}
//		}
//	}
//
//	protected void createInputDataSection(FormToolkit toolkit, Composite parent) {
//		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		section.setLayoutData(gd);
//		section.setText("Input Data");
//		section.setDescription("Add a file to be the input data");
//		FillLayout flayout = new FillLayout();
//		section.setLayout(flayout);
//
//		Composite mainComposite = toolkit.createComposite(section, SWT.NONE);
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 2;
//		mainComposite.setLayout(gl);
//		section.setClient(mainComposite);
//
//		Composite tableComposite = toolkit.createComposite(mainComposite, SWT.NONE);
//		FillLayout fillLayout = new FillLayout();
//		fillLayout.marginHeight = 1;
//		fillLayout.marginWidth = 1;
//		gd = new GridData(GridData.FILL_BOTH);
//		gd.heightHint = 200;
//		tableComposite.setLayoutData(gd);
//		tableComposite.setBackground(GraphicsConstants.BORDER_CORLOR);
//		tableComposite.setLayout(fillLayout);
//
//		inputDataViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
//		// inputDataViewer.set
//		inputDataViewer.setCheckStateProvider(new ICheckStateProvider() {
//
//			public boolean isGrayed(Object element) {
//				return isIncorrectInputType((InputType) element);
//			}
//
//			public boolean isChecked(Object element) {
//				if (element instanceof InputType) {
//					List<ParamType> params = ((InputType) element).getParam();
//					for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
//						ParamType paramType = (ParamType) iterator.next();
//						if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
//							String value = paramType.getValue();
//							if (value == null)
//								return false;
//							value = value.trim();
//							return "true".equalsIgnoreCase(value);
//						}
//					}
//				}
//				return false;
//			}
//		});
//
//		// inputDataViewer.addDoubleClickListener(new IDoubleClickListener() {
//		// public void doubleClick(DoubleClickEvent event) {
//		// InputType inputType =(InputType)
//		// ((IStructuredSelection)inputDataViewer.getSelection()).getFirstElement();
//		// String path = SmooksModelUtils.getInputPath(inputType);
//		// }
//		// });
//
//		// IFile file = File
//
//		inputDataViewer.addCheckStateListener(new ICheckStateListener() {
//
//			public void checkStateChanged(CheckStateChangedEvent event) {
//				if (lockCheck)
//					return;
//				boolean checked = event.getChecked();
//				InputType inputType = (InputType) event.getElement();
//				if (isIncorrectInputType(inputType)) {
//					lockCheck = true;
//					inputDataViewer.setChecked(inputType, false);
//					lockCheck = false;
//					return;
//				}
//				List<ParamType> params = inputType.getParam();
//
//				if (checked) {
//					boolean newOne = true;
//					for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
//						ParamType paramType = (ParamType) iterator.next();
//						if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
//							Command setCommand = SetCommand.create(getEditingDomain(), paramType,
//									GraphPackage.Literals.PARAM_TYPE__VALUE, String.valueOf(checked));
//							getEditingDomain().getCommandStack().execute(setCommand);
//							newOne = false;
//							break;
//						}
//					}
//					if (newOne) {
//						ParamType p = GraphFactory.eINSTANCE.createParamType();
//						p.setName(SmooksModelUtils.PARAM_NAME_ACTIVED);
//						p.setValue(String.valueOf(checked));
//						Command addCommand = AddCommand.create(getEditingDomain(), inputType,
//								GraphPackage.Literals.INPUT_TYPE__PARAM, p);
//						getEditingDomain().getCommandStack().execute(addCommand);
//						inputType.getParam().add(p);
//					}
//
//					Object[] checkedObjects = inputDataViewer.getCheckedElements();
//					for (int i = 0; i < checkedObjects.length; i++) {
//						InputType type = (InputType) checkedObjects[i];
//						if (type == event.getElement())
//							continue;
//						List<ParamType> params1 = type.getParam();
//						for (Iterator<?> iterator = params1.iterator(); iterator.hasNext();) {
//							ParamType paramType = (ParamType) iterator.next();
//							if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
//								Command setCommand = SetCommand.create(getEditingDomain(), paramType,
//										GraphPackage.Literals.PARAM_TYPE__VALUE, String.valueOf(!checked));
//								getEditingDomain().getCommandStack().execute(setCommand);
//								break;
//							}
//						}
//						lockCheck = true;
//						inputDataViewer.setChecked(type, false);
//						lockCheck = false;
//					}
//
//				} else {
//					for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
//						ParamType paramType = (ParamType) iterator.next();
//						if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
//							Command setCommand = SetCommand.create(getEditingDomain(), paramType,
//									GraphPackage.Literals.PARAM_TYPE__VALUE, String.valueOf(checked));
//							getEditingDomain().getCommandStack().execute(setCommand);
//
//							break;
//						}
//					}
//				}
//
//				SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
//				if (ext != null) {
//					List<ISmooksGraphChangeListener> listeners = ((SmooksGraphicsExtType) ext).getChangeListeners();
//					for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
//						ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator
//								.next();
//						smooksGraphChangeListener.inputTypeChanged((SmooksGraphicsExtType) ext);
//					}
//				}
//
//			}
//		});
//		TableColumn header = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
//		header.setText("Type");
//		header.setWidth(100);
//		TableColumn pathColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
//		pathColumn.setText("Path");
//		pathColumn.setWidth(300);
//
//		// TableColumn extColumn = new TableColumn(inputDataViewer.getTable(),
//		// SWT.NONE);
//		// extColumn.setText("Extension Paramers");
//		// extColumn.setWidth(400);
//		inputDataViewer.setContentProvider(new ExtentionInputContentProvider());
//		inputDataViewer.setLabelProvider(new InputDataViewerLabelProvider());
//		inputDataViewer.getTable().setHeaderVisible(true);
//		inputDataViewer.getTable().setLinesVisible(true);
//		SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
//		if (extType != null) {
//			inputDataViewer.setInput(extType);
//		}
//		Composite buttonComposite = toolkit.createComposite(mainComposite, SWT.NONE);
//		gd = new GridData(GridData.FILL_VERTICAL);
//		buttonComposite.setLayoutData(gd);
//		GridLayout l = new GridLayout();
//		buttonComposite.setLayout(l);
//
//		Button addButton = toolkit.createButton(buttonComposite, "Add", SWT.FLAT);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		addButton.setLayoutData(gd);
//		addButton.addSelectionListener(new SelectionAdapter() {
//
//			public void widgetSelected(SelectionEvent e) {
//				showInputDataWizard();
//			}
//
//		});
//
//		Button removeButton = toolkit.createButton(buttonComposite, "Delete", SWT.FLAT);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		removeButton.setLayoutData(gd);
//		removeButton.addSelectionListener(new SelectionAdapter() {
//
//			public void widgetSelected(SelectionEvent e) {
//				IStructuredSelection selection = (IStructuredSelection) inputDataViewer.getSelection();
//				if (selection != null) {
//					SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
//					if (extType != null) {
//						boolean canFireEvent = false;
//						Command rc = RemoveCommand.create(getEditingDomain(), selection.toList());
//						if (rc.canExecute()) {
//							getEditingDomain().getCommandStack().execute(rc);
//							canFireEvent = true;
//						}
//
//						if (!canFireEvent)
//							return;
//
//						List<ISmooksGraphChangeListener> listeners = extType.getChangeListeners();
//						for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
//							ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator
//									.next();
//							smooksGraphChangeListener.inputTypeChanged(extType);
//						}
//					}
//				}
//			}
//		});
//	}
//
//	protected SmooksGraphicsExtType getSmooksGraphicsExtType() {
//		SmooksGraphicsExtType extType = ((Smooks10MultiFormEditor) getEditor()).getSmooksGraphicsExt();
//		return extType;
//	}
//
//	protected AdapterFactoryEditingDomain getEditingDomain() {
//		AdapterFactoryEditingDomain editDomain = (AdapterFactoryEditingDomain) ((Smooks10MultiFormEditor) this
//				.getEditor()).getEditingDomain();
//		return editDomain;
//	}
//
//	protected SmooksResourceListType getSmooksConfigResourceList() {
//		EObject doc = ((Smooks10MultiFormEditor) this.getEditor()).getSmooksModel();
//		if (doc instanceof DocumentRoot) {
//			return ((DocumentRoot) doc).getSmooksResourceList();
//		}
//		return null;
//	}
//
//	protected void showInputDataWizard() {
//		StructuredDataSelectionWizard wizard = new StructuredDataSelectionWizard();
//		wizard.setInput(getEditorInput());
//		wizard.setSite(getEditorSite());
//		wizard.setForcePreviousAndNextButtons(true);
//		StructuredDataSelectionWizardDailog dialog = new StructuredDataSelectionWizardDailog(
//				getEditorSite().getShell(), wizard, getSmooksGraphicsExtType());
//		if (dialog.show() == Dialog.OK) {
//			SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
//			String type = dialog.getType();
//			String path = dialog.getPath();
//			Properties pros = dialog.getProperties();
//			SmooksUIUtils.recordInputDataInfomation(getEditingDomain(), null, extType, type, path, pros);
//		}
//	}
//
//	private void refreshInputModelView() {
//		if (inputModelViewer != null) {
//			List<Object> input = generateInputData();
//			inputModelViewer.setInput(input);
//			SmooksUIUtils.expandSelectorViewer(input, inputModelViewer);
//		}
//	}
//
//	public void inputTypeChanged(SmooksGraphicsExtType extType) {
//		if (inputDataViewer != null)
//			inputDataViewer.refresh();
//		refreshInputModelView();
//	}
//
//	protected void disposeCompositeControls(Composite composite, Control[] ignoreControl) {
//		if (composite != null) {
//			Control[] children = composite.getChildren();
//			for (int i = 0; i < children.length; i++) {
//				Control child = children[i];
//				if (ignoreControl != null) {
//					for (int j = 0; j < ignoreControl.length; j++) {
//						if (child == ignoreControl[j]) {
//							continue;
//						}
//					}
//				}
//				child.dispose();
//				child = null;
//			}
//		}
//	}
//
//	private Object getCurrentReaderModel() {
//		if (readerCombo == null || readerCombo.isDisposed())
//			return null;
//		int index = readerCombo.getSelectionIndex();
//		if (index < 0)
//			return null;
//		return readerTypeList.get(index);
//	}
//
//	public void sourceChange(Object model) {
//		bindingReaderCombo();
//		initReaderCombo();
//		initReaderConfigSection();
//		if (inputDataViewer != null) {
//			inputDataViewer.setInput(getSmooksGraphicsExtType());
//			inputDataViewer.refresh();
//		}
//		refreshInputModelView();
//	}
//
//	public void graphChanged(SmooksGraphicsExtType extType) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void graphPropertyChange(EStructuralFeature featre, Object value) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void validateEnd(List<Diagnostic> diagnosticResult) {
//		Object model = getCurrentReaderModel();
//		if (model == null)
//			return;
//		if (model instanceof EObject) {
//			this.getModelPanelCreator().markPropertyUI(diagnosticResult, (EObject) model);
//		}
//	}
//
//	public void validateStart() {
//
//	}
//
//	protected boolean isIncorrectInputType(InputType element) {
//		if (element == null)
//			return false;
//		if (element instanceof InputType) {
//			String type = ((InputType) element).getType();
//			int index = readerCombo.getSelectionIndex();
//			if (index == -1)
//				return true;
//
//			Object reader = readerTypeList.get(index);
//			if (reader instanceof NullReader) {
//				return true;
//			}
//
//			if (reader instanceof XMLReader) {
//				if (!SmooksModelUtils.INPUT_TYPE_XML.equals(type)) {
//					return true;
//				}
//			}
//			if (reader instanceof XSDReader) {
//				if (!SmooksModelUtils.INPUT_TYPE_XSD.equals(type)) {
//					return true;
//				}
//			}
//			if (reader instanceof JavaReader) {
//				if (!SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
//					return true;
//				}
//			}
//
//		}
//		return false;
//	}
//
//	private class NullReader {
//
//	}
//
//	private class XMLReader {
//
//	}
//
//	private class XSDReader {
//
//	}
//
//	private class JavaReader {
//
//	}
//
//	private class InputDataViewerLabelProvider extends ExtentionInputLabelProvider implements ITableColorProvider {
//
//		public Color getBackground(Object element, int columnIndex) {
//			if (isIncorrectInputType((InputType) element)) {
//				// return ColorConstants.darkGray;
//			}
//			return null;
//		}
//
//		public Color getForeground(Object element, int columnIndex) {
//			if (isIncorrectInputType((InputType) element)) {
//				return ColorConstants.lightGray;
//			}
//			return null;
//		}
//	}
}

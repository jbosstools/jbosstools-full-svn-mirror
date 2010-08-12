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
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.jface.wizard.WizardDialog;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
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
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.configuration.editors.input.InputType;
import org.jboss.tools.smooks.configuration.editors.input.InvalidInputSourceTypeException;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.wizard.StructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.wizard.ViewerInitorStore;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.ICoreFactory;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IGlobalParams;
import org.jboss.tools.smooks.model.core.IParam;
import org.milyn.SmooksException;
import org.milyn.javabean.dynamic.Model;



/**
 * @author Dart
 * 
 */
public class SmooksReaderFormPage extends FormPage implements ISourceSynchronizeListener,
		CommandStackListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.common.command.CommandStackListener#commandStackChanged
	 * (java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		final Command mostRecentCommand = ((org.eclipse.emf.common.command.CommandStack) event.getSource())
				.getMostRecentCommand();
		getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (mostRecentCommand != null) {
					Command rawCommand = mostRecentCommand;
					while (rawCommand instanceof CommandWrapper) {
						rawCommand = ((CommandWrapper) rawCommand).getCommand();
					}
					if (rawCommand instanceof CompoundCommand) {
						List<Command> command = ((CompoundCommand) rawCommand).getCommandList();
						for (Iterator<?> iterator = command.iterator(); iterator.hasNext();) {
							Command command2 = (Command) iterator.next();
							while (command2 instanceof CommandWrapper) {
								command2 = ((CommandWrapper) command2).getCommand();
							}
							if (handleInputParamChange(command2)) {
								break;
							}
						}
					} else {
						handleInputParamChange(rawCommand);
					}
				}
			}
		});
	}

	private CheckboxTableViewer inputDataViewer;
	private TreeViewer inputModelViewer;
	private Combo readerCombo;
	private Composite readerConfigComposite;
//	private ModelPanelCreator modelPanelCreator;
	protected boolean lockCheck = false;
	private Button removeInputDataButton;
	private Button addInputDataButton;
	private ScrolledPageBook scrolledPageBook;

	public SmooksReaderFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public SmooksReaderFormPage(String id, String title) {
		super(id, title);
	}
	
	public ISmooksModelProvider getSmooksModelProvider(){
		return (ISmooksModelProvider)getEditor();
	}
	

	private void handleCommandStack(CommandStack commandStack) {
		commandStack.addCommandStackListener(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		if (getEditor() != null && getEditor() instanceof ISmooksModelProvider) {
			this.handleCommandStack(((ISmooksModelProvider) getEditor()).getEditingDomain().getCommandStack());
		}
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(""); //$NON-NLS-1$
		// toolkit.decorateFormHeading(form.getForm());
		// // create master details UI
		// createMasterDetailBlock(managedForm);
		Composite leftComposite = toolkit.createComposite(form.getBody());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
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

		refreshInputDataButtons();
	}
	
	private void refreshInputDataButtons() {
		this.addInputDataButton.setEnabled(true);
		this.removeInputDataButton.setEnabled(true);

		String inputType = getSmooksModelProvider().getInputType();
		if (inputType == null || inputType.trim().equals("")) { //$NON-NLS-1$
			this.addInputDataButton.setEnabled(false);
			this.removeInputDataButton.setEnabled(false);
		}
	}
	
	private void createInputModelViewerSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.verticalAlignment = GridData.BEGINNING;
		section.setLayoutData(gd);
		section.setText(Messages.SmooksReaderFormPage_InputSectionTitle);
		// section.setDescription("View the XML structure model of the input data");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainContainer = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		mainContainer.setLayout(gl);
		section.setClient(mainContainer);

		Hyperlink refreshLink = toolkit.createHyperlink(mainContainer, Messages.SmooksReaderFormPage_RefreshLinkLabel,
				SWT.NONE);
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
		viewerContainer.setBackground(GraphicsConstants.BORDER_CORLOR);
		viewerContainer.setLayout(fillLayout);
		inputModelViewer = new TreeViewer(viewerContainer, SWT.NONE);
		inputModelViewer.setContentProvider(new CompoundStructuredDataContentProvider());
		inputModelViewer.setLabelProvider(new CompoundStructuredDataLabelProvider());
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

		refreshInputModelView();

	}
	
	private void refreshInputModelView() {
		if (this.getManagedForm() != null) {
			this.getManagedForm().getMessageManager().removeAllMessages();
		}
		if (inputModelViewer != null) {
			List<Object> input = generateInputData();
			try {
				inputModelViewer.setInput(input);
				SmooksUIUtils.expandSelectorViewer(input, inputModelViewer);
			} catch (Throwable e) {

			}

			if (input == null || input.isEmpty()) {
				Throwable t = SelectorCreationDialog.getCurrentException();
				if (t != null) {
					if (this.getManagedForm() != null) {
						if (t instanceof SmooksException && t.getCause() != null) {
							t = t.getCause();
						}
						this.getManagedForm().getMessageManager()
								.addMessage(
										Messages.SmooksReaderFormPage_Input_Error,
										Messages.SmooksReaderFormPage_Error_Creating_Input_Model + t.getMessage()
												+ "\"", null, IMessageProvider.ERROR); //$NON-NLS-1$
					}
				}
			}
		}
	}

	protected List<Object> generateInputData() {
		Model<SmooksModel> obj = ((SmooksMultiFormEditor) getEditor()).getSmooksModel();
		SmooksModel resourceList = obj.getModelRoot();
		IJavaProject javaProject = JavaCore.create(((IFileEditorInput)getEditorInput()).getFile().getProject());
		return SelectorCreationDialog.generateInputData(resourceList,javaProject);
	}
	
	private void createReaderConfigSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gd);
		section.setText(Messages.SmooksReaderFormPage_InputConfigurationSectionTitle);
		section.setDescription(Messages.SmooksReaderFormPage_InputConfigurationSectionDes);
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		scrolledPageBook = new ScrolledPageBook(section);
		scrolledPageBook.setBackground(toolkit.getColors().getBackground());
		section.setClient(scrolledPageBook);

		readerConfigComposite = scrolledPageBook.createPage(scrolledPageBook);
		scrolledPageBook.showPage(scrolledPageBook);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		readerConfigComposite.setLayout(gl);

		initReaderConfigSection();
	}
	
	private void initReaderConfigSection() {
		ISmooksModelProvider provider = getSmooksModelProvider();
		
		if (provider == null) {
			return;
		}

		addInputConfigControls();
	}
	
	private void addInputConfigControls() {
		disposeCompositeControls(readerConfigComposite, null);
		
		getCurrentInputType().getConfigurationContributor().addInputConfigControls(this, readerConfigComposite);
		
		readerConfigComposite.layout();
		scrolledPageBook.reflow(false);
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
	
	private InputSourceType getCurrentInputType() {
		if (readerCombo == null || readerCombo.isDisposed()) {
			return InputSourceType.NONE;
		}
		
		try {
			return InputSourceType.fromTypeIndex(readerCombo.getSelectionIndex());
		} catch (InvalidInputSourceTypeException e) {
			return InputSourceType.NONE;
		}
	}
	
	protected void createInputDataSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
				| Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gd);
		section.setText(Messages.SmooksReaderFormPage_InputDataSectionTitle);
		section.setDescription(Messages.SmooksReaderFormPage_InputDataSectionDes);
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
		gd.heightHint = 70;
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.BORDER_CORLOR);
		tableComposite.setLayout(fillLayout);

		inputDataViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		// inputDataViewer.set
		inputDataViewer.setCheckStateProvider(new ICheckStateProvider() {

			public boolean isGrayed(Object element) {
				return !isValidInputType((InputType) element);
			}

			public boolean isChecked(Object element) {
				if (element instanceof InputType) {
					return ((InputType) element).isActived();
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
				if (!isValidInputType(inputType)) {
					lockCheck = true;
					inputDataViewer.setChecked(inputType, false);
					lockCheck = false;
					return;
				}
				CompoundCommand compoundCommand = new CompoundCommand();
				if (checked) {
					IParam param = SmooksUIUtils.getInputTypeAssociatedParamType(inputType,
							getSmooksConfigResourceList());
					if (param != null) {
						inputType.setActived(checked);
						String value = SmooksModelUtils.INPUT_ACTIVE_TYPE;
						Command c = SetCommand.create(getEditingDomain(), param,
								ICorePackage.Literals.PARAM__TYPE, value);
						if (c.canExecute())
							compoundCommand.append(c);
					}

					Object[] checkedObjects = inputDataViewer.getCheckedElements();
					for (int i = 0; i < checkedObjects.length; i++) {
						InputType type = (InputType) checkedObjects[i];
						if (type == inputType) {
							continue;
						}
						type.setActived(!checked);
						IParam param1 = SmooksUIUtils.getInputTypeAssociatedParamType(type,
								getSmooksConfigResourceList());
						if (param1 != null) {
							String value1 = SmooksModelUtils.INPUT_DEACTIVE_TYPE;
							Command c1 = SetCommand.create(getEditingDomain(), param1,
									ICorePackage.Literals.PARAM__TYPE, value1);
							compoundCommand.append(c1);
						}

						lockCheck = true;
						inputDataViewer.setChecked(type, false);
						lockCheck = false;
					}

				} else {
					IParam param = SmooksUIUtils.getInputTypeAssociatedParamType(inputType,
							getSmooksConfigResourceList());
					if (param != null) {
						String value = SmooksModelUtils.INPUT_DEACTIVE_TYPE;
						Command c = SetCommand.create(getEditingDomain(), param,
								ICorePackage.Literals.PARAM__TYPE, value);
						compoundCommand.append(c);
					}
				}
				try {
					getEditingDomain().getCommandStack().execute(compoundCommand);
				} catch (Exception e) {
					// e.printStackTrace();
				}

				// refreshInputModelView();
			}
		});
		inputDataViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object element = selection.getFirstElement();
				if (element instanceof InputType) {
					String type = ((InputType) element).getType();
					String filePath = ((InputType) element).getPath();
					if (type != null && filePath != null) {
						if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
							IFile file = ((IFileEditorInput) getEditorInput()).getFile();
							IJavaProject javaProject = JavaCore.create(file.getProject());
							if (javaProject != null) {
								try {
									if (filePath.endsWith("[]")) { //$NON-NLS-1$
										filePath = filePath.substring(0, filePath.length() - 2);
									}
									IJavaElement result = javaProject.findType(filePath);
									if (result != null)
										JavaUI.openInEditor(result);
									else {
										MessageDialog.openError(getSite().getWorkbenchWindow().getShell(),
												Messages.SmooksReaderFormPage_CantFindTypeErrorTitle,
												"Can't find type \"" + filePath + "\" in \"" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
														+ javaProject.getProject().getName() + "\" project."); //$NON-NLS-1$
									}
								} catch (Exception e) {

								}
							}
						} else {
							try {
								filePath = SmooksUIUtils.parseFilePath(filePath);
								if (filePath != null) {
									IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(filePath));
									IFileInfo fetchInfo = fileStore.fetchInfo();
									if (!fetchInfo.isDirectory() && fetchInfo.exists()) {
										IWorkbenchWindow window = getSite().getWorkbenchWindow();
										IWorkbenchPage page = window.getActivePage();
										try {
											IDE.openEditorOnFileStore(page, fileStore);
										} catch (PartInitException e) {
											MessageDialog.open(MessageDialog.ERROR, window.getShell(),
													Messages.SmooksReaderFormPage_OpenFileErrorTitle,
													"Can't open the file : '" + filePath + "'", SWT.SHEET); //$NON-NLS-1$ //$NON-NLS-2$
										}
									} else {
									}
								}
							} catch (Exception e) {
								MessageDialog.open(MessageDialog.ERROR, getSite().getWorkbenchWindow().getShell(),
										Messages.SmooksReaderFormPage_OpenFileErrorTitle,
										"Can't open the file : '" + filePath + "'", SWT.SHEET); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
							}
						}
					}
				}
			}
		});
		TableColumn header = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		header.setText(Messages.SmooksReaderFormPage_TypeColumnText);
		header.setWidth(100);
		TableColumn pathColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		pathColumn.setText(Messages.SmooksReaderFormPage_PathColumnText);
		pathColumn.setWidth(300);

		// TableColumn extColumn = new TableColumn(inputDataViewer.getTable(),
		// SWT.NONE);
		// extColumn.setText("Extension Paramers");
		// extColumn.setWidth(400);
		inputDataViewer.setContentProvider(new ExtentionInputContentProvider());
		inputDataViewer.setLabelProvider(new InputDataViewerLabelProvider());
		inputDataViewer.getTable().setHeaderVisible(true);
		inputDataViewer.getTable().setLinesVisible(true);
		ISmooksModelProvider provider = getSmooksModelProvider();
		if (provider != null) {
			inputDataViewer.setInput(SmooksUIUtils.getInputTypeList(getSmooksConfigResourceList().getModelRoot()));
		}
		Composite buttonComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);
		GridLayout l = new GridLayout();
		buttonComposite.setLayout(l);

		addInputDataButton = toolkit.createButton(buttonComposite, Messages.SmooksReaderFormPage_AddButtonLabel,
				SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addInputDataButton.setLayoutData(gd);
		addInputDataButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				showInputDataWizard();
			}

		});

		removeInputDataButton = toolkit.createButton(buttonComposite, Messages.SmooksReaderFormPage_DeleteButtonLabel,
				SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeInputDataButton.setLayoutData(gd);
		removeInputDataButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) inputDataViewer.getSelection();
				if (selection != null) {
					List<?> inputs = selection.toList();
					ISmooksModelProvider smooksModelProvider = getSmooksModelProvider();
					for (Iterator<?> iterator = inputs.iterator(); iterator.hasNext();) {
						InputType input = (InputType) iterator.next();
						SmooksUIUtils.removeInputType(input, smooksModelProvider);
					}
					if (!inputs.isEmpty()) {
						List<?> viewerInput = (List<?>) inputDataViewer.getInput();
						viewerInput.removeAll(inputs);
						// inputTypeChanged();
						if (inputDataViewer != null) {
							inputDataViewer.refresh();
						}
					}
				}
			}
		});
	}
	
	
	protected boolean handleInputParamChange(Command command) {
		Collection<?> affectedObjects = command.getAffectedObjects();
		boolean refreshInputModel = false;
		for (Iterator<?> iterator2 = affectedObjects.iterator(); iterator2.hasNext();) {
			Object object = (Object) iterator2.next();
//			if (object instanceof AbstractReader) {
//				refreshInputModel = true;
//				break;
//			}
			if (object instanceof IParam) {
				if (SmooksUIUtils.isInputParamType((IParam) object)) {
					refreshInputModel = true;
					break;
				}
			}
			Collection<?> deletedObjs = null;
			if (command instanceof DeleteCommand) {
				deletedObjs = ((DeleteCommand) command).getCollection();
			}
			if (command instanceof RemoveCommand) {
				deletedObjs = ((RemoveCommand) command).getCollection();
			}
			if (deletedObjs != null) {
				for (Iterator<?> iterator = deletedObjs.iterator(); iterator.hasNext();) {
					Object object2 = (Object) iterator.next();
//					if (object2 instanceof AbstractReader) {
//						refreshInputModel = true;
//						break;
//					}
					if (object2 instanceof IParam) {
						if (SmooksUIUtils.isInputParamType((IParam) object2)) {
							refreshInputModel = true;
							break;
						}
					}
				}
			}
			if (refreshInputModel)
				break;
		}

		if (refreshInputModel) {
			refreshInputModelView();
		}

		return refreshInputModel;
	}

	protected EditingDomain getEditingDomain() {
		return getSmooksModelProvider().getEditingDomain();
	}

	protected Model<SmooksModel> getSmooksConfigResourceList() {
		return getSmooksModelProvider().getSmooksModel();
	}

	protected boolean isValidInputType(InputType element) {
		if (element == null) {
			// not specified is OK...
			return true;
		}
		
		String type = element.getType();
		int selectionIndex = readerCombo.getSelectionIndex();

		try {
			return InputSourceType.isValidIndexNamePair(selectionIndex, type);
		} catch (InvalidInputSourceTypeException e) {
			return false;
		}
	}

	
	private void createReaderSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		section.setLayoutData(gd);
		section.setText(Messages.SmooksReaderFormPage_InputTypeSectionTitle);
		// section.setDescription("Select the input type");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(section);
		section.setClient(mainComposite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);

		toolkit.createLabel(mainComposite, Messages.SmooksReaderFormPage_InputTypeLabel);

		readerCombo = new Combo(mainComposite, SWT.NONE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		readerCombo.setLayoutData(gd);

		toolkit.createLabel(mainComposite, " "); //$NON-NLS-1$

		bindingReaderCombo();

		initReaderCombo();

		gd = new GridData(GridData.FILL_HORIZONTAL);
		readerCombo.setLayoutData(gd);

		toolkit.paintBordersFor(mainComposite);
	}
	
	private void bindingReaderCombo() {
		if (readerCombo == null)
			return;

		readerCombo.removeAll();

		readerCombo.add(Messages.SmooksReaderFormPage_NoInputComboText, InputSourceType.NONE.getTypeIndex());
		readerCombo.add(Messages.SmooksReaderFormPage_XMLReaderComboText, InputSourceType.XML.getTypeIndex());
		readerCombo.add(Messages.SmooksReaderFormPage_XSDReaderComboText, InputSourceType.XSD.getTypeIndex());
		readerCombo.add(Messages.SmooksReaderFormPage_JavaReaderComboText, InputSourceType.JAVA.getTypeIndex());
	}
	
	private void initReaderCombo() {
		if (readerCombo == null)
			return;
		Model<SmooksModel> model = getSmooksModelProvider().getSmooksModel();
		if (model == null) {
			readerCombo.select(-1);
			return;
		}

		ISmooksModelProvider modelProvider = getSmooksModelProvider();

		String inputType = modelProvider.getInputType();

		if (inputType == null) {
			readerCombo.select(InputSourceType.NONE.getTypeIndex());
			return;
		}

		try {
			readerCombo.select(InputSourceType.fromTypeName(inputType).getTypeIndex());
		} catch (InvalidInputSourceTypeException e) {
			readerCombo.select(InputSourceType.NONE.getTypeIndex());
		}
	}
	
	private void handleReaderCombo(final Combo combo) {
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				readerChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void readerChanged() {

		String type = getCurrentInputType().toTypeString();
		String oldType = this.getSmooksModelProvider().getInputType();

		if (type == null && oldType == null) {
			return;
		}
		if (type != null && type.equals(oldType)) {
			return;
		}
		if (oldType != null && oldType.equals(type)) {
			return;
		}

		CompoundCommand compoundCommand = new CompoundCommand();

		IGlobalParams params = getSmooksConfigResourceList().getModelRoot().getParams();
		if (params == null) {
			params = ICoreFactory.eINSTANCE.createGlobalParams();
			Command addparamsCommand = SetCommand.create(getEditingDomain(), getSmooksConfigResourceList().getModelRoot(),
					ICorePackage.Literals.PARAMS, params);
			if (addparamsCommand.canExecute())
				compoundCommand.append(addparamsCommand);
		}

		IParam param = SmooksUIUtils.getInputTypeParam(getSmooksConfigResourceList().getModelRoot());
		if (param == null) {
			// add new one
			param = ICoreFactory.eINSTANCE.createParam();
			param.setName(SmooksModelUtils.INPUT_TYPE);
			param.setValue(type);
			Command addparamc = AddCommand.create(getEditingDomain(), params,
					ICorePackage.Literals.PARAMS__PARAMS, param);
			if (addparamc.canExecute())
				compoundCommand.append(addparamc);
		} else {

			Command setCommand = null;
			if (type != null) {
				setCommand = SetCommand.create(getEditingDomain(), param,ICorePackage.Literals.PARAM__VALUE,  type);
			}
			
			if (setCommand != null && setCommand.canExecute()) {
				compoundCommand.append(setCommand);
			}else{
				throw new RuntimeException("Can't set the value to Param element");
			}

		}
		Command removeCommand = createRemoveReaderCommand();
		if (removeCommand != null && removeCommand.canExecute()) {
			compoundCommand.append(removeCommand);
		}

		addInputConfigControls();		
		
		deactiveAllInputFile(compoundCommand);
		if (!compoundCommand.isEmpty() && compoundCommand.canExecute()) {
			getEditingDomain().getCommandStack().execute(compoundCommand);
		}

		if (inputDataViewer != null) {
			inputDataViewer.refresh();
		}
		refreshInputModelView();
		refreshInputDataButtons();
	}
	
	private Command createRemoveReaderCommand() {
		Model<SmooksModel> rlist = getSmooksConfigResourceList();
//		List<AbstractReader> readerList = rlist.getAbstractReader();
		CompoundCommand compoundCommand = new CompoundCommand();
//		for (Iterator<?> iterator = readerList.iterator(); iterator.hasNext();) {
//			AbstractReader abstractReader = (AbstractReader) iterator.next();
//			Object readerEntry = createReaderEntry(abstractReader, false);
//			if (readerEntry == null)
//				continue;
//			Command removeCommand = RemoveCommand.create(getEditingDomain(), rlist,
//					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, readerEntry);
//			if (removeCommand.canExecute()) {
//				compoundCommand.append(removeCommand);
//			}
//		}
//		if (compoundCommand.isEmpty()) {
//			return null;
//		}
		return compoundCommand;
	}
	
	protected void showInputDataWizard() {
		CompoundCommand compoundCommand = new CompoundCommand();
		// SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
		String inputType = getSmooksModelProvider().getInputType();
		List<InputType> inputTypes = null;
		Model<SmooksModel> smooksModel =  getSmooksConfigResourceList();
		SmooksModel rootModel = smooksModel.getModelRoot();
		if (inputType == null || SmooksModelUtils.INPUT_TYPE_CUSTOME.equals(inputType) || inputType.trim().equals("")) { //$NON-NLS-1$
			StructuredDataSelectionWizard wizard = new StructuredDataSelectionWizard();
			wizard.setInput(getEditorInput());
			wizard.setSite(getEditorSite());
			wizard.setForcePreviousAndNextButtons(true);
			StructuredDataSelectionWizardDailog dialog = new StructuredDataSelectionWizardDailog(getEditorSite()
					.getShell(), wizard);

			if (dialog.show() == Dialog.OK) {
				String type = dialog.getType();
				String path = dialog.getPath();
				Properties pros = dialog.getProperties();
				inputTypes = SmooksUIUtils.recordInputDataInfomation(getEditingDomain(),rootModel.getParams() , type, path, pros, compoundCommand);
			}
		} else {
			IStructuredDataSelectionWizard wizard = ViewerInitorStore.getInstance().getStructuredDataCreationWizard(
					inputType);
			WizardDialog dialog = new WizardDialog(getEditorSite().getShell(), wizard);
			wizard.init(getEditorSite(), getEditorInput());
			if (dialog.open() == Dialog.OK) {
				String path = wizard.getStructuredDataSourcePath();
				Properties pros = wizard.getProperties();
				inputTypes = SmooksUIUtils.recordInputDataInfomation(getEditingDomain(), rootModel.getParams(), inputType, path, pros, compoundCommand);
			}
		}

		if (inputTypes != null && !inputTypes.isEmpty()) {
			InputType addedInputType = inputTypes.get(0);
			Object obj = this.inputDataViewer.getInput();
			if (obj != null && obj instanceof List) {
				((List) obj).add(addedInputType);
			}

			deactiveAllInputFile(compoundCommand);
			if (inputType.equals(SmooksModelUtils.INPUT_TYPE_CUSTOME)) {
				// don't active the input file
			} else {
				addedInputType.setActived(true);
				IParam param = addedInputType.getRelatedParameter();
				if (param != null) {
					String value = SmooksModelUtils.INPUT_ACTIVE_TYPE;
					Command c = SetCommand.create(this.getEditingDomain(), param,
							ICorePackage.Literals.PARAM__TYPE, value);
					if (c.canExecute()) {
						compoundCommand.append(c);
					}
				}
			}
			if (!compoundCommand.isEmpty()) {
				getSmooksModelProvider().getEditingDomain().getCommandStack().execute(compoundCommand);
			}
			if (inputDataViewer != null)
				inputDataViewer.refresh();
		}
	}
	
	private void deactiveAllInputFile(CompoundCommand command) {
		Object viewerInput = this.inputDataViewer.getInput();
		if (viewerInput != null && viewerInput instanceof List<?>) {
			List<InputType> inputList = (List) viewerInput;
			for (Iterator<?> iterator = inputList.iterator(); iterator.hasNext();) {
				InputType inputType = (InputType) iterator.next();
				setInputDataActiveStatus(false, inputType, command);
			}
		}
	}
	
	private void setInputDataActiveStatus(boolean active, InputType inputType, final CompoundCommand command) {
		inputType.setActived(active);
		IParam param = SmooksUIUtils.getInputTypeAssociatedParamType(inputType, getSmooksConfigResourceList());
		if (param != null) {
			String value = SmooksModelUtils.INPUT_ACTIVE_TYPE;
			if (!active) {
				value = SmooksModelUtils.INPUT_DEACTIVE_TYPE;
			}
			Command c = SetCommand.create(this.getEditingDomain(), param, ICorePackage.Literals.PARAM__TYPE,
					value);
			if (command != null) {
				command.append(c);
			} else {
				getEditingDomain().getCommandStack().execute(c);
			}
		}
	}


	public void sourceChange(Object model) {
		
	}
	
	private class InputDataViewerLabelProvider extends ExtentionInputLabelProvider implements ITableColorProvider {

		public Color getBackground(Object element, int columnIndex) {
			if (!isValidInputType((InputType) element)) {
				// return ColorConstants.darkGray;
			}
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			if (!isValidInputType((InputType) element)) {
				return ColorConstants.lightGray;
			}
			return null;
		}
	}
}

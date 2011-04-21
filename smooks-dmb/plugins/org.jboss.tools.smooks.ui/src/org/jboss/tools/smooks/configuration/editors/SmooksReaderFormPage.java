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
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
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
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributor;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributorFactory;
import org.jboss.tools.smooks.configuration.editors.input.InvalidInputSourceTypeException;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
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

	private TreeViewer inputModelViewer;
	private Combo readerCombo;
	private Composite readerConfigComposite;
	private ScrolledPageBook scrolledPageBook;
	private InputTaskPanelContributor sourceTypeConfigurationContributor;

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
		createInputModelViewerSection(toolkit, rightComposite);
		createReaderConfigSection(toolkit, leftComposite);

		handleReaderCombo(readerCombo);

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 13;
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 20;
		gridLayout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(gridLayout);
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
			}
			public void linkEntered(HyperlinkEvent e) {
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

		refreshInputModelView();

	}
	
	private void refreshInputModelView() {
		inputModelViewer.setInput(new ArrayList<Object>());
		inputModelViewer.refresh();
		
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

		initReaderConfigSection();

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		readerConfigComposite.setLayout(layout);
		readerConfigComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		scrolledPageBook.showPage(scrolledPageBook);
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
		
		InputTaskPanelContributorFactory taskPanelContributorFactory = getCurrentInputType().getTaskPanelContributorFactory();
		sourceTypeConfigurationContributor = taskPanelContributorFactory.newInstance(getManagedForm().getToolkit(), this, getSmooksModel());
		sourceTypeConfigurationContributor.setInputSourceConfigComposite(readerConfigComposite);
		sourceTypeConfigurationContributor.addInputSourceTypeConfigControls();
		
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
	
	protected boolean handleInputParamChange(Command command) {
		Collection<?> affectedObjects = command.getAffectedObjects();
		boolean refreshInputModel = false;
		
		for (Object affectedObject : affectedObjects) {

			if (affectedObject instanceof IParam) {
				if (InputSourceType.isValidName(((IParam)affectedObject).getName())) {
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
				for (Object deletedObject : deletedObjs) {
					if (deletedObject instanceof IParam) {
						if (InputSourceType.isValidName(((IParam)deletedObject).getName())) {
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

	protected Model<SmooksModel> getSmooksModel() {
		return getSmooksModelProvider().getSmooksModel();
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

		IGlobalParams params = getSmooksModel().getModelRoot().getParams();
		if (params == null) {
			params = ICoreFactory.eINSTANCE.createGlobalParams();
			Command addparamsCommand = SetCommand.create(getEditingDomain(), getSmooksModel().getModelRoot(),
					ICorePackage.Literals.PARAMS, params);
			if (addparamsCommand.canExecute())
				compoundCommand.append(addparamsCommand);
		}

		IParam param = SmooksUIUtils.getInputTypeParam(getSmooksModel().getModelRoot());
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
		
		if (!compoundCommand.isEmpty() && compoundCommand.canExecute()) {
			getEditingDomain().getCommandStack().execute(compoundCommand);
		}

		refreshInputModelView();
	}
	
	private Command createRemoveReaderCommand() {
		Model<SmooksModel> rlist = getSmooksModel();
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

	public void sourceChange(Object model) {
		
	}
}

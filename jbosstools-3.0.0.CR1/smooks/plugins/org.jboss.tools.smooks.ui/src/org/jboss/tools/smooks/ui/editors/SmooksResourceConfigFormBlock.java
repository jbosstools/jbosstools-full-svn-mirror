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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelBuilder;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.BeanPopulatorWarrper;
import org.jboss.tools.smooks.ui.DateTypeWarrper;
import org.jboss.tools.smooks.ui.DocumentSelectionWarrper;
import org.jboss.tools.smooks.ui.ResourceConfigWarrper;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.wizards.NewResourceConfigFactory;
import org.jboss.tools.smooks.ui.wizards.NewResourceConfigKey;
import org.jboss.tools.smooks.ui.wizards.NewResourceConfigWizard;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class SmooksResourceConfigFormBlock extends MasterDetailsBlock implements
		ISelectionChangedListener {

	protected TreeViewer dateTypeViewer;

	protected NormalSmooksModelPackage modelPackage = null;

	protected ResourceConfigType transformType = null;

	protected SmooksFormEditor parentEditor;

	protected IManagedForm managedForm;

	protected EditingDomain domain;

	private SectionPart sectionPart;

	private Button addButton;

	private Button removeButton;

	private Button upButton;

	private Button downButton;

	private Section configurationSection;

	public SmooksResourceConfigFormBlock() {
		super();
	}

	public EditingDomain getDomain() {
		return domain;
	}

	public void setDomain(EditingDomain domain) {
		this.domain = domain;
	}

	public NormalSmooksModelPackage getModelPackage() {
		return modelPackage;
	}

	public void setModelPackage(NormalSmooksModelPackage modelPackage) {
		this.modelPackage = modelPackage;
		// if (this.dateTypeViewer != null) {
		// if (this.modelPackage != null) {
		// dateTypeViewer.setInput(this.modelPackage
		// .getSmooksResourceList().getAbstractResourceConfig());
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.
	 * ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		this.managedForm = managedForm;
		FormToolkit tool = managedForm.getToolkit();
		createDataTypeGUI(parent, tool, managedForm);
		configDateTypeViewer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse
	 * .ui.forms.IManagedForm)
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.
	 * forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(NormalResourceConfigWarpper.class,
				new NormalResourceConfigDetailPage(getParentEditor(), getDomain()));
		detailsPart.registerPage(DateTypeWarrper.class, new DateTypeDetailPage(
				getParentEditor(), getDomain()));
		detailsPart.registerPage(DocumentSelectionWarrper.class,
				new DocumentResourceTypeDetailPage(getParentEditor(),
						getDomain()));
	}

	protected void configDateTypeViewer() {
		dateTypeViewer.setContentProvider(new DateTypeContentProvider());
		dateTypeViewer.setLabelProvider(new ConfigurationViewerLabelProvider());
		// don't display the first ResourceConfig element;because it will be
		// "show" with Smooks TransformData Type GUI
		dateTypeViewer.setFilters(new ViewerFilter[] { new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element == transformType)
					return false;
				if(!(element instanceof ResourceConfigType)){
					return false;
				}
				if (modelPackage != null) {
					List hidenList = modelPackage.getHidenSmooksElements();
					if(hidenList == null) return false;
					for (Iterator iterator = hidenList.iterator(); iterator
							.hasNext();) {
						Object object = (Object) iterator.next();
						if (object == element)
							return false;
					}
				}
				return true;
			}
		} });
	}

	public void initViewers(ResourceConfigType transformType) {
		this.transformType = transformType;
		if (this.getModelPackage() != null) {
			if (dateTypeViewer != null) {
				SmooksResourceListType tea = modelPackage
						.getSmooksResourceList();
				if(tea != null){
					dateTypeViewer.setInput(tea.getAbstractResourceConfig());
				}
				
			}
		} else {
			if (dateTypeViewer != null) {
				dateTypeViewer.setInput(Collections.EMPTY_LIST);
				SelectionChangedEvent event = new SelectionChangedEvent(
						dateTypeViewer, new StructuredSelection(new Object()));
				selectionChanged(event);
			}
		}
	}

	protected void createDataTypeGUI(Composite rootMainControl,
			FormToolkit tool, final IManagedForm managedForm) {
		configurationSection = tool.createSection(rootMainControl,
				Section.TITLE_BAR | Section.DESCRIPTION);
		configurationSection.setText(Messages.getString("SmooksResourceConfigFormBlock.DateType")); //$NON-NLS-1$
		sectionPart = new SectionPart(configurationSection);
		managedForm.addPart(sectionPart);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Composite dataTypeComposite = tool
				.createComposite(configurationSection);
		configurationSection.setClient(dataTypeComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		dataTypeComposite.setLayout(layout);

		Composite tableComposite = tool.createComposite(dataTypeComposite);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		tableComposite.setLayout(fillLayout);
		dateTypeViewer = new TreeViewer(tableComposite, SWT.NONE);
		dateTypeViewer.addSelectionChangedListener(this);
		gd = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.groupBorderColor);
		tool.paintBordersFor(tableComposite);

		Composite buttonComposite = tool.createComposite(dataTypeComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		buttonComposite.setLayoutData(gd);

		GridLayout buttonLayout = new GridLayout();
		buttonComposite.setLayout(buttonLayout);

		addButton = tool.createButton(buttonComposite, Messages.getString("SmooksResourceConfigFormBlock.NewButton"), SWT.NONE); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);

		removeButton = tool.createButton(buttonComposite, Messages.getString("SmooksResourceConfigFormBlock.DeleteButton"), SWT.NONE); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(gd);

		upButton = tool.createButton(buttonComposite, Messages.getString("SmooksResourceConfigFormBlock.UpButton"), SWT.NONE); //$NON-NLS-1$
		upButton.setLayoutData(gd);

		downButton = tool.createButton(buttonComposite, Messages.getString("SmooksResourceConfigFormBlock.DownButton"), SWT.NONE); //$NON-NLS-1$
		downButton.setLayoutData(gd);

		hookButtons();
	}

	public void setSectionStates(boolean state) {
		if (configurationSection != null && !configurationSection.isDisposed()) {
			configurationSection.setEnabled(state);
		}
	}

	private void hookButtons() {
		addButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				addButtonSelected();
			}

		});

		removeButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				removeButtonSelected();
			}

		});

		upButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				upButtonSelected();
			}

		});

		downButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				downButtonSelected();
			}

		});
	}

	protected void downButtonSelected() {
		if (modelPackage == null)
			return;
		IStructuredSelection selection = (IStructuredSelection) dateTypeViewer
				.getSelection();
		Object object = selection.getFirstElement();
		int index = ((List) dateTypeViewer.getInput()).indexOf(object);
		if (index >= modelPackage.getSmooksResourceList()
				.getAbstractResourceConfig().size()) {
			return;
		}
		index++;
		Command command = MoveCommand.create(domain, modelPackage
				.getSmooksResourceList(), SmooksPackage.eINSTANCE
				.getSmooksResourceListType_AbstractResourceConfig(), object,
				index);
		domain.getCommandStack().execute(command);
		dateTypeViewer.refresh();
	}

	protected void upButtonSelected() {
		if (modelPackage == null)
			return;
		IStructuredSelection selection = (IStructuredSelection) dateTypeViewer
				.getSelection();
		Object object = selection.getFirstElement();
		int index = ((List) dateTypeViewer.getInput()).indexOf(object);
		if (index <= 0) {
			return;
		}
		index--;
		Command command = MoveCommand.create(domain, modelPackage
				.getSmooksResourceList(), SmooksPackage.eINSTANCE
				.getSmooksResourceListType_AbstractResourceConfig(), object,
				index);
		domain.getCommandStack().execute(command);
		dateTypeViewer.refresh();
	}

	protected void removeButtonSelected() {
		if (modelPackage == null)
			return;
		IStructuredSelection selection = (IStructuredSelection) dateTypeViewer
				.getSelection();
		List selections = selection.toList();
		CompoundCommand command = new CompoundCommand();
		for (Iterator iterator = selections.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			Command command1 = RemoveCommand
					.create(
							domain,
							modelPackage.getSmooksResourceList(),
							SmooksPackage.eINSTANCE
									.getSmooksResourceListType_AbstractResourceConfig(),
							object);
			// ((List) dateTypeViewer.getInput()).remove(object);
			command.append(command1);
		}
		domain.getCommandStack().execute(command);
		dateTypeViewer.refresh();
	}

	protected void addButtonSelected() {
		if (modelPackage == null)
			return;
		NewResourceConfigWizard wizard = new NewResourceConfigWizard();
		WizardDialog dialog = new WizardDialog(parentEditor.getSite()
				.getShell(), wizard);
		if (dialog.open() == Dialog.OK) {
			NewResourceConfigKey key = wizard.getSelectedKey();
			ResourceConfigType config = NewResourceConfigFactory.getInstance()
					.createNewResourceConfig(key);
			if (config != null) {
				// ((List) dateTypeViewer.getInput()).add(config);
				Command command = AddCommand.create(domain, modelPackage
						.getSmooksResourceList(), SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
						config);
				try{
				domain.getCommandStack().execute(command);
				}catch(Exception e){
					e.printStackTrace();
				}
				dateTypeViewer.refresh();
			}
		}
	}

	protected SmooksFormEditor getParentEditor() {
		return parentEditor;
	}

	public void setParentEditor(SmooksFormEditor parentEditor) {
		this.parentEditor = parentEditor;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (selection.isEmpty())
			return;
		Object obj = selection.getFirstElement();
		if (obj instanceof ResourceConfigType) {
			ResourceConfigWarrper warrper = ResourceConfigWarrperFactory
					.createResourceConfigWarrper((ResourceConfigType) obj);
			if (warrper != null) {
				selection = new StructuredSelection(warrper);
				managedForm.fireSelectionChanged(sectionPart, selection);
				return;
			}
		}
		managedForm.fireSelectionChanged(sectionPart, event.getSelection());
	}

}

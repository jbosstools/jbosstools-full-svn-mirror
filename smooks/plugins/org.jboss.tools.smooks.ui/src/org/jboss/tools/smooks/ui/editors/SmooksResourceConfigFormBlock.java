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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.ui.BeanPopulatorWarrper;
import org.jboss.tools.smooks.ui.DateTypeWarrper;
import org.jboss.tools.smooks.ui.ResourceConfigWarrper;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.milyn.xsd.smooks.ResourceConfigType;
import org.milyn.xsd.smooks.impl.ResourceConfigTypeImpl;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class SmooksResourceConfigFormBlock extends MasterDetailsBlock implements
		ISelectionChangedListener {

	TreeViewer dateTypeViewer;

	NormalSmooksModelPackage modelPackage = null;

	SmooksFormEditor parentEditor;

	IManagedForm managedForm;

	protected EditingDomain domain;

	private SectionPart sectionPart;

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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm,
	 *      org.eclipse.swt.widgets.Composite)
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
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(BeanPopulatorWarrper.class,
				new BeanPopulatorDetailPage(getParentEditor(), getDomain()));
		detailsPart.registerPage(DateTypeWarrper.class,
				new DateTypeDetailPage(getParentEditor(), getDomain()));
	}

	protected void configDateTypeViewer() {
		dateTypeViewer.setContentProvider(new DateTypeContentProvider());
	}

	public void initViewers() {
		if (this.getModelPackage() != null) {
			List all = new ArrayList();
			List list = modelPackage.getBeanPopulatorResourceConfigList();
			if (list != null) {
				all.addAll(list);
			}
			List dl = modelPackage.getDateResourceConfigList();
			if (dl != null) {
				all.addAll(dl);
			}

			dateTypeViewer.setInput(all);
		}
	}

	protected void createDataTypeGUI(Composite rootMainControl,
			FormToolkit tool, final IManagedForm managedForm) {
		Section section = tool.createSection(rootMainControl, Section.TITLE_BAR
				| Section.DESCRIPTION);
		section.setText("Data Type");
		sectionPart = new SectionPart(section);
		managedForm.addPart(sectionPart);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Composite dataTypeComposite = tool.createComposite(section);
		section.setClient(dataTypeComposite);
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
		buttonComposite.setLayoutData(gd);

		GridLayout buttonLayout = new GridLayout();
		buttonComposite.setLayout(buttonLayout);

		Button addButton = tool.createButton(buttonComposite, "New", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		Button removeButton = tool.createButton(buttonComposite, "Delete",
				SWT.NONE);
		removeButton.setLayoutData(gd);

		Button upButton = tool.createButton(buttonComposite, "Up", SWT.NONE);
		upButton.setLayoutData(gd);

		Button downButton = tool
				.createButton(buttonComposite, "Down", SWT.NONE);
		downButton.setLayoutData(gd);
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
				managedForm.fireSelectionChanged(sectionPart,selection);
				return;
			}
		}
		managedForm.fireSelectionChanged(sectionPart, event.getSelection());
	}

}

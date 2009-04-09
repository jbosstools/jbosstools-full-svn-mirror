/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.BasicFeatureMapEntry;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Dart Peng (dpeng@redhat.com)
 *         <p>
 *         Date Apr 1, 2009
 */
public class SmooksMasterDetailBlock extends MasterDetailsBlock implements IMenuListener,
		ISelectionChangedListener {

	private Section configurationSection;
	private SectionPart sectionPart;
	private TreeViewer smooksTreeViewer;
	private Button addButton;
	private Button removeButton;
	private Button upButton;
	private Button downButton;

	private AdapterFactoryEditingDomain editingDomain = null;

	private FormEditor formEditor;

	public SmooksMasterDetailBlock(FormEditor formEditor, AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
		this.formEditor = formEditor;
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
		FormToolkit tool = managedForm.getToolkit();
		createSmooksTreeViewer(tool, managedForm, parent);
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
		SmooksStuffDetailsPageProvider provider = new SmooksStuffDetailsPageProvider();
		detailsPart.setPageProvider(provider);
		// provider.registePage(ResourceType.class, new
		// SmooksStuffPropertyDetailPage());
		detailsPart.registerPage(EObject.class, new SmooksStuffPropertyDetailPage(
				(SmooksMultiFormEditor) this.formEditor));
	}

	protected void createSmooksTreeViewer(FormToolkit tool, final IManagedForm managedForm,
			Composite rootMainControl) {
		configurationSection = tool.createSection(rootMainControl, Section.TITLE_BAR | Section.DESCRIPTION);
		configurationSection
				.setDescription("Define Smooks stuffes for configuration file in the following section.");
		configurationSection.setText("All Smooks Stuffes");
		sectionPart = new SectionPart(configurationSection);
		managedForm.addPart(sectionPart);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Composite dataTypeComposite = tool.createComposite(configurationSection);
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
		smooksTreeViewer = new TreeViewer(tableComposite, SWT.NONE);
		smooksTreeViewer.addSelectionChangedListener(this);
		smooksTreeViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain
				.getAdapterFactory()));
		smooksTreeViewer.setLabelProvider(new AdapterFactoryLabelProvider(editingDomain.getAdapterFactory()));
		smooksTreeViewer.setFilters(new ViewerFilter[] { new TextEObjectModelFilter() });
		Object smooksModel = ((SmooksMultiFormEditor) this.formEditor).getSmooksModel();
		if (smooksModel != null) {
			smooksTreeViewer.setInput(smooksModel);
		}
		createMenuForViewer(smooksTreeViewer);
		formEditor.getSite().setSelectionProvider(smooksTreeViewer);

		if (formEditor.getEditorSite().getActionBarContributor() instanceof ISelectionChangedListener) {
			smooksTreeViewer.addSelectionChangedListener((ISelectionChangedListener) formEditor
					.getEditorSite().getActionBarContributor());
		}
		smooksTreeViewer.addSelectionChangedListener(this);

		gd = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(gd);
		tool.paintBordersFor(tableComposite);

		Composite buttonComposite = tool.createComposite(dataTypeComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		buttonComposite.setLayoutData(gd);

		GridLayout buttonLayout = new GridLayout();
		buttonComposite.setLayout(buttonLayout);

		addButton = tool.createButton(buttonComposite, "New", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);

		removeButton = tool.createButton(buttonComposite, "Remove", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(gd);

		upButton = tool.createButton(buttonComposite, "Up", SWT.NONE);
		upButton.setLayoutData(gd);

		downButton = tool.createButton(buttonComposite, "Down", SWT.NONE);
		downButton.setLayoutData(gd);
	}

	private void createMenuForViewer(TreeViewer smooksTreeViewer2) {
		MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);

		Menu menu = contextMenu.createContextMenu(smooksTreeViewer2.getControl());
		smooksTreeViewer2.getControl().setMenu(menu);
		formEditor.getSite().registerContextMenu(contextMenu,
				new UnwrappingSelectionProvider(smooksTreeViewer2));
	}

	public TreeViewer getSmooksTreeViewer() {
		return smooksTreeViewer;
	}

	public void setSmooksTreeViewer(TreeViewer smooksTreeViewer) {
		this.smooksTreeViewer = smooksTreeViewer;
	}

	public void menuAboutToShow(IMenuManager menuManager) {
		((IMenuListener) formEditor.getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
	}

	private class TextEObjectModelFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			Object obj = element;
			while (obj != null && obj instanceof IWrapperItemProvider) {
				obj = ((IWrapperItemProvider) obj).getValue();
			}
			if (obj instanceof BasicFeatureMapEntry) {
				EStructuralFeature sf = ((BasicFeatureMapEntry) obj).getEStructuralFeature();
				if (sf.equals(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT)
						|| sf.equals(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA)) {
					return false;
				}
			}
			return true;
		}
	}

	public void setSmooksModel(Object model) {
		if (model != null) {
			smooksTreeViewer.setInput(model);
		} else {
			smooksTreeViewer.setInput(new Object());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if (sectionPart != null && sectionPart.getManagedForm() != null) {
			Object[] objs = ((IStructuredSelection) event.getSelection()).toArray();
			List<Object> objList = new ArrayList<Object>();
			for (int i = 0; i < objs.length; i++) {
				Object obj = objs[i];
				obj = AdapterFactoryEditingDomain.unwrap(obj);
				objList.add(obj);
			}
			if (objList.isEmpty()) {
				sectionPart.getManagedForm().fireSelectionChanged(sectionPart, event.getSelection());
			} else {
				sectionPart.getManagedForm().fireSelectionChanged(sectionPart,
						new StructuredSelection(objList.toArray()));
			}
		}
	}

}

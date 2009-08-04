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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.BasicFeatureMapEntry;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.wizards.NewSmooksElementWizard;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;

/**
 * @author Dart Peng (dpeng@redhat.com)
 *         <p>
 *         Date Apr 1, 2009
 */
public class SmooksMasterDetailBlock extends MasterDetailsBlock implements IMenuListener, ISelectionChangedListener {

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

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		sashForm.setLayoutData(gd);
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

	protected void refreshSmooksTreeViewer() {
		if (smooksTreeViewer.getControl().isDisposed()) {
			return;
		}
		smooksTreeViewer.refresh();
	}

	protected void createSmooksTreeViewer(FormToolkit tool, final IManagedForm managedForm, Composite rootMainControl) {
		configurationSection = tool.createSection(rootMainControl, Section.TITLE_BAR);
		configurationSection.setDescription("Define Smooks elements for configuration file in the following section.");
		configurationSection.setText("Message Filtering Resources");
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

		smooksTreeViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));

		smooksTreeViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
				.getAdapterFactory()) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#
			 * getText(java.lang.Object)
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

		smooksTreeViewer.setComparer(new IElementComparer() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IElementComparer#equals(java.lang
			 * .Object, java.lang.Object)
			 */
			public boolean equals(Object a, Object b) {
				Object elementA = AdapterFactoryEditingDomain.unwrap(a);
				Object elementB = AdapterFactoryEditingDomain.unwrap(b);
				if (elementA == elementB) {
					return true;
				}
				return elementA == null ? elementB == null : elementA.equals(elementB);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IElementComparer#hashCode(java.
			 * lang.Object)
			 */
			public int hashCode(Object element) {
				return element.hashCode();
			}

		});

		smooksTreeViewer.setFilters(new ViewerFilter[] { new TextEObjectModelFilter() });
		Object smooksModel = ((ISmooksModelProvider) this.formEditor).getSmooksModel();
		if (smooksModel != null) {
			setTreeViewerModel(smooksModel);
		}
		createMenuForViewer(smooksTreeViewer);
		formEditor.getSite().setSelectionProvider(smooksTreeViewer);

		if (formEditor.getEditorSite().getActionBarContributor() instanceof ISelectionChangedListener) {
			smooksTreeViewer.addSelectionChangedListener((ISelectionChangedListener) formEditor.getEditorSite()
					.getActionBarContributor());
		}
		smooksTreeViewer.addSelectionChangedListener(this);
		tableComposite.setBackground(new Color(null, 128, 128, 128));

		gd = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(gd);
		tool.paintBordersFor(tableComposite);

		Composite buttonComposite = tool.createComposite(dataTypeComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		buttonComposite.setLayoutData(gd);

		GridLayout buttonLayout = new GridLayout();
		buttonComposite.setLayout(buttonLayout);

		addButton = tool.createButton(buttonComposite, "Add...", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);

		removeButton = tool.createButton(buttonComposite, "Remove", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(gd);
		
		Composite  sc= tool.createComposite(buttonComposite);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 40;
		sc.setLayoutData(gd);

		upButton = tool.createButton(buttonComposite, "Up", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		upButton.setLayoutData(gd);

		downButton = tool.createButton(buttonComposite, "Down", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		downButton.setLayoutData(gd);
		// don't display button area
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 70;
		buttonComposite.setLayoutData(gd);
		// buttonComposite.setVisible(false);

		hookButtons();
	}

	private void setTreeViewerModel(Object smooksModel) {
		boolean seted = false;
		if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
			smooksTreeViewer.setInput(((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel)
					.getSmooksResourceList());
			seted = true;
		}
		if (smooksModel instanceof DocumentRoot) {
			smooksTreeViewer.setInput(((DocumentRoot) smooksModel).getSmooksResourceList());
			seted = true;
		}
		if (!seted) {
			smooksTreeViewer.setInput(smooksModel);
		}
	}

	private EObject getTreeViewerInput() {
		EObject smooksModel = ((ISmooksModelProvider) this.formEditor).getSmooksModel();
		if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
			return ((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel).getSmooksResourceList();
		}
		if (smooksModel instanceof DocumentRoot) {
			return (((DocumentRoot) smooksModel).getSmooksResourceList());
		}
		return null;
	}

	protected List<?> getViewerSelections() {
		IStructuredSelection selections = (IStructuredSelection) smooksTreeViewer.getSelection();
		if (selections == null)
			return null;
		return selections.toList();
		// List<Object> nl = new ArrayList<Object>();
		// for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
		// Object object = (Object) iterator.next();
		// // object = AdapterFactoryEditingDomain.unwrap(object);
		// nl.add(object);
		// }
		// return l;
	}

	private void hookButtons() {
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				NewSmooksElementWizard wizard = new NewSmooksElementWizard(editingDomain, getTreeViewerInput());
				WizardDialog dialog = new WizardDialog(formEditor.getSite().getShell(), wizard);
				dialog.open();
			}

		});

		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				List<?> list = getViewerSelections();
				CompoundCommand ccommand = new CompoundCommand();
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					Command command = DeleteCommand.create(editingDomain, object);
					if (command.canExecute()) {
						ccommand.append(command);
					}
				}

				editingDomain.getCommandStack().execute(ccommand);
			}
		});

		downButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				List<?> list = getViewerSelections();
				if (list.size() == 1) {
					Object obj = list.get(0);
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(editingDomain, parent, null, obj, index + 1);
					editingDomain.getCommandStack().execute(command);
				}
			}
		});

		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				List<?> list = getViewerSelections();
				if (list.size() == 1) {
					Object obj = list.get(0);
					EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
					EObject parent = v.eContainer();
					int index = parent.eContents().indexOf(v);
					Command command = MoveCommand.create(editingDomain, parent, null, obj, index - 1);
					editingDomain.getCommandStack().execute(command);
				}
			}
		});
	}

	private void createMenuForViewer(TreeViewer smooksTreeViewer2) {
		MenuManager contextMenu = new MenuManager();
		// contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);

		Menu menu = contextMenu.createContextMenu(smooksTreeViewer2.getControl());
		smooksTreeViewer2.getControl().setMenu(menu);
		// formEditor.getSite().registerContextMenu(contextMenu,
		// new UnwrappingSelectionProvider(smooksTreeViewer2));
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
			obj = AdapterFactoryEditingDomain.unwrap(obj);
			if (obj instanceof BasicFeatureMapEntry) {
				EStructuralFeature sf = ((BasicFeatureMapEntry) obj).getEStructuralFeature();
				if (sf.equals(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT)
						|| sf.equals(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA)) {
					return false;
				}
			} else {
				if (obj.getClass() == String.class) {
					return false;
				}
			}
			return true;
		}
	}

	public void setSmooksModel(Object model) {
		if (model != null) {
			setTreeViewerModel(model);
			// smooksTreeViewer.setInput(model);
		} else {
			smooksTreeViewer.setInput(new Object());
		}
	}

	/**
	 * 
	 * @param selections
	 */
	protected void updateButtons(List<?> selections) {
		removeButton.setEnabled(true);
		upButton.setEnabled(true);
		downButton.setEnabled(true);
		List<?> list = getViewerSelections();
		if (list.size() == 1) {
			Object obj = list.get(0);
			EObject v = (EObject) AdapterFactoryEditingDomain.unwrap(obj);
			EObject parent = v.eContainer();
			int index = parent.eContents().indexOf(v);

			Command upCommand = MoveCommand.create(editingDomain, parent, null, obj, index - 1);
			upButton.setEnabled(upCommand.canExecute());

			Command downCommand = MoveCommand.create(editingDomain, parent, null, obj, index + 1);
			downButton.setEnabled(downCommand.canExecute());
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
			updateButtons(objList);
		}
	}

}

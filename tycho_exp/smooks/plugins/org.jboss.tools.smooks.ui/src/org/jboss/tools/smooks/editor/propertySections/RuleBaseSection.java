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
package org.jboss.tools.smooks.editor.propertySections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.actions.AddSmooksResourceAction;
import org.jboss.tools.smooks.configuration.editors.ModelPanelCreator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.rules10.RuleBase;
import org.jboss.tools.smooks.model.rules10.RuleBasesType;
import org.jboss.tools.smooks.model.rules10.Rules10Factory;
import org.jboss.tools.smooks.model.rules10.Rules10Package;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class RuleBaseSection extends AbstractSmooksPropertySection {

	private Composite controlComposite;
	private TreeViewer ruleBasesViewer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls
	 * (org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = aTabbedPropertySheetPage.getWidgetFactory();

		Section section = createRootSection(factory, parent);
		section.setText("Rule Bases Setting");
		SashForm sashForm = new SashForm(section, SWT.NONE);
		section.setClient(sashForm);

		createRuleViewer(factory, sashForm);

		controlComposite = factory.createComposite(sashForm, SWT.NONE);
		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 2;
		controlComposite.setLayout(gl1);

		// createValidationRulesGUIContents(model, provider, part, factory,
		// controlComposite);
	}

	private void createRuleViewer(TabbedPropertySheetWidgetFactory factory, Composite sashForm) {
		ISmooksModelProvider provider = getSmooksModelProvider();

		GridData gd = new GridData(GridData.FILL_BOTH);

		Composite viewerComposite = factory.createComposite(sashForm, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		viewerComposite.setLayout(gridLayout);

		Composite viewerContianer = factory.createComposite(viewerComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		viewerContianer.setLayoutData(gd);
		viewerContianer.setBackground(factory.getColors().getBorderColor());

		FillLayout layout = new FillLayout();
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		viewerContianer.setLayout(layout);

		ruleBasesViewer = new TreeViewer(viewerContianer, SWT.NONE);
		if (provider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();

			ruleBasesViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));

			ruleBasesViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
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
					if (obj instanceof EObject) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));
		}

		ruleBasesViewer.setFilters(new ViewerFilter[] { new RuleTypeFilter() });

		ruleBasesViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object model = selection.getFirstElement();
				model = AdapterFactoryEditingDomain.unwrap(model);
				ISmooksModelProvider provider = getSmooksModelProvider();
				IEditorPart part = getEditorPart();
				diposeControlComposite();
				createValidationRulesGUIContents(model, provider, part, getWidgetFactory(), controlComposite);
				if (controlComposite != null) {
					controlComposite.getParent().getParent().layout();
				}
			}
		});

		final MenuManager manager = new MenuManager("Add Rule Base");
		final MenuManager manager1 = new MenuManager("Add Child");
		manager.add(manager1);
		manager1.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager1) {
				manager1.removeAll();
				initMenu(manager1);
			}
		});
		manager1.add(new Action("test") {

		});
		Menu menu = manager.createContextMenu(ruleBasesViewer.getControl());
		ruleBasesViewer.getControl().setMenu(menu);

		Composite buttonComposite = factory.createComposite(viewerComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		buttonComposite.setLayoutData(gd);
		GridLayout gl2 = new GridLayout();
		buttonComposite.setLayout(gl2);

		Button newRuleBasesButton = factory.createButton(buttonComposite, "Add", SWT.NONE);
		newRuleBasesButton.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {

				RuleBasesType rule = Rules10Factory.eINSTANCE.createRuleBasesType();
				ISmooksModelProvider provider = getSmooksModelProvider();

				if (provider != null) {
					SmooksResourceListType listType = SmooksUIUtils.getSmooks11ResourceListType(provider
							.getSmooksModel());
					if (listType != null) {
						EditingDomain domain = provider.getEditingDomain();
						Object obj = FeatureMapUtil.createEntry(
								Rules10Package.Literals.RULES10_DOCUMENT_ROOT__RULE_BASES, rule);
						Command command = AddCommand.create(domain, listType,
								SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, obj);
						domain.getCommandStack().execute(command);
					}
				}

				ruleBasesViewer.refresh();

				super.widgetSelected(e);
			}

		});
		Button deleteButton = factory.createButton(buttonComposite, "Delete", SWT.NONE);
		deleteButton.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) ruleBasesViewer.getSelection();
				List<?> selectedRules = selection.toList();
				ISmooksModelProvider provider = getSmooksModelProvider();
				if (provider != null) {
					EditingDomain domain = provider.getEditingDomain();
					Command command = RemoveCommand.create(domain, selectedRules);
					domain.getCommandStack().execute(command);
				}
				super.widgetSelected(e);
			}

		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		newRuleBasesButton.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
	}

	private void initMenu(IMenuManager manager) {
		Object model = null;
		IStructuredSelection selection = (IStructuredSelection) ruleBasesViewer.getSelection();
		if (selection == null)
			return;
		model = selection.getFirstElement();
		model = AdapterFactoryEditingDomain.unwrap(model);
		ISmooksModelProvider provider = getSmooksModelProvider();

		if (provider != null) {
			Collection<?> newChildDescriptors = provider.getEditingDomain().getNewChildDescriptors(model, null);
			Collection<IAction> actions = generateCreateChildActions(newChildDescriptors, selection);
			for (Iterator<?> iterator = actions.iterator(); iterator.hasNext();) {
				IAction action = (IAction) iterator.next();
				manager.add(action);
			}
		}
	}

	protected Collection<IAction> generateCreateChildActions(Collection<?> descriptors, ISelection selection) {
		Collection<IAction> actions = new ArrayList<IAction>();
		if (descriptors != null) {
			for (Object descriptor : descriptors) {
				actions.add(new AddSmooksResourceAction(getEditorPart(), selection, descriptor));
			}
		}
		return actions;
	}

	private void initRuleBasesViewer() {
		ISmooksModelProvider provider = getSmooksModelProvider();
		if (provider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();

			ruleBasesViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));

			ruleBasesViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
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
					if (obj instanceof EObject) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));
			SmooksResourceListType listType = SmooksUIUtils.getSmooks11ResourceListType(provider.getSmooksModel());
			if (listType != null) {
				ruleBasesViewer.setInput(listType);
			}
		}
	}

	protected void createValidationRulesGUIContents(Object model, ISmooksModelProvider provider, IEditorPart part,
			FormToolkit factory, Composite controlComposite) {
		ModelPanelCreator creator = new ModelPanelCreator();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model != null && model instanceof EObject && provider != null && part != null) {
			AdapterFactoryEditingDomain domain = (AdapterFactoryEditingDomain) provider.getEditingDomain();
			IItemPropertySource itemPropertySource = (IItemPropertySource) domain.getAdapterFactory().adapt(model,
					IItemPropertySource.class);
			if (itemPropertySource != null) {
				creator
						.createModelPanel((EObject) model, factory, controlComposite, itemPropertySource, provider,
								part);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.editor.propertySections.AbstractSmooksPropertySection
	 * #refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		diposeControlComposite();
		initRuleBasesViewer();
	}

	protected void diposeControlComposite() {
		if (controlComposite != null) {
			Control[] children = controlComposite.getChildren();
			for (int i = 0; i < children.length; i++) {
				Control c = children[i];
				c.dispose();
			}
		}
	}

//	private String getCurrentModelSelectorPath() {
//		Object model = getPresentSelectedModel();
//		if (model instanceof IXMLStructuredObject) {
//			return SmooksUIUtils.generateFullPath((IXMLStructuredObject) model, "/");
//		}
//		return null;
//
//	}

	private class RuleTypeFilter extends ViewerFilter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers
		 * .Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			element = AdapterFactoryEditingDomain.unwrap(element);
			if (element instanceof RuleBase || element instanceof RuleBasesType) {
				return true;
			}
			return false;
		}
	}

}

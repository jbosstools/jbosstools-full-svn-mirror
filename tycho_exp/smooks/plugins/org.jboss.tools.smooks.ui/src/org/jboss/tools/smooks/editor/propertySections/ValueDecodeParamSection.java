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

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.ModelPanelCreator;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.javabean.DecodeParamType;
import org.jboss.tools.smooks.model.javabean.JavabeanFactory;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Factory;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 * 
 */
public class ValueDecodeParamSection extends AbstractSmooksPropertySection {

	private Composite controlComposite;
	private TableViewer paramterViewer;

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
		section.setText("Validation Rules Setting");

		controlComposite = factory.createComposite(section, SWT.NONE);

		controlComposite.setLayout(new FillLayout());

		section.setClient(controlComposite);

		createDecodeParamViewer(factory, controlComposite);
	}

	private void createDecodeParamViewer(TabbedPropertySheetWidgetFactory factory, Composite sashForm) {
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

		paramterViewer = new TableViewer(viewerContianer, SWT.FULL_SELECTION);
		if (provider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();

			paramterViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));

			paramterViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
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

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
				 * #getColumnText(java.lang.Object, int)
				 */
				@Override
				public String getColumnText(Object object, int columnIndex) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (columnIndex == 0) {
						if (obj instanceof DecodeParamType) {
							String name = ((DecodeParamType) obj).getName();
							if (name == null)
								name = "";
							return name;
						}
						if (obj instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
							String name = ((org.jboss.tools.smooks.model.javabean12.DecodeParamType) obj).getName();
							if (name == null)
								name = "";
							return name;
						}
					}

					if (columnIndex == 1) {
						if (obj instanceof DecodeParamType) {
							String value = ((DecodeParamType) obj).getValue();
							if (value == null)
								value = "";
							return value;
						}
						if (obj instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
							String name = ((org.jboss.tools.smooks.model.javabean12.DecodeParamType) obj).getValue();
							if (name == null)
								name = "";
							return name;
						}
					}
					return super.getColumnText(object, columnIndex);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));
		}

		paramterViewer.setFilters(new ViewerFilter[] { new DecodeParamTypeFilter() });

		TableColumn nameColumn = new TableColumn(paramterViewer.getTable(), SWT.NONE);
		nameColumn.setWidth(150);
		nameColumn.setText("Name");
		TableColumn valueColumn = new TableColumn(paramterViewer.getTable(), SWT.NONE);
		valueColumn.setWidth(150);
		valueColumn.setText("Value");

		paramterViewer.setCellEditors(new CellEditor[] { new TextCellEditor(paramterViewer.getTable()),
				new TextCellEditor(paramterViewer.getTable()) });
		paramterViewer.setColumnProperties(new String[] { "name", "value" });

		paramterViewer.setCellModifier(new ICellModifier() {

			public void modify(Object element, String property, Object value) {
				if (element instanceof TableItem) {
					element = ((TableItem) element).getData();
					element = AdapterFactoryEditingDomain.unwrap(element);
					if (element == null)
						return;
					EStructuralFeature feature = null;
					if (property.equals("name")) {
						if (element instanceof DecodeParamType) {
							feature = JavabeanPackage.Literals.DECODE_PARAM_TYPE__NAME;
						}
						if (element instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
							feature = Javabean12Package.Literals.DECODE_PARAM_TYPE__NAME;
						}
					}
					if (property.equals("value")) {
						if (element instanceof DecodeParamType) {
							feature = JavabeanPackage.Literals.DECODE_PARAM_TYPE__VALUE;
						}
						if (element instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
							feature = Javabean12Package.Literals.DECODE_PARAM_TYPE__VALUE;
						}
					}

					if (feature != null) {
						Command c = SetCommand.create(getSmooksModelProvider().getEditingDomain(), element, feature,
								value);
						getSmooksModelProvider().getEditingDomain().getCommandStack().execute(c);
					}
				}
			}

			public Object getValue(Object element, String property) {
				element = AdapterFactoryEditingDomain.unwrap(element);
				if (property.equals("name")) {
					if (element instanceof DecodeParamType) {
						String name = ((DecodeParamType) element).getName();
						if (name == null)
							name = "";
						return name;
					}
					if (element instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
						String name = ((org.jboss.tools.smooks.model.javabean12.DecodeParamType) element).getName();
						if (name == null)
							name = "";
						return name;
					}
				}
				if (property.equals("value")) {
					if (element instanceof DecodeParamType) {
						String value = ((DecodeParamType) element).getValue();
						if (value == null)
							value = "";
						return value;
					}
					if (element instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
						String value = ((org.jboss.tools.smooks.model.javabean12.DecodeParamType) element).getValue();
						if (value == null)
							value = "";
						return value;
					}
				}
				return null;
			}

			public boolean canModify(Object element, String property) {
				return true;
			}
		});

		paramterViewer.getTable().setHeaderVisible(true);
		paramterViewer.getTable().setLinesVisible(true);

		Composite buttonComposite = factory.createComposite(viewerComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 130;
		buttonComposite.setLayoutData(gd);
		GridLayout gl2 = new GridLayout();
		buttonComposite.setLayout(gl2);

		Button newRuleButton = factory.createButton(buttonComposite, "New Parameter", SWT.NONE);
		newRuleButton.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object model = null;
				EStructuralFeature feature = null;
				Object element = getPresentSelectedModel();
				element = AdapterFactoryEditingDomain.unwrap(element);
				if (element == null)
					return;
				if (element instanceof ValueType) {
					model = JavabeanFactory.eINSTANCE.createDecodeParamType();
					((DecodeParamType) model).setName("<null>");
					((DecodeParamType) model).setValue("<null>");
					feature = JavabeanPackage.Literals.VALUE_TYPE__DECODE_PARAM;
				}
				if (element instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
					model = Javabean12Factory.eINSTANCE.createDecodeParamType();
					((org.jboss.tools.smooks.model.javabean12.DecodeParamType) model).setName("<null>");
					((org.jboss.tools.smooks.model.javabean12.DecodeParamType) model).setValue("<null>");
					feature = Javabean12Package.Literals.VALUE_TYPE__DECODE_PARAM;
				}
				if (model != null && feature != null && element != null) {
					EditingDomain domain = getSmooksModelProvider().getEditingDomain();
					Command command = AddCommand.create(domain, element, feature, model);
					domain.getCommandStack().execute(command);
					paramterViewer.refresh();
				}

				super.widgetSelected(e);
			}

		});
		Button deleteRuleButton = factory.createButton(buttonComposite, "Delete", SWT.NONE);
		deleteRuleButton.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) paramterViewer.getSelection();
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
		newRuleButton.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteRuleButton.setLayoutData(gd);
	}

	private void initDecodeParamViewer() {
		ISmooksModelProvider provider = getSmooksModelProvider();
		if (provider != null) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();

			paramterViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));

			paramterViewer.setLabelProvider(new DecodeParamTypeLabelProvider(new AdapterFactoryLabelProvider(
					editingDomain.getAdapterFactory())));

			Object model = getPresentSelectedModel();
			model = AdapterFactoryEditingDomain.unwrap(model);
			if (model != null) {
				paramterViewer.setInput(model);
			}
		}
	}

	protected void createDecodeParamGUIContents(Object model, ISmooksModelProvider provider, IEditorPart part,
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
		initDecodeParamViewer();
	}

	private class DecodeParamTypeLabelProvider extends LabelProvider implements ITableLabelProvider {
		private AdapterFactoryLabelProvider labelProvider = null;

		public DecodeParamTypeLabelProvider(AdapterFactoryLabelProvider labelProvider) {
			this.labelProvider = labelProvider;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			if (labelProvider != null && columnIndex == 0) {
				return labelProvider.getImage(element);
			}
			return null;
		}

		public String getColumnText(Object object, int columnIndex) {
			Object obj = AdapterFactoryEditingDomain.unwrap(object);
			if (columnIndex == 0) {
				if (obj instanceof DecodeParamType) {
					String name = ((DecodeParamType) obj).getName();
					if (name == null)
						name = "";
					return name;
				}
				if (obj instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
					String name = ((org.jboss.tools.smooks.model.javabean12.DecodeParamType) obj).getName();
					if (name == null)
						name = "";
					return name;
				}
			}

			if (columnIndex == 1) {
				if (obj instanceof DecodeParamType) {
					String value = ((DecodeParamType) obj).getValue();
					if (value == null)
						value = "";
					return value;
				}
				if (obj instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
					String name = ((org.jboss.tools.smooks.model.javabean12.DecodeParamType) obj).getValue();
					if (name == null)
						name = "";
					return name;
				}
			}
			return "";
		}

	}

	private class DecodeParamTypeFilter extends ViewerFilter {

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
			if (element instanceof DecodeParamType
					|| element instanceof org.jboss.tools.smooks.model.javabean12.DecodeParamType) {
				return true;
			}
			return false;
		}
	}

}

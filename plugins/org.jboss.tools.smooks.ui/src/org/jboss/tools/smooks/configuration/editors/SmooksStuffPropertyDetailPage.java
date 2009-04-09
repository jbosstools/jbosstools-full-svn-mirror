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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;

/**
 * 
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 7, 2009
 */
public class SmooksStuffPropertyDetailPage implements IDetailsPage {
	FormToolkit formToolkit = null;
	private IManagedForm managedForm;
	private ISelection selection;
	private IFormPart formPart;
	private Section section;
	private SmooksMultiFormEditor formEditor;
	private AdapterFactoryLabelProvider labelProvider = null;
	private AdapterFactoryEditingDomain editingDomain = null;
	private IItemPropertySource itemPropertySource = null;

	private Object oldModel = null;

	private boolean isStale = false;
	private Composite propertyMainComposite;
	private Composite propertyComposite;

	public SmooksStuffPropertyDetailPage(SmooksMultiFormEditor formEditor) {
		super();
		this.formEditor = formEditor;
		editingDomain = (AdapterFactoryEditingDomain) formEditor.getEditingDomain();
		labelProvider = new AdapterFactoryLabelProvider(editingDomain.getAdapterFactory());
	}

	public void createContents(Composite parent) {
		parent.setLayout(new FillLayout());
		section = formToolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR);

		Composite client = formToolkit.createComposite(section);
		section.setLayout(new FillLayout());
		section.setClient(client);
		createSectionContents(client);
	}

	private void createSectionContents(Composite client) {
		client.setLayout(new FillLayout());
		propertyMainComposite = new Composite(client, SWT.NONE);
		propertyMainComposite.setLayout(new FillLayout());
		propertyComposite = new Composite(propertyMainComposite, SWT.NONE);
	}

	/**
	 * 
	 * @param propertyComposite
	 */
	protected void createPropertyComposite(Composite propertyComposite) {
		try {
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			propertyComposite.setLayout(layout);
			IPropertyUICreator creator = PropertyUICreatorManager.getInstance().getPropertyUICreator(
					getModel());
			List<IItemPropertyDescriptor> propertyDes = itemPropertySource.getPropertyDescriptors(getModel());
			for (Iterator<IItemPropertyDescriptor> iterator = propertyDes.iterator(); iterator.hasNext();) {
				final IItemPropertyDescriptor itemPropertyDescriptor = (IItemPropertyDescriptor) iterator
						.next();
				EAttribute feature = (EAttribute) itemPropertyDescriptor.getFeature(getModel());
				boolean createDefault = true;
				if (creator != null) {
					Composite composite = creator.createPropertyUI(formToolkit, propertyComposite,
							itemPropertyDescriptor, getModel(), feature);
					if (composite != null) {
						createDefault = false;
					}
				}
				if (createDefault) {
					EClassifier typeClazz = feature.getEType();
					if (typeClazz instanceof EEnum) {
						createEnumFieldEditor(propertyComposite, (EEnum) typeClazz, formToolkit,
								itemPropertyDescriptor);
					}
					if (typeClazz.getInstanceClass() == String.class) {
						createStringFieldEditor(propertyComposite, formToolkit, itemPropertyDescriptor);
					}
					if (typeClazz.getInstanceClass() == Boolean.class
							|| typeClazz.getInstanceClass() == boolean.class) {
						createBooleanFieldEditor(propertyComposite, formToolkit, itemPropertyDescriptor);
					}
					if (typeClazz.getInstanceClass() == Integer.class
							|| typeClazz.getInstanceClass() == int.class) {
						createIntegerFieldEditor(propertyComposite, formToolkit, itemPropertyDescriptor);
					}
				}
			}
			formToolkit.paintBordersFor(propertyComposite);
			propertyComposite.pack();
			propertyMainComposite.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createEnumFieldEditor(Composite propertyComposite, final EEnum typeClass,
			FormToolkit formToolKit, final IItemPropertyDescriptor itemPropertyDescriptor) {
		String displayName = itemPropertyDescriptor.getDisplayName(getModel());
		formToolKit.createLabel(propertyComposite, displayName + " :");
		final CCombo combo = new CCombo(propertyComposite, SWT.NONE);
		List<EEnumLiteral> literalList = typeClass.getELiterals();
		for (Iterator<EEnumLiteral> iterator = literalList.iterator(); iterator.hasNext();) {
			EEnumLiteral enumLiteral = (EEnumLiteral) iterator.next();
			combo.add(enumLiteral.getName());
		}
		Object value = itemPropertyDescriptor.getPropertyValue(getModel());
		if (value != null && value instanceof PropertyValueWrapper) {
			Object editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
			if (editValue != null && editValue instanceof Enumerator) {
				String[] strings = combo.getItems();
				for (int i = 0; i < strings.length; i++) {
					String item = strings[i];
					if (item.equals(((Enumerator) editValue).getName())) {
						combo.select(i);
						break;
					}
				}
			}
		}
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					Object value = itemPropertyDescriptor.getPropertyValue(getModel());
					Method method = typeClass.getInstanceClass().getMethod("get",
							new Class<?>[] { String.class });
					// it's static method
					Object v = method.invoke(null, combo.getText());
					if (value != null && value instanceof PropertyValueWrapper) {
						Object editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
						if (editValue != null) {

							if (!editValue.equals(v)) {
								itemPropertyDescriptor.setPropertyValue(getModel(), v);
							}
						} else {
							itemPropertyDescriptor.setPropertyValue(getModel(), v);
						}
					} else {
						itemPropertyDescriptor.setPropertyValue(getModel(), v);
					}
				} catch (Throwable t) {
					SmooksConfigurationActivator.getDefault().log(t);
				}
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gd);
	}

	protected void createBooleanFieldEditor(final Composite propertyComposite, FormToolkit formToolkit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		String displayName = itemPropertyDescriptor.getDisplayName(getModel());
		Object value = itemPropertyDescriptor.getPropertyValue(getModel());
		final Button checkButton = formToolkit.createButton(propertyComposite, displayName, SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		checkButton.setLayoutData(gd);
		Object editValue = null;
		if (value != null && value instanceof PropertyValueWrapper) {
			editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
			if (editValue != null && editValue instanceof Boolean)
				checkButton.setSelection((Boolean) editValue);
		}
		checkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				itemPropertyDescriptor.setPropertyValue(getModel(), checkButton.getSelection());
			}
		});
	}

	protected void createStringFieldEditor(final Composite propertyComposite, FormToolkit formToolKit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		String displayName = itemPropertyDescriptor.getDisplayName(getModel());
		formToolKit.createLabel(propertyComposite, displayName + " :");
		final Text text = formToolKit.createText(propertyComposite, "", SWT.NONE);
		Object value = itemPropertyDescriptor.getPropertyValue(getModel());
		if (value != null && value instanceof PropertyValueWrapper) {
			Object editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
			if (editValue != null)
				text.setText(editValue.toString());
		}
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object value = itemPropertyDescriptor.getPropertyValue(getModel());
				if (value != null && value instanceof PropertyValueWrapper) {
					Object editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
					if (editValue != null) {
						if (!editValue.equals(text.getText())) {
							itemPropertyDescriptor.setPropertyValue(getModel(), text.getText());
						}
					} else {
						itemPropertyDescriptor.setPropertyValue(getModel(), text.getText());
					}
				} else {
					itemPropertyDescriptor.setPropertyValue(getModel(), text.getText());
				}
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gd);
	}

	protected void createIntegerFieldEditor(final Composite propertyComposite, FormToolkit formToolKit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		String displayName = itemPropertyDescriptor.getDisplayName(getModel());
		formToolKit.createLabel(propertyComposite, displayName + " :");
		final Spinner spinner = new Spinner(propertyComposite, SWT.NONE);
		Object value = itemPropertyDescriptor.getPropertyValue(getModel());
		if (value != null && value instanceof PropertyValueWrapper) {
			Object editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
			if (editValue != null && editValue instanceof Integer)
				spinner.setSelection((Integer) editValue);
		}
		spinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object value = itemPropertyDescriptor.getPropertyValue(getModel());
				if (value != null && value instanceof PropertyValueWrapper) {
					Object editValue = ((PropertyValueWrapper) value).getEditableValue(getModel());
					if (editValue != null) {
						if (!editValue.equals(spinner.getSelection())) {
							itemPropertyDescriptor.setPropertyValue(getModel(), spinner.getSelection());
						}
					} else {
						itemPropertyDescriptor.setPropertyValue(getModel(), spinner.getSelection());
					}
				} else {
					itemPropertyDescriptor.setPropertyValue(getModel(), spinner.getSelection());
				}
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		spinner.setLayoutData(gd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse
	 * .ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IFormPart part, ISelection selection) {
		Object oldModel = getModel();
		setOldModel(oldModel);
		this.selection = selection;
		this.formPart = part;
		this.itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(getModel(),
				IItemPropertySource.class);
		if (getOldModel() == getModel())
			return;
		if (getOldModel() != getModel()) {
			if (propertyComposite != null) {
				propertyComposite.dispose();
				propertyComposite = new Composite(propertyMainComposite, SWT.NONE);
			}
			createPropertyComposite(propertyComposite);
		}
		refreshWhenSelectionChanged();
	}

	public void commit(boolean onSave) {

	}

	public void dispose() {

	}

	public IFormPart getFormPart() {
		return formPart;
	}

	public void initialize(IManagedForm form) {
		this.managedForm = form;
		if (managedForm != null) {
			formToolkit = managedForm.getToolkit();
		}
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isStale() {
		return this.isStale;
	}

	public void refresh() {
	}

	public void setFocus() {

	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public Object getModel() {
		if (selection != null && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).getFirstElement();
		}
		return null;
	}

	protected void refreshWhenSelectionChanged() {
		Object model = getModel();
		if (model instanceof EObject) {
			String text = labelProvider.getText(model);
			section.setText(text);
			section.setDescription("Details of " + text);
		}
	}

	public SmooksMultiFormEditor getFormEditor() {
		return formEditor;
	}

	protected Object getOldModel() {
		return oldModel;
	}

	protected void setOldModel(Object oldModel) {
		this.oldModel = oldModel;
	}

	public void setStale(boolean isStale) {
		this.isStale = isStale;
	}
}

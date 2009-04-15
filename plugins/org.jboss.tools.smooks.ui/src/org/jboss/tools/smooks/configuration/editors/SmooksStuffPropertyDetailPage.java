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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.editors.uitls.IModelProcsser;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;

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
	 * @param detailsComposite
	 */
	protected void createStuffDetailsComposite(Composite detailsComposite) {
		try {
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			detailsComposite.setLayout(layout);
			IPropertyUICreator creator = PropertyUICreatorManager.getInstance().getPropertyUICreator(getModel());
			List<IItemPropertyDescriptor> propertyDes = itemPropertySource.getPropertyDescriptors(getModel());
			for (int i = 0; i < propertyDes.size(); i++) {
				IItemPropertyDescriptor pd = propertyDes.get(i);
				EAttribute attribute = (EAttribute) pd.getFeature(getModel());
				if (attribute.isRequired()) {
					createAttributeUI(detailsComposite, pd, creator);
				}
			}
			for (int i = 0; i < propertyDes.size(); i++) {
				IItemPropertyDescriptor pd = propertyDes.get(i);
				EAttribute attribute = (EAttribute) pd.getFeature(getModel());
				if (!attribute.isRequired()) {
					createAttributeUI(detailsComposite, pd, creator);
				}
			}
			if (creator != null) {
				creator.createExtendUI((AdapterFactoryEditingDomain) formEditor.getEditingDomain(), formToolkit, detailsComposite, getModel(),
						getFormEditor());
			}
			formToolkit.paintBordersFor(detailsComposite);
			detailsComposite.pack();
			propertyMainComposite.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createAttributeUI(Composite detailsComposite, IItemPropertyDescriptor propertyDescriptor, IPropertyUICreator creator) {
		final IItemPropertyDescriptor itemPropertyDescriptor = propertyDescriptor;
		EAttribute feature = (EAttribute) itemPropertyDescriptor.getFeature(getModel());
		boolean createDefault = true;
		if (creator != null) {
			if (creator.ignoreProperty(feature)) {
				return;
			}
			Composite composite = creator.createPropertyUI(formToolkit, detailsComposite, itemPropertyDescriptor, getModel(), feature,
					getFormEditor());
			if (composite != null) {
				createDefault = false;
			}
		}
		if (createDefault) {
			EClassifier typeClazz = feature.getEType();
			boolean hasCreated = false;
			if (typeClazz instanceof EEnum) {
				createEnumFieldEditor(detailsComposite, feature, (EEnum) typeClazz, formToolkit, itemPropertyDescriptor);
				hasCreated = true;
			}
			if (typeClazz.getInstanceClass() == String.class) {
				createStringFieldEditor(detailsComposite, feature, formToolkit, itemPropertyDescriptor);
			}
			if (typeClazz.getInstanceClass() == Boolean.class || typeClazz.getInstanceClass() == boolean.class) {
				createBooleanFieldEditor(detailsComposite, feature, formToolkit, itemPropertyDescriptor);
				hasCreated = true;
			}
			if (typeClazz.getInstanceClass() == Integer.class || typeClazz.getInstanceClass() == int.class) {
				createStringFieldEditor(detailsComposite, feature, formToolkit, itemPropertyDescriptor);
				hasCreated = true;
			}
			if (!hasCreated) {
				// createStringFieldEditor(detailsComposite, feature,
				// formToolkit,
				// itemPropertyDescriptor);
			}
		}
	}

	protected Control createEnumFieldEditor(Composite propertyComposite, EAttribute feature, final EEnum typeClass, FormToolkit formToolKit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		List<EEnumLiteral> literalList = typeClass.getELiterals();
		String[] items = new String[literalList.size()];
		for (int i = 0; i < literalList.size(); i++) {
			EEnumLiteral enumLiteral = (EEnumLiteral) literalList.get(i);
			items[i] = (enumLiteral.getName());
		}
		final IItemPropertyDescriptor fip = itemPropertyDescriptor;
		IModelProcsser processer = new IModelProcsser() {

			public Object unwrapValue(Object model) {
				Object editValue = SmooksUIUtils.getEditValue(fip, getModel());
				if (editValue instanceof Enumerator) {
					return ((Enumerator) editValue).getName();
				}
				return null;
			}

			public Object wrapValue(Object model) {
				String name = model.toString();
				Object v = null;
				try {
					Method method = typeClass.getInstanceClass().getMethod("get", new Class<?>[] { String.class });
					// it's static method
					if (method != null) {
						v = method.invoke(null, name);
					}
				} catch (Throwable t) {
				}
				return v;
			}

		};
		return SmooksUIUtils.createChoiceFieldEditor(propertyComposite, formToolkit, itemPropertyDescriptor, getModel(), items, processer, true);
	}

	protected void createBooleanFieldEditor(final Composite propertyComposite, EAttribute feature, FormToolkit formToolkit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		IModelProcsser processer = new IModelProcsser() {

			public Object unwrapValue(Object model) {
				if (model instanceof Boolean) {
					if ((Boolean) model) {
						return "TRUE";
					} else {
						return "FALSE";
					}
				}
				return "FALSE";
			}

			public Object wrapValue(Object model) {
				try {
					return Boolean.parseBoolean(model.toString());
				} catch (Throwable t) {
				}
				return Boolean.FALSE;
			}

		};
		SmooksUIUtils.createChoiceFieldEditor(propertyComposite, formToolkit, itemPropertyDescriptor, getModel(), new String[] { "TRUE", "FALSE" },
				processer, true);
	}

	protected Text createStringFieldEditor(final Composite propertyComposite, EAttribute feature, FormToolkit formToolKit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		SmooksUIUtils.createFiledEditorLabel(propertyComposite, formToolKit, itemPropertyDescriptor, getModel(), false);
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
		return text;
	}

	protected void createIntegerFieldEditor(final Composite propertyComposite, EAttribute feature, FormToolkit formToolKit,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		SmooksUIUtils.createFiledEditorLabel(propertyComposite, formToolKit, itemPropertyDescriptor, getModel(), false);
		final Spinner spinner = new Spinner(propertyComposite, SWT.BORDER);
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
		this.itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(getModel(), IItemPropertySource.class);
		if (getOldModel() == getModel())
			return;
		if (getOldModel() != getModel()) {
			if (propertyComposite != null) {
				propertyComposite.dispose();
				propertyComposite = new Composite(propertyMainComposite, SWT.NONE);
			}
			createStuffDetailsComposite(propertyComposite);
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
			String text = ((EObject) model).eClass().getName();
			section.setText(text);
			section.setDescription("Details of " + text + ". Required fields are denoted by \"*\".");
			section.layout();
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

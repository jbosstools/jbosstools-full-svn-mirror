/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jsf.ui.editor.form;

import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.attribute.adapter.XChildrenTableStructuredAdapter;
import org.jboss.tools.common.model.ui.attribute.editor.IFieldEditor;
import org.jboss.tools.common.model.ui.attribute.editor.IPropertyEditor;
import org.jboss.tools.common.model.ui.attribute.editor.JavaHyperlinkLineFieldEditor;
import org.jboss.tools.common.model.ui.attribute.editor.TableStructuredEditor;
import org.jboss.tools.common.model.ui.attribute.editor.TableStructuredFieldEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jsf.ui.JsfUiPlugin;
import org.jboss.tools.jsf.ui.editor.FacesConfigEditorMessages;
import org.jboss.tools.common.model.ui.forms.ExpandableForm;
import org.jboss.tools.common.model.ui.widgets.IWidgetSettings;
import org.jboss.tools.common.model.ui.widgets.WhiteSettings;

/**
 * @author igels
 *
 */
public class ListEntries extends ExpandableForm {
	private XAttributeSupport support;
	private XModelObject xmo;
	private TableStructuredEditor tableEditor;
	private XChildrenTableStructuredAdapter tableAdapter;
	private IPropertyEditor valueClass;
	private IWidgetSettings settings = WhiteSettings.getWhite();

	public ListEntries() {
		support = new XAttributeSupport(settings);
		this.setCollapsable(Boolean.TRUE.booleanValue());
	}

	public void dispose() {
		super.dispose();
		if (support != null)
			support.dispose();
		support = null;
		if (tableEditor != null)
			tableEditor.dispose();
		tableEditor = null;
		if (tableAdapter != null)
			tableAdapter.dispose();
		tableAdapter = null;
	}

	protected Control createClientArea(Composite parent,
			IWidgetSettings settings) {
		Composite composite = new Composite(parent, SWT.NONE);
		settings.setupControl(composite);
		GridLayout layout = new GridLayout(2, Boolean.FALSE.booleanValue());

		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 5;
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		composite.setLayout(layout);
		Control[] control;
		GridData gd;

		String description = FacesConfigEditorMessages.LISTENTRIESFORM_DESCRIPTION;
		if (description != null && description.length() > 0) {
			Label label = new Label(composite, SWT.WRAP);
			settings.setupControl(label);
			label.setText(description);
			gd = new GridData();
			gd.horizontalSpan = 2;
			label.setLayoutData(gd);
		}

		if (xmo == null)
			return composite;

		if (valueClass != null)
			putFieldEditorInToComposit(composite, valueClass);

		TableStructuredFieldEditor tbEd = (TableStructuredFieldEditor) tableEditor
				.getFieldEditor(composite);
		control = ((IFieldEditor) tbEd).getControls(composite);
		tbEd.getTableViewer().setLabelProvider(new XMOTableLabelProvider());

		control[0].dispose(); // cannot show label

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		control[1].setLayoutData(gd);

		return composite;
	}

	private void putFieldEditorInToComposit(Composite composite,
			IPropertyEditor propertyEditor) {
		if (propertyEditor != null) {
			JavaHyperlinkLineFieldEditor sb = new JavaHyperlinkLineFieldEditor(
					settings);
			sb.setLabelText(propertyEditor.getLabelText());
			sb.setPropertyEditor(propertyEditor);
			sb.fillIntoGrid(composite, 2);
			support.registerFieldEditor(propertyEditor.getAttributeName(), sb);
		}
	}

	public void initialize(Object model) {
		this.setHeadingText(FacesConfigEditorMessages.LISTENTRIESFORM_HEADER);
		this.xmo = (XModelObject) model;
		if (xmo == null) {
			JsfUiPlugin.getPluginLog().logInfo(
					"Error to create form " //$NON-NLS-1$
							+ FacesConfigEditorMessages.APPLICATIONCONFIGFORM_HEADER
							+ ". Model object cannot be null.", new Exception());			 //$NON-NLS-1$
			return;
		}
		this.model = xmo.getModel();
		this.support.init(xmo);
		this.support.setAutoStore(Boolean.TRUE.booleanValue());

		valueClass = support.getPropertyEditorByName("value-class"); //$NON-NLS-1$
		/* TRIAL_JSF */
		this.tableAdapter = new XChildrenTableStructuredAdapter();
		this.tableAdapter.setShownEntities(new String[] { "JSFListEntry" }); //$NON-NLS-1$

		this.tableAdapter.getActionMapping().clear();

		this.tableAdapter.getActionMapping().put(
				TableStructuredEditor.ADD_ACTION, "CreateActions.AddEntry"); //$NON-NLS-1$
		this.tableAdapter.getActionMapping().put(
				TableStructuredEditor.REMOVE_ACTION, "DeleteActions.Delete"); //$NON-NLS-1$
		this.tableAdapter.getActionMapping().put(
				TableStructuredEditor.EDIT_ACTION, "Properties.Properties"); //$NON-NLS-1$
		this.tableAdapter.getActionMapping().put(
				TableStructuredEditor.UP_ACTION, "%internal%"); //$NON-NLS-1$
		this.tableAdapter.getActionMapping().put(
				TableStructuredEditor.DOWN_ACTION, "%internal%"); //$NON-NLS-1$

		this.tableAdapter.setShownProperties(new String[] { "value" }); //$NON-NLS-1$
		this.tableAdapter
				.setColumnLabels(new String[] { FacesConfigEditorMessages.LISTENTRIESFORM_ENTRY_COLUMN_LABEL });
		this.tableAdapter.setWidths(new int[] { 100 });
		this.tableAdapter.setModelObject(xmo);

		this.tableEditor = new TableStructuredEditor(settings);
		this.tableEditor.setLabelText(""); //$NON-NLS-1$
		this.tableEditor.setInput(this.tableAdapter);
	}

	public void update() {
		if (support != null) {
			support.load();			
			support.updateEnablementByModelObject();
		}
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (support != null) {
			support.updateEnablementByModelObject();
		}
	}

	public class XMOTableLabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			XModelObject xmo = (XModelObject) element;
			String value = xmo.getAttributeValue("null-value"); //$NON-NLS-1$
			if ("true".equals(value)) { //$NON-NLS-1$
				return "[null-value]"; //$NON-NLS-1$
			}
			return xmo.getAttributeValue("value"); //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}

	/**
	 * @see org.jboss.tools.common.model.ui.forms.IForm#doGlobalAction(java.lang.String)
	 */
	public boolean doGlobalAction(String actionId) {
		return support.doGlobalAction(actionId);
	}

}
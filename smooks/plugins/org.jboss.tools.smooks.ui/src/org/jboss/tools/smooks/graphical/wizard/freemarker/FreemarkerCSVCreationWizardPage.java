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
package org.jboss.tools.smooks.graphical.wizard.freemarker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Dart
 * 
 */
public class FreemarkerCSVCreationWizardPage extends WizardPage {

	private List<FieldText> fieldsList = new ArrayList<FieldText>();
	private Text seperatorText;
	private Text quoteText;

	public FreemarkerCSVCreationWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public FreemarkerCSVCreationWizardPage(String pageName) {
		super(pageName);
		this.setTitle("CSV Template Configuration");
		this.setDescription("Configurate CSV Template");
	}

	/**
	 * @return the fieldsList
	 */
	public List<FieldText> getFieldsList() {
		return fieldsList;
	}

	/**
	 * @return the seperatorText
	 */
	public Text getSeperatorText() {
		return seperatorText;
	}

	/**
	 * @return the quoteText
	 */
	public Text getQuoteText() {
		return quoteText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		fieldsList.clear();

		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		mainComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		mainComposite.setLayout(layout);

		Label seperatorLabel = new Label(mainComposite, SWT.NONE);
		seperatorLabel.setText("Seperator Character : ");
		seperatorText = new Text(mainComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		seperatorText.setLayoutData(gd);
		seperatorText.setTextLimit(1);
		
		Label quoteLabel = new Label(mainComposite, SWT.NONE);
		quoteLabel.setText("Quote Character : ");
		quoteText = new Text(mainComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		quoteText.setLayoutData(gd);
		quoteText.setTextLimit(1);
	
		

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		gd.horizontalSpan = 2;
		Group fieldsComposite = new Group(mainComposite, SWT.NONE);
		fieldsComposite.setText("Fields");
		fieldsComposite.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		fieldsComposite.setLayout(gl);

		gd = new GridData(GridData.FILL_BOTH);

		String fields = null;

		final TableViewer fieldsViewer = new TableViewer(fieldsComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		fieldsViewer.getControl().setLayoutData(gd);
		fieldsViewer.getTable().setLinesVisible(true);
		fieldsViewer.setContentProvider(new FieldsContentProvider());
		fieldsViewer.setLabelProvider(new FieldsLabelProvider());

		CellEditor fieldCellEditor = new TextCellEditor(fieldsViewer.getTable(), SWT.BORDER);

		fieldsViewer.getTable().setLinesVisible(true);

		fieldsViewer.setCellEditors(new CellEditor[] { fieldCellEditor });

		fieldsViewer.setColumnProperties(new String[] { "field" });

		fieldsViewer.setCellModifier(new ICellModifier() {

			public void modify(Object element, String property, Object value) {
				Object el = null;
				if (element instanceof Item) {
					el = ((Item) element).getData();
				}
				if (el == null)
					return;
				if (el instanceof FieldText && value instanceof String) {
					if (property.equals("field")) {
						if (value.toString().equals(((FieldText) el).getText())) {
							return;
						}
						((FieldText) el).setText(value.toString());
						fieldsViewer.refresh(el);
						updatePage();
					}
				}
			}

			public Object getValue(Object element, String property) {
				if (element instanceof FieldText) {
					if (property.equals("field")) {
						return ((FieldText) element).getText();
					}
				}

				return null;
			}

			public boolean canModify(Object element, String property) {
				if (element instanceof FieldText) {
					if (property.equals("field")) {
						return true;
					}
				}
				return false;
			}
		});
		if (fields == null) {
			fields = "";
		}
		fillFieldsList(fields);
		fieldsViewer.setInput(fieldsList);

		Composite buttonComposite = new Composite(fieldsComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		buttonComposite.setLayoutData(gd);

		GridLayout bgl = new GridLayout();
		buttonComposite.setLayout(bgl);

		gd = new GridData(GridData.FILL_HORIZONTAL);

		final Button addButton = new Button(buttonComposite, SWT.NONE);
		addButton.setLayoutData(gd);
		addButton.setText("Add Field");

		final Button removeButton = new Button(buttonComposite, SWT.NONE);
		removeButton.setLayoutData(gd);
		removeButton.setText("Remove");

		addButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				addButton.setEnabled(false);
				try {
					FieldText field = new FieldText("field");
					fieldsList.add(field);
					fieldsViewer.refresh();
					updatePage();
				} catch (Throwable t) {

				} finally {
					addButton.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		removeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection s = (IStructuredSelection) fieldsViewer.getSelection();
				fieldsList.removeAll(s.toList());
				fieldsViewer.refresh();
				updatePage();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		this.setControl(mainComposite);
		
		seperatorText.setText(",");
		quoteText.setText("\"");
		
		seperatorText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updatePage();
			}
		});
		quoteText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updatePage();
			}
		});
		
		this.setPageComplete(false);
	}

	private void fillFieldsList(String fieldsString) {
		if (fieldsString == null || fieldsString.length() == 0)
			return;
		String input = fieldsString.toString();
		input = input.trim();
		if (input.indexOf(",") != -1) {
			String[] fields = input.split(",");
			if (fields != null && fields.length > 0) {
				for (int i = 0; i < fields.length; i++) {
					String field = fields[i];
					if (field != null) {
						field = field.trim();
						fieldsList.add(new FieldText(field));
					}
				}
			}
		} else {
			fieldsList.add(new FieldText(input));
		}
	}

	protected void updatePage() {
		String error = null;
		String seperator = seperatorText.getText();
		String quote = quoteText.getText();
		if (seperator == null || seperator.length() == 0) {
			error = "Seperator can't be null";
		}
		if (quote == null || quote.length() == 0) {
			error = "Quote can't be null";
		}

		if (seperator != null && seperator.length() > 1) {
			error = "Seperator must be only one character";
		}
		if (quote != null && quote.length() > 1) {
			error = "Quote must be only one character";
		}
		if (fieldsList.isEmpty()) {
			error = "Fields can't be null";
		}
		this.setErrorMessage(error);
		this.setPageComplete(error == null);
	}

	private class FieldsLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof FieldText) {
				return ((FieldText) element).getText();
			}
			return super.getText(element);
		}

	}

	private class FieldsContentProvider implements IStructuredContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[] {};
		}
	}

	class FieldText {
		private String text = null;

		public FieldText(String t) {
			setText(t);
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

}

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
package org.jboss.tools.vpe.editor.preferences;

import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.common.model.ui.action.CommandBar;
import org.jboss.tools.common.model.ui.action.CommandBarListener;
import org.jboss.tools.common.model.ui.objecteditor.XTable;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeEditAnyDialog;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class TemplatesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, Listener {
	private static final String[] COLUMNS_NAMES = new String[] {
		VpeUIMessages.TemplatesTableProvider_TagName, 
		VpeUIMessages.TemplatesTableProvider_TagForDisplay,
		VpeUIMessages.TemplatesTableProvider_URI,
		VpeUIMessages.TemplatesTableProvider_Children};
	private static final int[] COLUMNS_WIDTHS = new int[] {
		50, 50, 90, 30
	};
	private Table tagsTable;
	private Button addButton;
	private Button editButton;
	private Button removeButton;
	private List<VpeAnyData> tagsList;
	
	protected boolean changed;
	
	public TemplatesPreferencePage() {
		setPreferenceStore(getPreferenceStore());
		/*
		 * Initialize tags list from the file
		 */
		tagsList = VpeTemplateManager.getInstance().getAnyTemplates();
	}
	
	public String getTitle() {
		return VpeUIMessages.TEMPLATES;
	}
	
	public void init(IWorkbench workbench) {
	}

	protected Control createContents(Composite parent) {
		/*
		 * Create main composite element with grid layout.
		 * Two columns.
		 */
		Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        composite.setFont(parent.getFont());
        
        /*
         * Create datatable with the list of unknown tags 
         */
        tagsTable = new Table(composite,  SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        TableLayout layout = new TableLayout();
        tagsTable.setLayout(layout);
        tagsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
        tagsTable.setHeaderVisible(true);
        tagsTable.setLinesVisible(true);
        tagsTable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
			}
        	
		});
        
        /*
         * Create columns in the table
         */
		ColumnLayoutData columnLayoutData;
		for (int i = 0; i < COLUMNS_NAMES.length; i++) {
			TableColumn column = new TableColumn(tagsTable, SWT.NONE);
			column.setText(COLUMNS_NAMES[i]);
			columnLayoutData = new ColumnWeightData(COLUMNS_WIDTHS[i], true);
			layout.addColumnData(columnLayoutData);
		}
		/*
		 * Fill the table with stored tags
		 */
		updateTagsTable();
		
		/*
		 * Add buttons
		 */
		addButton = new Button(composite, SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		addButton.setText(VpeUIMessages.TemplatesPreferencePage_Add);
		
		editButton = new Button(composite, SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		editButton.setText(VpeUIMessages.TemplatesPreferencePage_Edit);
		
		removeButton = new Button(composite, SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		removeButton.setText(VpeUIMessages.TemplatesPreferencePage_Remove);
		
		/*
		 * Adding event listeners to the buttons
		 */
		addButton.addListener(SWT.Modify, this);
		addButton.addListener(SWT.Selection, this);
		editButton.addListener(SWT.Modify, this);
		editButton.addListener(SWT.Selection, this);
		removeButton.addListener(SWT.Modify, this);
		removeButton.addListener(SWT.Selection, this);

		return composite;
	}

	private void updateTagsTable() {
		if(tagsTable == null || tagsTable.isDisposed()) {
			return;
		}
		int selectionIndex = tagsTable.getSelectionIndex();
		/*
		 * Clear all previously saved items.
		 */
		tagsTable.clearAll();
		TableItem tableItem = null;
		
		/*
		 * Recreate table items
		 */
		for (int i = 0; i < tagsList.size(); i++) {
			if(tagsTable.getItemCount() > i) {
				/*
				 * Use existed items
				 */
				tableItem = tagsTable.getItem(i);
			} else {
				/*
				 * Add necessary item
				 */
				tableItem = new TableItem(tagsTable, SWT.BORDER, i);
			}
			/*
			 * Fill in columns.
			 * Tags table has 5 columns with checkbox in the first column.
			 */
			String[] itemColumnsData = new String[tagsTable.getColumnCount()];
			for (int j = 0; j < itemColumnsData.length; j++) {
				/*
				 * Getting values from tagList
				 */
				itemColumnsData[j] = toVisualValue(getValueAt(i, (j)));
			}
			/*
			 * Set cells text
			 */
			tableItem.setText(itemColumnsData);
		}
		
		/*
		 * Restoring selection index
		 */
		if (selectionIndex > 0 ) {
			 try {
					tagsTable.setSelection(selectionIndex);
				} catch (SWTException e) {
					/*
					 * Do nothing
					 */
				}
		}
	}
	
	public String getValueAt(int row, int column) {
		String result = "List is empty"; //$NON-NLS-1$
		if (null != tagsList) {
			VpeAnyData tagItem = (VpeAnyData)tagsList.get(row);
			switch(column){
			case 0:
				result = tagItem.getName();
				break;
			case 1:
				result = tagItem.getTagForDisplay();
				break;
			case 2:
				result = tagItem.getUri();
				break;
			case 3:
				if(tagItem.isChildren()) {
					result = VpeUIMessages.TemplatesTableProvider_Yes;
				} else {
					result = VpeUIMessages.TemplatesTableProvider_No;
				}
				break;
			}
		}
		return result;
	}
	
	private String toVisualValue(String v) {
		if(v == null) return ""; //$NON-NLS-1$
		if(v.indexOf('\n') >= 0) v = v.replace('\n', ' ');
		if(v.indexOf('\t') >= 0) v = v.replace('\t', ' ');
		if(v.indexOf('\r') >= 0) v = v.replace('\r', ' ');
		return v;
	}

	public boolean performOk() {
		if(changed) {
			/*
			 * Commit changes to the file
			 */
			VpeTemplateManager.getInstance().setAnyTemplates(tagsList);
		}
		return true;
	}
	
	public void handleEvent(Event event) {
		Widget source = event.widget;
		if (source == addButton) {
			/*
			 * Handle add event
			 */
			VpeAnyData data = new VpeAnyData(Constants.EMPTY, Constants.EMPTY, Constants.EMPTY);
			VpeEditAnyDialog editDialog = new VpeEditAnyDialog(getShell(), data);
			editDialog.open();
			if(data.isChanged()) {
				/*
				 * Add new template to the list.
				 */
				tagsList.add(data);
			}
		} else if (source == editButton) {
			/*
			 * Handle edit event
			 */
			VpeAnyData data = (VpeAnyData) tagsList.get(tagsTable.getSelectionIndex());
			VpeEditAnyDialog editDialog = new VpeEditAnyDialog(getShell(), data);
			editDialog.open();
			if(data.isChanged()){
				changed = true;
			}
		} else if (source == removeButton) {
			/*
			 * Handle remove event
			 */
			tagsList.remove(tagsTable.getSelectionIndex());
		} else {
			/*
			 * Handle default event
			 */
		}
		/*
		 * Update tags table with the new tempales.
		 */
		updateTagsTable();
	}
}

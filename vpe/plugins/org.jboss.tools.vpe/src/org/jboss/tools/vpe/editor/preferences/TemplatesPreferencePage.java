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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeEditAnyDialog;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.wizards.ExportImportUnknownTagsTemplatesWizardDialog;
import org.jboss.tools.vpe.editor.wizards.ExportUnknownTagsTemplatesWizard;
import org.jboss.tools.vpe.editor.wizards.ImportUnknownTagsTemplatesWizard;
import org.jboss.tools.vpe.editor.wizards.VpeImportExportWizardsUtils;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class TemplatesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, Listener {
	private static final String[] COLUMNS_NAMES = new String[] {
		VpeUIMessages.TemplatesTableProvider_TagName, 
		VpeUIMessages.TemplatesTableProvider_TagForDisplay,
		VpeUIMessages.TemplatesTableProvider_URI,
		VpeUIMessages.TemplatesTableProvider_Children};
	private static final int[] COLUMNS_WIDTHS = new int[] {
		50, 50, 90, 40
	};
	private Table tagsTable;
	private Button addButton;
	private Button editButton;
	private Button removeButton;
	private Button exportButton;
	private Button importButton;
	private List<VpeAnyData> tagsList;
	
	protected boolean tagListWasChanged;
	
	public TemplatesPreferencePage() {
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
         * Create data-table with the list of unknown tags 
         */
        tagsTable = new Table(composite,  SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        TableLayout layout = new TableLayout();
        tagsTable.setLayout(layout);
        tagsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));
        tagsTable.setHeaderVisible(true);
        tagsTable.setLinesVisible(true);
        
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
		VpeImportExportWizardsUtils.updateTagsTable(tagsTable, tagsList, true);
		
		/*
		 * Add buttons
		 */
		addButton = new Button(composite, SWT.BUTTON1);
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		addButton.setText(VpeUIMessages.TemplatesPreferencePage_Add);
		
		editButton = new Button(composite, SWT.BUTTON1);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		editButton.setText(VpeUIMessages.TemplatesPreferencePage_Edit);
		
		removeButton = new Button(composite, SWT.BUTTON1);
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		removeButton.setText(VpeUIMessages.TemplatesPreferencePage_Remove);
		
		exportButton = new Button(composite, SWT.BUTTON1);
		exportButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		exportButton.setText(VpeUIMessages.TemplatesPreferencePage_Export);
		
		importButton = new Button(composite, SWT.BUTTON1);
		importButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		importButton.setText(VpeUIMessages.TemplatesPreferencePage_Import);
		
		/*
		 * Adding event listeners to the buttons
		 */
		addButton.addListener(SWT.Modify, this);
		addButton.addListener(SWT.Selection, this);
		editButton.addListener(SWT.Modify, this);
		editButton.addListener(SWT.Selection, this);
		removeButton.addListener(SWT.Modify, this);
		removeButton.addListener(SWT.Selection, this);
		exportButton.addListener(SWT.Modify, this);
		exportButton.addListener(SWT.Selection, this);
		importButton.addListener(SWT.Modify, this);
		importButton.addListener(SWT.Selection, this);
		
		return composite;
	}
	
	public void handleEvent(Event event) {
		Widget source = event.widget;
		int selectIndex = tagsTable.getSelectionIndex();
		if (source == addButton) {
			/*
			 * Handle add event
			 */
			VpeAnyData data = new VpeAnyData(Constants.EMPTY, Constants.EMPTY, Constants.EMPTY);
			VpeEditAnyDialog editDialog = new VpeEditAnyDialog(getShell(), data, tagsList);
			editDialog.open();
			if(data.isChanged()) {
				/*
				 * Add new template to the list.
				 */
				tagsList.add(data);
				tagListWasChanged = true;
			}
		} else if (source == editButton) {
			/*
			 * Handle edit event
			 */
			if (selectIndex > -1) {
				VpeAnyData data = (VpeAnyData) tagsList.get(selectIndex);
				VpeEditAnyDialog editDialog = new VpeEditAnyDialog(getShell(), data, tagsList);
				editDialog.open();
				if(data.isChanged()) {
					tagListWasChanged = true;
					/*
					 * Update all related tags if the URI was changed
					 */
					for (VpeAnyData tag : tagsList) {
						if (!tag.equals(data)
								&& tag.getName().split(":")[0] //$NON-NLS-1$
										.equalsIgnoreCase(data.getName().split(":")[0])) { //$NON-NLS-1$
							tag.setUri(data.getUri());
						}
					}
				}
			}
		} else if (source == removeButton) {
			/*
			 * Handle remove event
			 */
			if (selectIndex > -1) {
				tagsTable.remove(selectIndex);
				tagsList.remove(selectIndex);
				tagListWasChanged = true;
				if (selectIndex > 0) {
					tagsTable.setSelection(selectIndex - 1);
				}
			}
		} else if (source == exportButton) {
			/*
			 * Export templates
			 */
			ExportImportUnknownTagsTemplatesWizardDialog dlg = new ExportImportUnknownTagsTemplatesWizardDialog(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					new ExportUnknownTagsTemplatesWizard(tagsList));
			dlg.open();
		} else if (source == importButton) {
			/*
			 * Import templates
			 */
			ExportImportUnknownTagsTemplatesWizardDialog dlg = new ExportImportUnknownTagsTemplatesWizardDialog(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					new ImportUnknownTagsTemplatesWizard(tagsList));
			dlg.open();
			/*
			 * Re-initialize tags list from the file
			 */
			tagsList.addAll(dlg.getImportedList());
		} else {
			/*
			 * Handle default event
			 */
		}
		/*
		 * Update tags table with the new templates.
		 */
		VpeImportExportWizardsUtils.updateTagsTable(tagsTable, tagsList, true);
	}
	
	@Override
	public boolean performOk() {
		if(tagListWasChanged) {
			/*
			 * Commit changes to the file.
			 * While saving the templates' file will be rewritten from scratch.
			 * Only the first URI will be used for templates with the same prefix,
			 * other URI will be ignored.
			 */
			VpeTemplateManager.getInstance().setAnyTemplates(tagsList);
		}
		return true;
	}

	@Override
	protected void performApply() {
		performOk();
		/*
		 * After "Apply" button templates list should be updated
		 * due to URI changes 
		 */
		tagsList = VpeTemplateManager.getInstance().getAnyTemplates();
		VpeImportExportWizardsUtils.updateTagsTable(tagsTable, tagsList, true);
	}

	@Override
	protected void performDefaults() {
		/*
		 * Initialize tags list from the file again.
		 * Update visual table.
		 */
		tagsList = VpeTemplateManager.getInstance().getAnyTemplates();
		VpeImportExportWizardsUtils.updateTagsTable(tagsTable, tagsList, true);
	}
	
	
}

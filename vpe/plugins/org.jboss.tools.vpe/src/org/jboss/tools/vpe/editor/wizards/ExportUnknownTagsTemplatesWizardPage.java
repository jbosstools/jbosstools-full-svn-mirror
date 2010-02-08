/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.wizards;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.ReferenceWizardPage;

/**
 * Page for exporting unknown tags templates.
 * 
 * @author dmaliarevich
 */
public class ExportUnknownTagsTemplatesWizardPage extends WizardExportResourcesPage {

	private static final String[] COLUMNS_NAMES = new String[] {
		VpeUIMessages.TemplatesTableProvider_TagName, 
		VpeUIMessages.TemplatesTableProvider_TagForDisplay,
		VpeUIMessages.TemplatesTableProvider_URI,
		VpeUIMessages.TemplatesTableProvider_Children};
	private static final int[] COLUMNS_WIDTHS = new int[] {
		50, 50, 90, 40
	};
	
	private String pathString;
	private Table tagsTable;
	private List<VpeAnyData> tagsList;
	
	/**
	 * Constructor
	 * 
	 * @param pageName
	 * @param selection
	 */
	public ExportUnknownTagsTemplatesWizardPage(String pageName,
			IStructuredSelection selection) {
		super(pageName, selection);
		setTitle(VpeUIMessages.EXPORT_UNKNOWN_TAGS_PAGE_TITLE);
		setDescription(VpeUIMessages.EXPORT_UNKNOWN_TAGS_PAGE_DESCRIPTION);
		setImageDescriptor(ReferenceWizardPage.getImageDescriptor());
		/*
		 * Initialize tags list
		 */
		tagsList = VpeTemplateManager.getInstance().getAnyTemplates();
	}

	@Override
	public void createControl(Composite parent) {
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
        tagsTable = new Table(composite, SWT.BORDER);
        TableLayout layout = new TableLayout();
        tagsTable.setLayout(layout);
        tagsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
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
		updateTagsTable(true);
		
		/*
		 * Add path output and browse button 
		 */
		final Text pathText = new Text(composite, SWT.BORDER);
		pathText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				pathString = ((Text)e.getSource()).getText();
				setPageComplete(isPageComplete());
			}
		});
		
		Button browseButton = new Button(composite, SWT.NONE);
		browseButton.setText(VpeUIMessages.BROWSE_BUTTON_TEXT);
		browseButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(), SWT.NONE);
				String path = dialog.open();
				if (path != null) {
					File file = new File(path);
					pathString = file.toString();
					/*
					 * Then modifyText event will be dispatched.
					 */
					pathText.setText(pathString);
				}
			}
		});
		
		/*
		 * Finishing the initialization
		 */
		updateWidgetEnablements();
        setPageComplete(determinePageCompletion());
        setErrorMessage(null);	// should not initially have error message
        
        setControl(composite);
	}

	@Override
	protected void createDestinationGroup(Composite parent) {
		/*
		 * Create nothing
		 */
	}

	/**
	 * Updates visual table with tags templates.
	 * 
	 * @param clearTagsTable clears current tags table
	 */
	private void updateTagsTable(boolean clearTagsTable) {
		/*
		 * Return when visual table hasn't been initialized.
		 */
		if(tagsTable == null || tagsTable.isDisposed()) {
			return;
		}
		/*
		 * Clear current visual table.
		 */
		if (clearTagsTable) {
			tagsTable.clearAll();
		}
		/*
		 * Return when tags templates list hasn't been initialized.
		 */
		if (tagsList == null) {
			return;
		}
		/*
		 * Remember current selection index 
		 * and restore it at the end.
		 */
		int selectionIndex = tagsTable.getSelectionIndex();
		TableItem tableItem = null;
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
			 */
			String[] itemColumnsData = new String[tagsTable.getColumnCount()];
			for (int j = 0; j < itemColumnsData.length; j++) {
				/*
				 * Getting values from tagList
				 */
				itemColumnsData[j] = toVisualValue(getValueAt(i, j));
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
		if ((null != tagsList) && ((row >= 0) && (tagsList.size() > 0) && (row < tagsList.size()))) {
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
	
	public void handleEvent(Event event) {
		/*
		 * Do nothing
		 */
	}

	@Override
	public boolean isPageComplete() {
		boolean isPageComplete = false;
		if ((pathString != null) 
				&& !Constants.EMPTY.equalsIgnoreCase(pathString)
				&& (Path.ROOT.isValidPath(pathString))) {
			isPageComplete = true;
		}
		return isPageComplete;
	}

	 public boolean finish() {
		 List<VpeAnyData> templates = VpeTemplateManager.getInstance().getAnyTemplates();
		 IPath path = new Path(pathString);
		 VpeTemplateManager.getInstance().setAnyTemplates(templates, path);
		 return true;
	 }
	
}

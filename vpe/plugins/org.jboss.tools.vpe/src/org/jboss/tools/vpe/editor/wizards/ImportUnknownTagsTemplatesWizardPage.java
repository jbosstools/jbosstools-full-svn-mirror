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
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableLayout;
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
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.ReferenceWizardPage;


/**
 * Page for importing unknown tags templates.
 * 
 * @author dmaliarevich
 */
public class ImportUnknownTagsTemplatesWizardPage extends
		WizardResourceImportPage {

	private static final String[] COLUMNS_NAMES = new String[] {
		"", //$NON-NLS-1$
		VpeUIMessages.TemplatesTableProvider_TagName, 
		VpeUIMessages.TemplatesTableProvider_TagForDisplay,
		VpeUIMessages.TemplatesTableProvider_URI,
		VpeUIMessages.TemplatesTableProvider_Children};
	private static final int[] COLUMNS_WIDTHS = new int[] {
		15, 50, 50, 90, 30
	};
	
	private String pathString;
	private Table tagsTable;
//	private TableViewer tableViewer;
	private List<VpeAnyData> tagsList;
	private Button selectAllButton;
	private Button deselectAllButton;
	
	
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param selection
	 */
	public ImportUnknownTagsTemplatesWizardPage(String name,
			IStructuredSelection selection) {
		super(name, selection);
		setTitle(VpeUIMessages.IMPORT_UNKNOWN_TAGS_PAGE_TITLE);
		setDescription(VpeUIMessages.IMPORT_UNKNOWN_TAGS_PAGE_DESCRIPTION);
		setImageDescriptor(ReferenceWizardPage.getImageDescriptor());
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
		 * Add path output and browse button 
		 */
		final Text pathText = new Text(composite, SWT.BORDER);
		pathText.setEditable(false);
		pathText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				pathString = ((Text)e.getSource()).getText();
				setPageComplete(isPageComplete());
			}
		});
		
		Button browseButton = new Button(composite, SWT.NONE);
		browseButton.setText(VpeUIMessages.BROWSE_BUTTON_TEXT);
		browseButton.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(), SWT.NONE);
				String path = dialog.open();
				if (path != null) {
					File file = new File(path);
					pathString = file.toString();
					pathText.setText(pathString);
					/*
					 * Load tags list from the specified location.
					 */
					tagsList = VpeTemplateManager.getInstance().getAnyTemplates(new Path(pathString));
					/*
					 * Update table tags list based on the loaded file.
					 */
					updateTagsTable();
					/*
					 * Store loaded templates to the default auto-templates location.
					 */
					VpeTemplateManager.getInstance().setAnyTemplates(tagsList);
					/*
					 * Check if the page is complete.
					 */
					setPageComplete(isPageComplete());
				}
			}
		});
		
        /*
         * Create datatable with the list of unknown tags 
         */
        tagsTable = new Table(composite, SWT.BORDER);
        TableLayout layout = new TableLayout();
        tagsTable.setLayout(layout);
        tagsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
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
		 * Add buttons
		 */
		selectAllButton = new Button(composite, SWT.NONE);
		selectAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		selectAllButton.setText(VpeUIMessages.SELECT_ALL);
		
		deselectAllButton = new Button(composite, SWT.NONE);
		deselectAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		deselectAllButton.setText(VpeUIMessages.DESELECT_ALL);
		
		/*
		 * Adding event listeners to the buttons
		 */
		selectAllButton.addListener(SWT.Modify, this);
		selectAllButton.addListener(SWT.Selection, this);
		deselectAllButton.addListener(SWT.Modify, this);
		deselectAllButton.addListener(SWT.Selection, this);
		
		/*
		 * Finishing the initialization
		 */
        setErrorMessage(null);	// should not initially have error message
        setControl(composite);
		
	}

	private void updateTagsTable() {
		if(tagsTable == null || tagsTable.isDisposed()) {
			return;
		}
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
			 * Tags table has 5 columns with checkbox in the first column.
			 */
			String[] itemColumnsData = new String[tagsTable.getColumnCount()];
			itemColumnsData[0] = ""; //$NON-NLS-1$
			for (int j = 1; j < itemColumnsData.length; j++) {
				/*
				 * Getting values from tagList
				 */
				itemColumnsData[j] = toVisualValue(getValueAt(i, (j-1)));
			}
			/*
			 * Set cells text
			 */
			tableItem.setText(itemColumnsData);
			/*
			 * Adding checkbox to the first column 
		 	*/
//			TableEditor editor = new TableEditor(tagsTable);
//			Button check = new Button(tagsTable, SWT.CHECK);
//			check.setBackground(tagsTable.getBackground());
//			editor.minimumWidth = check.getSize().x;
//			editor.grabHorizontal = true;
//			editor.setEditor(check, tableItem, 0);
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
	
	public void handleEvent(Event event) {
		Widget source = event.widget;
		if (source == selectAllButton) {
			/*
			 * Handle select all event
			 */
			
		} else if (source == deselectAllButton) {
			/*
			 * Handle deselect all event
			 */
			
		} else {
			/*
			 * Handle chekbox event
			 */

		}
	}
	
	@Override
	public boolean isPageComplete() {
		/*
		 * Later page should be complete some tags are selected.
		 */
		boolean isPageComplete = false;
		if ((pathString != null) && !"".equalsIgnoreCase(pathString)) { //$NON-NLS-1$
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
	
	@Override
	protected void createSourceGroup(Composite parent) {
		/*
		 * Create nothing.
		 */
	}

	@Override
	protected ITreeContentProvider getFileProvider() {
		return null;
	}

	@Override
	protected ITreeContentProvider getFolderProvider() {
		return null;
	}

	

}

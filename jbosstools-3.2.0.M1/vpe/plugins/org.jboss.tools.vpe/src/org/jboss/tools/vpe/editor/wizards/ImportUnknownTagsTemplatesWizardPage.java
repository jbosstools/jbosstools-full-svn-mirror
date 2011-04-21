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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.ReferenceWizardPage;

/**
 * Page for importing unknown tags templates.
 * 
 * @author dmaliarevich
 */
public class ImportUnknownTagsTemplatesWizardPage extends
		WizardResourceImportPage implements VpeImportExportWizardPage {
	
	private String pathString;
	private Table tagsTable;
	private List<VpeAnyData> tagsList;
	
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
        composite.setLayout(new GridLayout(3, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        composite.setFont(parent.getFont());
        
        /*
		 * Add label 
		 */
		Label fileNamaLabel = new Label(composite, SWT.NONE); 
		fileNamaLabel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		fileNamaLabel.setText(VpeUIMessages.FILE_NAME_LABEL);
        /*
		 * Add output path 
		 */
		final Text pathText = new Text(composite, SWT.BORDER);
		pathText.setEditable(true);
		pathText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				pathString = ((Text)e.getSource()).getText();
				IPath enteredPath = null;
				/*
				 * Load templates if the path is valid
				 */
				if ((Path.ROOT.isValidPath(pathString))
						&& (enteredPath = Path.fromOSString(pathString)).toFile().exists()) {
					/*
					 * Load tags list from the specified location.
					 */
					tagsList = VpeTemplateManager.getInstance()
							.getAnyTemplates(enteredPath);
					
				} else {
					/*
					 * Reset taglist, show empty table
					 */
					if (tagsList != null) {
						tagsList.clear();
					}
				}
				/*
				 * Update table tags list based on the loaded file.
				 */
				VpeImportExportWizardsUtils.updateTagsTable(tagsTable, tagsList, true);
				/*
				 * Check if the page is complete.
				 */
				setPageComplete(isPageComplete());
			}
		});
		/*
		 * Add browse button
		 */
		Button browseButton = new Button(composite, SWT.NONE);
		browseButton.setText(VpeUIMessages.BROWSE_BUTTON_TEXT);
		browseButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
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
         * Create datatable with the list of unknown tags 
         */
        tagsTable = new Table(composite, SWT.BORDER);
        TableLayout layout = new TableLayout();
        tagsTable.setLayout(layout);
        tagsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
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
		 * Finishing the initialization
		 */
        setErrorMessage(null);	// should not initially have error message
        setControl(composite);
		
	}
	
	@Override
	public boolean isPageComplete() {
		boolean isPageComplete = false;
		if ((pathString != null)
				&& !Constants.EMPTY.equalsIgnoreCase(pathString)
				&& (Path.ROOT.isValidPath(pathString))
				&& ((Path.fromOSString(pathString)).toFile().exists())) {
			isPageComplete = true;
		}
		return isPageComplete;
	}

	 public boolean finish() {
		 /*
		  * Currently used templates list 
		  */
		 List<VpeAnyData>  currentList = VpeTemplateManager.getInstance().getAnyTemplates();
		 
		 /*
		  * Uploading will add only missing templates.
		  * So here all duplicated templates will be deleted from the tagsList.
		  */
		 Iterator<VpeAnyData> iterator = tagsList.iterator();
		 while (iterator.hasNext()) {
			VpeAnyData loadedTemplate = (VpeAnyData) iterator.next();
			for (VpeAnyData currentTemplate : currentList) {
				if (loadedTemplate.equals(currentTemplate)) {
					iterator.remove();
				}
			}
		}
		 /*
		  * Add unique templates to the current list.
		  */
		 if (currentList.addAll(tagsList)) {
			 /*
			  * Store loaded templates to the default auto-templates location.
			  */
			VpeTemplateManager.getInstance().setAnyTemplates(currentList);
		} else {
			/*
			 * Log WARNING if the operation could not be performed.
			 */
			VpePlugin.getDefault().logWarning(VpeUIMessages.NONE_TEMPLATES_WERE_ADDED);
		}
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

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
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.WizardPage;
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
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.ReferenceWizardPage;

public class ExportUnknownTagsTemplatesWizardPage extends WizardPage implements
		VpeImportExportWizardPage {

	private String pathString;
	private Table tagsTable;
	private List<VpeAnyData> tagsList;
	
	protected ExportUnknownTagsTemplatesWizardPage(String pageName, List<VpeAnyData>  currentList) {
		super(pageName);
		setTitle(VpeUIMessages.EXPORT_UNKNOWN_TAGS_PAGE_TITLE);
		setDescription(VpeUIMessages.EXPORT_UNKNOWN_TAGS_PAGE_DESCRIPTION);
		setImageDescriptor(ReferenceWizardPage.getImageDescriptor());
		/*
		 * Initialize tags list
		 */
		tagsList = currentList;
	}

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
		 * Fill the table with stored tags
		 */
		VpeImportExportWizardsUtils.updateTagsTable(tagsTable, tagsList, true);
		
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
		pathText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				pathString = ((Text)e.getSource()).getText();
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
		 * Finishing the initialization
		 */
        setPageComplete(isPageComplete());
        setErrorMessage(null);	// should not initially have error message
        
        setControl(composite);
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

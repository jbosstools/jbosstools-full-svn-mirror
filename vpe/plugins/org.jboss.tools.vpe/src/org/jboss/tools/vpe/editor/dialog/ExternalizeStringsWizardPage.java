/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.bundle.BundleMap.BundleEntry;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Attr;

public class ExternalizeStringsWizardPage extends WizardPage {

	private final int DIALOG_WIDTH = 450;
	private final int DIALOG_HEIGHT = 650;
	private VpeController vpeController;
	private Text textStringValue;
	private Text propsKey;
	private Text propsValue;
	private Button newFile;
	private Label propsFileLabel;
	private Text propsFile;
	private Label rbListLabel;
	private Combo rbCombo;
	private BundleMap bm;
	private Group propsFilesGroup;
	private Status propsKeyStatus;
	private Status propsValueStatus;
	private Status selectedTextStatus;
	private Table tagsTable;
	
	public ExternalizeStringsWizardPage(String pageName, VpeController vpeController) {
		/*
		 * Setting dialog Title, Description, Image.
		 */
		super(pageName,
				VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_TITLE, 
				ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		setDescription(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_DESCRIPTION);
		setPageComplete(false);
		this.vpeController = vpeController;
		this.bm = vpeController.getPageContext().getBundle();
		propsKeyStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		propsValueStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		selectedTextStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
	}

	public ExternalizeStringsWizardPage(String pageName) {
		super(pageName);
	}

	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = DIALOG_WIDTH;
		gd.heightHint = DIALOG_HEIGHT;
		composite.setLayoutData(gd);
		
		/*
		 * Create properties string group
		 */
		Group propsStringGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		propsStringGroup.setLayout(new GridLayout(3, false));
		propsStringGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		propsStringGroup.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPS_STRINGS_GROUP);
		
		/*
		 * Create Text String label
		 */
		Label textStringLabel = new Label(propsStringGroup, SWT.NONE);
		textStringLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		textStringLabel.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_TEXT_STRING);
		/*
		 * Create Text String value
		 */
		textStringValue = new Text(propsStringGroup, SWT.BORDER);
		textStringValue.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		textStringValue.setText(Constants.EMPTY);
		textStringValue.setEditable(false);

		/*
		 * Create Properties Key label
		 */
		Label propsKeyLabel = new Label(propsStringGroup, SWT.NONE);
		propsKeyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		propsKeyLabel.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		/*
		 * Create Properties Key value
		 */
		propsKey = new Text(propsStringGroup, SWT.BORDER);
		propsKey.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		propsKey.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_DEFAULT_KEY);
		propsKey.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if ((propsKey.getText() == null) 
						|| (Constants.EMPTY.equalsIgnoreCase(propsKey.getText().trim()))) {
					propsKeyStatus = new Status(
							IStatus.ERROR,
							VpePlugin.PLUGIN_ID,
							VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_KEY_MUST_BE_SET);
				} else {
					propsKeyStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
				}
				updateStatus();
			}
		});
		/*
		 * Create Properties Value  label
		 */
		Label propsValueLabel = new Label(propsStringGroup, SWT.NONE);
		propsValueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		propsValueLabel.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		/*
		 * Create Properties Value value
		 */
		propsValue = new Text(propsStringGroup, SWT.BORDER);
		propsValue.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		propsValue.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_DEFAULT_VALUE);
		propsValue.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if ((propsValue.getText() == null)
						|| (Constants.EMPTY.equalsIgnoreCase(propsValue.getText().trim()))) {
					propsValueStatus = new Status(
							IStatus.ERROR,
							VpePlugin.PLUGIN_ID,
							VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_VALUE_MUST_BE_SET);
				} else {
					propsValueStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
				}
				updateStatus();
			}
		});

		/*
		 * Create New File Checkbox
		 */
		newFile = new Button(composite, SWT.CHECK);
		newFile.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		newFile.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_NEW_FILE);
		
		/*
		 * Create properties string group
		 */
		propsFilesGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		propsFilesGroup.setLayout(new GridLayout(3, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd.heightHint = 300; 
		propsFilesGroup.setLayoutData(gd);
		propsFilesGroup.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPS_FILES_GROUP);

		/*
		 * Add selection listener to New File button
		 */
		newFile.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = ((Button)e.getSource()).getSelection();
				if (selected) {
					enableBundleGroup(false);
				} else {
					enableBundleGroup(true);
				}
				setPageComplete(isPageComplete());
			}
		});
		
		/*
		 * Create Resource Bundles List label
		 */
		rbListLabel = new Label(propsFilesGroup, SWT.NONE);
		rbListLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		rbListLabel.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_RESOURCE_BUNDLE_LIST);
		/*
		 * Create Resource Bundles combobox
		 */
		rbCombo = new Combo(propsFilesGroup, SWT.NONE);
		rbCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		rbCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IFile bundleFile = bm.getBundleFile(rbCombo.getText());
				String bundlePath = Constants.EMPTY;
				if (bundleFile != null) {
					bundlePath = bundleFile.getFullPath().toString();
					updateTable(bundleFile);
				} 
				propsFile.setText(bundlePath);
				setPageComplete(isPageComplete());
			}
		});
		
		/*
		 * Create Properties File label
		 */
		propsFileLabel = new Label(propsFilesGroup, SWT.NONE);
		propsFileLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false,false, 1, 1));
		propsFileLabel.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPERTIES_FILE);
		/*
		 * Create Properties File path field
		 */
		propsFile = new Text(propsFilesGroup, SWT.BORDER);
		propsFile.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,false, 2, 1));
		propsFile.setText(Constants.EMPTY);
		propsFile.setEditable(false);
		/*
		 * Create properties file table of content
		 */
		tagsTable = new Table(propsFilesGroup, SWT.BORDER);
        TableLayout layout = new TableLayout();
        tagsTable.setLayout(layout);
        tagsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        tagsTable.setHeaderVisible(true);
        tagsTable.setLinesVisible(true);
		
        ColumnLayoutData columnLayoutData;
        TableColumn propNameColumn = new TableColumn(tagsTable, SWT.NONE);
        propNameColumn.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPERTY_NAME);
        columnLayoutData = new ColumnWeightData(200, true);
        layout.addColumnData(columnLayoutData);
        TableColumn propValueColumn = new TableColumn(tagsTable, SWT.NONE);
        propValueColumn.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPERTY_VALUE);
        columnLayoutData = new ColumnWeightData(200, true);
        layout.addColumnData(columnLayoutData);
        
		/*
		 * Initialize all fields with real values.
		 */
		initializeTextFields();
		
		/*
		 * Wizard Page control should be initialized.
		 */
		setControl(composite);
	}

	private void initializeTextFields() {
		if ((vpeController == null) || (bm == null)){
			VpePlugin.getDefault().logWarning(
					VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_INITIALIZATION_ERROR);
		} else {
			ISelection sel = vpeController.getSourceEditor().getSelectionProvider().getSelection();
			if ((textStringValue != null) && (propsKey != null)
					&& isSelectionCorrect(sel)) {
				/*
				 * TODO: One must add functionality
				 * for convenient text replacement
				 */
				String stringToUpdate = Constants.EMPTY;
				TextSelection textSelection = null;
				String text = null;
				IStructuredSelection structuredSelection = (IStructuredSelection) sel;
				textSelection = (TextSelection) sel;
				text = textSelection.getText();
				if (text.trim().length() < 1) {
					selectedTextStatus = new Status(
							IStatus.ERROR,
							VpePlugin.PLUGIN_ID,
							VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_SELECTED_TEXT_IS_EMPTY);
				} else if ((text.indexOf(Constants.GT) != -1) ||  (text.indexOf(Constants.LT) != -1)) {
					selectedTextStatus = new Status(
							IStatus.ERROR,
							VpePlugin.PLUGIN_ID,
							VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_WRONG_SELECTED_TEXT);
				} else {
					selectedTextStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
				}
				/*
				 * Update status message.
				 */
				updateStatus();
				
				Object selectedElement = structuredSelection.getFirstElement();
				/*
				 * Parse selected element and find a string to replace
				 */
				if (selectedElement instanceof org.w3c.dom.Text) {
					org.w3c.dom.Text textNode = (org.w3c.dom.Text) selectedElement;
					if ((textNode.getNodeValue().trim().length() > 0)
							&& (text.trim().length() > 0)) {
						stringToUpdate = textNode.getNodeValue();
					}
				} else if (selectedElement instanceof Attr) {
					Attr attrNode = (Attr) selectedElement;
					if ((attrNode.getNodeValue().trim().length() > 0)
							&& (text.trim().length() > 0)) {
						stringToUpdate = attrNode.getNodeValue();
					}
				} else {
					VpePlugin.getDefault().logWarning(
							VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_INITIALIZATION_ERROR);
				}
				/*
				 * Update text string field
				 */
				textStringValue.setText(text);
				
				/*
				 * Initialize bundle messages field
				 */
				BundleEntry[] bundles = bm.getBundles();
				Set<String> uriSet = new HashSet<String>(); 
				for (BundleEntry bundleEntry : bundles) {
					if (!uriSet.contains(bundleEntry.uri)) {
						uriSet.add(bundleEntry.uri);
						rbCombo.add(bundleEntry.uri);
					}
				}
			} else {
				VpePlugin.getDefault().logWarning(
						VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_INITIALIZATION_ERROR);
			}
		}
	}
	
	private boolean isSelectionCorrect(ISelection sel) {
		if ((sel instanceof TextSelection)
				&& (sel instanceof IStructuredSelection)
				&& (((IStructuredSelection) sel).size() == 1)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void updateTable(IFile file) {
		if ((file != null) && file.exists()) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					file.getContents()));
			String line = in.readLine();
			int i = 0;
			while (line != null) {
				TableItem tableItem = null;
				String[] propertie = null;
				if (line.trim().length() > 0) {
					tableItem = new TableItem(tagsTable, SWT.BORDER, i);
					propertie = line.trim().split("="); //$NON-NLS-1$
					if (propertie.length < 3) {
						tableItem.setText(propertie);
						i++;
					}
				}
				line = in.readLine();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		}
	}
	
	private void enableBundleGroup(boolean enabled) {
			propsFilesGroup.setEnabled(enabled);
			propsFileLabel.setEnabled(enabled);
			propsFile.setEnabled(enabled);
			rbListLabel.setEnabled(enabled);
			rbCombo.setEnabled(enabled);
			tagsTable.setEnabled(enabled);
	}
	
	public String getKeyValuePair() {
		return "\n" + propsKey.getText() + Constants.EQUAL + propsValue.getText() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public IFile getBundleFile() {
		return bm.getBundleFile(rbCombo.getText());
	}
	
	public boolean isNewFile() {
		return newFile.getSelection();
	}
	
	public boolean replaceText() {
		StructuredTextEditor editor = vpeController.getSourceEditor();
		IDocumentProvider prov = editor.getDocumentProvider();
		IDocument doc = prov.getDocument(editor.getEditorInput());
		ISelection sel = editor.getSelectionProvider().getSelection();
		if (isSelectionCorrect(sel)) {
			try {
				/*
				 * Get source text and new text
				 */
				final TextSelection textSel = (TextSelection) sel;
				/*
				 * Replace text in the editor with "key.value"
				 */
				String bundlePrefix = Constants.EMPTY;
				if (!isNewFile()) {
					for (BundleEntry be : bm.getBundles()) {
						if (be.uri.equalsIgnoreCase(rbCombo.getText())) {
							bundlePrefix = be.prefix;
						}
					}
				}
				String newText = "#{" + bundlePrefix + Constants.DOT + propsKey.getText() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
				doc.replace(textSel.getOffset(), textSel.getLength(), newText);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
		 return true;
	}
	
	private void updateStatus() {
		applyStatus(this, new IStatus[] {selectedTextStatus, propsKeyStatus, propsValueStatus});
		setPageComplete(isPageComplete());
	}
	
	private void applyStatus(DialogPage page, IStatus[] statuses) {
		IStatus severeStatus = statuses[0];
		for (IStatus status : statuses) {
			severeStatus = severeStatus.getSeverity() >= status.getSeverity() 
				? severeStatus : status;
		}

		String message = severeStatus.getMessage();
		switch (severeStatus.getSeverity()) {
		case IStatus.OK:
			page.setMessage(null, IMessageProvider.NONE);
			page.setErrorMessage(null);
			break;

		case IStatus.WARNING:
			page.setMessage(message, IMessageProvider.WARNING);
			page.setErrorMessage(null);
			break;

		case IStatus.INFO:
			page.setMessage(message, IMessageProvider.INFORMATION);
			page.setErrorMessage(null);
			break;

		default:
			if (message.length() == 0) {
				message = null;
			}
			page.setMessage(null);
			page.setErrorMessage(message);
			break;
		}
	}

	@Override
	public boolean isPageComplete() {
		boolean isPageComplete = false;
		/*
		 * The page is ready when there are no error messages 
		 * and the bundle is selected
		 * and "key=value" exists.
		 */
		if ((getErrorMessage() == null)
				&& (getMessage() == null)
				&& ((rbCombo.getSelectionIndex() != -1) || isNewFile())) {
			isPageComplete = true;
		}
		return isPageComplete;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && (getNextPage() != null)
				&& (newFile.getSelection() == true);
	}

	
}

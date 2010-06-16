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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
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
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.ISourceEditingTextTools;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xml.ui.internal.provisional.IDOMSourceEditingTextTools;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.bundle.BundleMap.BundleEntry;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExternalizeStringsWizardPage extends WizardPage {

	private final int DIALOG_WIDTH = 450;
	private final int DIALOG_HEIGHT = 650;
	private StructuredTextEditor editor;
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
	private Status rbComboStatus;
	
	private Table tagsTable;
	
	public ExternalizeStringsWizardPage(String pageName, StructuredTextEditor editor, BundleMap bm) {
		/*
		 * Setting dialog Title, Description, Image.
		 */
		super(pageName,
				VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE, 
				ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		setDescription(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		setPageComplete(false);
		this.editor = editor;
		if (bm != null) {
			this.bm = bm;
		} else {
			/*
			 * When BundleMap is null create it manually
			 * with all necessary initialization
			 */
			this.bm = createBundleMap(editor);
		}
		propsKeyStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		propsValueStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		rbComboStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
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
		propsStringGroup.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPS_STRINGS_GROUP);

		/*
		 * Create Properties Key label
		 */
		Label propsKeyLabel = new Label(propsStringGroup, SWT.NONE);
		propsKeyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		propsKeyLabel.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		/*
		 * Create Properties Key value
		 */
		propsKey = new Text(propsStringGroup, SWT.BORDER);
		propsKey.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		propsKey.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_DEFAULT_KEY);
		propsKey.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateStatus();
			}
		});
		/*
		 * Create Properties Value  label
		 */
		Label propsValueLabel = new Label(propsStringGroup, SWT.NONE);
		propsValueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		propsValueLabel.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		/*
		 * Create Properties Value value
		 */
		propsValue = new Text(propsStringGroup, SWT.BORDER);
		propsValue.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		propsValue.setText(Constants.EMPTY);
		propsValue.setEditable(false);
		propsValue.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateStatus();
			}
		});

		/*
		 * Create New File Checkbox
		 */
		newFile = new Button(composite, SWT.CHECK);
		newFile.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		newFile.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE);
		
		/*
		 * Create properties string group
		 */
		propsFilesGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		propsFilesGroup.setLayout(new GridLayout(3, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd.heightHint = 300; 
		propsFilesGroup.setLayoutData(gd);
		propsFilesGroup.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPS_FILES_GROUP);

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
				updateStatus();
			}
		});
		
		/*
		 * Create Resource Bundles List label
		 */
		rbListLabel = new Label(propsFilesGroup, SWT.NONE);
		rbListLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		rbListLabel.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_RESOURCE_BUNDLE_LIST);
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
				} else {
					VpePlugin.getDefault().logError(
							"Could not get Bundle File for resource '" //$NON-NLS-1$
									+ rbCombo.getText() + "'"); //$NON-NLS-1$
				}
				propsFile.setText(bundlePath);
				
				updateDuplicateKeyStatus();
				updateStatus();
			}
		});
		
		/*
		 * Create Properties File label
		 */
		propsFileLabel = new Label(propsFilesGroup, SWT.NONE);
		propsFileLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false,false, 1, 1));
		propsFileLabel.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_FILE);
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
        propNameColumn.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTY_NAME);
        columnLayoutData = new ColumnWeightData(200, true);
        layout.addColumnData(columnLayoutData);
        TableColumn propValueColumn = new TableColumn(tagsTable, SWT.NONE);
        propValueColumn.setText(VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTY_VALUE);
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

	/**
	 * Initialize dialog's controls.
	 * Fill in appropriate text and make validation.
	 */
	private void initializeTextFields() {
		ISelection sel = editor.getSelectionProvider().getSelection();
		if (isSelectionCorrect(sel)) {
			String text = Constants.EMPTY;
			String stringToUpdate = Constants.EMPTY;
			TextSelection textSelection = null;
			IStructuredSelection structuredSelection = (IStructuredSelection) sel;
			textSelection = (TextSelection) sel;
			text = textSelection.getText();
			Object selectedElement = structuredSelection.getFirstElement();
			/*
			 * When selected text in empty
			 * parse selected element and find a string to replace..
			 */
			if ((text.trim().length() == 0)) {
				if (selectedElement instanceof org.w3c.dom.Text) {
					/*
					 * ..it could be a plain text
					 */
					org.w3c.dom.Text textNode = (org.w3c.dom.Text) selectedElement;
					if (textNode.getNodeValue().trim().length() > 0) {
						stringToUpdate = textNode.getNodeValue();
						editor.getSelectionProvider().setSelection(new StructuredSelection(stringToUpdate));
					}
				} else if (selectedElement instanceof Attr) {
					/*
					 * ..or an attribute's value
					 */
					Attr attrNode = (Attr) selectedElement;
					if (attrNode.getNodeValue().trim().length() > 0) {
						stringToUpdate = attrNode.getNodeValue();
						editor.getSelectionProvider().setSelection(new StructuredSelection(stringToUpdate));
					}
				}
				if ((stringToUpdate.trim().length() > 0)) {
					text = stringToUpdate;
				}
			}
			/*
			 * Update text string field.
			 * Trim the text to remove line breaks and caret returns.
			 */
			propsValue.setText(text.trim());
			/*
			 * Initialize bundle messages field
			 */
			if (bm == null) {
				VpePlugin.getDefault().logWarning(
						VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_RB_IS_MISSING);
			} else {
				BundleEntry[] bundles = bm.getBundles();
				Set<String> uriSet = new HashSet<String>(); 
				for (BundleEntry bundleEntry : bundles) {
					if (!uriSet.contains(bundleEntry.uri)) {
						uriSet.add(bundleEntry.uri);
						rbCombo.add(bundleEntry.uri);
					}
				}
			}
			/*
			 * Update status message.
			 */
			updateStatus();
		} else {
			VpePlugin.getDefault().logWarning(
					VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_INITIALIZATION_ERROR);
		}
	}
	
	/**
	 * Checks user has selected a correct string.
	 *  
	 * @param selection the current selection
	 * @return <code>true</code> if correct
	 */
	private boolean isSelectionCorrect(ISelection selection) {
		if ((selection instanceof TextSelection)
				&& (selection instanceof IStructuredSelection)
				&& (((IStructuredSelection) selection).size() == 1)) {
			return true;
		} 
		return false;
	}
	
	/**
	 * Checks keys in the selected resource bundle.
	 * 
	 * @param key the key name
	 * @return <code>true</code> if there is a key with the specified name
	 */
	private boolean isDuplicatedKey(String key) {
		boolean isDupliacted = false;
		if ((tagsTable.getItemCount() > 0) && (null != key) && !isNewFile()) {
			TableItem[] items = tagsTable.getItems();
			for (TableItem tableItem : items) {
				if (key.equalsIgnoreCase(tableItem.getText(0))) {
					isDupliacted = true;
					break;
				}
			}
		} 
		return isDupliacted; 
	}
	
	/**
	 * Update resource bundle table according to the selected file:
	 * it fills key and value columns.
	 * 
	 * @param file the resource bundle file
	 */
	private void updateTable(IFile file) {
		if ((file != null) && file.exists()) {
		try {
			/*
			 * Read the file content
			 */
			BufferedReader in = new BufferedReader(new InputStreamReader(
					file.getContents()));
			String line = in.readLine();
			/*
			 * Clear the table
			 */
			tagsTable.clearAll();
			/*
			 * Fill in new values
			 */
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
			in.close();
			in = null;
		} catch (CoreException e) {
			VpePlugin.getDefault().logError(
					"Could not load file content for '" + file + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			VpePlugin.getDefault().logError(
					"Could not read file: '" + file + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
		}		
		} else {
			VpePlugin.getDefault().logError(
					"Bundle File'" + file + "' does not exist!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * Enables or disables resource bundle information
	 * 
	 * @param enabled shows the status
	 */
	private void enableBundleGroup(boolean enabled) {
			propsFilesGroup.setEnabled(enabled);
			propsFileLabel.setEnabled(enabled);
			propsFile.setEnabled(enabled);
			rbListLabel.setEnabled(enabled);
			rbCombo.setEnabled(enabled);
			tagsTable.setEnabled(enabled);
	}
	
	/**
	 * Gets <code>key=value</code> pair
	 * 
	 * @return a pair <code>\nkey=value\n</code>
	 */
	public String getKeyValuePair() {
		return "\n" + propsKey.getText() + Constants.EQUAL + propsValue.getText() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Gets resource bundle's file
	 * @return the file
	 */
	public IFile getBundleFile() {
		return bm.getBundleFile(rbCombo.getText());
	}
	
	/**
	 * Check if "Create new file.." option is enabled
	 * 
	 * @return the status
	 */
	public boolean isNewFile() {
		return newFile.getSelection();
	}
	
	/**
	 * Replaces the text in the current file
	 */
	public void replaceText() {
		IDocumentProvider prov = editor.getDocumentProvider();
		IDocument doc = prov.getDocument(editor.getEditorInput());
		ISelection sel = editor.getSelectionProvider().getSelection();
		if (isSelectionCorrect(sel)) {
			try {
				/*
				 * Get source text and new text
				 */
				TextSelection textSel = (TextSelection) sel;
				IStructuredSelection structuredSelection = (IStructuredSelection) sel;
				Object firstElement = structuredSelection.getFirstElement();
				int offset = 0;
				int length = 0;
				/*
				 * When user selection is empty 
				 * underlying node will e automatically selected.
				 * Thus we need to correct replacement offsets. 
				 */
				if ((textSel.getLength() != 0)) {
					offset = textSel.getOffset();
					length = textSel.getLength();
				} else if (firstElement instanceof TextImpl) {
					TextImpl ti = (TextImpl) firstElement;
					offset = ti.getStartOffset();
					length = ti.getLength();
				} else if (firstElement instanceof AttrImpl) {
					AttrImpl ai = (AttrImpl) firstElement;
					/*
					 * Get offset and length without quotes ".."
					 */
					offset = ai.getValueRegionStartOffset() + 1;
					length = ai.getValueRegionText().length() - 2;
				}
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
				doc.replace(offset, length, newText);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Update duplicate key status.
	 */
	private void updateDuplicateKeyStatus() {
		if (isDuplicatedKey(propsKey.getText())) {
			rbComboStatus = new Status(
					IStatus.ERROR,
					VpePlugin.PLUGIN_ID,
					VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_KEY_ALREADY_EXISTS); 
		} else {
			rbComboStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		}
	}

	private void updatePropertiesValueStatus() {
		String text = propsValue.getText();
		if ((text == null)
				|| (Constants.EMPTY.equalsIgnoreCase(text.trim()))
				|| (text.indexOf(Constants.GT) != -1) 
				||  (text.indexOf(Constants.LT) != -1)) {
			propsValueStatus = new Status(
					IStatus.ERROR,
					VpePlugin.PLUGIN_ID,
					VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_WRONG_SELECTED_TEXT);
		} else {
			propsValueStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		}
	}
	
	/**
	 * Update properties key status.
	 */
	private void updatePropertiesKeyStatus() {
		if ((propsKey.getText() == null) 
				|| (Constants.EMPTY.equalsIgnoreCase(propsKey.getText().trim()))) {
			propsKeyStatus = new Status(
					IStatus.ERROR,
					VpePlugin.PLUGIN_ID,
					VpeUIMessages.EXTERNALIZE_STRINGS_DIALOG_KEY_MUST_BE_SET);
		} else {
			propsKeyStatus = new Status(IStatus.OK, VpePlugin.PLUGIN_ID, Constants.EMPTY);
		}
	}
	
	/**
	 * Update page status.
	 */
	private void updateStatus() {
		/*
		 * Update all statuses
		 */
		updatePropertiesKeyStatus();
		updatePropertiesValueStatus();
		updateDuplicateKeyStatus();
		/*
		 * Apply status to the dialog
		 */
		applyStatus(this, new IStatus[] {propsKeyStatus, propsValueStatus, rbComboStatus});
		/*
		 * Set page complete
		 */
		setPageComplete(isPageComplete());
	}
	
	/**
	 * Apply status to the dialog.
	 *
	 * @param page the page
	 * @param statuses all the statuses
	 */
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

	/**
	 * Creates new bundle map if no one was specified 
	 * during initialization of the page.
	 *
	 * @param editor the source editor 
	 * @return the new bundle map
	 */
	private BundleMap createBundleMap(StructuredTextEditor editor) {
		String uri = null;
		String prefix = null;
		int hash;
		Map<?, ?> map = null;
		BundleMap bm = new BundleMap();
		bm.init(editor);

		/*
		 * Check JSF Nature
		 */
		boolean hasJsfProjectNatureType = false;
		try {
			IEditorInput ei = editor.getEditorInput();
			if(ei instanceof IFileEditorInput) {
				IProject project = ((IFileEditorInput)ei).getFile().getProject();
				if (project.exists() && project.isOpen()) {
					if (project.hasNature(WebProject.JSF_NATURE_ID)) {
						hasJsfProjectNatureType = true;
					}
				}
			}
		} catch (CoreException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		/*
		 * Get Bundles from faces-config.xml
		 */
		if (hasJsfProjectNatureType
				&& (editor.getEditorInput() instanceof IFileEditorInput)) {
			IProject project = ((IFileEditorInput) editor.getEditorInput())
			.getFile().getProject();
			IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
			if (modelNature != null) {
				XModel model = modelNature.getModel();
				List<Object> l = WebPromptingProvider.getInstance().getList(model,
						WebPromptingProvider.JSF_REGISTERED_BUNDLES, null, null);
				if (l != null && l.size() > 1 && (l.get(1) instanceof Map)) {
					map = (Map<?, ?>) l.get(1);
					if ((null != map) && (map.keySet().size() > 0)) {
						Iterator<?> it = map.keySet().iterator();
						while (it.hasNext()) {
							uri = it.next().toString();
							prefix = map.get(uri).toString();
							hash = (prefix + ":" + uri).hashCode(); //$NON-NLS-1$
							bm.addBundle(hash, prefix, uri, false);
						}
					}
				} 
			}
		}
		/*
		 * Add bundles from <f:loadBundle> tags
		 */
		ISourceEditingTextTools sourceEditingTextTools = 
			(ISourceEditingTextTools) editor
				.getAdapter(ISourceEditingTextTools.class);
		IDOMSourceEditingTextTools domSourceEditingTextTools = 
			(IDOMSourceEditingTextTools) sourceEditingTextTools;
		Document doc = domSourceEditingTextTools.getDOMDocument();
		NodeList list = doc.getElementsByTagName("f:loadBundle"); //$NON-NLS-1$
		for (int i = 0; i < list.getLength(); i++) {
			Element node = (Element) list.item(i);
			uri = node.getAttribute("basename"); //$NON-NLS-1$
			prefix = node.getAttribute("var"); //$NON-NLS-1$
			hash = node.hashCode();
			bm.addBundle(hash, prefix, uri, false);
		}
		return bm;
	}
	
}

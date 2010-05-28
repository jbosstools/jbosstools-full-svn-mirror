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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.bundle.BundleMap.BundleEntry;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Attr;

public class ExternalizeStringsDialog extends TitleAreaDialog {

	private final int DIALOG_WIDTH = 450;
	private final int DIALOG_HEIGHT = 300;
	private VpeController vpeController;
	private Text textStringValue;
	private Text propsKey;
	private Text propsValue;
	private Text propsFile;
	private Combo rbCombo;
	private BundleMap bm;
	
	public ExternalizeStringsDialog(Shell parentShell,
			VpeController vpeController) {
		super(parentShell);
		this.vpeController = vpeController;
		this.bm = vpeController.getPageContext().getBundle();
		setHelpAvailable(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		/*
		 * Setting dialog Title, Message, Image.
		 */
		getShell().setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_TITLE);
		setTitle(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_TITLE);
		setTitleImage(ModelUIImages.getImageDescriptor(
				ModelUIImages.WIZARD_DEFAULT).createImage(null));
		setMessage(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_DESCRIPTION);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = DIALOG_WIDTH;
		gd.heightHint = DIALOG_HEIGHT;
		composite.setLayoutData(gd);

		/*
		 * Create dialog area separator
		 */
		Label dialogAreaSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		dialogAreaSeparator.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
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
		textStringValue.setText("Not initialized"); //$NON-NLS-1$
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
		propsKey.setText("key"); //$NON-NLS-1$
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
		propsValue.setText("value"); //$NON-NLS-1$

		/*
		 * Create properties string group
		 */
		Group propsFilesGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		propsFilesGroup.setLayout(new GridLayout(3, false));
		propsFilesGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		propsFilesGroup.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_PROPS_FILES_GROUP);
		
		/*
		 * Create Resource Bundles List label
		 */
		Label rbListLabel = new Label(propsFilesGroup, SWT.NONE);
		rbListLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		rbListLabel.setText(VpeUIMessages.EXTRNALIZE_STRINGS_DIALOG_RESOURCE_BUNDLE_LIST);
		/*
		 * Create Resource Bundles combobox
		 */
		rbCombo = new Combo(propsFilesGroup, SWT.NONE);
		rbCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
//		rbCombo.add(Constants.EMPTY);
		rbCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IFile bundleFile = bm.getBundleFile(rbCombo.getText());
				String bundlePath = Constants.EMPTY;
				if (bundleFile != null) {
					bundlePath = bundleFile.getFullPath().toString();
				} 
				propsFile.setText(bundlePath);
			}
		});
		
		/*
		 * Create Properties File label
		 */
		Label propsFileLabel = new Label(propsFilesGroup, SWT.NONE);
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
		 * Initialize all fields with real values.
		 */
		initializeTextFields();

		return composite;
	}

	private void initializeTextFields() {
		ISelection sel = vpeController.getSourceEditor().getSelectionProvider()
				.getSelection();
		if ((textStringValue != null) && (propsKey != null)
				&& isSelectionCorrect(sel)) {
			String stringToUpdate = Constants.EMPTY;
			TextSelection textSelection = null;
			String text = null;
			IStructuredSelection structuredSelection = (IStructuredSelection) sel;
			textSelection = (TextSelection) sel;
			text = textSelection.getText();
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

	@Override
	protected void okPressed() {
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
				String newText = "\n" + propsKey.getText() + Constants.EQUAL + propsValue.getText() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
				/*
				 * Add "key=value" to the bundle
				 */
				IFile bundleFile = bm.getBundleFile(rbCombo.getText());
				if ((bundleFile != null) && (bundleFile.exists())) {
					InputStream is = new ByteArrayInputStream(newText.getBytes());
					bundleFile.appendContents(is, false, true, null);
				} 
				/*
				 * Replace text in the editor with "key.value"
				 */
				String bundlePrefix = Constants.EMPTY;
				for (BundleEntry be : bm.getBundles()) {
					if (be.uri.equalsIgnoreCase(rbCombo.getText())) {
						bundlePrefix = be.prefix;
					}
				}
				newText = "#{" + bundlePrefix + Constants.DOT + propsKey.getText() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
				doc.replace(textSel.getOffset(), textSel.getLength(), newText);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		super.okPressed();
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

}

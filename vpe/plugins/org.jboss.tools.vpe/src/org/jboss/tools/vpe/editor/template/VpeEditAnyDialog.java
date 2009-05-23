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
package org.jboss.tools.vpe.editor.template;

import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSStyleDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.Message;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

/**
 * Class which presents dialog for any template
 * @author mareshkau
 *
 */
public class VpeEditAnyDialog extends TitleAreaDialog {

	private VpeAnyData data;
	private CheckControl ctlChildren;
	private Text txtTagForDisplay;
	private Text txtValue;
	private Text txtStyle;
    private VpeEditAnyDialogValidator templateVerifier;

	public VpeEditAnyDialog(Shell shell, VpeAnyData data) {
		super(shell);
		this.data = data;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		templateVerifier = new VpeEditAnyDialogValidator();
		getShell().setText(VpeUIMessages.TEMPLATE);
		setTitle(VpeUIMessages.TAG_ATTRIBUTES);

		setMessage(getDefaultMessage());

		Composite topComposite = (Composite)super.createDialogArea(parent);
		((GridData)topComposite.getLayoutData()).widthHint = 300;

		final Composite composite = new Composite(topComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginWidth = 50;
		gridLayout.marginHeight = 20;
		gridLayout.horizontalSpacing = 5;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		composite.setBackground(parent.getBackground());
		composite.setForeground(parent.getForeground());
		composite.setFont(parent.getFont());
		
	    //added by estherbin https://jira.jboss.org/jira/browse/JBIDE-2521
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        Label tagFDisplayLabel = makeLabel(composite, VpeUIMessages.TAG_FOR_DISPLAY);
        
        tagFDisplayLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        txtTagForDisplay = new Text(composite,SWT.BORDER);
        gd.horizontalSpan=2;
        txtTagForDisplay.setLayoutData(gd);
        txtTagForDisplay.setText(data.getTagForDisplay() != null ? data.getTagForDisplay() : ""); //$NON-NLS-1$
        txtTagForDisplay.addModifyListener(templateVerifier);
//        txtTagForDisplay.select(tagNameItemIndex);
        
        
//		ctlCaseSensitive = new CheckControl(composite, "Case sensitive", data.isCaseSensitive());
		ctlChildren = new CheckControl(composite, VpeUIMessages.CHILDREN, data.isChildren());
//		ctlModify = new CheckControl(composite, "Modify", data.isModify());
			
		//value control
		Label lblValue = makeLabel(composite, VpeUIMessages.VALUE);
		lblValue.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		txtValue = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtValue.setLayoutData(gd);
		txtValue.setText(data.getValue() != null ? data.getValue() : ""); //$NON-NLS-1$
		txtValue.addModifyListener(templateVerifier);

		//style control
		Label lbStyle = makeLabel(composite, VpeUIMessages.STYLE);
		lbStyle.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		txtStyle =  new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		txtStyle.setLayoutData(gd);
		txtStyle.setText(data.getStyle() !=null ? data.getStyle() : ""); //$NON-NLS-1$
	
		Button button = new Button(composite, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setToolTipText(VpeUIMessages.BACKGROUND_COLOR_TIP);
		ImageDescriptor colorDesc = JspEditorPlugin
			.getImageDescriptor(Constants.IMAGE_COLORLARGE_FILE_LOCATION);
		Image im = colorDesc.createImage();
		button.setImage(im);
		
		button.addDisposeListener(new DisposeListener() {
		    public void widgetDisposed(DisposeEvent e) {
			Button button = (Button) e.getSource();
			button.getImage().dispose();
		    }
		});
		button.addSelectionListener(new SelectionAdapter() {
		    @Override
			public void widgetSelected(SelectionEvent event) {
		    	CSSStyleDialog cssDialog = new CSSStyleDialog(composite.getShell(),txtStyle.getText());
				if (cssDialog.open() == Window.OK) {
				    txtStyle.setText(cssDialog.getStyle());
				}
		    }
		});

		return composite;
	}
	
	

	@Override
	public void create() {
		super.create();
		templateVerifier.validateAll(false);
	}

	private IMessageProvider getDefaultMessage() {
		final String message = (data.getUri() != null 
					? (MessageFormat.format(VpeUIMessages.TAG_URI, data.getUri()) + "\n") //$NON-NLS-1$ 
					: "") //$NON-NLS-1$
				+ MessageFormat.format(VpeUIMessages.TAG_NAME, data.getName()); 
		return new Message(message, IMessageProvider.NONE);
	}

	private Label makeLabel(Composite parent, String text) {
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setText(text);
		lbl.setBackground(parent.getBackground());
		return lbl;
	}

	@Override
	protected void okPressed() {

		data.setChanged(data.isChanged() || (data.isChildren() != ctlChildren.getSelection()));
		data.setChildren(ctlChildren.getSelection());

		data.setChanged(isChanged(data,data.getTagForDisplay(),txtTagForDisplay.getText()));
		data.setTagForDisplay(txtTagForDisplay.getText().trim());
		
		data.setChanged(isChanged(data, data.getValue(), txtValue.getText()));
		data.setValue(txtValue.getText().trim());

		data.setChanged(isChanged(data, data.getStyle(), txtStyle.getText()));
		data.setStyle(txtStyle.getText());
		
		super.okPressed();
	}

	private boolean isChanged(VpeAnyData data, String oldValue, String newValue) {
		boolean isChanged = false;
		if (oldValue == null) oldValue = ""; //$NON-NLS-1$
		if (newValue == null) newValue = ""; //$NON-NLS-1$
		if (data.isCaseSensitive()) {
			isChanged = !oldValue.trim().equals(newValue.trim());
		} else {
			isChanged = !oldValue.trim().equalsIgnoreCase(newValue.trim());
		}
		return data.isChanged() || isChanged;
	}

	private boolean isChanged(VpeAnyData data, boolean oldValue, boolean newValue) {
		return data.isChanged() || (oldValue != newValue);
	}

	/**
	 * Sets the message for this dialog with an indication of what type of
	 * message it is.
	 * <p>
	 * @param message the message, or <code>null</code> to clear the message
	 */
	public void setMessage(IMessageProvider message) {
		if (message == null) {
			setMessage(null, IMessageProvider.NONE);
		} else {
			setMessage(message.getMessage(), message.getMessageType());			
		}
	}

	private class CheckControl {
		private Label label;
		private Button button;

		public CheckControl(Composite parent, String labelText, boolean value) {
			label = new Label(parent, SWT.NONE);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			label.setText(labelText);
			button = new Button(parent, SWT.CHECK);
			GridData gd = new GridData(GridData.BEGINNING);
			gd.horizontalSpan = 2;
			button.setLayoutData(gd);
			button.setSelection(value);
		}

		public void setVisible(boolean visible) {
			label.setVisible(visible);
			button.setVisible(visible);
		}

		public boolean getSelection() {
			return button.getSelection();
		}
	}

	/**
	 * Validator of {@link VpeEditAnyDialog}.
	 * 
	 * @author yradtsevich
	 */
	private class VpeEditAnyDialogValidator implements ModifyListener {
		/**
		 * Used to validate tag-names.
		 */
		private Document xmlDocument = null;
		public VpeEditAnyDialogValidator() {
			try {
				xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			} catch (ParserConfigurationException e) {
				VpePlugin.getPluginLog().logError(e);
			}
		}

		/**
		 * Validates {@link VpeEditAnyDialog#txtTagForDisplay} field.
		 *
		 * @return {@code null} if it is valid or an instance of {@code IMessageProvider} if
		 * it contains any error.
		 */
		private IMessageProvider validateTagForDisplay() {
			if (xmlDocument != null) {
				try {
					xmlDocument.createElement(txtTagForDisplay.getText());
				} catch (DOMException e) {
					return new Message(
							MessageFormat.format(VpeUIMessages.TAG_FOR_DISPLAY_IS_NOT_VALID,
												e.getMessage()),
							IMessageProvider.ERROR);
				}
			}

			return null;
		}

		/**
		 * Validates {@link VpeEditAnyDialog#txtValue} field.
		 *
		 * @return {@code null} if it is valid or an instance of {@code IMessageProvider} if
		 * it contains any error.
		 */
		private IMessageProvider validateValue() {

			try {
				VpeExpressionBuilder.buildCompletedExpression(txtValue.getText(), true);
			} catch (VpeExpressionBuilderException e) {
				return new Message(
						MessageFormat.format(VpeUIMessages.VALUE_IS_NOT_VALID,
											e.getMessage()),
						IMessageProvider.ERROR);
			}

			return null;
		}

		/**
		 * Validates all fields of {@link VpeEditAnyDialog} and changes the view of
		 * dialog according to validation results.
		 * 
		 * @param updateMessage if it is {@code true}, the dialog's message will be updated.
		 */
		void validateAll(boolean updateMessage) {
			IMessageProvider message = VpeEditAnyDialog.this.getDefaultMessage();

			IMessageProvider tagForDisplayMessage = validateTagForDisplay();
			IMessageProvider valueMessage = validateValue();

			if (tagForDisplayMessage != null) {
				message = tagForDisplayMessage;
			} else if (valueMessage != null) {
				message = valueMessage;
			}

			Button okButton = getButton(IDialogConstants.OK_ID);
			if (message.getMessageType() <= IMessageProvider.INFORMATION) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}

			if (updateMessage) {
				VpeEditAnyDialog.this.setMessage(message);
			}
		}

		/**
		 * Fired when a field is modified.
		 */
		public void modifyText(ModifyEvent e) {
			validateAll(true);
		}
	}
}

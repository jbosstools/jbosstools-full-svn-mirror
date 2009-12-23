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

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
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
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSStyleDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.Message;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.Activator;
import org.jboss.tools.vpe.resref.core.Messages;
import org.osgi.framework.Bundle;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

/**
 * Class which presents dialog for any template
 * @author mareshkau
 *
 */
public class VpeEditAnyDialog extends TitleAreaDialog {

	private VpeAnyData data;
	Text tagName;
	Text tagUri;
	private Button childrenCheckbox;
	private Text txtTagForDisplay;
	private Text txtValue;
	private Text txtStyle;
    private VpeEditAnyDialogValidator templateVerifier;
    private final String DIALOG_TITLE_IMAGE_PATH = "/images/xstudio/wizards/EclipseDefault.png"; //$NON-NLS-1$

	public VpeEditAnyDialog(Shell shell, VpeAnyData data) {
		super(shell);
		this.data = data;
		setHelpAvailable(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		/*
		 * Setting dialog Title, Message, Image.
		 */
		Bundle bundle = Platform.getBundle(ModelUIPlugin.PLUGIN_ID);
		URL url = null;
		if (null != bundle) {
			try {
				url = FileLocator.resolve(bundle.getEntry(DIALOG_TITLE_IMAGE_PATH));
			} catch (IOException e) {
				Activator.getDefault().logError(
						NLS.bind(Messages.VRD_TITLE_IMAGE_CANNOT_BE_RESOLVED,
								url), e);
			}
		}
		if (null != url) {
			ImageDescriptor image = ImageDescriptor.createFromURL(url);
			setTitleImage(image.createImage(null));
		}
		getShell().setText(VpeUIMessages.TEMPLATE);
		setTitle(VpeUIMessages.TAG_ATTRIBUTES);
		setMessage(VpeUIMessages.UNKNOWN_TAGS_DIALOG_DESCRIPTION);
		
		/*
		 * Create validator
		 */
		templateVerifier = new VpeEditAnyDialogValidator();
		
		/*
		 * Create composites.
		 */
		Composite topComposite = (Composite)super.createDialogArea(parent);
		((GridData)topComposite.getLayoutData()).widthHint = 300;

		final Composite composite = new Composite(topComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginWidth = 50;
		gridLayout.marginHeight = 20;
		gridLayout.horizontalSpacing = 5;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		composite.setBackground(parent.getBackground());
		composite.setForeground(parent.getForeground());
		composite.setFont(parent.getFont());
		
		/*
		 * Create Tag Name label
		 */
		Label tagNameLabel = new Label(composite, SWT.NONE);
		tagNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		tagNameLabel.setText(VpeUIMessages.TAG_NAME);
		
		/*
		 * Create Tag Name value
		 */
		tagName = new Text(composite, SWT.BORDER);
		tagName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		String text = Constants.EMPTY;
		if ((data != null) && (data.getName() != null)){
			text = data.getName();
		}
		tagName.setText(text);
		tagName.addModifyListener(templateVerifier);
		
		/*
		 * Create Tag URI label
		 */
		Label tagUriLabel = new Label(composite, SWT.NONE);
		tagUriLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		tagUriLabel.setText(VpeUIMessages.TAG_URI);
		
		/*
		 * Create Tag URI value
		 */
		tagUri = new Text(composite, SWT.BORDER);
		tagUri.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		text = Constants.EMPTY;
		if ((data != null) && (data.getUri() != null)) {
			text = data.getUri();
		}
		tagUri.setText(text);
		tagUri.addModifyListener(templateVerifier);

		/*
		 * Create Tag for display label
		 */
        Label tagForDisplayLabel = new Label(composite, SWT.NONE); 
        tagForDisplayLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
        tagForDisplayLabel.setText(VpeUIMessages.TAG_FOR_DISPLAY);
        
        /*
		 * Create Tag for display value
		 */
        txtTagForDisplay = new Text(composite, SWT.BORDER);
        txtTagForDisplay.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
        text = Constants.EMPTY;
		if ((data != null) && (data.getTagForDisplay() != null)) {
			text = data.getTagForDisplay();
		}
        txtTagForDisplay.setText(text);
        txtTagForDisplay.addModifyListener(templateVerifier);
        
        /*
         * Create Children label
         */
        Label childrenCheckboxLabel = new Label(composite, SWT.NONE);
        childrenCheckboxLabel.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
        childrenCheckboxLabel.setText(VpeUIMessages.CHILDREN);
		
        /*
         * Create check box for Children
         */
        childrenCheckbox = new Button(composite, SWT.CHECK);
        childrenCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, true, false, 2, 1));
        childrenCheckbox.setSelection(data.isChildren());
        
		/*
		 * Create value label
		 */
		Label lblValue = new Label(composite, SWT.NONE); 
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		lblValue.setText( VpeUIMessages.VALUE);
		
		/*
		 * Create value
		 */
		txtValue = new Text(composite, SWT.BORDER);
		txtValue.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		text = Constants.EMPTY;
		if ((data != null) && (data.getValue() != null)) {
			text = data.getValue();
		}
		txtValue.setText(text);
		txtValue.addModifyListener(templateVerifier);

		/*
		 * Create style label
		 */
		Label lbStyle = new Label(composite, SWT.NONE); 
		lbStyle.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		lbStyle.setText(VpeUIMessages.TAG_STYLE);
		
		/*
		 * Create style value
		 */
		txtStyle =  new Text(composite, SWT.BORDER);
		txtStyle.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		 text = Constants.EMPTY;
		if ((data != null) && (data.getStyle() != null)) {
			text = data.getStyle();
		}
		txtStyle.setText(text);
	
		/*
		 * Create style button
		 */
		Button button = new Button(composite, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
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

	@Override
	protected void okPressed() {
		boolean isChanged = false;
		if ((data.isChildren() != childrenCheckbox.getSelection())
				|| isChanged(data, data.getName(), tagName.getText())
				|| isChanged(data, data.getUri(), tagUri.getText())
				|| isChanged(data, data.getTagForDisplay(), txtTagForDisplay.getText())
				|| isChanged(data, data.getValue(), txtValue.getText())
				|| isChanged(data, data.getStyle(), txtStyle.getText())) {
			isChanged = true;
		}
		
		data.setChanged(isChanged);
		
		data.setChildren(childrenCheckbox.getSelection());
		data.setName(tagName.getText().trim());
		data.setUri(tagUri.getText().trim());
		data.setTagForDisplay(txtTagForDisplay.getText().trim());
		data.setValue(txtValue.getText().trim());
		data.setStyle(txtStyle.getText());
		
		super.okPressed();
	}

	private boolean isChanged(VpeAnyData data, String oldValue, String newValue) {
		boolean isChanged = false;
		if (oldValue == null) {
			oldValue = Constants.EMPTY;
		}
		if (newValue == null) {
			newValue = Constants.EMPTY;
		}
		if (data.isCaseSensitive()) {
			isChanged = !oldValue.trim().equals(newValue.trim());
		} else {
			isChanged = !oldValue.trim().equalsIgnoreCase(newValue.trim());
		}
		return isChanged;
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
		 * Validates tag name.
		 * <p>
		 * Prefix should exist. 
		 * <p>
		 * Otherwise template won't be saved correctly to auto-templates.xml.
		 * 
		 * @return message is validation failed, null otherwise.
		 */
		private IMessageProvider validateTagName() {
			Message message = null;
			if (tagName.getText().indexOf(":") < 0) { //$NON-NLS-1$
				message = new Message(
						MessageFormat.format(VpeUIMessages.TAG_NAME_IS_NOT_VALID,
								tagName.getText().trim()),
			IMessageProvider.ERROR);
			}
			return message;
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
			IMessageProvider message = null;

			IMessageProvider tagForDisplayMessage = validateTagForDisplay();
			IMessageProvider valueMessage = validateValue();
			IMessageProvider tagNameMessage = validateTagName();

			if (tagNameMessage != null) {
				message = tagNameMessage;
			} else if (tagForDisplayMessage != null) {
				message = tagForDisplayMessage;
			} else if (valueMessage != null) {
				message = valueMessage;
			} else {
				/*
				 * If everything is OK - set default message
				 */
				setMessage(VpeUIMessages.UNKNOWN_TAGS_DIALOG_DESCRIPTION);
			}

			Button okButton = getButton(IDialogConstants.OK_ID);
			if ((message == null) || (message.getMessageType() <= IMessageProvider.INFORMATION)) {
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

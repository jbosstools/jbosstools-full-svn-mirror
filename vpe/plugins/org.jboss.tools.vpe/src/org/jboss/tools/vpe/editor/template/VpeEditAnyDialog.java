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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.css.CSSPlugin;
import org.jboss.tools.jst.css.dialog.CSSStyleDialog;
import org.jboss.tools.jst.css.dialog.common.Util;
import org.jboss.tools.jst.jsp.util.Constants;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.Message;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
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

	/*
	 * Current template
	 */
	private VpeAnyData data;
	/*
	 * List of already created templates
	 */
	List<VpeAnyData> tagsList;
	Map<String, VpeAnyData> tagsNamesSet = new HashMap<String, VpeAnyData>();
	Text tagName;
	Text tagUri;
	private String previousUri = Constants.EMPTY;
	private Button childrenCheckbox;
	private Text txtTagForDisplay;
	private Text txtValue;
	private Text txtStyle;
    private VpeEditAnyDialogValidator templateVerifier;

	public VpeEditAnyDialog(Shell shell, VpeAnyData data, List<VpeAnyData> tagsList) {
		super(shell);
		this.data = data;
		this.tagsList = tagsList; 
		if (null != tagsList) {
			for (VpeAnyData tag : tagsList) {
				tagsNamesSet.put(tag.getName(), tag);
			}
		}
		setHelpAvailable(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		/*
		 * Setting dialog Title, Message, Image.
		 */
		getShell().setText(VpeUIMessages.TEMPLATE);
		setTitle(VpeUIMessages.TAG_ATTRIBUTES);
		setTitleImage(ModelUIImages.getImage(ModelUIImages.WIZARD_DEFAULT)); //image is managed by registry
		setMessage(VpeUIMessages.USER_SPECIFIED_TAG_DIALOG_DESCRIPTION);
		
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
		button.setToolTipText(VpeUIMessages.EDIT_STYLE_TIP);
		ImageDescriptor colorDesc = CSSPlugin
			.getImageDescriptor(Util.IMAGE_COLORLARGE_FILE_LOCATION);
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
			String name = tagName.getText().trim();
			String[] parts = name.split(":"); //$NON-NLS-1$
			Pattern p = Pattern.compile("([a-zA-Z]+\\d*)+"); //$NON-NLS-1$
			/*
			 * Reset editable property for template URI
			 * after it has been disabled.
			 */
			if (!tagUri.getEditable()) {
				tagUri.removeModifyListener(templateVerifier);
				if ((null != data) && (null != data.getName()) &&
						data.getName().split(":")[0].equalsIgnoreCase(parts[0])) { //$NON-NLS-1$
					/*
					 * Restore initial URI
					 */
					previousUri = data.getUri();
				} 
				tagUri.setText(previousUri);
				tagUri.setEditable(true);
				tagUri.addModifyListener(templateVerifier);
			}
			/*
			 * Check if tag's name is correct
			 */
			if (parts.length != 2 || name.startsWith(":") //$NON-NLS-1$
					|| name.endsWith(":")) { //$NON-NLS-1$
				message = new Message(
						MessageFormat.format(VpeUIMessages.TAG_NAME_IS_NOT_VALID,
								name), IMessageProvider.ERROR);
			} else if ((parts[0].length() == 0) || (parts[1].length() == 0)
						|| (!p.matcher(parts[0]).matches()) 
						|| (!p.matcher(parts[1]).matches())) {
					/*
					 * Matcher will accept only word characters with optional numbers.
					 */
					message = new Message(
							MessageFormat.format(VpeUIMessages.TAG_NAME_IS_NOT_VALID,
									name), IMessageProvider.ERROR);
			} else if (tagsNamesSet.keySet().contains(name)
					&& (!tagsList.contains(data) 
						|| (tagsList.contains(data) && !data.getName().equalsIgnoreCase(name)))) {
				/*
				 * Find duplicate tag names.
				 */
				message = new Message(
						MessageFormat.format(VpeUIMessages.TAG_NAME_ALREADY_EXISTS,
								name), IMessageProvider.ERROR);
			} else {
				/*
				 * Inform that URI for the specified taglib namespace
				 * is already defined in another templates.
				 */
				for (String templateName : tagsNamesSet.keySet()) {
					if (parts[0].equalsIgnoreCase(templateName.split(":")[0]) //$NON-NLS-1$
							&& (!tagsList.contains(data) || (tagsList.contains(data) && !data.getName().split(":")[0].equalsIgnoreCase(parts[0])))) { //$NON-NLS-1$
						message = new Message(
						MessageFormat.format(VpeUIMessages.URI_TAGLIB_NAMESPACE_ALREADY_DEFINED,
								parts[0], tagsNamesSet.get(templateName).getUri()), IMessageProvider.WARNING);
						/*
						 * Set the URI and disable this field.
						 * Remove the listener to avoid dead lock
						 */
						tagUri.removeModifyListener(templateVerifier);
						previousUri = tagUri.getText();
						tagUri.setText(tagsNamesSet.get(templateName).getUri());
						tagUri.setEditable(false);
						tagUri.addModifyListener(templateVerifier);
						break;
					}
				}
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
					/*
					 * https://jira.jboss.org/browse/JBIDE-6599
					 * Changing warning message.
					 */
					return new Message(
							MessageFormat.format(VpeUIMessages.TAG_FOR_DISPLAY_IS_NOT_VALID,
									txtTagForDisplay.getText()),
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
			/*
			 * Initialize the message with the description
			 */
			IMessageProvider message = new Message(VpeUIMessages.USER_SPECIFIED_TAG_DIALOG_DESCRIPTION,
					IMessageProvider.NONE);
			List<IMessageProvider> statuses = new ArrayList<IMessageProvider>();
			/*
			 * Get messages from all validators
			 */
			statuses.add(validateTagForDisplay());
			statuses.add(validateValue());
			statuses.add(validateTagName());
			/*
			 * Find the message with the most severe status
			 */
			for (IMessageProvider status : statuses) {
				if (null != status) {
					message = message.getMessageType() >= status.getMessageType()
					? message : status;
				}
			}
			String messageText = message.getMessage();
			switch (message.getMessageType()) {
			case IMessageProvider.NONE:
				setMessage(messageText, IMessageProvider.NONE);
				setErrorMessage(null);
				break;

			case IMessageProvider.WARNING:
				setMessage(messageText, IMessageProvider.WARNING);
				setErrorMessage(null);
				break;

			case IMessageProvider.INFORMATION:
				setMessage(messageText, IMessageProvider.INFORMATION);
				setErrorMessage(null);
				break;

			default:
				/*
				 * Set ERROR message
				 */
				if (messageText.length() == 0) {
					messageText = null;
				}
				setMessage(null, IMessageProvider.NONE);
				setErrorMessage(messageText);
				break;
			} // end switch
			
			Button okButton = getButton(IDialogConstants.OK_ID);
			if ((message == null) || (message.getMessageType() <= IMessageProvider.WARNING)) {
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

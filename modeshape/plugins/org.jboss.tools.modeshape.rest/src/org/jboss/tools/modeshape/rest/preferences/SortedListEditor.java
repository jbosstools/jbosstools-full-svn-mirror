/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.rest.preferences;

import java.util.Arrays;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.modeshape.common.util.CheckArg;

/**
 * A preference list string value editor that sorts its entries each time a
 * value is added.
 */
public abstract class SortedListEditor extends ListEditor {

	/**
	 * Indicates if duplicate values are allowed.
	 */
	private boolean allowDuplicates;

	/**
	 * The listener verifying input characters.
	 */
	private VerifyListener verifyListener;

	/**
	 * Duplicate items are not allowed.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public SortedListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	private void disableUpDownButtons() {
		// disable up button
		Button button = getUpButton();

		if (button.getEnabled()) {
			button.setEnabled(false);
		}

		// disable down button
		button = getDownButton();

		if (button.getEnabled()) {
			button.setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.ListEditor#getButtonBoxControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite getButtonBoxControl(Composite parent) {
		Composite buttonBox = super.getButtonBoxControl(parent);
		getUpButton().setVisible(false);
		getDownButton().setVisible(false);
		disableUpDownButtons();
		return buttonBox;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
	 */
	@Override
	protected final String getNewInputObject() {
		NewItemDialog dialog = new NewItemDialog(getShell(), getNewItemDialogTitle(), getNewItemDialogLabel(),
		        this.verifyListener);
		if (!this.allowDuplicates) {
			dialog.setDisallowedValues(getList().getItems());
		}

		if (dialog.open() == Window.OK) {
			String newItem = dialog.getNewItem();

			// add new item, sort, and update list
			if (newItem != null) {
				getList().add(newItem);
				String[] items = getList().getItems();
				Arrays.sort(items);
				getList().setItems(items);
			}
		}

		// always return null because we have already updated the list
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.ListEditor#selectionChanged()
	 */
	@Override
	protected void selectionChanged() {
		super.selectionChanged();
		disableUpDownButtons();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.ListEditor#setEnabled(boolean,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		disableUpDownButtons();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.ListEditor#setFocus()
	 */
	@Override
	public void setFocus() {
		if ((getList() == null) || getList().getItemCount() == 0) {
			getAddButton().setFocus();
		} else {
			super.setFocus();
		}
	}

	/**
	 * @return the localized dialog title used when a new item is added (may not
	 *         be <code>null</code>)
	 */
	protected abstract String getNewItemDialogTitle();

	/**
	 * @return the localized label on the new item dialog that identifies the
	 *         type of the new item (may not be <code>null</code>)
	 */
	protected abstract String getNewItemDialogLabel();

	/**
	 * @param verifyListener
	 *            a listener that verifies input from the user of a new item
	 *            entry (can be <code>null</code>)
	 */
	protected void setVerifyListener(VerifyListener verifyListener) {
		this.verifyListener = verifyListener;
	}

	/**
	 * A <code>NewItemDialog</code> allows the user to enter a text value.
	 * Caller can optionally furnish a list of disallowed values.
	 */
	class NewItemDialog extends Dialog implements ModifyListener {

		/**
		 * The label describing the new item.
		 */
		private final String label;

		/**
		 * A list of disallowed values. If <code>null</code> any non-empty value
		 * is allowed.
		 */
		private String[] disallowedValues;

		/**
		 * A message for the user.
		 */
		private CLabel lblMessage;

		/**
		 * The contents of the new item text field.
		 */
		private String newItem;

		/**
		 * The dialog title.
		 */
		private final String title;

		/**
		 * The listener verifying input characters.
		 */
		private final VerifyListener verifyListener;

		/**
		 * @param parentShell
		 *            the parent shell (may be <code>null</code>)
		 * @param title
		 *            the localized dialog title (never <code>null</code>)
		 * @param label
		 *            the localized label (never <code>null</code>)
		 * @param verifyListener
		 *            a listener that validates input characters (may be
		 *            <code>null</code>)
		 */
		public NewItemDialog(Shell parentShell, String title, String label, VerifyListener verifyListener) {
			super(parentShell);

			CheckArg.isNotNull(title, "title"); //$NON-NLS-1$
			CheckArg.isNotNull(label, "label"); //$NON-NLS-1$

			this.title = title;
			this.label = label;
			this.verifyListener = verifyListener;

			setShellStyle(getShellStyle() | SWT.RESIZE);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		protected void configureShell(Shell newShell) {
			newShell.setText(this.title);
			super.configureShell(newShell);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite,
		 *      int, java.lang.String, boolean)
		 */
		@Override
		protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
			Button button = super.createButton(parent, id, label, defaultButton);

			// disable OK button initially
			if (id == OK) {
				button.setEnabled(false);
			}

			return button;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected Control createDialogArea(Composite parent) {
			Composite panel = (Composite) super.createDialogArea(parent);
			Composite pnlEditor = new Composite(panel, SWT.NONE);
			pnlEditor.setLayout(new GridLayout(2, false));
			pnlEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			Label label = new Label(pnlEditor, SWT.NONE);
			label.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false));
			label.setText(this.label);

			Text textField = new Text(pnlEditor, SWT.BORDER);
			textField.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
			textField.addModifyListener(this);

			// add listener if necessary
			if (this.verifyListener != null) {
				textField.addVerifyListener(this.verifyListener);
			}

			// add image and message labels
			this.lblMessage = new CLabel(pnlEditor, SWT.NONE);
			this.lblMessage.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
			((GridData) this.lblMessage.getLayoutData()).horizontalSpan = 2;

			return panel;
		}

		/**
		 * @return the new item or <code>null</code> if the dialog was canceled
		 */
		public String getNewItem() {
			if (getReturnCode() == OK) {
				return this.newItem;
			}

			return null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
		 */
		@Override
		protected void initializeBounds() {
			super.initializeBounds();

			// resize shell to be twice the width needed for the title (without
			// this the title maybe cropped)
			int width = (2 * convertWidthInCharsToPixels(this.title.length()));
			Rectangle rectangle = getShell().getBounds();
			getShell().setBounds(rectangle.x, rectangle.y, width, rectangle.height);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
		@Override
		public void modifyText(ModifyEvent event) {
			// clear message
			this.lblMessage.setImage(null);
			this.lblMessage.setText(""); //$NON-NLS-1$

			// enable/disable OK button
			this.newItem = ((Text) event.widget).getText();

			// make sure at least one character entered
			boolean enable = (this.newItem.length() != 0);

			// make sure value is not a disallowed value
			if (enable && (this.disallowedValues != null)) {
				for (String disallowedValue : this.disallowedValues) {
					if (this.newItem.equals(disallowedValue)) {
						enable = false;
						this.lblMessage
						        .setImage(Activator.getDefault().getSharedImage(ISharedImages.IMG_OBJS_INFO_TSK));
						this.lblMessage.setText(RestClientI18n.newItemDialogValueExists.text());
						break;
					}
				}
			}

			// set enabled state if different than current state
			if (getButton(OK).getEnabled() != enable) {
				getButton(OK).setEnabled(enable);
			}
		}

		/**
		 * A list of values that are not allowed as an input.
		 * 
		 * @param disallowedValues
		 *            the list of values which are not allowed (can be
		 *            <code>null</code> or empty)
		 */
		public void setDisallowedValues(String[] disallowedValues) {
			if ((disallowedValues == null) || (disallowedValues.length == 0)) {
				this.disallowedValues = null;
			} else {
				this.disallowedValues = disallowedValues;
			}
		}

	}
}

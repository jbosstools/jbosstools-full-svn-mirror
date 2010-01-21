/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.tools.modeshape.rest.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
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
import org.modeshape.common.util.CheckArg;

/**
 * A <code>NewItemDialog</code> allows the user to enter a new file extension or new folder name that they wish to be filtered out
 * of all publishing operations.
 */
public final class NewItemDialog extends Dialog implements ModifyListener {

    // =======================================================================================================================
    // Fields
    // =======================================================================================================================

    /**
     * The label describing the new item.
     */
    private final String label;

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

    // =======================================================================================================================
    // Constructors
    // =======================================================================================================================

    /**
     * @param parentShell the parent shell (may be <code>null</code>)
     * @param title the localized dialog title (never <code>null</code>)
     * @param label the localized label (never <code>null</code>)
     * @param verifyListener a listener that validates input characters (may be <code>null</code>)
     */
    public NewItemDialog( Shell parentShell,
                          String title,
                          String label,
                          VerifyListener verifyListener ) {
        super(parentShell);

        CheckArg.isNotNull(title, "title");
        CheckArg.isNotNull(label, "label");

        this.title = title;
        this.label = label;
        this.verifyListener = verifyListener;

        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    // =======================================================================================================================
    // Methods
    // =======================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell( Shell newShell ) {
        newShell.setText(this.title);
        super.configureShell(newShell);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite, int, java.lang.String, boolean)
     */
    @Override
    protected Button createButton( Composite parent,
                                   int id,
                                   String label,
                                   boolean defaultButton ) {
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
    protected Control createDialogArea( Composite parent ) {
        Composite panel = (Composite)super.createDialogArea(parent);
        Composite pnlEditor = new Composite(panel, SWT.NONE);
        pnlEditor.setLayout(new GridLayout(2, false));
        pnlEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Label label = new Label(pnlEditor, SWT.NONE);
        label.setText(this.label);

        Text textField = new Text(pnlEditor, SWT.BORDER);
        textField.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        textField.addModifyListener(this);

        // add listener if necessary
        if (this.verifyListener != null) {
            textField.addVerifyListener(this.verifyListener);
        }

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

        // resize shell to be twice the width needed for the title (without this the title maybe cropped)
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
    public void modifyText( ModifyEvent event ) {
        // disable OK button if text field does not have any characters
        this.newItem = ((Text)event.widget).getText();
        boolean enable = (this.newItem.length() != 0);

        if (getButton(OK).getEnabled() != enable) {
            getButton(OK).setEnabled(enable);
        }
    }

}

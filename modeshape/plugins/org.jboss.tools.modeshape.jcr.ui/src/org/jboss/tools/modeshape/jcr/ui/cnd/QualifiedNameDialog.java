/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.ui.cnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.jboss.tools.modeshape.jcr.MultiValidationStatus;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndValidator;
import org.jboss.tools.modeshape.jcr.cnd.QualifiedName;
import org.jboss.tools.modeshape.jcr.ui.Activator;
import org.jboss.tools.modeshape.jcr.ui.UiConstants;
import org.jboss.tools.modeshape.ui.forms.FormUtils.Styles;

/**
 * The <code>QualifiedNameDialog</code> is used to create or edit a qualified name.
 */
class QualifiedNameDialog extends FormDialog {

    private Button btnOk;

    /**
     * An optional list of existing qualified names. When this is non-empty, it is checked to make sure the qualified name being
     * edited is not a duplicate.
     */
    private List<QualifiedName> existingQNames;

    /**
     * The qualified name being edited or <code>null</code> when creating a qualified name.
     */
    private QualifiedName qnameBeingEdited;

    private final String qualifiedNameType;

    private String qualifier;

    private ScrolledForm scrolledForm;

    private final String title;

    private String unqualifiedName;

    /**
     * A collection of known qualifiers/namespace prefixes to the CND (never <code>null</code>).
     */
    private final List<String> validQualifiers;

    /**
     * Used to construct a new qualified name.
     * 
     * @param parentShell the parent shell (may be <code>null</code>)
     * @param title a short message area title for use when creating a new qualified name (cannot be <code>null</code> or empty)
     * @param qualifiedNameType a word describing what the qualified name represents (cannot be <code>null</code> or empty)
     * @param existingQualifiers the existing qualifies (can be <code>null</code> or empty)
     */
    public QualifiedNameDialog( final Shell parentShell,
                                final String title,
                                final String qualifiedNameType,
                                final Collection<String> existingQualifiers ) {
        super(parentShell);
        Utils.verifyIsNotNull(qualifiedNameType, "qualifiedNameType"); //$NON-NLS-1$

        this.title = title;
        this.qualifiedNameType = qualifiedNameType;
        this.validQualifiers = ((existingQualifiers == null) ? new ArrayList<String>(1) : new ArrayList<String>(existingQualifiers));
        this.validQualifiers.add(0, CndMessages.noNameQualifierChoice); // include empty qualifier at index 0
    }

    /**
     * Used to edit an existing qualified name.
     * 
     * @param parentShell the parent shell (may be <code>null</code>)
     * @param title a short message area title for use when creating a new qualified name (cannot be <code>null</code> or empty)
     * @param qualifiedNameType a word describing what the qualified name represents (cannot be <code>null</code> or empty)
     * @param existingQualifiers the existing qualifiers (can be <code>null</code> or empty)
     * @param qnameBeingEdited the qualified name being edited (cannot be <code>null</code>)
     */
    public QualifiedNameDialog( final Shell parentShell,
                                final String title,
                                final String qualifiedNameType,
                                final Collection<String> existingQualifiers,
                                final QualifiedName qnameBeingEdited ) {
        this(parentShell, title, qualifiedNameType, existingQualifiers);
        Utils.verifyIsNotNull(qnameBeingEdited, "qnameBeingEdited"); //$NON-NLS-1$

        this.qnameBeingEdited = qnameBeingEdited;
        this.qualifier = this.qnameBeingEdited.getQualifier();
        this.unqualifiedName = this.qnameBeingEdited.getUnqualifiedName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell( Shell newShell ) {
        super.configureShell(newShell);
        newShell.setText(CndMessages.qualifiedNameDialogTitle);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite, int, java.lang.String, boolean)
     */
    @Override
    protected Button createButton( final Composite parent,
                                   final int id,
                                   final String label,
                                   final boolean defaultButton ) {
        final Button btn = super.createButton(parent, id, label, defaultButton);

        if (id == IDialogConstants.OK_ID) {
            // disable OK button initially
            this.btnOk = btn;
            btn.setEnabled(false);
        }

        return btn;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.FormDialog#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    @Override
    protected void createFormContent( final IManagedForm managedForm ) {
        this.scrolledForm = managedForm.getForm();
        this.scrolledForm.setText(this.title);
        this.scrolledForm.setImage(Activator.getSharedInstance().getImage(UiConstants.Images.CND_EDITOR));
        this.scrolledForm.setMessage(NLS.bind(CndMessages.qualifiedNameDialogMsg, this.qualifiedNameType), IMessageProvider.NONE);

        final FormToolkit toolkit = managedForm.getToolkit();
        toolkit.decorateFormHeading(this.scrolledForm.getForm());

        final Composite body = this.scrolledForm.getBody();
        body.setLayout(new GridLayout(2, false));

        { // qualifier
            final Label lblQualifier = toolkit.createLabel(body, CndMessages.qualifierLabel, SWT.NONE);
            lblQualifier.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

            final CCombo cbxQualifiers = new CCombo(body, Styles.COMBO_STYLE);
            toolkit.adapt(cbxQualifiers, true, false);
            cbxQualifiers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            cbxQualifiers.setToolTipText(CndMessages.validQualifiersToolTip);

            // populate qualifiers
            for (final String validQualifier : this.validQualifiers) {
                cbxQualifiers.add(validQualifier);
            }

            // select the current qualifier
            if (isEditMode()) {
                final String currentQualifier = this.qnameBeingEdited.getQualifier();

                if (Utils.isEmpty(currentQualifier)) {
                    cbxQualifiers.select(0);
                } else {
                    final int index = cbxQualifiers.indexOf(currentQualifier);

                    if (index == -1) {
                        // not a valid qualifier but add and select
                        cbxQualifiers.add(currentQualifier);
                        cbxQualifiers.select(cbxQualifiers.getItemCount() - 1);
                    } else {
                        cbxQualifiers.select(index);
                    }
                }
            } else {
                cbxQualifiers.select(0);
            }

            cbxQualifiers.addSelectionListener(new SelectionAdapter() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected( final SelectionEvent e ) {
                    final String newQualifier = ((CCombo)e.widget).getText();
                    handleQualifierChanged(newQualifier);
                }
            });
        }

        { // unqualified name
            final Label lblName = toolkit.createLabel(body, CndMessages.nameLabel, SWT.NONE);
            lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

            final Text txtName = toolkit.createText(body, null, Styles.TEXT_STYLE);
            txtName.setToolTipText(CndMessages.unqualifiedNameToolTip);

            final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
            gd.verticalIndent += ((GridLayout)body.getLayout()).verticalSpacing;
            txtName.setLayoutData(gd);

            if (isEditMode()) {
                txtName.setText(this.qnameBeingEdited.getUnqualifiedName());
            }

            txtName.addModifyListener(new ModifyListener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( final ModifyEvent e ) {
                    handleNameChanged(((Text)e.widget).getText());
                }
            });

            txtName.setFocus();
        }
    }

    /**
     * <strong>Should only be called after the OK button has been pressed.</strong>
     * 
     * @return the new or edited qualified name (never <code>null</code>)
     */
    public QualifiedName getQualifiedName() {
        return new QualifiedName(this.qualifier, this.unqualifiedName);
    }

    void handleNameChanged( final String newName ) {
        this.unqualifiedName = newName;
        updateState();
    }

    void handleQualifierChanged( final String newQualifier ) {
        this.qualifier = newQualifier;
        updateState();
    }

    private boolean isEditMode() {
        return (this.qnameBeingEdited != null);
    }

    /**
     * @param existingQNames used to check against for duplicate qualified names (can be <code>null</code> or empty)
     */
    void setExistingQNames( final List<QualifiedName> existingQNames ) {
        if (!Utils.isEmpty(this.existingQNames) && isEditMode()) {
            this.existingQNames = new ArrayList<QualifiedName>(existingQNames);
            this.existingQNames.remove(this.qnameBeingEdited); // so that validating won't show it as a duplicate
        }
    }

    private void updateState() {
        // validate qname
        final QualifiedName currentQName = new QualifiedName(this.qualifier, this.unqualifiedName);
        final MultiValidationStatus status = CndValidator.validateQualifiedName(currentQName, this.qualifiedNameType,
                                                                                this.validQualifiers, this.existingQNames);
        boolean enable = !status.isError();

        // a bug in Eclipse doesn't reset the font color going from an error to NONE so first set to INFORMATION to get the
        // font color to change
        this.scrolledForm.setMessage(Utils.EMPTY_STRING, IMessageProvider.INFORMATION);

        if (!enable) {
            this.scrolledForm.setMessage(status.getMessage(), IMessageProvider.ERROR);
        } else if ((isEditMode() && currentQName.equals(this.qnameBeingEdited))
                || (!isEditMode() && Utils.isEmpty(this.qualifier) && Utils.isEmpty(this.unqualifiedName))) {
            enable = false;
            this.scrolledForm.setMessage(NLS.bind(CndMessages.qualifiedNameDialogMsg, this.qualifiedNameType),
                                         IMessageProvider.NONE);
        } else {
            int severity = IMessageProvider.NONE; // OK severity
            String message = status.getMessage();

            if (status.isWarning()) {
                severity = IMessageProvider.WARNING;
            } else if (status.isInfo()) {
                severity = IMessageProvider.INFORMATION;
            } else {
                message = NLS.bind(CndMessages.acceptQualifiedNameDialogMsg, this.qualifiedNameType);
            }

            this.scrolledForm.setMessage(message, severity);
        }

        // set enabled state of OK button
        if (this.btnOk.getEnabled() != enable) {
            this.btnOk.setEnabled(enable);
        }
    }
}

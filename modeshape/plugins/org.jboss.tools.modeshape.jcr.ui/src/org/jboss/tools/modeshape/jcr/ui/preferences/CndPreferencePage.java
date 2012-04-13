/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;
import org.jboss.tools.modeshape.jcr.preference.JcrPreferenceConstants.CndPreference;
import org.jboss.tools.modeshape.jcr.preference.JcrPreferenceStore;
import org.jboss.tools.modeshape.jcr.ui.JcrUiUtils;
import org.jboss.tools.modeshape.jcr.ui.cnd.CndMessages;

/**
 * 
 */
public final class CndPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
//
//    private static String[] QUOTE_CHAR_LABELS = new String[] { CndMessages.quoteCharNoneChoiceLabel,
//            CndMessages.quoteCharSingleChoiceLabel, CndMessages.quoteCharDoubleChoiceLabel };

    /**
     * The editor used to choose the notation type for the CND format.
     */
    private Combo cbxNotationType;
//
//    /**
//     * The editor used to choose the quote character.
//     */
//    private Combo cbxQuoteChar;

    private String notationType;
//
//    private String quoteString;

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents( final Composite parent ) {
        final Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        { // notation type editor
            final Label lbl = new Label(panel, SWT.NONE);
            lbl.setText(CndMessages.notationTypeLabel);

            this.cbxNotationType = new Combo(panel, SWT.NONE);
            this.cbxNotationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            this.cbxNotationType.setItems(new String[] { CndElement.NotationType.LONG.toString(),
                    CndElement.NotationType.COMPRESSED.toString(), CndElement.NotationType.COMPACT.toString() });
            this.cbxNotationType.setToolTipText(CndMessages.notationTypeToolTip);

            // set current value
            this.notationType = JcrPreferenceStore.get().get(CndPreference.NOTATION_TYPE);

            if (this.notationType == null) {
                this.notationType = CndElement.NotationType.LONG.toString();
            }

            this.cbxNotationType.setText(this.notationType);

            // add selection listener
            this.cbxNotationType.addSelectionListener(new SelectionAdapter() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected( final SelectionEvent e ) {
                    handleNotationTypeChanged();
                }
            });
        }

        { // quote chars
//            final Label lbl = new Label(panel, SWT.NONE);
//            lbl.setText(CndMessages.quoteCharPolicyLabel);
//
//            this.cbxQuoteChar = new Combo(panel, SWT.NONE);
//            this.cbxQuoteChar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//            this.cbxQuoteChar.setItems(QUOTE_CHAR_LABELS);
//            this.cbxQuoteChar.setToolTipText(CndMessages.quoteCharPolicyToolTip);
//
//            // set current value
//            this.quoteString = JcrPreferenceStore.get().get(CndPreference.QUOTE_CHAR);
//
//            if (Utils.DOUBLE_QUOTE.equals(this.quoteString)) {
//                this.cbxQuoteChar.setText(CndMessages.quoteCharDoubleChoiceLabel);
//            } else if (Utils.SINGLE_QUOTE.equals(this.quoteString)) {
//                this.cbxQuoteChar.setText(CndMessages.quoteCharSingleChoiceLabel);
//            } else {
//                this.cbxQuoteChar.setText(CndMessages.quoteCharNoneChoiceLabel);
//            }
//
//            // add selection listener
//            this.cbxQuoteChar.addSelectionListener(new SelectionAdapter() {
//
//                /**
//                 * {@inheritDoc}
//                 * 
//                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
//                 */
//                @Override
//                public void widgetSelected( final SelectionEvent e ) {
//                    handleQuotationCharacterChanged();
//                }
//            });
        }

        return panel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getDescription()
     */
    @Override
    public String getDescription() {
        return CndMessages.cndPrefPageDescription;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getImage()
     */
    @Override
    public Image getImage() {
        return JcrUiUtils.getCndEditorImage();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getMessage()
     */
    @Override
    public String getMessage() {
        return CndMessages.cndPrefPageMessage;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
     */
    @Override
    public IPreferenceStore getPreferenceStore() {
        return null;
    }
//
//    private String getQuoteStringFromSelection() {
//        final String quoteSelection = this.cbxQuoteChar.getText();
//
//        if (CndMessages.quoteCharNoneChoiceLabel.equals(quoteSelection)) {
//            return Utils.EMPTY_STRING;
//        }
//
//        if (CndMessages.quoteCharSingleChoiceLabel.equals(quoteSelection)) {
//            return Utils.SINGLE_QUOTE;
//        }
//
//        return Utils.DOUBLE_QUOTE;
//    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getTitle()
     */
    @Override
    public String getTitle() {
        return CndMessages.cndPrefPageTitle;
    }

    void handleNotationTypeChanged() {
        this.notationType = this.cbxNotationType.getText();
    }
//
//    void handleQuotationCharacterChanged() {
//        this.quoteString = getQuoteStringFromSelection();
//    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init( final IWorkbench workbench ) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        final JcrPreferenceStore prefStore = JcrPreferenceStore.get();

        { // notation type
            String defaultNotationType = prefStore.getDefault(CndPreference.NOTATION_TYPE);

            if (defaultNotationType == null) {
                defaultNotationType = CndElement.NotationType.LONG.toString();
            }

            this.notationType = defaultNotationType;
            this.cbxNotationType.setText(defaultNotationType);
        }

        { // quote character
//            final String quoteSelection = prefStore.getDefault(CndPreference.QUOTE_CHAR);
//
//            if (Utils.EMPTY_STRING.equals(quoteSelection)) {
//                this.cbxQuoteChar.setText(CndMessages.quoteCharNoneChoiceLabel);
//            } else if (Utils.SINGLE_QUOTE.equals(quoteSelection)) {
//                this.cbxQuoteChar.setText(CndMessages.quoteCharSingleChoiceLabel);
//            } else {
//                this.cbxQuoteChar.setText(CndMessages.quoteCharDoubleChoiceLabel);
//            }
        }

        super.performDefaults();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        JcrPreferenceStore.get().set(CndPreference.NOTATION_TYPE, this.notationType);
//        JcrPreferenceStore.get().set(CndPreference.QUOTE_CHAR, this.quoteString);
        return super.performOk();
    }
}

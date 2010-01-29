/*
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * This software is made available by Red Hat, Inc. under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.tools.modeshape.rest.preferences;

import static org.jboss.tools.modeshape.rest.IUiConstants.ModeShape_IMAGE_16x;
import static org.jboss.tools.modeshape.rest.IUiConstants.PREFERENCE_PAGE_HELP_CONTEXT;
import static org.jboss.tools.modeshape.rest.RestClientI18n.preferencePageDescription;
import static org.jboss.tools.modeshape.rest.RestClientI18n.preferencePageMessage;
import static org.jboss.tools.modeshape.rest.RestClientI18n.preferencePageTitle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.jboss.tools.modeshape.rest.Activator;

/**
 * The <code>ModeShapePreferencePage</code> is the UI for managing all ModeShape-related preferences.
 */
public final class ModeShapePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The editor used to manage the list of filtered file extensions.
     */
    private FilteredFileExtensionEditor extensionsEditor;

    /**
     * The editor used to manage the list of filtered folder names.
     */
    private FilteredFoldersEditor foldersEditor;

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents( Composite parent ) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // create the filtered extensions editor
        this.extensionsEditor = new FilteredFileExtensionEditor(panel);
        this.extensionsEditor.setPreferenceStore(getPreferenceStore());
        this.extensionsEditor.getListControl(panel).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // populate the extensions editor
        this.extensionsEditor.load();

        // create the filtered folders editor
        this.foldersEditor = new FilteredFoldersEditor(panel);
        this.foldersEditor.setPreferenceStore(getPreferenceStore());
        this.foldersEditor.getListControl(panel).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // populate the folders editor
        this.foldersEditor.load();

        // register with the help system
        IWorkbenchHelpSystem helpSystem = Activator.getDefault().getWorkbench().getHelpSystem();
        helpSystem.setHelp(panel, PREFERENCE_PAGE_HELP_CONTEXT);

        return panel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getDescription()
     */
    @Override
    public String getDescription() {
        return preferencePageDescription.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getImage()
     */
    @Override
    public Image getImage() {
        return Activator.getDefault().getImage(ModeShape_IMAGE_16x);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getMessage()
     */
    @Override
    public String getMessage() {
        return preferencePageMessage.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
     */
    @Override
    public IPreferenceStore getPreferenceStore() {
        return PrefUtils.getPreferenceStore();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#getTitle()
     */
    @Override
    public String getTitle() {
        return preferencePageTitle.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init( IWorkbench workbench ) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        this.extensionsEditor.loadDefault();
        this.foldersEditor.loadDefault();
        super.performDefaults();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        this.extensionsEditor.store();
        this.foldersEditor.store();
        return super.performOk();
    }

}

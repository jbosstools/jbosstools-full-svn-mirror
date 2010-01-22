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

import static org.jboss.tools.modeshape.rest.IUiConstants.FILTERED_FILE_EXTENSIONS_PREFERENCE;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFileExtensionDialogLabel;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFileExtensionDialogTitle;
import static org.jboss.tools.modeshape.rest.RestClientI18n.preferencePageFilteredFileExtensionsLabel;
import static org.jboss.tools.modeshape.rest.preferences.PrefUtils.FILE_EXT_DELIMITER;
import static org.jboss.tools.modeshape.rest.preferences.PrefUtils.FILE_EXT_INVALID_CHARS;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.modeshape.rest.Utils;

/**
 * The <code>FilteredFileExtensionEditor</code> is an editor for managing a set of filtered file extensions.
 */
public final class FilteredFileExtensionEditor extends ListEditor implements VerifyListener {

    // =======================================================================================================================
    // Fields
    // =======================================================================================================================

    /**
     * The current set of file extensions.
     */
    private final Set<String> extensions;

    // =======================================================================================================================
    // Constructors
    // =======================================================================================================================

    /**
     * @param parent the parent control
     */
    public FilteredFileExtensionEditor( Composite parent ) {
        super(FILTERED_FILE_EXTENSIONS_PREFERENCE, preferencePageFilteredFileExtensionsLabel.text(), parent);
        this.extensions = new TreeSet<String>();
    }

    // =======================================================================================================================
    // Methods
    // =======================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
     */
    @Override
    protected String createList( String[] items ) {
        return Utils.combineTokens(items, FILE_EXT_DELIMITER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
     */
    @Override
    protected String getNewInputObject() {
        NewItemDialog dialog = new NewItemDialog(getShell(), newFilteredFileExtensionDialogTitle.text(),
                                                 newFilteredFileExtensionDialogLabel.text(), this);

        if (dialog.open() == Window.OK) {
            String extension = dialog.getNewItem();

            // add new extension
            if (extension != null) {
                this.extensions.add(extension);
                return extension;
            }
        }

        // user canceled dialog
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
     */
    @Override
    protected String[] parseString( String stringList ) {
        String[] values = Utils.getTokens(stringList, Character.toString(FILE_EXT_DELIMITER), true);

        this.extensions.clear();
        this.extensions.addAll(Arrays.asList(values));

        return values;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
     */
    @Override
    public void verifyText( VerifyEvent event ) {
        for (char c : FILE_EXT_INVALID_CHARS.toCharArray()) {
            if (c == event.character) {
                event.doit = false;
                break;
            }
        }
    }

}

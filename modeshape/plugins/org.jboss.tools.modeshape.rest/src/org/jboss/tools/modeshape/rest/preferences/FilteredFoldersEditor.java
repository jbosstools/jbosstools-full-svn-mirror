/*
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

import static org.jboss.tools.modeshape.rest.IUiConstants.FILTERED_FOLDER_NAMES_PREFERENCE;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFolderNameDialogLabel;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFolderNameDialogTitle;
import static org.jboss.tools.modeshape.rest.RestClientI18n.preferencePageFilteredFolderNamesLabel;
import static org.jboss.tools.modeshape.rest.preferences.PrefUtils.FOLDER_NAME_DELIMITER;
import static org.jboss.tools.modeshape.rest.preferences.PrefUtils.FOLDER_NAME_INVALID_CHARS;
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
 * The <code>FilteredFoldersEditor</code> is an editor for managing a set of folder names.
 */
public final class FilteredFoldersEditor extends ListEditor implements VerifyListener {

    // =======================================================================================================================
    // Fields
    // =======================================================================================================================

    /**
     * The current set of folder names.
     */
    private final Set<String> folderNames;

    // =======================================================================================================================
    // Constructors
    // =======================================================================================================================

    /**
     * @param parent the parent control
     */
    public FilteredFoldersEditor( Composite parent ) {
        super(FILTERED_FOLDER_NAMES_PREFERENCE, preferencePageFilteredFolderNamesLabel.text(), parent);
        this.folderNames = new TreeSet<String>();
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
        return Utils.combineTokens(items, FOLDER_NAME_DELIMITER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
     */
    @Override
    protected String getNewInputObject() {
        NewItemDialog dialog = new NewItemDialog(getShell(), newFilteredFolderNameDialogTitle.text(),
                                                 newFilteredFolderNameDialogLabel.text(), this);

        if (dialog.open() == Window.OK) {
            String folderName = dialog.getNewItem();

            // add new folder name
            if (folderName != null) {
                this.folderNames.add(folderName);
                return folderName;
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
        String[] values = Utils.getTokens(stringList, Character.toString(FOLDER_NAME_DELIMITER), true);

        this.folderNames.clear();
        this.folderNames.addAll(Arrays.asList(values));

        return values;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
     */
    @Override
    public void verifyText( VerifyEvent event ) {
        for (char c : FOLDER_NAME_INVALID_CHARS.toCharArray()) {
            if (c == event.character) {
                event.doit = false;
                break;
            }
        }
    }

}

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

import static org.jboss.tools.modeshape.rest.IUiConstants.Preferences.FILTERED_FOLDER_NAMES_PREFERENCE;
import static org.jboss.tools.modeshape.rest.RestClientI18n.fileFiltersPreferencePageFilteredFolderNamesLabel;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFolderNameDialogLabel;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFolderNameDialogTitle;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.modeshape.rest.Utils;

/**
 * The <code>FilteredFoldersEditor</code> is an editor for managing a set of folder names.
 */
public final class FilteredFoldersEditor extends SortedListEditor implements VerifyListener {

    /**
     * The filter that removes resources, contained in specific folders, from publishing operations (never <code>null</code>).
     */
    private final PublishingFileFilter filter;

    /**
     * @param parent the parent control
     */
    public FilteredFoldersEditor( Composite parent ) {
        super(FILTERED_FOLDER_NAMES_PREFERENCE, fileFiltersPreferencePageFilteredFolderNamesLabel.text(), parent);
        this.filter = new PublishingFileFilter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
     */
    @Override
    protected String createList( String[] items ) {
        return Utils.combineTokens(items, this.filter.getFolderNameDelimiter());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.rest.preferences.SortedListEditor#getNewItemDialogLabel()
     */
    @Override
    protected String getNewItemDialogLabel() {
        return newFilteredFolderNameDialogLabel.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.rest.preferences.SortedListEditor#getNewItemDialogTitle()
     */
    @Override
    protected String getNewItemDialogTitle() {
        return newFilteredFolderNameDialogTitle.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
     */
    @Override
    protected String[] parseString( String stringList ) {
        return Utils.getTokens(stringList, Character.toString(this.filter.getFolderNameDelimiter()), true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
     */
    @Override
    public void verifyText( VerifyEvent event ) {
        for (char c : this.filter.getFolderNameInvalidCharacters().toCharArray()) {
            if (c == event.character) {
                event.doit = false;
                break;
            }
        }
    }

}

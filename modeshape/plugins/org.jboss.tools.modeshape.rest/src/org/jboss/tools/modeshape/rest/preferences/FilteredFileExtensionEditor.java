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

import static org.jboss.tools.modeshape.rest.IUiConstants.Preferences.FILTERED_FILE_EXTENSIONS_PREFERENCE;
import static org.jboss.tools.modeshape.rest.RestClientI18n.fileFiltersPreferencePageFilteredFileExtensionsLabel;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFileExtensionDialogLabel;
import static org.jboss.tools.modeshape.rest.RestClientI18n.newFilteredFileExtensionDialogTitle;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.modeshape.rest.Utils;

/**
 * The <code>FilteredFileExtensionEditor</code> is an editor for managing a set of filtered file extensions.
 */
public final class FilteredFileExtensionEditor extends SortedListEditor implements VerifyListener {
    
    /**
     * The filter that removes resources, with specific file extensions, from publishing operations (never <code>null</code>).
     */
    private final PublishingFileFilter filter;

    /**
     * @param parent the parent control
     */
    public FilteredFileExtensionEditor( Composite parent ) {
        super(FILTERED_FILE_EXTENSIONS_PREFERENCE, fileFiltersPreferencePageFilteredFileExtensionsLabel.text(), parent);
        this.filter = new PublishingFileFilter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
     */
    @Override
    protected String createList( String[] items ) {
        return Utils.combineTokens(items, this.filter.getFileExtensionDelimiter());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.rest.preferences.SortedListEditor#getNewItemDialogLabel()
     */
    @Override
    protected String getNewItemDialogLabel() {
        return newFilteredFileExtensionDialogLabel.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.rest.preferences.SortedListEditor#getNewItemDialogTitle()
     */
    @Override
    protected String getNewItemDialogTitle() {
        return newFilteredFileExtensionDialogTitle.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
     */
    @Override
    protected String[] parseString( String stringList ) {
        return Utils.getTokens(stringList, Character.toString(this.filter.getFileExtensionDelimiter()), true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
     */
    @Override
    public void verifyText( VerifyEvent event ) {
        for (char c : this.filter.getFileExtensionInvalidCharacters().toCharArray()) {
            if (c == event.character) {
                event.doit = false;
                break;
            }
        }
    }

}

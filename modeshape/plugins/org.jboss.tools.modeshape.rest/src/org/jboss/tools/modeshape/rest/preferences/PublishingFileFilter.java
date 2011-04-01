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
import static org.jboss.tools.modeshape.rest.IUiConstants.Preferences.FILTERED_FOLDER_NAMES_PREFERENCE;
import static org.jboss.tools.modeshape.rest.RestClientI18n.preferenceNotFound;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.jboss.tools.modeshape.rest.Activator;
import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.Status.Severity;

/**
 * The <code>PublishingFileFilter</code> is a file filter that uses the preferences when filtering files.
 */
public final class PublishingFileFilter {

    /**
     * The preference name for the delimiter that separates filtered file extensions when the preference value is stored.
     */
    private static final String FILE_EXT_DELIMITER_PREF_NAME = "fileExtension.delimiter"; //$NON-NLS-1$

    /**
     * The preference name for characters that are <strong>NOT</strong> allowed to appear in a file extension.
     */
    private static final String FILE_EXT_INVALID_CHARS_PREF_NAME = "fileExtension.invalidChars"; //$NON-NLS-1$

    /**
     * The preference name for the delimiter that separates filtered folder names when the preference value is stored.
     */
    private static final String FOLDER_NAME_DELIMITER_PREF_NAME = "folderName.delimiter"; //$NON-NLS-1$

    /**
     * The preference name for characters that are <strong>NOT</strong> allowed to appear in a folder name.
     */
    private static final String FOLDER_NAME_INVALID_CHARS_PREF_NAME = "folderName.invalidChars"; //$NON-NLS-1$

    /**
     * @param resource the resource being tested (never <code>null</code>)
     * @return <code>true</code> if the resource should be included (i.e., it is not filtered out)
     */
    public boolean accept( IResource resource ) {
        CheckArg.isNotNull(resource, "resource"); //$NON-NLS-1$

        if (resource instanceof IFolder) {
            String name = resource.getName();

            // see if folder name has been filtered
            for (String filteredName : getFilteredFolderNames()) {
                if (filteredName.equals(name)) {
                    return false;
                }
            }

            // check parent
            if (resource.getParent() != null) {
                return accept(resource.getParent());
            }
        } else if (resource instanceof IFile) {
            // see if file extension has been filtered
            for (String extension : getFilteredFileExtensions()) {
                if (resource.getFullPath().toString().endsWith('.' + extension)) {
                    return false;
                }
            }

            // check parent
            if (resource.getParent() != null) {
                return accept(resource.getParent());
            }
        }

        // must be project
        return true;
    }

    /**
     * The delimiter that separates filtered file extensions when the preference value is stored.
     * 
     * @return the file extension separator character
     */
    public char getFileExtensionDelimiter() {
        String value = PrefUtils.getPreferenceStore().getDefaultString(FILE_EXT_DELIMITER_PREF_NAME);

        if ((value != null) && (value.length() > 0)) {
            return value.charAt(0);
        }

        // no value found so log and give a default value
        value = ","; //$NON-NLS-1$
        Activator.getDefault().log(new Status(Severity.ERROR, preferenceNotFound.text(FILE_EXT_DELIMITER_PREF_NAME, value), null));
        return value.charAt(0);
    }

    /**
     * The characters that are <strong>NOT</strong> allowed to appear in a file extension.
     * 
     * @return the invalid file extension characters (never <code>null</code> or empty)
     */
    public String getFileExtensionInvalidCharacters() {
        String value = PrefUtils.getPreferenceStore().getDefaultString(FILE_EXT_INVALID_CHARS_PREF_NAME);

        if ((value != null) && (value.length() > 0)) {
            return value;
        }

        // no value found so log and give a default value
        value = "*?<>|/\\:;."; //$NON-NLS-1$
        Activator.getDefault().log(new Status(Severity.ERROR,
                                              preferenceNotFound.text(FILE_EXT_INVALID_CHARS_PREF_NAME, value),
                                              null));
        return value;
    }

    /**
     * @return the file extensions being filtered out of publishing operations (never <code>null</code> but can be empty)
     */
    public String[] getFilteredFileExtensions() {
        return PrefUtils.getListPropertyValue(FILTERED_FILE_EXTENSIONS_PREFERENCE, getFileExtensionDelimiter(), true);
    }

    /**
     * @return the folder names being filtered out of publishing operations (never <code>null</code> but can be empty)
     */
    public String[] getFilteredFolderNames() {
        return PrefUtils.getListPropertyValue(FILTERED_FOLDER_NAMES_PREFERENCE, getFolderNameDelimiter(), true);
    }

    /**
     * The delimiter that separates filtered folder names when the preference value is stored.
     * 
     * @return the folder name separator character
     */
    public char getFolderNameDelimiter() {
        String value = PrefUtils.getPreferenceStore().getDefaultString(FOLDER_NAME_DELIMITER_PREF_NAME);

        if ((value != null) && (value.length() > 0)) {
            return value.charAt(0);
        }

        // no value found so log and give a default value
        value = ","; //$NON-NLS-1$
        Activator.getDefault()
                 .log(new Status(Severity.ERROR, preferenceNotFound.text(FOLDER_NAME_DELIMITER_PREF_NAME, value), null));
        return value.charAt(0);
    }

    /**
     * The characters that are <strong>NOT</strong> allowed to appear in a folder name (never <code>null</code> but can be empty).
     * 
     * @return the invalid folder name characters (never <code>null</code> or empty)
     */
    public String getFolderNameInvalidCharacters() {
        String value = PrefUtils.getPreferenceStore().getDefaultString(FOLDER_NAME_INVALID_CHARS_PREF_NAME);

        if ((value != null) && (value.length() > 0)) {
            return value;
        }

        // no value found so log and give a default value
        value = "*?<>|/\\:;"; //$NON-NLS-1$ 
        Activator.getDefault().log(new Status(Severity.ERROR,
                                              preferenceNotFound.text(FOLDER_NAME_INVALID_CHARS_PREF_NAME, value),
                                              null));
        return value;
    }

}

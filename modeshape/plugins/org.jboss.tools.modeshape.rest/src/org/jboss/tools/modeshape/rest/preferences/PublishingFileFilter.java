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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.modeshape.common.util.CheckArg;

/**
 * The <code>PublishingFileFilter</code> is a file filter that uses the preferences when filtering files.
 */
public final class PublishingFileFilter {

    // =======================================================================================================================
    // Fields
    // =======================================================================================================================

    /**
     * The file extensions that should not be involved in publishing operations.
     */
    private final String[] filteredFileExtensions;

    /**
     * The folder names that should not be involved in publishing operations.
     */
    private final String[] filteredFolderNames;

    // =======================================================================================================================
    // Constructors
    // =======================================================================================================================

    /**
     * Construct a filter using the current preferences.
     */
    public PublishingFileFilter() {
        this.filteredFileExtensions = PrefUtils.getFilteredFileExtensions();
        this.filteredFolderNames = PrefUtils.getFilteredFolderNames();
    }

    // =======================================================================================================================
    // Methods
    // =======================================================================================================================

    /**
     * @param resource the resource being tested (never <code>null</code>)
     * @return <code>true</code> if the resource should be included (i.e., it is not filtered out)
     */
    public boolean accept( IResource resource ) {
        CheckArg.isNotNull(resource, "resource");

        if (resource instanceof IFolder) {
            String name = resource.getName();

            // see if folder name has been filtered
            for (String filteredName : this.filteredFolderNames) {
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
            for (String extension : this.filteredFileExtensions) {
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

}

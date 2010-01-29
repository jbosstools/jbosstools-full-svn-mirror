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
package org.jboss.tools.modeshape.rest;

public interface IUiConstants {

    /**
     * The Plug-in's identifier.
     */
    String PLUGIN_ID = "org.jboss.tools.modeshape.rest";

    String ICON_FOLDER = "icons/";

    //
    // /icons/objects/
    //

    String OBJECT_ICONS_FOLDER = ICON_FOLDER + "objects/";

    String CHECKMARK_IMAGE = OBJECT_ICONS_FOLDER + "checkmark.gif";

    String REPOSITORY_IMAGE = OBJECT_ICONS_FOLDER + "repository.gif";

    String SERVER_IMAGE = OBJECT_ICONS_FOLDER + "server.gif";

    String WORKSPACE_IMAGE = OBJECT_ICONS_FOLDER + "workspace.gif";

    //
    // /icons/views/
    //

    String VIEWS_ICON_FOLDER = ICON_FOLDER + "views/";

    String BLANK_IMAGE = VIEWS_ICON_FOLDER + "blank.gif";

    String COLLAPSE_ALL_IMAGE = VIEWS_ICON_FOLDER + "collapse_all.gif";

    String DELETE_SERVER_IMAGE = VIEWS_ICON_FOLDER + "delete_server.gif";

    String ModeShape_IMAGE_16x = VIEWS_ICON_FOLDER + "modeShape_icon_16x.png";

    String EDIT_SERVER_IMAGE = VIEWS_ICON_FOLDER + "edit_server.gif";

    String ERROR_OVERLAY_IMAGE = VIEWS_ICON_FOLDER + "error_overlay.gif";

    String NEW_SERVER_IMAGE = VIEWS_ICON_FOLDER + "new_server.gif";

    String PUBLISH_IMAGE = VIEWS_ICON_FOLDER + "publish.png";

    String PUBLISHED_OVERLAY_IMAGE = VIEWS_ICON_FOLDER + "published_overlay.png";

    String REFRESH_IMAGE = VIEWS_ICON_FOLDER + "refresh.gif";

    String UNPUBLISH_IMAGE = VIEWS_ICON_FOLDER + "unpublish.png";

    //
    // /icons/wizards/
    //

    String WIZARD_ICONS_FOLDER = ICON_FOLDER + "wizards/";

    String WIZARD_BANNER_IMAGE = WIZARD_ICONS_FOLDER + "wizard_banner.png";

    //
    // Help Contexts
    //

    String HELP_CONTEXT_PREFIX = PLUGIN_ID + '.';

    String MESSAGE_CONSOLE_HELP_CONTEXT = HELP_CONTEXT_PREFIX + "messageConsoleHelpContext";
    
    String PREFERENCE_PAGE_HELP_CONTEXT = HELP_CONTEXT_PREFIX + "preferencesHelpContext";

    String PUBLISH_DIALOG_HELP_CONTEXT = HELP_CONTEXT_PREFIX + "publishDialogHelpContext";

    String SERVER_DIALOG_HELP_CONTEXT = HELP_CONTEXT_PREFIX + "serverDialogHelpContext";

    String SERVER_VIEW_HELP_CONTEXT = HELP_CONTEXT_PREFIX + "serverViewHelpContext";

    //
    // Jobs
    //

    /**
     * The <code>Job</code> framework job family for the ModeShape publishing and unpublishing operations.
     */
    String PUBLISHING_JOB_FAMILY = "modeshape.publishing.job.family";

    //
    // Preferences
    //

    /**
     * A preference for a list of file extensions that will not be part of publishing operations.
     */
    String FILTERED_FILE_EXTENSIONS_PREFERENCE = "modeShape.preference.filteredFileExtensions";

    /**
     * A preference for a list of folder names whose contents will not be part of publishing operations.
     */
    String FILTERED_FOLDER_NAMES_PREFERENCE = "modeShape.preference.filteredFolderNames";

}

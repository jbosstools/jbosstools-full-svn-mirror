/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.ui;

/**
 * Constants used within the <code>org.jboss.tools.modeshape.jcr.ui</code> plug-in.
 */
public interface UiConstants {

    /**
     * The plug-in bundle's symbolic name.
     */
    String PLUGIN_ID = UiConstants.class.getPackage().getName();

    /**
     * The identifiers for the CND editor-related parts.
     */
    interface EditorIds {

        /**
         * The extension ID for the CND editor part.
         */
        String CND_EDITOR = PLUGIN_ID + ".cndEditor"; //$NON-NLS-1$

        /**
         * The ID of the CND editor's forms editor page.
         */
        String CND_FORMS_PAGE = CND_EDITOR + ".formsPage"; //$NON-NLS-1$

        /**
         * The ID of the CND editor's text editor page.
         */
        String CND_SOURCE_PAGE = CND_EDITOR + ".sourcePage"; //$NON-NLS-1$
    }

    /**
     * The image paths.
     */
    interface Images {

        /**
         * The relative path from the plugin folder to the icons folder.
         */
        String ICONS_FOLDER = "icons/"; //$NON-NLS-1$

        /**
         * The relative path from the plugin folder to the icon used for the CND editor.
         */
        String CND_EDITOR = ICONS_FOLDER + "cnd-editor-16x.png"; //$NON-NLS-1$
    }
}

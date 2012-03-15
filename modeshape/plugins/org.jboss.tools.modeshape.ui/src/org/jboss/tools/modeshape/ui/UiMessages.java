/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Reusable localized messages.
 */
public class UiMessages extends NLS {

    /**
     * The title of a generic error message dialog.
     */
    public static String errorDialogTitle;

    /**
     * The title of a generic information message dialog.
     */
    public static String infoDialogTitle;

    /**
     * The title of a generic question message dialog.
     */
    public static String questionDialogTitle;

    /**
     * The title of a generic warning message dialog.
     */
    public static String warningDialogTitle;

    static {
        NLS.initializeMessages("org.jboss.tools.modeshape.ui.uiMessages", UiMessages.class); //$NON-NLS-1$
    }
}

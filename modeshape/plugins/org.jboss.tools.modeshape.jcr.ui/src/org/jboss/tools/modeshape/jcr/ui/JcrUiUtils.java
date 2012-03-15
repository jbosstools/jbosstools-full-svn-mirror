/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.ui;

import org.jboss.tools.modeshape.jcr.ValidationStatus;
import org.jboss.tools.modeshape.ui.forms.ErrorMessage;

/**
 * 
 */
public class JcrUiUtils {

    /**
     * @param status the status being used to set the message (cannot be <code>null</code>)
     * @param message the message being set (cannot be <code>null</code>)
     */
    public static void setMessage( ValidationStatus status,
                                   ErrorMessage message ) {
        // TODO need to figure out how to get all the MultiValidationStatus errors to display
        if (status.isError()) {
            message.setErrorMessage(status.getMessage());
        } else if (status.isWarning()) {
            message.setWarningMessage(status.getMessage());
        } else if (status.isInfo()) {
            message.setInformationMessage(status.getMessage());
        } else if (status.isOk()) {
            message.setOkMessage(status.getMessage());
        } else {
            assert false : "Unexpected status type"; //$NON-NLS-1$
        }
    }

    /**
     * Don't allow construction.
     */
    private JcrUiUtils() {
        // nothing to do
    }
}

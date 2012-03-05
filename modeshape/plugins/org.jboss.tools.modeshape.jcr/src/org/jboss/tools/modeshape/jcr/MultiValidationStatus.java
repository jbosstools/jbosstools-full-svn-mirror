/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr;

import java.util.ArrayList;
import java.util.List;

/**
 * A validation status that can contain other statuses.
 */
public class MultiValidationStatus extends ValidationStatus {

    private final List<ValidationStatus> errors = new ArrayList<ValidationStatus>();

    private ValidationStatus primary = null;

    /**
     * Constructs an OK status.
     */
    public MultiValidationStatus() {
        super(Severity.OK, Messages.okValidationMsg);
    }

    /**
     * @param status the status used to construct (cannot be <code>null</code>)
     */
    public MultiValidationStatus( final ValidationStatus status ) {
        super(status.getSeverity(), status.getMessage());
    }

    /**
     * @param statusBeingAdded the status being added (cannot be <code>null</code>)
     */
    public void add( final ValidationStatus statusBeingAdded ) {
        this.errors.add(statusBeingAdded);

        if ((this.primary == null) || statusBeingAdded.getSeverity().isMoreSevere(getSeverity())) {
            this.primary = statusBeingAdded;
            this.severity = this.primary.getSeverity();
            this.message = this.primary.getMessage();
        }
    }

    /**
     * @return a collection of all contained statuses (never <code>null</code>)
     */
    public List<ValidationStatus> getAll() {
        return this.errors;
    }

}

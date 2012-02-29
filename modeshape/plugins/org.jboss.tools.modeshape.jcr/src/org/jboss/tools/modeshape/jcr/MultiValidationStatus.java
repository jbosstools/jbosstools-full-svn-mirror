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
 * 
 */
public class MultiValidationStatus extends ValidationStatus {

    private final List<ValidationStatus> errors = new ArrayList<ValidationStatus>();

    private ValidationStatus primary = null;

    public MultiValidationStatus() {
        super(Severity.OK, Messages.okValidationMsg);
    }

    public MultiValidationStatus( ValidationStatus status ) {
        super(status.getSeverity(), status.getMessage());
    }

    public void add( ValidationStatus statusBeingAdded ) {
        this.errors.add(statusBeingAdded);

        if ((this.primary == null) || statusBeingAdded.getSeverity().isMoreSevere(getSeverity())) {
            this.primary = statusBeingAdded;
            this.severity = this.primary.getSeverity();
            this.message = this.primary.getMessage();
        }
    }

    public List<ValidationStatus> getAll() {
        return this.errors;
    }

}

/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr;

/**
 * A validation status that has a severity and a message.
 */
public class ValidationStatus implements Comparable<ValidationStatus> {

    /**
     * An OK validation status with a standard, localized message.
     */
    public static final ValidationStatus OK_STATUS = createOkMessage(Messages.okValidationMsg);

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the error validation message (never <code>null</code>)
     */
    public static ValidationStatus createErrorMessage( final String message ) {
        return new ValidationStatus(Severity.ERROR, message);
    }

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the information validation message (never <code>null</code>)
     */
    public static ValidationStatus createInfoMessage( final String message ) {
        return new ValidationStatus(Severity.INFO, message);
    }

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the OK validation message (never <code>null</code>)
     */
    public static ValidationStatus createOkMessage( final String message ) {
        return new ValidationStatus(Severity.OK, message);
    }

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the warning validation message (never <code>null</code>)
     */
    public static ValidationStatus createWarningMessage( final String message ) {
        return new ValidationStatus(Severity.WARNING, message);
    }

    /**
     * The localized message which can be displayed to the user (never <code>null</code>).
     */
    protected String message;

    /**
     * The status severity (never <code>null</code>).
     */
    protected Severity severity;

    /**
     * @param severity the status severity (cannot be <code>null</code>)
     * @param message the status localized user message (cannot be <code>null</code>)
     */
    protected ValidationStatus( final Severity severity,
                                final String message ) {
        assert (severity != null) : "severity is null"; //$NON-NLS-1$
        Utils.verifyIsNotEmpty(message, "message"); //$NON-NLS-1$

        this.severity = severity;
        this.message = message;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( final ValidationStatus that ) {
        if ((this == that) || (this.severity == that.severity)) {
            return getMessage().compareTo(that.getMessage());
        }

        if (isError()) {
            if (that.isWarning()) {
                return -10;
            }

            if (that.isInfo()) {
                return -100;
            }

            return -1000; // ok
        }

        if (isWarning()) {
            if (that.isError()) {
                return 10;
            }

            if (that.isInfo()) {
                return -10;
            }

            return -100; // ok
        }

        if (isInfo()) {
            if (that.isError()) {
                return 100;
            }

            if (that.isWarning()) {
                return 10;
            }

            return -10; // ok
        }

        // OK
        if (that.isError()) {
            return 1000;
        }

        if (that.isWarning()) {
            return 100;
        }

        return 10; // info
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        ValidationStatus that = (ValidationStatus)obj;
        return (this.severity.equals(that.severity) && this.message.equals(that.message));
    }
    
    /**
     * @return the message pertaining to the worse validation severity (never <code>null</code>)
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return the status severity (never <code>null</code>)
     */
    public Severity getSeverity() {
        return this.severity;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Utils.hashCode(this.severity, this.message);
    }
    
    /**
     * @return <code>true</code> if the validation status has an error severity
     */
    public boolean isError() {
        return (Severity.ERROR == this.severity);
    }

    /**
     * @return <code>true</code> if the validation status has an information severity
     */
    public boolean isInfo() {
        return (Severity.INFO == this.severity);
    }

    /**
     * @return <code>true</code> if the validation status has an OK severity
     */
    public boolean isOk() {
        return (Severity.OK == this.severity);
    }

    /**
     * @return <code>true</code> if the validation status has a warning severity
     */
    public boolean isWarning() {
        return (Severity.WARNING == this.severity);
    }

    /**
     * The validation status severity.
     */
    public enum Severity {

        /**
         * Indicates the status is an error.
         */
        ERROR,

        /**
         * Indicates the status is an info.
         */
        INFO,

        /**
         * Indicates the status is a OK.
         */
        OK,

        /**
         * Indicates the status is a warning.
         */
        WARNING;

        /**
         * @param that the validation being compared (cannot be <code>null</code>)
         * @return <code>true</code> if this status more severe
         */
        public boolean isMoreSevere( final Severity that ) {
            Utils.verifyIsNotNull(that, "that"); //$NON-NLS-1$

            if ((this == that) || (this == WARNING)) {
                return false;
            }

            if (this == ERROR) {
                return true;
            }

            if (this == INFO) {
                return (that == OK);
            }

            // this == OK
            return (that == WARNING);
        }
    }

}

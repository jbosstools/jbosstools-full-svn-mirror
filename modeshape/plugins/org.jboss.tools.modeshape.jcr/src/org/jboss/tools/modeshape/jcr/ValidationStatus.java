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

    public enum Severity {
        ERROR,
        INFO,
        OK,
        WARNING;

        public boolean isMoreSevere( Severity that ) {
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

    /**
     * An OK validation status with a standard, localized message.
     */
    public static final ValidationStatus OK_STATUS = createOkMessage(Messages.okValidationMsg);

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the error validation message (never <code>null</code>)
     */
    public static ValidationStatus createErrorMessage( String message ) {
        return new ValidationStatus(Severity.ERROR, message);
    }

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the information validation message (never <code>null</code>)
     */
    public static ValidationStatus createInfoMessage( String message ) {
        return new ValidationStatus(Severity.INFO, message);
    }

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the OK validation message (never <code>null</code>)
     */
    public static ValidationStatus createOkMessage( String message ) {
        return new ValidationStatus(Severity.OK, message);
    }

    /**
     * @param message the validation message (cannot be <code>null</code> or empty)
     * @return the warning validation message (never <code>null</code>)
     */
    public static ValidationStatus createWarningMessage( String message ) {
        return new ValidationStatus(Severity.WARNING, message);
    }

    protected String message;
    protected Severity severity;

    protected ValidationStatus( Severity type,
                                String message ) {
        assert (type != null) : "severity is null"; //$NON-NLS-1$
        Utils.verifyIsNotEmpty(message, "message"); //$NON-NLS-1$

        this.severity = type;
        this.message = message;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( ValidationStatus that ) {
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
     * @return the message pertaining to the worse validation severity (never <code>null</code>)
     */
    public String getMessage() {
        return this.message;
    }

    protected Severity getSeverity() {
        return this.severity;
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

}

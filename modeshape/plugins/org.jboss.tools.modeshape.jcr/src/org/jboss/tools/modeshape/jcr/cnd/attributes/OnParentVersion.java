/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import javax.jcr.version.OnParentVersionAction;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;

public enum OnParentVersion implements CndElement {

    ABORT(OnParentVersionAction.ABORT),
    COMPUTE(OnParentVersionAction.COMPUTE),
    COPY(OnParentVersionAction.COPY),
    IGNORE(OnParentVersionAction.IGNORE),
    INITIALIZE(OnParentVersionAction.INITIALIZE),
    VARIANT(-1),
    VERSION(OnParentVersionAction.VERSION);

    public static OnParentVersion DEFAULT_VALUE = COPY;

    public static OnParentVersion findUsingJcrValue( int jcrValue ) {
        for (OnParentVersion opv : OnParentVersion.values()) {
            if (opv.asJcrValue() == jcrValue) {
                return opv;
            }
        }

        throw new IllegalArgumentException(NLS.bind(Messages.invalidFindUsingJcrValueRequest, jcrValue));
    }

    public static OnParentVersion find( String notation ) {
        for (OnParentVersion opv : OnParentVersion.values()) {
            if (opv.toCndNotation(NotationType.LONG).equalsIgnoreCase(notation)
                    || opv.toCndNotation(NotationType.COMPRESSED).equalsIgnoreCase(notation)
                    || opv.toCndNotation(NotationType.COMPACT).equalsIgnoreCase(notation)) {
                return opv;
            }
        }

        throw new IllegalArgumentException(NLS.bind(Messages.invalidFindRequest, notation));
    }

    public static String[] toArray() {
        OnParentVersion[] allOpv = OnParentVersion.values();
        String[] notations = new String[allOpv.length];
        int i = 0;

        for (OnParentVersion opv : allOpv) {
            notations[i++] = opv.toCndNotation(NotationType.LONG);
        }

        return notations;
    }

    private final int jcrValue;

    private OnParentVersion( int jcrValue ) {
        this.jcrValue = jcrValue;
    }

    public int asJcrValue() {
        return this.jcrValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    public String toCndNotation( NotationType notationType ) {
        if ((this == DEFAULT_VALUE) && (NotationType.LONG != notationType)) {
            return Utils.EMPTY_STRING;
        }

        if (this == VARIANT) {
            return "OPV?"; //$NON-NLS-1$
        }

        return super.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return OnParentVersionAction.nameFromValue(asJcrValue());
    }
}
/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;

public enum PropertyType implements CndElement {

    BINARY(javax.jcr.PropertyType.BINARY),
    BOOLEAN(javax.jcr.PropertyType.BOOLEAN),
    DATE(javax.jcr.PropertyType.DATE),
    DECIMAL(javax.jcr.PropertyType.DECIMAL),
    DOUBLE(javax.jcr.PropertyType.DOUBLE),
    LONG(javax.jcr.PropertyType.LONG),
    NAME(javax.jcr.PropertyType.NAME),
    PATH(javax.jcr.PropertyType.PATH),
    REFERENCE(javax.jcr.PropertyType.REFERENCE),
    STRING(javax.jcr.PropertyType.STRING),
    UNDEFINED(javax.jcr.PropertyType.UNDEFINED),
    URI(javax.jcr.PropertyType.URI),
    VARIANT(-1),
    WEAKREFERENCE(javax.jcr.PropertyType.WEAKREFERENCE);

    /**
     * The default property type. Defaults to {@value PropertyType#STRING}.
     */
    public static final PropertyType DEFAULT_VALUE = PropertyType.STRING;

    public static final String NOTATION_PREFIX = "("; //$NON-NLS-1$

    public static final String NOTATION_SUFFIX = ")"; //$NON-NLS-1$

    public static final String UNDEFINED_ADDITIONAL_NOTATION = "*"; //$NON-NLS-1$

    public static PropertyType find( String value ) {
        if (UNDEFINED_ADDITIONAL_NOTATION.equals(value)) {
            return UNDEFINED;
        }

        for (PropertyType type : PropertyType.values()) {
            if (PropertyType.VARIANT == type) {
                continue;
            }

            if (type.toString().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException(NLS.bind(Messages.invalidFindRequest, value));
    }

    public static PropertyType findUsingJcrValue( int propertyType ) {
        for (PropertyType type : PropertyType.values()) {
            if (type.asJcrValue() == propertyType) {
                return type;
            }
        }

        throw new IllegalArgumentException(NLS.bind(Messages.invalidFindUsingJcrValueRequest, propertyType));
    }

    public static String[] validValues() {
        PropertyType[] allTypes = PropertyType.values();
        // add one for additional undefined notation added later but subtract one for variant
        String[] result = new String[allTypes.length];
        int i = 0;

        for (PropertyType type : allTypes) {
            if (type != VARIANT) {
                result[i++] = type.toString();
            }
        }

        result[i] = UNDEFINED_ADDITIONAL_NOTATION;
        return result;
    }

    private final int jcrValue;

    private PropertyType( int propertyType ) {
        this.jcrValue = propertyType;
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
        StringBuilder builder = new StringBuilder(NOTATION_PREFIX);

        if (this == VARIANT) {
            builder.append(AttributeState.VARIANT_CHAR);
        } else {
            builder.append(toString());
        }

        return builder.append(NOTATION_SUFFIX).toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return javax.jcr.PropertyType.nameFromValue(asJcrValue());
    }
}
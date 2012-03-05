/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import java.util.List;

import javax.jcr.query.qom.QueryObjectModelConstants;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators.QueryOperator;

/**
 * The primary item attribute used by property definitions.
 */
public final class QueryOperators extends ListAttributeState<QueryOperator> {

    /**
     * The CND notation for each notation type.
     */
    public static final String[] NOTATION = new String[] { "queryops", "qop", "qop" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     * The valid query operators.
     */
    public enum QueryOperator implements CndElement {

        /**
         * The equals sign.
         */
        EQUALS("=", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO), //$NON-NLS-1$

        /**
         * The greater than sign.
         */
        GREATER_THAN(">", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO), //$NON-NLS-1$

        /**
         * The greater than or equal to sign.
         */
        GREATER_THAN_EQUALS(">=", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO), //$NON-NLS-1$

        /**
         * The less than sign.
         */
        LESS_THAN("<", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO), //$NON-NLS-1$

        /**
         * The less than or equal to sign.
         */
        LESS_THAN_EQUALS("<=", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO), //$NON-NLS-1$

        /**
         * The LIKE sign.
         */
        LIKE("LIKE", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO), //$NON-NLS-1$

        /**
         * The not equals sign.
         */
        NOT_EQUALS("<>", QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO); //$NON-NLS-1$

        /**
         * @param notation the operator's CND notation (cannot be <code>null</code> or empty)
         * @return the query operator (never <code>null</code>)
         * @throws IllegalArgumentException if an invalid CND notation
         */
        public static QueryOperator find( String notation ) {
            for (QueryOperator operator : QueryOperator.values()) {
                if (operator.notation.equals(notation)) {
                    return operator;
                }
            }

            throw new IllegalArgumentException(NLS.bind(Messages.invalidFindRequest, notation));
        }

        /**
         * @param jcrValue the {@link QueryObjectModelConstants} value of the query operator (cannot be <code>null</code> or empty)
         * @return the query operator (never <code>null</code>)
         * @throws IllegalArgumentException if an invalid JCR value
         */
        public static QueryOperator findUsingJcrValue( String jcrValue ) {
            for (QueryOperator operator : QueryOperator.values()) {
                if (operator.asJcrValue().equals(jcrValue)) {
                    return operator;
                }
            }

            throw new IllegalArgumentException(NLS.bind(Messages.invalidFindUsingJcrValueRequest, jcrValue));
        }

        private final String notation;
        private final String jcrValue;

        private QueryOperator( String notation,
                               String jcrValue ) {
            this.notation = notation;
            this.jcrValue = jcrValue;
        }

        /**
         * @return the {@link QueryObjectModelConstants} value (never <code>null</code> or empty)
         */
        public String asJcrValue() {
            return this.jcrValue;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.jboss.tools.modeshape.jcr.cnd.CndElement#toCndNotation(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
         */
        @Override
        public String toCndNotation( NotationType notationType ) {
            return toString();
        }

        /**
         * {@inheritDoc}
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.notation;
        }
    }

    /**
     * @param operator the operator notation (cannot be <code>null</code> or empty)
     * @return <code>true</code> if added
     * @throws IllegalArgumentException if an invalid operator notation
     */
    public boolean add( String operator ) {
        Utils.verifyIsNotEmpty(operator, "operator"); //$NON-NLS-1$
        return add(QueryOperator.find(operator));
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 0;

        for (QueryOperator operator : QueryOperator.values()) {
            if (supports(operator)) {
                result = Utils.hashCode(result, operator);
            }
        }

        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.cnd.attributes.ListAttributeState#getCndNotationPrefix(org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType)
     */
    @Override
    protected String getCndNotationPrefix( NotationType notationType ) {
        if (NotationType.LONG == notationType) {
            return NOTATION[CndElement.NotationType.LONG_INDEX];
        }

        if (NotationType.COMPRESSED == notationType) {
            return NOTATION[CndElement.NotationType.COMPRESSED_INDEX];
        }

        return NOTATION[CndElement.NotationType.COMPACT_INDEX];
    }

    /**
     * @return a list of supported operator notations (never <code>null</code> but can be empty)
     */
    public String[] toArray() {
        List<QueryOperator> operators = getSupportedItems();

        if (Utils.isEmpty(operators)) {
            return Utils.EMPTY_STRING_ARRAY;
        }

        String[] result = new String[operators.size()];
        int i = 0;

        for (QueryOperator operator : operators) {
            result[i++] = operator.toString();
        }

        return result;
    }
}

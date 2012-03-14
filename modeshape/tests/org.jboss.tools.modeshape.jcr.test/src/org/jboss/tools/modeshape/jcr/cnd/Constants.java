/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType;
import org.jboss.tools.modeshape.jcr.cnd.CndNotationPreferences.Preference;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Abstract;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Autocreated;
import org.jboss.tools.modeshape.jcr.cnd.attributes.DefaultType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.DefaultValues;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Mandatory;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Mixin;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Multiple;
import org.jboss.tools.modeshape.jcr.cnd.attributes.NoFullText;
import org.jboss.tools.modeshape.jcr.cnd.attributes.NoQueryOrder;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Orderable;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PrimaryItem;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Protected;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators.QueryOperator;
import org.jboss.tools.modeshape.jcr.cnd.attributes.RequiredTypes;
import org.jboss.tools.modeshape.jcr.cnd.attributes.SameNameSiblings;
import org.jboss.tools.modeshape.jcr.cnd.attributes.SuperTypes;
import org.jboss.tools.modeshape.jcr.cnd.attributes.ValueConstraints;

/**
 * 
 */
public interface Constants {

    String QUALIFIER1 = "QUALIFIER1"; //$NON-NLS-1$
    String QUALIFIER2 = "QUALIFIER2"; //$NON-NLS-1$
    String QUALIFIER3 = "QUALIFIER3"; //$NON-NLS-1$

    String UNQUALIFIED_NAME1 = "UNQUALIFIED_NAME1"; //$NON-NLS-1$
    String UNQUALIFIED_NAME2 = "UNQUALIFIED_NAME2"; //$NON-NLS-1$
    String UNQUALIFIED_NAME3 = "UNQUALIFIED_NAME3"; //$NON-NLS-1$

    QualifiedName QUALIFIED_NAME1 = new QualifiedName(QUALIFIER1, UNQUALIFIED_NAME1);
    QualifiedName QUALIFIED_NAME2 = new QualifiedName(QUALIFIER2, UNQUALIFIED_NAME2);
    QualifiedName QUALIFIED_NAME3 = new QualifiedName(QUALIFIER3, UNQUALIFIED_NAME3);

    String VARIANT = AttributeState.VARIANT_STRING;

    String ABSTRACT_VARIANT_COMPACT_FORM = Abstract.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String ABSTRACT_VARIANT_COMPRESSED_FORM = Abstract.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String ABSTRACT_VARIANT_LONG_FORM = Abstract.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String AUTOCREATED_VARIANT_COMPACT_FORM = Autocreated.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String AUTOCREATED_VARIANT_COMPRESSED_FORM = Autocreated.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String AUTOCREATED_VARIANT_LONG_FORM = Autocreated.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String DEFAULT_TYPE_VARIANT_FORM = DefaultType.NOTATION
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_TYPE_END_PREFIX_DELIMITER) + VARIANT;
    String DEFAULT_TYPE = "jcr:data"; //$NON-NLS-1$
    String DEFAULT_TYPE_TYPE_FORM = DefaultType.NOTATION
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_TYPE_END_PREFIX_DELIMITER) + DEFAULT_TYPE;

    String MANDATORY_VARIANT_COMPACT_FORM = Mandatory.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String MANDATORY_VARIANT_COMPRESSED_FORM = Mandatory.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String MANDATORY_VARIANT_LONG_FORM = Mandatory.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String MIXIN_VARIANT_COMPACT_FORM = Mixin.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String MIXIN_VARIANT_COMPRESSED_FORM = Mixin.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String MIXIN_VARIANT_LONG_FORM = Mixin.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String MULTIPLE_VARIANT_COMPACT_FORM = Multiple.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String MULTIPLE_VARIANT_COMPRESSED_FORM = Multiple.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String MULTIPLE_VARIANT_LONG_FORM = Multiple.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String NO_FULL_TEXT_VARIANT_COMPACT_FORM = NoFullText.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String NO_FULL_TEXT_VARIANT_COMPRESSED_FORM = NoFullText.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String NO_FULL_TEXT_VARIANT_LONG_FORM = NoFullText.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String NO_QUERY_ORDER_VARIANT_COMPACT_FORM = NoQueryOrder.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String NO_QUERY_ORDER_VARIANT_COMPRESSED_FORM = NoQueryOrder.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String NO_QUERY_ORDER_VARIANT_LONG_FORM = NoQueryOrder.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String ORDERABLE_VARIANT_COMPACT_FORM = Orderable.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String ORDERABLE_VARIANT_COMPRESSED_FORM = Orderable.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String ORDERABLE_VARIANT_LONG_FORM = Orderable.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String PRIMARY_ITEM_VARIANT_COMPACT_FORM = PrimaryItem.NOTATION[NotationType.COMPACT_INDEX] + ' ' + VARIANT;
    String PRIMARY_ITEM_VARIANT_COMPRESSED_FORM = PrimaryItem.NOTATION[NotationType.COMPRESSED_INDEX] + ' ' + VARIANT;
    String PRIMARY_ITEM_VARIANT_LONG_FORM = PrimaryItem.NOTATION[NotationType.LONG_INDEX] + ' ' + VARIANT;
    String PRIMARY_ITEM = "jcr:data"; //$NON-NLS-1$
    String PRIMARY_ITEM_ITEM_COMPACT_FORM = PrimaryItem.NOTATION[NotationType.COMPACT_INDEX] + ' ' + PRIMARY_ITEM;
    String PRIMARY_ITEM_ITEM_COMPRESSED_FORM = PrimaryItem.NOTATION[NotationType.COMPRESSED_INDEX] + ' ' + PRIMARY_ITEM;
    String PRIMARY_ITEM_ITEM_LONG_FORM = PrimaryItem.NOTATION[NotationType.LONG_INDEX] + ' ' + PRIMARY_ITEM;

    String PROTECTED_VARIANT_COMPACT_FORM = Protected.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String PROTECTED_VARIANT_COMPRESSED_FORM = Protected.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String PROTECTED_VARIANT_LONG_FORM = Protected.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String QUERY_OPS_COMPACT_FORM = QueryOperators.NOTATION[NotationType.COMPACT_INDEX]
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    String QUERY_OPS_COMPRESSED_FORM = QueryOperators.NOTATION[NotationType.COMPRESSED_INDEX]
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    String QUERY_OPS_LONG_FORM = QueryOperators.NOTATION[NotationType.LONG_INDEX]
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER);
    String QUERY_OPS_VARIANT_COMPACT_FORM = QUERY_OPS_COMPACT_FORM + VARIANT;
    String QUERY_OPS_VARIANT_COMPRESSED_FORM = QUERY_OPS_COMPRESSED_FORM + VARIANT;
    String QUERY_OPS_VARIANT_LONG_FORM = QUERY_OPS_LONG_FORM + VARIANT;
    QueryOperator OPERATOR_ONE = QueryOperator.EQUALS;
    QueryOperator OPERATOR_TWO = QueryOperator.GREATER_THAN;
    QueryOperator OPERATOR_THREE = QueryOperator.LESS_THAN;
    String QUERY_OPS_ONE_OPERATOR_COMPACT_FORM = QUERY_OPS_COMPACT_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_ONE_OPERATOR_COMPRESSED_FORM = QUERY_OPS_COMPRESSED_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_ONE_OPERATOR_LONG_FORM = QUERY_OPS_LONG_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_THREE_OPERATOR_COMPACT_FORM = QUERY_OPS_COMPACT_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_TWO.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_THREE.toCndNotation(NotationType.COMPACT)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_THREE_OPERATOR_COMPRESSED_FORM = QUERY_OPS_COMPRESSED_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_TWO.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_THREE.toCndNotation(NotationType.COMPRESSED)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);
    String QUERY_OPS_THREE_OPERATOR_LONG_FORM = QUERY_OPS_LONG_FORM
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + OPERATOR_ONE.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_TWO.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + OPERATOR_THREE.toCndNotation(NotationType.LONG)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR);

    String SAME_NAME_SIBLINGS_VARIANT_COMPACT_FORM = SameNameSiblings.NOTATION[NotationType.COMPACT_INDEX] + VARIANT;
    String SAME_NAME_SIBLINGS_VARIANT_COMPRESSED_FORM = SameNameSiblings.NOTATION[NotationType.COMPRESSED_INDEX] + VARIANT;
    String SAME_NAME_SIBLINGS_VARIANT_LONG_FORM = SameNameSiblings.NOTATION[NotationType.LONG_INDEX] + VARIANT;

    String ITEM_ONE = "item1"; //$NON-NLS-1$ 
    String ITEM_TWO = "item2"; //$NON-NLS-1$ 
    String ITEM_THREE = "item3"; //$NON-NLS-1$
    String ONE_ITEM_SINGLE_QUOTED_FORM = CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + ITEM_ONE + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);
    String THREE_ITEM_SINGLE_QUOTED_FORM = CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + ITEM_ONE + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + ITEM_TWO
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + ITEM_THREE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);

    String DEFAULT_VALUES_VARIANT = DefaultValues.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER) + VARIANT;
    String DEFAULT_VALUES_ONE_ITEM_FORM = DefaultValues.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_ONE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER);
    String DEFAULT_VALUES_THREE_ITEM_FORM = DefaultValues.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_ONE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_TWO
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR) + ITEM_THREE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.DEFAULT_VALUES_QUOTE_CHARACTER);

    String REQUIRED_TYPES_VARIANT = RequiredTypes.NOTATION_PREFIX + VARIANT + RequiredTypes.NOTATION_SUFFIX;
    String REQUIRED_TYPES_ONE_ITEM_FORM = RequiredTypes.NOTATION_PREFIX + QUALIFIED_NAME1.get() + RequiredTypes.NOTATION_SUFFIX;
    String REQUIRED_TYPES_THREE_ITEM_FORM = RequiredTypes.NOTATION_PREFIX + QUALIFIED_NAME1.get()
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + QUALIFIED_NAME2.get()
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER) + QUALIFIED_NAME3.get()
            + RequiredTypes.NOTATION_SUFFIX;

    String SUPER_TYPES_VARIANT = SuperTypes.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER) + VARIANT;
    String SUPER_TYPES_ONE_ITEM_FORM = SuperTypes.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER)
            + ONE_ITEM_SINGLE_QUOTED_FORM;
    String SUPER_TYPES_THREE_ITEM_FORM = SuperTypes.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER)
            + THREE_ITEM_SINGLE_QUOTED_FORM;

    String VALUE_CONSTRAINTS_VARIANT = ValueConstraints.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER) + VARIANT;
    String VALUE_CONSTRAINTS_ONE_ITEM_FORM = ValueConstraints.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + ITEM_ONE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);
    String VALUE_CONSTRAINTS_THREE_ITEM_FORM = ValueConstraints.NOTATION_PREFIX
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + ITEM_ONE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + ITEM_TWO
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER) + ITEM_THREE
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER)
            + CndNotationPreferences.DEFAULT_PREFERENCES.get(Preference.ATTRIBUTE_LIST_QUOTE_CHAR);
}

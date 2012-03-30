/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.tools.modeshape.jcr.Utils;

/**
 * Preferences used during constructing CND notation.
 */
public class CndNotationPreferences {

    /**
     * Default preferences.
     */
    public static final CndNotationPreferences DEFAULT_PREFERENCES = new CndNotationPreferences();

    /**
     * The preferences (never <code>null</code>).
     */
    private final Map<Preference, String> prefs;

    /**
     * Constructs default preferences.
     */
    private CndNotationPreferences() {
        final Map<Preference, String> temp = new HashMap<Preference, String>();
        loadDefaults(temp);
        this.prefs = Collections.unmodifiableMap(temp);
    }

    /**
     * @param overrideValues the preferences whose values are being overridden (cannot be <code>null</code>)
     */
    public CndNotationPreferences( final Map<Preference, String> overrideValues ) {
        Utils.verifyIsNotNull(overrideValues, "overrideValues"); //$NON-NLS-1$

        final Map<Preference, String> temp = new HashMap<Preference, String>();
        loadDefaults(temp);

        if ((overrideValues != null) && !overrideValues.isEmpty()) {
            for (final Entry<Preference, String> entry : overrideValues.entrySet()) {
                temp.put(entry.getKey(), entry.getValue());
            }
        }

        this.prefs = Collections.unmodifiableMap(temp);
    }

    /**
     * @param pref the preference name whose value is being requested (cannot be <code>null</code>)
     * @return the preference value (never <code>null</code> but can be empty)
     */
    public String get( final Preference pref ) {
        Utils.verifyIsNotNull(pref, "pref"); //$NON-NLS-1$
        return this.prefs.get(pref);
    }

    private void loadDefaults( final Map<Preference, String> map ) {
        assert (map != null) : "preference map is null"; //$NON-NLS-1$

        map.put(Preference.ATTRIBUTE_LIST_ELEMENT_DELIMITER, ", "); //$NON-NLS-1$
        map.put(Preference.ATTRIBUTE_LIST_ITEM_QUOTE_CHAR, Utils.EMPTY_STRING);
        map.put(Preference.CHILD_NODE_DEFINITION_END_PREFIX_DELIMITER, " "); //$NON-NLS-1$
        map.put(Preference.CHILD_NODE_PROPERTY_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.DEFAULT_VALUES_END_PREFIX_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.DEFAULT_VALUES_QUOTE_CHARACTER, "'"); //$NON-NLS-1$
        map.put(Preference.DEFAULT_TYPE_END_PREFIX_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.ATTRIBUTE_LIST_QUOTE_CHAR, Utils.EMPTY_STRING);
        map.put(Preference.ELEMENT_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.ELEMENTS_END_DELIMITER, Utils.EMPTY_STRING);
        map.put(Preference.ELEMENTS_START_DELIMITER, "\t"); //$NON-NLS-1$
        map.put(Preference.NAMESPACE_MAPPING_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.NAMESPACE_MAPPING_SECTION_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.CHILD_NODE_ATTRIBUTES_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.NODE_TYPE_DEFINITION_ATTRIBUTES_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.NODE_TYPE_DEFINITION_ATTRIBUTES_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.NODE_TYPE_DEFINITION_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.NODE_TYPE_DEFINITION_NAME_END_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.NODE_TYPE_DEFINITION_SECTION_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.PROPERTY_DEFINITION_ATTRIBUTES_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.PROPERTY_DEFINITION_END_PREFIX_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.REQUIRED_TYPES_END_PREFIX_DELIMITER, Utils.EMPTY_STRING);
        map.put(Preference.SUPER_TYPES_END_DELIMITER, Utils.SPACE_STRING);
        map.put(Preference.VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER, "'"); //$NON-NLS-1$
    }

    /**
     * All preferences used during CND notation construction.
     */
    public enum Preference {

        /**
         * The delimiter between list items.
         */
        ATTRIBUTE_LIST_ELEMENT_DELIMITER,

        /**
         * The string version of the quote character (empty string, single quote, or double quote) surrounding each item in
         * attribute lists.
         */
        ATTRIBUTE_LIST_ITEM_QUOTE_CHAR,

        /**
         * The delimiter after the prefix to attribute lists.
         */
        ATTRIBUTE_LIST_PREFIX_END_DELIMITER,

        /**
         * The quote character (single or double) surrounding attribute lists.
         */
        ATTRIBUTE_LIST_QUOTE_CHAR,

        /**
         * The delimiter between child node definition attributes.
         */
        CHILD_NODE_ATTRIBUTES_DELIMITER,

        /**
         * The delimiter after the plus sign and before the child node definition name.
         */
        CHILD_NODE_DEFINITION_END_PREFIX_DELIMITER,

        /**
         * The delimiter between child node properties.
         */
        CHILD_NODE_PROPERTY_DELIMITER,

        /**
         * The delimiter after the equals sign and before the default type.
         */
        DEFAULT_TYPE_END_PREFIX_DELIMITER,

        /**
         * The delimiter after the equals sign and before the list of default values.
         */
        DEFAULT_VALUES_END_PREFIX_DELIMITER,

        /**
         * The string form of the quotation character used to surround the one or more default values. Value can be either empty,
         * the single quote, or the double quote.
         */
        DEFAULT_VALUES_QUOTE_CHARACTER,

        /**
         * The delimiter between CND elements.
         */
        ELEMENT_DELIMITER,

        /**
         * The delimiter after the last CND element.
         */
        ELEMENTS_END_DELIMITER,

        /**
         * The delimiter before the first CND element.
         */
        ELEMENTS_START_DELIMITER,

        /**
         * The delimiter between namespace mappings.
         */
        NAMESPACE_MAPPING_DELIMITER,

        /**
         * The delimiter after the last namespace mapping.
         */
        NAMESPACE_MAPPING_SECTION_END_DELIMITER,

        /**
         * The delimiter between node type definition attributes.
         */
        NODE_TYPE_DEFINITION_ATTRIBUTES_DELIMITER,

        /**
         * The delimiter after all the node type definition attributes.
         */
        NODE_TYPE_DEFINITION_ATTRIBUTES_END_DELIMITER,

        /**
         * The delimiter between node type definitions.
         */
        NODE_TYPE_DEFINITION_DELIMITER,

        /**
         * The delimiter after the node type definition name.
         */
        NODE_TYPE_DEFINITION_NAME_END_DELIMITER,

        /**
         * The delimiter after the last node type definitions.
         */
        NODE_TYPE_DEFINITION_SECTION_END_DELIMITER,

        /**
         * The delimiter between property definition attributes.
         */
        PROPERTY_DEFINITION_ATTRIBUTES_DELIMITER,

        /**
         * The delimiter after the dash and before the property definition name.
         */
        PROPERTY_DEFINITION_END_PREFIX_DELIMITER,

        /**
         * The delimiter after the open paren and before the required types.
         */
        REQUIRED_TYPES_END_PREFIX_DELIMITER,

        /**
         * The delimiter after the last super type.
         */
        SUPER_TYPES_END_DELIMITER,

        /**
         * The string version of the quote character (empty string, single quote, or double quote) surrounding each item in value
         * constraints list.
         */
        VALUE_CONSTRAINTS_ITEM_QUOTE_CHARACTER,
    }
}

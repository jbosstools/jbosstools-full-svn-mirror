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

    public enum Preference {

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
         * The delimiter between child node definitions.
         */
        CHILD_NODE_DEFINITION_DELIMITER,

        /**
         * The delimiter after the last child node definition.
         */
        CHILD_NODE_DEFINITION_SECTION_END_DELIMITER,

        /**
         * The delimiter between child node properties.
         */
        CHILD_NODE_PROPERTY_DELIMITER,

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
         * The delimiter between property definitions.
         */
        PROPERTY_DEFINITION_DELIMITER,

        /**
         * The delimiter after the last property definition.
         */
        PROPERTY_DEFINITION_SECTION_END_DELIMITER,

        /**
         * The delimiter after the last super type.
         */
        SUPER_TYPES_END_DELIMITER,
    }

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
        Map<Preference, String> temp = new HashMap<Preference, String>();
        loadDefaults(temp);
        this.prefs = Collections.unmodifiableMap(temp);
    }

    /**
     * @param overrideValues the preferences whose values are being overridden (cannot be <code>null</code>)
     */
    public CndNotationPreferences( Map<Preference, String> overrideValues ) {
        Utils.isNotNull(overrideValues, "overrideValues"); //$NON-NLS-1$

        Map<Preference, String> temp = new HashMap<Preference, String>();
        loadDefaults(temp);

        if ((overrideValues != null) && !overrideValues.isEmpty()) {
            for (Entry<Preference, String> entry : overrideValues.entrySet()) {
                temp.put(entry.getKey(), entry.getValue());
            }
        }

        this.prefs = Collections.unmodifiableMap(temp);
    }

    /**
     * @param pref the preference name whose value is being requested (cannot be <code>null</code>)
     * @return the preference value (never <code>null</code> but can be empty)
     */
    public String get( Preference pref ) {
        Utils.isNotNull(pref, "pref"); //$NON-NLS-1$
        return this.prefs.get(pref);
    }

    private void loadDefaults( Map<Preference, String> map ) {
        assert (map != null) : "preference map is null"; //$NON-NLS-1$

        map.put(Preference.CHILD_NODE_DEFINITION_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.CHILD_NODE_PROPERTY_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.CHILD_NODE_DEFINITION_SECTION_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.ATTRIBUTE_LIST_PREFIX_END_DELIMITER, " "); //$NON-NLS-1$
        map.put(Preference.ATTRIBUTE_LIST_QUOTE_CHAR, "'"); //$NON-NLS-1$
        map.put(Preference.NAMESPACE_MAPPING_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.NAMESPACE_MAPPING_SECTION_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.CHILD_NODE_ATTRIBUTES_DELIMITER, " "); //$NON-NLS-1$
        map.put(Preference.NODE_TYPE_DEFINITION_ATTRIBUTES_DELIMITER, " "); //$NON-NLS-1$
        map.put(Preference.NODE_TYPE_DEFINITION_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.NODE_TYPE_DEFINITION_NAME_END_DELIMITER, " "); //$NON-NLS-1$
        map.put(Preference.NODE_TYPE_DEFINITION_SECTION_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.PROPERTY_DEFINITION_ATTRIBUTES_DELIMITER, " "); //$NON-NLS-1$
        map.put(Preference.PROPERTY_DEFINITION_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.PROPERTY_DEFINITION_SECTION_END_DELIMITER, "\n"); //$NON-NLS-1$
        map.put(Preference.SUPER_TYPES_END_DELIMITER, "\n"); //$NON-NLS-1$
    }
}

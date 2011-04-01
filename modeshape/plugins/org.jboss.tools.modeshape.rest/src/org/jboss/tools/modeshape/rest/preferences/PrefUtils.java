/*
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * This software is made available by Red Hat, Inc. under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.tools.modeshape.rest.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.Utils;
import org.modeshape.common.util.CheckArg;

/**
 * The <code>PrefUtils</code> class provides common utilities relating to preferences. This class assumes the Eclipse runtime
 * platform is running.
 */
public final class PrefUtils {

    /**
     * @param propertyId the property name whose list values are being requested (never <code>null</code>)
     * @param delimiter the character separating the items in the property value
     * @param removeDuplicates a flag indicating if duplicate items should be removed
     * @return the property value items (never <code>null</code>)
     */
    public static String[] getListPropertyValue( String propertyId,
                                                 char delimiter,
                                                 boolean removeDuplicates ) {
        CheckArg.isNotNull(propertyId, "propertyId"); //$NON-NLS-1$
        return Utils.getTokens(getPreferenceStore().getString(propertyId), Character.toString(delimiter), removeDuplicates);
    }

    /**
     * @return the plugin preference store
     */
    public static IPreferenceStore getPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }

    /**
     * @param propertyId the property name being set (never <code>null</code>)
     * @param items the items used to create the property value (never <code>null</code>)
     * @param delimiter the character to use to separate the items
     */
    public static void setListPropertyValue( String propertyId,
                                             String[] items,
                                             char delimiter ) {
        CheckArg.isNotNull(propertyId, "propertyId"); //$NON-NLS-1$
        CheckArg.isNotNull(items, "items"); //$NON-NLS-1$
        getPreferenceStore().setValue(propertyId, Utils.combineTokens(items, delimiter));
    }

    /**
     * Don't allow construction.
     */
    private PrefUtils() {
        // nothing to do
    }
}

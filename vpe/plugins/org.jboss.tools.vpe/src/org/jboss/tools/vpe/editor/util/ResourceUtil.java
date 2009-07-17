/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.editor.util;

import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;

/**
 * @author Evgenij Stherbin
 *
 */

public class ResourceUtil {
    /**
     * Gets the bundle value.
     * 
     * @param value the value
     * @param offfset      *
     * param pageContext the page context
     * @param pageContext the page context
     * @param offset the offset
     * 
     * @return the bundle value
     */
    public static String getBundleValue(VpePageContext pageContext, String value) {
        BundleMap bundle = pageContext.getBundle();
        return bundle.getBundleValue(value);

    }
}

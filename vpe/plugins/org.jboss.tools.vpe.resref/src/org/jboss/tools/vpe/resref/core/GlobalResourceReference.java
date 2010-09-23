/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.resref.core;

import org.jboss.tools.common.resref.core.ResourceReference;


/**
 * Global resource reference
 * @author Evgenij Stherbin
 *
 */
public class GlobalResourceReference extends ResourceReference {

    /**
     * @param location
     * @param scope
     */
    public GlobalResourceReference(String location, int scope) {
        super(location, scope);
    }

}

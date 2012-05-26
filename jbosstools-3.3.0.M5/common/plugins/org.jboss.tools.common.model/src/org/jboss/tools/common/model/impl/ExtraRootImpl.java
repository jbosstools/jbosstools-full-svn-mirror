/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.impl;

public class ExtraRootImpl extends RegularObjectImpl {
	private static final long serialVersionUID = 1L;

	public ExtraRootImpl() {}

    public boolean isActive() {
        return true;
    }

    public String getPathPart() {
        return "root:" + super.getPathPart(); //$NON-NLS-1$
    }
    public String getLongPath() {
        return getPathPart();
    }

    public String getPath() {
        return getPathPart();
    }

}

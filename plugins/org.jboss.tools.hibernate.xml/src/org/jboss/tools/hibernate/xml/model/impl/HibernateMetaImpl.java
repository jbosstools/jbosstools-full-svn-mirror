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
package org.jboss.tools.hibernate.xml.model.impl;

import org.jboss.tools.common.model.impl.RegularObjectImpl;

public class HibernateMetaImpl extends RegularObjectImpl {
    private static final long serialVersionUID = 6834715619315516824L;
	
	public String name() {
		return "" + getAttributeValue("attribute"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public String getPathPart() {
		String v = getAttributeValue("value"); //$NON-NLS-1$
		int hash = (v == null) ? -1 : v.hashCode();
		return name() + "[" + hash + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}

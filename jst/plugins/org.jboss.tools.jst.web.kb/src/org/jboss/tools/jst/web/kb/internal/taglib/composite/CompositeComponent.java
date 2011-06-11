/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.composite;

import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent;
import org.jboss.tools.jst.web.kb.internal.taglib.StaticAttribute;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;

public class CompositeComponent extends AbstractComponent {

	public static final String[] STATIC_ATTRIBUTES = new String[] {"id", "rendered"}; //$NON-NLS-1$ //$NON-NLS-2$

	public CompositeComponent() {
		for (String attribute : STATIC_ATTRIBUTES) {
			IAttribute staticAttribute = new StaticAttribute(this, attribute);
			attributes.put(attribute, staticAttribute);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#getXMLClass()
	 */
	@Override
	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_COMPOSITE_LIBRARY;
	}
}
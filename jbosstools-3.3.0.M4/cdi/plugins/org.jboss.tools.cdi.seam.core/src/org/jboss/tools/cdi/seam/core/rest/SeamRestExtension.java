/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.seam.core.rest;

import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.extension.ICDIExtension;
import org.jboss.tools.cdi.core.extension.feature.IInjectionPointValidatorFeature;

/**
 * @author Alexey Kazakov
 */
public class SeamRestExtension implements ICDIExtension, IInjectionPointValidatorFeature {

	private static final String REST_CLIENT_TYPE_NAME = "org.jboss.seam.rest.client.RestClient";

	/* (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.extension.feature.IInjectionPointValidatorFeature#shouldIgnoreInjection(org.eclipse.jdt.core.IType, org.jboss.tools.cdi.core.IInjectionPoint)
	 */
	@Override
	public boolean shouldIgnoreInjection(IType typeOfInjectionPoint, IInjectionPoint injection) {
		return injection.getAnnotation(REST_CLIENT_TYPE_NAME) != null;
	}
}
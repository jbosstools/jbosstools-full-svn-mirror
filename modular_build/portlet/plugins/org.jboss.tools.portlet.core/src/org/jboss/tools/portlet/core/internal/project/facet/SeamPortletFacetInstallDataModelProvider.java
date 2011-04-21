/*************************************************************************************
 * Copyright (c) 2008-2009 JBoss by Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal.project.facet;

import java.util.Set;

import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.jboss.tools.portlet.core.IPortletConstants;

/**
 * @author snjeza
 * 
 */
public class SeamPortletFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements IPortletConstants {

	@Override
	public Object getDefaultProperty(String propertyName) {
		if(propertyName.equals(FACET_ID)){
			return IPortletConstants.SEAMPORTLET_FACET_ID;
		}
		
		return super.getDefaultProperty(propertyName);
	}

	@Override
	public Set<String> getPropertyNames() {
		Set<String> propertyNames = super.getPropertyNames();
		return propertyNames;
	}
}

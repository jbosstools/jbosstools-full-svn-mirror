/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal.project.facet;

import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.jboss.tools.portlet.core.IPortletConstants;

/**
 * @author snjeza
 * 
 */
public class PortletFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements IPortletConstants {

	@Override
	public Object getDefaultProperty(String propertyName) {
		if(propertyName.equals(FACET_ID)){
			return IPortletConstants.PORTLET_FACET_ID;
		}
		return super.getDefaultProperty(propertyName);
	}

}

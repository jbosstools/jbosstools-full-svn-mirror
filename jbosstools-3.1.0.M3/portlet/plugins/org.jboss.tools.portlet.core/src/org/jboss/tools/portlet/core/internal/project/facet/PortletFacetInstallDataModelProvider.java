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
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

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
		if (propertyName.equals(IPortletConstants.DEPLOY_PORTLET_JARS)) {
			return Boolean.FALSE;
		}
		if (propertyName.equals(IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(IPortletConstants.USER_LIBRARY_NAME)) {
			return ""; //$NON-NLS-1$
		}
		if (propertyName.equals(IPortletConstants.IMPLEMENTATION_LIBRARY)) {
			boolean checkRuntimes = PortletCoreActivator.getDefault()
					.getPluginPreferences().getBoolean(
							PortletCoreActivator.CHECK_RUNTIMES);
			if (checkRuntimes) {
				return IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME;
			} else {
				return IPortletConstants.USER_LIBRARY;
			}
			//return IPortletConstants.LIBRARY_PROVIDED_BY_JBOSS_TOOLS;
		}
		return super.getDefaultProperty(propertyName);
	}
	
	@Override
	public Set<String> getPropertyNames() {
		Set<String> propertyNames = super.getPropertyNames();
		propertyNames.add(IPortletConstants.DEPLOY_PORTLET_JARS);
		propertyNames.add(IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY);
		propertyNames.add(IPortletConstants.USER_LIBRARY_NAME);
		propertyNames.add(IPortletConstants.IMPLEMENTATION_LIBRARY);
		
		return propertyNames;
	}

}

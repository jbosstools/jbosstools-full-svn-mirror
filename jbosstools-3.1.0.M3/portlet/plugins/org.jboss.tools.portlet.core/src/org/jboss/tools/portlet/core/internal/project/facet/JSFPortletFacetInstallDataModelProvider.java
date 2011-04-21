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

/**
 * @author snjeza
 * 
 */
public class JSFPortletFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements IPortletConstants {

	@Override
	public Object getDefaultProperty(String propertyName) {
		if(propertyName.equals(FACET_ID)){
			return IPortletConstants.JSFPORTLET_FACET_ID;
		}
		if (propertyName.equals(IPortletConstants.DEPLOY_JARS)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(IPortletConstants.RICHFACES_LIBRARIES_SELECTED)) {
			return Boolean.FALSE;
		}
		if (propertyName.equals(IPortletConstants.RICHFACES_LIBRARIES_TYPE)) {
			return ""; //$NON-NLS-1$
		}
		if (propertyName.equals(IPortletConstants.RICHFACES_CAPABILITIES)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(IPortletConstants.PORTLET_BRIDGE_RUNTIME)) {
			return ""; //$NON-NLS-1$
		}
		if (propertyName.equals(IPortletConstants.RICHFACES_RUNTIME)) {
			return ""; //$NON-NLS-1$
		}
		if (propertyName.equals(IPortletConstants.IMPLEMENTATION_LIBRARY)) {
			return IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE;
		}
		if (propertyName.equals(IPortletConstants.USER_LIBRARY_NAME)) {
			return ""; //$NON-NLS-1$
		}
		return super.getDefaultProperty(propertyName);
	}

	@Override
	public Set<String> getPropertyNames() {
		Set<String> propertyNames = super.getPropertyNames();
		propertyNames.add(IPortletConstants.DEPLOY_JARS);
		propertyNames.add(IPortletConstants.PORTLET_BRIDGE_RUNTIME);
		propertyNames.add(IPortletConstants.IMPLEMENTATION_LIBRARY);
		propertyNames.add(IPortletConstants.USER_LIBRARY_NAME);
		propertyNames.add(IPortletConstants.RICHFACES_RUNTIME);
		propertyNames.add(IPortletConstants.RICHFACES_LIBRARIES_SELECTED);
		propertyNames.add(IPortletConstants.RICHFACES_LIBRARIES_TYPE);
		propertyNames.add(IPortletConstants.RICHFACES_CAPABILITIES);
		return propertyNames;
	}
}

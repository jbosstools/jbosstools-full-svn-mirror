/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.common.project.facet.core.libprov.IPropertyChangeListener;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.portlet.core.IPortletConstants;

/**
 * @author snjeza
 * 
 */
public class JSFPortletFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements IPortletConstants {

	private LibraryInstallDelegate libraryInstallDelegate = null;
    
	private void initLibraryInstallDelegate() {
		final IFacetedProjectWorkingCopy fpjwc = (IFacetedProjectWorkingCopy) getProperty(FACETED_PROJECT_WORKING_COPY);
		final IProjectFacetVersion fv = (IProjectFacetVersion) getProperty(FACET_VERSION);
		if (libraryInstallDelegate == null && fpjwc != null && fv != null) {
			libraryInstallDelegate = new LibraryInstallDelegate(fpjwc, fv);
			libraryInstallDelegate.addListener(new IPropertyChangeListener() {

				public void propertyChanged(final String property,
								final Object oldValue, final Object newValue) {
					final IDataModel dm = getDataModel();
					if (dm != null) {
						dm.notifyPropertyChange(JSFPORTLET_LIBRARY_PROVIDER_DELEGATE,IDataModel.VALUE_CHG);
					}
				}
				
			});
		}
	}
	
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
		if (propertyName.equals(IPortletConstants.IS_EPP)) {
			return Boolean.FALSE;
		}
		if (propertyName.equals(IPortletConstants.USER_LIBRARY_NAME)) {
			return ""; //$NON-NLS-1$
		}
		if (propertyName.equals(JSFPORTLET_LIBRARY_PROVIDER_DELEGATE)) {
            return libraryInstallDelegate;
		}
		return super.getDefaultProperty(propertyName);
	}

	@Override
	public Set<String> getPropertyNames() {
		Set<String> propertyNames = super.getPropertyNames();
		propertyNames.add(IPortletConstants.DEPLOY_JARS);
		propertyNames.add(IPortletConstants.PORTLET_BRIDGE_RUNTIME);
		propertyNames.add(IPortletConstants.IMPLEMENTATION_LIBRARY);
		propertyNames.add(IPortletConstants.IS_EPP);
		propertyNames.add(IPortletConstants.USER_LIBRARY_NAME);
		propertyNames.add(IPortletConstants.RICHFACES_RUNTIME);
		propertyNames.add(IPortletConstants.RICHFACES_LIBRARIES_SELECTED);
		propertyNames.add(IPortletConstants.RICHFACES_LIBRARIES_TYPE);
		propertyNames.add(IPortletConstants.RICHFACES_CAPABILITIES);
		propertyNames.add(JSFPORTLET_LIBRARY_PROVIDER_DELEGATE);
		
		return propertyNames;
	}
	
	@Override
	public boolean propertySet(final String propertyName,
			final Object propertyValue) {
		if (propertyName.equals(FACETED_PROJECT_WORKING_COPY)
				|| propertyName.equals(FACET_VERSION)) {
			initLibraryInstallDelegate();
			if (this.libraryInstallDelegate != null && propertyName.equals(FACET_VERSION)) {
				final IProjectFacetVersion fv = (IProjectFacetVersion) getProperty(FACET_VERSION);
				this.libraryInstallDelegate.setProjectFacetVersion(fv);
			}
		}

		return super.propertySet(propertyName, propertyValue);
	}

	public IStatus validate(String name) {
		if (name.equals(JSFPORTLET_LIBRARY_PROVIDER_DELEGATE)) {
			return ((LibraryInstallDelegate) getProperty(JSFPORTLET_LIBRARY_PROVIDER_DELEGATE)).validate();
		}
		return super.validate(name);
	}
}

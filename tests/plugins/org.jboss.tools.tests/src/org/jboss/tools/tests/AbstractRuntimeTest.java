/******************************************************************************* 
 * Copyright (c) 2007-2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *     Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.tests;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class AbstractRuntimeTest extends TestCase{
	
	public static final String BUNDLE_GROUP_PROVIDER_NAME = "Update Manager Configurator";
	
	/**
	 * @param featureId
	 */
	public Bundle getFirstBundleFor(String featureId) {
		IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();
		System.out.println(providers.length);
		IBundleGroup iBundleGroup = getFirstBundleGroupFor(featureId);
		Bundle[] bundles = iBundleGroup.getBundles();
		for (Bundle bundle : bundles) {
			return bundle;
		}
		return null;
	}

	
	public IBundleGroup getFirstBundleGroupFor(String featureId) {
		IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();
		System.out.println(providers.length);
		for (IBundleGroupProvider iBundleGroupProvider : providers) {
			System.out.println(iBundleGroupProvider.getName());
			IBundleGroup[] bundleGroups = iBundleGroupProvider.getBundleGroups();
			if(BUNDLE_GROUP_PROVIDER_NAME.equals(iBundleGroupProvider.getName())) {
				for (IBundleGroup iBundleGroup : bundleGroups) {
					if(iBundleGroup.getIdentifier().equals(featureId)) {
						return  iBundleGroup;
					}
				}
			}
		}
		return null;
	}

	
	
	public boolean isPluginResolved(String pluginId) {
		Bundle bundle = Platform.getBundle(pluginId);
		assertNotNull(pluginId + " failed to load.", bundle); //$NON-NLS-1$
		try {
			// this line is needed to to force plug-in loading and to change it state to ACTIVE 
			bundle.loadClass("fake class"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			// It happens always because loaded class doesn't not exist
		}
		return ((bundle.getState() & Bundle.RESOLVED) > 0)
				|| ((bundle.getState() & Bundle.ACTIVE) > 0);
	}
}

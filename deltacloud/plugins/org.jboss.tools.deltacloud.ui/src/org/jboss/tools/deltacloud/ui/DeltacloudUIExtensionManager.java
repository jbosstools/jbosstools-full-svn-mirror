/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;


public class DeltacloudUIExtensionManager {
	/** Singleton instance of the manager */
	private static DeltacloudUIExtensionManager instance;
	
	/** Singleton getter */
	public static DeltacloudUIExtensionManager getDefault() {
		if( instance == null ) 
			instance = new DeltacloudUIExtensionManager();
		return instance;
	}
	
	public INewInstanceWizardPage[] loadNewInstanceWizardPages() {
		ArrayList<WizardPage> pages = new ArrayList<WizardPage>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(Activator.PLUGIN_ID, "newInstanceWizardPage"); //$NON-NLS-1$
		Object o = null;
		for( int i = 0; i < cf.length; i++ ) {
			o = null;
			try {
				o = cf[i].createExecutableExtension("class");
			} catch(CoreException ce) {
				ce.printStackTrace();
			}
			if( o != null ) {
				pages.add((WizardPage)o);
			}
		}
		return (INewInstanceWizardPage[]) pages.toArray(new INewInstanceWizardPage[pages.size()]);
	}

}

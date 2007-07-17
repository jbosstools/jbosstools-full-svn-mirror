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
package org.jboss.tools.seam.internal.core.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author eskimo
 *
 */
public class SeamFacetPostInstallDelegate implements
		IDelegate {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject, org.eclipse.wst.common.project.facet.core.IProjectFacetVersion, java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor) throws CoreException {
		IEclipsePreferences prefs = SeamCorePlugin.getSeamFacetPreferences(project);
		IDataModel model = (IDataModel)config;
		for (Object propertyName : model.getAllProperties()) {
			Object value = model.getProperty(propertyName.toString());
			prefs.put(propertyName.toString(), value==null?"":value.toString());
		}
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			SeamCorePlugin.getPluginLog().logError(e);
		}
	}
}

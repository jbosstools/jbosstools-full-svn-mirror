 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.internal.core.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ltk.core.refactoring.Change;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Alexey Kazakov
 */
public class SeamRenameFolderChange extends SeamProjectChange {

	private IPath oldPath;
	private IPath newPath;	

	private List<String> relevantProperties = new ArrayList<String>();

	public SeamRenameFolderChange(IProject project, String newName, IPath oldPath) {
		super(project);
		this.oldPath = oldPath;
		this.newPath = oldPath.removeLastSegments(1).append(newName);

		IEclipsePreferences ps = getSeamPreferences();
		for (int i = 0; i < FOLDER_PROPERTIES.length; i++) {
			String propertyValue = ps.get(FOLDER_PROPERTIES[i], null);
			if(propertyValue==null) {
				continue;
			}
			String oldPathString = oldPath.toString();
			if(propertyValue.equals(oldPathString) ||
					propertyValue.startsWith(oldPathString + "/")) {
				relevantProperties.add(FOLDER_PROPERTIES[i]);
			} 
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.refactoring.SeamProjectChange#isRelevant()
	 */
	@Override
	public boolean isRelevant() {
		return relevantProperties.size()>0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		if(!isRelevant()) {
			return null;
		}
		try {
			pm.beginTask(getName(), 1);

			IEclipsePreferences ps = getSeamPreferences();
			for (String propertyName: relevantProperties) {
				String propertyValue = ps.get(propertyName, "");
				String oldPathString = oldPath.toString();
				if(propertyValue.equals(oldPathString)) {
					ps.put(propertyName, newPath.toString());
				} else if(propertyValue.startsWith(oldPathString + "/")) {
					String newPathString = newPath.toString() + propertyValue.substring(oldPathString.length());
					ps.put(propertyName, newPathString);
				}
			}

			try {
				ps.flush();
			} catch (BackingStoreException e) {
				SeamCorePlugin.getPluginLog().logError(e);
			}
			return new SeamRenameFolderChange(project, oldPath.lastSegment(), newPath);
		} finally {
			pm.done();
		}
	}
}
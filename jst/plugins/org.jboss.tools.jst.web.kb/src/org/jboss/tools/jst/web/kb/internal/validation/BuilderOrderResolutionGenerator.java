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
package org.jboss.tools.jst.web.kb.internal.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.eclipse.wst.validation.internal.plugin.ValidationPlugin;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

public class BuilderOrderResolutionGenerator implements IMarkerResolutionGenerator2 {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		try {
			if(ValidatorManager.ORDER_PROBLEM_MARKER_TYPE.equals(marker.getType())) {
				return new IMarkerResolution[]{new BuilderOrderResolution()};
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return new IMarkerResolution[0];
	}

	public boolean hasResolutions(IMarker marker) {
		try {
			return ValidatorManager.ORDER_PROBLEM_MARKER_TYPE.equals(marker.getType());
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return false;
	}

}

class BuilderOrderResolution implements IMarkerResolution2 {

	public String getLabel() {
		return KbMessages.CHANGE_BUILDER_ORDER;
	}

	public void run(IMarker marker) {
		IProject project = marker.getResource().getProject();
		try {
			if(WebModelPlugin.makeBuilderLast(project, ValidationPlugin.VALIDATION_BUILDER_ID)) {
				project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		
	}

	public String getDescription() {
		return null;
	}

	public Image getImage() {
		return null;
	}
	
}

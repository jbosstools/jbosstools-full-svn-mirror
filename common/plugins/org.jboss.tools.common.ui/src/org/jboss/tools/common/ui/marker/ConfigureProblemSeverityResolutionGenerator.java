/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.common.ui.marker;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.ui.CommonUIPlugin;
import org.jboss.tools.common.validation.ValidationErrorManager;

/**
 * @author Daniel Azarov
 */
public class ConfigureProblemSeverityResolutionGenerator implements
		IMarkerResolutionGenerator2 {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		ArrayList<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();
		int position = marker.getAttribute(IMarker.CHAR_START, 0);
		try {
			if(marker.getResource() instanceof IFile){
				IFile file = (IFile)marker.getResource();
				if(file != null){
					String preferenceKey = getPreferenceKey(marker);
					String preferencePageId = getPreferencePageId(marker);
					if(preferenceKey != null && preferencePageId != null){
						resolutions.add(new ConfigureProblemSeverityMarkerResolution(preferencePageId, preferenceKey));
						boolean enabled = marker.getAttribute(ValidationErrorManager.SUPPRESS_WARNINGS_ENABLED_ATTRIBUTE, false);
						if(enabled){
							IJavaElement element = findJavaElement(file, position);
							if(element != null){
								resolutions.add(new AddSuppressWarningsMarkerResolution(file, element, preferenceKey));
							}
						}
					}
				}
			}
		} catch (CoreException e) {
			CommonUIPlugin.getDefault().logError(e);
		}
		return resolutions.toArray(new IMarkerResolution[] {});
	}
	
	private IJavaElement findJavaElement(IFile file, int position){
		ICompilationUnit compilationUnit = null;
		try {
			compilationUnit = EclipseUtil.getCompilationUnit(file);
			IJavaElement element = compilationUnit.getElementAt(position);
			if(element != null && element instanceof IMethod){
				IJavaElement parameter = findParameter((IMethod)element, position);
				if(parameter != null){
					return parameter;
				}
			}
			return element;
		} catch (CoreException e) {
			CommonUIPlugin.getDefault().logError(e);
		}
		return null;
	}
	
	private ILocalVariable findParameter(IMethod method, int position) throws JavaModelException{
		for(ILocalVariable parameter : method.getParameters()){
			if(parameter.getSourceRange().getOffset() <= position && parameter.getSourceRange().getOffset()+parameter.getSourceRange().getLength() > position)
				return parameter;
		}
		return null;
	}

	public boolean hasResolutions(IMarker marker) {
		try {
			return getPreferenceKey(marker) != null && getPreferencePageId(marker) != null;
		} catch (CoreException ex) {
			CommonUIPlugin.getDefault().logError(ex);
		}
		return false;
	}
	
	private String getPreferenceKey(IMarker marker)throws CoreException{
		String attribute = marker.getAttribute(ValidationErrorManager.PREFERENCE_KEY_ATTRIBUTE_NAME, null);
		return attribute; 
	}

	private String getPreferencePageId(IMarker marker)throws CoreException{
		String attribute = marker.getAttribute(ValidationErrorManager.PREFERENCE_PAGE_ID_NAME, null);
		return attribute; 
	}

}

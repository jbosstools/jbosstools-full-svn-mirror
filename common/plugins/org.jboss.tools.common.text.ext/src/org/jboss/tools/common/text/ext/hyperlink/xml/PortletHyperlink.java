/*******************************************************************************
 * Copyright (c) 2009-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.common.text.ext.hyperlink.xml;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.util.EclipseJavaUtil;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.text.ext.ExtensionsPlugin;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;

public class PortletHyperlink extends AbstractHyperlink {
	private static final String PROP_EXTENSION = ".properties"; //$NON-NLS-1$
	private String hyperlinkText = ""; //$NON-NLS-1$
	private String partitionType = null;

	@Override
	protected void doHyperlink(IRegion region) {
		if (region == null)
			return;

		try {
			IDocument document = getDocument();
			hyperlinkText = document
					.get(region.getOffset(), region.getLength());
		} catch (BadLocationException ex) {
			ExtensionsPlugin.getPluginLog().logError(ex);
		}

		if (partitionType == PortletHyperlinkPartitioner.PORTLET_CLASS_PARTITION)
			doPortletClassHyperlink(region);
		else if (partitionType == PortletHyperlinkPartitioner.PORTLET_RESOURCE_BUNDLE_PARTITION)
			doPortletResourceBundleHyperlink(region);

	}

	private void doPortletClassHyperlink(IRegion region) {
		IEditorPart part = null;
		IProject project = getProject();
		if(project != null){
			IJavaProject javaProject = EclipseResourceUtil.getJavaProject(project);
			if(javaProject != null){
				IType type = null;
				try{
					type = EclipseJavaUtil.findType(javaProject, hyperlinkText);
				}catch(JavaModelException ex){
					ExtensionsPlugin.getPluginLog().logError(ex);
				}
				if(type != null){
					IResource resource = type.getResource();
					if(resource != null && resource instanceof IFile){
						IFile file = (IFile)resource;
						
						if (file != null)
							part = openFileInEditor(file);
					}
				}
			}
		}
		if (part == null)
			openFileFailed();
	}
	
	private void doPortletResourceBundleHyperlink(IRegion region) {
		String fileName = new String(hyperlinkText);
		
		if(!fileName.endsWith(PROP_EXTENSION))
			fileName += PROP_EXTENSION;
		
		IFile file = getFileFromProject(fileName);
		
		IEditorPart part = null;
		if (file != null)
			part = openFileInEditor(file);
		
		if (part == null)
			openFileFailed();
	}
	
	private IProject getProject() {
		IFile documentFile = getFile();
		if (documentFile == null || !documentFile.isAccessible())
			return null;

		IProject project = documentFile.getProject();

		return project;
	}

	@Override
	public String getHyperlinkText() {
		return hyperlinkText;
	}
}

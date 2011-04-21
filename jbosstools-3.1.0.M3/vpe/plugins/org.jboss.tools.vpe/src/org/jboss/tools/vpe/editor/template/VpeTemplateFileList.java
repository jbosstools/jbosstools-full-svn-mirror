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
package org.jboss.tools.vpe.editor.template;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.jboss.tools.vpe.VpePlugin;

public class VpeTemplateFileList {

	private boolean changed;
	private VpeTemplateFile autoTemplateFile;
	private VpeTemplateFile[] templateFiles;

	VpeTemplateFileList() {
	}
	
	void load() {
		VpeTemplateFile newAutoTemplateFile = null;
		try {
			newAutoTemplateFile = new VpeTemplateFile(
					VpeTemplateManager.getAutoTemplates(), null);
		} catch (IOException e) {
			VpePlugin.getPluginLog().logError("Default template for unknown tags loading error ",e); //$NON-NLS-1$
		}
		changed = (autoTemplateFile == null) != (newAutoTemplateFile == null);
		if (!changed && autoTemplateFile != null) {
			changed = !autoTemplateFile.isEqual(newAutoTemplateFile);
		}
		VpeTemplateFile[] newTemplateFiles = createTemplateFileList();
		if (!changed && templateFiles != null) {
			changed = templateFiles.length != newTemplateFiles.length;
			if (!changed) {
				for (int i = 0; i < templateFiles.length; i++) {
					if (!templateFiles[i].isEqual(newTemplateFiles[i])) {
						changed = true;
						break;
					}
				}
			}
		}
		autoTemplateFile = newAutoTemplateFile;
		templateFiles = newTemplateFiles;
	}
	
	VpeTemplateFile getAutoTemplateFile() {
		return autoTemplateFile;
	}
	
	VpeTemplateFile[] getTemplateFiles() {
		return templateFiles;
	}
	
	boolean isChanged() {
		return changed;
	}
	
	private VpeTemplateFile[] createTemplateFileList() {
		List<VpeTemplateFile> templateList = createTemplateFileListImpl();
		return templateList.toArray(new VpeTemplateFile[templateList.size()]);
	}
		
	private List<VpeTemplateFile> createTemplateFileListImpl() {
		List<VpeTemplateFile> templateList = new ArrayList<VpeTemplateFile>();

			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(VpePlugin.EXTESION_POINT_VPE_TEMPLATES);
			IExtension[] extensions = extensionPoint.getExtensions();
			for (int i=0;i<extensions.length;i++) {
				IExtension extension = extensions[i];
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for(int j=0;j<elements.length;j++) {
					String pathAttrValue = elements[j].getAttribute("path"); //$NON-NLS-1$
					try {
					VpeTemplateFile templateFile = new VpeTemplateFile(pathAttrValue, elements[j]);
						templateList.add(templateFile);
					} catch (IOException e) {
						VpePlugin.getPluginLog().logError("Error during loading template '" + pathAttrValue + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		return templateList;
	}

	static IPath getFilePath(String name, IConfigurationElement confElement) throws IOException {
		VpePlugin plugin = VpePlugin.getDefault();
		Bundle bundle = confElement == null 
				? plugin.getBundle()
				: Platform.getBundle(confElement.getContributor().getName());
		URL url = bundle.getEntry("/"); //$NON-NLS-1$
		IPath path = new Path(FileLocator.toFileURL(url).getFile());
		path = path.append(name);
		return path;
	}
}

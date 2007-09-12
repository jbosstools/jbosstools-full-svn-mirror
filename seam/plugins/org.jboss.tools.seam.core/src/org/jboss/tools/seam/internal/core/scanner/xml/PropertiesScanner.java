/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.internal.core.scanner.xml;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FolderImpl;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.seam.internal.core.InnerModelHelper;
import org.jboss.tools.seam.internal.core.SeamPropertiesDeclaration;
import org.jboss.tools.seam.internal.core.SeamProperty;
import org.jboss.tools.seam.internal.core.SeamValueString;
import org.jboss.tools.seam.internal.core.scanner.IFileScanner;
import org.jboss.tools.seam.internal.core.scanner.LoadedDeclarations;
import org.jboss.tools.seam.internal.core.scanner.ScannerException;

public class PropertiesScanner implements IFileScanner {
	
	public PropertiesScanner() {}

	/**
	 * Returns true if file is probable component source - 
	 * has components.xml name or *.component.xml mask.
	 * @param resource
	 * @return
	 */	
	public boolean isRelevant(IFile resource) {
		if(resource.getName().equals("seam.properties")) return true;
		return false;
	}
	
	/**
	 * This method should be called only if isRelevant returns true;
	 * Makes simple check if this java file contains annotation Name. 
	 * @param resource
	 * @return
	 */
	public boolean isLikelyComponentSource(IFile f) {
		if(!f.isSynchronized(IFile.DEPTH_ZERO) || !f.exists()) return false;
		if(f.getName().equals("seam.properties")) return true;
		return false;
	}

	/**
	 * Returns list of components
	 * @param f
	 * @return
	 * @throws ScannerException
	 */
	public LoadedDeclarations parse(IFile f) throws ScannerException {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return null;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		return parse(o, f.getFullPath());
	}
	
	public LoadedDeclarations parse(XModelObject o, IPath source) {
		if(o == null) return null;

		if(o.getParent() instanceof FolderImpl) {
			IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(source);
			if(f != null && f.exists()) ((FolderImpl)o.getParent()).updateChildFile(o, f.getLocation().toFile());
		}
		
		LoadedDeclarations ds = new LoadedDeclarations();

		XModelObject[] properties = o.getChildren();
		Map<String, SeamPropertiesDeclaration> ds1 = new HashMap<String, SeamPropertiesDeclaration>();
		for (int i = 0; i < properties.length; i++) {
			String name = properties[i].getAttributeValue("name");
			int q = name.lastIndexOf('.');
			if(q < 0) continue;
			String componentName = name.substring(0, q);
			String propertyName = name.substring(q + 1);
			SeamPropertiesDeclaration d = ds1.get(componentName);
			if(d == null) {
				d = new SeamPropertiesDeclaration();
				d.setId(properties[i]);
				d.setSourcePath(source);
				d.setName(componentName);
				ds1.put(componentName, d);
			}
			SeamProperty p = new SeamProperty();
			p.setId(properties[i]);
			p.setName(new XMLValueInfo(properties[i], "name"));
			p.setName(propertyName);
			SeamValueString v = new SeamValueString();
			v.setValue(new XMLValueInfo(properties[i], "value"));
			p.setValue(v);
			d.addProperty(p);
		}
		ds.getComponents().addAll(ds1.values());
		return ds;
	}
	
}

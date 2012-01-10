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
package org.jboss.tools.common.model.ui.editor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarEntryDirectory;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.core.JarEntryResource;
import org.eclipse.jdt.internal.core.NonJavaResource;
import org.jboss.tools.common.core.resources.XModelObjectEditorInput;
import org.jboss.tools.common.core.resources.XModelObjectEditorInputFactory;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.ILocationProvider;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.*;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.Paths;
import org.jboss.tools.common.util.FileUtil;

public class ModelObjectStorageEditorInput extends ModelObjectEditorInput implements IStorageEditorInput {
	IJarEntryResource jarEntryFile = null;

	public ModelObjectStorageEditorInput(XModelObject object) {
		super(object);
		jarEntryFile = findJarEntryFile();
	}

	public IStorage getStorage() throws CoreException {
		return jarEntryFile != null ? jarEntryFile : storage;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter)	{
		if(adapter == IFile.class) return null;
		return super.getAdapter(adapter);
	}

	IJarEntryResource findJarEntryFile() {
		XModelObject o = object;
		JarEntryFile f = null;
		JarEntryResource current = null;
		String packageName = "";
		List<String> parts = new ArrayList<String>();
		List<XModelObject> os = new ArrayList<XModelObject>();
		while(o != null && o.getFileType() != XModelObject.SYSTEM) {
			String part = o.getFileType() == XModelObject.FILE ? FileAnyImpl.toFileName(o) :
				o.getFileType() == XModelObject.FOLDER ? o.getAttributeValue(XModelObjectConstants.ATTR_NAME) : null;
			if(part != null) {
				parts.add(0, part);
				os.add(0, o);
				if(f == null) {
					f = new JarEntryFile(part) {
						public InputStream getContents() throws CoreException {
							return storage.getContents();
						}
					};
					current = f;
				} else {
					if(packageName.length() > 0) {
						packageName = part + "." + packageName;
					} else {
						packageName = part;
					}
					JarEntryDirectory d = new JarEntryDirectory(part);
					current.setParent(d);
					current = d;
				}
				
			}
			o = o.getParent();
		}
//		if(!(o instanceof JarSystemImpl)) return null;
		String file = Paths.expand(o.get(XModelObjectConstants.ATTR_NAME_LOCATION), o.getModel().getProperties());

        IProject p = EclipseResourceUtil.getProject(o);     
        IJavaProject jp = EclipseResourceUtil.getJavaProject(p);        
        if(jp == null) return null;

        IPackageFragmentRoot root = null;

        try {
        	IPackageFragmentRoot[] rs = jp.getAllPackageFragmentRoots();
        	for (IPackageFragmentRoot r: rs) {
        		if(r.getResource() != null && r.getResource().exists()
        				&& r.getResource().getLocation().toString().equals(file)) {
        			root = r;
        		} else if(r.getPath() != null && r.getPath().toString().equals(file)) {
        			root = r;
        		}
        	}
        } catch (CoreException e) {
        	ModelUIPlugin.getDefault().logError(e);
        }
        
        if(root == null) {
        	root = jp.getPackageFragmentRoot(file);
        }
        
        if(root == null) {
        	return null;
        }

		if(current != null && !"META-INF".equalsIgnoreCase(current.getName()) && packageName.length() > 0) {
			IPackageFragment pf = root.getPackageFragment(packageName);
			f.setParent(pf);
		} else {		
			current.setParent(root);
			if(!(o instanceof JarSystemImpl)) {
				Object q = root;
				NonJavaResource nj = null;
				for (int i = 0; i < parts.size(); i++) {
					IResource ri = (IResource)os.get(i).getAdapter(IResource.class);
					if(ri == null) {
						return f;
					}
					nj = new NonJavaResource(q, ri);
					q = nj;
				}
				if(nj != null) {
					return nj;
				}
			}
		}
		
		return f;
	}
	
	IStorage storage = new Storage();
	
	class Storage implements IStorage {

		public InputStream getContents() throws CoreException {
			ByteArrayInputStream b = null;
			if(object instanceof FileAnyImpl) {
				FileAnyImpl f = (FileAnyImpl)object;
				String s = f.getAsText();
				String encoding = FileUtil.getEncoding(s);
				byte[] bs = null;
				if(encoding == null) { 
					bs = s.getBytes();
				} else {
					try {
						bs = s.getBytes(encoding);
					} catch (UnsupportedEncodingException e) {
						bs = s.getBytes();
					}
				}
				b = new ByteArrayInputStream(bs);
			} else {
				b = new ByteArrayInputStream(new byte[0]);
			}			
			return b;
		}

		public IPath getFullPath() {
			IProject p = EclipseResourceUtil.getProject(object);
			if(p == null) {
				String location = XModelObjectEditorInputFactory.getFileLocation(object);
				if(location != null) {
					return new Path(location); 
				}
			} else {
				XModelObject f = object;
				while(f != null && f.getFileType() != XModelObject.SYSTEM) f = f.getParent();
				if(f != null) {
					IResource jar = EclipseResourceUtil.getResource(f);
					if(jar != null) return jar.getFullPath();
				}
			}
			String n = p == null ? "" : p.getName(); //$NON-NLS-1$
			return new Path(n + "/" + object.getPath()); //$NON-NLS-1$
		}

		public String getName() {
			return ModelObjectStorageEditorInput.this.getName();
		}

		public boolean isReadOnly() {
			return !isEditable();
		}

		public Object getAdapter(Class adapter) {
			if(adapter == ILocationProvider.class) {
				if(ModelObjectStorageEditorInput.this instanceof ILocationProvider) {
					return (ILocationProvider)ModelObjectStorageEditorInput.this;
				}
			}
			return ModelObjectStorageEditorInput.this.getAdapter(adapter);
		}
	}
	
	protected boolean isEditable() {
		return false;
	}

	public boolean equals(Object o)	{
		if(super.equals(o)) return true;
		if(o instanceof IStorageEditorInput) {
			try {
				IStorage st = ((IStorageEditorInput)o).getStorage();
				if(jarEntryFile != null && jarEntryFile.equals(st)) return true;
			} catch (CoreException e) {
				//ignore
			}
			String[] entryInfo = XModelObjectEditorInput.parseJarEntryFileInput((IStorageEditorInput)o);
			if(entryInfo == null) return false;
			XModelObject mo = XModelObjectEditorInput.getJarEntryObject(null, entryInfo[0], entryInfo[1]);
			return mo != null && mo.equals(object);
		}
		return false;
	}
}

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
package org.jboss.tools.common.model.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.osgi.framework.Bundle;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.FilePathHelper;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.XFileObject;
import org.jboss.tools.common.model.filesystems.impl.*;
import org.jboss.tools.common.model.icons.impl.*;
import org.jboss.tools.common.model.impl.XModelObjectImpl;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.project.ModelNature;

public class EclipseResourceUtil {
	
	public static IProject getProject(XModelObject o) {
		return (o == null) ? null : (IProject)o.getModel().getProperties().get(XModelObjectConstants.PROJECT);
	}
	
	public static boolean isProjectFragment(XModel model) {
		return (XModelObjectConstants.TRUE.equals(model.getProperties().getProperty("isProjectFragment"))); //$NON-NLS-1$
	}

	public static IResource getResource(XModelObject object) {
		XModelObject f = ((XModelObjectImpl)object).getResourceAncestor();
		return (f == null) ? null : (IResource)f.getAdapter(IResource.class);
	}
	
	public static String getJavaClassQualifiedName(XModelObject object) {
		if(object.getFileType() != XFileObject.FILE) return null;
		String ext = object.getAttributeValue(XModelObjectConstants.ATTR_NAME_EXTENSION);
		if(!"java".equals(ext) && !"class".equals(ext)) return null; //$NON-NLS-1$ //$NON-NLS-2$
		String q = object.getAttributeValue(XModelObjectConstants.ATTR_NAME);
		String p = getJavaPackageName(object.getParent());
		return (p == null) ? q : p + "." + q;  //$NON-NLS-1$
	}
	
	public static String getJavaPackageName(XModelObject object) {
		if(object == null || object.getFileType() != XFileObject.FOLDER) return null;
		String q = object.getAttributeValue(XModelObjectConstants.ATTR_NAME);
		XModelObject o = object.getParent();
		while(o != null && o.getFileType() == XFileObject.FOLDER) {
			q = o.getAttributeValue(XModelObjectConstants.ATTR_NAME) + "." + q; //$NON-NLS-1$
			o = o.getParent();
		}
		return q;		
	}
	
	public static String getIconPath(XModelObject o) {
		String s = o.getMainIconName();
		return o.getModelEntity().getMetaModel().getIconList().getIconPath(s, "default.unknown"); //$NON-NLS-1$
	}

	public static Image getImage(String path) {
		return ModelImages.getImage(path); 
	}
	
	public static Image getImage(XModelObject object) {
		return new XModelObjectIcon(object).getEclipseImage();
	}

	public static XModelObject getObjectByResource(IResource resource) {
		if(resource == null) return null;
		IProject p = resource.getProject();
		IModelNature sp = getModelNature(p);
		if(sp == null) return null;
		return getObjectByResource(sp.getModel(), resource);
	}

	public static XModelObject getObjectByResource(XModel model, IResource resource) {
		if(resource == null) return null;
		IPath path = resource.getLocation();
		if (model != null) {
			FileSystemsImpl fso = (FileSystemsImpl)FileSystemsHelper.getFileSystems(model);
			if(fso == null) return null;
			fso.updateOverlapped();
			XModelObject[] fs = fso.getChildren(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER);
			for (int i = 0; i < fs.length; i++) {
				FileSystemImpl s = (FileSystemImpl)fs[i];
				XModelObject o = findResourceInFileSystem(s, resource);
				if(o != null) return o;
			}
			fs = fso.getChildren("FileSystemJar"); //$NON-NLS-1$
			String location = path == null ? null : path.toString().replace('\\', '/');
			if(location != null && isJar(location)) { 
				for (int i = 0; i < fs.length; i++) {
					JarSystemImpl jar = (JarSystemImpl)fs[i];
					String jl = jar.getLocation();
					if(jl.equals(location)) return jar;
				}
			}
		}
		return null;		
	}	

	public static XModelObject getObjectByPath(IProject p, String path) {
		if(p == null) return null;
		IModelNature sp = getModelNature(p);
		return (sp == null) ? null : sp.getModel().getByPath(path);
	}
	
	public static XModelObject findFileSystem(IResource resource, XModel model) {
		XModelObject fss = FileSystemsHelper.getFileSystems(model);
		if(fss == null) return null;
		XModelObject[] fs = fss.getChildren();
		XModelObject result = null;
		IPath resourcePath = resource == null ? null : resource.getFullPath();
		IPath path = null;
		for (int i = 0; i < fs.length; i++) {
			if(!(fs[i] instanceof FileSystemImpl)) continue;
			IResource r = (IResource)fs[i].getAdapter(IResource.class);
			if(r == null) continue;
			IPath p = r.getFullPath();
			if(r != null && p.isPrefixOf(resourcePath)) {
				if(path == null || path.isPrefixOf(p)) {
					result = fs[i];
					path = p;
				}
			} 
		}
		return result;
	}

	public static XModelObject addFileSystem(IResource resource, XModel model) {
		XModelObject fss = FileSystemsHelper.getFileSystems(model);
		if(fss == null) return null;
		if(resource == null) return null;
		Properties properties = new Properties();
		String fsLoc = resource.getLocation().toString();
		if(resource == resource.getProject()) {
			fsLoc = "%" + IModelNature.ECLIPSE_PROJECT + "%"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			fsLoc = getRelativeLocation(model, fsLoc);
		}
		XModelObject[] cs = fss.getChildren(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER);
		for (int i = 0; i < cs.length; i++) {
			String loc = cs[i].getAttributeValue(XModelObjectConstants.ATTR_NAME_LOCATION);
			if(fsLoc.equals(loc)) return null;
		}	
		properties.setProperty(XModelObjectConstants.ATTR_NAME_LOCATION, fsLoc);
		String name = resource.getName();
		name = XModelObjectUtil.createNewChildName(name, fss);
		properties.setProperty(XModelObjectConstants.ATTR_NAME, name);
		FileSystemImpl s = (FileSystemImpl)model.createModelObject(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER, properties);
		boolean b = fss.addChild(s);
		if(b) {
			fss.setModified(true);
			return s;
		} else {
			return null;
		}
	}

	private static String[] MODEL_NATURES = new String[0];
	
	static {
		List<String> natures = new ArrayList<String>();
		if(Platform.getBundle("org.jboss.tools.struts") != null) { //$NON-NLS-1$
			natures.add("org.jboss.tools.struts.strutsnature");	 //$NON-NLS-1$
		}
		if(Platform.getBundle("org.jboss.tools.jsf") != null) { //$NON-NLS-1$
			natures.add("org.jboss.tools.jsf.jsfnature");	 //$NON-NLS-1$
		}
		MODEL_NATURES = natures.toArray(new String[0]);
	}
	
	public static String[] getModelNatureNames() {
		return MODEL_NATURES;
	}
	
	public static boolean hasNature(XModel model, String nature) {
		if(model == null) return false;
		IProject p = (IProject)model.getProperties().get(XModelObjectConstants.PROJECT);
		if(p == null || !p.isOpen()) return false;
		try {
			if(p.hasNature(nature)) return true;
		} catch (CoreException e) {
			ModelPlugin.getPluginLog().logError(e);
		}
		return false;
	}
	static int count = 0;
	public static IModelNature getModelNature(IProject p) {
		if(p == null || !p.isOpen()) return null;
		String[] natures = getModelNatureNames();
		for (int i = 0; i < natures.length; i++) {
			try {
				if(p.hasNature(natures[i])) {
					long t1 = System.currentTimeMillis();
					count++;
					if(!ModelNature.checkModelNature(p, natures[i])) {
						continue;
					}
					long dt = System.currentTimeMillis() - t1;
					count += (int)dt;
//					System.out.println("--->" + count);
					IModelNature n = (IModelNature)p.getNature(natures[i]);
					if(n == null) return null;
					n = testNature(n);
					if(n == null) {
						ModelPlugin.getPluginLog().logWarning("Project " + p + " has corrupted nature: " + natures[i]); //$NON-NLS-1$ //$NON-NLS-2$
						removeNatureFromProject(p, natures[i]);
					}
				    return n;
				}
			} catch (CoreException e) {
				ModelPlugin.getPluginLog().logError(e);
			}
		}
		return null;
	}
	
	private static IModelNature testNature(IModelNature n) {
		if(n == null || n.getModel() == null) return null;
		XModel model = n.getModel();
		XModelObject object = model.getRoot();
		if(object == null) return null;
		if(!XModelObjectConstants.ROOT_OBJECT.equals(object.getModelEntity().getName())) return null;
		return n;
	}

	public static IModelNature getModelNature(IProject p, String id) {
		if(p == null || !p.isOpen()) return null;
		try {
			if(p.hasNature(id)) {
				if(!ModelNature.checkModelNature(p, id)) {
					return null;
				}
				IModelNature n = (IModelNature)p.getNature(id);
				if(n == null) return null;
				n = testNature(n);
				if(n == null) {
					ModelPlugin.getPluginLog().logWarning("Project " + p + " has corrupted nature: " + id); //$NON-NLS-1$ //$NON-NLS-2$
					removeNatureFromProject(p, id);
				}
			    return n;
			}
		} catch (CoreException e) {
			ModelPlugin.getPluginLog().logError(e);
		}
		return null;
	}
	
	static Map<IProject,XModel> models = new HashMap<IProject,XModel>();
	
	/**
	 * If project has no struts nature, the method creates new instance of model 
	 * populates it with a filesystems corresponding to the project root
	 * and links, and returns model object for the resource. 
	 * The model created is not complete project, so it has property 
	 * 'isProjectFragment' set to 'true'.
	 */
	 
	public static XModelObject createObjectForResource(IResource resource) {
		if(resource == null || !resource.exists()) return null;
		IProject project = resource.getProject();
		if(project == null || !project.isOpen()) return null;

		IModelNature sp = getModelNature(project);
		if(sp != null) {
			XModelObject result = getObjectByResource(resource);
			if(result == null) {
				XModelObject fs = findFileSystem(resource.getProject(), sp.getModel());
				if(fs == null) {
					fs = addFileSystem(resource.getProject(), sp.getModel());
					if(fs != null) result = getObjectByResource(resource);
				}
				if(result == null && resource != project) {
					IResource r = resource.getParent();
					if(r != null && r != project) {
						fs = addFileSystem(r, sp.getModel());
						if(fs != null) result = getObjectByResource(resource);
					}
				}
			}
			return result;
		}

		XModel model = models.get(project);
		if(model != null) {
			validateJarSystem(FileSystemsHelper.getFileSystems(model), resource);
			XModelObject result = getObjectByResource(model, resource);
			if(result == null && resource instanceof IFile) {
				IResource r = resource.getParent();
				if(r != null && project != r) {
					XModelObject fs = addFileSystem(r, model);
					if(fs != null) return getObjectByResource(model, resource);
				}
			}
			return result;
		}
		
		Properties properties = new Properties();
		properties.putAll(System.getProperties());
		IResource r = resource;
		if(!(r instanceof IProject)) {
			while(r != null && !(r.getParent() instanceof IProject)) r = r.getParent();
		}
		if(r == null) return null;
		properties.setProperty(XModelConstants.WORKSPACE, r.getParent().getLocation().toString());
		properties.setProperty(IModelNature.ECLIPSE_PROJECT, project.getLocation().toString());
		properties.put(XModelObjectConstants.PROJECT, project);
		properties.put("isProjectFragment", XModelObjectConstants.TRUE); //$NON-NLS-1$
		model = XModelFactory.getModel(properties);
		models.put(project, model);
		
		XModelObject fs = FileSystemsHelper.getFileSystems(model);
		if(fs == null) {
			ModelPlugin.getPluginLog().logInfo("Cannot create file systems for project " + project); //$NON-NLS-1$
			return null;
		}
		String fsLoc = null;
		FileSystemImpl s = null;
		properties = new Properties();

		fsLoc = project.getLocation().toString();
		properties.setProperty(XModelObjectConstants.ATTR_NAME_LOCATION, fsLoc);
		properties.setProperty(XModelObjectConstants.ATTR_NAME, project.getName());
		s = (FileSystemImpl)model.createModelObject(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER, properties);
		fs.addChild(s);
		if(!isJar(resource)) {
			IResource webRoot = getFirstWebContentResource(project);
			if(webRoot != null && webRoot.exists() && webRoot != project) {
				fsLoc = webRoot.getLocation().toString();
				properties.setProperty(XModelObjectConstants.ATTR_NAME_LOCATION, fsLoc);
				properties.setProperty(XModelObjectConstants.ATTR_NAME, "WEB-ROOT"); //$NON-NLS-1$
				s = (FileSystemImpl)model.createModelObject(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER, properties);
				fs.addChild(s);
			}
		}

		if(!isJar(resource) || getObjectByResource(model, resource) == null) {
			properties = new Properties();
			fsLoc = (r instanceof IFile) ? r.getParent().getLocation().toString() : r.getLocation().toString();
			properties.setProperty(XModelObjectConstants.ATTR_NAME_LOCATION, fsLoc);
			properties.setProperty(XModelObjectConstants.ATTR_NAME, r.getName());
			s = (FileSystemImpl)model.createModelObject(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER, properties);
			fs.addChild(s);
		}

		IResource[] cs = null;
		try {
			cs = project.members();
		} catch (CoreException e) {
			ModelPlugin.getPluginLog().logError(e);
		}
		if(cs != null) for (int i = 0; i < cs.length; i++) {
			if(!cs[i].isLinked()) continue;
			properties = new Properties();
			fsLoc = cs[i].getLocation().toString();
			properties.setProperty(XModelObjectConstants.ATTR_NAME_LOCATION, fsLoc);
			properties.setProperty(XModelObjectConstants.ATTR_NAME, cs[i].getName());
			s = (FileSystemImpl)model.createModelObject(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER, properties);
			fs.addChild(s);
		}
		validateJarSystem(fs, resource);
		return getObjectByResource(model, resource);
	}
	
	public static IResource getFirstWebContentResource(IProject project) {
		IVirtualComponent vc = ComponentCore.createComponent(project);
		if (vc == null || vc.getRootFolder() == null)
			return null;
		if (ModuleCoreNature.isFlexibleProject(project)) {
			return vc.getRootFolder().getUnderlyingResource();
		}

		return null;
	}

	private static boolean isJar(IResource resource) {
		return (resource instanceof IFile && isJar(resource.getName()));		
	}
	
	private static void validateJarSystem(XModelObject fs, IResource resource) {
		if(fs == null || !isJar(resource)) return;
		String jsname = "lib-" + resource.getName().toLowerCase(); //$NON-NLS-1$
		String location = resource.getLocation().toString().replace('\\', '/');
		if(fs.getChildByPath(jsname) == null) {
			XModelObject q = fs.getModel().createModelObject("FileSystemJar", null); //$NON-NLS-1$
			q.setAttributeValue(XModelObjectConstants.ATTR_NAME, jsname);
			q.setAttributeValue(XModelObjectConstants.ATTR_NAME_LOCATION, location);
			fs.addChild(q);
		}
	}
	
	public static XModelObject findResourceInFileSystem(FileSystemImpl s, IResource resource) {
		if(resource == null || resource.getLocation() == null) {
			return null;
		}
		IResource sr = s.getResource();
		if(sr == null) return null;
		if(!sr.getLocation().isPrefixOf(resource.getLocation())) return null;
		String path = resource.getLocation().toString();
		String rootpath = sr.getLocation().toString();
		String relpath = path.substring(rootpath.length()).replace('\\', '/');
		if(relpath.length() == 0) return s;

		XModelObject o = s.getChildByPath(relpath.substring(1));
		if(o == null && resource.exists()) {
			s.update();
			o = s.getChildByPath(relpath.substring(1));
		}
		if(o == null) return null;
		XModelObject p = o;
		while(p != null && !XModelObjectConstants.TRUE.equals(p.get("overlapped"))) p = p.getParent(); //$NON-NLS-1$
		if(p == null) {
			IResource r = (IResource)o.getAdapter(IResource.class);
			if(r == null || !resource.getLocation().equals(r.getLocation())) {
				//failure, more detailed file system is needed.
				return null;
			}
		}
		return (p == null) ? o : null;
	}
	
	public static boolean isOverlapped(XModelObject object) {
		XModelObject p = object;
		while(p != null && !XModelObjectConstants.TRUE.equals(p.get("overlapped"))) p = p.getParent(); //$NON-NLS-1$
		return (p != null);
	}

	/**
	 * 
	 * For an external location.
	 */
	public static XModelObject createObjectForLocation(String location) {
		if(location == null) return null;
		File f = new File(location);
		if(!f.isFile()) return null;
		Properties properties = new Properties();
		properties.putAll(System.getProperties());

		properties.setProperty(XModelConstants.WORKSPACE, f.getParent());
		properties.put("isProjectFragment", XModelObjectConstants.TRUE); //$NON-NLS-1$
		XModel model = XModelFactory.getModel(properties);
		XModelObject fs = FileSystemsHelper.getFileSystems(model);
		if(fs == null) {
			ModelPlugin.getPluginLog().logInfo("Cannot create file systems for model at " + location); //$NON-NLS-1$
			return null;
		}
		properties = new Properties();
		String fsLoc = f.getParent();
		properties.setProperty(XModelObjectConstants.ATTR_NAME_LOCATION, fsLoc);
		properties.setProperty(XModelObjectConstants.ATTR_NAME, f.getParentFile().getName());
		FileSystemImpl s = (FileSystemImpl)model.createModelObject(XModelObjectConstants.ENT_FILE_SYSTEM_FOLDER, properties);
		fs.addChild(s);
		String pp = FilePathHelper.toPathPath(f.getName());
		return model.getByPath(XModelObjectConstants.SEPARATOR + pp);
	}

	public static String[] getJavaProjectSrcLocations(IProject project) {
		IResource[] rs = getJavaSourceRoots(project);
		if(rs == null || rs.length == 0) return new String[0];
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < rs.length; i++) {
			IPath p = rs[i].getLocation();
			if(p != null) result.add(p.toString());
		}
		return result.toArray(new String[0]);
	}
	
	public static IJavaProject getJavaProject(IProject project) {
		try {
			if(project == null || !project.isOpen()) return null;
			if(!project.hasNature(JavaCore.NATURE_ID)) return null;
			return JavaCore.create(project);
		} catch (CoreException e) {
			ModelPlugin.getPluginLog().logError(e);
			return null;		
		}
	}

	public static String getJavaProjectOutputLocation(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		try {
			IPath p = javaProject.getOutputLocation();
			IResource r = project.getWorkspace().getRoot().findMember(p);
			return (r == null || r.getLocation() == null) ? null : r.getLocation().toString();
		} catch (CoreException e) {
			ModelPlugin.getPluginLog().logError(e);
			return null;
		}
	}
	
	/**
	 * Returns list of canonical paths to resources included in class path.
	 * @throws IOException 
	 */	
	public static List<String> getClassPath(IProject project) throws CoreException, IOException {
		if(project == null || !project.isAccessible() || !project.hasNature(JavaCore.NATURE_ID)) return null;
		ArrayList<String> l = new ArrayList<String>();
		IJavaProject javaProject = JavaCore.create(project);		

		IPath p = javaProject.getOutputLocation();
		IResource r = p == null ? null : project.getWorkspace().getRoot().findMember(p);
		if(r != null && r.getLocation() != null && r.exists()) {
			String s = r.getLocation().toString();
			l.add(new java.io.File(s).getCanonicalPath());
		}

		IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
		for (int i = 0; i < es.length; i++) {
			try {
				if(es[i].getEntryKind() == IClasspathEntry.CPE_SOURCE && es[i].getOutputLocation() != null) {
					IResource findMember = project.getWorkspace().getRoot().findMember(es[i].getOutputLocation());
					if(findMember!=null) {
						String s = findMember.getLocation().toString();
						l.add(new java.io.File(s).getCanonicalPath());
					}
				}
			} catch (IOException e) {
				//ignore - we do not care about non-existent files here.
			}
		}
		for (int i = 0; i < es.length; i++) {
			try {
				String s = null;
				String path = es[i].getPath().toString();

				//First let's check if path is defined within Eclipse work space.
				if(path.startsWith(XModelObjectConstants.SEPARATOR) && path.indexOf(XModelObjectConstants.SEPARATOR, 1) > 1) {
					IResource findMember = ResourcesPlugin.getWorkspace().getRoot().findMember(es[i].getPath());
					if(findMember != null) {
						s = findMember.getLocation().toString();
					} else {
						s = null;
					}
				}
				
				//If search in Eclipse work space has failed, this is a useless attempt, but
				//let keep it just in case (this is good old code that worked for a long while).
				if(s == null && path.startsWith(XModelObjectConstants.SEPARATOR + project.getName() + XModelObjectConstants.SEPARATOR)) {
					IResource findMember = project.findMember(es[i].getPath().removeFirstSegments(1));
					if(findMember != null) {
						s = findMember.getLocation().toString();
					} else {
						s = null;
					}
				}
				
				//If we failed to find resource in Eclipse work space, 
				//lets try the path as absolute on disk
				if(s == null && new java.io.File(path).isFile()) {
					s = path;
				}
				if(s != null) {
					l.add(new java.io.File(s).getCanonicalPath());
				}
			} catch (IOException e) {
				//ignore - we do not care about malformed URLs in classpath here.
			}
		}
		return l;
	}
	
	public static List<String> getJREClassPath(IProject project) throws CoreException {
		if(project == null || !project.isAccessible() || !project.hasNature(JavaCore.NATURE_ID)) return null;
		ArrayList<String> l = new ArrayList<String>();
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] es0 = javaProject.getRawClasspath();
		IClasspathEntry[] es = null;
		for (int i = 0; i < es0.length && es == null; i++) {
			if(es0[i].getEntryKind() == IClasspathEntry.CPE_CONTAINER && 
					es0[i].getPath().toString().equals("org.eclipse.jdt.launching.JRE_CONTAINER")) { //$NON-NLS-1$
				IClasspathContainer container = JavaCore.getClasspathContainer(es0[i].getPath(), javaProject);
				if(container == null) continue;
				es = container.getClasspathEntries();
			}
		}
		if(es == null) return l;
		for (int i = 0; i < es.length; i++) {
			try {
				String s = null;
				String path = es[i].getPath().toString();
				if(path.startsWith(XModelObjectConstants.SEPARATOR + project.getName() + XModelObjectConstants.SEPARATOR)) {
					s = project.findMember(es[i].getPath().removeFirstSegments(1)).getLocation().toString();
				} else if(new java.io.File(path).isFile()) {
					s = path;
				}
				if(s != null) {
					l.add(new java.io.File(s).getCanonicalPath());
				}
			} catch (IOException e) {
				//ignore - we do not care about malformed URLs in class path here.
			}
		}
		return l;
	}
	
	public static IJavaElement findJavaElement(XModelObject object) {
		int type = object.getFileType(); 
		IResource resource = getResource(object);
		if(resource == null) return null;
		IProject project = resource.getProject();
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		try	{
			if(type == XFileObject.SYSTEM) return javaProject.findPackageFragmentRoot(resource.getFullPath());
			Path path = null;
			if(type == XFileObject.FOLDER) {
				String p = getJavaPackageName(object);
				if(p == null) return null;
				path = new Path(p.replace('.', '/'));
			} else {
				String p = getJavaClassQualifiedName(object);
				if(p == null) return null;
				path = new Path(p.replace('.', '/') + ".java"); //$NON-NLS-1$
			}
			return javaProject.findElement(path);
		} catch (CoreException ex) {
			ModelPlugin.getPluginLog().logError(ex);
		}
		return null;
	}
	
	public static IType getValidType(IProject project, String className) {
		if (className == null || className.length() == 0) return null;
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		IFile f = null;
		try {
			IType t = javaProject.findType(className);
			if(t == null || t.isBinary()) return t;
			if(t.getParent() instanceof ICompilationUnit) {
				ICompilationUnit u = (ICompilationUnit)t.getParent();
				f = (IFile)u.getCorrespondingResource();
				IMarker[] ms = f.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_ZERO);
				for (int i = 0; i < ms.length; i++) {
					if(ms[i].getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR) return null;
				}
			}
			return t;
		} catch (JavaModelException t) {
			ModelPlugin.getPluginLog().logError("Error while obtaining type " + className, t); //$NON-NLS-1$
		} catch (CoreException t) {
			ModelPlugin.getPluginLog().logError("Error occured while obtaining Java Problem markers  for " + f.getLocation() , t); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * For *.java files returns qualified class name if 
	 * resource is in located in project source folder
	 * @param resource
	 * @return
	 */
	public static String getJavaClassName(IResource resource) {
		if(resource == null || !(resource instanceof IFile)) return null;
		IResource root = getJavaSourceRoot(resource.getProject());
		if(root == null) return null;
		if(!root.getLocation().isPrefixOf(resource.getLocation())) return null;
		String relative = resource.getLocation().toString().substring(root.getLocation().toString().length());
		if(!relative.endsWith(".java")) return null; //$NON-NLS-1$
		relative = relative.substring(0, relative.length() - 5);
		relative = relative.replace('\\', '/');
		if(relative.startsWith(XModelObjectConstants.SEPARATOR)) relative = relative.substring(1);
		return relative.replace('/', '.');
	}
	
	/**
	 * Returns true only if project has no sources but output contains *.class file 
	 */	
	public static boolean isContainedInOutput(IProject project, String className) {
		if (className == null || className.length() == 0) return false;
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return false;
		try {
			IPath p = javaProject.getOutputLocation();
			IResource r = project.getWorkspace().getRoot().findMember(p);
			if(r == null || r.getLocation() == null) return false;
			String output = r.getLocation().toString();
			String f = output + XModelObjectConstants.SEPARATOR + className.replace('.', '/') + ".class"; //$NON-NLS-1$
			return new java.io.File(f).isFile();
		} catch (JavaModelException t) {
			ModelPlugin.getPluginLog().logError("Error checking class " + className, t); //$NON-NLS-1$
			return false;
		}		
	}

	public static boolean hasSources(IJavaProject javaProject) throws JavaModelException {
		IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
		for (int i = 0; i < es.length; i++) {
			if(es[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) return true;
		}
		return false;
	}
	
	public static IResource[] getJavaSourceRoots(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		List<IResource> resources = new ArrayList<IResource>();
		try {
			IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
			for (int i = 0; i < es.length; i++) {
				if(es[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IResource findMember = ModelPlugin.getWorkspace().getRoot().findMember(es[i].getPath());
					if(findMember != null && findMember.exists()) {
						resources.add(findMember);
					}
				} 
			}
		} catch(CoreException ce) {
			ModelPlugin.getPluginLog().logError("Error while locating java source roots for " + project, ce); //$NON-NLS-1$
		}
		return resources.toArray(new IResource[resources.size()]);
	}
	                        
	public static IResource getJavaSourceRoot(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		try	{
			IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
			for (int i = 0; i < es.length; i++) {
				if(es[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IResource findMember = ModelPlugin.getWorkspace().getRoot().findMember(es[i].getPath());
					if(findMember != null && findMember.exists()) {
						return findMember;
					}
				} 
			}
		} catch (JavaModelException ex) {
			ModelPlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	public static void addNatureToProject(IProject project, String natureId) throws CoreException {
		IProject proj = project.getProject();
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();
		if (findIndex(prevNatures, natureId) != -1) return; 		
		description.setNatureIds(append(prevNatures, natureId));
		proj.setDescription(description, null);
	}
	
	private static int findIndex(String[] os, String s) {
		for (int i = 0; i < os.length; i++) 
			if(os[i].equals(s)) return i;
		return -1;
	}
	private static String[] append(String[] os, String s) {
		String[] ns = new String[os.length + 1];
		System.arraycopy(os, 0, ns, 0, os.length);
		ns[os.length] = s;
		return ns;
	}
	private static String[] remove(String[] os, int index) {
		String[] ns = new String[os.length - 1];
		System.arraycopy(os, 0, ns, 0, index);
		System.arraycopy(os, index + 1, ns, index, os.length - (index + 1));
		return ns;
	}

	public static void removeNatureFromProject(IProject project, String natureId) throws CoreException {
		IProject proj = project.getProject();
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();
		int natureIndex = findIndex(prevNatures, natureId);
		if(natureIndex == -1) return; 				
		description.setNatureIds(remove(prevNatures, natureIndex));
		proj.setDescription(description, null);
	}
	
	public static void openResource(IResource resource) {
		XModelObject o = getObjectByResource(resource);
		if(o == null) o = createObjectForResource(resource);
		if(o != null) XActionInvoker.invoke("Open", o, null); //$NON-NLS-1$
	}
	
	public static String getInstallPath(Plugin plugin) {
		URL url = getInstallURL(plugin);
		return (url == null) ? null : url.getPath();
	}

	public static URL getInstallURL(Plugin plugin) {
		return plugin == null ? null : getInstallURL(plugin.getBundle());
	}

	public static String getInstallPath(Bundle bundle) {
		URL url = getInstallURL(bundle);
		return (url == null) ? null : url.getPath();
	}

	public static URL getInstallURL(Bundle bundle) {
		try {
			return bundle == null ? null : FileLocator.resolve(bundle.getEntry(XModelObjectConstants.SEPARATOR));
		} catch (IOException e) {
			//ignore and try to execute it in the other way
			return bundle.getEntry(XModelObjectConstants.SEPARATOR);
		}
	}

	/**
	 * Tries to find Eclipse resource by absolute path on disk.
	 * Uses handle methods, that is, file on disk does not need to exist. 
	 * @location Absolute path on disk
	 */
	public static IFile getFile(String location) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(location).makeAbsolute());
	}

	public static IResource getFolder(String location) {
		location = new java.io.File(location).getAbsolutePath();
		IProject[] projects = ModelPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; projects != null && i < projects.length; i++) {
			if (!projects[i].isOpen()) continue;
			String l = projects[i].getLocation().toFile().getAbsolutePath();
			if (!location.startsWith(l)) {
				try {
					IResource[] ms = projects[i].members();
					for (int j = 0; j < ms.length; j++) {
						if(!(ms[j] instanceof IContainer)) continue;
						IContainer c = (IContainer)ms[j];
						l = c.getLocation().toFile().getAbsolutePath();
						if (!location.startsWith(l)) continue;
						String relative = location.substring(l.length()).replace('\\', '/');
						return (relative.length() == 0) ? (IResource)c : c.getFolder(new Path(relative));
					}
				} catch (CoreException e) {
					ModelPlugin.getPluginLog().logError(e);
				}
				continue;
			}
			String relative = location.substring(l.length()).replace('\\', '/');
			return (relative.length() == 0) ? (IResource)projects[i] : projects[i].getFolder(relative);
		}
		return null;
	}

	public static IPath getRelativePath(IProject project, String path) {		
		IPath result = null;		
		IPath projectPath = project.getLocation();
		IPath absolutePath = new Path(path);
		if (projectPath.isPrefixOf(absolutePath))
			result = new Path(project.getFullPath() + absolutePath.toString().substring(projectPath.toString().length()));
		else {
			try {
				IResource children[] = project.members(true);
				for (int i = 0; i < children.length && result == null; i++) {
					if (absolutePath.equals(children[i].getLocation()))
						result = children[i].getFullPath();
				}
				for (int i = 0; i < children.length && result == null; i++) {
					IPath memberPath = children[i].getLocation();
					if(memberPath == null) continue;
					if(memberPath.isPrefixOf(absolutePath)) {
						result = children[i].getFullPath().append(absolutePath.removeFirstSegments(memberPath.segmentCount()));
					}
				}
			} catch (CoreException ex) {			
				ModelPlugin.getPluginLog().logError(ex);
			}
		}
		if (result == null) result = absolutePath;
		return result;
	}

	public static IClasspathEntry[] getDefaultJRELibrary() {
		return PreferenceConstants.getDefaultJRELibrary();
	}			
	
	public static IResource[] getClasspathResources(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return new IProject[0];

		ArrayList<IResource> l = new ArrayList<IResource>();
		
		IClasspathEntry[] es = null;
		
		try {
		    es = javaProject.getResolvedClasspath(true);
		} catch (JavaModelException e) {
			//ignore - if project cannot respond, it may not have resources of interest.
			return new IProject[0];
		}

		if(es != null) for (int i = 0; i < es.length; i++) {
			if(es[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				IPath path = es[i].getPath();
				IProject p = (IProject)project.getWorkspace().getRoot().findMember(path);
				l.add(p);
			} else if(es[i].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				IPath path = es[i].getPath();
				IResource r = project.getWorkspace().getRoot().findMember(path);
				if(r instanceof IContainer) {
					l.add(r);
				} else if(r != null && !project.getFullPath().isPrefixOf(r.getFullPath())) {
					l.add(r); //probably it is jar 
				}
			}
		}
		
		return l.toArray(new IResource[0]);
	}

	public static String getRelativeLocation(XModel model, String path) {
		if(path == null || path.startsWith("%")) return path; //$NON-NLS-1$
		String workspace = XModelConstants.getWorkspace(model);
		if(workspace == null) return path;
		workspace = new File(workspace).getAbsolutePath().replace('\\', '/');
		path = path.replace('\\', '/');
		String relative = org.jboss.tools.common.util.FileUtil.getRelativePath(workspace, path);
		return (relative == null) ? path : XModelConstants.WORKSPACE_REF + relative;
	}

	public static boolean projectExistsIgnoreCase(String name) {
		return findProjectIgnoreCase(name) != null;
	}
	
	public static IProject findProjectIgnoreCase(String name) {
		if(name == null || name.length() == 0) return null;
		IProject[] ps = ModelPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < ps.length; i++) {
			if(ps[i].getName().equalsIgnoreCase(name)) {
				return ps[i];
			}
		}
		return null;
	}
	
	public static boolean isJar(String path) {
		if(path == null) return false;
		path = path.toLowerCase();
		return path.endsWith(".jar") || path.endsWith(".zip"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}

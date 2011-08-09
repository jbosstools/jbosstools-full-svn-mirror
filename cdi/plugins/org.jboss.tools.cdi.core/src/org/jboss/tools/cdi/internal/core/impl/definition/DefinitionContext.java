/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.internal.core.impl.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.IRootDefinitionContext;
import org.jboss.tools.cdi.core.extension.IDefinitionContextExtension;
import org.jboss.tools.cdi.core.extension.feature.IProcessAnnotatedTypeFeature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class DefinitionContext implements IRootDefinitionContext {
	protected CDICoreNature project;
	protected IJavaProject javaProject;

	private Set<String> types = new HashSet<String>();
	private Map<IPath, Set<IPath>> childPaths = new HashMap<IPath, Set<IPath>>();
	private Map<IPath, Set<String>> resources = new HashMap<IPath, Set<String>>();
	private Map<String, TypeDefinition> typeDefinitions = new HashMap<String, TypeDefinition>();
	private Map<String, AnnotationDefinition> annotations = new HashMap<String, AnnotationDefinition>();

	private Set<String> packages = new HashSet<String>();
	private Map<String, PackageDefinition> packageDefinitions = new HashMap<String, PackageDefinition>();

	private Map<IPath, BeansXMLDefinition> beanXMLs = new HashMap<IPath, BeansXMLDefinition>();

	Set<IDefinitionContextExtension> extensions = new HashSet<IDefinitionContextExtension>();

	private Dependencies dependencies = new Dependencies();

	private DefinitionContext workingCopy;
	private DefinitionContext original;

	public DefinitionContext() {}

	public void setExtensions(Set<IDefinitionContextExtension> extensions) {
		this.extensions.clear();
		this.extensions.addAll(extensions);
		for (IDefinitionContextExtension e: extensions) e.setRootContext(this);
	}

	public Set<IDefinitionContextExtension> getExtensions() {
		return extensions;
	}

	private DefinitionContext copy(boolean clean) {
		DefinitionContext copy = new DefinitionContext();
		copy.project = project;
		copy.javaProject = javaProject;
		copy.extensions = new HashSet<IDefinitionContextExtension>();
		for (IDefinitionContextExtension e: extensions) {
			e.newWorkingCopy(clean);
			IDefinitionContextExtension ecopy = e.getWorkingCopy();
			ecopy.setRootContext(copy);
			copy.extensions.add(ecopy);
		}
		if(!clean) {
			copy.types.addAll(types);
			copy.typeDefinitions.putAll(typeDefinitions);
			copy.annotations.putAll(annotations);

			copy.packages.addAll(packages);
			copy.packageDefinitions.putAll(packageDefinitions);

			for (IPath p: resources.keySet()) {
				Set<String> set = resources.get(p);
				if(set != null) {
					Set<String> s1 = new HashSet<String>();
					s1.addAll(set);
					copy.resources.put(p, s1);
				}
			}
			for (IPath p: childPaths.keySet()) {
				Set<IPath> set = childPaths.get(p);
				if(set != null) {
					Set<IPath> s1 = new HashSet<IPath>();
					s1.addAll(set);
					copy.childPaths.put(p, s1);
				}
			}
			copy.beanXMLs.putAll(beanXMLs);
			copy.dependencies = dependencies;
		}
		
		return copy;
	}

	public void setProject(CDICoreNature project) {
		this.project = project;
		javaProject = EclipseResourceUtil.getJavaProject(project.getProject());
	}

	public CDICoreNature getProject() {
		return project;
	}

	public IJavaProject getJavaProject() {
		return javaProject;
	}

	public void addType(IPath file, String typeName, AbstractTypeDefinition def) {
		addType(file, typeName);
		if(def != null) {
			if(def instanceof AnnotationDefinition) {
				AnnotationDefinition newD = (AnnotationDefinition)def;
				AnnotationDefinition oldD = annotations.get(def.getQualifiedName());
				synchronized (annotations) {
					annotations.put(def.getQualifiedName(), newD);
				}
				if(oldD != null && oldD.getKind() != newD.getKind()) {
					annotationKindChanged(typeName);
				}
			} else {
				synchronized (typeDefinitions) {
					typeDefinitions.put(def.getQualifiedName(), (TypeDefinition)def);
				}
			}
		}
	}

	public void addPackage(IPath file, String packageName, PackageDefinition def) {
		if(file != null) {
			Set<String> ts = resources.get(file);
			if(ts == null) {
				ts = new HashSet<String>();
				resources.put(file, ts);
			}
			ts.add(packageName);
			packages.add(packageName);
			addToParents(file);
		}
		if(def != null) {
			synchronized (packageDefinitions) {
				packageDefinitions.put(def.getQualifiedName(), def);
			}
		}
	}

	public void addBeanXML(IPath path, BeansXMLDefinition def) {
		synchronized (beanXMLs) {
			beanXMLs.put(path, def);
		}
		addToParents(path);
	}

	public void addType(IPath file, String typeName) {
		if(file != null) {
			Set<String> ts = resources.get(file);
			if(ts == null) {
				ts = new HashSet<String>();
				resources.put(file, ts);
			}
			ts.add(typeName);
			types.add(typeName);
			addToParents(file);
		}
	}

	public void addToParents(IPath file) {
		if(file == null) return;
		if(file.segmentCount() < 2) return;
		IPath q = file;
		while(q.segmentCount() >= 2) {
			q = q.removeLastSegments(1);
			Set<IPath> cs = childPaths.get(q);
			if(cs == null) {
				childPaths.put(q, cs = new HashSet<IPath>());
			}
			cs.add(file);
		}
	}

	public void clean() {
		childPaths.clear();
		resources.clear();
		types.clear();
		packages.clear();
		synchronized (typeDefinitions) {
			typeDefinitions.clear();
		}
		synchronized (annotations) {
			annotations.clear();
		}
		synchronized (packageDefinitions) {
			packageDefinitions.clear();
		}
		synchronized (beanXMLs) {
			beanXMLs.clear();
		}
	
		for (IDefinitionContextExtension e: extensions) e.clean();
		dependencies.clean();
	}

	public void clean(IPath path) {
		Set<String> ts = resources.remove(path);
		if(ts != null) for (String t: ts) {
			clean(t);
		}
		synchronized (beanXMLs) {
			beanXMLs.remove(path);
		}

		Set<IPath> cs = childPaths.get(path);
		if(cs != null) {
			IPath[] ps = cs.toArray(new IPath[0]);
			for (IPath p: ps) {
				clean(p);
			}
		} else {
			removeFromParents(path);
		}
	
		for (IDefinitionContextExtension e: extensions) e.clean(path);
		dependencies.clean(path);
	}

	public void clean(String typeName) {
		types.remove(typeName);
		synchronized (typeDefinitions) {
			typeDefinitions.remove(typeName);
		}
		synchronized (annotations) {
			annotations.remove(typeName);
		}
		packages.remove(typeName);
		synchronized (packageDefinitions) {
			packageDefinitions.remove(typeName);
		}
		for (IDefinitionContextExtension e: extensions) e.clean(typeName);
	}

	void removeFromParents(IPath file) {
		if(file == null) return;
		IPath q = file;
		while(q.segmentCount() >= 2) {
			q = q.removeLastSegments(1);
			Set<IPath> cs = childPaths.get(q);
			if(cs != null) {
				cs.remove(file);
				if(cs.isEmpty()) {
					childPaths.remove(q);
				}
			}
		}
	}

	private Set<String> underConstruction = new HashSet<String>();

	public int getAnnotationKind(IType annotationType) {
		if(annotationType == null) return -1;
		if(!annotationType.exists()) return -1;
		AnnotationDefinition d = getAnnotation(annotationType);
		if(d != null) {
			return d.getKind();
		}
		String name = annotationType.getFullyQualifiedName();
		//? use cache for basic?
		if(types.contains(name)) {
			return AnnotationDefinition.NON_RELEVANT;
		}
		if(AnnotationHelper.SCOPE_ANNOTATION_TYPES.contains(name)) {
			createAnnotation(annotationType, name);
			return AnnotationDefinition.SCOPE;
		}
		if(AnnotationHelper.STEREOTYPE_ANNOTATION_TYPES.contains(name)) {
			createAnnotation(annotationType, name);
			return AnnotationDefinition.STEREOTYPE;
		}
		if(AnnotationHelper.QUALIFIER_ANNOTATION_TYPES.contains(name)) {
			createAnnotation(annotationType, name);
			return AnnotationDefinition.QUALIFIER;
		}
		if(AnnotationHelper.BASIC_ANNOTATION_TYPES.contains(name)) {
			return AnnotationDefinition.BASIC;
		}
		if(AnnotationHelper.CDI_ANNOTATION_TYPES.contains(name)) {
			return AnnotationDefinition.CDI;
		}
		if(underConstruction.contains(name)) {
			return AnnotationDefinition.BASIC;
		}
		return createAnnotation(annotationType, name);
	}

	private int createAnnotation(IType annotationType, String name) {
		underConstruction.add(name);
		AnnotationDefinition d = new AnnotationDefinition();
		d.setType(annotationType, this, 0);
		int kind = d.getKind();
		if(kind <= AnnotationDefinition.CDI) {
//			d = null; //We need it to compare kind if extensions change it.
		}
		addType(annotationType.getPath(), name, d);
		underConstruction.remove(name);
		return kind;
	}

	public void newWorkingCopy(boolean forFullBuild) {
		if(original != null) return;
		workingCopy = copy(forFullBuild);
		workingCopy.original = this;
	}

	public DefinitionContext getWorkingCopy() {
		if(original != null) {
			return this;
		}
		if(workingCopy != null) {
			return workingCopy;
		}
		workingCopy = copy(false);
		workingCopy.original = this;
		return workingCopy;
	}

	public void applyWorkingCopy() {
		if(original != null) {
			original.applyWorkingCopy();
			return;
		}
		if(workingCopy == null) {
			return;
		}
		
		Set<TypeDefinition> newTypeDefinitions = new HashSet<TypeDefinition>();
		for (String typeName: workingCopy.typeDefinitions.keySet()) {
			TypeDefinition nd = workingCopy.typeDefinitions.get(typeName);
			TypeDefinition od = typeDefinitions.get(typeName);
			if(od != nd) {
				newTypeDefinitions.add(nd);
			}
		}
		
		types = workingCopy.types;
		resources = workingCopy.resources;
		childPaths = workingCopy.childPaths;
		typeDefinitions = workingCopy.typeDefinitions;
		annotations = workingCopy.annotations;
		packages = workingCopy.packages;
		packageDefinitions = workingCopy.packageDefinitions;
		beanXMLs = workingCopy.beanXMLs;

		Set<IProcessAnnotatedTypeFeature> fs = project.getExtensionManager().getProcessAnnotatedTypeFeatures();
		if(fs != null && !fs.isEmpty()) {
			for (TypeDefinition nd: newTypeDefinitions) {
				for (IProcessAnnotatedTypeFeature f: fs) {
					f.processAnnotatedType(nd, workingCopy);
				}
			}
		}
	
		for (IDefinitionContextExtension e: extensions) {
			e.applyWorkingCopy();
		}

		//extensions may add to dependencies while they change
		dependencies = workingCopy.dependencies;

		project.getDelegate().update();

		workingCopy = null;
	}

	public void dropWorkingCopy() {
		if(original != null) {
			original.dropWorkingCopy();
		} else {
			workingCopy = null;
		}
	}

	public AnnotationDefinition getAnnotation(IType type) {
		String name = type.getFullyQualifiedName();
		AnnotationDefinition result = annotations.get(name);
		if(result == null) {
			Set<CDICoreNature> ns = project.getCDIProjects(true);
			for (CDICoreNature n: ns) {
				result = n.getDefinitions().getAnnotation(type);
				if(result != null) {
					break;
				}
			}
		}
		return result;
	}

	public AnnotationDefinition getAnnotation(String fullyQualifiedName) {
		AnnotationDefinition result = annotations.get(fullyQualifiedName);
		if(result == null) {
			Set<CDICoreNature> ns = project.getCDIProjects(true);
			for (CDICoreNature n: ns) {
				result = n.getDefinitions().getAnnotation(fullyQualifiedName);
				if(result != null) {
					break;
				}
			}
		}
		return result;
	}

	public List<AnnotationDefinition> getAllAnnotations() {
		List<AnnotationDefinition> result = new ArrayList<AnnotationDefinition>();
		synchronized (annotations) {
			result.addAll(annotations.values());
		}
		return result;
	}

	public List<TypeDefinition> getTypeDefinitions() {
		List<TypeDefinition> result = new ArrayList<TypeDefinition>();
		synchronized (typeDefinitions) {
			result.addAll(typeDefinitions.values());
		}
		for (IDefinitionContextExtension e: extensions) {
			List<TypeDefinition> ds = e.getTypeDefinitions();
			if(ds != null && !ds.isEmpty()) result.addAll(ds);
		}
		return result;
	}

	public Set<BeansXMLDefinition> getBeansXMLDefinitions() {
		Set<BeansXMLDefinition> result = new HashSet<BeansXMLDefinition>();
		synchronized (beanXMLs) {
			result.addAll(beanXMLs.values());
		}
		return result;
	}

	public PackageDefinition getPackageDefinition(String packageName) {
		return packageDefinitions.get(packageName);
	}

	public TypeDefinition getTypeDefinition(String fullyQualifiedName) {
		return typeDefinitions.get(fullyQualifiedName);
	}

	private void annotationKindChanged(String typeName) {
		List<TypeDefinition> ds = getTypeDefinitions();
		for (TypeDefinition d: ds) {
			d.annotationKindChanged(typeName, this);
		}
	}

	public void veto(IType type) {
		TypeDefinition d = typeDefinitions.get(type.getFullyQualifiedName());
		if(d != null) d.veto();
		
	}
	
	public void unveto(IType type) {
		TypeDefinition d = typeDefinitions.get(type.getFullyQualifiedName());
		if(d != null) d.unveto();
	}

	public void addDependency(IPath source, IPath target) {
		dependencies.addDependency(source, target);
	}

	public Dependencies getDependencies() {
		return dependencies;
	}

	public Dependencies getAllDependencies() {
		Set<CDICoreNature> ns = project.getCDIProjects(true);
		if(!ns.isEmpty()) {
			Dependencies d = new Dependencies();
			d.direct.putAll(dependencies.direct);
			d.reverse.putAll(dependencies.reverse);
			for (CDICoreNature n: ns) {
				d.direct.putAll(n.getDefinitions().getDependencies().direct);
				d.reverse.putAll(n.getDefinitions().getDependencies().reverse);
			}
			return d;
		}
		return dependencies;
	}
	
}


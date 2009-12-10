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
package org.jboss.tools.cdi.internal.core.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.IAnnotationDeclaration;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IObserverMethod;
import org.jboss.tools.cdi.core.IStereotype;
import org.jboss.tools.cdi.internal.core.impl.definition.AnnotationDefinition;
import org.jboss.tools.common.text.INodeReference;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class CDIProject extends CDIElement implements ICDIProject {
	CDICoreNature n;

	Map<String, StereotypeElement> stereotypes = new HashMap<String, StereotypeElement>();
	Map<String, InterceptorBindingElement> interceptorBindings = new HashMap<String, InterceptorBindingElement>();

	public CDIProject() {}

	public CDICoreNature getNature() {
		return n;
	}

	public List<INodeReference> getAlternativeClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<INodeReference> getAlternativeStereotypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<INodeReference> getAlternatives(String fullQualifiedTypeName) {
		// TODO Auto-generated method stub
		return null;
	}

	public IClassBean getBeanClass(IType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IBean> getBeans(String name,
			boolean attemptToResolveAmbiguousNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IBean> getBeans(boolean attemptToResolveAmbiguousDependency,
			IType beanType, IAnnotationDeclaration... qualifiers) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IBean> getBeans(IInjectionPoint injectionPoints) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IBean> getBeans(IPath path) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<INodeReference> getDecoratorClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<INodeReference> getDecoratorClasses(String fullQualifiedTypeName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<INodeReference> getInterceptorClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<INodeReference> getInterceptorClasses(
			String fullQualifiedTypeName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IType> getQualifierTypes() {
		Set<IType> result = new HashSet<IType>();
		List<AnnotationDefinition> ds = n.getDefinitions().getAllAnnotations();
		for (AnnotationDefinition d: ds) {
			if(d.getKind() == AnnotationDefinition.QUALIFIER) {
				result.add(d.getType());
			}
		}
		return result;
	}

	public Set<IType> getStereotypes() {
		Set<IType> result = new HashSet<IType>();
		for (IStereotype d: stereotypes.values()) {
			result.add(d.getSourceType());
		}
		return result;
	}

	public boolean isNormalScope(IType annotationType) {
		if(annotationType == null) return false;
		try {
			if(!annotationType.isAnnotation()) return false;
		} catch (CoreException e) {
			return false;
		}
		AnnotationDefinition d = n.getDefinitions().getAnnotation(annotationType);
		List<AnnotationDeclaration> ds = d.getAnnotations();
		for (AnnotationDeclaration a: ds) {
			if(CDIConstants.NORMAL_SCOPE_ANNOTATION_TYPE_NAME.equals(a.getTypeName())) {
				return true;
			}
		}		
		return false;
	}

	public boolean isPassivatingScope(IType annotationType) {
		if(annotationType == null) return false;
		try {
			if(!annotationType.isAnnotation()) return false;
		} catch (CoreException e) {
			return false;
		}
		AnnotationDefinition d = n.getDefinitions().getAnnotation(annotationType);
		List<AnnotationDeclaration> ds = d.getAnnotations();
		for (AnnotationDeclaration a: ds) {
			if(CDIConstants.NORMAL_SCOPE_ANNOTATION_TYPE_NAME.equals(a.getTypeName())) {
				IAnnotation ann = a.getDeclaration();
				try {
					IMemberValuePair[] ps = ann.getMemberValuePairs();
					if(ps != null) for (IMemberValuePair p: ps) {
						if("passivating".equals(p.getMemberName())) {
							Object o = p.getValue();
							return o != null && "true".equalsIgnoreCase(o.toString());
						}
					}
				} catch (JavaModelException e) {
					
				}
				return true;
			}
		}		
		return false;
	}

	public boolean isQualifier(IType annotationType) {
		if(annotationType == null) return false;
		try {
			if(!annotationType.isAnnotation()) return false;
		} catch (CoreException e) {
			return false;
		}
		int k = n.getDefinitions().getAnnotationKind(annotationType);
		
		return k == AnnotationDefinition.QUALIFIER;
	}

	public boolean isScope(IType annotationType) {
		if(annotationType == null) return false;
		try {
			if(!annotationType.isAnnotation()) return false;
		} catch (CoreException e) {
			return false;
		}
		int k = n.getDefinitions().getAnnotationKind(annotationType);
		
		return k == AnnotationDefinition.SCOPE;
	}

	public boolean isStereotype(IType annotationType) {
		if(annotationType == null) return false;
		try {
			if(!annotationType.isAnnotation()) return false;
		} catch (CoreException e) {
			return false;
		}
		int k = n.getDefinitions().getAnnotationKind(annotationType);
		
		return k == AnnotationDefinition.STEREOTYPE;
	}

	public Set<IBean> resolve(Set<IBean> beans) {
		// TODO 
		return beans;
	}

	public Set<IObserverMethod> resolveObserverMethods(
			IInjectionPoint injectionPoint) {
		// TODO 
		return new HashSet<IObserverMethod>();
	}

	public CDIProject getCDIProject() {
		return this;
	}

	public IResource getResource() {
		return n.getProject();
	}

	public IPath getSourcePath() {
		return n.getProject().getFullPath();
	}

	public StereotypeElement getStereotype(String qualifiedName) {
		return stereotypes.get(qualifiedName);
	}

	public InterceptorBindingElement getInterceptorBinding(String qualifiedName) {
		return interceptorBindings.get(qualifiedName);
	}

	public void rebuildAnnotationTypes() {
		stereotypes.clear();
		interceptorBindings.clear();
		List<AnnotationDefinition> ds = n.getDefinitions().getAllAnnotations();
		for (AnnotationDefinition d: ds) {
			if(d.getKind() == AnnotationDefinition.STEREOTYPE) {
				StereotypeElement s = new StereotypeElement();
				s.setDefinition(d);
				s.setParent(this);
				IPath r = d.getType().getPath();
				if(r != null) {
					s.setSourcePath(r);
				}
				stereotypes.put(d.getQualifiedName(), s);
			} else if(d.getKind() == AnnotationDefinition.INTERCEPTOR_BINDING) {
				InterceptorBindingElement s = new InterceptorBindingElement();
				s.setDefinition(d);
				s.setParent(this);
				IPath r = d.getType().getPath();
				if(r != null) {
					s.setSourcePath(r);
				}
				interceptorBindings.put(d.getQualifiedName(), s);
			}
		}
	}
}

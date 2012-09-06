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
package org.jboss.tools.cdi.internal.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.ICDIAnnotation;
import org.jboss.tools.cdi.internal.core.impl.definition.AnnotationDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.AnnotationMemberDefinition;
import org.jboss.tools.common.java.IAnnotated;
import org.jboss.tools.common.java.IAnnotationDeclaration;
import org.jboss.tools.common.java.IJavaSourceReference;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class CDIAnnotationElement extends CDIElement implements ICDIAnnotation, IAnnotated {

	protected AnnotationDefinition definition;

	Collection<IMethod> nonbindingMethods = null;

	public CDIAnnotationElement() {}

	public void setDefinition(AnnotationDefinition definition) {
		this.definition = definition;
	}

	public AnnotationDefinition getDefinition() {
		return definition;
	}

	public Collection<IMethod> getNonBindingMethods() {
		if(nonbindingMethods == null) {
			Collection<IMethod> result = new ArrayList<IMethod>();
			for (AnnotationMemberDefinition m: definition.getMethods()) {
				if(m.getNonbindingAnnotation() != null) {
					result.add(m.getMethod());
				}
			}
			if(result.size() > 5) {
				result = new HashSet<IMethod>(result);
			}
			nonbindingMethods = result;
		}
		return nonbindingMethods;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.ICDIAnnotation#getSourceType()
	 */
	public IType getSourceType() {
		return definition.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.ICDIAnnotation#getInheritedDeclaration()
	 */
	public IAnnotationDeclaration getInheritedDeclaration() {
		return definition.getInheritedAnnotation();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.ICDIAnnotation#getAnnotationDeclarations()
	 */
	public List<IAnnotationDeclaration> getAnnotationDeclarations() {
		List<IAnnotationDeclaration> result = new ArrayList<IAnnotationDeclaration>();
		result.addAll(definition.getAnnotations());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.ICDIAnnotation#getAnnotationDeclaration(java.lang.String)
	 */
	public IAnnotationDeclaration getAnnotationDeclaration(String typeName) {
		return definition.getAnnotation(typeName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.java.IAnnotated#getAnnotations()
	 */
	@Override
	public List<IAnnotationDeclaration> getAnnotations() {
		return definition.getAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.java.IAnnotated#getAnnotation(java.lang.String)
	 */
	@Override
	public IAnnotationDeclaration getAnnotation(String annotationTypeName) {
		return definition.getAnnotation(annotationTypeName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.java.IAnnotated#getAnnotationPosition(java.lang.String)
	 */
	@Override
	public IJavaSourceReference getAnnotationPosition(String annotationTypeName) {
		return definition.getAnnotationPosition(annotationTypeName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.java.IAnnotated#isAnnotationPresent(java.lang.String)
	 */
	@Override
	public boolean isAnnotationPresent(String annotationTypeName) {
		return definition.isAnnotationPresent(annotationTypeName);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String type = getSourceType() == null ? "" : getSourceType().getFullyQualifiedName();
		return super.toString() + " type=" + type; 
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.ICDIElement#getSimpleJavaName()
	 */
	@Override
	public String getElementName() {
		return definition.getType().getElementName();
	}
}
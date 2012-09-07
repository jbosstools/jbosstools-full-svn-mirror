/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.ui.test.testmodel;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IQualifier;
import org.jboss.tools.common.java.IAnnotationDeclaration;
import org.jboss.tools.common.java.IJavaSourceReference;

public class CDIQualifier implements IQualifier{
	private ICDIProject project;
	private Type cdiClass;
	
	public CDIQualifier(ICDIProject project, String qualifiedName){
		this.project = project;
		this.cdiClass = new AnnotationType(qualifiedName);
	}

	@Override
	public IType getSourceType() {
		return cdiClass;
	}

	@Override
	public IAnnotationDeclaration getInheritedDeclaration() {
		return null;
	}

	@Override
	public List<IAnnotationDeclaration> getAnnotationDeclarations() {
		return null;
	}

	@Override
	public IAnnotationDeclaration getAnnotationDeclaration(String typeName) {
		return null;
	}

	@Override
	public Set<IMethod> getNonBindingMethods() {
		return null;
	}

	@Override
	public ICDIProject getCDIProject() {
		return project;
	}

	@Override
	public IPath getSourcePath() {
		return null;
	}

	@Override
	public IResource getResource() {
		return null;
	}

	@Override
	public ICDIProject getDeclaringProject() {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public List<IAnnotationDeclaration> getAnnotations() {
		return null;
	}

	@Override
	public IAnnotationDeclaration getAnnotation(String annotationTypeName) {
		return null;
	}

	@Override
	public IJavaSourceReference getAnnotationPosition(String annotationTypeName) {
		return null;
	}

	@Override
	public boolean isAnnotationPresent(String annotationTypeName) {
		return false;
	}

	@Override
	public String getElementName() {
		return null;
	}

	@Override
	public int getId() {
		return 0;
	}
}
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

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDICorePlugin;
import org.jboss.tools.cdi.core.IAnnotationDeclaration;
import org.jboss.tools.cdi.core.ICDIAnnotation;
import org.jboss.tools.common.model.util.EclipseJavaUtil;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AnnotationDeclaration implements IAnnotationDeclaration {
	protected CDICoreNature project;
	protected IAnnotation annotation;
	protected int startPosition = -1;
	protected int length = 0;	
	protected String annotationTypeName = null;
	protected IType type = null;

	public AnnotationDeclaration() {}

	protected void copyTo(AnnotationDeclaration other) {
		other.project = project;
		other.annotation = annotation;
		other.startPosition = startPosition;
		other.length = length;
		other.annotationTypeName = annotationTypeName;
		other.type = type;
	}

	public void setProject(CDICoreNature project) {
		this.project = project;
	}

	public void setDeclaration(IAnnotation annotation, IType declaringType) {
		this.annotation = annotation;
		try {
			ISourceRange range = annotation.getSourceRange();
			if(range != null) {
				startPosition = range.getOffset();
				length = range.getLength();
			}
			String name = annotation.getElementName();
			annotationTypeName = EclipseJavaUtil.resolveType(declaringType, name);
			type = EclipseJavaUtil.findType(annotation.getJavaProject(), annotationTypeName);
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	public IAnnotation getDeclaration() {
		return annotation;
	}

	public IMember getParentMember() {
		return (IMember)annotation.getParent();
	}

	public String getTypeName() {
		return annotationTypeName;
	}

	public IType getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public int getStartPosition() {
		return startPosition;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IAnnotationDeclaration#getAnnotation()
	 */
	public ICDIAnnotation getAnnotation() {
		return null;
	}
}
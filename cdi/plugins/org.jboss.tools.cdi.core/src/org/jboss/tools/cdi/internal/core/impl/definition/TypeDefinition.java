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
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.internal.core.impl.AnnotationDeclaration;
import org.jboss.tools.cdi.internal.core.impl.ParametedType;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class TypeDefinition extends AbstractTypeDefinition {
	boolean isAbstract;
	List<FieldDefinition> fields = new ArrayList<FieldDefinition>();
	List<MethodDefinition> methods = new ArrayList<MethodDefinition>();
	boolean hasBeanConstructor = false;

	public TypeDefinition() {
	}

	@Override
	protected void init(IType contextType, DefinitionContext context) throws CoreException {
		super.init(contextType, context);
		isAbstract = Flags.isAbstract(type.getFlags());
		for (AnnotationDeclaration a: annotations) {
			//provide initialization
			context.getAnnotationKind(a.getType());
		}
		IField[] fs = getType().getFields();
		for (int i = 0; i < fs.length; i++) {
			FieldDefinition f = new FieldDefinition();
			f.setTypeDefinition(this);
			f.setField(fs[i], context);
			if(f.isCDIAnnotated()) {
				fields.add(f);
			}
		}
		IMethod[] ms = getType().getMethods();
		boolean hasConstructor = false;
		for (int i = 0; i < ms.length; i++) {
			MethodDefinition m = new MethodDefinition();
			m.setTypeDefinition(this);
			m.setMethod(ms[i], context);
			if(m.isCDIAnnotated() || (ms[i].isConstructor() && ms[i].getNumberOfParameters()==0)) {
				methods.add(m);
			}
			if(ms[i].isConstructor()) {
				hasConstructor = true; 
				if(ms[i].getNumberOfParameters() == 0 || m.getInjectAnnotation() != null) {
					hasBeanConstructor = true;
				}
			}
		}
		if(!hasConstructor) {
			hasBeanConstructor = true;
		}
	}

	public ParametedType getSuperType() {
		return parametedType == null ? null : parametedType.getSuperType();
	}

	public List<FieldDefinition> getFields() {
		return fields;
	}

	public List<MethodDefinition> getMethods() {
		return methods;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean hasBeanConstructor() {
		return hasBeanConstructor;
	}

	public AnnotationDeclaration getDecoratorAnnotation() {
		return annotationsByType.get(CDIConstants.DECORATOR_STEREOTYPE_TYPE_NAME);
	}

	public AnnotationDeclaration getInterceptorAnnotation() {
		return annotationsByType.get(CDIConstants.INTERCEPTOR_ANNOTATION_TYPE_NAME);
	}
	
	public AnnotationDeclaration getStatefulAnnotation() {
		return annotationsByType.get(CDIConstants.STATEFUL_ANNOTATION_TYPE_NAME);
	}

	public AnnotationDeclaration getStatelessAnnotation() {
		return annotationsByType.get(CDIConstants.STATELESS_ANNOTATION_TYPE_NAME);
	}

	public AnnotationDeclaration getSingletonAnnotation() {
		return annotationsByType.get(CDIConstants.SINGLETON_ANNOTATION_TYPE_NAME);
	}
}
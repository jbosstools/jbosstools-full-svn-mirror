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
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.core.IAnnotationDeclaration;
import org.jboss.tools.cdi.internal.core.impl.AnnotationDeclaration;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AnnotationDefinition extends AbstractTypeDefinition {
	public static final int NON_RELEVANT = 0;
	public static final int BASIC = 1; //has Inherited, Target, Retention
	public static final int CDI = 2; //has Model, Named, Typed, *Scoped, New
	public static final int QUALIFIER = 3; //has Qualifier
	public static final int STEREOTYPE = 4;	//has Stereotype
	public static final int INTERCEPTOR_BINDING = 5; //has InterceptorBinding
	public static final int SCOPE = 6; //has Scope or NormalScope
	//TODO add other definition kinds of interest

	protected int kind = NON_RELEVANT;

	List<AnnotationMemberDefinition> methods = new ArrayList<AnnotationMemberDefinition>();

	public AnnotationDefinition() {}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getKind() {
		return kind;
	}

	public List<AnnotationMemberDefinition> getMethods() {
		return methods;
	}

	@Override
	protected void init(IType contextType, DefinitionContext context) throws CoreException {
		super.init(contextType, context);
		if(annotations.isEmpty()) {
			//TODO check super ?
			return;
		}
		Map<String, AnnotationDeclaration> ds = new HashMap<String, AnnotationDeclaration>();
		
		for (IAnnotationDeclaration a: annotations) {
			if(a instanceof AnnotationDeclaration) {
				AnnotationDeclaration aa = (AnnotationDeclaration)a;
				String typeName = aa.getTypeName();
				ds.put(typeName, aa);
			}
		}

		if(ds.containsKey(CDIConstants.SCOPE_ANNOTATION_TYPE_NAME) 
				|| ds.containsKey(CDIConstants.NORMAL_SCOPE_ANNOTATION_TYPE_NAME)) {
			kind = SCOPE;
		} else if(ds.containsKey(CDIConstants.STEREOTYPE_ANNOTATION_TYPE_NAME)) {
			kind = STEREOTYPE;
		} else if(ds.containsKey(CDIConstants.QUALIFIER_ANNOTATION_TYPE_NAME)) {
			kind = QUALIFIER;
		} else if(ds.containsKey(CDIConstants.INTERCEPTOR_BINDING_ANNOTATION_TYPE_NAME)) {
			kind = INTERCEPTOR_BINDING;
		} else if(AnnotationHelper.BASIC_ANNOTATION_TYPES.contains(qualifiedName)) {
			kind = AnnotationDefinition.BASIC;
		} else if(AnnotationHelper.CDI_ANNOTATION_TYPES.contains(qualifiedName)) {
			kind = AnnotationDefinition.CDI;
		}
		
		if(kind == QUALIFIER) {
			initMemberDefinitions(contextType, context);
		}
	}

	void initMemberDefinitions(IType contextType, DefinitionContext context) throws CoreException {
		IMethod[] ms = getType().getMethods();
		for (int i = 0; i < ms.length; i++) {
			AnnotationMemberDefinition m = new AnnotationMemberDefinition();
			m.setAnnotationDefinition(this);
			m.setMethod(ms[i], context);
			if(m.isCDIAnnotated()) {
				methods.add(m);
			}
		}
	}

	public AnnotationDeclaration getInheritedAnnotation() {
		return annotationsByType.get(CDIConstants.INHERITED_ANNOTATION_TYPE_NAME);
	}

}

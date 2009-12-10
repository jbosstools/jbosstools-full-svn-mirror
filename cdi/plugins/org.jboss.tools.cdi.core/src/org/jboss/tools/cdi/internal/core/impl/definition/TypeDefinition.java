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
package org.jboss.tools.cdi.internal.core.impl.definition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.internal.core.impl.AnnotationDeclaration;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class TypeDefinition extends AbstractTypeDefinition {
	List<FieldDefinition> fields = new ArrayList<FieldDefinition>();
	List<MethodDefinition> methods = new ArrayList<MethodDefinition>();

	public TypeDefinition() {
	}

	@Override
	protected void init(IType contextType, DefinitionContext context) throws CoreException {
		super.init(contextType, context);
		for (AnnotationDeclaration d: annotations) {
			int kind = context.getAnnotationKind(d.getType());
			//TODO do we need to create members for specific annotations?
		}
		IField[] fs = getType().getFields();
		for (int i = 0; i < fs.length; i++) {
			FieldDefinition f = new FieldDefinition();
			f.setField(fs[i], context);
			if(f.isCDIAnnotated()) {
				fields.add(f);
			}
		}
		IMethod[] ms = getType().getMethods();
		for (int i = 0; i < ms.length; i++) {
			MethodDefinition m = new MethodDefinition();
			m.setMethod(ms[i], context);
			if(m.isCDIAnnotated()) {
				methods.add(m);
			}
		}
	}

	public List<FieldDefinition> getFields() {
		return fields;
	}

	public List<MethodDefinition> getMethods() {
		return methods;
	}

}

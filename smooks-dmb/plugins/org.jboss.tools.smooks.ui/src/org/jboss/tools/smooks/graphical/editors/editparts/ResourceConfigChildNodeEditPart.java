/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.editparts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jboss.tools.smooks.model.javabean.IExpression;
import org.jboss.tools.smooks.model.javabean.IValue;
import org.jboss.tools.smooks.model.javabean.IWiring;
import org.jboss.tools.smooks.model.javabean.JavaBeanPackage;

/**
 * @author Dart
 *
 */
public class ResourceConfigChildNodeEditPart extends AbstractResourceConfigChildNodeEditPart {

	
	public ResourceConfigChildNodeEditPart() {
		super();
	}



	@Override
	protected EStructuralFeature getFeature(EObject model) {
		
		if(model instanceof IWiring){
			return JavaBeanPackage.Literals.BEAN__WIRE_BINDINGS;
		}
		
		if(model instanceof IValue){
			return JavaBeanPackage.Literals.BEAN__VALUE_BINDINGS;
		}
		
		if(model instanceof IExpression){
			return JavaBeanPackage.Literals.BEAN__EXPRESSION_BINDINGS;
		}
		return null;
	}
	
}

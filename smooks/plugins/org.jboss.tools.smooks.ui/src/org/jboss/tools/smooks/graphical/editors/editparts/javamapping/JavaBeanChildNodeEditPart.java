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
package org.jboss.tools.smooks.graphical.editors.editparts.javamapping;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.IGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.SmooksFreemarkerTemplateGraphicalEditor;
import org.jboss.tools.smooks.graphical.editors.editparts.AbstractResourceConfigChildNodeEditPart;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 *
 */
public class JavaBeanChildNodeEditPart extends AbstractResourceConfigChildNodeEditPart {

	private List<Object> supportTypes = new ArrayList<Object>();
	
	
	
	public JavaBeanChildNodeEditPart() {
		super();
		supportTypes.add(ValueType.class);
		supportTypes.add(ExpressionType.class);
		supportTypes.add(WiringType.class);
		
		supportTypes.add(org.jboss.tools.smooks.model.javabean12.ValueType.class);
		supportTypes.add(org.jboss.tools.smooks.model.javabean12.ExpressionType.class);
		supportTypes.add(org.jboss.tools.smooks.model.javabean12.WiringType.class);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.editparts.AbstractResourceConfigChildNodeEditPart#isDragLink()
	 */
	@Override
	protected boolean isDragLink() {
		IGraphicalEditorPart part = (IGraphicalEditorPart)getEditorPart();
		if(SmooksFreemarkerTemplateGraphicalEditor.ID.equals(part.getID())){
			AbstractSmooksGraphicalModel model = (AbstractSmooksGraphicalModel) this.getModel();
			Object data = model.getData();
			data = AdapterFactoryEditingDomain.unwrap(data);
			if(data instanceof ValueType || data instanceof org.jboss.tools.smooks.model.javabean12.ValueType){
				return true;
			}
		}
		return super.isDragLink();
	}



	@Override
	protected EStructuralFeature getFeature(EObject model) {
		if(model instanceof WiringType){
			return JavabeanPackage.Literals.BINDINGS_TYPE__WIRING;
		}
		
		if(model instanceof ValueType){
			return JavabeanPackage.Literals.BINDINGS_TYPE__VALUE;
		}
		
		if(model instanceof ExpressionType){
			return JavabeanPackage.Literals.BINDINGS_TYPE__EXPRESSION;
		}
		
		if(model instanceof org.jboss.tools.smooks.model.javabean12.WiringType){
			return Javabean12Package.Literals.BEAN_TYPE__WIRING;
		}
		
		if(model instanceof org.jboss.tools.smooks.model.javabean12.ValueType){
			return Javabean12Package.Literals.BEAN_TYPE__VALUE;
		}
		
		if(model instanceof org.jboss.tools.smooks.model.javabean12.ExpressionType){
			return Javabean12Package.Literals.BEAN_TYPE__EXPRESSION;
		}
		return null;
	}
	
}

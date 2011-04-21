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

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.editparts.AbstractResourceConfigEditPart;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 * 
 */
public class JavaBeanEditPart extends AbstractResourceConfigEditPart {
	private List<Object> supportTypes = new ArrayList<Object>();

	public JavaBeanEditPart() {
		super();
		supportTypes.add(BindingsType.class);
		supportTypes.add(BeanType.class);
	}

	@Override
	protected EStructuralFeature getHostFeature(EObject model) {
		if (model instanceof BindingsType) {
			return JavabeanPackage.Literals.DOCUMENT_ROOT__BINDINGS;
		}
		if (model instanceof BeanType) {
			return Javabean12Package.Literals.JAVABEAN12_DOCUMENT_ROOT__BEAN;
		}
		return null;
	}
	
	

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.editparts.TreeContainerEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return super.createFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.editparts.
	 * AbstractResourceConfigEditPart
	 * #createModelCreationEMFCommand(org.eclipse.emf.edit.domain.EditingDomain,
	 * java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Command createModelCreationEMFCommand(EditingDomain domain, Object owner, Object type, Object collections) {
		Object model = ((AbstractSmooksGraphicalModel) getModel()).getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			boolean isArray = SmooksUIUtils.isArrayJavaGraphModel((EObject) model);
			boolean isCollection = SmooksUIUtils.isCollectionJavaGraphModel((EObject) model);
			if(isArray || isCollection){
				return null;
			}
		}
		return super.createModelCreationEMFCommand(domain, owner, type, collections);
	}

}

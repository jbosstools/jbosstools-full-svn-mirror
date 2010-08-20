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
package org.jboss.tools.smooks.graphical.editors.model.javamapping;

import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.smooks.graphical.actions.AddSmooksObjectAction;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.javabean.IJavaBeanPackage;
import org.jboss.tools.smooks.model.javabean.JavaBeanFactory;

/**
 * @author Dart
 * 
 */
public class AddExpressionAction extends AddSmooksObjectAction {

	public AddExpressionAction(IWorkbenchPart part, int style, ISmooksModelProvider provider) {
		super(part, style, provider);
		// TODO Auto-generated constructor stub
	}

	public AddExpressionAction(IWorkbenchPart part, ISmooksModelProvider provider) {
		super(part, provider);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Object getNewObject() {
		return JavaBeanFactory.eINSTANCE.createExpression();
	}

	@Override
	protected Object getNewType() {
		return IJavaBeanPackage.Literals.EXPRESSION;
	}

}

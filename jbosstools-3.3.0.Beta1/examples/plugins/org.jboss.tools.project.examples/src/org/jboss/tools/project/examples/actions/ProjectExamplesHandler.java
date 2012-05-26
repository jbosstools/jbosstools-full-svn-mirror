/*************************************************************************************
 * Copyright (c) 2008-2012 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.project.examples.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.project.examples.wizard.NewProjectExamplesWizard2;

/**
* @author snjeza
* 
*/
public class ProjectExamplesHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Wizard wizard = new NewProjectExamplesWizard2();
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
		return null;
	}

}

/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.ui.wizard;

import java.util.List;

import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.internal.ui.refactoring.RefactoringWizardDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.ui.marker.AddQualifiersToBeanProcessor;

public abstract class AbstractModifyInjectionPointWizard extends RefactoringWizard {
	public AbstractModifyInjectionPointWizard(ProcessorBasedRefactoring refactoring){
		super(refactoring, RefactoringWizard.WIZARD_BASED_USER_INTERFACE);
	}
	
	public boolean showWizard() {
		final IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		RefactoringWizardDialog dialog = new RefactoringWizardDialog(win.getShell(),this);
		dialog.open();
		return true;
	}

	
	public IBean getSelectedBean(){
		return ((AddQualifiersToBeanProcessor)((ProcessorBasedRefactoring)getRefactoring()).getProcessor()).getSelectedBean();
	}

	public IInjectionPoint getInjectionPoint(){
		return ((AddQualifiersToBeanProcessor)((ProcessorBasedRefactoring)getRefactoring()).getProcessor()).getInjectionPoint();
	}

	public void setSelectedBean(IBean bean){
		((AddQualifiersToBeanProcessor)((ProcessorBasedRefactoring)getRefactoring()).getProcessor()).setSelectedBean(bean);
	}

	public List<IBean> getBeans(){
		return ((AddQualifiersToBeanProcessor)((ProcessorBasedRefactoring)getRefactoring()).getProcessor()).getBeans();
	}
}

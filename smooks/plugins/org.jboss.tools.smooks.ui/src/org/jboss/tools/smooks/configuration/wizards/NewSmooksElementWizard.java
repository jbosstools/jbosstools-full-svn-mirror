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
package org.jboss.tools.smooks.configuration.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;

/**
 * @author Dart
 * 
 */
public class NewSmooksElementWizard extends Wizard {

	private EObject parentElement;

	private AdapterFactoryEditingDomain editingDomain;

	private NewSmooksElementWizardPage page;

	private ViewerFilter[] filters;
	
	private String text;
	
	private String description;

	public NewSmooksElementWizard(AdapterFactoryEditingDomain editingDomain, EObject parentElement,
			ViewerFilter[] filters, String text, String description) {
		super();
		this.parentElement = parentElement;
		this.editingDomain = editingDomain;
		this.filters = filters;
		this.text = text;
		this.description = description;
	}

	@Override
	public void addPages() {
		Collection<?> childDescriptors = editingDomain.getNewChildDescriptors(parentElement, null);
		Collection<IAction> actions = SmooksUIUtils.generateCreateChildActions(editingDomain, childDescriptors,
				new StructuredSelection(parentElement));
		page = new NewSmooksElementWizardPage(Messages.NewSmooksElementWizard_Smooks_Elements, actions , filters , text , description);

		this.addPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final IAction action = page.getSelectedAction();
		if (action != null) {
			try {
				this.getContainer().run(false, false, new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						action.run();
					}
				});
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}

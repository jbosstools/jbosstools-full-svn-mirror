/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.model.validate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

//import org.eclipse.emf.edit.ui.EMFEditUIPlugin;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 14, 2009
 */
public class SmooksModelValidator {

	Collection<?> selectedObjects;
	EditingDomain domain;
	
	public SmooksModelValidator(Collection<?> selectedObjects,EditingDomain domain){
		this.selectedObjects = selectedObjects;
		this.domain = domain;
	}

	public Diagnostic validate(final IProgressMonitor progressMonitor) {
		EObject eObject = (EObject) selectedObjects.iterator().next();
		int count = 0;
		for (Iterator<?> i = eObject.eAllContents(); i.hasNext(); i.next()) {
			++count;
		}

		progressMonitor.beginTask("", count);

		final AdapterFactory adapterFactory = domain instanceof AdapterFactoryEditingDomain ? ((AdapterFactoryEditingDomain) domain)
				.getAdapterFactory()
				: null;

		Diagnostician diagnostician = new Diagnostician() {
			@Override
			public String getObjectLabel(EObject eObject) {
				if (adapterFactory != null && !eObject.eIsProxy()) {
					IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory.adapt(eObject,
							IItemLabelProvider.class);
					if (itemLabelProvider != null) {
						return itemLabelProvider.getText(eObject);
					}
				}

				return super.getObjectLabel(eObject);
			}

			@Override
			public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics,
					Map<Object, Object> context) {
				progressMonitor.worked(1);
				return super.validate(eClass, eObject, diagnostics, context);
			}
		};

		progressMonitor.setTaskName("Validating...");

		return diagnostician.validate(eObject);
	}
}

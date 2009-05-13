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
package org.jboss.tools.smooks.configuration.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.swt.widgets.Display;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 14, 2009
 */
public class SmooksModelValidator implements ISmooksValidator{

	Collection<?> selectedObjects;
	EditingDomain domain;
	private boolean starting = false;
	private boolean waiting = false;
	private Object lock = new Object();
	
	private long watingTime = 300;

	private List<ISmooksModelValidateListener> listeners = new ArrayList<ISmooksModelValidateListener>();
	
	private List<ISmooksValidator> validatorList = new ArrayList<ISmooksValidator>();

	public SmooksModelValidator(Collection<?> selectedObjects, EditingDomain domain) {
		this();
		this.selectedObjects = selectedObjects;
		this.domain = domain;
	}

	public SmooksModelValidator() {
		validatorList.add(new ClassFieldEditorValidator());
	}

	public void addValidateListener(ISmooksModelValidateListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void removeValidateListener(ISmooksModelValidateListener l) {
		listeners.remove(l);
	}

	public List<Diagnostic> validate(Collection<?> selectedObjects, EditingDomain editingDomain) {
		this.selectedObjects = selectedObjects;
		domain = editingDomain;
		return validate(new NullProgressMonitor());
	}

	public List<Diagnostic> validate(final IProgressMonitor progressMonitor) {
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

		Diagnostic diagnostic = diagnostician.validate(eObject);
		
		List<Diagnostic> list = new ArrayList<Diagnostic>();
		list.add(diagnostic);
		for (Iterator<?> iterator = this.validatorList.iterator(); iterator.hasNext();) {
			ISmooksValidator validator = (ISmooksValidator) iterator.next();
			List<Diagnostic> d = validator.validate(selectedObjects, domain);
			for (Iterator<?> iterator2 = d.iterator(); iterator2.hasNext();) {
				Diagnostic diagnostic2 = (Diagnostic) iterator2.next();
				((BasicDiagnostic)diagnostic).add(diagnostic2);
			}
		}
		return list;
	}

	public void startValidate(final Collection<?> selectedObjects, final EditingDomain editingDomain) {
		if (starting) {
			synchronized (lock) {
				waiting = true;
			}
			return;
		}
		Thread thread = new Thread() {
			public void run() {
				synchronized (lock) {
					starting = true;
					waiting = true;
				}
				while (waiting) {
					try {
						waiting = false;
						Thread.sleep(watingTime);
						Thread.yield();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
						final ISmooksModelValidateListener l = (ISmooksModelValidateListener) iterator.next();
						Display.getDefault().syncExec(new Runnable() {

							/*
							 * (non-Javadoc)
							 * 
							 * @see java.lang.Runnable#run()
							 */
							public void run() {
								l.validateStart();
							}

						});

					}

					final List<Diagnostic> d = validate(selectedObjects, editingDomain);

					for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
						final ISmooksModelValidateListener l = (ISmooksModelValidateListener) iterator.next();
						Display.getDefault().syncExec(new Runnable() {

							/*
							 * (non-Javadoc)
							 * 
							 * @see java.lang.Runnable#run()
							 */
							public void run() {
								l.validateEnd(d);
							}

						});
					}
				} finally {
					waiting = false;
					starting = false;
				}
			}
		};
		thread.setName("Validate Smooks model");
		thread.start();
	}
}

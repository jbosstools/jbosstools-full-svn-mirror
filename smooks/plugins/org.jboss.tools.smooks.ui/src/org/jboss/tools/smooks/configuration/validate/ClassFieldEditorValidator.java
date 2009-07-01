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
package org.jboss.tools.smooks.configuration.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class ClassFieldEditorValidator extends AbstractValidator {

	private ProjectClassLoader classLoader;

	/**
	 * @return the classLoader
	 */
	public ProjectClassLoader getClassLoader(EObject obj) {
		if (classLoader != null) {
			return classLoader;
		}
		IResource resource = SmooksUIUtils.getResource(obj);
		try {
			classLoader = new ProjectClassLoader(JavaCore.create(resource.getProject()));
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return classLoader;
	}

	public List<Diagnostic> validate(Collection<?> selectionObjects) {
		List<Diagnostic> list = new ArrayList<Diagnostic>();
		for (Iterator<?> iterator = selectionObjects.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof BindingsType) {
				BindingsType bindings = (BindingsType) object;
				classLoader = getClassLoader(bindings);
				String clazz = bindings.getClass_();
				if (clazz != null) {
					clazz = clazz.trim();
					if (clazz.endsWith("[]")) {
						clazz = clazz.substring(0, clazz.length() - 2);
					}
				}
				Class<?> clazz1 = null;
				if (clazz != null && classLoader != null) {
					try {
						clazz1 = classLoader.loadClass(clazz);
					} catch (ClassNotFoundException e) {
						// ignore
					}
				}
				String message = "Can't find class : \"" + clazz + "\"";
				if (clazz1 == null) {
					list.add(newWaringDiagnostic(message, bindings, JavabeanPackage.Literals.BINDINGS_TYPE__CLASS));
				}
			}

			if (object instanceof EObject) {
				List<Diagnostic> dd = validate(((EObject) object).eContents());
				if (dd != null) {
					list.addAll(dd);
				}
			}
		}
		return list;
	}

	public List<Diagnostic> validate(Collection<?> selectedObjects, EditingDomain editingDomain) {
		return validate(selectedObjects);
	}

	public void initValidator(Collection<?> selectedObjects, EditingDomain editingDomain) {
		// TODO Auto-generated method stub
		
	}
}

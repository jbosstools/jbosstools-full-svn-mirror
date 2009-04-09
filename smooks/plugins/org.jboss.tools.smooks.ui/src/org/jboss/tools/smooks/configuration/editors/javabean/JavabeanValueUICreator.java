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
package org.jboss.tools.smooks.configuration.editors.javabean;

import org.eclipse.emf.ecore.EAttribute;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;


/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 9, 2009
 */
public class JavabeanValueUICreator extends PropertiesAndSetterMethodSearchFieldEditorCreator {

	@Override
	protected boolean canCreatePropertiesSearchFieldEditor(EAttribute feature) {
		if(feature == JavabeanPackage.eINSTANCE.getValueType_Property()){
			return true;
		}
		return false;
	}

	@Override
	protected boolean canCreateMethodsSearchFieldEditor(EAttribute feature) {
		if(feature == JavabeanPackage.eINSTANCE.getValueType_SetterMethod()){
			return true;
		}
		return super.canCreateMethodsSearchFieldEditor(feature);
	}

}

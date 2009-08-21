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
package org.jboss.tools.smooks.graphical.editors.editparts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;

/**
 * @author Dart
 * 
 */
public class JavaBeanChildNodeEditPart extends TreeNodeEditPart {

	@Override
	protected boolean isDragLink() {
		AbstractSmooksGraphicalModel model = (AbstractSmooksGraphicalModel) getModel();
		if (model != null) {
			Object data = model.getData();
			data = AdapterFactoryEditingDomain.unwrap(data);
			if (data != null && data instanceof EObject) {
				EStructuralFeature idRefFeature = SmooksUIUtils.getBeanIDRefFeature((EObject) data);
				if (idRefFeature != null) {
					if (((EObject) data).eGet(idRefFeature) == null || !((EObject) data).eIsSet(idRefFeature)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}

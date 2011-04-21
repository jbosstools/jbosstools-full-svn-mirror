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
package org.jboss.tools.smooks.graphical.editors.editparts.freemarker;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.commands.AddSmooksGraphicalModelCommand;
import org.jboss.tools.smooks.graphical.editors.editparts.AbstractResourceConfigEditPart;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerCSVNodeGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerCreationFactory;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;

/**
 * @author Dart
 * 
 */
public class FreemarkerTemplateEditPart extends AbstractResourceConfigEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.editparts.
	 * AbstractResourceConfigEditPart
	 * #getHostFeature(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected EStructuralFeature getHostFeature(EObject model) {
		return FreemarkerPackage.Literals.DOCUMENT_ROOT__FREEMARKER;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.editparts.
	 * AbstractResourceConfigEditPart#getCreateCommand(org.eclipse.gef.EditPart,
	 * org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(EditPart host, CreateRequest request) {
		Object type = request.getNewObjectType();
		Object model = request.getNewObject();
		if (FreemarkerCreationFactory.CSV_RECORD.equals(type)) {
			Object parent = host.getModel();
			ILabelProvider provider = ((AbstractResourceConfigGraphModel) parent).getLabelProvider();
			ITreeContentProvider provider1 = ((AbstractResourceConfigGraphModel) parent).getContentProvider();
			IEditingDomainProvider provider2 = ((AbstractResourceConfigGraphModel) parent).getDomainProvider();
			AbstractSmooksGraphicalModel childModel = new FreemarkerCSVNodeGraphicalModel(model, provider1, provider, provider2);
			if (model != null && childModel instanceof AbstractSmooksGraphicalModel
					&& parent instanceof AbstractSmooksGraphicalModel) {
				return new AddSmooksGraphicalModelCommand((AbstractSmooksGraphicalModel) parent,
						(AbstractSmooksGraphicalModel) childModel);
			}
		}
		return null;
	}
}

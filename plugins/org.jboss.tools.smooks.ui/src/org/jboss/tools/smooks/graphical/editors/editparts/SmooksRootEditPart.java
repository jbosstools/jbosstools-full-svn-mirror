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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.command.GEFAdapterCommand;
import org.jboss.tools.smooks.gef.tree.editparts.RootEditPart;
import org.jboss.tools.smooks.gef.tree.editpolicy.RootPanelXYLayoutEditPolicy;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.commands.DefaultSmooksCommandProvider;
import org.jboss.tools.smooks.graphical.editors.commands.ISmooksCommandProvider;

/**
 * @author Dart
 * 
 */
public class SmooksRootEditPart extends RootEditPart {

	public static final int BEAN_TYPE = 2;

	public static final int BINDINGS_TYPE = 1;

	private ISmooksCommandProvider commandProvider = null;

	private GEFAdapterCommand createGEFCommand(EditingDomain domain, org.eclipse.emf.common.command.Command emfCommand,
			final ISmooksModelProvider provider, Object collections, Object owner, Object feature,
			IEditorPart editorPart) {
		GEFAdapterCommand command = (GEFAdapterCommand) getCommandProvider().createSmooksCommand(domain, emfCommand,
				provider, collections, owner, feature, editorPart);
		return command;
	}

	/**
	 * @return the commandProvider
	 */
	public ISmooksCommandProvider getCommandProvider() {
		if (commandProvider == null) {
			commandProvider = new DefaultSmooksCommandProvider();
		}
		return commandProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootPanelXYLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				Object model = request.getNewObject();
				Object type = request.getNewObjectType();
				GraphicalViewer viewer = (GraphicalViewer) ((GraphicalEditPart) getHost()).getViewer();
				IEditorPart editorPart = ((DefaultEditDomain) viewer.getEditDomain()).getEditorPart();
				if (editorPart instanceof SmooksGraphicalEditorPart) {
					EObject owner = ((SmooksGraphicalEditorPart) editorPart).getSmooksResourceList();
					EditingDomain domain = ((SmooksGraphicalEditorPart) editorPart).getEditingDomain();
					if (model instanceof FeatureMap.Entry) {
						EStructuralFeature type1 = ((FeatureMap.Entry) model).getEStructuralFeature();
						model = ((FeatureMap.Entry) model).getValue();
						model = EcoreUtil.copy((EObject) model);
						model = FeatureMapUtil.createEntry(type1, model);
					}
					org.eclipse.emf.common.command.Command emfCommand = AddCommand.create(domain, owner, type, model);
					final ISmooksModelProvider provider = (ISmooksModelProvider) ((SmooksGraphicalEditorPart) editorPart)
							.getSmooksModelProvider();
					if (emfCommand.canExecute()) {
						GEFAdapterCommand command = createGEFCommand(domain, emfCommand, provider, model, owner, type,
								editorPart);
						if (command == null) {
							return null;
						}
						command.setCollections(model);
						command.setOwner(owner);
						command.setFeature(type);
						command.setX(request.getLocation().x);
						command.setY(request.getLocation().y);
						return command;
					}
				}
				return null;
			}

		});
	}

	protected IEditorPart getEditorPart() {
		GraphicalViewer viewer = (GraphicalViewer) this.getViewer();
		DefaultEditDomain editDomain = (DefaultEditDomain) viewer.getEditDomain();
		return editDomain.getEditorPart();
	}

}

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
package org.jboss.tools.smooks.edimap.editor;

import org.eclipse.draw2d.Label;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.edimap.actions.DeleteEDIModelCommand;
import org.jboss.tools.smooks.edimap.actions.RenameXmlTagNameCommand;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.figures.TreeNodeFigure;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.medi.MEdiPackage;
import org.jboss.tools.smooks.model.medi.MappingNode;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class EDITreeNodeEditPart extends TreeNodeEditPart {

	private DirectEditManager manager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#performDirectEdit
	 * ()
	 */
	@Override
	protected void performDirectEdit() {
		if (manager == null) {
			Label l = ((TreeNodeFigure) getFigure()).getLabel();
			manager = new EDIEdiPartDirectEditManager(this, TextCellEditor.class, new EDILabelCellEditorLocator(l), l);
		}
		manager.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#createEditPolicies
	 * ()
	 */
	@Override
	protected void createEditPolicies() {

		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicy() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand
			 * (org.eclipse.gef.requests.DirectEditRequest)
			 */
			@Override
			protected Command getDirectEditCommand(DirectEditRequest request) {
				TreeNodeModel node = (TreeNodeModel) getHost().getModel();
				GraphicalViewer viewer = (GraphicalViewer) ((AbstractGraphicalEditPart) getHost()).getViewer();
				DefaultEditDomain domain = (DefaultEditDomain) viewer.getEditDomain();
				IEditorPart part = domain.getEditorPart();
				if (part instanceof EDIMapFormPage) {
					part = ((EDIMapFormPage) part).getEditor();
				}
				if (part instanceof ISmooksModelProvider) {
					EditingDomain ed = ((ISmooksModelProvider) part).getEditingDomain();
					Object feature = null;
					Object owner = node.getData();
					if (owner instanceof MappingNode) {
						feature = MEdiPackage.Literals.MAPPING_NODE__XMLTAG;
					}
					org.eclipse.emf.common.command.Command command = SetCommand.create(ed, node.getData(), feature,
							request.getCellEditor().getValue());
					if (command != null) {
						return new RenameXmlTagNameCommand((TreeNodeEditPart) getHost(), ed, command);
					}
				}
				return null;
				// RenameActivityCommand cmd = new RenameActivityCommand();
				// cmd.setSource((Activity)getHost().getModel());
				// cmd.setOldName(((Activity)getHost().getModel()).getName());
				// cmd.setName((String)request.getCellEditor().getValue());
				// return cmd;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue
			 * (org.eclipse.gef.requests.DirectEditRequest)
			 */
			@Override
			protected void showCurrentEditValue(DirectEditRequest request) {

			}

		});

		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand
			 * (org.eclipse.gef.requests.GroupRequest)
			 */
			@Override
			protected Command createDeleteCommand(GroupRequest deleteRequest) {
				TreeNodeModel node = (TreeNodeModel) getHost().getModel();
				GraphicalViewer viewer = (GraphicalViewer) ((AbstractGraphicalEditPart) getHost()).getViewer();
				DefaultEditDomain domain = (DefaultEditDomain) viewer.getEditDomain();
				IEditorPart part = domain.getEditorPart();
				if (part instanceof EDIMapFormPage) {
					part = ((EDIMapFormPage) part).getEditor();
				}
				if (part instanceof ISmooksModelProvider) {
					EditingDomain ed = ((ISmooksModelProvider) part).getEditingDomain();
					org.eclipse.emf.common.command.Command command = DeleteCommand.create(ed, node.getData());
					if (command != null) {
						return new DeleteEDIModelCommand((TreeNodeEditPart) getHost(), ed, command);
					}
				}

				return super.createDeleteCommand(deleteRequest);
			}

		});
		super.createEditPolicies();
	}

}

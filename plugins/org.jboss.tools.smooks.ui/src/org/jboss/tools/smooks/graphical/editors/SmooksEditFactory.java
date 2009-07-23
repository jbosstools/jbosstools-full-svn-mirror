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
package org.jboss.tools.smooks.graphical.editors;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.common.SmooksGEFEditFactory;
import org.jboss.tools.smooks.gef.tree.editparts.RootEditPart;
import org.jboss.tools.smooks.gef.tree.editparts.TreeContainerEditPart;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeConnectionEditPart;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.editparts.InputDataContainerEditPart;
import org.jboss.tools.smooks.graphical.editors.editparts.InputDataTreeNodeEditPart;
import org.jboss.tools.smooks.graphical.editors.model.InputDataContianerModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataTreeNodeModel;

/**
 * @author Dart
 *
 */
public class SmooksEditFactory extends SmooksGEFEditFactory implements EditPartFactory{

	public SmooksEditFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if(model instanceof RootModel){
			editPart = new RootEditPart();
		}
		if(model.getClass() == TreeNodeModel.class){
			editPart = new TreeNodeEditPart();
		}if(model.getClass() == TreeContainerModel.class){
			editPart = new TreeContainerEditPart();
		}
		if(model.getClass() == InputDataTreeNodeModel.class){
			editPart = new InputDataTreeNodeEditPart();
		}if(model.getClass() == InputDataContianerModel.class){
			editPart = new InputDataContainerEditPart();
		}
		if(model.getClass() == TreeNodeConnection.class){
			editPart = new TreeNodeConnectionEditPart();
		}
		if(editPart != null){
			editPart.setModel(model);
		}
		return editPart;
	}

}

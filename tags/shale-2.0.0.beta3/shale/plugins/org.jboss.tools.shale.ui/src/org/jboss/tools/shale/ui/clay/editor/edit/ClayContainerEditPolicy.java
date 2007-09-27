/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.ui.clay.editor.edit;

//import java.util.List;

//import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
//import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class ClayContainerEditPolicy extends ContainerEditPolicy {

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

/*public Command getOrphanChildrenCommand(GroupRequest request) {
	List parts = request.getEditParts();
	CompoundCommand result = 
		new CompoundCommand(JSFMessages.LogicContainerEditPolicy_OrphanCommandLabelText);
	for (int i = 0; i < parts.size(); i++) {
		OrphanChildCommand orphan = new OrphanChildCommand();
		orphan.setChild((JSFSubpart)((EditPart)parts.get(i)).getModel());
		orphan.setParent((JSFModel)getHost().getModel());
		orphan.setLabel(JSFMessages.LogicElementEditPolicy_OrphanCommandLabelText);
		result.add(orphan);
	}
	return result.unwrap();
}*/

}

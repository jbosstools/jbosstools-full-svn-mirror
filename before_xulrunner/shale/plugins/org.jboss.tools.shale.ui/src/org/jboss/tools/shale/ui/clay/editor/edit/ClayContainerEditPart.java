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

import java.util.List;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.jboss.tools.shale.ui.clay.editor.model.IClayModel;

	/**
	 * Provides support for Container EditParts.
	 */
	abstract public class ClayContainerEditPart extends ClayEditPart {
	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {
			public void getName(AccessibleEvent e) {
				e.result = getClayModel().toString();
			}
		};
	}

	/**
	 * Installs the desired EditPolicies for this.
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ClayContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ClayXYLayoutEditPolicy());
	}

	/**
	 * Returns the model of this as a LogicDiagram.
	 * 
	 * @return LogicDiagram of this.
	 */
	protected IClayModel getClayModel() {
		return (IClayModel)getModel();
	}

	/**
	 * Returns the children of this through the model.
	 * 
	 * @return Children of this as a List.
	 */
	protected List getModelChildren() {
		return getClayModel().getComponentList().getElements();
	}

}

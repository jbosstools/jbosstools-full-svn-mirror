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

import java.beans.PropertyChangeEvent;

import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.gef.tree.editparts.TreeContainerEditPart;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;

/**
 * @author Dart
 *
 */
public class InputDataContainerEditPart extends TreeContainerEditPart {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}

	@Override
	protected String generateFigureID() {
		TreeNodeModel model = (TreeNodeModel)this.getModel();
		Object data = model.getData();
		if(data instanceof IXMLStructuredObject){
			return ((IXMLStructuredObject)data).getID().toString();
		}
		return null;
	}
	
}

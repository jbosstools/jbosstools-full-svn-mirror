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
package org.jboss.tools.smooks.graphical.editors.model;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.gef.tree.model.TriggerConnection;
import org.jboss.tools.smooks.gef.tree.model.ValueBindingConnection;

/**
 * @author Dart
 *
 */
public class InputDataTreeNodeModel extends TreeNodeModel {

	public InputDataTreeNodeModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider) {
		super(data, contentProvider, labelProvider);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		return new InputDataTreeNodeModel(model, contentProvider, labelProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#isLinkable(java.lang.Class)
	 */
	@Override
	public boolean isLinkable(Class<?> connectionType) {
		if(connectionType == null){
			return true;
		}
		if(connectionType == TriggerConnection.class || connectionType == ValueBindingConnection.class){
			return true;
		}
		return false;
	}
}

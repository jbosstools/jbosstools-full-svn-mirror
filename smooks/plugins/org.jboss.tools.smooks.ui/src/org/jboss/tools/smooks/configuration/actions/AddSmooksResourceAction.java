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
package org.jboss.tools.smooks.configuration.actions;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class AddSmooksResourceAction extends CreateChildAction {

	public AddSmooksResourceAction(EditingDomain editingDomain, ISelection selection, Object descriptor) {
		super(editingDomain, selection, descriptor);
		// TODO Auto-generated constructor stub
	}

	public AddSmooksResourceAction(IEditorPart editorPart, ISelection selection, Object descriptor) {
		super(editorPart, selection, descriptor);
		// TODO Auto-generated constructor stub
	}

	public AddSmooksResourceAction(IWorkbenchPart workbenchPart, ISelection selection, Object descriptor) {
		super(workbenchPart, selection, descriptor);
		// TODO Auto-generated constructor stub
	}

	public Object getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Object descriptor) {
		this.descriptor = descriptor;
	}
}

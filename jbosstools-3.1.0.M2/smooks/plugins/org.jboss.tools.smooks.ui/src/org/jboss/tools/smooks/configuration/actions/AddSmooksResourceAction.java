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

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.smooks.model.smooks.AbstractReader;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class AddSmooksResourceAction extends CreateChildAction {

	public AddSmooksResourceAction(EditingDomain editingDomain, ISelection selection, Object descriptor) {
		super(editingDomain, selection, descriptor);
		resetActionText();
	}

	public AddSmooksResourceAction(IEditorPart editorPart, ISelection selection, Object descriptor) {
		super(editorPart, selection, descriptor);
		resetActionText();
	}

	public AddSmooksResourceAction(IWorkbenchPart workbenchPart, ISelection selection, Object descriptor) {
		super(workbenchPart, selection, descriptor);
		resetActionText();
	}

	public Object getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Object descriptor) {
		this.descriptor = descriptor;
	}
	
	protected void resetActionText(){
		if(descriptor instanceof CommandParameter){
			CommandParameter parameter = (CommandParameter)descriptor;
			if(parameter.getValue() != null){
				Object value = AdapterFactoryEditingDomain.unwrap(parameter.getValue());
				if(value instanceof AbstractReader){
//					if(value instanceof JsonReader){
//						setText("JSON Reader");
//					}
//					
//					if(value instanceof ReaderType){
//						setText("Custome Reader");
//					}
				}
			}
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.javabean.commandprocessor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.javabean.ui.JavaBeanModelCreationDialog;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class JavaBeanModelCommandProcessor implements ICommandProcessor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor#getNewModel
	 * (java.lang.Object, java.lang.Object, org.eclipse.gef.GraphicalEditPart)
	 */
	public Object getNewModel(CreateRequest request,
			GraphicalEditPart rootEditPart) {
		DefaultEditDomain domain = (DefaultEditDomain) ((GraphicalViewer) rootEditPart
				.getViewer()).getEditDomain();
		// rootEditPart.getg
		IEditorPart editorPart = domain.getEditorPart();
		Shell shell = editorPart.getSite().getShell();
		JavaBeanModelCreationDialog dialog = new JavaBeanModelCreationDialog(
				shell, UIUtils
						.getJavaProjectFromEditorPart(editorPart));
		if(dialog.open() == Window.OK){
			return dialog.getCheckedJavaBeanModel();
		}
		return null;
	}

}

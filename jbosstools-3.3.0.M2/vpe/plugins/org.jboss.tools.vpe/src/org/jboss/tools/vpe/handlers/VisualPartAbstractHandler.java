/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.ISources;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;

/**
 * Handler which disable icons when vpe part not visible
 * 
 * @author mareshkau
 * 
 */
public abstract class VisualPartAbstractHandler extends AbstractHandler
		implements IElementUpdater {
	@Override
	public void setEnabled(Object evaluationContext) {
		boolean enabled = false;
		
		if (evaluationContext instanceof IEvaluationContext) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object activeEditor = context.getVariable(ISources.ACTIVE_EDITOR_NAME);
			if(activeEditor instanceof JSPMultiPageEditor){
				JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) activeEditor;
				if(jspEditor.getVisualEditor().getController()!=null)
				enabled=((VpeController)(jspEditor.getVisualEditor().getController())).isVisualEditorVisible();
			}
		}
		
		if (enabled != isEnabled()) {
			setBaseEnabled(enabled);
		}
	}
	
	public void updateElement(UIElement element, Map parameters) {
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}
}

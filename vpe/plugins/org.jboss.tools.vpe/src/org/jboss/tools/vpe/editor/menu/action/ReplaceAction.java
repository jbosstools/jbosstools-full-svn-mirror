/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu.action;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.vpe.editor.context.VpePageContext;

/**
 * Replace Action for visual editor from content menu
 * @author mareshkau
 *
 */
public class ReplaceAction  extends InsertAction{
	
	/**
	 * 
	 * @param title
	 * @param region
	 * @param item
	 * @param pageContext
	 * @param sourceEditor
	 */
	public ReplaceAction(String title, Point region, XModelObject item,
			VpePageContext pageContext, StructuredTextEditor sourceEditor) {
		super(title, region, item, pageContext, sourceEditor);
	}

	@Override
	public void run() {
		getSourceEditor().getTextViewer().getTextWidget().replaceTextRange(getRegion().x, getRegion().y, ""); //$NON-NLS-1$
		getRegion().y=0;
		super.run();
	}

	
}

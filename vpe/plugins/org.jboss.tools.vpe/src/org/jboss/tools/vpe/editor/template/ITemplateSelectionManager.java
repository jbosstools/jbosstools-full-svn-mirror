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
package org.jboss.tools.vpe.editor.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsISelection;

/**
 * 
 * interface for template selection
 * 
 * @author Sergey Dzmitrovich
 */
public interface ITemplateSelectionManager {

	/**
	 * selection
	 * 
	 * @param pageContext
	 * @param selection
	 */
	public void setSelection(VpePageContext pageContext, nsISelection selection);

	/**
	 * select visual element by source selection
	 * 
	 * @param pageContext
	 * @return true if visual element was selected
	 */
	public void setSelectionBySource(VpePageContext pageContext,
			VpeSelectionController selectionController, int focus, int anchor);

	/**
	 * set selection by mouse
	 * 
	 * @param visualSelectionController
	 * 
	 * @param mouseEvent
	 */
	public void setSelectionByMouse(VpePageContext pageContext,
			VpeSelectionController selectionController,
			nsIDOMMouseEvent mouseEvent);

}

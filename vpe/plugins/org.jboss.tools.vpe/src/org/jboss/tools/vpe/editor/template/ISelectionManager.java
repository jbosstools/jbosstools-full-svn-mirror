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

import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsISelection;

/**
 * 
 * interface for template selection
 * 
 * @author Sergey Dzmitrovich
 */
public interface ISelectionManager {

	/**
	 * set selection
	 * 
	 * @param selection
	 */
	public void setSelection(nsISelection selection);

	/**
	 * set selection by mouse
	 * 
	 * @param mouseEvent
	 */
	public void setSelection(nsIDOMMouseEvent mouseEvent);

	/**
	 * to bring in correspondence visual selection and source selection
	 */
	public void refreshVisualSelection();

}

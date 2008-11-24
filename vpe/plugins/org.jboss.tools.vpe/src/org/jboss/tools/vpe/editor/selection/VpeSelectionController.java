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
package org.jboss.tools.vpe.editor.selection;

import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;

/**
 * Class decorator for nsISelectionController
 * @author Maxim Areshkau
 */
public class VpeSelectionController {

	private nsISelectionController selectionController;

	/**
	 * @param selection
	 */
	public VpeSelectionController(nsISelectionController selectionController) {
		
		setSelectionController(selectionController);
	}

	/**
	 * type - not used in 
	 * @return the selection
	 */
	public nsISelection getSelection(short type) {
		
		return getSelectionController().getSelection(type);
	}
	
	/**
	 * Sets caret enables or disabled
	 * @param value
	 */
	public void setCaretEnabled(boolean value){	
		
		getSelectionController().setCaretEnabled(value);
	}
	/**
	 * Sets selection flags
	 * @param selectionFlags
	 */
	public void setSelectionFlags(short selectionFlags){		
	
		getSelectionController().setSelectionFlags(selectionFlags);
	}
	/**
	 * @see nsISelectionController.lineMove()
	 * @param forward
	 * @param extend
	 */
	public void lineMove(boolean forward, boolean extend) {
		//mareshkau, hack for JBIDE-3209
		selectionController.characterMove(true, false);
		selectionController.characterMove(false, false);
		selectionController.lineMove(forward, extend);
	}

	/**
	 * @return the selectionController
	 */
	private nsISelectionController getSelectionController() {
		return selectionController;
	}

	/**
	 * @param selectionController the selectionController to set
	 */
	private void setSelectionController(nsISelectionController selectionController) {
		this.selectionController = selectionController;
	}
}

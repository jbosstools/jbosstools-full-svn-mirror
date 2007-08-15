/**
 * 
 */
package org.jboss.tools.vpe.editor.selection;

import org.mozilla.interfaces.nsISelection;

/**
 * @author Maxim Areshkau
 * Class which replace nsISelectionController functionality
 */
public class VpeSelectionController {

	private nsISelection selection;

	/**
	 * @param selection
	 */
	public VpeSelectionController(nsISelection selection) {
		this.selection = selection;
	}

	/**
	 * type - not used in 
	 * @return the selection
	 */
	public nsISelection getSelection(long type) {
		return selection;
	}

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(nsISelection selection) {
		this.selection = selection;
	}
	
	//method stub just because it is exist in nsISelectionController
	public void setCaretEnabled(boolean value){	
	}
	//method stub just because it is exist in nsISelectionController
	public void setSelectionFlags(short selectionFlags){		
	}
}

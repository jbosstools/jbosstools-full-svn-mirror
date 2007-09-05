/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.mozilla;


import org.mozilla.interfaces.nsIClipboardDragDropHooks;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDragSession;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;

public class MozillaBaseEventListener implements nsIClipboardDragDropHooks {
	// TODO Max Areshkau add DnD support
//	private XPCOMObject dropListener;
//	private VpeDnD dnd; 
//	private int refCount = 0;

	
	public MozillaBaseEventListener() {
		// TODO Max Areshkau delete this reference afrer DnD had been added
//		createCOMInterfaces();
	}

	void createCOMInterfaces() {
		// TODO Max Areshkau delete this reference afrer DnD had been added
//		// VpeDnD
//		dropListener = new XPCOMObject(new int[]{2,0,0,4,4,3,6,5}) {
//			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
//			public int method1(int[] args) {return AddRef();}
//			public int method2(int[] args) {return Release();}
//			public int method3(int[] args) {return CanDrag(args[0], args[1], args[2], args[3]);}
//			public int method4(int[] args) {return CanDrop(args[0], args[1], args[2], args[3]);}
//			public int method5(int[] args) {return Drop(args[0], args[1], args[2]);}
//			public int method6(int[] args) {return CanDropExternal(args[0], args[1], args[2], args[3], args[4], args[5]);}
//			public int method7(int[] args) {return DropExternal(args[0], args[1], args[2], args[3], args[4]);}
//		};

		// TODO Max Areshkau delete this reference afrer DnD had been added
//		dnd = getDnD();
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#allowDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsIDragSession)
	 */
	public boolean allowDrop(nsIDOMEvent event, nsIDragSession dragSession) {
		event.preventDefault();
		event.stopPropagation();
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#allowStartDrag(org.mozilla.interfaces.nsIDOMEvent)
	 */
	public boolean allowStartDrag(nsIDOMEvent event) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onCopyOrDrag(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
	 */
	public boolean onCopyOrDrag(nsIDOMEvent event, nsITransferable transferable) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onPasteOrDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
	 */
	public boolean onPasteOrDrop(nsIDOMEvent event,	nsITransferable transferable) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
	 */
	public nsISupports queryInterface(String arg0) {
		return Mozilla.queryInterface(this, arg0);
	}
}

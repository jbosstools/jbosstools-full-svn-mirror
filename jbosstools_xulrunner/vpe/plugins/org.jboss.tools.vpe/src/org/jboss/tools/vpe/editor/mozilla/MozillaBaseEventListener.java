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
	// TODO Max Areshkau delete this reference afrer DnD had been added
//	VpeDnD getLocalDnD() {
//		return dnd;
//	}

	// TODO Max Areshkau delete this reference afrer DnD had been added
//	XPCOMObject getDropListener() {
//		return dropListener;
//	}

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

	// TODO Max Areshkau delete this reference afrer DnD had been added
//	void disposeCOMInterfaces() {
//		if (dropListener != null) {
//			dropListener.dispose();
//			dropListener = null;
//		}
//	}

	// TODO Max Areshkau delete this reference afrer DnD had been added
	// nsISupports implementation
//	int QueryInterface(int riid, int ppvObject) {
//		if (riid == 0 || ppvObject == 0) {
//			return XPCOM.NS_ERROR_NO_INTERFACE;
//		}
//		nsID guid = new nsID();
//		XPCOM.memmove(guid, riid, nsID.sizeof);
//		
//		if (MozillaDebug.printVisualNoInterface) {
//			System.out.println("DomEventListener - NO INTERFACE: " + guid.ToString());
//		}
//		XPCOM.memmove(ppvObject, new int[] {0}, 4);
//		return XPCOM.NS_ERROR_NO_INTERFACE;
//	}

	// TODO Max Areshkau delete this reference afrer DnD had been added
//	int AddRef() {
//		refCount++;
//		return refCount;
//	}
	        	
	// TODO Max Areshkau delete this reference afrer DnD had been added
//	int Release() {
//		refCount--;
//		if (refCount == 0) {
//			disposeCOMInterfaces();
//		}
//		return refCount;
//	}
	
	// TODO Max Areshkau delete this reference afrer DnD had been added
//	VpeDnD getDnD() {
//		int[] result = new int[] {0};
//		int rc = XPCOM.NS_GetComponentManager(result);
//		if (rc != XPCOM.NS_OK) {
//			VpePlugin.getPluginLog().logError("(DND) GetComponentManager error " + rc);
//		}
//		if (result[0] == 0) {
//			VpePlugin.getPluginLog().logError("(DND) GetComponentManager error NO_INTERFACE");
//		}
//		
//		nsIComponentManager componentManager = new nsIComponentManager(result[0]);
//		result[0] = 0;
//		rc = componentManager.CreateInstance(XPCOM.VPE_DND_CID, 0, VpeDnD.VPE_DND_IID, result);
//		if (rc != XPCOM.NS_OK) {
//			VpePlugin.getPluginLog().logError("(DND) Create instance error " + rc);
//		}
//		if (result[0] == 0) {
//			VpePlugin.getPluginLog().logError("(DND) Create instance error NO_INTERFACE");
//		}
//		componentManager.Release();
//
//		return new VpeDnD(result[0]);
//	}
	
	// TODO Max Areshkau delete this reference afrer DnD had been added
	// IDropListener
//	int CanDrag(int aDragEvent, int _retval, int aNode, int offset) {
//		XPCOM.memmove(_retval, new int[] {0}, 4);
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
//	}

	// TODO Max Areshkau delete this reference afrer DnD had been added
//	int CanDrop(int aDropEvent, int _retval, int aNode, int offset) {
//		XPCOM.memmove(_retval, new int[] {0}, 4);
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
//	}
	
	// TODO Max Areshkau delete this reference afrer DnD had been added
//	int Drop(int aDropEvent, int aNode, int offset) {
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
//	}
	
	// TODO Max Areshkau delete this reference afrer DnD had been added
//	int CanDropExternal(int aDropEvent, int aFlavor, int aTransData, int _retval, int aNode, int offset) {
//		XPCOM.memmove(_retval, new int[] {0}, 4);
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
//	}
	
	// TODO Max Areshkau delete this reference afrer DnD had been added
//	int DropExternal(int aDropEvent, int aFlavor, int aTransData, int aNode, int offset) {
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
////		return XPCOM.NS_OK;
//		return XPCOM.NS_ERROR_ABORT;
//	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#allowDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsIDragSession)
	 */
	@Override
	public boolean allowDrop(nsIDOMEvent event, nsIDragSession dragSession) {
		event.preventDefault();
		event.stopPropagation();
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#allowStartDrag(org.mozilla.interfaces.nsIDOMEvent)
	 */
	@Override
	public boolean allowStartDrag(nsIDOMEvent event) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onCopyOrDrag(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
	 */
	@Override
	public boolean onCopyOrDrag(nsIDOMEvent event, nsITransferable transferable) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onPasteOrDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
	 */
	@Override
	public boolean onPasteOrDrop(nsIDOMEvent event,	nsITransferable transferable) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
	 */
	@Override
	public nsISupports queryInterface(String arg0) {
		return Mozilla.queryInterface(this, arg0);
	}
}

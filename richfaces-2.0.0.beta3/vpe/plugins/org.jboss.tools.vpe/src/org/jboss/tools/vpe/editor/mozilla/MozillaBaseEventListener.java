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


import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.mozilla.browser.MozillaDebug;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.VpeDnD;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOM;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOMObject;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIClipboardDragDropHooks;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIComponentManager;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsID;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseEvent;

public class MozillaBaseEventListener {
	private XPCOMObject dropListener;
	private XPCOMObject dragDropHook;
	private VpeDnD dnd; 
	private int refCount = 0;

	
	public MozillaBaseEventListener() {
		createCOMInterfaces();
	}
	
	VpeDnD getLocalDnD() {
		return dnd;
	}

	XPCOMObject getDropListener() {
		return dropListener;
	}

	XPCOMObject getDragDropHook() {
		return dragDropHook;
	}

	void createCOMInterfaces() {
		// VpeDnD
		dropListener = new XPCOMObject(new int[]{2,0,0,4,4,3,6,5}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return CanDrag(args[0], args[1], args[2], args[3]);}
			public int method4(int[] args) {return CanDrop(args[0], args[1], args[2], args[3]);}
			public int method5(int[] args) {return Drop(args[0], args[1], args[2]);}
			public int method6(int[] args) {return CanDropExternal(args[0], args[1], args[2], args[3], args[4], args[5]);}
			public int method7(int[] args) {return DropExternal(args[0], args[1], args[2], args[3], args[4]);}
		};

		dnd = getDnD();
		
		// nsIClipboardDragDropHooks
		dragDropHook = new XPCOMObject(new int[]{2, 0, 0, 2, 3, 3, 3}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return AllowStartDrag(args[0], args[1]);}
			public int method4(int[] args) {return AllowDrop(args[0], args[1], args[2]);}
			public int method5(int[] args) {return OnCopyOrDrag(args[0], args[1], args[2]);}
			public int method6(int[] args) {return OnPasteOrDrop(args[0], args[1], args[2]);}
		};
	}

	void disposeCOMInterfaces() {
		if (dropListener != null) {
			dropListener.dispose();
			dropListener = null;
		}
		if (dragDropHook != null) {
			dragDropHook.dispose();
			dragDropHook = null;
		}
	}

	// nsISupports implementation
	int QueryInterface(int riid, int ppvObject) {
		if (riid == 0 || ppvObject == 0) {
			return XPCOM.NS_ERROR_NO_INTERFACE;
		}
		nsID guid = new nsID();
		XPCOM.memmove(guid, riid, nsID.sizeof);
		
		if (guid.Equals(nsIClipboardDragDropHooks.NS_ICLIPBOARDDRAGDROPHOOKS_IID)) {
			XPCOM.memmove(ppvObject, new int[] {dragDropHook.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		
		if (MozillaDebug.printVisualNoInterface) {
			System.out.println("DomEventListener - NO INTERFACE: " + guid.ToString());
		}
		XPCOM.memmove(ppvObject, new int[] {0}, 4);
		return XPCOM.NS_ERROR_NO_INTERFACE;
	}

	int AddRef() {
		refCount++;
		return refCount;
	}
	        	
	int Release() {
		refCount--;
		if (refCount == 0) {
			disposeCOMInterfaces();
		}
		return refCount;
	}
	
	VpeDnD getDnD() {
		int[] result = new int[] {0};
		int rc = XPCOM.NS_GetComponentManager(result);
		if (rc != XPCOM.NS_OK) {
			VpePlugin.getPluginLog().logError("(DND) GetComponentManager error " + rc);
		}
		if (result[0] == 0) {
			VpePlugin.getPluginLog().logError("(DND) GetComponentManager error NO_INTERFACE");
		}
		
		nsIComponentManager componentManager = new nsIComponentManager(result[0]);
		result[0] = 0;
		rc = componentManager.CreateInstance(XPCOM.VPE_DND_CID, 0, VpeDnD.VPE_DND_IID, result);
		if (rc != XPCOM.NS_OK) {
			VpePlugin.getPluginLog().logError("(DND) Create instance error " + rc);
		}
		if (result[0] == 0) {
			VpePlugin.getPluginLog().logError("(DND) Create instance error NO_INTERFACE");
		}
		componentManager.Release();

		return new VpeDnD(result[0]);
	}
	
	// IDropListener
	int CanDrag(int aDragEvent, int _retval, int aNode, int offset) {
		XPCOM.memmove(_retval, new int[] {0}, 4);
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}

	int CanDrop(int aDropEvent, int _retval, int aNode, int offset) {
		XPCOM.memmove(_retval, new int[] {0}, 4);
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}
	
	int Drop(int aDropEvent, int aNode, int offset) {
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}
	
	int CanDropExternal(int aDropEvent, int aFlavor, int aTransData, int _retval, int aNode, int offset) {
		XPCOM.memmove(_retval, new int[] {0}, 4);
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}
	
	int DropExternal(int aDropEvent, int aFlavor, int aTransData, int aNode, int offset) {
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
		return XPCOM.NS_ERROR_ABORT;
	}
	
	// nsIClipboardDragDropHooks implementation
	int AllowStartDrag(int aDOMEvent, int _retval) {
		XPCOM.memmove(_retval, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}

	int AllowDrop(int aDOMEvent, int aDragSession, int _retval) {
		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDOMEvent);
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();
		XPCOM.memmove(_retval, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}

	int OnCopyOrDrag(int aDOMEvent, int aTransferable, int _retval) {
		XPCOM.memmove(_retval, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}

	int OnPasteOrDrop(int aDOMEvent, int aTransferable, int _retval) {
		XPCOM.memmove(_retval, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}
}

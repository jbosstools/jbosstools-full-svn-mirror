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

import org.w3c.dom.Node;



import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.util.MozillaSupports;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;
import org.jboss.tools.vpe.mozilla.browser.MozillaDebug;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.VpeDnD;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOM;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOMObject;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIClipboardDragDropHooks;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIComponentManager;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIControllers;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsID;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMDocument;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMDragListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMElement;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEventListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEventTarget;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMFocusListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMKeyEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMKeyListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseMotionListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMutationEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMutationListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNSEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNode;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDragSession;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelection;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelectionListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISupports;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsString;

class MozillaDomEventListener {
	private XPCOMObject supports;
	private XPCOMObject domEventListener;
	private XPCOMObject domMutationListener;
	private XPCOMObject selectionListener;
	private XPCOMObject mouseListener;
	private XPCOMObject mouseMotionListener;
	private XPCOMObject focusListener;
	private XPCOMObject keyListener;
	private XPCOMObject dragListener;
	private XPCOMObject resizeListener;
	private XPCOMObject dropListener;
	private XPCOMObject dragDropHook;
	private int refCount = 0;

	private MozillaBrowser mozillaBrowser;
	private VpeDnD dnd; 
	private EditorDomEventListener editorDomEventListener;
	
	public MozillaDomEventListener() {
		createCOMInterfaces();
	}

	void createCOMInterfaces() {
		// nsISupports
		supports = new XPCOMObject(new int[]{2, 0, 0}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
		};
		
		// nsIDOMEventListener
		domEventListener = new XPCOMObject(new int[]{2, 0, 0, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
		};
		
		// nsIDOMMutationListener
		domMutationListener = new XPCOMObject(new int[]{2, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
			public int method4(int[] args) {return SubtreeModified(args[0]);}
			public int method5(int[] args) {return NodeInserted(args[0]);}
			public int method6(int[] args) {return NodeRemoved(args[0]);}
			public int method7(int[] args) {return NodeRemovedFromDocument(args[0]);}
			public int method8(int[] args) {return NodeInsertedIntoDocument(args[0]);}
			public int method9(int[] args) {return AttrModified(args[0]);}
			public int method10(int[] args) {return CharacterDataModified(args[0]);}
		};
		
		// nsISelectionListener
		selectionListener = new XPCOMObject(new int[]{2, 0, 0, 3}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return NotifySelectionChanged(args[0], args[1], args[2] & 0xffff);}
		};
		
		// nsIDOMMouseListener
		mouseListener = new XPCOMObject(new int[]{2, 0, 0, 1, 1, 1, 1, 1, 1, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
			public int method4(int[] args) {return MouseDown(args[0]);}
			public int method5(int[] args) {return MouseUp(args[0]);}
			public int method6(int[] args) {return MouseClick(args[0]);}
			public int method7(int[] args) {return MouseDblClick(args[0]);}
			public int method8(int[] args) {return MouseOver(args[0]);}
			public int method9(int[] args) {return MouseOut(args[0]);}
		};
		
		// nsIDOMMouseMotionListener
		mouseMotionListener = new XPCOMObject(new int[]{2, 0, 0, 1, 1, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
			public int method4(int[] args) {return MouseMove(args[0]);}
			public int method5(int[] args) {return DragMove(args[0]);}
		};
		
		// nsIDOMFocusListener
		focusListener = new XPCOMObject(new int[]{2, 0, 0, 1, 1, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
			public int method4(int[] args) {return Focus(args[0]);}
			public int method5(int[] args) {return Blur(args[0]);}
		};
		
		// nsIDOMKeyListener
		keyListener = new XPCOMObject(new int[]{2, 0, 0, 1, 1, 1, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
			public int method4(int[] args) {return KeyDown(args[0]);}
			public int method5(int[] args) {return KeyUp(args[0]);}
			public int method6(int[] args) {return KeyPress(args[0]);}
		};
		
		// nsIDOMDragListener
		dragListener = new XPCOMObject(new int[]{2, 0, 0, 1, 1, 1, 1, 1, 1}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return HandleEvent(args[0]);}
			public int method4(int[] args) {return DragEnter(args[0]);}
			public int method5(int[] args) {return DragOver(args[0]);}
			public int method6(int[] args) {return DragExit(args[0]);}
			public int method7(int[] args) {return DragDrop(args[0]);}
			public int method8(int[] args) {return DragGesture(args[0]);}
		};

		// IVpeResizeListener
		resizeListener = new XPCOMObject(new int[]{2,0,0,6}) {
			public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
			public int method1(int[] args) {return AddRef();}
			public int method2(int[] args) {return Release();}
			public int method3(int[] args) {return EndResizing(args[0], args[1], args[2], args[3], args[4], args[5]);}
		};
		
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
		if (supports != null) {
			supports.dispose();
			supports = null;
		}	
		if (domEventListener != null) {
			domEventListener.dispose();
			domEventListener = null;	
		}
		if (domMutationListener != null) {
			domMutationListener.dispose();
			domMutationListener = null;	
		}
		if (selectionListener != null) {
			selectionListener.dispose();
			selectionListener = null;	
		}
		if (mouseListener != null) {
			mouseListener.dispose();
			mouseListener = null;	
		}
		if (mouseMotionListener != null) {
			mouseMotionListener.dispose();
			mouseMotionListener = null;	
		}
		if (focusListener != null) {
			focusListener.dispose();
			focusListener = null;	
		}
		if (keyListener != null) {
			keyListener.dispose();
			keyListener = null;	
		}
		if (dragListener != null) {
			dragListener.dispose();
			dragListener = null;	
		}
		
		if (resizeListener != null) {
			resizeListener.dispose();
			resizeListener = null;
		}
		
		if (dropListener != null) {
			dropListener.dispose();
			dropListener = null;
		}
		
		if (dragDropHook != null) {
			dragDropHook.dispose();
			dragDropHook = null;
		}
	}

	int getAddress() {
		return domEventListener.getAddress();
	}

	XPCOMObject getDomEventListener() {
		return domEventListener;
	}

	XPCOMObject getSelectionListener() {
		return selectionListener;
	}

	XPCOMObject getMouseListener() {
		return mouseListener;
	}

	XPCOMObject getMouseMotionListener() {
		return mouseMotionListener;
	}

	XPCOMObject getFocusListener() {
		return focusListener;
	}

	XPCOMObject getKeyListener() {
		return keyListener;
	}

	XPCOMObject getDragListener() {
		return dragListener;
	}

	XPCOMObject getDropListener() {
		return dropListener;
	}
	
	VpeDnD getLocalDnD() {
		return dnd;
	}

	XPCOMObject getDragDropHook() {
		return dragDropHook;
	}
	
	void setEditorDomEventListener(EditorDomEventListener listener) {
		editorDomEventListener = listener;
	}

	// nsISupports implementation
	int QueryInterface(int riid, int ppvObject) {
		if (riid == 0 || ppvObject == 0) {
			return XPCOM.NS_ERROR_NO_INTERFACE;
		}
		nsID guid = new nsID();
		XPCOM.memmove(guid, riid, nsID.sizeof);
		
		if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
			XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}
		if (guid.Equals(nsIDOMEventListener.NS_IDOMEVENTLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {domEventListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsIDOMMutationListener.NS_IDOMMUTATIONLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {domMutationListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsISelectionListener.NS_ISELECTIONLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {selectionListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsIDOMMouseListener.NS_IDOMMOUSELISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {mouseListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsIDOMMouseMotionListener.NS_IDOMMOUSEMOTIONLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {mouseMotionListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsIDOMFocusListener.NS_IDOMFOCUSLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {focusListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsIDOMKeyListener.NS_IDOMKEYLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {keyListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}	
		if (guid.Equals(nsIDOMDragListener.NS_IDOMDRAGLISTENER_IID)) {
			XPCOM.memmove(ppvObject, new int[] {dragListener.getAddress()}, 4);
			AddRef();
			return XPCOM.NS_OK;
		}

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

	// nsIDOMEventListener implementation
	int HandleEvent(int aEvent) {
		nsIDOMEvent domEvent = new nsIDOMEvent(aEvent);
		String type = domEvent.getType();
		Node node = domEvent.getTargetNode();
		return XPCOM.NS_OK;     	
	}

	// nsIDOMMutationListener implementation
	int SubtreeModified(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.subtreeModified(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int NodeInserted(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.nodeInserted(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int NodeRemoved(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.nodeRemoved(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int NodeRemovedFromDocument(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.nodeRemovedFromDocument(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int NodeInsertedIntoDocument(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.nodeInsertedIntoDocument(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int AttrModified(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.attrModified(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int CharacterDataModified(int aMutationEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.characterDataModified(new nsIDOMMutationEvent(aMutationEvent));
		}
		return XPCOM.NS_OK;     	
	}

	// nsISelectionListener implementation
	public int NotifySelectionChanged(int doc, int sel, int reason) {
		if (editorDomEventListener != null) {
			editorDomEventListener.notifySelectionChanged(new nsIDOMDocument(doc), new nsISelection(sel), reason);
		}
		return XPCOM.NS_OK;     	
	}

	// nsIDOMMouseListener implementation
	public int MouseDown(int aMouseEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.mouseDown(new nsIDOMMouseEvent(aMouseEvent));
		}
		return XPCOM.NS_OK;     	
	}

	public int MouseUp(int aMouseEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.mouseUp(new nsIDOMMouseEvent(aMouseEvent));
		}
		return XPCOM.NS_OK;     	
	}

	public int MouseClick(int aMouseEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.mouseClick(new nsIDOMMouseEvent(aMouseEvent));
		}
		return XPCOM.NS_OK;     	
	}

	public int MouseDblClick(int aMouseEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.mouseDblClick(new nsIDOMMouseEvent(aMouseEvent));
		}
		return XPCOM.NS_OK;     	
	}

	public int MouseOver(int aMouseEvent) {
		return XPCOM.NS_OK;     	
	}

	public int MouseOut(int aMouseEvent) {
		return XPCOM.NS_OK;     	
	}

	// nsIDOMMouseMotionListener implementation
	int MouseMove(int aMouseEvent) {
		if (editorDomEventListener != null) {
			editorDomEventListener.mouseMove(new nsIDOMMouseEvent(aMouseEvent));
		}
		return XPCOM.NS_OK;     	
	}

	int DragMove(int aMouseEvent) {
		return XPCOM.NS_OK;     	
	}

	// nsIDOMFocusListener implementation
	int Focus(int aEvent) {
		return XPCOM.NS_OK;     	
	}

	int Blur(int aEvent) {
		return XPCOM.NS_OK;     	
	}

	// nsIDOMKeyListener implementation
	int KeyDown(int aEvent) {
		return XPCOM.NS_OK;     	
	}

	int KeyUp(int aEvent) {
		return XPCOM.NS_OK;     	
	}

	int KeyPress(int aEvent) {
		if (editorDomEventListener != null) {
			int aKeyEvent = nsISupports.queryInterface(aEvent, nsIDOMKeyEvent.NS_IDOMKEYEVENT_IID);
			editorDomEventListener.keyPress(new nsIDOMKeyEvent(aKeyEvent));
		}
		return XPCOM.NS_OK;     	
	}

	// nsIDOMDragListener implementation
	int DragEnter(int aEvent) {
//		if (editorDomEventListener != null) {
//			nsIDOMEvent event = new nsIDOMEvent(aEvent);
//			editorDomEventListener.dragEnter(event);
//		}
		return XPCOM.NS_OK;     	
	}

	int DragOver(int aEvent) {
		nsIDOMEvent event = new nsIDOMEvent(aEvent);
		event.stopPropagation();
		event.preventDefault();
//		if (editorDomEventListener != null) {
//			nsIDOMEvent event = new nsIDOMEvent(aEvent);
//			editorDomEventListener.dragOver(event);
//		}
		return XPCOM.NS_OK;     	
	}

	int DragExit(int aEvent) {
//		if (editorDomEventListener != null) {
//			nsIDOMEvent event = new nsIDOMEvent(aEvent);
//			editorDomEventListener.dragExit(event);
//		}
		return XPCOM.NS_OK;     	
	}

	int DragDrop(int aEvent) {
		nsIDOMEvent event = new nsIDOMEvent(aEvent);
		event.stopPropagation();
		event.preventDefault();
//		if (editorDomEventListener != null) {
//			nsIDOMEvent event = new nsIDOMEvent(aEvent);
//			editorDomEventListener.drop(event);
//		}
		return XPCOM.NS_OK;     	
	}

	int DragGesture(int aEvent) {
//		nsIDOMEvent event = new nsIDOMEvent(aEvent);
//		event.stopPropagation();
//		event.preventDefault();
		return XPCOM.NS_OK;     	
	}
	
	// IVpeResizeListener
	int EndResizing(int usedHandle, int newTop, int newLeft, int newWidth, int newHeight, int aResizedObject) {
		nsIDOMElement element = new nsIDOMElement(aResizedObject);
		return XPCOM.NS_OK;
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
	
	// IVpeDropListener
	int CanDrag(int aDragEvent, int _retval, int aNode, int offset) {
		int canDrag = 0;
		
		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDragEvent);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			boolean canDragFlag = editorDomEventListener.canInnerDrag(mouseEvent);
			canDrag = canDragFlag ? 1 : 0;
		}
		XPCOM.memmove(_retval, new int[] {canDrag}, 4);
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}

	int CanDrop(int aDropEvent, int _retval, int aNode, int offset) {
		int canDrop = 0;
		int aCaretParent = 0;
		int caretOffset = 0;

		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//			nsIDOMNode visualNode = mouseEvent.getTargetNode();
			MozillaDropInfo info = editorDomEventListener.canInnerDrop(mouseEvent);
			if (info != null && info.canDrop()) {
				canDrop = 1;
				Node caretParent = info.getCaretParent();
				if (caretParent != null) {
					MozillaSupports.addRef(caretParent);
					aCaretParent = MozillaSupports.getAddress(caretParent);
					caretOffset = info.getCaretOffset();
				}
			}
		}
		XPCOM.memmove(_retval, new int[] {canDrop}, 4);
		XPCOM.memmove(aNode, new int[] {aCaretParent}, 4);
		XPCOM.memmove(offset, new int[] {caretOffset}, 4);
		return XPCOM.NS_OK;
	}
	
	int Drop(int aDropEvent, int aNode, int offset) {
		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			editorDomEventListener.innerDrop(mouseEvent);
		}
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}
	
	int CanDropExternal(int aDropEvent, int aFlavor, int aTransData, int _retval, int aNode, int offset) {
		int canDrop = 0;
		int aCaretParent = 0;
		int caretOffset = 0;

		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);
		
		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			String flavor = new nsString(aFlavor).toString();
			String data = new nsString(aTransData).toString();
			MozillaDropInfo info = editorDomEventListener.canExternalDrop(mouseEvent, flavor, data);
			if (info != null) {
				canDrop = info.canDrop() ? 1 : 0;
				Node caretParent = info.getCaretParent();
				if (caretParent != null) {
					MozillaSupports.addRef(caretParent);
					aCaretParent = MozillaSupports.getAddress(caretParent);
					caretOffset = info.getCaretOffset();
				}
			}
		}
		XPCOM.memmove(_retval, new int[] {canDrop}, 4);
		XPCOM.memmove(aNode, new int[] {aCaretParent}, 4);
		XPCOM.memmove(offset, new int[] {caretOffset}, 4);
		return XPCOM.NS_OK;
	}
	
	int DropExternal(int aDropEvent, int aFlavor, int aTransData, int aNode, int offset) {
		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			String flavor = new nsString(aFlavor).toString();
			String data = new nsString(aTransData).toString();
			editorDomEventListener.externalDrop(mouseEvent, flavor, data);
		}
		XPCOM.memmove(aNode, new int[] {0}, 4);
		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
		return XPCOM.NS_ERROR_ABORT;
	}
	
	boolean isXulElement(nsIDOMMouseEvent mouseEvent) {
		int aNsEvent = mouseEvent.queryInterface(nsIDOMNSEvent.NS_IDOMNSEVENT_IID);
		nsIDOMNSEvent nsEvent = new nsIDOMNSEvent(aNsEvent);
		nsIDOMEventTarget target = nsEvent.getTmpRealOriginalTarget();	
		int aDragNode = target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
		nsIDOMNode originalNode = nsIDOMNode.getNodeAtAddress(aDragNode);
		String prefix = originalNode.getPrefix();
		boolean isXul = "XUL".equalsIgnoreCase(prefix);
		target.Release();
		nsEvent.Release();
		return isXul;
	}
	
	// nsIClipboardDragDropHooks implementation
	int AllowStartDrag(int aDOMEvent, int _retval) {
		XPCOM.memmove(_retval, new int[] {1}, 4);
		return XPCOM.NS_OK;
	}

	int AllowDrop(int aDOMEvent, int aDragSession, int _retval) {
		int canDrop = 0;
		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDOMEvent);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			nsIDragSession dragSession = new nsIDragSession(aDragSession);
			if (dragSession.isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
				MozillaDropInfo info = editorDomEventListener.canExternalDrop(mouseEvent, VpeController.MODEL_FLAVOR, "");
//				if (info != null) {
//					canDrop = info.canDrop() ? 1 : 0;
//				}
				if (info != null && info.canDrop()) {
					String nodeName = info.getCaretParent().getNodeName();
					if ("input".equalsIgnoreCase(nodeName)) {
						canDrop = 1;
					}
				}
			}
		}
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();
		XPCOM.memmove(_retval, new int[] {canDrop}, 4);
		return XPCOM.NS_OK;
	}

	int OnCopyOrDrag(int aDOMEvent, int aTransferable, int _retval) {
		XPCOM.memmove(_retval, new int[] {1}, 4);
		return XPCOM.NS_OK;
	}

	int OnPasteOrDrop(int aDOMEvent, int aTransferable, int _retval) {
		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDOMEvent);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			nsIDragSession dragSession = mozillaBrowser.getCurrentDragSession();
			if (dragSession.isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
				editorDomEventListener.onPasteOrDrop(mouseEvent, VpeController.MODEL_FLAVOR, "");
			}
		}
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();
		XPCOM.memmove(_retval, new int[] {0}, 4);
		return XPCOM.NS_OK;
	}
	
	void setMozillaBrowser(MozillaBrowser mozillaBrowser) {
		this.mozillaBrowser = mozillaBrowser;
	}
}

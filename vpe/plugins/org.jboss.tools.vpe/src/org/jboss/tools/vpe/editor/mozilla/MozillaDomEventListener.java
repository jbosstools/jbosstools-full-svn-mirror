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

import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIClipboardDragDropHooks;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragSession;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionListener;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;


class MozillaDomEventListener implements nsIClipboardDragDropHooks, 
		nsIDOMEventListener, nsISelectionListener {
	// TODO Max Areshkau add DnD
//	private XPCOMObject dropListener;

	private XulRunnerEditor visualEditor;
	// TODO Max Areshkau add DnD
//	private VpeDnD dnd; 
	private EditorDomEventListener editorDomEventListener;
	
	//possible events
	public static final String MOUSEMOVEEVENTTYPE="mousemove";
	
	public static final String MOUSEDOWNEVENTTYPE="mousedown";
	
	public static final String MOUSEUPEVENTTYPE="mouseup";
	
	public static final String CLICKEVENTTYPE="click";
	
	public static final String KEYPRESS="keypress";
	
	public static final String DBLCLICK="dblclick";
	
	public static final String CONTEXTMENUEVENTTYPE="contextmenu";
	
	public static final String DRAGGESTUREEVENT = "draggesture";
	
	public static final String DRAGOVEREVENT = "dragover";
	
	public static final String DRAGDROPEVENT = "dragdrop";
	
	public static final String DRAGENTEREVENT = "dragenter";
	
	public static final String DRAGEXITEVENT = "dragexit";
	
	public MozillaDomEventListener() {
		createCOMInterfaces();
	}

	void createCOMInterfaces() {
		
		// TODO Max Areshkau add DnD
		// VpeDnD
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
//	
//		dnd = getDnD();
	}

	void disposeCOMInterfaces() {
		// TODO Max Areshakau add DnD
//		if (dropListener != null) {
//			dropListener.dispose();
//			dropListener = null;
//		}
	}

	// TODO Max Areshkau add DnD support
//	XPCOMObject getDropListener() {
//		return dropListener;
//	}

	// TODO Max Areshkau add DnD support
//	VpeDnD getLocalDnD() {
//		return dnd;
//	}

	void setEditorDomEventListener(EditorDomEventListener listener) {
		editorDomEventListener = listener;
	}

	
	/**
	 * Returns event handler
	 * @return
	 */
	private EditorDomEventListener  getEditorDomEventListener(){
		
		return editorDomEventListener;
	}
	
	// TODO Max Areshkau add DnD support
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
//	
//	// IVpeDropListener
	//generates when drag event exist
//	int CanDrag(int aDragEvent, int _retval, int aNode, int offset) {
//		int canDrag = 0;
//		
//		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDragEvent);
//
//		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//			boolean canDragFlag = editorDomEventListener.canInnerDrag(mouseEvent);
//			canDrag = canDragFlag ? 1 : 0;
//		}
//		XPCOM.memmove(_retval, new int[] {canDrag}, 4);
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
//	}
//
//	int CanDrop(int aDropEvent, int _retval, int aNode, int offset) {
//		int canDrop = 0;
//		int aCaretParent = 0;
//		int caretOffset = 0;
//
//		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);
//
//		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
////			nsIDOMNode visualNode = mouseEvent.getTargetNode();
//			MozillaDropInfo info = editorDomEventListener.canInnerDrop(mouseEvent);
//			if (info != null && info.canDrop()) {
//				canDrop = 1;
//				Node caretParent = info.getCaretParent();
//				if (caretParent != null) {
//					MozillaSupports.addRef(caretParent);
//					aCaretParent = MozillaSupports.getAddress(caretParent);
//					caretOffset = info.getCaretOffset();
//				}
//			}
//		}
//		XPCOM.memmove(_retval, new int[] {canDrop}, 4);
//		XPCOM.memmove(aNode, new int[] {aCaretParent}, 4);
//		XPCOM.memmove(offset, new int[] {caretOffset}, 4);
//		return XPCOM.NS_OK;
//	}
//	
//	int Drop(int aDropEvent, int aNode, int offset) {
//		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);
//
//		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//			editorDomEventListener.innerDrop(mouseEvent);
//		}
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
//		return XPCOM.NS_OK;
//	}
//	generates on external drop event
//	int CanDropExternal(int aDropEvent, int aFlavor, int aTransData, int _retval, int aNode, int offset) {
//		int canDrop = 0;
//		int aCaretParent = 0;
//		int caretOffset = 0;
//
//		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);
//		
//		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//			String flavor = new nsString(aFlavor).toString();
//			String data = new nsString(aTransData).toString();
//			MozillaDropInfo info = editorDomEventListener.canExternalDrop(mouseEvent, flavor, data);
//			if (info != null) {
//				canDrop = info.canDrop() ? 1 : 0;
//				Node caretParent = info.getCaretParent();
//				if (caretParent != null) {
//					MozillaSupports.addRef(caretParent);
//					aCaretParent = MozillaSupports.getAddress(caretParent);
//					caretOffset = info.getCaretOffset();
//				}
//			}
//		}
//		XPCOM.memmove(_retval, new int[] {canDrop}, 4);
//		XPCOM.memmove(aNode, new int[] {aCaretParent}, 4);
//		XPCOM.memmove(offset, new int[] {caretOffset}, 4);
//		return XPCOM.NS_OK;
//	}
//  	
//	int DropExternal(int aDropEvent, int aFlavor, int aTransData, int aNode, int offset) {
//		nsIDOMMouseEvent mouseEvent = nsIDOMEvent.queryMouseEvent(aDropEvent);
//
//		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//			String flavor = new nsString(aFlavor).toString();
//			String data = new nsString(aTransData).toString();
//			editorDomEventListener.externalDrop(mouseEvent, flavor, data);
//		}
//		XPCOM.memmove(aNode, new int[] {0}, 4);
//		XPCOM.memmove(offset, new int[] {0}, 4);
////		return XPCOM.NS_OK;
//		return XPCOM.NS_ERROR_ABORT;
//	}
	
	boolean isXulElement(nsIDOMMouseEvent mouseEvent) {
		// TODO Sergey Vasilyev figure out with getTmpRealOriginalTarget
//		nsIDOMNSEvent nsEvent = (nsIDOMNSEvent)mouseEvent.queryInterface(nsIDOMNSEvent.NS_IDOMNSEVENT_IID);
//		nsIDOMEventTarget target = nsEvent.getTmpRealOriginalTarget();	
//		int aDragNode = target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
//		nsIDOMNode originalNode = nsIDOMNode.getNodeAtAddress(aDragNode);
//		String prefix = originalNode.getPrefix();
//		boolean isXul = "XUL".equalsIgnoreCase(prefix);
//		target.Release();
//		nsEvent.Release();
		return false;
	}
	


	void setVisualEditor(XulRunnerEditor visualEditor) {
		this.visualEditor = visualEditor;
	}

	/*-------------------------------------------*\
	 *  nsIClipboardDragDropHooks implementation *
	 *-------------------------------------------*/
	
	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#allowDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsIDragSession)
	 */
	// checks is drop allowed
	@Deprecated 
	// functionality was moved to VpeDnD.dragOver
	public boolean allowDrop(nsIDOMEvent event, nsIDragSession dragSession) {
		boolean canDrop = false;
//		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
//
//		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//			if (dragSession.isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
//				MozillaDropInfo info = editorDomEventListener.canExternalDrop(mouseEvent, VpeController.MODEL_FLAVOR, "");
//				if (info != null && info.canDrop()) {
//					// TODO Sergey Vasilyev figures out with this drop
//					String nodeName = info.getCaretParent().getNodeName();
//					if ("input".equalsIgnoreCase(nodeName)) {
//						canDrop = true;
//					}
//				}
//			}
//		}
//		mouseEvent.preventDefault();
//		mouseEvent.stopPropagation();
//		
		return canDrop;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#allowStartDrag(org.mozilla.interfaces.nsIDOMEvent)
	 */
	public boolean allowStartDrag(nsIDOMEvent arg0) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onCopyOrDrag(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
	 */
	public boolean onCopyOrDrag(nsIDOMEvent arg0, nsITransferable arg1) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onPasteOrDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
	 */
	public boolean onPasteOrDrop(nsIDOMEvent event,	nsITransferable transferable) {
		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent)event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);

		if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
			nsIDragSession dragSession = visualEditor.getCurrentDragSession();
			if (dragSession.isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
				editorDomEventListener.onPasteOrDrop(mouseEvent, VpeController.MODEL_FLAVOR, "");
			}
		}
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
	 */
	public nsISupports queryInterface(String arg0) {
		return Mozilla.queryInterface(this, arg0);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsIDOMEventListener#handleEvent(org.mozilla.interfaces.nsIDOMEvent)
	 */
	public void handleEvent(nsIDOMEvent domEvent) {	
		try{
		if(getEditorDomEventListener()==null){
			
			return;
		} else if(MOUSEMOVEEVENTTYPE.equals(domEvent.getType())) {
			
			nsIDOMMouseEvent mouseEvent;
			mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			getEditorDomEventListener().mouseMove(mouseEvent);
		} else if(MOUSEDOWNEVENTTYPE.equals(domEvent.getType())) {
			
			nsIDOMMouseEvent mouseEvent;
			mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			getEditorDomEventListener().mouseDown(mouseEvent);

		} else if(MOUSEUPEVENTTYPE.equals(domEvent.getType())) {
			
			nsIDOMMouseEvent mouseEvent;
			mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			getEditorDomEventListener().mouseUp(mouseEvent);
		} else if(CLICKEVENTTYPE.equals(domEvent.getType())) {
			
			nsIDOMMouseEvent mouseEvent;
			mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			getEditorDomEventListener().mouseClick(mouseEvent);
		} else if(DBLCLICK.equals(domEvent.getType())) {
			
			nsIDOMMouseEvent mouseEvent;
			mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			getEditorDomEventListener().mouseDblClick(mouseEvent);
		} else if(KEYPRESS.equals(domEvent.getType())) {
			
			nsIDOMKeyEvent keyEvent = (nsIDOMKeyEvent) domEvent.queryInterface(nsIDOMKeyEvent.NS_IDOMKEYEVENT_IID);
			getEditorDomEventListener().keyPress(keyEvent);
		} else if(CONTEXTMENUEVENTTYPE.equals(domEvent.getType())) {
			//first param are null 0, because this not used in event handler
			getEditorDomEventListener().onShowContextMenu(0, domEvent, (nsIDOMNode) domEvent.getTarget().queryInterface(nsIDOMNode.NS_IDOMNODE_IID));
		} else if(DRAGGESTUREEVENT.equals(domEvent.getType())) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
	
			if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
				boolean canDragFlag = editorDomEventListener.canInnerDrag(mouseEvent);
				//start drag sessionvpe-element
				if(canDragFlag) {
					
					getEditorDomEventListener().startDragSession(domEvent);
					}
			}
		} else if(DRAGDROPEVENT.equals(domEvent.getType())) {
			// calls when drop event occure
			getEditorDomEventListener().dragDrop(domEvent);
			domEvent.stopPropagation();
			domEvent.preventDefault();
		} else if(DRAGENTEREVENT.equals(domEvent.getType())) {
			//just ignore this event
		} else if(DRAGEXITEVENT.equals(domEvent.getType())) {
			//just ignore this event
		} else if(DRAGOVEREVENT.equals(domEvent.getType())) {
			getEditorDomEventListener().dragOver(domEvent);	
		} 
		
		getEditorDomEventListener().onRefresh();
		//not using default mozilla event handlers
		}catch(Throwable th) {
			//TODO Max Areshkau remove when all will be adjusted
			th.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsISelectionListener#notifySelectionChanged(org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsISelection, short)
	 */
	public void notifySelectionChanged(nsIDOMDocument domDocument, nsISelection selection, short reason) {
		if (editorDomEventListener != null) {
			editorDomEventListener.notifySelectionChanged(domDocument, selection, reason);
		}
	}
}

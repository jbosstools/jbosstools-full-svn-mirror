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
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
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


class MozillaDomEventListener implements nsIDOMEventListener, nsISelectionListener {

	private XulRunnerEditor visualEditor;

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
				
				if(getEditorDomEventListener()!=null) {
		
					getEditorDomEventListener().dragGesture(domEvent);
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
			if(getEditorDomEventListener()!=null) {
				getEditorDomEventListener().onRefresh();
			}

		}catch(Throwable th) {

			VpePlugin.getPluginLog().logError("Event Handling Error", th);
			throw new RuntimeException(th);
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

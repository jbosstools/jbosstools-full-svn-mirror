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
	public static final String MOUSEMOVEEVENTTYPE="mousemove"; //$NON-NLS-1$
	
	public static final String MOUSEDOWNEVENTTYPE="mousedown"; //$NON-NLS-1$
	
	public static final String MOUSEUPEVENTTYPE="mouseup"; //$NON-NLS-1$
	
	public static final String CLICKEVENTTYPE="click"; //$NON-NLS-1$
	
	public static final String KEYPRESS="keypress"; //$NON-NLS-1$
	
	public static final String DBLCLICK="dblclick"; //$NON-NLS-1$
	
	public static final String CONTEXTMENUEVENTTYPE="contextmenu"; //$NON-NLS-1$
	
	public static final String DRAGGESTUREEVENT = "draggesture"; //$NON-NLS-1$
	
	public static final String DRAGOVEREVENT = "dragover"; //$NON-NLS-1$
	
	public static final String DRAGDROPEVENT = "dragdrop"; //$NON-NLS-1$
	
	public static final String DRAGENTEREVENT = "dragenter"; //$NON-NLS-1$
	
	public static final String DRAGEXITEVENT = "dragexit"; //$NON-NLS-1$
	
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
				editorDomEventListener.onPasteOrDrop(mouseEvent, VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
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

		if(getEditorDomEventListener() != null) {
			final String eventType = domEvent.getType();
			if(MOUSEMOVEEVENTTYPE.equals(eventType)) {			
				nsIDOMMouseEvent mouseEvent;
				mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
				getEditorDomEventListener().mouseMove(mouseEvent);
			} else if(MOUSEDOWNEVENTTYPE.equals(eventType)) {
				nsIDOMMouseEvent mouseEvent;
				mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
				getEditorDomEventListener().mouseDown(mouseEvent);
			} else if(MOUSEUPEVENTTYPE.equals(eventType)) {
				nsIDOMMouseEvent mouseEvent;
				mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
				getEditorDomEventListener().mouseUp(mouseEvent);
			} else if(CLICKEVENTTYPE.equals(eventType)) {
				nsIDOMMouseEvent mouseEvent;
				mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
				getEditorDomEventListener().mouseClick(mouseEvent);
			} else if(DBLCLICK.equals(eventType)) {
				nsIDOMMouseEvent mouseEvent;
				mouseEvent = (nsIDOMMouseEvent) domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
				getEditorDomEventListener().mouseDblClick(mouseEvent);
			} else if(KEYPRESS.equals(eventType)) {
				nsIDOMKeyEvent keyEvent = (nsIDOMKeyEvent) domEvent.queryInterface(nsIDOMKeyEvent.NS_IDOMKEYEVENT_IID);
				getEditorDomEventListener().keyPress(keyEvent);
			} else if(CONTEXTMENUEVENTTYPE.equals(eventType)) {			
				//first param are null 0, because this not used in event handler
				getEditorDomEventListener().onShowContextMenu(0, domEvent, (nsIDOMNode) domEvent.getTarget().queryInterface(nsIDOMNode.NS_IDOMNODE_IID));
			} else if(DRAGGESTUREEVENT.equals(eventType)) {
				if(getEditorDomEventListener()!=null) {
					getEditorDomEventListener().dragGesture(domEvent);
				}
			} else if(DRAGDROPEVENT.equals(eventType)) {
				// calls when drop event occure		 
				getEditorDomEventListener().dragDrop(domEvent);
				domEvent.stopPropagation();
				domEvent.preventDefault();
			} else if(DRAGENTEREVENT.equals(eventType)) {
				//just ignore this event
			} else if(DRAGEXITEVENT.equals(eventType)) {
				//just ignore this event
			} else if(DRAGOVEREVENT.equals(eventType)) {			
				getEditorDomEventListener().dragOver(domEvent);
			}

			getEditorDomEventListener().onRefresh();
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

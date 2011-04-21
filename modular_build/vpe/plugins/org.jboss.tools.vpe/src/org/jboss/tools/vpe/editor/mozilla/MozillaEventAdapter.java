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

import javax.swing.event.EventListenerList;

import org.jboss.tools.vpe.editor.mozilla.listener.MozillaContextMenuListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaDndListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaKeyListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaMouseListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaSelectionListener;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionListener;
import org.mozilla.interfaces.nsISelectionPrivate;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;

/**
 * Listens to events from Mozilla and transfers them to
 * {@code org.jboss.tools.vpe.editor.mozilla.listener.*Listener}'s.
 * 
 * @author Yahor Radtsevich (yradtsevich) and others
 */
public class MozillaEventAdapter implements nsIDOMEventListener, nsISelectionListener {
	public MozillaEventAdapter(){}

	//possible events
	private static final String MOZAFTERPAINT = "MozAfterPaint"; //$NON-NLS-1$
	private static final String SCROLL = "scroll"; //$NON-NLS-1$
	private static final String MOUSEMOVEEVENTTYPE="mousemove"; //$NON-NLS-1$
	private static final String MOUSEDOWNEVENTTYPE="mousedown"; //$NON-NLS-1$
	private static final String MOUSEUPEVENTTYPE="mouseup"; //$NON-NLS-1$
	private static final String CLICKEVENTTYPE="click"; //$NON-NLS-1$
	private static final String KEYPRESS="keypress"; //$NON-NLS-1$
	private static final String DBLCLICK="dblclick"; //$NON-NLS-1$
	private static final String CONTEXTMENUEVENTTYPE="contextmenu"; //$NON-NLS-1$
	private static final String DRAGGESTUREEVENT = "draggesture"; //$NON-NLS-1$
	private static final String DRAGOVEREVENT = "dragover"; //$NON-NLS-1$
	private static final String DRAGDROPEVENT = "dragdrop"; //$NON-NLS-1$
	private static final String DRAGENTEREVENT = "dragenter"; //$NON-NLS-1$
	private static final String DRAGEXITEVENT = "dragexit"; //$NON-NLS-1$

	private EventListenerList listeners = new EventListenerList();
	private nsIDOMEventTarget window;
	private nsIDOMEventTarget document;
	private nsIDOMEventTarget contentArea;
	private nsISelectionPrivate selectionPrivate;
	private boolean attached = false;
	// this field is never used	
	// private XulRunnerEditor visualEditor;

	/**
	 * Attach this instance to the specified Mozilla window and content area
	 * event target.
	 * 
	 * @throws IllegalStateException if the instance is attached twice.
	 */
	public void attach(nsIDOMWindow domWindow, nsIDOMEventTarget contentArea) {
		if (attached) {
			new IllegalStateException("Instance of the " //$NON-NLS-1$
					+ "MozillaMouseListener cannot be attached twice.");//$NON-NLS-1$
		}
		attached = true;
		
		this.window = (nsIDOMEventTarget) domWindow
				.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
		this.document = (nsIDOMEventTarget) domWindow.getDocument()
				.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
		this.contentArea = contentArea;
		
		if (contentArea != null) {
			contentArea.addEventListener(MozillaEventAdapter.CLICKEVENTTYPE, this, false); 
			contentArea.addEventListener(MozillaEventAdapter.MOUSEDOWNEVENTTYPE, this, false); 
			contentArea.addEventListener(MozillaEventAdapter.MOUSEUPEVENTTYPE, this, false); 
			contentArea.addEventListener(MozillaEventAdapter.MOUSEMOVEEVENTTYPE, this, false); 
			contentArea.addEventListener(MozillaEventAdapter.CONTEXTMENUEVENTTYPE, this, false);
			contentArea.addEventListener(MozillaEventAdapter.DRAGDROPEVENT, this, false);
			contentArea.addEventListener(MozillaEventAdapter.DRAGENTEREVENT, this, false);
			contentArea.addEventListener(MozillaEventAdapter.DRAGEXITEVENT,this, false);
			contentArea.addEventListener(MozillaEventAdapter.DRAGGESTUREEVENT, this, false);
			contentArea.addEventListener(MozillaEventAdapter.DRAGOVEREVENT, this, false);
			contentArea.addEventListener(MozillaEventAdapter.DBLCLICK, this, false);
		}
		if (window != null) {
			window.addEventListener(MozillaEventAdapter.MOZAFTERPAINT, this, false);

			nsISelection selection = domWindow.getSelection();
			selectionPrivate = (nsISelectionPrivate) selection
					.queryInterface(nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
			selectionPrivate.addSelectionListener(this);
		}
		if (document != null) {
			document.addEventListener(MozillaEventAdapter.KEYPRESS, this, false);
			//as a fix of https://jira.jboss.org/jira/browse/JBIDE-4022
			//scroll event listener was added for selection border redrawing
			document.addEventListener(MozillaEventAdapter.SCROLL, this, false);
		}
	}
	
	/**
	 * Detach this instance from all Mozilla event notifiers
	 */
	public void detach() {
		attached = false;

		if (contentArea != null) {
			contentArea.removeEventListener(MozillaEventAdapter.CLICKEVENTTYPE, this, false); 
			contentArea.removeEventListener(MozillaEventAdapter.MOUSEDOWNEVENTTYPE, this, false); 
			contentArea.removeEventListener(MozillaEventAdapter.MOUSEUPEVENTTYPE, this, false); 
			contentArea.removeEventListener(MozillaEventAdapter.MOUSEMOVEEVENTTYPE, this, false); 
			contentArea.removeEventListener(MozillaEventAdapter.CONTEXTMENUEVENTTYPE, this, false);
			contentArea.removeEventListener(MozillaEventAdapter.DRAGDROPEVENT, this, false);
			contentArea.removeEventListener(MozillaEventAdapter.DRAGENTEREVENT, this, false);
			contentArea.removeEventListener(MozillaEventAdapter.DRAGEXITEVENT, this, false);
			contentArea.removeEventListener(MozillaEventAdapter.DRAGGESTUREEVENT, this, false);
			contentArea.removeEventListener(MozillaEventAdapter.DRAGOVEREVENT, this, false);
			contentArea.removeEventListener(MozillaEventAdapter.DBLCLICK, this, false);
			contentArea = null;
		}
		if (document != null) {
			document.removeEventListener(MozillaEventAdapter.KEYPRESS, this, false); 
			document.removeEventListener(MozillaEventAdapter.SCROLL, this, false);
			document = null;
		}
		if (window != null) {
			window.removeEventListener(MozillaEventAdapter.MOZAFTERPAINT, this, false);
			window = null;
		}
		if (selectionPrivate != null) {
			try {
				selectionPrivate.removeSelectionListener(this);
			} catch (XPCOMException xpcomException) {
				// this exception throws when progress listener already has been
				// deleted,
				// so just ignore if error code NS_ERROR_FAILURE
				// mareshkau fix for jbide-3155
				if (xpcomException.errorcode != XulRunnerBrowser.NS_ERROR_FAILURE) {
					throw xpcomException;
				}
			} finally {
				selectionPrivate = null;
			}
		}
	}

	public void addMouseListener(MozillaMouseListener listener) {
		listeners.add(MozillaMouseListener.class, listener);
	}

	public void removeMouseListener(MozillaMouseListener listener) {
		listeners.remove(MozillaMouseListener.class, listener);
	}

	public void addKeyListener(MozillaKeyListener listener) {
		listeners.add(MozillaKeyListener.class, listener);
	}

	public void removeKeyListener(MozillaKeyListener listener) {
		listeners.remove(MozillaKeyListener.class, listener);
	}

	public void addContextMenuListener(MozillaContextMenuListener listener) {
		listeners.add(MozillaContextMenuListener.class, listener);
	}

	public void removeContextMenuListener(MozillaContextMenuListener listener) {
		listeners.remove(MozillaContextMenuListener.class, listener);
	}

	public void addDndListener(MozillaDndListener listener) {
		listeners.add(MozillaDndListener.class, listener);
	}

	public void removeDndListener(MozillaDndListener listener) {
		listeners.remove(MozillaDndListener.class, listener);
	}

	public void addSelectionListener(MozillaSelectionListener listener) {
		listeners.add(MozillaSelectionListener.class, listener);
	}

	public void removeSelectionListener(MozillaSelectionListener listener) {
		listeners.remove(MozillaSelectionListener.class, listener);
	}
	/**
	 * {@inheritDoc}
	 */
	public void notifySelectionChanged(nsIDOMDocument domDocument, nsISelection selection, short reason) {
		for (MozillaSelectionListener listener : listeners.getListeners(
				MozillaSelectionListener.class)) {
			listener.notifySelectionChanged(domDocument, selection, reason);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleEvent(nsIDOMEvent domEvent) {
		final String eventType = domEvent.getType();
		if(MOUSEMOVEEVENTTYPE.equals(eventType)) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent
					.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			for (MozillaMouseListener listener : listeners.getListeners(
					MozillaMouseListener.class)) {
				listener.mouseMove(mouseEvent);
			}
		} else if(MOUSEDOWNEVENTTYPE.equals(eventType)) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent
					.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			for (MozillaMouseListener listener : listeners.getListeners(
					MozillaMouseListener.class)) {
				listener.mouseDown(mouseEvent);
			}
		} else if(MOUSEUPEVENTTYPE.equals(eventType)) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent
					.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			for (MozillaMouseListener listener : listeners.getListeners(
					MozillaMouseListener.class)) {
				listener.mouseUp(mouseEvent);
			}
		} else if(CLICKEVENTTYPE.equals(eventType)) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent
					.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			for (MozillaMouseListener listener : listeners.getListeners(
					MozillaMouseListener.class)) {
				listener.mouseClick(mouseEvent);
			}
		} else if(DBLCLICK.equals(eventType)) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent
					.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			for (MozillaMouseListener listener : listeners.getListeners(
					MozillaMouseListener.class)) {
				listener.mouseDblClick(mouseEvent);
			}
		} else if(KEYPRESS.equals(eventType)) {
			nsIDOMKeyEvent keyEvent = (nsIDOMKeyEvent) domEvent
					.queryInterface(nsIDOMKeyEvent.NS_IDOMKEYEVENT_IID);
			for (MozillaKeyListener listener : listeners.getListeners(
					MozillaKeyListener.class)) {
				listener.keyPress(keyEvent);
			}
		} else if(CONTEXTMENUEVENTTYPE.equals(eventType)) {			
			//first param are null 0, because this not used in event handler
			nsIDOMNode node = (nsIDOMNode) domEvent.getTarget()
					.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
			for (MozillaContextMenuListener listener : listeners.getListeners(
					MozillaContextMenuListener.class)) {
				listener.onShowContextMenu(0, domEvent, node);
			}
		} else if(DRAGGESTUREEVENT.equals(eventType)) {
			// fix of JBIDE-4998: since drag events now are implemented by
			// handling CLICKEVENTTYPE, there is no need to handle them here 
			//for (DndDomEventListener listener : dndListeners) {
			//	listener.dragGesture(domEvent);
			//}
		} else if(DRAGDROPEVENT.equals(eventType)) {
			// calls when drop event occure		 
			for (MozillaDndListener listener : listeners.getListeners(
					MozillaDndListener.class)) {
				listener.dragDrop(domEvent);
			}
			domEvent.stopPropagation();
			domEvent.preventDefault();
		} else if(DRAGENTEREVENT.equals(eventType)) {
			//just ignore this event
		} else if(DRAGEXITEVENT.equals(eventType)) {
			//just ignore this event
		} else if(DRAGOVEREVENT.equals(eventType)) {			
			for (MozillaDndListener listener : listeners.getListeners(
					MozillaDndListener.class)) {
				listener.dragOver(domEvent);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
	 */
	public nsISupports queryInterface(String arg0) {
		return Mozilla.queryInterface(this, arg0);
	}
	
	// this method is never used		
//		boolean isXulElement(nsIDOMMouseEvent mouseEvent) {
//			// TODO Sergey Vasilyev figure out with getTmpRealOriginalTarget
////			nsIDOMNSEvent nsEvent = (nsIDOMNSEvent)mouseEvent.queryInterface(nsIDOMNSEvent.NS_IDOMNSEVENT_IID);
////			nsIDOMEventTarget target = nsEvent.getTmpRealOriginalTarget();	
////			int aDragNode = target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
////			nsIDOMNode originalNode = nsIDOMNode.getNodeAtAddress(aDragNode);
////			String prefix = originalNode.getPrefix();
////			boolean isXul = "XUL".equalsIgnoreCase(prefix);
////			target.Release();
////			nsEvent.Release();
//			return false;
//		}
		
// commented, since visualEditor was never used locally
// if it is needed, the calls to this method should be uncommented
//		(see MozillaEditor)
//		void setVisualEditor(XulRunnerEditor visualEditor) {
//			this.visualEditor = visualEditor;
//		}

// this method is never used
//		/* (non-Javadoc)
//		 * @see org.mozilla.interfaces.nsIClipboardDragDropHooks#onPasteOrDrop(org.mozilla.interfaces.nsIDOMEvent, org.mozilla.interfaces.nsITransferable)
//		 */
//		public boolean onPasteOrDrop(nsIDOMEvent event,	nsITransferable transferable) {
//			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent)event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
//
//			if (editorDomEventListener != null && !isXulElement(mouseEvent)) {
//				nsIDragSession dragSession = visualEditor.getCurrentDragSession();
//				if (dragSession.isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
//					editorDomEventListener.onPasteOrDrop(mouseEvent, VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
//				}
//			}
//			mouseEvent.preventDefault();
//			mouseEvent.stopPropagation();
//			
//			return false;
//		}
}

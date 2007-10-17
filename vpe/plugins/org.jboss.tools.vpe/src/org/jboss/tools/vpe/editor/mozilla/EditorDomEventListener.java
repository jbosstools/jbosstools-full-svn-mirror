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


import org.mozilla.interfaces.nsIContextMenuListener;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMMutationEvent;
import org.mozilla.interfaces.nsISelection;


public interface EditorDomEventListener extends nsIContextMenuListener {

	// nsIDOMMutationListener
	void subtreeModified(nsIDOMMutationEvent mutationEvent);
	void nodeInserted(nsIDOMMutationEvent mutationEvent);
	void nodeRemoved(nsIDOMMutationEvent mutationEvent);
	void nodeRemovedFromDocument(nsIDOMMutationEvent mutationEvent);
	void nodeInsertedIntoDocument(nsIDOMMutationEvent mutationEvent);
	void attrModified(nsIDOMMutationEvent mutationEvent);
	void characterDataModified(nsIDOMMutationEvent mutationEvent);
	void notifySelectionChanged(nsIDOMDocument domDocument, nsISelection selection, short reason);
	void mouseDown(nsIDOMMouseEvent mouseEvent);
	void mouseUp(nsIDOMMouseEvent mouseEvent);
	void mouseClick(nsIDOMMouseEvent mouseEvent);
	void mouseDblClick(nsIDOMMouseEvent mouseEvent);
//	void mouseOver(nsIDOMMouseEvent mouseEvent);
//	void mouseOut(nsIDOMMouseEvent mouseEvent);
//	void keyDown(nsIDOMKeyEvent keyEvent);
//	void keyUp(nsIDOMKeyEvent keyEvent);
	void mouseMove(nsIDOMMouseEvent mouseEvent);
	void keyPress(nsIDOMKeyEvent keyEvent);
	void elementResized(nsIDOMElement element, int resizerConstrains, int top, int left, int width, int height);
	
	void dragEnter(nsIDOMEvent event);
	void dragExit(nsIDOMEvent event);
	void dragOver(nsIDOMEvent event);
	void drop(nsIDOMEvent event);
	/**
	 * Drag gesture event handler
	 * @param event xulrunner drag event
	 */
	void dragGesture(nsIDOMEvent event);

	boolean canInnerDrag(nsIDOMMouseEvent mouseEvent);
	MozillaDropInfo canInnerDrop(nsIDOMMouseEvent mouseEvent);
	void innerDrop(nsIDOMMouseEvent mouseEvent);

	MozillaDropInfo canExternalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data);
	void externalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data);
	
	void onPasteOrDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data);

	void onShowTooltip(int x, int y, String text);
	void onHideTooltip();
	/**
	 * Calls when editor content changed, and we should highlight selected element.
	 */
	void onRefresh();
	void startDragSession(nsIDOMEvent domEvent);
	
	/**
	 * Calls when drop event occures 
	 * @param domEvent
	 */
	void dragDrop(nsIDOMEvent domEvent);
}

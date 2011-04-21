/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.mozilla.listener;

import java.util.EventListener;

import org.mozilla.interfaces.nsIDOMEvent;

/**
 * Listener for Drag&Drop events.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface MozillaDndListener extends EventListener {
	void dragOver(nsIDOMEvent event);

	/**
	 * Drag gesture event handler
	 * @param event xulrunner drag event
	 */
	void dragGesture(nsIDOMEvent event);

	/**
	 * Calls when drop event occurs 
	 * @param domEvent
	 */
	void dragDrop(nsIDOMEvent domEvent);
// these methods are never used
//	void dragEnter(nsIDOMEvent event);
//	void dragExit(nsIDOMEvent event);
//	void drop(nsIDOMEvent event);
//	void onPasteOrDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data);
}

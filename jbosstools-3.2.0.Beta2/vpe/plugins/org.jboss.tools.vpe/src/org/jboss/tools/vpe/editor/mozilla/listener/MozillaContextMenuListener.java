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

import org.mozilla.interfaces.nsIContextMenuListener;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Listener for context-menu events. 
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface MozillaContextMenuListener extends EventListener {

	/**
	 * @see nsIContextMenuListener#onShowContextMenu(long, nsIDOMEvent, nsIDOMNode)
	 */
	void onShowContextMenu(long aContextFlags, nsIDOMEvent aEvent,
			nsIDOMNode aNode);

}

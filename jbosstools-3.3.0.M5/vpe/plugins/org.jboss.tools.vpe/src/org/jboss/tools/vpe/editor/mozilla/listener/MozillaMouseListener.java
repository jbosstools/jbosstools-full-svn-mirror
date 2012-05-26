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

import org.mozilla.interfaces.nsIDOMMouseEvent;

/**
 * Listener for mouse events.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface MozillaMouseListener extends EventListener {

	void mouseDown(nsIDOMMouseEvent mouseEvent);

	void mouseUp(nsIDOMMouseEvent mouseEvent);

	void mouseClick(nsIDOMMouseEvent mouseEvent);

	void mouseDblClick(nsIDOMMouseEvent mouseEvent);

	void mouseMove(nsIDOMMouseEvent mouseEvent);

}

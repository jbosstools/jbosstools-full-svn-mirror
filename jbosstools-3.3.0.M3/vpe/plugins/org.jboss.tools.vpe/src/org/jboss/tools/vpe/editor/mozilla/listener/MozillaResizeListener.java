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

import org.mozilla.interfaces.nsIDOMElement;

/**
 * Listener for element resize events,
 * 
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public interface MozillaResizeListener {

	void elementResized(nsIDOMElement element, int resizerConstrains, int top,
			int left, int width, int height);

}

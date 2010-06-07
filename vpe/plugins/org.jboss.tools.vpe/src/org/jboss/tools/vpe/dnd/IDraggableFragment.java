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
package org.jboss.tools.vpe.dnd;

import org.eclipse.swt.graphics.Point;
import org.mozilla.interfaces.nsIDOMElement;

/**
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public interface IDraggableFragment {
	public Point getPosition(); 
	public nsIDOMElement createRepresentation();
}

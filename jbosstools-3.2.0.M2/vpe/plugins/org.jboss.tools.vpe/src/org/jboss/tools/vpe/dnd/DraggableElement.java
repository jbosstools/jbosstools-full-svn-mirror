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

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.xulrunner.util.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMElement;

/**
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public class DraggableElement extends AbstractDraggableFragment {
	private final nsIDOMElement element;

	public DraggableElement(nsIDOMElement element) {
		this.element = element;
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.dnd.IDraggableFragment#createRepresentation()
	 */
	public nsIDOMElement cloneFragmentAsElement() {
		nsIDOMElement representation
				= queryInterface(element.cloneNode(true), nsIDOMElement.class);
		element.getParentNode().appendChild(representation);

		return representation;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.dnd.IDraggableFragment#getPosition()
	 */
	public Point getPosition() {
		Rectangle bounds = XulRunnerVpeUtils.getElementBounds(element);
		return new Point(bounds.x, bounds.y);
	}

}

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

import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMElement;

/**
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public abstract class AbstractDraggableFragment implements IDraggableFragment {
	private static final String DRAGGING_OPACITY = "0.5";		  //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.dnd.IDraggableFragment#createRepresentation()
	 */
	public final nsIDOMElement createRepresentation() {
		nsIDOMElement representation = cloneFragmentAsElement();
		
		DndUtil.setTemporaryDndElement(representation, true);

		nsIDOMCSSStyleDeclaration representationStyle
				= VpeStyleUtil.getStyle(representation);
		representationStyle.setProperty(HTML.STYLE_PARAMETER_POSITION,
				HTML.STYLE_VALUE_ABSOLUTE, HTML.STYLE_PRIORITY_IMPORTANT);
		representationStyle.setProperty(HTML.STYLE_PARAMETER_OPACITY,
				DRAGGING_OPACITY, HTML.STYLE_PRIORITY_IMPORTANT);
		VpeStyleUtil.setElementVisible(representation, false);
		
		return representation;
	}
	
	public abstract nsIDOMElement cloneFragmentAsElement();
}

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
package org.jboss.tools.vpe.selbar;

import java.util.EventListener;

/**
 * Classes which implement this interface provide method
 * that deal with the events that are generated when visibility
 * of a widget is changed.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * {@code addVisibilityListener} method and removed using
 * the {@code removeVisibilityListener} method. When
 * visibility state is changed in a control the appropriate method
 * will be invoked.
 * </p>
 *
 * @see VisibilityEvent
 * 
 * @author yradtsevich
 */
public interface VisibilityListener extends EventListener {
	/**
	 * Sent when the visibility state of the widget is changed.
	 *
	 * @param event an event containing information about the operation
	 */
	public void visibilityChanged(VisibilityEvent event);
}

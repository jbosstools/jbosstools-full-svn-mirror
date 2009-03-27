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

import java.util.EventObject;

/**
 * Instances of this class are sent as a result of a visibility state
 * of widgets is changed.
 *
 * @see VisibilityListener
 *
 * @author yradtsevich
 */
public class VisibilityEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public VisibilityEvent(SelectionBar source) {
		super(source);
	}

	@Override
	public SelectionBar getSource() {
		return (SelectionBar) super.getSource();
	}
}

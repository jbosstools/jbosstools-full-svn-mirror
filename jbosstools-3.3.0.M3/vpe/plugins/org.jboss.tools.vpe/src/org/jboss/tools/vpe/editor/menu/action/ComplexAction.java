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
package org.jboss.tools.vpe.editor.menu.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * Allows to create an action which composed of multiple actions.
 * 
 * @author yradtsevich
 */
public class ComplexAction extends Action {
	private final IAction[] actions;

	public ComplexAction(final String name, final IAction... actions) {
		super(name);
		this.actions = actions;
	}

	@Override
	public void run() {
		for (final IAction action : actions) {
			action.run();
		}
	}
}
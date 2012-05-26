/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.editor.menu.action;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

/**
 * This is listener class that should be implemented by childs.
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public abstract class VpeMenuListener implements IMenuListener {

	private MenuManager manager = null;
	private boolean loaded = false;

	/**
	 * Constructor.
	 *
	 * @param manager MenuManager object
	 */
	public VpeMenuListener(MenuManager manager) {
		this.manager = manager;
	}

	public void menuAboutToShow(IMenuManager m) {
		if (loaded) {
			return;
		}
		loaded = true;
		fillContextMenu();
		manager.getParent().update(true);
	}

	protected abstract void fillContextMenu();
}
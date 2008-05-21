/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.view.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

// tau 22.11.2005
// edit tau 27.01.2006

public abstract class ActionOrmTree extends Action {
	
	private TreeViewer viewer;

	public ActionOrmTree() {
		super();
	}
	
	public void run(TreeViewer viewer) {
		this.viewer = viewer;
		run();
	}

	public void run() {
		try {
			this.rush();
		} finally {
//	    	 OrmCore.lock.release();
//			 ((OrmContentProvider)viewer.getContentProvider()).lockMenu = false; // add tau 06.12.2005
//			 
//			// add tau 27.01.2006			
//			OrmCore.getDefault().setLockResourceChangeListener(false);

//			if (ViewPlugin.TRACE || ViewPlugin.TRACE_VIEW ) ExceptionHandler.logInfo("!Finally ActionOrmTree.run() lock(=" + OrmCore.lock + ").acquire(), Depth=" + OrmCore.lock.getDepth());
		}
		
	}
	
	protected abstract void rush();

	public TreeViewer getViewer() {
		return viewer;
	}

	public ActionOrmTree setViewer(TreeViewer viewer) {
		this.viewer = viewer;
		return this;
	}

}

/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 

package org.jboss.tools.vpe.editor.toolbar;

import org.eclipse.swt.widgets.Composite;

/**
 * This interface create a toolbarmanager for swt toolbar in the VPE. 
 * To use it procede only in the following order: first call 
 * createToolBarComposite(composite) where composite is the parent for the future toolbar
 * then call addToolBar(IvpeToolbar) to add it and finally createMenuComposite(Composite) to add it to the menu bar. 
 * @author Igels
 */
public interface IVpeToolBarManager {

	/**
	 * This method create a splitter in the given composite
	 * @param parent
	 * @return
	 */
	public Composite createToolBarComposite(Composite parent);

	/**
	 * This method add the Toolbar to the splitter with the scpecified layout
	 * @param bar
	 */
	public void addToolBar(IVpeToolBar bar);

	/*
	 * Sets the toolbar visibility
	 */
	 void setToolbarVisibility(boolean isVisible);
	
	public void dispose();
}
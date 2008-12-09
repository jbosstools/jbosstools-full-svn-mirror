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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Erick
 * Created on 14.07.2005
 * This interface is the descriptor for the ToolItem 
 * to insert them in to the menu bar 
 */
public interface IItems {
	
	/*
	 * Return the listeners list from the item 
	 */
	public Listener[] getListeners();
	
	/*
	 * Return the size of item in the toolbar
	 */
	public int getSize();
	
	/*
	 * Show shouldthis item be inserted in the menu 
	 */
	public boolean isInsertable();
	
	/*
	 * Return the image of image
	 */
	public Image getItemImage();
	
	/*
	 * Retrun the text to insert for the menuitem 
	 */
	public String getItemToolTip();
	
	/*
	 * Check if it is a toolitem and not any other type of separetor 
	 */
	public boolean isToolItem();
}
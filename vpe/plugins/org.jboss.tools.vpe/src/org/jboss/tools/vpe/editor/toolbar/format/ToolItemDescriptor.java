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
package org.jboss.tools.vpe.editor.toolbar.format;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolItem;

import org.jboss.tools.vpe.editor.toolbar.IItems;

/**
 * @author Erick
 * Created on 14.07.2005
 * @see Iitems 
 */
public class ToolItemDescriptor implements IItems {

	private boolean isInsertable;
	private boolean isToolItem;
	private ArrayList arList = new ArrayList();
	private ToolItem item;

	public ToolItemDescriptor(ToolItem it, boolean isInsertable, Listener listener, boolean isToolItem){
		item = it;
		setInsertable(isInsertable);
		if(listener!=null) {
			addListeners(listener);
		}
		setToolItem(isToolItem);
	}

	public Listener[] getListeners() {
		return (Listener[])arList.toArray(new Listener[arList.size()]);
	}

	public void addListeners(Listener listener){
		arList.add(listener);
	}

	public int getSize() {
		return item.getWidth();
	}

	public boolean isInsertable() {
		return isInsertable;
	}

	public void setInsertable(boolean isInsertable) {
		this.isInsertable = isInsertable;
	}

	public Image getItemImage(){
		return item.getImage();		
	}

	public String getItemToolTip(){
		return item.getToolTipText();
	}

	public boolean isToolItem() {
		return isToolItem;
	}

	public void setToolItem(boolean isToolItem) {
		this.isToolItem = isToolItem;
	}	
}
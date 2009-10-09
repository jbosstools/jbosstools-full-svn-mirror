/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

/**
 * @author Dart
 *
 */
public abstract class AbstractProcessGraphAction extends Action implements ISelectionChangedListener{

	protected ISmooksModelProvider provider;
	
	protected ISelection currentSelection = null;
	
	public AbstractProcessGraphAction(String text , ISmooksModelProvider provider){
		super();
		this.provider = provider;
		this.setText(text);
		init();
	}
	
	protected void init(){
		
	}
	
	@Override
	public void run() {
		super.run();
	}
	
	public void update(){
		
	}

	public ISmooksModelProvider getProvider() {
		return provider;
	}

	public void setProvider(ISmooksModelProvider provider) {
		this.provider = provider;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		currentSelection = event.getSelection();
		update();
	}
	
	protected List<TaskType> getCurrentSelectedTask(){
		if(currentSelection != null){
			List<TaskType> selectedTasks = new ArrayList<TaskType>();
			List<?> selections = ((IStructuredSelection)currentSelection).toList();
			for (Iterator<?> iterator = selections.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if(object != null && object instanceof TaskType){
					selectedTasks.add((TaskType)object);
				}
			}
			return selectedTasks;
		}
		return null;
	}

}

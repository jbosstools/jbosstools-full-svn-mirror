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
package org.jboss.tools.shale.ui.clay.editor;

import org.jboss.tools.common.editor.AbstractSectionEditor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.shale.ui.ShaleUiPlugin;
import org.jboss.tools.shale.ui.clay.editor.model.impl.*;

public class ClayGuiEditor extends AbstractSectionEditor {
	private ClayEditor gui = null;
	private IModelObjectEditorInput input;
	private boolean isInitialized = false;
	private XModelObject installedProcess = null;
	private ActionRegistry actionRegistry;
	private ClayModel model;
	
	protected boolean isWrongEntity(String entity) {
		return !entity.equals(ShaleClayConstants.ENT_SHALE_CLAY);
	}
	
	public void setInput(IEditorInput input) {
		super.setInput(input);
		if(input instanceof IModelObjectEditorInput) {		
			this.input = (IModelObjectEditorInput)input;
		}
	}
	
    public ClayEditor getGUI(){
    	return gui;
    }

	public ISelectionProvider getSelectionProvider() {
		return (gui == null) ? null : gui.getModelSelectionProvider();
	}

	protected XModelObject getInstalledObject() {
		return installedProcess;
	}
	
	protected void updateGui() {
		if(object != installedProcess) disposeGui(); else if(isInitialized) return;
		isInitialized = true;
		installedProcess = object;
		guiControl.setVisible(object != null);
		if(object == null) return;
		try {
            gui = new ClayEditor((IEditorInput)input);
            model = new ClayModel(object);
            model.updateLinks();

			gui.setClayModel(model);

			gui.init((IEditorSite)getSite(), (IEditorInput)input);
			gui.createPartControl(guiControl);
			control = guiControl.getChildren()[0];
			control.setLayoutData(new GridData(GridData.FILL_BOTH));
			guiControl.layout();
			wrapper.update();
			wrapper.layout();
		} catch (Exception ex) {
			ShaleUiPlugin.log(ex);
		}
	}
	
	public ClayEditor getDiagram(){
		return this.gui;
	}
	
  	public void dispose() {
		if(model != null) model.dispose();
		model = null;
		super.dispose();
  	}
	
	protected void disposeGui() {
		if(gui != null) {
			gui.dispose();
			gui = null;
			try { control.dispose(); } catch (Exception e) {}
			control = null;
		}
	}
	protected ActionRegistry getActionRegistry() {
			if (actionRegistry == null)
				actionRegistry = new ActionRegistry();
			return actionRegistry;
		}
	
	protected void createActions() {}
	
	public Object getAdapter(Class adapter) {
		if(adapter == ActionRegistry.class || adapter == org.eclipse.gef.editparts.ZoomManager.class){
				if(gui != null)
					return gui.getAdapter(adapter);
		}
		return super.getAdapter(adapter);
	}

	public String getTitle() {
		return "Diagram";
	}

}

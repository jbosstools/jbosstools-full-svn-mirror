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

import org.jboss.tools.common.editor.AbstractSelectionProvider;
import org.jboss.tools.common.editor.ObjectMultiPageEditor;
import org.jboss.tools.common.editor.ObjectTextEditor;
import org.jboss.tools.common.model.ui.texteditors.XMLTextEditorComponent;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jboss.tools.common.gef.outline.xpl.DiagramContentOutlinePage;
import org.jboss.tools.shale.model.clay.ClayConfigFilteredTreeConstraint;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.shale.ui.ShaleUiPlugin;

public class ClayCompoundEditor extends ObjectMultiPageEditor {
	protected ClayGuiEditor guiEditor;
	protected ClayConfigFilteredTreeConstraint constraint = new ClayConfigFilteredTreeConstraint();
	
	public ClayCompoundEditor() {
		outline.addFilter(constraint);
	}

	protected void doCreatePages() {
		if (isAppropriateNature()) {
			treeFormPage = createTreeFormPage();
			treeFormPage.setTitle("Shale Clay Editor");
			treeFormPage.addFilter(constraint);
			treeFormPage.initialize(object);
			addFormPage(treeFormPage);
			createGuiPage();
		}
		createTextPage();
		initEditors();
	}

	protected boolean isWrongEntity(String entity) {
		return !entity.equals(ShaleClayConstants.ENT_SHALE_CLAY);
	}

	public ClayGuiEditor getGuiEditor(){
		return this.guiEditor;
	}
	
	protected void createGuiPage() {
//@S_CHECK@
		guiEditor = new ClayGuiEditor();
		try {
			int index = addPage(guiEditor, guiEditor.getEditorInput());
			setPageText(index, "Diagram");
			guiEditor.setInput(input);
			selectionProvider.setHost(guiEditor.getSelectionProvider());
			guiEditor.addErrorSelectionListener(createErrorSelectionListener());
			selectionProvider.addHost("guiEditor", guiEditor.getSelectionProvider());
		} catch (Exception e) {
			ShaleUiPlugin.log(e);
		}
	}
	
	protected ObjectTextEditor createTextEditor() {
		return new XMLTextEditorComponent();	
	}

	public void dispose() {
		if(input != null) {
			selectionProvider.setHost(null);
			try { getSite().setSelectionProvider(null); } catch (Exception e) {}
		}
		super.dispose();
		if(guiEditor != null) {
			guiEditor.dispose();
			guiEditor = null;
		}
	}
	
	protected void setNormalMode() {
		if ((guiEditor != null) && isAppropriateNature()) {
			guiEditor.setObject(getModelObject(), isErrorMode());
		}
		if (treeFormPage != null) { // AU added
			treeFormPage.initialize(getModelObject()); // AU added
			treeFormPage.setErrorMode(isErrorMode());
		} // AU added
		updateSelectionProvider();
	}

	protected void setErrorMode() {
		setNormalMode();
	}

	protected int getGuiPageIndex() {
		return 1; 
	}
	
	public boolean isGuiEditorActive() {
		return getActivePage() == getGuiPageIndex();
	}
	
	protected void updateSelectionProvider() {
		if(guiEditor != null) {
			selectionProvider.addHost("guiEditor", guiEditor.getSelectionProvider());
		}
		if(textEditor != null) {
			selectionProvider.addHost("textEditor", getTextSelectionProvider());
		}
		int index = getActivePage();
		if(index == getSourcePageIndex()) {
			if(textEditor != null) {
				selectionProvider.setHost(getTextSelectionProvider());
			}
			return;
		}
		if(index != getGuiPageIndex() || guiEditor == null || guiEditor.getSelectionProvider() == null) {
			if (treeEditor != null) {
				selectionProvider.setHost(treeEditor.getSelectionProvider());
				treeEditor.fireEditorSelected();
			}
			if (treeFormPage != null) {
				selectionProvider.addHost("treeEditor", treeFormPage.getSelectionProvider(), true);
			}
		} else {
			ISelectionProvider p = guiEditor.getSelectionProvider();
			selectionProvider.setHost(p);
			if(p instanceof AbstractSelectionProvider) {
				((AbstractSelectionProvider)p).fireSelectionChanged();
			}		
		}
	}

//@S_CLASS@
	public Object getAdapter(Class adapter) {
		if(adapter == IContentOutlinePage.class){
			if(guiEditor == null || guiEditor.getGUI() == null) {
				return super.getAdapter(adapter);
			}
			Object o = guiEditor.getGUI().getAdapter(adapter);
			if(o instanceof DiagramContentOutlinePage) {
				DiagramContentOutlinePage g = (DiagramContentOutlinePage)o;
				g.setTreeOutline(outline);
			}
			return o;  
		}
		if(adapter == ActionRegistry.class || adapter == org.eclipse.gef.editparts.ZoomManager.class){
			 if(guiEditor != null)
			 	if(guiEditor.getGUI() != null)
			 		return guiEditor.getGUI().getAdapter(adapter);
		}

		return super.getAdapter(adapter);
	}

}

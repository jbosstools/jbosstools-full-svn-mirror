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
package org.jboss.tools.smooks.edimap.editor;

import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.wizards.SmooksConfigurationFileNewWizard;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.medi.Delimiters;
import org.jboss.tools.smooks.model.medi.Description;
import org.jboss.tools.smooks.model.medi.DocumentRoot;
import org.jboss.tools.smooks.model.medi.EdiMap;
import org.jboss.tools.smooks.model.medi.MEdiFactory;

/**
 * 
 * @author Dart dpeng@redhat.com
 * 
 */
public class EDIMapFormEditor extends AbstractSmooksFormEditor {
	private EDIMapFormPage ediPage = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		try {
			ediPage = new EDIMapFormPage(this,"EDIMap", "EDIMap");
			this.addValidateListener(ediPage);
			int index = this.addPage(ediPage);
			setPageText(index, "Design");
		} catch (PartInitException e) {
			SmooksConfigurationActivator.getDefault().log(e);
		}
		super.addPages();
	}
	
	
	public void inputTypeChanged(SmooksGraphicsExtType extType) {
		 graphChanged = true;
		 firePropertyChange(PROP_DIRTY);
	}

	

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.editor.AbstractSmooksFormEditor#handleDocumentChange()
	 */
	@Override
	protected void handleDocumentChange() {
		super.handleDocumentChange();
		if(ediPage != null){
			ediPage.rebuildGUI();
		}
	}





	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.editor.AbstractSmooksFormEditor#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		ediPage.doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.editor.AbstractSmooksFormEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		EObject smooksModel = getSmooksModel();
		// init MEDI model
		if(smooksModel instanceof DocumentRoot){
			EdiMap map = ((DocumentRoot)smooksModel).getEdimap();
			if(map == null){
				map = MEdiFactory.eINSTANCE.createEdiMap();
				((DocumentRoot)smooksModel).setEdimap(map);
			}
			Description description = map.getDescription();
			if(description == null){
				description = MEdiFactory.eINSTANCE.createDescription();
				map.setDescription(description);
			}
			
			Delimiters delimiters = map.getDelimiters();
			if(delimiters == null){
				delimiters = MEdiFactory.eINSTANCE.createDelimiters();
				map.setDelimiters(delimiters);
			}
		}
	}

	@Override
	protected SmooksGraphicsExtType createSmooksGraphcsExtType(Object smooksModel) {
		IFileEditorInput editorInput = (IFileEditorInput)getEditorInput();
		IFile file = editorInput.getFile();
		String extFileName = file.getName() + SmooksConstants.SMOOKS_GRAPHICSEXT_EXTENTION_NAME_WITHDOT;
		setPartName(file.getName());
		IContainer container = file.getParent();
		if (container != null && container.exists()) {
			IFile extFile = container.getFile(new Path(extFileName));
			if (extFile != null && !extFile.exists()) {
				try {
					String version = SmooksUIUtils.judgeSmooksPlatformVersion((EObject)smooksModel);
					String inputType = SmooksUIUtils.judgeInputType((EObject)smooksModel);
					SmooksConfigurationFileNewWizard.createExtentionFile(extFile, version, inputType, null);
				} catch (Throwable t) {
					// ignore
				}
			}
			if (extFile != null && extFile.exists()) {
				try {
					smooksGraphicsExt = SmooksUIUtils.loadSmooksGraphicsExt(extFile);
				} catch (IOException e) {
					SmooksConfigurationActivator.getDefault().log(e);
				}
			}
		}
		return smooksGraphicsExt;
	}

	@Override
	protected void generateSmooksGraphExt() {
		super.generateSmooksGraphExt();
	}
	
}

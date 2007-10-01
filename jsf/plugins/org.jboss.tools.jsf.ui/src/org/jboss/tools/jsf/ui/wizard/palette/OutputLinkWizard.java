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
package org.jboss.tools.jsf.ui.wizard.palette;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import org.jboss.tools.common.model.ui.editors.dnd.*;
import org.jboss.tools.common.model.ui.editors.dnd.composite.*;

/**
 *  @author erick 
 */

public class OutputLinkWizard extends Wizard implements PropertyChangeListener, IDropWizard {
	PaletteDropCommand fDropCommand;
	IDropWizardModel fModel;
	private OutputLinkWizardPage page2 = null;
	
	public OutputLinkWizard (){
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
	}
	
	public boolean canFinish() {		
		return getWizardModel().isValid();
	}
	
	public boolean performFinish() {
		setText();
		fDropCommand.execute();
		return true;
	}
	
	public boolean performCancel() {
		return true;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		IWizardPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			DefaultDropWizardPage page = (DefaultDropWizardPage)pages[i];
			page.runValidation();
		}
	}
	
	public void setCommand(IDropCommand command) {
		fDropCommand = (PaletteDropCommand)command;		
	}
	
	public IDropWizardModel getWizardModel() {
		return fDropCommand.getDefaultModel();		
	}
	
	public String getMimeData() {
		return  getWizardModel().getDropData().getMimeData();
	}
	
	public String getMimeType() {
		return  getWizardModel().getDropData().getMimeType();
	}		
	
	/**
	 * 
	 */
	public void addPages() {
		super.addPages();		

		page2 = new OutputLinkWizardPage();
		
		TagProposal[] proposals = 
			TagProposalsComposite.getTagProposals(getMimeType(),getMimeData(), fDropCommand.getTagProposalFactory());
		
		this.addPage(page2);

		getWizardModel().addPropertyChangeListener(this);		
		
		if(proposals.length==1) { 
			getWizardModel().setTagProposal(proposals[0]);
		}
	}
	
	static String F_PREFIX  = "%prefix|http://java.sun.com/jsf/core|f%";
	
	public void setText(){
		StringBuffer text = new StringBuffer();		
			
		if(page2.getText()==null || page2.getText().trim().length() == 0) {
			return;
		} else {
			 
			text.append(fDropCommand.getProperties().getProperty("start text"));			
			fDropCommand.getProperties().setProperty("new line", "false");			
			text.append("\n\t<" + F_PREFIX+ "verbatim>"+page2.getText()+"</" + F_PREFIX + "verbatim>");			
			fDropCommand.getProperties().setProperty("start text", text.toString());
			
			if (page2.isValue()) {
				StringBuffer text2 = new StringBuffer();
				text2.append(fDropCommand.getProperties().getProperty("end text"));
				text2.append('|');
				fDropCommand.getProperties().setProperty("end text", text2.toString());
			}			
		}
	}

}

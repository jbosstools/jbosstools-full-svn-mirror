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
package org.jboss.tools.smooks.graphical.editors.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.command.GEFAdapterCommand;
import org.jboss.tools.smooks.graphical.wizards.XSLTemplateCreationWizard;

/**
 * @author Dart
 *
 */
public class CreateXSLTemplateModelCommand extends GEFAdapterCommand {
	
	private IEditorPart editorPart = null;
	
	private ISmooksModelProvider provider = null;
	
	public CreateXSLTemplateModelCommand(EditingDomain domain, Command emfCommand , IEditorPart editorPart,ISmooksModelProvider provider) {
		super(domain, emfCommand);
		this.editorPart = editorPart;
		this.provider = provider;
	}

	boolean firstTime = true;

	@Override
	public void execute() {
		if (firstTime) {
			XSLTemplateCreationWizard wizard = new XSLTemplateCreationWizard(provider, null);
			final WizardDialog dialog = new WizardDialog(editorPart.getSite().getShell(), wizard);
			if(dialog.open() == Dialog.OK){
				if (collections instanceof FeatureMap.Entry) {
					collections = FeatureMapUtil.createEntry(((FeatureMap.Entry) collections)
							.getEStructuralFeature(), wizard.getXslModel());
				}
				emfCommand = AddCommand.create(domain, owner, feature, collections);
			}else{
				throw new IgnoreException();
			}
			firstTime = false;
		}
		super.execute();
	}
}

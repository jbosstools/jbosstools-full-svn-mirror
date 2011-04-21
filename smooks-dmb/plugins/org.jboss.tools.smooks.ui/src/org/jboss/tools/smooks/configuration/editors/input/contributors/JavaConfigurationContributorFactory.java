/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.input.contributors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributor;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributorFactory;
import org.jboss.tools.smooks.configuration.editors.uitls.JavaTypeFieldDialog;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.util.TextFieldChangeListener;
import org.milyn.javabean.dynamic.Model;

/**
 * Java Source configuration contributor factory.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class JavaConfigurationContributorFactory implements InputTaskPanelContributorFactory {

	public InputTaskPanelContributor newInstance(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
		return new PanelContributor(toolkit, editorPart, configModel);
	}

	public void setInputSourceType(InputSourceType inputSourceType) {
	}

	private class PanelContributor extends AbstractContributor {
		
		private Composite inputSourceConfigComposite;
		private Text classTextField;

		public PanelContributor(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
			super(editorPart, configModel);
		}

		public void setInputSourceConfigComposite(Composite inputSourceConfigComposite) {
			this.inputSourceConfigComposite = inputSourceConfigComposite;
		}

		public void addInputSourceTypeConfigControls() {
			Composite javaClassSelectionSection = new Composite(inputSourceConfigComposite, SWT.NONE);
			GridLayout layout = new GridLayout();
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);

			layout.numColumns = 3;
			javaClassSelectionSection.setLayout(layout);
			javaClassSelectionSection.setLayoutData(gd);

			new Label(javaClassSelectionSection, SWT.NONE).setText(Messages.JavaConfigurationContributorFactory_Class_Name);
			
			gd = new GridData(GridData.FILL_HORIZONTAL);
			classTextField = new Text(javaClassSelectionSection, SWT.BORDER);
			IParam inputParam = getInputParam(SmooksModelUtils.INPUT_TYPE_JAVA);
			if(inputParam != null) {
				classTextField.setText(inputParam.getValue());
			}
			new TextFieldChangeListener(classTextField) {
				public void fieldChanged(String newValue) {
					setInputClass();
				}				
			};
			classTextField.setLayoutData(gd);
			
			Button browseButton = new Button(javaClassSelectionSection, SWT.NONE);
			browseButton.setText(Messages.ConfigurationContributorFactory_Browse_Button);
			browseButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browseForClass();
				}
			});
			
			inputSourceConfigComposite.setBackgroundMode(SWT.TRANSPARENT);
			inputSourceConfigComposite.layout();			
		}

		protected void browseForClass() {
			IJavaProject javaProject = JavaCore.create(((IFileEditorInput)getEditorPart().getEditorInput()).getFile().getProject());
			String classString = JavaTypeFieldDialog.openJavaTypeDialog(getEditorPart().getEditorSite().getShell(), javaProject);
			
			if(classString != null) {
				classTextField.setText(classString);
				setInputClass();
			}
		}

		protected void setInputClass() {
			CompoundCommand compoundCommand = new CompoundCommand();
			
			setInputParam(SmooksModelUtils.INPUT_TYPE_JAVA, classTextField.getText(), compoundCommand);
			if(!compoundCommand.isEmpty()) {
				getEditingDomain().getCommandStack().execute(compoundCommand);	
			}
		}
	}
}

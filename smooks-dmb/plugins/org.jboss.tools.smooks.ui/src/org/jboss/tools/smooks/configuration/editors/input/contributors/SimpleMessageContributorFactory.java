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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributor;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributorFactory;
import org.jboss.tools.smooks.model.SmooksModel;
import org.milyn.javabean.dynamic.Model;

/**
 * Simple contributor used to add a message to the configuration composite.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SimpleMessageContributorFactory implements InputTaskPanelContributorFactory {

	private String message;

	public SimpleMessageContributorFactory(String message) {
		this.message = message;
	}

	public void setInputSourceType(InputSourceType inputSourceType) {
	}

	public InputTaskPanelContributor newInstance(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
		return new PanelContributor(toolkit);
	}
	
	private class PanelContributor implements InputTaskPanelContributor {

		private FormToolkit toolkit;
		private Composite inputSourceConfigComposite;

		public PanelContributor(FormToolkit toolkit) {
			this.toolkit = toolkit;
		}

		public void setInputSourceConfigComposite(Composite inputSourceConfigComposite) {
			this.inputSourceConfigComposite = inputSourceConfigComposite;
		}

		public void addInputSourceTypeConfigControls() {
			Label formText = toolkit.createLabel(inputSourceConfigComposite, ""); //$NON-NLS-1$
			GridData gd = new GridData(GridData.FILL_BOTH);
			
			gd.heightHint = 50;
			gd.horizontalSpan = 2;
			formText.setLayoutData(gd);
			formText.setText(message);

			inputSourceConfigComposite.layout();
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.gwt.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.jboss.tools.gwt.core.GWTInstallDataModelProvider;

/**
 * @author adietish
 */
public class GWTInstallFacetWizardPage extends AbstractFacetWizardPage implements IFacetWizardPage {

	private static final String WIZARD_PAGE_ID = "jboss.gwt.install.page"; //$NON-NLS-1$

	GWTInstallDataModelProvider gwtInstallDataModel;

	private boolean generateSampleCode;

	public GWTInstallFacetWizardPage() {
		super(WIZARD_PAGE_ID); 
		setTitle("Google Web Toolkit");
		setDescription("Configure project for using Google Web Toolkit.");
	}

	public void setConfig(Object gwtInstallDataModel) {
		this.gwtInstallDataModel = (GWTInstallDataModelProvider) gwtInstallDataModel;
	}

	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Generate sample code:");
		Button checkBox = new Button(composite, SWT.CHECK);
		checkBox.addSelectionListener(onCheckboxSelected(checkBox));
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(checkBox);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(6, 6).applyTo(composite);

		setControl(composite);
	}

	private SelectionAdapter onCheckboxSelected(final Button checkBox) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GWTInstallFacetWizardPage.this.generateSampleCode = checkBox.getSelection();
			}
		};
	}

	@Override
	public void transferStateToConfig() {
		gwtInstallDataModel.setGenerateSampleCode(generateSampleCode);
	}
}

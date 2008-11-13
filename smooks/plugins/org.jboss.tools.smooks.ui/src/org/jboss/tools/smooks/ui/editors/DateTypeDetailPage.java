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
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author Dart Peng<br>
 *         Date : Sep 16, 2008
 */
public class DateTypeDetailPage extends AbstractSmooksModelDetailPage {

	private Text formatText;
	private Combo localeLangaugeCombo;
	private Combo localeContryCombo;

	public DateTypeDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		super(parentEditor, domain);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isStale() {
		return true;
	}

	@Override
	protected void createSectionContents(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		this.formToolKit.createLabel(parent, Messages.getString("DateTypeDetailPage.DateTypeFormatText")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		formatText = this.formToolKit.createText(parent, ""); //$NON-NLS-1$
		formatText.setLayoutData(gd);

		this.formToolKit.createLabel(parent, Messages.getString("DateTypeDetailPage.DateTypeLocaleLanguageText")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		localeLangaugeCombo = new Combo(parent, SWT.FLAT);
		localeLangaugeCombo.setLayoutData(gd);

		this.formToolKit.createLabel(parent, Messages.getString("DateTypeDetailPage.DateTypeLocaleContryText")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		localeContryCombo = new Combo(parent, SWT.FLAT);
		localeContryCombo.setLayoutData(gd);

		formToolKit.paintBordersFor(parent);
	}

	@Override
	protected void initSectionUI() {
		if(this.resourceConfigList != null){
			String formate = SmooksModelUtils.getParmaText("format", resourceConfigList); //$NON-NLS-1$
			String locallang = SmooksModelUtils.getParmaText("Locale-Language", resourceConfigList); //$NON-NLS-1$
			String localcontry = SmooksModelUtils.getParmaText("Locale-Contry", resourceConfigList); //$NON-NLS-1$
			if(formate == null) formate = ""; //$NON-NLS-1$
			if(locallang == null) locallang = ""; //$NON-NLS-1$
			if(localcontry == null) localcontry = ""; //$NON-NLS-1$
			
			
			formatText.setText(formate);
			localeContryCombo.setText(localcontry);
			localeLangaugeCombo.setText(locallang);
		}
	}

}

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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author Dart Peng<br>
 *         Date : Sep 16, 2008
 */
public class DateTypeDetailPage extends AbstractSmooksModelDetailPage {

	private static final String DECODER = "decoder";
	private Text formatText;
	private Combo localeLangaugeCombo;
	private Combo localeContryCombo;
	private Text selectorText;
	private Combo decoderCombo;

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
		
		this.formToolKit.createLabel(parent, "Name :");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		selectorText = this.formToolKit.createText(parent, ""); //$NON-NLS-1$
		selectorText.setLayoutData(gd);
		
		this.formToolKit.createLabel(parent, "Decoder Class :");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		decoderCombo = new Combo(parent, SWT.FLAT);
		decoderCombo.setLayoutData(gd);
		
		this.formToolKit.createLabel(parent, Messages.getString("DateTypeDetailPage.DateTypeFormatText")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
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
		
		initComboBox();
		hookControls();
	}

	private void initComboBox() {
		for (int i = 0; i < SmooksModelConstants.DECODER_CLASSES.length; i++) {
			decoderCombo.add(SmooksModelConstants.DECODER_CLASSES[i]);
		}
	}

	private void hookControls() {
		
		decoderCombo.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				setDecoderClass();
			}
			
		});
		
		selectorText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				setSelector();
			}
		});
		
		formatText.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				setFormat();
			}
			
		});
		
		localeContryCombo.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				setLocalContry();
			}
			
		});
		
		localeLangaugeCombo.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				setLocalLang();
			}
			
		});
	}
	
	protected void setDecoderClass() {
		String text = decoderCombo.getText();
		if(text == null) text = "";
		if(this.resourceConfig != null){
			ResourceType resource = resourceConfig.getResource();
			if(resource == null){
				resource = SmooksFactory.eINSTANCE.createResourceType();
				resourceConfig.setResource(resource);
			}
			if(text.trim().equals(resource.getStringValue())){
				return;
			}
			resource.setStringValue(text);
			this.parentEditor.fireEditorDirty(true);
		}
		
	}

	protected void setFormat() {
		String format = formatText.getText();
		if(format == null) format = "";
		SmooksModelUtils.setParamText("format", format, resourceConfig);
		this.parentEditor.fireEditorDirty(true);
	}

	protected void setLocalLang() {
		String ll = localeLangaugeCombo.getText();
		if(ll == null) ll = "";
		SmooksModelUtils.setParamText("locale-language", ll, resourceConfig);
		this.parentEditor.fireEditorDirty(true);
	}

	protected void setLocalContry() {
		String lc = localeContryCombo.getText();
		if(lc == null) lc = "";
		SmooksModelUtils.setParamText("locale-country", lc, resourceConfig);
		this.parentEditor.fireEditorDirty(true);
	}

	protected void setSelector() {
		String text = selectorText.getText();
		if(text == null || text.length() == 0){
			resourceConfig.setSelector("");
			this.parentEditor.fireEditorDirty(true);
			return;
		}
		text = DECODER+ ":" + text;
		if(this.resourceConfig != null){
			resourceConfig.setSelector(text);
			this.parentEditor.fireEditorDirty(true);
		}
	}

	private String getSelectorName(String selector){
		if(selector == null) return "";
		if(selector.indexOf(":") != -1){
			return selector.substring(selector.indexOf(":") + 1,selector.length());
		}
		return selector;
	}

	@Override
	protected void initSectionUI() {
		if(this.resourceConfig != null){
			String formate = SmooksModelUtils.getParmaText("format", resourceConfig); //$NON-NLS-1$
			String locallang = SmooksModelUtils.getParmaText("locale-language", resourceConfig); //$NON-NLS-1$
			String localcontry = SmooksModelUtils.getParmaText("locale-country", resourceConfig); //$NON-NLS-1$
			String decoderClass = "";
			ResourceType resource = resourceConfig.getResource();
			if(resource != null){
				decoderClass = resource.getStringValue();
				if(decoderClass != null) decoderClass = decoderClass.trim();
			}
			if(formate == null) formate = ""; //$NON-NLS-1$
			if(locallang == null) locallang = ""; //$NON-NLS-1$
			if(localcontry == null) localcontry = ""; //$NON-NLS-1$
			
			String selector = resourceConfig.getSelector();
			selector = getSelectorName(selector);
			selectorText.setText(selector);
			formatText.setText(formate);
			localeContryCombo.setText(localcontry);
			localeLangaugeCombo.setText(locallang);
			decoderCombo.setText(decoderClass);
		}
	}

}

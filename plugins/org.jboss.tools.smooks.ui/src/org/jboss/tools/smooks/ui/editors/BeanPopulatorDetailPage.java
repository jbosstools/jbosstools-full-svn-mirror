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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.milyn.xsd.smooks.util.SmooksModelUtils;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class BeanPopulatorDetailPage extends AbstractSmooksModelDetailPage {

	private Text selectorText;
	private Text beanClassText;
	private Text beanIDText;
	private Button clazzBrowseButton;
	private Button idBrowseButton;

	public BeanPopulatorDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		super(parentEditor, domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.ui.editors.AbstractSmooksModelDetailPage#createSectionContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createSectionContents(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);

		this.formToolKit.createLabel(parent, "Selector : ");
		selectorText = formToolKit.createText(parent, "");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		selectorText.setLayoutData(gd);

		this.formToolKit.createLabel(parent, "Bean Class : ");
		Composite beanClassComposite = formToolKit.createComposite(parent);
		GridLayout bcgl = new GridLayout();
		bcgl.numColumns = 2;
		bcgl.marginHeight = 0;
		bcgl.marginWidth = 1;
		beanClassComposite.setLayout(bcgl);
		beanClassText = formToolKit.createText(beanClassComposite, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanClassText.setLayoutData(gd);

		clazzBrowseButton = formToolKit.createButton(beanClassComposite,
				"Browse", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanClassComposite.setLayoutData(gd);
		formToolKit.paintBordersFor(beanClassComposite);
		
		this.formToolKit.createLabel(parent, "Bean ID : ");
		Composite beanIDComposite = formToolKit.createComposite(parent);
		GridLayout bilg = new GridLayout();
		bilg.numColumns = 2;
		bilg.marginHeight = 0;
		bilg.marginWidth = 1;
		beanIDComposite.setLayout(bilg);

		beanIDText = formToolKit.createText(beanIDComposite, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanIDText.setLayoutData(gd);

		idBrowseButton = formToolKit.createButton(beanIDComposite, "Browse",
				SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanIDComposite.setLayoutData(gd);
		formToolKit.paintBordersFor(beanIDComposite);
		formToolKit.paintBordersFor(parent);
		configControls();
		hookContorls();
	}

	protected void idBrowseButtonSelected() {

	}

	protected void clazzBrowseButtonSelected() {

	}
	
	

	@Override
	public boolean isStale() {
		return true;
	}

	@Override
	public void refresh() {
		configControls();
	}

	private void hookContorls() {
		selectorText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				selectorChanged();
			}

		});
		beanClassText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				beanClassChanged();
			}

		});
		clazzBrowseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				clazzBrowseButtonSelected();
			}

		});
		beanIDText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				beanIDChanged();
			}

		});
		idBrowseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				idBrowseButtonSelected();
			}

		});
	}

	private void configControls() {
	
		if (resourceConfigList != null) {
			String selector = "";
			selector = resourceConfigList.getSelector();
			if (selector == null)
				selector = "";
			selectorText.setText(selector);
			
			String beanClass = SmooksModelUtils.getParmaText("beanClass", resourceConfigList);
			if(beanClass == null) beanClass = "";
			String beanId = SmooksModelUtils.getParmaText("beanId", resourceConfigList);
			if(beanId == null) beanId = "";
			
			beanClassText.setText(beanClass);
			beanIDText.setText(beanId);
		}
	
	}

	protected void beanIDChanged() {

	}

	protected void beanClassChanged() {

	}

	protected void selectorChanged() {

	}

}

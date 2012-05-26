/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.resref.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TaglibReferenceWizardPage extends ReferenceWizardPage {

	private Text taglibUri = null;
	private Text taglibPrefix = null;
	private String taglibUriStr = ""; //$NON-NLS-1$
	private String taglibPrefixStr = ""; //$NON-NLS-1$
	
	public TaglibReferenceWizardPage(String pageName, String title,
			ImageDescriptor titleImage, Object fileLocation) {
		super(pageName, title, titleImage, fileLocation);
	}

	public void createControl(Composite parent) {
		Composite pageControl = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		pageControl.setLayout(gridLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		pageControl.setLayoutData(gd);
		
		/*
		 * Create first row
		 * 1) Create label
		 */
		Label pathLabel = new Label(pageControl, SWT.RIGHT);
		pathLabel.setText(Messages.TAGLIB_URI);
		gd = new GridData();
		pathLabel.setLayoutData(gd);
		
		/*
		 * 2) Create text field 
		 */
		taglibUri = new Text(pageControl, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		taglibUri.setLayoutData(gd);
		taglibUri.setText(taglibUriStr);
		taglibUri.addListener(SWT.Selection, this);
		taglibUri.addListener(SWT.Modify, this);
		
		/*
		 * Create empty label
		 */
		Label emptyLabel1 = new Label(pageControl, SWT.NONE);
		
		/*
		 * Create scope group
		 */
		Control groupControl = createScopeGroup(pageControl);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		groupControl.setLayoutData(gd); 
		
		/*
		 * Create third row
		 */
		Label prefixLabel = new Label(pageControl, SWT.RIGHT);
		prefixLabel.setText(Messages.TAGLIB_PREFIX);
		gd = new GridData();
		prefixLabel.setLayoutData(gd);
		taglibPrefix = new Text(pageControl, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		taglibPrefix.setLayoutData(gd);
		taglibPrefix.setText(taglibPrefixStr);
		taglibPrefix.addListener(SWT.Selection, this);
		taglibPrefix.addListener(SWT.Modify, this);
		
		/*
		 * Dialog's control should be initialized.
		 */
		setControl(pageControl);
	}

	@Override
	protected ResourceReferenceValidator getUpdatedValidator() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put(TaglibResourceReferenceValidator.TAGLIB_URI, getTaglibUri());
		fields.put(TaglibResourceReferenceValidator.TAGLIB_PREFIX, getTaglibPrefix());
		if (null == validator) {
			validator = new TaglibResourceReferenceValidator(fields);
		} else {
			validator.setFields(fields);
		}
		return validator;
	}
	
	public String getTaglibUri() {
		if ((null != taglibPrefix) && (null != taglibPrefix.getText())) {
			taglibPrefixStr = taglibUri.getText().trim();
		}
		return taglibPrefixStr;
	}

	public String getTaglibPrefix() {
		if ((null != taglibUri) && (null != taglibUri.getText())) {
			taglibUriStr = taglibPrefix.getText().trim();
		}
		return taglibUriStr;
	}

	@Override
	protected String getLocation() {
		return getTaglibUri();
	}

	@Override
	protected String getProperties() {
		return getTaglibPrefix();
	}

	@Override
	protected void setLocation(String location) {
		taglibUriStr = location;
	}

	@Override
	protected void setProperties(String properties) {
		taglibPrefixStr = properties;
	}
	
	
	
}

/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.central.editors;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author snjeza
 *
 */
public class DescriptionToolTip extends ToolTip {

	private String description;

	public DescriptionToolTip(Control control, String description) {
		super(control);
		this.description = description;
		setHideOnMouseDown(true);
		setPopupDelay(400);
		
	}

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) {
		// Create the content area
		parent.setLayout(new GridLayout());
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 2;
		layout.marginLeft = 2;
		composite.setLayout(layout);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd);
		composite.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_INFO_BACKGROUND));
		Text text = new Text(composite,
				SWT.READ_ONLY | SWT.WRAP);
		text.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		text.setText(description);
		
		return composite;
	}

}

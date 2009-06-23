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
package org.jboss.tools.smooks.configuration.editors.uitls;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class TextTypeSwicher {

	private Button textButton;
	private Button cdataButton;

	public void hookSwicher(AttributeFieldEditPart textFieldEditPart, AttributeFieldEditPart cdataFieldEditPart,
			AdapterFactoryEditingDomain editingdomain, Object model) {
		String cdata = SmooksModelUtils.getAnyTypeCDATA((AnyType) model);
		String text = SmooksModelUtils.getAnyTypeText((AnyType) model);

		textButton.setSelection(true);
		cdataButton.setSelection(false);
		if ((cdata == null || cdata.length() == 0) && (text == null || text.length() == 0)) {
			textButton.setSelection(true);
			cdataButton.setSelection(false);
		} else {
			if ((text == null || text.length() == 0)) {
				textButton.setSelection(false);
				cdataButton.setSelection(true);
			}
		}
		final AttributeFieldEditPart tp = textFieldEditPart;
		final AttributeFieldEditPart cp = cdataFieldEditPart;
		tp.getContentControl().setEnabled(textButton.getSelection());
		cp.getContentControl().setEnabled(cdataButton.getSelection());
		final AnyType fm = (AnyType) model;
		final AdapterFactoryEditingDomain fd = editingdomain;
		textButton.addSelectionListener(new SelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected
			 * (org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				tp.getContentControl().setEnabled(textButton.getSelection());
				cp.getContentControl().setEnabled(cdataButton.getSelection());
				SmooksModelUtils.setCDATAToSmooksType(fd, fm, null);
				Text t = (Text) tp.getContentControl();
				String text = t.getText();
				if (text != null) {
					text = text.replaceAll("\r", "");
				}
				if (text.length() == 0)
					text = null;
				SmooksModelUtils.setTextToSmooksType(fd, fm, text);
			}

		});

		cdataButton.addSelectionListener(new SelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected
			 * (org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				tp.getContentControl().setEnabled(textButton.getSelection());
				cp.getContentControl().setEnabled(cdataButton.getSelection());
				SmooksModelUtils.setTextToSmooksType(fd, fm, null);
				Text t = (Text) cp.getContentControl();
				String text = t.getText();
				if (text != null) {
					text = text.replaceAll("\r", "");
				}
				if (text.length() == 0)
					text = null;
				SmooksModelUtils.setCDATAToSmooksType(fd, fm, text);
			}

		});
	}

	public void createSwicherGUI(String textString, String cdataString, Composite parent, FormToolkit toolkit) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 15;

		toolkit.createSeparator(parent, SWT.HORIZONTAL).setLayoutData(gd);

		textButton = toolkit.createButton(parent, textString, SWT.RADIO);
		cdataButton = toolkit.createButton(parent, cdataString, SWT.RADIO);
	}
}

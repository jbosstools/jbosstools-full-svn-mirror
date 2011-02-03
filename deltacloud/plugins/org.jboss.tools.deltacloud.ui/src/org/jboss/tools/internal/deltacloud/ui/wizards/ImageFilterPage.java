/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

/**
 * @author Jeff Johnston
 */
public class ImageFilterPage extends WizardPage {

	private final static String NAME = "ImageFilter.name"; //$NON-NLS-1$
	private final static String TITLE = "ImageFilter.title"; //$NON-NLS-1$
	private final static String DESC = "ImageFilter.desc"; //$NON-NLS-1$
	private final static String FILTER_LABEL = "ImageFilter.label"; //$NON-NLS-1$
	private final static String EMPTY_RULE = "ErrorFilterEmptyRule.msg"; //$NON-NLS-1$
	private final static String INVALID_SEMICOLON = "ErrorFilterSemicolon.msg"; //$NON-NLS-1$
	private final static String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private final static String ID_LABEL = "Id.label"; //$NON-NLS-1$
	private final static String ARCH_LABEL = "Arch.label"; //$NON-NLS-1$
	private final static String DESC_LABEL = "Desc.label"; //$NON-NLS-1$
	private final static String DEFAULT_LABEL = "DefaultButton.label"; //$NON-NLS-1$

	private DeltaCloud cloud;
	private Text nameText;
	private Text idText;
	private Text archText;
	private Text descText;

	private Button defaultName;
	private Button defaultId;
	private Button defaultArch;
	private Button defaultDesc;

	public ImageFilterPage(DeltaCloud cloud) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		setDescription(WizardMessages.getString(DESC));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}

	public String getNameRule() {
		return nameText.getText();
	}

	public String getIdRule() {
		return idText.getText();
	}

	public String getArchRule() {
		return archText.getText();
	}

	public String getDescRule() {
		return descText.getText();
	}

	private ModifyListener Listener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			// TODO Auto-generated method stub
			validate();
		}
	};

	private SelectionAdapter ButtonListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button) e.widget;
			Text text = getTextWidget(b);
			if (text != null) {
				text.setText(ICloudElementFilter.ALL_MATCHER_EXPRESSION);
			}
		}

		private Text getTextWidget(Button button) {
			Text text = null;
			if (button == defaultName) {
				text = nameText;
			}
			else if (button == defaultId) {
				text = idText;
			}
			else if (button == defaultArch) {
				text = archText;
			}
			else if (button == defaultDesc) {
				text = descText;
			}
			return text;
		}

	};

	private void validate() {
		boolean complete = true;
		boolean error = false;

		if (nameText.getText().length() == 0 ||
				idText.getText().length() == 0 ||
				archText.getText().length() == 0 ||
				descText.getText().length() == 0) {

			setErrorMessage(WizardMessages.getString(EMPTY_RULE));
			error = true;
		} else if (nameText.getText().contains(";") ||
				idText.getText().contains(";") ||
				archText.getText().contains(";") ||
				descText.getText().contains(";")) {
			setErrorMessage(WizardMessages.getString(INVALID_SEMICOLON));
			error = true;
		}

		if (!error)
			setErrorMessage(null);
		setPageComplete(complete && !error);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Label label = new Label(container, SWT.NULL);
		label.setText(WizardMessages.getString(FILTER_LABEL));

		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setText(cloud.getImageFilter().getNameRule().toString());
		nameText.addModifyListener(Listener);

		defaultName = new Button(container, SWT.NULL);
		defaultName.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultName.addSelectionListener(ButtonListener);

		Label idLabel = new Label(container, SWT.NULL);
		idLabel.setText(WizardMessages.getString(ID_LABEL));

		idText = new Text(container, SWT.BORDER | SWT.SINGLE);
		idText.setText(cloud.getImageFilter().getIdRule().toString());
		idText.addModifyListener(Listener);

		defaultId = new Button(container, SWT.NULL);
		defaultId.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultId.addSelectionListener(ButtonListener);

		Label archLabel = new Label(container, SWT.NULL);
		archLabel.setText(WizardMessages.getString(ARCH_LABEL));

		archText = new Text(container, SWT.BORDER | SWT.SINGLE);
		archText.setText(cloud.getImageFilter().getArchRule().toString());
		archText.addModifyListener(Listener);

		defaultArch = new Button(container, SWT.NULL);
		defaultArch.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultArch.addSelectionListener(ButtonListener);

		Label descLabel = new Label(container, SWT.NULL);
		descLabel.setText(WizardMessages.getString(DESC_LABEL));

		descText = new Text(container, SWT.BORDER | SWT.SINGLE);
		descText.setText(cloud.getImageFilter().getDescRule().toString());
		descText.addModifyListener(Listener);

		defaultDesc = new Button(container, SWT.NULL);
		defaultDesc.setText(WizardMessages.getString(DEFAULT_LABEL));
		defaultDesc.addSelectionListener(ButtonListener);

		Point p1 = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = nameText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p3 = defaultName.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;
		int centering2 = (p3.y - p2.y + 1) / 2;

		FormData f = new FormData();
		f.top = new FormAttachment(0);
		label.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(label, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		nameLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(label, 11);
		f.right = new FormAttachment(100);
		defaultName.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(label, 11 + centering2);
		f.left = new FormAttachment(archLabel, 5);
		f.right = new FormAttachment(defaultName, -10);
		nameText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		idLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameLabel, 11);
		f.right = new FormAttachment(100);
		defaultId.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(nameLabel, 11 + centering2);
		f.left = new FormAttachment(archLabel, 5);
		f.right = new FormAttachment(defaultId, -10);
		idText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(idLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		archLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(idLabel, 11);
		f.right = new FormAttachment(100);
		defaultArch.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(idLabel, 11 + centering2);
		f.left = new FormAttachment(archLabel, 5);
		f.right = new FormAttachment(defaultArch, -10);
		archText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(archLabel, 11 + centering + centering2);
		f.left = new FormAttachment(0, 0);
		descLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(archLabel, 11);
		f.right = new FormAttachment(100);
		defaultDesc.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(archLabel, 11 + centering2);
		f.left = new FormAttachment(archLabel, 5);
		f.right = new FormAttachment(defaultDesc, -10);
		descText.setLayoutData(f);

		setControl(container);
		setPageComplete(true);
	}

}

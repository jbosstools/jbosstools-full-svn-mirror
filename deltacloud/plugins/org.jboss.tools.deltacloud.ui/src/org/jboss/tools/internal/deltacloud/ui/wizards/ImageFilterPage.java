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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.fieldassist.ControlDecoration;
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
import org.jboss.tools.deltacloud.core.IFieldMatcher;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

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
	private ControlDecoration nameDecoration;
	private Button defaultName;
	private Text idText;
	private ControlDecoration idDecoration;
	private Button defaultId;
	private Text archText;
	private ControlDecoration archDecoration;
	private Button defaultArch;
	private Text descText;
	private ControlDecoration descDecoration;
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

	private ModifyListener textListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			validate();
		}
	};

	private SelectionAdapter buttonListener = new SelectionAdapter() {
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
		nameDecoration.hide();
		idDecoration.hide();
		archDecoration.hide();
		descDecoration.hide();
		String error = null;
		error = validate(nameText, nameDecoration, error);
		error = validate(idText, idDecoration, error);
		error = validate(archText, archDecoration, error);
		error = validate(descText, descDecoration, error);
		setPageComplete(error == null);
		setErrorMessage(error);
	}
	
	private String validate(Text text, ControlDecoration decoration, String formError) {
		String error = null;
		if (text.getText().length() == 0) {
			error = WizardMessages.getString(EMPTY_RULE);
		} else if (text.getText().contains(ICloudElementFilter.EXPRESSION_DELIMITER)) {
			error = WizardMessages.getString(INVALID_SEMICOLON);
		}
		if (error != null) {
			decoration.setDescriptionText(error);
			decoration.show();
			setPageComplete(false);
			setErrorMessage(error);
			return error;
		} else {
			return formError;
		}
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		IImageFilter filter = cloud.getImageFilter();

		Label label = new Label(container, SWT.NULL);
		label.setText(WizardMessages.getString(FILTER_LABEL));

		Label nameLabel = createRuleLabel(WizardMessages.getString(NAME_LABEL), container);
		this.nameText = createRuleText(filter.getNameRule(), container);
		this.nameDecoration = UIUtils.createErrorDecoration("", nameText);
		this.defaultName = createDefaultRuleButton(container);

		Label idLabel = createRuleLabel(WizardMessages.getString(ID_LABEL), container);
		this.idText = createRuleText(filter.getIdRule(), container);
		this.idDecoration = UIUtils.createErrorDecoration("", idText);
		this.defaultId = createDefaultRuleButton(container);

		Label archLabel = createRuleLabel(WizardMessages.getString(ARCH_LABEL), container);
		this.archText = createRuleText(filter.getArchRule(), container);
		this.archDecoration = UIUtils.createErrorDecoration(WizardMessages.getString(""), archText);
		this.defaultArch = createDefaultRuleButton(container);

		Label descLabel = createRuleLabel(WizardMessages.getString(DESC_LABEL), container);
		this.descText = createRuleText(filter.getDescRule(), container);
		this.descDecoration = UIUtils.createErrorDecoration("", descText);
		this.defaultDesc = createDefaultRuleButton(container);

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

	private Label createRuleLabel(String text, Composite container) {
		Label label = new Label(container, SWT.NULL);
		label.setText(text);
		return label;
	}

	private Button createDefaultRuleButton(final Composite container) {
		Button button = new Button(container, SWT.NULL);
		button.setText(WizardMessages.getString(DEFAULT_LABEL));
		button.addSelectionListener(buttonListener);
		return button;
	}

	private Text createRuleText(IFieldMatcher rule, final Composite container) {
		Assert.isNotNull(rule, "Rule may not be null");

		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		text.setText(rule.toString());
		text.addModifyListener(textListener);
		return text;
	}

}

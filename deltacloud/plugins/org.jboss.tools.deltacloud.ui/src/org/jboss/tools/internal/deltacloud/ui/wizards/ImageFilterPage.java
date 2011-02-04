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

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Jeff Johnston
 */
public class ImageFilterPage extends AbstractFilterPage {

	private final static String NAME = "ImageFilter.name"; //$NON-NLS-1$
	private final static String TITLE = "ImageFilter.title"; //$NON-NLS-1$
	private final static String DESC = "ImageFilter.desc"; //$NON-NLS-1$
	private final static String FILTER_LABEL = "ImageFilter.label"; //$NON-NLS-1$
	private final static String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private final static String ID_LABEL = "Id.label"; //$NON-NLS-1$
	private final static String ARCH_LABEL = "Arch.label"; //$NON-NLS-1$
	private final static String DESC_LABEL = "Desc.label"; //$NON-NLS-1$

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
		super(WizardMessages.getString(NAME), WizardMessages.getString(TITLE), WizardMessages.getString(DESC), cloud);
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

	@Override
	protected Text getTextWidget(Button button) {
		Text text = null;
		if (button == defaultName) {
			text = nameText;
		} else if (button == defaultId) {
			text = idText;
		} else if (button == defaultArch) {
			text = archText;
		} else if (button == defaultDesc) {
			text = descText;
		}
		return text;
	}

	@Override
	protected void validate() {
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

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		IImageFilter filter = getDeltaCloud().getImageFilter();

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
}

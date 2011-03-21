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
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		GridLayoutFactory.fillDefaults().numColumns(3).spacing(8, 4).applyTo(container);

		IImageFilter filter = getDeltaCloud().getImageFilter();

		Label label = new Label(container, SWT.NULL);
		label.setText(WizardMessages.getString(FILTER_LABEL));
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.LEFT, SWT.CENTER).indent(0, 14).hint(SWT.DEFAULT, 30)
		.applyTo(label);

		Label nameLabel = createRuleLabel(WizardMessages.getString(NAME_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(nameLabel);
		this.nameText = createRuleText(filter.getNameRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(nameText);
		this.nameDecoration = UIUtils.createErrorDecoration("", nameText);
		this.defaultName = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(defaultName);

		Label idLabel = createRuleLabel(WizardMessages.getString(ID_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(idLabel);
		this.idText = createRuleText(filter.getIdRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(idText);
		this.idDecoration = UIUtils.createErrorDecoration("", idText);
		this.defaultId = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(defaultId);

		Label archLabel = createRuleLabel(WizardMessages.getString(ARCH_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(archLabel);
		this.archText = createRuleText(filter.getArchRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(archText);
		this.archDecoration = UIUtils.createErrorDecoration(WizardMessages.getString(""), archText);
		this.defaultArch = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(defaultArch);

		Label descLabel = createRuleLabel(WizardMessages.getString(DESC_LABEL), container);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(descLabel);
		this.descText = createRuleText(filter.getDescRule(), container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(descText);
		this.descDecoration = UIUtils.createErrorDecoration("", descText);
		this.defaultDesc = createDefaultRuleButton(container);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(defaultDesc);

		Label dummyLabel = new Label(container, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.RIGHT, SWT.CENTER).applyTo(dummyLabel);
		Button defaultAllButton = new Button(container, SWT.BORDER);
		defaultAllButton.setText("Default All");
		GridDataFactory.fillDefaults().indent(0, 10).align(SWT.RIGHT, SWT.BOTTOM).applyTo(defaultAllButton);
		defaultAllButton.addSelectionListener(onDefaultAllPressed());

		setControl(container);
		setPageComplete(true);
	}
	
	private SelectionListener onDefaultAllPressed() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				resetFilter(nameText);
				resetFilter(idText);
				resetFilter(archText);
				resetFilter(descText);
			}
		};
	}

}

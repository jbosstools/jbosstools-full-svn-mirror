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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.IFieldMatcher;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public abstract class AbstractFilterPage extends WizardPage {

	protected final static String EMPTY_RULE = "ErrorFilterEmptyRule.msg"; //$NON-NLS-1$
	protected final static String INVALID_SEMICOLON = "ErrorFilterSemicolon.msg"; //$NON-NLS-1$

	protected final static String DEFAULT_LABEL = "DefaultButton.label"; //$NON-NLS-1$
	protected final static String DEFAULT_ALL_LABEL = "DefaultAllButton.label"; //$NON-NLS-1$

	private DeltaCloud cloud;

	public AbstractFilterPage(String name, String title, String description, DeltaCloud cloud) {
		super(name);
		this.cloud = cloud;
		setTitle(title);
		setDescription(description);
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
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
				resetFilter(text);
			}
		}
	};

	protected void resetFilter(Text text) {
		text.setText(ICloudElementFilter.ALL_MATCHER_EXPRESSION);
	}

	protected abstract Text getTextWidget(Button button);
	
	protected abstract void validate();
	
	protected String validate(Text text, ControlDecoration decoration, String formError) {
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

	protected Label createRuleLabel(String text, Composite container) {
		Label label = new Label(container, SWT.NULL);
		label.setText(text);
		return label;
	}

	protected Button createDefaultRuleButton(final Composite container) {
		Button button = new Button(container, SWT.NULL);
		button.setText(WizardMessages.getString(DEFAULT_LABEL));
		button.addSelectionListener(buttonListener);
		return button;
	}

	protected Text createRuleText(IFieldMatcher rule, final Composite container) {
		Assert.isNotNull(rule, "Rule may not be null");

		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		text.setText(rule.toString());
		text.addModifyListener(textListener);
		return text;
	}

	protected DeltaCloud getDeltaCloud() {
		return cloud;
	}
}

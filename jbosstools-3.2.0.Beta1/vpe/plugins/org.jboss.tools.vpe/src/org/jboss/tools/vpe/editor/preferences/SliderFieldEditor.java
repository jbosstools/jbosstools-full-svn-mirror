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
package org.jboss.tools.vpe.editor.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

public class SliderFieldEditor extends FieldEditor {

	private static final String SLIDER_LABEL_DEFAULT_TEXT = "50% "; //$NON-NLS-1$
	private static final String SLIDER_LABEL_MAX_SIZE_TEXT = "100% "; //$NON-NLS-1$
	private static final int MAX_SLIDER_VALUE = 1000;
	private static final int MIN_SLIDER_VALUE = 0;
	private static final int INCREMENT_SLIDER_VALUE = 10;
	private static final int DEFAULT_SLIDER_VALUE = 500;
	
	private Composite sliderComposite;
	private Label sliderLabel;
	private Slider slider;
	private int value;
	
	public SliderFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		if (numColumns > 1) {
			Control control = getLabelControl();
			int left = numColumns;
			if (control != null) {
				((GridData)control.getLayoutData()).horizontalSpan = 1;
				left = left - 1;
			}
			if (sliderComposite != null) {
				((GridData)sliderComposite.getLayoutData()).horizontalSpan = left;
			}
		} else {
			Control control = getLabelControl();
			if (control != null) {
				((GridData)control.getLayoutData()).horizontalSpan = 1;
			}
			if (sliderComposite != null) {
				((GridData)sliderComposite.getLayoutData()).horizontalSpan = 1;
			}
		}
	}

	@Override
	protected void createControl(Composite parent) {
		doFillIntoGrid(parent, 2);
	}
	
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		int sliderSpan = 1;
		if (numColumns > 1) {
			sliderSpan = numColumns - 1;
		}
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
		control = getSliderComposite(parent);
		gd = new GridData();
		gd.horizontalSpan = sliderSpan;
		gd.horizontalAlignment = GridData.FILL;
		control.setLayoutData(gd);
		control.setFont(parent.getFont());
	}

	@Override
	protected void doLoad() {
		updateSliderForValue(getPreferenceStore().getInt(getPreferenceName()));
	}

	@Override
	protected void doLoadDefault() {
		updateSliderForValue(getPreferenceStore().getDefaultInt(getPreferenceName()));
	}

	@Override
	protected void doStore() {
		if (value < 0) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		}
		getPreferenceStore().setValue(getPreferenceName(), value);
	}

	@Override
	public int getNumberOfControls() {
		return 2;
	}
	
	private Composite getSliderComposite(Composite parent) {
		if (sliderComposite == null) {
			/*
			 * Create composite control with label and slider in it. 
			 */
			sliderComposite = new Composite(parent, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
	        gridLayout.marginWidth = 0;
	        gridLayout.marginHeight = 0;
	        gridLayout.horizontalSpacing = HORIZONTAL_GAP;
			sliderComposite.setLayout(gridLayout);
			GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
			sliderComposite.setLayoutData(gd);
			
			/*
			 * Create slider label
			 */
			sliderLabel = new Label(sliderComposite,SWT.NONE);
			gd = new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1);
			sliderLabel.setLayoutData(gd);
			/*
			 * Compute label size
			 */
			sliderLabel.setText(SLIDER_LABEL_MAX_SIZE_TEXT);
			Point size = sliderLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			sliderLabel.setSize(size);
			sliderLabel.setText(SLIDER_LABEL_DEFAULT_TEXT);

			/*
			 * Create slider
			 */
			slider = new Slider(sliderComposite, SWT.HORIZONTAL);
			gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
			slider.setLayoutData(gd);
			slider.setMaximum(MAX_SLIDER_VALUE + slider.getThumb());
			slider.setMinimum(MIN_SLIDER_VALUE);
			slider.setIncrement(INCREMENT_SLIDER_VALUE);
			slider.setSelection(DEFAULT_SLIDER_VALUE);
			
			slider.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateSlider();
				}
			});
		}
		
		return sliderComposite;
	}
	
	private void updateSlider() {
		int oldValue = value;
		value = slider.getSelection();
		if (oldValue != value) {
			setPresentsDefaultValue(false);
			String weightsString = "" + (value /10) + "%";  //$NON-NLS-1$ //$NON-NLS-2$
			slider.setToolTipText(weightsString);
			sliderLabel.setText(weightsString);
			fireValueChanged(VALUE, oldValue, value);
		}
	}
	
	private void updateSliderForValue(int value) {
		if (slider != null) {
			String weightsString = "" + (value /10) + "%";  //$NON-NLS-1$ //$NON-NLS-2$
			this.value = value;
			slider.setSelection(value);
			slider.setToolTipText(weightsString);
			sliderLabel.setText(weightsString);
		}
	}

}

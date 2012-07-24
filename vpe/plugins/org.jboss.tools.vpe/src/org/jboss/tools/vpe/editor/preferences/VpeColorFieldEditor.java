/*******************************************************************************
 * Copyright (c) 2012 Exadel, Inc. and Red Hat, Inc.
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
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A field editor for a color type preference.
 */
public class VpeColorFieldEditor extends FieldEditor {

	/**
	 * The color selector, or <code>null</code> if none.
	 */
	private VpeColorSelector colorSelector;

	/**
	 * Creates a new color field editor
	 */
	protected VpeColorFieldEditor() {
		//No default behavior
	}

	/**
	 * Creates a color field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public VpeColorFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void adjustForNumColumns(int numColumns) {
		((GridData) colorSelector.getButton().getLayoutData()).horizontalSpan = numColumns - 1;
	}

	/**
	 * Computes the size of the color image displayed on the button.
	 * <p>
	 * This is an internal method and should not be called by clients.
	 * </p>
	 * 
	 * @param window
	 *            the window to create a GC on for calculation.
	 * @return Point The image size
	 *  
	 */
	protected Point computeImageSize(Control window) {
		// Make the image height as high as a corresponding character. This
		// makes sure that the button has the same size as a "normal" text
		// button.
		GC gc = new GC(window);
		Font f = JFaceResources.getFontRegistry().get(
				JFaceResources.DEFAULT_FONT);
		gc.setFont(f);
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();
		Point p = new Point(height * 3 - 6, height);
		return p;
	}

	@Override
	protected void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = HORIZONTAL_GAP;
        /*
         * Create only 1 top control
         */
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        doFillIntoGrid(container, layout.numColumns);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Control control = getLabelControl(parent);
		control.setLayoutData(new GridData());

		Button colorButton = getChangeControl(parent);
		colorButton.setLayoutData(new GridData());
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		if (colorSelector == null) {
			return;
		}
		colorSelector.setColorValue(PreferenceConverter.getColor(
				getPreferenceStore(), getPreferenceName()));
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoadDefault() {
		if (colorSelector == null) {
			return;
		}
		colorSelector.setColorValue(PreferenceConverter.getDefaultColor(
				getPreferenceStore(), getPreferenceName()));
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doStore() {
		PreferenceConverter.setValue(getPreferenceStore(), getPreferenceName(),
				colorSelector.getColorValue());
	}

	/**
	 * Get the color selector used by the receiver.
	 * 
	 * @return ColorSelector/
	 */
	public VpeColorSelector getColorSelector() {
		return colorSelector;
	}

	/**
	 * Returns the change button for this field editor.
	 * 
	 * @param parent
	 *            The control to create the button in if required.
	 * @return the change button
	 */
	protected Button getChangeControl(Composite parent) {
		if (colorSelector == null) {
			colorSelector = new VpeColorSelector(parent);
			colorSelector.addListener(new IPropertyChangeListener() {
				// forward the property change of the color selector
				public void propertyChange(PropertyChangeEvent event) {
					VpeColorFieldEditor.this.fireValueChanged(event.getProperty(),
							event.getOldValue(), event.getNewValue());
					setPresentsDefaultValue(false);
				}
			});

		} else {
			checkParent(colorSelector.getButton(), parent);
		}
		return colorSelector.getButton();
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	public int getNumberOfControls() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#setEnabled(boolean,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		getChangeControl(parent).setEnabled(enabled);
	}

}

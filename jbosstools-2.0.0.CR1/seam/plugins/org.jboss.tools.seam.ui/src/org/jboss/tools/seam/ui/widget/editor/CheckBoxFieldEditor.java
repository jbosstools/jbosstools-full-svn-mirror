/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 

package org.jboss.tools.seam.ui.widget.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.seam.ui.widget.field.CheckBoxField;

/**
 * @author eskimo
 *
 */
public class CheckBoxFieldEditor extends BaseFieldEditor implements PropertyChangeListener {

	private Control checkBoxControl;

	/**
	 * @param name
	 * @param label
	 * @param defaultValue
	 */
	public CheckBoxFieldEditor(String name, String label, Object defaultValue) {
		super(name, label, defaultValue);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.internal.project.facet.BaseFieldEditor#createEditorControls(java.lang.Object)
	 */
	@Override
	public Object[] getEditorControls(Object composite) {
		// TODO Auto-generated method stub
		return new Control[] {createCheckBoxControl((Composite)composite)};
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.internal.project.facet.BaseFieldEditor#getEditorControls()
	 */
	@Override
	public Object[] getEditorControls() {
		return new Control[] {getCheckBoxControl()};
	}

	public Control getCheckBoxControl() {
		return createCheckBoxControl(null);
	}

	private Control createCheckBoxControl(Composite parent) {
		if(checkBoxControl==null) {
			CheckBoxField checkBoxFild= new CheckBoxField(parent);
			checkBoxFild.addPropertyChangeListener(this);
			checkBoxControl = checkBoxFild.getCheckBox();
			if(getValue() instanceof Boolean) {
				checkBoxFild.getCheckBox().setSelection(((Boolean)getValue()).booleanValue());
			}
		} else if(parent!=null) {
			Assert.isTrue(checkBoxControl.getParent()==parent);
		}
		return checkBoxControl;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.internal.project.facet.IFieldEditor#isEditable()
	 */
	@Override
	public boolean isEditable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.internal.project.facet.IFieldEditor#save(java.lang.Object)
	 */
	public void save(Object object) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.internal.project.facet.IFieldEditor#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean ediatble) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFillIntoGrid(Object parent) {

	}

	public void propertyChange(PropertyChangeEvent evt) {
		setValue(evt.getNewValue());
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.widget.editor.BaseFieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		// TODO Auto-generated method stub
		return 1;
	}

}

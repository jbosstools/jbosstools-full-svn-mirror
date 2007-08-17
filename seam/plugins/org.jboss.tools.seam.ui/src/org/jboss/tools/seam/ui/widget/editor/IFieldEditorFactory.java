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

import java.util.List;

import org.jboss.tools.seam.ui.internal.project.facet.IValidator;

public interface IFieldEditorFactory {

	public static final IFieldEditorFactory INSTANCE = new SwtFieldEditorFactory();

	/**
	 * 
	 * @param name
	 *            TODO
	 * @param label
	 * @param defaultValue
	 * @return
	 */
	IFieldEditor createTextEditor(String name, String label, String defaultValue);

	/**
	 * 
	 * @param name
	 *            TODO
	 * @param label
	 * @param values
	 * @param defaultValue
	 * @return
	 */
	ITaggedFieldEditor createComboEditor(String name, String label,
			List values, Object defaultValue);

	/**
	 * 
	 * @param name
	 *            TODO
	 * @param label
	 * @param values
	 * @param defaultValue
	 * @return
	 */
	ITaggedFieldEditor createComboEditor(String name, String label,
			List values, Object defaultValue, boolean flat);

	/**
	 * 
	 * @param name
	 *            TODO
	 * @param label
	 * @param defaultValue
	 * @return
	 */
	IFieldEditor createCheckboxEditor(String name, String label,
			boolean defaultValue);

	/**
	 * 
	 * @param name
	 * @param label
	 * @param defaultValue
	 * @return
	 */
	IFieldEditor createBrowseFolderEditor(String name, String label,
			String defaultValue);

	/**
	 * 
	 * @param name
	 * @param label
	 * @param defaultValue
	 * @return
	 */
	IFieldEditor createBrowseFileEditor(String name, String label,
			String defaultValue);

	/**
	 * @param jdbcDriverClassName
	 * @param string
	 * @param string2
	 * @return
	 */
	IFieldEditor createUneditableTextEditor(String jdbcDriverClassName,
			String string, String string2);

	/**
	 * 
	 * @param name
	 * @param label
	 * @param defaultValue
	 * @param action
	 * @param validator
	 * @return
	 */
	public IFieldEditor createButtonFieldEditor(String name, String label,
			String defaultValue, ButtonFieldEditor.ButtonPressedAction action,
			IValidator validator);

	/**
	 * 
	 * @param name
	 * @param label
	 * @param values
	 * @param defaultValue
	 * @param flat
	 * @param action1
	 * @param action2
	 * @param validator
	 * @return
	 */
	public IFieldEditor createComboWithTwoButtons(String name, String label,
			List values, Object defaultValue, boolean flat,
			ButtonFieldEditor.ButtonPressedAction action1,
			ButtonFieldEditor.ButtonPressedAction action2, IValidator validator);
	
	public ButtonFieldEditor.ButtonPressedAction createNotImplementedYetAction(String buttonName);
}

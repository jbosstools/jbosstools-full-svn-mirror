/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.edi;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.IModelProcsser;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.medi.MEdiPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class DelimitersUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart part) {

		if (feature == MEdiPackage.eINSTANCE.getDelimiters_Component()) {
			return SmooksUIUtils.createChoiceFieldEditor(parent, toolkit, propertyDescriptor, model,
					new String[] { ENTER_CHAR_STRING }, new IModelProcsser() {

						public Object unwrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals("\n")) {
									return ENTER_CHAR_STRING;
								}
								return model.toString();
							}
							return null;
						}

						public Object wrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals(ENTER_CHAR_STRING)) {
									return "\n";
								}
								return model.toString();
							}
							return null;
						}

					}, false);
		}
		if (feature == MEdiPackage.eINSTANCE.getDelimiters_Field()) {
			return SmooksUIUtils.createChoiceFieldEditor(parent, toolkit, propertyDescriptor, model,
					new String[] { ENTER_CHAR_STRING }, new IModelProcsser() {

						public Object unwrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals("\n")) {
									return ENTER_CHAR_STRING;
								}
								return model.toString();
							}
							return null;
						}

						public Object wrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals(ENTER_CHAR_STRING)) {
									return "\n";
								}
								return model.toString();
							}
							return null;
						}

					}, false);
		}
		if (feature == MEdiPackage.eINSTANCE.getDelimiters_Segment()) {
			return SmooksUIUtils.createChoiceFieldEditor(parent, toolkit, propertyDescriptor, model,
					new String[] { ENTER_CHAR_STRING }, new IModelProcsser() {

						public Object unwrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals("\n")) {
									return ENTER_CHAR_STRING;
								}
								return model.toString();
							}
							return null;
						}

						public Object wrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals(ENTER_CHAR_STRING)) {
									return "\n";
								}
								return model.toString();
							}
							return null;
						}

					}, false);
		}
		if (feature == MEdiPackage.eINSTANCE.getDelimiters_SubComponent()) {
			return SmooksUIUtils.createChoiceFieldEditor(parent, toolkit, propertyDescriptor, model,
					new String[] { ENTER_CHAR_STRING }, new IModelProcsser() {

						public Object unwrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals("\n")) {
									return ENTER_CHAR_STRING;
								}
								return model.toString();
							}
							return null;
						}

						public Object wrapValue(Object model) {
							if (model == null)
								return null;
							if (model instanceof String) {
								if (((String) model).equals(ENTER_CHAR_STRING)) {
									return "\n";
								}
								return model.toString();
							}
							return null;
						}

					}, false);
		}

		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor, part);
	}
	
	public static final String ENTER_CHAR_STRING = "<Enter>";

}
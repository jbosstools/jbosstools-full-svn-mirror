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
package org.jboss.tools.smooks.configuration.editors.groovy;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.actions.OpenEditorEditInnerContentsAction;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 13, 2009
 */
public class ScriptUICreator extends PropertyUICreator {
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	@Override
	public List<AttributeFieldEditPart> createExtendUI(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			SmooksMultiFormEditor formEditor) {
		OpenEditorEditInnerContentsAction action2 = new OpenEditorEditInnerContentsAction(editingdomain,(AnyType) model, SmooksUIUtils.VALUE_TYPE_COMMENT, "groovy");
		AttributeFieldEditPart editPart = SmooksUIUtils.createCommentFieldEditor("Script Contents", editingdomain, toolkit, parent, model, action2);
		action2.setRelateText((Text)editPart.getContentControl());
		return Collections.emptyList();
	}

	@Override
	public boolean ignoreProperty(EAttribute feature) {
		return super.ignoreProperty(feature);
	}
}

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
package org.jboss.tools.smooks.configuration.editors.xsl;

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
import org.jboss.tools.smooks.model.xsl.XslPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class TemplateUICreator extends PropertyUICreator {

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
		if (feature == XslPackage.eINSTANCE.getTemplate_Value()) {
		}
		if (feature == XslPackage.eINSTANCE.getTemplate_Encoding()) {
		}

		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
	 * (org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == XslPackage.eINSTANCE.getTemplate_Value()) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	@Override
	public List<AttributeFieldEditPart> createExtendUI(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			SmooksMultiFormEditor formEditor) {
		OpenEditorEditInnerContentsAction openCdataEditorAction = new OpenEditorEditInnerContentsAction(editingdomain,(AnyType) model, SmooksUIUtils.VALUE_TYPE_CDATA, "xsl");
		OpenEditorEditInnerContentsAction openCommentEditorAction = new OpenEditorEditInnerContentsAction(editingdomain,(AnyType) model, SmooksUIUtils.VALUE_TYPE_COMMENT, "xsl");
		SmooksUIUtils.createFileSelectionTextFieldEditor("Text Value", parent, editingdomain, toolkit, null, model, SmooksUIUtils.VALUE_TYPE_TEXT,
				null, null);
		AttributeFieldEditPart text1 = SmooksUIUtils.createCDATAFieldEditor("Template Contents (CDATA)", editingdomain, toolkit, parent, model, openCdataEditorAction);
		AttributeFieldEditPart text2 = SmooksUIUtils.createCommentFieldEditor("Template Contents (Comment)", editingdomain, toolkit, parent, model, openCommentEditorAction);
		openCdataEditorAction.setRelateText((Text)text1.getContentControl());
		openCommentEditorAction.setRelateText((Text)text2.getContentControl());
		
		return Collections.emptyList();
	}

}
/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.esbrouter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.IModelProcsser;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class RouteBeanPropertyUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
	 * createExtendUIOnTop
	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
	 * org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	@Override
	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingDomain,
			FormToolkit formToolkit, Composite detailsComposite, Object model, ISmooksModelProvider formEditor,
			IEditorPart part) {
		List<AttributeFieldEditPart> attributeEditPartList = createElementSelectionSection("Route On Element",
				editingDomain, formToolkit, detailsComposite, model, formEditor, part,
				EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT,
				EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS);
		return attributeEditPartList;
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
		if (feature == EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT) {
			return true;
		}
		if (feature == EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS) {
			return true;
		}
		if (feature == EsbroutingPackage.Literals.ROUTE_BEAN__TO_SERVICE_NAME) {
			return true;
		}
		if (feature == EsbroutingPackage.Literals.ROUTE_BEAN__TO_SERVICE_CATEGORY) {
			return true;
		}
		if (feature == EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_BEFORE) {
			return true;
		}
		if (feature == EsbroutingPackage.Literals.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION) {
			return true;
		}
		if (feature == SmooksPackage.Literals.ELEMENT_VISITOR__TARGET_PROFILE) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
	 * isBeanIDRefFieldFeature(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected boolean isBeanIDRefFieldFeature(EAttribute attribute) {
		if (attribute == EsbroutingPackage.Literals.ROUTE_BEAN__BEAN_ID_REF) {
			return true;
		}
		return super.isBeanIDRefFieldFeature(attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
	 * createExtendUIOnBottom
	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
	 * org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	@Override
	public List<AttributeFieldEditPart> createExtendUIOnBottom(AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
		List<AttributeFieldEditPart> list = new ArrayList<AttributeFieldEditPart>();
		Group group = new Group(parent, SWT.NONE);
		// Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		// section.setText("Route To Serivce");
		group.setText("Route To Serivce");
		// section.setLayoutData(gd);
		group.setLayoutData(gd);
		FillLayout fl = new FillLayout();
		fl.marginHeight = 0;
		fl.marginWidth = 0;

		// section.setLayout(fl);
		group.setLayout(fl);

		Composite composite = toolkit.createComposite(group);
		// section.setClient(composite);
		group.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		Font f = composite.getFont();
		if (f != null) {
			FontData fd = f.getFontData()[0];
			if (fd != null)
				group.setFont(new Font(null, new FontData(fd.getName(), fd.getHeight(), SWT.BOLD)));
		}

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;

		composite.setLayout(gl);

		AttributeFieldEditPart serviceCategoryEditPart = SmooksUIUtils.createStringFieldEditor("Category", composite,
				editingdomain, toolkit, getPropertyDescriptor(editingdomain,
						EsbroutingPackage.Literals.ROUTE_BEAN__TO_SERVICE_CATEGORY, model), model, false, false, false,
				0, null, SmooksUIUtils.VALUE_TYPE_VALUE, null);
		list.add(serviceCategoryEditPart);

		AttributeFieldEditPart serviceNameEditPart = SmooksUIUtils.createStringFieldEditor("Name", composite,
				editingdomain, toolkit, getPropertyDescriptor(editingdomain,
						EsbroutingPackage.Literals.ROUTE_BEAN__TO_SERVICE_NAME, model), model, false, false, false, 0,
				null, SmooksUIUtils.VALUE_TYPE_VALUE, null);
		list.add(serviceNameEditPart);

		IModelProcsser processer = new IModelProcsser() {

			public Object unwrapValue(Object model) {
				if (model instanceof Boolean) {
					if ((Boolean) model) {
						return "TRUE";
					} else {
						return "FALSE";
					}
				}
				return "FALSE";
			}

			public Object wrapValue(Object model) {
				try {
					return Boolean.parseBoolean(model.toString());
				} catch (Throwable t) {
				}
				return Boolean.FALSE;
			}

		};
		AttributeFieldEditPart routeBeforeEP = SmooksUIUtils.createChoiceFieldEditor(parent, toolkit,
				getPropertyDescriptor(editingdomain, EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_BEFORE, model),
				model, new String[] { "TRUE", "FALSE" }, processer, true);
		list.add(routeBeforeEP);

		list.add(SmooksUIUtils.createStringFieldEditor(parent, toolkit, getPropertyDescriptor(editingdomain,
				EsbroutingPackage.Literals.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION, model), model, false, false, null));
		list.add(SmooksUIUtils.createStringFieldEditor(parent, toolkit, getPropertyDescriptor(editingdomain,
				SmooksPackage.Literals.ELEMENT_VISITOR__TARGET_PROFILE, model), model, false, false, null));

		return list;
	}

}

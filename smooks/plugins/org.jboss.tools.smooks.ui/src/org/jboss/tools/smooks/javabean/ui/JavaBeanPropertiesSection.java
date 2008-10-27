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
package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * @author Dart Peng
 * @Date : Oct 27, 2008
 */
public class JavaBeanPropertiesSection extends AbstractPropertySection {

	private Text beanClassText;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		Composite main = factory.createComposite(parent);
		FillLayout fill = new FillLayout();
		fill.marginHeight = 8;
		fill.marginWidth = 8;
		main.setLayout(fill);

		Section section = factory.createSection(main, Section.TITLE_BAR);
		section.setText("JavaBean Properties");
		Composite controlComposite = factory.createComposite(section);
		section.setClient(controlComposite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;

		controlComposite.setLayout(gl);

		factory.createLabel(controlComposite, "BeanClass");

		beanClassText = factory.createText(controlComposite, "");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		beanClassText.setLayoutData(gd);
	}

	public void refresh() {
		super.refresh();
		beanClassText.setEnabled(false);
		IStructuredSelection selection = (IStructuredSelection) this
				.getSelection();
		Object obj = selection.getFirstElement();
		if (obj == null)
			return;
		if (obj instanceof EditPart) {
			Object model = ((EditPart) obj).getModel();
			if (model instanceof LineConnectionModel) {
				AbstractStructuredDataModel target = (AbstractStructuredDataModel) ((LineConnectionModel) model)
						.getTarget();
				Object referenceObj = target.getReferenceEntityModel();
				if(referenceObj instanceof JavaBeanModel){
					beanClassText.setEnabled(true);
					String className = ((JavaBeanModel)referenceObj).getBeanClass().getName();
					beanClassText.setText(className);
				}
			}
		}
	}

}

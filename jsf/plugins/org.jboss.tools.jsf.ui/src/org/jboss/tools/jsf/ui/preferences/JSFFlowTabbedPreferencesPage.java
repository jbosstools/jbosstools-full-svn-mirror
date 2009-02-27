/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jsf.ui.preferences;

import org.jboss.tools.common.model.ui.preferences.*;
import org.eclipse.ui.*;
import org.jboss.tools.common.meta.constraint.impl.XAttributeConstraintAList;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jsf.web.JSFTemplate;

public class JSFFlowTabbedPreferencesPage extends TabbedPreferencesPage implements IWorkbenchPreferencePage {

	public static final String ID = "org.jboss.tools.jsf.ui.jsfflowdiagram";
	
	public static String JSF_EDITOR_PATH = "%Options%/Struts Studio/Editors/JSF Flow Diagram";
	public static String JSF_ADD_VIEW_PATH = "%Options%/Struts Studio/Editors/JSF Flow Diagram/Add View";
	
	public JSFFlowTabbedPreferencesPage() {
		XModel model = getPreferenceModel();
		XModelObject editor = model.getByPath(JSF_EDITOR_PATH);
		addPreferencePage(new XMOBasedPreferencesPage(editor));
		XModelObject addView = model.getByPath(JSF_ADD_VIEW_PATH);
		initTemplateList(addView);
		addPreferencePage(new XMOBasedPreferencesPage(addView));
	}

	public void init(IWorkbench workbench)  {}
	
	void initTemplateList(XModelObject addView) {
		if(addView == null) return;
		JSFTemplate templates = new JSFTemplate();
		XAttributeConstraintAList l = (XAttributeConstraintAList)addView.getModelEntity().getAttribute("Page Template").getConstraint();
		l.setValues(templates.getPageTemplateList());
	}

}

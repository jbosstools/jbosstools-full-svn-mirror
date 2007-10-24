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
package org.jboss.tools.jst.web.ui.editors;

import org.jboss.tools.common.editor.TreeFormPage;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.ui.editor.EditorDescriptor;
import org.jboss.tools.common.model.ui.editors.multipage.DefaultMultipageEditor;
import org.jboss.tools.jst.web.tld.model.EditorTreeConstraint;

public class WebCompoundEditor extends DefaultMultipageEditor {
			
	protected void doCreatePages() {
		if(isAppropriateNature()) {
			treeFormPage = createTreeFormPage();
			String title = "Web XML Editor"; 
			if(object != null) {
				String key = object.getModelEntity().getName() + ".editorTitle";
				String s = WizardKeys.getString(key);
				if(s != null) title = s;
			}
			treeFormPage.setTitle(title);
			((TreeFormPage)treeFormPage).addFilter(new EditorTreeConstraint());
			treeFormPage.initialize(object);
			addFormPage(treeFormPage);
		}
		createTextPage();
		initEditors();
		if(treeFormPage != null) selectionProvider.addHost("treeEditor", treeFormPage.getSelectionProvider());
		if(textEditor != null) selectionProvider.addHost("textEditor", getTextSelectionProvider());
	}

	public Object getAdapter(Class adapter) {
		if (adapter == EditorDescriptor.class)
			return new EditorDescriptor("web.xml");

		return super.getAdapter(adapter);
	}

	protected String[] getSupportedNatures() {
		return new String[0];
	}

}
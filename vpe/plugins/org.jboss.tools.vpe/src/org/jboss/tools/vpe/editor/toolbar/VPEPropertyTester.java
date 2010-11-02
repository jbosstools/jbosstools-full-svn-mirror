/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.toolbar;

import org.eclipse.core.expressions.PropertyTester;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;

/**
 * @author mareshkau
 *
 */
public class VPEPropertyTester extends PropertyTester{

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		return !JspEditorPlugin.getDefault().getPreferenceStore()
		.getBoolean(IVpePreferencesPage.SHOW_VISUAL_TOOLBAR);
	}
}

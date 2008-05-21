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
package org.jboss.tools.vpe.editor.util;

import java.util.Properties;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.vpe.VpePlugin;

public class VpeDndUtil {
    public static boolean isDropEnabled(IModelObjectEditorInput input) {
	return DnDUtil.isPasteEnabled(input.getXModelObject());
    }

    public static void drop(IModelObjectEditorInput input,
	    ISourceViewer viewer, ISelectionProvider provider) {
	Properties properties = new Properties();
	properties.setProperty("isDrop", "true");
	properties.setProperty("actionSourceGUIComponentID", "editor");
	properties.setProperty("accepsAsString", "true");
	properties.put("selectionProvider", provider);
	properties.put("viewer", viewer);

	try {
	    DnDUtil.paste(input.getXModelObject(), properties);
	    PaletteInsertHelper.insertIntoEditor(viewer, properties);
	} catch (Exception ex) {
	    VpePlugin.getPluginLog().logError(ex);
	}
    }

}

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
package org.jboss.tools.vpe.ui.palette;

import java.util.*;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.internal.ui.palette.editparts.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Display;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.vpe.ui.palette.model.PaletteItem;

public class PaletteDragSourceListener extends DragSourceAdapter {
	EditPartViewer viewer;
	boolean isDragging = false;

	public PaletteDragSourceListener(EditPartViewer viewer) {
		this.viewer = viewer;
	}

	boolean isDragging() {
		return isDragging;
	}

	public void dragStart(DragSourceEvent event) {
		try {
			List list = ((PaletteViewer)viewer).getSelectedEditParts();
			XModelObject object = (list.size() == 0) ? null : getObject(list.get(0));
			if(object == null) {
				event.doit = false;
				return;
			}
			Properties p = new Properties();
			p.setProperty("isDrag", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			XActionInvoker.invoke("CopyActions.Copy", object, p); //$NON-NLS-1$
		} catch (Exception e) {
			PalettePlugin.getPluginLog().logError(e);
		}
		isDragging = true;
	}
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = "data"; //$NON-NLS-1$
		} else {
///			event.data = new String[] {"model object"};
			event.data = "model object"; //$NON-NLS-1$
		}
	}

	public void dragFinished(DragSourceEvent event) { 
		event.data = null;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				isDragging = false;
			}
		});
	}
	
	XModelObject getObject(Object part) {
		if(part instanceof ToolEntryEditPart) {
			PaletteEditPart entry = (PaletteEditPart)part;
			PaletteItem item = (PaletteItem)entry.getModel();
			return item.getXModelObject();
		} else {
			if(ModelPlugin.isDebugEnabled()){
				// TODO Should be replaced with trace in future
				PalettePlugin.getPluginLog().logInfo(part.getClass().getName());
			}
		}
		return null;
	}
}

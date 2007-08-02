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
package org.jboss.tools.vpe.editor.toolbar.format;

import org.eclipse.swt.widgets.Event;

import org.jboss.tools.vpe.editor.template.textformating.FormatData;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler;

/**
 * @author Igels
 */
abstract public class FormatController implements IFormatController {

	protected FormatControllerManager manager;

	public FormatController(FormatControllerManager manager) {
		this.manager = manager;
		manager.addFormatController(this);
	}

	public FormatControllerManager getManager() {
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		manager.setControllerNotifedSelectionChange(true);
		run();
//		manager.getVpeController().sourceSelectionChanged(true);
		manager.getVpeController().sourceSelectionToVisualSelection(true);
		manager.setControllerNotifedSelectionChange(false);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#run()
	 */
	public void run() {
		TextFormatingData data = manager.getFormatTemplateForSelectedNode();
		if(data==null) {
			return;
		}
		FormatData[] formats = data.getFormatDatas(getType());
		for(int i=0; i<formats.length; i++) {
			IFormatHandler handler = manager.getHandlerFactory().createHandler(formats[i]);
			if(handler!=null && handler.formatIsAllowable()) {
				handler.run(formats[i]);
				return;
			} else if(formatIsAllowable(formats[i])) {
				run(formats[i]);
				return;
			}
		}
	}

	abstract protected void run(FormatData templateData);
	abstract protected boolean formatIsAllowable(FormatData templateData);
}
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
package org.jboss.tools.shale.model.dialog;

import org.jboss.tools.jsf.web.ConfigFilesData;

public interface ShaleDialogConstants {
	public String DOC_PUBLICID = "-//Apache Software Foundation//DTD Shale Dialog Configuration 1.0//EN";
	public String DOC_EXTDTD = "http://struts.apache.org/dtds/shale-dialog-config_1_0.dtd";
	
	public String ENT_SHALE_DIALOG = "FileShaleDialog";
	public String DIALOG_ENTITY = "ShaleDialog";
	public String TRANSITION_ENTITY = "ShaleTransition";
	public String ACTION_ENTITY = "ShaleAction";
	public String END_ENTITY = "ShaleEnd";
	public String SUBDIALOG_ENTITY = "ShaleSubdialog";
	public String VIEW_ENTITY = "ShaleView";
	
	public ConfigFilesData REGISTRATION_DATA = new ConfigFilesData(
			"org.apache.shale.dialog.CONFIGURATION",
			new String[]{"/WEB-INF/dialog-config.xml"}
	);

}

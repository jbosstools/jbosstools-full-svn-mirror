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
package org.jboss.tools.shale.model.dialog.handlers;

import org.jboss.tools.jsf.web.ConfigFilesData;
import org.jboss.tools.shale.model.dialog.ShaleDialogConstants;
import org.jboss.tools.shale.model.handlers.AbstractDeleteConfigFileHandler;

public class DeleteDialogConfigFileHandler extends AbstractDeleteConfigFileHandler {

	protected ConfigFilesData getConfigFilesData() {
		return ShaleDialogConstants.REGISTRATION_DATA;
	}

}

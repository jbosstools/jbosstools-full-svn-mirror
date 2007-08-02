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
package org.jboss.tools.shale.model.clay.handlers;

import org.jboss.tools.jsf.web.ConfigFilesData;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.shale.model.handlers.AbstractCreateConfigFileSupport;

public class CreateClayConfigFileSupport extends AbstractCreateConfigFileSupport {

	protected ConfigFilesData getConfigFilesData() {
		return ShaleClayConstants.REGISTRATION_DATA;
	}

	protected String getDefaultNameBase() {
		return "clay-config";
	}

}

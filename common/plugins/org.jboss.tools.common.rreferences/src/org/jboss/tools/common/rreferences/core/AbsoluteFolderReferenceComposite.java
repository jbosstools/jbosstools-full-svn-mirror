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
package org.jboss.tools.common.rreferences.core;

import org.jboss.tools.common.rreferences.messages.Messages;

public class AbsoluteFolderReferenceComposite extends FolderReferenceComposite {

	protected ResourceReferenceList getReferenceList() {
		return AbsoluteFolderReferenceList.getInstance();
	}

	protected String getTitle() {
		return Messages.ACTUAL_RUN_TIME_ABSOLUTE_FOLDER;
	}

}

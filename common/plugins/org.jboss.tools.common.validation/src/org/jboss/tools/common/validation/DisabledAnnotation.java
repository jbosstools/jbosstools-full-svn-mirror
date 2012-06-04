/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.common.validation;

import org.eclipse.ui.internal.WorkbenchImages;

/**
 * @author Alexey Kazakov
 */
public class DisabledAnnotation extends AbstractTemporaryAnnotation {

	private static final String warningPath = WorkbenchImages.ICONS_PATH + "dlcl16/showwarn_tsk.gif";
	private static final String errorPath = WorkbenchImages.ICONS_PATH + "dlcl16/showerr_tsk.gif";

	public DisabledAnnotation(String type, boolean isPersistent, String text,	boolean warning) {
		super(type, isPersistent, text, warning);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.validation.AbstractTemporaryAnnotation#getWarningIconPath()
	 */
	@Override
	protected String getWarningIconPath() {
		return warningPath;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.validation.AbstractTemporaryAnnotation#getErrorIconPath()
	 */
	@Override
	protected String getErrorIconPath() {
		return errorPath;
	}
}
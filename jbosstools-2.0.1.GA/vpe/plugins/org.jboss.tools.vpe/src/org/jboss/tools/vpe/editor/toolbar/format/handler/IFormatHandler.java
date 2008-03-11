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
package org.jboss.tools.vpe.editor.toolbar.format.handler;

import org.jboss.tools.vpe.editor.template.textformating.FormatData;
import org.jboss.tools.vpe.editor.toolbar.format.IFormatController;

/**
 * @author Igels
 */
public interface IFormatHandler {

	/**
	 * @return true if this format action is allowable for selected node.
	 */
	public boolean formatIsAllowable();

	/**
	 * Execute format.
	 * @param formatData
	 */
	public void run(FormatData formatData);

	public void setFormatController(IFormatController controller);
}
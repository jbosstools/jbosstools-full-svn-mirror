/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.modelparser;

import java.io.OutputStream;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Dart Peng
 * @Date Aug 18, 2008
 */
public interface ISmooksConfigurationFileGenerateFactory {
	/**
	 * Generate the file
	 * @param monior
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public OutputStream generateFile(IProgressMonitor monior,
			SmooksConfigurationFileGenerateContext context) throws Exception;
}

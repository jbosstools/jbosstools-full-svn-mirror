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
public class DefaultSmooksConfigFileGenerateFactory implements
		ISmooksConfigurationFileGenerateFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.ui.modelparser.ISmooksConfigurationFileGenerateFactory#generateFile(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public OutputStream generateFile(IProgressMonitor monior,
			SmooksConfigurationFileGenerateContext context) throws Exception {
		// TODO Auto-generated method stub
//		context.getDataMappingRootModel();
		return null;
	}

}

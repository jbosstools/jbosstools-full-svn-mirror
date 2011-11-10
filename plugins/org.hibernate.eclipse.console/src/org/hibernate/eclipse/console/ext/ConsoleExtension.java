/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.eclipse.console.ext;

import org.hibernate.console.ext.HibernateExtension;

/**
 * @author Dmitry Geraskov
 *
 */
public interface ConsoleExtension {
	
	public CompletionProposalsResult hqlCodeComplete(String query, int position);
	
	public void setHibernateException(HibernateExtension hibernateExtension);

	/*public void launchExporters(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException;*/
	
}

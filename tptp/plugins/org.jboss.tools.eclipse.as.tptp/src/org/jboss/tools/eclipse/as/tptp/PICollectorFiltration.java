/**********************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 			Qingwu Lin (linqingw@cn.ibm.com)
 *  
 * IBM - Initial API and implementation
 **********************************************************************/
package org.jboss.tools.eclipse.as.tptp;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.tptp.trace.ui.internal.launcher.deleg.application.PIDelegateHelper.JVMVersionDetector;
import org.eclipse.tptp.trace.ui.provisional.launcher.ICollectorFiltration;

/**
 * This filtration class is used to filter the JVMPI data collector if the
 * targeted host uses JRE 1.6 or greater.
 *  
 */
public class PICollectorFiltration implements ICollectorFiltration
{

	public boolean include(String id, ILaunchConfiguration configuration, Object context)
	{
		boolean isInclude;
		JVMVersionDetector jvmVersionDetector = new JVMVersionDetector(configuration);
		String version = jvmVersionDetector.retrieveVersionOutput();		
		isInclude = version.indexOf("1.3") >0 || version.indexOf("1.4") >0 || version.indexOf("1.5") >0; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return isInclude;
	}
}


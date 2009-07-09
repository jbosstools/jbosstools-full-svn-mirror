/******************************************************************************* 
* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.VpeDebug;

/**
 * @author Max Areshkau (mareshkau@exadel.com)
 *
 *Class created to print debug info
 */
public class VpeDebugUtil {
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat();
	static {
		formatter.applyPattern("hh:mm:ss.SSS"); //$NON-NLS-1$
	}
	/**
	 * Prints debug info on console
	 * @param msg
	 */
	public static void debugInfo(String msg) {
		
		if(Platform.inDebugMode()) {

			System.out.print(formatter.format(new Date())+":"+ msg); //$NON-NLS-1$
		}
	}
	/**
	 * 
	 */
	public static void debugVPEDnDEvents(String msg) {
		if(VpeDebug.PRINT_VISUAL_DRAGDROP_EVENT) {
			
			System.out.println(formatter.format(new Date())+":"+ msg); //$NON-NLS-1$
		}
	}
}

 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;

/**
 * Base class for SWTBot Tests using SWTBotExt
 * @author jpeterka
 *
 */
public class SWTTestExt extends SWTBotTestCase{

	public static final Logger log = Logger.getLogger(SWTTestExt.class);
	public static final SWTBotExt bot = new SWTBotExt();
	public static final SWTEclipseExt eclipse = new SWTEclipseExt(bot);
	public static final SWTUtilExt util = new SWTUtilExt(bot);
	public static final SWTOpenExt open = new SWTOpenExt(bot);
	public static final SWTJBTExt jbt = new SWTJBTExt(bot);
	
	// Views
	public static final PackageExplorer packageExplorer = new PackageExplorer();
	public static final ProjectExplorer projectExplorer = new ProjectExplorer();
	public static final ServersView servers = new ServersView();
	
	public static Properties properties;
	
	/**
	 * Get properties for hibernate tests
	 * @param key
	 */
	public static String getProperty(String key) {
		return util.getValue(properties,key);
	}	
	
	// Wait Constants
	public static int TIME_500MS = 500;
	public static int TIME_1S = 1000;
	public static int TIME_5S = 5000;
	public static int TIME_10S = 10000;
	public static int TIME_20S = 20000;
}

/*************************************************************************************
 * Copyright (c) 2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.central.configurators;

import org.jboss.tools.central.discovery.JBossCentralDiscoveryActivator;

/**
 * 
 * @author snjeza, nboldt
 *
 */
public class DefaultJBossCentralDiscoveryConfigurator implements
		IJBossCentralDiscoveryConfigurator {
	
	private static final String JBOSS_SOA_TOOLING_URL = "http://download.jboss.org/jbosstools/updates/development/indigo/soa-tooling/"; // published URL

	@Override
	public String getJBossSOAToolingUpdateURL() {
		String directory = System.getProperty(JBossCentralDiscoveryActivator.JBOSS_SOA_TOOLING, null);
		System.out.println("directory = " + directory);
		if (directory == null) {
			return JBOSS_SOA_TOOLING_URL;
		}
		return directory;
	}
}

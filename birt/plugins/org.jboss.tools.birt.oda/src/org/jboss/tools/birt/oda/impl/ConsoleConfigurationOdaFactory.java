/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.birt.oda.impl;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.osgi.util.NLS;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.jboss.tools.birt.oda.IOdaFactory;
import org.jboss.tools.birt.oda.Messages;

/**
 * 
 * @author snjeza
 * 
 */
public class ConsoleConfigurationOdaFactory extends AbstractOdaFactory {

	ConsoleConfiguration consoleConfiguration;
	
	public ConsoleConfigurationOdaFactory(Properties properties) throws OdaException {
		getSessionFactory(properties);
		String maxRowString = properties.getProperty(IOdaFactory.MAX_ROWS);
        try {
			setMaxRows(new Integer(maxRowString).intValue());
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	public SessionFactory getSessionFactory(Properties properties) throws OdaException {
		String configurationName = properties.getProperty(CONFIGURATION);
        ConsoleConfiguration[] configurations = KnownConfigurations.getInstance().getConfigurations();
        for (int i = 0; i < configurations.length; i++) {
			if (configurations[i].getName().equals(configurationName)) {
				consoleConfiguration=configurations[i];
				break;
			}
		}
        if (!isOpen()) {
        	try {
				sessionFactory = consoleConfiguration.getSessionFactory();
				if (sessionFactory == null) {
					consoleConfiguration.build();
					consoleConfiguration.buildSessionFactory();
					sessionFactory = consoleConfiguration.getSessionFactory();
				}
			} catch (HibernateException e) {
				throw new OdaException(e.getLocalizedMessage());
			}
        } else {
        	throw new OdaException(NLS.bind(Messages.ConsoleConfigurationOdaFactory_Invalid_configuration, configurationName));
        }
		return sessionFactory;
	}

}

package org.jboss.tools.birt.oda.impl;

import java.util.Properties;

import javax.naming.InitialContext;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.hibernate.SessionFactory;
import org.jboss.tools.birt.oda.IOdaFactory;

public class ServerOdaFactory extends AbstractOdaFactory {

	public ServerOdaFactory(Properties properties) throws OdaException {
		getSessionFactory(properties);
		String maxRowString = properties
				.getProperty(IOdaFactory.MAX_ROWS);
		try {
			setMaxRows(new Integer(maxRowString).intValue());
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	public Object getSessionFactory(Properties properties) throws OdaException {
		String configurationName = properties.getProperty(CONFIGURATION);
		if (sessionFactory == null) {
			InitialContext ctx = null;
			try {
				ctx = new InitialContext();
				sessionFactory = (SessionFactory) ctx.lookup("java:/"
						+ configurationName);

			} catch (Exception e) {
				e.printStackTrace();
				throw new OdaException(
						"Cannot create Hibernate session factory");
			}

		}
		return sessionFactory;
	}

	public boolean isOpen() {
		return sessionFactory != null && !sessionFactory.isClosed();
	}

}

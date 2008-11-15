package org.jboss.tools.birt.oda.impl;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.EntityManagerImpl;
import org.jboss.tools.birt.oda.IOdaFactory;
import org.jboss.tools.birt.oda.Messages;

public class ServerOdaFactory extends AbstractOdaFactory {

	private EntityManager manager;

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
		String jndiName = properties.getProperty(JNDI_NAME);
		String configurationName = properties.getProperty(CONFIGURATION);
		if (configurationName != null) {
			int index = configurationName.indexOf("-ejb"); //$NON-NLS-1$
			if (index > 0) {
				configurationName = configurationName.substring(0, index);
			}
		}
		if (jndiName == null || jndiName.length() <= 0) {
			jndiName = "java:/" + configurationName; //$NON-NLS-1$
		}
		String entityFactoryName = "java:/" + configurationName + "EntityManagerFactory"; //$NON-NLS-1$ //$NON-NLS-2$
		
		if (sessionFactory == null) {
			InitialContext ctx = null;
			try {
				ctx = new InitialContext();
				//sessionFactory = (SessionFactory) ctx.lookup("java:/"
				//		+ configurationName);
				try {
					Object object = ctx.lookup(jndiName);
					if (object instanceof SessionFactory) {
						sessionFactory = (SessionFactory) object;
						return sessionFactory;
					}
					if (object instanceof EntityManagerFactory) {
						EntityManagerFactory entityManagerFactory = (EntityManagerFactory) object;
						manager = entityManagerFactory.createEntityManager();
						if (manager instanceof EntityManagerImpl) {
							EntityManagerImpl hibernateManager = (EntityManagerImpl) manager;
							sessionFactory = hibernateManager.getSession().getSessionFactory();
							manager.close();
							return sessionFactory;
						}
					}
				} catch (Exception e1) {
					Object object = ctx.lookup(entityFactoryName);
					if (object instanceof SessionFactory) {
						sessionFactory = (SessionFactory) object;
						return sessionFactory;
					}
					if (object instanceof EntityManagerFactory) {
						EntityManagerFactory entityManagerFactory = (EntityManagerFactory) object;
						manager = entityManagerFactory.createEntityManager();
						if (manager instanceof EntityManagerImpl) {
							EntityManagerImpl hibernateManager = (EntityManagerImpl) manager;
							sessionFactory = hibernateManager.getSession().getSessionFactory();
							manager.close();
							return sessionFactory;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new OdaException(
						Messages.ServerOdaFactory_Cannot_create_Hibernate_session_factory);
			}
		}
		return sessionFactory;
	}

	public boolean isOpen() {
		return sessionFactory != null && !sessionFactory.isClosed();
	}

}

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
package org.jboss.tools.hibernate4_0;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Settings;
import org.hibernate.console.ConsoleConfigClassLoader;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.QueryInputModel;
import org.hibernate.console.QueryPage;
import org.hibernate.console.execution.DefaultExecutionContext;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.ext.HibernateException;
import org.hibernate.console.ext.HibernateExtension;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences;
import org.hibernate.console.preferences.PreferencesClassPathUtils;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.internal.StandardServiceRegistryImpl;

/**
 * 
 * @author Dmitry Geraskov
 *
 */
public class HibernateExtension4_0 implements HibernateExtension {
	
	private ConsoleConfigClassLoader classLoader = null;

	private ExecutionContext executionContext;
	
	private ConsoleConfigurationPreferences prefs;
	
	private Configuration configuration;
	
	private SessionFactory sessionFactory;
	
	private ServiceRegistry serviceRegistry;
	
	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	public HibernateExtension4_0() {
	}

	@Override
	public String getHibernateVersion() {
		return "4.0";
	}
	
	@Override
	public QueryPage executeHQLQuery(final String hql,
			final QueryInputModel queryParameters) {
		return (QueryPage)execute(new Command() {
			public Object execute() {
				Session session = sessionFactory.openSession();
				QueryPage qp = new HQLQueryPage(HibernateExtension4_0.this, hql,queryParameters);
				qp.setSession(session);
				return qp;
			}
		});
	}

	@Override
	public QueryPage executeCriteriaQuery(final String criteriaCode,
			final QueryInputModel model) {
		return (QueryPage)execute(new Command() {
			public Object execute() {
				Session session = sessionFactory.openSession();
				QueryPage qp = new JavaPage(HibernateExtension4_0.this,criteriaCode,model);
				qp.setSession(session);
				return qp;
			}
		});
	}

	/**
	 * @param ConsoleConfigurationPreferences the prefs to set
	 */
	public void setConsoleConfigurationPreferences(ConsoleConfigurationPreferences prefs) {
		this.prefs = prefs;
	}
	
	public void build() {
		configuration = buildWith(null, true);
	}

	@Override
	public void buildSessionFactory() {
		execute(new Command() {
			public Object execute() {
				if (sessionFactory != null) {
					throw new HibernateException("Factory was not closed before attempt to build a new Factory");
				}
				serviceRegistry =  new ServiceRegistryBuilder()
					.applySettings( configuration.getProperties())
					.buildServiceRegistry();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				return null;
			}
		});
	}

	@Override
	public boolean closeSessionFactory() {
		boolean res = false;
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
			( (StandardServiceRegistryImpl) serviceRegistry ).destroy();
			serviceRegistry = null;
			res = true;
		}
		return res;
	}

	public Configuration buildWith(final Configuration cfg, final boolean includeMappings) {
		reinitClassLoader();
		//TODO handle user libraries here
		executionContext = new DefaultExecutionContext(prefs.getName(), classLoader);
		Configuration result = (Configuration)execute(new Command() {
			public Object execute() {
				ConfigurationFactory cf = new ConfigurationFactory(prefs, fakeDrivers);
				return cf.createConfiguration(cfg, includeMappings);
			}
		});
		return result;
	}
	
	/**
	 * Create class loader - so it uses the original urls list from preferences. 
	 */
	protected void reinitClassLoader() {
		//the class loader caches user's compiled classes
		//need to rebuild it on every console configuration rebuild to pick up latest versions.
		final URL[] customClassPathURLs = PreferencesClassPathUtils.getCustomClassPathURLs(prefs);
		cleanUpClassLoader();
		classLoader = createClassLoader(customClassPathURLs);
	}
	
	protected ConsoleConfigClassLoader createClassLoader(final URL[] customClassPathURLs) {
		ConsoleConfigClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ConsoleConfigClassLoader>() {
			public ConsoleConfigClassLoader run() {
				return new ConsoleConfigClassLoader(customClassPathURLs, Thread.currentThread().getContextClassLoader()) {
					protected Class<?> findClass(String name) throws ClassNotFoundException {
						try {
							return super.findClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						} catch (IllegalStateException e){
							e.printStackTrace();
							throw e;
						}
					}
		
					protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
						try {
							return super.loadClass(name, resolve);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
		
					public Class<?> loadClass(String name) throws ClassNotFoundException {
						try {
							return super.loadClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
					
					public URL getResource(String name) {
					      return super.getResource(name);
					}
				};
			}
		});
		return classLoader;
	}

	public String getName() {
		return prefs.getName();
	}
	
	public ExecutionContext getExecutionContext() {
		return executionContext;
	}
	
	public Object execute(Command c) {
		if (executionContext != null) {
			return executionContext.execute(c);
		}
		final String msg = NLS.bind(ConsoleMessages.ConsoleConfiguration_null_execution_context, getName());
		throw new HibernateException(msg);
	}

	@Override
	public boolean reset() {
		boolean res = false;
		// reseting state
		if (configuration != null) {
			configuration = null;
			res = true;
		}
		
		boolean tmp = closeSessionFactory();
		res = res || tmp;
		if (executionContext != null) {
			executionContext.execute(new Command() {
				public Object execute() {
					Iterator<FakeDelegatingDriver> it = fakeDrivers.values().iterator();
					while (it.hasNext()) {
						try {
							DriverManager.deregisterDriver(it.next());
						} catch (SQLException e) {
							// ignore
						}
					}
					return null;
				}
			});
		}
		if (fakeDrivers.size() > 0) {
			fakeDrivers.clear();
			res = true;
		}
		tmp = cleanUpClassLoader();
		res = res || tmp;
		executionContext = null;
		return res;
	}
	
	protected boolean cleanUpClassLoader() {
		boolean res = false;
		ClassLoader classLoaderTmp = classLoader;
		while (classLoaderTmp != null) {
			if (classLoaderTmp instanceof ConsoleConfigClassLoader) {
				((ConsoleConfigClassLoader)classLoaderTmp).close();
				res = true;
			}
			classLoaderTmp = classLoaderTmp.getParent();
		}
		if (classLoader != null) {
			classLoader = null;
			res = true;
		}
		return res;
	}

	@Override
	public boolean hasConfiguration() {
		return configuration != null;
	}

	/**
	 * @return
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public Settings getSettings(final Configuration cfg, final ServiceRegistry serviceRegisrty) {
		return (Settings) execute(new Command() {
			public Object execute() {
				return cfg.buildSettings(serviceRegisrty);
			}
		});
	}

	@Override
	public boolean isSessionFactoryCreated() {
		return sessionFactory != null;
	}
	
	public String generateSQL(final String query) {
		return QueryHelper.generateSQL(executionContext, sessionFactory, query);
	}

	@Override
	public void buildMappings() {
		execute(new Command() {
			public Object execute() {
				getConfiguration().buildMappings();
				return null;
			}
		});
	}
	
	@Override
	public boolean hasExecutionContext() {
		return executionContext != null;
	}
	
	@Override
	public String getConsoleConfigurationName() {
		return prefs.getName();
	}
}
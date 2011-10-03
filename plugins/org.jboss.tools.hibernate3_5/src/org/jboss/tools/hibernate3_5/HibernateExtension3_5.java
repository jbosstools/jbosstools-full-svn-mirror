package org.jboss.tools.hibernate3_5;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.console.ConfigurationFactory;
import org.hibernate.console.ConsoleConfigClassLoader;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.ConsoleQueryParameter;
import org.hibernate.console.FakeDelegatingDriver;
import org.hibernate.console.QueryInputModel;
import org.hibernate.console.execution.DefaultExecutionContext;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.ext.HibernateException;
import org.hibernate.console.ext.HibernateExtension;
import org.hibernate.console.ext.QueryResult;
import org.hibernate.console.ext.QueryResultImpl;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences;
import org.hibernate.console.preferences.PreferencesClassPathUtils;
import org.hibernate.type.Type;

public class HibernateExtension3_5 implements HibernateExtension {
	
	private ConsoleConfigClassLoader classLoader = null;
	
	private ExecutionContext executionContext;
	
	private ConsoleConfigurationPreferences prefs;
	
	private Configuration configuration;
	
	private SessionFactory sessionFactory;

	public HibernateExtension3_5() {
	}

	@Override
	public String getHibernateVersion() {
		return "3.5";
	}

	@Override
	public QueryResult executeHQLQuery(final String hql,
			final QueryInputModel queryParameters) {
		System.out.println("Execute HQLQuery in " + getClass().getName());
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		List<Object> list = Collections.emptyList();
		long queryTime = 0;
		try {
			list = new ArrayList<Object>();
			setupParameters(query, queryParameters);
			long startTime = System.currentTimeMillis();
			Iterator<?> iter = query.list().iterator(); // need to be user-controllable to toggle between iterate, scroll etc.
			queryTime = System.currentTimeMillis() - startTime;
			while (iter.hasNext() ) {
				Object element = iter.next();
				list.add(element);
			}
		} 
		catch (HibernateException e) {
			e.printStackTrace();
		}
		return new QueryResultImpl(list,
				getPathNames(query), queryTime);
	}
	
	public List<String> getPathNames(Query query) {
    	List<String> l = Collections.emptyList();
    
    	try {
    		if(query==null) return l;
    		String[] returnAliases = null;
    		try {
    			returnAliases = query.getReturnAliases();
    		} catch(NullPointerException e) {
    			// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
    		}
			if(returnAliases==null) {
    		Type[] t;
    		try {
			t = query.getReturnTypes();
    		} catch(NullPointerException npe) {
    			t = new Type[] { null };
    			// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
    		}
    		l = new ArrayList<String>(t.length);
    
    		for (int i = 0; i < t.length; i++) {
    			Type type = t[i];
    			if(type==null) {
    			    l.add("<multiple types>");	 //$NON-NLS-1$
    			} else {
    				l.add(type.getName() );
    			}
    		}
    		} else {
    			String[] t = returnAliases;
        		l = new ArrayList<String>(t.length);
        
        		for (int i = 0; i < t.length; i++) {
        			l.add(t[i]);
        		}			
    		}
    	} catch (HibernateException he) {
    		he.printStackTrace();           
    	}
    
    	return l;
    }
	
	private void setupParameters(Query query2, QueryInputModel model) {
		
		if(model.getMaxResults()!=null) {
			query2.setMaxResults( model.getMaxResults().intValue() );
		}
		
		ConsoleQueryParameter[] qp = model.getQueryParameters();
		for (int i = 0; i < qp.length; i++) {
			ConsoleQueryParameter parameter = qp[i];
		
			try {
				int pos = Integer.parseInt(parameter.getName());
				//FIXME no method to set positioned list value
				query2.setParameter(pos, calcValue( parameter ), parameter.getType());
			} catch(NumberFormatException nfe) {
				Object value = parameter.getValue();
				if (value != null && value.getClass().isArray()){
					Object[] values = (Object[])value;
					query2.setParameterList(parameter.getName(), Arrays.asList(values), parameter.getType());
				} else {
					query2.setParameter(parameter.getName(), calcValue( parameter ), parameter.getType());
				}
			}
		}		
	}
	
	private Object calcValue(ConsoleQueryParameter parameter) {
		return parameter.getValueForQuery();				
	}

	@Override
	public QueryResult executeCriteriaQuery(String criteria,
			QueryInputModel queryParameters) {
		return null;
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
					throw new HibernateException("SessionFactory should be closed before building new SessionFactory");
				}
				sessionFactory = configuration.buildSessionFactory();
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
				ConfigurationFactory csf = new ConfigurationFactory(prefs, new HashMap<String, FakeDelegatingDriver>());
				return csf.createConfiguration(cfg, includeMappings);
			}
		});
		return result;
	}
	
	
	/**
	 * Create class loader - so it uses the original urls list from preferences. 
	 */
	protected void reinitClassLoader() {
		boolean recreateFlag = true;
		final URL[] customClassPathURLs = PreferencesClassPathUtils.getCustomClassPathURLs(prefs);
		if (classLoader != null) {
			// check -> do not recreate class loader in case if urls list is the same
			final URL[] oldURLS = classLoader.getURLs();
			if (customClassPathURLs.length == oldURLS.length) {
				int i = 0;
				for (; i < oldURLS.length; i++) {
					if (!customClassPathURLs[i].sameFile(oldURLS[i])) {
						break;
					}
				}
				if (i == oldURLS.length) {
					recreateFlag = false;
				}
			}
		}
		if (recreateFlag) {
			reset();
			classLoader = createClassLoader(customClassPathURLs);
		}
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
		executionContext = null;
		return res;
	}

	@Override
	public boolean hasConfiguration() {
		// TODO Auto-generated method stub
		return configuration != null;
	}

}

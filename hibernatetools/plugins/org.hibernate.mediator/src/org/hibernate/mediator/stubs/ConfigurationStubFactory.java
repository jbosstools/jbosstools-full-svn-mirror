package org.hibernate.mediator.stubs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.DOMWriter;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.osgi.util.NLS;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.resolver.DialectFactory;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.preferences.ConsoleConfigurationPreferences;
import org.hibernate.mediator.preferences.ConsoleConfigurationPreferences.ConfigurationMode;
import org.hibernate.mediator.stubs.util.ReflectHelper;
import org.hibernate.mediator.stubs.util.StringHelper;
import org.hibernate.util.ConfigHelper;
import org.hibernate.util.XMLHelper;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ConfigurationStubFactory {

	private ConsoleConfigurationPreferences prefs = null;
	
	public ConfigurationStubFactory(ConsoleConfigurationPreferences prefs) {
		this.prefs = prefs;
	}

	public ConfigurationStub createConfiguration() {
		ConfigurationStub res = new ConfigurationStub(new Configuration());
		return res;
	}

	public ConfigurationStubJDBCMetaData createConfigurationJDBCMetaData() {
		ConfigurationStubJDBCMetaData res = new ConfigurationStubJDBCMetaData(new JDBCMetaDataConfiguration());
		return res;
	}
	
	public ConfigurationStub createConfiguration(ConfigurationStub cfg, boolean includeMappings) {
		Configuration localCfg = cfg == null ? null : cfg.getConfiguration();
		Properties properties = prefs.getProperties();
		if (properties != null) {
			// to fix: JBIDE-5839 - setup this property: false is default value
			// to make hibernate tools diff hibernate versions compatible
			if (properties.getProperty("hibernate.search.autoregister_listeners") == null) { //$NON-NLS-1$
				properties.setProperty("hibernate.search.autoregister_listeners", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// in case the transaction manager is empty then we need to inject a faketm since
			// hibernate will still try and instantiate it.
			String str = properties.getProperty("hibernate.transaction.manager_lookup_class"); //$NON-NLS-1$
			if (str != null && StringHelper.isEmpty(str)) {
				properties
						.setProperty(
								"hibernate.transaction.manager_lookup_class", "org.hibernate.mediator.FakeTransactionManagerLookup"); //$NON-NLS-1$ //$NON-NLS-2$
				// properties.setProperty( "hibernate.transaction.factory_class", "");
			}
		}
		if (localCfg == null) {
			localCfg = buildConfiguration(properties, includeMappings);
		} else {
			// Properties origProperties = cfg.getProperties();
			// origProperties.putAll(properties);
			// cfg.setProperties(origProperties);
			// TODO: this is actually only for jdbc reveng...
			// localCfg = configureStandardConfiguration( includeMappings, localCfg, properties );
		}
		// non-running databases + i havent been needed until now...
		// TODO: jpa configuration ?
		if (includeMappings) {
			File[] mappingFiles = prefs.getMappingFiles();
			for (int i = 0; i < mappingFiles.length; i++) {
				File hbm = mappingFiles[i];
				localCfg = localCfg.addFile(hbm);
			}
		}
		// TODO: HBX-
		localCfg.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false"); //$NON-NLS-1$//$NON-NLS-2$
		localCfg.setProperty(Environment.HBM2DDL_AUTO, "false"); //$NON-NLS-1$
		ConfigurationStub res = new ConfigurationStub(localCfg);
		// here both setProperties and configxml have had their chance to tell which databasedriver
		// is needed.
		res.registerFakeDriver(localCfg.getProperty(Environment.DRIVER));
		// autoConfigureDialect(localCfg); Disabled for now since it causes very looong timeouts for
		return res;
	}

	private Configuration buildAnnotationConfiguration() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		Class<?> clazz = ReflectHelper
				.classForName("org.hibernate.cfg.AnnotationConfiguration", ConfigurationStubFactory.class); //$NON-NLS-1$
		Configuration annotationConfig = (Configuration)clazz.newInstance();
		return annotationConfig;
	}

	private Configuration buildJPAConfiguration(String persistenceUnit, Properties properties,
			String entityResolver, boolean includeMappings) {
		if (StringHelper.isEmpty(persistenceUnit)) {
			persistenceUnit = null;
		}
		try {
			Map<Object, Object> overrides = new HashMap<Object, Object>();
			if (properties != null) {
				overrides.putAll(properties);
			}
			if (StringHelper.isNotEmpty(prefs.getNamingStrategy())) {
				overrides.put("hibernate.ejb.naming_strategy", prefs.getNamingStrategy()); //$NON-NLS-1$
			}
			if (StringHelper.isNotEmpty(prefs.getDialectName())) {
				overrides.put("hibernate.dialect", prefs.getDialectName()); //$NON-NLS-1$
			}
			if (!includeMappings) {
				overrides.put("hibernate.archive.autodetection", "none"); //$NON-NLS-1$//$NON-NLS-2$
			}
			Class<?> clazz = ReflectHelper.classForName(
					"org.hibernate.ejb.Ejb3Configuration", ConfigurationStubFactory.class); //$NON-NLS-1$
			Object ejb3cfg = clazz.newInstance();

			if (StringHelper.isNotEmpty(entityResolver)) {
				Class<?> resolver = ReflectHelper.classForName(entityResolver, this.getClass());
				Object object = resolver.newInstance();
				Method method = clazz.getMethod(
						"setEntityResolver", new Class[] { EntityResolver.class });//$NON-NLS-1$
				method.invoke(ejb3cfg, new Object[] { object });
			}

			Method method = clazz.getMethod("configure", new Class[] { String.class, Map.class }); //$NON-NLS-1$
			if (method.invoke(ejb3cfg, new Object[] { persistenceUnit, overrides }) == null) {
				String out = NLS.bind(
						Messages.ConsoleConfiguration_persistence_unit_not_found,
						persistenceUnit);
				throw new HibernateConsoleRuntimeException(out, null);
			}

			method = clazz.getMethod("getHibernateConfiguration", new Class[0]);//$NON-NLS-1$
			Object obj = method.invoke(ejb3cfg, (Object[]) null);
			Configuration invoke = (Configuration)obj;
			invoke = configureConnectionProfile(invoke);

			return invoke;
		} catch (HibernateConsoleRuntimeException he) {
			throw he;
		} catch (Exception e) {
			throw new HibernateConsoleRuntimeException(
					Messages.ConsoleConfiguration_could_not_create_jpa_based_configuration,
					e);
		}
	}

	private Configuration buildConfiguration(Properties properties, boolean includeMappings) {
		Configuration localCfg = null;
		if (prefs.getConfigurationMode().equals(ConfigurationMode.ANNOTATIONS)) {
			try {
				localCfg = buildAnnotationConfiguration();
				localCfg = configureStandardConfiguration(includeMappings, localCfg, properties);
			} catch (HibernateConsoleRuntimeException he) {
				throw he;
			} catch (Exception e) {
				throw new HibernateConsoleRuntimeException(
						Messages.ConsoleConfiguration_could_not_load_annotationconfiguration,
						e);
			}
		} else if (prefs.getConfigurationMode().equals(ConfigurationMode.JPA)) {
			try {
				localCfg = buildJPAConfiguration(prefs.getPersistenceUnitName(), properties, prefs
						.getEntityResolverName(), includeMappings);
			} catch (HibernateConsoleRuntimeException he) {
				throw he;
			} catch (Exception e) {
				throw new HibernateConsoleRuntimeException(
						Messages.ConsoleConfiguration_could_not_load_jpa_configuration, e);
			}
		} else {
			localCfg = new Configuration();
			localCfg = configureStandardConfiguration(includeMappings, localCfg, properties);
		}
		return localCfg;
	}

	private Configuration configureStandardConfiguration(final boolean includeMappings,
			Configuration localCfg, Properties properties) {
		if (properties != null) {
			localCfg = localCfg.setProperties(properties);
		}
		EntityResolver entityResolver = XMLHelper.DEFAULT_DTD_RESOLVER;
		if (StringHelper.isNotEmpty(prefs.getEntityResolverName())) {
			try {
				entityResolver = (EntityResolver) ReflectHelper.classForName(
						prefs.getEntityResolverName()).newInstance();
			} catch (Exception c) {
				throw new HibernateConsoleRuntimeException(
						Messages.ConsoleConfiguration_could_not_configure_entity_resolver
								+ prefs.getEntityResolverName(), c);
			}
		}
		localCfg.setEntityResolver(entityResolver);
		if (StringHelper.isNotEmpty(prefs.getNamingStrategy())) {
			try {
				NamingStrategy ns = (NamingStrategy) ReflectHelper.classForName(
						prefs.getNamingStrategy()).newInstance();
				localCfg.setNamingStrategy(ns);
			} catch (Exception c) {
				throw new HibernateConsoleRuntimeException(
						Messages.ConsoleConfiguration_could_not_configure_naming_strategy
								+ prefs.getNamingStrategy(), c);
			}
		}
		localCfg = loadConfigurationXML(localCfg, includeMappings, entityResolver);
		localCfg = configureConnectionProfile(localCfg);

		// replace dialect if it is set in preferences
		if (StringHelper.isNotEmpty(prefs.getDialectName())) {
			localCfg.setProperty("hibernate.dialect", prefs.getDialectName()); //$NON-NLS-1$
		}

		return localCfg;
	}

	@SuppressWarnings("unchecked")
	private Configuration loadConfigurationXML(Configuration localCfg, boolean includeMappings,
			EntityResolver entityResolver) {
		File configXMLFile = prefs.getConfigXMLFile();
		if (!includeMappings) {
			org.dom4j.Document doc;
			XMLHelper xmlHelper = new XMLHelper();
			InputStream stream = null;
			String resourceName = "<unknown>"; //$NON-NLS-1$
			if (configXMLFile != null) {
				resourceName = configXMLFile.toString();
				try {
					stream = new FileInputStream(configXMLFile);
				} catch (FileNotFoundException e1) {
					throw new HibernateConsoleRuntimeException(
							Messages.ConsoleConfiguration_could_not_access + configXMLFile,
							e1);
				}
			} else {
				resourceName = "/hibernate.cfg.xml"; //$NON-NLS-1$
				if (checkHibernateResoureExistence(resourceName)) {
					stream = ConfigHelper.getResourceAsStream(resourceName); // simulate hibernate's
					// default look up
				} else {
					return localCfg;
				}
			}

			try {
				List<Throwable> errors = new ArrayList<Throwable>();

				doc = xmlHelper.createSAXReader(resourceName, errors, entityResolver).read(
						new InputSource(stream));
				if (errors.size() != 0) {
					throw new MappingException(
							Messages.ConsoleConfiguration_invalid_configuration, errors
									.get(0));
				}

				List<Node> list = doc.getRootElement()
						.element("session-factory").elements("mapping"); //$NON-NLS-1$ //$NON-NLS-2$
				for (Node element : list) {
					element.getParent().remove(element);
				}

				DOMWriter dw = new DOMWriter();
				Document document = dw.write(doc);
				return localCfg.configure(document);

			} catch (DocumentException e) {
				throw new HibernateException(
						Messages.ConsoleConfiguration_could_not_parse_configuration
								+ resourceName, e);
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException ioe) {
					// log.warn( "could not close input stream for: " + resourceName, ioe );
				}
			}
		} else {
			if (configXMLFile != null) {
				return localCfg.configure(configXMLFile);
			} else {
				Configuration resultCfg = localCfg;
				if (checkHibernateResoureExistence("/hibernate.cfg.xml")) { //$NON-NLS-1$
					resultCfg = localCfg.configure();
				}
				return resultCfg;
			}
		}
	}

	private boolean checkHibernateResoureExistence(String resource) {
		InputStream is = null;
		try {
			is = ConfigHelper.getResourceAsStream(resource);
		} catch (HibernateException e) {
			// just ignore
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				// ignore
			}
		}
		return (is != null);
	}

	private Configuration configureConnectionProfile(Configuration localCfg) {
		String connectionProfile = prefs.getConnectionProfileName();
		if (connectionProfile == null) {
			return localCfg;
		}
		IConnectionProfile profile = ProfileManager.getInstance().getProfileByName(
				connectionProfile);
		if (profile != null) {
			refreshProfile(profile);
			//
			final Properties invokeProperties = localCfg.getProperties();
			// set this property to null!
			invokeProperties.remove(Environment.DATASOURCE);
			localCfg.setProperties(invokeProperties);
			Properties cpProperties = profile.getProperties(profile.getProviderId());
			// seems we should not setup dialect here
			// String dialect =
			// "org.hibernate.dialect.HSQLDialect";//cpProperties.getProperty("org.eclipse.datatools.connectivity.db.driverClass");
			// invoke.setProperty(Environment.DIALECT, dialect);
			String driver = cpProperties
					.getProperty("org.eclipse.datatools.connectivity.db.driverClass"); //$NON-NLS-1$
			localCfg.setProperty(Environment.DRIVER, driver);
			// TODO:
			@SuppressWarnings("unused")
			String driverJarPath = cpProperties.getProperty("jarList"); //$NON-NLS-1$
			String url = cpProperties.getProperty("org.eclipse.datatools.connectivity.db.URL"); //$NON-NLS-1$
			// url += "/";// +
			// cpProperties.getProperty("org.eclipse.datatools.connectivity.db.databaseName");
			localCfg.setProperty(Environment.URL, url);
			String user = cpProperties
					.getProperty("org.eclipse.datatools.connectivity.db.username"); //$NON-NLS-1$
			if (null != user && user.length() > 0) {
				localCfg.setProperty(Environment.USER, user);
			}
			String pass = cpProperties
					.getProperty("org.eclipse.datatools.connectivity.db.password"); //$NON-NLS-1$
			if (null != pass && pass.length() > 0) {
				localCfg.setProperty(Environment.PASS, pass);
			}
		} else {
			String out = NLS.bind(
					Messages.ConsoleConfiguration_connection_profile_not_found,
					connectionProfile);
			throw new HibernateConsoleRuntimeException(out, null);
		}
		return localCfg;
	}

	@SuppressWarnings("unused")
	private void autoConfigureDialect(Configuration localCfg) {
		if (localCfg.getProperty(Environment.DIALECT) == null) {
			String url = localCfg.getProperty(Environment.URL);
			String user = localCfg.getProperty(Environment.USER);
			String pass = localCfg.getProperty(Environment.PASS);
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, user, pass);
				// SQL Dialect:
				Dialect dialect = DialectFactory.buildDialect(localCfg.getProperties(), connection);
				localCfg.setProperty(Environment.DIALECT, dialect.toString());
			} catch (SQLException e) {
				// can't determine dialect
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
	}

	public static void refreshProfile(IConnectionProfile profile) {
		// refresh profile (refresh jpa connection):
		// get fresh information about current db structure and update error markers
		if (profile.getConnectionState() == IConnectionProfile.CONNECTED_STATE) {
			profile.disconnect(null);
		}
		profile.connect(null);
	}
}

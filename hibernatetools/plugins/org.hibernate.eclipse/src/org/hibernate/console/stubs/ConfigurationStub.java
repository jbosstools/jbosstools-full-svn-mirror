package org.hibernate.console.stubs;

import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.cfg.Settings;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.FakeDelegatingDriver;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.hbm2x.HibernateMappingExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.ide.completion.IHQLCodeAssist;
import org.hibernate.util.ReflectHelper;
import org.xml.sax.EntityResolver;

public class ConfigurationStub {
	
	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	// configuration != null - by default
	protected Configuration configuration;

	protected ConfigurationStub(Configuration configuration) {
		this.configuration = configuration;
	}

	protected Configuration getConfiguration() {
		return configuration;
	}

	public void cleanUp() {
		Iterator<FakeDelegatingDriver> it = fakeDrivers.values().iterator();
		while (it.hasNext()) {
			try {
				DriverManager.deregisterDriver(it.next());
			} catch (SQLException e) {
				// ignore
			}
		}
		fakeDrivers.clear();
	}

	public void buildMappings() {
		configuration.buildMappings();
	}

	// TODO: temporary should be protected and/or SettingsStub?
	public Settings buildSettings() {
		return configuration.buildSettings();
	}

	protected SessionFactory buildSessionFactory() {
		return configuration.buildSessionFactory();
	}

	public EntityResolver getEntityResolver() {
		return configuration.getEntityResolver();
	}

	public IHQLCodeAssist getHQLCodeAssist() {
		return new HQLCodeAssist(configuration);
	}
	
	// TODO: temporary should be protected and/or NamingStrategyStub?
	public NamingStrategy getNamingStrategy() {
		return configuration.getNamingStrategy();
	}
	
	// TODO: temporary?
	@SuppressWarnings("unchecked")
	public Iterator getClassMappings() {
		return configuration.getClassMappings();
	}
	
	// TODO: temporary should be protected and/or PersistentClassStub?
	public PersistentClass getClassMapping(String entityName) {
		return configuration.getClassMapping(entityName);
	}

	// TODO: temporary should be protected and/or MappingsStub?
	public Mappings createMappings() {
		return configuration.createMappings();
	}

	// TODO: temporary should be protected and/or MappingsStub?
	public Mapping buildMapping() {
		return configuration.buildMapping();
	}

	public static interface IExporterNewOutputDir {
		File getNewOutputDir(POJOClass element, File outputdir4FileNew);
	}
	
	public HibernateMappingExporter createHibernateMappingExporter(File folder2Gen, final IExporterNewOutputDir enod) {
		return new HibernateMappingExporter(configuration, folder2Gen) {
			@SuppressWarnings("unchecked")
			protected void exportPOJO(Map additionalContext, POJOClass element) {
				File outputdir4FileOld = getOutputDirectory();
				File outputdir4FileNew = enod.getNewOutputDir(element, outputdir4FileOld);
				if (!outputdir4FileNew.exists()) {
					outputdir4FileNew.mkdirs();
				}
				setOutputDirectory(outputdir4FileNew);
				super.exportPOJO(additionalContext, element);
				setOutputDirectory(outputdir4FileOld);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<Throwable> doSchemaExport() {
		SchemaExport export = new SchemaExport(configuration);
		export.create(false, true);
		if (!export.getExceptions().isEmpty()) {
			return (Iterator<Throwable>)export.getExceptions().iterator();
		}
		return null;
	}
	
	public void updateExporter(Exporter exporter) {
		exporter.setConfiguration(configuration);
	}
	
	public Properties getProperties() {
		return configuration.getProperties();
	}
	
	public String getProperty(String propertyName) {
		return configuration.getProperty(propertyName);
	}
	
	/**
	 * DriverManager checks what classloader a class is loaded from thus we register a FakeDriver
	 * that we know is loaded "properly" which delegates all it class to the real driver. By doing
	 * so we can convince DriverManager that we can use any dynamically loaded driver.
	 * 
	 * @param driverClassName
	 */
	@SuppressWarnings("unchecked")
	protected void registerFakeDriver(String driverClassName) {
		if (driverClassName != null) {
			try {
				Class<Driver> driverClass = ReflectHelper.classForName(driverClassName);
				if (!fakeDrivers.containsKey(driverClassName)) { // To avoid "double registration"
					FakeDelegatingDriver fakeDelegatingDriver = new FakeDelegatingDriver(
							driverClass.newInstance());
					DriverManager.registerDriver(fakeDelegatingDriver);
					fakeDrivers.put(driverClassName, fakeDelegatingDriver);
				}
			} catch (ClassNotFoundException e) {
				String out = NLS
						.bind(
								ConsoleMessages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			} catch (InstantiationException e) {
				String out = NLS
						.bind(
								ConsoleMessages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			} catch (IllegalAccessException e) {
				String out = NLS
						.bind(
								ConsoleMessages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			} catch (SQLException e) {
				String out = NLS
						.bind(
								ConsoleMessages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			}
		}
	}
}

package org.hibernate.mediator.stubs;

import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.FakeDelegatingDriver;
import org.hibernate.mediator.stubs.util.ReflectHelper;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.xml.sax.EntityResolver;

public class ConfigurationStub {
	
	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	// configuration != null - by default
	protected Configuration configuration;

	protected ConfigurationStub(Configuration configuration) {
		if (configuration == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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

	public SettingsStub buildSettings() {
		Object obj = configuration.buildSettings();
		if (obj == null) {
			return null;
		}
		return new SettingsStub(obj);
	}

	protected SessionFactory buildSessionFactory() {
		return configuration.buildSessionFactory();
	}

	public EntityResolver getEntityResolver() {
		return configuration.getEntityResolver();
	}

	public IHQLCodeAssistStub getHQLCodeAssist() {
		return new IHQLCodeAssistStub(new HQLCodeAssist(configuration));
	}
	
	public NamingStrategyStub getNamingStrategy() {
		Object obj = configuration.getNamingStrategy();
		if (obj == null) {
			return null;
		}
		return new NamingStrategyStub(obj);
	}

	public boolean hasNamingStrategy() {
		return (configuration.getNamingStrategy() != null);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PersistentClassStub> getClassMappings() {
		Iterator<PersistentClass> it = (Iterator<PersistentClass>)configuration.getClassMappings();
		ArrayList<PersistentClassStub> arr = new ArrayList<PersistentClassStub>();
		while (it.hasNext()) {
			arr.add(PersistentClassStubFactory.createPersistentClassStub(it.next()));
		}
		return arr.iterator();
	}
	
	public PersistentClassStub getClassMapping(String entityName) {
		return PersistentClassStubFactory.createPersistentClassStub(configuration.getClassMapping(entityName));
	}

	public MappingsStub createMappings() {
		Object obj = configuration.createMappings();
		if (obj == null) {
			return null;
		}
		return new MappingsStub(obj);
	}

	public MappingStub buildMapping() {
		Object obj = configuration.buildMapping();
		if (obj == null) {
			return null;
		}
		return new MappingStub(obj);
	}

	public static interface IExporterNewOutputDir {
		File getNewOutputDir(POJOClassStub element, File outputdir4FileNew);
	}
	
	public HibernateMappingExporterStub createHibernateMappingExporter(File folder2Gen, final IExporterNewOutputDir enod) {
		return new HibernateMappingExporterStub(this, folder2Gen) {
			@SuppressWarnings("unchecked")
			protected void exportPOJO(Map additionalContext, POJOClassStub element) {
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
	
	public void updateExporter(ExporterStub exporter) {
		exporter.setConfiguration(this);
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
								Messages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			} catch (InstantiationException e) {
				String out = NLS
						.bind(
								Messages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			} catch (IllegalAccessException e) {
				String out = NLS
						.bind(
								Messages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			} catch (SQLException e) {
				String out = NLS
						.bind(
								Messages.ConsoleConfiguration_problems_while_loading_database_driverclass,
								driverClassName);
				throw new HibernateConsoleRuntimeException(out, e);
			}
		}
	}
}

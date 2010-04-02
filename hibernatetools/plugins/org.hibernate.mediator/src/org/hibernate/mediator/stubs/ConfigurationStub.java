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
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.FakeDelegatingDriver;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.stubs.util.ReflectHelper;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

public class ConfigurationStub extends HObject {
	public static final String CL = "org.hibernate.cfg.Configuration"; //$NON-NLS-1$

	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	protected ConfigurationStub(Object configuration) {
		super(configuration, CL);
	}

	public static ConfigurationStub newInstance() {
		return new ConfigurationStub(newInstance(CL));
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
		invoke("buildMappings"); //$NON-NLS-1$
	}

	public SettingsStub buildSettings() {
		Object obj = invoke("buildSettings"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new SettingsStub(obj);
	}

	public SessionFactoryStub buildSessionFactory() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new SessionFactoryStub(obj);
	}

	public EntityResolver getEntityResolver() {
		return (EntityResolver)invoke("getEntityResolver"); //$NON-NLS-1$
	}

	public IHQLCodeAssistStub getHQLCodeAssist() {
		// TODO: fix this
		return new IHQLCodeAssistStub(new HQLCodeAssist((Configuration)Obj()));
	}
	
	public NamingStrategyStub getNamingStrategy() {
		Object obj = invoke("getNamingStrategy"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new NamingStrategyStub(obj);
	}

	public boolean hasNamingStrategy() {
		return (invoke("getNamingStrategy") != null); //$NON-NLS-1$
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PersistentClassStub> getClassMappings() {
		Iterator<PersistentClass> it = (Iterator<PersistentClass>)invoke("getClassMappings"); //$NON-NLS-1$
		ArrayList<PersistentClassStub> arr = new ArrayList<PersistentClassStub>();
		while (it.hasNext()) {
			arr.add(PersistentClassStubFactory.createPersistentClassStub(it.next()));
		}
		return arr.iterator();
	}
	
	public PersistentClassStub getClassMapping(String entityName) {
		return PersistentClassStubFactory.createPersistentClassStub(invoke("getClassMapping", entityName)); //$NON-NLS-1$
	}

	public MappingsStub createMappings() {
		Object obj = invoke("createMappings"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new MappingsStub(obj);
	}

	public MappingStub buildMapping() {
		Object obj = invoke("buildMapping"); //$NON-NLS-1$
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
		// TODO: fix this
		SchemaExport export = new SchemaExport((Configuration)Obj());
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
		return (Properties)invoke("getProperties"); //$NON-NLS-1$
	}
	
	public String getProperty(String propertyName) {
		return (String)invoke("getProperty", propertyName); //$NON-NLS-1$
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

	public ConfigurationStub setProperties(Properties properties) {
		Object tmp = invoke("setProperties", properties); //$NON-NLS-1$
		return (tmp == Obj() ? this : new ConfigurationStub(tmp));
	}

	public void setProperty(String propertyName, String value) {
		invoke("setProperty", propertyName, value); //$NON-NLS-1$
	}

	public ConfigurationStub configure(Document document) {
		Object tmp = invoke("configure", document); //$NON-NLS-1$
		return (tmp == Obj() ? this : new ConfigurationStub(tmp));
	}

	public ConfigurationStub configure() {
		Object tmp = invoke("configure"); //$NON-NLS-1$
		return (tmp == Obj() ? this : new ConfigurationStub(tmp));
	}

	public ConfigurationStub configure(File configFile) {
		Object tmp = invoke("configure", configFile); //$NON-NLS-1$
		return (tmp == Obj() ? this : new ConfigurationStub(tmp));
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		invoke("setEntityResolver", entityResolver); //$NON-NLS-1$
	}

	public void setNamingStrategy(NamingStrategyStub ns) {
		invoke("setNamingStrategy", ns); //$NON-NLS-1$
	}

	public ConfigurationStub addFile(File xmlFile) {
		Object tmp = invoke("addFile", xmlFile); //$NON-NLS-1$
		return (tmp == Obj() ? this : new ConfigurationStub(tmp));
	}
}

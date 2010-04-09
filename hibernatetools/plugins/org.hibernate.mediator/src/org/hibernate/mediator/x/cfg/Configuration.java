package org.hibernate.mediator.x.cfg;

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
import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.FakeDelegatingDriver;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.stubs.util.ReflectHelper;
import org.hibernate.mediator.x.SessionFactory;
import org.hibernate.mediator.x.engine.Mapping;
import org.hibernate.mediator.x.mapping.PersistentClass;
import org.hibernate.mediator.x.mapping.PersistentClassFactory;
import org.hibernate.mediator.x.tool.hbm2ddl.SchemaExport;
import org.hibernate.mediator.x.tool.hbm2x.Exporter;
import org.hibernate.mediator.x.tool.hbm2x.HibernateMappingExporter;
import org.hibernate.mediator.x.tool.hbm2x.pojo.POJOClass;
import org.hibernate.mediator.x.tool.ide.completion.HQLCodeAssist;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

public class Configuration extends HObject {
	public static final String CL = "org.hibernate.cfg.Configuration"; //$NON-NLS-1$

	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	protected Configuration(Object configuration) {
		super(configuration, CL);
	}

	protected Configuration(Object configuration, String cn) {
		super(configuration, cn);
	}

	public static Configuration newInstance() {
		return new Configuration(HObject.newInstance(CL));
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
		invoke(mn());
	}

	public Settings buildSettings() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Settings(obj);
	}

	public SessionFactory buildSessionFactory() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new SessionFactory(obj);
	}

	public EntityResolver getEntityResolver() {
		return (EntityResolver)invoke(mn());
	}

	public HQLCodeAssist getHQLCodeAssist() {
		return HQLCodeAssist.createHQLCodeAssist(this);
	}
	
	public NamingStrategy getNamingStrategy() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new NamingStrategy(obj);
	}

	public boolean hasNamingStrategy() {
		return (invoke("getNamingStrategy") != null); //$NON-NLS-1$
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PersistentClass> getClassMappings() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<PersistentClass> arr = new ArrayList<PersistentClass>();
		while (it.hasNext()) {
			arr.add(PersistentClassFactory.createPersistentClassStub(it.next()));
		}
		return arr.iterator();
	}
	
	public PersistentClass getClassMapping(String entityName) {
		return PersistentClassFactory.createPersistentClassStub(invoke(mn(), entityName));
	}

	public Mappings createMappings() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Mappings(obj);
	}

	public Mapping buildMapping() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Mapping(obj);
	}

	public static interface IExporterNewOutputDir {
		File getNewOutputDir(POJOClass element, File outputdir4FileNew);
	}
	
	public HibernateMappingExporter createHibernateMappingExporter(File folder2Gen, final IExporterNewOutputDir enod) {
		return new HibernateMappingExporter(this, folder2Gen) {
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
		SchemaExport export = SchemaExport.createSchemaExport(this);
		export.create(false, true);
		if (!export.getExceptions().isEmpty()) {
			return (Iterator<Throwable>)export.getExceptions().iterator();
		}
		return null;
	}
	
	public void updateExporter(Exporter exporter) {
		exporter.setConfiguration(this);
	}
	
	public Properties getProperties() {
		return (Properties)invoke(mn());
	}
	
	public String getProperty(String propertyName) {
		return (String)invoke(mn(), propertyName);
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

	public Configuration setProperties(Properties properties) {
		Object tmp = invoke(mn(), properties);
		return (tmp == Obj() ? this : new Configuration(tmp));
	}

	public void setProperty(String propertyName, String value) {
		invoke(mn(), propertyName, value);
	}

	public Configuration configure(Document document) {
		Object tmp = invoke(mn(), document);
		return (tmp == Obj() ? this : new Configuration(tmp));
	}

	public Configuration configure() {
		Object tmp = invoke(mn());
		return (tmp == Obj() ? this : new Configuration(tmp));
	}

	public Configuration configure(File configFile) {
		Object tmp = invoke(mn(), configFile);
		return (tmp == Obj() ? this : new Configuration(tmp));
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		invoke(mn(), entityResolver);
	}

	public void setNamingStrategy(NamingStrategy ns) {
		invoke(mn(), ns);
	}

	public Configuration addFile(File xmlFile) {
		Object tmp = invoke(mn(), xmlFile);
		return (tmp == Obj() ? this : new Configuration(tmp));
	}
}

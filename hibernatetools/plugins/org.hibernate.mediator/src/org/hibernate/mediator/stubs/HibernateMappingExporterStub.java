package org.hibernate.mediator.stubs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.hibernate.mediator.base.HObject;

public class HibernateMappingExporterStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateMappingExporter"; //$NON-NLS-1$

	protected HibernateMappingExporterStub(ConfigurationStub cfg, File outputdir) {
		super(HObject.newInstance(CL, cfg, outputdir), CL);    	
    }

	public void setGlobalSettings(HibernateMappingGlobalSettingsStub hmgs) {
		invoke(mn(), hmgs);
	}

	public void start() {
		invoke(mn());
	}

	protected File getOutputDirectory() {
		return (File)invoke(mn());
	}

	protected void setOutputDirectory(File outputdir) {
		invoke(mn(), outputdir);
	}

	@SuppressWarnings("unchecked")
	protected void exportPOJO(Map additionalContext, POJOClassStub element) {
		// protected -> call via reflection
		Method m = null;
		try {
			m = Cl().getDeclaredMethod("exportPOJO");//$NON-NLS-1$
		} catch (SecurityException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		m.setAccessible(true);
		try {
			m.invoke(Obj(), additionalContext, element.Obj());
		} catch (IllegalArgumentException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
	}

	public ArtifactCollectorStub getArtifactCollector() {
		return new ArtifactCollectorStub(invoke(mn()));
	}

}

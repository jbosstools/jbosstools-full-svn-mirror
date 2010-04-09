package org.hibernate.mediator.x.tool.hbm2x;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.cfg.Configuration;
import org.hibernate.mediator.x.tool.hbm2x.pojo.POJOClass;

public class HibernateMappingExporter extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateMappingExporter"; //$NON-NLS-1$

	protected HibernateMappingExporter(Configuration cfg, File outputdir) {
		super(HObject.newInstance(CL, cfg, outputdir), CL);    	
    }

	public void setGlobalSettings(HibernateMappingGlobalSettings hmgs) {
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
	protected void exportPOJO(Map additionalContext, POJOClass element) {
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

	public ArtifactCollector getArtifactCollector() {
		return new ArtifactCollector(invoke(mn()));
	}

}

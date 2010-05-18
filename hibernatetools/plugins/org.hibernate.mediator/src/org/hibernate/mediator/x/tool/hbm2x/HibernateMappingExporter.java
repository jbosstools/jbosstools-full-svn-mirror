package org.hibernate.mediator.x.tool.hbm2x;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.stubs.util.ReflectHelper;
import org.hibernate.mediator.x.cfg.Configuration;
import org.hibernate.mediator.x.tool.hbm2x.pojo.POJOClass;

public class HibernateMappingExporter extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateMappingExporter"; //$NON-NLS-1$
	
	public interface IExportPOJOInterceptor {
		public Object exportPOJO(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable;
	}

	protected HibernateMappingExporter(Object hibernateMappingExporter) {
		super(hibernateMappingExporter, CL);    	
    }

	public static HibernateMappingExporter newInstance(Object hibernateMappingExporter) {
		return new HibernateMappingExporter(hibernateMappingExporter);
	}

	public static HibernateMappingExporter newInstance(Configuration cfg, File outputdir, final IExportPOJOInterceptor exportPOJOInterceptor) {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(CL);
		} catch (ClassNotFoundException ex) {
			throw new HibernateConsoleRuntimeException(ex);
		}
		MethodInterceptor mi = new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				return exportPOJOInterceptor.exportPOJO(obj, method, args, proxy);
			}
		};
		Enhancer e = createEnhancer(clazz, mi);
		try {
			clazz = ReflectHelper.classForName(Configuration.CL);
		} catch (ClassNotFoundException ex) {
			throw new HibernateConsoleRuntimeException(ex);
		}
		Object hibernateMappingExporter = e.create(new Class[] { clazz, File.class },
			new Object[] { cfg.Obj(), outputdir } );
		return new HibernateMappingExporter(hibernateMappingExporter);
	}
	
	private static final CallbackFilter baseExportPOJO = new CallbackFilter() {
		public int accept(Method method) {
			if ("exportPOJO".equals(method.getName() ) ) {
				return 1;
			}
			return 0;
		}
	};
	
	public static Enhancer createEnhancer(Class<?> clazz, MethodInterceptor mi) {
		Enhancer e = new Enhancer();
        e.setSuperclass(clazz);
		MethodInterceptor miDef = new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				return proxy.invokeSuper(obj, args);
			}
		};
        e.setCallbacks(new Callback[] { miDef, mi });
        e.setCallbackFilter(baseExportPOJO);
        return e;
	}
	
	public void setGlobalSettings(HibernateMappingGlobalSettings hmgs) {
		invoke(mn(), hmgs);
	}

	public void start() {
		invoke(mn());
	}

	public File getOutputDirectory() {
		return (File)invoke(mn());
	}

	public void setOutputDirectory(File outputdir) {
		invoke(mn(), outputdir);
	}

	@SuppressWarnings("unchecked")
	public void exportPOJO(Map additionalContext, POJOClass element) {
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

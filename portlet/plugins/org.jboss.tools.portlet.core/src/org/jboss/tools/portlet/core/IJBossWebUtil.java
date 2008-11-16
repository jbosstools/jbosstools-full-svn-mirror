package org.jboss.tools.portlet.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IJBossWebUtil {

	static final Object JSF_SERVLET_CLASS = "javax.faces.webapp.FacesServlet"; //$NON-NLS-1$
	static final String WEB_INF_FACES_CONFIG_XML = "/WEB-INF/faces-config.xml"; //$NON-NLS-1$
	static final String JAVAX_FACES_CONFIG_FILES = "javax.faces.CONFIG_FILES"; //$NON-NLS-1$

	void configureContextParam( IProject project,
			 IProgressMonitor monitor,String name, String value,String description);

	void configureFilter(IProject project,
			 IProgressMonitor monitor, String name, String className,String displayName,
				String description);

	void configureFilterMapping(IProject project,
			 IProgressMonitor monitor, String name, String servletName);

	String findJsfServlet(Object modelObject);

	String getFacesConfig(IProject project,
			IProgressMonitor monitor);
}

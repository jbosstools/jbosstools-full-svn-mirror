package org.jboss.tools.birt.core.internal.project.facet;

import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IBirtUtil {

	void configureWebApp(WebAppBean webAppBean, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureContextParam(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureListener(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureServlet(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureServletMapping(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureFilter(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureFilterMapping(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

	void configureTaglib(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor);

}

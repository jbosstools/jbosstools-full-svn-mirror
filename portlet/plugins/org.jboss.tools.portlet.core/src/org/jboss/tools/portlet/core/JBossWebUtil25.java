package org.jboss.tools.portlet.core;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.core.Description;
import org.eclipse.jst.javaee.core.DisplayName;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.ParamValue;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.DispatcherType;
import org.eclipse.jst.javaee.web.Filter;
import org.eclipse.jst.javaee.web.FilterMapping;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.ui.dialogs.IOverwriteQuery;

public class JBossWebUtil25 implements IJBossWebUtil {

	public void configureContextParam(IProject project,
			IProgressMonitor monitor, String name, String value,
			String description) {
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null)
			return;
		IOverwriteQuery query = PortletCoreActivator.OVERWRITE_ALL_QUERY;
		List list = webApp.getContextParams();
		int index = getContextParamIndexByName(list, name);
		if (index >= 0) {
			String ret = query.queryOverwrite("Context-param '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$

			// check overwrite query result
			if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
				monitor.setCanceled(true);
				return;
			}
			if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
				monitor.setCanceled(true);
				return;
			}

			// remove old item
			list.remove(index);
		}

		ParamValue param = JavaeeFactory.eINSTANCE.createParamValue();
		param.setParamName(name);
		param.setParamValue(value);
		if (description != null) {
			Description descriptionObj = JavaeeFactory.eINSTANCE
					.createDescription();
			descriptionObj.setValue(description);
			param.getDescriptions().add(descriptionObj);

		}

		// add into list
		webApp.getContextParams().add(param);

	}

	private int getContextParamIndexByName(List list, String name) {
		if (list == null || name == null)
			return -1;
		Iterator it = list.iterator();
		int index = 0;
		while (it.hasNext()) {
			Object paramObj = it.next();
			if (paramObj instanceof ParamValue) {
				ParamValue param = (ParamValue) paramObj;
				if (name.equals(param.getParamName()))
					return index;
			}
			index++;
		}
		return -1;
	}

	public void configureFilter(IProject project, IProgressMonitor monitor,
			String name, String className, String displayName,
			String description) {
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null)
			return;
		IOverwriteQuery query = PortletCoreActivator.OVERWRITE_ALL_QUERY;

		// if contained this filter
		Object obj = getFilterByName(webApp, name);
		if (obj != null) {
			String ret = query.queryOverwrite("Filter '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			// check overwrite query result
			if (IOverwriteQuery.NO.equalsIgnoreCase(ret)
					|| IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
				monitor.setCanceled(true);
				return;
			}
			// remove old item
			webApp.getFilters().remove(obj);
		}
		// create filter object
		Filter filter = WebFactory.eINSTANCE.createFilter();
		filter.setFilterName(name);
		filter.setFilterClass(className);
		DisplayName dName = JavaeeFactory.eINSTANCE.createDisplayName();
		dName.setValue(displayName);
		filter.getDisplayNames().add(dName);
		Description descriptionObj = JavaeeFactory.eINSTANCE
				.createDescription();
		descriptionObj.setValue(description);
		webApp.getFilters().add(filter);

	}

	private Object getFilterByName(WebApp webApp, String name) {
		if (webApp == null || name == null)
			return null;
		List filters = webApp.getFilters();
		for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
			Filter filter = (Filter) iterator.next();
			if (filter != null && name.equals(filter.getFilterName()))
				return filter;
		}

		return null;
	}

	public void configureFilterMapping(IProject project,
			IProgressMonitor monitor, String name, String servletName) {
		// FIXME check if filter mapping already exists
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null)
			return;
		IOverwriteQuery query = PortletCoreActivator.OVERWRITE_ALL_QUERY;

		// if contained this filter-mapping
		Object obj = getFilterMappingByKey(webApp.getFilterMappings(), name);
		if (obj != null) {
			String ret = query.queryOverwrite("Filter-mapping '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			// check overwrite query result
			if (IOverwriteQuery.NO.equalsIgnoreCase(ret)
					|| IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
				monitor.setCanceled(true);
				return;
			}
			// remove old item
			webApp.getFilterMappings().remove(obj);
		}
		// filter name
		// create FilterMapping object
		FilterMapping mapping = WebFactory.eINSTANCE.createFilterMapping();
		// get filter by name
		Filter filter = (Filter) getFilterByName(webApp, name);
		if (filter != null) {
			mapping.setFilterName(filter.getFilterName());
			mapping.getDispatchers().add(DispatcherType.REQUEST_LITERAL);
			mapping.getDispatchers().add(DispatcherType.FORWARD_LITERAL);
			mapping.getDispatchers().add(DispatcherType.INCLUDE_LITERAL);
			mapping.getServletNames().add(servletName);

			// get Servlet object
			// Servlet servlet = findServletByName(webApp, servletName);
			// mapping.setServlet(servlet);
			// if (servlet != null || uri != null)
			webApp.getFilterMappings().add(mapping);
		}
	}

	private Object getFilterMappingByKey(List list, String key) {
		if (list == null || key == null)
			return null;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get filter-mapping object
			FilterMapping filterMapping = (FilterMapping) it.next();
			if (filterMapping != null) {
				String name = filterMapping.getFilterName();
				List servletNames = filterMapping.getServletNames();
				for (Iterator iterator = servletNames.iterator(); iterator
						.hasNext();) {
					String servletName = (String) iterator.next();
					List urlPatterns = filterMapping.getUrlPatterns();
					for (Iterator iterator2 = urlPatterns.iterator(); iterator2
							.hasNext();) {
						UrlPatternType urlPattern = (UrlPatternType) iterator2
								.next();
						String uri = urlPattern.getValue();
						String curKey = getFilterMappingString(name,
								servletName, uri);
						if (key.equals(curKey))
							return filterMapping;
					}

				}

			}
		}
		return null;
	}

	private String getFilterMappingString(String name, String servletName,
			String uri) {
		return (name != null ? name : "") //$NON-NLS-1$
				+ (servletName != null ? servletName : "") //$NON-NLS-1$
				+ (uri != null ? uri : ""); //$NON-NLS-1$
	}

	private Servlet findServletByName(WebApp webApp, String name) {
		Iterator it = webApp.getServlets().iterator();
		while (it.hasNext()) {
			Servlet servlet = (Servlet) it.next();
			if (servlet.getServletName() != null
					&& servlet.getServletName().trim().equals(name)) {
				return servlet;
			}
		}
		return null;
	}

	private WebApp getWebApp(IProject project, IProgressMonitor monitor) {
		if (monitor.isCanceled())
			return null;
		if (project == null) {
			return null;
		}
		IModelProvider modelProvider = ModelProviderManager
				.getModelProvider(project);
		Object modelObject = modelProvider.getModelObject();
		if (!(modelObject instanceof WebApp)) {
			return null;
		}
		WebApp webApp = (WebApp) modelObject;
		return webApp;
	}

	public String findJsfServlet(Object modelObject) {
		WebApp webApp = (WebApp) modelObject;
		Iterator it = webApp.getServlets().iterator();
		while (it.hasNext()) {
			Servlet servlet = (Servlet) it.next();
			if (servlet.getServletClass() != null
					&& servlet.getServletClass().trim().equals(
							JSF_SERVLET_CLASS)) {
				return servlet.getServletName();
			}
		}
		return null;
	}

	public String getFacesConfig(IProject project, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null) {
			return null;
		}
		List contextParams = webApp.getContextParams();
		for (Iterator iterator = contextParams.iterator(); iterator.hasNext();) {
			Object paramObj = (Object) iterator.next();
			if (paramObj instanceof ParamValue) {
				ParamValue param = (ParamValue) paramObj;
				if (JAVAX_FACES_CONFIG_FILES.equals(param.getParamName()))
					return param.getParamValue();
			}
		}
		return WEB_INF_FACES_CONFIG_XML;
	}

}

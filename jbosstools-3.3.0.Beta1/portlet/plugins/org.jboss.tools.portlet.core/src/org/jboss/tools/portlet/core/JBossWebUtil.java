package org.jboss.tools.portlet.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.j2ee.common.CommonFactory;
import org.eclipse.jst.j2ee.common.Description;
import org.eclipse.jst.j2ee.common.ParamValue;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.webapplication.ContextParam;
import org.eclipse.jst.j2ee.webapplication.DispatcherType;
import org.eclipse.jst.j2ee.webapplication.Filter;
import org.eclipse.jst.j2ee.webapplication.FilterMapping;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.ui.dialogs.IOverwriteQuery;

public class JBossWebUtil implements IJBossWebUtil {

	public void configureContextParam(IProject project,
			IProgressMonitor monitor, String name, String value,
			String description) {
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null)
			return;
		IOverwriteQuery query = PortletCoreActivator.OVERWRITE_ALL_QUERY;

		// handle context-param settings
		// if contained this param
		List list = null;
		if (webApp.getVersionID() == 23) {
			// for servlet 2.3
			list = webApp.getContexts();
		} else {
			// for servlet 2.4
			list = webApp.getContextParams();
		}

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

		if (webApp.getVersionID() == 23) {
			// create context-param object
			ContextParam param = WebapplicationFactory.eINSTANCE
					.createContextParam();
			param.setParamName(name);
			param.setParamValue(value);
			if (description != null)
				param.setDescription(description);

			param.setWebApp(webApp);
		} else {
			// create ParamValue object for servlet 2.4
			ParamValue param = CommonFactory.eINSTANCE.createParamValue();
			param.setName(name);
			param.setValue(value);
			if (description != null) {
				Description descriptionObj = CommonFactory.eINSTANCE
						.createDescription();
				descriptionObj.setValue(description);
				param.getDescriptions().add(descriptionObj);
				param.setDescription(description);
			}

			// add into list
			webApp.getContextParams().add(param);
		}

	}

	private int getContextParamIndexByName(List list, String name) {
		if (list == null || name == null)
			return -1;
		Iterator it = list.iterator();
		int index = 0;
		while (it.hasNext()) {
			// get param object
			Object paramObj = it.next();
			// for servlet 2.3
			if (paramObj instanceof ContextParam) {
				ContextParam param = (ContextParam) paramObj;
				if (name.equals(param.getParamName()))
					return index;
			}
			// for servlet 2.4
			if (paramObj instanceof ParamValue) {
				ParamValue param = (ParamValue) paramObj;
				if (name.equals(param.getName()))
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

		// handle filter settings

		// if contained this filter
		Object obj = webApp.getFilterNamed(name);
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
		Filter filter = WebapplicationFactory.eINSTANCE.createFilter();
		filter.setName(name);
		filter.setFilterClassName(className);
		filter.setDescription(description);
		filter.setDisplayName(displayName);
		webApp.getFilters().add(filter);
	}

	public void configureFilterMapping(IProject project,
			IProgressMonitor monitor, String name, String servletName) {
		// FIXME check if filter mapping already exists
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null)
			return;
		IOverwriteQuery query = PortletCoreActivator.OVERWRITE_ALL_QUERY;

		// create FilterMapping object
		FilterMapping mapping = WebapplicationFactory.eINSTANCE
				.createFilterMapping();
		// get filter by name
		Filter filter = webApp.getFilterNamed(name);
		if (filter != null) {
			mapping.setFilter(filter);
			mapping.setServletName(servletName);
			EList dispatcherTypes = mapping.getDispatcherType();
			dispatcherTypes.add(DispatcherType.REQUEST_LITERAL);
			dispatcherTypes.add(DispatcherType.FORWARD_LITERAL);
			dispatcherTypes.add(DispatcherType.INCLUDE_LITERAL);
			// get Servlet object
			Servlet servlet = webApp.getServletNamed(servletName);
			mapping.setServlet(servlet);
			// if (uri != null || servlet != null)
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
				String name = filterMapping.getFilter().getName();
				String servletName = filterMapping.getServletName();
				String uri = filterMapping.getUrlPattern();
				String curKey = getFilterMappingString(name, servletName, uri);
				if (key.equals(curKey))
					return filterMapping;
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
			// TODO log
			return null;
		}
		return (WebApp) modelObject;
	}

	public String findJsfServlet(Object modelObject) {
		WebApp webApp = (WebApp) modelObject;
		Iterator it = webApp.getServlets().iterator();

		while (it.hasNext()) {
			Servlet servlet = (Servlet) it.next();
			JavaClass servletClass = servlet.getServletClass();
			if (servletClass != null
					&& servletClass.getInstanceClassName().trim().equals(
							JSF_SERVLET_CLASS)) {
				return servlet.getServletName();
			}
		}
		return null;
	}

	public String getFacesConfig(IProject project,
			IProgressMonitor monitor) {
		WebApp webApp = getWebApp(project, monitor);
		if (webApp == null) {
			return null;
		}
		EList contextParams = webApp.getContextParams();
		for (Iterator iterator = contextParams.iterator(); iterator.hasNext();) {
			Object paramObj = (Object) iterator.next();
			if (paramObj instanceof ContextParam) {
				ContextParam param = (ContextParam) paramObj;
				if (JAVAX_FACES_CONFIG_FILES.equals(param.getParamName()))
					return param.getParamValue();
			}
		}
		return WEB_INF_FACES_CONFIG_XML;
	}
}

package org.jboss.tools.birt.core.internal.project.facet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.util.DataUtil;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ContextParamBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.FilterBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.FilterMappingBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ListenerBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ServletBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ServletMappingBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.TagLibBean;
import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.jsp.JSPConfig;
import org.eclipse.jst.j2ee.jsp.TagLibRefType;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.webapplication.TagLibRef;
import org.eclipse.jst.javaee.core.Description;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.Listener;
import org.eclipse.jst.javaee.core.ParamValue;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.jsp.JspConfig;
import org.eclipse.jst.javaee.jsp.JspFactory;
import org.eclipse.jst.javaee.jsp.TagLib;
import org.eclipse.jst.javaee.web.Filter;
import org.eclipse.jst.javaee.web.FilterMapping;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.ui.dialogs.IOverwriteQuery;

public class JBossBirtUtil25 implements IBirtUtil {

	public void configureContextParam(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String name = DataUtil.getString(it.next(), false);
			ContextParamBean bean = (ContextParamBean) map.get(name);
			if (bean == null)
				continue;

			// if contained this param
			List list = webApp.getContextParams();

			int index = getContextParamIndexByName(list, name);
			if (index >= 0) {
				String ret = query
						.queryOverwrite("Context-param '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$

				// check overwrite query result
				if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
					continue;
				}
				if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
					monitor.setCanceled(true);
					return;
				}

				// remove old item
				list.remove(index);
			}

			String value = bean.getValue();
			String description = bean.getDescription();
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

	public void configureFilter(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;

		// handle filter settings
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String name = DataUtil.getString(it.next(), false);
			FilterBean bean = (FilterBean) map.get(name);
			if (bean == null)
				continue;
			// if contained this filter
			Object obj = getFilterByName(webApp, name);
			if (obj != null) {
				String ret = query.queryOverwrite("Filter '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				// check overwrite query result
				if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
					continue;
				}
				if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
					monitor.setCanceled(true);
					return;
				}
				// remove old item
				webApp.getFilters().remove(obj);
			}
			String className = bean.getClassName();
			String description = bean.getDescription();
			// create filter object
			Filter filter = WebFactory.eINSTANCE.createFilter();
			filter.setFilterName(name);
			filter.setFilterClass(className);
			Description descriptionObj = JavaeeFactory.eINSTANCE
					.createDescription();
			descriptionObj.setValue(description);
			webApp.getFilters().add(filter);
		}
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

	public void configureFilterMapping(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;

		// handle filter-mapping settings
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = DataUtil.getString(it.next(), false);
			FilterMappingBean bean = (FilterMappingBean) map.get(key);
			if (bean == null)
				continue;
			// if contained this filter-mapping
			Object obj = getFilterMappingByKey(webApp.getFilterMappings(), key);
			if (obj != null) {
				String ret = query
						.queryOverwrite("Filter-mapping '" + key + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				// check overwrite query result
				if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
					continue;
				}
				if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
					monitor.setCanceled(true);
					return;
				}
				// remove old item
				webApp.getFilterMappings().remove(obj);
			}
			// filter name
			String name = bean.getName();
			// create FilterMapping object
			FilterMapping mapping = WebFactory.eINSTANCE.createFilterMapping();
			// get filter by name
			Filter filter = (Filter) getFilterByName(webApp, name);
			if (filter != null) {
				mapping.setFilterName(filter.getFilterName());
				if (bean.getUri() != null) {
					UrlPatternType urlPattern = JavaeeFactory.eINSTANCE
							.createUrlPatternType();
					urlPattern.setValue(bean.getUri());
					mapping.getUrlPatterns().add(urlPattern);
				}
				mapping.getServletNames().add(bean.getServletName());

				// get Servlet object
				Servlet servlet = findServletByName(webApp, bean
						.getServletName());
				// mapping.setServlet(servlet);
				if (servlet != null || bean.getUri() != null)
					webApp.getFilterMappings().add(mapping);
			}
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

	public void configureListener(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;

		// handle listeners settings
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String name = DataUtil.getString(it.next(), false);
			ListenerBean bean = (ListenerBean) map.get(name);
			if (bean == null)
				continue;

			String className = bean.getClassName();
			String description = bean.getDescription();

			// if listener existed in web.xml, skip it
			Object obj = getListenerByClassName(webApp.getListeners(),
					className);
			if (obj != null)
				continue;

			// create Listener object
			Listener listener = JavaeeFactory.eINSTANCE.createListener();
			listener.setListenerClass(className);
			if (description != null) {
				Description descriptionObj = JavaeeFactory.eINSTANCE
						.createDescription();
				descriptionObj.setValue(description);
				listener.getDescriptions().add(descriptionObj);
			}

			webApp.getListeners().remove(listener);
			webApp.getListeners().add(listener);
		}
	}

	private Object getListenerByClassName(List list, String className) {
		if (list == null || className == null)
			return null;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Listener listener = (Listener) it.next();
			if (listener != null
					&& className.equals(listener.getListenerClass())) {
				return listener;
			}
		}
		return null;
	}

	public void configureServlet(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String name = DataUtil.getString(it.next(), false);
			ServletBean bean = (ServletBean) map.get(name);
			if (bean == null)
				continue;
			// if contained this servlet
			Object obj = findServletByName(webApp, name);
			// webapp.getServletNamed(name);
			if (obj != null) {
				String ret = query.queryOverwrite("Servlet '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				// check overwrite query result
				if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
					continue;
				}
				if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
					monitor.setCanceled(true);
					return;
				}
				// remove old item
				webApp.getServlets().remove(obj);
			}
			String className = bean.getClassName();
			String description = bean.getDescription();
			// create Servlet Type object
			Servlet servlet = WebFactory.eINSTANCE.createServlet();
			servlet.setServletName(name);
			servlet.setServletClass(className);
			// FIXME
			// servlet.setDescription(description);
			servlet.setLoadOnStartup(Integer.valueOf(1));
			// Add the servlet to the web application model
			webApp.getServlets().add(servlet);
			// FIXME
			// servlet.setWebApp(webapp);
		}
	}

	private WebApp getWebApp(Map map, IProject project, IProgressMonitor monitor) {
		if (monitor.isCanceled())
			return null;
		if (map == null || project == null) {
			return null;
		}
		IModelProvider modelProvider = ModelProviderManager
				.getModelProvider(project);
		Object modelObject = modelProvider.getModelObject();
		if (!(modelObject instanceof WebApp)) {
			// TODO log
			return null;
		}
		WebApp webApp = (WebApp) modelObject;
		return webApp;
	}

	public void configureServletMapping(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String uri = DataUtil.getString(it.next(), false);
			ServletMappingBean bean = (ServletMappingBean) map.get(uri);
			if (bean == null)
				continue;
			// if contained this servlet-mapping
			Object obj = getServletMappingByUri(webApp.getServletMappings(),
					uri);
			if (obj != null) {
				String ret = query
						.queryOverwrite("Servlet-mapping '" + uri + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				// check overwrite query result
				if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
					continue;
				}
				if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
					monitor.setCanceled(true);
					return;
				}
				// remove old item
				webApp.getServletMappings().remove(obj);
			}
			// servlet name
			String name = bean.getName();
			// create ServletMapping object
			ServletMapping mapping = WebFactory.eINSTANCE
					.createServletMapping();
			// get servlet by name
			Servlet servlet = findServletByName(webApp, name);
			if (servlet != null) {
				mapping.setServletName(servlet.getServletName());
				UrlPatternType urlPattern = JavaeeFactory.eINSTANCE
						.createUrlPatternType();
				urlPattern.setValue(uri);
				mapping.getUrlPatterns().add(urlPattern);
				webApp.getServletMappings().add(mapping);
				// mapping.setUrlPattern(uri);
				// mapping.setWebApp( webapp );
			}
		}
	}

	public void configureTaglib(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		// Feature 'taglib' not implemented
		return;
	}
	
	public void configureTaglibOrig(Map map, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		WebApp webApp = getWebApp(map, project, monitor);
		if (webApp == null)
			return;
		// handle taglib settings
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String uri = DataUtil.getString(it.next(), false);
			TagLibBean bean = (TagLibBean) map.get(uri);

			if (bean == null)
				continue;
			// if contained this taglib
			Object obj = getTagLibByUri(webApp, uri);
			if (obj != null) {
				String ret = query.queryOverwrite("Taglib '" + uri + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				// check overwrite query result
				if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
					continue;
				}
				if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
					monitor.setCanceled(true);
					return;
				}

				List jspConfigs = webApp.getJspConfigs();
				for (Iterator iterator = jspConfigs.iterator(); iterator
						.hasNext();) {
					JspConfig jspConfig = (JspConfig) iterator.next();
					jspConfig.getTagLibs().remove(obj);
				}

			}
			String location = bean.getLocation();
			JspConfig jspConfig = JspFactory.eINSTANCE.createJspConfig();
			TagLib ref = JspFactory.eINSTANCE.createTagLib();
			ref.setTaglibUri(uri);
			ref.setTaglibLocation(location);
			jspConfig.getTagLibs().add(ref);
			webApp.getJspConfigs().add(jspConfig);
		}
	}

	private Object getTagLibByUri(WebApp webapp, String uri) {
		if (webapp == null || uri == null)
			return null;
		List jspConfigs = webapp.getJspConfigs();
		for (Iterator iterator = jspConfigs.iterator(); iterator.hasNext();) {
			JspConfig jspConfig = (JspConfig) iterator.next();
			List list = jspConfig.getTagLibs();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				TagLib ref = (TagLib) obj;
				if (uri.equals(ref.getTaglibUri()))
					return ref;
			}
		}
		return null;
	}
	
	public void configureWebApp(WebAppBean webAppBean, IProject project,
			SimpleImportOverwriteQuery query, IProgressMonitor monitor) {
		if (monitor.isCanceled())
			return;
		if (webAppBean == null || project == null) {
			return;
		}
		IModelProvider modelProvider = ModelProviderManager
				.getModelProvider(project);
		Object modelObject = modelProvider.getModelObject();
		if (!(modelObject instanceof WebApp)) {
			// TODO log
			return;
		}
		WebApp webApp = (WebApp) modelObject;
		Description descriptionObj = JavaeeFactory.eINSTANCE
				.createDescription();
		descriptionObj.setValue(webAppBean.getDescription());
		webApp.getDescriptions().add(descriptionObj);
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

	private Object getServletMappingByUri(List list, String uri) {
		if (list == null || uri == null)
			return null;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get servlet-mapping object
			ServletMapping servletMapping = (ServletMapping) it.next();
			if (servletMapping == null)
				continue;
			List<UrlPatternType> urlPaterns = servletMapping.getUrlPatterns();
			for (UrlPatternType patern : urlPaterns)
				if (patern != null && uri.equals(patern.getValue())) {
					return servletMapping;
				}
		}
		return null;
	}
}

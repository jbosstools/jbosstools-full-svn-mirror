package org.jboss.tools.portlet.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PortletCoreActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.portlet.core";
	public static final String RESOURCES_FOLDER = "resources";
	public static final String JSFPORTLET_FOLDER = "jsfportlet";

	public static final IOverwriteQuery OVERWRITE_ALL_QUERY = new IOverwriteQuery() {
		public String queryOverwrite(String pathString) {
			return IOverwriteQuery.ALL;
		}
	};

	// The shared instance
	private static PortletCoreActivator plugin;

	/**
	 * The constructor
	 */
	public PortletCoreActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static PortletCoreActivator getDefault() {
		return plugin;
	}

	/**
	 * @param webProject
	 * @return IModelProvider
	 */
	public static IModelProvider getModelProvider(IProject webProject) {
		IModelProvider provider = ModelProviderManager
				.getModelProvider(webProject);
		Object webAppObj = provider.getModelObject();
		if (webAppObj == null) {
			return null;
		}
		return provider;
	}

	public static IStatus getStatus(String message) {
		return new Status(IStatus.ERROR, PLUGIN_ID, message);

	}

	public static void createPortletXml(String versionString, IProject project,
			IProgressMonitor monitor) {
		IPath portletXmlPath = ComponentCore.createComponent(project)
				.getRootFolder().getUnderlyingFolder().getRawLocation().append(
						new Path(IPortletConstants.CONFIG_PATH));
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		if (IPortletConstants.PORTLET_FACET_VERSION_20.equals(versionString)) {
			buffer
					.append("<portlet-app xmlns=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\"");
			buffer.append("\n\t");
			buffer
					.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
			buffer.append("\n\t");
			buffer
					.append("xsi:schemaLocation=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\" ");
			buffer.append("\n\t");
			buffer.append("version=\"2.0\">");
		} else {
			buffer
					.append("<portlet-app xmlns=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\"");
			buffer.append("\n\t");
			buffer
					.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			buffer.append("\n\t");
			buffer
					.append("xsi:schemaLocation=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\"");
			buffer.append("\n\t");
			buffer.append("version=\"1.0\">");
		}
		buffer.append("\n\n");
		buffer.append("</portlet-app>");
		buffer.append("\n");
		OutputStream outputStream = null;
		try {
			IPath directory = portletXmlPath.removeLastSegments(1);
			directory.toFile().mkdirs();
			File file = portletXmlPath.toFile();
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			PrintWriter printWriter = new PrintWriter(outputStream);
			printWriter.write(buffer.toString());
			printWriter.close();
		} catch (Exception e) {
			log(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public static void log(Exception e, String message) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, message, e);
		PortletCoreActivator.getDefault().getLog().log(status);
	}

	public static void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, e
				.getLocalizedMessage(), e);
		PortletCoreActivator.getDefault().getLog().log(status);
	}

	public static void createPortletInstances(IProject project, IFile file)
			throws CoreException, UnsupportedEncodingException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<!DOCTYPE deployments PUBLIC \n");
		buffer.append("\"-//JBoss Portal//DTD Portlet Instances 2.6//EN\"\n");
		buffer
				.append("\"http://www.jboss.org/portal/dtd/portlet-instances_2_6.dtd\">");
		buffer.append("<deployments>");
		buffer.append("</deployments>");

		ByteArrayInputStream source = new ByteArrayInputStream(buffer
				.toString().getBytes("UTF8"));
		file.create(source, true, new NullProgressMonitor());
	}

	public static void createPortletObject(IProject project, IFile file)
			throws CoreException, UnsupportedEncodingException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<!DOCTYPE deployments PUBLIC \n");
		buffer.append("\"-//JBoss Portal//DTD Portal Object 2.6//EN\"\n");
		buffer
				.append("\"http://www.jboss.org/portal/dtd/portal-object_2_6.dtd\">");
		buffer.append("<deployments>");
		buffer.append("</deployments>");

		ByteArrayInputStream source = new ByteArrayInputStream(buffer
				.toString().getBytes("UTF8"));
		file.create(source, true, new NullProgressMonitor());
	}

	public static void createJBossApp(IProject project, IFile file)
			throws CoreException, UnsupportedEncodingException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<!DOCTYPE jboss-app PUBLIC \n");
		buffer.append("\"-//JBoss Portal//DTD JBoss Web Application 2.6//EN\"\n");
		buffer.append("\"http://www.jboss.org/portal/dtd/jboss-app_2_6.dtd\">");
		buffer.append("<jboss-app>");
		buffer.append("</jboss-app>");

		ByteArrayInputStream source = new ByteArrayInputStream(buffer
				.toString().getBytes("UTF8"));
		file.create(source, true, new NullProgressMonitor());
	}
	
	public static void createJBossPortlet(IProject project, IFile file)
			throws CoreException, UnsupportedEncodingException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<!DOCTYPE portlet-app PUBLIC \n");
		buffer
				.append("\"-//JBoss Portal//DTD JBoss Portlet 2.6//EN\"\n");
		buffer.append("\"http://www.jboss.org/portal/dtd/jboss-portlet_2_6.dtd\">");
		buffer.append("<portlet-app>");
		buffer.append("</portlet-app>");

		ByteArrayInputStream source = new ByteArrayInputStream(buffer
				.toString().getBytes("UTF8"));
		file.create(source, true, new NullProgressMonitor());
	}
	
}

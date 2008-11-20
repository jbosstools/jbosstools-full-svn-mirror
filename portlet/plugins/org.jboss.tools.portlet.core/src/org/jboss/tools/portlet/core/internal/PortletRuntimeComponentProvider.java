package org.jboss.tools.portlet.core.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntimeComponent;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntimeComponentType;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntimeComponentVersion;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.internal.facets.RuntimeFacetComponentProviderDelegate;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public class PortletRuntimeComponentProvider extends
		RuntimeFacetComponentProviderDelegate {

	private static final String SERVER_DEFAULT_DEPLOY_JBOSS_PORTAL_SAR = "server/default/deploy/jboss-portal.sar"; //$NON-NLS-1$

	private static final String SERVER_DEFAULT_DEPLOY_SIMPLE_PORTAL = "server/default/deploy/simple-portal"; //$NON-NLS-1$

	private static final String LIB = "lib"; //$NON-NLS-1$

	private static final String JAR = ".jar"; //$NON-NLS-1$

	private static final String PORTLET_API = "portlet-api"; //$NON-NLS-1$

	private static final IRuntimeComponentType PORTAL_TYPE = RuntimeManager
			.getRuntimeComponentType("org.jboss.tools.portlet.core.runtime.component"); //$NON-NLS-1$

	private static final IRuntimeComponentVersion PORTAL_VERSION_1 = PORTAL_TYPE
			.getVersion("1.0"); //$NON-NLS-1$

	public List<IRuntimeComponent> getRuntimeComponents(final IRuntime runtime) {
		final File location = runtime.getLocation().toFile();
		final List<IRuntimeComponent> components = new ArrayList<IRuntimeComponent>();
		if (isPortalPresent(location)) {
			final IRuntimeComponent portalComponent = RuntimeManager
					.createRuntimeComponent(PORTAL_VERSION_1, null);
			components.add(portalComponent);
		}
		return components;
	}

	
	private static boolean isPortalPresent(final File location) {
		boolean check = PortletCoreActivator.getDefault().getPluginPreferences().getBoolean(PortletCoreActivator.CHECK_RUNTIMES);
		if (!check) {
			return true;
		}
		// JBoss Portal server
		if (exists(location, SERVER_DEFAULT_DEPLOY_JBOSS_PORTAL_SAR)) {
			return true;
		}
		// JBoss portletcontainer
		if (exists(location,SERVER_DEFAULT_DEPLOY_SIMPLE_PORTAL)) {
			return true;
		}
		// Tomcat portletcontainer
		File tomcatLib = new File(location,LIB);
		if (tomcatLib.exists() && tomcatLib.isDirectory()) {
			String[] files = tomcatLib.list(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					if (name.startsWith(PORTLET_API) && name.endsWith(JAR)) {
						return true;
					}
					return false;
				}
				
			});
			return files.length > 0;
		}
		
		return false; 
	}


	private static boolean exists(final File location,String portalDir) {
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			portalDir = portalDir.replace("/", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		File file = new File(location,portalDir);
		return file.exists();
	}
}

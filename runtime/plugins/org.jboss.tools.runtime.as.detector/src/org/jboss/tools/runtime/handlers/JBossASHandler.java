/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.runtime.handlers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.ConnectionProfileConstants;
import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.db.generic.IDBConnectionProfileConstants;
import org.eclipse.datatools.connectivity.db.generic.IDBDriverDefinitionConstants;
import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.eclipse.datatools.connectivity.drivers.IDriverMgmtConstants;
import org.eclipse.datatools.connectivity.drivers.IPropertySet;
import org.eclipse.datatools.connectivity.drivers.PropertySetImpl;
import org.eclipse.datatools.connectivity.drivers.models.TemplateDescriptor;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.jboss.ide.eclipse.as.core.publishers.LocalPublishMethod;
import org.jboss.ide.eclipse.as.core.server.bean.JBossServerType;
import org.jboss.ide.eclipse.as.core.server.bean.ServerBean;
import org.jboss.ide.eclipse.as.core.server.bean.ServerBeanLoader;
import org.jboss.ide.eclipse.as.core.util.ServerCreationUtils;
import org.jboss.tools.runtime.as.detector.IJBossRuntimePluginConstants;
import org.jboss.tools.runtime.as.detector.Messages;
import org.jboss.tools.runtime.as.detector.RuntimeAsActivator;
import org.jboss.tools.runtime.core.JBossRuntimeLocator;
import org.jboss.tools.runtime.core.RuntimeCoreActivator;
import org.jboss.tools.runtime.core.model.AbstractRuntimeDetector;
import org.jboss.tools.runtime.core.model.IRuntimeDetector;
import org.jboss.tools.runtime.core.model.ServerDefinition;
import org.osgi.framework.Bundle;

public class JBossASHandler extends AbstractRuntimeDetector implements IJBossRuntimePluginConstants {
	
	private static final int JBOSS_AS70_INDEX = 8;
	private static final int JBOSS_AS71_INDEX = 9;
	private static final int JBOSS_EAP60_INDEX = 10;
	private static String[] hasIncludedRuntimes = new String[] {SOA_P, EAP, EPP, EWP, SOA_P_STD};
	private static final String DROOLS = "DROOLS"; // NON-NLS-1$
	private static final String ESB = "ESB"; //$NON-NLS-1$

	public void initializeRuntimes(List<ServerDefinition> serverDefinitions) {
		createJBossServerFromDefinitions(serverDefinitions);
	}
		
	private static File getLocation(ServerDefinition serverDefinition) {
		String type = serverDefinition.getType();
		String version = serverDefinition.getVersion();
		if (EAP.equals(type) && version != null && version.startsWith("6") ) {
			return serverDefinition.getLocation();
		}
		if (SOA_P.equals(type) || EAP.equals(type) || EPP.equals(type)) {
			return new File(serverDefinition.getLocation(), "jboss-as");
		}
		if (SOA_P_STD.equals(type)) {
			return new File(serverDefinition.getLocation(),"jboss-esb"); //$NON-NLS-1$					
		}
		if(EWP.equals(type)) {
				return new File(serverDefinition.getLocation(),"jboss-as-web"); //$NON-NLS-1$
		}
		if (AS.equals(type) || EAP_STD.equals(type)) {
			return serverDefinition.getLocation();
		}
		return null;
	}
	
	public static void createJBossServerFromDefinitions(List<ServerDefinition> serverDefinitions) {
		for (ServerDefinition serverDefinition:serverDefinitions) {
			if (!serverDefinition.isEnabled()) {
				continue;
			}
			File asLocation = getLocation(serverDefinition);
			if (asLocation == null || !asLocation.isDirectory()) {
				continue;
			}
			String type = serverDefinition.getType();
			if (SOA_P.equals(type) || EAP.equals(type) || EPP.equals(type)
					|| SOA_P_STD.equals(type) || EWP.equals(type)
					|| EAP_STD.equals(type)) {
				String name = serverDefinition.getName();
				String runtimeName = name + " " + RUNTIME; //$NON-NLS-1$
				int index = getJBossASVersion(asLocation, serverDefinition);
				createJBossServer(asLocation, index, name, runtimeName);
			} else if (AS.equals(type)){
				String version = serverDefinition.getVersion();
				int index = 2;
				if ("3.2".equals(version)) { //$NON-NLS-1$
					index = 0;
				} else if ("4.0".equals(version)) { //$NON-NLS-1$
					index = 1;
				} else if ("4.2".equals(version)) { //$NON-NLS-1$
					index = 2;
				} else if ("5.0".equals(version)) { //$NON-NLS-1$
					index = 3;
				} else if ("5.1".equals(version)) { //$NON-NLS-1$
					index = 4;
				} else if ("6.0".equals(version) || "6.1".equals(version)) { //$NON-NLS-1$
					index = 5;
				} else if ("7.0".equals(version)) { //$NON-NLS-1$
					index = JBOSS_AS70_INDEX;
				} else if ("7.1".equals(version)) { //$NON-NLS-1$
					index = JBOSS_AS71_INDEX;
				}
				// NEW_SERVER_ADAPTER add logic for new adapter here
				createJBossServer(serverDefinition.getLocation(),index,serverDefinition.getName(),serverDefinition.getName() + " " + RUNTIME); //$NON-NLS-1$
			}
			createJBossServerFromDefinitions(serverDefinition.getIncludedServerDefinitions());
		}	
	}

	private static int getJBossASVersion(File asLocation, ServerDefinition serverDefinition) {
		int index = -1;
		String type = serverDefinition.getType();
		String ver = serverDefinition.getVersion();
		String fullVersion;
		if (EAP.equals(type) && "6.0".equals(ver)) {
			fullVersion = new ServerBeanLoader(asLocation).getFullServerVersion();
		} else {
			fullVersion = new ServerBeanLoader(asLocation).getFullServerVersion();
		}
		if(fullVersion != null ) {
			String version = fullVersion.substring(0, 3);
			if ("4.3".equals(version)) { //$NON-NLS-1$
				index = 6;
			} else if ("5.0".equals(version)) { //$NON-NLS-1$
				index = 7;
			} else if ("5.1".equals(version)) { //$NON-NLS-1$
				// FIXME - this needs to be changed when adding a new runtime type for JBoss EAP 5.1
				index = 7;
			} else if ("5.2".equals(version)) { //$NON-NLS-1$
				// SOA-P 5.2
				index = 7;
			} else if ("6.0".equals(version)) { //$NON-NLS-1$
				// EAP 6.0
				index = 10;
			}
		}
		return index;
	}

	private static void createJBossServer(File asLocation, int index, String name, String runtimeName) {
		if (asLocation == null || !asLocation.isDirectory() || index==-1) {
			return;
		}
		IPath jbossAsLocationPath = new Path(asLocation.getAbsolutePath());

		IServer[] servers = ServerCore.getServers();
		for (int i = 0; i < servers.length; i++) {
			IRuntime runtime = servers[i].getRuntime();
			if(runtime != null && runtime.getLocation() != null && runtime.getLocation().equals(jbossAsLocationPath)) {
				return;
			}
		}

		IRuntime runtime = null;
		IRuntime[] runtimes = ServerCore.getRuntimes();
		for (int i = 0; i < runtimes.length; i++) {
			if (runtimes[i] == null || runtimes[i].getLocation() == null) {
				continue;
			}
			if (runtimes[i].getLocation().equals(jbossAsLocationPath)) {
				runtime = runtimes[i].createWorkingCopy();
				break;
			}
		}

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		try {
			if (runtime == null) {
				runtime = createRuntime(runtimeName, asLocation.getAbsolutePath(), progressMonitor, index);
			}
			if (runtime != null) {
				createServer(progressMonitor, runtime, index, name);
			}

			createDriver(asLocation.getAbsolutePath(), index);
		} catch (CoreException e) {
			RuntimeAsActivator.log(e,Messages.JBossRuntimeStartup_Cannot_create_new_JBoss_Server);
		} catch (ConnectionProfileException e) {
			RuntimeAsActivator.log(e,Messages.JBossRuntimeStartup_Cannott_create_new_DTP_Connection_Profile);
		}
	}

	/**
	 * Creates new JBoss AS Runtime
	 * @param jbossASLocation location of JBoss AS
	 * @param progressMonitor
	 * @return runtime working copy
	 * @throws CoreException
	 */
	private static IRuntime createRuntime(String runtimeName, String jbossASLocation, IProgressMonitor progressMonitor, int index) throws CoreException {
		IRuntimeWorkingCopy runtime = null;
		String type = null;
		String version = null;
		String runtimeId = null;
		IPath jbossAsLocationPath = new Path(jbossASLocation);
		IRuntimeType[] runtimeTypes = ServerUtil.getRuntimeTypes(type, version, JBOSS_AS_RUNTIME_TYPE_ID[index]);
		if (runtimeTypes.length > 0) {
			runtime = runtimeTypes[0].createRuntime(runtimeId, progressMonitor);
			runtime.setLocation(jbossAsLocationPath);
			if(runtimeName!=null) {
				runtime.setName(runtimeName);				
			}
			return runtime.save(false, progressMonitor);
		}
		return runtime;
	}

	/**
	 * Creates new JBoss Server
	 * @param progressMonitor
	 * @param runtime parent JBoss AS Runtime
	 * @return server working copy
	 * @throws CoreException
	 */
	private static void createServer(IProgressMonitor progressMonitor, IRuntime runtime, int index, String name) throws CoreException {
		if (name == null) {
			name = JBOSS_AS_NAME[index];
		}
		IServer[] servers = ServerCore.getServers();
		for (IServer server:servers) {
			if (name.equals(server.getName()) ) {
				return;
			}
		}
		IServerType serverType = ServerCore.findServerType(JBOSS_AS_TYPE_ID[index]);
		ServerCreationUtils.createServer2(runtime, serverType, name, LocalPublishMethod.LOCAL_PUBLISH_METHOD);
	}

	/**
	 * Creates HSQL DB Driver
	 * @param jbossASLocation location of JBoss AS
	 * @param index 
	 * @throws ConnectionProfileException
	 * @return driver instance
	 */
	private static void createDriver(String jbossASLocation, int index) throws ConnectionProfileException {
		if(ProfileManager.getInstance().getProfileByName(DEFAULT_DS) != null) {
			// Don't create the driver a few times
			return;
		}
		if (index == JBOSS_AS70_INDEX || index == JBOSS_AS71_INDEX || index == JBOSS_EAP60_INDEX) {
			// AS 7
			return;
		}
		String driverPath;
		try {
			driverPath = new File(jbossASLocation + JBOSS_AS_HSQL_DRIVER_LOCATION[index]).getCanonicalPath(); //$NON-NLS-1$
		} catch (IOException e) {
			RuntimeAsActivator.getDefault().getLog().log(new Status(IStatus.ERROR,
					RuntimeAsActivator.PLUGIN_ID, Messages.JBossRuntimeStartup_Cannott_create_new_HSQL_DB_Driver, e));
			return;
		}

		DriverInstance driver = DriverManager.getInstance().getDriverInstanceByName(HSQL_DRIVER_NAME);
		if (driver == null) {
			TemplateDescriptor descr = TemplateDescriptor.getDriverTemplateDescriptor(HSQL_DRIVER_TEMPLATE_ID);
			IPropertySet instance = new PropertySetImpl(HSQL_DRIVER_NAME, HSQL_DRIVER_DEFINITION_ID);
			instance.setName(HSQL_DRIVER_NAME);
			instance.setID(HSQL_DRIVER_DEFINITION_ID);
			Properties props = new Properties();

			IConfigurationElement[] template = descr.getProperties();
			for (int i = 0; i < template.length; i++) {
				IConfigurationElement prop = template[i];
				String id = prop.getAttribute("id"); //$NON-NLS-1$

				String value = prop.getAttribute("value"); //$NON-NLS-1$
				props.setProperty(id, value == null ? "" : value); //$NON-NLS-1$
			}
			props.setProperty(DTP_DB_URL_PROPERTY_ID, "jdbc:hsqldb:."); //$NON-NLS-1$
			props.setProperty(IDriverMgmtConstants.PROP_DEFN_TYPE, descr.getId());
			props.setProperty(IDriverMgmtConstants.PROP_DEFN_JARLIST, driverPath);

			instance.setBaseProperties(props);
			DriverManager.getInstance().removeDriverInstance(instance.getID());
			System.gc();
			DriverManager.getInstance().addDriverInstance(instance);
		}

		driver = DriverManager.getInstance().getDriverInstanceByName(HSQL_DRIVER_NAME);
		if (driver != null && ProfileManager.getInstance().getProfileByName(DEFAULT_DS) == null) { //$NON-NLS-1$
			// create profile
			Properties props = new Properties();
			props.setProperty(ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID, HSQL_DRIVER_DEFINITION_ID);
			props.setProperty(IDBConnectionProfileConstants.CONNECTION_PROPERTIES_PROP_ID, ""); //$NON-NLS-1$
			props.setProperty(IDBDriverDefinitionConstants.DRIVER_CLASS_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.DRIVER_CLASS_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID,	driver.getProperty(IDBDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_VERSION_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.DATABASE_VERSION_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_NAME_PROP_ID, "Default"); //$NON-NLS-1$
			props.setProperty(IDBDriverDefinitionConstants.PASSWORD_PROP_ID, ""); //$NON-NLS-1$
			props.setProperty(IDBConnectionProfileConstants.SAVE_PASSWORD_PROP_ID, "false"); //$NON-NLS-1$
			props.setProperty(IDBDriverDefinitionConstants.USERNAME_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.USERNAME_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.URL_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.URL_PROP_ID));

			ProfileManager.getInstance().createProfile(DEFAULT_DS,	Messages.JBossRuntimeStartup_The_JBoss_AS_Hypersonic_embedded_database, HSQL_PROFILE_ID, props, "", false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
	}

	public ServerDefinition getServerDefinition(File root,
			IProgressMonitor monitor) {
		if (monitor.isCanceled() || root == null || !isEnabled()) {
			return null;
		}
		ServerBeanLoader loader = new ServerBeanLoader(root);
		ServerBean serverBean = loader.getServerBean();
		
		if (!JBossServerType.UNKNOWN.equals(serverBean.getType())) {
			ServerDefinition serverDefinition = new ServerDefinition(serverBean.getName(), 
					serverBean.getVersion(), serverBean.getType().getId(), new File(serverBean.getLocation()));
			calculateIncludedServerDefinition(serverDefinition, monitor);
			return serverDefinition;
		}
		return null;
	}
	
	private void calculateIncludedServerDefinition(
			ServerDefinition serverDefinition, IProgressMonitor monitor) {
		if (serverDefinition == null || serverDefinition.getType() == null) {
			return;
		}
		String type = serverDefinition.getType();
		if (!hasIncludedRuntimes(type)) {
			return;
		}
		serverDefinition.getIncludedServerDefinitions().clear();
		List<ServerDefinition> serverDefinitions = serverDefinition
				.getIncludedServerDefinitions();
		JBossRuntimeLocator locator = new JBossRuntimeLocator();
		final File location = getLocation(serverDefinition);
		File[] directories = serverDefinition.getLocation().listFiles(
				new FileFilter() {

					@Override
					public boolean accept(File file) {
						if (!file.isDirectory() || file.equals(location)) {
							return false;
						}
						return true;
					}
				});
		boolean saved = isEnabled();
		try {
			setEnabled(false);
			for (File directory : directories) {
				List<ServerDefinition> definitions = new ArrayList<ServerDefinition>();
				locator.searchDirectory(directory, definitions, 1, monitor);
				for (ServerDefinition definition:definitions) {
					definition.setParent(serverDefinition);
				}
				serverDefinitions.addAll(definitions);
			}
			if (SOA_P.equals(type) || SOA_P_STD.equals(type)) {
				addDrools(serverDefinition);
				addEsb(serverDefinition);
			}
		} finally {
			setEnabled(saved);
		}
	}

	private void addDrools(ServerDefinition serverDefinition) {
		if (serverDefinition == null) {
			return;
		}
		Bundle drools = Platform.getBundle("org.drools.eclipse");
		Bundle droolsDetector = Platform
				.getBundle("org.jboss.tools.runtime.drools.detector");
		if (drools != null && droolsDetector != null) {
			File droolsRoot = serverDefinition.getLocation();
			if (droolsRoot.isDirectory()) {
				String name = "Drools - " + serverDefinition.getName();
				ServerDefinition droolsDefinition = new ServerDefinition(
						name, serverDefinition.getVersion(), DROOLS,
						droolsRoot);
				droolsDefinition.setParent(serverDefinition);
				serverDefinition.getIncludedServerDefinitions().add(
						droolsDefinition);
			}
		}
	}
	
	private void addEsb(ServerDefinition serverDefinition) {
		if (serverDefinition == null) {
			return;
		}
		Bundle esb = Platform.getBundle("org.jboss.tools.esb.project.core");
		Bundle esbDetectorPlugin = Platform
				.getBundle("org.jboss.tools.runtime.esb.detector");
		if (esb != null && esbDetectorPlugin != null) {
			String type = serverDefinition.getType();
			File esbRoot;
			if (SOA_P.equals(type)) {
				esbRoot = serverDefinition.getLocation();
			} else {
				esbRoot = new File(serverDefinition.getLocation(), "jboss-esb"); //$NON-NLS-1$
			}
			if (esbRoot.isDirectory()) {
				String name = "ESB - " + serverDefinition.getName();
				String version="";
				ServerDefinition esbDefinition = new ServerDefinition(
						name, version, ESB,
						esbRoot);
				IRuntimeDetector esbDetector = RuntimeCoreActivator.getEsbDetector();
				if (esbDetector != null) {
					version = esbDetector.getVersion(esbDefinition);
					esbDefinition.setVersion(version);
				}
				
				esbDefinition.setParent(serverDefinition);
				serverDefinition.getIncludedServerDefinitions().add(
						esbDefinition);
			}
		}
	}

	private boolean hasIncludedRuntimes(String type) {
		for (String t:hasIncludedRuntimes) {
			if (t.equals(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean exists(ServerDefinition serverDefinition) {
		if (serverDefinition == null || serverDefinition.getLocation() == null) {
			return false;
		}
		File location = getLocation(serverDefinition);
		if (location == null || !location.isDirectory()) {
			return false;
		}
		String path = location.getAbsolutePath();
		if (path == null) {
			return false;
		}
		IServer[] servers = ServerCore.getServers();
		for (int i = 0; i < servers.length; i++) {
			IRuntime runtime = servers[i].getRuntime();
			if (runtime == null || runtime.getLocation() == null) {
				continue;
			}
			if(path.equals(runtime.getLocation().toOSString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void computeIncludedServerDefinition(
			ServerDefinition serverDefinition) {
		if (serverDefinition == null) {
			return;
		}
		String type = serverDefinition.getType();
		if (AS.equals(type)) {
			return;
		}
		calculateIncludedServerDefinition(serverDefinition, new NullProgressMonitor());
	}
	
}

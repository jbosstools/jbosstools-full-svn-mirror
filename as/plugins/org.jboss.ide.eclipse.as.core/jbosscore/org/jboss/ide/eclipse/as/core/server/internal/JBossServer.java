/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.core.server.internal;

import static org.jboss.ide.eclipse.as.core.util.IJBossRuntimeResourceConstants.DEPLOY;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JBOSSTOOLS_TMP;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JBOSS_WEB_DEFAULT_PORT;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JNDI_DEFAULT_PORT;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JNDI_PORT;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JNDI_PORT_DEFAULT_XPATH;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JNDI_PORT_DETECT;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.JNDI_PORT_DETECT_XPATH;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.SERVER_AS_50;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.SERVER_PASSWORD;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.SERVER_USERNAME;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.TEMP_DEPLOY;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.TMP;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.WEB_PORT;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.WEB_PORT_DEFAULT_XPATH;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.WEB_PORT_DETECT;
import static org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants.WEB_PORT_DETECT_XPATH;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IURLProvider;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.extensions.descriptors.XPathModel;
import org.jboss.ide.eclipse.as.core.extensions.descriptors.XPathQuery;
import org.jboss.ide.eclipse.as.core.publishers.LocalPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IDeployableServer;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.ide.eclipse.as.core.util.DeploymentPreferenceLoader;
import org.jboss.ide.eclipse.as.core.util.ExpressionResolverUtil;
import org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants;
import org.jboss.ide.eclipse.as.core.util.RuntimeUtils;
import org.jboss.ide.eclipse.as.core.util.ServerUtil;

/**
 * 
 * @author Rob Stryker rob.stryker@jboss.com
 *
 */
public class JBossServer extends DeployableServer 
		implements IDeployableServer, IURLProvider {
	
	public static final String AUTOMATICALLY_UPDATE_LAUNCH = "org.jboss.ide.eclipse.as.core.server.internal.AUTOMATICALLY_UPDATE_LAUNCH";  //$NON-NLS-1$
	
	public void setDefaults(IProgressMonitor monitor) {
		super.setDefaults(monitor);
		setAttribute("auto-publish-time", 1); //$NON-NLS-1$
		setAttribute("id", getAttribute("id", (String)"") + new Date().getTime()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setUsername("admin"); //$NON-NLS-1$
		boolean defaultServerDeployment = isAS50() || isEAP(getServer());
		setDeployLocationType(defaultServerDeployment ? IDeployableServer.DEPLOY_SERVER : IDeployableServer.DEPLOY_METADATA);
	}
	
	public static boolean isEAP(IServer server) {
		return server.getServerType().getId().startsWith(IJBossToolingConstants.EAP_SERVER_PREFIX);
	}

	private boolean isAS50() {
		return getServer().getServerType().getRuntimeType().getId().equals(SERVER_AS_50);
	}
	
	public String getHost() {
		return getServer().getHost();
	}
		
	/**
	 * The full path of the configuration, ex:
	 *  /home/rob/tmp/default_copy3 would return /home/rob/tmp/default_copy3 
	 *  /home/rob/jboss-5.x.x/server/default would return /home/rob/jboss-5.x.x/server/default
	 * @return
	 */
	public String getConfigDirectory() {
		IJBossServerRuntime runtime = (IJBossServerRuntime)getServer().getRuntime().loadAdapter(IJBossServerRuntime.class, null);
		return runtime.getConfigurationFullPath().toOSString();
	}
	
	public String getDeployFolder() {
		return getDeployFolder(getDeployLocationType());
	}
	
	public String getDeployFolder(String type) {
		return getDeployFolder(this, type);
	}
	
	/**
	 * Legacy method used by some (such as UI) who want to 
	 * deeply introspect the value for specific deployment location
	 * constants. 
	 * 
	 * @param jbs
	 * @param type
	 * @return
	 */
	public static String getDeployFolder(JBossServer jbs, String type) {
		IServer server = jbs.getServer();
		IJBossServerRuntime jbsrt = RuntimeUtils.getJBossServerRuntime(server);
		if( type.equals(DEPLOY_CUSTOM)) {
			String val = jbs.getAttribute(DEPLOY_DIRECTORY, (String)null);
			if( val != null ) {
				IPath val2 = new Path(val);
				return ServerUtil.makeGlobal(jbsrt.getRuntime(), val2).toString();
			}
			// if no value is set, default to metadata
			type = DEPLOY_METADATA;
		}
		if( type.equals(DEPLOY_METADATA)) {
			return JBossServerCorePlugin.getServerStateLocation(server).
				append(DEPLOY).makeAbsolute().toString();
		} else if( type.equals(DEPLOY_SERVER)) {
			String loc = jbsrt.getConfigLocation();
			String config = jbsrt.getJBossConfiguration();
			IPath p = new Path(loc).append(config)
				.append(DEPLOY);
			return ServerUtil.makeGlobal(jbsrt.getRuntime(), p).toString();
		}
		return null;
	}
	
	public String getTempDeployFolder() {
		return getTempDeployFolder(this, getDeployLocationType());
	}
	
	/**
	 * Legacy method used by some (such as UI) who want to 
	 * deeply introspect the value for specific deployment location
	 * constants. 
	 * 
	 * @param jbs
	 * @param type
	 * @return
	 */
	public static String getTempDeployFolder(JBossServer jbs, String type) {
		IServer server = jbs.getServer();
		IJBossServerRuntime jbsrt =RuntimeUtils.getJBossServerRuntime(server);
		if( type.equals(DEPLOY_CUSTOM))
			return ServerUtil.makeGlobal(jbsrt.getRuntime(), 
					new Path(server.getAttribute(TEMP_DEPLOY_DIRECTORY, ""))).toString(); //$NON-NLS-1$
		if( type.equals(DEPLOY_METADATA)) {
			return JBossServerCorePlugin.getServerStateLocation(server).
				append(TEMP_DEPLOY).makeAbsolute().toString();
		} else if( type.equals(DEPLOY_SERVER)) {
			String loc = jbsrt.getConfigLocation();
			String config = jbsrt.getJBossConfiguration();
			IPath p = new Path(loc)
				.append(config).append(TMP)
				.append(JBOSSTOOLS_TMP);
			return ServerUtil.makeGlobal(jbsrt.getRuntime(), p).toString();
		}
		return null;
	}
	
	public int getJNDIPort() {
		return findPort(JNDI_PORT, JNDI_PORT_DETECT, JNDI_PORT_DETECT_XPATH, 
				JNDI_PORT_DEFAULT_XPATH, JNDI_DEFAULT_PORT);
	}
	
	public int getJBossWebPort() {
		return findPort(WEB_PORT, WEB_PORT_DETECT, WEB_PORT_DETECT_XPATH, 
				WEB_PORT_DEFAULT_XPATH, JBOSS_WEB_DEFAULT_PORT);
	}

	protected int findPort(String attributeKey, String detectKey, String xpathKey, String defaultXPath, int defaultValue) {
		boolean detect = getAttribute(detectKey, true);
		String result = null;
		if( !detect ) {
			result = getAttribute(attributeKey, (String)null);
		} else {
			String xpath = getAttribute(xpathKey, defaultXPath);
			XPathQuery query = XPathModel.getDefault().getQuery(getServer(), new Path(xpath));
			if(query!=null) {
				result = query.getFirstResult();
			}
		}
		
		if( result != null ) {
			result = resolveXPathResult(result);

			try {
				return Integer.parseInt(result);
			} catch(NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}
	
	protected String resolveXPathResult(String result) {
		return ExpressionResolverUtil.safeReplaceProperties(result);
	}
	
	public URL getModuleRootURL(IModule module) {
		return getModuleRootURL(module, getHost(), getJBossWebPort());
	}
	
	public static URL getModuleRootURL(IModule module, String host, int port) {
		return getModuleRootURL(module, host, port, false);
	}
	
	public static URL getModuleRootURL(IModule module, String host, int port, boolean ignoreContextRoot) {
        if (module == null || module.loadAdapter(IWebModule.class,null)==null )
			return null;
        
        IWebModule webModule =(IWebModule)module.loadAdapter(IWebModule.class,null);
		String url = host;
		if( !url.startsWith("http://") && !url.startsWith("https://") ) { //$NON-NLS-1$ //$NON-NLS-2$
			url = "http://"+host; //$NON-NLS-1$
		}
		if (port != 80)
			url += ":" + port; //$NON-NLS-1$

		if( !ignoreContextRoot ) {
			String cxRoot = webModule.getContextRoot();
			if( !cxRoot.equals("/") && !cxRoot.equals("./")) //$NON-NLS-1$ //$NON-NLS-2$
				url += "/"+webModule.getContextRoot(); //$NON-NLS-1$
		}
		if (!url.endsWith("/")) //$NON-NLS-1$
			url += "/"; //$NON-NLS-1$

		try {
			return new URL(url);
		} catch( MalformedURLException murle) { return null; }
	}
	
	
	// first class parameters
	public String getUsername() {
		return getAttribute(SERVER_USERNAME, "admin"); //$NON-NLS-1$
	}
	public void setUsername(String name) {
		setAttribute(SERVER_USERNAME, name);
	}

	public String getPassword() {
		String s = ServerUtil.getFromSecureStorage(getServer(), SERVER_PASSWORD);
		if( s == null )
			return getAttribute(SERVER_PASSWORD, "admin"); //$NON-NLS-1$
		return s;
	}
	public void setPassword(String pass) {
		ServerUtil.storeInSecureStorage(getServer(), SERVER_PASSWORD, pass);
	}
	
	
	public boolean hasJMXProvider() {
		DeployableServerBehavior beh = (DeployableServerBehavior)getServer().loadAdapter(
				DeployableServerBehavior.class, new NullProgressMonitor());
		if( beh == null )
			return false;
		String type = DeploymentPreferenceLoader.getCurrentDeploymentMethodTypeId(getServer(), LocalPublishMethod.LOCAL_PUBLISH_METHOD);
		if( type.equals(LocalPublishMethod.LOCAL_PUBLISH_METHOD)) {
			return true;
		}
		return false;
	}

}
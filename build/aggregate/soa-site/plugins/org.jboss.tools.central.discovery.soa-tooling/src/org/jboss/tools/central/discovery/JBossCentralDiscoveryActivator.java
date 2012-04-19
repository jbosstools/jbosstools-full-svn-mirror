/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.central.discovery;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import org.jboss.tools.central.configurators.DefaultJBossCentralDiscoveryConfigurator;
import org.jboss.tools.central.configurators.IJBossCentralDiscoveryConfigurator;

/**
 * The activator class controls the plug-in life cycle
 */
public class JBossCentralDiscoveryActivator extends AbstractUIPlugin {

	public static final String JBOSS_SOA_TOOLING = "jboss.soa.update.url";

	// The plug-in ID
	public static final String PLUGIN_ID = "com.jboss.jbds.central.discovery.soa-tooling"; //$NON-NLS-1$

	public static final String CONFIGURATORS_EXTENSION_ID = "org.jboss.tools.central.configurators";

	private IJBossCentralDiscoveryConfigurator configurator;

	private BundleContext bundleContext;

	private static final Object CONFIGURATOR = "configurator";

	// The shared instance
	private static JBossCentralDiscoveryActivator plugin;

	/**
	 * The constructor
	 */
	public JBossCentralDiscoveryActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.setBundleContext(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		setBundleContext(null);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static JBossCentralDiscoveryActivator getDefault() {
		return plugin;
	}

	public static void log(Exception e, String message) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, message, e);
		plugin.getLog().log(status);
	}

	public static void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID,
				e.getLocalizedMessage(), e);
		plugin.getLog().log(status);
	}

	public static void log(String message) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, message);
		plugin.getLog().log(status);
	}

	public static void logWarning(String message) {
		IStatus status = new Status(IStatus.WARNING, PLUGIN_ID, message);
		plugin.getLog().log(status);
	}

	public IJBossCentralDiscoveryConfigurator getConfigurator() {
		if (configurator == null) {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
					.getExtensionPoint(CONFIGURATORS_EXTENSION_ID);
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions.length > 0) {
				IExtension extension = extensions[0];
				IConfigurationElement[] configurationElements = extension
						.getConfigurationElements();
				for (int j = 0; j < configurationElements.length; j++) {
					IConfigurationElement configurationElement = configurationElements[j];
					if (CONFIGURATOR.equals(configurationElement.getName())) {
						try {
							configurator = (IJBossCentralDiscoveryConfigurator) configurationElement
									.createExecutableExtension("class");
						} catch (CoreException e) {
							JBossCentralDiscoveryActivator.log(e);
							continue;
						}
						break;
					}
				}

			}
			if (configurator == null) {
				configurator = new DefaultJBossCentralDiscoveryConfigurator();
			}
		}
		return configurator;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
}

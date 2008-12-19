/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.jboss.tools.jmx.ui;

import javax.management.MBeanServerConnection;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.jmx.core.DomainWrapper;
import org.jboss.tools.jmx.core.MBeanAttributeInfoWrapper;
import org.jboss.tools.jmx.core.MBeanInfoWrapper;
import org.jboss.tools.jmx.core.MBeanOperationInfoWrapper;
import org.jboss.tools.jmx.ui.internal.adapters.JMXAdapterFactory;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JMXUIActivator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.jboss.tools.jmx.ui"; //$NON-NLS-1$

    // The shared instance
    private static JMXUIActivator plugin;

    private JMXAdapterFactory adapterFactory;
    private MBeanServerConnection connection;


    /**
     * The constructor
     */
    public JMXUIActivator() {
    	super();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        registerAdapters();
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        unregisterAdapters();
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static JMXUIActivator getDefault() {
        return plugin;
    }

    public static Shell getActiveWorkbenchShell() {
         IWorkbenchWindow window= getActiveWorkbenchWindow();
         if (window != null) {
            return window.getShell();
         }
         return null;
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    public static IWorkbenchPage getActivePage() {
        return getDefault().internalGetActivePage();
    }

    private IWorkbenchPage internalGetActivePage() {
        IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
        if (window == null)
            return null;
        return window.getActivePage();
    }

    public void setCurrentConnection(MBeanServerConnection connection) {
    	this.connection  =  connection;
    }

    public MBeanServerConnection getCurrentConnection() {
    	return this.connection;
    }

    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

    /**
     * Log the given exception along with the provided message and severity
     * indicator
     */
    public static void log(int severity, String message, Throwable e) {
        log(new Status(severity, PLUGIN_ID, 0, message, e));
    }

    private void registerAdapters() {
        adapterFactory = new JMXAdapterFactory();
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                DomainWrapper.class);
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                MBeanInfoWrapper.class);
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                MBeanAttributeInfoWrapper.class);
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                MBeanOperationInfoWrapper.class);
    }

    private void unregisterAdapters() {
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                DomainWrapper.class);
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                MBeanInfoWrapper.class);
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                MBeanAttributeInfoWrapper.class);
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                MBeanOperationInfoWrapper.class);
    }
}

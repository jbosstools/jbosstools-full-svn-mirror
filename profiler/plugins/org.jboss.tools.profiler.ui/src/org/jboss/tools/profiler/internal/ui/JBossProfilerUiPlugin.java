
package org.jboss.tools.profiler.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.profiler.internal.ui.launch.LaunchListener;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JBossProfilerUiPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.profiler.internal.ui"; //$NON-NLS-1$

	// The shared instance
	private static JBossProfilerUiPlugin plugin;
	
	/**
	 * The constructor
	 */
	public JBossProfilerUiPlugin() {
	}

	
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	// Launches listener
	private LaunchListener launchListener;

	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public LaunchListener getLaunchListener() {
		if (launchListener == null)
			launchListener = new LaunchListener();
		return launchListener;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (launchListener != null)
			launchListener.shutdown();
		
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static JBossProfilerUiPlugin getDefault() {
		return plugin;
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	/**
	 * Logs the specified status with this plug-in's log.
	 *
	 * @param status status to log
	 */
	public void log(IStatus status) {
		getLog().log(status);
	}


	/**
	 * Logs an internal info with the specified message.
	 *
	 * @param message the error message to log
	 */
	public void log(String message) {
		log(new Status(IStatus.INFO, getPluginId(), 0, message, null) );
	}

	/**
	 * Logs an internal error with the specified message.
	 *
	 * @param message the error message to log
	 */
	public void logErrorMessage(String message, Throwable t) {
		logMessage(IStatus.ERROR, message, t);
	}

	public void logMessage(int lvl, String message, Throwable t) {
		if(t==null) {
			log(message);
		} else {
			log(new MultiStatus(getPluginId(), lvl , new IStatus[] { throwableToStatus(t) }, message, null));
		}
	}

	public static IStatus throwableToStatus(Throwable t, int code) {
		List<IStatus> causes = new ArrayList<IStatus>();
		Throwable temp = t;
		while(temp!=null && temp.getCause()!=temp) {
			causes.add(new Status(IStatus.ERROR, getPluginId(), code, temp.getMessage()==null?temp.toString() + ":" + Messages.JBossProfilerUiPlugin_no_message:temp.toString(), temp) ); //$NON-NLS-1$
			temp = temp.getCause();
		}
        String msg = Messages.JBossProfilerUiPlugin_no_message;
        if(t!=null && t.getMessage()!=null) {
            msg = t.toString();
        }

        if(causes.isEmpty()) {
        	return new Status(IStatus.ERROR, getPluginId(), code, msg, t);
        } else {
        	return new MultiStatus(getPluginId(), code,causes.toArray(new IStatus[causes.size()]), msg, t);
        }

	}

	public static IStatus throwableToStatus(Throwable t) {
		return throwableToStatus(t, 150);
	}

	public void logErrorMessage(String message, Throwable t[]) {
		IStatus[] children = new IStatus[t.length];
		for (int i = 0; i < t.length; i++) {
			Throwable throwable = t[i];
			children[i] = throwableToStatus(throwable);
		}

		IStatus s = new MultiStatus(getPluginId(), 0,children, message, null);
		log(s);
	}

	/**
	 * Logs an internal error with the specified throwable
	 *
	 * @param e the exception to be logged
	 */
	public void log(Throwable e) {
		log(new Status(IStatus.ERROR, getPluginId(), 150, "Internal Error", e) );  //$NON-NLS-1$
	}

}

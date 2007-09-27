package org.jboss.ide.eclipse.archives.test;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.ide.eclipse.archives.test";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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
	public static Activator getDefault() {
		return plugin;
	}
	
    public String getExampleProject() {
    	return getEntry("testProject");
    }
    public String getOutputFolder() {
    	return getEntry("outputs");
    }
    public String getDummyFolder() {
    	return getEntry("dummyFiles");
    }
    public String getEntry(String entry) {
        try  {
            URL installURL = FileLocator.toFileURL(this.getBundle().getEntry(entry));//$NON-NLS-1$
            return installURL.getFile().toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }



}

package org.jboss.tools.equinox.transforms.xslt;

import java.net.URL;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	private ServiceRegistration registration;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Properties properties = new Properties();
		properties.put("equinox.transformerType", "xslt"); //$NON-NLS-1$ //$NON-NLS-2$
		registration = context.registerService(URL.class.getName(), context.getBundle().getEntry("/jboss.csv"), properties); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (registration != null)
			registration.unregister();
	}

}

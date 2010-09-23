package org.jboss.ide.eclipse.as.rse.ui;

import org.jboss.ide.eclipse.as.rse.core.RSEPublishMethod;
import org.jboss.ide.eclipse.as.ui.editor.DeploymentModuleOptionCompositeAssistant;
import org.jboss.ide.eclipse.as.ui.launch.JBossLaunchConfigurationTabGroup;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RSEUIPlugin implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		RSEUIPlugin.context = bundleContext;
		DeploymentModuleOptionCompositeAssistant.addMapping(RSEPublishMethod.RSE_ID, new RSEDeploymentPageCallback());
		JBossLaunchConfigurationTabGroup.addTabProvider(new RSELaunchTabProvider());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		RSEUIPlugin.context = null;
	}

}

package org.jboss.ide.eclipse.as.test.server;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.as.core.server.IJBoss7ManagerService;
import org.jboss.ide.eclipse.as.core.server.internal.v7.IJBoss7DeploymentResult;
import org.jboss.ide.eclipse.as.core.server.internal.v7.JBoss7DeploymentState;
import org.jboss.ide.eclipse.as.core.server.internal.v7.JBoss7ManagerServiceProxy;
import org.jboss.ide.eclipse.as.core.server.internal.v7.JBoss7ManangerException;
import org.jboss.ide.eclipse.as.test.ASTest;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;

public class JBossManagerTest {

	@Test
	public void canUseService() throws JBoss7ManangerException, InvalidSyntaxException {
		BundleContext context = ASTest.getContext();
		JBoss7ManagerServiceProxy serviceProxy = new JBoss7ManagerServiceProxy(context,
				IJBoss7ManagerService.AS_VERSION_700);
		serviceProxy.open();
		IJBoss7ManagerService manager = serviceProxy.getService();
		assertNotNull(manager);
	}

	@Test
	public void canUseServiceEvenIfAlternativeIsRegistered() throws JBoss7ManangerException, InvalidSyntaxException {
		BundleContext context = ASTest.getDefault().getBundle().getBundleContext();
		registerFakeASService("710");
		JBoss7ManagerServiceProxy serviceProxy =
				new JBoss7ManagerServiceProxy(context, IJBoss7ManagerService.AS_VERSION_700);
		serviceProxy.open();
		IJBoss7ManagerService manager = serviceProxy.getService();
		assertNotNull(manager);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void canUseAlternative() throws Exception {
		BundleContext context = ASTest.getDefault().getBundle().getBundleContext();
		registerFakeASService("710");
		JBoss7ManagerServiceProxy managerProxy =
				new JBoss7ManagerServiceProxy(context, "710");
		managerProxy.open();
		IJBoss7ManagerService manager = managerProxy.getService();
		assertNotNull(manager);
		manager.getDeploymentState("fake", 4242, "fake");
	}

	private void registerFakeASService(String version) {
		Dictionary<String, String> serviceProperties = new Hashtable<String, String>();
		serviceProperties.put(IJBoss7ManagerService.AS_VERSION_PROPERTY, version);
		ASTest.getContext().registerService(IJBoss7ManagerService.class, new JBoss71Manager(),
				serviceProperties);
	}

	private static class JBoss71Manager implements IJBoss7ManagerService {

		public IJBoss7DeploymentResult deployAsync(String host, int port, String deploymentName, File file,
				IProgressMonitor monitor) throws JBoss7ManangerException {
			throw new UnsupportedOperationException();
		}

		public IJBoss7DeploymentResult deploySync(String host, int port, String deploymentName, File file,
				IProgressMonitor monitor) throws JBoss7ManangerException {
			throw new UnsupportedOperationException();
		}

		public IJBoss7DeploymentResult undeployAsync(String host, int port, String deploymentName, boolean removeFile,
				IProgressMonitor monitor) throws JBoss7ManangerException {
			throw new UnsupportedOperationException();
		}

		public IJBoss7DeploymentResult syncUndeploy(String host, int port, String deploymentName, boolean removeFile,
				IProgressMonitor monitor) throws JBoss7ManangerException {
			throw new UnsupportedOperationException();
		}

		public JBoss7DeploymentState getDeploymentState(String host, int port, String deploymentName)
				throws JBoss7ManangerException {
			throw new UnsupportedOperationException();
		}
		
		public void stop(String host, int port) throws JBoss7ManangerException {
		}

		public void stop(String host) throws JBoss7ManangerException {
		}

		@Override
		public void dispose() {
		}
	}
}

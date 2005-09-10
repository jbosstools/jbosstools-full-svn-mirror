
package org.jboss.ide.eclipse.ejb3.wizards.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopClasspathContainer;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;

/**
 * @author Marshall
 */
public class EJB3ClasspathContainer extends AopClasspathContainer {
	public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.ejb3.wizards.core.classpath.EJB3_CONTAINER";
	public static final String DESCRIPTION = "JBoss EJB3 Libraries";
	public static final QualifiedName JBOSS_EJB3_CONFIGURATION = new QualifiedName("org.jboss.ide.eclipse.ejb3.wizards.core.classpath", "jboss-ejb3-configuration");
	
	protected IJavaProject javaProject;
	protected ILaunchConfiguration jbossConfig;
	protected String jbossConfigurationName;
	
	private static IPath clientPath, libPath, serverConfigLibPath, ejb3DeployerPath;
	static {
		clientPath = new Path("client");
		libPath = new Path("lib");
		
		serverConfigLibPath = new Path("lib");
		ejb3DeployerPath = new Path("deploy/ejb3.deployer");
	}
	
	public static final IPath[] jbossJarPaths = new IPath [] {
		clientPath.append("jnp-client.jar"),  clientPath.append("jbosssx-client.jar"),
		clientPath.append("jboss-j2ee.jar"), clientPath.append("jboss-transaction-client.jar"),
		libPath.append("commons-logging.jar"), 
	};
	
	public static final IPath[] jbossConfigRelativeJarPaths = new IPath [] {
		ejb3DeployerPath.append("jboss-ejb3.jar"), ejb3DeployerPath.append("jboss-ejb3x.jar"),
		ejb3DeployerPath.append("hibernate3.jar"), ejb3DeployerPath.append("ejb3-persistence.jar"),
		serverConfigLibPath.append("jboss-remoting.jar")
	};
	
	public EJB3ClasspathContainer (IPath path, IJavaProject project)
	{
		super (path);
		
		this.javaProject = project;

		try {
			String configName = path.segment(1);
			ILaunchConfiguration[] configurations = ServerLaunchManager.getInstance().getServerConfigurations();
			
			if (configName == null)
			{
				// old classpath container, try finding the persisten property
				configName = project.getProject().getPersistentProperty(JBOSS_EJB3_CONFIGURATION);
				if (configName != null)
				{
					// go ahead and remove the persistent property
					project.getProject().setPersistentProperty(JBOSS_EJB3_CONFIGURATION, null);
				}
			}
			
			for (int i = 0; i < configurations.length; i++)
			{
				if (configurations[i].getName().equals(configName)) {
					jbossConfig = configurations[i];
					break;
				}
			}
			
			if (jbossConfig != null)
			{
				jbossConfigurationName = jbossConfig.getName();
			}
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public IPath[] getAopJarPaths ()
	{
		if (jbossConfig == null) return new IPath[0];
		
		String jbossBaseDir = null;
		String jbossConfigDir = null;
		
		try {
			jbossBaseDir = jbossConfig.getAttribute(IJBossConstants.ATTR_JBOSS_HOME_DIR, "");
			jbossConfigDir = jbossConfig.getAttribute(IJBossConstants.ATTR_SERVER_CONFIGURATION, "default");
		} catch (CoreException e) {
			
		}
		
		ArrayList paths = new ArrayList();

		if (jbossBaseDir != null)
		{
			for (int i = 0; i < jbossJarPaths.length; i++)
			{
				IPath jar = jbossJarPaths[i];
				IPath entryPath = new Path(jbossBaseDir).append(jar);
				paths.add(entryPath);
			}
			
			if (jbossConfigDir != null)
			{
				IPath jbossServerConfigPath = new Path(jbossBaseDir).append("server").append(jbossConfigDir);
				for (int i = 0; i < jbossConfigRelativeJarPaths.length; i++)
				{
					IPath jar = jbossConfigRelativeJarPaths[i];
					IPath entryPath = jbossServerConfigPath.append(jar);
					paths.add(entryPath);
				}
			}
		}
		
		//System.out.println("paths = " + paths);
		return (IPath []) paths.toArray(new IPath[paths.size()]);
	}
	
	protected IPath[] getAopJarRelativePaths() {
		return new IPath[0];
	}

	public String getContainerId() {
		return CONTAINER_ID;
	}
	
	public String getDescription() {
		return DESCRIPTION + " [" + (jbossConfig == null ? "error" : jbossConfigurationName) + "]";
	}

	public String getJBossConfigurationName() {
		return jbossConfigurationName;
	}

	public void setJBossConfigurationName(String jbossConfigurationName) {
		this.jbossConfigurationName = jbossConfigurationName;
	}

	public ILaunchConfiguration getJBossConfiguration () {
		return jbossConfig;
	}

	public void setJBossConfiguration (ILaunchConfiguration jbossConfig) {
		this.jbossConfig = jbossConfig;
	}

}

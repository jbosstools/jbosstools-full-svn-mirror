
package org.jboss.ide.eclipse.ejb3.wizards.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.jboss.ide.eclipse.ejb3.wizards.core.EJB3WizardsCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopClasspathContainer;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;

/**
 * @author Marshall
 */
public class EJB3ClasspathContainer extends AopClasspathContainer {
	public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.ejb3.wizards.core.classpath.EJB3_CONTAINER";
	public static final String DESCRIPTION = "JBoss EJB3 Libraries";
	public static final QualifiedName JBOSS_EJB3_CONFIGURATION = new QualifiedName("org.jboss.ide.eclipse.ejb3.wizards.core.classpath", "jboss-ejb3-configuration");
	protected ILaunchConfiguration jbossConfig;

	private static IPath clientPath, libPath, serverAllLibPath, ejb3DeployerPath;
	static {
		clientPath = new Path("client");
		libPath = new Path("lib");
		serverAllLibPath = new Path("server/all/lib");
		ejb3DeployerPath = new Path("server/all/deploy/ejb3.deployer");
	}
	
	protected static final IPath[] jbossJarPaths = new IPath [] {
		clientPath.append("jnp-client.jar"),  clientPath.append("jbosssx-client.jar"), clientPath.append("jboss-j2ee.jar"), clientPath.append("jboss-transaction-client.jar"),
		ejb3DeployerPath.append("jboss-ejb3.jar"), ejb3DeployerPath.append("jboss-ejb3x.jar"), ejb3DeployerPath.append("hibernate3.jar"),
		libPath.append("commons-logging.jar"), serverAllLibPath.append("jboss-remoting.jar")
	};
	
	public EJB3ClasspathContainer (IPath path, ILaunchConfiguration jbossConfig)
	{
		super (path);
		this.jbossConfig = jbossConfig;
	}
	
	public IPath[] getAopJarPaths ()
	{
		String baseDir = EJB3WizardsCorePlugin.getDefault().getBaseDir();
		String jbossBaseDir = null;
		try {
			jbossBaseDir = jbossConfig.getAttribute(IJBossConstants.ATTR_JBOSS_HOME_DIR, "");
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
		}
		
		System.out.println("paths = " + paths);
		return (IPath []) paths.toArray(new IPath[paths.size()]);
	}
	
	protected IPath[] getAopJarRelativePaths() {
		return new IPath[0];
	}

	public String getContainerId() {
		return CONTAINER_ID;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}

}


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
	
	protected static final IPath[] ejb3JarPaths = new IPath [] {
		new Path("jboss-ejb3x.jar"),new Path("jboss-remoting.jar"), new Path("jboss-ejb3.jar")
	};
	
	protected static final IPath[] jbossJarPaths = new IPath [] {
		new Path("client").append(new Path("jnp-client.jar")),  new Path("client").append(new Path("jbosssx-client.jar")),
		new Path("client").append(new Path("jboss-j2ee.jar")), new Path("client").append(new Path("jboss-transaction-client.jar")),
		new Path("server/all/deploy/ejb3.deployer/hibernate3.jar"), new Path("lib/commons-logging.jar")
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
		
		for (int i = 0; i < ejb3JarPaths.length; i++)
		{
			IPath jar = ejb3JarPaths[i];
			IPath entryPath = new Path(baseDir).append(jar);
			paths.add(entryPath);
		}
		
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
		return ejb3JarPaths;
	}

	public String getContainerId() {
		return CONTAINER_ID;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}

}

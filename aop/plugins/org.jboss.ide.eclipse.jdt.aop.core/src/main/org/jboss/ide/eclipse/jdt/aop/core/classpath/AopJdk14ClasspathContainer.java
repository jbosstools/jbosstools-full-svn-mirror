/*
 * Created on Sep 20, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.classpath;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author Marshall
 */
public class AopJdk14ClasspathContainer extends AopClasspathContainer
{
	
	public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.aop.core.classpath.AOP_14_CONTAINER";
	public static final String DESCRIPTION = "JBossAOP 1.3 Libraries";
	
	protected final static IPath aopJars[] = new IPath[] {
			new Path("jboss-aop.jar"), new Path("jboss-common.jar"), new Path("qdox.jar"),
			new Path("concurrent.jar"), new Path("trove.jar"), new Path("javassist.jar"),
			new Path("jboss-aspect-library.jar"), new Path("bsh-1.3.0.jar")};
	
	public AopJdk14ClasspathContainer (IPath path)
	{
		super(path);
	}
	
	protected IPath[] getAopJarRelativePaths() {
		return aopJars;
	}
	
	public String getContainerId () {
		return CONTAINER_ID;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}

}
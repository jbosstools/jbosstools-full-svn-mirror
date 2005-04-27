package org.jboss.ide.eclipse.jdt.aop.core.classpath;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author Marshall
 */
public class AopJdk15ClasspathContainer extends AopClasspathContainer {
	public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.aop.core.classpath.AOP_15_CONTAINER";
	public static final String DESCRIPTION = "JBossAOP 1.1 Libraries (jdk 1.5)";
	
	protected final static IPath aopJars[] = new IPath[] {
		new Path("jboss-aop-jdk50.jar"), new Path("jboss-common.jar"), new Path("qdox.jar"),
		new Path("concurrent.jar"), new Path("trove.jar"), new Path("javassist.jar"),
		new Path("jboss-aspect-library-jdk50.jar"), new Path("pluggable-instrumentor.jar")};
	
	public AopJdk15ClasspathContainer (IPath path)
	{
		super(path);
	}
	
	protected IPath[] getAopJarRelativePaths() {
		return aopJars;
	}
	
	public String getContainerId() {
		return CONTAINER_ID;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}
}

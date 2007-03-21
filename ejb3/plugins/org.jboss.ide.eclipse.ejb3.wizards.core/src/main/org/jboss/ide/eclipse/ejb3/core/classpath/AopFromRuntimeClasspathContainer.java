/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.ejb3.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.ide.eclipse.as.core.runtime.server.AbstractJBossServerRuntime;

/**
 * @author Marshall
 */
public class AopFromRuntimeClasspathContainer implements IClasspathContainer
{
   public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.ejb3.wizards.core.classpath.AOP15_CONTAINER";
   public static final String DESCRIPTION = "JBossAOP 1.3 Libraries (jdk 1.5)";

   private static IPath clientPath, libPath, aopDeployerPath;
   static {
      clientPath = new Path("client");
      libPath = new Path("lib");
      aopDeployerPath = new Path("deploy/jboss-aop-jdk50.deployer");
   }

   
   protected final static IPath aopJars[] = new IPath[]
     {
	   libPath.append("jboss-common.jar"), 
	   clientPath.append("concurrent.jar"),
       clientPath.append("trove.jar"), clientPath.append("javassist.jar")};
   
   protected final static IPath configRelativePaths[] = 
	   new IPath[] { aopDeployerPath.append("jboss-aop-jdk50.jar"),
	   aopDeployerPath.append("jboss-aspect-library-jdk50.jar")};

   
   protected IJavaProject javaProject;
   protected AbstractJBossServerRuntime jbsRuntime;
   protected IPath path;
   public AopFromRuntimeClasspathContainer(IPath path, IJavaProject project) {
	  this.path = path;
      this.javaProject = project;
      String runtimeName = path.segment(1);
      IRuntime runtime = ServerCore.findRuntime(runtimeName);
      if( runtime != null ) 
    	  jbsRuntime = (AbstractJBossServerRuntime)runtime.loadAdapter(AbstractJBossServerRuntime.class, new NullProgressMonitor());
   }

   public IPath[] getAopJarPaths() {
      if (jbsRuntime == null)
         return new IPath[0];

      String jbossBaseDir = jbsRuntime.getRuntime().getLocation().toOSString();
      String jbossConfigDir = jbsRuntime.getJBossConfiguration();

      ArrayList paths = new ArrayList();

      if (jbossBaseDir != null) {
         for (int i = 0; i < aopJars.length; i++) {
            IPath jar = aopJars[i];
            IPath entryPath = new Path(jbossBaseDir).append(jar);
            paths.add(entryPath);
         }

         if (jbossConfigDir != null) {
            IPath jbossServerConfigPath = new Path(jbossBaseDir).append("server").append(jbossConfigDir);
            for (int i = 0; i < configRelativePaths.length; i++) {
               IPath jar = configRelativePaths[i];
               IPath entryPath = jbossServerConfigPath.append(jar);
               paths.add(entryPath);
            }
         }
      }

      return (IPath[]) paths.toArray(new IPath[paths.size()]);
   }

   protected IPath[] getAopJarRelativePaths() {
      return new IPath[0];
   }

   public String getContainerId() {
      return CONTAINER_ID;
   }

   public String getDescription() {
      return DESCRIPTION + " [" + (jbsRuntime == null ? "error" : jbsRuntime.getRuntime().getName()) + "]";
   }

   public IClasspathEntry[] getClasspathEntries() {
      ArrayList entries = new ArrayList();
      IPath jarPaths[] = getAopJarPaths();

      for (int i = 0; i < jarPaths.length; i++) {
         IPath jar = jarPaths[i];

         // Later we can add the source jars here..
         IClasspathEntry entry = JavaCore.newLibraryEntry(jar, null, null, false);
         entries.add(entry);
      }

      return (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
   }
	
	public int getKind() {
		return K_APPLICATION;
	}
	
	public IPath getPath() {
		return path;
	}
}

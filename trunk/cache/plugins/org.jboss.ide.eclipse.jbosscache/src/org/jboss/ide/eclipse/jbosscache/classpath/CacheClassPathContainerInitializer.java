/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class CacheClassPathContainerInitializer extends ClasspathContainerInitializer
{
   
   public CacheClassPathContainerInitializer(){
      
   }

   public void initialize(IPath containerPath, IJavaProject project) throws CoreException
   {
      String containerId = containerPath.segment(0);
      IClasspathContainer container = null;
      
      if(containerId.equals(CacheVersion124CpContainer.CONTAINER_ID)){
         container = new CacheVersion124CpContainer(containerPath);
      }else if(containerId.equals(CacheVersion130CpContainer.CONTAINER_ID)){
         container = new CacheVersion130CpContainer(containerPath);
      }
      
      if(container != null)
      {
         JavaCore.setClasspathContainer(containerPath,new IJavaProject[]{project},new IClasspathContainer[]{container},null);
         
      }
      
   }

   public String getDescription(IPath containerPath, IJavaProject project)
   {
      String containerId = containerPath.segment(0);
      
      if(containerId.equals(CacheVersion124CpContainer.CONTAINER_ID)){
         return CacheVersion124CpContainer.DESCRIPTION;
      }else if(containerId.equals(CacheVersion130CpContainer.CONTAINER_ID)){
         return CacheVersion130CpContainer.DESCRIPTION;
      }else
         return "";
      
   }
   
   

}

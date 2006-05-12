/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.core.classpath;

import java.io.File;
import java.io.FileFilter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.JDTCorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractClasspathVariableInitializer extends ClasspathVariableInitializer
{
   private IProgressMonitor fMonitor;

   /**Constructor for the AbstractClasspathVariableInitializer object */
   public AbstractClasspathVariableInitializer()
   {
   }

   /**
    * Description of the Method
    *
    * @param variable  Description of the Parameter
    */
   public void initialize(String variable)
   {
      IPath newPath = searchJar(JDTCorePlugin.getDefault());
      if (newPath != null)
      {
         try
         {
            this.setJ2EEVariable(variable, newPath);
         }
         catch (CoreException ce)
         {
            AbstractPlugin.logError("Cannot initialize variable", ce);//$NON-NLS-1$
         }
      }
   }

   /**
    * Gets the jarName attribute of the AbstractClasspathVariableInitializer object
    *
    * @return   The jarName value
    */
   protected abstract String getJarName();

   /**
    * Gets the libFolder attribute of the AbstractClasspathVariableInitializer object
    *
    * @return   The libFolder value
    */
   protected abstract String getLibFolder();

   /**
    * Gets the monitor attribute of the AbstractClasspathVariableInitializer object
    *
    * @return   The monitor value
    */
   protected IProgressMonitor getMonitor()
   {
      if (fMonitor == null)
      {
         return new NullProgressMonitor();
      }
      return fMonitor;
   }

   /**
    * Description of the Method
    *
    * @param plugin  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected IPath searchJar(AbstractPlugin plugin)
   {
      IPath result = null;

      String baseDir = plugin.getBaseDir();
      File libDir = new File(baseDir + "/" + ClassPathConstants.LIB_FOLDER + "/" + this.getLibFolder());//$NON-NLS-1$ //$NON-NLS-2$

      // Search for the jar
      final String searchedJar = this.getJarName();
      File[] jars = libDir.listFiles(new FileFilter()
      {
         public boolean accept(File file)
         {
            return (file.getName().equals(searchedJar));//$NON-NLS-1$
         }
      });

      if (jars.length == 1)
      {
         File jarFile = jars[0];
         result = new Path(jarFile.toString());
      }
      return result;
   }

   /**
    * Description of the Method
    *
    * @param variable           Description of the Parameter
    * @param newPath            Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   private boolean changedJ2EEVariable(String variable, IPath newPath) throws CoreException
   {
      IPath oldPath = JavaCore.getClasspathVariable(variable);
      return !newPath.equals(oldPath);
   }

   /**
    * Sets the j2EEVariable attribute of the AbstractClasspathVariableInitializer object
    *
    * @param variable           The new j2EEVariable value
    * @param newPath            The new j2EEVariable value
    * @exception CoreException  Description of the Exception
    */
   private void setJ2EEVariable(String variable, IPath newPath) throws CoreException
   {
      if (this.changedJ2EEVariable(variable, newPath))
      {
         JavaCore.setClasspathVariable(variable, newPath, getMonitor());
      }
   }
}

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

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

/**
 * Utility class for classpath access
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ClassPathContainerRepository
{
   private Map repository = new Hashtable();

   private static ClassPathContainerRepository instance = new ClassPathContainerRepository();

   /** Avoid instantiation */
   private ClassPathContainerRepository()
   {
   }

   /**
    * Adds a feature to the ClassPathEntry attribute of the ClassPathContainerRepository object
    *
    * @param containerId  The feature to be added to the ClassPathEntry attribute
    */
   public void addClassPathEntry(String containerId)
   {
      IClasspathEntry entry = JavaCore.newContainerEntry(new Path(containerId), true);
      this.repository.put(containerId, entry);
   }

   /**
    * Gets the entries attribute of the ClassPathContainerRepository object
    *
    * @return   The entries value
    */
   public Map getEntries()
   {
      return Collections.unmodifiableMap(this.repository);
   }

   /**
    * Gets the entry attribute of the ClassPathContainerRepository object
    *
    * @param containerId  Description of the Parameter
    * @return             The entry value
    */
   public IClasspathEntry getEntry(String containerId)
   {
      return (IClasspathEntry) this.repository.get(containerId);
   }

   /**
    * Gets the instance attribute of the ClassPathContainerRepository class
    *
    * @return   The instance value
    */
   public static ClassPathContainerRepository getInstance()
   {
      return instance;
   }
}

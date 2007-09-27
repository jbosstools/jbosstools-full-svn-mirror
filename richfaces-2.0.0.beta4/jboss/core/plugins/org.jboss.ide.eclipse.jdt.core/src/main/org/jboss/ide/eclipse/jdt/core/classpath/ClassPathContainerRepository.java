/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   private ClassPathContainerRepository() { }


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

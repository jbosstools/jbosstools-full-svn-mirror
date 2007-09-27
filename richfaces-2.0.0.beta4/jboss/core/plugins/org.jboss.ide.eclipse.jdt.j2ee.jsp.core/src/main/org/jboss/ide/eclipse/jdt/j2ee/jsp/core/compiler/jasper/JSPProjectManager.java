/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCorePlugin;

/**
 * Factory that manages all the JSPProject instance in the
 * Eclipse workspace
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPProjectManager
{

   /** Qualified name where the webroot is stored */
   public static QualifiedName QNAME_WEBROOT = new QualifiedName("", JDTJ2EEJSPCorePlugin.JSP_NATURE_ID);//$NON-NLS-1$

   /** Map of the projects */
   private static Map projects = new Hashtable();


   /** Avoid instantiation */
   private JSPProjectManager() { }


   /**
    * Lazy creation of the JSPProject for the given Eclipse project.
    *
    * @param project  Description of the Parameter
    * @return         The jSPProject value
    */
   public static JSPProject getJSPProject(IProject project)
   {
      JSPProject jProject = (JSPProject) projects.get(project);
      if (jProject == null)
      {
         jProject = new JSPProject(project);
         AbstractPlugin.getWorkspace().addResourceChangeListener(jProject);
         projects.put(project, jProject);
      }
      return jProject;
   }


   /**
    * Gets the propertyFromWorkspace attribute of the JSPProjectManager class
    *
    * @param project  Description of the Parameter
    * @param key      Description of the Parameter
    * @return         The propertyFromWorkspace value
    */
   public static String getPropertyFromWorkspace(IProject project, QualifiedName key)
   {
      String result = null;
      try
      {
         result = project.getPersistentProperty(key);
      }
      catch (CoreException ce)
      {
      }
      return result;
   }


   /**
    * Remove the JSPProject when not needed.
    *
    * @param project  Description of the Parameter
    */
   public static void removeJSPProject(IProject project)
   {
      projects.remove(project);
   }


   /**
    * Sets the propertyToWorkspace attribute of the JSPProjectManager class
    *
    * @param project  The new propertyToWorkspace value
    * @param key      The new propertyToWorkspace value
    * @param value    The new propertyToWorkspace value
    */
   public static void setPropertyToWorkspace(IProject project, QualifiedName key, String value)
   {
      try
      {
         project.setPersistentProperty(key, value);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Error while trying to save a property");//$NON-NLS-1$
      }
   }
}

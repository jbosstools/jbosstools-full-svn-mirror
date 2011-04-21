/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ServerLaunchUtil
{
   /** Constructor for the ServerLaunchUtil object */
   private ServerLaunchUtil() { }


   /**
    * Description of the Method
    *
    * @param text       Description of the Parameter
    * @param separator  Description of the Parameter
    * @param list       Description of the Parameter
    * @return           Description of the Return Value
    */
   public static List appendListElementToString(String text, String separator, List list)
   {
      ArrayList newList = new ArrayList();
      for (int i = 0; i < list.size(); i++)
      {
         newList.add(text + separator + list.get(i));
      }
      return newList;
   }


   /**
    * Gets the arrayFromList attribute of the ServerLaunchUtil object
    *
    * @param configuration  Description of the Parameter
    * @param attribute      Description of the Parameter
    * @return               The arrayFromList value
    */
   public static String[] getArrayFromList(ILaunchConfiguration configuration, String attribute)
   {
      List list = null;
      try
      {
         if ((list = configuration.getAttribute(attribute, (List) null)) == null)
         {
            return null;
         }
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
      return (String[]) list.toArray(new String[list.size()]);
   }


   /**
    * Gets the validDirectory attribute of the ServerLaunchUtil object
    *
    * @param configuration      Description of the Parameter
    * @param parentDir          Description of the Parameter
    * @param attribute          Description of the Parameter
    * @return                   The validDirectory value
    * @exception CoreException  Description of the Exception
    */
   public static boolean isValidDirectory(ILaunchConfiguration configuration, String parentDir, String attribute) throws CoreException
   {
      List list = configuration.getAttribute(attribute, Collections.EMPTY_LIST);
      for (Iterator iter = list.iterator(); iter.hasNext(); )
      {
         File file = new File(parentDir + File.separator + (String) iter.next());
         if (!file.isFile())
         {
            return false;
         }
      }
      return true;
   }


   /**
    * Precondition: The elements of a single list where attributesForLists
    * points to are expected to be strings
    *
    * @param configuration
    * @param attribute
    * @param attributesForLists
    * @return                    Description of the Return Value
    * @throws CoreException
    */
   public static List setAttributeFromListAttributes(ILaunchConfigurationWorkingCopy configuration, String attribute, String[] attributesForLists)
   {
      ArrayList target = new ArrayList();
      List source;
      for (int i = 0; i < attributesForLists.length; i++)
      {
         try
         {
            if ((source = configuration.getAttribute(attributesForLists[i], (List) null)) != null)
            {
               for (Iterator iter = source.iterator(); iter.hasNext(); )
               {
                  target.add(iter.next());
               }
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
            return null;
         }
      }
      configuration.setAttribute(attribute, target);
      return target;
   }


   /**
    * Sets the attributeFromStringAttributes attribute of the ServerLaunchUtil
    * object
    *
    * @param configuration       The new attributeFromStringAttributes value
    * @param attribute           The new attributeFromStringAttributes value
    * @param attributesForLists  The new attributeFromStringAttributes value
    * @return                    Description of the Return Value
    */
   public static String setAttributeFromStringAttributes(ILaunchConfigurationWorkingCopy configuration, String attribute, String[] attributesForLists)
   {
      String s = null;
      String result = "";//$NON-NLS-1$
      for (int i = 0; i < attributesForLists.length; i++)
      {
         try
         {
            if ((s = configuration.getAttribute(attributesForLists[i], (String) null)) != null
                  && s.length() > 0)
            {
               result += " " + s;//$NON-NLS-1$
               result = result.trim();
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
            return null;
         }
      }
      configuration.setAttribute(attribute, result);
      return result;
   }


   /**
    * Sets the classpath attribute of the ServerLaunchUtil object
    *
    * @param type           The new classpath value
    * @param configuration  The new classpath value
    * @param attributes     The new classpath value
    */
   public static void setClasspath(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String[] attributes)
   {
      // List list =
      setAttributeFromListAttributes(configuration, type == LaunchType.START ? IServerLaunchConfigurationConstants.ATTR_CLASSPATH
            : IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_CLASSPATH, attributes);
   }


   /**
    * Sets the listAttributeFromArray attribute of the ServerLaunchUtil object
    *
    * @param configuration  The new listAttributeFromArray value
    * @param array          The new listAttributeFromArray value
    * @param attribute      The new listAttributeFromArray value
    */
   public static void setListAttributeFromArray(ILaunchConfigurationWorkingCopy configuration, String[] array, String attribute)
   {
      ArrayList list = new ArrayList();
      for (int i = 0; i < array.length; i++)
      {
         list.add(array[i]);
      }
      configuration.setAttribute(attribute, list);
   }


   /**
    * Sets the mainType attribute of the ServerLaunchUtil object
    *
    * @param type           The new mainType value
    * @param configuration  The new mainType value
    * @param typeName       The new mainType value
    */
   public static void setMainType(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String typeName)
   {
      configuration.setAttribute(type == LaunchType.START ? IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME
            : IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_TYPE, typeName);
   }


   /**
    * Sets the programArgs attribute of the ServerLaunchUtil object
    *
    * @param type           The new programArgs value
    * @param configuration  The new programArgs value
    * @param attributes     The new programArgs value
    */
   public static void setProgramArgs(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String[] attributes)
   {
      setAttributeFromStringAttributes(configuration, type == LaunchType.START ? IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS
            : IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_PROGRAM_ARGS, attributes);
   }


   /**
    * Sets the relativeToHomedirClasspath attribute of the ServerLaunchUtil
    * object
    *
    * @param type           The new relativeToHomedirClasspath value
    * @param configuration  The new relativeToHomedirClasspath value
    * @param classpath      The new relativeToHomedirClasspath value
    */
   public static void setRelativeToHomedirClasspath(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String[] classpath)
   {
      setListAttributeFromArray(configuration, classpath, type == LaunchType.START ? IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_CLASSPATH
            : IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_SHUTDOWN_CLASSPATH);
   }


   /**
    * Sets the relativeToJDKClasspath attribute of the ServerLaunchUtil object
    *
    * @param type           The new relativeToJDKClasspath value
    * @param configuration  The new relativeToJDKClasspath value
    * @param classpath      The new relativeToJDKClasspath value
    */
   public static void setRelativeToJDKClasspath(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String[] classpath)
   {
      setListAttributeFromArray(configuration, classpath, type == LaunchType.START ? IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_JDK_CLASSPATH
            : null);
   }


   /**
    * Sets the userProgramArgs attribute of the ServerLaunchUtil object
    *
    * @param type           The new userProgramArgs value
    * @param configuration  The new userProgramArgs value
    * @param attribute      The new userProgramArgs value
    */
   public static void setUserProgramArgs(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String attribute)
   {
      configuration.setAttribute(type == LaunchType.START ? IServerLaunchConfigurationConstants.ATTR_USER_PROGRAM_ARGS
            : IServerLaunchConfigurationConstants.ATTR_USER_SHUTDOWN_PROGRAM_ARGS, attribute);
   }


   /**
    * Sets the vMArgs attribute of the ServerLaunchUtil object
    *
    * @param type           The new vMArgs value
    * @param configuration  The new vMArgs value
    * @param attributes     The new vMArgs value
    */
   public static void setVMArgs(LaunchType type, ILaunchConfigurationWorkingCopy configuration, String[] attributes)
   {
      setAttributeFromStringAttributes(configuration, type == LaunchType.START ? IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS
            : IServerLaunchConfigurationConstants.ATTR_SHUTDOWN_VM_ARGS, attributes);
   }
}

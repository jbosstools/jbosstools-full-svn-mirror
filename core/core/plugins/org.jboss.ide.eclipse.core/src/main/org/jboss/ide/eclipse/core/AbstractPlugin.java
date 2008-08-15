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
package org.jboss.ide.eclipse.core;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract plugin for all the JBoss-IDE plugins. Includes many functionnalities
 * such as logging and environment access.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AbstractPlugin extends AbstractUIPlugin
{
   /** Resource bundle */
   protected ResourceBundle resourceBundle;

   /** The shared instance */
   private static AbstractPlugin plugin;

   /** The constructor */
   public AbstractPlugin()
   {
      plugin = this;
   }

   /**
    * Gets the base installation dir of the plugin
    *
    * @return   The installation dir
    */
   public String getBaseDir()
   {
      try
      {
         URL installURL = Platform.asLocalURL(this.getBundle().getEntry("/"));//$NON-NLS-1$
         return installURL.getFile().toString();
      }
      catch (IOException ioe)
      {
         logError("Cannot compute base installation dir of plugin", ioe);//$NON-NLS-1$
      }
      return null;
   }

   /**
    * Returns the plugin's resource bundle,
    *
    * @return   The resource bundle
    */
   public ResourceBundle getResourceBundle()
   {
      return resourceBundle;
   }

   /**
    * Shows a message dialog with the given message
    *
    * @param message  Message to display
    */
   public void showErrorMessage(String message)
   {
      IWorkbenchWindow window = getDefault().getWorkbench().getActiveWorkbenchWindow();
      if (window != null)
      {
         MessageDialog.openError(window.getShell().getShell(),
               CoreMessages.getString("AbstractPlugin.level.error"), message);//$NON-NLS-1$
      }
   }

   /**
    * Shows a message dialog with the given throwable
    *
    * @param throwable  Throwable to display
    */
   public void showErrorMessage(Throwable throwable)
   {
      showErrorMessage(throwable.getClass().getName() + " " + throwable.getMessage());//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param message  Description of the Parameter
    */
   public void showInfoMessage(String message)
   {
      IWorkbenchWindow window = getDefault().getWorkbench().getActiveWorkbenchWindow();
      if (window != null)
      {
         MessageDialog.openInformation(window.getShell().getShell(), CoreMessages
               .getString("AbstractPlugin.level.info"), message);//$NON-NLS-1$
      }
   }

   /**
    * Description of the Method
    *
    * @param message  Description of the Parameter
    */
   public void showWarningMessage(String message)
   {
      IWorkbenchWindow window = getDefault().getWorkbench().getActiveWorkbenchWindow();
      if (window != null)
      {
         MessageDialog.openWarning(window.getShell().getShell(),
               CoreMessages.getString("AbstractPlugin.level.warning"), message);//$NON-NLS-1$
      }
   }

   /**
    * Returns the string from the plugin's resource bundle,
    * or 'key' if not found.
    *
    * @param key  Description of the Parameter
    * @return     The resourceString value
    */
   public static String getResourceString(String key)
   {
      ResourceBundle bundle = getDefault().getResourceBundle();
      try
      {
         return bundle.getString(key);
      }
      catch (MissingResourceException e)
      {
         return key;
      }
   }

   /**
    * Gets the current Shell for the plugin
    *
    * @return   The shell value
    */
   public static Shell getShell()
   {
      return getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
   }

   /**
    * Returns the standard display to be used. The method first checks, if
    * the thread calling this method has an associated display. If so, this
    * display is returned. Otherwise the method returns the default display.
    *
    * @return   The standardDisplay value
    */
   public static Display getStandardDisplay()
   {
      Display display = Display.getCurrent();
      if (display == null)
      {
         display = Display.getDefault();
      }
      return display;
   }

   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The unique indentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }

   /**
    * Returns the workspace instance.
    *
    * @return   The workspace value
    */
   public static IWorkspace getWorkspace()
   {
      return ResourcesPlugin.getWorkspace();
   }

   /**
    * Logs the specified status with this plug-in's log.
    *
    * @param status  status to log
    */
   public static void log(IStatus status)
   {
      getDefault().getLog().log(status);
   }

   /**
    * Logs an internal error with the specified throwable
    *
    * @param e  the exception to be logged
    */
   public static void log(Throwable e)
   {
      log(new Status(IStatus.ERROR, getUniqueIdentifier(), 0, "Internal Error", e));//$NON-NLS-1$
   }

   /**
    * Logs an internal error with the specified message.
    *
    * @param message  the error message to log
    */
   public static void logError(String message)
   {
      logError(message, null);
   }

   /**
    * Logs a throwable with the specified message.
    *
    * @param message    Message to log
    * @param throwable  Throwable to log
    */
   public static void logError(String message, Throwable throwable)
   {
      log(new Status(IStatus.ERROR, getUniqueIdentifier(), 0, message, throwable));
   }

   /**
    * Wrap a throwable and add plugin identification
    *
    * @param throwable  Throwable to log
    * @return           Description of the Return Value
    */
   public static CoreException wrapException(Throwable throwable)
   {
      String message = (throwable.getMessage() != null) ? throwable.getMessage() : throwable.toString();
      logError(message, throwable);
      return new CoreException(new Status(IStatus.ERROR, getUniqueIdentifier(), 0, message, throwable));
   }

   /**
    * Returns the shared instance.
    *
    * @return   The shared instance
    */
   private static AbstractPlugin getDefault()
   {
      return plugin;
   }
}

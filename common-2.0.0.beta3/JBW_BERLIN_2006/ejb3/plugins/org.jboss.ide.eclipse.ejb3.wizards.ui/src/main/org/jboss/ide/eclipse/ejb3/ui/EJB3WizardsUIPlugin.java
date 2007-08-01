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
package org.jboss.ide.eclipse.ejb3.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EJB3WizardsUIPlugin extends AbstractUIPlugin
{
   //The shared instance.
   private static EJB3WizardsUIPlugin plugin;

   //Resource bundle.
   private ResourceBundle resourceBundle;

   /**
    * The constructor.
    */
   public EJB3WizardsUIPlugin()
   {
      super();
      plugin = this;
   }

   /**
    * This method is called upon plug-in activation
    */
   public void start(BundleContext context) throws Exception
   {
      super.start(context);
   }

   /**
    * This method is called when the plug-in is stopped
    */
   public void stop(BundleContext context) throws Exception
   {
      super.stop(context);
      plugin = null;
      resourceBundle = null;
   }

   /**
    * Returns the shared instance.
    */
   public static EJB3WizardsUIPlugin getDefault()
   {
      return plugin;
   }

   /**
    * Returns the string from the plugin's resource bundle,
    * or 'key' if not found.
    */
   public static String getResourceString(String key)
   {
      ResourceBundle bundle = EJB3WizardsUIPlugin.getDefault().getResourceBundle();
      try
      {
         return (bundle != null) ? bundle.getString(key) : key;
      }
      catch (MissingResourceException e)
      {
         return key;
      }
   }

   /**
    * Returns the plugin's resource bundle,
    */
   public ResourceBundle getResourceBundle()
   {
      try
      {
         if (resourceBundle == null)
            resourceBundle = ResourceBundle
                  .getBundle("org.jboss.ide.eclipse.ejb3.ui.EJB3WizardsUIPluginResources");
      }
      catch (MissingResourceException x)
      {
         resourceBundle = null;
      }
      return resourceBundle;
   }

   public static void alert(String string)
   {
      MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
            "EJB3 Tools - Alert", Display.getDefault().getSystemImage(SWT.ICON_INFORMATION), string,
            MessageDialog.INFORMATION, new String[]
            {"OK",}, 0);

      dialog.setBlockOnOpen(true);

      dialog.open();
   }

   public static void error(String string)
   {
      MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
            "EJB3 Tools - Error", Display.getDefault().getSystemImage(SWT.ICON_ERROR), string, MessageDialog.ERROR,
            new String[]
            {"OK",}, 0);

      dialog.setBlockOnOpen(true);

      dialog.open();
   }

   public static void warn(String string)
   {
      MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
            "EJB3 Tools - Warning", Display.getDefault().getSystemImage(SWT.ICON_WARNING), string,
            MessageDialog.WARNING, new String[]
            {"OK",}, 0);

      dialog.setBlockOnOpen(true);

      dialog.open();
   }

}

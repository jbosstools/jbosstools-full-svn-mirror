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
package org.jboss.ide.eclipse.firstrun;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class FirstRunPlugin extends AbstractUIPlugin
{

   //The shared instance.
   private static FirstRunPlugin plugin;

   public static final String FIRST_RUN_PROPERTY = "org.jboss.ide.eclipse.firstrun";
   public static final String PLUGIN_ID = "org.jboss.ide.eclipse.firstrun";
   public static final String EXTENSION_WIZARD_PAGE = "FirstRunWizardPage";
   
   
   public static final String NEW_WORKSPACE = "NEW_WORKSPACE";
   
   // The largest JBoss IDE version used in this workspace
   public static final String FIRST_RUN_PROPERTY_LATEST_VERSION = 
	   "org.jboss.ide.eclipse.firstrun.latest.version";
   
   public static final String FIRST_RUN_VERSION_PREFIX = 
	   "org.jboss.ide.eclipse.firstrun.version";
   

   public static final String ICON_JBOSSIDE_LOGO = "icons/jbosside-logo.png";

   /**
    * The constructor.
    */
   public FirstRunPlugin()
   {
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
   }

   /**
    * Returns the shared instance.
    */
   public static FirstRunPlugin getDefault()
   {
      return plugin;
   }

   /**
    * Returns an image descriptor for the image file at the given
    * plug-in relative path.
    *
    * @param path the path
    * @return the image descriptor
    */
   public static ImageDescriptor getImageDescriptor(String path)
   {
      return AbstractUIPlugin.imageDescriptorFromPlugin("org.jboss.ide.eclipse.firstrun", path);
   }
}

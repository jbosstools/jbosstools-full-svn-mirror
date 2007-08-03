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

import java.util.Dictionary;

/**
 * Core plugin.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class CorePlugin extends AbstractPlugin
{
   /** The shared instance */
   private static CorePlugin plugin;
   
   
   public static final String getCurrentVersion() {
	   Dictionary d = plugin.getBundle().getHeaders();
	   Object o = d.get("Bundle-Version");
	   if( o != null && o instanceof String ) return (String)o;
	   return null;
   }
   
   
   /**
    *  Returns 0 on a match, -1 if id is below target, 1 if id is above target
    */
   public static final int compare(String id, String targetId) {
	   String[] targetAsArray = targetId.split("\\.");
	   String[] idAsArray = id.split("\\.");
	   
	   for( int i = 0; i < targetAsArray.length; i++ ) {
		   if( targetAsArray[i].equals("*")) 
			   return 0;
		   if( i < idAsArray.length ) {
			   if( !idAsArray[i].equals(targetAsArray[i])) {
				   return idAsArray[i].compareTo(targetAsArray[i]);
			   }
		   }
	   }
	   
	   return 0;
   }
   
   
   /** The constructor. */
   public CorePlugin()
   {
      plugin = this;
   }

   /**
    * Returns the shared instance.
    *
    * @return   The shared instance
    */
   public static CorePlugin getDefault()
   {
      return plugin;
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
}
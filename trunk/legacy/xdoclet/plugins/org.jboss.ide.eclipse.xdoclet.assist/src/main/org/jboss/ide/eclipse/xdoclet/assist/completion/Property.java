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
package org.jboss.ide.eclipse.xdoclet.assist.completion;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 * @todo      Javadoc to complete
 */
public class Property
{
   private String name;

   private String value;

   /**
    *Constructor for the Property object
    *
    * @param name   Description of the Parameter
    * @param value  Description of the Parameter
    */
   public Property(String name, String value)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("Name of Property must not be null");//$NON-NLS-1$
      }
      this.name = name;
      this.value = value;
   }

   /**
    * Gets the name attribute of the Property object
    *
    * @return   The name value
    */
   public String getName()
   {
      return name;
   }

   /**
    * Gets the value attribute of the Property object
    *
    * @return   The value value
    */
   public String getValue()
   {
      return value;
   }

   /**
    * Sets the value attribute of the Property object
    *
    * @param value  The new value value
    */
   public void setValue(String value)
   {
      this.value = value;
   }
}

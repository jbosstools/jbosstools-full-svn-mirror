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
package org.jboss.ide.eclipse.core.util;

/**
 * General purpose class for unsafe named type enumeration
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class NamedType
{
   private String name;

   /**
    * Constructor for a Named yype object
    *
    * @param name  Name of the type
    */
   protected NamedType(String name)
   {
      this.name = name;
   }

   /**
    * Implementation of equals method.
    * To be equal, must be of the same class and
    * have the same name
    *
    * @param obj  Object to test equality
    * @return     True if Object is equal
    */
   public boolean equals(Object obj)
   {
      return (this.getClass().equals(obj.getClass()) && this.name.equals(((NamedType) obj).getName()));
   }

   /**
    * Gets the name of the type
    *
    * @return   The name of the type
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * Implementation of the hashCode method.
    *
    * @return   Hash code of the named type
    */
   public int hashCode()
   {
      return this.name.hashCode();
   }

   /**
    * Gets the String representation
    *
    * @return   The String representation
    */
   public String toString()
   {
      return this.name;
   }
}

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
 * Name/Value pair holder for general purpose
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NameValuePair
{
   private String name = null;

   private String value = null;

   /**
    * Constructor
    *
    * @param name  Name for the pair
    */
   public NameValuePair(String name)
   {
      this.name = name;
      this.value = "";//$NON-NLS-1$
   }

   /**
    *Constructor for the NameValuePair object
    *
    * @param name   Description of the Parameter
    * @param value  Description of the Parameter
    */
   public NameValuePair(String name, String value)
   {
      this.name = name;
      this.value = value;
   }

   /**
    * @return   The pair name
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * @return   The pair value
    */
   public String getValue()
   {
      return this.value;
   }

   /**
    * Sets the pair name
    *
    * @param name  The new name
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the pair value
    *
    * @param value  The new value
    */
   public void setValue(String value)
   {
      this.value = value;
   }

   /**
    * toString implementation
    *
    * @return   The String representation
    */
   public String toString()
   {
      return name + ":" + value;//$NON-NLS-1$
   }
}

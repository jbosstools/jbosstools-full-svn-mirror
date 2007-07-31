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
package org.jboss.ide.eclipse.xdoclet.assist.model;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 */
public class DocletType
{
   private String name;

   /** Description of the Field */
   public final static DocletType COMMAND = new DocletType("command");//$NON-NLS-1$

   /** Description of the Field */
   public final static DocletType DISCRETE_ATTRIBUTE = new DocletType("discrete_attribute");//$NON-NLS-1$

   /** Description of the Field */
   public final static DocletType NAMESPACE = new DocletType("namespace");//$NON-NLS-1$

   /** Description of the Field */
   public final static DocletType NON_DISCRETE_ATTRIBUTE = new DocletType("nondiscrete_attribute");//$NON-NLS-1$

   /** Description of the Field */
   public final static DocletType ROOT = new DocletType("root");//$NON-NLS-1$

   /** Description of the Field */
   public final static DocletType VALUE_WITHOUT_VARIABLE = new DocletType("value_without_variable");//$NON-NLS-1$

   /** Description of the Field */
   public final static DocletType VALUE_WITH_VARIABLE = new DocletType("value_with_variable");//$NON-NLS-1$

   /**
    *Constructor for the DocletType object
    *
    * @param name  Description of the Parameter
    */
   private DocletType(String name)
   {
      this.name = name;
   }

   /**
    * Gets the name attribute of the DocletType object
    *
    * @return   The name value
    */
   public String getName()
   {
      return name;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getName();
   }

}

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
package org.jboss.ide.eclipse.jdt.ui.preferences;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Preference descriptor.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public final class PreferenceDescriptor
{
   /** Description of the Field */
   public final String key;

   /** Description of the Field */
   public final Type type;

   /** Description of the Field */
   public final static Type BOOLEAN = new Type();

   /** Description of the Field */
   public final static Type DOUBLE = new Type();

   /** Description of the Field */
   public final static Type FLOAT = new Type();

   /** Description of the Field */
   public final static Type INT = new Type();

   /** Description of the Field */
   public final static Type LONG = new Type();

   /** Description of the Field */
   public final static Type STRING = new Type();

   /**
    *Constructor for the PreferenceDescriptor object
    *
    * @param type  Description of the Parameter
    * @param key   Description of the Parameter
    */
   public PreferenceDescriptor(Type type, String key)
   {
      this.type = type;
      this.key = key;
   }

   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   public final static class Type
   {
      /**Constructor for the Type object */
      Type()
      {
      }
   }
}

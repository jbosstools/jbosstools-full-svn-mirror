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
package org.jboss.ide.eclipse.jdt.ui.wizards.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeHierarchyChangedListener;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Utility class for type hierarchies manipulation
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TypeHierarchyUtil implements ITypeHierarchyChangedListener
{
   private Map hierarchies = new HashMap();

   private static TypeHierarchyUtil instance = new TypeHierarchyUtil();

   /** Avoid instantiation */
   private TypeHierarchyUtil()
   {
   }

   /**
    * Gets the typeHierarchy attribute of the TypeHierarchyUtil object
    *
    * @param type                    Description of the Parameter
    * @return                        The typeHierarchy value
    * @exception JavaModelException  Description of the Exception
    */
   public ITypeHierarchy getTypeHierarchy(IType type) throws JavaModelException
   {
      if (type == null)
      {
         return null;
      }

      ITypeHierarchy hierarchy = null;
      synchronized (this.hierarchies)
      {
         hierarchy = (ITypeHierarchy) this.hierarchies.get(type);
         if (hierarchy == null)
         {
            hierarchy = type.newTypeHierarchy(new NullProgressMonitor());
            this.hierarchies.put(type, hierarchy);
            hierarchy.addTypeHierarchyChangedListener(this);
         }
      }
      return hierarchy;
   }

   /**
    * Description of the Method
    *
    * @param typeHierarchy  Description of the Parameter
    */
   public void typeHierarchyChanged(ITypeHierarchy typeHierarchy)
   {
      synchronized (this.hierarchies)
      {
         try
         {
            typeHierarchy.refresh(new NullProgressMonitor());
         }
         catch (JavaModelException jme)
         {
            // Do nothing
         }
      }
   }

   /**
    * Gets the instance attribute of the TypeHierarchyUtil class
    *
    * @return   The instance value
    */
   public static TypeHierarchyUtil getInstance()
   {
      return instance;
   }
}

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
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class IsClassOfType extends Condition
{
   private final String className;

   /**
    *Constructor for the IsClassOfType object
    *
    * @param className  Description of the Parameter
    */
   public IsClassOfType(String className)
   {
      this.className = className;
   }

   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   public boolean evalInternal(IMember member) throws JavaModelException
   {
      IType type = (new JavaElementAnalyzer(member)).getType();
      if (type != null)
      {
         if (type.getFullyQualifiedName().equals(className))
         {
            return true;
         }
         ITypeHierarchy hierarchy = type.newSupertypeHierarchy(new NullProgressMonitor());
         IType[] superTypes = hierarchy.getAllSupertypes(type);
         for (int i = 0; i < superTypes.length; i++)
         {
            if (superTypes[i].getFullyQualifiedName().equals(className))
            {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Returns the className.
    *
    * @return   String
    */
   public String getClassName()
   {
      return className;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName() + " : " + className;//$NON-NLS-1$
   }

}

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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 */
public class JavaElementAnalyzer
{

   IJavaElement javaElement;

   /**
    * Constructor for JavaElementAnalyzer.
    *
    * @param javaElement  Description of the Parameter
    */
   public JavaElementAnalyzer(IJavaElement javaElement)
   {
      super();
      setJavaElement(javaElement);
   }

   /**
    * Returns the javaElement.
    *
    * @return   IJavaElement
    */
   public IJavaElement getJavaElement()
   {
      return javaElement;
   }

   /**
    * @return   The package value
    * @see      xjavadoc.XProgramElement#getContainingPackage()
    */
   public IPackageFragment getPackage()
   {
      return getType().getPackageFragment();
   }

   /**
    * @return   The type value
    * @see      xjavadoc.XProgramElement#getContainingClass()
    */
   public IType getType()
   {
      if (javaElement instanceof IMethod)
      {
         return (IType) ((IMethod) javaElement).getParent();
      }
      else if (javaElement instanceof IType)
      {
         return (IType) javaElement;
      }
      else
      {
         return null;
      }
   }

   /**
    * Sets the javaElement.
    *
    * @param javaElement  The javaElement to set
    */
   public void setJavaElement(IJavaElement javaElement)
   {
      if (javaElement == null)
      {
         throw new IllegalArgumentException();
      }
      this.javaElement = javaElement;
   }

}

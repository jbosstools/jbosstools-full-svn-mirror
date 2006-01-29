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
package org.jboss.ide.eclipse.ui.util;

import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ProjectContentProvider implements ITreeContentProvider
{
   /**Constructor for the FragmentTreeContentProvider object */
   public ProjectContentProvider()
   {
      super();
   }

   /** Description of the Method */
   public void dispose()
   {
   }

   /**
    * Gets the children attribute of the FragmentTreeContentProvider object
    *
    * @param parentElement  Description of the Parameter
    * @return               The children value
    */
   public Object[] getChildren(Object parentElement)
   {
      try
      {
         if (parentElement instanceof IContainer)
         {
            IContainer container = (IContainer) parentElement;
            return container.members();
         }
      }
      catch (CoreException ce)
      {
      }
      return null;
   }

   /**
    * Gets the elements attribute of the FragmentTreeContentProvider object
    *
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    */
   public Object[] getElements(Object inputElement)
   {
      if (inputElement instanceof Collection)
      {
         return ((Collection) inputElement).toArray();
      }
      return null;
   }

   /**
    * Gets the parent attribute of the FragmentTreeContentProvider object
    *
    * @param element  Description of the Parameter
    * @return         The parent value
    */
   public Object getParent(Object element)
   {
      return null;
   }

   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    */
   public boolean hasChildren(Object element)
   {
      if (element instanceof IFile)
      {
         return false;
      }
      return true;
   }

   /**
    * Description of the Method
    *
    * @param viewer    Description of the Parameter
    * @param oldInput  Description of the Parameter
    * @param newInput  Description of the Parameter
    */
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
   }
}

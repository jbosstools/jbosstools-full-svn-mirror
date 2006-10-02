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
package org.jboss.ide.eclipse.xdoclet.run.util;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletData;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   20 mars 2003
 * @todo      Javadoc to complete
 */
public class ConfigurationContentProvider implements ITreeContentProvider
{
   /**Constructor for the ConfigurationContentProvider object */
   public ConfigurationContentProvider()
   {
      super();
   }

   /**
    *Description of the Method
    *
    * @see   org.eclipse.jface.viewers.IContentProvider#dispose()
    */
   public void dispose()
   {
   }

   /**
    * Gets the children attribute of the ConfigurationContentProvider object
    *
    * @param parentElement  Description of the Parameter
    * @return               The children value
    * @see                  org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
    */
   public Object[] getChildren(Object parentElement)
   {
      if (parentElement instanceof XDocletData)
      {
         return ((XDocletData) parentElement).getNodes().toArray();
      }
      return null;
   }

   /**
    * Gets the elements attribute of the ConfigurationContentProvider object
    *
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    * @see                 org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
    */
   public Object[] getElements(Object inputElement)
   {
      if (inputElement instanceof XDocletData)
      {
         return ((XDocletData) inputElement).getNodes().toArray();
      }
      return null;
   }

   /**
    * Gets the parent attribute of the ConfigurationContentProvider object
    *
    * @param element  Description of the Parameter
    * @return         The parent value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
    */
   public Object getParent(Object element)
   {
      if (element instanceof XDocletData)
      {
         return ((XDocletData) element).getParent();
      }
      return null;
   }

   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
    */
   public boolean hasChildren(Object element)
   {
      if (element instanceof XDocletData)
      {
         return !((XDocletData) element).isEmpty();
      }
      return false;
   }

   /**
    * Description of the Method
    *
    * @param viewer    Description of the Parameter
    * @param oldInput  Description of the Parameter
    * @param newInput  Description of the Parameter
    * @see             org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
    */
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
   }
}

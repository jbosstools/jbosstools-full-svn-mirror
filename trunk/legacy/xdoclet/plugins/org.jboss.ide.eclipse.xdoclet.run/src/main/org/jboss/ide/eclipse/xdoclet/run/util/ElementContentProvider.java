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

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletElement;

/**
 * @author    Laurent Etiemble
 * @version   $Revision: 1420 $
 * @created   20 mars 2003
 * @todo      Javadoc to complete
 */
public class ElementContentProvider implements IStructuredContentProvider
{
   /**Constructor for the ElementContentProvider object */
   public ElementContentProvider()
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
    * Gets the elements attribute of the ConfigurationContentProvider object
    *
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    * @see                 org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
    */
   public Object[] getElements(Object inputElement)
   {
      if (inputElement instanceof XDocletElement)
      {
         Collection attributes = ((XDocletElement) inputElement).getAttributes();
         return attributes.toArray();
      }
      return null;
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

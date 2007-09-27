/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.ui.util;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ListContentProvider
    implements IStructuredContentProvider
{
   /**Constructor for the ConfigurationListContentProvider object */
   public ListContentProvider()
   {
      super();
   }


   /**
    * Description of the Method
    *
    * @see   org.eclipse.jface.viewers.IContentProvider#dispose()
    */
   public void dispose() { }


   /**
    * Gets the elements attribute of the ConfigurationContentProvider object
    *
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    * @see                 org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
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
    * Description of the Method
    *
    * @param viewer    Description of the Parameter
    * @param oldInput  Description of the Parameter
    * @param newInput  Description of the Parameter
    */
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
}

/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.util;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletElement;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
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
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
}
